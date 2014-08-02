package mini_test;

import java.io.*;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.misc.basicStructures.LPARAM;
import org.xvolks.jnative.misc.basicStructures.UINT;
import org.xvolks.jnative.misc.basicStructures.WPARAM;
import org.xvolks.jnative.util.User32;

import features.FeatureIm;

public class Humidity_test extends FeatureIm{
	
	
	
	public static void serialcomm(String num, String filepath){
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
		
			print(edit,"cd /usr/lib/leeonl/sensors");
			Thread.sleep(500);
			print(edit,"./get_humidity.rb > humid_test.txt");
			Thread.sleep(500);
			print(edit,"sz humid_test.txt");
			Thread.sleep(2000);
			print(edit,"rm -r humid_test.txt");
			Thread.sleep(500);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	// Process the file and generate a standardized file
	public static boolean fileprocess(String downloadpath, String filepath, String []paras, String sn){

		int errorcode=0;// print error code in the final output file;
		int errornum = 13;
		String result="";

		File downloadfile = new File(downloadpath+"\\humid_test.txt");
		File outputfile = new File(filepath);
		

		double value =0;
		String line="";
		ArrayList <String> content = new ArrayList<String>();

		try {
			
			
			BufferedReader br = new BufferedReader(new FileReader(downloadfile));
			while ((line=br.readLine())!=null){
				content.add(line);
			}
			br.close();
			
			downloadfile.delete();

			double lower_bound=Double.parseDouble(paras[0]);			
			double upper_bound=Double.parseDouble(paras[1]);	
			
			if (content.size()!=1){
				result = "FAILED";
				errorcode=3;
			}
			else{
				value=Double.parseDouble(content.get(0));
				
				if (value>=lower_bound){
					if (value<=upper_bound){
						result="PASS";
					}
					else {
						result = "FAILED";
						errorcode =2;
					}
				}
				else{
					result = "FAILED";
					errorcode=1;
				}
			}
					
			

			

		} catch (IOException e) {
			e.printStackTrace();			
		}
			catch (NumberFormatException ex){
				result = "FAILED";
				errorcode = 0;
			}
		
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile,true));
			bw.write("Humidity="+value+"\r\n");
			System.out.println("Humidity="+value);
			bw.write("Humidity_Result="+result+"\r\n");
			System.out.println("Humidity_Result="+result);
			if (result.equals("FAILED")){
				NumberFormat nf = NumberFormat.getIntegerInstance();
				nf.setMinimumIntegerDigits(2);
				bw.write("Error_Code= "+nf.format(errornum)+nf.format(errorcode)+"\r\n");
				System.out.println("Error_Code="+nf.format(errornum)+nf.format(errorcode));
			}
			bw.flush();
			bw.close();
		}catch (Exception e){
			e.printStackTrace();
		}

		return true;
	}

}
