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

import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.hammer.validation.issues.IIssue;
import com.archimatetool.hammer.validation.issues.WarningType;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.viewpoints.IViewpoint;
import com.archimatetool.model.viewpoints.ViewpointManager;


/**
 * Checks for problems in a Viewpoint
 * 
 * @author Phillip Beauvoir
 */
public class ViewpointChecker implements IChecker {
    
    final String fName = Messages.ViewpointChecker_0;
    final String fDescription = Messages.ViewpointChecker_1;
    final String fExplanation = Messages.ViewpointChecker_2 +
                                Messages.ViewpointChecker_3;

    private List<IArchimateDiagramModel> fViews;
    
    public ViewpointChecker(List<IArchimateDiagramModel> views) {
        fViews = views;
    }

    @Override
    public List<IIssue> getIssues() {
        return findComponentsInWrongViewpoints();
    }

    List<IIssue> findComponentsInWrongViewpoints() {
        List<IIssue> issues = new ArrayList<IIssue>();
        
        for(IArchimateDiagramModel dm : fViews) {
            String id = dm.getViewpoint();
            IViewpoint viewPoint = ViewpointManager.INSTANCE.getViewpoint(id);
            
            for(Iterator<EObject> iter = dm.eAllContents(); iter.hasNext();) {
                EObject eObject = iter.next();
                
                if(eObject instanceof IDiagramModelArchimateObject) {
                    IDiagramModelArchimateObject dmo = (IDiagramModelArchimateObject)eObject;
                    IArchimateElement element = dmo.getArchimateElement();
                    if(!viewPoint.isAllowedConcept(element.eClass())) {
                        IIssue issue = createIssue(dmo, dm.getName(), viewPoint.getName());
                        issues.add(issue);
                    }
                }
            }
        }
        
        return issues;
    }
    
    IIssue createIssue(Object object, String viewName, String viewpointName) {
        String description =  NLS.bind(fDescription, new Object[] {
                ArchiLabelProvider.INSTANCE.getLabel(object),
                viewName,
                viewpointName
        });
        
        String type, concept;
        if(object instanceof IDiagramModelArchimateObject) {
            concept = ArchiLabelProvider.INSTANCE.getDefaultName(((IDiagramModelArchimateObject)object).getArchimateElement().eClass());
            type = Messages.ViewpointChecker_4;
        }
        else {
            concept = ArchiLabelProvider.INSTANCE.getDefaultName(((IDiagramModelArchimateConnection)object).getArchimateRelationship().eClass());
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
