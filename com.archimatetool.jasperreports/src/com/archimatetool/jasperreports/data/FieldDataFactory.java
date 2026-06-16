/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports.data;

import org.eclipse.emf.ecore.EObject;

import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.markdown.MarkdownUtils;
import com.archimatetool.model.IAccessRelationship;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDocumentable;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.IInfluenceRelationship;
import com.archimatetool.model.INameable;
import com.archimatetool.model.IProfiles;



/**
 * Factory class for return data child objects depending on data type and field value
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class FieldDataFactory {
    
    public static final String THIS = "this";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String DOCUMENTATION = "documentation";
    public static final String PURPOSE = "purpose";
    public static final String RELATION_SOURCE = "relation_source";
    public static final String RELATION_TARGET = "relation_target";
    public static final String INFLUENCE_STRENGTH = "influence_strength";
    public static final String ACCESS_TYPE = "access_accesstype";
    
    public static final String KEY = "key";
    public static final String VALUE = "value";
    
    public static final String IMAGE_PATH = "imagePath";
    public static final String VIEWPOINT = "viewpoint";
    
    public static Object getFieldValue(Object dataElement, String fieldName) {
        if(THIS.equals(fieldName)) {
            return dataElement;
        }
        
        if(ID.equals(fieldName) && dataElement instanceof IIdentifier identifier) {
            return identifier.getId();
        }
        
        if(NAME.equals(fieldName) && dataElement instanceof INameable nameable) {
            String name = nameable.getName();
            if(name == null || "".equals(name)) {
                name = ArchiLabelProvider.INSTANCE.getDefaultName(nameable.eClass());
            }
            return name;
        }
        
        if(TYPE.equals(fieldName) && dataElement instanceof EObject eObject) {
            // Class name
            String value = ArchiLabelProvider.INSTANCE.getDefaultName(eObject.eClass());
            
            // Profile Name
            if(dataElement instanceof IProfiles profiles && profiles.getPrimaryProfile() != null) {
                value += " (" + profiles.getPrimaryProfile().getName() + ")";
            }
            
            return value;
        }
        
        if(DOCUMENTATION.equals(fieldName) && dataElement instanceof IDocumentable documentable) {
            String text = documentable.getDocumentation();
            return StringUtils.isSet(text) ? MarkdownUtils.convertMarkdownToText(text) : null;
        }
        
        if(PURPOSE.equals(fieldName) && dataElement instanceof IArchimateModel model) {
            String text = model.getPurpose();
            return StringUtils.isSet(text) ? MarkdownUtils.convertMarkdownToText(text) : null;
        }
        
        if(RELATION_SOURCE.equals(fieldName) && dataElement instanceof IArchimateRelationship relation) {
            IArchimateConcept source = relation.getSource();
            String s = source.getName();
            return StringUtils.isSet(s) ? s : null;
        }
        
        if(RELATION_TARGET.equals(fieldName) && dataElement instanceof IArchimateRelationship relation) {
            IArchimateConcept target = relation.getTarget();
            String s = target.getName();
            return StringUtils.isSet(s) ? s : null;
        }
        
        if(INFLUENCE_STRENGTH.equals(fieldName) && dataElement instanceof IInfluenceRelationship relation) {
            String s = relation.getStrength();
            return StringUtils.isSet(s) ? s : null;
        }

        if(ACCESS_TYPE.equals(fieldName) && dataElement instanceof IAccessRelationship relation) {
            return relation.getAccessType();
        }
        
        return null;
    }
}
