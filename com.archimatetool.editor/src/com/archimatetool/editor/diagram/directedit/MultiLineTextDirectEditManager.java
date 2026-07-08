package com.archimatetool.editor.diagram.directedit;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.connections.IDiagramConnectionFigure;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.INameable;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextContent;



/**
 * Multiline Text Direct Edit Manager
 * 
 * @author Phillip Beauvoir
 */
public class MultiLineTextDirectEditManager extends AbstractDirectEditManager {
    
    private TraverseListener traverseListener;
    
    private boolean isSingleText;
    private IFigure referenceFigure;
    
    public MultiLineTextDirectEditManager(GraphicalEditPart source) {
        this(source, false);
    }
    
    /**
     * @param isSingleText if true only single line text is supported. i.e newlines are stripped
     */
    public MultiLineTextDirectEditManager(GraphicalEditPart source, boolean isSingleText) {
        this(source, isSingleText, source.getFigure());
    }

    public MultiLineTextDirectEditManager(GraphicalEditPart source, boolean isSingleText, IFigure referenceFigure) {
        super(source, MultiLineCellEditor.class, null);
        this.isSingleText = isSingleText;
        this.referenceFigure = referenceFigure;
        setLocator(new MultiLineCellEditorLocator());
    }

    @Override
    protected CellEditor createCellEditorOn(Composite composite) {
        int alignment = SWT.LEFT;
        
        if(getEditPart().getModel() instanceof ITextAlignment textAlignment) {
            switch(textAlignment.getTextAlignment()) {
                case ITextAlignment.TEXT_ALIGNMENT_CENTER -> alignment = SWT.CENTER;
                case ITextAlignment.TEXT_ALIGNMENT_RIGHT -> alignment = SWT.RIGHT;
            }
        }
        
        return new MultiLineCellEditor(composite, alignment);
    }

    @Override
    protected void initCellEditor() {
        super.initCellEditor();
        
        // Font
        getTextControl().setFont(referenceFigure.getFont());
        
        Object model = getEditPart().getModel();
        String value = ""; //$NON-NLS-1$
        
        if(model instanceof ITextContent textContent) {
            value = textContent.getContent();
        }
        else if(model instanceof INameable nameable) {
            value = nameable.getName();
        }
        
        getCellEditor().setValue(StringUtils.safeString(value));

        if(isSingleText) {
            setNormalised();
            
            traverseListener = event -> {
                if(event.detail == SWT.TRAVERSE_RETURN || event.detail == SWT.TRAVERSE_TAB_PREVIOUS || event.detail == SWT.TRAVERSE_TAB_NEXT) {
                    commit();
                }
            };

            getTextControl().addTraverseListener(traverseListener);
        }
    }

    /**
     * Need to override so as to remove the verify listener
     */
    @Override
    protected void unhookListeners() {
        super.unhookListeners();
        
        if(traverseListener != null) {
            getTextControl().removeTraverseListener(traverseListener);
            traverseListener = null;
        }
    }

    private class MultiLineCellEditorLocator implements CellEditorLocator {
        @Override
        public void relocate(CellEditor celleditor) {
            final Text text = getTextControl();
            
            Rectangle rect = referenceFigure.getBounds().getCopy();
            referenceFigure.translateToAbsolute(rect); // rect will be scaled to display scale on Windows
            
            // Connection
            if(getEditPart().getFigure() instanceof IDiagramConnectionFigure) {
                if(isSingleText) {
                    rect.width = Math.max(getScaledValue(100), Math.min(getScaledValue(600), rect.width));  // minimum width 100, max width 600, or width
                    rect.height = text.computeSize(rect.width - getTrimWidth(), SWT.DEFAULT).y;
                }
                else {
                    Point preferredSize = text.computeSize(SWT.DEFAULT, SWT.DEFAULT);
                    rect.width = Math.max(getScaledValue(150), rect.width);        // minimum of 150 width
                    rect.height = Math.max(getScaledValue(60), preferredSize.y);   // minimum of 60 height
                }
            }
            // Object figure
            else if(referenceFigure instanceof IDiagramModelObjectFigure dmoFigure) {
                rect.x += 5;

                if(isSingleText) {
                    int height = text.computeSize(rect.width - getTrimWidth(), SWT.DEFAULT).y;
                    // Use height of the figure box or text edit control, whichever is the smallest
                    rect.height = Math.min(height, rect.height);

                    // Position the y at the same y position as the figure's text control
                    Rectangle textControlBounds = dmoFigure.getTextControl().getBounds().getCopy();
                    dmoFigure.getTextControl().translateToAbsolute(textControlBounds);
                    rect.y = textControlBounds.y;
                }
                else {
                    rect.y += 5;
                }
            }
            // Label
            else if(referenceFigure instanceof Label) {
                rect.x += 5;
                
                if(isSingleText) {
                    rect.height = text.computeSize(rect.width - getTrimWidth(), SWT.DEFAULT).y;
                }
                else {
                    rect.y += 5;
                }
            }
            
            text.setBounds(rect.x, rect.y, rect.width, rect.height);
        }
        
        /**
         * @return The width of the text control's trim (vertical scroll bar) scaled as needed
         */
        private int getTrimWidth() {
            return getScaledValue(getTextControl().computeTrim(0, 0, 0, 0).width);
        }
        
        /**
         * On Windows calling Figure#translateToAbsolute(Rectangle) will also scale the width and height
         * of the Rectangle by the display scale so all other values need to be scaled accordingly.
         * 
         * @return the value that has been scaled according to display scaling
         */
        private int getScaledValue(int value) {
            // This scales by display scale and diagram zoom but we only want the display scale for the text control
            // Dimension d = new Dimension(value, 0);
            // referenceFigure.translateToAbsolute(d);
            // return d.width();
            
            return Math.round(value * FigureUtils.getDisplayScale());
        }
    }
    
    class MultiLineCellEditor extends TextCellEditor {
        public MultiLineCellEditor(Composite composite, int style) {
            super(composite, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | style);
        }
    }

}
