/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.directedit;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.TextFlow;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

import uk.ac.bolton.archimate.editor.ui.UIUtils;


/**
 * A DirectEdit manager to be used for labels that grow in size as you type in them.
 * The Text control is a single-line Text control.
 * 
 * @author Phillip Beauvoir
 */
public class LabelDirectEditManager extends AbstractDirectEditManager {

    // If true use original font
    private boolean USE_ORIGINAL_FONT = false;
    
    private VerifyListener fVerifyListener;
    private IFigure fTextFigure;

    public LabelDirectEditManager(GraphicalEditPart source, IFigure textFigure) {
        super(source, TextCellEditor.class, null);
        fTextFigure = textFigure;
        setLocator(new TextCellEditorLocator());
    }

    /**
     * @see org.eclipse.gef.tools.DirectEditManager#initCellEditor()
     */
    @Override
    protected void initCellEditor() {
        super.initCellEditor();
        
        final Text text = (Text)getCellEditor().getControl();
        // Single text control strips CRLFs
        UIUtils.conformSingleTextControl(text);

        /**
         * Changes the size of the editor control to reflect the changed text
         */
        fVerifyListener = new VerifyListener() {
            public void verifyText(VerifyEvent event) {
                String oldText = text.getText();
                String leftText = oldText.substring(0, event.start);
                String rightText = oldText.substring(event.end, oldText.length());
                
                GC gc = new GC(text);
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
        if(fTextFigure instanceof Label) {
            getCellEditor().setValue(((Label)fTextFigure).getText());
        }
        else if(fTextFigure instanceof TextFlow) {
            getCellEditor().setValue(((TextFlow)fTextFigure).getText());
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
    
    
    class TextCellEditorLocator implements CellEditorLocator {
        public void relocate(CellEditor celleditor) {
            Text text = (Text)celleditor.getControl();
            
            Point preferredSize = text.computeSize(SWT.DEFAULT, SWT.DEFAULT);
            
            Rectangle rect;
            
            if(fTextFigure instanceof Label) {
                rect = ((Label)fTextFigure).getTextBounds().getCopy();
            }
            else {
                rect = fTextFigure.getBounds().getCopy();
            }
            
            fTextFigure.translateToAbsolute(rect);
            
            if(text.getCharCount() > 1) {
                text.setBounds(rect.x - 1, rect.y - 1, preferredSize.x + 1, preferredSize.y + 1);
            }
            else {
                text.setBounds(rect.x - 1, rect.y - 1, preferredSize.y + 1, preferredSize.y + 1);
            }
        }
    }
}