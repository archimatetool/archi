/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.help.IContextProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.model.commands.EObjectNonNotifyingCompoundCommand;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.textrender.TextRenderer;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IProperty;
import com.archimatetool.model.util.ArchimateModelUtils;



/**
 * View that shows some aspect of the Model, and has hooks to it via its Command Stack and a Properties Sheet
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractModelView extends ViewPart
implements IContextProvider, PropertyChangeListener, ITabbedPropertySheetPageContributor, IModelView {

    /**
     * This is an empty, unused CommandStack used in case we have no Models open or selected in the Tree View.
     * GEF's UndoAction and RedoAction both expect a non-null CommandStack when they update their enablement state.
     */
    private static final CommandStack EMPTY_COMMANDSTACK = new CommandStack();

    protected UndoAction fActionUndo = new UndoAction(this);
    protected RedoAction fActionRedo = new RedoAction(this);
    
    /**
     * Application Preferences Listener
     */
    private IPropertyChangeListener prefsListener;

    protected IAction fActionSelectAll = new Action() {
        @Override
        public void run() {
            selectAll();
        }
    };
    
    @Override
    public void createPartControl(Composite parent) {
        doCreatePartControl(parent);
        
        // Register Global Action Handlers
        IActionBars actionBars = getViewSite().getActionBars();
        actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), fActionUndo);
        actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), fActionRedo);
        
        // Global Handle Select All
        // We need to enforce this at a global level in order to enable/disable the main menu item
        actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), fActionSelectAll);

        // Register us as a Model Listener - this has to be done last, *after* the tree/selection listener is created
        IEditorModelManager.INSTANCE.addPropertyChangeListener(this);
        
        // Listen to selections
        hookSelectionListener();
        
        // Prefs listener
        prefsListener = this::applicationPreferencesChanged;
        ArchiPlugin.PREFERENCES.addPropertyChangeListener(prefsListener);
    }
    
    private void hookSelectionListener() {
        getViewer().addSelectionChangedListener(new ISelectionChangedListener() {   
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                // Update status bar on selection
                Object selected = ((IStructuredSelection)event.getSelection()).getFirstElement();
                if(selected == null) {
                    selected = getViewer().getInput();
                }
                updateStatusBarWithSelection(selected);
                
                // Update shell text
                updateShellTitleBarWithFileName();
            }
        });
    }
    
    /**
     * Update Status Bar with selected image and text
     * @param selected
     */
    protected void updateStatusBarWithSelection(Object selected) {
        IStatusLineManager status = getViewSite().getActionBars().getStatusLineManager();
        
        if(selected instanceof IArchimateModelObject) {
            IArchimateModelObject object = (IArchimateModelObject)selected;
            Image image = ArchiLabelProvider.INSTANCE.getImage(object);
            
            String text = ""; //$NON-NLS-1$
            List<String> list = new ArrayList<String>();
            
            do {
                list.add(ArchiLabelProvider.INSTANCE.getLabelNormalised(object));
                object = (IArchimateModelObject)object.eContainer();
            } while((object != null));
            
            for(int i = list.size() - 1; i >= 0; i--) {
                text += list.get(i);
                if(i != 0) {
                    text += " > "; //$NON-NLS-1$
                }
            }
            
            status.setMessage(image, text);
        }
        else {
            status.setMessage(null, ""); //$NON-NLS-1$
        }
    }

    /**
     * Update Shell title bar with file name of current model
     */
    protected void updateShellTitleBarWithFileName() {
        String appname = Platform.getProduct().getName();
        
        if(getActiveArchimateModel() != null && getActiveArchimateModel().getFile() != null) {
            getSite().getShell().setText(appname + " - " + getActiveArchimateModel().getFile().getPath()); //$NON-NLS-1$
        }
        else {
            getSite().getShell().setText(appname);
        }
    }
    
    /**
     * Create Part Control
     * @param parent
     */
    protected abstract void doCreatePartControl(Composite parent);
    
    /**
     * Do the "Select All" global action. The default is to do nothing. Clients can over-ride.
     */
    protected void selectAll() {
    }
    
    /**
     * An Application Preference Property change event occurred
     * @param event
     */
    protected void applicationPreferencesChanged(org.eclipse.jface.util.PropertyChangeEvent event) {
    }

    /**
     * Update the Undo/Redo Actions
     */
    protected void updateUndoActions() {
        fActionUndo.update();
        fActionRedo.update();
    }
    
    @Override
    public String getContributorId() {
        return ArchiPlugin.PLUGIN_ID;
    }
    
    @Override
    public <T> T getAdapter(Class<T> adapter) {
        /*
         * Return the PropertySheet Page
         */
        if(adapter == IPropertySheetPage.class) {
            return adapter.cast(new TabbedPropertySheetPage(this));
        }
        
        // The Selected Archimate Model in scope
        if(adapter == IArchimateModel.class) {
            return adapter.cast(getActiveArchimateModel());
        }
        
        // CommandStack (requested by GEF's UndoAction and RedoAction and our SaveAction)
        if(adapter == CommandStack.class) {
            IArchimateModel model = getActiveArchimateModel();
            if(model != null) {
                return adapter.cast(model.getAdapter(CommandStack.class));
            }
            else {
                return adapter.cast(EMPTY_COMMANDSTACK); // Need an Empty One!
            }
        }

        return super.getAdapter(adapter);
    }
    
    /**
     * @return The active ArchimateModel in scope. May be null
     */
    protected abstract IArchimateModel getActiveArchimateModel();
    
    // =================================================================================
    //                       Listen to Editor Model Changes
    // =================================================================================
    
    /**
     * If true will not refresh viewer on multiple eCore notifications
     */
    private Boolean fAddingToBuffer = false;
    
    /**
     * Buffer notifications to optimise updates
     */
    private List<Notification> fNotificationBuffer;
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        Object newValue = evt.getNewValue();
        
        // Start: Buffer all incoming notifications
        if(propertyName == IEditorModelManager.PROPERTY_ECORE_EVENTS_START) {
            fAddingToBuffer = true;
            fNotificationBuffer = new ArrayList<Notification>();
        }
        // End: Refresh Viewer with buffered notifications
        else if(propertyName == IEditorModelManager.PROPERTY_ECORE_EVENTS_END) {
            doRefreshFromNotifications(fNotificationBuffer);
        }
        // ECore model event
        else if(propertyName == IEditorModelManager.PROPERTY_ECORE_EVENT) {
            // Normal event
            if(!fAddingToBuffer) {
                eCoreChanged((Notification)newValue);
            }
            // Else add to buffer
            else {
                fNotificationBuffer.add((Notification)newValue);
            }
        }
    }
    
    /**
     * React to ECore Model Changes to refresh the view
     */
    protected void eCoreChanged(Notification msg) {
        int type = msg.getEventType();
        
        // Not interested in these types
        if(type == Notification.ADD_MANY || type == Notification.REMOVE_MANY || type == Notification.MOVE
                || type == EObjectNonNotifyingCompoundCommand.START || type == EObjectNonNotifyingCompoundCommand.END) {
            return;
        }
        
        try {
            getViewer().getControl().setRedraw(false);

            // Update affected element node(s)
            Set<EObject> elements = getElementsToUpdateFromNotification(msg);
            getViewer().update(elements.toArray(), null);

            // Refresh parent node
            Object parent = getParentToRefreshFromNotification(msg);
            if(parent != null) {
                getViewer().refresh(parent);
            }
        }
        finally {
            getViewer().getControl().setRedraw(true);
        }
    }
    
    /**
     * Refresh any tree elements from buffered notifications
     * Overriders should call super after doing their thing
     */
    protected void doRefreshFromNotifications(List<Notification> notifications) {
        fAddingToBuffer = false;
        fNotificationBuffer = null;
    }
    
    /**
     * @return The parent folder to refresh when one of its children is added/removed/set
     */
    protected EObject getParentToRefreshFromNotification(Notification msg) {
        int type = msg.getEventType();
        EObject element = null;
        
        if(type == Notification.REMOVE) {
            if(msg.getNotifier() instanceof EObject) {
                element = (EObject)msg.getNotifier();
                
                // Property removed and element has format expression
                if(msg.getOldValue() instanceof IProperty && hasFormatExpression(element)) {
                    element = element.eContainer();
                }
            }
        }
        else if(type == Notification.ADD) {
            if(msg.getNewValue() instanceof EObject) {
                element = ((EObject)msg.getNewValue()).eContainer();
                
                // Property added and element has format expression
                if(msg.getNewValue() instanceof IProperty && hasFormatExpression(element)) {
                    element = element.eContainer();
                }
            }
        }
        else if(type == Notification.SET) {
            // Need to refresh parent node on name or label expression change because of using a ViewerSorter
            if(msg.getNotifier() instanceof EObject) {
                element = ((EObject)msg.getNotifier());
                
                // Name
                if(msg.getFeature() == IArchimatePackage.Literals.NAMEABLE__NAME) {
                    element = element.eContainer();
                }
                // Ancestor folder has label expression
                else if(hasFormatExpression(element)) {
                    element = element.eContainer();
                    
                    // Property set
                    if(msg.getNotifier() instanceof IProperty) {
                        element = element.eContainer();
                    }
                }
            }
        }
        
        return (element instanceof IFolder) ? element : null;
    }
    
    /**
     * @return true if this objects parent folder has a label expression
     */
    protected boolean hasFormatExpression(EObject eObject) {
        while(eObject != null) {
            if(eObject instanceof IFolder) {
                if(TextRenderer.getDefault().hasFormatExpression((IFolder)eObject)) {
                    return true;
                }
            }
            eObject = eObject.eContainer();
        }
        
        return false;
    }
    
    /**
     * @return All the tree element nodes that may need updating when a change occurs
     */
    protected Set<EObject> getElementsToUpdateFromNotification(Notification msg) {
        Set<EObject> list = new HashSet<>();
        
        // If notifier is a folder ignore
        if(msg.getNotifier() instanceof IFolder) {
            return list;
        }
        
        int type = msg.getEventType();
        
        EObject element = null;
        
        if(type == Notification.REMOVE) {
            element = (EObject)msg.getOldValue();
        }
        else if(type == Notification.ADD) {
            element = (EObject)msg.getNewValue();
        }
        else if(type == Notification.SET) {
            element = (EObject)msg.getNotifier();
        }
        
        // If it's a diagram object or a diagram dig in and treat it separately
        if(element instanceof IDiagramModelContainer) {
            getDiagramElementsToUpdate(list, (IDiagramModelContainer)element);
            return list;
        }
        
        // If it's a diagram connection get the relationship
        if(element instanceof IDiagramModelArchimateConnection) {
            element = ((IDiagramModelArchimateConnection)element).getArchimateRelationship();
        }
        
        // Got either a folder, a relationship or an element
        if(element != null) {
            list.add(element);
            
            // If an element, also add any attached relationships
            if(element instanceof IArchimateElement) {
                getRelationshipsToUpdate(list, (IArchimateElement)element);
            }
        }
        
        return list;
    }
    
    /**
     * Find all elements contained in Diagram or Diagram objects including any child objects
     */
    private void getDiagramElementsToUpdate(Set<EObject> list, IDiagramModelContainer container) {
        // ArchiMate element
        if(container instanceof IDiagramModelArchimateObject) {
            IArchimateElement element = ((IDiagramModelArchimateObject)container).getArchimateElement();
            list.add(element);
            getRelationshipsToUpdate(list, element);
        }
        
        // Children
        for(IDiagramModelObject child : container.getChildren()) {
            if(child instanceof IDiagramModelContainer) {
                getDiagramElementsToUpdate(list, (IDiagramModelContainer)child);
            }
        }
    }
    
    /**
     * Find all relationships to update from given element
     * TODO: A3 Does this need to be for all concepts?
     */
    private void getRelationshipsToUpdate(Set<EObject> list, IArchimateElement element) {
        for(IArchimateRelationship relation : ArchimateModelUtils.getAllRelationshipsForConcept(element)) {
            list.add(relation);
        }
    }
    
    // =================================================================================
    // =================================================================================


    @Override
    public void dispose() {
        super.dispose();
        
        // Unregister us as a Model Manager Listener
        IEditorModelManager.INSTANCE.removePropertyChangeListener(this);
        
        // Remove Prefs listener
        ArchiPlugin.PREFERENCES.removePropertyChangeListener(prefsListener);
        
        // Update shell text
        getSite().getShell().setText(Platform.getProduct().getName());
    }
}
