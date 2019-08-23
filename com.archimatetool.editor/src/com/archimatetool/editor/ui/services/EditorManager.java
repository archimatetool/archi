/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.services;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.Logger;
import com.archimatetool.editor.diagram.DiagramEditorFactoryExtensionHandler;
import com.archimatetool.editor.diagram.DiagramEditorInput;
import com.archimatetool.editor.diagram.IArchimateDiagramEditor;
import com.archimatetool.editor.diagram.IDiagramEditorFactory;
import com.archimatetool.editor.diagram.IDiagramModelEditor;
import com.archimatetool.editor.diagram.sketch.ISketchEditor;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.ISketchModel;



/**
 * Editor Manager
 * 
 * @author Phillip Beauvoir
 */
public class EditorManager {
    
    /**
     * Open an Editor and activate if required
     * 
     * @param input
     * @param editorID
     * @param activate
     * @return
     */
    public static IEditorPart openEditor(IEditorInput input, String editorID, boolean activate) {
        if(!PlatformUI.isWorkbenchRunning()) {
            return null;
        }
        
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        try {
            return page.openEditor(input, editorID, activate);
        }
        catch(PartInitException ex) {
            Logger.logError("Could not open Editor " + editorID); //$NON-NLS-1$
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Open an Editor and activate will be true
     * @param input
     * @param editorID
     * @return
     */
    public static IEditorPart openEditor(IEditorInput input, String editorID) {
        return openEditor(input, editorID, true);
    }

    /**
     * Open the Diagram Editor for a given DiagramModel Model
     * Activate is true
     * @param name
     */
    public static IDiagramModelEditor openDiagramEditor(IDiagramModel model) {
        return openDiagramEditor(model, true);
    }   
    
    /**
     * Open the Diagram Editor for a given DiagramModel Model
     * @param name
     */
    public static IDiagramModelEditor openDiagramEditor(IDiagramModel model, boolean activate) {
        if(model == null || model.eContainer() == null || !PlatformUI.isWorkbenchRunning()) {
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
            throw new RuntimeException("Unsupported model type"); //$NON-NLS-1$
        }
        
        IEditorPart part = openEditor(editorInput, id, activate);
        
        // Check it actually is IDiagramModelEditor, it could be an org.eclipse.ui.internal.ErrorEditorPart if an error occurs
        return part instanceof IDiagramModelEditor ? (IDiagramModelEditor)part : null;
    }
    
    /**
     * Close open Diagram Editor Part for a model
     * @param model
     */
    public static void closeDiagramEditor(IDiagramModel diagramModel) {
        if(diagramModel == null || !PlatformUI.isWorkbenchRunning()) {
            return;
        }
        
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        page.closeEditors(getDiagramEditorReferences(diagramModel), false);
    }
    
    /**
     * Close open Diagram Editor Parts for a model
     * @param model
     */
    public static void closeDiagramEditors(IArchimateModel model) {
        if(model == null || !PlatformUI.isWorkbenchRunning()) {
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
    
    /**
     * @param diagramModel
     * @return All editor references that have diagramModel in their editor input
     */
    public static IEditorReference[] getDiagramEditorReferences(IDiagramModel diagramModel) {
        List<IEditorReference> list = new ArrayList<IEditorReference>();
        
        if(diagramModel != null && PlatformUI.isWorkbenchRunning()) {
            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            for(IEditorReference ref : page.getEditorReferences()) {
                try {
                    IEditorInput input = ref.getEditorInput();
                    if(input instanceof DiagramEditorInput && ((DiagramEditorInput)input).getDiagramModel() == diagramModel) {
                        list.add(ref);
                    }
                }
                catch(PartInitException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        return list.toArray(new IEditorReference[list.size()]);
    }
}
