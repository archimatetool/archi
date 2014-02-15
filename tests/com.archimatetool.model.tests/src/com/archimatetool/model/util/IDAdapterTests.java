/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.junit.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.impl.Folder;
import com.archimatetool.tests.TestUtils;


@SuppressWarnings("nls")
public class IDAdapterTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(IDAdapterTests.class);
    }
    
    
    @Test
    public void testNotifyChanged() {
        Folder folder = (Folder)IArchimateFactory.eINSTANCE.createFolder();
        assertNull(folder.getId());
        
        IDAdapter adapter = new IDAdapter();
        Notification msg = new ENotificationImpl(folder, Notification.ADD, IArchimatePackage.Literals.FOLDER_CONTAINER__FOLDERS, null, folder);
        adapter.notifyChanged(msg);
        
        assertNotNull(folder.getId());
    }

    
    @Test
    public void testRegisterID() throws Exception {
        IDAdapter adapter = new IDAdapter();
        
        String id = "someID";
        adapter.registerID(null);
        List<String> usedIDs = getPrivateIDList(adapter);
        assertTrue(usedIDs.isEmpty());
        
        adapter.registerID(id);
        usedIDs = getPrivateIDList(adapter);
        assertTrue(usedIDs.contains(id));
        
        adapter.registerID(id);
        usedIDs = getPrivateIDList(adapter);
        assertTrue(usedIDs.contains(id));
        assertEquals(1, usedIDs.size());
    }
    
    @Test
    public void testGetNewID() throws Exception {
        IDAdapter adapter = new IDAdapter();
        String id = adapter.getNewID();
        assertEquals(8, id.length());
        
        List<String> usedIDs = getPrivateIDList(adapter);
        assertTrue(usedIDs.contains(id));
    }

    @SuppressWarnings("unchecked")
    private List<String> getPrivateIDList(IDAdapter adapter) throws Exception {
        return (List<String>)TestUtils.getPrivateField(adapter, "fUsedIDs");
    }
} 
