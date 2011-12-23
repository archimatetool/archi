package uk.ac.bolton.archimate.canvas.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.BlockFlow;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

import uk.ac.bolton.archimate.canvas.model.ICanvasModelSticky;
import uk.ac.bolton.archimate.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.editor.utils.StringUtils;

/**
 * Sticky Figure
 * 
 * @author Phillip Beauvoir
 */
public class CanvasStickyFigure
extends AbstractDiagramModelObjectFigure {
    
    static Dimension DEFAULT_SIZE = new Dimension(75, 63);
    
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
        BlockFlow block = new BlockFlow();
        fTextFlow = new TextFlow();
        fTextFlow.setLayoutManager(new ParagraphTextLayout(fTextFlow, ParagraphTextLayout.WORD_WRAP_HARD));
        block.add(fTextFlow);
        flowPage.add(block);
        
        add(flowPage, new GridData(SWT.CENTER, SWT.CENTER, true, true));
        fTextPositionDelegate = new TextPositionDelegate(this, flowPage, getDiagramModelObject());
        
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

        // Text Alignment
        ((BlockFlow)fTextFlow.getParent()).setHorizontalAligment(getDiagramModelObject().getTextAlignment());
        
        // Text Position
        fTextPositionDelegate.updateTextPosition();
    }
    
    public void updateImage() {
        fIconicDelegate.updateImage();
        repaint();
    }
    
    private void setText() {
        String text = getDiagramModelObject().getContent();
        getTextControl().setText(StringUtils.safeString(text));
    }

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
    public Dimension getPreferredSize(int wHint, int hHint) {
        return getDefaultSize();
    }

    public Dimension getDefaultSize() {
        return DEFAULT_SIZE;
    }

    @Override
    protected void paintFigure(Graphics graphics) {
        graphics.setAntialias(SWT.ON);
        
        Rectangle bounds = getBounds().getCopy();
        
        graphics.setForegroundColor(getFillColor());
        graphics.setBackgroundColor(ColorFactory.getLighterColor(getFillColor(), 0.9f));
        graphics.fillGradient(bounds, false);
        
        // Border
        if(getBorderColor() != null) {
            graphics.setForegroundColor(ColorFactory.getLighterColor(getBorderColor(), 0.82f));
            graphics.drawLine(bounds.x, bounds.y, bounds.x + bounds.width - 1, bounds.y);
            graphics.drawLine(bounds.x + bounds.width - 1, bounds.y, bounds.x + bounds.width - 1, bounds.y + bounds.height - 1);

            graphics.setForegroundColor(getBorderColor());
            graphics.drawLine(bounds.x, bounds.y, bounds.x, bounds.y + bounds.height - 1);
            graphics.drawLine(bounds.x, bounds.y + bounds.height - 1, bounds.x + bounds.width - 1, bounds.y + bounds.height - 1);
        }

        fIconicDelegate.drawIcon(graphics, bounds);
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
            fTooltip.setText("Sticky");
        }
        
        return fTooltip;
    }
    
    @Override
    public void dispose() {
        fIconicDelegate.dispose();
    }
}
