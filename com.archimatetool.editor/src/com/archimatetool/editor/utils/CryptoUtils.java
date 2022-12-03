/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * CryptoUtils
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class CryptoUtils {
    
    /**
     * Algorithms for PBE
     */
    public static String[] PBE_ALGORITHMS = {
            "PBEwithHmacSHA256AndAES_256",
            "PBEWithHmacSHA1AndAES_256",
            "PBEWithHmacSHA512AndAES_128",
            "PBEWithHmacSHA1AndAES_128",
            "PBEWithHmacSHA384AndAES_256",
            "PBEWithMD5AndDES"
    };
    
    /**
     * Algorithms for PBK
     */
    public static String[] PBK_ALGORITHMS = {
            "PBKDF2WithHmacSHA1",
            "PBKDF2WithHmacSHA224",
            "PBKDF2WithHmacSHA256",
            "PBKDF2WithHmacSHA384",
            "PBKDF2WithHmacSHA512"
    };

    
    /**
     * Cipher transformations
     */
    public static final String AES = "AES";                       // Weakest, no iv is used
    public static final String AES_CBC = "AES/CBC/PKCS5Padding";  // Better 
    public static final String AES_GCM = "AES/GCM/NoPadding";     // Strongest
    
    /**
     * IV lengths
     */
    public static final int CBC_IV_LENGTH = 16;  // IV length for AES_CBC
    public static final int GCM_IV_LENGTH = 12;  // IV length for AES_GCM
    
    
    /**
     * Convert char array to byte array without using intermediate String
     * chars are converted to UTF-8
     */
    public static byte[] convertCharsToBytes(char[] chars) {
        ByteBuffer buf = Charset.forName("UTF-8").encode(CharBuffer.wrap(chars.clone()));
        byte[] bytes = new byte[buf.remaining()];
        buf.get(bytes);
        Arrays.fill(buf.array(), (byte) 0); // clear ByteBuffer
        return bytes;
    }

    /**
     * Convert byte array to char array without using intermediate String
     * chars are converted to UTF-8
     */
    public static char[] convertBytesToChars(byte[] bytes) {
        ByteBuffer buf = ByteBuffer.wrap(bytes.clone());
        CharBuffer charBuffer = Charset.forName("UTF-8").decode(buf);
        char[] charArray = new char[charBuffer.remaining()];
        charBuffer.get(charArray);  
        Arrays.fill(buf.array(), (byte) 0); // clear ByteBuffer
        return charArray;
    }

    /**
     * Encode a char array to a Base64 encoded char array
     * the chars are converted to UTF-8
     */
    public static char[] encodeCharsToBase64(char[] chars) {
        byte[] bytes = convertCharsToBytes(chars); // convert to bytes using UTF-8
        String encoded = Base64.getEncoder().encodeToString(bytes); // Encode bytes to Base64 String
        return encoded.toCharArray(); // Return string chars
    }
    
    /**
     * Generate random bytes for salt or iv
     */
    public static byte[] generateRandomBytes(int length) {
        byte[] salt = new byte[length];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }

    /**
     * Generate a random SecretKey
     */
    public static SecretKey generateRandomSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256, SecureRandom.getInstanceStrong());
        return keyGenerator.generateKey();
    }
    
    /**
     * Generate a SecretKey from a password using a PBE based algorithm
     */
    public static SecretKey generateKeyFromPassword(char[] password, String algorithm) throws GeneralSecurityException {
        // Convert the password bytes to Base64 characters because PBEKey class will not accept non-Ascii characters in a password
        char[] encodedPassword = encodeCharsToBase64(password);

        PBEKeySpec keySpec = new PBEKeySpec(encodedPassword);
        
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
        return keyFactory.generateSecret(keySpec);
    }
    
    /**
     * Generate a SecretKey from a password using a PBK based algorithm
     */
    public static SecretKey generateKeyFromPassword(char[] password, String algorithm, byte[] salt, int iterations) throws GeneralSecurityException {
        // Convert the password bytes to Base64 characters because PBEKey class will not accept non-Ascii characters in a password
        char[] encodedPassword = encodeCharsToBase64(password);
        
        PBEKeySpec pbeKeySpec = new PBEKeySpec(encodedPassword, salt, iterations, 256);
        
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
        SecretKey pbeKey = keyFactory.generateSecret(pbeKeySpec);
        
        // Wrap the pbeKey in a SecretKeySpec
        return new SecretKeySpec(pbeKey.getEncoded(), "AES");
    }

    /**
     * Encrypt/ Decrypt bytes with a password using a PBE based algorithm
     * 
     * @param password Password 
     * @param pbeAlgorithm The PBE algorithm to use
     * @param mode Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE
     * @param bytes The bytes to encrypt/decypt
     * @param salt Salt (required)
     * @param iv IV (optional)
     * @param iterations Iterations for the PBEParameterSpec
     * @return The encrypted or decrypted bytes
     */
    public static byte[] transformWithPassword(char[] password, String pbeAlgorithm, int mode, byte[] bytes, byte[] salt, byte[] iv,
                                               int iterations) throws GeneralSecurityException {
        // Generate a PBE key for the Cipher
        SecretKey secretKey = generateKeyFromPassword(password, pbeAlgorithm);

        // Encrypt the input with the Cipher
        Cipher keyCipher = Cipher.getInstance(pbeAlgorithm);
        
        // IvParameterSpec is optional depending on the algorithm
        IvParameterSpec paramSpec = iv != null ? new IvParameterSpec(iv) : null;
        
        // Create parameters from the salt and the iterations (paramSpec will be null if iv is null)
        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, iterations, paramSpec);
        
        // Set the cipher mode to decryption or encryption
        keyCipher.init(mode, secretKey, pbeParamSpec);
        
        // Encypt/Decrypt the bytes
        return keyCipher.doFinal(bytes);
    }

    /**
     * Encrypt/ Decrypt bytes with a password using a PBK based algorithm
     * 
     * @param password Password 
     * @param pbkAlgorithm  The PBK algorithm to use
     * @param cipherAlgorithm The Cipher algorithm to use - AES or AES/CBC/PKCS5Padding or AES/GCM/NoPadding
     * @param mode Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE
     * @param bytes The bytes to encrypt/decypt
     * @param salt Salt (required)
     * @param iv IV (required)
     * @param iterations Iterations for the PBEKeySpec
     * @return The encrypted or decrypted bytes
     */
    public static byte[] transformWithPassword2(char[] password, String pbkAlgorithm, String cipherAlgorithm, int mode, byte[] bytes, byte[] salt, byte[] iv,
                                                int iterations) throws GeneralSecurityException {
        // Generate a key with the PBK Algorithm
        SecretKey secretKey = generateKeyFromPassword(password, pbkAlgorithm, salt, iterations);

        // Get the cipher from the key and cipher algorithm
        Cipher keyCipher = getCipher(secretKey, cipherAlgorithm, mode, iv);
        
        // Encypt/Decrypt the bytes
        return keyCipher.doFinal(bytes);
    }
    
    /**
     * Encrypt/ Decrypt bytes with a SecretKey 
     * 
     * @param key The secret key to use for the Cipher
     * @param cipherAlgorithm The Cipher algorithm to use - AES or AES/CBC/PKCS5Padding or AES/GCM/NoPadding
     * @param mode Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE
     * @param bytes The bytes to encrypt/decypt
     * @param iv IV (optional)
     * @return The encrypted or decrypted bytes
     * @throws GeneralSecurityException
     */
    public static byte[] transformWithKey(SecretKey key, String cipherAlgorithm, int mode, byte[] bytes, byte[] iv) throws GeneralSecurityException {
        Cipher cipher = getCipher(key, cipherAlgorithm, mode, iv);
        return cipher.doFinal(bytes);
    }

    /**
     * Get and initialise Cipher with a secret key and optional iv
     * param @iv is optional and not used for AES
     * 
     * @param key The secret key 
     * @param algorithm The transformation algorithm to use - AES, AES/CBC/PKCS5Padding or AES/GCM/NoPadding
     * @param mode Cipher.ENCRYPT_MODE or Cipher.DECRYPT_MODE
     * @param iv Optional IV
     */
    public static Cipher getCipher(SecretKey key, String algorithm, int mode, byte... iv) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(algorithm);
        
        switch(algorithm) {
            case AES:
                cipher.init(mode, key);
                break;

            case AES_CBC:
                cipher.init(mode, key, new IvParameterSpec(iv));
                break;

            case AES_GCM:
                cipher.init(mode, key, new GCMParameterSpec(128, iv));
                break;
        }
        
        return cipher;
    }

    /**
     * Detect PBE algorithms which require initialization vector
     */
    public static boolean usesIV(String algorithm) {
        return algorithm.contains("AES");
    }
    
    /**
     * Print available algorithms
     */
    public static void printAlgorithms() {
        for(Provider provider : Security.getProviders()) {
            System.out.println(provider.getName());
            for(String key : provider.stringPropertyNames()) {
                System.out.println("\t" + key + "\t" + provider.getProperty(key));
            }
        }
    }

}
