/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree;

import java.beans.PropertyChangeEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.commands.ActionHandler;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
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
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.handlers.IHandlerService;
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
import com.archimatetool.editor.ui.services.IUIRequestListener;
import com.archimatetool.editor.ui.services.UIRequest;
import com.archimatetool.editor.ui.services.UIRequestManager;
import com.archimatetool.editor.ui.services.ViewManager;
import com.archimatetool.editor.utils.StringUtils;
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
import com.archimatetool.editor.views.tree.search.SearchWidget;
import com.archimatetool.model.FolderType;
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
    
    private SearchWidget fSearchWidget;
    
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
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;
        parent.setLayout(layout);
        
        fTreeViewer = new TreeModelViewer(getSite().getWorkbenchWindow(), parent, SWT.NULL);
        fTreeViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        
        // Drill down
        fDrillDownAdapter = new DrillDownAdapter(fTreeViewer);
        
        // Listen to Double-click and press Return Action
        fTreeViewer.addDoubleClickListener(event -> {
            handleOpenAction();
        });
        
        // Tree selection listener
        fTreeViewer.addSelectionChangedListener(event -> {
            // Update actions
            updateActions();
        });
        
        // Register selections
        getSite().setSelectionProvider(getViewer());
        
        // Add Selection Sync
        fSynchroniser = new TreeSelectionSynchroniser(this);
        
        // Register us as a UIRequest Listener
        UIRequestManager.INSTANCE.addListener(this);
        
        makeActions();
        hookContextMenu();
        registerGlobalActions();
        makeLocalToolBar();
        makeLocalMenuActions();
        
        // Drag support
        new TreeModelViewerDragDropHandler(fTreeViewer);
        
        // Set model input now
        fTreeViewer.setInput(IEditorModelManager.INSTANCE);
        
        // Expand tree elements after model input
        TreeStateHelper.INSTANCE.restoreExpandedTreeElements(fTreeViewer);

        // This will update previous Undo/Redo text if Tree was closed before
        updateActions();
    }
    
    @Override
    public void init(IViewSite site, IMemento memento) throws PartInitException {
        super.init(site, memento);
        // Set memento with expanded tree state when creating the Tree
        TreeStateHelper.INSTANCE.setMemento(memento);
    }
    
    /**
     * This is called:
     * 1. Every 5 minutes by the workbench autosaving the workbench state (this can be set in org.eclipse.ui.internal.IPreferenceConstants#WORKBENCH_SAVE_INTERVAL)
     * 2. When this ViewPart is closed
     * 3. When the app quits (which is really 2)
     */
    @Override
    public void saveState(IMemento memento) {
        // Save expanded tree state
        TreeStateHelper.INSTANCE.saveStateToMemento(fTreeViewer, memento);
    }
    
    /**
     * This is called on double click and when the Return key is pressed. Both cases can be multi-select.
     */
    private void handleOpenAction() {
        // If a concept, folder or model is selected open the Properties view
        boolean openPropertiesView = getViewer().getStructuredSelection().stream().anyMatch(item -> !(item instanceof IDiagramModel));
        if(openPropertiesView) {
            ViewManager.showViewPart(ViewManager.PROPERTIES_VIEW, false);
        }
        
        // Diagrams
        fActionOpenDiagram.run();
    }
    
    /**
     * Show the Search Widget
     */
    private void showSearchWidget() {
        fSearchWidget = new SearchWidget(fTreeViewer);
        fSearchWidget.moveAbove(fTreeViewer.getControl());
        fTreeViewer.getControl().getParent().layout();
        fSearchWidget.setFocus();
    }
    
    /**
     * Remove the Search Widget
     */
    private void removeSearchWidget() {
        if(fSearchWidget != null && !fSearchWidget.isDisposed()) {
            fSearchWidget.close();
            fSearchWidget = null;
            fTreeViewer.getControl().getParent().layout();
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
        
        fActionLinkToEditor = new LinkToEditorAction();
        
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
                    removeSearchWidget();
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
                for(Object o : getViewer().getStructuredSelection().toArray()) {
                    if(fTreeViewer.isExpandable(o) && fTreeViewer.getExpandedState(o)) {
                        fTreeViewer.collapseToLevel(o, AbstractTreeViewer.ALL_LEVELS);
                    }
                }
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_COLLAPSEALL);
            }
            
            @Override
            public String getActionDefinitionId() {
                return IWorkbenchCommandConstants.NAVIGATE_COLLAPSE_ALL;
            }
        };
        
        fActionExpandSelected = new Action(Messages.TreeModelView_4) {
            @Override
            public void run() {
                for(Object o : getViewer().getStructuredSelection().toArray()) {
                    if(hasExpandableNodes(o)) {
                        fTreeViewer.expandToLevel(o, AbstractTreeViewer.ALL_LEVELS);
                    }
                }
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_EXPANDALL);
            }
            
            @Override
            public String getActionDefinitionId() {
                return IWorkbenchCommandConstants.NAVIGATE_EXPAND_ALL;
            }
        };
        
        // Add these actions to the key binding service
        IHandlerService handlerService = getSite().getService(IHandlerService.class);
        handlerService.activateHandler(IWorkbenchCommandConstants.NAVIGATE_COLLAPSE_ALL, new ActionHandler(fActionCollapseSelected));
        handlerService.activateHandler(IWorkbenchCommandConstants.NAVIGATE_EXPAND_ALL, new ActionHandler(fActionExpandSelected));
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
        
        menuMgr.addMenuListener(manager -> {
            fillContextMenu(manager);
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
        IStructuredSelection selection = getViewer().getStructuredSelection();
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
        if(fActionOpenDiagram.isEnabled()) {
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
     * Make local toolbar actions
     */
    private void makeLocalMenuActions() {
        IActionBars actionBars = getViewSite().getActionBars();
        
        // Local menu items go here
        IMenuManager manager = actionBars.getMenuManager();
        
        // Filter folder action
        class FolderFilterAction extends Action {
            FolderType folderType;
            String prefsKey;
            
            ViewerFilter filter = new ViewerFilter() {
                @Override
                public boolean select(Viewer viewer, Object parentElement, Object element) {
                    return element instanceof IFolder folder ? folder.getType() != folderType : true;
                }
            };
            
            FolderFilterAction(FolderType folderType) {
                this.folderType = folderType;
                prefsKey = "modelTreeFolderHidden_" + folderType.getName(); //$NON-NLS-1$
                setText(StringUtils.escapeAmpersandsInText(folderType.getLabel()));
                
                boolean hidden = ArchiPlugin.getInstance().getPreferenceStore().getBoolean(prefsKey);
                setChecked(!hidden);
                if(hidden) {
                    getViewer().addFilter(filter);
                }
            }
            
            @Override
            public void run() {
                if(isChecked()) {
                    getViewer().removeFilter(filter);
                }
                else {
                    getViewer().addFilter(filter);
                }
                
                ArchiPlugin.getInstance().getPreferenceStore().setValue(prefsKey, !isChecked());
            }
        }
        
        MenuManager filterMenu = new MenuManager(Messages.TreeModelView_5);
        manager.add(filterMenu);
        
        List<FolderFilterAction> filterActions = new ArrayList<>();
        
        filterActions.add(new FolderFilterAction(FolderType.STRATEGY));
        filterActions.add(new FolderFilterAction(FolderType.BUSINESS));
        filterActions.add(new FolderFilterAction(FolderType.APPLICATION));
        filterActions.add(new FolderFilterAction(FolderType.TECHNOLOGY));
        filterActions.add(new FolderFilterAction(FolderType.MOTIVATION));
        filterActions.add(new FolderFilterAction(FolderType.IMPLEMENTATION_MIGRATION));
        filterActions.add(new FolderFilterAction(FolderType.OTHER));
        filterActions.add(new FolderFilterAction(FolderType.RELATIONS));
        filterActions.add(new FolderFilterAction(FolderType.DIAGRAMS));
        
        for(Action action : filterActions) {
            filterMenu.add(action);
        }
        
        filterMenu.add(new Separator());
        
        // Show All
        filterMenu.add(new Action(Messages.TreeModelView_6) {
            @Override
            public void run() {
                List<ViewerFilter> filtersToRemove = new ArrayList<>();
                
                for(FolderFilterAction action : filterActions) {
                    if(!action.isChecked()) {
                        action.setChecked(true);
                        ArchiPlugin.getInstance().getPreferenceStore().setValue(action.prefsKey, false);
                        filtersToRemove.add(action.filter);
                    }
                }
                
                if(filtersToRemove.isEmpty()) {
                    return;
                }
                
                // Remove the filters in one operation by calling Viewer#setFilters.
                // This ensures that the tree is refreshed only once.
                ViewerFilter[] filters = Arrays.stream(getViewer().getFilters())
                                               .filter(e -> !filtersToRemove.contains(e))
                                               .toArray(ViewerFilter[]::new);
                
                getViewer().setFilters(filters);
            }
        });
    }
    
    /**
     * @return true if the node containing object, or any of its child nodes, can be expanded
     */
    private boolean hasExpandableNodes(Object object) {
        if(fTreeViewer.isExpandable(object) && !fTreeViewer.getExpandedState(object)) {
            return true;
        }
        if(object instanceof IFolderContainer container) {
            for(IFolder folder : container.getFolders()) {
                if(hasExpandableNodes(folder)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Check the tree's input is not a deleted object. If it is, set drilldown to home
     */
    private void checkDrillDownHasValidInput() {
        if(fTreeViewer.getInput() instanceof IArchimateModelObject modelObject && modelObject.getArchimateModel() == null) {
            setDrillDownHome();
        }
    }
    
    /**
     * Set the drill down to home
     */
    private void setDrillDownHome() {
        if(fDrillDownAdapter.canGoHome()) { // Important check!
            try {
                getViewer().getControl().setRedraw(false);
                fDrillDownAdapter.goHome();
            }
            finally {
                getViewer().getControl().setRedraw(true);
            }
        }
    }
    
    @Override
    protected void applicationPreferencesChanged(org.eclipse.jface.util.PropertyChangeEvent event) {
        switch(event.getProperty()) {
            case IPreferenceConstants.HIGHLIGHT_UNUSED_ELEMENTS_IN_MODEL_TREE,
                 IPreferenceConstants.VIEWPOINTS_FILTER_MODEL_TREE,
                 IPreferenceConstants.SHOW_SPECIALIZATION_ICONS_IN_MODEL_TREE -> {
                getViewer().update();
            }
            
            case IPreferenceConstants.TREE_DISPLAY_NODE_INCREMENT -> {
                int limit = ArchiPlugin.getInstance().getPreferenceStore().getInt(IPreferenceConstants.TREE_DISPLAY_NODE_INCREMENT);
                getViewer().setDisplayIncrementally(limit);
                if(fDrillDownAdapter.canGoHome()) { // We are drilled in
                    setDrillDownHome(); // This will call setInput on Viewer and restore expanded nodes
                }
                else {
                    getViewer().setInputPreservingExpandedNodes(IEditorModelManager.INSTANCE);
                }
            }
            
            case IPreferenceConstants.TREE_ALPHANUMERIC_SORT -> {
                getViewer().setUseAlphanumericComparator((Boolean)event.getNewValue());
            }
        }

        if(event.getProperty().startsWith(IPreferenceConstants.FOLDER_COLOUR_PREFIX)) {
            getViewer().update();
        }
    }

    @Override
    protected IArchimateModel getActiveArchimateModel() {
        // viewer can be null if model dirty, focus is on tree, a diagram is open, you close the app
        // and Eclipse tries to refresh some toolbar items
        if(getViewer() != null) { 
            Object selected = getViewer().getStructuredSelection().getFirstElement();
            
            if(selected instanceof IArchimateModelObject modelObject) {
                return modelObject.getArchimateModel();
            }
            
            if(getViewer().getInput() instanceof IArchimateModelObject modelObject) {
                return modelObject.getArchimateModel();
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
        fSynchroniser = null;
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
        switch(evt.getPropertyName()) {
            // New Model created or opened
            case IEditorModelManager.PROPERTY_MODEL_CREATED,
                 IEditorModelManager.PROPERTY_MODEL_OPENED -> {
                // Go Home
                setDrillDownHome();
                
                getViewer().refreshTreePreservingExpandedNodes();
                
                // Expand and Select new node
                IArchimateModel model = (IArchimateModel)evt.getNewValue();
                getViewer().expandToLevel(model.getFolder(FolderType.DIAGRAMS), 1);
                getViewer().setSelection(new StructuredSelection(model), true);
                
                // Update Search Widget
                if(fSearchWidget != null) {
                    fSearchWidget.update(evt);
                }
            }
            
            // Model removed
            case IEditorModelManager.PROPERTY_MODEL_REMOVED -> {
                // Clear Cut/Paste clipboard
                TreeModelCutAndPaste.INSTANCE.clear();
                
                // Check Drilldown state
                checkDrillDownHasValidInput();

                // Update Search Widget
                if(fSearchWidget != null) {
                    fSearchWidget.update(evt);
                }

                getViewer().refreshTreePreservingExpandedNodes();
            }
            
            // Model dirty state, so update Actions and modified state of source (asterisk on model node)
            case IEditorModelManager.COMMAND_STACK_CHANGED -> {
                updateActions();
                getViewer().update(evt.getSource(), null);
            }
            
            // Ecore events will come...
            case IEditorModelManager.PROPERTY_ECORE_EVENTS_START -> {
                super.propertyChange(evt);
                // Suspend Syncing
                fSynchroniser.setSynchronise(false);
            }
            
            // ECore events have finished
            case IEditorModelManager.PROPERTY_ECORE_EVENTS_END -> {
                super.propertyChange(evt);
                // Reactivate Syncing
                fSynchroniser.setSynchronise(true);
            }
            
            default -> {
                super.propertyChange(evt);
            }
        }
    }
    
    // ======================================================================
    // Listen to UI Requests
    // ======================================================================
    
    @Override
    public void requestAction(UIRequest request) {
        // Request to select elements
        if(request instanceof TreeSelectionRequest req) {
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
        // If this is a viewpoint update return
        if(refreshViewPoint(msg)) {
            return;
        }
        
        super.eCoreChanged(msg);
        
        checkDrillDownHasValidInput();
        
        // Update search widget
        if(fSearchWidget != null) {
            fSearchWidget.update(msg);
        }
    }
    
    @Override
    protected void doRefreshFromNotifications(final List<Notification> notifications) {
        Display.getCurrent().asyncExec(() -> {
            if(!getViewer().getControl().isDisposed() && notifications != null) { // check inside run loop
                refreshFromNotifications(notifications);
            }
        });
    }
    
    @Override
    protected Set<EObject> getElementsToUpdateFromNotification(Notification msg) {
        Set<EObject> list = super.getElementsToUpdateFromNotification(msg);
        
        // Update Specialization icons if preference set
        if(msg.getFeature() == IArchimatePackage.Literals.PROFILES__PROFILES
                               && ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.SHOW_SPECIALIZATION_ICONS_IN_MODEL_TREE)) {
            list.add((EObject)msg.getNotifier());
        }
        
        return list;
    }
    
    private void refreshFromNotifications(List<Notification> notifications) {
        Set<EObject> refreshElements = new HashSet<>();
        Set<EObject> updateElements = new HashSet<>();
        
        for(Notification msg : notifications) {
            // If this is a viewpoint update continue
            if(refreshViewPoint(msg)) {
                continue;
            }
            
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
        
        checkDrillDownHasValidInput();
        
        // Update Search Widget
        if(fSearchWidget != null) {
            fSearchWidget.update(notifications);
        }
    }

    /**
     * Check if msg is a set viewpoint message and update all model nodes if preference enabled
     * @return true if msg was a set viewpoint message
     */
    private boolean refreshViewPoint(Notification msg) {
        boolean isSetViewpoint = msg.getFeature() == IArchimatePackage.Literals.ARCHIMATE_DIAGRAM_MODEL__VIEWPOINT
                                                     && msg.getEventType() == Notification.SET
                                                     && msg.getNotifier() instanceof IDiagramModel;
        
        // If preference set then update all model nodes
        if(isSetViewpoint && ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.VIEWPOINTS_FILTER_MODEL_TREE)) {
            getViewer().update(((IDiagramModel)msg.getNotifier()).getArchimateModel());
        }
        
        return isSetViewpoint;
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