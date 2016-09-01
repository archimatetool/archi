/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.connections;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;

import com.archimatetool.editor.diagram.figures.ToolTipFigure;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModelArchimateConnection;



/**
 * Abstract implementation of an Archimate connection figure.  Subclasses should decide how to draw the line
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractArchimateConnectionFigure
extends AbstractDiagramConnectionFigure implements IArchimateConnectionFigure {
    
    @Override
    public IDiagramModelArchimateConnection getModelConnection() {
        return (IDiagramModelArchimateConnection)super.getModelConnection();
    }

    @Override
    public void highlight(boolean set) {
        if(set) {
            setForegroundColor(ColorConstants.red);
            fLineColor = ColorConstants.red;
            setLineWidth(2);
        }
        else {
            setLineColor();
            setLineWidth();
        }
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
