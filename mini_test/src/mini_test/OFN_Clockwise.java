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

public class OFN_Clockwise extends FeatureIm{
	
	public static void serialcomm(String num, long waittime){
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
		
			print(edit,"cd /usr/lib/leeonl/sensors");
			Thread.sleep(500);
			print(edit,"./set_ofn.sh");
			Thread.sleep(500);
			System.out.println("The OFN clockwise test is going to start:");
			print(edit,"./ofn_loop.sh "+waittime);
			Thread.sleep(waittime*2);
			print(edit,"sz /usr/lib/leeonl/sensors/ofn_loop.txt");
			Thread.sleep(4000);
			print(edit,"rm /usr/lib/leeonl/sensors/ofn_loop.txt");
			Thread.sleep(500);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
public static boolean fileprocess(String downloadpath, String filepath, String []paras){
		
		int errorcode=0;// print error code in the final output file;
		int errornum = 18;
		String result="";

		File downloadfile = new File(downloadpath+"\\ofn_loop.txt");
		File outputfile = new File(filepath);

		String line="";
		// the ofn scripts will return 2 value each time
		// to distinguish them, 2 arraylist are adopted
		ArrayList <String> content_first = new ArrayList<String>();
		ArrayList <String> content_last = new ArrayList<String>();

		try {
			
			
			BufferedReader br = new BufferedReader(new FileReader(downloadfile));
			while ((line=br.readLine())!=null){
				content_first.add(line);
				line=br.readLine();
				content_last.add(line);
			}
			br.close();
			
//			downloadfile.delete();

			if ((content_first.size()!=(Integer.parseInt(paras[0])/20))||(content_first.size()!=(Integer.parseInt(paras[0])/20))){
				throw new ofn_exception(1);
			}
			
			int value = 0;
			
			int count=0; // count the number of the first value which is more than 0
			for (int i=0;i<content_first.size();i++){
				value = Integer.valueOf(content_last.get(i).split("x")[1], 16);
				if (value!=0){
					throw new ofn_exception(2);
				}
				
				value = Integer.valueOf(content_first.get(i).split("x")[1], 16);
				
				if ((value>128)&&(count<5)){
					throw new ofn_exception(3);
				}
				else if (value!=0){
					count++;
				}
			}
			
			if (count>0){
				result = "PASS";
			}
			else {
				throw new ofn_exception(4);
			}

			

		} catch (IOException e) {
			e.printStackTrace();			
		}
			catch (NumberFormatException ex){
				result = "FAILED";
				errorcode = 0;
			}
		catch (ofn_exception ex){
			result = "FAILED";
			errorcode = ex.errorcode;
		}
		
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile,true));
			bw.write("OFN_Clockwise_Result="+result+"\r\n");
			System.out.println("OFN_Clockwise_Result="+result);
			if (result.equals("FAILED")){
				NumberFormat nf = NumberFormat.getIntegerInstance();
				nf.setMinimumIntegerDigits(2);
				bw.write("Error_Code= "+nf.format(errornum)+nf.format(errorcode)+"\r\n");
				System.out.println("Error_Code="+nf.format(errornum)+nf.format(errorcode));
			}
			bw.flush();
			bw.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return true;
	}

}
