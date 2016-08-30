/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.BlockFlow;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.SWT;

import com.archimatetool.editor.diagram.figures.AbstractContainerFigure;
import com.archimatetool.editor.diagram.figures.ToolTipFigure;
import com.archimatetool.editor.diagram.util.AnimationUtil;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.ISketchModelSticky;


/**
 * Sticky Figure
 * 
 * @author Phillip Beauvoir
 */
public class StickyFigure extends AbstractContainerFigure {
    
    protected static final int TEXT_INDENT = 10;
    
    private TextFlow fTextFlow;
    
    public StickyFigure(ISketchModelSticky diagramModelSticky) {
        super(diagramModelSticky);
    }
    
    @Override
    protected void setUI() {
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
        
        // Line Color
        setLineColor();
        
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

    @Override
    protected void drawFigure(Graphics graphics) {
        graphics.setAntialias(SWT.ON);
        
        Rectangle bounds = getBounds().getCopy();
        graphics.setAlpha(100);
        graphics.setBackgroundColor(ColorConstants.black);
        graphics.fillRectangle(new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height));

        graphics.setAlpha(255);
        graphics.setBackgroundColor(getFillColor());
        graphics.fillRectangle(new Rectangle(bounds.x, bounds.y, bounds.width, bounds.height));
        
        // Outline
        graphics.setForegroundColor(getLineColor());
        graphics.drawRectangle(new Rectangle(bounds.x, bounds.y, bounds.width - 1, bounds.height - 1));
    }

    @Override
    protected void drawTargetFeedback(Graphics graphics) {
        Rectangle bounds = getBounds().getCopy();
        graphics.pushState();
        graphics.setForegroundColor(ColorConstants.blue);
        graphics.setLineWidth(2);
        graphics.drawRectangle(new Rectangle(bounds.x + 1, bounds.y + 1, bounds.width - 2, bounds.height - 2));
        graphics.popState();
    }
    
    @Override
    public IFigure getToolTip() {
        ToolTipFigure tooltip = (ToolTipFigure)super.getToolTip();
        
        if(tooltip == null) {
            return null;
        }
        
        String text = ArchiLabelProvider.INSTANCE.getLabel(getDiagramModelObject());
        tooltip.setText(text);
        tooltip.setType(Messages.StickyFigure_0);
       
        return tooltip;
    }
}
