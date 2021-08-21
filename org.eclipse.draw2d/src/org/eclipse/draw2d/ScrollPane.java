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
package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Point;

/**
 * A class which implements automatic horizontal and/or vertical scrolling for a
 * single IFigure child.
 * <p>
 * ScrollBar visibilites are represented by integer class constants:
 * <ul>
 * <li>NEVER: Never show the ScrollBar
 * <li>AUTOMATIC: Show as needed, when the ScrollPane can no longer contain its
 * view
 * <li>ALWAYS: Always show the ScrollBar
 * </ul>
 * To use, instantiate a ScrollPane object and call its setView(IFigure) method
 * passing the IFigure that is desired to have scrolling ability.
 */
public class ScrollPane extends Figure {

    /** Constant indicating to never show the ScrollBar */
    public static final int NEVER = 0;
    /**
     * Constant indicating to show as needed, when the ScrollPane can't contain
     * its view
     */
    public static final int AUTOMATIC = 1;
    /** Constant indicating to always show the ScrollBar */
    public static final int ALWAYS = 2;

    /** The viewport being scrolled */
    protected Viewport viewport;
    /** The horizontal scrollbar */
    protected ScrollBar hBar;
    /** The vertical scrollbar */
    protected ScrollBar vBar;
    private int hVisibility = AUTOMATIC, vVisibility = AUTOMATIC;

    /**
     * Constructs a new ScrollPane with a ScrollPaneLayout.
     * 
     * @since 2.0
     */
    public ScrollPane() {
        setLayoutManager(new ScrollPaneLayout());
    }

    /**
     * Creates a new horizontally oriented ScrollBar and adds it to this
     * ScrollPane.
     * 
     * @since 2.0
     */
    protected void createHorizontalScrollBar() {
        ScrollBar bar = new ScrollBar();
        bar.setHorizontal(true);
        setHorizontalScrollBar(bar);
    }

    /**
     * Creates a new Viewport and adds it to this ScrollPane.
     * 
     * @since 2.0
     */
    protected void createViewport() {
        setViewport(new Viewport());
    }

    /**
     * Creates a new vertically oriented ScrollBar and adds it to this
     * ScrollPane.
     * 
     * @since 2.0
     */
    protected void createVerticalScrollBar() {
        ScrollBar bar = new ScrollBar();
        setVerticalScrollBar(bar);
    }

    /**
     * Returns the ScrollPane's horizontal ScrollBar.
     * 
     * @return the horizontal scrollbar
     * @since 2.0
     */
    public ScrollBar getHorizontalScrollBar() {
        if (hBar == null)
            createHorizontalScrollBar();
        return hBar;
    }

    /**
     * Returns the visibility of the ScrollPane's horizontal ScrollBar. These
     * are represented by the integer class constants {@link #NEVER},
     * {@link #AUTOMATIC}, and {@link #ALWAYS}. The default is
     * {@link #AUTOMATIC}.
     * 
     * @return the visiblity of the horizontal scrollbar
     * @since 2.0
     */
    public int getHorizontalScrollBarVisibility() {
        return hVisibility;
    }

    /**
     * Returns the ScrollPane's vertical ScrollBar.
     * 
     * @return teh vertical scrollbar
     * @since 2.0
     */
    public ScrollBar getVerticalScrollBar() {
        if (vBar == null)
            createVerticalScrollBar();
        return vBar;
    }

    /**
     * Returns the visibility of the ScrollPane's vertical ScrollBar. These are
     * represented by the integer class constants {@link #NEVER},
     * {@link #AUTOMATIC}, and {@link #ALWAYS}. The default is
     * {@link #AUTOMATIC}.
     * 
     * @return the visibility of the vertical scrollbar
     * @since 2.0
     */
    public int getVerticalScrollBarVisibility() {
        return vVisibility;
    }

    /**
     * Returns the contents of the viewport.
     * 
     * @return the contents of the viewport
     */
    public IFigure getContents() {
        return getView();
    }

    /**
     * Returns the ScrollPane's view. The view is the IFigure that is the
     * contents of the ScrollPane.
     * 
     * @return the contents
     * @deprecated use getContents()
     * @since 2.0
     */
    public IFigure getView() {
        return getViewport().getContents();
    }

    /**
     * Returns the ScrollPane's {@link Viewport}.
     * 
     * @return the viewport
     * @since 2.0
     */
    public Viewport getViewport() {
        if (viewport == null)
            createViewport();
        return viewport;
    }

    /**
     * Returns true because ScrollPanes are always opaque.
     * 
     * @see IFigure#isOpaque()
     */
    @Override
    public boolean isOpaque() {
        return true;
    }

