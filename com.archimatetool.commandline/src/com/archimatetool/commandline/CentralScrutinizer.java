/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.commandline;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.WorkbenchCleaner;
import com.archimatetool.editor.utils.StringUtils;




/**
 * The main application class for standalone operation.
 * 
 * @author Phillip Beauvoir
 */
public class CentralScrutinizer implements IApplication {

    public CentralScrutinizer() {
    }
    
    // Provider Info representing extensions declared in plugin.xml extension point
    private record ProviderInfo(String id, String name, String description) {}

    // Registed CLI Providers
    private Map<ICommandLineProvider, ProviderInfo> providers;
    
    @Override
    public Object start(IApplicationContext context) throws Exception {
        try {
            // Register providers
            registerProviders();
            
            // Process options
            CommandLine commandLine = processOptions();
            
            // Show help if set and exit
            if(commandLine.hasOption("help")) { //$NON-NLS-1$
                showHelp();
                if(commandLine.hasOption("pause")) { //$NON-NLS-1$
                    pause();
                }
                return EXIT_OK;
            }
            
            // Run providers
            return runProviders(commandLine);
        }
        // Clean the workbench config area on exit
        finally {
            WorkbenchCleaner.cleanConfigOnExit(false);
        }
    }
    
    @Override
    public void stop() {
        // do nothing
    }

    // Collect registered command line providers
    private void registerProviders() {
        providers = new LinkedHashMap<ICommandLineProvider, ProviderInfo>(); // LinkedHashMap for initial ordering by id
        
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        
        for(IConfigurationElement configurationElement : registry.getConfigurationElementsFor(ICommandLineProvider.EXTENSION_ID)) {
            try {
                String id = configurationElement.getAttribute("id"); //$NON-NLS-1$
                String name = configurationElement.getAttribute("name"); //$NON-NLS-1$
                String description = configurationElement.getAttribute("description"); //$NON-NLS-1$
                ICommandLineProvider provider = (ICommandLineProvider)configurationElement.createExecutableExtension("class"); //$NON-NLS-1$
                
                if(id != null && provider != null) {
                    ProviderInfo info = new ProviderInfo(id, StringUtils.isSet(name) ? name : id, StringUtils.safeString(description));
                    providers.put(provider, info);
                }
            } 
            catch(CoreException ex) {
                ex.printStackTrace();
            } 
        }
    }
    
    // Create a CommandLine from the application arguments
    private CommandLine processOptions() throws ParseException {
        // Get core options
        Options options = getCoreOptions();
        
        // Add provider options
        options.addOptions(getProviderOptions());
        
        // Filter out any non-valid arguments
        List<String> args = new ArrayList<>();
        boolean nextArgument = false;
        
        for(String arg : Platform.getApplicationArgs()) {
            if(options.hasOption(arg) || nextArgument) {
                args.add(arg);
            }
            nextArgument = options.hasOption(arg) && options.getOption(arg).hasArg();
        }
        
        // Parse options
        CommandLineParser parser = new DefaultParser();
        return parser.parse(options, args.toArray(new String[args.size()]), false);
    }
    
    private Options getCoreOptions() {
        Options options = new Options();
        options.addOption("h", "help", false, Messages.CentralScrutinizer_0); //$NON-NLS-1$ //$NON-NLS-2$
        options.addOption("a", "abortOnException", false, Messages.CentralScrutinizer_1); //$NON-NLS-1$ //$NON-NLS-2$
        options.addOption("p", "pause", false, Messages.CentralScrutinizer_6); //$NON-NLS-1$ //$NON-NLS-2$
        return options;
    }
    
    private Options getProviderOptions() {
        Options options = new Options();
        for(ICommandLineProvider provider : providers.keySet()) {
            Options providerOptions = provider.getOptions();
            if(providerOptions != null) {
                options.addOptions(providerOptions);
            }
        }
        return options;
    }
    
    // Run providers command line
    private int runProviders(CommandLine commandLine) {
        // Ensure Current Display is initialised by simply calling this
        Display.getDefault(); 
        
        // Sort providers by priority
        List<ICommandLineProvider> sortedProviders = new ArrayList<>(providers.keySet());
        sortedProviders.sort((ICommandLineProvider p1, ICommandLineProvider p2) -> {
            return p1.getPriority() - p2.getPriority();
        });
        
        // Invoke providers' run() method
        for(ICommandLineProvider provider : sortedProviders) {
            try {
                provider.run(commandLine);
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
        
        if(commandLine.hasOption("pause")) { //$NON-NLS-1$
            pause();
        }
        
        return EXIT_OK;
    }
    
    private void showHelp() {
        final int width = 140;
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out, true); // Don't close the PrintWriter as this will also close System.out!
        
        System.out.println(Messages.CentralScrutinizer_2);
        System.out.println();

        System.out.println(Messages.CentralScrutinizer_3);
        System.out.println("---------------"); //$NON-NLS-1$
        formatter.printOptions(pw, width, getCoreOptions(), 1, 10);

        System.out.println();
        System.out.println(Messages.CentralScrutinizer_4);
        System.out.println("---------------------"); //$NON-NLS-1$

        // Sort providers by name
        List<ProviderInfo> infos = new ArrayList<>(providers.values());
        infos.sort((ProviderInfo info1, ProviderInfo info2) -> {
            return info1.name.compareToIgnoreCase(info2.name);
        });

        for(ProviderInfo info : infos) {
            System.out.println(" [" + info.name() + "] " + info.description());  //$NON-NLS-1$//$NON-NLS-2$
        }

        System.out.println();
        System.out.println(Messages.CentralScrutinizer_5);
        System.out.println("--------"); //$NON-NLS-1$
        formatter.printOptions(pw, width, getProviderOptions(), 1, 10);
    }

    private void pause() {
        System.out.println();
        System.out.println(Messages.CentralScrutinizer_7);
        
        try {
            System.in.read();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
}
