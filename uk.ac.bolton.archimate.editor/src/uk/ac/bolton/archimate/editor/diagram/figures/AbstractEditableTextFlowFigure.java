/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.figures;

import org.eclipse.draw2d.DelegatingLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.BlockFlow;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.swt.graphics.Image;

import uk.ac.bolton.archimate.editor.diagram.util.AnimationUtil;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.IDiagramModelObject;
import uk.ac.bolton.archimate.model.IFontAttribute;


/**
 * Abstract Figure with Editable Label
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractEditableTextFlowFigure extends AbstractContainerFigure
implements IEditableTextFlowFigure {
    
    static Dimension DEFAULT_SIZE = new Dimension(120, 55);

    private TextFlow fTextFlow;
    
    protected int TEXT_PADDING = 10;
    
    public AbstractEditableTextFlowFigure(IDiagramModelObject diagramModelObject) {
        super(diagramModelObject);
    }
    
    @Override
    protected void setUI() {
        setLayoutManager(new DelegatingLayout());
        
        Locator locator = new Locator() {
            public void relocate(IFigure target) {
                target.setBounds(calculateTextControlBounds());
            }
        };

        FlowPage page = new FlowPage();
        BlockFlow block = new BlockFlow();
        fTextFlow = new TextFlow();
        fTextFlow.setLayoutManager(new ParagraphTextLayout(fTextFlow, ParagraphTextLayout.WORD_WRAP_TRUNCATE));
        block.add(fTextFlow);
        page.add(block);
        add(page, locator);
        
        Locator locator2 = new Locator() {
            public void relocate(IFigure target) {
                Rectangle bounds = getBounds().getCopy();
                bounds.x = 0;
                bounds.y = 0;
                target.setBounds(bounds);
            }
        };
        
        add(getMainFigure(), locator2);

        // Have to add this if we want Animation to work on figures!
        AnimationUtil.addFigureForAnimation(getMainFigure());
    }
    
    public void refreshVisuals() {
        // Text
        setText();
        
        // Tooltip
        setToolTip();
        
        // Font
        setFont();
        
        // Fill Color
        setFillColor();
        
        // Font Color
        setFontColor();
        
        // Alignment default is CENTER
        int alignment = getDiagramModelObject().getTextAlignment();
        if(alignment == IFontAttribute.TEXT_ALIGNMENT_NONE) {
            alignment = IFontAttribute.TEXT_ALIGNMENT_CENTER;
        }
        ((BlockFlow)getTextControl().getParent()).setHorizontalAligment(alignment);
    }
    
    protected void setText() {
        String text = StringUtils.safeString(getDiagramModelObject().getName());
        getTextControl().setText(StringUtils.safeString(text));
    }
    
    public TextFlow getTextControl() {
        return fTextFlow;
    }

    public boolean didClickTextControl(Point requestLoc) {
        TextFlow textControl = getTextControl();
        textControl.translateToRelative(requestLoc);
        return textControl.containsPoint(requestLoc);
    }
    
    /**
     * @return The Image to display
     */
    protected abstract Image getImage();
    
    @Override
    public Dimension getDefaultSize() {
        return DEFAULT_SIZE;
    }
}