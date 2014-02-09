/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.tests;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;



/**
 * Testing Utils
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class TestUtils {

    /**
     * Temporary folder
     */
    public static final String TESTS_TEMP_FOLDER = "com.archimatetool.tests.tmp";
    
    /**
     * Test Folder in Temp dir
     */
    public static File TMP_FOLDER = new File(System.getProperty("java.io.tmpdir"), TESTS_TEMP_FOLDER);
        
    /**
     * @return a Temporary Folder for testing.  Will be deleted on exit (if empty).
     */
    public static File createTempFolder(String folderName) {
        File tmp = new File(TMP_FOLDER, folderName);
        tmp.mkdirs();
        return tmp;
    }
    
    /**
     * @return a Temporary File handle for testing.  Will be deleted on exit.
     */
    public static File createTempFile(String extension) throws IOException {
        File tmp = File.createTempFile("~test", extension, getMainTempFolder());
        tmp.deleteOnExit();
        return tmp;
    }
    
    /**
     * @return The Temporary Testing Main Folder for testing.
     * It's best that Test routines that create folders delete this folder on tearDown()
     */
    public static File getMainTempFolder() {
        TMP_FOLDER.mkdirs();
        return TMP_FOLDER;
    }
    
    /**
     * @param bundleName
     * @param path
     * @return A File folder path relative to bundle
     */
    public static File getLocalBundleFolder(String bundleName, String path) {
        URL url = Platform.getBundle(bundleName).getEntry("/");
        try {
            url = FileLocator.resolve(url);
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        return new File(url.getPath(), path);
    }

    /**
     * Invoke a private Java method by reflection
     * 
     * @param obj the object the underlying method is invoked from
     * @param methodName the name of the method
     * @param parameterTypes the parameters that define the method
     * @param args The arguments to the actual method
     * @return the result of dispatching the method represented by this object on <code>obj</code> with parameters
     * @throws Exception
     */
    public static Object invokePrivateMethod(Object obj, String methodName, Class<?>[] parameterTypes, Object[] args) throws Exception {
        Method m = obj.getClass().getDeclaredMethod(methodName, parameterTypes);
        m.setAccessible(true);
        return m.invoke(obj, args);
    }

    /**
     * @param clazz The parent class
     * @param name The name of the class to find
     * @return A private member class from a parent class or null
     */
    public static Class<?> getMemberClass(Class<?> clazz, String name) {
        for(Class<?> c : clazz.getDeclaredClasses()) {
            if(c.getName().equals(name)) {
                return c;
            }
        }
        
        return null;
    }
}
