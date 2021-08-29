/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures.diagram;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Path;
import org.eclipse.swt.graphics.Pattern;

import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.ITextFigure;
import com.archimatetool.editor.diagram.figures.IconicDelegate;
import com.archimatetool.editor.diagram.figures.TextPositionDelegate;
import com.archimatetool.editor.ui.textrender.TextRenderer;
import com.archimatetool.model.IDiagramModelNote;


/**
 * Note Figure
 * 
 * @author Phillip Beauvoir
 */
public class NoteFigure extends AbstractDiagramModelObjectFigure implements ITextFigure {
    
    private TextFlow fTextFlow;
    
    private TextPositionDelegate fTextPositionDelegate;
    
    public NoteFigure(IDiagramModelNote diagramModelNote) {
        super(diagramModelNote);
    }
    
    @Override
    public IDiagramModelNote getDiagramModelObject() {
        return (IDiagramModelNote)super.getDiagramModelObject();
    }
    
    @Override
    protected void setUI() {
        setLayoutManager(new GridLayout());

        FlowPage page = new FlowPage();
        fTextFlow = new TextFlow();
        fTextFlow.setLayoutManager(new ParagraphTextLayout(fTextFlow, ParagraphTextLayout.WORD_WRAP_SOFT));
        page.add(fTextFlow);
        setOpaque(true);
        
        GridData gd = new GridData(SWT.LEFT, SWT.TOP, true, true);
        add(page, gd);
        
        fTextPositionDelegate = new TextPositionDelegate(this, page, getDiagramModelObject());
        
        setIconicDelegate(new IconicDelegate(getDiagramModelObject()));
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
        
        // Line Color
        setLineColor();

        // Alignment
        ((FlowPage)fTextFlow.getParent()).setHorizontalAligment(getDiagramModelObject().getTextAlignment());
        fTextPositionDelegate.updateTextPosition();

        // Icon Image
        updateIconImage();
        
        // Repaint
        repaint();
    }
    
    @Override
    public void setText() {
        String text = TextRenderer.getDefault().render(getDiagramModelObject(), getDiagramModelObject().getContent());
        fTextFlow.setText(text);
    }

    @Override
    public IFigure getTextControl() {
        return fTextFlow;
    }

    @Override
    protected void paintFigure(Graphics graphics) {
        graphics.pushState();
        
        Rectangle bounds = getBounds().getCopy();
        
        bounds.width--;
        bounds.height--;
        
        // Set line width here so that the whole figure is constrained, otherwise SVG graphics will have overspill
        if(getDiagramModelObject().getBorderType() != IDiagramModelNote.BORDER_NONE) {
            setLineWidth(graphics, 1, bounds);
        }
        
        // Fill
        PointList points = new PointList();
        
        if(getDiagramModelObject().getBorderType() == IDiagramModelNote.BORDER_DOGEAR) {
            points.addPoint(bounds.x, bounds.y);
            points.addPoint(bounds.getTopRight().x, bounds.y);
            points.addPoint(bounds.getTopRight().x, bounds.getBottomRight().y - 13);
            points.addPoint(bounds.getTopRight().x - 13, bounds.getBottomRight().y);
            points.addPoint(bounds.x, bounds.getBottomLeft().y);
        }
        else {
            points.addPoint(bounds.x, bounds.y);
            points.addPoint(bounds.getTopRight().x, bounds.y);
            points.addPoint(bounds.getTopRight().x, bounds.getBottomRight().y);
            points.addPoint(bounds.x, bounds.getBottomLeft().y);
        }
        
        graphics.setAlpha(getAlpha());
        
        graphics.setBackgroundColor(getFillColor());
        
        Pattern gradient = applyGradientPattern(graphics, bounds);
        
        Path path = FigureUtils.createPathFromPoints(points);
        graphics.fillPath(path);
        path.dispose();
        
        disposeGradientPattern(graphics, gradient);

        // Icon
        drawIconImage(graphics, bounds);

        if(getDiagramModelObject().getBorderType() != IDiagramModelNote.BORDER_NONE) {
            graphics.setAlpha(getLineAlpha());
            graphics.setForegroundColor(getLineColor());
            graphics.drawPolygon(points);
        }
        
        graphics.popState();
    }
}
