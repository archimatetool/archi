/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

import com.archimatetool.editor.Application;



/**
 * Check for New Action
 * 
 * @author Phillip Beauvoir
 */
public class CheckForNewVersionAction extends Action {
    
    public CheckForNewVersionAction() {
        super(Messages.CheckForNewVersionAction_0);
    }

    @Override
    public void run() {
        URL url = null;
        HttpURLConnection connection = null;

        try {
            url = new URL("http://www.archimatetool.com/download/version.txt"); //$NON-NLS-1$
            connection = (HttpURLConnection)url.openConnection();
            connection.connect();
            InputStream is = connection.getInputStream();
            char[] buf = new char[32];
            Reader r = new InputStreamReader(is, "UTF-8"); //$NON-NLS-1$
            StringBuilder s = new StringBuilder();
            while(true) {
                int n = r.read(buf);
                if(n < 0) {
                    break;
                }
                s.append(buf, 0, n);
            }
            
            String newVersion = s.toString();
            
            // Get this app's main version number
            String thisVersion = System.getProperty(Application.APPLICATION_BUILDID);
            thisVersion = thisVersion.substring(0, 5);
            
            if(newVersion.compareTo(thisVersion) > 0) {
                boolean reply = MessageDialog.openQuestion(null, Messages.CheckForNewVersionAction_1,
                        Messages.CheckForNewVersionAction_2 +
                        " (" + newVersion + "). " + //$NON-NLS-1$ //$NON-NLS-2$
                        Messages.CheckForNewVersionAction_3);
                
                if(reply) {
                    IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
                    IWebBrowser browser = support.getExternalBrowser();
                    if(browser != null) {
                        URL url2 = new URL("http://www.archimatetool.com/download.html"); //$NON-NLS-1$
                        browser.openURL(url2);
                    }
                }
            }
            else {
                MessageDialog.openInformation(null, Messages.CheckForNewVersionAction_1, Messages.CheckForNewVersionAction_4);
            }
        }
        catch(MalformedURLException ex) {
            ex.printStackTrace();
        }
        catch(IOException ex) {
            ex.printStackTrace();
            showErrorMessage(Messages.CheckForNewVersionAction_5);
            return;
        }
        catch(PartInitException ex) {
            ex.printStackTrace();
        }

    };
    
    private void showErrorMessage(String message) {
        MessageDialog.openError(null, Messages.CheckForNewVersionAction_6, message);
    }
}
