package temperature_test;

import java.io.*;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.misc.basicStructures.LPARAM;
import org.xvolks.jnative.misc.basicStructures.UINT;
import org.xvolks.jnative.misc.basicStructures.WPARAM;
import org.xvolks.jnative.util.User32;

import features.FeatureIm;

public class Temperature_teest extends FeatureIm{
	public static void main(String []args){
		// Allocate the arguments: The first one is port number, the second one is the output file path, and the rest are parameters.
		String num=args[0];
		String filepath=args[1];
		String []paras=new String[args.length-2];
		int i;
		for (i=0;i<paras.length;i++){
			paras[i]=args[i+2];
		}		
			
		serialcomm(num);
		fileprocess(filepath+"\\downloadfile_com"+num,filepath,paras);	
	}
	
	public static void serialcomm(String num){
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
		
			print(edit,"cd /usr/lib/leeoalpha/sensors");
			Thread.sleep(500);
			print(edit,"./get_temperature.rb > temp_test.txt");
			Thread.sleep(500);
			print(edit,"sz temp_test.txt");
			Thread.sleep(2000);
			print(edit,"rm -r temp_test.txt");
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	// Process the file and generate a standardized file
	public static boolean fileprocess(String downloadpath, String filepath, String []paras){

		int errorcode=0;// print error code in the final output file;
		int errornum = 9;
		String result="";

		File downloadfile = new File(downloadpath+"\\temp_test.txt");
		File outputfile = new File(filepath+"\\Temperature_log.bat");
		if (outputfile.exists()){
			outputfile.delete();
		}// Delete the existing file, which is result of the prior device;

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

			

		} catch (IOException e) {
			e.printStackTrace();			
		}
			catch (NumberFormatException ex){
				result = "FAILED";
				errorcode = 0;
			}
		
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile));
			bw.write("set Temperature="+value+"\r\n");
			bw.write("set Temperature_Result="+result+"\r\n");
			if (result.equals("FAILED")){
				NumberFormat nf = NumberFormat.getIntegerInstance();
				nf.setMinimumIntegerDigits(2);
				bw.write("set Error_Code= "+nf.format(errornum)+nf.format(errorcode));
			}
			bw.flush();
			bw.close();
		}catch (Exception e){
			e.printStackTrace();
		}


		return true;
	}

}
