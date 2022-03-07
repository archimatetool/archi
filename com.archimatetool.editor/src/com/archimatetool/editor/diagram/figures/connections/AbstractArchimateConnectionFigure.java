/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.connections;

import org.eclipse.draw2d.IFigure;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.figures.ToolTipFigure;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IConnectable;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IJunction;
import com.archimatetool.model.viewpoints.ViewpointManager;



/**
 * Abstract implementation of an Archimate connection figure.  Subclasses should decide how to draw the line
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractArchimateConnectionFigure
extends AbstractDiagramConnectionFigure implements IArchimateConnectionFigure {
    
    @Override
    public void refreshVisuals() {
        super.refreshVisuals();
        
        // Set Enabled according to current Viewpoint
        boolean enabled = true;

        // Set Enabled according to current Viewpoint
        if(ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.VIEWPOINTS_GHOST_DIAGRAM_ELEMENTS)) {
            enabled = ViewpointManager.INSTANCE.isAllowedDiagramModelComponent(getModelConnection());
        }
        
        setEnabled(enabled);
        
        if(getSourceDecoration() != null) {
            getSourceDecoration().setEnabled(enabled);
        }
        
        if(getTargetDecoration() != null) {
            getTargetDecoration().setEnabled(enabled);
        }
        
        getConnectionLabel().setEnabled(enabled);
        
        repaint(); // repaint when figure changes
    }

    @Override
    public IFigure getToolTip() {
        ToolTipFigure toolTipFigure = (ToolTipFigure)super.getToolTip();
        
        if(toolTipFigure == null) {
            return null;
        }
        
        IArchimateRelationship relation = getDiagramModelArchimateConnection().getArchimateRelationship();
        
        String text = ArchiLabelProvider.INSTANCE.getLabel(relation);
        toolTipFigure.setText(text);

        String type = ArchiLabelProvider.INSTANCE.getDefaultName(relation.eClass());
        toolTipFigure.setType(Messages.AbstractArchimateConnectionFigure_0 + " " + type); //$NON-NLS-1$

        String rubric = ArchiLabelProvider.INSTANCE.getRelationshipSentence(relation);
        toolTipFigure.setRubric(rubric);

        return toolTipFigure;
    }
    
    /**
     * @return true if the option is set to hide incoming target connection arrows on a Junction
     */
    protected boolean usePlainJunctionTargetDecoration() {
        IConnectable target = getModelConnection().getTarget();
        
        if(target instanceof IDiagramModelArchimateObject && ((IDiagramModelArchimateObject)target).getArchimateElement() instanceof IJunction) {
            return target
                   .getFeatures()
                   .getBoolean(IDiagramModelArchimateObject.FEATURE_HIDE_JUNCTION_ARROWS,
                           IDiagramModelArchimateObject.FEATURE_HIDE_JUNCTION_ARROWS_DEFAULT);

        }
        
        return false;
    }
    
    /**
     * @return true if the option is set to hide outgoing source connection ends on a Junction
     */
    protected boolean usePlainJunctionSourceDecoration() {
        IConnectable source = getModelConnection().getSource();
        
        if(source instanceof IDiagramModelArchimateObject && ((IDiagramModelArchimateObject)source).getArchimateElement() instanceof IJunction) {
            return source
                   .getFeatures()
                   .getBoolean(IDiagramModelArchimateObject.FEATURE_HIDE_JUNCTION_ARROWS,
                           IDiagramModelArchimateObject.FEATURE_HIDE_JUNCTION_ARROWS_DEFAULT);

        }
        
        return false;
    }

}
