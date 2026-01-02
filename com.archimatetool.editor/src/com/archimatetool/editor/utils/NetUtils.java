/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.utils;

import java.io.IOException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.Logger;
import com.archimatetool.editor.preferences.IPreferenceConstants;

/**
 * Net Connection Utilities
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public final class NetUtils  {
    
    // Store the default ProxySelector before we set ours
    private static final ProxySelector DEFAULT_PROXY_SELECTOR = ProxySelector.getDefault();

    private static Authenticator AUTHENTICATOR = new Authenticator() {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            // If proxy request, return its credentials
            // Otherwise the requested URL is the endpoint (and not the proxy host)
            if(getRequestorType() == RequestorType.PROXY) {
                // Get credentials from secure storage
                // This is the Archi node for all secure entries. We could clear it with coArchiNode.removeNode()
                ISecurePreferences archiNode = SecurePreferencesFactory.getDefault().node(ArchiPlugin.PLUGIN_ID);
                
                try {
                    String userName = archiNode.get(IPreferenceConstants.PREFS_PROXY_USERNAME, "");
                    String pw = archiNode.get(IPreferenceConstants.PREFS_PROXY_PASSWORD, "");
                    return new PasswordAuthentication(userName, pw.toCharArray());
                }
                catch(StorageException ex) {
                    Logger.error("Could not get secure storage", ex);
                }
            }
            
            // Not a proxy request
            return null;
        }
    };
    
    public static void initialise() {
        // This needs to be set in order to avoid this exception when using a Proxy:
        // "Unable to tunnel through proxy. Proxy returns "HTTP/1.1 407 Proxy Authentication Required""
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
        
        // And this one too, but not sure. I think it's for HTTP
        System.setProperty("jdk.http.auth.proxying.disabledSchemes", "");
        
        // Set global timeouts. These might or might not work.
        System.setProperty("sun.net.client.defaultConnectTimeout", Integer.toString(getNetworkTimeout()));
        System.setProperty("sun.net.client.defaultReadTimeout", Integer.toString(getNetworkTimeout()));
        
        // The default for this is 20, but if the password for the proxy is wrong for example that can take too long (over a minute)
        System.setProperty("http.maxRedirects", "5");

        // Set default proxy now
        setDefaultProxy();
        
        // On preference change setup again
        ArchiPlugin.getInstance().getPreferenceStore().addPropertyChangeListener(event -> {
            if(event.getProperty() == IPreferenceConstants.PREFS_PROXY_ENABLED || 
                                        event.getProperty() == IPreferenceConstants.PREFS_PROXY_REQUIRES_AUTHENTICATION ||
                                        event.getProperty() == IPreferenceConstants.PREFS_PROXY_HOST || 
                                        event.getProperty() == IPreferenceConstants.PREFS_PROXY_PORT) {
                setDefaultProxy();
            }
        });
    }

    /**
     * Open a URL Connection with the given URL
     * This is really meant to be used with HTTP and HTTPS protocols but will work with the FILE protocol
     */
    public static URLConnection openConnection(URL url) throws IOException {
        URLConnection connection = null;
        
        if(isProxyEnabled()) {
            connection = url.openConnection(getHTTPProxy());
            if(connection instanceof HttpURLConnection httpConnection) {
                httpConnection.setAuthenticator(AUTHENTICATOR);
            }
        }
        else {
            connection = url.openConnection();
        }
        
        // Set timeouts, these will work even if the global properties don't
        connection.setConnectTimeout(getNetworkTimeout());
        connection.setReadTimeout(getNetworkTimeout());

        return connection;
    }
    
    public static void setDefaultProxy() {
        // If not enabled set back to default ProxySelector
        if(!isProxyEnabled()) {
            ProxySelector.setDefault(DEFAULT_PROXY_SELECTOR); // Restore this
            return;
        }

        // Authentication is used
        if(isProxyAuthenticated()) {
            // Set our Authenticator
            
            // However, once this is set Java caches the authentication details and it is never called again
            // So setting it back to null makes no difference
            // See https://bugs.openjdk.org/browse/JDK-4679480
            // See https://www.eclipse.org/forums/index.php?t=msg&th=1085879&goto=1816302&#msg_1816302
            Authenticator.setDefault(AUTHENTICATOR);
        }
        
        // Our default ProxySelector
        ProxySelector.setDefault(new ProxySelector() {
            @Override
            public List<Proxy> select(URI uri) {
                return Arrays.asList(getHTTPProxy());
            }

            @Override
            public void connectFailed(URI uri, SocketAddress sa, IOException ex) {
                Logger.error("Proxy connect failed", ex);
            }
        });
    }
    
    /**
     * Whether we are using a proxy as set in preferences
     */
    public static boolean isProxyEnabled() {
        return ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.PREFS_PROXY_ENABLED);
    }
    
    /**
     * Whether we are using authentication for the proxy as set in preferences
     */
    public static boolean isProxyAuthenticated() {
        return ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.PREFS_PROXY_REQUIRES_AUTHENTICATION);
    }

    /**
     * Return the Proxy Host as set in preferences
     */
    public static String getProxyHost() {
        return ArchiPlugin.getInstance().getPreferenceStore().getString(IPreferenceConstants.PREFS_PROXY_HOST);
    }

    /**
     * Return the Proxy Port as set in preferences
     */
    public static int getProxyPort() {
        return ArchiPlugin.getInstance().getPreferenceStore().getInt(IPreferenceConstants.PREFS_PROXY_PORT);
    }
    
    /**
     * Return the network timeout in milliseconds
     */
    public static int getNetworkTimeout() {
        return ArchiPlugin.getInstance().getPreferenceStore().getInt(IPreferenceConstants.PREFS_NETWORK_TIMEOUT);
    }

    private static Proxy getHTTPProxy() {
        InetSocketAddress socketAddress = new InetSocketAddress(getProxyHost(), getProxyPort());
        return new Proxy(Type.HTTP, socketAddress);
    }
}

