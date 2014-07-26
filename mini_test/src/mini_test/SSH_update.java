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


public class SSH_update extends FeatureIm{
	
	public static void featuretest(String filepath){
		
		String ip_address = "";
		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(new File(filepath+"\\ip_addr.txt")));
			ip_address = br.readLine();
			br.close();
			
			Process pscp = Runtime.getRuntime().exec("pscp -pw password C:\\FI\\workspace\\mini_test\\bsl_update.bin root@"+ip_address+":/usr/local/bin/bsl_update.bin");
			pscp.waitFor();
			Process putty = Runtime.getRuntime().exec("putty.exe -ssh -pw password -P 22 -m bsl_update_ssh.txt root@"+ip_address);
			putty.waitFor();
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void checkupdate(String num, String downloadpath, String filepath){
		
		String result = "";
		
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
		
			print(edit,"i2cget -y 1 0x4c 0x0E w > msp.txt");
			Thread.sleep(500);
			print(edit,"sz msp.txt");
			Thread.sleep(2000);
			print(edit,"rm -r msp.txt");
			Thread.sleep(500);
			
			File downloadfile = new File(downloadpath+"\\msp.txt");
			File outputfile  = new File(filepath);
			
			BufferedReader br = new BufferedReader(new FileReader(downloadfile));
			if (!(br.readLine().equals(""))){
				result = "PASS";
				System.out.println("Update Succeed!");
			}
			else{
				result = "FAILED";
				System.out.println("Updated Fail!");
			}
			br.close();
			downloadfile.delete();
			
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile,true));
			bw.write("Wifi_Result="+result);
			bw.flush();
			bw.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}

}
