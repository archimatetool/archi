/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.checkers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.osgi.util.NLS;

import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.hammer.validation.issues.ErrorType;
import com.archimatetool.hammer.validation.issues.IIssue;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IJunction;
import com.archimatetool.model.util.ArchimateModelUtils;


/**
 * Checks for non-matching relations to Junctions
 * 
 * @author Phillip Beauvoir
 */
public class JunctionsChecker implements IChecker {
    
    final String NAME = Messages.JunctionsChecker_0;
    final String DESCRIPTION = Messages.JunctionsChecker_1;
    final String EXPLANATION = Messages.JunctionsChecker_2;
    
    private List<IArchimateElement> fArchimateElements;

    public JunctionsChecker(List<IArchimateElement> archimateElements) {
        fArchimateElements = archimateElements;
    }

    @Override
    public List<IIssue> getIssues() {
        return findBogusJunctions();
    }
    
    List<IIssue> findBogusJunctions() {
        List<IIssue> issues = new ArrayList<IIssue>();
        
        for(IArchimateElement element : fArchimateElements) {
            if(element instanceof IJunction) {
                
                IArchimateRelationship rel = null;
                for(IArchimateRelationship relation : ArchimateModelUtils.getAllRelationshipsForConcept(element)) {
                    if(rel != null && rel.eClass() != relation.eClass()) {
                        String name = ArchiLabelProvider.INSTANCE.getLabel(element);
                        String description = NLS.bind(DESCRIPTION, name);
                        String explanation = NLS.bind(EXPLANATION, name);
                        
                        IIssue issue = new ErrorType(NAME, description, explanation, element);
                        issues.add(issue);
                        break;
                    }
                    
                    rel = relation;
                }
            }
        }
        
        return issues;
    }
}
