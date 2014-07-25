package mini_test;

import java.io.*;
import java.text.NumberFormat;



import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.misc.basicStructures.LPARAM;
import org.xvolks.jnative.misc.basicStructures.UINT;
import org.xvolks.jnative.misc.basicStructures.WPARAM;
import org.xvolks.jnative.util.User32;

import features.FeatureIm;

public class LED_test extends FeatureIm{
	
	
	public static void serialcomm(String num, int para){
		
		String script=null;
		switch (para){
		case 0:
			script = "led_off.sh";
			break;
		case 1:
			script = "led_blue.sh";
			break;
		case 2:
			script = "led_green.sh";
			break;
		case 3:
			script = "led_red.sh";
			break;
		case 4:
			script = "led_white.sh";
			break;
		}
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
			
			print(edit,"cd /usr/lib/leeoalpha/sensors");
			Thread.sleep(500);
			print(edit,"./led_off.sh"); // firstly turn off all the LEDs.
			Thread.sleep(500);
			print(edit,"./"+script);
			Thread.sleep(500);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	public static void ledprocess(String filepath, String []paras, String result, String sn){
		
		String finalresult = "";
		int errorcode=0;// print error code in the final output file;
		int errornum = 2;
		File outputfile = new File(filepath);
		
		if ((result.equals("P"))||(result.equals("p"))){
			finalresult = "PASS";
		}
		else {
			finalresult = "FAILED";
		}
		
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile, true));// append the content to the end instead of overwriting the prior contents.			
			bw.write("LED_Result="+finalresult+"\r\n");
			System.out.println("LED_Result="+finalresult+"\r\n");
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