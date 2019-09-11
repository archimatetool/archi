/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;

import com.archimatetool.editor.ui.FontFactory;


/**
 * Tooltip Figure
 * 
 * @author Phillip Beauvoir
 */
public class ToolTipFigure extends Figure {
    private Label l1, l2, l3;

    public ToolTipFigure() {
        setBorder(new MarginBorder(3));
        ToolbarLayout layout = new ToolbarLayout();
        layout.setStretchMinorAxis(false);
        setLayoutManager(layout);
        l1 = new Label();
        add(l1);
    }
    
    public ToolTipFigure(String text) {
        this();
        setText(text);
    }

    public void setText(String text) {
        l1.setText(text);
    }

    @Override
    public Dimension getPreferredSize(int wHint, int hHint) {
        // Not too wide
        return super.getPreferredSize(300, hHint);
    }
    
    public void setType(String type) {
        if(l2 == null) {
            l1.setFont(FontFactory.SystemFontBold); // this becomes bold if we are a two-liner
            l2 = new Label();
            add(l2);
        }
        l2.setText(type);
    }
    
    public void setRubric(String text) {
        if(l3 == null) {
            l3 = new Label();
            l3.setFont(FontFactory.SystemFontItalic);
            add(l3);
        }
        l3.setText(text);
    }
}