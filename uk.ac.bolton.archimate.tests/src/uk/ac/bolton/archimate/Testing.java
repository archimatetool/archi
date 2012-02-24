/*******************************************************************************
 * Copyright (c) 2010-12 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;


/**
 * Testing Utils
 * 
 * @author Phillip Beauvoir
 */
public class Testing {

    /**
     * Temporary folder
     */
    public static final String TESTS_TEMP_FOLDER = "uk.ac.bolton.tests.tmp"; //$NON-NLS-1$
    
    /**
     * Test Folder in Temp dir
     */
    public static File TMP_FOLDER = new File(System.getProperty("java.io.tmpdir"), TESTS_TEMP_FOLDER); //$NON-NLS-1$
    
    public static File TESTDATA_FOLDER = new File("testdata"); //$NON-NLS-1$
    
    public static File TEST_MODEL_FILE = new File(TESTDATA_FOLDER, "Archisurance.archimate");

    
    /**
     * @return a Temporary Folder for testing.  Will be deleted on exit (if empty).
     */
    public static File getTempFolder(String folderName) {
        File tmp = new File(TMP_FOLDER, folderName);
        tmp.mkdirs();
        return tmp;
    }
    
    /**
     * @return a Temporary File handle for testing.  Will be deleted on exit.
     */
    public static File getTempFile(String extension) throws IOException {
        File tmp = File.createTempFile("~test", extension, getMainTestFolder()); //$NON-NLS-1$
        tmp.deleteOnExit();
        return tmp;
    }
    
    /**
     * @return The Temporary Testing Main Folder for testing.
     * It's best that Test routines that create folders delete this folder on tearDown()
     */
    public static File getMainTestFolder() {
        TMP_FOLDER.mkdirs();
        return TMP_FOLDER;
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
