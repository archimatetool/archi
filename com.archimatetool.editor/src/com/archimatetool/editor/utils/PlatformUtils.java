/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.utils;

import java.io.File;

import org.eclipse.core.runtime.Platform;


/**
 * PlatformUtils
 * 
 * @author Phillip Beauvoir
 */
public class PlatformUtils {
    
    /**
     * @return True if we're running on Windows
     */
    public static boolean isWindows() {
        return Platform.getOS().equals(Platform.OS_WIN32);
    }
    
    /**
     * @return True if we're running on Mac OS X
     */
    public static boolean isMac() {
        return Platform.getOS().equals(Platform.OS_MACOSX);
    }
    
    /**
     * @return True if we're running on Linux
     */
    public static boolean isLinux() {
        return Platform.getOS().equals(Platform.OS_LINUX);
    }
    
    /**
     * @return True if we're running on Linux GTK
     */
    public static boolean isGTK() {
    	return Platform.WS_GTK.equals(Platform.getWS());
    }

    /**
     * @return Trus if the platform support Full Screen mode (Mac 10.7 or greater)
     */
    public static boolean supportsMacFullScreen() {
        return isMac() && System.getProperty("os.version").compareTo("10.7") >= 0;  //$NON-NLS-1$//$NON-NLS-2$
    }

    /**
     * @return The App Data folder for each platform
     */
    public static File getApplicationDataFolder() {
        // Windows
        if(isWindows()) {
            return new File(System.getenv("APPDATA")); //$NON-NLS-1$
        }
        
        // Linux
        if(isLinux()) {
            return new File(System.getProperty("user.home")); //$NON-NLS-1$
        }
        
        // Mac
        if(isMac()) {
            return new File(System.getProperty("user.home"), "Library/Application Support"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        // Default
        return new File(System.getProperty("user.home")); //$NON-NLS-1$
    }
}
