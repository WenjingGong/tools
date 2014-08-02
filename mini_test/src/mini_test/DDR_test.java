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

public class DDR_test extends FeatureIm{
	
	
	// Execute specific command, and seize the output result back.
	public static void serialcomm(String num, String filepath){
				try {
					HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
					HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
					User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
					
					print(edit,"free > ddr_test.txt");
					Thread.sleep(500);
					print(edit,"sz ddr_test.txt");
					Thread.sleep(2000);
					print(edit,"rm -r ddr_test.txt");
					Thread.sleep(500);
				} catch (Exception e) {
					e.printStackTrace();
				} 
	}
	
	
	// Process the file and generate a standardized file
	public static boolean fileprocess(String downloadpath, String filepath, String []paras, String sn){
		
		int errorcode=0;// print error code in the final output file;
		int errornum=5;
		String result="";
		
		File downloadfile = new File(downloadpath+"\\ddr_test.txt");
		File outputfile = new File(filepath);	
		
		
		long memsize_total = 0;
		
		try {
			String line="";
			ArrayList<String> content = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new FileReader(downloadfile));
			while ((line=br.readLine())!=null){
				content.add(line);
			}
			br.close();
			
			String []values = content.get(1).split("\\s+"); // the second row contains specific values; The split sign is continuous blank space
			memsize_total = Integer.parseInt(values[1]); // the second element of the current row is total memory size.
			
			
			if (((memsize_total/1024)>Integer.parseInt(paras[0]))&&((memsize_total/1024)<Integer.parseInt(paras[1]))){
				result="PASS";
			}
			else if ((memsize_total/1024)<Integer.parseInt(paras[0])){
				result="FAILED";
				errorcode=1;
			}			
			else {
				result = "PASS";
				errorcode=2;
			}
			
			downloadfile.delete();
		
		} catch (IOException e) {
			e.printStackTrace();			
		}
			catch (NumberFormatException ex){
				result = "FAILED";
				errorcode=0;
			}
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile,true));
			bw.write("DDR="+memsize_total+" KB\r\n");
			System.out.println("DDR="+memsize_total+" KB");
			bw.write("DDR_Result="+result+"\r\n");
			System.out.println("DDR_Result="+result);
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
