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
import com.archimatetool.model.IArchimateComponent;
import com.archimatetool.model.IRelationship;


/**
 * Checks for unused components in a model
 * 
 * @author Phillip Beauvoir
 */
public class UnusedComponentsChecker extends AbstractChecker {
    
    final String fName = Messages.UnusedComponentsChecker_0;
    final String fDescription = Messages.UnusedComponentsChecker_1;
    final String fExplanation = Messages.UnusedComponentsChecker_2 +
                               Messages.UnusedComponentsChecker_3;

    public UnusedComponentsChecker(Validator validator) {
        super(validator);
    }

    public List<IIssue> getIssues() {
        List<IIssue> issues = new ArrayList<IIssue>();
        
        issues.addAll(findUnusedComponents(archimateElements));
        issues.addAll(findUnusedComponents(archimateRelations));
        
        return issues;
    }
    
    // Unused components
    List<IIssue> findUnusedComponents(List<? extends IArchimateComponent> archimateComponents) {
        List<IIssue> issues = new ArrayList<IIssue>();
        
        for(IArchimateComponent archimateComponent : archimateComponents) {
            if(!DiagramModelUtils.isArchimateComponentReferencedInDiagrams(archimateComponent)) {
                String type = (archimateComponent instanceof IRelationship) ? Messages.UnusedComponentsChecker_4 : Messages.UnusedComponentsChecker_5;
                String name = NLS.bind(fName, type);
                String description = NLS.bind(fDescription, ArchimateLabelProvider.INSTANCE.getLabel(archimateComponent));
                String explanation = NLS.bind(fExplanation, new Object[] {
                        type,
                        ArchimateLabelProvider.INSTANCE.getLabel(archimateComponent)
                });
                
                IIssue issue = new WarningType(name, description, explanation, archimateComponent);
                issues.add(issue);
            }
        }
        
        return issues;
    }
}
