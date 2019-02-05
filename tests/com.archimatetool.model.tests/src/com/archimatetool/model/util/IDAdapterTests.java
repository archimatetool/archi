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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.junit.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.impl.Folder;

import junit.framework.JUnit4TestAdapter;


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
        assertTrue(adapter.usedIds.isEmpty());
        
        adapter.registerID(id);
        assertTrue(adapter.usedIds.contains(id));
        
        adapter.registerID(id);
        assertTrue(adapter.usedIds.contains(id));
        assertEquals(1, adapter.usedIds.size());
    }
    
    @Test
    public void testGetNewID() throws Exception {
        IDAdapter adapter = new IDAdapter();
        String id = adapter.getNewID();
        assertEquals(36, id.length());
        
        assertTrue(adapter.usedIds.contains(id));
    }
} 
