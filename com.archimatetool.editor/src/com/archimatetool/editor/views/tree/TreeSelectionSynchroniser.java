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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.IDiagramModelEditor;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.components.PartListenerAdapter;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelReference;



/**
 * Keeps Tree and Diagram Editors in Sync
 * 
 * @author Phillip Beauvoir
 */
public class TreeSelectionSynchroniser implements ISelectionChangedListener {

    private TreeModelViewer fTreeViewer;
    
    private boolean isSelecting = false;
    private boolean doSync = true; // This can suspend sync without disabling and unregistering listeners
    
    // Store last selection event so a selection can be made when this is re-enabled
    private SelectionChangedEvent lastSelectionEvent;
    
    // Store last activated Editor Part in order to not trigger spurious selections
    private IDiagramModelEditor lastActiveEditor;
    
    private PartListenerAdapter partListenerAdapter = new PartListenerAdapter() {
        @Override
        public void partOpened(IWorkbenchPart part) {
            if(part instanceof IDiagramModelEditor) {
                registerEditor((IDiagramModelEditor)part);
            }
        }
        
        @Override
        public void partClosed(IWorkbenchPart part) {
            if(part instanceof IDiagramModelEditor) {
                unregisterEditor((IDiagramModelEditor)part);
            }
            
            // This is important for garbage collection!
            // If we hold a reference to an IDiagramModelEditor it can't be disposed and neither can its IDiagramModel
            if(part == lastActiveEditor) {
                lastActiveEditor = null;
            }
        }
        
        @Override
        public void partActivated(IWorkbenchPart part) {
            if(part == lastActiveEditor) {
                return;
            }
            
            // Select all types of diagram in Tree
            if(part instanceof IDiagramModelEditor) {
                IDiagramModelEditor editor = (IDiagramModelEditor)part;
                
                // Editor model could be null if model's file was deleted/renamed and app opened
                if(editor.getModel() != null && doSync()) {
                    fTreeViewer.setSelection(new StructuredSelection(editor.getModel()), true);
                }
                
                lastActiveEditor = editor;
            }                
        }
    };

    TreeSelectionSynchroniser(TreeModelViewer treeViewer) {
        fTreeViewer = treeViewer;
        registerListeners();
        
        treeViewer.getTree().addDisposeListener((e) -> {
            unregisterListeners();
            
            // Ensure this stuff can be garbage collected
            fTreeViewer = null;
            lastActiveEditor = null;
            lastSelectionEvent = null;
            partListenerAdapter = null;
        });
    }
    
    /**
     * Setting to false will pause sync
     * @param set
     */
    public void setSynchronise(boolean set) {
        doSync = set;
    }
    
    private boolean doSync() {
        return doSync && ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.LINK_VIEW);
    }
    
    /**
     * Update with the last known selection
     */
    public void updateSelection() {
        // In this case we have created a new TreeViewer and synchroniser, so create a new selection event
        if(lastSelectionEvent == null) {
            IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
            if(activeEditor instanceof IDiagramModelEditor) {
                GraphicalViewer source = ((IDiagramModelEditor)activeEditor).getGraphicalViewer();
                ISelection selection = source.getSelection();
                selectionChanged(new SelectionChangedEvent(source, selection));
            }
        }
        else {
            selectionChanged(lastSelectionEvent);
        }
    }

    @Override
    public void selectionChanged(SelectionChangedEvent event) {
        if(isSelecting) {
            return;
        }
        
        // Store this even if we are not syncing
        lastSelectionEvent = event;

        if(!doSync()) {
            return;
        }
        
        isSelecting = true;
        
        ISelection selection = event.getSelection();
        Object source = event.getSource();
        
        // Selection from a Diagram Editor, so select objects in the Tree
        if(source instanceof GraphicalViewer) {
            List<Object> selected = new ArrayList<Object>();
            
            for(Object o : ((IStructuredSelection)selection).toArray()) {
                if(o instanceof EditPart) {
                    Object model = ((EditPart)o).getModel();
                    // Archimate concept
                    if(model instanceof IDiagramModelArchimateComponent) {
                        model = ((IDiagramModelArchimateComponent)model).getArchimateConcept();
                        selected.add(model);
                    }
                    // Diagram model reference
                    else if (model instanceof IDiagramModelReference) {
                        selected.add(((IDiagramModelReference)model).getReferencedModel());
                    }
                    // Diagram model
                    else if(model instanceof IDiagramModel) {
                        selected.add(model);
                    }
                }
            }
            
            // Select in tree
            fTreeViewer.setSelection(new StructuredSelection(selected), true);
        }
        // Selection from Tree, so select objects in any open Archimate Diagram Editors
        else if(source instanceof TreeViewer) {
            // Select these (or an empty selection) in the Diagram Editors
            for(IDiagramModelEditor editor : getOpenEditors()) {
                editor.selectObjects(((IStructuredSelection)selection).toArray());
            }
        }
        
        isSelecting = false;
    }
    
    private void registerListeners() {
        // Part listener
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        window.getPartService().addPartListener(partListenerAdapter);
     
        // Tree listener
        fTreeViewer.addSelectionChangedListener(this);
        
        // Open Editors
        for(IDiagramModelEditor editor : getOpenEditors()) {
            registerEditor(editor);
        }
    }
    
    private void unregisterListeners() {
        // Part listener
        IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        window.getPartService().removePartListener(partListenerAdapter);
     
        // Tree listener
        fTreeViewer.removeSelectionChangedListener(this);
        
        // Open Editors
        for(IDiagramModelEditor editor : getOpenEditors()) {
            unregisterEditor(editor);
        }
    }

    private void registerEditor(IDiagramModelEditor editor) {
        GraphicalViewer viewer = editor.getAdapter(GraphicalViewer.class);
        if(viewer != null) {
            viewer.addSelectionChangedListener(this);
        }
    }
    
    private void unregisterEditor(IDiagramModelEditor editor) {
        GraphicalViewer viewer = editor.getAdapter(GraphicalViewer.class);
        if(viewer != null) {
            viewer.removeSelectionChangedListener(this);
        }
    }

    private List<IDiagramModelEditor> getOpenEditors() {
        List<IDiagramModelEditor> list = new ArrayList<>();
        
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        for(IEditorReference ref : page.getEditorReferences()) {
            IEditorPart part = ref.getEditor(false);
            if(part instanceof IDiagramModelEditor) {
                list.add((IDiagramModelEditor)part);
            }
        }
        
        return list;
    }
}
