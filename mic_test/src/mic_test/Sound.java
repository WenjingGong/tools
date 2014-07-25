package mic_test;

import java.io.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;

public class Sound {
	
	public ByteArrayOutputStream readwav(String filename){
		try {
			File wavfile = new File(filename);
			AudioInputStream audioinputstream = AudioSystem.getAudioInputStream(wavfile);
			AudioFormat audiof = audioinputstream.getFormat();
			
			DataLine.Info info = new DataLine.Info(SourceDataLine.class,audiof);
			SourceDataLine line = (SourceDataLine)AudioSystem.getLine(info);
			line.open(audiof);
			
			byte audiodata[]=new byte[audiof.getSampleSizeInBits()];
			OutputStream audiooutputstream = new ByteArrayOutputStream();
			
			int inbytes = 0;
			while (inbytes!=-1){
				inbytes = audioinputstream.read(audiodata);
				if (inbytes>=0){
					audiooutputstream.write(audiodata, 0, inbytes);
				}
			}
			
			audiooutputstream.close();
			return (ByteArrayOutputStream) audiooutputstream;
			
			
		}catch (Exception ex){
			ex.printStackTrace();
		}
		
		return null;
		
	}
	
	

}
