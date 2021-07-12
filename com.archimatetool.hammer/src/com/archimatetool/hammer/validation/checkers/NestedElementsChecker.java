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
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelConnection;
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
        
        // Iterate through all Views
        for(IArchimateDiagramModel dm : fViews) {
            for(Iterator<EObject> iter = dm.eAllContents(); iter.hasNext();) {
                EObject eObject = iter.next();
                
                // If this is an Archimate diagram object...
                if(eObject instanceof IDiagramModelArchimateObject) {
                    IDiagramModelArchimateObject parent = (IDiagramModelArchimateObject)eObject;
                    
                    // Get its children
                    for(IDiagramModelObject dmoChild : parent.getChildren()) {
                        // If the child is an Archimate diagram object...
                        if(dmoChild instanceof IDiagramModelArchimateObject) {
                            IDiagramModelArchimateObject child = (IDiagramModelArchimateObject)dmoChild;
                            
                            // Check nested state
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
        
        // Get all diagram connections between parent and child objects
        List<IDiagramModelConnection> connections = new ArrayList<>(parent.getSourceConnections());
        connections.addAll(parent.getTargetConnections());
        
        for(IDiagramModelConnection connection : connections) {
            // If this is an ArchiMate connection...
            if(connection instanceof IDiagramModelArchimateConnection && (connection.getSource() == child || connection.getTarget() == child)) {
                // Get its relationship
                IArchimateRelationship relation = ((IDiagramModelArchimateConnection)connection).getArchimateRelationship();
                
                // Check for non-nested type relationships
                if(!isNestedTypeRelationship(relation)) {
                    return true;
                }

                // Specialization relationship needs a special check as it goes the other way around
                if(relation instanceof ISpecializationRelationship) {
                    if(relation.getTarget() == childElement) {
                        return true;
                    }
                }
                // Else reversed nested
                else if(relation.getSource() == childElement && isNestedTypeRelationship(relation)) {
                    return true;
                }
            }
        }
        
        // Check for any nested type relationships in the model, return false if one is found
        for(IArchimateRelationship relation : ArchimateModelUtils.getAllRelationshipsForConcept(parentElement)) {
            if((relation.getTarget() == childElement || relation.getSource() == childElement) && isNestedTypeRelationship(relation)) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * @return true if relation is a nested type relationship as defined in the ArchiMate spec
     */
    private boolean isNestedTypeRelationship(IArchimateRelationship relation) {
        return relation instanceof ICompositionRelationship
                || relation instanceof ISpecializationRelationship
                || relation instanceof IAggregationRelationship
                || relation instanceof IAssignmentRelationship
                || relation instanceof IRealizationRelationship
                || relation instanceof IAccessRelationship;
    }
}
