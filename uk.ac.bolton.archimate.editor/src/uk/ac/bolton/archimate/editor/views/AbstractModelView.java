/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.views;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.help.IContextProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import uk.ac.bolton.archimate.editor.ArchimateEditorPlugin;
import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IArchimateModelElement;
import uk.ac.bolton.archimate.model.IArchimatePackage;


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
            Object selected = ((IStructuredSelection)getViewer().getSelection()).getFirstElement();
            if(selected instanceof IArchimateModel) {
                return selected;
            }
            if(selected instanceof IArchimateModelElement) {
                return ((IArchimateModelElement)selected).getArchimateModel();
            }
            return null;
        }
        
        // CommandStack (requested by GEF's UndoAction and RedoAction and our SaveAction)
        if(adapter == CommandStack.class) {
            IArchimateModel model = (IArchimateModel)getAdapter(IArchimateModel.class);
            if(model != null) {
                return model.getAdapter(CommandStack.class);
            }
            else {
                return EMPTY_COMMANDSTACK; // Need an Empty One!
            }
        }

        return super.getAdapter(adapter);
    }
    
    // =================================================================================
    //                       Listen to Editor Model Changes
    // =================================================================================
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        Object newValue = evt.getNewValue();
        
        // Tree Refresh Off
        if(propertyName == IEditorModelManager.PROPERTY_ECORE_EVENTS_START) {
            fIgnoreECoreNotifications = true;
        }
        // Tree Refresh On
        else if(propertyName == IEditorModelManager.PROPERTY_ECORE_EVENTS_END) {
            fIgnoreECoreNotifications = false;
            // and Refresh now
            getViewer().getControl().setRedraw(false);
            getViewer().refresh();
            getViewer().getControl().setRedraw(true);
        }
        // ECore model event
        else if(propertyName == IEditorModelManager.PROPERTY_ECORE_EVENT && !fIgnoreECoreNotifications) {
            eCoreChanged((Notification)newValue);
        }
    }
    
    /**
     * React to ECore Model Changes
     * @param msg
     */
    protected void eCoreChanged(Notification msg) {
        int type = msg.getEventType();
        
        // Structural Model change
        if(type == Notification.ADD || type == Notification.ADD_MANY ||
                type == Notification.REMOVE || type == Notification.REMOVE_MANY || type == Notification.MOVE) {
            getViewer().refresh();
        }
        // Set
        else if(type == Notification.SET) {
            Object notifier = msg.getNotifier();
            Object feature = msg.getFeature();
            
            // Element Name - need to use refresh() because of ViewerSorter
            if(feature == IArchimatePackage.Literals.NAMEABLE__NAME) {
                getViewer().refresh();
            }
            // Interface type
            else if(feature == IArchimatePackage.Literals.INTERFACE_ELEMENT__INTERFACE_TYPE) {
                getViewer().update(notifier, null);
            }
        }
        
    }

    @Override
    public void dispose() {
        super.dispose();
        
        // Unregister us as a Model Manager Listener
        IEditorModelManager.INSTANCE.removePropertyChangeListener(this);
    }
}
