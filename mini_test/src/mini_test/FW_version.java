package mini_test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.misc.basicStructures.LPARAM;
import org.xvolks.jnative.misc.basicStructures.UINT;
import org.xvolks.jnative.misc.basicStructures.WPARAM;
import org.xvolks.jnative.util.User32;

import features.FeatureIm;

public class FW_version extends FeatureIm{
	
	public static void serialcomm(String num){
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
		
			print(edit,"cd /usr/lib/leeonl/sensors");
			Thread.sleep(500);
			print(edit,"i2cget -y 1 0x4c 0x0e w > mcu_version.txt");
			Thread.sleep(500);
			print(edit,"sz mcu_version.txt");
			Thread.sleep(2000);
			print(edit,"rm mcu_version.txt");
			Thread.sleep(500);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static boolean fileprocess(String downloadpath, String filepath, String []paras, String sn){
		int errorcode=0;// print error code in the final output file;
		int errornum = 4;
		String result="";

		File downloadfile = new File(downloadpath+"\\mcu_version.txt");
		File outputfile = new File(filepath);
		

		double value =0;
		String line="";
		ArrayList<String> contents = new ArrayList<String>();

		try {
			
			
			BufferedReader br = new BufferedReader(new FileReader(downloadfile));
			while ((line=br.readLine())!=null){
				contents.add(line);
			}
			br.close();
			downloadfile.delete();
			
			if (contents.size()>1){
				result = "FAILED";
				errorcode = 3;
			}
			
			else if (contents.get(0).equals("0xffff")){
				result = "FAILED";
				errorcode = 4;
			}
			else {
				line=contents.get(0).substring(2);
				line=""+line.charAt(0)+line.charAt(1)+"."+line.charAt(2)+line.charAt(3);
				value = Double.parseDouble(line);
				
				if (Double.parseDouble(paras[0])==0){
					result = "PASS";
				}
				else {
					Double version_value = Double.parseDouble(paras[0]);
					if (value==version_value){
						result = "PASS";
					}
					else {
						result = "FAILED";
						errorcode = 2;
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();			
		}
			catch (NumberFormatException ex){
				result = "FAILED";
				errorcode = 0;
			}
		catch (IndexOutOfBoundsException ex){
			result ="FAILED";
			errorcode = 1;
		}
		
		
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile,true));
			bw.write("FW_Version="+value+"\r\n");
			System.out.println("FW_Version="+value);
			bw.write("FW_Version_Result="+result+"\r\n");
			System.out.println("FW_Version_Result="+result);
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
