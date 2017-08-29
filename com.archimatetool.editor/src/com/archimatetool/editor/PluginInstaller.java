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
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;

import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.editor.utils.ZipUtils;


/**
 * Installs Plug-ins
 * 
 * @author Phillip Beauvoir
 */
public class PluginInstaller {
    
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
     * Unzip the zip file to the plugins folder
     * @param zipFile The zip file
     * @throws IOException
     */
    public static void unpackZipPackageToPluginsFolder(File zipFile) throws IOException {
        Path tmp = Files.createTempDirectory("archi"); //$NON-NLS-1$
        File tmpFolder = tmp.toFile();
        
        try {
            ZipUtils.unpackZip(zipFile, tmpFolder);
            
            File pluginsFolder = getPluginsFolder();

            for(File file : tmpFolder.listFiles()) {
                // Ignore the magic entry file
                if(MAGIC_ENTRY.equalsIgnoreCase(file.getName())) {
                    continue;
                }
                
                // Delete old ones on exit in target plugins folder
                deleteMatchingPluginOnExit(file, pluginsFolder);

                // Copy new ones
                if(file.isDirectory()) {
                    FileUtils.copyFolder(file, new File(pluginsFolder, file.getName()));
                }
                else {
                    FileUtils.copyFile(file, new File(pluginsFolder, file.getName()), false);
                }
            }
        }
        finally {
            FileUtils.deleteFolder(tmpFolder);
        }
    }

    /**
     * @return True if we can write to the plugins folder
     */
    public static boolean canWrite() {
        try {
            File pluginsFolder = getPluginsFolder();
            return Files.isWritable(pluginsFolder.toPath());
        }
        catch(IOException ex) {
            return false;
        }
    }
    
    // Delete matching target plugin
    private static void deleteMatchingPluginOnExit(File newPlugin, File pluginsFolder) throws IOException {
        for(File file : findMatchingPlugins(pluginsFolder, newPlugin)) {
            if(file.isDirectory()) {
                recursiveDeleteOnExit(file.toPath());
            }
            else {
                file.deleteOnExit();
            }
        }
    }
    
    private static File[] findMatchingPlugins(File pluginsFolder, File newPlugin) {
        String pluginName = getPluginName(newPlugin.getName());
        
        return pluginsFolder.listFiles(new FileFilter() {
            public boolean accept(File file) {
                String targetPluginName = getPluginName(file.getName());
                return targetPluginName.equals(pluginName) && !newPlugin.getName().equals(file.getName());
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
    
    static File getPluginsFolder() throws IOException {
        URL url = Platform.getBundle(ArchiPlugin.PLUGIN_ID).getEntry("/"); //$NON-NLS-1$
        url = FileLocator.resolve(url);
        return new File(url.getPath()).getParentFile();
    }
    
    static void recursiveDeleteOnExit(Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                file.toFile().deleteOnExit();
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                dir.toFile().deleteOnExit();
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
