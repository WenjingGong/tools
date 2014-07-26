package ssh_update;

import java.io.*;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.misc.basicStructures.LPARAM;
import org.xvolks.jnative.misc.basicStructures.UINT;
import org.xvolks.jnative.misc.basicStructures.WPARAM;
import org.xvolks.jnative.util.User32;

import features.FeatureIm;

public class IP_Addr extends FeatureIm{
	
	public static void serialcomm(String num){
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
		
			print(edit,"ip addr > ip.txt");
			Thread.sleep(500);
			print(edit,"sz ip.txt");
			Thread.sleep(2000);
			print(edit,"rm -r ip.txt");
			Thread.sleep(500);
			print (edit,"cd /usr/local/bin");
			Thread.sleep(500);
			print(edit,"rz");
			Thread.sleep(15000);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	// Process the file and generate a standardized file
			public static String fileprocess(String downloadpath, String filepath) {

			//	int errornum=17;
				//int errorcode = 0;
				//String result = "";
				
				File downloadfile = new File(downloadpath+"\\ip.txt");
				File outputfile = new File(downloadpath+"\\ip_addr.txt");
				if (outputfile.exists()){
					outputfile.delete();
				}// Delete the existing file, which is result of the prior device;

				String ip_addr = "";
				String line="";
				ArrayList <String> content = new ArrayList<String>();

				try {
					
					
					BufferedReader br = new BufferedReader(new FileReader(downloadfile));
					while ((line=br.readLine())!=null){
						content.add(line);
					}
					br.close();
					
					downloadfile.delete();	
					
					line=content.get(content.size()-1);
					String []line4 = (line.split(" "));
					ip_addr = line4[5].split("/")[0];
					
					//result = "PASS";

					

				} catch (IOException e) {
					e.printStackTrace();			
				}
				

				return ip_addr;
			}



}
