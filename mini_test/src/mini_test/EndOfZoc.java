package mini_test;

import org.xvolks.jnative.util.User32;
import org.xvolks.jnative.misc.basicStructures.HWND;

public class EndOfZoc {
	
	public static void endzoc(String num){
		try {
			//search for the ZOC window of specific com port
			HWND targetwindow=User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");
			//close the specific window
			Runtime.getRuntime().exec("taskkill /pid "+User32.GetWindowThreadProcessId(targetwindow)+" -f");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
