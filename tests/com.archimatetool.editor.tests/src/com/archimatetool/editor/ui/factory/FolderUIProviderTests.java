/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory;

import static org.junit.Assert.assertNull;
import junit.framework.JUnit4TestAdapter;

import org.junit.Before;

import com.archimatetool.editor.ui.factory.model.FolderUIProvider;
import com.archimatetool.model.IArchimatePackage;

public class FolderUIProviderTests extends AbstractObjectUIProviderTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(FolderUIProviderTests.class);
    }
    
    @Before
    public void runOnceBeforeAllTests() {
        provider = new FolderUIProvider();
        expectedClass = IArchimatePackage.eINSTANCE.getFolder();
    }
    
    @Override
    public void testCreateEditPart() {
        assertNull(provider.createEditPart());
    }
}
