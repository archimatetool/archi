/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.checkers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.osgi.util.NLS;

import com.archimatetool.editor.ui.ArchimateLabelProvider;
import com.archimatetool.hammer.validation.Validator;
import com.archimatetool.hammer.validation.issues.ErrorType;
import com.archimatetool.hammer.validation.issues.IIssue;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.util.ArchimateModelUtils;


/**
 * Checks for problems with Invalid Relations
 * 
 * @author Phillip Beauvoir
 */
public class InvalidRelationsChecker extends AbstractChecker {
    
    final String fName = Messages.InvalidRelationsChecker_0;
    final String fDescription = Messages.InvalidRelationsChecker_1;
    final String fExplanation = Messages.InvalidRelationsChecker_2 +
                                      Messages.InvalidRelationsChecker_3;

    public InvalidRelationsChecker(Validator validator) {
        super(validator);
    }

    public List<IIssue> getIssues() {
        return findInvalidRelations();
    }
    
    // Invalid Relations
    List<IIssue> findInvalidRelations() {
        List<IIssue> issues = new ArrayList<IIssue>();
        
        for(IArchimateRelationship relation : archimateRelations) {
            boolean valid = ArchimateModelUtils.isValidRelationship(relation.getSource(), relation.getTarget(), relation.eClass());
            if(!valid) {
                String className = ArchimateLabelProvider.INSTANCE.getDefaultName(relation.eClass());
                
                String description = NLS.bind(fDescription, new Object[] {
                        className,
                        ArchimateLabelProvider.INSTANCE.getLabel(relation.getSource()),
                        ArchimateLabelProvider.INSTANCE.getLabel(relation.getTarget())
                });
                
                String explanation = NLS.bind(fExplanation, new Object[] {
                        className,
                        ArchimateLabelProvider.INSTANCE.getDefaultName(relation.getSource().eClass()),
                        ArchimateLabelProvider.INSTANCE.getDefaultName(relation.getTarget().eClass())
                });
                
                IIssue issue = new ErrorType(fName, description, explanation, relation);
                issues.add(issue);
            }
        }
        
        return issues;
    }
}
