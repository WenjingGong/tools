package mini_test;

import java.io.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Scanner;

import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.misc.basicStructures.LPARAM;
import org.xvolks.jnative.misc.basicStructures.UINT;
import org.xvolks.jnative.misc.basicStructures.WPARAM;
import org.xvolks.jnative.util.User32;

import features.FeatureIm;

public class OFN_test extends FeatureIm{
	
	public static void ofn_branch(String num, long waittime, String filepath, String downloadpath, String []paras, Scanner scanner){
		String line="", result = "";
		int item=0;
		System.out.println("Please choose the OFN rotate direction you want to test: 1=clockwise, 2=counterclockwise, 0=return:");
		line=scanner.nextLine();
		
		while(!(line.equals("0"))){
			while ((!(line.equals("1")))&&(!(line.equals("2")))&&(!(line.equals("0")))){
				System.out.println("Error input! ");
				System.out.println("Please choose the OFN rotate direction you want to test: 1=clockwise, 2=counterclockwise, 0=return:");
				line=scanner.nextLine();
			}
			
			if (line.equals("0")){
				break;
			}
			item = Integer.parseInt(line);
			
			switch(item){
			case 1:
				OFN_Clockwise.serialcomm(num,waittime);
				OFN_Clockwise.fileprocess(downloadpath, filepath, paras);
				break;
			case 2:
				OFN_CounterClockwise.serialcomm(num,waittime);
				OFN_CounterClockwise.fileprocess(downloadpath, filepath, paras);
				break;
			}
			
			System.out.println("Please choose the OFN rotate direction you want to test: 1=clockwise, 2=counterclockwise, 0=return:");
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
			System.out.println("Please rotate in the clockwise direction");
			Thread.sleep(waittime);
			print(edit,"./get_ofn.rb > ofn_test_first.txt");
			Thread.sleep(500);
			print(edit,"sz ofn_test_first.txt");
			Thread.sleep(4000);
			System.out.println("Please rotate in the counter-clockwise direction");
			Thread.sleep(waittime);
			print(edit,"./get_ofn.rb > ofn_test_last.txt");
			Thread.sleep(500);
			print(edit,"sz ofn_test_last.txt");
			Thread.sleep(4000);
			print(edit,"rm -r ofn_test_first.txt ofn_test_last.txt");
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	
	
	// Process the file and generate a standardized file
			public static boolean fileprocess(String downloadpath, String filepath, String []paras, String sn){

				int errorcode=0;// print error code in the final output file;
				int errornum=18;
				int value = 0;
				String result="";

				File downloadfile_first = new File(downloadpath+"\\ofn_test_first.txt");
				File downloadfile_last = new File(downloadpath+"\\ofn_test_last.txt");
				File outputfile = new File(filepath);
				

				String line="";
				ArrayList <String> content_first = new ArrayList<String>();
				ArrayList <String> content_last = new ArrayList<String>();

				try {					
					
					BufferedReader br_first = new BufferedReader(new FileReader(downloadfile_first));
					BufferedReader br_last = new BufferedReader(new FileReader(downloadfile_last));
					while ((line=br_first.readLine())!=null){
						content_first.add(line);
					}
					br_first.close();
					downloadfile_first.delete();
					while ((line=br_last.readLine())!=null){
						content_last.add(line);
					}
					br_last.close();
					downloadfile_last.delete();
							
					ArrayList<String> content;
					for (int i=0;i<2;i++){
						int action=Integer.parseInt(paras[i+1]); // paras[1] is 0 or 1, which indicates the rotation direction.
						//  0 = clockwise direction;
						// 1 = counter-clockwise direction.
						if (i==0){
							content=content_first;
						}
						else {
							content = content_last;
						}
						
						if (Integer.parseInt(content.get(1))!=0){
							result = "FAILED";
							errorcode = 3;
							break;
						}
						else if (action==0){
							if (Integer.parseInt(content.get(0))==0){
								result = "FAILED";
								errorcode=4;
							}
							
							else if ((Integer.parseInt(content.get(0))>0)&&(Integer.parseInt(content.get(0))<=127)){
								result = "PASS";
								value = Integer.parseInt(content.get(0));
							}
							else {
								result = "FAILED";
								errorcode = 2;
								break;
							}
						}
						else {
							if ((Integer.parseInt(content.get(0))>=128)&&(Integer.parseInt(content.get(0))<=255)){
								result = "PASS";
								value = Integer.parseInt(content.get(0));
							}
							else {
								result = "FAILED";
								errorcode = 2;
								break;
							}
						}
					}

				} catch (IOException e) {
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
					bw.write("OFN_Result="+result+"\r\n");
					System.out.println("OFN_Result="+result);
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


*/
}
