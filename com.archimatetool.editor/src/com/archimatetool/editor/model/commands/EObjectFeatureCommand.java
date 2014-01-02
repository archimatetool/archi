/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.commands;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gef.commands.Command;


/**
 * General Purpose Command for setting a new value for an EObject's feature
 * 
 * @author Phillip Beauvoir
 */
public class EObjectFeatureCommand extends Command {

    protected EObject fEObject;
    protected EStructuralFeature fFeature;
    protected Object fOldValue;
    protected Object fNewValue;
    
    public EObjectFeatureCommand(String label, EObject eObject, EStructuralFeature feature, Object newValue) {
        super(label);
        fEObject = eObject;
        fFeature = feature;
        fOldValue = fEObject.eGet(feature);
        fNewValue = newValue;
    }
    
    @Override
    public void execute() {
        fEObject.eSet(fFeature, fNewValue);
    }

    @Override
    public void undo() {
        fEObject.eSet(fFeature, fOldValue);
    }
    
    @Override
    public boolean canExecute() {
        return (fNewValue != null) ? !fNewValue.equals(fOldValue)
                : (fOldValue != null) ? !fOldValue.equals(fNewValue)
                : false;
    }
    
    @Override
    public void dispose() {
        fEObject = null;
    }
}
