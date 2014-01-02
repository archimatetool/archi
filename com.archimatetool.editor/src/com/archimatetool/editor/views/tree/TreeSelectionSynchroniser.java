/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree;

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
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.IArchimateDiagramEditor;
import com.archimatetool.editor.diagram.IDiagramModelEditor;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.components.PartListenerAdapter;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;



/**
 * Keeps Tree and Diagram Editors in Sync
 * 
 * @author Phillip Beauvoir
 */
public class TreeSelectionSynchroniser implements ISelectionChangedListener {

    public static TreeSelectionSynchroniser INSTANCE = new TreeSelectionSynchroniser();
    
    private ITreeModelView fTreeView;
    
    /**
     * List of active editors
     */
    private List<IDiagramModelEditor> fDiagramEditors = new ArrayList<IDiagramModelEditor>();
    
    private boolean isDispatching = false;
    
    private boolean fDoSync = true;
    
    private SelectionChangedEvent fLastEvent;
    
    private TreeSelectionSynchroniser() {
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        window.getPartService().addPartListener(new PartListenerAdapter() {
            @Override
            public void partActivated(IWorkbenchPart part) {
                if(part instanceof IDiagramModelEditor) {
                    IDiagramModelEditor diagramEditor = (IDiagramModelEditor)part;
                    if(!fDiagramEditors.contains(diagramEditor)) {
                        GraphicalViewer viewer = (GraphicalViewer)diagramEditor.getAdapter(GraphicalViewer.class);
                        if(viewer != null) {
                            viewer.addSelectionChangedListener(TreeSelectionSynchroniser.this);
                            fDiagramEditors.add(diagramEditor);
                        }
                    }
                }
            }
            
            @Override
            public void partClosed(IWorkbenchPart part) {
                if(part instanceof IDiagramModelEditor) {
                    IDiagramModelEditor diagramEditor = (IDiagramModelEditor)part;
                    if(fDiagramEditors.contains(diagramEditor)) { // important guard
                        GraphicalViewer viewer = (GraphicalViewer)diagramEditor.getAdapter(GraphicalViewer.class);
                        if(viewer != null) { // could be null under some circumstances
                            viewer.removeSelectionChangedListener(TreeSelectionSynchroniser.this);
                        }
                        fDiagramEditors.remove(diagramEditor);
                    }
                }
            }
        });
    }
    
    void setTreeModelView(ITreeModelView treeView) {
        if(treeView != fTreeView) {
            fTreeView = treeView;
            fTreeView.getViewer().addSelectionChangedListener(this);
        }
    }
    
    void removeTreeModelView() {
        if(fTreeView != null) {
            fTreeView.getViewer().removeSelectionChangedListener(this);
            fTreeView = null;
        }
    }
    
    void setSynchronise(boolean set) {
        fDoSync = set;
    }
    
    public void refresh() {
        if(fLastEvent != null) {
            selectionChanged(fLastEvent);
        }
    }

    @Override
    public void selectionChanged(SelectionChangedEvent event) {
        if(isDispatching) {
            return;
        }
        
        fLastEvent = event;

        if(!Preferences.doLinkView() || !fDoSync) {
            return;
        }
        
        isDispatching = true;
        
        ISelection selection = event.getSelection();
        Object source = event.getSource();
        
        // Selection from Diagram Editor, so update the Tree if it is showing
        if(source instanceof GraphicalViewer && fTreeView != null) {
            List<Object> list = new ArrayList<Object>();
            
            for(Object o : ((IStructuredSelection)selection).toArray()) {
                if(o instanceof EditPart) {
                    Object model = ((EditPart)o).getModel();
                    // Archimate element
                    if(model instanceof IDiagramModelArchimateObject) {
                        model = ((IDiagramModelArchimateObject)model).getArchimateElement();
                        list.add(model);
                    }
                    // Archimate connection
                    else if(model instanceof IDiagramModelArchimateConnection) {
                        model = ((IDiagramModelArchimateConnection)model).getRelationship();
                        list.add(model);
                    }
                    // Diagram model
                    else if(model instanceof IDiagramModel) {
                        list.add(model);
                    }
                }
            }
            
            // Select in tree
            fTreeView.getViewer().setSelection(new StructuredSelection(list), true);
        }
        
        // Archimate objects selection from Tree View, so update any Archimate Diagram Editors
        else if(source instanceof TreeViewer) {
            List<IArchimateElement> list = new ArrayList<IArchimateElement>();
            
            // Archimate elements
            for(Object o : ((IStructuredSelection)selection).toArray()) {
                if(o instanceof IArchimateElement) {
                    list.add((IArchimateElement)o);
                }
            }
            
            // Select these in the Diagram Editors
            for(IDiagramModelEditor diagramEditor : fDiagramEditors) {
                if(diagramEditor instanceof IArchimateDiagramEditor) {
                    ((IArchimateDiagramEditor)diagramEditor).selectElements(list.toArray(new IArchimateElement[list.size()]));
                }
            }
        }
        
        isDispatching = false;
    }
    
    
}
