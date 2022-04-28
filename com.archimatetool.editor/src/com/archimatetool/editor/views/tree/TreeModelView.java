/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.AbstractTreeViewer;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.DrillDownAdapter;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.actions.ArchiActionFactory;
import com.archimatetool.editor.actions.NewArchimateModelAction;
import com.archimatetool.editor.actions.OpenModelAction;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.ThemeUtils;
import com.archimatetool.editor.ui.findreplace.IFindReplaceProvider;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.editor.ui.services.IUIRequestListener;
import com.archimatetool.editor.ui.services.UIRequest;
import com.archimatetool.editor.ui.services.UIRequestManager;
import com.archimatetool.editor.ui.services.ViewManager;
import com.archimatetool.editor.views.AbstractModelView;
import com.archimatetool.editor.views.tree.actions.CloseModelAction;
import com.archimatetool.editor.views.tree.actions.CutAction;
import com.archimatetool.editor.views.tree.actions.DeleteAction;
import com.archimatetool.editor.views.tree.actions.DuplicateAction;
import com.archimatetool.editor.views.tree.actions.FindReplaceAction;
import com.archimatetool.editor.views.tree.actions.GenerateViewAction;
import com.archimatetool.editor.views.tree.actions.IViewerAction;
import com.archimatetool.editor.views.tree.actions.LinkToEditorAction;
import com.archimatetool.editor.views.tree.actions.OpenDiagramAction;
import com.archimatetool.editor.views.tree.actions.PasteAction;
import com.archimatetool.editor.views.tree.actions.PropertiesAction;
import com.archimatetool.editor.views.tree.actions.RenameAction;
import com.archimatetool.editor.views.tree.actions.SaveModelAction;
import com.archimatetool.editor.views.tree.commands.DuplicateCommandHandler;
import com.archimatetool.editor.views.tree.search.SearchFilter;
import com.archimatetool.editor.views.tree.search.SearchWidget;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IFolderContainer;



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
    private IAction fActionFindReplace;
    private IAction fActionCollapseSelected;
    private IAction fActionExpandSelected;
    
    private IViewerAction fActionProperties;
    private IViewerAction fActionSaveModel;
    private IViewerAction fActionCloseModel;
    private IViewerAction fActionDelete;
    private IViewerAction fActionRename;
    private IViewerAction fActionOpenDiagram;
    private IViewerAction fActionDuplicate;
    
    private IViewerAction fActionCut;
    private IViewerAction fActionPaste;
    
    private IViewerAction fActionGenerateView;
    
    private TreeModelViewerFindReplaceProvider fFindReplaceProvider;
    
    private TreeSelectionSynchroniser fSynchroniser;
    
    private DrillDownAdapter fDrillDownAdapter;
    
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
        
        fDrillDownAdapter = new DrillDownAdapter(fTreeViewer);
        
        /*
         * Listen to Double-click and press Return Action
         */
        fTreeViewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                handleOpenAction();
            }
        });
        
        // Tree selection listener
        fTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                // Update actions
                updateActions();
            }
        });
        
        // Register selections
        getSite().setSelectionProvider(getViewer());
        
        // Add Selection Sync
        fSynchroniser = new TreeSelectionSynchroniser(getViewer());
        
        // Register us as a UIRequest Listener
        UIRequestManager.INSTANCE.addListener(this);
        
        // Search Filter
        fSearchFilter = new SearchFilter(fTreeViewer);
        fTreeViewer.addFilter(fSearchFilter);
        
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
        // Restore expanded tree state from file when opening for first time
        TreeStateHelper.INSTANCE.setMemento(memento);
    }
    
    @Override
    public void saveState(IMemento memento) {
        // saveState() is called periodically by Eclipse so only do this when closing the workbench
        if(PlatformUI.getWorkbench().isClosing()) {
            // Reset drill-down
            if(fDrillDownAdapter.canGoHome()) {
                try {
                    getViewer().getControl().setRedraw(false);
                    fDrillDownAdapter.goHome();
                }
                finally {
                    getViewer().getControl().setRedraw(true);
                }
            }
            
            // Save expanded tree state
            TreeStateHelper.INSTANCE.saveStateOnApplicationClose(fTreeViewer, memento);
        }
    }
    
    /**
     * Open Objects could be a selection of various types and user presses Return key
     */
    private void handleOpenAction() {
        for(Object selected : ((IStructuredSelection)getViewer().getSelection()).toArray()) {
            // Element or Relation - open Properties view
            if(selected instanceof IArchimateConcept) {
                ViewManager.showViewPart(ViewManager.PROPERTIES_VIEW, false);
            }
            // Folder - open Properties view
            if(selected instanceof IFolder) {
                ViewManager.showViewPart(ViewManager.PROPERTIES_VIEW, false);
            }
            // Model - open Properties view
            else if(selected instanceof IArchimateModel) {
                ViewManager.showViewPart(ViewManager.PROPERTIES_VIEW, false);
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
            try {
                fSearchFilter.clear();
            }
            finally {
                fTreeViewer.getTree().setRedraw(true);
            }
        }
    }
    
    @Override
    public void setFocus() {
        fTreeViewer.getControl().setFocus();
    }
    
    @Override
    protected void selectAll() {
        if(fTreeViewer != null) {
            fTreeViewer.getTree().selectAll();
        }
    }
    
    /**
     * @return The Selection Provider
     */
    @Override
    public ISelectionProvider getSelectionProvider() {
        return fTreeViewer;
    }
    
    @Override
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
        
        fActionFindReplace = new FindReplaceAction(this);
        
        fActionProperties = new PropertiesAction(getSelectionProvider());
        
        fActionLinkToEditor = new LinkToEditorAction(fSynchroniser);
        
        fActionDuplicate = new DuplicateAction(getViewer());
        
        fActionCut = new CutAction(getViewer());
        fActionPaste = new PasteAction(getViewer());
        
        fActionGenerateView = new GenerateViewAction(getSelectionProvider());
        
        fActionToggleSearchField = new Action("", IAction.AS_CHECK_BOX) { //$NON-NLS-1$
            @Override
            public void run() {
                if(isChecked()) {
                    showSearchWidget();
                }
                else {
                    hideSearchWidget();
                }
            };
            
            @Override
            public String getToolTipText() {
                return Messages.TreeModelView_0;
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ThemeUtils.isDarkTheme() ? IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_SEARCH_LIGHT) :
                                                  IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_SEARCH);
            }
        };
        
        fActionCollapseSelected = new Action(Messages.TreeModelView_3) {
            @Override
            public void run() {
                IStructuredSelection selection = ((IStructuredSelection)getViewer().getSelection());
                for(Object o : selection.toArray()) {
                    if(fTreeViewer.isExpandable(o) && fTreeViewer.getExpandedState(o)) {
                        fTreeViewer.collapseToLevel(o, AbstractTreeViewer.ALL_LEVELS);
                    }
                }
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_COLLAPSEALL);
            }
        };
        
        fActionExpandSelected = new Action(Messages.TreeModelView_4) {
            @Override
            public void run() {
                IStructuredSelection selection = ((IStructuredSelection)getViewer().getSelection());
                for(Object o : selection.toArray()) {
                    if(hasExpandableNodes(o)) {
                        fTreeViewer.expandToLevel(o, AbstractTreeViewer.ALL_LEVELS);
                    }
                }
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_EXPANDALL);
            }
        };
    }
    
    /**
     * Register Global Action Handlers
     */
    private void registerGlobalActions() {
        IActionBars actionBars = getViewSite().getActionBars();
        
        // Register our interest in the global menu actions
        actionBars.setGlobalActionHandler(ArchiActionFactory.CLOSE_MODEL.getId(), fActionCloseModel);
        actionBars.setGlobalActionHandler(ArchiActionFactory.OPEN_DIAGRAM.getId(), fActionOpenDiagram);
        actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), fActionDelete);
        actionBars.setGlobalActionHandler(ActionFactory.PROPERTIES.getId(), fActionProperties);
        actionBars.setGlobalActionHandler(ActionFactory.RENAME.getId(), fActionRename);
        actionBars.setGlobalActionHandler(ArchiActionFactory.DUPLICATE.getId(), fActionDuplicate);
        actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(), fActionCut);
        actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), fActionPaste);
        actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(), fActionFindReplace);
        actionBars.setGlobalActionHandler(ArchiActionFactory.GENERATE_VIEW.getId(), fActionGenerateView);
    }
    
    /**
     * Disable Global Actions
     * We need to do this when closing the View
     */
    private void disableGlobalActions() {
        // These two have to be manually done
        fActionFindReplace.setEnabled(false);
        fActionSelectAll.setEnabled(false);
        
        // This should setEnabled = false
        updateActions();
    }
    
    /**
     * Hook into a right-click menu
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#TreeModelViewPopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        
        menuMgr.addMenuListener(new IMenuListener() {
            @Override
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
        
        if(isEmpty && fTreeViewer.getInput() instanceof IEditorModelManager) {
            manager.add(fActionNewModel);
            manager.add(fActionOpenModel);
            return;
        }
        
        manager.add(new Separator("new")); //$NON-NLS-1$
        
        manager.add(new Separator());
        
        // Selected a Model
        if(selected instanceof IArchimateModel) {
            manager.add(fActionCloseModel);
            manager.add(fActionSaveModel);
            manager.add(new Separator());
        }
        
        // Selected a Diagram
        if(selected instanceof IDiagramModel) {
            manager.add(fActionOpenDiagram);
            manager.add(new Separator("open")); //$NON-NLS-1$
        }
        
        if(!isEmpty) {
            manager.add(new Separator());

            manager.add(fActionCut);
            manager.add(fActionPaste);
            manager.add(fActionDelete);
            
            manager.add(new Separator("start_collapse")); //$NON-NLS-1$
            
            // Expand selected
            for(Object o : selection.toArray()) {
                if(hasExpandableNodes(o)) {
                    manager.add(fActionExpandSelected);
                    break;
                }
            }

            // Collapse selected
            for(Object o : selection.toArray()) {
                if(fTreeViewer.isExpandable(o) && fTreeViewer.getExpandedState(o)) {
                    manager.add(fActionCollapseSelected);
                    break;
                }
            }

            manager.add(new Separator("end_collapse")); //$NON-NLS-1$
        }
        
        // Drill-down adapter
        if(fDrillDownAdapter.canGoInto() || fDrillDownAdapter.canGoBack() || fDrillDownAdapter.canGoHome()) {
            fDrillDownAdapter.addNavigationActions(manager);
            manager.add(new Separator("drill")); //$NON-NLS-1$
        }
        
        if(!isEmpty) {
            if(DuplicateCommandHandler.canDuplicate(selection)) {
                manager.add(fActionDuplicate);
            }
            
            manager.add(fActionRename);

            manager.add(new Separator("start_extensions")); //$NON-NLS-1$
            manager.add(fActionGenerateView);
            manager.add(new GroupMarker("append_extensions")); //$NON-NLS-1$
            manager.add(new Separator("end_extensions")); //$NON-NLS-1$
            
            manager.add(new Separator("start_properties")); //$NON-NLS-1$
            manager.add(fActionProperties);
            manager.add(new GroupMarker("append_properties")); //$NON-NLS-1$
            manager.add(new Separator("end_properties")); //$NON-NLS-1$
        }
        
        // Other plug-ins can contribute their actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }
    
    /**
     * Update the Local Actions depending on the selection 
     */
    private void updateActions() {
        fActionSaveModel.update();
        fActionOpenDiagram.update();
        fActionCloseModel.update();
        fActionDelete.update();
        fActionDuplicate.update();
        fActionCut.update();
        fActionPaste.update();
        fActionRename.update();
        fActionProperties.update();
        fActionGenerateView.update();
        
        updateUndoActions();
    }
    
    /**
     * Populate the ToolBar
     */
    private void makeLocalToolBar() {
        IActionBars bars = getViewSite().getActionBars();
        IToolBarManager manager = bars.getToolBarManager();

        fDrillDownAdapter.addNavigationActions(manager);
        manager.add(new Separator());
        
        manager.add(fActionToggleSearchField);
        manager.add(fActionLinkToEditor);
    }
    
    /**
     * @return true if the node containing object, or any of its child nodes, can be expanded
     */
    private boolean hasExpandableNodes(Object object) {
        if(fTreeViewer.isExpandable(object) && !fTreeViewer.getExpandedState(object)) {
            return true;
        }
        if(object instanceof IFolderContainer) {
            for(IFolder folder : ((IFolderContainer)object).getFolders()) {
                if(hasExpandableNodes(folder)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Reset drilldown if it is showing a deleted object
     */
    private void checkDrillDown() {
        if(fTreeViewer.getInput() instanceof IArchimateModelObject && ((IArchimateModelObject)fTreeViewer.getInput()).getArchimateModel() == null) {
            if(fDrillDownAdapter.canGoHome()) {
                fDrillDownAdapter.goHome();
            }
        }
    }
    
    @Override
    protected void applicationPreferencesChanged(org.eclipse.jface.util.PropertyChangeEvent event) {
        if(event.getProperty().startsWith(IPreferenceConstants.FOLDER_COLOUR_PREFIX)) {
            getViewer().refresh();
        }
    }

    @Override
    protected IArchimateModel getActiveArchimateModel() {
        // viewer can be null if model dirty, focus is on tree, a diagram is open, you close the app
        // and Eclipse tries to refresh some toolbar items
        if(getViewer() != null) { 
            Object selected = getViewer().getStructuredSelection().getFirstElement();
            
            if(selected instanceof IArchimateModelObject) {
                return ((IArchimateModelObject)selected).getArchimateModel();
            }
            
            if(getViewer().getInput() instanceof IArchimateModelObject) {
                return ((IArchimateModelObject)getViewer().getInput()).getArchimateModel();
            }
        }
        
        return null;
    }
    
    @Override
    public <T> T getAdapter(Class<T> adapter) {
        // Find/Replace Provider
        if(adapter == IFindReplaceProvider.class) {
            if(fFindReplaceProvider == null) {
                fFindReplaceProvider = new TreeModelViewerFindReplaceProvider(getViewer());
            }
            return adapter.cast(fFindReplaceProvider);
        }
        
        return super.getAdapter(adapter);
    }
    
    @Override
    public void dispose() {
        super.dispose();

        // Remove UI Request Listener
        UIRequestManager.INSTANCE.removeListener(this);
        
        // Save Editor Model List
        try {
            IEditorModelManager.INSTANCE.saveState();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        
        // Disable Global actions
        disableGlobalActions();
        
        // Garbage collection
        fTreeViewer = null;
        fFindReplaceProvider = null;
        fSearchFilter = null;
        fSynchroniser = null;
        fParentComposite = null;
        fDrillDownAdapter = null;
        fSearchWidget = null;
        
        fActionFindReplace = null;
        fActionProperties = null;
        fActionDuplicate = null;
        fActionCut = null;
        fActionPaste = null;
        fActionGenerateView = null;
        fActionToggleSearchField = null;
        fActionCollapseSelected = null;
        fActionExpandSelected = null;
        fActionLinkToEditor = null;
        fActionOpenDiagram = null;
        fActionCloseModel = null;
        fActionSaveModel = null;
        fActionDelete = null;
        
        // Clear Cut/Paste clipboard
        TreeModelCutAndPaste.INSTANCE.clear();
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
        if(propertyName == IEditorModelManager.PROPERTY_MODEL_CREATED || propertyName == IEditorModelManager.PROPERTY_MODEL_OPENED) {
            // Go Home
            if(fDrillDownAdapter.canGoHome()) {
                fDrillDownAdapter.goHome();
            }
            
            getViewer().refreshTreePreservingExpandedNodes();

            IArchimateModel model = (IArchimateModel)evt.getNewValue();
            
            // Expand and Select new node
            getViewer().expandToLevel(model.getFolder(FolderType.DIAGRAMS), 1);
            getViewer().setSelection(new StructuredSelection(model), true);
            
            // Search Filter soft reset on open model
            if(propertyName == IEditorModelManager.PROPERTY_MODEL_OPENED && 
                    fSearchWidget != null && !fSearchWidget.isDisposed()) {
                fSearchWidget.softReset();
            }
       }
        
        // Model removed
        else if(propertyName == IEditorModelManager.PROPERTY_MODEL_REMOVED) {
            // Clear Cut/Paste clipboard
            TreeModelCutAndPaste.INSTANCE.clear();
            
            // Check Drilldown state
            checkDrillDown();

            // Search Filter soft reset
            if(fSearchWidget != null && !fSearchWidget.isDisposed()) {
                fSearchWidget.softReset();
            }

            getViewer().refreshTreePreservingExpandedNodes();
        }
        
        // Model dirty state, so update Actions and modified state of source (asterisk on model node)
        else if(propertyName == IEditorModelManager.COMMAND_STACK_CHANGED) {
            updateActions();
            getViewer().update(source, null);
        }
        
        // Ecore Events will come so turn tree refresh off
        else if(propertyName == IEditorModelManager.PROPERTY_ECORE_EVENTS_START) {
            super.propertyChange(evt);
            // Suspend Syncing
            fSynchroniser.setSynchronise(false);
        }
        
        // Ecore Events have finished so turn tree refresh on
        else if(propertyName == IEditorModelManager.PROPERTY_ECORE_EVENTS_END) {
            super.propertyChange(evt);
            // Reactivate Syncing
            fSynchroniser.setSynchronise(true);
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
        Object notifier = msg.getNotifier();
        Object feature = msg.getFeature();
        
        // Attribute set
        if(type == Notification.SET) {
            // Viewpoint changed
            if(feature == IArchimatePackage.Literals.ARCHIMATE_DIAGRAM_MODEL__VIEWPOINT) {
                if(ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.VIEWPOINTS_FILTER_MODEL_TREE)) {
                    if(notifier instanceof IDiagramModel) {
                        IArchimateModel model = ((IDiagramModel)notifier).getArchimateModel();
                        getViewer().refreshInBackground(model);
                    }
                }
            }
            // Model renamed
            // This is too expensive in refreshing the whole tree
            //else if(feature == IArchimatePackage.Literals.NAMEABLE__NAME && notifier instanceof IArchimateModel) {
            //    getViewer().refreshTreePreservingExpandedNodes();
            //    getViewer().setSelection(new StructuredSelection(notifier), true);
            //}
            else {
                super.eCoreChanged(msg);
            }
        }
        else {
            super.eCoreChanged(msg);
            checkDrillDown();
        }
    }
    
    @Override
    protected void doRefreshFromNotifications(final List<Notification> notifications) {
        Display.getCurrent().asyncExec(new Runnable() {
            @Override
            public void run() {
                if(!getViewer().getControl().isDisposed()) { // check inside run loop
                    refreshFromNotifications(notifications);
                }
                
                // Call super
                TreeModelView.super.doRefreshFromNotifications(notifications);
            }
        });
    }
    
    private void refreshFromNotifications(List<Notification> notifications) {
        if(notifications == null) {
            return;
        }
        
        Set<EObject> refreshElements = new HashSet<>();
        Set<EObject> updateElements = new HashSet<>();
        
        for(Notification msg : notifications) {
            // Get parent nodes to refresh
            EObject parent = getParentToRefreshFromNotification(msg);
            if(parent != null) {
                refreshElements.add(parent);
            }
            
            // Get elements to update
            updateElements.addAll(getElementsToUpdateFromNotification(msg));
        }
        
        // Optimise refresh by refreshing only ancestors
        for(EObject object : new HashSet<>(refreshElements)) {
            for(EObject parent = object.eContainer(); parent != null; parent = parent.eContainer()) {
                if(refreshElements.contains(parent)) {
                    refreshElements.remove(object);
                }
            }
        }
        
        try {
            getViewer().getControl().setRedraw(false);

            for(EObject object : refreshElements) {
                getViewer().refresh(object);
            }

            for(EObject object : updateElements) {
                getViewer().update(object, null);
            }
        }
        finally {
            getViewer().getControl().setRedraw(true);
        }
        
        checkDrillDown();
    }

    // =================================================================================
    //                       Contextual Help support
    // =================================================================================

    @Override
    public int getContextChangeMask() {
        return NONE;
    }

    @Override
    public IContext getContext(Object target) {
        return HelpSystem.getContext(HELP_ID);
    }

    @Override
    public String getSearchExpression(Object target) {
        return Messages.TreeModelView_2;
    }
}