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

public class Mac_Read extends FeatureIm{
	
	public static void serialcomm(String num, String filepath){
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
			
			print(edit,"sz /mnt/other/mac.txt");
			Thread.sleep(4000);		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Process the file and generate a standardized file
				public static boolean fileprocess(String downloadpath, String filepath, String sn){

					int errorcode=0;// print error code in the final output file;
					int errornum=6; // feature number
					String result="",mac="";

					
					File downloadfile_r = new File(downloadpath+"\\mac.txt");
					File outputfile = new File(filepath);	

					try {
						String line="";
						ArrayList<String> content_r = new ArrayList<String>();
						BufferedReader br_r = new BufferedReader(new FileReader(downloadfile_r));
						
						while ((line=br_r.readLine())!=null){
							content_r.add(line);
						}
						
						br_r.close();					
						downloadfile_r.delete();
						
						if (content_r.size()>=1){
							result = "PASS";
						}
						else {
							result = "FAILED";
							errorcode = 2;
						}
						
						if (!content_r.get(0).contains(":")){
							result = "FAILED";
							errorcode = 3;
						}
						else {
							String []addr = content_r.get(0).split(":");
							for (int i = 0; i<addr.length;i++){
								mac+=addr[i];
							}
						}						
						
					} catch (IOException e) {
						result = "FAILED";
						errorcode = 0;
					}
						catch (NumberFormatException ex){
							result="FAILED";
							errorcode=1;
						}
					
					
					try {
						BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile,true));
						bw.write("MAC_Address="+mac+"\r\n");
						bw.write("MAC_Read_Result="+result+"\r\n");
						System.out.println("MAC_Address="+mac);
						System.out.println("MAC_Read_Result="+result);
						if (result.equals("FAILED")){
							NumberFormat nf = NumberFormat.getIntegerInstance();
							nf.setMinimumIntegerDigits(2);
							bw.write("Error_Code= "+nf.format(errornum)+nf.format(errorcode));
							System.out.println("Error_Code= "+nf.format(errornum)+nf.format(errorcode));
						}
						bw.flush();
						bw.close();
					}catch (Exception e){
						e.printStackTrace();
					}
					return true;
				}

}
