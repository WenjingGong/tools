package ssh_update;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;



public class SSH_Update {
	
	public static void main(String []args){
		
		String num = args[0];
		String filepath=args[1];
		String ip_address = "";
		String downloadpath = filepath+"\\downloadfile_com"+num;
		
		try {
			LaunchZoc.onewindow(num);
			Thread.sleep(1000);
			LaunchZoc.runhelper(num, filepath);
			Thread.sleep(1000);
		} catch (Exception ex){
			ex.printStackTrace();
		}
		
		IP_Addr.serialcomm(num);
		ip_address=IP_Addr.fileprocess(downloadpath, filepath);
		
		try {
			
			// pscp = Runtime.getRuntime().exec("pscp -pw password C:\\FI\\workspace\\mini_test\\bsl_update.bin root@"+ip_address+":/usr/local/bin/bsl_update.bin");
			//pscp.waitFor();
			Process putty = Runtime.getRuntime().exec("putty.exe -ssh -pw password -P 22 -m bsl_update_ssh.txt root@"+ip_address);
			putty.waitFor();
			
			System.out.println("update complete!");
			
			EndOfZoc.endzoc(num);
			
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}

}
