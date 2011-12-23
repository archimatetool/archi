/*******************************************************************************
 * Copyright (c) 2010-11 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.policies;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.ResizeTracker;


/**
 * A Resize Edit Policy to limit resizing of IConstrainedSizeEditPart parts
 * 
 * @author Phillip Beauvoir
 */
public class ConstrainedResizableEditPolicy extends ResizableEditPolicy {
    
    protected final Dimension MIN_DIMENSION = new Dimension(32, 32);
    
    @Override
    protected ResizeTracker getResizeTracker(int direction) {
        return new ResizeTracker((GraphicalEditPart) getHost(), direction) {
            @Override
            protected Request createSourceRequest() {
                ChangeBoundsRequest request = new ChangeBoundsRequest(REQ_RESIZE) {
                    @Override
                    public boolean isConstrainedResize() {
                        return super.isConstrainedResize();
                        //return getCurrentInput().isModKeyDown(SWT.SHIFT) ? false : true;
                    }
                    
                    @Override
                    public boolean isCenteredResize() {
                        return super.isCenteredResize();
                        //return false;
                    }
                    
                    @Override
                    public boolean isSnapToEnabled() {
                        return super.isSnapToEnabled();
                        
//                        int direction = getResizeDirection();
//                        if(direction == PositionConstants.EAST) {
//                            return super.isSnapToEnabled();
//                        }
//                        return false;
                    }
                };
                
                request.setResizeDirection(getResizeDirection());
                
                return request;
            }
            
            @Override
            protected Dimension getMinimumSizeFor(ChangeBoundsRequest request) {
                return MIN_DIMENSION;
            }
        };
    }
    
//    @SuppressWarnings("rawtypes")
//    @Override
//    protected List createSelectionHandles() {
//        List list = new ArrayList();
//        createMoveHandle(list);
//        createResizeHandle(list, PositionConstants.SOUTH_EAST);
//        createResizeHandle(list, PositionConstants.SOUTH_WEST);
//        createResizeHandle(list, PositionConstants.NORTH_WEST);
//        createResizeHandle(list, PositionConstants.NORTH_EAST);
//        return list;
//    }

}
