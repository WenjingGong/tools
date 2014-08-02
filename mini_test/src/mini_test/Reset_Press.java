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

public class Reset_Press extends FeatureIm{
	
	public static void serialcomm(String num){
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
		
			print(edit,"cd /usr/lib/leeonl/sensors");
			Thread.sleep(500);
			print(edit,"./reset-button.sh > reset_detec_press.txt");
			Thread.sleep(500);
			System.out.println("Now you can release the reset button.");
			print(edit,"sz reset_detec_press.txt");
			Thread.sleep(2000);
			print(edit,"rm reset_detec_press.txt");
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static boolean fileprocess(String downloadpath, String filepath){
		int errorcode=0;// print error code in the final output file;
		int errornum = 15;
		String result="";

		File downloadfile1 = new File(downloadpath+"\\reset_detec_press.txt");
		File outputfile = new File(filepath);

		String line="";
		ArrayList <String> content = new ArrayList<String>();

		try {
			
			
			BufferedReader br1 = new BufferedReader(new FileReader(downloadfile1));
			line = br1.readLine();
			content.add(line);
			br1.close();				
			downloadfile1.delete();
			
			if (Integer.parseInt(content.get(0))!=1){
				result = "FAILED";
				errorcode = 1;
			}
			else {
				result = "PASS";
			}
			

		} catch (IOException e) {
			e.printStackTrace();			
		}
			catch (NumberFormatException ex){
				result = "FAILED";
				errorcode = 0;
			}
		
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile,true));
			bw.write("Reset_Press_Result="+result+"\r\n");
			System.out.println("Reset_Press_Result="+result);
			if (result.equals("FAILED")){
				NumberFormat nf = NumberFormat.getIntegerInstance();
				nf.setMinimumIntegerDigits(2);
				bw.write("Error_Code= "+nf.format(errornum)+nf.format(errorcode)+"\r\n");
			}
			bw.flush();
			bw.close();
		}catch (Exception e){
			e.printStackTrace();
		}

		return true;
	
	}

}
