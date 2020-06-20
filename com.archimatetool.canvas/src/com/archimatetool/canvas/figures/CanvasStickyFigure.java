/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Path;

import com.archimatetool.canvas.model.ICanvasModelSticky;
import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.ITextFigure;
import com.archimatetool.editor.diagram.figures.TextPositionDelegate;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.utils.StringUtils;


/**
 * Sticky Figure
 * 
 * @author Phillip Beauvoir
 */
public class CanvasStickyFigure
extends AbstractDiagramModelObjectFigure implements ITextFigure {
    
    private TextFlow fTextFlow;
    private TextPositionDelegate fTextPositionDelegate;
    private IconicDelegate fIconicDelegate;
    private MultiToolTipFigure fTooltip;
    private Color fBorderColor;
    
    public CanvasStickyFigure(ICanvasModelSticky diagramModelSticky) {
        super(diagramModelSticky);
    }
    
    @Override
    public ICanvasModelSticky getDiagramModelObject() {
        return (ICanvasModelSticky)super.getDiagramModelObject();
    }
    
    @Override
    protected void setUI() {
        setLayoutManager(new GridLayout());
        
        FlowPage flowPage = new FlowPage();
        fTextFlow = new TextFlow();
        fTextFlow.setLayoutManager(new ParagraphTextLayout(fTextFlow, ParagraphTextLayout.WORD_WRAP_HARD));
        flowPage.add(fTextFlow);
        
        add(flowPage, new GridData(SWT.CENTER, SWT.CENTER, true, true));
        fTextPositionDelegate = new TextPositionDelegate(this, flowPage, getDiagramModelObject());
        
        fIconicDelegate = new IconicDelegate(getDiagramModelObject());
        fIconicDelegate.updateImage();
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

        // Text Alignment
        ((FlowPage)fTextFlow.getParent()).setHorizontalAligment(getDiagramModelObject().getTextAlignment());
        
        // Text Position
        fTextPositionDelegate.updateTextPosition();
        
        // Repaint
        repaint();
    }
    
    public void updateImage() {
        fIconicDelegate.updateImage();
        repaint();
    }
    
    @Override
    public void setText() {
        String text = getDiagramModelObject().getContent();
        getTextControl().setText(StringUtils.safeString(text));
    }

    @Override
    public TextFlow getTextControl() {
        return fTextFlow;
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
    protected void paintFigure(Graphics graphics) {
        graphics.setAntialias(SWT.ON);
        
        graphics.setAlpha(getAlpha());
        
        Rectangle bounds = getBounds().getCopy();
        
        bounds.width--;
        bounds.height--;
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        float lineWidth = 1.0f;
        float offSet = lineWidth / 2;
        setLineWidth(graphics, lineWidth, bounds);
        
        graphics.setForegroundColor(getFillColor());
        graphics.setBackgroundColor(ColorFactory.getLighterColor(getFillColor(), 0.9f));
        graphics.fillGradient(bounds, false);
        
        fIconicDelegate.drawIcon(graphics, bounds);
        
        // Border
        if(getBorderColor() != null) {
            graphics.setAlpha(getLineAlpha());
            
            graphics.setForegroundColor(ColorFactory.getLighterColor(getBorderColor(), 0.82f));
            Path path = new Path(null);
            path.moveTo(bounds.x - offSet, bounds.y);
            path.lineTo(bounds.x + bounds.width, bounds.y);
            path.lineTo(bounds.x + bounds.width, bounds.y + bounds.height);
            graphics.drawPath(path);
            path.dispose();

            graphics.setForegroundColor(getBorderColor());
            path = new Path(null);
            path.moveTo(bounds.x, bounds.y - offSet);
            path.lineTo(bounds.x, bounds.y + bounds.height);
            path.lineTo(bounds.x + bounds.width + offSet, bounds.y + bounds.height);
            graphics.drawPath(path);
            path.dispose();
        }
    }
    
    @Override
    public IFigure getToolTip() {
        if(fTooltip == null && Preferences.doShowViewTooltips()) {
            fTooltip = new MultiToolTipFigure();
            setToolTip(fTooltip);
        }
        
        if(fTooltip == null || !Preferences.doShowViewTooltips()) {
            return null;
        }

        String notes = getDiagramModelObject().getNotes();
        if(StringUtils.isSet(notes)) {
            fTooltip.setText(notes);
        }
        else {
            fTooltip.setText(Messages.CanvasStickyFigure_0);
        }
        
        return fTooltip;
    }
    
    @Override
    public void dispose() {
        fIconicDelegate.dispose();
    }
}
