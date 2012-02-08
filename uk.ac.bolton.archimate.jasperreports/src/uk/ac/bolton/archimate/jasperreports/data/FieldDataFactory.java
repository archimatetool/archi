/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.jasperreports.data;

import org.eclipse.emf.ecore.EObject;

import uk.ac.bolton.archimate.editor.ui.ArchimateLabelProvider;
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
        if("this".equals(fieldName)) { //$NON-NLS-1$
            return dataElement;
        }
        if("name".equals(fieldName) && dataElement instanceof INameable) { //$NON-NLS-1$
            String name = ((INameable)dataElement).getName();
            if(name == null || "".equals(name)) { //$NON-NLS-1$
                name = ArchimateLabelProvider.INSTANCE.getDefaultName(((EObject)dataElement).eClass());
            }
            return name;
        }
        if("type".equals(fieldName) && dataElement instanceof EObject) { //$NON-NLS-1$
            return ArchimateLabelProvider.INSTANCE.getDefaultName(((EObject)dataElement).eClass());
        }
        if("documentation".equals(fieldName) && dataElement instanceof IDocumentable) { //$NON-NLS-1$
            String s = ((IDocumentable)dataElement).getDocumentation();
            return StringUtils.isSet(s) ? s : null;
        }
        if("purpose".equals(fieldName) && dataElement instanceof IArchimateModel) { //$NON-NLS-1$
            String s = ((IArchimateModel)dataElement).getPurpose();
            return StringUtils.isSet(s) ? s : null;
        }
        if("relation_source".equals(fieldName) && dataElement instanceof IRelationship) { //$NON-NLS-1$
            IRelationship relation = (IRelationship)dataElement;
            IArchimateElement source = relation.getSource();
            String s = source.getName();
            return StringUtils.isSet(s) ? s : null;
        }
        if("relation_target".equals(fieldName) && dataElement instanceof IRelationship) { //$NON-NLS-1$
            IRelationship relation = (IRelationship)dataElement;
            IArchimateElement target = relation.getTarget();
            String s = target.getName();
            return StringUtils.isSet(s) ? s : null;
        }
        
        return null;
    }
}
