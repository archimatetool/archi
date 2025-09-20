/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.policies.snaptogrid;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.handles.BendpointCreationHandle;
import org.eclipse.swt.graphics.Color;

/**
 * snap-to-grid patch by Jean-Baptiste Sarrodie (aka Jaiguru)
 * 
 * This class has been extended to allow overriding of class ExtendedConnectionBendpointTracker
 * 
 * September 2025 - added custom colouring and round graphics
 */
public class ExtendedBendpointCreationHandle extends BendpointCreationHandle {

    /**
     * Creates a new ExtendedBendpointCreationHandle, sets its owner to
     * <code>owner</code> and its index to <code>index</code>, and sets its
     * locator to a new {@link MidpointLocator} with the given
     * <code>locatorIndex</code>.
     * 
     * @param owner
     *            the ConnectionEditPart owner
     * @param index
     *            the index
     * @param locatorIndex
     *            the locator index
     */
	public ExtendedBendpointCreationHandle(ConnectionEditPart owner, int index, int locatorIndex) {
	    super(owner, index, locatorIndex);
	    setPreferredSize(new Dimension(6, 6));
    }

    /**
	 * Creates and returns a new {@link ExtendedConnectionBendpointTracker}.
	 * 
	 * @return the new ExtendedConnectionBendpointTracker
	 */
	@Override
    protected DragTracker createDragTracker() {
		ExtendedConnectionBendpointTracker tracker = new ExtendedConnectionBendpointTracker((ConnectionEditPart) getOwner(), getIndex());
		tracker.setType(RequestConstants.REQ_CREATE_BENDPOINT);
		tracker.setDefaultCursor(getCursor());
		return tracker;
	}
	
    /**
     * Over-ride for custom colors
     */
    @Override
    protected Color getBorderColor() {
        return isPrimary() ? ColorConstants.black : ColorConstants.gray;
    }
    
    /**
     * Over-ride for custom colors
     */
    @Override
    protected Color getFillColor() {
        return ColorConstants.white;
    }

    /**
     * Over-ride to draw a circle instead of a square
     */
    @Override
    public void paintFigure(Graphics g) {
        Rectangle r = getBounds();
        r.shrink(1, 1);
        try {
            g.setBackgroundColor(getFillColor());
            g.fillOval(r);
            g.setForegroundColor(getBorderColor());
            g.drawOval(r);
        }
        finally {
            // We don't really own rect 'r', so fix it.
            r.expand(1, 1);
        }
    }
}

