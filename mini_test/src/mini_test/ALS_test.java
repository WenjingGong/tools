package mini_test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Scanner;

import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.misc.basicStructures.LPARAM;
import org.xvolks.jnative.misc.basicStructures.UINT;
import org.xvolks.jnative.misc.basicStructures.WPARAM;
import org.xvolks.jnative.util.User32;

import features.FeatureIm;

public class ALS_test extends FeatureIm{
	
	
	public static void ALS_branch(String num, String filepath, String downloadpath, String []paras, Scanner scanner){
		String line="", result = "";
		int item=0;
		System.out.println("Please choose the light status you want to test: 1=dark, 2=bright, 0=return:");
		System.out.println("If you choose to test the dark status, please keep blocking the light before you press 1 until you see the instruction.");
		line=scanner.nextLine();
		while(!(line.equals("0"))){
			while ((!(line.equals("1")))&&(!(line.equals("2")))&&(!(line.equals("0")))){
				System.out.println("Error input! ");
				System.out.println("Please choose the light status you want to test: 1=dark, 2=bright, 0=return:");
				line=scanner.nextLine();
			}
			if (line.equals("0")){
				break;
			}
			item = Integer.parseInt(line);
			switch(item){
			case 1:
				ALS_dark.serialcomm(num);
				ALS_dark.fileprocess(downloadpath, filepath, paras);
				break;
			case 2:
				ALS_bright.serialcomm(num);
				ALS_bright.fileprocess(downloadpath, filepath, paras);
				break;
			}
			System.out.println("Please choose the light status you want to test: 1=dark, 2=bright, 0=return:");
			line=scanner.nextLine();
		}
	}
	
	/*public static void serialcomm(String num, long waittime, String filepath){
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
		
			print(edit,"cd /usr/lib/leeoalpha/sensors");
			Thread.sleep(500);
			System.out.println("Please execute ALS test actions");
			Thread.sleep(waittime);
			print(edit,"./get_light.rb > als_test.txt");
			Thread.sleep(500);
			print(edit,"sz als_test.txt");
			Thread.sleep(4000);
			print(edit,"rm -r als_test.txt");
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	// Process the file and generate a standardized file
		public static boolean fileprocess(String downloadpath, String filepath, String []paras, String sn){

			int errorcode=0;// print error code in the final output file;
			int errornum=4;
			int ch0=0,ch1=0;
			double light=0;
			String result="";

			File downloadfile = new File(downloadpath+"\\als_test.txt");
			File outputfile = new File(filepath);
			

			String line_first="";
			ArrayList <String> content = new ArrayList<String>();
			
			try {				
				
				BufferedReader br = new BufferedReader(new FileReader(downloadfile));
						
				while ((line_first=br.readLine())!=null){
					content.add(line_first);
				}
				br.close();
				downloadfile.delete();
				
				double range0_lb = Double.parseDouble(paras[1]);
				double range0_ub = Double.parseDouble(paras[2]);
				double range1_lb = Double.parseDouble(paras[3]);
				double range1_ub = Double.parseDouble(paras[4]);
				double range2_lb = Double.parseDouble(paras[5]);
				double range2_ub = Double.parseDouble(paras[6]);
				
				ch0 = Integer.parseInt(content.get(0));
				ch1 = Integer.parseInt(content.get(1));
				light = Double.parseDouble(content.get(2));
				
				if ((ch0>=range0_lb)&&(ch0<=range0_ub)){
					if ((ch1>=range1_lb)&&(ch1<=range1_ub)){
						if ((light>=range2_lb)&&(light<=range2_ub)){
							result = "PASS";
						}
						else{
							result = "FAILED";
							errorcode = 4;
						}
					}
					else {
						result = "FAILED";
						errorcode = 3;
					}
				}
				else{
					result = "FAILED";
					errorcode = 2;
				}
				
				


			}  catch (IOException e) {
				e.printStackTrace();
			}
				catch (NumberFormatException e){
					result = "FAILED";
					errorcode=0;
				}
				catch (IndexOutOfBoundsException e){
					result="FAILED";
					errorcode=1;
				}
			
			
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile,true));
				bw.write("ALS_CH0="+(int)ch0+"\r\n");
				System.out.println("ALS_CH0="+(int)ch0);
				bw.write("ALS_CH1="+(int)ch1+"\r\n");
				System.out.println("ALS_CH1="+(int)ch1);
				bw.write("ALS_light="+light+"\r\n");
				System.out.println("ALS_light="+light);
				bw.write("ALS_Result="+result+"\r\n");
				System.out.println("ALS_Result="+result);
				if (result.equals("FAILED")){
					NumberFormat nf = NumberFormat.getIntegerInstance();
					nf.setMinimumIntegerDigits(2);
					bw.write("Error_Code= "+nf.format(errornum)+nf.format(errorcode)+"\r\n");
					System.out.println("Error_Code= "+nf.format(errornum)+nf.format(errorcode));
				}
				bw.flush();
				bw.close();
			}catch (Exception e){
				e.printStackTrace();
			}

			return true;
		}
*/

}
