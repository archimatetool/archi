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
import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.ITextFigure;
import com.archimatetool.editor.diagram.figures.IconicDelegate;
import com.archimatetool.editor.diagram.figures.TextPositionDelegate;
import com.archimatetool.editor.preferences.IPreferenceConstants;
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
        
        // Text Alignment
        ((FlowPage)fTextFlow.getParent()).setHorizontalAligment(getDiagramModelObject().getTextAlignment());
        
        // Text Position
        fTextPositionDelegate.updateTextPosition();

        // Do this last
        super.refreshVisuals();
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
    protected void paintFigure(Graphics graphics) {
        graphics.pushState();
        
        graphics.setAntialias(SWT.ON);
        
        graphics.setAlpha(getAlpha());
        
        Rectangle rect = getBounds().getCopy();
        
        // Reduce width and height by 1 pixel
        rect.resize(-1, -1);
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        setLineWidth(graphics, rect);
        
        // Bug on Linux hi-res using Graphics.fillGradient()
        // See https://bugs.eclipse.org/bugs/show_bug.cgi?id=568864
        if(PlatformUtils.isLinux() && ImageFactory.getDeviceZoom() > 100) {
            graphics.setBackgroundColor(getFillColor());
            graphics.fillRectangle(rect);
        }
        else {
            graphics.setForegroundColor(getFillColor());
            graphics.setBackgroundColor(ColorFactory.getLighterColor(getFillColor(), 0.9f));
            graphics.fillGradient(rect, false);
        }
        
        // Icon
        drawIconImage(graphics, rect);
        
        // Border
        if(getBorderColor() != null) {
            graphics.setAlpha(getLineAlpha());
            
            float lineOffset = (float)getLineWidth() / 2;

            graphics.setForegroundColor(ColorFactory.getLighterColor(getBorderColor(), 0.82f));
            Path path = new Path(null);
            path.moveTo(rect.x - lineOffset, rect.y);
            path.lineTo(rect.x + rect.width, rect.y);
            path.lineTo(rect.x + rect.width, rect.y + rect.height);
            graphics.drawPath(path);
            path.dispose();

            graphics.setForegroundColor(getBorderColor());
            path = new Path(null);
            path.moveTo(rect.x, rect.y - lineOffset);
            path.lineTo(rect.x, rect.y + rect.height);
            path.lineTo(rect.x + rect.width + lineOffset, rect.y + rect.height);
            graphics.drawPath(path);
            path.dispose();
        }
        
        graphics.popState();
    }
    
    @Override
    public IFigure getToolTip() {
        if(!ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.VIEW_TOOLTIPS)) {
            return null;
        }
        
        if(fTooltip == null) {
            fTooltip = new MultiToolTipFigure();
            setToolTip(fTooltip);
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
