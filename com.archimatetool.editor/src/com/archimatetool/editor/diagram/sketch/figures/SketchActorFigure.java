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
        graphics.setAlpha(getAlpha());
        
        graphics.setForegroundColor(getFillColor());
        
        graphics.setAntialias(SWT.ON);
        Rectangle rect = getBounds().getCopy();
        
        graphics.setLineWidth(getLineWidth() + 1);
        
        rect.height -= getLabel().getPreferredSize().height;
        
        int narrowest = Math.min(rect.width, rect.height);
        
        int diameter = narrowest / 3;
        int midX = rect.x + rect.width / 2;
        
        // Head
        graphics.drawOval(midX - (diameter / 2), rect.y + 1, diameter, diameter);
        
        // Body
        int bodyY2 = rect.y + rect.height - (rect.height / 3);
        graphics.drawLine(midX, rect.y + diameter, midX, bodyY2);
        
        // Arms
        int armsY = rect.y + diameter + (rect.height / 8);
        graphics.drawLine(midX - diameter, armsY, midX + diameter, armsY);
        
        // Legs
        graphics.drawLine(midX, bodyY2, midX - diameter, rect.y + rect.height);
        graphics.drawLine(midX, bodyY2, midX + diameter, rect.y + rect.height);
    }

    @Override
    public Rectangle calculateTextControlBounds() {
        Rectangle rect = getBounds().getCopy();
        rect.y += rect.height - getLabel().getPreferredSize().height;
        rect.height = getLabel().getPreferredSize().height;
        
        return rect;
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
