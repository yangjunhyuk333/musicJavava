package kr.hs.dgsw;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class RecodeTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("record start");

		RecodeTest record = new RecodeTest();
		record.captureAudio();
		try {
			Thread.sleep(5 * 1000);
			record.ais.close();
		} catch (Exception e) {

		}
		
		System.out.println("record end");

	}

	protected boolean running;
	ByteArrayOutputStream out;
	AudioInputStream ais;

	private void captureAudio() {
		try {
			final AudioFormat format = getFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
			final TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
			line.open(format);
			line.start();
			Runnable runner = new Runnable() {
				int bufferSize = (int) format.getSampleRate() * format.getFrameSize();
				byte buffer[] = new byte[bufferSize];

				public void run() {
//					out = new ByteArrayOutputStream();
//					running = true;
//					try {
//						while (running) {
//							int count = line.read(buffer, 0, buffer.length);
//							if (count > 0) {
//								out.write(buffer, 0, count);
//							}
//						}
//						out.close();
//					} catch (IOException e) {
//						System.err.println("I/O problems: " + e);
//						System.exit(-1);
//					}
					ais = new AudioInputStream(line);
					File fileOut = new File("C:\\audio\\record.wav");
					try {
						AudioSystem.write(ais, AudioFileFormat.Type.WAVE, fileOut);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			};
			Thread captureThread = new Thread(runner);
			captureThread.start();
		} catch (LineUnavailableException e) {
			System.err.println("Line unavailable: " + e);
			System.exit(-2);
		}
	}

	private AudioFormat getFormat() {
		float sampleRate = 16000; // 8kHz
		int sampleSizeInBits = 8;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = true;
		//return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
		AudioFormat af = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,16000, 16, 1, 2, 16000.0F, false);
		return af;
		
	}

}
