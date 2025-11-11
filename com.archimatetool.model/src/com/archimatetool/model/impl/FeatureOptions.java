/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.archimatetool.model.IFeatureOptions;

/**
 * Feature Options<p>
 * 
 * Get and set options in a Feature string.<p>
 * 
 * For example, suppose a feature is persisted in XML as:<p>
 * <code>&lt;feature name="legend" value="display=15,rows=28,offset=0,color=2"/&gt;</code><p>
 * 
 * Then create a sub-class of this class and implement {@link #toFeatureString()} and setters and getters.
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public abstract class FeatureOptions implements IFeatureOptions {
    
    private Map<String, Object> options = new HashMap<>();

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IFeatureOptions> T setValue(String key, Object value) {
        if(key == null || value == null) {
            throw new IllegalArgumentException("key/value cannot be null");
        }
        options.put(key, value);
        return (T)this;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <V> V getValue(String key, V defaultValue) {
        if(key == null || defaultValue == null) {
            throw new IllegalArgumentException("key/defaultValue cannot be null");
        }
        Object value = options.get(key);
        return defaultValue.getClass().isInstance(value) ? (V)value : defaultValue;
    }
    
    protected static Pattern regexIntegerPattern(String prefix) {
        return Pattern.compile(Pattern.quote(prefix) + "=\\s*(-?\\d+)\\s*");
    }
    
    protected int parseInteger(String featureValue, String prefix, int defaultValue) {
        Matcher matcher = regexIntegerPattern(prefix).matcher(featureValue);
        if(matcher.find() && matcher.groupCount() > 0) {
            try {
                return Integer.parseInt(matcher.group(1));
            }
            catch(NumberFormatException ex) {}
        }
        return defaultValue;
    }
    
    protected static Pattern regexBooleanPattern(String prefix) {
        return Pattern.compile(Pattern.quote(prefix) + "=\\s*(true|false)\\s*(?:,|$)", Pattern.CASE_INSENSITIVE);
    }
    
    protected boolean parseBoolean(String featureValue, String prefix, boolean defaultValue) {
        Matcher matcher = regexBooleanPattern(prefix).matcher(featureValue);
        return matcher.find() && matcher.groupCount() > 0 ? Boolean.valueOf(matcher.group(1)) : defaultValue;
    }
    
    protected static Pattern regexStringPattern(String prefix) {
        return Pattern.compile(Pattern.quote(prefix) + "=\\s*([^,\\s][^,]*?)\\s*(?:,|$)");
    }
    
    protected String parseString(String featureValue, String prefix, String defaultValue) {
        Matcher matcher = regexStringPattern(prefix).matcher(featureValue);
        return matcher.find() && matcher.groupCount() > 0 ? matcher.group(1) : defaultValue;
    }
}