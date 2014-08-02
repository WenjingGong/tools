package mini_test;

import java.util.Scanner;

import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.misc.basicStructures.LPARAM;
import org.xvolks.jnative.misc.basicStructures.UINT;
import org.xvolks.jnative.misc.basicStructures.WPARAM;
import org.xvolks.jnative.util.User32;

import features.FeatureIm;

public class fw_update extends FeatureIm{
	
	public static void serialcomm(String num, String filename){
		try{
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
		
			print(edit,"cd /mnt/data/mspfirmware");
			Thread.sleep(500);
			
			print(edit,"./load_mspfirmware "+filename);
			// At this time, no specific time is estimated for the update process. However, the LED may indicate the completion.
			
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}

}
