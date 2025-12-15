/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
    
    public FeaturesEList(InternalEObject owner, int featureID) {
        super(IFeature.class, owner, featureID);
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
        
        return stream()
                .filter(feature -> Objects.equals(feature.getName(), name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Return true so that {@link #contains(Object)} checks {@link Feature#equals(Object)}
     * for unique entries keyed by name.
     */
    @Override
    protected boolean useEquals() {
        return Feature.useEquals();
    }

    private void checkNull(String s) {
        if(s == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
    }
    
    /*
     * Over-ride these methods so duplicates can't be added.
     * 
     * No need to over-ride addAllUnique(Collection<? extends E> collection)
     * as it calls addAllUnique(int index, Collection<? extends IFeature> collection)
     */
    
    @Override
    public boolean add(IFeature feature) {
        // If we are checking equality using Feature#equals() just call super()
        if(useEquals()) {
            return super.add(feature);
        }

        // If we are not checking feature name equality in Feature#equals() we need to check it here
        return containsName(feature.getName()) ? false : super.add(feature);
    }
    
    @Override
    public void add(int index, IFeature feature) {
        if(!containsName(feature.getName())) {
            super.add(index, feature);
        }
    }
    
    @Override
    public void addUnique(IFeature feature) {
        if(!containsName(feature.getName())) {
            super.addUnique(feature);
        }
    }
    
    @Override
    public void addUnique(int index, IFeature feature) {
        if(!containsName(feature.getName())) {
            super.addUnique(index, feature);
        }
    }
    
    @Override
    public boolean addAllUnique(int index, Collection<? extends IFeature> collection) {
        // Remove actual duplicate objects from the supplied collection
        collection = getNonDuplicates(collection);
        
        // Remove objects with the same feature name from the supplied collection
        collection = getNonDuplicateNames(collection);
        
        // If we are not checking equality with equals() remove objects with the same feature name from this FeaturesEList
        if(!useEquals()) {
            for(IFeature feature : new ArrayList<>(collection)) {
                // If this FeaturesEList contains a feature as one with the same name in the collection remove it from the collection
                if(containsName(feature.getName())) {
                    collection.remove(feature);
                }
            }
        }
        
        return super.addAllUnique(index, collection);
    }
    
    /**
     * @return the collection with all duplicates by name removed
     */
    private Collection<? extends IFeature> getNonDuplicateNames(Collection<? extends IFeature> collection) {
        List<IFeature> result = new ArrayList<>(collection);
        Set<String> seen = new HashSet<>();
        result.removeIf(feature -> !seen.add(feature.getName()));
        return result;
    }
    
    /**
     * @return true if this FeaturesEList contains a feature with the given name
     */
    private boolean containsName(String name) {
        return stream().anyMatch(o -> Objects.equals(o.getName(), name));
    }
}
