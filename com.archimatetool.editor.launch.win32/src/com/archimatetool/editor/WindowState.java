/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;

import com.archimatetool.editor.Logger;


/**
 * Window State
 * 
 * @author Phillip Beauvoir
 */
public class WindowState {
    public static final String K_PRIMARY_WINDOW = "PRIMARY_WINDOW"; //$NON-NLS-1$
    public static final String WINDOW = ".window"; //$NON-NLS-1$
    
    private File file;
    private Properties properties;
    
    public static WindowState get(String name) {
        URL url = Platform.getInstanceLocation().getURL();
        try {
            url = FileLocator.toFileURL(url);
        } catch (IOException e) {
            //ignore this exception
        }
        File file = new File(url.getFile(), name);
        WindowState state = new WindowState(file);
        return state;
    }

    private WindowState(File file) {
        this.file = file;
    }

    public Properties getProperties() {
        if(properties == null) {
            loadProperties();
        }
        return properties;
    }
    
    public void loadProperties() {
        properties = new Properties();

        if(file.isFile() && file.canRead()) {
            try {
                FileInputStream is = new FileInputStream(file);
                try {
                    properties.load(is);
                }
                finally {
                    is.close();
                }
            }
            catch(IOException e) {
                Logger.logError("Failed to load properties from lock file: " + file.getAbsolutePath(), e); //$NON-NLS-1$
            }
        }
    }

    public void saveProperties() {
        if(properties == null) {
            return;
        }

        file.getParentFile().mkdirs();
        try {
            FileOutputStream out = new FileOutputStream(file);
            try {
                properties.store(out, null);
            }
            finally {
                out.close();
            }
        }
        catch(IOException e) {
            Logger.logError("Failed to save properties to lock file: " + file.getAbsolutePath(), e); //$NON-NLS-1$
        }
    }

    public void delete() {
        file.delete();
        properties = null;
    }

    public boolean exists() {
        return file.exists();
    }
}