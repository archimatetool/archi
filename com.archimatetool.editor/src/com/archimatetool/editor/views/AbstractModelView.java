/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.help.IContextProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import com.archimatetool.editor.ArchimateEditorPlugin;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IRelationship;
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
     * If true will not refresh viewer on multiple eCore notifications
     */
    private Boolean fIgnoreECoreNotifications = false;
    
    /**
     * Buffer notifications to optimise updates
     */
    private List<Notification> notificationBuffer;
    
    
    @Override
    public void createPartControl(Composite parent) {
        doCreatePartControl(parent);
        
        // Register Global Action Handlers
        IActionBars actionBars = getViewSite().getActionBars();
        actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), fActionUndo);
        actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), fActionRedo);

        // Register us as a Model Listener - this has to be done last, *after* the tree/selection listener is created
        IEditorModelManager.INSTANCE.addPropertyChangeListener(this);
    }
    
    /**
     * Create Part Control
     * @param parent
     */
    protected abstract void doCreatePartControl(Composite parent);
    
    /**
     * Update the Undo/Redo Actions
     */
    protected void updateUndoActions() {
        fActionUndo.update();
        fActionRedo.update();
    }
    
    @Override
    public String getContributorId() {
        return ArchimateEditorPlugin.PLUGIN_ID;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
        /*
         * Return the PropertySheet Page
         */
        if(adapter == IPropertySheetPage.class) {
            return new TabbedPropertySheetPage(this);
        }
        
        // The Selected Archimate Model in scope
        if(adapter == IArchimateModel.class) {
            return getActiveArchimateModel();
        }
        
        // CommandStack (requested by GEF's UndoAction and RedoAction and our SaveAction)
        if(adapter == CommandStack.class) {
            IArchimateModel model = getActiveArchimateModel();
            if(model != null) {
                return model.getAdapter(CommandStack.class);
            }
            else {
                return EMPTY_COMMANDSTACK; // Need an Empty One!
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
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        Object newValue = evt.getNewValue();
        
        // Buffer all incoming notifications
        if(propertyName == IEditorModelManager.PROPERTY_ECORE_EVENTS_START) {
            fIgnoreECoreNotifications = true;
            notificationBuffer = new ArrayList<Notification>();
        }
        // Refresh all buffered notifications
        else if(propertyName == IEditorModelManager.PROPERTY_ECORE_EVENTS_END) {
            refreshElementsFromBufferedNotifications();
            fIgnoreECoreNotifications = false;
            notificationBuffer = null;
        }
        // ECore model event
        else if(propertyName == IEditorModelManager.PROPERTY_ECORE_EVENT) {
            // Normal event
            if(!fIgnoreECoreNotifications) {
                eCoreChanged((Notification)newValue);
            }
            // Else add to buffer
            else {
                notificationBuffer.add((Notification)newValue);
            }
        }
    }
    
    /**
     * React to ECore Model Changes to refresh the view
     * @param msg
     */
    protected void eCoreChanged(Notification msg) {
        int type = msg.getEventType();
        
        // Large structural model change - refresh the whole thing
        if(type == Notification.ADD_MANY || type == Notification.REMOVE_MANY || type == Notification.MOVE) {
            getViewer().refresh();
            return;
        }
        
        // Update affected element node(s)
        List<Object> elements = getElementsToUpdateFromNotification(msg);
        getViewer().update(elements.toArray(), null);

        // Refresh parent node
        Object parent = getParentToRefreshFromNotification(msg);
        if(parent != null) {
            getViewer().refresh(parent);
        }
    }
    
    /**
     * Refresh any tree elements from buffered notifications
     */
    protected void refreshElementsFromBufferedNotifications() {
        if(notificationBuffer == null) {
            return;
        }
        
        List<Object> refreshElements = new ArrayList<Object>();
        List<Object> updateElements = new ArrayList<Object>();
            
        for(Notification msg : notificationBuffer) {
            // Get parent nodes to refresh
            Object parent = getParentToRefreshFromNotification(msg);
            if(parent != null && !refreshElements.contains(parent)) {
                refreshElements.add(parent);
            }
            
            // Get elements to update
            List<Object> elements = getElementsToUpdateFromNotification(msg);
            for(Object object : elements) {
                if(!updateElements.contains(object)) {
                    updateElements.add(object);
                }
            }
        }
        
        // Refresh and update nodes
        getViewer().getControl().setRedraw(false);

        for(Object object : refreshElements) {
            getViewer().refresh(object);
        }

        for(Object object : updateElements) {
            getViewer().update(object, null);
        }

        getViewer().getControl().setRedraw(true);
    }
    
    /**
     * @param msg
     * @return The correct parent node (IFolder) to refresh when one of its children is added/removed
     */
    protected Object getParentToRefreshFromNotification(Notification msg) {
        int type = msg.getEventType();
        
        Object element = null;
        
        if(type == Notification.REMOVE) {
            element = msg.getNotifier();
        }
        else if(type == Notification.ADD) {
            element = msg.getNewValue();
            if(element instanceof EObject) {
                element = ((EObject)element).eContainer();
            }
        }
        else if(type == Notification.SET) {
            // Name changed - need to refresh parent node because of using a ViewerSorter to sort on name
            if(msg.getFeature() == IArchimatePackage.Literals.NAMEABLE__NAME) {
                element = msg.getNotifier();
                if(element instanceof EObject) {
                    element = ((EObject)element).eContainer();
                }
            }
        }
        
        if(element instanceof IDiagramModelArchimateObject) {
            element = ((IDiagramModelArchimateObject)element).getArchimateElement();
        }
        else if(element instanceof IDiagramModelArchimateConnection) {
            element = ((IDiagramModelArchimateConnection)element).getRelationship();
        }
        
        return (element instanceof IFolder) ? element : null;
    }
    
    /**
     * @param msg
     * @return All the tree element nodes that may need updating when a change occurs
     */
    protected List<Object> getElementsToUpdateFromNotification(Notification msg) {
        int type = msg.getEventType();
        
        List<Object> list = new ArrayList<Object>();
        
        Object element = null;
        
        if(type == Notification.REMOVE) {
            element = msg.getOldValue();
        }
        else if(type == Notification.ADD) {
            element = msg.getNewValue();
        }
        else if(type == Notification.SET) {
            element = msg.getNotifier();
        }
        
        // If it's a diagram object it may have child objects so treat it separately
        if(element instanceof IDiagramModelObject) {
            getElementsToUpdate(list, (IDiagramModelObject)element);
            return list;
        }
        
        // A diagram connection so get the relationship
        if(element instanceof IDiagramModelArchimateConnection) {
            element = ((IDiagramModelArchimateConnection)element).getRelationship();
        }
        
        // Got either a folder, a relationship or an element
        if(element != null) {
            if(!list.contains(element)) {
                list.add(element);
            }
            
            // If an element, also add any attached relationships
            if(element instanceof IArchimateElement) {
                getRelationshipsToUpdate(list, (IArchimateElement)element);
            }
        }
        
        return list;
    }
    
    /**
     * Find all elements contained in IDiagramModelObject including any child objects
     */
    private void getElementsToUpdate(List<Object> list, IDiagramModelObject dmo) {
        if(dmo instanceof IDiagramModelArchimateObject) {
            IArchimateElement element = ((IDiagramModelArchimateObject)dmo).getArchimateElement();
            if(!list.contains(element)) {
                list.add(element);
                getRelationshipsToUpdate(list, element);
            }
        }
        
        if(dmo instanceof IDiagramModelContainer) {
            for(IDiagramModelObject child : ((IDiagramModelContainer)dmo).getChildren()) {
                getElementsToUpdate(list, child);
            }
        }
    }
    
    /**
     * Find all relationships to update from given element
     */
    private void getRelationshipsToUpdate(List<Object> list, IArchimateElement element) {
        for(IRelationship relation : ArchimateModelUtils.getRelationships(element)) {
            if(!list.contains(relation)) {
                list.add(relation);
            }
        }
    }
    
    // =================================================================================
    // =================================================================================


    @Override
    public void dispose() {
        super.dispose();
        
        // Unregister us as a Model Manager Listener
        IEditorModelManager.INSTANCE.removePropertyChangeListener(this);
    }
}
