/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.zest;

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
import com.archimatetool.editor.ui.ArchimateLabelProvider;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.editor.views.AbstractModelView;
import com.archimatetool.editor.views.tree.actions.IViewerAction;
import com.archimatetool.editor.views.tree.actions.PropertiesAction;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelElement;
import com.archimatetool.model.IArchimatePackage;



/**
 * Zest View
 * 
 * @author Phillip Beauvoir
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
    
    private void setElement(Object object) {
        IArchimateElement element = null;
        
        if(object instanceof IArchimateElement) {
            element = (IArchimateElement)object;
        }
        else if(object instanceof IAdaptable) {
            element = (IArchimateElement)((IAdaptable)object).getAdapter(IArchimateElement.class);
        }
        
        fDrillDownManager.setNewInput(element);
        updateActions();
        
        updateLabel();
    }
    
    void refresh() {
        getViewer().refresh();
        updateActions();
        updateLabel();
    }
    
    /**
     * Update local label
     */
    void updateLabel() {
        String text = ArchimateLabelProvider.INSTANCE.getLabel(fDrillDownManager.getCurrentElement());
        text = StringUtils.escapeAmpersandsInText(text);
        fLabel.setText(text);
        fLabel.setImage(ArchimateLabelProvider.INSTANCE.getImage(fDrillDownManager.getCurrentElement()));
    }

    /**
     * Update the Local Actions depending on the selection, and undo/redo actions
     */
    void updateActions() {
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
            fDepthActions[i] = new Action(Messages.ZestView_3 + " " + (i + 1), IAction.AS_RADIO_BUTTON) { //$NON-NLS-1$
                @Override
                public void run() {
                    IStructuredSelection selection = (IStructuredSelection)fGraphViewer.getSelection();
                    // set depth
                    int depth = Integer.valueOf(getId());
                    ((ZestViewerContentProvider)fGraphViewer.getContentProvider()).setDepth(depth);
                    // store in prefs
                    ArchimateZestPlugin.INSTANCE.getPreferenceStore().setValue(IPreferenceConstants.VISUALISER_DEPTH, depth);
                    // update viewer
                    fGraphViewer.setInput(fGraphViewer.getInput());
                    fGraphViewer.setSelection(selection);
                    fGraphViewer.doApplyLayout();
                }
            };
            
            fDepthActions[i].setId(Integer.toString(i));
            depthMenuManager.add(fDepthActions[i]);
        }
        
        // Set depth from prefs
        int depth = ArchimateZestPlugin.INSTANCE.getPreferenceStore().getInt(IPreferenceConstants.VISUALISER_DEPTH);
        ((ZestViewerContentProvider)fGraphViewer.getContentProvider()).setDepth(depth);
        fDepthActions[depth].setChecked(true);
        
        menuManager.add(new Separator());
        menuManager.add(fActionCopyImageToClipboard);
        menuManager.add(fActionExportImageToFile);
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
                return AbstractUIPlugin.imageDescriptorFromPlugin(ArchimateZestPlugin.PLUGIN_ID,
                        "img/layout.gif"); //$NON-NLS-1$
            }
        };
        
        fActionPinContent = new Action(Messages.ZestView_4, IAction.AS_CHECK_BOX) {
            {
                setToolTipText(Messages.ZestView_1);
                setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_PIN_16));
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
        IMenuManager depthMenuManager = new MenuManager(Messages.ZestView_3);
        manager.add(depthMenuManager); 
        
        for(int i = 0; i < fDepthActions.length; i++) {
            depthMenuManager.add(fDepthActions[i]);
        }
        
        manager.add(new Separator());
        manager.add(fActionCopyImageToClipboard);
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
        IArchimateElement element = fDrillDownManager.getCurrentElement();
        return element != null ? element.getArchimateModel() : null;
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
            if(input instanceof IArchimateModelElement && ((IArchimateModelElement)input).getArchimateModel() == newValue) {
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
        int type = msg.getEventType();
        
        if(type == Notification.ADD || type == Notification.ADD_MANY ||
                type == Notification.REMOVE || type == Notification.REMOVE_MANY || type == Notification.MOVE) {
            refresh();
        }
        
        // Attribute set
        else if(type == Notification.SET) {
            Object feature = msg.getFeature();
            Object notifier = msg.getNotifier();

            // Relationship/Connection changed - requires full refresh
            if(feature == IArchimatePackage.Literals.RELATIONSHIP__SOURCE ||
                                        feature == IArchimatePackage.Literals.RELATIONSHIP__TARGET) {
                refresh();
            }
            else {
                super.eCoreChanged(msg);
            }
            
            if(notifier == fDrillDownManager.getCurrentElement()) {
                updateLabel();
            }
        }
        else {
            super.eCoreChanged(msg);
        }
    }
    
    @Override
    protected void refreshElementsFromBufferedNotifications() {
        refresh();
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