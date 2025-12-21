/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.figures;

import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import com.archimatetool.canvas.model.ICanvasModelBlock;
import com.archimatetool.editor.diagram.figures.AbstractContainerFigure;
import com.archimatetool.editor.diagram.figures.ITextFigure;
import com.archimatetool.editor.diagram.figures.IconicDelegate;
import com.archimatetool.editor.diagram.figures.TextPositionDelegate;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IDiagramModelObject;


/**
 * Canvas Block Figure
 * 
 * @author Phillip Beauvoir
 */
public class CanvasBlockFigure extends AbstractContainerFigure implements ITextFigure {
    
    private TextFlow fTextFlow;
    private TextPositionDelegate fTextPositionDelegate;
    
    private static final int MAX_ICON_SIZE = 100;
    
    public CanvasBlockFigure(ICanvasModelBlock diagramModelBlock) {
        super(diagramModelBlock);
    }
    
    @Override
    public ICanvasModelBlock getDiagramModelObject() {
        return (ICanvasModelBlock)super.getDiagramModelObject();
    }
    
    @Override
    protected void setUI() {
        setLayoutManager(new DelegatingLayout());

        Locator mainLocator = new Locator() {
            @Override
            public void relocate(IFigure target) {
                Rectangle rect = getBounds().getCopy();
                translateFromParent(rect);
                target.setBounds(rect);
            }
        };
        
        FlowPage flowPage = new FlowPage();
        fTextFlow = new TextFlow();
        fTextFlow.setLayoutManager(new ParagraphTextLayout(fTextFlow, ParagraphTextLayout.WORD_WRAP_HARD));
        flowPage.add(fTextFlow);
        
        Figure textWrapperFigure = new Figure();
        textWrapperFigure.setLayoutManager(new GridLayout());
        textWrapperFigure.add(flowPage, new GridData(SWT.CENTER, SWT.CENTER, true, true));
        add(textWrapperFigure, mainLocator);
        fTextPositionDelegate = new TextPositionDelegate(textWrapperFigure, flowPage, getDiagramModelObject());
        
        // This last
        add(getMainFigure(), mainLocator);

        setIconicDelegate(new IconicDelegate(getDiagramModelObject(), MAX_ICON_SIZE));
    }
    
    @Override
    public void refreshVisuals() {
        // Text
        setText();
        
        // Alignment
        ((FlowPage)fTextFlow.getParent()).setHorizontalAligment(getDiagramModelObject().getTextAlignment());
        
        // Text Position
        fTextPositionDelegate.updateTextPosition();

        // Do this last
        super.refreshVisuals();
    }

    @Override
    public void setText() {
        String content = getDiagramModelObject().getContent();
        getTextControl().setText(StringUtils.safeString(content));
    }
    
    /**
     * @return The Border Color to use or null for none
     */
    public Color getBorderColor() {
        return getCachedValue("borderColor", key -> { //$NON-NLS-1$
            // Null is allowed
            return ColorFactory.get(getDiagramModelObject().getBorderColor());
        });
    }

    @Override
    public TextFlow getTextControl() {
        return fTextFlow;
    }
    
    @Override
    protected void drawFigure(Graphics graphics) {
        drawFigure(graphics, getFillColor());
    }

    @Override
    protected void drawTargetFeedback(Graphics graphics) {
        drawFigure(graphics, ColorFactory.getDarkerColor(getFillColor()));
    }
    
    private void drawFigure(Graphics graphics, Color background) {
        graphics.pushState();
        
        graphics.setAntialias(SWT.ON);
        
        graphics.setAlpha(getAlpha());
        
        Rectangle rect = getBounds().getCopy();
        
        // Reduce width and height by 1 pixel
        rect.resize(-1, -1);
        
        boolean drawBorder = getBorderColor() != null && getLineStyle() != IDiagramModelObject.LINE_STYLE_NONE;
        
        if(drawBorder) {
            // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
            setLineWidth(graphics, rect);
            setLineStyle(graphics);
        }
        
        graphics.setBackgroundColor(background);
        graphics.fillRectangle(rect);
        
        // Icon
        drawIconImage(graphics, rect);
        
        // Border
        if(drawBorder) {
            graphics.setAlpha(getLineAlpha());
            graphics.setForegroundColor(getBorderColor());
            graphics.drawRectangle(rect.x, rect.y, rect.width, rect.height);
        }
        
        graphics.popState();
    }
}
