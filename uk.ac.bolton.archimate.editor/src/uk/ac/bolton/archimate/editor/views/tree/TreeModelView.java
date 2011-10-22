/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.views.tree;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import uk.ac.bolton.archimate.editor.actions.ArchimateEditorActionFactory;
import uk.ac.bolton.archimate.editor.actions.NewArchimateModelAction;
import uk.ac.bolton.archimate.editor.actions.OpenModelAction;
import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.editor.preferences.IPreferenceConstants;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.editor.ui.services.EditorManager;
import uk.ac.bolton.archimate.editor.ui.services.IUIRequestListener;
import uk.ac.bolton.archimate.editor.ui.services.UIRequest;
import uk.ac.bolton.archimate.editor.ui.services.UIRequestManager;
import uk.ac.bolton.archimate.editor.ui.services.ViewManager;
import uk.ac.bolton.archimate.editor.views.AbstractModelView;
import uk.ac.bolton.archimate.editor.views.tree.actions.CloseModelAction;
import uk.ac.bolton.archimate.editor.views.tree.actions.DeleteAction;
import uk.ac.bolton.archimate.editor.views.tree.actions.DuplicateAction;
import uk.ac.bolton.archimate.editor.views.tree.actions.IViewerAction;
import uk.ac.bolton.archimate.editor.views.tree.actions.LinkToEditorAction;
import uk.ac.bolton.archimate.editor.views.tree.actions.NewFolderAction;
import uk.ac.bolton.archimate.editor.views.tree.actions.OpenDiagramAction;
import uk.ac.bolton.archimate.editor.views.tree.actions.PropertiesAction;
import uk.ac.bolton.archimate.editor.views.tree.actions.RenameAction;
import uk.ac.bolton.archimate.editor.views.tree.actions.SaveModelAction;
import uk.ac.bolton.archimate.editor.views.tree.actions.TreeModelViewActionFactory;
import uk.ac.bolton.archimate.editor.views.tree.commands.DuplicateCommandHandler;
import uk.ac.bolton.archimate.editor.views.tree.search.SearchFilter;
import uk.ac.bolton.archimate.editor.views.tree.search.SearchWidget;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IFolder;


/**
 * Tree Model View
 * 
 * @author Phillip Beauvoir
 */
