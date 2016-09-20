/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.jasperreports.data;

import org.eclipse.emf.ecore.EObject;

import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDocumentable;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.INameable;



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
        
        if("id".equals(fieldName) && dataElement instanceof IIdentifier) { //$NON-NLS-1$
            return ((IIdentifier)dataElement).getId();
        }
        
        if("name".equals(fieldName) && dataElement instanceof INameable) { //$NON-NLS-1$
            String name = ((INameable)dataElement).getName();
            if(name == null || "".equals(name)) { //$NON-NLS-1$
                name = ArchiLabelProvider.INSTANCE.getDefaultName(((EObject)dataElement).eClass());
            }
            return name;
        }
        
        if("type".equals(fieldName) && dataElement instanceof EObject) { //$NON-NLS-1$
            return ArchiLabelProvider.INSTANCE.getDefaultName(((EObject)dataElement).eClass());
        }
        
        if("documentation".equals(fieldName) && dataElement instanceof IDocumentable) { //$NON-NLS-1$
            String s = ((IDocumentable)dataElement).getDocumentation();
            return StringUtils.isSet(s) ? s : null;
        }
        
        if("purpose".equals(fieldName) && dataElement instanceof IArchimateModel) { //$NON-NLS-1$
            String s = ((IArchimateModel)dataElement).getPurpose();
            return StringUtils.isSet(s) ? s : null;
        }
        
        if("relation_source".equals(fieldName) && dataElement instanceof IArchimateRelationship) { //$NON-NLS-1$
            IArchimateRelationship relation = (IArchimateRelationship)dataElement;
            IArchimateConcept source = relation.getSource();
            String s = source.getName();
            return StringUtils.isSet(s) ? s : null;
        }
        
        if("relation_target".equals(fieldName) && dataElement instanceof IArchimateRelationship) { //$NON-NLS-1$
            IArchimateRelationship relation = (IArchimateRelationship)dataElement;
            IArchimateConcept target = relation.getTarget();
            String s = target.getName();
            return StringUtils.isSet(s) ? s : null;
        }
        
        return null;
    }
}
