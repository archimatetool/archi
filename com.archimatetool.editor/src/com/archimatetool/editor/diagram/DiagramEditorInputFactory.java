/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import java.io.File;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;

import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;



/**
 * Factory for restoring the state of Diagram Editor windows
 * 
 * @author Phillip Beauvoir
 */
public class DiagramEditorInputFactory implements IElementFactory {

    /**
     * Factory id. The workbench plug-in registers a factory by this name
     * with the "org.eclipse.ui.elementFactories" extension point.
     */
    public static final String ID_FACTORY = "uk.ac.bolton.archimate.editor.DiagramEditorInputFactory"; //$NON-NLS-1$
    
    /**
     * Tag for the diagram model's id and file
     */
    public static final String TAG_VIEW_ID = "view_id"; //$NON-NLS-1$
    public static final String TAG_VIEW_FILE = "file"; //$NON-NLS-1$
    public static final String TAG_VIEW_NAME = "name"; //$NON-NLS-1$

    @Override
    public IAdaptable createElement(IMemento memento) {
        String viewID = memento.getString(TAG_VIEW_ID);
        String fileName = memento.getString(TAG_VIEW_FILE);
        String viewName = memento.getString(TAG_VIEW_NAME);

        if(viewID != null && fileName != null) {
            File file = new File(fileName);
            for(IArchimateModel model : IEditorModelManager.INSTANCE.getModels()) {
                if(file.equals(model.getFile())) {
                    for(IDiagramModel diagramModel : model.getDiagramModels()) {
                        if(viewID.equals(diagramModel.getId())) {
                            return new DiagramEditorInput(diagramModel);
                        }
                    }
                }
            }
        }
        
        // Cannot find it, must have been removed from file
        return new NullDiagramEditorInput(fileName, viewName);
    }

    /**
     * Saves the state of the given diagram editor input into the given memento.
     *
     * @param memento the storage area for element state
     * @param input the diagram editor input
     */
    public static void saveState(IMemento memento, DiagramEditorInput input) {
        IDiagramModel diagramModel = input.getDiagramModel();
        if(diagramModel != null && diagramModel.getArchimateModel() != null) {
            memento.putString(TAG_VIEW_ID, diagramModel.getId());
            String name = diagramModel.getName();
            if(name != null) {
                memento.putString(TAG_VIEW_NAME, name);
            }
            File file = diagramModel.getArchimateModel().getFile();
            if(file != null) {
                memento.putString(TAG_VIEW_FILE, file.getAbsolutePath());
            }
        }
    }

}
