/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.IArchimateDiagramEditor;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.viewpoints.IViewpoint;
import com.archimatetool.model.viewpoints.ViewpointManager;



/**
 * Provides Tree filtering support when a Viewpoint is selected
 * 
 * @author Phillip Beauvoir
 */
public class TreeViewpointFilterProvider implements IPartListener {

    /**
     * Active Diagram Model
     */
    private IArchimateDiagramModel fActiveDiagramModel;
    
    /**
     * Tree Viewer
     */
    private TreeModelViewer fViewer;
    
    private Color colorGrey = new Color(128, 128, 128);
    
    /**
     * Application Preferences Listener
     */
    private IPropertyChangeListener prefsListener = new IPropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if(IPreferenceConstants.VIEWPOINTS_FILTER_MODEL_TREE.equals(event.getProperty())) {
                fViewer.refresh();
            }
        }
    };
    
    TreeViewpointFilterProvider(TreeModelViewer viewer) {
        fViewer = viewer;

        // Listen to Part selections
        if(PlatformUI.isWorkbenchRunning()) {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPartService().addPartListener(this);
        }
        
        // Listen to Preferences
        ArchiPlugin.PREFERENCES.addPropertyChangeListener(prefsListener);

        fViewer.getControl().addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
            	if(PlatformUI.isWorkbenchRunning() && PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null) {
            		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPartService().removePartListener(TreeViewpointFilterProvider.this);
            	}
                
                ArchiPlugin.PREFERENCES.removePropertyChangeListener(prefsListener);
                
                fActiveDiagramModel = null;
                fViewer = null;
            }
        });
    }
    
    /**
     * Refresh the Archimate model in the tree
     */
    private void refreshTreeModel(IArchimateDiagramModel dm) {
        if(dm != null && isActive()) {
            IArchimateModel model = dm.getArchimateModel();
            fViewer.refreshInBackground(model);
        }
    }

    @Override
    public void partActivated(IWorkbenchPart part) {
        /*
         * Refresh Tree only if an IEditorPart is activated.
         * 
         * If we call refresh() when a ViewPart is activated then a problem occurs when:
         * 1. User adds a new Diagram View to the Tree
         * 2. Element is added to model - refresh() is called on Tree
         * 3. Diagram Editor is opened and activated
         * 4. This is notified of Diagram Editor Part activation and calls refresh() on Tree
         * 5. NewDiagramCommand.execute() calls TreeModelViewer.editElement() to edit the cell name
         * 6. The Tree is then activated and This is notified of Diagram Editor Part activation and calls refresh() on Tree
         * 7. TreeModelViewer.refresh(element) then cancels editing
         */
        if(part instanceof IEditorPart) {
            IArchimateDiagramModel previous = fActiveDiagramModel;

            // Archimate editor
            if(part instanceof IArchimateDiagramEditor) {
                IArchimateDiagramModel dm = (IArchimateDiagramModel)((IArchimateDiagramEditor)part).getModel();
                
                if(previous == dm) {
                    return;
                }
                
                fActiveDiagramModel = dm;
            }
            // Other type of editor (sketch, canvas)
            else {
                fActiveDiagramModel = null;
            }
            
            // Refresh previous model
            refreshTreeModel(previous);

            // Refresh selected model
            refreshTreeModel(fActiveDiagramModel);
        }
    }

    @Override
    public void partBroughtToTop(IWorkbenchPart part) {
    }

    @Override
    public void partClosed(IWorkbenchPart part) {
        // Check if no editors open
        if(part instanceof IEditorPart) {
        	if(PlatformUI.getWorkbench().getActiveWorkbenchWindow() != null) {
        		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
                if(page != null && page.getActiveEditor() == null) {
                    fActiveDiagramModel = null;
                    if(isActive()) {
                        fViewer.refreshInBackground(null);
                    }
                }
        	}
        }
    }

    @Override
    public void partDeactivated(IWorkbenchPart part) {
    }

    @Override
    public void partOpened(IWorkbenchPart part) {
    }

    /**
     * If the element is disallowed in a Viewpoint grey it out
     * @param element
     * @return Color or null
     */
    Color getTextColor(Object element) {
        if(isActive() && fActiveDiagramModel != null && element instanceof IArchimateConcept) {
            String id = fActiveDiagramModel.getViewpoint();
            IViewpoint viewpoint = ViewpointManager.INSTANCE.getViewpoint(id);
            if(viewpoint != null) {
                // From same model as active diagram
                IArchimateModel model = ((IArchimateConcept)element).getArchimateModel();
                if(model == fActiveDiagramModel.getArchimateModel()) {
                    if(element instanceof IArchimateRelationship) {
                        IArchimateConcept source = ((IArchimateRelationship)element).getSource();
                        IArchimateConcept target = ((IArchimateRelationship)element).getTarget();
                        if(!viewpoint.isAllowedConcept(source.eClass()) || !viewpoint.isAllowedConcept(target.eClass())) {
                            return colorGrey;
                        }
                    }
                    else if(element instanceof IArchimateElement) {
                        if(!viewpoint.isAllowedConcept(((IArchimateElement)element).eClass())) {
                            return colorGrey;
                        }
                    }
                }
            }
        }

        return null;
    }
    
    boolean isActive() {
        return ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.VIEWPOINTS_FILTER_MODEL_TREE);
    }
}
