/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.commandline;

import java.io.PrintWriter;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import com.archimatetool.editor.utils.StringUtils;




/**
 * The main application class for standalone operation.
 * 
 * @author Phillip Beauvoir
 */
public class CentralScrutinizer implements IApplication {

    /**
     * Constructor
     */
    public CentralScrutinizer() {
    }
    
    private class ProviderInfo {
        String id;
        String name;
        String description;
        
        public ProviderInfo(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }
    }

    private Map<ICommandLineProvider, ProviderInfo> providers;
    
    public Object start(IApplicationContext context) throws Exception {
        // Register providers
        registerProviders();
        
        // Process options
        CommandLine commandLine = processOptions();
        
        // Show help if set and exit
        if(commandLine.hasOption("help")) { //$NON-NLS-1$
            showHelp();
            return EXIT_OK;
        }
        
        // Run provider options
        return runProviderOptions(commandLine);
    }
    
    // Collect registered command line providers
    private void registerProviders() {
        // Sort the providers by priority...
        Comparator<ICommandLineProvider> comparator = (ICommandLineProvider p1, ICommandLineProvider p2) -> {
            int result = p1.getPriority() - p2.getPriority();
            return result == 0 ? p1.getPriority() : result; // Can't have duplicate comparison value
        };
        
        providers = new TreeMap<ICommandLineProvider, ProviderInfo>(comparator);
        
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        
        for(IConfigurationElement configurationElement : registry.getConfigurationElementsFor(ICommandLineProvider.EXTENSION_ID)) {
            try {
                String id = configurationElement.getAttribute("id"); //$NON-NLS-1$
                String name = configurationElement.getAttribute("name"); //$NON-NLS-1$
                String description = configurationElement.getAttribute("description"); //$NON-NLS-1$
                ICommandLineProvider provider = (ICommandLineProvider)configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
                
                if(id != null && provider != null) {
                    ProviderInfo info = new ProviderInfo(id, name, description);
                    providers.put(provider, info);
                }
            } 
            catch(CoreException ex) {
                ex.printStackTrace();
            } 
        }
    }
    
    private CommandLine processOptions() throws ParseException {
        // Get core options
        Options options = getCoreOptions();
        
        // Add provider options
        for(ICommandLineProvider provider : providers.keySet()) {
            Options providerOptions = provider.getOptions();
            
            if(providerOptions != null) {
                for(Option option : providerOptions.getOptions()) {
                    options.addOption(option);
                }
            }
        }
        
        // Parse options
        CommandLineParser parser = new DefaultParser();
        return parser.parse(options, Platform.getApplicationArgs(), false);
    }
    
    private Options getCoreOptions() {
        Options options = new Options();
        
        options.addOption("h", "help", false, Messages.CentralScrutinizer_0); //$NON-NLS-1$ //$NON-NLS-2$
        options.addOption("a", "abortOnException", false, Messages.CentralScrutinizer_1); //$NON-NLS-1$ //$NON-NLS-2$
        
        return options;
    }
    
    // Run providers' options
    @SuppressWarnings("deprecation")
    private int runProviderOptions(CommandLine commandLine) {
        // Invoke providers' run() method
        for(ICommandLineProvider provider : providers.keySet()) {
            try {
                // Use the old method if it's used by the provider
                try {
                    provider.getClass().getDeclaredMethod("run", String[].class); //$NON-NLS-1$
                    provider.run(Platform.getApplicationArgs()); // Backward compatibility
                }
                // Nope, so use new method
                catch(NoSuchMethodException | SecurityException ex) {
                    provider.run(commandLine);
                }
            }
            catch(Exception ex) {
                ex.printStackTrace();
                
                if(commandLine.hasOption("abortOnException")) { //$NON-NLS-1$
                    return -1;
                }
                else {
                    // Consume?
                }
            }
        }
        
        return EXIT_OK;
    }
    
    private void showHelp() {
        HelpFormatter formatter = new HelpFormatter();
        //formatter.setOptionComparator(null);

        int width = 140;
        
        PrintWriter pw = new PrintWriter(System.out);
        
        System.out.println(Messages.CentralScrutinizer_2);
        
        System.out.println();
        
        System.out.println(Messages.CentralScrutinizer_3);
        System.out.println("---------------"); //$NON-NLS-1$
        formatter.printOptions(pw, width, getCoreOptions(), 1, 10);
        pw.flush();
        
        System.out.println();
        
        System.out.println(Messages.CentralScrutinizer_4);
        System.out.println("---------------------"); //$NON-NLS-1$
        
        for(Entry<ICommandLineProvider, ProviderInfo> info : providers.entrySet()) {
            String pluginName = info.getValue().name;
            if(!StringUtils.isSet(pluginName)) {
                pluginName = info.getValue().id;
            }
            
            String pluginDescription = StringUtils.safeString(info.getValue().description);

            System.out.println(" [" + pluginName + "] " + pluginDescription);  //$NON-NLS-1$//$NON-NLS-2$
        }

        System.out.println();
        System.out.println(Messages.CentralScrutinizer_5);
        System.out.println("--------"); //$NON-NLS-1$
        
        Options allOptions = new Options();
        
        for(ICommandLineProvider provider : providers.keySet()) {
            for(Option option : provider.getOptions().getOptions()) {
                allOptions.addOption(option);
            }
        }
        
        formatter.printOptions(pw, width, allOptions, 0, 10);
        pw.flush();
    }
    
    public void stop() {
    }

}
