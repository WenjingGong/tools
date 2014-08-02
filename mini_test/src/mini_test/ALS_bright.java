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

public class ALS_bright extends FeatureIm{
	
	public static void serialcomm(String num){
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
		
			System.out.println("Please keep the light on:");
			print(edit,"cd /usr/lib/leeonl/sensors");
			Thread.sleep(500);
			print(edit,"./get_light.rb");
			Thread.sleep(500);
			print(edit,"./get_light.rb > als_test.txt");
			Thread.sleep(500);
			print(edit,"sz als_test.txt");
			Thread.sleep(2000);
			print(edit,"rm -r als_test.txt");
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	// Process the file and generate a standardized file
			public static boolean fileprocess(String downloadpath, String filepath, String []paras){

				int errorcode=0;// print error code in the final output file;
				int errornum=10;
				String result="";

				File downloadfile = new File(downloadpath+"\\als_test.txt");
				File outputfile = new File(filepath);

				String line="";
				ArrayList <String> content_first = new ArrayList<String>();
				
				try {				
					
					BufferedReader br_first = new BufferedReader(new FileReader(downloadfile));
							
					while ((line=br_first.readLine())!=null){
						content_first.add(line);
					}
					br_first.close();
					downloadfile.delete();
					
					if (Integer.parseInt(content_first.get(0))<Integer.parseInt(paras[3])){
						throw new als_exception(2);
					}
					
					if (Integer.parseInt(content_first.get(0))>Integer.parseInt(paras[4])){
						throw new als_exception(3);
					}
					
					if (Integer.parseInt(content_first.get(1))<Integer.parseInt(paras[5])){
						throw new als_exception(4);
					}
					
					if (Integer.parseInt(content_first.get(1))>Integer.parseInt(paras[6])){
						throw new als_exception(5);
					}
					
					if (Double.parseDouble(content_first.get(2))<Double.parseDouble(paras[7])){
						throw new als_exception(6);
					}
					
					if (Double.parseDouble(content_first.get(2))>Double.parseDouble(paras[8])){
						throw new als_exception(7);
					}
					
					result = "PASS";
				
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
				catch (als_exception ex){
					result = "FAILED";
					errorcode = ex.errorcode;
				}
					
				
				
				try {
					BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile,true));
					bw.write("ALS_Light_Value="+Double.parseDouble(content_first.get(2))+"\r\n");
					System.out.println("ALS_Light_Value="+Double.parseDouble(content_first.get(2)));
					bw.write("ALS_Light_Result="+result+"\r\n");
					System.out.println("ALS_Light_Result="+result);
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
