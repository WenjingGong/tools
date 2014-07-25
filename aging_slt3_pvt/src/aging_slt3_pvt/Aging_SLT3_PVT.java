package aging_slt3_pvt;

import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.misc.basicStructures.LPARAM;
import org.xvolks.jnative.misc.basicStructures.UINT;
import org.xvolks.jnative.misc.basicStructures.WPARAM;
import org.xvolks.jnative.util.User32;

import features.FeatureIm;

public class Aging_SLT3_PVT extends FeatureIm{
	public static void main(String []args){
		// Allocate the arguments: The first one is port number, the second one is the output file path, and the rest are parameters.
				String num=args[0];
				serialcomm(num);
	}
	
	public static void serialcomm(String num){
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
		
			print(edit,"echo 0 > /usr/lib/leeoalpha/aging/non-aging.txt");
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

}
