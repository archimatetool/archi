/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Byte Array Storage Unit
 * 
 * @author Phillip Beauvoir
 */
public class ByteArrayStorage {
    
    private Map<String, byte[]> fdataTable = new HashMap<String, byte[]>();
    
    InputStream getInputStream(String entryName) {
        if(entryName != null) {
            byte[] bytes = fdataTable.get(entryName);
            if(bytes != null) {
                return new ByteArrayInputStream(bytes);
            }
        }
        return null;
    }

    String getKey(byte[] bytes) {
        for(Entry<String, byte[]> entry : fdataTable.entrySet()) {
            byte[] entryBytes = entry.getValue();
            if(Arrays.equals(bytes, entryBytes)) {
                return entry.getKey();
            }
        }
        
        return null;
    }
    
    long getEntrySize(String entryName) {
        if(entryName != null) {
            byte[] bytes = fdataTable.get(entryName);
            if(bytes != null) {
                return bytes.length;
            }
        }
        return -1;
    }
    
    Set<Entry<String, byte[]>> getEntrySet() {
        return Set.copyOf(fdataTable.entrySet());
    }
    
    Set<String> getEntryNames() {
        return Set.copyOf(fdataTable.keySet());
    }

    boolean hasEntries() {
        return !fdataTable.isEmpty();
    }
    
    boolean hasEntry(String entryName) {
        return fdataTable.containsKey(entryName);
    }
    
    void removeEntry(String entryName) {
        fdataTable.remove(entryName);
    }
    
    byte[] getEntry(String entryName) {
        return fdataTable.get(entryName);
    }
    
    void addFileContentEntry(String entryName, File file) throws IOException {
        addStreamEntry(entryName, new FileInputStream(file));
    }
    
    void addStreamEntry(String entryName, InputStream in) throws IOException {
        byte[] bytes = getBytesFromStream(in);
        addByteContentEntry(entryName, bytes);
    }

    void addByteContentEntry(String entryName, byte[] bytes) {
        // Check if we have these bytes already. If we do then re-reference them
        // We might be adding the same set of bytes but from a different file
        String key = getKey(bytes);
        
        // Yes we have them, so re-use the bytes
        if(key != null) {
            fdataTable.put(entryName, getEntry(key));
        }
        // No, so add the bytes
        else {
            fdataTable.put(entryName, bytes);
        }
    }
    
    byte[] getBytesFromFile(File file) throws IOException {
        // Get the bytes from the file
        if(file != null && file.exists()) {
            InputStream in = new FileInputStream(file);
            return getBytesFromStream(in);
        }
        return null;
    }
    
    void dispose() {
        fdataTable.clear();
        fdataTable = null;
    }
    
    /**
     * Read in a stream and return its contents as a byte array
     */
    private byte[] getBytesFromStream(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        try {
            int size;
            while((size = in.read(buf)) != -1) {
                out.write(buf, 0, size);
            }
        }
        finally {
            out.close();
            in.close();
        }
        
        return out.toByteArray();
    }
}
