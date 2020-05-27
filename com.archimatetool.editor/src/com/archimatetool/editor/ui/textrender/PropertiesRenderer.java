/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IProperty;

/**
 * Properties renderer
 * 
 * @author Phillip Beauvoir
 * @author jbsarrodie
 */
@SuppressWarnings("nls")
public class PropertiesRenderer extends AbstractTextRenderer {
    
    private static final String startOfExpression = "\\$" + allPrefixesGroup + "\\{";
    private static final String notStartOfExpression = "(?!" + startOfExpression + ")";
    private static final String acceptedKeyChar = "[^\\}]";
    private static final String key = "(" + notStartOfExpression + acceptedKeyChar + ")+";

    private static final Pattern PROPERTY_VALUE_PATTERN = Pattern.compile(startOfExpression + "property:(" + key + ")\\}");
    private static final Pattern PROPERTIES_PATTERN = Pattern.compile(startOfExpression + "properties\\}");
    private static final Pattern PROPERTIES_VALUES_PATTERN = Pattern.compile(startOfExpression + "propertiesvalues\\}");
    private static final Pattern FILTERED_PROPERTIES_WITH_SEPARATOR_PATTERN = Pattern.compile(startOfExpression + "properties:([^:]*):(" + key +")\\}");
    
    
    @Override
    public String render(IArchimateModelObject object, String text) {
        text = renderPropertyValue(object, text);
        text = renderPropertiesList(object, text);
        text = renderPropertiesValues(object, text);
        text = renderPropertiesValuesCustomList(object, text);
        return text;
    }
    
    private String renderPropertyValue(IArchimateModelObject object, String text) {
        Matcher matcher = PROPERTY_VALUE_PATTERN.matcher(text);
        
        while(matcher.find()) {
            String prefix = matcher.group(1);
            String key = matcher.group(2);
            String propertyValue = "";
            
            IArchimateModelObject refObject = getObjectFromPrefix(object, prefix);
            if(refObject instanceof IProperties) {
                IProperty property = getProperty((IProperties)refObject, key);
                if(property != null) {
                    propertyValue = property.getValue();
                }
            }
            
            text = text.replace(matcher.group(), propertyValue);
        }

        return text;
    }
    
    // List all properties like key: value
    private String renderPropertiesList(IArchimateModelObject object, String text) {
        Matcher matcher = PROPERTIES_PATTERN.matcher(text);
        
        while(matcher.find()) {
            String prefix = matcher.group(1);
            
            IArchimateModelObject refObject = getObjectFromPrefix(object, prefix);
            if(refObject instanceof IProperties) {
                text = text.replace(matcher.group(), getAllProperties((IProperties)refObject, true));
            }
        }
        
        return text;
    }
    
    // List all properties' values
    private String renderPropertiesValues(IArchimateModelObject object, String text) {
        Matcher matcher = PROPERTIES_VALUES_PATTERN.matcher(text);
        
        while(matcher.find()) {
            String prefix = matcher.group(1);
            
            IArchimateModelObject refObject = getObjectFromPrefix(object, prefix);
            if(refObject instanceof IProperties) {
                text = text.replace(matcher.group(), getAllProperties((IProperties)refObject, false));
            }
        }
        
        return text;
    }

    // List of all of a certain property key with separator
    private String renderPropertiesValuesCustomList(IArchimateModelObject object, String text) {
        Matcher matcher = FILTERED_PROPERTIES_WITH_SEPARATOR_PATTERN.matcher(text);
        
        while(matcher.find()) {
            String prefix = matcher.group(1);
            String separator = matcher.group(2);
            String key = matcher.group(3);
            String s = "";
            
            IArchimateModelObject refObject = getObjectFromPrefix(object, prefix);
            if(refObject instanceof IProperties) {
                for(IProperty property : ((IProperties)refObject).getProperties()) {
                    if(property.getKey().equals(key)) {
                        if(!s.isEmpty()) {
                            s += separator;
                        }
                        
                        s += property.getValue();
                    }
                }
                text = text.replace(matcher.group(), s);
            }
        }
        
        return text;
    }

    private String getAllProperties(IProperties object, boolean full) {
        String s = "";
        
        for(int i = 0; i < object.getProperties().size(); i++) {
            IProperty property = object.getProperties().get(i);
            
            if(full) {
                s += property.getKey() + ": ";
            }

            s += property.getValue();
            
            if(i < object.getProperties().size() - 1) {
                s += "\n";
            }
        }
        
        return s;
    }
    
    private IProperty getProperty(IProperties object, String key) {
        for(IProperty property : object.getProperties()) {
            if(property.getKey().equals(key)) {
                return property;
            }
        }
        
        return null;
    }
}