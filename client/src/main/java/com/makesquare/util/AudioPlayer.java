package com.makesquare.util;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class for playing audio files
 */
public class AudioPlayer {

    public static void playSound(String filePath) {
        File audioFile = new File(filePath);
        if (!audioFile.exists()) {
            System.err.println("Audio file not found: " + filePath);
            return;
        }

        try (InputStream stream = new FileInputStream(audioFile)) {
            playSound(stream);
        } catch (IOException e) {
            System.err.println("Error playing audio: " + e.getMessage());
        }
    }

    /**
     * Play a sound that is already open.
     *
     * <p>The sound is read from a stream rather than from a path, because a resource inside a
     * JAR has no path on disk for the audio system to open.
     *
     * @param stream the sound to play
     */
    public static void playSound(InputStream stream) {
        new Thread(() -> {
            Clip clip = null;
            try {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(stream);
                clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();

                // Wait for the clip to finish
                Thread.sleep(clip.getMicrosecondLength() / 1000);

            } catch (UnsupportedAudioFileException | IOException | LineUnavailableException
                    | InterruptedException e) {
                System.err.println("Error playing audio: " + e.getMessage());
            } finally {
                if (clip != null) {
                    clip.close();
                }
            }
        }).start();
    }
}
