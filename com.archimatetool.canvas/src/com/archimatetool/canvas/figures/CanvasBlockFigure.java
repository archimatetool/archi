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


/**
 * Canvas Block Figure
 * 
 * @author Phillip Beauvoir
 */
public class CanvasBlockFigure extends AbstractContainerFigure implements ITextFigure {
    
    private TextFlow fTextFlow;
    private TextPositionDelegate fTextPositionDelegate;
    private Color fBorderColor;
    
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
                Rectangle bounds = getBounds().getCopy();
                translateFromParent(bounds);
                target.setBounds(bounds);
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
        
        // Font
        setFont();

        // Fill Color
        setFillColor();
        
        // Font Color
        setFontColor();
        
        // Border Color
        setBorderColor();
        
        // Alignment
        ((FlowPage)fTextFlow.getParent()).setHorizontalAligment(getDiagramModelObject().getTextAlignment());
        
        // Text Position
        fTextPositionDelegate.updateTextPosition();

        // Icon Image
        updateIconImage();

        // Repaint
        repaint();
    }

    @Override
    public void setText() {
        String content = getDiagramModelObject().getContent();
        getTextControl().setText(StringUtils.safeString(content));
    }
    
    protected void setBorderColor() {
        String val = getDiagramModelObject().getBorderColor();
        Color c = ColorFactory.get(val);
        if(c != fBorderColor) {
            fBorderColor = c;
            repaint();
        }
    }
    
    /**
     * @return The Border Color to use or null for none
     */
    public Color getBorderColor() {
        return fBorderColor;
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
        
        Rectangle bounds = getBounds().getCopy();
        
        bounds.width--;
        bounds.height--;
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, 1, bounds);
        
        graphics.setBackgroundColor(background);
        graphics.fillRectangle(bounds);
        
        // Icon
        drawIconImage(graphics, bounds);
        
        // Border
        if(getBorderColor() != null) {
            graphics.setAlpha(getLineAlpha());
            graphics.setForegroundColor(getBorderColor());
            graphics.drawRectangle(bounds.x, bounds.y, bounds.width, bounds.height);
        }
        
        graphics.popState();
    }
}