    /**
     * Scrolls the Scrollpane horizontally x pixels from its left-most position.
     * 
     * @param x
     *            the amount to scroll horizontally
     * @since 2.0
     */
    public void scrollHorizontalTo(int x) {
        getViewport().setHorizontalLocation(x);
    }

    /**
     * Scrolls the Scrollpane horizontally from its left-most position by
     * location.x pixels and vertically from its top-most position by location.y
     * pixels.
     * 
     * @param location
     *            the point to scroll to
     * @since 2.0
     */
    public void scrollTo(Point location) {
        scrollHorizontalTo(location.x);
        scrollVerticalTo(location.y);
    }

    /**
     * Scrolls the Scrollpane vertically y pixels from its top-most position.
     * 
     * @param y
     *            the amount to scroll vertically
     * @since 2.0
     */
    public void scrollVerticalTo(int y) {
        getViewport().setVerticalLocation(y);
    }

    /**
     * Sets the contents of the current viewport.
     * 
     * @param figure
     *            the contents of the viewport
     */
    public void setContents(IFigure figure) {
        setView(figure);
    }

    /**
     * Sets the ScrollPane's horizontal ScrollBar to the passed ScrollBar.
     * 
     * @param bar
     *            the new horizontal scrollbar
     * @since 2.0
     */
    public void setHorizontalScrollBar(ScrollBar bar) {
        if (hBar != null) {
            remove(hBar);
            hBar.getRangeModel().removePropertyChangeListener(hBar);
        }
        hBar = bar;
        if (hBar != null) {
            add(hBar);
            hBar.setRangeModel(getViewport().getHorizontalRangeModel());
        }
    }

    /**
     * Sets the horizontal ScrollBar visibility of the ScrollPane to the passed
     * value. These are represented by the integer class constants
     * {@link #NEVER}, {@link #AUTOMATIC}, and {@link #ALWAYS}. The default is
     * {@link #AUTOMATIC}.
     * 
     * @param v
     *            the new horizontal visibility
     * @since 2.0
     */
    public void setHorizontalScrollBarVisibility(int v) {
        if (hVisibility == v)
            return;
        hVisibility = v;
        revalidate();
    }

    /**
     * Sets both the horizontal and vertical ScrollBar visibilities of the
     * ScrollPane to the passed value. These are represented by the integer
     * class constants {@link #NEVER}, {@link #AUTOMATIC}, and {@link #ALWAYS}.
     * The default is {@link #AUTOMATIC}.
     * 
     * @param v
     *            the new vertical and horizontal visibility
     * @since 2.0
     */
    public void setScrollBarVisibility(int v) {
        setHorizontalScrollBarVisibility(v);
        setVerticalScrollBarVisibility(v);
    }

    /**
     * Sets the ScrollPane's vertical ScrollBar to the passed Scrollbar.
     * 
     * @param bar
     *            the new vertical scrollbar
     * @since 2.0
     */
    public void setVerticalScrollBar(ScrollBar bar) {
        if (vBar != null) {
            remove(vBar);
            vBar.getRangeModel().removePropertyChangeListener(vBar);
        }
        vBar = bar;
        if (vBar != null) {
            add(vBar);
            vBar.setRangeModel(getViewport().getVerticalRangeModel());
        }
    }

    /**
     * Sets the vertical ScrollBar visibility of the ScrollPane to the passed
     * value. These are represented by the integer class constants
     * {@link #NEVER}, {@link #AUTOMATIC}, and {@link #ALWAYS}. The default is
     * {@link #AUTOMATIC}.
     * 
     * @param v
     *            the new vertical scrollbar visibility
     * @since 2.0
     */
    public void setVerticalScrollBarVisibility(int v) {
        if (vVisibility == v)
            return;
        vVisibility = v;
        revalidate();
    }

    /**
     * Sets the ScrollPane's view to the passed IFigure. The view is the
     * top-level IFigure which represents the contents of the ScrollPane.
     * 
     * @param figure
     *            the new contents
     * @deprecated call setContents(IFigure) instead
     * @since 2.0
     */
    public void setView(IFigure figure) {
        getViewport().setContents(figure);
    }

    /**
     * Sets the ScrollPane's Viewport to the passed value.
     * 
     * @param vp
     *            the new viewport
     * @since 2.0
     */
    public void setViewport(Viewport vp) {
        if (viewport != null)
            remove(viewport);
        viewport = vp;
        if (vp != null)
            add(vp, 0);
    }

    /**
     * @see IFigure#validate()
     */
    @Override
    public void validate() {
        super.validate();
        getHorizontalScrollBar().validate();
        getVerticalScrollBar().validate();
    }

}
