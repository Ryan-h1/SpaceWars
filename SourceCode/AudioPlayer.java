/**
 * Ryan Hecht
 * Mr. Corea
 * ICS4U1-1B
 * First Created: 2018-04-27
 * Last Edited: 2020-11-18
 * 
 * This class is the audio player class. It handles the playing, looping, and stopping of audio clips
 * used for music and sound effects. An object of this class is created with a wav file string name inputed.
 * The audio is then loaded and the instance methods can be called on the object to update the audio.
 * 
 * This class belongs to Peter Meijer and is being used under the S.A.M. use policy.
 * 
 * Modified by Ryan Hecht to fit the needs of the Space Invaders project. 
 * 
 */

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.IOException;


public class AudioPlayer {
    private Clip c;

    // constructor to create audioplayer based on file name
    public AudioPlayer(String fileName) {
        try {
            String basePath = System.getProperty("user.dir");
            String soundPath = "";

            if (basePath.endsWith("SourceCode")) {
                soundPath = basePath + "/../Sound/" + fileName;
            } else if (basePath.endsWith("src")) {
                soundPath = basePath + "/../Sound/" + fileName;
            } else {
                soundPath = basePath + "/Sound/" + fileName;
            }

            File file = new File(soundPath);
            if (file.exists()) {
                AudioInputStream sound = AudioSystem.getAudioInputStream(file);
                c = AudioSystem.getClip();
                c.open(sound);
            } else {
                throw new IOException("Audio file not found: " + soundPath);
            }
        } catch (Exception e) {
            System.err.println("Error loading the audio file: " + e.getMessage());
            System.exit(1);
        }
    }

    //method play to play the audio
    public void play() {
        if (c != null) {
            c.setFramePosition(0);
            c.start();
        } else {
            System.err.println("Error: Clip not initialized. Cannot play audio.");
        }
    }

    //method to set looping of audio to forever
    public void loop() {
        if (c != null) {
            c.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            System.err.println("Error: Clip not initialized. Cannot loop audio.");
        }
    }

    //method to stop the playing of the clip
    public void stop() {
        if (c != null) {
            c.stop();
        } else {
            System.err.println("Error: Clip not initialized. Cannot stop audio.");
        }
    }
}
