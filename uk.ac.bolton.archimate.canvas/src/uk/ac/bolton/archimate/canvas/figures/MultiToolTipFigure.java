/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.canvas.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.text.BlockFlow;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.draw2d.text.TextFlow;

/**
 * Tooltip Figure
 * 
 * @author Phillip Beauvoir
 */
public class MultiToolTipFigure extends Figure {
    
    private TextFlow fTextFlow;

    public MultiToolTipFigure() {
        setBorder(new MarginBorder(3));
        
        ToolbarLayout layout = new ToolbarLayout();
        setLayoutManager(layout);
        
        FlowPage page = new FlowPage() {
            @Override
            public Dimension getPreferredSize(int wHint, int hHint) {
                Dimension d = FigureUtilities.getTextExtents(fTextFlow.getText(), fTextFlow.getFont());
                if(d.width > 400) {
                    d.width = 400;
                }
                return d;
            }
        };
        
        BlockFlow block = new BlockFlow();
        fTextFlow = new TextFlow();
        fTextFlow.setLayoutManager(new ParagraphTextLayout(fTextFlow, ParagraphTextLayout.WORD_WRAP_HARD));
        block.add(fTextFlow);
        page.add(block);
        add(page);
    }
    
    public MultiToolTipFigure(String text) {
        this();
        setText(text);
    }

    public void setText(String text) {
        fTextFlow.setText(text);
    }
}