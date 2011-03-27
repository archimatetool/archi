/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.utils;

import java.util.regex.Pattern;


/**
 * HTML Utils
 * 
 * @author Phillip Beauvoir
 */
public class HTMLUtils {
    
    // Previous versions
    // "(http|https|ftp)://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?"    // Original
    // "(http|https|ftp)://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%~&=]*)?"   // Added ~
    // "(http|https|ftp)://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%~&=]*)?"    // Removed space
    
    /**
     * The reg expression for HTML links
     */
    public static final String HTML_LINK_REGEX = "(http|https|ftp)://([\\w-]+\\.)+[\\w-]+(/[\\w-./?%~&=\\(\\)]*)?";  // added \\( and \\)
    
    /**
     * The compiled pattern to match HTML links
     */
    public static final Pattern HTML_LINK_PATTERN = Pattern.compile(HTML_LINK_REGEX);
    
    /**
     * The reg expression for HTML tags
     */
    public static final String HTML_TAG_REGEX = "<[^>]+>"; //$NON-NLS-1$

    /** 
     * The compiled pattern to match HTML tags
     */
    public static final Pattern HTML_TAG_REGEX_PATTERN = Pattern.compile(HTML_TAG_REGEX);
    
    /**
     * Strip tags out of a String
     * @param str
     * @return
     */
    public static String stripTags(String str) {
        if (str == null || str.indexOf('<') == -1 || str.indexOf('>') == -1) {
            return str;
        }
        
        str = HTML_TAG_REGEX_PATTERN.matcher(str).replaceAll(""); //$NON-NLS-1$
        return str;
    }


    
}
