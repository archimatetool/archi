/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.connections;

import org.eclipse.draw2d.IFigure;

import com.archimatetool.editor.diagram.figures.ToolTipFigure;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.viewpoints.ViewpointManager;



/**
 * Abstract implementation of an Archimate connection figure.  Subclasses should decide how to draw the line
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractArchimateConnectionFigure
extends AbstractDiagramConnectionFigure {
    
    @Override
    public IDiagramModelArchimateConnection getModelConnection() {
        return (IDiagramModelArchimateConnection)super.getModelConnection();
    }
    
    @Override
    public void refreshVisuals() {
        super.refreshVisuals();
        
        // Set Enabled according to current Viewpoint
        boolean enabled = true;

        // Set Enabled according to current Viewpoint
        if(Preferences.STORE.getBoolean(IPreferenceConstants.VIEWPOINTS_GHOST_DIAGRAM_ELEMENTS)) {
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
        
        IArchimateRelationship relation = getModelConnection().getArchimateRelationship();
        
        String text = ArchiLabelProvider.INSTANCE.getLabel(relation);
        toolTipFigure.setText(text);

        String type = ArchiLabelProvider.INSTANCE.getDefaultName(relation.eClass());
        toolTipFigure.setType(Messages.AbstractArchimateConnectionFigure_0 + " " + type); //$NON-NLS-1$

        String rubric = ArchiLabelProvider.INSTANCE.getRelationshipSentence(relation);
        toolTipFigure.setRubric(rubric);

        return toolTipFigure;
    }
    
}
