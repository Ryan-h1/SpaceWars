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
 */

import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioPlayer
{
  private Clip c;
  
  // constructor to create audioplayer based on file name
  public AudioPlayer(String fileName) {
    try
    {
      File file = new File(fileName);
      if (file.exists())
      {
        AudioInputStream sound = AudioSystem.getAudioInputStream(file);
        c = AudioSystem.getClip();
        c.open(sound);
      }
    } catch (Exception e)
    {
      
    }
    
  }
  
  //method play to play the audio
  public void play()
  {
    c.setFramePosition(0);
    c.start();
  }
  
  //method to set looping of audio to forever
  public void loop()
  {
    c.loop(Clip.LOOP_CONTINUOUSLY);
  }
  
  //method to stop the playing of the clip
  public void stop()
  {
    c.stop();
  }
}
