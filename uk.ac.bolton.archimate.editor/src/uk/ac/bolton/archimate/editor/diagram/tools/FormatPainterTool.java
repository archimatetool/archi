/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.tools;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.tools.AbstractTool;

import uk.ac.bolton.archimate.editor.diagram.commands.ConnectionLineColorCommand;
import uk.ac.bolton.archimate.editor.diagram.commands.ConnectionLineWidthCommand;
import uk.ac.bolton.archimate.editor.diagram.commands.FillColorCommand;
import uk.ac.bolton.archimate.editor.diagram.commands.FontColorCommand;
import uk.ac.bolton.archimate.editor.diagram.commands.FontStyleCommand;
import uk.ac.bolton.archimate.editor.diagram.commands.TextAlignmentCommand;
import uk.ac.bolton.archimate.editor.diagram.tools.FormatPainterInfo.ConnectionPaintFormat;
import uk.ac.bolton.archimate.editor.diagram.tools.FormatPainterInfo.ElementPaintFormat;
import uk.ac.bolton.archimate.editor.diagram.tools.FormatPainterInfo.PaintFormat;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.IDiagramModelObject;
import uk.ac.bolton.archimate.model.IJunction;


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
                        FormatPainterInfo.INSTANCE.updatePaintFormat(object);
                    }
                    else {
                        Command cmd = createCommand(pf, object);
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
    
    private Command createCommand(PaintFormat pf, Object object) {
        CompoundCommand result = new CompoundCommand("Format Painter");
        
        if(pf instanceof ElementPaintFormat && object instanceof IDiagramModelObject) {
            ElementPaintFormat epf = (ElementPaintFormat)pf;
            IDiagramModelObject dmo = (IDiagramModelObject)object;
            
            Command cmd = new FillColorCommand(dmo, epf.fillColor);
            if(cmd.canExecute()) {
                result.add(cmd);
            }
            cmd = new FontStyleCommand(dmo, epf.font);
            if(cmd.canExecute()) {
                result.add(cmd);
            }
            cmd = new FontColorCommand(dmo, epf.fontColor);
            if(cmd.canExecute()) {
                result.add(cmd);
            }
            cmd = new TextAlignmentCommand(dmo, epf.textAlignment);
            if(cmd.canExecute()) {
                result.add(cmd);
            }
        }
        else if(pf instanceof ConnectionPaintFormat && object instanceof IDiagramModelConnection) {
            ConnectionPaintFormat cpf = (ConnectionPaintFormat)pf;
            IDiagramModelConnection connection = (IDiagramModelConnection)object;
            
            Command cmd = new ConnectionLineColorCommand(connection, cpf.lineColor);
            if(cmd.canExecute()) {
                result.add(cmd);
            }
            cmd = new FontStyleCommand(connection, cpf.font);
            if(cmd.canExecute()) {
                result.add(cmd);
            }
            cmd = new FontColorCommand(connection, cpf.fontColor);
            if(cmd.canExecute()) {
                result.add(cmd);
            }
            cmd = new ConnectionLineWidthCommand(connection, cpf.lineWidth);
            if(cmd.canExecute()) {
                result.add(cmd);
            }
        }
        
        return result;
    }

    private boolean isPaintableObject(Object object) {
        // Junctions are a no-no
        if(object instanceof IDiagramModelArchimateObject) {
            IArchimateElement element = ((IDiagramModelArchimateObject)object).getArchimateElement();
            return !(element instanceof IJunction);
        }
        
        return object instanceof IDiagramModelObject || object instanceof IDiagramModelConnection;
    }
    
    @Override
    protected String getCommandName() {
        return "FormatPaint";
    }
}