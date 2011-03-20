/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.directedit;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

/**
 * A DirectEdit manager to be used for labels that grow in size as you type in them.
 * The Text control is a single-line Text control.
 */
public class LabelDirectEditManager extends DirectEditManager {

    // If true use original font
    private boolean USE_ORIGINAL_FONT = false;
    
    private VerifyListener fVerifyListener;
    private IFigure fOriginalFigure;

    public LabelDirectEditManager(GraphicalEditPart source, CellEditorLocator locator, IFigure originalFigure) {
        super(source, TextCellEditor.class, locator);
        fOriginalFigure = originalFigure;
    }

    /**
     * @see org.eclipse.gef.tools.DirectEditManager#initCellEditor()
     */
    @Override
    protected void initCellEditor() {
        Text text = (Text)getCellEditor().getControl();

        /**
         * Changes the size of the editor control to reflect the changed text
         */
        fVerifyListener = new VerifyListener() {
            public void verifyText(VerifyEvent event) {
                Text text = (Text)getCellEditor().getControl();
                String oldText = text.getText();
                String leftText = oldText.substring(0, event.start);
                String rightText = oldText.substring(event.end, oldText.length());
                GC gc = new GC(text);
                if(leftText == null) {
                    leftText = ""; //$NON-NLS-1$
                }
                if(rightText == null) {
                    rightText = ""; //$NON-NLS-1$
                }

                Point size = gc.textExtent(leftText + event.text + rightText);

                gc.dispose();
                if(size.x != 0) {
                    size = text.computeSize(size.x, SWT.DEFAULT);
                }
                else {
                    // just make it square
                    size.x = size.y;
                }
                getCellEditor().getControl().setSize(size.x, size.y);
            }

        };
        text.addVerifyListener(fVerifyListener);

        // set the initial value of the text
        if(fOriginalFigure instanceof Label) {
            getCellEditor().setValue(((Label)fOriginalFigure).getText());
        }
        else if(fOriginalFigure instanceof TextFlow) {
            getCellEditor().setValue(((TextFlow)fOriginalFigure).getText());
        }

        if(USE_ORIGINAL_FONT) {
            IFigure figure = (getEditPart()).getFigure();
            text.setFont(figure.getFont());
        }
    }

    /**
     * Need to override so as to remove the verify listener
     */
    @Override
    protected void unhookListeners() {
        super.unhookListeners();
        Text text = (Text)getCellEditor().getControl();
        text.removeVerifyListener(fVerifyListener);
        fVerifyListener = null;
    }
}