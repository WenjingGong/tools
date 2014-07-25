package features;

import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.misc.basicStructures.LPARAM;
import org.xvolks.jnative.misc.basicStructures.UINT;
import org.xvolks.jnative.misc.basicStructures.WPARAM;
import org.xvolks.jnative.util.User32;

public class FeatureIm {
		
		// This is a method to enter commands in the ZOC window;
		public static void print(HWND edit, String input){
		char[] inputchar=input.toCharArray();
		try {
			// press the input string
			for (int i=0;i<inputchar.length;i++){
				User32.SendMessage(edit, new UINT(0x102), new WPARAM((int)inputchar[i]), new LPARAM(1));
			}
			// press enter
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1));
		}catch (Exception ex){
			ex.printStackTrace();
		}
		
	}

		// This is a method to process the download file and generate a standardized output file in the specific location.
		public static boolean fileprocess(String downloadpath, String filepath, String []paras){
			/* Arguments Declaration: downloadpath is the path of the downloaded files from ZOC;
			 * 						  filepath is the path of the final output file, which is of a standardized format;
			 * 						  paras contains parameters which are useful to judge the final result and should be contained within the final output file;
			*/
			return true;
		}

}
