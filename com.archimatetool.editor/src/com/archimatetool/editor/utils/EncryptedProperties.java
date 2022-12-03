/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Map.Entry;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.security.auth.DestroyFailedException;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ArchiPlugin;

/**
 * Encrypted Properties using a Password
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public final class EncryptedProperties {
    
    // Used for PBE
    private static final String PBE_ALGORITHM = "PBEwithHmacSHA256AndAES_256";
    
    // Number of iterations for PBEKeySpec and PBEParameterSpec
    private static final int PBE_ITERATIONS = 1000;

    // Salt length for PBE of primary key
    private static final int PBE_SALT_LENGTH = 8;
    
    // IV length for PBE of primary key - has to be 16
    private static final int PBE_IV_LENGTH = 16;
    
    private static final String ENCRYPTED_VALUE_PREFIX = "ENC(";
    private static final String ENCRYPTED_VALUE_SUFFIX = ")";
    
    private File fStorageFile;
    private Properties fProperties;
    
    public EncryptedProperties(File storageFile) {
        fStorageFile = storageFile;
    }
    
    private static EncryptedProperties defaultProps;
    
    public static EncryptedProperties getDefault() {
        if(defaultProps == null) {
            defaultProps = new EncryptedProperties(new File(ArchiPlugin.INSTANCE.getUserDataFolder(), "props"));
        }
        return defaultProps;
    }
    
    // ==============================================================================================
    // Password management
    // ==============================================================================================
    
    // Store password encrypted here
    private SecretKey secretKey = null;
    private byte[] encryptedPassword = null;
    
    private char[] getPassword() throws GeneralSecurityException {
        if(encryptedPassword == null) {
            char[][] password = new char[1][];
            
            // Ask user for this
            PlatformUI.getWorkbench().getDisplay().syncExec(() -> {
                InputDialog dialog = new InputDialog(null, "Enter password", "Password:", "", null) {
                    @Override
                    protected int getInputTextStyle() {
                        return super.getInputTextStyle() | SWT.PASSWORD;
                    }
                };
                
                if(dialog.open() == Window.OK) {
                    password[0] = dialog.getValue().toCharArray();
                }
            });
            
            if(password[0] != null) {
                setPassword(password[0]);
            }

            return password[0];
        }
        
        Cipher cipher = CryptoUtils.getCipher(secretKey, CryptoUtils.AES, Cipher.DECRYPT_MODE);
        byte[] bytes = cipher.doFinal(encryptedPassword);
        return CryptoUtils.convertBytesToChars(bytes);
    }
    
    private void setPassword(char[] password) throws GeneralSecurityException {
        byte[] bytes = CryptoUtils.convertCharsToBytes(password);
        secretKey = CryptoUtils.generateRandomSecretKey();
        Cipher cipher = CryptoUtils.getCipher(secretKey, CryptoUtils.AES, Cipher.ENCRYPT_MODE);
        encryptedPassword = cipher.doFinal(bytes);
    }
    
    public void changePassword(char[] oldPassword, char[] newPassword) throws GeneralSecurityException, IOException {
        if(Arrays.equals(getPassword(), oldPassword)) {
            
            // Set new encrypted values with the new password
            for(Entry<Object, Object> entry : getProperties().entrySet()) {
                if(entry.getValue() instanceof String) {
                    String value = (String)entry.getValue();
                    if(isEncryptedValue(value)) {
                        String key = (String)entry.getKey();
                        setSecureProperty(key, getSecureProperty(key), newPassword);
                    }
                }
            }
            
            // Set password
            setPassword(newPassword);
        }
    }
    
    public void clearPassword() {
        if(secretKey != null) {
            try {
                secretKey.destroy();
            }
            catch(DestroyFailedException ex) {
                ex.printStackTrace();
            }
            
            secretKey = null;
        }
        
        if(encryptedPassword != null) {
            Arrays.fill(encryptedPassword, (byte)0);
            encryptedPassword = null;
        }
    }

    
    // ==============================================================================================
    // Properties Storage
    // ==============================================================================================
    
    
    public void setProperty(String key, String value) throws IOException {
        // If value not set remove it
        if(!StringUtils.isSet(value)) {
            getProperties().remove(key);
        }
        else {
            getProperties().setProperty(key, value);
        }
        
        saveProperties();
    }
    
    public void setSecureProperty(String key, char[] value) throws GeneralSecurityException, IOException {
        setSecureProperty(key, value, getPassword());
    }
    
    private void setSecureProperty(String key, char[] value, char[] password) throws GeneralSecurityException, IOException {
        // If value not set remove it
        if(value == null || value.length == 0) {
            getProperties().remove(key);
        }
        else {
            if(password == null) {
                throw new IOException("No password");
            }
            
            // salt
            byte[] salt = CryptoUtils.generateRandomBytes(PBE_SALT_LENGTH);

            // Generate a new random iv for the Cipher
            byte[] iv = CryptoUtils.generateRandomBytes(PBE_IV_LENGTH);

            // Get chars as bytes - Must use UTF-8
            byte[] bytes = CryptoUtils.convertCharsToBytes(value);
            
            // Encrypt the bytes
            byte[] encrypted = CryptoUtils.transformWithPassword(password,
                                                                 PBE_ALGORITHM,
                                                                 Cipher.ENCRYPT_MODE,
                                                                 bytes,
                                                                 salt,
                                                                 iv,
                                                                 PBE_ITERATIONS);
            
            // Store in properties file as a Base64 encoded string
            CryptoData cd = new CryptoData(salt, iv, encrypted);
            getProperties().setProperty(key, ENCRYPTED_VALUE_PREFIX + cd.getBase64String() + ENCRYPTED_VALUE_SUFFIX); // Use Base64 because this is a string
        }
        
        saveProperties();
    }
    
    public String getProperty(String key) throws IOException {
        if(!StringUtils.isSet(key)) {
            return null;
        }
        
        String value = getProperties().getProperty(key);
        if(isEncryptedValue(value)) {
            return null;
        }
        
        return value;
    }

    public char[] getSecureProperty(String key) throws GeneralSecurityException, IOException {
        if(!StringUtils.isSet(key)) {
            return null;
        }
        
        String value = getProperties().getProperty(key);
        
        // Encrypted Base64 String
        if(isEncryptedValue(value)) {
            char[] password = getPassword();
            if(password == null) {
                throw new IOException("No password");
            }

            // Remove prefix and suffix from String
            value = getEncryptedValue(value);
            
            // Decode it
            CryptoData cd = new CryptoData(value);
            
            // Get salt
            byte[] salt = cd.getSalt();
            
            // Get iv
            byte[] iv = cd.getIV();
            
            // Get bytes
            byte[] bytes = cd.getEncryptedData();
            
            if(salt == null || iv == null || bytes == null) {
                throw new GeneralSecurityException("Could not get property for: " + key);
            }

            // Decrypt the bytes
            bytes = CryptoUtils.transformWithPassword(password,
                                                      PBE_ALGORITHM,
                                                      Cipher.DECRYPT_MODE,
                                                      bytes,
                                                      salt,
                                                      iv,
                                                      PBE_ITERATIONS);
            
            // Convert back to UTF-8 chars
            return CryptoUtils.convertBytesToChars(bytes);
        }
        
        return null;
    }
    
    public void removeProperty(String key) throws IOException {
        getProperties().remove(key);
        saveProperties();
    }
    
    public boolean hasProperty(String key) throws IOException {
        return getProperties().containsKey(key);
    }
    
    public boolean hasPropertiesFile() {
        return getPropertiesFile().exists();
    }
    
    private File getPropertiesFile() {
        return fStorageFile;
    }
    
    private Properties getProperties() throws IOException {
        if(fProperties == null) {
            fProperties = new Properties();
            
            if(hasPropertiesFile()) {
                try(FileInputStream is = new FileInputStream(getPropertiesFile())) {
                    fProperties.load(is);
                }
            }
        }
        
        return fProperties;
    }
    
    private void saveProperties() throws IOException {
        File file = getPropertiesFile();
        file.getParentFile().mkdirs(); // Ensure parent folder exists
            
        try(FileOutputStream out = new FileOutputStream(file)) {
            getProperties().store(out, null);
        }
    }
    
    private boolean isEncryptedValue(String value) {
        if(!StringUtils.isSet(value)) {
            return false;
        }
        value = value.trim();
        return value.startsWith(ENCRYPTED_VALUE_PREFIX) && value.endsWith(ENCRYPTED_VALUE_SUFFIX);
    }
    
    private String getEncryptedValue(String value) {
        return value.substring(ENCRYPTED_VALUE_PREFIX.length(), value.length() - ENCRYPTED_VALUE_SUFFIX.length());
    }
}
