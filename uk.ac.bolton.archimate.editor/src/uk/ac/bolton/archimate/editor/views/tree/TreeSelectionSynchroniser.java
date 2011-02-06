/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.views.tree;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import uk.ac.bolton.archimate.editor.diagram.IDiagramEditor;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;


/**
 * Keeps Tree and Diagram Editors in Sync
 * 
 * @author Phillip Beauvoir
 */
public class TreeSelectionSynchroniser implements ISelectionChangedListener {

    public static TreeSelectionSynchroniser INSTANCE = new TreeSelectionSynchroniser();
    
    private ITreeModelView fTreeView;
    private List<IDiagramEditor> fDiagramEditors = new ArrayList<IDiagramEditor>();
    
    private boolean isDispatching = false;
    
    private SelectionChangedEvent cachedEvent;
    
    public void addDiagramEditor(IDiagramEditor diagramEditor) {
        GraphicalViewer viewer = (GraphicalViewer)diagramEditor.getAdapter(GraphicalViewer.class);
        if(viewer != null) {
            viewer.addSelectionChangedListener(this);
            fDiagramEditors.add(diagramEditor);
        }
    }
    
    public void removeDiagramEditor(IDiagramEditor diagramEditor) {
        if(diagramEditor != null && fDiagramEditors.contains(diagramEditor)) {
            GraphicalViewer viewer = (GraphicalViewer)diagramEditor.getAdapter(GraphicalViewer.class);
            viewer.removeSelectionChangedListener(this);
            fDiagramEditors.remove(diagramEditor);
        }
    }
    
    public void setTreeModelView(ITreeModelView treeView) {
        fTreeView = treeView;
        fTreeView.getViewer().addSelectionChangedListener(this);
    }
    
    public void removeTreeModelView() {
        if(fTreeView != null) {
            fTreeView.getViewer().removeSelectionChangedListener(this);
            fTreeView = null;
        }
    }
    
    public void refresh() {
        if(cachedEvent != null) {
            selectionChanged(cachedEvent);
        }
    }

    @Override
    public void selectionChanged(SelectionChangedEvent event) {
        cachedEvent = event;

        if(!Preferences.doLinkView()) {
            return;
        }
        
        if(isDispatching) {
            return;
        }
        
        isDispatching = true;
        
        ISelection selection = event.getSelection();
        Object source = event.getSource();
        
        // Selection from Diagram Editor, so update the Tree
        if(source instanceof GraphicalViewer && fTreeView != null) {
            List<IArchimateElement> list = new ArrayList<IArchimateElement>();
            for(Object o : ((IStructuredSelection)selection).toArray()) {
                if(o instanceof EditPart) {
                    Object model = ((EditPart)o).getModel();
                    if(model instanceof IDiagramModelArchimateObject) {
                        model = ((IDiagramModelArchimateObject)model).getArchimateElement();
                        list.add((IArchimateElement)model);
                    }
                    else if(model instanceof IDiagramModelArchimateConnection) {
                        model = ((IDiagramModelArchimateConnection)model).getRelationship();
                        list.add((IArchimateElement)model);
                    }
                }
            }
            
            fTreeView.getViewer().setSelection(new StructuredSelection(list), true);
        }
        // Selection from Tree View, so update any Diagram Editors
        else if(source instanceof TreeViewer) {
            List<IArchimateElement> list = new ArrayList<IArchimateElement>();
            for(Object o : ((IStructuredSelection)selection).toArray()) {
                if(o instanceof IArchimateElement) {
                    list.add((IArchimateElement)o);
                }
            }
            for(IDiagramEditor diagramEditor : fDiagramEditors) {
                diagramEditor.selectElements(list.toArray(new IArchimateElement[list.size()]));
            }
        }
        
        isDispatching = false;
    }
    
    
}
