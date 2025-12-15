/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import java.util.Objects;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EcoreEMap;

import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IFeaturesEMap;

/**
 * Convenience class to set and get IFeature names and values
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class FeaturesEMap extends EcoreEMap<String, String> implements IFeaturesEMap {
    
    public FeaturesEMap(InternalEObject owner, int featureID) {
        super(IArchimatePackage.Literals.FEATURE_ENTRY, FeatureEntry.class, owner, featureID);
    }

    @Override
    public void putString(String name, String value) {
        putString(name, value, null);
    }
    
    @Override
    public void putString(String name, String value, String defaultValue) {
        checkNull(name);
        
        if(value == null) {
            value = "";
        }
        
        if(defaultValue == null) {
            defaultValue = "";
        }
        
        // value == default value so remove it or don't add it
        if(Objects.equals(value, defaultValue)) {
            remove(name);
        }
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
        try {
            String value = get(name);
            return value == null ? defaultValue : Integer.valueOf(value);
        }
        catch(NumberFormatException ex) {
            return defaultValue;
        }
    }

    @Override
    public boolean getBoolean(String name, boolean defaultValue) {
        String value = get(name);
        return value == null ? defaultValue : Boolean.valueOf(value);
    }

    @Override
    public void remove(String name) {
        checkNull(name);
        removeKey(name);
    }
    
    @Override
    public boolean has(String name) {
        checkNull(name);
        return get(name) != null;
    }

    @Override
    public FeatureEntry getFeatureEntry(String name) {
        checkNull(name);
        int index = indexOfKey(name);
        return index == -1 ? null : (FeatureEntry)get(index);
    }

    private void checkNull(String s) {
        if(s == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
    }
}
