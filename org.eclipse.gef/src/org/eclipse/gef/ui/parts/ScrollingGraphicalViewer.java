/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.parts;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;

/**
 * A Graphical Viewer implementation which uses a
 * {@link org.eclipse.draw2d.FigureCanvas} for native scrolling. Because the
 * scrolling is handled natively, the root editpart should not contain a
 * {@link org.eclipse.draw2d.ScrollPane} figure. Do not use root editparts which
 * provide scrollpane figures, such as <code>GraphicalRootEditPart</code>.
 * <P>
 * The RootEditPart for a ScrollingGraphicalViewer may contain a Viewport. If it
 * does, that viewport will be set as the FigureCanvas' viewport. FigureCanvas
 * has certain requirements on the viewport figure, see
 * {@link FigureCanvas#setViewport(Viewport)}.
 * 
 * @author hudsonr
 */
public class ScrollingGraphicalViewer extends GraphicalViewerImpl {

    /**
     * Constructs a ScrollingGraphicalViewer;
     */
    public ScrollingGraphicalViewer() {
    }

    /**
     * @see org.eclipse.gef.EditPartViewer#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public final Control createControl(Composite parent) {
        setControl(new FigureCanvas(parent, getLightweightSystem()));
        hookRootFigure();
        return getControl();
    }

    /**
     * Convenience method which types the control as a <code>FigureCanvas</code>
     * . This method returns <code>null</code> whenever the control is null.
     * 
     * @return <code>null</code> or the Control as a FigureCanvas
     */
    protected FigureCanvas getFigureCanvas() {
        return (FigureCanvas) getControl();
    }

    /**
     * Extends the superclass implementation to scroll the native Canvas control
     * after the super's implementation has completed.
     * 
     * @see org.eclipse.gef.EditPartViewer#reveal(org.eclipse.gef.EditPart)
     */
    @Override
    public void reveal(EditPart part) {
        super.reveal(part);
        Viewport port = getFigureCanvas().getViewport();
        IFigure target = ((GraphicalEditPart) part).getFigure();
        Rectangle exposeRegion = target.getBounds().getCopy();
        target = target.getParent();
        while (target != null && target != port) {
            target.translateToParent(exposeRegion);
            target = target.getParent();
        }
        exposeRegion.expand(5, 5);

        Dimension viewportSize = port.getClientArea().getSize();

        Point topLeft = exposeRegion.getTopLeft();
        Point bottomRight = exposeRegion.getBottomRight().translate(
                viewportSize.getNegated());
        Point finalLocation = new Point();
        if (viewportSize.width < exposeRegion.width)
            finalLocation.x = Math.min(bottomRight.x,
                    Math.max(topLeft.x, port.getViewLocation().x));
        else
            finalLocation.x = Math.min(topLeft.x,
                    Math.max(bottomRight.x, port.getViewLocation().x));

        if (viewportSize.height < exposeRegion.height)
            finalLocation.y = Math.min(bottomRight.y,
                    Math.max(topLeft.y, port.getViewLocation().y));
        else
            finalLocation.y = Math.min(topLeft.y,
                    Math.max(bottomRight.y, port.getViewLocation().y));

        getFigureCanvas().scrollSmoothTo(finalLocation.x, finalLocation.y);
    }

    /**
     * If the figure is a viewport, set the canvas' viewport, otherwise, set its
     * contents.
     */
    @Override
    protected void hookRootFigure() {
        if (getFigureCanvas() == null)
            return;
        if (rootFigure instanceof Viewport)
            getFigureCanvas().setViewport((Viewport) rootFigure);
        else
            getFigureCanvas().setContents(rootFigure);
    }

}
