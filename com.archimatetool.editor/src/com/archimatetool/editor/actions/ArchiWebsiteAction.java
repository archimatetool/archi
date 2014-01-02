/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import java.net.URL;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;


/**
 * Archi on the Web Action
 * 
 * @author Phillip Beauvoir
 */
public class ArchiWebsiteAction extends Action {
    
    public ArchiWebsiteAction() {
        super(Messages.ArchiWebsiteAction_0);
    }

    @Override
    public void run() {
        IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
        try {
            IWebBrowser browser = support.getExternalBrowser();
            if(browser != null) {
                URL url = new URL("http://www.archimatetool.com"); //$NON-NLS-1$
                browser.openURL(url);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
