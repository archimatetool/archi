/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.navigator;

import java.beans.PropertyChangeEvent;
import java.util.List;

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

import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.views.AbstractModelView;
import com.archimatetool.editor.views.tree.actions.IViewerAction;
import com.archimatetool.editor.views.tree.actions.PropertiesAction;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimatePackage;



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
            return element != getViewerInput();
        }

        @Override
        public void goInto() {
            goInto(new Object[] { ((IStructuredSelection)fTreeViewer.getSelection()).getFirstElement() });
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
        
        /*
         * Listen to Double-click Action
         */
        fTreeViewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                fDrillDownAdapter.goInto();
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
                setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_PIN));
            }
        };
        
        fActionNavDown = new Action(Messages.NavigatorView_2, IAction.AS_RADIO_BUTTON) {
            {
                setToolTipText(Messages.NavigatorView_3);
                setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_NAVIGATOR_DOWNWARD));
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
                setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_NAVIGATOR_UPWARD));
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
        fActionProperties.update();
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
        if(part == this || fActionPinContent.isChecked()) {
            return;
        }
        
        // Don't reset if we select something in another Eclipse view
        if(selection instanceof IStructuredSelection && !selection.isEmpty()) {
            setElement(((IStructuredSelection)selection).getFirstElement());
        }
    }
    
    @Override
    protected void selectAll() {
        fTreeViewer.getTree().selectAll();
    }
    
    private void setElement(Object object) {
        fDrillDownAdapter.reset();
        
        IArchimateConcept concept = null;
        
        if(object instanceof IArchimateConcept) {
            concept = (IArchimateConcept)object;
        }
        else if(object instanceof IAdaptable) {
            concept = ((IAdaptable)object).getAdapter(IArchimateConcept.class);
        }
        
        getViewer().setInput(new Object[] { concept }); // Need to use an array
    }
    
    private void reset() {
        fDrillDownAdapter.reset();
        getViewer().setInput(null);
    }
    
    @Override
    protected IArchimateModel getActiveArchimateModel() {
        return getViewerInput() != null ? getViewerInput().getArchimateModel() : null;
    }
    
    private IArchimateModelObject getViewerInput() {
        return getViewer() != null && getViewer().getActualInput() != null ? getViewer().getActualInput() : null;
    }

    @Override
    public void dispose() {
        super.dispose();
        
        // Unregister selection listener
        getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
        
        fTreeViewer = null;
    }
    
    // =================================================================================
    //                       Listen to Editor Model Changes
    // =================================================================================
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // Model Closed
        if(evt.getPropertyName() == IEditorModelManager.PROPERTY_MODEL_REMOVED) {
            if(getActiveArchimateModel() == evt.getNewValue()) {
                reset();
            }
        }
        // Command Stack - update Actions
        else if(evt.getPropertyName() == IEditorModelManager.COMMAND_STACK_CHANGED) {
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
        }
        // Requires a full refresh
        else if(isRefreshEvent(msg)) {
            getViewer().refreshTreePreservingExpandedNodes();
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
        return Messages.NavigatorView_6;
    }
}