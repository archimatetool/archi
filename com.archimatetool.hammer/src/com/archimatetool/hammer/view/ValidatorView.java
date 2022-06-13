/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.preference.PreferenceDialog;
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
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.IDiagramModelEditor;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.editor.ui.services.ViewManager;
import com.archimatetool.editor.views.tree.ITreeModelView;
import com.archimatetool.hammer.IHammerImages;
import com.archimatetool.hammer.validation.Validator;
import com.archimatetool.hammer.validation.issues.IIssue;
import com.archimatetool.help.hints.IHintsView;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelComponent;


/**
 * Validator View
 * 
 * @author Phillip Beauvoir
 */
public class ValidatorView extends ViewPart
implements IValidatorView, ISelectionListener, IContextProvider, ITabbedPropertySheetPageContributor, PropertyChangeListener {
    
    private ValidatorViewer fViewer;
    
    private IAction fActionValidate;
    private IAction fActionExplain;
    private IAction fActionSelectObjects;
    private IAction fActionShowPreferences;
    
    private IArchimateModel fModel;
    
    public ValidatorView() {
    }

    @Override
    public void createPartControl(Composite parent) {
        Composite treeComp = new Composite(parent, SWT.NULL);
        treeComp.setLayout(new TreeColumnLayout());
        treeComp.setLayoutData(new GridData(GridData.FILL_BOTH));

        fViewer = new ValidatorViewer(treeComp, SWT.NULL);
        
        fViewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                selectObjects((IStructuredSelection)event.getSelection());
            }
        });
        
        fViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                updateMenuItems((IStructuredSelection)event.getSelection());
            }
        });
        
        makeActions();
        registerGlobalActions();
        makeLocalToolBar();
        hookContextMenu();
        
        // Listen to global selections to update the viewer
        getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
        
        // Selection Provider
        getSite().setSelectionProvider(fViewer);

        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        // Register us as a Model Listener
        IEditorModelManager.INSTANCE.addPropertyChangeListener(this);

        // Initialise with whatever is selected in the workbench
        selectionChanged(getSite().getWorkbenchWindow().getPartService().getActivePart(),
                getSite().getWorkbenchWindow().getSelectionService().getSelection());
        
        validateModel();
    }
    
    /**
     * Make local actions
     */
    private void makeActions() {
        fActionValidate = new Action(Messages.ValidatorView_0) {
            @Override
            public void run() {
                validateModel();
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return IHammerImages.ImageFactory.getImageDescriptor(IHammerImages.ICON_APP);
            }
        };
        fActionValidate.setEnabled(false);
        
        fActionExplain = new Action(Messages.ValidatorView_1) {
            @Override
            public void run() {
                ViewManager.showViewPart(IHintsView.ID, false);
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return ResourceLocator.imageDescriptorFromBundle("com.archimatetool.help", "img/hint.png").orElse(null); //$NON-NLS-1$ //$NON-NLS-2$
            }
        };
        fActionExplain.setEnabled(false);
        
        fActionSelectObjects = new Action(Messages.ValidatorView_2) {
            @Override
            public void run() {
                selectObjects((IStructuredSelection)getViewer().getSelection());
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
        };
        
        fActionShowPreferences = new Action(Messages.ValidatorView_4) {
            @Override
            public void run() {
                PreferenceDialog dialog = PreferencesUtil.createPreferenceDialogOn(getSite().getShell(),
                        "com.archimatetool.hammer.preferences.ValidatorPreferencePage", null, null); //$NON-NLS-1$
                dialog.open();
            }
            
            @Override
            public String getToolTipText() {
                return getText();
            }
        };
    }

    /**
     * Register Global Action Handlers
     */
    private void registerGlobalActions() {
        // None
    }

    /**
     * Populate the ToolBar
     */
    private void makeLocalToolBar() {
        IActionBars bars = getViewSite().getActionBars();
        IToolBarManager manager = bars.getToolBarManager();
        manager.add(fActionValidate);
        manager.add(new Separator());
        manager.add(fActionExplain);
        
        final IMenuManager menuManager = bars.getMenuManager();
        menuManager.add(fActionShowPreferences); 
    }
    
    /**
     * Hook into a right-click menu
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#ValidatorViewPopupMenu"); //$NON-NLS-1$
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
        IStructuredSelection selection = (IStructuredSelection)getViewer().getSelection();

        manager.add(fActionValidate);
        manager.add(new Separator());
        
        if(isIssueObjectSelected(selection)) {
            manager.add(fActionSelectObjects);
        }
        
        if(isIssueSelected(selection)) {
            manager.add(fActionExplain);
        }
        
        manager.add(new Separator());
        manager.add(fActionShowPreferences);
        
        // Other plug-ins can contribute their actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    @Override
    public void setFocus() {
        if(fViewer != null) {
            fViewer.getControl().setFocus();
        }
    }
    
    public ValidatorViewer getViewer() {
        return fViewer;
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if(part == this || part == null) {
            return;
        }
        
        IArchimateModel model = part.getAdapter(IArchimateModel.class);
        
        if(model != null) {
            fModel = model;
        }
        
        fActionValidate.setEnabled(fModel != null);
    }
    
    void selectObjects(IStructuredSelection selection) {
        if(selection != null) {
            List<IArchimateConcept> treeList = new ArrayList<IArchimateConcept>();
            List<IDiagramModel> viewList = new ArrayList<IDiagramModel>();
            List<IDiagramModelComponent> viewComponentList = new ArrayList<IDiagramModelComponent>();
            
            for(Object o : selection.toArray()) {
                if(o instanceof IIssue) {
                    IIssue issue = (IIssue)o;
                    if(issue.getObject() instanceof IArchimateConcept) {
                        treeList.add((IArchimateConcept)issue.getObject());
                    }
                    else if(issue.getObject() instanceof IDiagramModel) {
                        viewList.add((IDiagramModel)issue.getObject());
                    }
                    else if(issue.getObject() instanceof IDiagramModelComponent) {
                        viewList.add(((IDiagramModelComponent)issue.getObject()).getDiagramModel());
                        viewComponentList.add(((IDiagramModelComponent)issue.getObject()));
                    }
                }
            }
            
            if(!treeList.isEmpty()) {
                ITreeModelView view = (ITreeModelView)ViewManager.showViewPart(ITreeModelView.ID, false);
                if(view != null) {
                    view.getViewer().setSelection(new StructuredSelection(treeList), true);
                }
            }
            
            if(!viewList.isEmpty()) {
                for(IDiagramModel dm : viewList) {
                    IDiagramModelEditor editor = EditorManager.openDiagramEditor(dm, false);
                    if(editor != null) {
                        // Needs to be asyncExec to allow EditorPart to open if it is currently closed
                        getSite().getShell().getDisplay().asyncExec(()-> { 
                            editor.selectObjects(viewComponentList.toArray());
                        });
                    }
                }
            }
       }
    }
    
    @Override
    public void validateModel() {
        BusyIndicator.showWhile(null, new Runnable() {
            @Override
            public void run() {
                updateStatusBar();
                Validator validator = new Validator(fModel);
                List<Object> result = validator.validate();
                fViewer.setInput(result);
                fViewer.expandAll();
            }
        });
    }
    
    private void updateStatusBar() {
        if(fModel != null) {
            getViewSite().getActionBars().getStatusLineManager().setMessage(ArchiLabelProvider.INSTANCE.getImage(fModel),
                    ArchiLabelProvider.INSTANCE.getLabel(fModel));
        }
        else {
            getViewSite().getActionBars().getStatusLineManager().setMessage(null, ""); //$NON-NLS-1$
        }
    }
    
    private void updateMenuItems(IStructuredSelection selection) {
        fActionExplain.setEnabled(isIssueSelected(selection));
    }
    
    private boolean isIssueSelected(IStructuredSelection selection) {
        for(Object o : selection.toArray()) {
            if(o instanceof IIssue) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isIssueObjectSelected(IStructuredSelection selection) {
        for(Object o : selection.toArray()) {
            if(o instanceof IIssue && ((IIssue)o).getObject() != null) {
                if(((IIssue)o).getObject() != null) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Object getAdapter(Class adapter) {
        /*
         * Return the PropertySheet Page
         */
        if(adapter == IPropertySheetPage.class) {
            return new TabbedPropertySheetPage(this);
        }
        
        return super.getAdapter(adapter);
    }

    @Override
    public String getContributorId() {
        return ArchiPlugin.PLUGIN_ID;
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
            if(fModel == newValue) {
                fModel = null;
                fViewer.setInput(null);
                fActionValidate.setEnabled(false);
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        
        // Unregister selection listener
        getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
        
        // Unregister us as a Model Manager Listener
        IEditorModelManager.INSTANCE.removePropertyChangeListener(this);
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
        return Messages.ValidatorView_3;
    }

}
