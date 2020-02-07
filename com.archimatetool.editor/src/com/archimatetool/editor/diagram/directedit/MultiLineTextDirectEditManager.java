package com.archimatetool.editor.diagram.directedit;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

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
        
        if(getEditPart().getModel() instanceof ITextAlignment) {
            int ta = ((ITextAlignment)getEditPart().getModel()).getTextAlignment();
            if(ta == ITextAlignment.TEXT_ALIGNMENT_CENTER) {
                alignment = SWT.CENTER;
            }
            else if(ta == ITextAlignment.TEXT_ALIGNMENT_RIGHT) {
                alignment = SWT.RIGHT;
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
        
        if(model instanceof ITextContent) {
            value = ((ITextContent)model).getContent();
        }
        else if(model instanceof INameable) {
            value = ((INameable)model).getName();
        }
        
        getCellEditor().setValue(StringUtils.safeString(value));

        if(isSingleText) {
            setNormalised();
            
            traverseListener = new TraverseListener() {
                @Override
                public void keyTraversed(TraverseEvent event) {
                    if(event.detail == SWT.TRAVERSE_RETURN || event.detail == SWT.TRAVERSE_TAB_PREVIOUS
                            || event.detail == SWT.TRAVERSE_TAB_NEXT) {
                        commit();
                    }
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
            Text text = getTextControl();
            
            Rectangle rect = referenceFigure.getBounds().getCopy();
            referenceFigure.translateToAbsolute(rect);
            
            // Connection Label
            if(referenceFigure.getParent() instanceof IDiagramConnectionFigure) {
                if(isSingleText) {
                    int trimWidth = text.computeTrim(0, 0, 0, 0).width;
                    rect.width += trimWidth;
                    if(rect.width < 100) {
                        rect.width = 100;
                    }
                    int height = text.computeSize(rect.width - trimWidth, SWT.DEFAULT).y;
                    text.setBounds(rect.x, rect.y, rect.width, height);
                }
                else {
                    Point preferredSize = text.computeSize(SWT.DEFAULT, SWT.DEFAULT);
                    text.setBounds(rect.x, rect.y, Math.max(150, rect.width), Math.max(60, preferredSize.y));
                }
            }
            // IDiagramModelObjectFigure
            else {
                rect.x += 5;

                // Single Text control
                if(isSingleText) {
                    int height = text.computeSize(rect.width - text.computeTrim(0, 0, 0, 0).width, SWT.DEFAULT).y;
                    
                    // Reference figure is not a Label so it's a figure box 
                    if(!(referenceFigure instanceof Label)) {
                        // Use height of the figure box or text edit control, whichever is the smallest
                        height = Math.min(height, rect.height);
                        
                        // Position the y at the same y position as the figure's text control
                        Rectangle textControlBounds = ((IDiagramModelObjectFigure)referenceFigure).getTextControl().getBounds().getCopy();
                        ((IDiagramModelObjectFigure)referenceFigure).getTextControl().translateToAbsolute(textControlBounds);
                        
                        rect.y = textControlBounds.y;
                    }
                    
                    rect.height = height;
                }
                // Multi Text control
                else {
                    rect.y += 5;
                }

                text.setBounds(rect.x, rect.y, rect.width, rect.height);
            }
        }
    }
    
    class MultiLineCellEditor extends TextCellEditor {
        public MultiLineCellEditor(Composite composite, int style) {
            super(composite, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | style);
        }
    }

}
