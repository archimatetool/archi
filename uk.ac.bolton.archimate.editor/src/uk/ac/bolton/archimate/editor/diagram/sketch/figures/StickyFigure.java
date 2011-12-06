/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.sketch.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.BlockFlow;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.SWT;

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractContainerFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.ToolTipFigure;
import uk.ac.bolton.archimate.editor.diagram.util.AnimationUtil;
import uk.ac.bolton.archimate.editor.ui.ArchimateLabelProvider;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.ISketchModelSticky;

/**
 * Sticky Figure
 * 
 * @author Phillip Beauvoir
 */
public class StickyFigure extends AbstractContainerFigure {
    
    protected int SHADOW_OFFSET = 3;
    protected int TEXT_INDENT = 10;
    
    static Dimension DEFAULT_SIZE = new Dimension(135, 70);
    
    private TextFlow fTextFlow;
    
    public StickyFigure(ISketchModelSticky diagramModelSticky) {
        super(diagramModelSticky);
    }
    
    @Override
    protected void setUI() {
        //setToolTip(new ToolTipFigure("Double-click to edit"));
        
        setLayoutManager(new DelegatingLayout());
        
        Locator textLocator = new Locator() {
            public void relocate(IFigure target) {
                Rectangle bounds = calculateTextControlBounds();
                translateFromParent(bounds);
                target.setBounds(bounds);
            }
        };
        
        FlowPage page = new FlowPage();
        BlockFlow block = new BlockFlow();
        fTextFlow = new TextFlow();
        fTextFlow.setLayoutManager(new ParagraphTextLayout(fTextFlow, ParagraphTextLayout.WORD_WRAP_SOFT));
        block.add(fTextFlow);
        page.add(block);
        add(page, textLocator);
        
        Locator mainLocator = new Locator() {
            public void relocate(IFigure target) {
                Rectangle bounds = getBounds().getCopy();
                translateFromParent(bounds);
                target.setBounds(bounds);
            }
        };
        
        add(getMainFigure(), mainLocator);

        // Have to add this if we want Animation to work on figures!
        AnimationUtil.addFigureForAnimation(getMainFigure());
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
        
        // Alignment
        ((BlockFlow)fTextFlow.getParent()).setHorizontalAligment(getDiagramModelObject().getTextAlignment());
    }
    
    private void setText() {
        String text = ((ISketchModelSticky)getDiagramModelObject()).getContent();
        getTextControl().setText(StringUtils.safeString(text));
    }

    public TextFlow getTextControl() {
        return fTextFlow;
    }
    
    public Rectangle calculateTextControlBounds() {
        Rectangle bounds = getBounds().getCopy();
        bounds.x += TEXT_INDENT;
        bounds.y += 5;
        bounds.width = bounds.width - (TEXT_INDENT * 2);
        bounds.height -= 10;
        return bounds;
    }

    public Dimension getDefaultSize() {
        return DEFAULT_SIZE;
    }

    @Override
    protected void drawFigure(Graphics graphics) {
        graphics.setAntialias(SWT.ON);
        
        Rectangle bounds = getBounds().getCopy();
        graphics.setAlpha(100);
        graphics.setBackgroundColor(ColorConstants.black);
        graphics.fillRectangle(new Rectangle(bounds.x + SHADOW_OFFSET, bounds.y + SHADOW_OFFSET, bounds.width - SHADOW_OFFSET, bounds.height - SHADOW_OFFSET));

        graphics.setAlpha(255);
        graphics.setBackgroundColor(getFillColor());
        graphics.fillRectangle(new Rectangle(bounds.x, bounds.y, bounds.width - SHADOW_OFFSET, bounds.height - SHADOW_OFFSET));
        
        // Outline
        graphics.setForegroundColor(ColorConstants.darkGray);
        graphics.drawRectangle(new Rectangle(bounds.x, bounds.y, bounds.width - SHADOW_OFFSET - 1, bounds.height - SHADOW_OFFSET - 1));
    }

    @Override
    protected void drawTargetFeedback(Graphics graphics) {
        Rectangle bounds = getBounds().getCopy();
        graphics.pushState();
        graphics.setForegroundColor(ColorConstants.blue);
        graphics.setLineWidth(2);
        graphics.drawRectangle(new Rectangle(bounds.x + 1, bounds.y + 1, bounds.width - SHADOW_OFFSET - 1, bounds.height - SHADOW_OFFSET - 1));
        graphics.popState();
    }
    
    @Override
    public IFigure getToolTip() {
        ToolTipFigure tooltip = (ToolTipFigure)super.getToolTip();
        
        if(tooltip == null) {
            return null;
        }
        
        String text = ArchimateLabelProvider.INSTANCE.getLabel(getDiagramModelObject());
        tooltip.setText(text);
        tooltip.setType("Type: Sticky");
       
        return tooltip;
    }
}
