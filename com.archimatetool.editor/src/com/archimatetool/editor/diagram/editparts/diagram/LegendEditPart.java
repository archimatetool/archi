/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts.diagram;

import java.util.Iterator;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.jface.util.PropertyChangeEvent;

import com.archimatetool.editor.diagram.editparts.AbstractConnectedEditPart;
import com.archimatetool.editor.diagram.figures.diagram.LegendFigure;
import com.archimatetool.editor.diagram.policies.ArchimateDiagramConnectionPolicy;
import com.archimatetool.editor.diagram.policies.PartComponentEditPolicy;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.IFeatures;
import com.archimatetool.model.util.IModelContentListener;



/**
 * Legend Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class LegendEditPart extends AbstractConnectedEditPart {
    
    private IArchimateModel model;
    
    private final IModelContentListener modelListener = this::modelChanged;
    
    @Override
    protected void createEditPolicies() {
        // Allow parts to be connected
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ArchimateDiagramConnectionPolicy());
        
        // Add a policy to handle editing the Parts (for example, deleting a part)
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new PartComponentEditPolicy());
    }

    @Override
    protected IFigure createFigure() {
        return new LegendFigure(getModel());
    }
    
    @Override
    public IDiagramModelNote getModel() {
        return (IDiagramModelNote)super.getModel();
    }
    
    @Override
    public LegendFigure getFigure() {
        return (LegendFigure)super.getFigure();
    }
    
    @Override
    protected void addECoreAdapter() {
        super.addECoreAdapter();
        
        // Listen to changes in model
        model = getModel().getArchimateModel();
        model.addModelContentListener(modelListener);
    }
    
    @Override
    protected void removeECoreAdapter() {
        super.removeECoreAdapter();
        
        // Remove model adapter
        if(model != null) {
            model.removeModelContentListener(modelListener);
            model = null;
        }
    }
    
    @Override
    protected void eCoreChanged(Notification msg) {
        // Legend state notification
        if(IFeatures.isFeatureNotification(msg, IDiagramModelNote.FEATURE_LEGEND)) {
            getFigure().updateLegend();
            return;
        }
        
        super.eCoreChanged(msg);
    }
    
    /**
     * Model notification handler. Keep this separate from the eCoreChanged handler.
     */
    protected void modelChanged(Notification msg) {
        if(isModelChange(msg)) {
            getFigure().updateLegend();
        }
    }
    
    /**
     * The legend needs to be notified of other model object changes:
     * 
     * - When an ArchiMate object or connection is added/removed on the same diagram
     * - When an ArchiMate concept's IProfile (Specialization) changes
     * 
     * @return true if the model change is one we're interested in 
     */
    protected boolean isModelChange(Notification msg) {
        Object feature = msg.getFeature();
        Object notifier = msg.getNotifier();
        IDiagramModel dm = getModel().getDiagramModel();
        
        // Notifier is parent diagram or diagram component and ArchiMate component added/removed
        if((notifier == dm || (notifier instanceof IDiagramModelComponent dmc && dmc.getDiagramModel() == dm))
                && (msg.getEventType() == Notification.ADD || msg.getEventType() == Notification.REMOVE)
                && (msg.getOldValue() instanceof IDiagramModelArchimateComponent || msg.getNewValue() instanceof IDiagramModelArchimateComponent)) {
            return true;
        }
        
        // Profile (Specialization) change
        if(feature == IArchimatePackage.Literals.PROFILES__PROFILES) {
            // Determine if the affected object is on the diagram
            for(Iterator<EObject> iter = dm.eAllContents(); iter.hasNext();) {
                if(iter.next() instanceof IDiagramModelArchimateComponent dmc && dmc.getArchimateConcept() == notifier) {
                    return true;
                }
            }
        }
        
        return false;
    }

    @Override
    protected void applicationPreferencesChanged(PropertyChangeEvent event) {
        // Legend user label or fill color need an update
        if(event.getProperty().startsWith(IPreferenceConstants.LEGEND_LABEL_PREFIX)
                           || event.getProperty().startsWith(IPreferenceConstants.DEFAULT_FILL_COLOR_PREFIX)) {
            getFigure().updateLegend();
        }
        else {
            super.applicationPreferencesChanged(event);
        }
    }
    
    @Override
    public void performRequest(Request req) {
        // Show Properties view
        if(req.getType() == RequestConstants.REQ_OPEN) {
            showPropertiesView();
        }
    }
}
