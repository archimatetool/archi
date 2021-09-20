/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.tests;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;



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
        tmp.deleteOnExit();
        return tmp;
    }
    
    /**
     * @return a Temporary File handle for testing.  Will be deleted on exit.
     * The extension if provided must have a dot
     * The extension argument may be null, in which case the suffix ".tmp" will be used. 
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
        if(!TMP_FOLDER.exists()) {
            TMP_FOLDER.mkdirs();
            TMP_FOLDER.deleteOnExit();
        }
        
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
    
    /**
     * @param object
     * @param field
     * @return A private field in an Object
     * @throws Exception
     */
    public static Object getPrivateField(Object object, String field) throws Exception {
        Field f = getField(object.getClass(), field);
        f.setAccessible(true);
        return f.get(object);
    }
    
    // Get a private/protected field from a Class.
    // If not found, search superclasses
    private static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        try {
            return clazz.getDeclaredField(fieldName);
        }
        catch(NoSuchFieldException ex) {
            Class<?> superClass = clazz.getSuperclass();
            if(superClass == null) {
                throw ex;
            }
            else {
                return getField(superClass, fieldName);
            }
        }
    }
    
    /**
     * Close all open Editor Parts
     * @param model
     */
    public static void closeAllEditors() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if(page != null) {
            page.closeAllEditors(false);
        }
    }
    

    /*
     * We need to call this in cases where Display.getDefault() has not yet been called yet.
     * Some Eclipse methods assume that a current Display has been created.
     * Note - Creating a new Shell() at some point in the sequence of tests will call Display.getDefault() but let's be explicit here.
     */
    public static void ensureDefaultDisplay() {
        if(Display.getCurrent() == null) {
            Display.getDefault();
        }
    }
}
