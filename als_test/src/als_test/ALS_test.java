package als_test;

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

public class ALS_test extends FeatureIm{
	
public static void main(String []args){
		
		// Allocate the arguments: The first one is port number, the second one is the output file path, and the rest are parameters.
		String num=args[0];
		String filepath=args[1];
		String waittime = args[2];
		
		serialcomm(num,Long.parseLong(waittime));
		fileprocess(filepath+"\\downloadfile_com"+num,filepath);		
	}
	
	public static void serialcomm(String num, long waittime){
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
		
			print(edit,"cd /usr/lib/leeoalpha/sensors");
			Thread.sleep(500);
			System.out.println("Please block the light until you see the next instruction:");
			Thread.sleep(waittime);
			print(edit,"./get_light.rb > als_test_first.txt");
			Thread.sleep(500);
			print(edit,"sz als_test_first.txt");
			Thread.sleep(2000);
			System.out.println("Please remove the light blocking");
			Thread.sleep(waittime);
			print(edit,"./get_light.rb > als_test_last.txt");
			Thread.sleep(500);
			print(edit,"sz als_test_last.txt");
			Thread.sleep(2000);
			print(edit,"rm -r als_test_first.txt als_test_last.txt");
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	// Process the file and generate a standardized file
		public static boolean fileprocess(String downloadpath, String filepath){

			int errorcode=0;// print error code in the final output file;
			int errornum=4;
			double light_first=0, light_last=0;
			String result="";

			File downloadfile_first = new File(downloadpath+"\\als_test_first.txt");
			File downloadfile_last = new File(downloadpath+"\\als_test_last.txt");
			File parafile = new File("als_para.txt");
			File outputfile = new File(filepath+"\\ALS_log.bat");
			if (outputfile.exists()){
				outputfile.delete();
			}// Delete the existing file, which is result of the prior device;

			String line="";
			ArrayList <String> content_first = new ArrayList<String>();
			ArrayList <String> content_last = new ArrayList<String>();
			String []paras = new String[9];
			
			try {				
				
				BufferedReader br_first = new BufferedReader(new FileReader(downloadfile_first));
						
				while ((line=br_first.readLine())!=null){
					content_first.add(line);
				}
				br_first.close();
				downloadfile_first.delete();
				
				BufferedReader br_last = new BufferedReader(new FileReader(downloadfile_last));
				
				while ((line=br_last.readLine())!=null){
					content_last.add(line);
				}
				br_last.close();
				downloadfile_last.delete();
				
				BufferedReader br_para = new BufferedReader(new FileReader(parafile));
				int i=0;
				while ((line=br_para.readLine())!=null){
					paras[i]=line;
					i++;
				}
				br_para.close();
				
				light_first = Double.parseDouble(content_first.get(2));
				light_last = Double.parseDouble(content_last.get(2));
				
				if (Integer.parseInt(content_first.get(0))>Integer.parseInt(paras[0])){
					throw new als_exception(2);
				}
				
				if (Integer.parseInt(content_first.get(1))>Integer.parseInt(paras[3])){
					throw new als_exception(3);
				}
				
				if (light_first>Double.parseDouble(paras[6])){
					throw new als_exception(4);
				}
				
				if ((Integer.parseInt(content_last.get(0))<Integer.parseInt(paras[1]))||(Integer.parseInt(content_last.get(0))>Integer.parseInt(paras[2]))){
					throw new als_exception(5);
				}
				
				if ((Integer.parseInt(content_last.get(1))<Integer.parseInt(paras[4]))||(Integer.parseInt(content_last.get(0))>Integer.parseInt(paras[5]))){
					throw new als_exception(6);
				}
				
				if ((light_last<Double.parseDouble(paras[7]))||(light_last>Double.parseDouble(paras[8]))){
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
					errorcode = ex.returncode();
				}
			
			
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile));
				bw.write("set ALS_Darkness="+light_first+"\r\n");
				bw.write("set ALS_Brightness="+light_last+"\r\n");
				bw.write("set ALS_Result="+result+"\r\n");
				if (result.equals("FAILED")){
					NumberFormat nf = NumberFormat.getIntegerInstance();
					nf.setMinimumIntegerDigits(2);
					bw.write("set Error_Code= "+nf.format(errornum)+nf.format(errorcode));
				}
				bw.flush();
				bw.close();
			}catch (Exception e){
				e.printStackTrace();
			}

			return true;
		}


}
