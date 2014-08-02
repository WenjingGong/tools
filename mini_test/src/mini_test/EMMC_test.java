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

public class EMMC_test extends FeatureIm{
	


/*
 * The following function is to send command to zoc terminal.
 */
	public static void serialcomm(String num, String filepath){
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
			
			print(edit,"df > emmc_test.txt");
			Thread.sleep(500);
			print(edit,"sz emmc_test.txt");
			Thread.sleep(2000);
			print(edit,"rm -r emmc_test.txt");
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}


// Process the file and generate a standardized file
	public static boolean fileprocess(String downloadpath, String filepath, String []paras, String sn){

		int errorcode=0;// print error code in the final output file;
		int errornum=6; // feature number
		String result="";
		double emmc_size=0;
		double size = 0;

		File downloadfile = new File(downloadpath+"\\emmc_test.txt");
		File outputfile = new File(filepath);	
				

		try {
			String line="";
			ArrayList<String> content = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new FileReader(downloadfile));
			while ((line=br.readLine())!=null){
				content.add(line);
			}
			br.close();
			downloadfile.delete();
			
			for (int i=1;i<content.size();i++){
				String []values = content.get(i).split("\\s+"); // the second row contains specific values; The split sign is continuous blank space
				size+=Integer.parseInt(values[1]);
			}
			
			
			emmc_size=size/(1024*1024);
			
			if ((emmc_size>Double.parseDouble(paras[0])&&(emmc_size<Double.parseDouble(paras[1])))){
				result="PASS";
			}
			else if (emmc_size<Double.parseDouble(paras[0])){
				result = "FAILED";
				errorcode=1;
			}
			else {
				result = "FAILED";
				errorcode=2;
			}

			

		} catch (IOException e) {
			e.printStackTrace();
		}
			catch (NumberFormatException ex){
				result="FAILED";
				errorcode=0;
			}
		
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile,true));
			bw.write("EMMC="+size+" KB\r\n");
			System.out.println("EMMC="+size+" KB");
			bw.write("EMMC_Result="+result+"\r\n");
			System.out.println("EMMC_Result="+result);
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

}
