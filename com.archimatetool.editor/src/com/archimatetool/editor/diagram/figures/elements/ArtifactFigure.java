/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.elements;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractTextControlContainerFigure;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.RectangleFigureDelegate;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.model.IIconic;




/**
 * Artifact Figure
 * 
 * @author Phillip Beauvoir
 */
public class ArtifactFigure extends AbstractTextControlContainerFigure implements IArchimateFigure {

    private static final int FOLD_HEIGHT = 18;
    
    protected IFigureDelegate rectangleDelegate;

    public ArtifactFigure() {
        super(TEXT_FLOW_CONTROL);
        rectangleDelegate = new RectangleFigureDelegate(this);
    }

    @Override
    public void drawFigure(Graphics graphics) {
        if(getFigureDelegate() != null) {
            getFigureDelegate().drawFigure(graphics);
            drawIcon(graphics);
            return;
        }

        graphics.pushState();
        
        Rectangle rect = getBounds().getCopy();
        rect.width--;
        rect.height--;
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, 1, rect);
        
        if(!isEnabled()) {
            setDisabledState(graphics);
        }

        graphics.setAlpha(getAlpha());
        graphics.setBackgroundColor(getFillColor());
        Pattern gradient = applyGradientPattern(graphics, rect);
        
        Path path1 = new Path(null);
        path1.moveTo(rect.x, rect.y);
        path1.lineTo(rect.x + rect.width - FOLD_HEIGHT, rect.y);
        path1.lineTo(rect.x + rect.width, rect.y + FOLD_HEIGHT);
        path1.lineTo(rect.x + rect.width, rect.y + rect.height);
        path1.lineTo(rect.x, rect.y + rect.height);
        path1.close();
        graphics.fillPath(path1);
        
        disposeGradientPattern(graphics, gradient);

        // Fold
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
        
        Path path2 = new Path(null);
        path2.moveTo(rect.x + rect.width - FOLD_HEIGHT, rect.y);
        path2.lineTo(rect.x + rect.width, rect.y + FOLD_HEIGHT);
        path2.lineTo(rect.x + rect.width - FOLD_HEIGHT, rect.y + FOLD_HEIGHT);
        graphics.fillPath(path2);
        
        path2.dispose();
        
        // Lines
        graphics.setAlpha(getLineAlpha());
        graphics.setForegroundColor(getLineColor());
        
        graphics.drawPath(path1);
        path1.dispose();
        
        Path path3 = new Path(null);
        path3.moveTo(rect.x + rect.width, rect.y + FOLD_HEIGHT);
        path3.lineTo(rect.x + rect.width - FOLD_HEIGHT, rect.y + FOLD_HEIGHT);
        path3.lineTo(rect.x + rect.width - FOLD_HEIGHT, rect.y);
        graphics.drawPath(path3);
        
        path3.dispose();
        
        // Icon
        // drawIconImage(graphics, bounds);
        int rightOffset = ((IIconic)getDiagramModelObject()).getImagePosition() == IIconic.ICON_POSITION_TOP_RIGHT ? -(FOLD_HEIGHT + 1) : 0;
        //int rightOffset = -(FOLD_HEIGHT + 1);
        drawIconImage(graphics, rect, 0, rightOffset, 0, 0);

        graphics.popState();
    }
    
    /**
     * Draw the icon
     */
    private void drawIcon(Graphics graphics) {
        if(!isIconVisible()) {
            return;
        }
        
        graphics.pushState();
        
        graphics.setLineWidth(1);
        graphics.setForegroundColor(isEnabled() ? ColorConstants.black : ColorConstants.gray);
        
        Point pt = getIconOrigin();
        
        Path path = new Path(null);
        
        path.moveTo(pt.x, pt.y);
        path.lineTo(pt.x + 7, pt.y);
        path.lineTo(pt.x + 12, pt.y + 5);
        path.lineTo(pt.x + 12, pt.y + 15);
        path.lineTo(pt.x, pt.y + 15);
        path.lineTo(pt.x, pt.y - 0.5f);
        
        path.moveTo(pt.x + 7, pt.y);
        path.lineTo(pt.x + 7, pt.y + 5);
        path.lineTo(pt.x + 12, pt.y + 5);
        
        graphics.drawPath(path);
        path.dispose();
        
        graphics.popState();
    }
    
    /**
     * @return The icon start position
     */
    private Point getIconOrigin() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width - 16, bounds.y + 6);
    }

    @Override
    public int getIconOffset() {
        return 22; // will also work for the right indent in the alternate figure
    }
    
    @Override
    public IFigureDelegate getFigureDelegate() {
        return getDiagramModelArchimateObject().getType() == 0 ? null : rectangleDelegate;
    }
}
