/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures.diagram;

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

import uk.ac.bolton.archimate.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.ToolTipFigure;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.IDiagramModelNote;

/**
 * Note Figure
 * 
 * @author Phillip Beauvoir
 */
public class NoteFigure
extends AbstractDiagramModelObjectFigure {
    
    static Dimension DEFAULT_SIZE = new Dimension(185, 80);
    
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
                tempRect.setBounds(getPaintRectangle(figure, insets));
                if(getWidth() % 2 == 1) {
                    tempRect.width--;
                    tempRect.height--;
                }
                tempRect.shrink(getWidth() / 2, getWidth() / 2);
                graphics.setLineWidth(getWidth());

                graphics.setForegroundColor(ColorConstants.black);

                PointList list = new PointList();
                list.addPoint(tempRect.x, tempRect.y);
                list.addPoint(tempRect.x + tempRect.width - 2, tempRect.y);
                list.addPoint(tempRect.x + tempRect.width - 2, tempRect.y + tempRect.height - 12);
                list.addPoint(tempRect.x + tempRect.width - 12, tempRect.y + tempRect.height - 2);
                list.addPoint(tempRect.x, tempRect.y + tempRect.height - 2);
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
        
        Rectangle tempRect = getBounds().getCopy();
        PointList list = new PointList();
        graphics.setAlpha(100);
        graphics.setBackgroundColor(ColorConstants.black);
        list.addPoint(tempRect.x, tempRect.y);
        list.addPoint(tempRect.x + tempRect.width, tempRect.y + 2);
        list.addPoint(tempRect.x + tempRect.width, tempRect.y + tempRect.height - 12);
        list.addPoint(tempRect.x + tempRect.width - 12, tempRect.y + tempRect.height);
        list.addPoint(tempRect.x + 2, tempRect.y + tempRect.height);
        graphics.fillPolygon(list);
        
        list.removeAllPoints();
        list.addPoint(tempRect.x, tempRect.y);
        list.addPoint(tempRect.x + tempRect.width - 3, tempRect.y);
        list.addPoint(tempRect.x + tempRect.width - 3, tempRect.y + tempRect.height - 13);
        list.addPoint(tempRect.x + tempRect.width - 13, tempRect.y + tempRect.height - 3);
        list.addPoint(tempRect.x, tempRect.y + tempRect.height - 3);
        graphics.setAlpha(255);
        graphics.setBackgroundColor(getFillColor());
        graphics.fillPolygon(list);
    }
    
    @Override
    public IFigure getToolTip() {
        ToolTipFigure toolTipFigure = (ToolTipFigure)super.getToolTip();
        
        if(toolTipFigure != null) {
            toolTipFigure.setText("Note");
        }
        
        return toolTipFigure;
    }
}
