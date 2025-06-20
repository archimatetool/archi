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
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;

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

    private IWorkbenchPage fPage;
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
            if(part instanceof IDiagramModelEditor editor) {
                registerEditor(editor);
            }
        }
        
        @Override
        public void partClosed(IWorkbenchPart part) {
            if(part instanceof IDiagramModelEditor editor) {
                unregisterEditor(editor);
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
            if(part instanceof IDiagramModelEditor editor) {
                // Editor model could be null if model's file was deleted/renamed and app opened (zombie editor part)
                if(editor.getModel() != null && doSync()) {
                    fTreeViewer.setSelection(new StructuredSelection(editor.getModel()), true);
                }
                
                lastActiveEditor = editor;
            }                
        }
    };
    
    private IPropertyChangeListener prefsListener = event -> {
        if(event.getProperty().equals(IPreferenceConstants.LINK_VIEW) && event.getNewValue() == Boolean.TRUE) {
            updateSelection();
        }
    };

    TreeSelectionSynchroniser(TreeModelView treeModelView) {
        fTreeViewer = treeModelView.getViewer();
        fPage = treeModelView.getSite().getPage();
        
        registerListeners();
        
        fTreeViewer.getTree().addDisposeListener(e -> {
            unregisterListeners();
            
            // Ensure this stuff can be garbage collected
            fTreeViewer = null;
            fPage = null;
            lastActiveEditor = null;
            lastSelectionEvent = null;
            partListenerAdapter = null;
            prefsListener = null;
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
        return doSync && ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.LINK_VIEW);
    }
    
    /**
     * Update with the last known selections
     */
    private void updateSelection() {
        // In this case we have created a new TreeViewer and synchroniser, so create a new selection event
        if(lastSelectionEvent == null) {
            IEditorPart activeEditor = fPage.getActiveEditor();
            if(activeEditor instanceof IDiagramModelEditor editor && editor.getGraphicalViewer() != null) { // check this is not a zombie editor part
                selectionChanged(new SelectionChangedEvent(editor.getGraphicalViewer(), editor.getGraphicalViewer().getSelection()));
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
        
        Object source = event.getSource();
        Object[] selectedObjects = event.getStructuredSelection().toArray();
        
        // Selection from a Diagram Editor, so select objects in the Tree
        if(source instanceof GraphicalViewer) {
            List<Object> selected = new ArrayList<>();
            
            for(Object o : selectedObjects) {
                if(o instanceof EditPart editPart) {
                    Object model = editPart.getModel();
                    // Archimate concept
                    if(model instanceof IDiagramModelArchimateComponent dmc) {
                        selected.add(dmc.getArchimateConcept());
                    }
                    // Diagram model reference
                    else if (model instanceof IDiagramModelReference dmr) {
                        selected.add(dmr.getReferencedModel());
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
                editor.selectObjects(selectedObjects);
            }
        }
        
        isSelecting = false;
    }
    
    private void registerListeners() {
        // Part listener
        fPage.addPartListener(partListenerAdapter);
     
        // Tree listener
        fTreeViewer.addSelectionChangedListener(this);
        
        // Open Editors
        for(IDiagramModelEditor editor : getOpenEditors()) {
            registerEditor(editor);
        }
        
        // Preference listener
        ArchiPlugin.getInstance().getPreferenceStore().addPropertyChangeListener(prefsListener);
    }
    
    private void unregisterListeners() {
        // Part listener
        fPage.removePartListener(partListenerAdapter);
     
        // Tree listener
        fTreeViewer.removeSelectionChangedListener(this);
        
        // Open Editors
        for(IDiagramModelEditor editor : getOpenEditors()) {
            unregisterEditor(editor);
        }
        
        // Preference listener
        ArchiPlugin.getInstance().getPreferenceStore().removePropertyChangeListener(prefsListener);
    }

    private void registerEditor(IDiagramModelEditor editor) {
        GraphicalViewer viewer = editor.getGraphicalViewer();
        if(viewer != null) {
            viewer.addSelectionChangedListener(this);
        }
    }
    
    private void unregisterEditor(IDiagramModelEditor editor) {
        GraphicalViewer viewer = editor.getGraphicalViewer();
        if(viewer != null) {
            viewer.removeSelectionChangedListener(this);
        }
    }

    private List<IDiagramModelEditor> getOpenEditors() {
        List<IDiagramModelEditor> list = new ArrayList<>();
        
        for(IEditorReference ref : fPage.getEditorReferences()) {
            // Editor model could be null if model's file was deleted/renamed and app opened (zombie editor part)
            if(ref.getEditor(false) instanceof IDiagramModelEditor editor && editor.getModel() != null) {
                list.add(editor);
            }
        }
        
        return list;
    }
}
