/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.editparts.connections;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;

import uk.ac.bolton.archimate.editor.diagram.IDiagramEditor;
import uk.ac.bolton.archimate.editor.diagram.figures.connections.IDiagramConnectionFigure;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;
import uk.ac.bolton.archimate.model.IRelationship;
import uk.ac.bolton.archimate.model.util.DerivedRelationsUtils;

/**
 * Abstract class for all implementations of Archimate Connection parts
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractArchimateConnectionEditPart extends AbstractDiagramConnectionEditPart 
implements IArchimateConnectionEditPart {
    
    private IArchimateModel fArchimateModel;
    
    /**
     * Listen to all model changes to refresh Structural color.
     */
    private Adapter fModelAdapter = new EContentAdapter() {
        @Override
        public void notifyChanged(Notification msg) {
            super.notifyChanged(msg);

            Object feature = msg.getFeature();
            if(feature == IArchimatePackage.Literals.RELATIONSHIP__SOURCE
                                                    || feature == IArchimatePackage.Literals.RELATIONSHIP__TARGET
                                                    || feature == IArchimatePackage.Literals.FOLDER__ELEMENTS) {
                showStructural();
            }
        }
    };
    
    /**
     * Listen to user toggling on/off show structural chains
     */
    private PropertyChangeListener propertyListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if(evt.getPropertyName() == IDiagramEditor.PROPERTY_SHOW_STRUCTURAL_CHAIN) {
                registerStructural();
            }
        }
    };
    
    ///----------------------------------------------------------------------------------------
    ///----------------------------------------------------------------------------------------
    ///----------------------------------------------------------------------------------------

    @Override
    public void activate() {
		if(!isActive()) {
			super.activate();
			// Store this
            fArchimateModel = ((IDiagramModelArchimateConnection)getModel()).getDiagramModel().getArchimateModel();
			// Listen to Archimate Relationship changes
			((IDiagramModelArchimateConnection)getModel()).getRelationship().eAdapters().add(fConnectionAdapter);
			// Register to listen to overall model changes that affect the structural relationship chains
			if(isShowStructural()) {
	            fArchimateModel.eAdapters().add(fModelAdapter);
	        }
			// Listen to Viewer Property changes for "Show Structural Chains"
			getViewer().addPropertyChangeListener(propertyListener);
		}
	}
	
    @Override
    public void deactivate() {
        if(isActive()) {
            super.deactivate();
            ((IDiagramModelArchimateConnection)getModel()).getRelationship().eAdapters().remove(fConnectionAdapter);
            fArchimateModel.eAdapters().remove(fModelAdapter);
            getViewer().removePropertyChangeListener(propertyListener);
        }
    }
    
    @Override
    protected void refreshVisuals() {
        super.refreshVisuals();
        if(isShowStructural()) {
            showStructural();
        }
    }
    
    /**
     * Register Model Listener to update Structural Chains
     */
    protected void registerStructural() {
        if(isShowStructural()) {
            fArchimateModel.eAdapters().add(fModelAdapter);
            showStructural();
        }
        else {
            fArchimateModel.eAdapters().remove(fModelAdapter);
            clearStructural();
        }
    }
    
    protected boolean isShowStructural() {
        return Boolean.TRUE.equals(getViewer().getProperty(IDiagramEditor.PROPERTY_SHOW_STRUCTURAL_CHAIN));
    }
    
    protected void showStructural() {
        IRelationship relation = ((IDiagramModelArchimateConnection)getModel()).getRelationship();
        boolean doHighlight = DerivedRelationsUtils.isInDerivedChain(relation);
        ((IDiagramConnectionFigure)getFigure()).highlight(doHighlight);
    }
    
    protected void clearStructural() {
        ((IDiagramConnectionFigure)getFigure()).highlight(false);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
        if(adapter == IDiagramModelArchimateConnection.class) {
            return getModel();
        }
        if(adapter == IArchimateElement.class) {
            return ((IDiagramModelArchimateConnection)getModel()).getRelationship();
        }
        return super.getAdapter(adapter);
    }
}
