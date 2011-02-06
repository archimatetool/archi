/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.directedit;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

/**
 * A CellEditor Locator for a label
 * 
 * @author Phillip Beauvoir
 */
public class LabelCellEditorLocator implements CellEditorLocator {

    private Label fLabel;

    /**
     * Creates a new CellEditorLocator for the given Label
     * 
     * @param label the Label
     */
    public LabelCellEditorLocator(Label label) {
        fLabel = label;
    }

    /**
     * expands the size of the control by 1 pixel in each direction
     */
    public void relocate(CellEditor celleditor) {
        Text text = (Text)celleditor.getControl();
        Point preferredSize = text.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        Rectangle rect = fLabel.getTextBounds().getCopy();
        fLabel.translateToAbsolute(rect);
        if(text.getCharCount() > 1) {
            text.setBounds(rect.x - 1, rect.y - 1, preferredSize.x + 1, preferredSize.y + 1);
        }
        else {
            text.setBounds(rect.x - 1, rect.y - 1, preferredSize.y + 1, preferredSize.y + 1);
        }
    }

}