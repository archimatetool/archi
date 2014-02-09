/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.archimatetool.editor.TestSupport;


@SuppressWarnings("nls")
public class ByteArrayStorageTests {
    
    private ByteArrayStorage storage;
    private File img1File, img2File;
    private long imgfileByteSize = 662299;
    
    private String entry1 = "entry1", entry2 = "entry2";
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ByteArrayStorageTests.class);
    }
    
    @Before
    public void runBeforeEachTest() {
        storage = new ByteArrayStorage();
        img1File = new File(TestSupport.getTestDataFolder(), "/img/img1.png");
        img2File = new File(TestSupport.getTestDataFolder(), "/img/img2.png");
    }
    
    // ---------------------------------------------------------------------------------------------
    // Tests
    // ---------------------------------------------------------------------------------------------

    @Test
    public void testImageFilesAreTheSameAndGetBytesFromFile() throws Exception {
        byte[] bytes1 = storage.getBytesFromFile(img1File);
        byte[] bytes2 = storage.getBytesFromFile(img1File);
        
        assertNotNull(bytes1);
        assertNotNull(bytes2);
        assertEquals(bytes1.length, bytes2.length);
        assertEquals(imgfileByteSize, bytes1.length);
    }

    @Test
    public void getInputStream() throws Exception {
        storage.addFileContentEntry(entry1, img1File);
        
        InputStream is = storage.getInputStream(entry1);
        assertTrue(is instanceof ByteArrayInputStream);
        is.close();
    }    

    @Test
    public void getKey() throws Exception {
        byte[] bytes = storage.getBytesFromFile(img1File);
        storage.addByteContentEntry(entry1, bytes);
        
        assertEquals(entry1, storage.getKey(bytes));
    }    

    @Test
    public void getEntrySize() throws Exception {
        assertEquals(-1, storage.getEntrySize(entry1));

        storage.addFileContentEntry(entry1, img1File);
        assertEquals(imgfileByteSize, storage.getEntrySize(entry1));
    }    

    @Test
    public void getEntrySet() throws Exception {
        assertEquals(0, storage.getEntrySet().size());

        storage.addFileContentEntry(entry1, img1File);
        assertEquals(1, storage.getEntrySet().size());
    }    

    @Test
    public void hasEntries() throws Exception {
        assertFalse(storage.hasEntries());

        storage.addFileContentEntry(entry1, img1File);
        assertTrue(storage.hasEntries());
    }    

    @Test
    public void hasEntry() throws Exception {
        assertFalse(storage.hasEntry(entry1));

        storage.addFileContentEntry(entry1, img1File);
        assertTrue(storage.hasEntry(entry1));
    }    
    
    @Test
    public void removeEntry() throws Exception {
        storage.addFileContentEntry(entry1, img1File);
        byte[] bytes = storage.getEntry(entry1);
        assertNotNull(bytes);
        
        storage.removeEntry(entry1);
        bytes = storage.getEntry(entry1);
        assertNull(bytes);
    }
    
    @Test
    public void addFileContentEntry() throws Exception {
        storage.addFileContentEntry(entry1, img1File);
        
        byte[] bytes = storage.getEntry(entry1);
        assertNotNull(bytes);
        assertEquals(imgfileByteSize, bytes.length);
    }

    @Test
    public void addStreamEntry() throws Exception {
        InputStream in = new FileInputStream(img1File);
        storage.addStreamEntry(entry1, in);
        
        byte[] bytes = storage.getEntry(entry1);
        assertNotNull(bytes);
        assertEquals(imgfileByteSize, bytes.length);
    }
    
    @Test
    public void addByteContentEntry() throws Exception {
        byte[] bytes1 = storage.getBytesFromFile(img1File);
        storage.addByteContentEntry(entry1, bytes1);
        
        assertEquals(bytes1, storage.getEntry(entry1));
        
        // And add the same image again but with different file name (same bytes) and with new key
        byte[] bytes2 = storage.getBytesFromFile(img2File);
        storage.addByteContentEntry(entry2, bytes2);
        
        // entry2 should reference original bytes1
        assertEquals(bytes1, storage.getEntry(entry2));
    }

    
}
