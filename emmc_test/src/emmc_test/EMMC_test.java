package emmc_test;

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
	
public static void main(String []args){
		
		// Allocate the arguments: The first one is port number, the second one is the output file path, and the rest are parameters.
		String num=args[0];
		String filepath=args[1];
		String []paras=new String[args.length-2];
		int i;
		for (i=0;i<paras.length;i++){
			paras[i]=args[i+2];
		}		
		
		serialcomm(num);
		fileprocess(filepath+"\\downloadfile_com"+num,filepath,paras);		
	}

/*
 * The following function is to send command to zoc terminal.
 */
	public static void serialcomm(String num){
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
			
			print(edit,"cd /root");
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
	public static boolean fileprocess(String downloadpath, String filepath, String []paras){

		int errorcode=0;// print error code in the final output file;
		int errornum=1; // feature number
		String result="";
		double emmc_size=0;

		File downloadfile = new File(downloadpath+"\\emmc_test.txt");
		File outputfile = new File(filepath+"\\eMMc_log.bat");
		if (outputfile.exists()){
			outputfile.delete();
		}// Delete the existing file, which is result of the prior device;		

		try {
			String line="";
			ArrayList<String> content = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new FileReader(downloadfile));
			while ((line=br.readLine())!=null){
				content.add(line);
			}
			br.close();
			downloadfile.delete();
			
			String []values = content.get(1).split("\\s+"); // the second row contains specific values; The split sign is continuous blank space
			emmc_size=(Double.parseDouble(values[1]))/(1024*1024);
			
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
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile));
			bw.write("set EMMC="+(int)emmc_size*1024*1024+"\r\n");
			bw.write("set EMMC_Result="+result+"\r\n");
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
