/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts.connections;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.util.EContentAdapter;

import com.archimatetool.editor.diagram.IArchimateDiagramEditor;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.util.DerivedRelationsUtils;


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
            if(evt.getPropertyName() == IArchimateDiagramEditor.PROPERTY_SHOW_STRUCTURAL_CHAIN) {
                registerStructural();
            }
        }
    };
    
    ///----------------------------------------------------------------------------------------
    ///----------------------------------------------------------------------------------------
    ///----------------------------------------------------------------------------------------
    
    @Override
    public IDiagramModelArchimateConnection getModel() {
        return (IDiagramModelArchimateConnection)super.getModel();
    }

    @Override
    public void activate() {
		if(!isActive()) {
			super.activate();
			// Store this
            fArchimateModel = getModel().getDiagramModel().getArchimateModel();
			// Listen to Archimate Relationship changes
			getModel().getRelationship().eAdapters().add(getECoreAdapter());
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
            getModel().getRelationship().eAdapters().remove(getECoreAdapter());
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
        return Boolean.TRUE.equals(getViewer().getProperty(IArchimateDiagramEditor.PROPERTY_SHOW_STRUCTURAL_CHAIN));
    }
    
    protected void showStructural() {
        IRelationship relation = getModel().getRelationship();
        boolean doHighlight = DerivedRelationsUtils.isInDerivedChain(relation);
        getFigure().highlight(doHighlight);
    }
    
    protected void clearStructural() {
        getFigure().highlight(false);
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
        if(adapter == IDiagramModelArchimateConnection.class) {
            return getModel();
        }
        if(adapter == IArchimateElement.class || adapter == IProperties.class) {
            return getModel().getRelationship();
        }
        return super.getAdapter(adapter);
    }
}
