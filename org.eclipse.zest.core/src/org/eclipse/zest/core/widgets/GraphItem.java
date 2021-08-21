/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.zest.core.widgets;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Widget;

/**
 * Provides support for property changes. All model elements extend this class.
 * Also extends the Item (Widget) class to be used inside a StructuredViewer.
 * 
 * @author Chris Callendar
 */
public abstract class GraphItem extends Item {

    public static final int GRAPH = 0;
    public static final int NODE = 1;
    public static final int CONNECTION = 2;
    public static final int CONTAINER = 3;

    /**
     * @param parent
     * @param style
     */
    public GraphItem(Widget parent, int style) {
        this(parent, style | SWT.NO_BACKGROUND, null);
    }

    /**
     * @param parent
     * @param style
     */
    public GraphItem(Widget parent, int style, Object data) {
        super(parent, style | SWT.NO_BACKGROUND);
        if (data != null) {
            this.setData(data);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.swt.widgets.Widget#dispose()
     */
    @Override
    public void dispose() {
        // @tag zest.bug.167132-ListenerDispose : remove all listeners.
        // pcsDelegate = new PropertyChangeSupport(this);
        super.dispose();
    }

    /**
     * Gets the graph item type. The item type is one of: GRAPH, NODE or
     * CONNECTION
     * 
     * @return
     */
    public abstract int getItemType();

    /**
     * Set the visibility of this item.
     * 
     * @param visible
     *            whether or not this item is visible.
     */
    public abstract void setVisible(boolean visible);

    /**
     * Get the visibility of this item.
     * 
     * @return the visibility of this item.
     */
    public abstract boolean isVisible();

    /**
     * Gets the graph that this item is rooted on. If this item is itself a
     * graph, then this is returned.
     * 
     * @return the parent graph.
     */
    public abstract Graph getGraphModel();

    /**
     * Highlights the current GraphItem.  A graph item is either a graph node or 
     * graph connection, and highlighting them will set the appropriate highlight
     * color.
     */
    public abstract void highlight();

    /**
     * Unhighlight sets the graphItem (either a graphNode or graphConnection) back
     * to the unhighlight figure or color.
     */
    public abstract void unhighlight();

    abstract IFigure getFigure();

    /**
     * Checks a style to see if it is set on the given graph item
     * @param styleToCheck The style to check
     * @return
     */
    protected boolean checkStyle(int styleToCheck) {
        return ((getStyle() & styleToCheck) > 0);
    }
}
