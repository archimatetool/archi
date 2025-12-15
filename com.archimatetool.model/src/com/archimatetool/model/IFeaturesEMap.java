/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.eclipse.emf.common.util.EMap;


/**
 * Convenience class to set and get and set keys and values from a IFeatures Map
 * 
 * @author Phillip Beauvoir
 */
public interface IFeaturesEMap extends EMap<String, String> {

    /**
     * Put or update a name/value as an IFeature
     * @param name The name, non-null. If it already exists the value will be updated with the new value.
     * @param value The value. If null will be set to an empty string.
     */
    void putString(String name, String value);
    
    /**
     * Put or update a name/value as an IFeature
     * @param name The name, non-null. If it already exists the value will be updated with the new value.
     * @param value The value, non-null string.
     * @param defaultValue The default value. If value and default value are equal the feature is either not added or is removed and null is returned
     */
    void putString(String name, String value, String defaultValue);
    
    /**
     * Put or update a name/value as an IFeature where value is an integer
     * @param name The name, non-null. If it already exists the value will be updated with the new value.
     * @param value The value, integer.
     */
    void putInt(String name, int value);
    
    /**
     * Put or update a name/value as an IFeature where value is an integer
     * @param name The name, non-null. If it already exists the value will be updated with the new value.
     * @param value The value, integer.
     * @param defaultValue The default value. If value and default value are equal the feature is removed
     */
    void putInt(String name, int value, int defaultValue);
    
    /**
     * Put or update a name/value as an IFeature where value is a boolean
     * @param name The name, non-null. If it already exists the value will be updated with the new value.
     * @param value The value, boolean.
     */
    void putBoolean(String name, boolean value);

    /**
     * Put or update a name/value as an IFeature where value is a boolean
     * @param name The name, non-null. If it already exists the value will be updated with the new value.
     * @param value The value, boolean.
     * @param defaultValue The default value. If value and default value are equal the feature is removed
     */
    void putBoolean(String name, boolean value, boolean defaultValue);

    /**
     * Return a value from its name. If it does not exist, defaultValue is returned.
     * @param name The name, non-null.
     * @param defaultValue The default value.
     * @return The value.
     */
    String getString(String name, String defaultValue);

    /**
     * Return an integer value from its name. If it does not exist, defaultValue is returned.
     * @param name The name, non-null.
     * @param defaultValue The default value.
     * @return The value.
     */
    int getInt(String name, int defaultValue);
    
    /**
     * Return a boolean value from its name. If it does not exist, defaultValue is returned.
     * @param name The name, non-null.
     * @param defaultValue The default value.
     * @return The value.
     */
    boolean getBoolean(String name, boolean defaultValue);
    
    /** 
     * Remove an entry by its name
     * @param name The name, non-null.
     */
    void remove(String name);
    
    /**
     * @param name The name, non-null.
     * @return true if there is an entry with name
     */
    boolean has(String name);
    
    /**
     * @param name The name, non-null.
     * @return the feature of the given name, or null if not found
     */
    IFeature getFeature(String name);
}