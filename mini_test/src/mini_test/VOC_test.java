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

public class VOC_test extends FeatureIm{
		
	public static void serialcomm(String num, String filepath){
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
		
			print(edit,"cd /usr/lib/leeoalpha/sensors");
			Thread.sleep(500);
			print(edit,"./get_voc.rb > voc_test.txt");
			Thread.sleep(500);
			print(edit,"sz voc_test.txt");
			Thread.sleep(4000);
			print(edit,"rm -r voc_test.txt");
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	
	// Process the file and generate a standardized file
	public static boolean fileprocess(String downloadpath, String filepath, String []paras, String sn){

		int errorcode=0;// print error code in the final output file;
		int errornum = 7;
		String result="";

		File downloadfile = new File(downloadpath+"\\voc_test.txt");
		File outputfile = new File(filepath);
		
		int value = 0, ux = 0, rh = 0, pwm = 0, setrh = 0;
		String line="";
		ArrayList <String> content = new ArrayList<String>();

		try {		
			
			BufferedReader br = new BufferedReader(new FileReader(downloadfile));
			while ((line=br.readLine())!=null){
				content.add(line);
			}
			br.close();
			downloadfile.delete();
						
			int upper_bound=Integer.parseInt(paras[1]);
			int lower_bound=Integer.parseInt(paras[0]);
			
			value = Integer.parseInt(content.get(0));
			ux = Integer.parseInt(content.get(1));
			rh = Integer.parseInt(content.get(2));
			pwm = Integer.parseInt(content.get(3));
			setrh = Integer.parseInt(content.get(4));
					
			// judge the testing result. 
			// all the returned values should be within the defined range. otherwise, the result is "failed".
			if ((value>lower_bound)&&(value<upper_bound)){
				if ((ux>=68)&&(ux<=102)){
					if ((rh>=80)&&(rh<=170)){
						if ((pwm>=600)&&(pwm<=1400)){
							if ((setrh>=80)&&(setrh<=170)){
								result ="PASS";
							}
							else {
								result = "FAILED";
								errorcode = 6;
							}
						}
						else {
							result="FAILED";
							errorcode = 5;
						}
					}
					else {
						result = "FAILED";
						errorcode =4;
					}
				}
				else {
					result = "FAILED";
					errorcode =3;
				}
			}
			else{
				result = "FAILED";
				errorcode = 2;
				
			}
			

			

		} catch (IOException e) {
			e.printStackTrace();
		}
			catch (NumberFormatException e){
				result = "FAILED";
				errorcode=0;
			}
			catch (IndexOutOfBoundsException e){
				result="FAILED";
				errorcode=1;
			}
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile,true));
			bw.write("VOC="+value+"\r\n");
			System.out.println("VOC="+value);
			bw.write("VOC_Result="+result+"\r\n");
			System.out.println("VOC_Result="+result);
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
