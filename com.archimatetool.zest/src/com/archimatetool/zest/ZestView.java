/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.zest;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.editor.views.AbstractModelView;
import com.archimatetool.editor.views.tree.actions.IViewerAction;
import com.archimatetool.editor.views.tree.actions.PropertiesAction;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.model.viewpoints.IViewpoint;
import com.archimatetool.model.viewpoints.ViewpointManager;



/**
 * Zest View
 * 
 * @author Phillip Beauvoir
 * @author Jean-Baptiste Sarrodie
 */
public class ZestView extends AbstractModelView
implements IZestView, ISelectionListener {
    
    private ZestGraphViewer fGraphViewer;
    
    private CLabel fLabel;
    
    private IAction fActionLayout;
    private IViewerAction fActionProperties;
    private IAction fActionPinContent;
    private IAction fActionCopyImageToClipboard;
    private IAction fActionExportImageToFile;
    
    // Depth Actions
    private IAction[] fDepthActions;
    private List<IAction> fViewpointActions;
    private List<IAction> fRelationshipActions;
    private IAction[] fDirectionActions;


    private DrillDownManager fDrillDownManager;
    
    @Override
    protected void doCreatePartControl(Composite parent) {
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;
        parent.setLayout(layout);
        
        fLabel = new CLabel(parent, SWT.NONE);
        fLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fGraphViewer = new ZestGraphViewer(parent, SWT.NONE);
        fGraphViewer.getGraphControl().setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        
        // spring is the default - we do need to set this here!
        fGraphViewer.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
        //fGraphViewer.setLayoutAlgorithm(new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
        //fGraphViewer.setLayoutAlgorithm(new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
        //fGraphViewer.setLayoutAlgorithm(new HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
        
        // Graph selection listener
        fGraphViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                // Update actions
                updateActions();
                // Need to do this in order for Tabbed Properties View to update on Selection
                getSite().getSelectionProvider().setSelection(event.getSelection());
            }
        });
        
        // Double-click
        fGraphViewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                fDrillDownManager.goInto();
            }
        });

        fDrillDownManager = new DrillDownManager(this);
        
        makeActions();
        hookContextMenu();
        registerGlobalActions();
        makeLocalToolBar();

        // This will update previous Undo/Redo text if View was closed before
        updateActions();

        // Register selections
        getSite().setSelectionProvider(getViewer());
        
        // Listen to global selections to update the viewer
        getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(fGraphViewer.getControl(), HELP_ID);
        
        // Initialise with whatever is selected in the workbench
        ISelection selection = getSite().getWorkbenchWindow().getSelectionService().getSelection();
        selectionChanged(null, selection);
    }
    
    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if(part == this) {
            return;
        }
        
        if(fActionPinContent.isChecked()) {
            return;
        }
        
        if(selection instanceof IStructuredSelection && !selection.isEmpty()) {
            Object object = ((IStructuredSelection)selection).getFirstElement();
            setElement(object);
        }
    }
    
    @Override
    protected void selectAll() {
        fGraphViewer.getGraphControl().selectAll();
    }
    
    private void setElement(Object object) {
        IArchimateConcept concept = null;
        
        if(object instanceof IArchimateConcept) {
            concept = (IArchimateConcept)object;
        }
        else if(object instanceof IAdaptable) {
            concept = ((IAdaptable)object).getAdapter(IArchimateConcept.class);
        }
        
        fDrillDownManager.setNewInput(concept);
        updateActions();
        
        updateLabel();
    }
    
    void refresh() {
        updateActions();
        updateLabel();
        getViewer().refresh();
    }
    
    /**
     * Update local label
     */
    void updateLabel() {
        String text = ArchiLabelProvider.INSTANCE.getLabel(fDrillDownManager.getCurrentConcept());
        text = StringUtils.escapeAmpersandsInText(text);
        
        String viewPointName = getContentProvider().getViewpointFilter().getName();
       	String relationshipName = getRelationshipFilterName(getContentProvider().getRelationshipFilter());	
        
        fLabel.setText(text + " (" + Messages.ZestView_5 + ": " + viewPointName + ", " + Messages.ZestView_6 + ": " + relationshipName + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        fLabel.setImage(ArchiLabelProvider.INSTANCE.getImage(fDrillDownManager.getCurrentConcept()));
    }

    /**
     * Update the Local Actions depending on the selection, and undo/redo actions
     */
    void updateActions() {
        IStructuredSelection selection = (IStructuredSelection)getViewer().getSelection();
        fActionProperties.update(selection);
        
        boolean hasData = fGraphViewer.getInput() != null;
        fActionExportImageToFile.setEnabled(hasData);
        fActionCopyImageToClipboard.setEnabled(hasData);
        fActionLayout.setEnabled(hasData);
        
        updateUndoActions();
    }
    
    /**
     * Populate the ToolBar
     */
    private void makeLocalToolBar() {
        IActionBars bars = getViewSite().getActionBars();
        
        IToolBarManager manager = bars.getToolBarManager();
        
        fDrillDownManager.addNavigationActions(manager);
        manager.add(new Separator());
        manager.add(fActionPinContent);
        manager.add(new Separator());
        manager.add(fActionLayout);
        
        final IMenuManager menuManager = bars.getMenuManager();

        IMenuManager depthMenuManager = new MenuManager(Messages.ZestView_3);
        menuManager.add(depthMenuManager); 

        // Depth Actions
        fDepthActions = new Action[6];
        for(int i = 0; i < fDepthActions.length; i++) {
            fDepthActions[i] = createDepthAction(i, i + 1);
            depthMenuManager.add(fDepthActions[i]);
        }
        
        // Set depth from prefs
        int depth = ArchiZestPlugin.INSTANCE.getPreferenceStore().getInt(IPreferenceConstants.VISUALISER_DEPTH);
        getContentProvider().setDepth(depth);
        fDepthActions[depth].setChecked(true);
        
        // Set filter based on Viewpoint
        IMenuManager viewpointMenuManager = new MenuManager(Messages.ZestView_5);
        menuManager.add(viewpointMenuManager);
        
        // Get viewpoint from prefs
        String viewpointID = ArchiZestPlugin.INSTANCE.getPreferenceStore().getString(IPreferenceConstants.VISUALISER_VIEWPOINT);
        getContentProvider().setViewpointFilter(ViewpointManager.INSTANCE.getViewpoint(viewpointID));
        
        // Viewpoint actions
        fViewpointActions = new ArrayList<IAction>();
        
        for(IViewpoint vp : ViewpointManager.INSTANCE.getAllViewpoints()) {
            IAction action = createViewpointMenuAction(vp);
            fViewpointActions.add(action);
            viewpointMenuManager.add(action);
            
            // Set checked
            if(vp.getID().equals(viewpointID)) {
                action.setChecked(true);
            }
        }
        
        // Set filter based on Relationship
        IMenuManager relationshipMenuManager = new MenuManager(Messages.ZestView_6);
        menuManager.add(relationshipMenuManager);
        
        // Get relationship from prefs
        String relationshipID = ArchiZestPlugin.INSTANCE.getPreferenceStore().getString(IPreferenceConstants.VISUALISER_RELATIONSHIP);
        EClass eClass = (EClass)IArchimatePackage.eINSTANCE.getEClassifier(relationshipID);
        getContentProvider().setRelationshipFilter(eClass);
        // Relationship actions, first the "None" relationship
        fRelationshipActions = new ArrayList<IAction>();
        IAction action = createRelationshipMenuAction(null);
        if(eClass == null) {
            action.setChecked(true);
        }
        fRelationshipActions.add(action);
        relationshipMenuManager.add(action);
        
        // Then get all relationships and sort them
        ArrayList<EClass> actionList = new ArrayList<EClass>(Arrays.asList(ArchimateModelUtils.getRelationsClasses()));
        actionList.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        for(EClass rel : actionList) {
        	action = createRelationshipMenuAction(rel);
        	fRelationshipActions.add(action);
        	relationshipMenuManager.add(action);
        	
        	// Set checked
            if(eClass != null && rel.getName().equals(eClass.getName())) {
        		action.setChecked(true);
        	}
        }
        
        // Orientation
        IMenuManager orientationMenuManager = new MenuManager(Messages.ZestView_32);
        menuManager.add(orientationMenuManager);
        
        // Direction
        fDirectionActions = new Action[3];
        fDirectionActions[0] = createOrientationMenuAction(0, Messages.ZestView_33, ZestViewerContentProvider.DIR_BOTH);
        orientationMenuManager.add(fDirectionActions[0]);
        fDirectionActions[1] = createOrientationMenuAction(1, Messages.ZestView_34, ZestViewerContentProvider.DIR_IN);
        orientationMenuManager.add(fDirectionActions[1]);
        fDirectionActions[2] = createOrientationMenuAction(2, Messages.ZestView_35, ZestViewerContentProvider.DIR_OUT);
        orientationMenuManager.add(fDirectionActions[2]);
        
        // Set direction from prefs
        int direction = ArchiZestPlugin.INSTANCE.getPreferenceStore().getInt(IPreferenceConstants.VISUALISER_DIRECTION);
        getContentProvider().setDirection(direction);
        fDirectionActions[direction].setChecked(true);
        
		menuManager.add(new Separator());
		
        // TODO Copy as Image to Clipboard. But not on Linux 64-bit. See https://bugs.eclipse.org/bugs/show_bug.cgi?id=283960
        if(!(PlatformUtils.isLinux() && PlatformUtils.is64Bit())) {
            menuManager.add(fActionCopyImageToClipboard);
        }
		menuManager.add(fActionExportImageToFile);
    }
    
    private IAction createDepthAction(final int actionId, final int depth) {
        IAction act = new Action(Messages.ZestView_3 + " " + depth, IAction.AS_RADIO_BUTTON) { //$NON-NLS-1$
            @Override
            public void run() {
                IStructuredSelection selection = (IStructuredSelection)fGraphViewer.getSelection();
                // set depth
                int depth = Integer.valueOf(getId());
                getContentProvider().setDepth(depth);
                // store in prefs
                ArchiZestPlugin.INSTANCE.getPreferenceStore().setValue(IPreferenceConstants.VISUALISER_DEPTH, depth);
                // update viewer
                fGraphViewer.setInput(fGraphViewer.getInput());
                fGraphViewer.setSelection(selection);
                fGraphViewer.doApplyLayout();
            }
        };

        act.setId(Integer.toString(actionId));
        
        return act;
    }

    private IAction createViewpointMenuAction(final IViewpoint vp) {
        IAction act = new Action(vp.getName(), IAction.AS_RADIO_BUTTON) {
            @Override
            public void run() {
            	// Set viewpoint filter
                getContentProvider().setViewpointFilter(vp);
            	// Store in prefs
                ArchiZestPlugin.INSTANCE.getPreferenceStore().setValue(IPreferenceConstants.VISUALISER_VIEWPOINT, vp.getID());

                // update viewer
            	fGraphViewer.setInput(fGraphViewer.getInput());
                IStructuredSelection selection = (IStructuredSelection)fGraphViewer.getSelection();
                fGraphViewer.setSelection(selection);
                fGraphViewer.doApplyLayout();
                updateLabel();
            }
        };
        
        act.setId(vp.getID());
        
        return act;
    }
    
    private IAction createRelationshipMenuAction(final EClass relationClass) {
        String id = relationClass == null ? "none" : relationClass.getName(); //$NON-NLS-1$
        
    	IAction act = new Action(getRelationshipFilterName(relationClass), IAction.AS_RADIO_BUTTON) {
    		@Override
            public void run() {
            	// Set relationship filter
                getContentProvider().setRelationshipFilter(relationClass);
            	// Store in prefs
                ArchiZestPlugin.INSTANCE.getPreferenceStore().setValue(IPreferenceConstants.VISUALISER_RELATIONSHIP, id);

                // update viewer
            	fGraphViewer.setInput(fGraphViewer.getInput());
                IStructuredSelection selection = (IStructuredSelection)fGraphViewer.getSelection();
                fGraphViewer.setSelection(selection);
                fGraphViewer.doApplyLayout();
                updateLabel();
            }

    	};
    	
        act.setId(id);
        
    	return act;
    }
    
    private IAction createOrientationMenuAction(final int actionId, String label, final int orientation) {
        IAction act = new Action(label, IAction.AS_RADIO_BUTTON) {
            @Override
            public void run() {
            	IStructuredSelection selection = (IStructuredSelection)fGraphViewer.getSelection();
            	// Set orientation 
                getContentProvider().setDirection(orientation);
            	// Store in prefs
                ArchiZestPlugin.INSTANCE.getPreferenceStore().setValue(IPreferenceConstants.VISUALISER_DIRECTION, actionId);
                // update viewer
            	fGraphViewer.setInput(fGraphViewer.getInput());
                fGraphViewer.setSelection(selection);
                fGraphViewer.doApplyLayout();
            }
        };
        
        act.setId(Integer.toString(actionId));
        
        return act;
    }
    
    private String getRelationshipFilterName(EClass relationClass) {
        return relationClass == null ? Messages.ZestView_7: ArchiLabelProvider.INSTANCE.getDefaultName(relationClass);
    }
    
    @Override
    public void setFocus() {
        if(fGraphViewer != null) {
            fGraphViewer.getControl().setFocus();
        }
    }
    
    @Override
    public ZestGraphViewer getViewer() {
        return fGraphViewer;
    }
    
    /**
     * Make local actions
     */
    private void makeActions() {
        fActionProperties = new PropertiesAction(getViewer());
        
        fActionLayout = new Action(Messages.ZestView_0) {
            @Override
            public void run() {
                fGraphViewer.doApplyLayout();
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return AbstractUIPlugin.imageDescriptorFromPlugin(ArchiZestPlugin.PLUGIN_ID,
                        "img/layout.png"); //$NON-NLS-1$
            }
        };
        
        fActionPinContent = new Action(Messages.ZestView_4, IAction.AS_CHECK_BOX) {
            {
                setToolTipText(Messages.ZestView_1);
                setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_PIN));
            }
        };
        
        fActionCopyImageToClipboard = new CopyZestViewAsImageToClipboardAction(fGraphViewer);
        fActionExportImageToFile = new ExportAsImageAction(fGraphViewer);
    }

    /**
     * Register Global Action Handlers
     */
    private void registerGlobalActions() {
        IActionBars actionBars = getViewSite().getActionBars();
        
        // Register our interest in the global menu actions
        actionBars.setGlobalActionHandler(ActionFactory.PROPERTIES.getId(), fActionProperties);
    }
    
    /**
     * Hook into a right-click menu
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#ZestViewPopupMenu"); //$NON-NLS-1$
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
        Object selected = ((IStructuredSelection)getViewer().getSelection()).getFirstElement();
        boolean isEmpty = selected == null;
        
        fDrillDownManager.addNavigationActions(manager);
        manager.add(new Separator());
        manager.add(fActionLayout);
        
        manager.add(new Separator());
        
        // Depth
        IMenuManager depthMenuManager = new MenuManager(Messages.ZestView_3);
        manager.add(depthMenuManager); 
        
        for(IAction action : fDepthActions) {
            depthMenuManager.add(action);
        }
        
        // Viewpoint filter
        IMenuManager vpMenuManager = new MenuManager(Messages.ZestView_5);
        manager.add(vpMenuManager); 
        
        for(IAction action : fViewpointActions) {
            vpMenuManager.add(action);
        }

        // Relationship filter
        IMenuManager relationshipMenuManager = new MenuManager(Messages.ZestView_6);
        manager.add(relationshipMenuManager); 

        for(IAction action : fRelationshipActions) {
            relationshipMenuManager.add(action);
        }

        
        // Direction
        IMenuManager directionMenuManager = new MenuManager(Messages.ZestView_32);
        manager.add(directionMenuManager); 
        
        for(IAction action : fDirectionActions) {
            directionMenuManager.add(action);
        }
        
        
        manager.add(new Separator());
        
        // TODO Copy as Image to Clipboard. But not on Linux 64-bit. See https://bugs.eclipse.org/bugs/show_bug.cgi?id=283960
        if(!(PlatformUtils.isLinux() && PlatformUtils.is64Bit())) {
            manager.add(fActionCopyImageToClipboard);
        }
        
        manager.add(fActionExportImageToFile);
        
        if(!isEmpty) {
            manager.add(new Separator());
            manager.add(fActionProperties);
        }
        
        // Other plug-ins can contribute their actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }
    
    @Override
    protected IArchimateModel getActiveArchimateModel() {
        IArchimateConcept concept = fDrillDownManager.getCurrentConcept();
        return concept != null ? concept.getArchimateModel() : null;
    }
    
    /**
     * @return Casted Content Provider
     */
    protected ZestViewerContentProvider getContentProvider() {
        return (ZestViewerContentProvider)fGraphViewer.getContentProvider();
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        // Explicit dispose seems to be needed if the GraphViewer is displaying scrollbars
        // See https://bugs.eclipse.org/bugs/show_bug.cgi?id=373191
        // In fact, Graph.dispose() seems never to be called
        // fGraphViewer.getControl().dispose();
        
        // Unregister selection listener
        getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
    }
    
    // =================================================================================
    //                       Listen to Editor Model Changes
    // =================================================================================
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        Object newValue = evt.getNewValue();
        
        // Model Closed
        if(propertyName == IEditorModelManager.PROPERTY_MODEL_REMOVED) {
            Object input = getViewer().getInput();
            if(input instanceof IArchimateModelObject && ((IArchimateModelObject)input).getArchimateModel() == newValue) {
                fDrillDownManager.reset();
            }
        }
        // Command Stack - update Actions
        else if(propertyName == IEditorModelManager.COMMAND_STACK_CHANGED) {
            updateActions();
        }
        else {
            super.propertyChange(evt);
        }
    }
    
    // =================================================================================
    //                       React to ECore Model Changes
    // =================================================================================
    
    @Override
    protected void eCoreChanged(Notification msg) {
        switch(msg.getEventType()) {
            case Notification.ADD:
            case Notification.ADD_MANY:
            case Notification.REMOVE:
            case Notification.REMOVE_MANY:
            case Notification.MOVE:
                refresh();
                break;
            case Notification.SET:
                // Current component name change
                if(msg.getNotifier() == fDrillDownManager.getCurrentConcept()) {
                    updateLabel();
                }
                if(!(msg.getNewValue() instanceof IBounds)) { // Don't update on bounds change. This can cause a conflict with Undo/Redo animation
                    super.eCoreChanged(msg);
                }
                break;

            default:
                break;
        }
    }
    
    @Override
    protected void doRefreshFromNotifications(List<Notification> notifications) {
        refresh();
        super.doRefreshFromNotifications(notifications);
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
        return Messages.ZestView_2;
    }
}