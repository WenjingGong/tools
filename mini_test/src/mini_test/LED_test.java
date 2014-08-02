package mini_test;

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
	
	
	public static void led_branch(String num, String filepath, String sn, Scanner scanner){
		
		String line="", result = "";
		int item=0;
		System.out.println("Please choose the LED light you want to test: 1=white led, 2=rgb led, 0=return:");
		line=scanner.nextLine();
		while (!(line.equals("0"))){
			while ((!(line.equals("1")))&&(!(line.equals("2")))&&(!(line.equals("0")))){
				System.out.println("Error input! ");
				System.out.println("Please choose the LED light you want to test: 1=white led, 2=rgb led, 0=return:");
				line=scanner.nextLine();
			}
			if (line.equals("0")){
				break;
			}
			item = Integer.parseInt(line);
			switch(item){
			case 1:
				result = LED_White.serialcomm(num,scanner);
				LED_White.ledprocess(filepath, result, sn);
				break;
			case 2:
				result = LED_RGB.serialcomm(num,scanner);
				LED_RGB.ledprocess(filepath, result, sn);
				break;
			}
			System.out.println("Please choose the LED light you want to test: 1=white led, 2=rgb led, 0=return:");
			line=scanner.nextLine();
		}
			
	}
	
	/*public static void ledprocess(String filepath, String []paras, String result, String sn){
		
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
		
	}*/

}