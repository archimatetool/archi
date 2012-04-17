/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.views.navigator;

import java.beans.PropertyChangeEvent;

import org.eclipse.core.runtime.IAdaptable;
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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
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
import org.eclipse.ui.part.DrillDownAdapter;

import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.editor.views.AbstractModelView;
import uk.ac.bolton.archimate.editor.views.tree.actions.IViewerAction;
import uk.ac.bolton.archimate.editor.views.tree.actions.PropertiesAction;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateModelElement;
import uk.ac.bolton.archimate.model.IArchimatePackage;


/**
 * Navigator View
 * 
 * @author Phillip Beauvoir
 */
public class NavigatorView
extends AbstractModelView
implements INavigatorView, ISelectionListener {
    
    private NavigatorViewer fTreeViewer;
    
    private IViewerAction fActionProperties;
    private IAction fActionNavDown;
    private IAction fActionNavUp;
    private IAction fActionPinContent;
    
    private NavigatorDrillDownAdapter fDrillDownAdapter;
    
    private class NavigatorDrillDownAdapter extends DrillDownAdapter {
        public NavigatorDrillDownAdapter() {
            super(fTreeViewer);
        }
        
        @Override
        public boolean canExpand(Object element) {
            Object root = fTreeViewer.getActualInput();
            element = fTreeViewer.getActualInput(element);
            //return element != root && fTreeViewer.isExpandable(element);
            return element != root;
        }

        @Override
        public void goInto() {
            IStructuredSelection sel = (IStructuredSelection)fTreeViewer.getSelection();
            Object element = sel.getFirstElement();
            goInto(new Object[] { element });
        }
    }
    
    @Override
    public void doCreatePartControl(Composite parent) {
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;
        parent.setLayout(layout);
        
        fTreeViewer = new NavigatorViewer(parent, SWT.NULL);
        fTreeViewer.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
        
        fTreeViewer.setInput(IEditorModelManager.INSTANCE);
        
        /*
         * Listen to Double-click Action
         */
        fTreeViewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                fDrillDownAdapter.goInto();
            }
        });
        
        // Tree selection listener
        fTreeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                // Update actions
                updateActions();
            }
        });
        
        fDrillDownAdapter = new NavigatorDrillDownAdapter();
        
        // Register selections
        getSite().setSelectionProvider(getViewer());
        
        makeActions();
        hookContextMenu();
        registerGlobalActions();
        makeLocalToolBar();
        
        // This will update previous Undo/Redo text if View was closed before
        updateActions();
        
        // Listen to global selections to update the tree
        getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
        
        // DnD
        new NavigatorViewerDragHandler(fTreeViewer);
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
        
        // Initialise with whatever is selected in the workbench
        ISelection selection = getSite().getWorkbenchWindow().getSelectionService().getSelection();
        selectionChanged(null, selection);
    }
    
    @Override
    public void setFocus() {
        fTreeViewer.getControl().setFocus();
    }
    
    @Override
    public NavigatorViewer getViewer() {
        return fTreeViewer;
    } 
    
    /**
     * Make local actions
     */
    private void makeActions() {
        fActionProperties = new PropertiesAction(getViewer());
        
        fActionPinContent = new Action(Messages.NavigatorView_0, IAction.AS_CHECK_BOX) {
            {
                setToolTipText(Messages.NavigatorView_1);
                setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_PIN_16));
            }
        };
        
        fActionNavDown = new Action(Messages.NavigatorView_2, IAction.AS_RADIO_BUTTON) {
            {
                setToolTipText(Messages.NavigatorView_3);
                setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_NAVIGATOR_DOWNWARD_16));
                setChecked(true);
            }
            
            @Override
            public void run() {
                fActionNavUp.setChecked(false);
                fTreeViewer.setShowTargetElements(true);
            }
        };
        
        fActionNavUp = new Action(Messages.NavigatorView_4, IAction.AS_RADIO_BUTTON) {
            {
                setToolTipText(Messages.NavigatorView_5);
                setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_NAVIGATOR_UPWARD_16));
            }
            
            @Override
            public void run() {
                fActionNavDown.setChecked(false);
                fTreeViewer.setShowTargetElements(false);
            }
        };
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
        MenuManager menuMgr = new MenuManager("#NavigatorViewPopupMenu"); //$NON-NLS-1$
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
        
        // Drill-down adapter
        if(fDrillDownAdapter.canGoInto() || fDrillDownAdapter.canGoBack() || fDrillDownAdapter.canGoHome()) {
            fDrillDownAdapter.addNavigationActions(manager);
        }
        
        if(!isEmpty) {
            manager.add(new Separator());
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
        fActionProperties.update(selection);
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
        manager.add(fActionNavDown);
        manager.add(fActionNavUp);
        manager.add(new Separator());
        manager.add(fActionPinContent);
    }
    
    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if(part == this) {
            return;
        }
        
        if(fActionPinContent.isChecked()) {
            return;
        }
        
        // Don't reset if we select something in another Eclipse view
        if(selection instanceof IStructuredSelection && !selection.isEmpty()) {
            Object object = ((IStructuredSelection)selection).getFirstElement();
            setElement(object);
        }
    }
    
    private void setElement(Object object) {
        fDrillDownAdapter.reset();
        
        if(object instanceof IAdaptable) {
            object = ((IAdaptable)object).getAdapter(IArchimateElement.class);
        }
        if(object instanceof IArchimateElement) {
            getViewer().setInput(new Object[] { object }); // Need to use an array
        }
        else {
            getViewer().setInput(null);
        }
    }
    
    private void reset() {
        fDrillDownAdapter.reset();
        getViewer().setInput(null);
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
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
            Object input = getViewer().getActualInput();
            if(input instanceof IArchimateModelElement && ((IArchimateModelElement)input).getArchimateModel() == newValue) {
                reset();
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
        int type = msg.getEventType();
        
        if(type == Notification.ADD || type == Notification.REMOVE) {
            getViewer().refresh();
        }
        // Attribute set
        else if(type == Notification.SET) {
            Object feature = msg.getFeature();

            // Relationship/Connection changed - requires full refresh
            if(feature == IArchimatePackage.Literals.RELATIONSHIP__SOURCE ||
                                        feature == IArchimatePackage.Literals.RELATIONSHIP__TARGET) {
                getViewer().refresh();
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
        return Messages.NavigatorView_6;
    }
}