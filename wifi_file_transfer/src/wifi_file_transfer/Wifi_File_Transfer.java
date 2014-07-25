package wifi_file_transfer;

import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.misc.basicStructures.LPARAM;
import org.xvolks.jnative.misc.basicStructures.UINT;
import org.xvolks.jnative.misc.basicStructures.WPARAM;
import org.xvolks.jnative.util.User32;

import features.FeatureIm;

public class Wifi_File_Transfer extends FeatureIm{
	public static void main(String []args){
		// Allocate the arguments: The first one is port number, the second one is the output file path, and the rest are parameters.
		String num=args[0];
		
//		String []paras=new String[args.length-2];
	//	int i;
		//for (i=0;i<paras.length;i++){
			///paras[i]=args[i+2];
//		}		
		
		serialcomm(num);
	}
	
	public static void serialcomm(String num){
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
			
			print(edit,"cd /lib/firmware/ti-connectivity");
			Thread.sleep(500);
			print(edit,"rm -r wl127x-fw-4-plt.bin");
			Thread.sleep(500);
			print(edit,"rm -r /etc/udev/rules.d/70-persistent-net.rules");
			Thread.sleep(500);
			print(edit,"rz");
			Thread.sleep(26000);
	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
