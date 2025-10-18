/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import java.util.Objects;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IFeature;
import com.archimatetool.model.IFeaturesEList;

/**
 * Convenience class to set and get IFeature names and values
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class FeaturesEList extends EObjectContainmentEList<IFeature> implements IFeaturesEList {
    
    public FeaturesEList(Class<?> dataClass, InternalEObject owner, int featureID) {
        super(dataClass, owner, featureID);
    }

    @Override
    public IFeature putString(String name, String value) {
        return putString(name, value, null);
    }
    
    @Override
    public IFeature putString(String name, String value, String defaultValue) {
        checkNull(name);
        
        if(value == null) {
            value = "";
        }
        
        if(defaultValue == null) {
            defaultValue = "";
        }
        
        // value == default value so remove it or don't add it and return null
        if(Objects.equals(value, defaultValue)) {
            remove(name);
            return null;
        }
        
        IFeature feature = getFeature(name);

        // New one
        if(feature == null) {
            feature = IArchimateFactory.eINSTANCE.createFeature();
            feature.setName(name);
            feature.setValue(value);
            add(feature);
        }
        // Different value
        else if(!Objects.equals(value, feature.getValue())) {
            feature.setValue(value);
        }
        
        return feature;
    }
    
    @Override
    public IFeature putInt(String name, int value) {
        return putString(name, Integer.toString(value), null);
    }
    
    @Override
    public IFeature putInt(String name, int value, int defaultValue) {
        return putString(name, Integer.toString(value), Integer.toString(defaultValue));
    }
    
    @Override
    public IFeature putBoolean(String name, boolean value) {
        return putString(name, Boolean.toString(value), null);
    }
    
    @Override
    public IFeature putBoolean(String name, boolean value, boolean defaultValue) {
        return putString(name, Boolean.toString(value), Boolean.toString(defaultValue));
    }
    
    @Override
    public String getString(String name, String defaultValue) {
        IFeature feature = getFeature(name);
        return feature == null ? defaultValue : feature.getValue();
    }

    @Override
    public int getInt(String name, int defaultValue) {
        try {
            IFeature feature = getFeature(name);
            return feature == null ? defaultValue : Integer.valueOf(feature.getValue());
        }
        catch(NumberFormatException ex) {
            return defaultValue;
        }
    }

    @Override
    public boolean getBoolean(String name, boolean defaultValue) {
        IFeature feature = getFeature(name);
        return feature == null ? defaultValue : Boolean.valueOf(feature.getValue());
    }

    @Override
    public boolean remove(String name) {
        checkNull(name);
        
        IFeature feature = getFeature(name);
        if(feature != null) {
            return remove(feature);
        }
        
        return false;
    }
    
    @Override
    public boolean has(String name) {
        return getFeature(name) != null;
    }

    @Override
    public IFeature getFeature(String name) {
        checkNull(name);
        
        for(IFeature f : this) {
            if(f.getName().equals(name)) {
                return f;
            }
        }
        
        return null;
    }

    private void checkNull(String s) {
        if(s == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
    }
    
    /**
     * Return true so that {@link #contains(Object)} checks {@link Feature#equals(Object)}
     * For unique entries keyed by name
     */
    @Override
    protected boolean useEquals() {
        return true;
    }
}
