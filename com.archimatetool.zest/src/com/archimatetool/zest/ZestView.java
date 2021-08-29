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
import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
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
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.services.ViewManager;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.editor.views.AbstractModelView;
import com.archimatetool.editor.views.tree.ITreeModelView;
import com.archimatetool.editor.views.tree.actions.IViewerAction;
import com.archimatetool.editor.views.tree.actions.PropertiesAction;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimatePackage;
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
    private IAction fActionSelectInModelTree;
    
    private IAction[] fDepthActions;
    private IAction[] fDirectionActions;
    private List<IAction> fViewpointActions;
    
    private List<IAction> fRelationshipActions;

    private List<IAction> fAllElementActions;
    private IAction fNoneElementAction;
    private List<IAction> fStrategyElementActions;
    private List<IAction> fBusinessElementActions;
    private List<IAction> fApplicationElementActions;
    private List<IAction> fTechnologyElementActions;
    private List<IAction> fPhysicalElementActions;
    private List<IAction> fImplementationMigrationElementActions;
    private List<IAction> fMotivationElementActions;
    private List<IAction> fOtherElementActions;
  
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
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                // Update actions
                updateActions();
                // Need to do this in order for Tabbed Properties View to update on Selection
                getSite().getSelectionProvider().setSelection(event.getSelection());
            }
        });
        
        // Double-click
        fGraphViewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
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
        String elementName = getElementFilterName(getContentProvider().getElementFilter());
       	String relationshipName = getRelationshipFilterName(getContentProvider().getRelationshipFilter());

        fLabel.setText(text + " (" + Messages.ZestView_5 + ": " + viewPointName + ", " + //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    Messages.ZestView_9 + ": " + elementName + ", " + Messages.ZestView_6 + ": " + relationshipName + ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        fLabel.setImage(ArchiLabelProvider.INSTANCE.getImage(fDrillDownManager.getCurrentConcept()));
    }

    /**
     * Update the Local Actions depending on the selection, and undo/redo actions
     */
    void updateActions() {
        IStructuredSelection selection = (IStructuredSelection)getViewer().getSelection();
        fActionProperties.update();
        fActionSelectInModelTree.setEnabled(!selection.isEmpty());
        
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
        
        // Depth
        menuManager.add(createDepthMenu());

        // Viewpoints
        menuManager.add(createViewpointMenu());

        // Elements
        menuManager.add(createElementsMenu());

        // Relationships
        menuManager.add(createRelationshipsMenu());

        // Direction
        menuManager.add(createDirectionMenu());

		menuManager.add(new Separator());
		
		menuManager.add(fActionSelectInModelTree);
		menuManager.add(fActionCopyImageToClipboard);
		menuManager.add(fActionExportImageToFile);
    }

    private String getRelationshipFilterName(EClass relationClass) {
        return relationClass == null ? Messages.ZestView_7 : ArchiLabelProvider.INSTANCE.getDefaultName(relationClass);
    }

    private String getElementFilterName(EClass elementClass) {
        return elementClass == null ? Messages.ZestView_7 : ArchiLabelProvider.INSTANCE.getDefaultName(elementClass);
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

    // ==============================================================================================
    // Menu Actions
    // ==============================================================================================
    
    /**
     * Make local actions
     */
    private void makeActions() {
        // Depth
        createDepthActions();
        
        // Viewpoints
        createViewpointActions();
        
        // Elements
        createElementsActions();
        
        // Relationships
        createRelationshipsActions();
        
        // Direction
        createDirectionActions();

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
                return ResourceLocator.imageDescriptorFromBundle(ArchiZestPlugin.PLUGIN_ID, "img/layout.gif").orElse(null); //$NON-NLS-1$
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

        fActionSelectInModelTree = new Action(Messages.ZestView_8) {

            @Override
            public void run() {
                IStructuredSelection selection = (IStructuredSelection)getViewer().getSelection();
                ITreeModelView view = (ITreeModelView)ViewManager.showViewPart(ITreeModelView.ID, true);
                if(view != null && !selection.isEmpty()) {
                    view.getViewer().setSelection(new StructuredSelection(selection.toArray()), true);
                }
            }

            @Override
            public String getToolTipText() {
                return getText();
            }
        };
    }

    private void createDepthActions() {
        fDepthActions = new Action[6];
        for(int i = 0; i < fDepthActions.length; i++) {
            fDepthActions[i] = createDepthAction(i, i + 1);
        }

        // Set depth from prefs
        int depth = ArchiZestPlugin.INSTANCE.getPreferenceStore().getInt(IPreferenceConstants.VISUALISER_DEPTH);
        getContentProvider().setDepth(depth);
        fDepthActions[depth].setChecked(true);
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

    private void createViewpointActions() {
        // Get viewpoint from prefs
        String viewpointID = ArchiZestPlugin.INSTANCE.getPreferenceStore().getString(IPreferenceConstants.VISUALISER_VIEWPOINT);
        getContentProvider().setViewpointFilter(ViewpointManager.INSTANCE.getViewpoint(viewpointID));

        // Viewpoint actions
        fViewpointActions = new ArrayList<IAction>();

        for(IViewpoint vp : ViewpointManager.INSTANCE.getAllViewpoints()) {
            IAction action = createViewpointMenuAction(vp);
            fViewpointActions.add(action);

            // Set checked
            if(vp.getID().equals(viewpointID)) {
                action.setChecked(true);
            }
        }
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

    private void createElementsActions() {
        fAllElementActions = new ArrayList<IAction>();

        // The "All" option
        fNoneElementAction = createElementAction(null);
        fAllElementActions.add(fNoneElementAction);

        // Strategy
        fStrategyElementActions = createElementActionsGroup(ArchimateModelUtils.getStrategyClasses());

        // Business
        fBusinessElementActions = createElementActionsGroup(ArchimateModelUtils.getBusinessClasses());

        // Application
        fApplicationElementActions = createElementActionsGroup(ArchimateModelUtils.getApplicationClasses());

        // Technology
        fTechnologyElementActions = createElementActionsGroup(ArchimateModelUtils.getTechnologyClasses());

        // Physical
        fPhysicalElementActions = createElementActionsGroup(ArchimateModelUtils.getPhysicalClasses());

        // Motivation
        fMotivationElementActions = createElementActionsGroup(ArchimateModelUtils.getMotivationClasses());

        // Implementation & Migration
        fImplementationMigrationElementActions = createElementActionsGroup(ArchimateModelUtils.getImplementationMigrationClasses());

        // Other
        fOtherElementActions = createElementActionsGroup(ArchimateModelUtils.getOtherClasses());

        // Get selected element from prefs
        String elementsID = ArchiZestPlugin.INSTANCE.getPreferenceStore().getString(IPreferenceConstants.VISUALISER_ELEMENT);
        EClass elementClass = (EClass)IArchimatePackage.eINSTANCE.getEClassifier(elementsID);
        getContentProvider().setElementFilter(elementClass);

        // Set Checked
        if(elementClass == null) {
            fNoneElementAction.setChecked(true);
        }
        else {
            for(IAction a : fAllElementActions) {
                if(a.getId().equals(elementClass.getName())) {
                    a.setChecked(true);
                }
            }
        }
    }

    private List<IAction> createElementActionsGroup(EClass[] eClasses) {
        List<IAction> actions = new ArrayList<IAction>();

        ArrayList<EClass> list = new ArrayList<EClass>(Arrays.asList(eClasses));
        list.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));

        for(EClass elem : list) {
            IAction elementAction = createElementAction(elem);
            actions.add(elementAction);
            fAllElementActions.add(elementAction);
        }

        return actions;
    }

    private IAction createElementAction(final EClass elementClass) {
        String id = elementClass == null ? "none" : elementClass.getName(); //$NON-NLS-1$

        IAction act = new Action(getElementFilterName(elementClass), IAction.AS_RADIO_BUTTON) {

            @Override
            public void run() {
                // Set element filter
                getContentProvider().setElementFilter(elementClass);
                // Store in prefs
                ArchiZestPlugin.INSTANCE.getPreferenceStore().setValue(IPreferenceConstants.VISUALISER_ELEMENT, id);

                // update viewer
                fGraphViewer.setInput(fGraphViewer.getInput());
                IStructuredSelection selection = (IStructuredSelection)fGraphViewer.getSelection();
                fGraphViewer.setSelection(selection);
                fGraphViewer.doApplyLayout();
                updateLabel();

                // Uncheck all other actions
                for(IAction a : fAllElementActions) {
                    if(a != this) {
                        a.setChecked(false);
                    }
                }
            }
        };

        act.setId(id);

        return act;
    }

    private void createRelationshipsActions() {
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

        // Then get all relationships and sort them
        ArrayList<EClass> actionList = new ArrayList<EClass>(Arrays.asList(ArchimateModelUtils.getRelationsClasses()));
        actionList.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        for(EClass rel : actionList) {
            action = createRelationshipMenuAction(rel);
            fRelationshipActions.add(action);

            // Set checked
            if(eClass != null && rel.getName().equals(eClass.getName())) {
                action.setChecked(true);
            }
        }
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

    private void createDirectionActions() {
        // Direction
        fDirectionActions = new Action[3];
        fDirectionActions[0] = createDirectionMenuAction(0, Messages.ZestView_33, ZestViewerContentProvider.DIR_BOTH);
        fDirectionActions[1] = createDirectionMenuAction(1, Messages.ZestView_34, ZestViewerContentProvider.DIR_IN);
        fDirectionActions[2] = createDirectionMenuAction(2, Messages.ZestView_35, ZestViewerContentProvider.DIR_OUT);

        // Set direction from prefs
        int direction = ArchiZestPlugin.INSTANCE.getPreferenceStore().getInt(IPreferenceConstants.VISUALISER_DIRECTION);
        getContentProvider().setDirection(direction);
        fDirectionActions[direction].setChecked(true);
    }

    private IAction createDirectionMenuAction(final int actionId, String label, final int orientation) {
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
     * 
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
        manager.add(createDepthMenu());
        
        // Viewpoint filter
        manager.add(createViewpointMenu());

        // Element filter
        manager.add(createElementsMenu());

        // Relationship filter
        manager.add(createRelationshipsMenu());
        
        // Direction
        manager.add(createDirectionMenu());

        manager.add(new Separator());

        manager.add(fActionCopyImageToClipboard);
        manager.add(fActionExportImageToFile);

        if(!isEmpty) {
            manager.add(fActionSelectInModelTree);
            manager.add(new Separator());
            manager.add(fActionProperties);
        }

        // Other plug-ins can contribute their actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }
    
    private IMenuManager createViewpointMenu() {
        IMenuManager vpMenuManager = new MenuManager(Messages.ZestView_5);

        for(IAction action : fViewpointActions) {
            vpMenuManager.add(action);
        }
        
        return vpMenuManager;
    }
    
    private IMenuManager createDirectionMenu() {
        IMenuManager directionMenuManager = new MenuManager(Messages.ZestView_32);

        for(IAction action : fDirectionActions) {
            directionMenuManager.add(action);
        }

        return directionMenuManager;
    }
    
    private IMenuManager createDepthMenu() {
        IMenuManager depthMenuManager = new MenuManager(Messages.ZestView_3);

        for(IAction action : fDepthActions) {
            depthMenuManager.add(action);
        }

        return depthMenuManager;
    }

    private IMenuManager createElementsMenu() {
        IMenuManager elementMenuManager = new MenuManager(Messages.ZestView_9);

        // "All"
        elementMenuManager.add(fNoneElementAction);

        IMenuManager strategyElementMenuManager = new MenuManager(Messages.ZestView_10);
        elementMenuManager.add(strategyElementMenuManager);
        for(IAction action : fStrategyElementActions) {
            strategyElementMenuManager.add(action);
        }

        IMenuManager businessElementMenuManager = new MenuManager(Messages.ZestView_11);
        elementMenuManager.add(businessElementMenuManager);
        for(IAction action : fBusinessElementActions) {
            businessElementMenuManager.add(action);
        }

        IMenuManager applicationElementMenuManager = new MenuManager(Messages.ZestView_12);
        elementMenuManager.add(applicationElementMenuManager);
        for(IAction action : fApplicationElementActions) {
            applicationElementMenuManager.add(action);
        }

        IMenuManager technologyElementMenuManager = new MenuManager(Messages.ZestView_13);
        elementMenuManager.add(technologyElementMenuManager);
        for(IAction action : fTechnologyElementActions) {
            technologyElementMenuManager.add(action);
        }

        IMenuManager physicalElementMenuManager = new MenuManager(Messages.ZestView_14);
        elementMenuManager.add(physicalElementMenuManager);
        for(IAction action : fPhysicalElementActions) {
            physicalElementMenuManager.add(action);
        }

        IMenuManager motivationElementMenuManager = new MenuManager(Messages.ZestView_15);
        elementMenuManager.add(motivationElementMenuManager);
        for(IAction action : fMotivationElementActions) {
            motivationElementMenuManager.add(action);
        }

        IMenuManager implementationMigrationElementMenuManager = new MenuManager(Messages.ZestView_16);
        elementMenuManager.add(implementationMigrationElementMenuManager);
        for(IAction action : fImplementationMigrationElementActions) {
            implementationMigrationElementMenuManager.add(action);
        }

        IMenuManager otherElementMenuManager = new MenuManager(Messages.ZestView_17);
        elementMenuManager.add(otherElementMenuManager);
        for(IAction action : fOtherElementActions) {
            otherElementMenuManager.add(action);
        }

        return elementMenuManager;
    }
    
    private IMenuManager createRelationshipsMenu() {
        IMenuManager relationshipMenuManager = new MenuManager(Messages.ZestView_6);

        for(IAction action : fRelationshipActions) {
            relationshipMenuManager.add(action);
        }

        return relationshipMenuManager;
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
        doRefresh(msg);
    }
    
    @Override
    protected void doRefreshFromNotifications(List<Notification> notifications) {
        for(Notification msg : notifications) {
            if(doRefresh(msg)) {
                break; // Only need to refresh once
            }
        }
        
        super.doRefreshFromNotifications(notifications);
    }
    
    private boolean doRefresh(Notification msg) {
        // Name change
        if(msg.getFeature() == IArchimatePackage.Literals.NAMEABLE__NAME) {
            getViewer().update(msg.getNotifier(), null);
            // Update label
            if(msg.getNotifier() == fDrillDownManager.getCurrentConcept()) {
                updateLabel();
            }
        }
        // Requires a full refresh
        else if(isRefreshEvent(msg)) {
            refresh();
            return true;
        }
        
        return false;
    }
    
    private boolean isRefreshEvent(Notification msg) {
        if(msg.getNewValue() instanceof IArchimateConcept || msg.getOldValue() instanceof IArchimateConcept) {
            return true;
        }
        
        return false;
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
        return Messages.ZestView_2;
    }
}