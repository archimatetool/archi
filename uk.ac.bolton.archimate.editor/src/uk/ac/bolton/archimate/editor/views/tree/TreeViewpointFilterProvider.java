/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.views.tree;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

import uk.ac.bolton.archimate.editor.diagram.IDiagramModelEditor;
import uk.ac.bolton.archimate.editor.model.viewpoints.IViewpoint;
import uk.ac.bolton.archimate.editor.model.viewpoints.ViewpointsManager;
import uk.ac.bolton.archimate.editor.preferences.IPreferenceConstants;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IRelationship;


/**
 * Provides Tree filtering support when a Viewpoint is selected
 * 
 * @author Phillip Beauvoir
 */
public class TreeViewpointFilterProvider implements IPartListener {

    /**
     * Active Viewpoint
     */
    private IViewpoint fViewpoint;
    
    /**
     * Active Diagram Model
     */
    private IDiagramModel fActiveDiagramModel;
    
    /**
     * Tree Viewer
     */
    private Viewer fViewer;
    
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
    
    TreeViewpointFilterProvider(Viewer viewer) {
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPartService().addPartListener(this);
        fViewer = viewer;

        // Listen to Preferences
        Preferences.STORE.addPropertyChangeListener(prefsListener);
        fViewer.getControl().addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                PlatformUI.getWorkbench().getActiveWorkbenchWindow().getPartService().removePartListener(TreeViewpointFilterProvider.this);
                Preferences.STORE.removePropertyChangeListener(prefsListener);
            }
        });
    }
    
    @Override
    public void partActivated(IWorkbenchPart part) {
        if(part instanceof IDiagramModelEditor) { //also check sketch view
            fActiveDiagramModel = ((IDiagramModelEditor)part).getModel();
        }
        if(isActive()) {
            fViewer.refresh(); // refresh in all cases
        } 
    }

    @Override
    public void partBroughtToTop(IWorkbenchPart part) {
    }

    @Override
    public void partClosed(IWorkbenchPart part) {
        // Check if no editors open
        if(part instanceof IEditorPart) {
            IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
            if(page != null && page.getActiveEditor() == null) {
                fActiveDiagramModel = null;
                if(isActive()) {
                    fViewer.refresh();
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
    public Color getTextColor(Object element) {
        if(isActive() && fActiveDiagramModel != null) {
            int index = fActiveDiagramModel.getViewpoint();
            fViewpoint = ViewpointsManager.INSTANCE.getViewpoint(index);
            
            if(fViewpoint != null && element instanceof IArchimateElement) {
                // From same model as active diagram
                IArchimateModel model = ((IArchimateElement)element).getArchimateModel();
                if(model == fActiveDiagramModel.getArchimateModel()) {
                    if(element instanceof IRelationship) {
                        IArchimateElement source = ((IRelationship)element).getSource();
                        IArchimateElement target = ((IRelationship)element).getTarget();
                        if(!fViewpoint.isAllowedType(source.eClass()) || !fViewpoint.isAllowedType(target.eClass())) {
                            return ColorFactory.get(128, 128, 128);
                        }
                    }
                    else {
                        if(!fViewpoint.isAllowedType(((IArchimateElement)element).eClass())) {
                            return ColorFactory.get(128, 128, 128);
                        }
                    }
                }
            }
        }

        return null;
    }
    
    boolean isActive() {
        return Preferences.STORE.getBoolean(IPreferenceConstants.VIEWPOINTS_FILTER_MODEL_TREE);
    }
}
