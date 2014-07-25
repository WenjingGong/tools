package reset_detection;

import java.io.*;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.misc.basicStructures.LPARAM;
import org.xvolks.jnative.misc.basicStructures.UINT;
import org.xvolks.jnative.misc.basicStructures.WPARAM;
import org.xvolks.jnative.util.User32;

import features.FeatureIm;

public class Reset_detection extends FeatureIm{
	
	public static void main(String []args){
		// Allocate the arguments: The first one is port number, the second one is the output file path, and the rest are parameters.
				String num=args[0];
				String filepath=args[1];
				String []paras=new String[args.length-2];
				int i;
				for (i=0;i<paras.length;i++){
					paras[i]=args[i+2];
				}		
					
				serialcomm(num,Long.parseLong(paras[0]));
				fileprocess(filepath+"\\downloadfile_com"+num,filepath,paras);	
	}
	
	public static void serialcomm(String num, long waittime){
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
		
			print(edit,"cd /usr/lib/leeoalpha/sensors");
			Thread.sleep(500);
			print(edit,"./reset-button.sh > reset_detec1.txt");
			Thread.sleep(500);
			print(edit,"sz reset_detec1.txt");
			Thread.sleep(2000);
			System.out.println("Please press the reset button");
			Thread.sleep(waittime);
			print(edit,"./reset-button.sh > reset_detec2.txt");
			Thread.sleep(500);
			print(edit,"sz reset_detec2.txt");
			Thread.sleep(2000);
			System.out.println("Please release the reset button");
			Thread.sleep(waittime);
			print(edit,"./reset-button.sh > reset_detec3.txt");
			Thread.sleep(500);
			print(edit,"sz reset_detec3.txt");
			Thread.sleep(2000);
			print(edit,"rm -r reset_detec1_test.txt reset_detec2_test.txt reset_detec3_test.txt");
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	// Process the file and generate a standardized file
		public static boolean fileprocess(String downloadpath, String filepath, String []paras){

			int errorcode=0;// print error code in the final output file;
			int errornum = 10;
			String result="";

			File downloadfile1 = new File(downloadpath+"\\reset_detec1.txt");
			File downloadfile2 = new File(downloadpath+"\\reset_detec2.txt");
			File downloadfile3 = new File(downloadpath+"\\reset_detec3.txt");
			File outputfile = new File(filepath+"\\Reset_Detection_log.bat");
			if (outputfile.exists()){
				outputfile.delete();
			}// Delete the existing file, which is result of the prior device;


			String line="";
			ArrayList <String> content = new ArrayList<String>();

			try {
				
				
				BufferedReader br1 = new BufferedReader(new FileReader(downloadfile1));
				line = br1.readLine();
				content.add(line);
				br1.close();				
				downloadfile1.delete();
				
				BufferedReader br2 = new BufferedReader(new FileReader(downloadfile2));
				line = br2.readLine();
				content.add(line);
				br2.close();				
				downloadfile2.delete();
				
				BufferedReader br3 = new BufferedReader(new FileReader(downloadfile3));
				line = br3.readLine();
				content.add(line);
				br3.close();				
				downloadfile3.delete();
				
				

				if (Integer.parseInt(content.get(0))==0){
					if (Integer.parseInt(content.get(1))==1){
						if (Integer.parseInt(content.get(2))==0){
							result = "PASS";
						}
						else {
							result = "FAILED";
							errorcode = 3;
						}
					}
					else {
						result = "FAILED";
						errorcode = 2;
					}
				}
				else {
					result = "FAILED";
					errorcode = 1;
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
				bw.write("set Reset_Result="+result+"\r\n");
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
