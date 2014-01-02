package com.archimatetool.editor.diagram.directedit;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IFontAttribute;
import com.archimatetool.model.ITextContent;



/**
 * Multiline Text Direct Edit Manager
 * 
 * @author Phillip Beauvoir
 */
public class MultiLineTextDirectEditManager extends AbstractDirectEditManager {
    
    public MultiLineTextDirectEditManager(GraphicalEditPart source) {
        super(source, MultiLineCellEditor.class, null);
        setLocator(new MultiLineCellEditorLocator());
    }

    @Override
    protected CellEditor createCellEditorOn(Composite composite) {
        Object model = getEditPart().getModel();
        
        int alignment = SWT.CENTER;
        
        if(model instanceof IFontAttribute) {
            alignment = ((IFontAttribute)model).getTextAlignment();
            if(alignment == IFontAttribute.TEXT_ALIGNMENT_CENTER) {
                alignment = SWT.CENTER;
            }
            else if(alignment == IFontAttribute.TEXT_ALIGNMENT_RIGHT) {
                alignment = SWT.RIGHT;
            }
            else {
                alignment = SWT.LEFT;
            }
        }
        
        return new MultiLineCellEditor(composite, alignment);
    }

    @Override
    protected void initCellEditor() {
        super.initCellEditor();
        
        IFigure figure = getEditPart().getFigure();
        Object model = getEditPart().getModel();
        
        if(model instanceof ITextContent) {
            String value = ((ITextContent)model).getContent();
            getCellEditor().setValue(StringUtils.safeString(value));
        }

        Text text = (Text)getCellEditor().getControl();
        text.setFont(figure.getFont());
        //text.setForeground(figure.getTextControl().getForegroundColor());
    }

    /**
     * CellEditorLocator
     */
    class MultiLineCellEditorLocator implements CellEditorLocator {
        public void relocate(CellEditor celleditor) {
            IFigure figure = getEditPart().getFigure();
            Text text = (Text)celleditor.getControl();
            Rectangle rect = figure.getBounds().getCopy();
            figure.translateToAbsolute(rect);
            text.setBounds(rect.x + 5, rect.y + 5, rect.width, rect.height);
        }
    }
    
    class MultiLineCellEditor extends TextCellEditor {
        public MultiLineCellEditor(Composite composite, int style) {
            super(composite, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | style);
        }
    }

}
