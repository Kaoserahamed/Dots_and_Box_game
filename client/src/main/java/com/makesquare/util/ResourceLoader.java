package com.makesquare.util;

import javax.swing.ImageIcon;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Loads the game's images and sounds.
 *
 * <p>Resources are read from the classpath first, so the game finds them whether it runs from
 * an IDE, from a JAR or from a container. The on-disk lookup is kept as a fallback for the case
 * where the resources sit on disk but not on the classpath.
 */
public class ResourceLoader {

    private static final String RESOURCE_PATH = "src/main/resources/";

    /**
     * Load an image from the game's resources.
     *
     * @param filename the name of the image inside the resources
     * @return the image, or an empty icon when the image cannot be found
     */
    public static ImageIcon loadImage(String filename) {
        URL resource = ResourceLoader.class.getClassLoader().getResource(filename);
        if (resource != null) {
            return new ImageIcon(resource);
        }

        File file = new File(RESOURCE_PATH + filename);
        if (file.exists()) {
            return new ImageIcon(file.getAbsolutePath());
        }

        file = new File(filename);
        if (file.exists()) {
            return new ImageIcon(file.getAbsolutePath());
        }

        System.err.println("Image not found: " + filename);
        return new ImageIcon();
    }

    /**
     * Open a sound from the game's resources.
     *
     * <p>The sound is handed over as a stream rather than a path, because a resource inside a
     * JAR has no path on disk for the audio system to open.
     *
     * @param filename the name of the sound inside the resources
     * @return the stream, or {@code null} when the sound cannot be found
     */
    public static InputStream getAudioStream(String filename) {
        InputStream resource = ResourceLoader.class.getClassLoader().getResourceAsStream(filename);
        if (resource != null) {
            return resource;
        }

        File file = new File(RESOURCE_PATH + filename);
        if (file.exists()) {
            try {
                return new FileInputStream(file);
            } catch (IOException e) {
                System.err.println("Audio file could not be opened: " + filename);
                return null;
            }
        }

        System.err.println("Audio file not found: " + filename);
        return null;
    }
}
