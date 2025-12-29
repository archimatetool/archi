/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageFileNameProvider;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.model.util.Logger;

/**
 * Helper to create temporary SVG image files on Mac/Linux
 * 
 * When using the Image constructors Image(Display, InputStream) or Image(Display, String)
 * a scaled up SVG image is only rendered correctly on Windows.
 * 
 * The only Image constructor that works on all platforms is Image(Display, ImageFileNameProvider).
 * So if on Mac/Linux use this helper to create a temporary SVG file and load it with a ImageFileNameProvider
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
class SVGImageHelper {
    
    /**
     * Temp directory
     */
    private static Path tempDir = Path.of(System.getProperty("java.io.tmpdir"), "com.archimatetool.img.tmp");
    
    /**
     * Cache of imagePath -> temp image file path
     */
    private Map<String, Path> tempImagePaths = new HashMap<>();
    
    static {
        // Delete contents of temp dir on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                FileUtils.deleteDir(tempDir);
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }));
    }

    /**
     * Create an image by creating a temporary file and loading it
     */
    Image createImage(String imagePath, byte[] imageBytes) throws IOException {
        // Create temp image if not present in cache
        Path path = tempImagePaths.get(imagePath);
        if(path == null) {
            path = createTempImageFile(imageBytes);
            tempImagePaths.put(imagePath, path);
        }
        
        if(!path.toFile().exists()) {
            return null;
        }
        
        final String pathStr = path.toString();
        
        return new Image(Display.getCurrent(), (ImageFileNameProvider) zoom -> {
            // Only return the image path for 100% zoom. For SVG files the image can be natively scaled up.
            // Also, on Windows, if we return the same image path at 200% there will be an IllegalArgumentException when calling
            // GC.drawImage(Image image, int srcX, int srcY, int srcWidth, int srcHeight, int destX, int destY, int destWidth, int destHeight)
            return zoom == 100 ? pathStr : null;
        });
    }
    
    /**
     * Create a temporary SVG image file and return the Path to it
     */
    private Path createTempImageFile(byte[] imageBytes) throws IOException {
        tempDir.toFile().mkdirs();
        
        // Write image bytes to temp file
        Path path = Files.createTempFile(tempDir, null, ".svg");
        Files.write(path, imageBytes);
        
        return path;
    }

    void dispose() {
        if(tempImagePaths != null) {
            for(Path path : tempImagePaths.values()) {
                try {
                    Files.deleteIfExists(path);
                }
                catch(IOException ex) {
                    Logger.logError("Could not delete temporary SVG image", ex);
                }
            }
            
            tempImagePaths = null;
        }
    }
}
