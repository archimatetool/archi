/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.ui.services;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.Logger;
import uk.ac.bolton.archimate.editor.diagram.DiagramEditorInput;
import uk.ac.bolton.archimate.editor.diagram.DiagramEditorFactoryExtensionHandler;
import uk.ac.bolton.archimate.editor.diagram.IArchimateDiagramEditor;
import uk.ac.bolton.archimate.editor.diagram.IDiagramEditorFactory;
import uk.ac.bolton.archimate.editor.diagram.IDiagramModelEditor;
import uk.ac.bolton.archimate.editor.diagram.sketch.ISketchEditor;
import uk.ac.bolton.archimate.model.IArchimateDiagramModel;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.ISketchModel;


/**
 * Editor Manager
 * 
 * @author Phillip Beauvoir
 */
public class EditorManager {
    
    /**
     * Open an Editor
     * 
     * @param input
     * @param editorID
     */
    public static IEditorPart openEditor(IEditorInput input, String editorID) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        try {
            return page.openEditor(input, editorID);
        }
        catch(PartInitException ex) {
            Logger.logError("Could not open Editor " + editorID); //$NON-NLS-1$
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Open the Diagram Editor for a given DiagramModel Model
     * @param name
     */
    public static IDiagramModelEditor openDiagramEditor(IDiagramModel model) {
        if(model == null || model.eContainer() == null) {
            return null;
        }
        
        String id = null;
        IEditorInput editorInput = null;
        
        if(model instanceof IArchimateDiagramModel) {
            id = IArchimateDiagramEditor.ID;
            editorInput = new DiagramEditorInput(model);
        }
        else if(model instanceof ISketchModel) {
            id = ISketchEditor.ID;
            editorInput = new DiagramEditorInput(model);
        }
        else {
            IDiagramEditorFactory factory = DiagramEditorFactoryExtensionHandler.INSTANCE.getFactory(model);
            if(factory != null) {
                id = factory.getEditorID();
                editorInput = factory.createEditorInput(model);
            }
        }

        if(id == null || editorInput == null) {
            throw new RuntimeException("Unsupported model type");
        }
        
        IEditorPart part = openEditor(editorInput, id);
        
        // Check it actually is IDiagramModelEditor, it could be an org.eclipse.ui.internal.ErrorEditorPart if an error occurs
        return part instanceof IDiagramModelEditor ? (IDiagramModelEditor)part : null;
    }
    
    /**
     * Close open Diagram Editor Part for a model
     * @param model
     */
    public static void closeDiagramEditor(IDiagramModel diagramModel) {
        if(diagramModel == null) {
            return;
        }
        
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        for(IEditorReference ref : page.getEditorReferences()) {
            try {
                IEditorInput input = ref.getEditorInput();
                if(input instanceof DiagramEditorInput && ((DiagramEditorInput)input).getDiagramModel() == diagramModel) {
                    page.closeEditors(new IEditorReference[] {ref}, false);
                }
            }
            catch(PartInitException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    /**
     * Close open Diagram Editor Parts for a model
     * @param model
     */
    public static void closeDiagramEditors(IArchimateModel model) {
        if(model == null) {
            return;
        }
        
        List<IEditorReference> list = new ArrayList<IEditorReference>();
        
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        for(IEditorReference ref : page.getEditorReferences()) {
            try {
                IEditorInput input = ref.getEditorInput();
                if(input instanceof DiagramEditorInput && ((DiagramEditorInput)input).getDiagramModel().getArchimateModel() == model) {
                    list.add(ref);
                }
            }
            catch(PartInitException ex) {
                ex.printStackTrace();
            }
        }
        
        if(!list.isEmpty()) {
            IEditorReference[] refs = list.toArray(new IEditorReference[list.size()]);
            page.closeEditors(refs, false);
        }
    }
}