public class TreeModelView
extends AbstractModelView
implements ITreeModelView, IUIRequestListener {
    
    private TreeModelViewer fTreeViewer;
    
    private Composite fParentComposite;
    private SearchWidget fSearchWidget;
    private SearchFilter fSearchFilter; // Keep track of Search Filter so we can restore expanded nodes state
    
    private IAction fActionToggleSearchField;
    
    private IAction fActionNewModel;
    private IAction fActionOpenModel;
    private IAction fActionLinkToEditor;
    
    private IViewerAction fActionProperties;
    private IViewerAction fActionSaveModel;
    private IViewerAction fActionCloseModel;
    private IViewerAction fActionDelete;
    private IViewerAction fActionRename;
    private IViewerAction fActionOpenDiagram;
    private IViewerAction fActionNewFolder;
    private IViewerAction fActionDuplicate;
    
    public TreeModelView() {
    }
    
    @Override
    public void doCreatePartControl(Composite parent) {
        fParentComposite = parent;
        
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;
        parent.setLayout(layout);
        
        fTreeViewer = new TreeModelViewer(parent, SWT.NULL);
        fTreeViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        
        fTreeViewer.setInput(IEditorModelManager.INSTANCE);
        
        /*
         * Listen to Double-click and press Return Action
         */
        fTreeViewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                handleOpenAction();
            }
        });
        
        // Tree selection listener
        fTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                // Update actions
                updateActions();
            }
        });
        
        // Register selections
        getSite().setSelectionProvider(getViewer());
        
        // Add Selection Sync
        TreeSelectionSynchroniser.INSTANCE.setTreeModelView(this);
        
        // Register us as a UIRequest Listener
        UIRequestManager.INSTANCE.addListener(this);
        
        // Search Filter
        fSearchFilter = new SearchFilter(fTreeViewer);

        makeActions();
        hookContextMenu();
        registerGlobalActions();
        makeLocalToolBar();
        
        // Drag support
        new TreeModelViewerDragDropHandler(fTreeViewer);
        
        // Expand tree elements
        TreeStateHelper.INSTANCE.restoreExpandedTreeElements(fTreeViewer);
        
        // This will update previous Undo/Redo text if Tree was closed before
        updateActions();
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    public void init(IViewSite site, IMemento memento) throws PartInitException {
        super.init(site, memento);
        // Restore expanded tree state
        TreeStateHelper.INSTANCE.setMemento(memento);
    }
    
    @Override
    public void saveState(IMemento memento) {
        // Save expanded tree state
        TreeStateHelper.INSTANCE.saveStateOnApplicationClose(memento);
    }
    
    /**
     * Open Objects could be a selection of various types and user presses Return key
     */
    private void handleOpenAction() {
        for(Object selected : ((IStructuredSelection)getViewer().getSelection()).toArray()) {
            // Element or Folder - open Properties view
            if(selected instanceof IArchimateElement) {
                ViewManager.showViewPart(ViewManager.PROPERTIES_VIEW, true);
            }
            // Folder - open Properties view
            if(selected instanceof IFolder) {
                ViewManager.showViewPart(ViewManager.PROPERTIES_VIEW, true);
            }
            // Model - open Properties view
            else if(selected instanceof IArchimateModel) {
                ViewManager.showViewPart(ViewManager.PROPERTIES_VIEW, true);
            }
            // Diagram - open diagram
            else if(selected instanceof IDiagramModel) {
                EditorManager.openDiagramEditor((IDiagramModel)selected);
            }
        }
    }
    
    /**
     * Show the Search Widget
     */
    private void showSearchWidget() {
        fSearchWidget = new SearchWidget(fParentComposite, fSearchFilter);
        fSearchWidget.moveAbove(fTreeViewer.getControl());
        fTreeViewer.addFilter(fSearchFilter);
        fParentComposite.layout();
        fSearchWidget.setFocus();
    }
    
    /**
     * Hide the Search Widget
     */
    private void hideSearchWidget() {
        if(fSearchWidget != null && !fSearchWidget.isDisposed()) {
            fSearchWidget.dispose();
            fSearchWidget = null;
            fParentComposite.layout();
            fTreeViewer.getTree().setRedraw(false);
            fTreeViewer.removeFilter(fSearchFilter);
            fSearchFilter.clear();
            fTreeViewer.getTree().setRedraw(true);
        }
    }
    
    @Override
    public void setFocus() {
        fTreeViewer.getControl().setFocus();
    }
    
    /**
     * @return The Selection Provider
     */
    public ISelectionProvider getSelectionProvider() {
        return fTreeViewer;
    }
    
    public TreeModelViewer getViewer() {
        return fTreeViewer;
    }
    
    /**
     * Make local actions
     */
    private void makeActions() {
        IWorkbenchWindow window = getViewSite().getWorkbenchWindow();
        
        fActionNewModel = new NewArchimateModelAction();
        fActionOpenModel = new OpenModelAction(window);
        
        fActionOpenDiagram = new OpenDiagramAction(getSelectionProvider());

        fActionCloseModel = new CloseModelAction(getSelectionProvider());
        fActionSaveModel = new SaveModelAction(this);
        
        fActionDelete = new DeleteAction(getViewer());
        
        fActionRename = new RenameAction(getViewer());
        
        fActionProperties = new PropertiesAction(getSelectionProvider());
        
        fActionLinkToEditor = new LinkToEditorAction();
        
        fActionNewFolder = new NewFolderAction(getSelectionProvider());
        
        fActionDuplicate = new DuplicateAction(getViewer());
        
        fActionToggleSearchField = new Action("", IAction.AS_CHECK_BOX) {
            @Override
            public void run() {
                if(isChecked()) {
                    showSearchWidget();
                }
                else {
                    hideSearchWidget();
                }
            };
        };
        fActionToggleSearchField.setToolTipText("Search");
        fActionToggleSearchField.setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_SEARCH_16));
    }
    
    /**
     * Register Global Action Handlers
     */
    private void registerGlobalActions() {
        IActionBars actionBars = getViewSite().getActionBars();
        
        // Register our interest in the global menu actions
        actionBars.setGlobalActionHandler(ArchimateEditorActionFactory.CLOSE_MODEL.getId(), fActionCloseModel);
        actionBars.setGlobalActionHandler(ArchimateEditorActionFactory.OPEN_DIAGRAM.getId(), fActionOpenDiagram);
        actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), fActionDelete);
        actionBars.setGlobalActionHandler(ActionFactory.PROPERTIES.getId(), fActionProperties);
        actionBars.setGlobalActionHandler(ActionFactory.RENAME.getId(), fActionRename);
        actionBars.setGlobalActionHandler(ArchimateEditorActionFactory.DUPLICATE.getId(), fActionDuplicate);
    }
    
    /**
     * Hook into a right-click menu
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#TreeModelViewPopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                fillContextMenu(manager);
            }
        });
        
        Menu menu = menuMgr.createContextMenu(getViewer().getControl());
        getViewer().getControl().setMenu(menu);
        
        getSite().registerContextMenu(menuMgr, getViewer());
    }
    
    /**
     * Fill context menu when user right-clicks
     * @param manager
     */
    private void fillContextMenu(IMenuManager manager) {
        IStructuredSelection selection = ((IStructuredSelection)getViewer().getSelection());
        Object selected = selection.getFirstElement();
        boolean isEmpty = selected == null;
        
        if(isEmpty) {
            manager.add(fActionNewModel);
            manager.add(fActionOpenModel);
            return;
        }
        
        MenuManager newMenu = new MenuManager("New"); //$NON-NLS-1$
        manager.add(newMenu);

        manager.add(new Separator());
        
        // Selected model
        if(selected instanceof IArchimateModel) {
            manager.add(fActionCloseModel);
            manager.add(fActionSaveModel);
            manager.add(new Separator());
        }
        
        // Selected Diagram
        if(selected instanceof IDiagramModel) {
            manager.add(fActionOpenDiagram);
            manager.add(new Separator("open"));
        }
        
        if(selected instanceof IFolder) {
            newMenu.add(fActionNewFolder);
            newMenu.add(new Separator());
        }
        
        // Create "New" Actions
        List<IAction> actions = TreeModelViewActionFactory.INSTANCE.getNewObjectActions(selected);
        if(!actions.isEmpty()) {
            for(IAction action : actions) {
                newMenu.add(action);
            }
        }
        
        newMenu.add(new Separator());
        getSite().registerContextMenu(ID + ".new_menu", newMenu, getViewer());
       
        if(!isEmpty) {
            manager.add(new Separator());
            manager.add(fActionDelete);
            manager.add(fActionRename);
            manager.add(new Separator());
            if(DuplicateCommandHandler.canDuplicate(selection)) {
                manager.add(fActionDuplicate);
                manager.add(new Separator());
            }
            manager.add(fActionProperties);
        }
        
        // Other plug-ins can contribute their actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }
    
    /**
     * Update the Local Actions depending on the selection 
     */
    private void updateActions() {
        IStructuredSelection selection = (IStructuredSelection)getViewer().getSelection();
        fActionSaveModel.update(selection);
        fActionOpenDiagram.update(selection);
        fActionCloseModel.update(selection);
        fActionDelete.update(selection);
        fActionDuplicate.update(selection);
        fActionRename.update(selection);
        fActionProperties.update(selection);
        fActionNewFolder.update(selection);
        updateUndoActions();
    }
    
    /**
     * Populate the ToolBar
     */
    private void makeLocalToolBar() {
        IActionBars bars = getViewSite().getActionBars();
        IToolBarManager manager = bars.getToolBarManager();
        manager.add(fActionToggleSearchField);
        manager.add(fActionLinkToEditor);
    }
    
    @Override
    public void dispose() {
        super.dispose();

        // Remove Selection Sync
        TreeSelectionSynchroniser.INSTANCE.removeTreeModelView();
        
        // Remove UI Request Listener
        UIRequestManager.INSTANCE.removeListener(this);
        
        // Save Editor Model List
        try {
            IEditorModelManager.INSTANCE.saveState();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    // ======================================================================
    // Listen to Property Changes from IEditorModelManager
    // ======================================================================
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        Object source = evt.getSource();
        //Object newValue = evt.getNewValue();
        
        // New Model created or opened
        if(propertyName == IEditorModelManager.PROPERTY_MODEL_CREATED ||
                propertyName == IEditorModelManager.PROPERTY_MODEL_OPENED) {
            getViewer().refresh();
            
            IArchimateModel model = (IArchimateModel)evt.getNewValue();
            
            // Expand and Select new node
            getViewer().expandToLevel(model, 1);
            // Expand views node
            getViewer().expandToLevel(model.getDefaultDiagramModel(), 1);

            getViewer().setSelection(new StructuredSelection(model), true);
        }
        
        // Model removed
        else if(propertyName == IEditorModelManager.PROPERTY_MODEL_REMOVED) {
            getViewer().refresh();
        }
        
        // Model dirty state, so update Actions and modified state of source (asterisk on model node)
        else if(propertyName == IEditorModelManager.COMMAND_STACK_CHANGED) {
            updateActions();
            getViewer().update(source, null);
        }
        
        // Ecore Events will come so turn tree refresh off
        else if(propertyName == IEditorModelManager.PROPERTY_ECORE_EVENTS_START) {
            super.propertyChange(evt);
            // Remove Syncing
            TreeSelectionSynchroniser.INSTANCE.removeTreeModelView();
        }
        
        // Ecore Events have finished so turn tree refresh on
        else if(propertyName == IEditorModelManager.PROPERTY_ECORE_EVENTS_END) {
            super.propertyChange(evt);
            // Add Syncing
            TreeSelectionSynchroniser.INSTANCE.setTreeModelView(this);
        }
        
        else {
            super.propertyChange(evt);
        }
    }
    
    // ======================================================================
    // Listen to UI Requests
    // ======================================================================
    
    @Override
    public void requestAction(UIRequest request) {
        // Request to select elements
        if(request instanceof TreeSelectionRequest) {
            TreeSelectionRequest req = (TreeSelectionRequest)request;
            if(req.shouldSelect(getViewer())) {
                getViewer().setSelection(req.getSelection(), req.doReveal());
            }
        }
        // Request element name in-place
        else if(request instanceof TreeEditElementRequest) {
            getViewer().editElement(request.getTarget());
        }
    }
    
    // =================================================================================
    //                       React to ECore Model Changes
    // =================================================================================
    
    @Override
    protected void eCoreChanged(Notification msg) {
        int type = msg.getEventType();
        
        // Attribute set
        if(type == Notification.SET) {
            Object notifier = msg.getNotifier();
            Object feature = msg.getFeature();

            // Relationship/Connection changed - update element's name
            if(feature == IArchimatePackage.Literals.RELATIONSHIP__SOURCE ||
                                        feature == IArchimatePackage.Literals.RELATIONSHIP__TARGET) {
                getViewer().update(notifier, null);
            }
            
            // Viewpoint changed
            else if(feature == IArchimatePackage.Literals.ARCHIMATE_DIAGRAM_MODEL__VIEWPOINT) {
                if(Preferences.STORE.getBoolean(IPreferenceConstants.VIEWPOINTS_FILTER_MODEL_TREE)) {
                    getViewer().refresh();
                }
            }
            
            else {
                super.eCoreChanged(msg);
            }
        }
        else {
            super.eCoreChanged(msg);
        }
    }

    // =================================================================================
    //                       Contextual Help support
    // =================================================================================

    public int getContextChangeMask() {
        return NONE;
    }

    public IContext getContext(Object target) {
        return HelpSystem.getContext(HELP_ID);
    }

    public String getSearchExpression(Object target) {
        return "Model Tree";
    }
}