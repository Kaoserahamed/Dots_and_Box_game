package com.makesquare.util;

import javax.swing.ImageIcon;
import java.io.File;

/**
 * Utility class for loading game resources
 */
public class ResourceLoader {
    private static final String RESOURCE_PATH = "src/main/resources/";
    
    public static ImageIcon loadImage(String filename) {
        File file = new File(RESOURCE_PATH + filename);
        if (file.exists()) {
            return new ImageIcon(file.getAbsolutePath());
        }
        
        // Fallback to root directory for backward compatibility
        file = new File(filename);
        if (file.exists()) {
            return new ImageIcon(file.getAbsolutePath());
        }
        
        System.err.println("Image not found: " + filename);
        return new ImageIcon();
    }
    
    public static String getAudioPath(String filename) {
        File file = new File(RESOURCE_PATH + filename);
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        
        // Fallback to root directory
        file = new File(filename);
        if (file.exists()) {
            return file.getAbsolutePath();
        }
        
        System.err.println("Audio file not found: " + filename);
        return "";
    }
}
