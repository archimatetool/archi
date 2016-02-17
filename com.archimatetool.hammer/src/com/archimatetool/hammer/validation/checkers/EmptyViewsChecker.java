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
import com.archimatetool.hammer.validation.issues.AdviceType;
import com.archimatetool.hammer.validation.issues.IIssue;
import com.archimatetool.model.IArchimateDiagramModel;


/**
 * Checks for empty Views in a model
 * 
 * @author Phillip Beauvoir
 */
public class EmptyViewsChecker extends AbstractChecker {
    
    final String fName = Messages.EmptyViewsChecker_0;
    final String fDescription = Messages.EmptyViewsChecker_1;
    final String fExplanation = Messages.EmptyViewsChecker_2 +
                               Messages.EmptyViewsChecker_3;
    
    public EmptyViewsChecker(Validator validator) {
        super(validator);
    }

    public List<IIssue> getIssues() {
        return findEmptyViews();
    }
    
    // Empty Views
    List<IIssue> findEmptyViews() {
        List<IIssue> issues = new ArrayList<IIssue>();
        
        for(IArchimateDiagramModel view : archimateViews) {
            if(view.getChildren().isEmpty()) {
                String viewName = ArchimateLabelProvider.INSTANCE.getLabel(view);
                String description = NLS.bind(fDescription, viewName);
                String explanation = NLS.bind(fExplanation, viewName);
                
                IIssue issue = new AdviceType(fName, description, explanation, view);
                issues.add(issue);
            }
        }
        
        return issues;
    }
}
