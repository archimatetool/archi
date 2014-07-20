/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.utils;




/**
 * Some useful String Utilities
 *
 * @author Phillip Beauvoir
 */
public final class StringUtils {
    
    /**
     * Empty String
     */
    public final static String ZERO_LENGTH_STRING = ""; //$NON-NLS-1$

    /**
     * Ensures that a string is not null. Converts null strings into empty
     * strings, and leaves any other string unmodified. Use this to help
     * wrap calls to methods that return null instead of the empty string.
     * Can also help protect against implementation errors in methods that
     * are not supposed to return null. 
     * 
     * @param input input string (may be null)
     * @return input if not null, or the empty string if input is null
     */
    public static String safeString(String input) {
        if(input != null) {
            return input;
        }

        return ZERO_LENGTH_STRING;
    }
    
    /**
     * Checks that a String contains some content
     * 
     * @param input
     * @return
     */
    public static boolean isSet(String input) {
        return input != null && !ZERO_LENGTH_STRING.equals(input);
    }
    
    /**
     * Checks that a String contains some content after calling trim()
     * 
     * @param input
     * @return
     */
    public static boolean isSetAfterTrim(String input) {
        return isSet(input) && input.trim().length() > 0;
    }
    
    /**
     * @param s
     * @return A String consisting of x amount of the same character s
     *         If amount is zero or less, the empty String is returned.
     */
    public static String getStringCharacters(String s, int amount) {
        if(amount < 1) {
            return ""; //$NON-NLS-1$
        }
        
        String ret = s;
        
        for(int i = 1; i < amount; i++) {
            ret += s;
        }
        
        return ret;
    }
    
    /**
     * @param id
     * @return If ID is a valid XML ID or IDREF
     */
    public static boolean isValidID(String id) {
        if(!isSet(id)) {
            return false;
        }
        
        return id.matches("[a-zA-Z_][\\w-_.]*"); //$NON-NLS-1$
    }
    
    /**
     * Convert all illegal characters to "_"
     * @param id
     * @return The converted string
     */
    public static String getValidID(String id) {
        if(!StringUtils.isSet(id)) {
            return id;
        }
        return id.replaceAll("[^a-zA-Z0-9-]", "_"); //$NON-NLS-1$ //$NON-NLS-2$
    }
    
    /**
     * In some controls (Label and CLabel) single ampersands don't show up
     * @param text
     * @return The escaped text
     */
    public static String escapeAmpersandsInText(String text) {
        if(isSet(text)) {
            return text.replace("&", "&&");  //$NON-NLS-1$//$NON-NLS-2$
        }
        return text;
    }
    
    /**
     * Replace all newline and cr characters from a String with given string
     * @param text
     * @Param replace the string to replace with
     * @return The new text
     */
    public static String replaceNewLineCharacters(String text, String replace) {
        if(isSet(text)) {
            return text.replaceAll("(\r\n|\r|\n)", replace);  //$NON-NLS-1$
        }
        return text;
    }
    
    /**
     * Compare two version numbers with the format 1, 1.1, or 1.1.1
     * 
     * From http://stackoverflow.com/questions/6701948/efficient-way-to-compare-version-strings-in-java
     * 
     * @param newer The version string considered to be newer
     * @param older The version string considered to be older
     * @return -1 if newer is less than older <br/>
     *         0 if newer is equal to older <br/>
     *         1 if newer is greater than older
     */
    public static int compareVersionNumbers(String newer, String older) {
        if(!isSetAfterTrim(newer)) {
            newer = "0"; //$NON-NLS-1$
        }
        if(!isSetAfterTrim(older)) {
            older = "0"; //$NON-NLS-1$
        }
        
        String[] vals1 = newer.split("\\."); //$NON-NLS-1$
        String[] vals2 = older.split("\\."); //$NON-NLS-1$
        
        int i = 0;
        while(i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i])) {
            i++;
        }

        if(i < vals1.length && i < vals2.length) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }

        return Integer.signum(vals1.length - vals2.length);
    }
}