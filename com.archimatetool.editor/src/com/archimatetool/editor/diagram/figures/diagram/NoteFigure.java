/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.BlockFlow;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IDiagramModelNote;


/**
 * Note Figure
 * 
 * @author Phillip Beauvoir
 */
public class NoteFigure extends AbstractDiagramModelObjectFigure {
    
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
        ToolbarLayout layout = new ToolbarLayout();
        setLayoutManager(layout);

        FlowPage page = new FlowPage();
        page.setBorder(new MarginBorder(4));
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
        graphics.pushState();
        
        Rectangle bounds = getBounds().getCopy();
        
        // Fill
        PointList points = new PointList();
        
        if(getDiagramModelObject().getBorderType() == IDiagramModelNote.BORDER_DOGEAR) {
            points.addPoint(bounds.x, bounds.y);
            points.addPoint(bounds.getTopRight().x - 1, bounds.y);
            points.addPoint(bounds.getTopRight().x - 1, bounds.getBottomRight().y - 13);
            points.addPoint(bounds.getTopRight().x - 13, bounds.getBottomRight().y - 1);
            points.addPoint(bounds.x, bounds.getBottomLeft().y - 1);
        }
        else {
            points.addPoint(bounds.x, bounds.y);
            points.addPoint(bounds.getTopRight().x - 1, bounds.y);
            points.addPoint(bounds.getTopRight().x - 1, bounds.getBottomRight().y - 1);
            points.addPoint(bounds.x, bounds.getBottomLeft().y - 1);
        }
        
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = null;
        if(Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_GRADIENT)) {
            gradient = FigureUtils.createGradient(graphics, bounds, getFillColor());
            graphics.setBackgroundPattern(gradient);
        }
        
        graphics.fillPolygon(points);
        
        if(gradient != null) {
            gradient.dispose();
        }

        if(getDiagramModelObject().getBorderType() != IDiagramModelNote.BORDER_NONE) {
            graphics.setForegroundColor(getLineColor());
            graphics.drawPolygon(points);
        }
        
        graphics.popState();
    }
}
