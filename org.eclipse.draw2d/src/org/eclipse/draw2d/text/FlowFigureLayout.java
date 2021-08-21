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
package org.eclipse.draw2d.text;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * A LayoutManager for use with FlowFigure.
 * 
 * <P>
 * WARNING: This class is not intended to be subclassed by clients.
 * 
 * @author hudsonr
 * @since 2.1
 */
public abstract class FlowFigureLayout implements LayoutManager {

    /**
     * The flow context in which this LayoutManager exists.
     */
    private FlowContext context;

    /**
     * The figure passed by layout(Figure) is held for convenience.
     */
    private final FlowFigure flowFigure;

    /**
     * Constructs a new FlowFigureLayout with the given FlowFigure.
     * 
     * @param flowfigure
     *            the FlowFigure
     */
    protected FlowFigureLayout(FlowFigure flowfigure) {
        this.flowFigure = flowfigure;
    }

    /**
     * Not applicable.
     * 
     * @see org.eclipse.draw2d.LayoutManager#getConstraint(org.eclipse.draw2d.IFigure)
     */
    @Override
    public Object getConstraint(IFigure child) {
        return null;
    }

    /**
     * Returns this layout's context or <code>null</code>.
     * 
     * @return <code>null</code> or a context
     * @since 3.1
     */
    protected FlowContext getContext() {
        return context;
    }

    /**
     * @return the FlowFigure
     */
    protected FlowFigure getFlowFigure() {
        return flowFigure;
    }

    /**
     * Not applicable.
     * 
     * @see org.eclipse.draw2d.LayoutManager#getMinimumSize(org.eclipse.draw2d.IFigure,
     *      int, int)
     */
    @Override
    public Dimension getMinimumSize(IFigure container, int wHint, int hHint) {
        return null;
    }

    /**
     * Not applicable.
     * 
     * @see org.eclipse.draw2d.LayoutManager#getPreferredSize(org.eclipse.draw2d.IFigure,
     *      int, int)
     */
    @Override
    public Dimension getPreferredSize(IFigure container, int wHint, int hHint) {
        return null;
    }

    /**
     * Not applicable.
     * 
     * @see org.eclipse.draw2d.LayoutManager#invalidate()
     */
    @Override
    public void invalidate() {
    }

    /**
     * Called during {@link #layout(IFigure)}.
     */
    protected abstract void layout();

    /**
     * @see org.eclipse.draw2d.LayoutManager#layout(IFigure)
     */
    @Override
    public final void layout(IFigure figure) {
        layout();
    }

    /**
     * Not applicable.
     * 
     * @see org.eclipse.draw2d.LayoutManager#remove(org.eclipse.draw2d.IFigure)
     */
    @Override
    public void remove(IFigure child) {
    }

    /**
     * Not applicable.
     * 
     * @see org.eclipse.draw2d.LayoutManager#setConstraint(org.eclipse.draw2d.IFigure,
     *      java.lang.Object)
     */
    @Override
    public void setConstraint(IFigure child, Object constraint) {
    }

    /**
     * Sets the context for this layout manager.
     * 
     * @param flowContext
     *            the context of this layout
     */
    public void setFlowContext(FlowContext flowContext) {
        context = flowContext;
    }

}
