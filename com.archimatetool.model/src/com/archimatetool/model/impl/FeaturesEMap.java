/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreEMap;

import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IFeaturesEMap;

/**
 * Convenience class to set and get feature names and values
 * 
 * @author Phillip Beauvoir
 */
public class FeaturesEMap extends EcoreEMap<String, String> implements IFeaturesEMap {
    
    public FeaturesEMap(InternalEObject owner, int featureID) {
        super(IArchimatePackage.Literals.FEATURE, Feature.class, owner, featureID);
    }

    @Override
    public void putString(String name, String value) {
        putString(name, value, null);
    }
    
    @Override
    public void putString(String name, String value, String defaultValue) {
        checkNull(name);
        checkNull(value);
        
        // Value equals default value so remove it
        if(value.equals(defaultValue)) {
            remove(name);
        }
        // New one or changed value
        else {
            put(name, value);
        }
    }
    
    @Override
    public void putInt(String name, int value) {
        putString(name, Integer.toString(value), null);
    }
    
    @Override
    public void putInt(String name, int value, int defaultValue) {
        putString(name, Integer.toString(value), Integer.toString(defaultValue));
    }
    
    @Override
    public void putBoolean(String name, boolean value) {
        putString(name, Boolean.toString(value), null);
    }
    
    @Override
    public void putBoolean(String name, boolean value, boolean defaultValue) {
        putString(name, Boolean.toString(value), Boolean.toString(defaultValue));
    }
    
    @Override
    public String getString(String name, String defaultValue) {
        String value = get(name);
        return value == null ? defaultValue : value;
    }

    @Override
    public int getInt(String name, int defaultValue) {
        String val = getString(name, String.valueOf(defaultValue));
        try {
            return Integer.valueOf(val);
        }
        catch(NumberFormatException ex) {
            return defaultValue;
        }
    }

    @Override
    public boolean getBoolean(String name, boolean defaultValue) {
        String val = getString(name, Boolean.toString(defaultValue));
        return Boolean.valueOf(val);
    }

    @Override
    public void remove(String name) {
        checkNull(name);
        removeKey(name);
    }
    
    @Override
    public boolean has(String name) {
        return get(name) != null;
    }

    private void checkNull(String s) {
        if(s == null) {
            throw new IllegalArgumentException("key or value cannot be null"); //$NON-NLS-1$
        }
    }
}
