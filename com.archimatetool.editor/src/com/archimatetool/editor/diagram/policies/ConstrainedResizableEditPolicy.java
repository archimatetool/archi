/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.policies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.ResizeTracker;
import org.eclipse.swt.SWT;


/**
 * A Resize Edit Policy to limit resizing of IConstrainedSizeEditPart parts.
 * The idea is to maintain aspect ratio of the figure as it is resized but over-ride that with the Shift key
 * 
 * Currently not used
 * 
 * @author Phillip Beauvoir
 */
public class ConstrainedResizableEditPolicy extends ResizableEditPolicy {
    
    @Override
    protected ResizeTracker getResizeTracker(int direction) {
        return new ResizeTracker((GraphicalEditPart) getHost(), direction) {
            @Override
            protected Request createSourceRequest() {
                ChangeBoundsRequest request = new ChangeBoundsRequest(REQ_RESIZE) {
                    @Override
                    public boolean isConstrainedResize() {
                        return getCurrentInput().isModKeyDown(SWT.SHIFT) ? false : true;
                    }
                    
                    @Override
                    public boolean isCenteredResize() {
                        return false;
                    }
                    
                    @Override
                    public boolean isSnapToEnabled() {
                        int direction = getResizeDirection();
                        if(direction == PositionConstants.EAST) {
                            return super.isSnapToEnabled();
                        }
                        return false;
                    }
                };
                
                request.setResizeDirection(getResizeDirection());
                
                return request;
            }
        };
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    protected List createSelectionHandles() {
        List list = new ArrayList();
        createMoveHandle(list);
        createResizeHandle(list, PositionConstants.SOUTH_EAST);
        createResizeHandle(list, PositionConstants.SOUTH_WEST);
        createResizeHandle(list, PositionConstants.NORTH_WEST);
        createResizeHandle(list, PositionConstants.NORTH_EAST);
        return list;
    }

}
