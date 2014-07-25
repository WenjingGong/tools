package exitprogram;
// This program is to exit ZOC program
import org.xvolks.jnative.util.User32;
import org.xvolks.jnative.misc.basicStructures.HWND;

public class EndOfZoc {
	
	public static void main(String []args){
		
		String num=args[0];
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
