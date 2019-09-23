/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.zest;

import java.util.HashMap;
import java.util.Stack;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.zest.core.widgets.GraphNode;

import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;



/**
 * DrillDown Manager
 * 
 * @author Phillip Beauvoir
 */
public class DrillDownManager implements ISelectionChangedListener {

    private Stack<Object> fBackStack = new Stack<Object>();

    private IAction fActionHome;
    private IAction fActionBack;
    private IAction fActionGoInto;
    
    private ZestView fView;
    private ZestGraphViewer fGraphViewer;
    
    private IArchimateConcept fHomeConcept;
    private IArchimateConcept fCurrentConcept;
    
    private HashMap<Object, NodePositions> fPositions = new HashMap<Object, NodePositions>();

    DrillDownManager(ZestView view) {
        fView = view;
        fGraphViewer = view.getViewer();
        fGraphViewer.addSelectionChangedListener(this);
        makeActions();
    }
    
    void setNewInput(IArchimateConcept concept) {
        if(concept == fCurrentConcept) {
            return;
        }
        
        saveCurrentState();
        
        fHomeConcept = concept;
        fCurrentConcept = concept;
        
        setGraphViewerInput(concept);
        
        fBackStack.clear();
        restoreLastState();
        updateNavigationButtons();
    }
    
    private void setGraphViewerInput(Object object) {
        ((ZestViewerLabelProvider)fGraphViewer.getLabelProvider()).setFocusElement(object);
        fGraphViewer.setInput(object);
    }
    
    IArchimateConcept getCurrentConcept() {
        return fCurrentConcept;
    }
    
    void reset() {
        setNewInput(null); // this first
        fPositions.clear();
        fView.updateLabel();
        fView.updateActions();
    }
    
    void goHome() {
        if(isDeletedObject(fHomeConcept)) {
            reset();
        }
        else {
            setNewInput(fHomeConcept);
            fView.updateLabel();
        }
    }
    
    void goInto() {
        IStructuredSelection sel = (IStructuredSelection)fGraphViewer.getSelection();
        IArchimateConcept concept = (IArchimateConcept)sel.getFirstElement();
        
        if(isValidObject(concept)) {
            saveCurrentState();

            fBackStack.push(fCurrentConcept);
            fCurrentConcept = concept;
            setGraphViewerInput(concept);
            
            updateNavigationButtons();
            
            restoreLastState();
            
            fView.updateLabel();
        }
    }
    
    void goBack() {
        if(!fBackStack.isEmpty()) {
            saveCurrentState();
            
            IArchimateConcept concept = (IArchimateConcept)fBackStack.pop();
            
            if(isDeletedObject(concept)) {
                reset();
                return;
            }
            
            fCurrentConcept = concept;
            setGraphViewerInput(concept);
            
            updateNavigationButtons();
            
            restoreLastState();
            
            fView.updateLabel();
        }
    }

    void addNavigationActions(IToolBarManager toolBar) {
        toolBar.add(fActionHome);
        toolBar.add(fActionBack);
        toolBar.add(fActionGoInto);
    }
    
    void addNavigationActions(IMenuManager manager) {
        manager.add(fActionHome);
        manager.add(fActionBack);
        manager.add(fActionGoInto);
    }

    void saveCurrentState() {
        if(fCurrentConcept == null) {
            return;
        }
        
        NodePositions pos = fPositions.get(fCurrentConcept);
        if(pos == null) {
            pos = new NodePositions();
            fPositions.put(fCurrentConcept, pos);
        }
        pos.saveNodePositions();
    }
    
    void restoreLastState() {
        if(fCurrentConcept == null) {
            return;
        }
        
        NodePositions pos = fPositions.get(fCurrentConcept);
        if(pos != null) {
            // Restore positions
            pos.restoreNodePositions();
        }
        else {
            // Use default layout
            fGraphViewer.doApplyLayout();
        }
    }

    private void makeActions() {
        fActionHome = new Action(Messages.DrillDownManager_0) {
            @Override
            public void run() {
                goHome();
            }

            @Override
            public String getToolTipText() {
                return Messages.DrillDownManager_1;
            }

            @Override
            public ImageDescriptor getImageDescriptor() {
                return ResourceLocator.imageDescriptorFromBundle(ArchiZestPlugin.PLUGIN_ID, "img/home_nav.png").orElse(null); //$NON-NLS-1$
            }
        };
        
        fActionHome.setEnabled(false);
        
        fActionBack = new Action(Messages.DrillDownManager_2) {
            @Override
            public void run() {
                goBack();
            }

            @Override
            public String getToolTipText() {
                return Messages.DrillDownManager_3;
            }

            @Override
            public ImageDescriptor getImageDescriptor() {
                return ResourceLocator.imageDescriptorFromBundle(ArchiZestPlugin.PLUGIN_ID, "img/backward_nav.png").orElse(null); //$NON-NLS-1$
            }
        };
        
        fActionBack.setEnabled(false);

        fActionGoInto = new Action(Messages.DrillDownManager_4) {
            @Override
            public void run() {
                goInto();
            }

            @Override
            public String getToolTipText() {
                return getText();
            }

            @Override
            public ImageDescriptor getImageDescriptor() {
                return ResourceLocator.imageDescriptorFromBundle(ArchiZestPlugin.PLUGIN_ID, "img/forward_nav.png").orElse(null); //$NON-NLS-1$
            }
        };

        fActionGoInto.setEnabled(false);
    }
    
    private void updateNavigationButtons() {
        IStructuredSelection selection = (IStructuredSelection)fGraphViewer.getSelection();
        Object selected = selection.getFirstElement();
        
        fActionHome.setEnabled(fHomeConcept != null && fHomeConcept != fCurrentConcept);
        fActionBack.setEnabled(!fBackStack.isEmpty());
        fActionGoInto.setEnabled(isValidObject(selected));
    }
    
    private boolean isValidObject(Object selected) {
        return (selected != null) && (selected != fCurrentConcept) && (selected instanceof IArchimateElement);
    }
    
    private boolean isDeletedObject(IArchimateConcept concept) {
        return concept != null && concept.eContainer() == null;
    }

    @Override
    public void selectionChanged(SelectionChangedEvent event) {
        updateNavigationButtons();
    }
    
    /*
     * Stores an Element's node positions
     */
    private class NodePositions {
        private HashMap<Object, Point> nodePositions = new HashMap<Object, Point>();
        
        void saveNodePositions() {
            for(Object n : fGraphViewer.getGraphControl().getNodes()) {
                GraphNode node = (GraphNode)n;
                Object element = node.getData();
                nodePositions.put(element, node.getLocation());
            }
        }
        
        void restoreNodePositions() {
            for(Object n : fGraphViewer.getGraphControl().getNodes()) {
                GraphNode node = (GraphNode)n;
                Object element = node.getData();
                Point pt = nodePositions.get(element);
                if(pt != null) {
                    node.setLocation(pt.x, pt.y);
                }
                else {
                    nodePositions.put(element, node.getLocation());
                }
            }
        }
    }

}
