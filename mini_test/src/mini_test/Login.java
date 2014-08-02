package mini_test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.misc.basicStructures.LPARAM;
import org.xvolks.jnative.misc.basicStructures.UINT;
import org.xvolks.jnative.misc.basicStructures.WPARAM;
import org.xvolks.jnative.util.User32;

import features.FeatureIm;

public class Login extends FeatureIm{
	
	public static void serialcomm(String num){
		
		
		try{
			HWND targetwindow=User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			print(edit,"root");// input "root" into the terminal area
			Thread.sleep(500);
			print(edit,"password");
			Thread.sleep(500);
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
			Thread.sleep(200);//wait for response, the time may change
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
			Thread.sleep(200);//wait for response, the time may change
			print(edit,"root");// input "root" into the terminal area
			Thread.sleep(500);
			print(edit,"password");
			Thread.sleep(500);
		}catch (Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	public static void checkloginstatus(String num, String downloadpath){
		try{
			HWND targetwindow=User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
			Thread.sleep(200);//wait for response, the time may change
			print(edit,"/usr/lib/leeonl/sensors/led_off.sh");
			Thread.sleep(500);
			print(edit,"/usr/lib/leeonl/sensors/led_green.sh");
			Thread.sleep(500);
			print(edit,"sz /usr/lib/leeonl/sensors/reset_detec1.txt");
			Thread.sleep(2000);
			
		}catch (Exception ex){
			ex.printStackTrace();
		}
		
		File outputfile = new File(downloadpath+"\\login_status.txt");
		File downloadfile = new File(downloadpath+"\\reset_detec1.txt");
		if (downloadfile.exists()){
			System.out.println("Log in Succeed!");
			downloadfile.delete();
			if (outputfile.exists()){
				outputfile.delete();
			}
		}
		else {
			if (!outputfile.exists()){
				try {
					outputfile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			ArrayList<String> content = new ArrayList<String>();
			
			try{
				BufferedReader br = new BufferedReader(new FileReader(outputfile));
				String line="";
				while((line=br.readLine())!=null){
					content.add(line);
				}
				br.close();
				
				if (content.size()<2){
					BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile,true));
					bw.write("1\r\n");
					bw.flush();
					bw.close();
					System.out.println("Log in Failed. Try again!");
				}
				else {
					System.out.println("Log in Fail for 3 times!");
				}
				
			}catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	

}
