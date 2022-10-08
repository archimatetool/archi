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
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.Proxy.Type;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.utils.StringUtils;



/**
 * Check for New Action
 * 
 * @author Phillip Beauvoir
 */
public class CheckForNewVersionAction extends Action {
    
    public CheckForNewVersionAction() {
        super(Messages.CheckForNewVersionAction_0);
    }
    
    String getOnlineVersion(URL url) throws IOException {
        URLConnection connection = url.openConnection();
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
        
        is.close();
        r.close();
        
        return s.toString();
    }

    /**
     * Get the connection through a Proxy
     * Should we ever implement Proxy support in Archi this is the pattern to use
     * 
     * coArchi sets Authenticator.setDefault(a) and resets it with Authenticator.setDefault(null)
     * but this does not clear Java's cached authentication once Authenticator.setDefault(a) has been called
     * so we need to explicitly set one on the connection here
     */
    @SuppressWarnings("unused")
    private URLConnection getProxyConnection(URL url) throws IOException {
        InetSocketAddress socketAddress = new InetSocketAddress("1.1.1.1", 1234); //$NON-NLS-1$
        Proxy proxy = new Proxy(Type.HTTP, socketAddress);
        
        HttpURLConnection connection = (HttpURLConnection)url.openConnection(proxy);
        
        connection.setAuthenticator(new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("username", "password".toCharArray()); //$NON-NLS-1$ //$NON-NLS-2$
            }
        });
        
        return connection;
    }
    
    @Override
    public void run() {
        try {
            String versionFile = ArchiPlugin.PREFERENCES.getString(IPreferenceConstants.UPDATE_URL);
            
            if(!StringUtils.isSet(versionFile)) {
                return;
            }
            
            URL url = new URL(versionFile);
            String newVersion = getOnlineVersion(url);
            
            // Get this app's main version number
            String thisVersion = ArchiPlugin.INSTANCE.getVersion();
            
            if(StringUtils.compareVersionNumbers(newVersion, thisVersion) > 0) {
                String downloadURL = ArchiPlugin.PREFERENCES.getString(IPreferenceConstants.DOWNLOAD_URL);
                
                // No download URL
                if(!StringUtils.isSet(downloadURL)) {
                    MessageDialog.openInformation(null, Messages.CheckForNewVersionAction_1,
                            Messages.CheckForNewVersionAction_2 + " (" + newVersion + "). "); //$NON-NLS-1$ //$NON-NLS-2$
                    return;
                }

                // Does have download URL
                boolean reply = MessageDialog.openQuestion(null, Messages.CheckForNewVersionAction_1,
                        Messages.CheckForNewVersionAction_2 + " (" + newVersion + "). " + //$NON-NLS-1$ //$NON-NLS-2$
                                Messages.CheckForNewVersionAction_3);

                if(reply) {
                    IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
                    IWebBrowser browser = support.getExternalBrowser();
                    if(browser != null) {
                        URL url2 = new URL(downloadURL);
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
            showErrorMessage(Messages.CheckForNewVersionAction_5 + " " + ex.getMessage()); //$NON-NLS-1$
            return;
        }
        catch(PartInitException ex) {
            ex.printStackTrace();
        }

    };
    
    @Override
    public boolean isEnabled() {
        String versionFile = ArchiPlugin.PREFERENCES.getString(IPreferenceConstants.UPDATE_URL);
        return StringUtils.isSet(versionFile);
    }
    
    private void showErrorMessage(String message) {
        MessageDialog.openError(null, Messages.CheckForNewVersionAction_6, message);
    }
}
