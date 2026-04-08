package com.makesquare.util;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * Utility class for playing audio files
 */
public class AudioPlayer {
    
    public static void playSound(String filePath) {
        new Thread(() -> {
            Clip clip = null;
            try {
                File audioFile = new File(filePath);
                if (!audioFile.exists()) {
                    System.err.println("Audio file not found: " + filePath);
                    return;
                }
                
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
                clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                
                // Wait for the clip to finish
                Thread.sleep(clip.getMicrosecondLength() / 1000);
                
            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | InterruptedException e) {
                System.err.println("Error playing audio: " + e.getMessage());
            } finally {
                if (clip != null) {
                    clip.close();
                }
            }
        }).start();
    }
}
