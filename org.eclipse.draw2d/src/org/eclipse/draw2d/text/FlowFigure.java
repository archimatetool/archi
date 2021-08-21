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

import java.util.Iterator;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * The base implementation for text flow figures. A flow figure is used to
 * render a document in which elements are laid out horizontally within a "line"
 * until that line is filled. Layout continues on the next line.
 * 
 * <p>
 * WARNING: This class is not intended to be subclassed by clients. Future
 * versions may contain additional abstract methods.
 * 
 * @author hudsonr
 * @since 2.1
 */
@SuppressWarnings("rawtypes")
public abstract class FlowFigure extends Figure {

    /**
     * integer indicating whether selection should be displayed.
     */
    protected int selectionStart = -1;

    /**
     * Constructs a new FlowFigure.
     */
    public FlowFigure() {
        setLayoutManager(createDefaultFlowLayout());
    }

    /**
     * If the child is a <code>FlowFigure</code>, its FlowContext is passed to
     * it.
     * 
     * @see org.eclipse.draw2d.IFigure#add(IFigure, Object, int)
     */
    @Override
    public void add(IFigure child, Object constraint, int index) {
        super.add(child, constraint, index);
        // If this layout manager is a FlowContext, then the child *must* be a
        // FlowFigure
        if (getLayoutManager() instanceof FlowContext)
            ((FlowFigure) child)
                    .setFlowContext((FlowContext) getLayoutManager());
        revalidateBidi(this);
    }

    /**
     * Calculates the width of text before the next line-break is encountered.
     * <p>
     * Default implementation treats each FlowFigure as a line-break. It adds no
     * width and returns <code>true</code>. Sub-classes should override as
     * needed.
     * 
     * @param width
     *            the width before the next line-break (if one's found; all the
     *            width, otherwise) will be added on to the first int in the
     *            given array
     * @return boolean indicating whether or not a line-break was found
     * @since 3.1
     */
    public boolean addLeadingWordRequirements(int[] width) {
        return true;
    }

    /**
     * FlowFigures can contribute text for their block to the given
     * {@link BidiProcessor}, which will process the contributions to determine
     * Bidi levels and shaping requirements.
     * <p>
     * This method is invoked as part of validating Bidi.
     * <p>
     * Sub-classes that cache the BidiInfo and/or the bidi level in ContentBoxes
     * should clear the cached values when this method is invoked.
     * 
     * @param proc
     *            the BidiProcessor to which contributions should be made
     * @see BidiProcessor#add(FlowFigure, String)
     * @since 3.1
     */
    protected void contributeBidi(BidiProcessor proc) {
        for (Iterator iter = getChildren().iterator(); iter.hasNext();)
            ((FlowFigure) iter.next()).contributeBidi(proc);
    }

    /**
     * Creates the default layout manager
     * 
     * @return The default layout
     */
    protected abstract FlowFigureLayout createDefaultFlowLayout();

    /**
     * Called after validate has occurred. This is used to update the bounds of
     * the FlowFigure to encompass its new flow boxed created during validate.
     */
    public abstract void postValidate();

    /**
     * Overridden to revalidateBidi when fragments are removed.
     * 
     * @see org.eclipse.draw2d.IFigure#remove(org.eclipse.draw2d.IFigure)
     */
    @Override
    public void remove(IFigure figure) {
        super.remove(figure);
        revalidateBidi(this);
    }

    /**
     * This method should be invoked whenever a change that can potentially
     * affect the Bidi evaluation is made (eg., adding or removing children,
     * changing text, etc.).
     * <p>
     * The default implementation delegates the revalidation task to the parent.
     * Only {@link BlockFlow#revalidateBidi(IFigure) blocks} perform the actual
     * revalidation.
     * <p>
     * The given IFigure is the one that triggered the revalidation. This can be
     * used to optimize bidi evaluation.
     * 
     * @param origin
     *            the figure that was revalidated
     * @since 3.1
     */
    protected void revalidateBidi(IFigure origin) {
        if (getParent() != null)
            ((FlowFigure) getParent()).revalidateBidi(origin);
    }

    /**
     * Sets the bidi information for this figure. A flow figure contributes bidi
     * text in {@link #contributeBidi(BidiProcessor)}. If the figure contributes
     * text associated with it, this method is called back to indicate the bidi
     * properties for that text within its block.
     * 
     * @param info
     *            the BidiInfo for this figure
     * @since 3.1
     */
    public void setBidiInfo(BidiInfo info) {
    }

    /**
     * FlowFigures override setBounds() to prevent translation of children.
     * "bounds" is a derived property for FlowFigures, calculated from the
     * fragments that make up the FlowFigure.
     * 
     * @see Figure#setBounds(Rectangle)
     */
    @Override
    public void setBounds(Rectangle r) {
        if (bounds.equals(r))
            return;
        if (!r.contains(bounds))
            erase();
        bounds.x = r.x;
        bounds.y = r.y;
        bounds.width = r.width;
        bounds.height = r.height;
        fireFigureMoved();
        if (isCoordinateSystem())
            fireCoordinateSystemChanged();
        repaint();
    }

    /**
     * Sets the flow context.
     * 
     * @param flowContext
     *            the flow context for this flow figure
     */
    public void setFlowContext(FlowContext flowContext) {
        ((FlowFigureLayout) getLayoutManager()).setFlowContext(flowContext);
    }

    /**
     * Sets the selection or a range of selection. A start value of -1 is used
     * to indicate no selection. A start value >=0 indicates show selection. A
     * start and end value can be used to represent a range of offsets which
     * should render selection.
     * 
     * @param start
     *            the start offset
     * @param end
     *            the end offset
     * @since 3.1
     */
    public void setSelection(int start, int end) {
        if (selectionStart == start)
            return;
        selectionStart = start;
        repaint();
    }

}
