package mini_test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.util.User32;


public class LaunchZoc {
	
	
	public static void onewindow(String num){
		try {
			//find the ZOC window of specific com port with its class and name
			HWND targetwindow=User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");
			if (targetwindow.getValue().intValue()>0){//if the target window exists, close it and create a new one to avoid port conflict
				Runtime.getRuntime().exec("taskkill /pid "+User32.GetWindowThreadProcessId(targetwindow)+" -f");
			}
			// to find the possible window with the same port number and close them.
			HWND existwindow=User32.FindWindow("ZocMainWindow", "ZOC/Pro (Standard.zoc) [evaluation mode]");
			HWND existnullwindow=User32.FindWindow("ZocMainWindow", "(unknown) (Standard.zoc) [evaluation mode]");
			int PID=User32.GetWindowThreadProcessId(existwindow);
			Runtime.getRuntime().exec("taskkill /pid "+PID+" -f");
			PID=User32.GetWindowThreadProcessId(existnullwindow);
			Runtime.getRuntime().exec("taskkill /pid "+PID+" -f");
		}catch (Exception ex){
			ex.printStackTrace();
		}
	}
	
	
	public static void runhelper(String num, String path){
		try{
			File filepath=new File(path);//where the required file locates.
			if (!filepath.exists()){
				filepath.mkdirs();
			}
			File downloadpath=new File(filepath.getAbsolutePath()+"\\downloadfile_com"+num);//where the file downloaded from DUT locates.
			if (!downloadpath.exists()){//the download path location depends on the file path. 
				downloadpath.mkdir();
			}
			File scriptpath,script;
			scriptpath=new File(filepath.getAbsolutePath()+"\\zocscript_com"+num);// where the script file locates. The script file is a necessary file of ZOC configuration.
			if (!scriptpath.exists()){
				scriptpath.mkdirs();
			}
			script=new File(scriptpath.getAbsolutePath()+"\\script.zrx");// a new script overwrites the old one.
			if (script.exists()){
				script.delete();
			}
			File originalpath,summarypath;
			originalpath=new File(filepath.getAbsolutePath()+"\\original output");
			summarypath=new File(filepath.getAbsolutePath()+"\\summary output");
			if (!originalpath.exists()){
				originalpath.mkdirs();
			}
			if (!summarypath.exists()){
				summarypath.mkdirs();
			}
			BufferedWriter bw=new BufferedWriter(new FileWriter(script));//write the script file
			//The next line is to launch ZOC with a valid license and define the window's name
			bw.write("CALL ZocSetProgramOption \'WindowTitle=\""+"COM"+num+" [evaluation mode]\"\'\r\n");	
			//The next line is to launch ZOC without license(evaluation mode) and define the window's name
			bw.write("CALL ZocSetProgramOption \'WindowTitle=\""+"COM"+num+"\"\'\r\n");	// define the ZOC window title with port number			
			bw.write("CALL ZocSetProgramOption \'DownloadPath=\""+downloadpath.getAbsolutePath()+"\\\"\'\r\n");// define the ZOC download path
			//bw.write("CALL ZocSetProgramOption \"SafWarnRTS=no\"");
			bw.write("CALL ZocSetSessionOption \"ExistActionOldDate=4\"\r\n");
			bw.write("CALL ZocSetSessionOption \"ExistActionNewerDate=4\"\r\n");
			bw.write("CALL ZocSetSessionOption \"ExistActionSameDate=4\"\r\n");
			bw.write("CALL ZocSetSessionOption \"ExistActionNoDate=4\"\r\n");
			bw.write("CALL ZocSetProgramOption \"HideOnTransfer=yes\"\r\n");
			bw.write("CALL ZocSetSessionOption \'ZNoWait=yes\'\r\n");// make sure that download sub-window of ZOC will disappear automatically after download is completed. 
			//perhaps an repeat file strategy is needed.
			bw.close();
			//the following zoc path could be modified.
			String command="C:\\Program Files (x86)\\ZOC6\\zoc /dev:serial/modem@com"+num+":115200 /emu:vt102 /min /run:"+script.getAbsolutePath();// run a new ZOC window.
			Runtime.getRuntime().exec(command);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	
}
