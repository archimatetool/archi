/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.jasperreports.data;

import org.eclipse.emf.ecore.EObject;

import uk.ac.bolton.archimate.editor.ui.ArchimateNames;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IDocumentable;
import uk.ac.bolton.archimate.model.INameable;
import uk.ac.bolton.archimate.model.IRelationship;


/**
 * Factory class for return data child objects depending on data type and field value
 * 
 * @author Phillip Beauvoir
 */
public class FieldDataFactory {

    public static Object getFieldValue(Object dataElement, String fieldName) {
        if("this".equals(fieldName)) {
            return dataElement;
        }
        if("name".equals(fieldName) && dataElement instanceof INameable) {
            return ((INameable)dataElement).getName();
        }
        if("type".equals(fieldName) && dataElement instanceof EObject) {
            return ArchimateNames.getDefaultName(((EObject)dataElement).eClass());
        }
        if("documentation".equals(fieldName) && dataElement instanceof IDocumentable) {
            String s = ((IDocumentable)dataElement).getDocumentation();
            return StringUtils.isSet(s) ? s : null;
        }
        if("purpose".equals(fieldName) && dataElement instanceof IArchimateModel) {
            String s = ((IArchimateModel)dataElement).getPurpose();
            return StringUtils.isSet(s) ? s : null;
        }
        if("relation_source".equals(fieldName) && dataElement instanceof IRelationship) {
            IRelationship relation = (IRelationship)dataElement;
            IArchimateElement source = relation.getSource();
            String s = source.getName();
            return StringUtils.isSet(s) ? s : null;
        }
        if("relation_target".equals(fieldName) && dataElement instanceof IRelationship) {
            IRelationship relation = (IRelationship)dataElement;
            IArchimateElement target = relation.getTarget();
            String s = target.getName();
            return StringUtils.isSet(s) ? s : null;
        }
        
        return null;
    }
}
