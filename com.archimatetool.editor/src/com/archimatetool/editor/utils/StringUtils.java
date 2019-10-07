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
@SuppressWarnings("nls")
public final class StringUtils {
    
    /**
     * Empty String
     */
    public final static String ZERO_LENGTH_STRING = "";

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
        return input != null ? input : ZERO_LENGTH_STRING;
    }
    
    /**
     * Checks that a String contains some content
     * 
     * @param input
     * @return
     */
    public static boolean isSet(String input) {
        return input != null && input.length() > 0;
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
        
        return id.matches("[a-zA-Z_][\\w-_.]*");
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
        return id.replaceAll("[^a-zA-Z0-9-]", "_");
    }
    
    /**
     * In some controls (Label and CLabel) single ampersands don't show up
     * @param text
     * @return The escaped text
     */
    public static String escapeAmpersandsInText(String text) {
        if(isSet(text)) {
            return text.replace("&", "&&");
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
            return text.replaceAll("(\r\n|\r|\n)", replace);
        }
        return text;
    }
    
    /**
     * Remove all newline and cr characters from a String and replace all instances with a single space
     * @param text
     * @return The new text
     */
    public static String normaliseNewLineCharacters(String text) {
        if(isSet(text)) {
            return text.replaceAll("(\r\n|\r|\n)+", " ");
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
     * @return -1 if newer < older <br/>
     *          0 if newer == older <br/>
     *          1 if newer > older
     */
    public static int compareVersionNumbers(String newer, String older) {
        return Integer.compare(versionNumberAsInt(newer), versionNumberAsInt(older));
    }
    
    /**
     * Convert a version number to an integer
     * @param version
     * @return integer
     */
    public static int versionNumberAsInt(String version) {
        if(!isSet(version)) {
            return 0;
        }
        
        String[] vals = version.split("\\.");
        
        try {
            if(vals.length == 1) {
                return Integer.parseInt(vals[0]);
            }
            if(vals.length == 2) {
                return (Integer.parseInt(vals[0]) << 16) + (Integer.parseInt(vals[1]) << 8);
            }
            if(vals.length == 3) {
                return (Integer.parseInt(vals[0]) << 16) + (Integer.parseInt(vals[1]) << 8) + Integer.parseInt(vals[2]);
            }
        }
        catch(NumberFormatException ex) {
        }
        
        return 0;
    }
}