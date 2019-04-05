/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;


/**
 * Testing Support
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class TestSupport {
    
    public static File testFolder = getLocalBundleFolder("org.opengroup.archimate.xmlexchange.tests", "testdata");
    
    public static File xmlFile1 = new File(testFolder, "sample1.xml");
    public static File xmlFile2 = new File(testFolder, "archisurance.xml");
    
    public static File archiFile1 = new File(testFolder, "archisurance.archimate");
    
    
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
    
}
