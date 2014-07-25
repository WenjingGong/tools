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

public class Reset_key extends FeatureIm{
	
	
	
	public static void serialcomm(String num, long waittime, String filepath){
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
		
			print(edit,"cd /usr/lib/leeoalpha/sensors");
			Thread.sleep(500);
			print(edit,"./reset-button.sh > reset_detec1.txt");
			Thread.sleep(500);
			print(edit,"sz reset_detec1.txt");
			Thread.sleep(4000);
			System.out.println("Please press the reset button");
			Thread.sleep(waittime);
			print(edit,"./reset-button.sh > reset_detec2.txt");
			Thread.sleep(500);
			print(edit,"sz reset_detec2.txt");
			Thread.sleep(4000);
			System.out.println("Please release the reset button");
			Thread.sleep(waittime);
			print(edit,"./reset-button.sh > reset_detec3.txt");
			Thread.sleep(500);
			print(edit,"sz reset_detec3.txt");
			Thread.sleep(4000);
			print(edit,"rm -r humid_test.txt");
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	// Process the file and generate a standardized file
		public static boolean fileprocess(String downloadpath, String filepath, String []paras, String sn){

			int errorcode=0;// print error code in the final output file;
			int errornum = 10;
			String result="";

			File downloadfile1 = new File(downloadpath+"\\reset_detec1.txt");
			File downloadfile2 = new File(downloadpath+"\\reset_detec2.txt");
			File downloadfile3 = new File(downloadpath+"\\reset_detec3.txt");
			File outputfile = new File(filepath);	
			


			String line="";
			ArrayList <String> content = new ArrayList<String>();

			try {
				
				
				BufferedReader br1 = new BufferedReader(new FileReader(downloadfile1));
				line = br1.readLine();
				br1.close();
				if (line.equals("")){
					throw new IndexOutOfBoundsException();
				}
				else {
					content.add(line);
				}								
				downloadfile1.delete();
				
				BufferedReader br2 = new BufferedReader(new FileReader(downloadfile2));
				line = br2.readLine();
				br2.close();
				if (line.equals("")){
					throw new IndexOutOfBoundsException();
				}
				else {
					content.add(line);
				}									
				downloadfile2.delete();
				
				BufferedReader br3 = new BufferedReader(new FileReader(downloadfile3));
				line = br3.readLine();
				br3.close();
				if (line.equals("")){
					throw new IndexOutOfBoundsException();
				}
				else {
					content.add(line);
				}					
				downloadfile3.delete();
				
				

				if (Integer.parseInt(content.get(0))==0){
					if (Integer.parseInt(content.get(1))==1){
						if (Integer.parseInt(content.get(2))==0){
							result = "PASS";
						}
						else {
							result = "FAILED";
							errorcode = 4;
						}
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
				
				

				

			} catch (IOException e) {
				e.printStackTrace();			
			}
				catch (NumberFormatException ex){
					result = "FAILED";
					errorcode = 0;
				}
			catch (IndexOutOfBoundsException e){
				result="FAILED";
				errorcode=1;
			}
			
			
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile,true));
				bw.write("Reset_Result="+result+"\r\n");
				System.out.println("Reset_Result="+result);
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
