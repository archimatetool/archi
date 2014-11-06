/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.ToolbarLayout;
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
    
    private TextFlow fTextFlow;
    
    public NoteFigure(IDiagramModelNote diagramModelNote) {
        super(diagramModelNote);
    }
    
    @Override
    public IDiagramModelNote getDiagramModelObject() {
        return (IDiagramModelNote)super.getDiagramModelObject();
    }
    
    @Override
    protected void setUI() {
        setBorder(new LineBorder() {
            @Override
            public void paint(IFigure figure, Graphics graphics, Insets insets) {
                if(getDiagramModelObject().getBorderType() == IDiagramModelNote.BORDER_NONE) {
                    return; 
                }
                
                tempRect.setBounds(getPaintRectangle(figure, insets));
                if(getWidth() % 2 == 1) {
                    tempRect.width--;
                    tempRect.height--;
                }
                tempRect.shrink(getWidth() / 2, getWidth() / 2);
                graphics.setLineWidth(getWidth());
                
                graphics.setForegroundColor(getLineColor());

                boolean drawShadows = Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_SHADOWS);
                int shadow_offset = drawShadows ? 2 : 0;
                
                PointList list = new PointList();
                
                if(getDiagramModelObject().getBorderType() == IDiagramModelNote.BORDER_DOGEAR) {
                    list.addPoint(tempRect.x, tempRect.y);
                    list.addPoint(tempRect.x + tempRect.width - shadow_offset, tempRect.y);
                    list.addPoint(tempRect.x + tempRect.width - shadow_offset, tempRect.y + tempRect.height - 12);
                    list.addPoint(tempRect.x + tempRect.width - 12, tempRect.y + tempRect.height - shadow_offset);
                    list.addPoint(tempRect.x, tempRect.y + tempRect.height - shadow_offset);
                }
                else if(getDiagramModelObject().getBorderType() == IDiagramModelNote.BORDER_RECTANGLE) {
                    list.addPoint(tempRect.x, tempRect.y);
                    list.addPoint(tempRect.x + tempRect.width - shadow_offset, tempRect.y);
                    list.addPoint(tempRect.x + tempRect.width - shadow_offset, tempRect.y + tempRect.height - shadow_offset);
                    list.addPoint(tempRect.x, tempRect.y + tempRect.height - shadow_offset);
                }
                
                graphics.drawPolygon(list);
            }
        });
        
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
        setText(getDiagramModelObject().getContent());
        
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
        
        // Repaint for border
        repaint();
    }
    
    public void setText(String text) {
        fTextFlow.setText(StringUtils.safeString(text));
    }

    @Override
    public IFigure getTextControl() {
        return fTextFlow;
    }

    @Override
    protected void paintFigure(Graphics graphics) {
        graphics.setAntialias(SWT.ON);
        
        boolean drawShadows = Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_SHADOWS);
        int shadow_offset = drawShadows ? 3 : 0;
        
        Rectangle tempRect = getBounds().getCopy();
        
        // Shadows
        if(drawShadows) {
            graphics.setAlpha(100);
            graphics.setBackgroundColor(ColorConstants.black);
            PointList shadowPoints = new PointList();
            
            if(getDiagramModelObject().getBorderType() == IDiagramModelNote.BORDER_DOGEAR) {
                shadowPoints.addPoint(tempRect.x + 2, tempRect.y + 2);
                shadowPoints.addPoint(tempRect.x + tempRect.width, tempRect.y + 2);
                shadowPoints.addPoint(tempRect.x + tempRect.width, tempRect.y + tempRect.height - 12);
                shadowPoints.addPoint(tempRect.x + tempRect.width - 12, tempRect.y + tempRect.height);
                shadowPoints.addPoint(tempRect.x + 2, tempRect.y + tempRect.height);
            }
            else if(getDiagramModelObject().getBorderType() == IDiagramModelNote.BORDER_RECTANGLE) {
                shadowPoints.addPoint(tempRect.x + 2, tempRect.y + 2);
                shadowPoints.addPoint(tempRect.x + tempRect.width, tempRect.y + 2);
                shadowPoints.addPoint(tempRect.x + tempRect.width, tempRect.y + tempRect.height);
                shadowPoints.addPoint(tempRect.x + 2, tempRect.y + tempRect.height);
            }

            graphics.fillPolygon(shadowPoints);
        }
        
        // Fill
        PointList fillPoints = new PointList();
        
        if(getDiagramModelObject().getBorderType() == IDiagramModelNote.BORDER_DOGEAR) {
            fillPoints.addPoint(tempRect.x, tempRect.y);
            fillPoints.addPoint(tempRect.x + tempRect.width - shadow_offset, tempRect.y);
            fillPoints.addPoint(tempRect.x + tempRect.width - shadow_offset, tempRect.y + tempRect.height - 13);
            fillPoints.addPoint(tempRect.x + tempRect.width - 13, tempRect.y + tempRect.height - shadow_offset);
            fillPoints.addPoint(tempRect.x, tempRect.y + tempRect.height - shadow_offset);
        }
        else {
            fillPoints.addPoint(tempRect.x, tempRect.y);
            fillPoints.addPoint(tempRect.x + tempRect.width - shadow_offset, tempRect.y);
            fillPoints.addPoint(tempRect.x + tempRect.width - shadow_offset, tempRect.y + tempRect.height - shadow_offset);
            fillPoints.addPoint(tempRect.x, tempRect.y + tempRect.height - shadow_offset);
        }
        
        graphics.setAlpha(255);
        graphics.setBackgroundColor(getFillColor());
        graphics.fillPolygon(fillPoints);
    }
}
