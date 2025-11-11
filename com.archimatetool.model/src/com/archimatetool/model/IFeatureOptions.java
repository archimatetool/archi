/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model;

/**
 * Feature Options
 * 
 * @author Phillip Beauvoir
 */
public interface IFeatureOptions {

    /**
     * Set a value by key and value
     */
    <T extends IFeatureOptions> T setValue(String key, Object value);
    
    /**
     * @return a value or defaultValue if the value is not found
     */
    <V> V getValue(String key, V defaultValue);

    /**
     * @return a string with the options that can be saved to a IFeature
     */
    default String toFeatureString() {
        return null;
    }
}