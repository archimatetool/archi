/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.directedit;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
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



/**
 * A DirectEdit manager to be used for labels that grow in size as you type in them.
 * The Text control is a single-line Text control.
 * 
 * @author Phillip Beauvoir
 */
public class LabelDirectEditManager extends AbstractDirectEditManager {

    private VerifyListener verifyListener;
    private IFigure textFigure;
    private String initialText;

    public LabelDirectEditManager(GraphicalEditPart source, IFigure textFigure, String initialText) {
        super(source, TextCellEditor.class, null);
        this.textFigure = textFigure;
        this.initialText = initialText;
        setLocator(new TextCellEditorLocator());
    }

    /**
     * @see org.eclipse.gef.tools.DirectEditManager#initCellEditor()
     */
    @Override
    protected void initCellEditor() {
        super.initCellEditor();
        
        // Single text control strips CRLFs
        setNormalised();
        
        /**
         * Changes the size of the editor control to reflect the changed text
         */
        verifyListener = new VerifyListener() {
            @Override
            public void verifyText(VerifyEvent event) {
                String oldText = getTextControl().getText();
                String leftText = oldText.substring(0, event.start);
                String rightText = oldText.substring(event.end, oldText.length());
                
                GC gc = new GC(getTextControl());
                Point size = gc.textExtent(leftText + event.text + rightText);
                gc.dispose();
                
                if(size.x != 0) {
                    size = getTextControl().computeSize(size.x, SWT.DEFAULT);
                }
                else {
                    // just make it square
                    size.x = size.y;
                }
                
                getCellEditor().getControl().setSize(size.x, size.y);
            }

        };
        
        getTextControl().addVerifyListener(verifyListener);

        // set the initial value of the text
        getCellEditor().setValue(initialText);

        IFigure figure = (getEditPart()).getFigure();
        getTextControl().setFont(figure.getFont());
    }

    /**
     * Need to override so as to remove the verify listener
     */
    @Override
    protected void unhookListeners() {
        super.unhookListeners();
        
        getTextControl().removeVerifyListener(verifyListener);
        verifyListener = null;
    }
    
    
    class TextCellEditorLocator implements CellEditorLocator {
        @Override
        public void relocate(CellEditor celleditor) {
            Text text = getTextControl();
            
            Point preferredSize = text.computeSize(SWT.DEFAULT, SWT.DEFAULT);
            
            Rectangle rect;
            
            if(textFigure instanceof Label) {
                rect = ((Label)textFigure).getTextBounds().getCopy();
            }
            else {
                rect = textFigure.getBounds().getCopy();
            }
            
            textFigure.translateToAbsolute(rect);
            
            if(text.getCharCount() > 1) {
                text.setBounds(rect.x - 1, rect.y - 1, preferredSize.x + 1, preferredSize.y + 1);
            }
            else {
                text.setBounds(rect.x - 1, rect.y - 1, preferredSize.y + 1, preferredSize.y + 1);
            }
        }
    }
}