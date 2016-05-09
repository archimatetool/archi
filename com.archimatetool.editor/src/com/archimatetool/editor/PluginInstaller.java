/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;

import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.editor.utils.ZipUtils;


/**
 * Installs Plug-ins
 * 
 * @author Phillip Beauvoir
 */
public class PluginInstaller {
    
    static final String INSTALL_FOLDER = ".install"; //$NON-NLS-1$
    static final String MAGIC_ENTRY = "archi-plugin"; //$NON-NLS-1$

    /**
     * @param file
     * @return True if the file is an Archi plugin Zip file
     * @throws IOException
     */
    public static boolean isPluginZipFile(File file) throws IOException {
        return ZipUtils.isZipFile(file) && ZipUtils.hasZipEntry(file, MAGIC_ENTRY);
    }

    /**
     * Unzip the zip file to the install folder
     * @param zipFile The zip file
     * @throws IOException
     */
    public static void unpackZipPackageToInstallFolder(File zipFile) throws IOException {
        File installFolder = getInstallFolder();
        if(installFolder != null) {
            FileUtils.deleteFolder(installFolder);
            ZipUtils.unpackZip(zipFile, installFolder);
        }
    }

    /**
     * Check for and copy any pending plugins in the install folder.
     * @return True if plugins were copied to the plugins folder and the install folder deleted
     * @throws IOException
     */
    public static boolean installPendingPlugins() throws IOException {
        File installFolder = getInstallFolder();

        if(installFolder != null && installFolder.exists() && installFolder.isDirectory()) {
            File pluginsFolder = getPluginsFolder();
            
            for(File file : installFolder.listFiles()) {
                // Ignore the magic entry file
                if(MAGIC_ENTRY.equalsIgnoreCase(file.getName())) {
                    continue;
                }
                
                // Delete old ones in target plugins folder
                deleteMatchingPlugin(file, pluginsFolder);
                
                // Copy new ones
                if(file.isDirectory()) {
                    FileUtils.copyFolder(file, new File(pluginsFolder, file.getName()));
                }
                else {
                    FileUtils.copyFile(file, new File(pluginsFolder, file.getName()), false);
                }
            }
            
            // Now delete the install folder
            FileUtils.deleteFolder(installFolder);
            
            // If we got this far and successfully deleted the install folder then return true
            if(!installFolder.exists()) {
                return true;
            }
        }
        
        return false;
    }
    
    // Delete matching target plugin
    private static void deleteMatchingPlugin(File newPlugin, File pluginsFolder) throws IOException {
        String pluginName = getPluginName(newPlugin.getName());

        for(File file : findMatchingPlugins(pluginsFolder, pluginName)) {
            if(file.isDirectory()) {
                FileUtils.deleteFolder(file);
            }
            else {
                file.delete();
            }
        }
    }
    
    private static File[] findMatchingPlugins(File pluginsFolder, final String pluginName) {
        return pluginsFolder.listFiles(new FileFilter() {
            public boolean accept(File file) {
                String targetPluginName = getPluginName(file.getName());
                return targetPluginName.equals(pluginName);
            }
        });
    }
    
    static String getPluginName(String string) {
        int index = string.indexOf("_"); //$NON-NLS-1$
        if(index != -1) {
            string = string.substring(0, index);
        }
        
        return string;
    }

    static String getPluginVersion(String string) {
        int index = string.lastIndexOf(".jar"); //$NON-NLS-1$
        if(index != -1) {
            string = string.substring(0, index);
        }
        
        index = string.lastIndexOf("_"); //$NON-NLS-1$
        if(index != -1) {
            string = string.substring(index + 1);
        }
        
        return string;
    }
    
    static File getInstallFolder() {
        Location instanceLoc = Platform.getInstanceLocation();
        if(instanceLoc != null) {
            return new File(instanceLoc.getURL().getPath(), INSTALL_FOLDER);
        }
        return null;
    }
    
    static File getPluginsFolder() throws IOException {
        URL url = Platform.getBundle(ArchimateEditorPlugin.PLUGIN_ID).getEntry("/"); //$NON-NLS-1$
        url = FileLocator.resolve(url);
        return new File(url.getPath()).getParentFile();
    }
}
