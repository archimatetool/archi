/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.directedit;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

/**
 * TextFlow CellEditorLocator
 * 
 * @author Phillip Beauvoir
 */
public class TextFlowCellEditorLocator implements CellEditorLocator {
    
    private TextFlow fTextFlow;
    
    public TextFlowCellEditorLocator(TextFlow textFlow) {
        fTextFlow = textFlow;
    }

    public void relocate(CellEditor celleditor) {
        Text text = (Text)celleditor.getControl();
        Point preferredSize = text.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        Rectangle rect = fTextFlow.getBounds().getCopy();
        fTextFlow.translateToAbsolute(rect);
        if(text.getCharCount() > 1) {
            text.setBounds(rect.x - 1, rect.y - 1, preferredSize.x + 1, preferredSize.y + 1);
        }
        else {
            text.setBounds(rect.x - 1, rect.y - 1, preferredSize.y + 1, preferredSize.y + 1);
        }
    }
}