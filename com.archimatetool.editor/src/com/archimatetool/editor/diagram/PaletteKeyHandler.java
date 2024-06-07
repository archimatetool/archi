/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IParameter;
import org.eclipse.core.commands.IParameterValues;
import org.eclipse.core.commands.Parameterization;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.jface.bindings.Binding;
import org.eclipse.jface.bindings.TriggerSequence;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.keys.IBindingService;

import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.util.ArchimateModelUtils;

/**
 * Handle Palette Key commands
 * 
 * @author Phillip Beauvoir
 */
public class PaletteKeyHandler extends AbstractHandler implements IParameterValues {
    
    public static final String COMMAND_ID = "com.archimatetool.editor.palette.command"; //$NON-NLS-1$
    public static final String PARAMETER_ID = "com.archimatetool.editor.palette.command.params"; //$NON-NLS-1$
    public static final String KEY_BINDING = "keyBinding"; //$NON-NLS-1$
    
    private static Map<String, String> paramValues;
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        String parameterValue = event.getParameter(PARAMETER_ID);
        
        if(parameterValue != null && HandlerUtil.getActiveEditor(event) instanceof IDiagramModelEditor editor) {
            PaletteViewer paletteViewer = editor.getGraphicalViewer().getEditDomain().getPaletteViewer();

            for(Object rootEntry : paletteViewer.getPaletteRoot().getChildren()) {
                if(rootEntry instanceof PaletteContainer container) {
                    for(Object childEntry : container.getChildren()) {
                        if(childEntry instanceof ToolEntry toolEntry) {
                            String keyName = (String)toolEntry.getToolProperty(KEY_BINDING);
                            if(parameterValue.equals(keyName)) {
                                if(toolEntry != paletteViewer.getActiveTool()) {
                                    paletteViewer.setActiveTool(toolEntry);
                                }
                                return null;
                            }
                        }
                    }
                }
            }
        }
        
        return null;
    }

    @Override
    public Map<String, String> getParameterValues() {
        if(paramValues == null) {
            paramValues = new HashMap<>();
            
            // ArchiMate elements
            for(EClass eClass : ArchimateModelUtils.getAllArchimateClasses()) {
                paramValues.put(ArchiLabelProvider.INSTANCE.getDefaultName(eClass), eClass.getName());
            }
            
            // ArchiMate relations
            for(EClass eClass : ArchimateModelUtils.getRelationsClasses()) {
                paramValues.put(ArchiLabelProvider.INSTANCE.getDefaultName(eClass), eClass.getName());
            }
            
            // Connectors
            for(EClass eClass : ArchimateModelUtils.getConnectorClasses()) {
                paramValues.put(ArchiLabelProvider.INSTANCE.getDefaultName(eClass), eClass.getName());
            }
            
            // Magic Connector
            paramValues.put(Messages.ArchimateDiagramEditorPalette_14, "MagicConnector"); //$NON-NLS-1$
            
            // Note
            paramValues.put(ArchiLabelProvider.INSTANCE.getDefaultName(IArchimatePackage.eINSTANCE.getDiagramModelNote()),
                    IArchimatePackage.eINSTANCE.getDiagramModelNote().getName());
            
            // Group
            paramValues.put(ArchiLabelProvider.INSTANCE.getDefaultName(IArchimatePackage.eINSTANCE.getDiagramModelGroup()),
                    IArchimatePackage.eINSTANCE.getDiagramModelGroup().getName());
            
            // Connection
            paramValues.put(ArchiLabelProvider.INSTANCE.getDefaultName(IArchimatePackage.eINSTANCE.getDiagramModelConnection()),
                    IArchimatePackage.eINSTANCE.getDiagramModelConnection().getName());
        }
        
        return paramValues;
    }
    
    /**
     * Set the key binding and the accelerator text, if any, for a ToolEntry
     */
    public static void setKeyBinding(ToolEntry toolEntry, String parameterValue) {
        toolEntry.setToolProperty(KEY_BINDING, parameterValue);
        String acceleratorText = getAcceleratorText(parameterValue);
        if(acceleratorText != null) {
            toolEntry.setDescription(StringUtils.safeString(toolEntry.getDescription()) + " [" + acceleratorText + "]"); //$NON-NLS-1$ //$NON-NLS-2$
        }
    }

    /**
     * @return Shortcut keys, if any, as text, or null
     */
    private static String getAcceleratorText(String parameterValue) {
        if(parameterValue == null || !PlatformUI.isWorkbenchRunning()) {
            return null;
        }
        
        Command command = PlatformUI.getWorkbench().getService(ICommandService.class).getCommand(COMMAND_ID);
        if(command == null) {
            return null;
        }

        IParameter parameter = null;
        try {
            parameter = command.getParameter(PARAMETER_ID);
        }
        catch(NotDefinedException ex) {
            ex.printStackTrace();
        }
        
        if(parameter == null) {
            return null;
        }

        ParameterizedCommand parameterizedCommand = new ParameterizedCommand(command,
                new Parameterization[] { new Parameterization(parameter, parameterValue) });
        
        IBindingService bindingService = PlatformUI.getWorkbench().getService(IBindingService.class);
        
        TriggerSequence ts = null;
        
        /*
         * If the workbench is still loading at this point, the "org.eclipse.ui.contexts.dialogAndWindow" context is the only active context
         * and a keybinding that uses a different context will not be found (the default context is "org.eclipse.ui.contexts.window").
         * This is a workaround to find the binding and its trigger sequence by brute force if only one context is active.
         */
        Collection<?> contexts = PlatformUI.getWorkbench().getService(IContextService.class).getActiveContextIds();
        if(contexts != null && contexts.size() == 1) {
            for(Binding binding : bindingService.getBindings()) {
                if(parameterizedCommand.equals(binding.getParameterizedCommand())) {
                    ts = binding.getTriggerSequence();
                    break;
                }
            }
        }
        else {
            ts = bindingService.getBestActiveBindingFor(parameterizedCommand);
        }
        
        return ts != null ? ts.format() : null;
    }
}
