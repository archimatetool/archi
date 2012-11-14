/*******************************************************************************
 * Copyright (c) 2010-12 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.zest;

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
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.zest.core.widgets.GraphNode;

import uk.ac.bolton.archimate.model.IRelationship;


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
    
    private Object fOriginal;
    private Object fCurrent;
    
    private HashMap<Object, NodePositions> fPositions = new HashMap<Object, NodePositions>();

    DrillDownManager(ZestView view) {
        fView = view;
        fGraphViewer = view.getViewer();
        fGraphViewer.addSelectionChangedListener(this);
        makeActions();
    }
    
    void setNewInput(Object object) {
        if(object == fCurrent) {
            return;
        }
        
        saveCurrentState();
        
        fOriginal = object;
        fCurrent = object;
        
        fGraphViewer.setInput(object);
        fBackStack.clear();
        
        restoreLastState();
        
        if(object != null) {
            fGraphViewer.setSelection(new StructuredSelection(object));
        }
        
        updateNavigationButtons();
    }
    
    void reset() {
        setNewInput(null);
        fPositions.clear();
    }
    
    void goHome() {
        setNewInput(fOriginal);
        fView.updateLabel(fOriginal);
    }
    
    void goInto() {
        IStructuredSelection sel = (IStructuredSelection)fGraphViewer.getSelection();
        Object selected = sel.getFirstElement();
        
        if(isValidObject(selected)) {
            saveCurrentState();

            fBackStack.push(fCurrent);
            fCurrent = selected;
            fGraphViewer.setInput(selected);
            
            fGraphViewer.setSelection(new StructuredSelection(selected));
            updateNavigationButtons();
            
            restoreLastState();
            
            fView.updateLabel(selected);
        }
    }
    
    void goBack() {
        if(!fBackStack.isEmpty()) {
            saveCurrentState();
            
            Object selected = fBackStack.pop();
            fCurrent = selected;
            fGraphViewer.setInput(selected);
            
            fGraphViewer.setSelection(new StructuredSelection(selected));
            updateNavigationButtons();
            
            restoreLastState();
            
            fView.updateLabel(selected);
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
        if(fCurrent == null) {
            return;
        }
        
        NodePositions pos = fPositions.get(fCurrent);
        if(pos == null) {
            pos = new NodePositions();
            fPositions.put(fCurrent, pos);
        }
        pos.saveNodePositions();
    }
    
    void restoreLastState() {
        if(fCurrent == null) {
            return;
        }
        
        NodePositions pos = fPositions.get(fCurrent);
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
        
        fActionHome.setEnabled(fOriginal != null && fOriginal != fCurrent);
        fActionBack.setEnabled(!fBackStack.isEmpty());
        fActionGoInto.setEnabled(isValidObject(selected));
    }
    
    private boolean isValidObject(Object selected) {
        return selected != null && selected != fCurrent && !(selected instanceof IRelationship);
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
