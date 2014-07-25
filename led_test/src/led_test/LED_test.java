package led_test;

import java.io.*;
import java.text.NumberFormat;
import java.util.Scanner;

import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.misc.basicStructures.LPARAM;
import org.xvolks.jnative.misc.basicStructures.UINT;
import org.xvolks.jnative.misc.basicStructures.WPARAM;
import org.xvolks.jnative.util.User32;

import features.FeatureIm;

public class LED_test extends FeatureIm{
	public static void main(String []args){
		// Allocate the arguments: The first one is port number, the second one is the output file path, and the rest are parameters.
				String num=args[0];
				String filepath=args[1];
				
				String result = "";
				serialcomm_start(num);
				Scanner scanner = new Scanner(System.in);
				System.out.println("Please input the observation result: P=PASS, F=FAILED");
				result=scanner.nextLine();  
				scanner.close();
				serialcomm_end(num);
				ledprocess(filepath,result);
	}
	
	public static void serialcomm_start(String num){
	
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
			
			print(edit,"/usr/lib/leeoalpha/sensors/led_loop.sh &");
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	public static void serialcomm_end(String num){
		
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
			
			print(edit,"/usr/lib/leeoalpha/sensors/led_pid.sh");
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	public static void ledprocess(String filepath, String result){
		
		int errorcode=0;// print error code in the final output file;
		int errornum = 2;
		File outputfile = new File(filepath+"\\LED_log.bat");
		if (outputfile.exists()){
			outputfile.delete();
		}// Delete the existing file, which is result of the prior device;
		
		String finalresult="";
		if ((result.equals("P"))||(result.equals("p"))){
			finalresult = "PASS";
		}
		else {
			finalresult="FAILED";
		}
		try {
			System.out.println("Final Result="+finalresult);
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile, true));// append the content to the end instead of overwriting the prior contents.
			bw.write("set LED_Result="+finalresult+"\r\n");
			if (finalresult.equals("FAILED")){
				NumberFormat nf = NumberFormat.getIntegerInstance();
				nf.setMinimumIntegerDigits(2);
				bw.write("set Error_Code= "+nf.format(errornum)+nf.format(errorcode));
			}
			
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
}
