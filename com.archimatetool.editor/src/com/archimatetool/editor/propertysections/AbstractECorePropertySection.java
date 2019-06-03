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
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.model.commands.NonNotifyingCompoundCommand;
import com.archimatetool.editor.ui.components.StyledTextControl;
import com.archimatetool.model.IAdapter;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ILockable;



/**
 * Abstract Archi Property Section for Ecore Elements
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractECorePropertySection extends AbstractArchiPropertySection {
    
    /**
     * Set this flag to true when executing a command to stop unnecessary refreshing of controls
     */
    protected boolean fIsExecutingCommand;
    
    /**
     * EObjects that are the subject of this Property Section
     */
    private List<EObject> fObjects;
    
    /**
     * Adapter to listen to model element changes
     * Use a EContentAdapter to listen to child changes
     */
    private Adapter eAdapter = new EContentAdapter()  {
        @Override
        public void notifyChanged(Notification msg) {
            super.notifyChanged(msg);
            AbstractECorePropertySection.this.notifyChanged(msg);
        }
    };
    
    @Override
    protected void handleSelection(IStructuredSelection selection) {
        // stop double-firing
        if(selection != getSelection()) { 
            // Remove previous listener adapter
            removeEObjectAdapter();
            
            // Get the correct EObjects
            fObjects = getFilteredObjects(selection.toList());
            
            // Update section
            update();
            
            // Add ECore listener adapter
            addEObjectAdapter();
        }
    }
    
    /**
     * Update the Property section
     */
    protected abstract void update();
    
    /**
     * Notify that an Ecore event happened which might affect the underlying object in this property section
     * @param msg
     */
    protected void notifyChanged(Notification msg) {
    }
    
    /**
     * @return The EObjects for this Property Section
     */
    protected List<EObject> getEObjects() {
        return fObjects;
    }
    
    /**
     * @return The first selected object
     */
    protected EObject getFirstSelectedObject() {
        return (fObjects == null || fObjects.isEmpty()) ? null : fObjects.get(0);
    }
    
    /**
     * Filter selected objects.
     * Also ensure that selected objects are from only one model.
     * We don't support selections from more than one model due to each model having its own command stack.
     * 
     * @return A list of filtered adaptable objects according to type
     */
    private List<EObject> getFilteredObjects(List<?> objects) {
        ArrayList<EObject> list = new ArrayList<EObject>();
        
        IObjectFilter filter = getFilter();
        
        // Get underlying object if a Filter is applied
        for(Object object : objects) {
            if(filter != null) {
                object = filter.adaptObject(object);
            }
            
            if(object instanceof EObject) {
                list.add((EObject)object);
            }
        }
        
        // Only use the objects that are in *one* model - the model in the first selected object
        if(!list.isEmpty()) {
            IArchimateModel firstModel = ((IArchimateModelObject)list.get(0)).getArchimateModel();
            
            // Remove objects with different parent models
            for(int i = list.size() - 1; i >= 1; i--) {
                IArchimateModelObject eObject = (IArchimateModelObject)list.get(i);
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
     * Execuate a command on the selected objects' CommandStack
     * @param cmd
     */
    protected void executeCommand(Command command) {
        fIsExecutingCommand = true;
        
        EObject eObject = getFirstSelectedObject();
        
        if(eObject != null && eObject instanceof IAdapter) {
            CommandStack commandStack = (CommandStack)((IAdapter)eObject).getAdapter(CommandStack.class);
            if(commandStack != null) {
                commandStack.execute(command);
            }
        }
        
        fIsExecutingCommand = false;
    }
    
    /**
     * If the Property sheet was Active (or Pinned) and the Element deleted then the Element's
     * info could still be showing.
     * @return True if alive
     */
    protected boolean isAlive(EObject eObject) {
        return (eObject != null) && (eObject instanceof IArchimateModel || eObject.eContainer() != null);
    }

    private void addEObjectAdapter() {
        EObject object = getFirstSelectedObject();
        
        if(object != null && !object.eAdapters().contains(eAdapter)) {
            object.eAdapters().add(eAdapter);
        }
    }
    
    private void removeEObjectAdapter() {
        EObject object = getFirstSelectedObject();
        
        if(object != null) {
            object.eAdapters().remove(eAdapter);
        }
    }
    
    /**
     * @param object
     * @return true if object is of type ILockable and is locked
     */
    protected boolean isLocked(Object object) {
        return object instanceof ILockable && ((ILockable)object).isLocked();
    }
    
    @Override
    public void dispose() {
        removeEObjectAdapter();
    }
    
    // ===========================================================================================================================
    // WIDGET FACTORY METHODS
    // ===========================================================================================================================
    
    /**
     * Create a Name control
     */
    protected PropertySectionTextControl createNameControl(Composite parent, String hint) {
        // Label
        Label label = createLabel(parent, Messages.AbstractECorePropertySection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        // CSS
        label.setData("org.eclipse.e4.ui.css.CssClassName", "PropertiesNameLabel"); //$NON-NLS-1$ //$NON-NLS-2$

        // Text
        Text textControl = createSingleTextControl(parent, SWT.NONE);
        
        // CSS
        textControl.setData("org.eclipse.e4.ui.css.CssClassName", "PropertiesNameText"); //$NON-NLS-1$ //$NON-NLS-2$

        PropertySectionTextControl textName = new PropertySectionTextControl(textControl, IArchimatePackage.Literals.NAMEABLE__NAME) {
            @Override
            protected void textChanged(String oldText, String newText) {
                if(fObjects != null) {
                    CompoundCommand result = new NonNotifyingCompoundCommand(Messages.AbstractECorePropertySection_1);

                    for(EObject eObject : fObjects) {
                        if(isAlive(eObject)) {
                            Command cmd = new EObjectFeatureCommand(Messages.AbstractECorePropertySection_1 + " " + oldText, eObject, //$NON-NLS-1$
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
        textName.setHint(hint);

        return textName;
    }

    /**
     * Create a Documentation control
     */
    protected PropertySectionTextControl createDocumentationControl(Composite parent, String hint) {
        // Label
        Label label = createLabel(parent, Messages.AbstractECorePropertySection_2, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.NONE);
        
        // CSS
        label.setData("org.eclipse.e4.ui.css.CssClassName", "PropertiesDocumentationLabel"); //$NON-NLS-1$ //$NON-NLS-2$
        
        // Text
        StyledTextControl styledTextControl = createStyledTextControl(parent, SWT.NONE);
        
        // CSS
        styledTextControl.getControl().setData("org.eclipse.e4.ui.css.CssClassName", "PropertiesDocumentationText"); //$NON-NLS-1$ //$NON-NLS-2$
        
        PropertySectionTextControl textDoc = new PropertySectionTextControl(styledTextControl.getControl(), IArchimatePackage.Literals.DOCUMENTABLE__DOCUMENTATION) {
            @Override
            protected void textChanged(String oldText, String newText) {
                if(fObjects != null) {
                    CompoundCommand result = new CompoundCommand(Messages.AbstractECorePropertySection_3);

                    for(EObject eObject : fObjects) {
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
        textDoc.setHint(hint);
        
        return textDoc;
    }
}
