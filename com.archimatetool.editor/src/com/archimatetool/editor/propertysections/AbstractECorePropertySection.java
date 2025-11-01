/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.model.commands.NonNotifyingCompoundCommand;
import com.archimatetool.editor.ui.components.StyledTextControl;
import com.archimatetool.model.IAdapter;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IFeature;
import com.archimatetool.model.IFeatures;
import com.archimatetool.model.ILockable;
import com.archimatetool.model.util.LightweightEContentAdapter;



/**
 * Abstract Archi Property Section for Ecore Elements
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractECorePropertySection extends AbstractArchiPropertySection {
    
    /**
     * Set this flag to true when executing a command to stop unnecessary refreshing of controls.
     * @deprecated This field will be private in future versions (and renamed to <code>isExecutingCommand)</code>. Use {@link #isExecutingCommand()} instead.
     */
    @Deprecated
    protected boolean fIsExecutingCommand;
    
    /**
     * IArchimateModelObject objects that are selected and using this Property Section
     */
    private List<IArchimateModelObject> eObjects;
    
    /**
     * We need an EContentAdapter to listen to all child IFeature objects
     */
    private LightweightEContentAdapter eAdapter = new LightweightEContentAdapter(this::notifyChanged, IFeature.class);
    
    @Override
    protected void handleSelection(IStructuredSelection selection) {
        if(selection != getSelection()) { // block unnecessary double selections
            // Remove previous listener adapter
            removeAdapter();
            
            // Get the correct EObjects
            eObjects = getFilteredObjects(selection.toList());
            
            // Update section
            update();
            
            // Add listener adapter
            addAdapter();
        }
    }
    
    /**
     * Update the Property section
     */
    protected abstract void update();
    
    /**
     * Notify that an Ecore event happened which might affect the underlying object in this property section
     * Sub-classes should call super if they need to update the label name
     */
    protected void notifyChanged(Notification msg) {
        // Update properties label on name change
        if(msg.getFeature() == IArchimatePackage.Literals.NAMEABLE__NAME && msg.getNotifier() == getFirstSelectedObject()) {
            updatePropertiesLabel();
        }
    }
    
    /**
     * @return The IArchimateModelObjects that are selected and using this Property Section
     */
    protected List<IArchimateModelObject> getEObjects() {
        return eObjects;
    }
    
    /**
     * @return The first selected IArchimateModelObject
     */
    protected IArchimateModelObject getFirstSelectedObject() {
        return (eObjects == null || eObjects.isEmpty()) ? null : eObjects.get(0);
    }
    
    /**
     * Filter selected objects.
     * Also ensure that selected objects are from only one model.
     * We don't support selections from more than one model due to each model having its own command stack.
     * 
     * @return A list of filtered adaptable objects according to type
     */
    private List<IArchimateModelObject> getFilteredObjects(List<?> objects) {
        List<IArchimateModelObject> list = new ArrayList<>();
        
        IObjectFilter filter = getFilter();
        
        // Get underlying object if a Filter is applied
        for(Object object : objects) {
            if(filter != null) {
                object = filter.adaptObject(object);
            }
            
            if(object instanceof IArchimateModelObject modelObject) {
                list.add(modelObject);
            }
        }
        
        // Only use the objects that are in *one* model - the model in the first selected object
        if(!list.isEmpty()) {
            IArchimateModel firstModel = list.get(0).getArchimateModel();
            
            // Remove objects with different parent models
            for(int i = list.size() - 1; i >= 1; i--) {
                IArchimateModelObject eObject = list.get(i);
                if(eObject.getArchimateModel() != firstModel) {
                    list.remove(eObject);
                }
            }
        }
        
        return list;
    }

    /**
     * @return The Filter for this section
     */
    protected IObjectFilter getFilter() {
        return null;
    }
    
    /**
     * Execute a command on the first selected object's CommandStack
     * @param command The command to execute
     */
    protected void executeCommand(Command command) {
        EObject eObject = getFirstSelectedObject();
        
        if(eObject != null && eObject instanceof IAdapter adapter) {
            CommandStack commandStack = (CommandStack)adapter.getAdapter(CommandStack.class);
            if(commandStack != null) {
                try {
                    fIsExecutingCommand = true;
                    commandStack.execute(command);
                }
                finally {
                    fIsExecutingCommand = false;
                }
            }
        }
    }
    
    /**
     * @return true if a command is being run, false otherwise.
     * Some controls will be notified of a model change when a Command is run and can ignore the notification
     * @since 5.8.0
     */
    protected boolean isExecutingCommand() {
        return fIsExecutingCommand;
    }

    /**
     * If the Property sheet was Active (or Pinned) and the Element deleted then the Element's info could still be showing.
     * @return True if the eObject has a parent eContainer and belongs to an IArchimateModel, false if orphaned.
     */
    protected boolean isAlive(EObject eObject) {
        return (eObject != null) && (eObject instanceof IArchimateModel || eObject.eContainer() != null);
    }

    protected void addAdapter() {
        IArchimateModelObject selected = getFirstSelectedObject();
        Adapter adapter = getECoreAdapter();
        
        if(selected != null && adapter != null && !selected.eAdapters().contains(adapter)) {
            selected.eAdapters().add(adapter);
        }
    }
    
    protected void removeAdapter() {
        IArchimateModelObject selected = getFirstSelectedObject();
        Adapter adapter = getECoreAdapter();
        
        if(selected != null && adapter != null ) {
            selected.eAdapters().remove(adapter);
        }
    }
    
    /**
     * Return the Adapter to listen to model element changes
     */
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }
    
    /**
     * Return true if the message notification is a feature with the given name
     */
    protected boolean isFeatureNotification(Notification msg, String name) {
        return IFeatures.isFeatureNotification(msg, name);
    }
    
    /**
     * @param object
     * @return true if object is of type ILockable and is locked
     */
    protected boolean isLocked(Object object) {
        return object instanceof ILockable lockable && lockable.isLocked();
    }
    
    @Override
    public void dispose() {
        removeAdapter();
        eObjects = null;
    }
    
    // ===========================================================================================================================
    // WIDGET FACTORY METHODS
    // ===========================================================================================================================
    
    /**
     * Create a Name control
     */
    protected PropertySectionTextControl createNameControl(Composite parent, String hint) {
        // Label
        createLabel(parent, Messages.AbstractECorePropertySection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        // Text
        Text textControl = createSingleTextControl(parent, SWT.NONE);
        textControl.setMessage(hint);
        
        PropertySectionTextControl textName = new PropertySectionTextControl(textControl, IArchimatePackage.Literals.NAMEABLE__NAME) {
            @Override
            protected void textChanged(String oldText, String newText) {
                if(getEObjects() != null) {
                    CompoundCommand result = new NonNotifyingCompoundCommand(Messages.AbstractECorePropertySection_1);

                    for(EObject eObject : getEObjects()) {
                        if(isAlive(eObject)) {
                            Command cmd = new EObjectFeatureCommand(Messages.AbstractECorePropertySection_1, eObject,
                                    IArchimatePackage.Literals.NAMEABLE__NAME, newText);
                            if(cmd.canExecute()) {
                                result.add(cmd);
                            }
                        }
                    }

                    executeCommand(result.unwrap());
                }
            }
        };

        return textName;
    }

    /**
     * Create a Documentation control
     */
    protected PropertySectionTextControl createDocumentationControl(Composite parent, String hint) {
        // Label
        createLabel(parent, Messages.AbstractECorePropertySection_2, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.NONE);
        
        // Text
        StyledTextControl styledTextControl = createStyledTextControl(parent, SWT.NONE);
        styledTextControl.setMessage(hint);
        
        PropertySectionTextControl textDoc = new PropertySectionTextControl(styledTextControl.getControl(), IArchimatePackage.Literals.DOCUMENTABLE__DOCUMENTATION) {
            @Override
            protected void textChanged(String oldText, String newText) {
                if(getEObjects() != null) {
                    CompoundCommand result = new CompoundCommand(Messages.AbstractECorePropertySection_3);

                    for(EObject eObject : getEObjects()) {
                        if(isAlive(eObject)) {
                            Command cmd = new EObjectFeatureCommand(Messages.AbstractECorePropertySection_3 , eObject,
                                    IArchimatePackage.Literals.DOCUMENTABLE__DOCUMENTATION, newText);
                            if(cmd.canExecute()) {
                                result.add(cmd);
                            }
                        }
                    }

                    executeCommand(result.unwrap());
                }
            }
        };
        
        return textDoc;
    }
}
