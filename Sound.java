import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.sound.sampled.*;

/**
 * This class loads a sound file and enables it to be played, paused, and closed
 * Make sure the sound file is in your "src" folder!
 * @author Morgan Roff
 * @version 1.0
 */
public class Sound {
	
	private Clip clip;
	private AudioInputStream stream;
	private FloatControl volume;
	 /**
	  * Loads and opens the specified sound file. Make sure the sound file is in your "src" folder!
	  * @param songName A string indicating the name of the file.
	  */
	public Sound(String songName) {

		ClassLoader loader = getClass().getClassLoader();
		try {
			clip = AudioSystem.getClip();
			stream = AudioSystem.getAudioInputStream(loader.getSystemResource(songName));
			clip.open(stream);
		}
		catch (Exception e) {
		}
		try {
		volume = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		}
		catch(IllegalArgumentException e) {
			System.err.println("MASTER_GAIN error. Audio file did not load.");
		}
	}
	
	/**
	 * Plays the sound file from the beginning
	 */
	public void playSong() {
		clip.setFramePosition(0);
		clip.start();
	}
	
	/**
	 * Stops playback of the sound file.
	 */
	public void endSong() {
		clip.stop();	
	}
	
	/**
	 * Closes the sound file.
	 */
	public void closeFile() {
		try {
			clip.close();
			stream.close();
		} catch (IOException e) {
		}
	}
	
	/**
	 * Mutes the volume
	 */
	public void quiet() {
		volume.setValue(volume.getMinimum());
	}
	
	/**
	 * Sets the volume to maximum
	 */
	public void loud() {
		volume.setValue(volume.getMaximum());
	}
	
}
