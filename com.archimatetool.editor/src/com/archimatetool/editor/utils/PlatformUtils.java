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
@SuppressWarnings("nls")
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
     * @return True if we're running on a x86 64bit-based architecture.
     */
    public static boolean isX86_64() {
        return Platform.getOSArch().equals(Platform.ARCH_X86_64);
    }
    
    /**
     * @return True if we're running on a AARCH64 bit-based architecture.
     */
    public static boolean isAarch64() {
        return Platform.getOSArch().equals(Platform.ARCH_AARCH64);
    }
    
    /**
     * @return True if we're running on Apple Silicon
     */
    public static boolean isAppleSilicon() {
        return isMac() && isAarch64();
    }
    
    /**
     * @return True if we're running on Linux Wayland graphics
     */
    public static boolean isLinuxWayland() {
        return isLinux() && "wayland".equalsIgnoreCase(System.getenv("XDG_SESSION_TYPE"));
    }

    /**
     * @return True if we're running on Linux X11 graphics
     */
    public static boolean isLinuxX11() {
        return isLinux() && "x11".equalsIgnoreCase(System.getenv("XDG_SESSION_TYPE"));
    }

    /**
     * @return true if The OS version number matches version
     */
    public static boolean isOSVersion(String version) {
        return System.getProperty("os.version").equals(version);
    }

    /**
     * Compare given version to current OS version and see if the current OS version is greater than the given version
     * 
     * @param version The version string to compare to system OS version
     * @return -1 if newer < older <br/>
     *          0 if newer == older <br/>
     *          1 if newer > older
     */
    public static int compareOSVersion(String version) {
        String current = System.getProperty("os.version");
        return StringUtils.compareVersionNumbers(current, version);
    }

    /**
     * @return The App Data folder for each platform
     */
    public static File getApplicationDataFolder() {
        // Windows
        if(isWindows()) {
            return new File(System.getenv("APPDATA"));
        }
        
        // Linux
        if(isLinux()) {
            return new File(System.getProperty("user.home"));
        }
        
        // Mac
        if(isMac()) {
            return new File(System.getProperty("user.home"), "Library/Application Support");
        }

        // Default
        return new File(System.getProperty("user.home"));
    }
}
