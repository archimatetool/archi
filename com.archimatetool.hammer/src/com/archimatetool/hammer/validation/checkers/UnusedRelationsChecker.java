/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.checkers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.osgi.util.NLS;

import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.hammer.validation.issues.IIssue;
import com.archimatetool.hammer.validation.issues.WarningType;
import com.archimatetool.model.IArchimateRelationship;


/**
 * Checks for unused relations in a model
 * 
 * @author Phillip Beauvoir
 */
public class UnusedRelationsChecker implements IChecker {
    
    final String NAME = Messages.UnusedRelationsChecker_0;
    final String DESCRIPTION = Messages.UnusedRelationsChecker_1;
    final String EXPLANATION = Messages.UnusedRelationsChecker_2 +
                               Messages.UnusedRelationsChecker_3;

    private List<IArchimateRelationship> fRelations;

    public UnusedRelationsChecker(List<IArchimateRelationship> relations) {
        fRelations = relations;
    }

    @Override
    public List<IIssue> getIssues() {
        return findUnusedRelations();
    }
    
    List<IIssue> findUnusedRelations() {
        List<IIssue> issues = new ArrayList<IIssue>();
        
        for(IArchimateRelationship relation : fRelations) {
            if(!DiagramModelUtils.isArchimateConceptReferencedInDiagrams(relation)) {
                String name = ArchiLabelProvider.INSTANCE.getLabel(relation);
                String description = NLS.bind(DESCRIPTION, name);
                String explanation = NLS.bind(EXPLANATION, name);
                
                IIssue issue = new WarningType(NAME, description, explanation, relation);
                issues.add(issue);
            }
        }
        
        return issues;
    }
}
