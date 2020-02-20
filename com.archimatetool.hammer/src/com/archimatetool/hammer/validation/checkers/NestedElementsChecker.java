/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.checkers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.osgi.util.NLS;

import com.archimatetool.hammer.validation.issues.AdviceType;
import com.archimatetool.hammer.validation.issues.IIssue;
import com.archimatetool.model.IAccessRelationship;
import com.archimatetool.model.IAggregationRelationship;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IAssignmentRelationship;
import com.archimatetool.model.ICompositionRelationship;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IJunction;
import com.archimatetool.model.IRealizationRelationship;
import com.archimatetool.model.ISpecializationRelationship;
import com.archimatetool.model.util.ArchimateModelUtils;


/**
 * Checks for problems with nested elements in Views
 * 
 * @author Phillip Beauvoir
 */
public class NestedElementsChecker implements IChecker {
    
    final String fName = Messages.NestedElementsChecker_0;
    final String fDescription = Messages.NestedElementsChecker_1;
    final String fExplanation = Messages.NestedElementsChecker_2 +
                                      Messages.NestedElementsChecker_3;
    
    private List<IArchimateDiagramModel> fViews;
    
    public NestedElementsChecker(List<IArchimateDiagramModel> views) {
        fViews = views;
    }

    @Override
    public List<IIssue> getIssues() {
        return findWrongNestedElements();
    }
    
    // Nested diagram elements without correct relationships
    List<IIssue> findWrongNestedElements() {
        List<IIssue> issues = new ArrayList<IIssue>();
        
        for(IArchimateDiagramModel dm : fViews) {
            for(Iterator<EObject> iter = dm.eAllContents(); iter.hasNext();) {
                EObject eObject = iter.next();
                
                if(eObject instanceof IDiagramModelArchimateObject) {
                    IDiagramModelArchimateObject parent = (IDiagramModelArchimateObject)eObject;
                    
                    for(IDiagramModelObject dmoChild : parent.getChildren()) {
                        if(dmoChild instanceof IDiagramModelArchimateObject) {
                            IDiagramModelArchimateObject child = (IDiagramModelArchimateObject)dmoChild;
                            
                            if(isNestedWithoutValidRelation(parent, child)) {
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

    private boolean isNestedWithoutValidRelation(IDiagramModelArchimateObject parent, IDiagramModelArchimateObject child) {
        IArchimateElement parentElement = parent.getArchimateElement();
        IArchimateElement childElement = child.getArchimateElement();
        
        // Ignore nested Junctions
        if(childElement instanceof IJunction) {
            return false;
        }
        
        // No relationships between parent and child
        if(!hasParentChildRelationships(parentElement, childElement)) {
            return true;
        }
        
        for(IArchimateRelationship r : ArchimateModelUtils.getAllRelationshipsForConcept(parentElement)) {
            // Check for non-nested type relationships
            if((r.getTarget() == childElement || r.getSource() == childElement) && !isNestedTypeRelationship(r)) {
                return true;
            }
            
            // Specialization relationship needs a special check as it goes the other way around
            if(r instanceof ISpecializationRelationship) {
                if(r.getTarget() == childElement) {
                    return true;
                }
            }
            else if(r.getSource() == childElement && isNestedTypeRelationship(r)) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean hasParentChildRelationships(IArchimateElement parentElement, IArchimateElement childElement) {
        for(IArchimateRelationship r : ArchimateModelUtils.getAllRelationshipsForConcept(parentElement)) {
            if(r.getTarget() == childElement || r.getSource() == childElement) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean isNestedTypeRelationship(IArchimateRelationship r) {
        return r instanceof ICompositionRelationship
                || r instanceof ISpecializationRelationship
                || r instanceof IAggregationRelationship
                || r instanceof IAssignmentRelationship
                || r instanceof IRealizationRelationship
                || r instanceof IAccessRelationship;
    }
}
