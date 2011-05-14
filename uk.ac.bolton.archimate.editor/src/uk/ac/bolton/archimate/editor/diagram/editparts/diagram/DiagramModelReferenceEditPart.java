/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.editparts.diagram;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

import uk.ac.bolton.archimate.editor.diagram.editparts.AbstractBaseEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.IColoredEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.ITextAlignedEditPart;
import uk.ac.bolton.archimate.editor.diagram.figures.IDiagramModelObjectFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.diagram.DiagramModelReferenceFigure;
import uk.ac.bolton.archimate.editor.diagram.policies.PartComponentEditPolicy;
import uk.ac.bolton.archimate.editor.ui.services.EditorManager;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModelReference;


/**
 * Diagram Model Reference Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class DiagramModelReferenceEditPart extends AbstractBaseEditPart
implements IColoredEditPart, ITextAlignedEditPart {

    private Adapter adapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            switch(msg.getEventType()) {
                case Notification.SET:
                    Object feature = msg.getFeature();
                    if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__BOUNDS) {
                        refreshBounds();
                    }
                    else {
                        refreshFigure();
                    }
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected Adapter getECoreAdapter() {
        return adapter;
    }
    
    @Override
    protected void addECoreAdapter() {
        super.addECoreAdapter();
        
        // Listen to referenced model
        IDiagramModel ref = ((IDiagramModelReference)getModel()).getReferencedModel();
        if(ref != null) {
            ref.eAdapters().add(getECoreAdapter());
        }
    }
    
    @Override
    protected void removeECoreAdapter() {
        super.removeECoreAdapter();
        
        // Unlisten to referenced model
        IDiagramModel ref = ((IDiagramModelReference)getModel()).getReferencedModel();
        if(ref != null) {
            ref.eAdapters().remove(getECoreAdapter());
        }
    }

    @Override
    protected void createEditPolicies() {
        // Add a policy to handle editing the Parts (for example, deleting a part)
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new PartComponentEditPolicy());
    }

    @Override
    protected IFigure createFigure() {
        DiagramModelReferenceFigure figure = new DiagramModelReferenceFigure((IDiagramModelReference)getModel());
        return figure;
    }

    @Override
    protected void refreshFigure() {
        ((IDiagramModelObjectFigure)figure).refreshVisuals();
    }

    @Override
    public void performRequest(Request req) {
        if(req.getType() == RequestConstants.REQ_OPEN) {
            // Open Diagram
            EditorManager.openDiagramEditor(((IDiagramModelReference)getModel()).getReferencedModel());
        }
    }
}
