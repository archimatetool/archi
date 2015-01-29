/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.checkers;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.osgi.util.NLS;

import com.archimatetool.editor.model.viewpoints.IViewpoint;
import com.archimatetool.editor.model.viewpoints.ViewpointsManager;
import com.archimatetool.editor.ui.ArchimateLabelProvider;
import com.archimatetool.hammer.validation.Validator;
import com.archimatetool.hammer.validation.issues.IIssue;
import com.archimatetool.hammer.validation.issues.WarningType;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IRelationship;


/**
 * Checks for problems in a Viewpoint
 * 
 * @author Phillip Beauvoir
 */
public class ViewpointChecker extends AbstractChecker {
    
    final String fName = Messages.ViewpointChecker_0;
    final String fDescription = Messages.ViewpointChecker_1;
    final String fExplanation = Messages.ViewpointChecker_2 +
                                Messages.ViewpointChecker_3;

    public ViewpointChecker(Validator validator) {
        super(validator);
    }

    public List<IIssue> getIssues() {
        return findComponentsInWrongViewpoints();
    }

    List<IIssue> findComponentsInWrongViewpoints() {
        List<IIssue> issues = new ArrayList<IIssue>();
        
        for(IArchimateDiagramModel dm : archimateViews) {
            int index = dm.getViewpoint();
            IViewpoint viewPoint = ViewpointsManager.INSTANCE.getViewpoint(index);
            issues.addAll(findComponentsInWrongViewpoints(dm, viewPoint));
        }
        
        return issues;
    }
    
    List<IIssue> findComponentsInWrongViewpoints(IDiagramModelContainer container, IViewpoint viewPoint) {
        List<IIssue> issues = new ArrayList<IIssue>();
    
        for(IDiagramModelObject dmo : container.getChildren()) {
            if(dmo instanceof IDiagramModelArchimateObject) {
                IArchimateElement element = ((IDiagramModelArchimateObject)dmo).getArchimateElement();
                if(!viewPoint.isAllowedType(element.eClass())) {
                    IIssue issue = createIssue(dmo, container.getDiagramModel().getName(), viewPoint.getName());
                    issues.add(issue);
                }
            }
            
            for(IDiagramModelConnection conn : dmo.getSourceConnections()) {
                if(conn instanceof IDiagramModelArchimateConnection) {
                    IRelationship relation = ((IDiagramModelArchimateConnection)conn).getRelationship();
                    if(!viewPoint.isAllowedType(relation.eClass())) {
                        IIssue issue = createIssue(conn, container.getDiagramModel().getName(), viewPoint.getName());
                        issues.add(issue);
                    }
                }
            }
            
            if(dmo instanceof IDiagramModelContainer) {
                issues.addAll(findComponentsInWrongViewpoints((IDiagramModelContainer)dmo, viewPoint));
            }
        }
        
        return issues;
    }

    IIssue createIssue(Object object, String viewName, String viewpointName) {
        String description =  NLS.bind(fDescription, new Object[] {
                ArchimateLabelProvider.INSTANCE.getLabel(object),
                viewName,
                viewpointName
        });
        
        String type, concept;
        if(object instanceof IDiagramModelArchimateObject) {
            concept = ArchimateLabelProvider.INSTANCE.getDefaultName(((IDiagramModelArchimateObject)object).getArchimateElement().eClass());
            type = Messages.ViewpointChecker_4;
        }
        else {
            concept = ArchimateLabelProvider.INSTANCE.getDefaultName(((IDiagramModelArchimateConnection)object).getRelationship().eClass());
            type = Messages.ViewpointChecker_5;
        }
        
        String explanation =  NLS.bind(fExplanation, new Object[] {
                concept,
                viewpointName,
                type
        });
        
        return new WarningType(fName, description, explanation, object);
    }
}
