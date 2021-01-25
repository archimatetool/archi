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
import com.archimatetool.editor.diagram.figures.IconicDelegate;
import com.archimatetool.editor.diagram.figures.TextPositionDelegate;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.ImageFactory;
import com.archimatetool.editor.utils.PlatformUtils;
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
    private MultiToolTipFigure fTooltip;
    private Color fBorderColor;
    
    private static final int MAX_ICON_SIZE = 100;
    
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

        // Text Alignment
        ((FlowPage)fTextFlow.getParent()).setHorizontalAligment(getDiagramModelObject().getTextAlignment());
        
        // Text Position
        fTextPositionDelegate.updateTextPosition();
        
        // Repaint
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
        graphics.pushState();
        
        graphics.setAntialias(SWT.ON);
        
        graphics.setAlpha(getAlpha());
        
        Rectangle bounds = getBounds().getCopy();
        
        bounds.width--;
        bounds.height--;
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        int lineWidth = 1;
        setLineWidth(graphics, lineWidth, bounds);
        
        // Bug on Linux hi-res using Graphics.fillGradient()
        // See https://bugs.eclipse.org/bugs/show_bug.cgi?id=568864
        if(PlatformUtils.isLinux() && ImageFactory.getDeviceZoom() > 100) {
            graphics.setBackgroundColor(getFillColor());
            graphics.fillRectangle(bounds);
        }
        else {
            graphics.setForegroundColor(getFillColor());
            graphics.setBackgroundColor(ColorFactory.getLighterColor(getFillColor(), 0.9f));
            graphics.fillGradient(bounds, false);
        }
        
        // Icon
        drawIconImage(graphics, bounds);
        
        // Border
        if(getBorderColor() != null) {
            graphics.setAlpha(getLineAlpha());
            
            float lineOffset = (float)lineWidth / 2;

            graphics.setForegroundColor(ColorFactory.getLighterColor(getBorderColor(), 0.82f));
            Path path = new Path(null);
            path.moveTo(bounds.x - lineOffset, bounds.y);
            path.lineTo(bounds.x + bounds.width, bounds.y);
            path.lineTo(bounds.x + bounds.width, bounds.y + bounds.height);
            graphics.drawPath(path);
            path.dispose();

            graphics.setForegroundColor(getBorderColor());
            path = new Path(null);
            path.moveTo(bounds.x, bounds.y - lineOffset);
            path.lineTo(bounds.x, bounds.y + bounds.height);
            path.lineTo(bounds.x + bounds.width + lineOffset, bounds.y + bounds.height);
            graphics.drawPath(path);
            path.dispose();
        }
        
        graphics.popState();
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
}
