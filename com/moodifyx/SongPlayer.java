package com.moodifyx;

import javax.sound.sampled.*;
import java.io.File;

public class SongPlayer {
    private static Clip clip;
 
    public static void play(String filePath){
         try{
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.flush();
            clip.start();
        } catch(Exception e){
            System.out.println("Error Playing Sound: " + e.getMessage());
        }
    }
    public static void stop(){
        if(clip != null && clip.isRunning()){
            clip.stop();
            clip.close();
        }
    }
}
  