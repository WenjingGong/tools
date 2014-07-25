package sn_write;

import java.io.*;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.misc.basicStructures.LPARAM;
import org.xvolks.jnative.misc.basicStructures.UINT;
import org.xvolks.jnative.misc.basicStructures.WPARAM;
import org.xvolks.jnative.util.User32;

import features.FeatureIm;

public class SN_Write extends FeatureIm{
	
	public static void main(String []args){
		// Allocate the arguments: The first one is port number, the second one is the output file path, and the rest are parameters.
		String num=args[0];
		String filepath=args[1];
		String []paras=new String[args.length-2];
		int i;
		for (i=0;i<paras.length;i++){
			paras[i]=args[i+2];
		}		
		
		serialcomm(num,paras);
		
	}
	
	public static void serialcomm(String num, String []paras){
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
			
			print(edit,"cd /usr/lib/leeoalpha/sensors");
			Thread.sleep(500);
			print(edit,"./mount-data.sh");
			Thread.sleep(500);
			print(edit,"rm -r /mnt/other/sn.txt");
			Thread.sleep(500);
			print(edit,"touch /mnt/other/sn.txt");
			Thread.sleep(500);
			print(edit,"echo "+paras[0]+" > /mnt/other/sn.txt");
			Thread.sleep(500);			
			print(edit,"./umount-date.sh");
			Thread.sleep(500);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
