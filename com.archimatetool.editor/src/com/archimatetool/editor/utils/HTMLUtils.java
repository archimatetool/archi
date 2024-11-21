/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;


/**
 * HTML Utils
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class HTMLUtils {
    
    /**
     * The reg expression for HTML links
     */
    public static final String HTML_LINK_REGEX = "(http|https|ftp|file)://\\S+";
    
    /**
     * The compiled pattern to match HTML links
     */
    public static final Pattern HTML_LINK_PATTERN = Pattern.compile(HTML_LINK_REGEX);
    
    /**
     * The reg expression for HTML tags
     */
    public static final String HTML_TAG_REGEX = "<[^>]+>";

    /** 
     * The compiled pattern to match HTML tags
     */
    public static final Pattern HTML_TAG_REGEX_PATTERN = Pattern.compile(HTML_TAG_REGEX);
    
    /**
     * Strip tags out of a String
     */
    public static String stripTags(String str) {
        if (str == null || str.indexOf('<') == -1 || str.indexOf('>') == -1) {
            return str;
        }
        
        str = HTML_TAG_REGEX_PATTERN.matcher(str).replaceAll("");
        return str;
    }

    /**
     * Open a link in a Browser
     */
    public static void openLinkInBrowser(String href) throws PartInitException, MalformedURLException {
        IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
        IWebBrowser browser = support.getExternalBrowser();
        try {
            browser.openURL(new URI(href.replaceAll(" ", "%20")).toURL());
        }
        catch(URISyntaxException ex) {
            throw new MalformedURLException(ex.toString());
        }
    }
}
