/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CompoundBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.BlockFlow;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.SWT;

import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IDiagramModelNote;


/**
 * Note Figure
 * 
 * @author Phillip Beauvoir
 */
public class NoteFigure
extends AbstractDiagramModelObjectFigure {
    
    protected static final Dimension DEFAULT_SIZE = new Dimension(185, 80);
    
    private TextFlow fTextFlow;
    
    public NoteFigure(IDiagramModelNote diagramModelNote) {
        super(diagramModelNote);
    }
    
    @Override
    protected void setUI() {
        //setToolTip(new ToolTipFigure("Double-click to edit"));
        
        setBorder(new CompoundBorder(new LineBorder() {

            @Override
            public void paint(IFigure figure, Graphics graphics, Insets insets) {
                boolean drawShadows = Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_SHADOWS);
                int shadow_offset = drawShadows ? 2 : 0;
                
                tempRect.setBounds(getPaintRectangle(figure, insets));
                if(getWidth() % 2 == 1) {
                    tempRect.width--;
                    tempRect.height--;
                }
                tempRect.shrink(getWidth() / 2, getWidth() / 2);
                graphics.setLineWidth(getWidth());

                graphics.setForegroundColor(getLineColor());

                PointList list = new PointList();
                list.addPoint(tempRect.x, tempRect.y);
                list.addPoint(tempRect.x + tempRect.width - shadow_offset, tempRect.y);
                list.addPoint(tempRect.x + tempRect.width - shadow_offset, tempRect.y + tempRect.height - 12);
                list.addPoint(tempRect.x + tempRect.width - 12, tempRect.y + tempRect.height - shadow_offset);
                list.addPoint(tempRect.x, tempRect.y + tempRect.height - shadow_offset);
                graphics.drawPolygon(list);
            }
        }, new MarginBorder(3)));
        
        ToolbarLayout layout = new ToolbarLayout();
        setLayoutManager(layout);

        FlowPage page = new FlowPage();
        BlockFlow block = new BlockFlow();
        fTextFlow = new TextFlow();
        fTextFlow.setLayoutManager(new ParagraphTextLayout(fTextFlow, ParagraphTextLayout.WORD_WRAP_SOFT));
        block.add(fTextFlow);
        page.add(block);
        setOpaque(true);
        add(page);
    }
    
    public void refreshVisuals() {
        // Text
        setText(((IDiagramModelNote)getDiagramModelObject()).getContent());
        
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
    
    public void setText(String text) {
        fTextFlow.setText(StringUtils.safeString(text));
    }

    @Override
    public IFigure getTextControl() {
        return fTextFlow;
    }

    /**
     * Over-ride this to give us a bigger figure
     * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
     */
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
        
        boolean drawShadows = Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_SHADOWS);
        int shadow_offset = drawShadows ? 3 : 0;
        
        Rectangle tempRect = getBounds().getCopy();
        PointList list = new PointList();
        
        if(drawShadows) {
            graphics.setAlpha(100);
            graphics.setBackgroundColor(ColorConstants.black);
            list.addPoint(tempRect.x, tempRect.y);
            list.addPoint(tempRect.x + tempRect.width, tempRect.y + 2);
            list.addPoint(tempRect.x + tempRect.width, tempRect.y + tempRect.height - 12);
            list.addPoint(tempRect.x + tempRect.width - 12, tempRect.y + tempRect.height);
            list.addPoint(tempRect.x + 2, tempRect.y + tempRect.height);
            graphics.fillPolygon(list);
        }
        
        list.removeAllPoints();
        list.addPoint(tempRect.x, tempRect.y);
        list.addPoint(tempRect.x + tempRect.width - shadow_offset, tempRect.y);
        list.addPoint(tempRect.x + tempRect.width - shadow_offset, tempRect.y + tempRect.height - 13);
        list.addPoint(tempRect.x + tempRect.width - 13, tempRect.y + tempRect.height - shadow_offset);
        list.addPoint(tempRect.x, tempRect.y + tempRect.height - shadow_offset);
        
        graphics.setAlpha(255);
        graphics.setBackgroundColor(getFillColor());
        graphics.fillPolygon(list);
    }
}
