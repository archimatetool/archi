/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.diagram.figures.ToolTipFigure;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.IGraphicsIcon;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IDiagramModelReference;


/**
 * Figure for a Diagram Model Reference Figure
 * 
 * @author Phillip Beauvoir
 */
public class DiagramModelReferenceFigure extends AbstractTextControlContainerFigure {
    
    public DiagramModelReferenceFigure(IDiagramModelObject diagramModelObject) {
        super(diagramModelObject, TEXT_FLOW_CONTROL);
        
        // Use a Rectangle Figure Delegate to Draw
        setFigureDelegate(new RectangleFigureDelegate(this, 22 - getTextControlMarginWidth()));
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        super.drawFigure(graphics);
        drawIcon(graphics);
    }
    
    /**
     * Draw the icon
     */
    protected void drawIcon(Graphics graphics) {
        if(hasIconImage()) {
            return;
        }
        
        // Draw the icon depending on the diagramModelObject
        IDiagramModel dm = ((IDiagramModelReference)getDiagramModelObject()).getReferencedModel();

        IGraphicsIcon graphicsIcon = ArchiLabelProvider.INSTANCE.getGraphicsIconForDiagramModel(dm);
        if(graphicsIcon != null) {
            graphicsIcon.drawIcon(graphics, getIconOrigin());
        }
    }
    
    /**
     * @return The icon start position
     */
    protected Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 19, bounds.y + 6);
    }
    
    @Override
    public IFigure getToolTip() {
        ToolTipFigure tooltip = (ToolTipFigure)super.getToolTip();
        
        if(tooltip == null) {
            return null;
        }
        
        tooltip.setType(Messages.DiagramModelReferenceFigure_0);
        
        return tooltip;
    }
}