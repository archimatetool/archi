/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IFilter;

import com.archimatetool.editor.ui.factory.IObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;

/**
 * Object Filter class to show or reject this section depending on input value
 */
public abstract class ObjectFilter implements IFilter, IObjectFilter {
    
    @Override
    public boolean select(Object object) {
        return adaptObject(object) != null;
    }
    
    @Override
    public Object adaptObject(Object object) {
        if(isRequiredType(object)) {
            return object;
        }
        
        if(object instanceof IAdaptable) {
            object = ((IAdaptable)object).getAdapter(getAdaptableType());
            return isRequiredType(object) ? object : null;
        }
        
        return null;
    }
    
    @Override
    @Deprecated
    public boolean shouldExposeFeature(EObject eObject, EAttribute feature) {
        return shouldExposeFeature(eObject, feature.getName());
    }
    
    
    @Override
    public boolean shouldExposeFeature(EObject eObject, String featureName) {
        IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProvider(eObject);
        
        if(provider != null) {
            return provider.shouldExposeFeature(featureName);
        }
        
        return true;
    }
}