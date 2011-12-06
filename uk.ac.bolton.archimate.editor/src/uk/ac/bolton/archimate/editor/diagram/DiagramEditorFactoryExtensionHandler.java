/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import uk.ac.bolton.archimate.model.IDiagramModel;


/**
 * Manages Diagram Extensions
 * 
 * @author Phillip Beauvoir
 */
public class DiagramEditorFactoryExtensionHandler {
    
    public static String EXTENSIONPOINT = "uk.ac.bolton.archimate.editor.diagramEditorFactory";
    
    public static DiagramEditorFactoryExtensionHandler INSTANCE = new DiagramEditorFactoryExtensionHandler();
    
    private List<IDiagramEditorFactory> factories = new ArrayList<IDiagramEditorFactory>();
    
    private DiagramEditorFactoryExtensionHandler() {
        registerFactories();
    }
    
    public List<IDiagramEditorFactory> getRegisteredFactories() {
        return factories;
    }
    
    public IDiagramEditorFactory getFactory(IDiagramModel model) {
        for(IDiagramEditorFactory factory : factories) {
            if(factory.isFactoryFor(model)) {
                return factory;
            }
        }
        
        return null;
    }

    private void registerFactories() {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        for(IConfigurationElement configurationElement : registry.getConfigurationElementsFor(EXTENSIONPOINT)) {
            try {
                String id = configurationElement.getAttribute("id");
                IDiagramEditorFactory factory = (IDiagramEditorFactory)configurationElement.createExecutableExtension("class");
                if(id != null && factory != null) {
                    factories.add(factory);
                }
            } 
            catch(CoreException ex) {
                ex.printStackTrace();
            } 
        }
    }
}
