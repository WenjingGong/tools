package loginprocess;

import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.misc.basicStructures.LPARAM;
import org.xvolks.jnative.misc.basicStructures.UINT;
import org.xvolks.jnative.misc.basicStructures.WPARAM;
import org.xvolks.jnative.util.User32;

import features.FeatureIm;

public class Login extends FeatureIm{
	
	public static void main(String []args){
		
		String num=args[0];
		try{
			HWND targetwindow=User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			print(edit,"root");// input "root" into the terminal area
			Thread.sleep(500);
			print(edit,"password");
			Thread.sleep(500);
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
			Thread.sleep(200);//wait for response, the time may change
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
			Thread.sleep(1000);//wait for response, the time may change
			print(edit,"root");// input "root" into the terminal area
			Thread.sleep(500);
			print(edit,"password");
			Thread.sleep(500);
		}catch (Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	

}
