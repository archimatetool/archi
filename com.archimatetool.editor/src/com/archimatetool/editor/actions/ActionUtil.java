/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import java.util.Objects;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.ui.IWorkbenchWindow;

/**
 * Some utils for Actions and creating dynamic menus based on registered extension points
 * 
 * @author Phillip Beauvoir
 * @since 5.7
 */
@SuppressWarnings("nls")
public class ActionUtil {
    
    public static final String EXTENSIONPOINT_EXPORT_HANDLER = "com.archimatetool.editor.exportHandler";
    public static final String EXTENSIONPOINT_IMPORT_HANDLER = "com.archimatetool.editor.importHandler";
    
    public static final String EXTENSION_IMPORT_HANDLER = "importHandler";
    public static final String EXTENSION_IMPORT_HANDLER2 = "importHandler2";
    
    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_LABEL = "label";
    private static final String ATTRIBUTE_CLASS = "class";
    
    /**
     * Add any contributed export model menu items to the main menu
     */
    public static void addExportModelExtensionMenuItems(IWorkbenchWindow window, IMenuManager menu) {
        for(IConfigurationElement configurationElement : Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSIONPOINT_EXPORT_HANDLER)) {
            String id = configurationElement.getAttribute(ATTRIBUTE_ID);
            String label = configurationElement.getAttribute(ATTRIBUTE_LABEL);
            if(id != null && label != null) {
                menu.add(new ExportModelAction(window, id, label));
            } 
        }
    }

    /**
     * Add any contributed import model menu items to the main menu
     */
    public static void addImportModelExtensionMenuItems(IWorkbenchWindow window, IMenuManager menu) {
        for(IConfigurationElement configurationElement : Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSIONPOINT_IMPORT_HANDLER)) {
            String id = configurationElement.getAttribute(ATTRIBUTE_ID);
            String label = configurationElement.getAttribute(ATTRIBUTE_LABEL);
            if(id != null && label != null) {
                String extensionName = configurationElement.getName();
                // Import model
                if(EXTENSION_IMPORT_HANDLER.equals(extensionName)) {
                    menu.add(new ImportModelAction(window, id, label));
                }
                // Import into model
                else if(EXTENSION_IMPORT_HANDLER2.equals(extensionName)) {
                    menu.add(new ImportIntoModelAction(window, id, label));
                } 
            }
        }
    }

    /**
     * Create an instance of an extension point's extension class that will be invoked in an Action
     * This should really live in another util class for handling extensions but will do here for now
     */
    public static Object createExtensionPointInstance(String extensionPointId, String extensionId) throws CoreException {
        for(IConfigurationElement configurationElement : Platform.getExtensionRegistry().getConfigurationElementsFor(extensionPointId)) {
            if(Objects.equals(configurationElement.getAttribute(ATTRIBUTE_ID), extensionId)) {
                return configurationElement.createExecutableExtension(ATTRIBUTE_CLASS);
            }
        }
        
        return null;
    }
}
