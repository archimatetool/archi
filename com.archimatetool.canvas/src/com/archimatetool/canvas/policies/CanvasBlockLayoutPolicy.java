/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.policies;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import com.archimatetool.editor.diagram.policies.DiagramLayoutPolicy;



/**
 * Policy for Canvas Block
 * 
 * @author Phillip Beauvoir
 */
public class CanvasBlockLayoutPolicy
extends DiagramLayoutPolicy {
    
    private boolean constrainChildren = false;
    
    @Override
    protected AddObjectCommand createAddCommand(ChangeBoundsRequest request, EditPart child, Object constraint) {
        Rectangle rect = (Rectangle)(constrainChildren ? getLimitedBounds(child, (Rectangle)constraint) : constraint);
        return super.createAddCommand(request, child, rect);
    }
    
    @Override
    protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child, Object constraint) {
        Rectangle rect = (Rectangle)(constrainChildren ? getLimitedBounds(child, (Rectangle)constraint) : constraint);
        return super.createChangeConstraintCommand(request, child, rect);
    }
    
    /**
     * Restrict child parts to parent size
     */
    protected Rectangle getLimitedBounds(EditPart child, Rectangle bounds) {
        bounds = bounds.getCopy();
        
        // Get width and height from figure because these can be -1 in model
        IFigure childFigure = ((GraphicalEditPart)child).getFigure();
        int childWidth = childFigure.getBounds().width;
        int childHeight = childFigure.getBounds().height;

        int x = bounds.x + childWidth;
        int parentWidth = getHostFigure().getBounds().width;
        int y = bounds.y + childHeight;
        int parentHeight = getHostFigure().getBounds().height;
        
        if(bounds.x < 1) {
            bounds.x = 1;
        }
        else if(x > parentWidth) {
            bounds.x = parentWidth - childWidth;
        }
        
        if(bounds.y < 1) {
            bounds.y = 1;
        }
        else if(y > parentHeight) {
            bounds.y = parentHeight - childHeight;
        }
        
        if(bounds.x + bounds.width > parentWidth) {
            bounds.width = parentWidth - bounds.x;
        }
        
        if(bounds.y + bounds.height > parentHeight) {
            bounds.height = parentHeight - bounds.y;
        }
        
        if(childWidth > parentWidth) {
            bounds.width = parentWidth;
        }
        
        if(childHeight > parentHeight) {
            bounds.height = parentHeight;
        }

        return bounds;
    }

}

