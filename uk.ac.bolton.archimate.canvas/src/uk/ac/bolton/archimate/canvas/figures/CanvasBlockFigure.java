/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.canvas.figures;

import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.BlockFlow;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import uk.ac.bolton.archimate.canvas.model.ICanvasModelBlock;
import uk.ac.bolton.archimate.editor.diagram.figures.AbstractContainerFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.ToolTipFigure;
import uk.ac.bolton.archimate.editor.diagram.util.AnimationUtil;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.editor.utils.StringUtils;

/**
 * Canvas Block Figure
 * 
 * @author Phillip Beauvoir
 */
public class CanvasBlockFigure extends AbstractContainerFigure {
    
    public static Dimension DEFAULT_SIZE = new Dimension(200, 200);
    
    private TextFlow fTextFlow;
    private TextPositionDelegate fTextPositionDelegate;
    private IconicDelegate fIconicDelegate;
    private Color fBorderColor;
    
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
            public void relocate(IFigure target) {
                Rectangle bounds = getBounds().getCopy();
                translateFromParent(bounds);
                target.setBounds(bounds);
            }
        };
        
        FlowPage flowPage = new FlowPage();
        BlockFlow block = new BlockFlow();
        fTextFlow = new TextFlow();
        fTextFlow.setLayoutManager(new ParagraphTextLayout(fTextFlow, ParagraphTextLayout.WORD_WRAP_HARD));
        block.add(fTextFlow);
        flowPage.add(block);
        
        Figure textWrapperFigure = new Figure();
        textWrapperFigure.setLayoutManager(new GridLayout());
        textWrapperFigure.add(flowPage, new GridData(SWT.CENTER, SWT.CENTER, true, true));
        add(textWrapperFigure, mainLocator);
        fTextPositionDelegate = new TextPositionDelegate(textWrapperFigure, flowPage, getDiagramModelObject());
        
        // This last
        add(getMainFigure(), mainLocator);

        // Have to add this if we want Animation to work on figures!
        AnimationUtil.addFigureForAnimation(getMainFigure());

        fIconicDelegate = new IconicDelegate(getDiagramModelObject());
        fIconicDelegate.updateImage();
        repaint();
    }
    
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
        ((BlockFlow)fTextFlow.getParent()).setHorizontalAligment(getDiagramModelObject().getTextAlignment());
        
        // Text Position
        fTextPositionDelegate.updateTextPosition();
    }

    public void updateImage() {
        fIconicDelegate.updateImage();
        repaint();
    }
    
    private void setText() {
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

    public TextFlow getTextControl() {
        return fTextFlow;
    }
    
    public Dimension getDefaultSize() {
        return DEFAULT_SIZE;
    }

    @Override
    protected void drawFigure(Graphics graphics) {
        graphics.setAntialias(SWT.ON);
        
        Rectangle bounds = getBounds().getCopy();
        
        graphics.setBackgroundColor(getFillColor());
        graphics.fillRectangle(bounds);
        
        // Border
        if(getBorderColor() != null) {
            graphics.setForegroundColor(getBorderColor());
            graphics.drawRectangle(new Rectangle(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1));
        }
        
        fIconicDelegate.drawIcon(graphics, bounds);
    }

    @Override
    protected void drawTargetFeedback(Graphics graphics) {
        Rectangle boundsCopy = getBounds().getCopy();
        boundsCopy.shrink(1, 1);
        graphics.pushState();
        graphics.setBackgroundColor(ColorFactory.getDarkerColor(getFillColor()));
        graphics.fillRectangle(boundsCopy);
        fIconicDelegate.drawIcon(graphics, getBounds().getCopy());
        //graphics.setForegroundColor(ColorConstants.blue);
        //graphics.setLineWidth(2);
        //graphics.drawRectangle(boundsCopy);
        graphics.popState();
    }
    
    @Override
    public IFigure getToolTip() {
        ToolTipFigure toolTipFigure = (ToolTipFigure)super.getToolTip();
        
        if(toolTipFigure != null) {
            toolTipFigure.setText("Block");
        }
        
        return toolTipFigure;
    }

    @Override
    public void dispose() {
        fIconicDelegate.dispose();
    }
}
