package mini_test;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class Mini_test {
	public static void main(String []args){
		String num = args[0];
		String filepath=args[1];
		ArrayList<String> content = new ArrayList<String> ();
		String downloadpath = filepath+"\\downloadfile_com"+num;
		try{
			BufferedReader br = new BufferedReader(new FileReader("parameters.txt"));
			String line = "";
			while ((line=br.readLine())!=null){
				content.add(line);
			}
			br.close();
		}catch (IOException ex){
			ex.printStackTrace();
			System.out.println("No input parameters file");
		}
		try {
			LaunchZoc.onewindow(num);
			Thread.sleep(1000);
			LaunchZoc.runhelper(num, filepath, content.get(content.size()-1));
			Thread.sleep(1000);
			Login.serialcomm(num);
			Login.checkloginstatus(num, downloadpath);
			
		}catch (Exception ex){
			ex.printStackTrace();
		}
		String sn = SN_check.sn_check(num, downloadpath);
		System.out.println("Please enter the test item ID, in the specific order:(1-6),0 is quit");	
		System.out.println("If this is the first test of this device, please enter 7 to update the firmware:");
		System.out.println("0:quit");
		System.out.println("1:SW_Version_check,FW_Version_Check,MAC_Address_check,ddr_test,emmc_test,rw_emmc_test,temperature_test,humidity_test");
		System.out.println("2:LED_test(including white LED and RGB LED tests,chosen by user)");
		System.out.println("3:OFN_test(including clockwise and counterwise tests, chosen by user)");
		System.out.println("4:ALS_test(including dark and light tests, chosen by user)");
		System.out.println("5:MIC_test");
		System.out.println("6:Reset_Detection_test(including press and release tests, chosen by user)");
		//System.out.println("7:Wifi_test");
		System.out.println("7:firmware update");
		Scanner scanner = new Scanner(System.in);
		String line = scanner.nextLine();
		
		int fileindex=0;
		File outputfile = new File(filepath+"\\original output\\"+sn+".txt");
		while (outputfile.exists()){
			fileindex++;
			outputfile = new File(filepath+"\\original output\\"+sn+"("+fileindex+").txt");
		}
		
		File summaryfile = new File(filepath+"\\"+sn+"_summary.txt");
		if (fileindex!=0){
			summaryfile = new File(filepath+"\\"+sn+"_summary("+fileindex+").txt");
		}
		
		
		
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile));
			Date current = new Date();
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
			bw.write("Begin_Date="+date.format(current)+"\r\n");
			bw.write("Begin_Time="+time.format(current)+"\r\n");
			bw.close();
		}catch (Exception ex){
			ex.printStackTrace();
		}
		
		
		while (!line.equals("0")){
			
			String []inputID = line.split(" ");
			for (int i=0;i<inputID.length;i++){
				int itemID = Integer.parseInt(inputID[i]);
				String []paras;
				switch(itemID){
				case 1:
					// sw ddr emmc_size emmc_rw rw_eeprom wifi temperature humidity
					//sw image version check 
					paras = content.get(0).split(" ");
					SW_version.serialcomm(num, downloadpath);
					SW_version.fileprocess(downloadpath, outputfile.getAbsolutePath(), sn);
					//fw version check 
					paras = content.get(1).split(" ");
					FW_version.serialcomm(num);
					FW_version.fileprocess(downloadpath, outputfile.getAbsolutePath(), paras, sn);
					//mac address check
					paras = content.get(2).split(" ");
					Mac_Read.serialcomm(num, downloadpath);
					Mac_Read.fileprocess(downloadpath, outputfile.getAbsolutePath(), sn);					
					//ddr size check
					paras = content.get(3).split(" ");
					DDR_test.serialcomm(num, downloadpath);
					DDR_test.fileprocess(downloadpath,outputfile.getAbsolutePath(),paras,sn);
					//emmc size check
					paras = content.get(4).split(" ");
					EMMC_test.serialcomm(num, downloadpath);
					EMMC_test.fileprocess(downloadpath, outputfile.getAbsolutePath(), paras,sn);
					//emmc rw check
					paras = content.get(5).split(" ");
					RW_EMMC.serialcomm(num, paras, downloadpath);
					RW_EMMC.fileprocess(downloadpath, outputfile.getAbsolutePath(), paras, sn);
					//temperature check
					paras = content.get(6).split(" ");
					Temperature_test.serialcomm(num, downloadpath);
					Temperature_test.fileprocess(downloadpath, outputfile.getAbsolutePath(), paras, sn);
					//humidity check
					paras = content.get(7).split(" ");
					Humidity_test.serialcomm(num, downloadpath);
					Humidity_test.fileprocess(downloadpath, outputfile.getAbsolutePath(), paras, sn);
					break;
				case 2:
					//LED test
					paras = content.get(8).split(" ");
					LED_test.led_branch(num, outputfile.getAbsolutePath(), sn, scanner);
					break;
				case 3:
					//OFN test
					paras = content.get(9).split(" ");
					OFN_test.ofn_branch(num, Long.parseLong(paras[0]), outputfile.getAbsolutePath(), downloadpath, paras, scanner);
					break;
				case 4:
					//ALS test
					paras = content.get(10).split(" ");
					ALS_test.ALS_branch(num, outputfile.getAbsolutePath(), downloadpath, paras, scanner);
					break;
				case 5:
					//mic test
					paras = content.get(11).split(" ");
					MIC_test.process(num, downloadpath, outputfile.getAbsolutePath(), paras, sn);
					break;
				case 6:
					//reset key detection
					paras = content.get(12).split(" ");
					Reset_key.reset_branch(num, outputfile.getAbsolutePath(), downloadpath, scanner);
					break;
				case 7:
					paras = content.get(13).split(" ");
					fw_update.serialcomm(num, paras[0]);
				}				
			}
			System.out.println("Please enter the test item ID, in the specific order:(1-6),0 is quit");	
			System.out.println("0:quit");
			System.out.println("1:SW_Version_check,FW_Version_Check,MAC_Address_check,ddr_test,emmc_test,rw_emmc_test,temperature_test,humidity_test");
			System.out.println("2:LED_test(including white LED and RGB LED tests,chosen by user)");
			System.out.println("3:OFN_test(including clockwise and counterwise tests, chosen by user)");
			System.out.println("4:ALS_test(including dark and light tests, chosen by user)");
			System.out.println("5:MIC_test");
			System.out.println("6:Reset_Detection_test(including press and release tests, chosen by user)");
			//System.out.println("7:Wifi_test");
			System.out.println("7:firmware update(if needed)");
			System.out.println("If you have just updated the firmware, please wait for the LED light turn on again...");
			
			line=scanner.nextLine();
		}
		
		System.out.println("Exit! Please wait...\r\n");
		scanner.close();
		EndOfZoc.endzoc(num);
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outputfile,true));
			Date current = new Date();
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
			bw.write("End_Date="+date.format(current)+"\r\n");
			bw.write("End_Time="+time.format(current)+"\r\n");
			bw.close();
		}catch (Exception ex){
			ex.printStackTrace();
		}
		
		//summary file process
		File fileformat = new File("final_format.txt");
		ArrayList<String> format = new ArrayList<String>();
		line="";
		try {
			BufferedReader br_format = new BufferedReader(new FileReader(fileformat));
			while ((line=br_format.readLine())!=null){
				format.add(line);
			}
			br_format.close();
		}catch (Exception ex){
			ex.printStackTrace();
		}
		
		//get a hashmap, key is the title of each feature, value is its index in the summary file.
		//By the hashmap, we can quickly differentiate the title of a line from the original file.
		HashMap<String,Integer> titles = new HashMap<String,Integer>();
		for (int i=0;i<format.size();i++){
			titles.put(format.get(i), i);
		}
		
		ArrayList<String> original_content = new ArrayList<String>();
		line="";
		try{
			BufferedReader br_original = new BufferedReader(new FileReader(outputfile));
			while((line=br_original.readLine())!=null){
				original_content.add(line);
			}
			br_original.close();
		}catch (Exception ex){
			ex.printStackTrace();
		}
		
		String []vars=new String[2];
		String []results=new String[format.size()];
		ArrayList<String> errors = new ArrayList<String>();
		
		int index=0;
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(summaryfile));
			for (int i=0;i<original_content.size();i++){
				vars=original_content.get(i).split("=");
				if (!vars[0].equals("Error_Code")){
					index=titles.get(vars[0]);
					results[index]=vars[1];
				}
				else{
					errors.add(vars[1]);
				}
			}
			
			for (int i=0;i<format.size();i++){
				bw.write(format.get(i)+"="+results[i]+"\r\n");
			}
			bw.flush();
			for (int i=0;i<errors.size();i++){
				bw.write("Error_Code="+errors.get(i)+"\r\n");
			}
			bw.flush();
			bw.close();
		}catch (Exception ex){
			ex.printStackTrace();
		}
		
		System.out.println("Test is done!");
	}
}
