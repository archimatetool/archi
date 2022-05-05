/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.Stream;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Some useful File Utilities
 *
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public final class FileUtils  {
	
    /**
     * Static file name initial number
     */
    public static int fFileNameCounter = -1;
    
	private FileUtils() {}
	
	/**
	 * Get the extension portion of a filename.
	 * @param file The File in question
	 * @return The extension part of the filename including the "." or "" if no extension
	 */
    public static String getFileExtension(File file) {
		String fileName = file.getName();
		int i = fileName.lastIndexOf('.');
		if(i > 0 && i < fileName.length() - 1) {
			return fileName.substring(i).toLowerCase();
		}
		return "";
	}
	
	/**
	 * Get the short name portion of a filename not including the extension.
	 * @param file The File in question
	 * @return The name part of a file name excluding the extension
	 */
	public static String getFileNameWithoutExtension(File file) {
		String fileName = file.getName();
		int i = fileName.lastIndexOf('.');
		if(i > 0 && i < fileName.length() - 1) {
		    return fileName.substring(0, i);
		}
		else {
		    return fileName;
		}
	}
	
	/**
	 * Calculate the size of files
	 * 
	 * @param files
	 * @return
	 */
	public static long getFileSize(File[] files) {
	    long num = 0L;
	    
	    for(File file : files) {
	        if(file.isDirectory()) {
	            num += getFileSize(file.listFiles());
	        }
	        else {
	            num += file.length();
	        }
        }
	    
	    return num;
	}
	
	public static void deleteFiles(File[] files) throws IOException {
	    for(File file : files) {
            if(file.isDirectory()) {
                deleteFolder(file);
            }
            else {
                file.delete();
            }
        }
	}
	
	/**
	 * Copy Files to target folder
	 * 
	 * @param files
	 * @param destFolder
	 * @throws IOException 
	 */
	public static void copyFiles(File[] files, File destFolder, IProgressMonitor progressMonitor) throws IOException {
	    if(!destFolder.isDirectory()) {
            throw new IOException("Parent folder should be directory");
        }
	    
	    if(progressMonitor != null) {
	        progressMonitor.setTaskName(Messages.FileUtils_0);
	    }
	    
	    for(File file : files) {
            if(file.exists()) {
                File target = new File(destFolder, file.getName());
                if(file.isDirectory()) {
                    copyFolder(file, target, progressMonitor);
                }
                else {
                    copyFile(file, target, true);
                }
            }
        }
	}
	
	/**
     * Move Files to target folder
     * 
     * @param files
     * @param destFolder
     * @throws IOException 
     */
    public static void moveFiles(File[] files, File destFolder, IProgressMonitor progressMonitor) throws IOException {
        if(!destFolder.isDirectory()) {
            throw new IOException("Parent folder should be directory");
        }
        
        if(progressMonitor != null) {
            progressMonitor.setTaskName(Messages.FileUtils_1);
        }
        
        for(File file : files) {
            if(file.exists()) {
                File target = new File(destFolder, file.getName());
                if(file.isDirectory()) {
                    moveFolder(file, target, progressMonitor);
                }
                else {
                    moveFile(file, target);
                }
            }
        }
    }
	
	/**
	 * Copy a Folder and all its files and sub-folder to target Folder which will be created if not there.
	 * @param srcFolder The Source Folder
	 * @param destFolder The Destination Folder
	 * @param progressMonitor An optional IProgressMonitor.  Can be null.
	 * @throws IOException On error or if there is a IProgressMonitor and user pressed Cancel
	 */
	public static void copyFolder(File srcFolder, File destFolder, IProgressMonitor progressMonitor) throws IOException {
	    if(srcFolder.equals(destFolder)) {
	        throw new IOException("Source and target folders cannot be the same.");
	    }
	    
        if(!srcFolder.exists()) {
            throw new IOException("Source folder does not exist");
        }

        // Check that destFolder is not a child of srcFolder
	    for(File dest = destFolder.getParentFile(); dest != null; dest = dest.getParentFile()) {
	        if(dest.equals(srcFolder)) {
	            throw new IOException("The destination folder cannot be a subfolder of the source folder.");
	        }
	    }
	        
	    destFolder.mkdirs();
	    File[] srcFiles = srcFolder.listFiles();
	    for(int i = 0; i < srcFiles.length; i++) {
	        File srcFile = srcFiles[i];
	        // If we have a Progress Monitor...
	        if(progressMonitor != null) {
	            progressMonitor.subTask(srcFile.getName());
	            if(progressMonitor.isCanceled()) {
                    throw new IOException("User cancelled.");
	            }
	        }
	        if(srcFile.isDirectory()) {
	            copyFolder(srcFile, new File(destFolder, srcFile.getName()), progressMonitor);
	        }
	        else {
	            copyFile(srcFile, new File(destFolder, srcFile.getName()), false);
	        }
	    }
	}
	
	/**
	 * Copy a Folder and all its files and sub-folders to target Folder
	 * @throws IOException
	 */
	public static void copyFolder(File srcFolder, File destFolder) throws IOException {
	    copyFolder(srcFolder, destFolder, null);
	}
	
	/**
	 * Copy a File.  The Source file must exist.
	 * @param srcFile
	 * @param destFile
	 * @param createCopy If this is true and the destination file exists, the file is copied such as "myfile(1).txt"
	 * @throws IOException
	 */
	public static void copyFile(File srcFile, File destFile, boolean createCopy) throws IOException {
	    if(createCopy) {
	        if(srcFile.equals(destFile) || destFile.exists()) {
	            int i = 1;
	            String name = getFileNameWithoutExtension(srcFile);
	            String ext = getFileExtension(srcFile);
	            do {
	                destFile = new File(destFile.getParentFile(), name + "(" + i++ + ")" + ext);
	            }
	            while(destFile.exists());
	        }
	    }
	    else {
	        if(srcFile.equals(destFile)) {
	            throw new IOException("Source and Target Files cannot be the same");
	        }
	    }
	    
	    int bufSize = 1024 * 64;
	    byte[] buf = new byte[bufSize];
	    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile), bufSize);
	    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile), bufSize);
	    int size;
	    while((size = bis.read(buf)) != -1) {
	        bos.write(buf, 0, size);
	    }
	    bos.flush();
	    bos.close();
	    bis.close();
	}
	
	/**
     * Move a Folder
     */
    public static void moveFolder(File srcFolder, File destFolder, IProgressMonitor progressMonitor) throws IOException {
        copyFolder(srcFolder, destFolder, progressMonitor);
        deleteFolder(srcFolder);
    }
	
	/**
	 * Move a File
	 */
	public static void moveFile(File srcFile, File destFile) throws IOException {
		copyFile(srcFile, destFile, false);
		srcFile.delete();
	}
	
	/**
	 * Delete a folder and its contents
	 * @param afolder -  a folder
	 */
	public static void deleteFolder(File afolder) throws IOException {
	    if(afolder == null) {
	        return;
	    }
	    
	    // Not root directories
	    // This way does not work where afolder = new File("aFolder");
	    // File parent = afolder.getParentFile();
	    File parent = new File(afolder.getAbsolutePath()).getParentFile();
	    if(parent == null) {
	        throw new IOException("Cannot delete root folder");
	    }
	    
	    if(afolder.exists() && afolder.isDirectory()) {
	        //delete content of directory:
	        File[] files = afolder.listFiles();
	        int count = files.length;
	        for(int i = 0; i < count; i++) {
	            File f = files[i];
	            if(f.isFile()) {
	                f.delete();
	            }
	            else if(f.isDirectory()) {
	                deleteFolder(f);
	            }
	        }
	        afolder.delete();
	    }
	}
	
    /**
	 * Sort a list of files into Folders first, files second
	 * @param files The array of files
	 * @return The sorted files as an array
	 */
	public static File[] sortFiles(File[] files) {
	    if(files == null || files.length == 0) {
	        return files;
	    }
	    
	    Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File f1, File f2) {
                if(f1 == null || f2 == null) {
                    return 0;
                }
                if(f1.isDirectory() && f2.isFile()) {
                    return -1;
                }
                if(f1.isFile() && f2.isDirectory()) {
                    return 1;
                }
                return f1.compareTo(f2);
            }
        });
	    
	    return files;
	}
	
	/**
	 * Get a relative path for a file given its relationship to basePath
	 */
    @SuppressWarnings("deprecation")
    public static String getRelativePath(File path, File basePath) {
        try {
            String dir = path.toURL().toExternalForm();
            String baseDir = appendSeparator(basePath.toURL().toExternalForm(), "/");
            StringBuffer result = new StringBuffer();
            while (dir.indexOf(baseDir) == -1) {
                basePath = basePath.getParentFile();
                if(basePath == null) {
                    return path.getName();
                }
                baseDir = appendSeparator(basePath.toURL().toExternalForm(), "/");
                result.append("../");
            }
            if (dir.indexOf(baseDir) == 0) {
                String delta = dir.substring(baseDir.length());
                result.append(delta);
            }
            return result.toString();
        } catch(Exception ex) {
            ex.printStackTrace();
            return path.getName();
        }
    }
	
    /**
     * Appends the platform specific path separator to the end of a path.
     * 
     * @param path
     *            a path name
     * @return the path name appended with the platform specific path separator
     */
    public static String appendSeparator(String path) {
        return appendSeparator(path, File.separator);
    }

    /**
     * Appends the given path separator to the end of a path
     * 
     * @param path
     *            a path name
     * @param separator
     *            a path separator
     * @return the path name appended with the given separator
     */
    public static String appendSeparator(String path, String separator) {
        return path.endsWith(separator) ? path : path + separator;
    }
    
    /**
     * 
     * Generate a unique file name for a file checking in folder for a unique name
     * @param folder the folder to generate the name in 
     * @param prefix the file prefix
     * @param suffix the file suffix without a dot
     * @return
     */
    public static String generateFileName(File folder, String prefix, String suffix) {
        if(fFileNameCounter == -1) {
            fFileNameCounter = new Random().nextInt() & 0xffff;
        }
        
        File tmpFile;
        
        do {
            fFileNameCounter++;
            tmpFile = new File(folder, prefix + Integer.toString(fFileNameCounter) + "." + suffix);

        } while(tmpFile.exists());
        
        return tmpFile.getName();
    }

    /**
     * Checks for valid file name, and converts illegal character to "_" character
     * @param name
     * @return The valid name
     */
    public static String getValidFileName(String name) {
        if(!StringUtils.isSet(name)) {
            return "untitled";
        }
        return name.replaceAll("[^a-zA-Z0-9]", "_");
    }
    
    /**
     * Check if a folder is empty
     * @param folder
     * @return true if the folder is empty
     */
    public static boolean isFolderEmpty(File folder) {
        if(!(folder != null && folder.exists() && folder.isDirectory())) {
            return true;
        }
        
        // A lazy stream is faster than folder.list()
        try(Stream<Path> entries = Files.list(folder.toPath())
                                        .filter(path -> !path.endsWith(".DS_Store"))) { // Ignore Mac file
            return entries.findFirst().isEmpty();
        }
        catch(IOException ex) {
            return true;
        }
    }
}

