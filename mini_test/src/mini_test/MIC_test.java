package mini_test;

import java.io.*;

import org.xvolks.jnative.misc.basicStructures.HWND;
import org.xvolks.jnative.misc.basicStructures.LPARAM;
import org.xvolks.jnative.misc.basicStructures.UINT;
import org.xvolks.jnative.misc.basicStructures.WPARAM;
import org.xvolks.jnative.util.User32;

import java.text.NumberFormat;




import features.FeatureIm;

public class MIC_test extends FeatureIm{
	final static int CHUNK_SIZE = 65536;
	
	public static void process(String num, String filepath, String []paras, String sn){
			
		String result="";
		int errornum=3;
		int errorcode=0;
		
		File sourcefile = new File(filepath);
		if (sourcefile.exists()){
			sourcefile.delete();
		}
		serialcomm(num,paras);
		if (!sourcefile.exists()){
			try {
				System.out.println("Please wait for the wave file ready...");
				Thread.sleep(8000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		try {
		Sound wavfile = new Sound();
		ByteArrayOutputStream wavstream = wavfile.readwav(sourcefile.getAbsolutePath());
		short []audiodata = byteToShort(wavstream.toByteArray());
		
		int totalsize = audiodata.length;
		int proportion;
		if (totalsize%CHUNK_SIZE==0){
			proportion = totalsize/CHUNK_SIZE;
			if (proportion==0){
				proportion=1;
			}
		}
		else {
			proportion = totalsize/CHUNK_SIZE+1;
		}
		
		//double freq = 0.673; // 44100 / 65536 = 0.673. This is a constant, which means the frequency distance of 2 adjacent points.
		Complex [][]results = new Complex [proportion][CHUNK_SIZE];
		// For all the chunks
		for (int times = 0;times < proportion;times++){
			for (int i = 0; i<CHUNK_SIZE;i++){
				if (times*CHUNK_SIZE+i<totalsize){
					results[times][i]=new Complex(audiodata[times*CHUNK_SIZE+i],0);
				}
				else {
					results[times][i]=new Complex(0,0);
				}
			}			
			results[times]=FFT.fft(results[times]);
		}
		
		Complex []freq_results = new Complex[CHUNK_SIZE];
		for (int times = 0; times < CHUNK_SIZE; times ++){
			freq_results[times]=new Complex(0,0);
			for (int i = 0;i<proportion;i++){
				freq_results[times]=freq_results[times].add(results[i][times]);
			}
		}
		
		
		
			
			File targetfile = new File("wavfeatureslist.txt");
			
			BufferedReader br = new BufferedReader(new FileReader(targetfile));
			
			double []content = new double[CHUNK_SIZE/2];
			String line = "";
			for (int times = 0; times < CHUNK_SIZE/2; times++){
				line = br.readLine();
				content[times]=Double.parseDouble(line);
			}
			br.close();
			
			double max=0;
			for (int times = 0; times < CHUNK_SIZE/2;times++){
				if (freq_results[times].abs()>max){
					max=freq_results[times].abs();
				}
			}
			int count = 0;
			for (int times = 0; times < CHUNK_SIZE/2;times++){
				double mag = 20*Math.log(freq_results[times].abs()/max);
				if (Math.abs(mag-content[times])/Math.abs(content[times])<Double.parseDouble(paras[1])){
					count++;
				}
			}
			
			float correct_proportion = (float)count/(CHUNK_SIZE/2);
			if (correct_proportion>Double.parseDouble(paras[2])){
				result = "PASS";
			}
			else {
				result = "FAILED";
				errorcode = 1;
			}
			// if the proportion of valid lines is above input request, the result of test is passed.
			
		} catch (FileNotFoundException e) {
			result = "FAILED";
			errorcode = 0;
		}	
		catch (IOException ex){
			ex.printStackTrace();
		}
			
			
			
		File finalresult = new File(filepath+"\\original output\\"+sn+".txt");	
			
			try{
			    //need to integrate now?
			    BufferedWriter bw = new BufferedWriter(new FileWriter(finalresult));
			    bw.write("MIC_Result="+result+"\r\n");
			    System.out.println("MIC_Result="+result+"\r\n");
			    if (result.equals("FAILED")){
					NumberFormat nf = NumberFormat.getIntegerInstance();
					nf.setMinimumIntegerDigits(2);
					bw.write("Error_Code= "+nf.format(errornum)+nf.format(errorcode));
				}
			    bw.flush();
			    bw.close();
			}catch (Exception ex){
				ex.printStackTrace();
				}	
		
	}
	
	
	public static void serialcomm(String num, String []paras){
		try {
			HWND targetwindow = User32.FindWindow("ZocMainWindow", "COM"+num+" [evaluation mode]");//find the ZOC window with specific port number by its class and name
			HWND edit=User32.FindWindowEx(targetwindow, new HWND(0),"ZocTerminalArea", null);// find the input terminal sub-window of the ZOC main window.			
			User32.SendMessage(edit, new UINT(0x102), new WPARAM(10), new LPARAM(1)); // simulate a press on "enter"
			
			print(edit,"cd /usr/lib/leeoalpha/sensors");
			Thread.sleep(500);
			print(edit,"jack_rec -f alarm.wav -d "+Integer.parseInt(paras[0])+" -b 44100 system:capture_1");
			System.out.println("Record now:");
			Thread.sleep(Integer.parseInt(paras[0])*1000);
			print(edit,"sz alarm.wav");
			Thread.sleep(8000);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static short[] byteToShort(byte[] b){
		short []r = new short[b.length/2];
		for (int i = 0; i<r.length;i++){
			short s = 0; 
	        short s0 = (short) (b[2*i] & 0xff); 
	        short s1 = (short) (b[2*i+1] & 0xff); 
	        s1 <<= 8; 
	        s = (short) (s0 | s1); 
	        r[i]=s;
		}
		return r;
	}

	
	

}
