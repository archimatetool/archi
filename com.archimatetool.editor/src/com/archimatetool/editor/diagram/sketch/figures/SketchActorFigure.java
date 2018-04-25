/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

import com.archimatetool.editor.diagram.figures.AbstractLabelFigure;
import com.archimatetool.editor.diagram.figures.ToolTipFigure;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.model.ISketchModelActor;



/**
 * Sketch Actor Figure
 * 
 * @author Phillip Beauvoir
 */
public class SketchActorFigure extends AbstractLabelFigure {

    public SketchActorFigure(ISketchModelActor actor) {
        super(actor);
    }
    
    @Override
    protected void paintFigure(Graphics graphics) {
        super.paintFigure(graphics);
        
        graphics.setAlpha(getAlpha());
        
        graphics.setForegroundColor(getFillColor());
        
        graphics.setAntialias(SWT.ON);
        Rectangle bounds = getBounds().getCopy();
        
        graphics.setLineWidth(2);
        
        bounds.height -= getLabel().getPreferredSize().height;
        
        int narrowest = Math.min(bounds.width, bounds.height);
        
        int diameter = narrowest / 3;
        int midX = bounds.x + bounds.width / 2;
        
        // Head
        graphics.drawOval(midX - (diameter / 2), bounds.y + 1, diameter, diameter);
        
        // Body
        int bodyY2 = bounds.y + bounds.height - (bounds.height / 3);
        graphics.drawLine(midX, bounds.y + diameter, midX, bodyY2);
        
        // Arms
        int armsY = bounds.y + diameter + (bounds.height / 8);
        graphics.drawLine(midX - diameter, armsY, midX + diameter, armsY);
        
        // Legs
        graphics.drawLine(midX, bodyY2, midX - diameter, bounds.y + bounds.height);
        graphics.drawLine(midX, bodyY2, midX + diameter, bounds.y + bounds.height);
    }

    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds().getCopy();
        bounds.y += bounds.height - getLabel().getPreferredSize().height;
        bounds.height = getLabel().getPreferredSize().height;
        
        return bounds;
    }
    
    @Override
    public IFigure getToolTip() {
        ToolTipFigure tooltip = (ToolTipFigure)super.getToolTip();
        
        if(tooltip == null) {
            return null;
        }
        
        String text = ArchiLabelProvider.INSTANCE.getLabel(getDiagramModelObject());
        tooltip.setText(text);
        tooltip.setType(Messages.SketchActorFigure_0);
        
        return tooltip;
    }
}
