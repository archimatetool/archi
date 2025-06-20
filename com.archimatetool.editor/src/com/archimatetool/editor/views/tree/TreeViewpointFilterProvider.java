/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree;

import java.util.Objects;

import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.IArchimateDiagramEditor;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.components.PartListenerAdapter;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.viewpoints.IViewpoint;
import com.archimatetool.model.viewpoints.ViewpointManager;



/**
 * Provides Tree filtering support when a Viewpoint is selected
 * 
 * @author Phillip Beauvoir
 */
public class TreeViewpointFilterProvider {

    private IArchimateDiagramModel activeDiagramModel;
    private TreeModelViewer treeViewer;
    
    private Color colorGrey = new Color(128, 128, 128);
    
    private IPartListener partListener = new PartListenerAdapter() {
        @Override
        public void partActivated(IWorkbenchPart part) {
            if(part instanceof IEditorPart) {
                // Track active part even if the filter is not on, so we have them if it is turned on in preferences
                
                // Previous active diagram model
                IArchimateDiagramModel previousDiagramModel = activeDiagramModel;
                
                // If Part is an Archimate editor store active diagram model or else null
                activeDiagramModel = (part instanceof IArchimateDiagramEditor diagramEditor) ? (IArchimateDiagramModel)diagramEditor.getModel() : null;
                
                if(isActive()) {
                    // If the diagram models are in the same model...
                    if(activeDiagramModel != null && previousDiagramModel != null
                            && activeDiagramModel.getArchimateModel() == previousDiagramModel.getArchimateModel()) {
                        
                        // ...and have different viewpoints
                        if(!Objects.equals(previousDiagramModel.getViewpoint(), activeDiagramModel.getViewpoint())) {
                            treeViewer.updateInBackground(activeDiagramModel.getArchimateModel());
                        }
                    }
                    else {
                        if(previousDiagramModel != null) {
                            treeViewer.updateInBackground(previousDiagramModel.getArchimateModel());
                        }
                        if(activeDiagramModel != null) {
                            treeViewer.updateInBackground(activeDiagramModel.getArchimateModel());
                        }
                    }
                }
            }
        }

        @Override
        public void partClosed(IWorkbenchPart part) {
            // If no editors are open in the workbench then update the tree
            if(part instanceof IEditorPart) {
                if(part.getSite().getPage().getActiveEditor() == null) {
                    activeDiagramModel = null;
                    if(isActive()) {
                        treeViewer.updateInBackground();
                    }
                }
            }
        }
    };

    TreeViewpointFilterProvider(IWorkbenchWindow window, TreeModelViewer viewer) {
        treeViewer = viewer;

        // Listen to Part selections
        window.getPartService().addPartListener(partListener);
        
        // Dispose and clean up
        treeViewer.getControl().addDisposeListener(event -> {
            window.getPartService().removePartListener(partListener);

            activeDiagramModel = null;
            treeViewer = null;
        });
    }
    
    /**
     * @return grey text color if the given concept is disallowed in a Viewpoint for the active model, or else null
     */
    Color getTextColor(Object object) {
        if(isActive() && activeDiagramModel != null && object instanceof IArchimateConcept concept
                      && concept.getArchimateModel() == activeDiagramModel.getArchimateModel()) { // From same model as active diagram
            IViewpoint viewpoint = ViewpointManager.INSTANCE.getViewpoint(activeDiagramModel.getViewpoint());
            if(viewpoint != null) {
                if(concept instanceof IArchimateRelationship relation) {
                    IArchimateConcept source = relation.getSource();
                    IArchimateConcept target = relation.getTarget();
                    return viewpoint.isAllowedConcept(source.eClass())
                            && viewpoint.isAllowedConcept(target.eClass()) ? null : colorGrey;
                }
                else if(object instanceof IArchimateElement element) {
                    return viewpoint.isAllowedConcept(element.eClass()) ? null : colorGrey;
                }
            }
        }

        return null;
    }
    
    boolean isActive() {
        return ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.VIEWPOINTS_FILTER_MODEL_TREE);
    }
}
