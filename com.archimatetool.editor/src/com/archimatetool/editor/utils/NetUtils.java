/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.utils;

import java.io.IOException;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.net.proxy.IProxyData;
import org.eclipse.core.net.proxy.IProxyService;
import org.eclipse.ui.PlatformUI;

/**
 * Net Connection Utilities
 * 
 * Taken from org.eclipse.help.internal.base.util.ProxyUtil
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public final class NetUtils  {
    
    public static void initialise() {
        // This needs to be set in order to avoid this exception when using a Proxy:
        // "Unable to tunnel through proxy. Proxy returns "HTTP/1.1 407 Proxy Authentication Required""
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
        
        // And this one too, but not sure. I think it's for HTTP
        System.setProperty("jdk.http.auth.proxying.disabledSchemes", "");
    }

    public static URLConnection openConnection(URL url) throws IOException {
        URLConnection connection = null;
        
        IProxyData proxyData = getProxyData(url);
        
        // Note - ProxyManager initialises an Authenticator so we have to set ours after ProxyManager has been initialised
        
        // No proxyData, so normal connection
        if(proxyData == null) {
            // Set Authenticator to default
            Authenticator.setDefault(null);
            connection = url.openConnection();
        }
        // Using a Proxy
        else {
            if(proxyData.isRequiresAuthentication()) {
                // Use this Authenticator
                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(proxyData.getUserId(), proxyData.getPassword().toCharArray());
                    }
                });
            }
            else {
                // Set Authenticator to default
                Authenticator.setDefault(null);
            }
            
            Proxy proxy = getProxy(proxyData);
            connection = url.openConnection(proxy);
        }

        return connection;
    }
    
    public static Proxy getProxy(IProxyData proxyData) {
        InetSocketAddress socketAddress = new InetSocketAddress(proxyData.getHost(), proxyData.getPort());
        return new Proxy(getProxyType(proxyData), socketAddress);
    }
    
    public static Type getProxyType(IProxyData proxyData) {
        return proxyData.getType() == IProxyData.SOCKS_PROXY_TYPE ? Type.SOCKS : Type.HTTP;
    }
    
    public static IProxyService getProxyService() {
        // We can get the ProxyManager directly
        // return org.eclipse.core.internal.net.ProxyManager.getProxyManager();
        
        // Or by using an EclipseContextFactory
        // BundleContext context = ArchiPlugin.INSTANCE.getBundle().getBundleContext();
        // return EclipseContextFactory.getServiceContext(context).get(IProxyService.class);
        
        // Or from the workbench. This will return null if the workbench is not running (JUnit tests)
        return PlatformUI.isWorkbenchRunning() ? PlatformUI.getWorkbench().getService(IProxyService.class) : null;
    }
    
    public static IProxyData getProxyData(URI uri) {
        IProxyService service = getProxyService();
        
        if(service == null) {
            return null;
        }

        if(!service.isProxiesEnabled()) {
            return null;
        }

        // Not sure if we need this
        // IProxyService.getNonProxiedHosts() is checked anyway, but not ProxyManager.getNativeNonProxiedHosts()
        if(shouldBypass(uri)) {
            return null;
        }
        
        IProxyData[] data = service.select(uri);

        if(data.length == 0) {
            return null;
        }
        
        return data[0];
    }
    
    public static IProxyData getProxyData(URL url) {
        try {
            return getProxyData(url.toURI());
        }
        catch(URISyntaxException ex) {
            ex.printStackTrace();
            return null;
        }
    }
	
    public static boolean shouldBypass(URI uri) {
        String host = uri.getHost();
        if(host == null) {
            return true;
        }

        List<String> hosts = getProxyBypassHosts();
        if(hosts.contains(host)) {
            return true;
        }
        
        return false;
    }

    @SuppressWarnings("restriction")
    public static List<String> getProxyBypassHosts() {
        List<String> hosts = new ArrayList<>();

        IProxyService service = getProxyService();

        Collections.addAll(hosts, service.getNonProxiedHosts());
        
        if(service instanceof org.eclipse.core.internal.net.ProxyManager) {
            String[] natives = ((org.eclipse.core.internal.net.ProxyManager)service).getNativeNonProxiedHosts();
            if(natives != null) {
                Collections.addAll(hosts, natives);
            }
        }
        
        return hosts;
    }
}

