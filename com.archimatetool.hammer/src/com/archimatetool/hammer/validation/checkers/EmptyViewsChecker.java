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
import com.archimatetool.hammer.validation.issues.AdviceType;
import com.archimatetool.hammer.validation.issues.IIssue;
import com.archimatetool.model.IArchimateDiagramModel;


/**
 * Checks for empty Views in a model
 * 
 * @author Phillip Beauvoir
 */
public class EmptyViewsChecker implements IChecker {
    
    final String fName = Messages.EmptyViewsChecker_0;
    final String fDescription = Messages.EmptyViewsChecker_1;
    final String fExplanation = Messages.EmptyViewsChecker_2 +
                               Messages.EmptyViewsChecker_3;
    
    private List<IArchimateDiagramModel> fViews;
    
    public EmptyViewsChecker(List<IArchimateDiagramModel> views) {
        fViews = views;
    }

    @Override
    public List<IIssue> getIssues() {
        return findEmptyViews();
    }
    
    // Empty Views
    List<IIssue> findEmptyViews() {
        List<IIssue> issues = new ArrayList<IIssue>();
        
        for(IArchimateDiagramModel view : fViews) {
            if(view.getChildren().isEmpty()) {
                String viewName = ArchiLabelProvider.INSTANCE.getLabel(view);
                String description = NLS.bind(fDescription, viewName);
                String explanation = NLS.bind(fExplanation, viewName);
                
                IIssue issue = new AdviceType(fName, description, explanation, view);
                issues.add(issue);
            }
        }
        
        return issues;
    }
}
