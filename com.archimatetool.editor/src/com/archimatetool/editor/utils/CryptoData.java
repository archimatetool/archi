/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.utils;

import java.util.Base64;

/**
 * Represents encrypted data, salt and inialization vector 
 * These can be encoded to and decoded from a Base64 String
 * 
 * @author Phillip Beauvoir
 */
public class CryptoData {
    // Separates salt and iv from the data. These must not be valid Base64 characters
    private static final char SALT_SEPARATOR = ',';
    private static final char IV_SEPARATOR = ';';
    
    private final byte[] salt;
    private final byte[] iv;
    private final byte[] encryptedData;

    /**
     * Take salt, iv and encryptedData
     * @param salt optional salt
     * @param iv optional iv
     * @param encryptedData
     */
    public CryptoData(byte[] salt, byte[] iv, byte[] encryptedData) {
        this.salt = salt;
        this.iv = iv;
        this.encryptedData = encryptedData;
    }
    
    /**
     * Take Base64 encoded string and get salt, iv and data bytes
     * salt, iv areoptional
     * @param encodedString
     */
    public CryptoData(String encodedString) {
        int saltPos = encodedString.indexOf(SALT_SEPARATOR);
        int ivPos = encodedString.indexOf(IV_SEPARATOR);
        
        if(saltPos != -1) {
            salt = Base64.getDecoder().decode(encodedString.substring(0, saltPos));
        }
        else { // no salt
            salt = null;
        }
        
        if(ivPos != -1) {
            iv = Base64.getDecoder().decode(encodedString.substring(saltPos + 1, ivPos));
        }
        else { // No iv
            iv = null;
            ivPos = saltPos;
        }
        
        encryptedData = Base64.getDecoder().decode(encodedString.substring(ivPos + 1));
    }
    
    public byte[] getSalt() {
        return salt;
    }
    
    public byte[] getIV() {
        return iv;
    }
    
    public byte[] getEncryptedData() {
        return encryptedData;
    }
    
    public String getBase64String() {
        // Encode to Base64 and concatanate to one string
        StringBuilder encryptedText = new StringBuilder();
        
        // salt
        if(salt != null) {
            encryptedText.append(Base64.getEncoder().encodeToString(salt));
            encryptedText.append(SALT_SEPARATOR);
        }
        
        // iv (non-AES algorithms don't use it)
        if(iv != null) {
            encryptedText.append(Base64.getEncoder().encodeToString(iv));
            encryptedText.append(IV_SEPARATOR);
        }
        
        // data
        encryptedText.append(Base64.getEncoder().encodeToString(encryptedData));
        
        return encryptedText.toString();
    }
}