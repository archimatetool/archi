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
import com.archimatetool.editor.ui.ArchimateLabelProvider;
import com.archimatetool.hammer.validation.Validator;
import com.archimatetool.hammer.validation.issues.IIssue;
import com.archimatetool.hammer.validation.issues.WarningType;
import com.archimatetool.model.IArchimateElement;


/**
 * Checks for unused elements in a model
 * 
 * @author Phillip Beauvoir
 */
public class UnusedElementsChecker extends AbstractChecker {
    
    final String NAME = Messages.UnusedElementsChecker_0;
    final String DESCRIPTION = Messages.UnusedElementsChecker_1;
    final String EXPLANATION = Messages.UnusedElementsChecker_2 +
                               Messages.UnusedElementsChecker_3;

    public UnusedElementsChecker(Validator validator) {
        super(validator);
    }

    public List<IIssue> getIssues() {
        return findUnusedElements();
    }
    
    List<IIssue> findUnusedElements() {
        List<IIssue> issues = new ArrayList<IIssue>();
        
        for(IArchimateElement element : archimateElements) {
            if(!DiagramModelUtils.isArchimateComponentReferencedInDiagrams(element)) {
                String name = ArchimateLabelProvider.INSTANCE.getLabel(element);
                String description = NLS.bind(DESCRIPTION, name);
                String explanation = NLS.bind(EXPLANATION, name);
                
                IIssue issue = new WarningType(NAME, description, explanation, element);
                issues.add(issue);
            }
        }
        
        return issues;
    }
}
