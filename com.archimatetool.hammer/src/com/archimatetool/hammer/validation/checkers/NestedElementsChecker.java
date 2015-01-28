/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.checkers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.osgi.util.NLS;

import com.archimatetool.hammer.validation.Validator;
import com.archimatetool.hammer.validation.issues.AdviceType;
import com.archimatetool.hammer.validation.issues.IIssue;
import com.archimatetool.model.IAggregationRelationship;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IAssignmentRelationship;
import com.archimatetool.model.ICompositionRelationship;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.util.ArchimateModelUtils;


/**
 * Checks for problems with nested elements in Views
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class NestedElementsChecker extends AbstractChecker {
    
    final String fName = "Visual Nesting";
    final String fDescription = "''{0}'' should not be nested in ''{1}'' unless there is a valid relationship";
    final String fExplanation = "<p>An element in a View is nested inside of another element. " +
                                      "However, this is only visual as the two elements do not have a semantic relationship in the model.</p>" +
                                      "<p>The relationship between the elements should be either Composition, Aggregation, or Assignment.</p>";

    public NestedElementsChecker(Validator validator) {
        super(validator);
    }

    public List<IIssue> getIssues() {
        return findWrongNestedElements();
    }
    
    // Nested diagram elements without correct relationships
    List<IIssue> findWrongNestedElements() {
        List<IIssue> issues = new ArrayList<IIssue>();
        
        for(IArchimateDiagramModel dm : archimateViews) {
            for(IDiagramModelObject dmo : dm.getChildren()) {
                if(dmo instanceof IDiagramModelArchimateObject) {
                    IDiagramModelArchimateObject parent = (IDiagramModelArchimateObject)dmo;
                    for(IDiagramModelObject dmoChild : parent.getChildren()) {
                        if(dmoChild instanceof IDiagramModelArchimateObject) {
                            IDiagramModelArchimateObject child = (IDiagramModelArchimateObject)dmoChild;
                            
                            if(!hasValidNestedRelation(parent, child)) {
                                String description =  NLS.bind(fDescription, new Object[] {
                                        child.getName(),
                                        parent.getName()
                                });
                                
                                IIssue issue = new AdviceType(fName, description, fExplanation, child);
                                issues.add(issue);
                            }
                        }
                    }
                }
            }
        }
        
        return issues;
    }

    boolean hasValidNestedRelation(IDiagramModelArchimateObject parent, IDiagramModelArchimateObject child) {
        for(IRelationship relation : ArchimateModelUtils.getSourceRelationships(parent.getArchimateElement())) {
            if(relation.getTarget() == child.getArchimateElement()) {
                if(relation instanceof ICompositionRelationship || relation instanceof IAggregationRelationship
                        || relation instanceof IAssignmentRelationship) {
                    return true;
                }
            }
        }
        
        return false;
    }
}
