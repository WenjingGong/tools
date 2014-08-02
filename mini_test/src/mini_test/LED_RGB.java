package mini_test;

import java.io.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Scanner;

import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.misc.basicStructures.LPARAM;
import org.xvolks.jnative.misc.basicStructures.UINT;
import org.xvolks.jnative.misc.basicStructures.WPARAM;
import org.xvolks.jnative.util.User32;

import features.FeatureIm;

public class LED_RGB extends FeatureIm{
	
public static String serialcomm(String num, Scanner scanner){
		
		String result="";
		
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
			
			print(edit,"/usr/lib/leeonl/sensors/led_loop.sh &");
			Thread.sleep(500);
			System.out.println("Please enter the result of RGB LED lights: p=pass, f=failed:");
			result=scanner.nextLine();
			
			print(edit,"/usr/lib/leeonl/sensors/led_pid.sh");
			Thread.sleep(500);
			print(edit,"/usr/lib/leeonl/sensors/led_off.sh");
			Thread.sleep(500);
			print(edit,"/usr/lib/leeonl/sensors/led_green.sh");
			Thread.sleep(500);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

public static void ledprocess(String filepath, String result, String sn){
	
	String finalresult = "";
	int errorcode=0;// print error code in the final output file;
	int errornum = 8;
	File outputfile = new File(filepath);

	if ((result.equals("P"))||(result.equals("p"))||(result.equals("pass"))||(result.equals("PASS"))){
		finalresult = "PASS";
	}
	else {
		finalresult="FAILED";
		errorcode = 0;
	}
	
	
	try {
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile, true));// append the content to the end instead of overwriting the prior contents.			
		bw.write("LED_RGB_Result="+finalresult+"\r\n");
		System.out.println("LED_RGB_Result="+finalresult);
		if (finalresult.equals("FAILED")){
			NumberFormat nf = NumberFormat.getIntegerInstance();
			nf.setMinimumIntegerDigits(2);
			bw.write("Error_Code="+nf.format(errornum)+nf.format(errorcode)+"\r\n");
			System.out.println("Error_Code="+nf.format(errornum)+nf.format(errorcode));
		}
		
		bw.flush();
		bw.close();
	} catch (IOException e) {
		e.printStackTrace();
	}
	
}

}
