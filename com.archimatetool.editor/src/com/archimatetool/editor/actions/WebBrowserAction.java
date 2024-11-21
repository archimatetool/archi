/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import java.net.URI;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;


/**
 * Web Action
 * 
 * @author Phillip Beauvoir
 */
public class WebBrowserAction extends Action {
    
    private String fUrl;
    
    public WebBrowserAction(String title, String url) {
        super(title);
        fUrl = url;
    }

    @Override
    public void run() {
        IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
        try {
            IWebBrowser browser = support.getExternalBrowser();
            if(browser != null) {
                browser.openURL(new URI(fUrl).toURL());
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
