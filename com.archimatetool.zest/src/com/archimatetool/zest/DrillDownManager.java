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
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.zest.core.widgets.GraphNode;

import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IRelationship;



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
    
    private IArchimateElement fHomeElement;
    private IArchimateElement fCurrentElement;
    
    private HashMap<Object, NodePositions> fPositions = new HashMap<Object, NodePositions>();

    DrillDownManager(ZestView view) {
        fView = view;
        fGraphViewer = view.getViewer();
        fGraphViewer.addSelectionChangedListener(this);
        makeActions();
    }
    
    void setNewInput(IArchimateElement element) {
        if(element == fCurrentElement) {
            return;
        }
        
        saveCurrentState();
        
        fHomeElement = element;
        fCurrentElement = element;
        
        setGraphViewerInput(element);
        
        fBackStack.clear();
        restoreLastState();
        updateNavigationButtons();
    }
    
    private void setGraphViewerInput(Object object) {
        ((ZestViewerLabelProvider)fGraphViewer.getLabelProvider()).setFocusElement(object);
        fGraphViewer.setInput(object);
    }
    
    IArchimateElement getCurrentElement() {
        return fCurrentElement;
    }
    
    void reset() {
        setNewInput(null); // this first
        fPositions.clear();
        fView.updateLabel();
        fView.updateActions();
    }
    
    void goHome() {
        if(isDeletedObject(fHomeElement)) {
            reset();
        }
        else {
            setNewInput(fHomeElement);
            fView.updateLabel();
        }
    }
    
    void goInto() {
        IStructuredSelection sel = (IStructuredSelection)fGraphViewer.getSelection();
        IArchimateElement element = (IArchimateElement)sel.getFirstElement();
        
        if(isValidObject(element)) {
            saveCurrentState();

            fBackStack.push(fCurrentElement);
            fCurrentElement = element;
            setGraphViewerInput(element);
            
            updateNavigationButtons();
            
            restoreLastState();
            
            fView.updateLabel();
        }
    }
    
    void goBack() {
        if(!fBackStack.isEmpty()) {
            saveCurrentState();
            
            IArchimateElement element = (IArchimateElement)fBackStack.pop();
            
            if(isDeletedObject(element)) {
                reset();
                return;
            }
            
            fCurrentElement = element;
            setGraphViewerInput(element);
            
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
        if(fCurrentElement == null) {
            return;
        }
        
        NodePositions pos = fPositions.get(fCurrentElement);
        if(pos == null) {
            pos = new NodePositions();
            fPositions.put(fCurrentElement, pos);
        }
        pos.saveNodePositions();
    }
    
    void restoreLastState() {
        if(fCurrentElement == null) {
            return;
        }
        
        NodePositions pos = fPositions.get(fCurrentElement);
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
                return AbstractUIPlugin.imageDescriptorFromPlugin(ArchimateZestPlugin.PLUGIN_ID, "img/home_nav.gif"); //$NON-NLS-1$
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
                return AbstractUIPlugin.imageDescriptorFromPlugin(ArchimateZestPlugin.PLUGIN_ID, "img/backward.gif"); //$NON-NLS-1$
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
                return AbstractUIPlugin.imageDescriptorFromPlugin(ArchimateZestPlugin.PLUGIN_ID, "img/forward.gif"); //$NON-NLS-1$
            }
        };

        fActionGoInto.setEnabled(false);
    }
    
    private void updateNavigationButtons() {
        IStructuredSelection selection = (IStructuredSelection)fGraphViewer.getSelection();
        Object selected = selection.getFirstElement();
        
        fActionHome.setEnabled(fHomeElement != null && fHomeElement != fCurrentElement);
        fActionBack.setEnabled(!fBackStack.isEmpty());
        fActionGoInto.setEnabled(isValidObject(selected));
    }
    
    private boolean isValidObject(Object selected) {
        return selected != null && selected != fCurrentElement && !(selected instanceof IRelationship);
    }
    
    private boolean isDeletedObject(IArchimateElement element) {
        return element != null && element.eContainer() == null;
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
