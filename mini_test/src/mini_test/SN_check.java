package mini_test;

import java.io.*;

import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.misc.basicStructures.LPARAM;
import org.xvolks.jnative.misc.basicStructures.UINT;
import org.xvolks.jnative.misc.basicStructures.WPARAM;
import org.xvolks.jnative.util.User32;

import features.FeatureIm;

public class SN_check extends FeatureIm{
	public static String sn_check(String num, String filepath){
		int errorcode = 0;
		int errornum = 14;
		String sn = "";
		String result="";
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
			
			print(edit,"/usr/lib/leeoalpha/sensors/mount-data.sh");
			Thread.sleep(3000);
			print(edit,"sz /mnt/other/sn.txt");
			Thread.sleep(4000);
			print(edit,"/usr/lib/leeoalpha/sensors/umount-date.sh");
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		File downloadfile = new File(filepath+"\\sn.txt");
		try{
			BufferedReader br = new BufferedReader(new FileReader(downloadfile));
			sn=br.readLine();
			br.close();
			if (sn.equals("")){
				result = "FAILED";
				errorcode = 1;
			}
		}catch (IOException ex){
			result = "FAILED";
			errorcode=0;
		}
		downloadfile.delete();
		return sn;
	}

}
