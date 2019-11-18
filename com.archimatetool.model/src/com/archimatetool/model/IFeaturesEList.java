/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

import org.eclipse.emf.common.util.EList;

/**
 * Convenience class to set and get and set keys and values from a IFeature EList
 * 
 * @author Phillip Beauvoir
 */
public interface IFeaturesEList extends EList<IFeature> {

    /**
     * Put or update a name/value as an IFeature
     * @param name The name, non-null. If it already exists the value will be updated with the new value.
     * @param value The value, non-null string.
     * @return the updated or new feature
     */
    IFeature putString(String name, String value);
    
    /**
     * Put or update a name/value as an IFeature
     * @param name The name, non-null. If it already exists the value will be updated with the new value.
     * @param value The value, non-null string.
     * @param defaultValue The default value. If value and default value are equal the feature is removed
     * @return the updated or new feature
     */
    IFeature putString(String name, String value, String defaultValue);
    
    /**
     * Put or update a name/value as an IFeature where value is an integer
     * @param name The name, non-null. If it already exists the value will be updated with the new value.
     * @param value The value, integer.
     * @return the updated or new feature
     */
    IFeature putInt(String name, int value);
    
    /**
     * Put or update a name/value as an IFeature where value is an integer
     * @param name The name, non-null. If it already exists the value will be updated with the new value.
     * @param value The value, integer.
     * @param defaultValue The default value. If value and default value are equal the feature is removed
     * @return the updated or new feature
     */
    IFeature putInt(String name, int value, int defaultValue);
    
    /**
     * Put or update a name/value as an IFeature where value is a boolean
     * @param name The name, non-null. If it already exists the value will be updated with the new value.
     * @param value The value, boolean.
     * @return the updated or new feature
     */
    IFeature putBoolean(String name, boolean value);

    /**
     * Put or update a name/value as an IFeature where value is a boolean
     * @param name The name, non-null. If it already exists the value will be updated with the new value.
     * @param value The value, boolean.
     * @param defaultValue The default value. If value and default value are equal the feature is removed
     * @return the updated or new feature
     */
    IFeature putBoolean(String name, boolean value, boolean defaultValue);

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
     * @return true if the entry was removed
     */
    boolean remove(String name);
    
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