/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.tools;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.tools.AbstractTool;

import com.archimatetool.editor.diagram.commands.BorderColorCommand;
import com.archimatetool.editor.diagram.commands.FillColorCommand;
import com.archimatetool.editor.diagram.commands.FontColorCommand;
import com.archimatetool.editor.diagram.commands.FontStyleCommand;
import com.archimatetool.editor.diagram.commands.LineColorCommand;
import com.archimatetool.editor.diagram.commands.LineWidthCommand;
import com.archimatetool.editor.diagram.commands.TextAlignmentCommand;
import com.archimatetool.editor.diagram.commands.TextPositionCommand;
import com.archimatetool.editor.diagram.tools.FormatPainterInfo.PaintFormat;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IBorderObject;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IFontAttribute;
import com.archimatetool.model.IJunction;
import com.archimatetool.model.ILineObject;
import com.archimatetool.model.ILockable;



/**
 * Format Painter Tool
 * 
 * @author Phillip Beauvoir
 */
public class FormatPainterTool extends AbstractTool {
    
    public FormatPainterTool() {
        setDefaultCursor(FormatPainterInfo.INSTANCE.getCursor());
    }
    
    @Override
    protected boolean handleButtonUp(int button) {
        if(button == 1) {
            Point pt = getLocation();
            EditPart editpart = getCurrentViewer().findObjectAt(pt);
            if(editpart != null && editpart.getModel() != null) {
                Object object = editpart.getModel();
                if(isPaintableObject(object)) {
                    PaintFormat pf = FormatPainterInfo.INSTANCE.getPaintFormat();
                    if(pf == null) {
                        FormatPainterInfo.INSTANCE.updatePaintFormat((IDiagramModelComponent)object);
                    }
                    else if(!isObjectLocked(object)) {
                        Command cmd = createCommand(pf, (IDiagramModelComponent)object);
                        if(cmd.canExecute()) {
                            executeCommand(cmd);
                        }
                    }
                }

                return true;
            }
        }
        
        return false;
    }
    
    @Override
    protected boolean handleDoubleClick(int button) {
        /*
         * Double-click on canvas clears it
         */
        if(button == 1) {
            Point pt = getLocation();
            EditPart editpart = getCurrentViewer().findObjectAt(pt);
            if(editpart == getCurrentViewer().getRootEditPart()) {
                FormatPainterInfo.INSTANCE.reset();
                return true;
            }
            else return handleButtonUp(1);
        }
        return false;
    }
    
    protected CompoundCommand createCommand(PaintFormat pf, IDiagramModelComponent targetComponent) {
        CompoundCommand result = new CompoundCommand(Messages.FormatPainterTool_0);
        
        // IFontAttribute
        if(pf.getSourceComponent() instanceof IFontAttribute && targetComponent instanceof IFontAttribute) {
            IFontAttribute source = (IFontAttribute)pf.getSourceComponent();
            IFontAttribute target = (IFontAttribute)targetComponent;
            
            Command cmd = new FontStyleCommand(target, source.getFont());
            if(cmd.canExecute()) {
                result.add(cmd);
            }
            
            cmd = new FontColorCommand(target, source.getFontColor());
            if(cmd.canExecute()) {
                result.add(cmd);
            }
        }
        
        // ILineObject
        if(pf.getSourceComponent() instanceof ILineObject && targetComponent instanceof ILineObject) {
            ILineObject source = (ILineObject)pf.getSourceComponent();
            ILineObject target = (ILineObject)targetComponent;
            
            Command cmd = new LineColorCommand(target, source.getLineColor());
            if(cmd.canExecute()) {
                result.add(cmd);
            }
            cmd = new LineWidthCommand(target, source.getLineWidth());
            if(cmd.canExecute()) {
                result.add(cmd);
            }
        }

        // IBorderObject
        if(pf.getSourceComponent() instanceof IBorderObject && targetComponent instanceof IBorderObject) {
            Command cmd = new BorderColorCommand((IBorderObject)pf.getSourceComponent(), ((IBorderObject)targetComponent).getBorderColor());
            if(cmd.canExecute()) {
                result.add(cmd);
            }
        }
        
        // IDiagramModelObject
        if(pf.getSourceComponent() instanceof IDiagramModelObject && targetComponent instanceof IDiagramModelObject) {
            IDiagramModelObject source = (IDiagramModelObject)pf.getSourceComponent();
            IDiagramModelObject target = (IDiagramModelObject)targetComponent;
            
            // Source fill colour is null which is "default"
            String fillColorString = source.getFillColor();
            if(fillColorString == null) {
                fillColorString = ColorFactory.convertColorToString(ColorFactory.getDefaultFillColor(source));
            }
            
            Command cmd = new FillColorCommand(target, fillColorString);
            if(cmd.canExecute()) {
                result.add(cmd);
            }
            cmd = new TextAlignmentCommand(target, source.getTextAlignment());
            if(cmd.canExecute()) {
                result.add(cmd);
            }
            cmd = new TextPositionCommand(target, source.getTextPosition());
            if(cmd.canExecute()) {
                result.add(cmd);
            }
        }
        
        return result;
    }
    
    protected boolean isObjectLocked(Object object) {
        return object instanceof ILockable && ((ILockable)object).isLocked();
    }

    protected boolean isPaintableObject(Object object) {
        // Junctions are a no-no
        if(object instanceof IDiagramModelArchimateObject) {
            IArchimateElement element = ((IDiagramModelArchimateObject)object).getArchimateElement();
            return !(element instanceof IJunction);
        }
        
        return object instanceof IDiagramModelObject || object instanceof IDiagramModelConnection;
    }
    
    @Override
    protected String getCommandName() {
        return "FormatPaint"; //$NON-NLS-1$
    }

}