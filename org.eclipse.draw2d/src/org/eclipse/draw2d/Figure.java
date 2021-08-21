/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Mike Marchand - fix for bug #472848
 *******************************************************************************/
package org.eclipse.draw2d;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;

/**
 * The base implementation for graphical figures.
 */
@SuppressWarnings({"rawtypes", "unchecked", "deprecation"})
public class Figure implements IFigure {

    private static final Rectangle PRIVATE_RECT = new Rectangle();
    private static final Point PRIVATE_POINT = new Point();
    private static final int FLAG_VALID = new Integer(1).intValue(),
            FLAG_OPAQUE = new Integer(1 << 1).intValue(),
            FLAG_VISIBLE = new Integer(1 << 2).intValue(),
            FLAG_FOCUSABLE = new Integer(1 << 3).intValue(),
            FLAG_ENABLED = new Integer(1 << 4).intValue(),
            FLAG_FOCUS_TRAVERSABLE = new Integer(1 << 5).intValue();

    static final int FLAG_REALIZED = 1 << 31;

    /**
     * The largest flag defined in this class. If subclasses define flags, they
     * should declare them as larger than this value and redefine MAX_FLAG to be
     * their largest flag value.
     * <P>
     * This constant is evaluated at runtime and will not be inlined by the
     * compiler.
     */
    protected static int MAX_FLAG = FLAG_FOCUS_TRAVERSABLE;

    /**
     * The rectangular area that this Figure occupies.
     */
    protected Rectangle bounds = new Rectangle(0, 0, 0, 0);

    private LayoutManager layoutManager;

    /**
     * The flags for this Figure.
     */
    protected int flags = FLAG_VISIBLE | FLAG_ENABLED;

    private IFigure parent;
    private IClippingStrategy clippingStrategy = null;
    private Cursor cursor;

    private PropertyChangeSupport propertyListeners;
    private EventListenerList eventListeners = new EventListenerList();

    private List children = Collections.EMPTY_LIST;

    /**
     * This Figure's preferred size.
     */
    protected Dimension prefSize;

    /**
     * This Figure's minimum size.
     */
    protected Dimension minSize;

    /**
     * This Figure's maximum size.
     */
    protected Dimension maxSize;

    /**
     * @deprecated access using {@link #getLocalFont()}
     */
    protected Font font;

    /**
     * @deprecated access using {@link #getLocalBackgroundColor()}.
     */
    protected Color bgColor;

    /**
     * @deprecated access using {@link #getLocalForegroundColor()}.
     */
    protected Color fgColor;

    /**
     * @deprecated access using {@link #getBorder()}
     */
    protected Border border;

    /**
     * @deprecated access using {@link #getToolTip()}
     */
    protected IFigure toolTip;

    private AncestorHelper ancestorHelper;

    /**
     * Calls {@link #add(IFigure, Object, int)} with -1 as the index.
     * 
     * @see IFigure#add(IFigure, Object)
     */
    @Override
    public final void add(IFigure figure, Object constraint) {
        add(figure, constraint, -1);
    }

    /**
     * @see IFigure#add(IFigure, Object, int)
     */
    @Override
    public void add(IFigure figure, Object constraint, int index) {
        if (children == Collections.EMPTY_LIST)
            children = new ArrayList(2);
        if (index < -1 || index > children.size())
            throw new IndexOutOfBoundsException("Index does not exist"); //$NON-NLS-1$

        // Check for Cycle in hierarchy
        for (IFigure f = this; f != null; f = f.getParent())
            if (figure == f)
                throw new IllegalArgumentException(
                        "Figure being added introduces cycle"); //$NON-NLS-1$

        // Detach the child from previous parent
        if (figure.getParent() != null)
            figure.getParent().remove(figure);

        if (index == -1)
            children.add(figure);
        else
            children.add(index, figure);
        figure.setParent(this);

        if (layoutManager != null)
            layoutManager.setConstraint(figure, constraint);

        revalidate();

        if (getFlag(FLAG_REALIZED))
            figure.addNotify();
        figure.repaint();
    }

    /**
     * Calls {@link #add(IFigure, Object, int)} with <code>null</code> as the
     * constraint and -1 as the index.
     * 
     * @see IFigure#add(IFigure)
     */
    @Override
    public final void add(IFigure figure) {
        add(figure, null, -1);
    }

    /**
     * Calls {@link #add(IFigure, Object, int)} with <code>null</code> as the
     * constraint.
     * 
     * @see IFigure#add(IFigure, int)
     */
    @Override
    public final void add(IFigure figure, int index) {
        add(figure, null, index);
    }

    /**
     * @see IFigure#addAncestorListener(AncestorListener)
     */
    @Override
    public void addAncestorListener(AncestorListener ancestorListener) {
        if (ancestorHelper == null)
            ancestorHelper = new AncestorHelper(this);
        ancestorHelper.addAncestorListener(ancestorListener);
    }

    /**
     * @see IFigure#addCoordinateListener(CoordinateListener)
     */
    @Override
    public void addCoordinateListener(CoordinateListener listener) {
        eventListeners.addListener(CoordinateListener.class, listener);
    }

    /**
     * @see IFigure#addFigureListener(FigureListener)
     */
    @Override
    public void addFigureListener(FigureListener listener) {
        eventListeners.addListener(FigureListener.class, listener);
    }

    /**
     * @see IFigure#addFocusListener(FocusListener)
     */
    @Override
    public void addFocusListener(FocusListener listener) {
        eventListeners.addListener(FocusListener.class, listener);
    }

    /**
     * @see IFigure#addKeyListener(KeyListener)
     */
    @Override
    public void addKeyListener(KeyListener listener) {
        eventListeners.addListener(KeyListener.class, listener);
    }

    /**
     * Appends the given layout listener to the list of layout listeners.
     * 
     * @since 3.1
     * @param listener
     *            the listener being added
     */
    @Override
    public void addLayoutListener(LayoutListener listener) {
        if (layoutManager instanceof LayoutNotifier) {
            LayoutNotifier notifier = (LayoutNotifier) layoutManager;
            notifier.listeners.add(listener);
        } else
            layoutManager = new LayoutNotifier(layoutManager, listener);
    }

    /**
     * Adds a listener of type <i>clazz</i> to this Figure's list of event
     * listeners.
     * 
     * @param clazz
     *            The listener type
     * @param listener
     *            The listener
     */
    protected void addListener(Class clazz, Object listener) {
        eventListeners.addListener(clazz, listener);
    }

    /**
     * @see IFigure#addMouseListener(MouseListener)
     */
    @Override
    public void addMouseListener(MouseListener listener) {
        eventListeners.addListener(MouseListener.class, listener);
    }

    /**
     * @see IFigure#addMouseMotionListener(MouseMotionListener)
     */
    @Override
    public void addMouseMotionListener(MouseMotionListener listener) {
        eventListeners.addListener(MouseMotionListener.class, listener);
    }

    /**
     * Called after the receiver's parent has been set and it has been added to
     * its parent.
     * 
     * @since 2.0
     */
    @Override
    public void addNotify() {
        if (getFlag(FLAG_REALIZED))
            throw new RuntimeException(
                    "addNotify() should not be called multiple times"); //$NON-NLS-1$
        setFlag(FLAG_REALIZED, true);
        for (int i = 0; i < children.size(); i++)
            ((IFigure) children.get(i)).addNotify();
    }

    /**
     * @see IFigure#addPropertyChangeListener(String, PropertyChangeListener)
     */
    @Override
    public void addPropertyChangeListener(String property,
            PropertyChangeListener listener) {
        if (propertyListeners == null)
            propertyListeners = new PropertyChangeSupport(this);
        propertyListeners.addPropertyChangeListener(property, listener);
    }

    /**
     * @see IFigure#addPropertyChangeListener(PropertyChangeListener)
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (propertyListeners == null)
            propertyListeners = new PropertyChangeSupport(this);
        propertyListeners.addPropertyChangeListener(listener);
    }

    /**
     * This method is final. Override {@link #containsPoint(int, int)} if
     * needed.
     * 
     * @see IFigure#containsPoint(Point)
     * @since 2.0
     */
    @Override
    public final boolean containsPoint(Point p) {
        return containsPoint(p.x, p.y);
    }

    /**
     * @see IFigure#containsPoint(int, int)
     */
    @Override
    public boolean containsPoint(int x, int y) {
        return getBounds().contains(x, y);
    }

    /**
     * @see IFigure#erase()
     */
    @Override
    public void erase() {
        if (getParent() == null || !isVisible())
            return;

        Rectangle r = new Rectangle(getBounds());
        getParent().translateToParent(r);
        getParent().repaint(r.x, r.y, r.width, r.height);
    }

    /**
     * Returns a descendant of this Figure such that the Figure returned
     * contains the point (x, y), and is accepted by the given TreeSearch.
     * Returns <code>null</code> if none found.
     * 
     * @param x
     *            The X coordinate
     * @param y
     *            The Y coordinate
     * @param search
     *            the TreeSearch
     * @return The descendant Figure at (x,y)
     */
    protected IFigure findDescendantAtExcluding(int x, int y, TreeSearch search) {
        PRIVATE_POINT.setLocation(x, y);
        translateFromParent(PRIVATE_POINT);
        if (!getClientArea(Rectangle.SINGLETON).contains(PRIVATE_POINT))
            return null;

        x = PRIVATE_POINT.x;
        y = PRIVATE_POINT.y;
        IFigure fig;
        for (int i = children.size(); i > 0;) {
            i--;
            fig = (IFigure) children.get(i);
            if (fig.isVisible()) {
                fig = fig.findFigureAt(x, y, search);
                if (fig != null)
                    return fig;
            }
        }
        // No descendants were found
        return null;
    }

    /**
     * @see IFigure#findFigureAt(Point)
     */
    @Override
    public final IFigure findFigureAt(Point pt) {
        return findFigureAtExcluding(pt.x, pt.y, Collections.EMPTY_LIST);
    }

    /**
     * @see IFigure#findFigureAt(int, int)
     */
    @Override
    public final IFigure findFigureAt(int x, int y) {
        return findFigureAt(x, y, IdentitySearch.INSTANCE);
    }

    /**
     * @see IFigure#findFigureAt(int, int, TreeSearch)
     */
    @Override
    public IFigure findFigureAt(int x, int y, TreeSearch search) {
        if (!containsPoint(x, y))
            return null;
        if (search.prune(this))
            return null;
        IFigure child = findDescendantAtExcluding(x, y, search);
        if (child != null)
            return child;
        if (search.accept(this))
            return this;
        return null;
    }

    /**
     * @see IFigure#findFigureAtExcluding(int, int, Collection)
     */
    @Override
    public final IFigure findFigureAtExcluding(int x, int y, Collection c) {
        return findFigureAt(x, y, new ExclusionSearch(c));
    }

    /**
     * Returns the deepest descendant for which {@link #isMouseEventTarget()}
     * returns <code>true</code> or <code>null</code> if none found. The
     * Parameters <i>x</i> and <i>y</i> are absolute locations. Any Graphics
     * transformations applied by this Figure to its children during
     * {@link #paintChildren(Graphics)} (thus causing the children to appear
     * transformed to the user) should be applied inversely to the points
     * <i>x</i> and <i>y</i> when called on the children.
     * 
     * @param x
     *            The X coordinate
     * @param y
     *            The Y coordinate
     * @return The deepest descendant for which isMouseEventTarget() returns
     *         true
     */
    @Override
    public IFigure findMouseEventTargetAt(int x, int y) {
        if (!containsPoint(x, y))
            return null;
        IFigure f = findMouseEventTargetInDescendantsAt(x, y);
        if (f != null)
            return f;
        if (isMouseEventTarget())
            return this;
        return null;
    }

    /**
     * Searches this Figure's children for the deepest descendant for which
     * {@link #isMouseEventTarget()} returns <code>true</code> and returns that
     * descendant or <code>null</code> if none found.
     * 
     * @see #findMouseEventTargetAt(int, int)
     * @param x
     *            The X coordinate
     * @param y
     *            The Y coordinate
     * @return The deepest descendant for which isMouseEventTarget() returns
     *         true
     */
    protected IFigure findMouseEventTargetInDescendantsAt(int x, int y) {
        PRIVATE_POINT.setLocation(x, y);
        translateFromParent(PRIVATE_POINT);

        if (!getClientArea(Rectangle.SINGLETON).contains(PRIVATE_POINT))
            return null;

        IFigure fig;
        for (int i = children.size(); i > 0;) {
            i--;
            fig = (IFigure) children.get(i);
            if (fig.isVisible() && fig.isEnabled()) {
                if (fig.containsPoint(PRIVATE_POINT.x, PRIVATE_POINT.y)) {
                    fig = fig.findMouseEventTargetAt(PRIVATE_POINT.x,
                            PRIVATE_POINT.y);
                    if (fig != null) {
                        return fig;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Notifies to all {@link CoordinateListener}s that this figure's local
     * coordinate system has changed in a way which affects the absolute bounds
     * of figures contained within.
     * 
     * @since 3.1
     */
    protected void fireCoordinateSystemChanged() {
        if (!eventListeners.containsListener(CoordinateListener.class))
            return;
        Iterator figureListeners = eventListeners
                .getListeners(CoordinateListener.class);
        while (figureListeners.hasNext())
            ((CoordinateListener) figureListeners.next())
                    .coordinateSystemChanged(this);
    }

    /**
     * Notifies to all {@link FigureListener}s that this figure has moved. Moved
     * means that the bounds have changed in some way, location and/or size.
     * 
     * @since 3.1
     */
    protected void fireFigureMoved() {
        if (!eventListeners.containsListener(FigureListener.class))
            return;
        Iterator figureListeners = eventListeners
                .getListeners(FigureListener.class);
        while (figureListeners.hasNext())
            ((FigureListener) figureListeners.next()).figureMoved(this);
    }

    /**
     * Fires both figuremoved and coordinate system changed. This method exists
     * for compatibility. Some listeners which used to listen for figureMoved
     * now listen for coordinates changed. So to be sure that those new
     * listeners are notified, any client code which used called this method
     * will also result in notification of coordinate changes.
     * 
     * @since 2.0
     * @deprecated call fireFigureMoved() or fireCoordinateSystemChanged() as
     *             appropriate
     */
    protected void fireMoved() {
        fireFigureMoved();
        fireCoordinateSystemChanged();
    }

    /**
     * Notifies any {@link PropertyChangeListener PropertyChangeListeners}
     * listening to this Figure that the boolean property with id
     * <i>property</i> has changed.
     * 
     * @param property
     *            The id of the property that changed
     * @param old
     *            The old value of the changed property
     * @param current
     *            The current value of the changed property
     * @since 2.0
     */
    protected void firePropertyChange(String property, boolean old,
            boolean current) {
        if (propertyListeners == null)
            return;
        propertyListeners.firePropertyChange(property, old, current);
    }

    /**
     * Notifies any {@link PropertyChangeListener PropertyChangeListeners}
     * listening to this figure that the Object property with id <i>property</i>
     * has changed.
     * 
     * @param property
     *            The id of the property that changed
     * @param old
     *            The old value of the changed property
     * @param current
     *            The current value of the changed property
     * @since 2.0
     */
    protected void firePropertyChange(String property, Object old,
            Object current) {
        if (propertyListeners == null)
            return;
        propertyListeners.firePropertyChange(property, old, current);
    }

    /**
     * Notifies any {@link PropertyChangeListener PropertyChangeListeners}
     * listening to this figure that the integer property with id
     * <code>property</code> has changed.
     * 
     * @param property
     *            The id of the property that changed
     * @param old
     *            The old value of the changed property
     * @param current
     *            The current value of the changed property
     * @since 2.0
     */
    protected void firePropertyChange(String property, int old, int current) {
        if (propertyListeners == null)
            return;
        propertyListeners.firePropertyChange(property, old, current);
    }

    /**
     * Returns this Figure's background color. If this Figure's background color
     * is <code>null</code> and its parent is not <code>null</code>, the
     * background color is inherited from the parent.
     * 
     * @see IFigure#getBackgroundColor()
     */
    @Override
    public Color getBackgroundColor() {
        if (bgColor == null && getParent() != null)
            return getParent().getBackgroundColor();
        return bgColor;
    }

    /**
     * @see IFigure#getBorder()
     */
    @Override
    public Border getBorder() {
        return border;
    }

    /**
     * Returns the smallest rectangle completely enclosing the figure.
     * Implementors may return the Rectangle by reference. For this reason,
     * callers of this method must not modify the returned Rectangle.
     * 
     * @return The bounds of this Figure
     */
    @Override
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * @see IFigure#getChildren()
     */
    @Override
    public List getChildren() {
        return children;
    }

    /**
     * @see IFigure#getClientArea(Rectangle)
     */
    @Override
    public Rectangle getClientArea(Rectangle rect) {
        rect.setBounds(getBounds());
        rect.crop(getInsets());
        if (useLocalCoordinates())
            rect.setLocation(0, 0);
        return rect;
    }

    /**
     * @see IFigure#getClientArea()
     */
    @Override
    public final Rectangle getClientArea() {
        return getClientArea(new Rectangle());
    }

    /**
     * Returns the IClippingStrategy used by this figure to clip its children
     * 
     * @return the IClipppingStrategy used to clip this figure's children.
     * @since 3.6
     */
    @Override
    public IClippingStrategy getClippingStrategy() {
        return clippingStrategy;
    }

    /**
     * @see IFigure#getCursor()
     */
    @Override
    public Cursor getCursor() {
        if (cursor == null && getParent() != null)
            return getParent().getCursor();
        return cursor;
    }

    /**
     * Returns the value of the given flag.
     * 
     * @param flag
     *            The flag to get
     * @return The value of the given flag
     */
    protected boolean getFlag(int flag) {
        return (flags & flag) != 0;
    }

    /**
     * @see IFigure#getFont()
     */
    @Override
    public Font getFont() {
        if (font != null)
            return font;
        if (getParent() != null)
            return getParent().getFont();
        return null;
    }

    /**
     * @see IFigure#getForegroundColor()
     */
    @Override
    public Color getForegroundColor() {
        if (fgColor == null && getParent() != null)
            return getParent().getForegroundColor();
        return fgColor;
    }

    /**
     * Returns the border's Insets if the border is set. Otherwise returns
     * NO_INSETS, an instance of Insets with all 0s. Returns Insets by
     * reference. DO NOT Modify returned value. Cannot return null.
     * 
     * @return This Figure's Insets
     */
    @Override
    public Insets getInsets() {
        if (getBorder() != null)
            return getBorder().getInsets(this);
        return NO_INSETS;
    }

    /**
     * @see IFigure#getLayoutManager()
     */
    @Override
    public LayoutManager getLayoutManager() {
        if (layoutManager instanceof LayoutNotifier)
            return ((LayoutNotifier) layoutManager).realLayout;
        return layoutManager;
    }

    /**
     * Returns an Iterator over the listeners of type <i>clazz</i> that are
     * listening to this Figure. If there are no listeners of type <i>clazz</i>,
     * an empty iterator is returned.
     * 
     * @param clazz
     *            The type of listeners to get
     * @return An Iterator over the requested listeners
     * @since 2.0
     */
    protected Iterator getListeners(Class clazz) {
        if (eventListeners == null)
            return Collections.EMPTY_LIST.iterator();
        return eventListeners.getListeners(clazz);
    }

    /**
     * Returns <code>null</code> or the local background Color of this Figure.
     * Does not inherit this Color from the parent.
     * 
     * @return bgColor <code>null</code> or the local background Color
     */
    @Override
    public Color getLocalBackgroundColor() {
        return bgColor;
    }

    /**
     * Returns <code>null</code> or the local font setting for this figure. Does
     * not return values inherited from the parent figure.
     * 
     * @return <code>null</code> or the local font
     * @since 3.1
     */
    protected Font getLocalFont() {
        return font;
    }

    /**
     * Returns <code>null</code> or the local foreground Color of this Figure.
     * Does not inherit this Color from the parent.
     * 
     * @return fgColor <code>null</code> or the local foreground Color
     */
    @Override
    public Color getLocalForegroundColor() {
        return fgColor;
    }

    /**
     * Returns the top-left corner of this Figure's bounds.
     * 
     * @return The top-left corner of this Figure's bounds
     * @since 2.0
     */
    public final Point getLocation() {
        return getBounds().getLocation();
    }

    /**
     * @see IFigure#getMaximumSize()
     */
    @Override
    public Dimension getMaximumSize() {
        if (maxSize != null)
            return maxSize;
        return MAX_DIMENSION;
    }

    /**
     * @see IFigure#getMinimumSize()
     */
    @Override
    public final Dimension getMinimumSize() {
        return getMinimumSize(-1, -1);
    }

    /**
     * @see IFigure#getMinimumSize(int, int)
     */
    @Override
    public Dimension getMinimumSize(int wHint, int hHint) {
        if (minSize != null)
            return minSize;
        if (getLayoutManager() != null) {
            Dimension d = getLayoutManager().getMinimumSize(this, wHint, hHint);
            if (d != null)
                return d;
        }
        return getPreferredSize(wHint, hHint);
    }

    /**
     * @see IFigure#getParent()
     */
    @Override
    public IFigure getParent() {
        return parent;
    }

    /**
     * @see IFigure#getPreferredSize()
     */
    @Override
    public final Dimension getPreferredSize() {
        return getPreferredSize(-1, -1);
    }

    /**
     * @see IFigure#getPreferredSize(int, int)
     */
    @Override
    public Dimension getPreferredSize(int wHint, int hHint) {
        if (prefSize != null)
            return prefSize;
        if (getLayoutManager() != null) {
            Dimension d = getLayoutManager().getPreferredSize(this, wHint,
                    hHint);
            if (d != null)
                return d;
        }
        return getSize();
    }

    /**
     * @see IFigure#getSize()
     */
    @Override
    public final Dimension getSize() {
        return getBounds().getSize();
    }

    /**
     * @see IFigure#getToolTip()
     */
    @Override
    public IFigure getToolTip() {
        return toolTip;
    }

    /**
     * @see IFigure#getUpdateManager()
     */
    @Override
    public UpdateManager getUpdateManager() {
        if (getParent() != null)
            return getParent().getUpdateManager();
        // Only happens when the figure has not been realized
        return NO_MANAGER;
    }

    /**
     * @see IFigure#handleFocusGained(FocusEvent)
     */
    @Override
    public void handleFocusGained(FocusEvent event) {
        Iterator iter = eventListeners.getListeners(FocusListener.class);
        while (iter.hasNext())
            ((FocusListener) iter.next()).focusGained(event);
    }

    /**
     * @see IFigure#handleFocusLost(FocusEvent)
     */
    @Override
    public void handleFocusLost(FocusEvent event) {
        Iterator iter = eventListeners.getListeners(FocusListener.class);
        while (iter.hasNext())
            ((FocusListener) iter.next()).focusLost(event);
    }

    /**
     * @see IFigure#handleKeyPressed(KeyEvent)
     */
    @Override
    public void handleKeyPressed(KeyEvent event) {
        Iterator iter = eventListeners.getListeners(KeyListener.class);
        while (!event.isConsumed() && iter.hasNext())
            ((KeyListener) iter.next()).keyPressed(event);
    }

    /**
     * @see IFigure#handleKeyReleased(KeyEvent)
     */
    @Override
    public void handleKeyReleased(KeyEvent event) {
        Iterator iter = eventListeners.getListeners(KeyListener.class);
        while (!event.isConsumed() && iter.hasNext())
            ((KeyListener) iter.next()).keyReleased(event);
    }

    /**
     * @see IFigure#handleMouseDoubleClicked(MouseEvent)
     */
    @Override
    public void handleMouseDoubleClicked(MouseEvent event) {
        Iterator iter = eventListeners.getListeners(MouseListener.class);
        while (!event.isConsumed() && iter.hasNext())
            ((MouseListener) iter.next()).mouseDoubleClicked(event);
    }

    /**
     * @see IFigure#handleMouseDragged(MouseEvent)
     */
    @Override
    public void handleMouseDragged(MouseEvent event) {
        Iterator iter = eventListeners.getListeners(MouseMotionListener.class);
        while (!event.isConsumed() && iter.hasNext())
            ((MouseMotionListener) iter.next()).mouseDragged(event);
    }

    /**
     * @see IFigure#handleMouseEntered(MouseEvent)
     */
    @Override
    public void handleMouseEntered(MouseEvent event) {
        Iterator iter = eventListeners.getListeners(MouseMotionListener.class);
        while (!event.isConsumed() && iter.hasNext())
            ((MouseMotionListener) iter.next()).mouseEntered(event);
    }

    /**
     * @see IFigure#handleMouseExited(MouseEvent)
     */
    @Override
    public void handleMouseExited(MouseEvent event) {
        Iterator iter = eventListeners.getListeners(MouseMotionListener.class);
        while (!event.isConsumed() && iter.hasNext())
            ((MouseMotionListener) iter.next()).mouseExited(event);
    }

    /**
     * @see IFigure#handleMouseHover(MouseEvent)
     */
    @Override
    public void handleMouseHover(MouseEvent event) {
        Iterator iter = eventListeners.getListeners(MouseMotionListener.class);
        while (!event.isConsumed() && iter.hasNext())
            ((MouseMotionListener) iter.next()).mouseHover(event);
    }

    /**
     * @see IFigure#handleMouseMoved(MouseEvent)
     */
    @Override
    public void handleMouseMoved(MouseEvent event) {
        Iterator iter = eventListeners.getListeners(MouseMotionListener.class);
        while (!event.isConsumed() && iter.hasNext())
            ((MouseMotionListener) iter.next()).mouseMoved(event);
    }

    /**
     * @see IFigure#handleMousePressed(MouseEvent)
     */
    @Override
    public void handleMousePressed(MouseEvent event) {
        Iterator iter = eventListeners.getListeners(MouseListener.class);
        while (!event.isConsumed() && iter.hasNext())
            ((MouseListener) iter.next()).mousePressed(event);
    }

    /**
     * @see IFigure#handleMouseReleased(MouseEvent)
     */
    @Override
    public void handleMouseReleased(MouseEvent event) {
        Iterator iter = eventListeners.getListeners(MouseListener.class);
        while (!event.isConsumed() && iter.hasNext())
            ((MouseListener) iter.next()).mouseReleased(event);
    }

    /**
     * @see IFigure#hasFocus()
     */
    @Override
    public boolean hasFocus() {
        EventDispatcher dispatcher = internalGetEventDispatcher();
        if (dispatcher == null)
            return false;
        return dispatcher.getFocusOwner() == this;
    }

    /**
     * @see IFigure#internalGetEventDispatcher()
     */
    @Override
    public EventDispatcher internalGetEventDispatcher() {
        if (getParent() != null)
            return getParent().internalGetEventDispatcher();
        return null;
    }

    /**
     * @see IFigure#intersects(Rectangle)
     */
    @Override
    public boolean intersects(Rectangle rect) {
        return getBounds().intersects(rect);
    }

    /**
     * @see IFigure#invalidate()
     */
    @Override
    public void invalidate() {
        if (layoutManager != null)
            layoutManager.invalidate();
        setValid(false);
    }

    /**
     * @see IFigure#invalidateTree()
     */
    @Override
    public void invalidateTree() {
        invalidate();
        for (Iterator iter = children.iterator(); iter.hasNext();) {
            IFigure child = (IFigure) iter.next();
            child.invalidateTree();
        }
    }

    /**
     * @see IFigure#isCoordinateSystem()
     */
    @Override
    public boolean isCoordinateSystem() {
        return useLocalCoordinates();
    }

    /**
     * @see IFigure#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return (flags & FLAG_ENABLED) != 0;
    }

    /**
     * @see IFigure#isFocusTraversable()
     */
    @Override
    public boolean isFocusTraversable() {
        return (flags & FLAG_FOCUS_TRAVERSABLE) != 0;
    }

    /**
     * Returns <code>true</code> if this Figure can receive {@link MouseEvent
     * MouseEvents}.
     * 
     * @return <code>true</code> if this Figure can receive {@link MouseEvent
     *         MouseEvents}
     * @since 2.0
     */
    protected boolean isMouseEventTarget() {
        return (eventListeners.containsListener(MouseListener.class) || eventListeners
                .containsListener(MouseMotionListener.class));
    }

    /**
     * @see org.eclipse.draw2d.IFigure#isMirrored()
     */
    @Override
    public boolean isMirrored() {
        if (getParent() != null)
            return getParent().isMirrored();
        return false;
    }

    /**
     * @see IFigure#isOpaque()
     */
    @Override
    public boolean isOpaque() {
        return (flags & FLAG_OPAQUE) != 0;
    }

    /**
     * @see IFigure#isRequestFocusEnabled()
     */
    @Override
    public boolean isRequestFocusEnabled() {
        return (flags & FLAG_FOCUSABLE) != 0;
    }

    /**
     * @see IFigure#isShowing()
     */
    @Override
    public boolean isShowing() {
        return isVisible() && (getParent() == null || getParent().isShowing());
    }

    /**
     * Returns <code>true</code> if this Figure is valid.
     * 
     * @return <code>true</code> if this Figure is valid
     * @since 2.0
     */
    protected boolean isValid() {
        return (flags & FLAG_VALID) != 0;
    }

    /**
     * Returns <code>true</code> if revalidating this Figure does not require
     * revalidating its parent.
     * 
     * @return <code>true</code> if revalidating this Figure doesn't require
     *         revalidating its parent.
     * @since 2.0
     */
    protected boolean isValidationRoot() {
        return false;
    }

    /**
     * @see IFigure#isVisible()
     */
    @Override
    public boolean isVisible() {
        return getFlag(FLAG_VISIBLE);
    }

    /**
     * Lays out this Figure using its {@link LayoutManager}.
     * 
     * @since 2.0
     */
    protected void layout() {
        if (layoutManager != null)
            layoutManager.layout(this);
    }

    /**
     * Paints this Figure and its children.
     * 
     * @param graphics
     *            The Graphics object used for painting
     * @see #paintFigure(Graphics)
     * @see #paintClientArea(Graphics)
     * @see #paintBorder(Graphics)
     */
    @Override
    public void paint(Graphics graphics) {
        if (getLocalBackgroundColor() != null)
            graphics.setBackgroundColor(getLocalBackgroundColor());
        if (getLocalForegroundColor() != null)
            graphics.setForegroundColor(getLocalForegroundColor());
        if (font != null)
            graphics.setFont(font);

        graphics.pushState();
        try {
            paintFigure(graphics);
            graphics.restoreState();
            paintClientArea(graphics);
            paintBorder(graphics);
        } finally {
            graphics.popState();
        }
    }

    /**
     * Paints the border associated with this Figure, if one exists.
     * 
     * @param graphics
     *            The Graphics used to paint
     * @see Border#paint(IFigure, Graphics, Insets)
     * @since 2.0
     */
    protected void paintBorder(Graphics graphics) {
        if (getBorder() != null)
            getBorder().paint(this, graphics, NO_INSETS);
    }

    /**
     * Paints this Figure's children. The caller must save the state of the
     * graphics prior to calling this method, such that
     * <code>graphics.restoreState()</code> may be called safely, and doing so
     * will return the graphics to its original state when the method was
     * entered.
     * <P>
     * This method must leave the Graphics in its original state upon return.
     * 
     * @param graphics
     *            the graphics used to paint
     * @since 2.0
     */
    protected void paintChildren(Graphics graphics) {
        for (int i = 0; i < children.size(); i++) {
            IFigure child = (IFigure) children.get(i);
            if (child.isVisible()) {
                // determine clipping areas for child
                Rectangle[] clipping = null;
                if (clippingStrategy != null) {
                    clipping = clippingStrategy.getClip(child);
                } else {
                    // default clipping behaviour is to clip at bounds
                    clipping = new Rectangle[] { child.getBounds() };
                }
                // child may now paint inside the clipping areas
                for (int j = 0; j < clipping.length; j++) {
                    if (clipping[j].intersects(graphics
                            .getClip(Rectangle.SINGLETON))) {
                        graphics.clipRect(clipping[j]);
                        child.paint(graphics);
                        graphics.restoreState();
                    }
                }
            }
        }
    }

    /**
     * Paints this Figure's client area. The client area is typically defined as
     * the anything inside the Figure's {@link Border} or {@link Insets}, and by
     * default includes the children of this Figure. On return, this method must
     * leave the given Graphics in its initial state.
     * 
     * @param graphics
     *            The Graphics used to paint
     * @since 2.0
     */
    protected void paintClientArea(Graphics graphics) {
        if (children.isEmpty())
            return;

        boolean optimizeClip = getBorder() == null || getBorder().isOpaque();

        if (useLocalCoordinates()) {
            graphics.translate(getBounds().x + getInsets().left, getBounds().y
                    + getInsets().top);
            if (!optimizeClip)
                graphics.clipRect(getClientArea(PRIVATE_RECT));
            graphics.pushState();
            paintChildren(graphics);
            graphics.popState();
            graphics.restoreState();
        } else {
            if (optimizeClip)
                paintChildren(graphics);
            else {
                graphics.clipRect(getClientArea(PRIVATE_RECT));
                graphics.pushState();
                paintChildren(graphics);
                graphics.popState();
                graphics.restoreState();
            }
        }
    }

    /**
     * Paints this Figure's primary representation, or background. Changes made
     * to the graphics to the graphics current state will not affect the
     * subsequent calls to {@link #paintClientArea(Graphics)} and
     * {@link #paintBorder(Graphics)}. Furthermore, it is safe to call
     * <code>graphics.restoreState()</code> within this method, and doing so
     * will restore the graphics to its original state upon entry.
     * 
     * @param graphics
     *            The Graphics used to paint
     * @since 2.0
     */
    protected void paintFigure(Graphics graphics) {
        if (isOpaque())
            graphics.fillRectangle(getBounds());
        if (getBorder() instanceof AbstractBackground)
            ((AbstractBackground) getBorder()).paintBackground(this, graphics,
                    NO_INSETS);
    }

    /**
     * Translates this Figure's bounds, without firing a move.
     * 
     * @param dx
     *            The amount to translate horizontally
     * @param dy
     *            The amount to translate vertically
     * @see #translate(int, int)
     * @since 2.0
     */
    protected void primTranslate(int dx, int dy) {
        bounds.x += dx;
        bounds.y += dy;
        if (useLocalCoordinates()) {
            fireCoordinateSystemChanged();
            return;
        }
        for (int i = 0; i < children.size(); i++)
            ((IFigure) children.get(i)).translate(dx, dy);
    }

    /**
     * Removes the given child Figure from this Figure's hierarchy and
     * revalidates this Figure. The child Figure's {@link #removeNotify()}
     * method is also called.
     * 
     * @param figure
     *            The Figure to remove
     */
    @Override
    public void remove(IFigure figure) {
        if ((figure.getParent() != this))
            throw new IllegalArgumentException("Figure is not a child"); //$NON-NLS-1$
        if (getFlag(FLAG_REALIZED))
            figure.removeNotify();
        if (layoutManager != null)
            layoutManager.remove(figure);
        // The updates in the UpdateManager *have* to be
        // done asynchronously, else will result in
        // incorrect dirty region corrections.
        figure.erase();
        figure.setParent(null);
        children.remove(figure);
        revalidate();
    }

    /**
     * Removes all children from this Figure.
     * 
     * @see #remove(IFigure)
     * @since 2.0
     */
    public void removeAll() {
        List list = new ArrayList(getChildren());
        for (int i = 0; i < list.size(); i++) {
            remove((IFigure) list.get(i));
        }
    }

    /**
     * @see IFigure#removeAncestorListener(AncestorListener)
     */
    @Override
    public void removeAncestorListener(AncestorListener listener) {
        if (ancestorHelper != null) {
            ancestorHelper.removeAncestorListener(listener);
            if (ancestorHelper.isEmpty()) {
                ancestorHelper.dispose();
                ancestorHelper = null;
            }
        }
    }

    /**
     * @see IFigure#removeCoordinateListener(CoordinateListener)
     */
    @Override
    public void removeCoordinateListener(CoordinateListener listener) {
        eventListeners.removeListener(CoordinateListener.class, listener);
    }

    /**
     * @see IFigure#removeFigureListener(FigureListener)
     */
    @Override
    public void removeFigureListener(FigureListener listener) {
        eventListeners.removeListener(FigureListener.class, listener);
    }

    /**
     * @see IFigure#removeFocusListener(FocusListener)
     */
    @Override
    public void removeFocusListener(FocusListener listener) {
        eventListeners.removeListener(FocusListener.class, listener);
    }

    /**
     * @see IFigure#removeKeyListener(KeyListener)
     */
    @Override
    public void removeKeyListener(KeyListener listener) {
        eventListeners.removeListener(KeyListener.class, listener);
    }

    /**
     * Removes the first occurence of the given listener.
     * 
     * @since 3.1
     * @param listener
     *            the listener being removed
     */
    @Override
    public void removeLayoutListener(LayoutListener listener) {
        if (layoutManager instanceof LayoutNotifier) {
            LayoutNotifier notifier = (LayoutNotifier) layoutManager;
            notifier.listeners.remove(listener);
            if (notifier.listeners.isEmpty())
                layoutManager = notifier.realLayout;
        }
    }

    /**
     * Removes <i>listener</i> of type <i>clazz</i> from this Figure's list of
     * listeners.
     * 
     * @param clazz
     *            The type of listener
     * @param listener
     *            The listener to remove
     * @since 2.0
     */
    protected void removeListener(Class clazz, Object listener) {
        if (eventListeners == null)
            return;
        eventListeners.removeListener(clazz, listener);
    }

    /**
     * @see IFigure#removeMouseListener(MouseListener)
     */
    @Override
    public void removeMouseListener(MouseListener listener) {
        eventListeners.removeListener(MouseListener.class, listener);
    }

    /**
     * @see IFigure#removeMouseMotionListener(MouseMotionListener)
     */
    @Override
    public void removeMouseMotionListener(MouseMotionListener listener) {
        eventListeners.removeListener(MouseMotionListener.class, listener);
    }

    /**
     * Called prior to this figure's removal from its parent
     */
    @Override
    public void removeNotify() {
        for (int i = 0; i < children.size(); i++)
            ((IFigure) children.get(i)).removeNotify();
        if (internalGetEventDispatcher() != null)
            internalGetEventDispatcher().requestRemoveFocus(this);
        setFlag(FLAG_REALIZED, false);
    }

    /**
     * @see IFigure#removePropertyChangeListener(PropertyChangeListener)
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (propertyListeners == null)
            return;
        propertyListeners.removePropertyChangeListener(listener);
    }

    /**
     * @see IFigure#removePropertyChangeListener(String, PropertyChangeListener)
     */
    @Override
    public void removePropertyChangeListener(String property,
            PropertyChangeListener listener) {
        if (propertyListeners == null)
            return;
        propertyListeners.removePropertyChangeListener(property, listener);
    }

    /**
     * @see IFigure#repaint(Rectangle)
     */
    @Override
    public final void repaint(Rectangle rect) {
        repaint(rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * @see IFigure#repaint(int, int, int, int)
     */
    @Override
    public void repaint(int x, int y, int w, int h) {
        if (isVisible())
            getUpdateManager().addDirtyRegion(this, x, y, w, h);
    }

    /**
     * @see IFigure#repaint()
     */
    @Override
    public void repaint() {
        repaint(getBounds());
    }

    /**
     * @see IFigure#requestFocus()
     */
    @Override
    public final void requestFocus() {
        if (!isRequestFocusEnabled() || hasFocus())
            return;
        EventDispatcher dispatcher = internalGetEventDispatcher();
        if (dispatcher == null)
            return;
        dispatcher.requestFocus(this);
    }

    /**
     * @see IFigure#revalidate()
     */
    @Override
    public void revalidate() {
        invalidate();
        if (getParent() == null || isValidationRoot())
            getUpdateManager().addInvalidFigure(this);
        else
            getParent().revalidate();
    }

    /**
     * @see IFigure#setBackgroundColor(Color)
     */
    @Override
    public void setBackgroundColor(Color bg) {
        // Set background color to bg unless in high contrast mode.
        // In that case, get the color from system
        if (bgColor != null && bgColor.equals(bg))
            return;
        Display display = Display.getCurrent();
        if (display == null) {
            display = Display.getDefault();
        }
        Color highContrastClr = null;
        try {
            if (display.getHighContrast()) {
                highContrastClr = display
                        .getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
            }
        } catch (SWTException e) {
            highContrastClr = null;
        }
        bgColor = highContrastClr == null ? bg : highContrastClr;
        repaint();
    }

    /**
     * @see IFigure#setBorder(Border)
     */
    @Override
    public void setBorder(Border border) {
        this.border = border;
        revalidate();
        repaint();
    }

    /**
     * Sets the bounds of this Figure to the Rectangle <i>rect</i>. Note that
     * <i>rect</i> is compared to the Figure's current bounds to determine what
     * needs to be repainted and/or exposed and if validation is required. Since
     * {@link #getBounds()} may return the current bounds by reference, it is
     * not safe to modify that Rectangle and then call setBounds() after making
     * modifications. The figure would assume that the bounds are unchanged, and
     * no layout or paint would occur. For proper behavior, always use a copy.
     * 
     * @param rect
     *            The new bounds
     * @since 2.0
     */
    @Override
    public void setBounds(Rectangle rect) {
        int x = bounds.x, y = bounds.y;

        boolean resize = (rect.width != bounds.width)
                || (rect.height != bounds.height), translate = (rect.x != x)
                || (rect.y != y);

        if ((resize || translate) && isVisible())
            erase();
        if (translate) {
            int dx = rect.x - x;
            int dy = rect.y - y;
            primTranslate(dx, dy);
        }

        bounds.width = rect.width;
        bounds.height = rect.height;

        if (translate || resize) {
            if (resize)
                invalidate();
            fireFigureMoved();
            repaint();
        }
    }

    /**
     * Sets the direction of any {@link Orientable} children. Allowable values
     * for <code>dir</code> are found in {@link PositionConstants}.
     * 
     * @param direction
     *            The direction
     * @see Orientable#setDirection(int)
     * @since 2.0
     */
    protected void setChildrenDirection(int direction) {
        FigureIterator iterator = new FigureIterator(this);
        IFigure child;
        while (iterator.hasNext()) {
            child = iterator.nextFigure();
            if (child instanceof Orientable)
                ((Orientable) child).setDirection(direction);
        }
    }

    /**
     * Sets all childrens' enabled property to <i>value</i>.
     * 
     * @param value
     *            The enable value
     * @see #setEnabled(boolean)
     * @since 2.0
     */
    protected void setChildrenEnabled(boolean value) {
        FigureIterator iterator = new FigureIterator(this);
        while (iterator.hasNext())
            iterator.nextFigure().setEnabled(value);
    }

    /**
     * Sets the orientation of any {@link Orientable} children. Allowable values
     * for <i>orientation</i> are found in {@link PositionConstants}.
     * 
     * @param orientation
     *            The Orientation
     * @see Orientable#setOrientation(int)
     * @since 2.0
     */
    protected void setChildrenOrientation(int orientation) {
        FigureIterator iterator = new FigureIterator(this);
        IFigure child;
        while (iterator.hasNext()) {
            child = iterator.nextFigure();
            if (child instanceof Orientable)
                ((Orientable) child).setOrientation(orientation);
        }
    }

    /**
     * @see IFigure#setConstraint(IFigure, Object)
     */
    @Override
    public void setConstraint(IFigure child, Object constraint) {
        if (child.getParent() != this)
            throw new IllegalArgumentException("Figure must be a child"); //$NON-NLS-1$

        if (layoutManager != null)
            layoutManager.setConstraint(child, constraint);
        revalidate();
    }

    /**
     * Registers a clipping strategy to specify how clipping is performed for
     * child figures.
     * 
     * @param clippingStrategy
     * @since 3.6
     */
    @Override
    public void setClippingStrategy(IClippingStrategy clippingStrategy) {
        this.clippingStrategy = clippingStrategy;
    }

    /**
     * @see IFigure#setCursor(Cursor)
     */
    @Override
    public void setCursor(Cursor cursor) {
        if (this.cursor == cursor)
            return;
        this.cursor = cursor;
        EventDispatcher dispatcher = internalGetEventDispatcher();
        if (dispatcher != null)
            dispatcher.updateCursor();
    }

    /**
     * @see IFigure#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean value) {
        if (isEnabled() == value)
            return;
        setFlag(FLAG_ENABLED, value);
    }

    /**
     * Sets the given flag to the given value.
     * 
     * @param flag
     *            The flag to set
     * @param value
     *            The value
     * @since 2.0
     */
    protected final void setFlag(int flag, boolean value) {
        if (value)
            flags |= flag;
        else
            flags &= ~flag;
    }

    /**
     * @see IFigure#setFocusTraversable(boolean)
     */
    @Override
    public void setFocusTraversable(boolean focusTraversable) {
        if (isFocusTraversable() == focusTraversable)
            return;
        setFlag(FLAG_FOCUS_TRAVERSABLE, focusTraversable);
    }

    /**
     * @see IFigure#setFont(Font)
     */
    @Override
    public void setFont(Font f) {
        if (font != f) {
            font = f;
            revalidate();
            repaint();
        }
    }

    /**
     * @see IFigure#setForegroundColor(Color)
     */
    @Override
    public void setForegroundColor(Color fg) {
        // Set foreground color to fg unless in high contrast mode.
        // In that case, get the color from system
        if (fgColor != null && fgColor.equals(fg))
            return;
        Display display = Display.getCurrent();
        if (display == null) {
            display = Display.getDefault();
        }
        Color highContrastClr = null;
        try {
            if (display.getHighContrast()) {
                highContrastClr = display
                        .getSystemColor(SWT.COLOR_WIDGET_FOREGROUND);
            }
        } catch (SWTException e) {
            highContrastClr = null;
        }
        fgColor = highContrastClr == null ? fg : highContrastClr;
        repaint();
    }

    /**
     * @see IFigure#setLayoutManager(LayoutManager)
     */
    @Override
    public void setLayoutManager(LayoutManager manager) {
        if (layoutManager instanceof LayoutNotifier)
            ((LayoutNotifier) layoutManager).realLayout = manager;
        else
            layoutManager = manager;
        revalidate();
    }

    /**
     * @see IFigure#setLocation(Point)
     */
    @Override
    public void setLocation(Point p) {
        if (getLocation().equals(p))
            return;
        Rectangle r = new Rectangle(getBounds());
        r.setLocation(p);
        setBounds(r);
    }

    /**
     * @see IFigure#setMaximumSize(Dimension)
     */
    @Override
    public void setMaximumSize(Dimension d) {
        if (maxSize != null && maxSize.equals(d))
            return;
        maxSize = d;
        revalidate();
    }

    /**
     * @see IFigure#setMinimumSize(Dimension)
     */
    @Override
    public void setMinimumSize(Dimension d) {
        if (minSize != null && minSize.equals(d))
            return;
        minSize = d;
        revalidate();
    }

    /**
     * @see IFigure#setOpaque(boolean)
     */
    @Override
    public void setOpaque(boolean opaque) {
        if (isOpaque() == opaque)
            return;
        setFlag(FLAG_OPAQUE, opaque);
        repaint();
    }

    /**
     * @see IFigure#setParent(IFigure)
     */
    @Override
    public void setParent(IFigure p) {
        IFigure oldParent = parent;
        parent = p;
        firePropertyChange("parent", oldParent, p);//$NON-NLS-1$
    }

    /**
     * @see IFigure#setPreferredSize(Dimension)
     */
    @Override
    public void setPreferredSize(Dimension size) {
        if (prefSize != null && prefSize.equals(size))
            return;
        prefSize = size;
        revalidate();
    }

    /**
     * Sets the preferred size of this figure.
     * 
     * @param w
     *            The new preferred width
     * @param h
     *            The new preferred height
     * @see #setPreferredSize(Dimension)
     * @since 2.0
     */
    public final void setPreferredSize(int w, int h) {
        setPreferredSize(new Dimension(w, h));
    }

    /**
     * @see IFigure#setRequestFocusEnabled(boolean)
     */
    @Override
    public void setRequestFocusEnabled(boolean requestFocusEnabled) {
        if (isRequestFocusEnabled() == requestFocusEnabled)
            return;
        setFlag(FLAG_FOCUSABLE, requestFocusEnabled);
    }

    /**
     * @see IFigure#setSize(Dimension)
     */
    @Override
    public final void setSize(Dimension d) {
        setSize(d.width, d.height);
    }

    /**
     * @see IFigure#setSize(int, int)
     */
    @Override
    public void setSize(int w, int h) {
        Rectangle bounds = getBounds();
        if (bounds.width == w && bounds.height == h)
            return;
        Rectangle r = new Rectangle(getBounds());
        r.setSize(w, h);
        setBounds(r);
    }

    /**
     * @see IFigure#setToolTip(IFigure)
     */
    @Override
    public void setToolTip(IFigure f) {
        if (toolTip == f)
            return;
        toolTip = f;
    }

    /**
     * Sets this figure to be valid if <i>value</i> is <code>true</code> and
     * invalid otherwise.
     * 
     * @param value
     *            The valid value
     * @since 2.0
     */
    public void setValid(boolean value) {
        setFlag(FLAG_VALID, value);
    }

    /**
     * @see IFigure#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        boolean currentVisibility = isVisible();
        if (visible == currentVisibility)
            return;
        if (currentVisibility)
            erase();
        setFlag(FLAG_VISIBLE, visible);
        if (visible)
            repaint();
        revalidate();
    }

    /**
     * @see IFigure#translate(int, int)
     */
    @Override
    public final void translate(int x, int y) {
        primTranslate(x, y);
        fireFigureMoved();
    }

    /**
     * @see IFigure#translateFromParent(Translatable)
     */
    @Override
    public void translateFromParent(Translatable t) {
        if (useLocalCoordinates())
            t.performTranslate(-getBounds().x - getInsets().left,
                    -getBounds().y - getInsets().top);
    }

    /**
     * @see IFigure#translateToAbsolute(Translatable)
     */
    @Override
    public final void translateToAbsolute(Translatable t) {
        if (getParent() != null) {
            getParent().translateToParent(t);
            getParent().translateToAbsolute(t);
        }
    }

    /**
     * @see IFigure#translateToParent(Translatable)
     */
    @Override
    public void translateToParent(Translatable t) {
        if (useLocalCoordinates())
            t.performTranslate(getBounds().x + getInsets().left, getBounds().y
                    + getInsets().top);
    }

    /**
     * @see IFigure#translateToRelative(Translatable)
     */
    @Override
    public final void translateToRelative(Translatable t) {
        if (getParent() != null) {
            getParent().translateToRelative(t);
            getParent().translateFromParent(t);
        }
    }

    /**
     * Returns <code>true</code> if this Figure uses local coordinates. This
     * means its children are placed relative to this Figure's top-left corner.
     * 
     * @return <code>true</code> if this Figure uses local coordinates
     * @since 2.0
     */
    protected boolean useLocalCoordinates() {
        return false;
    }

    /**
     * @see IFigure#validate()
     */
    @Override
    public void validate() {
        if (isValid())
            return;
        setValid(true);
        layout();
        for (int i = 0; i < children.size(); i++)
            ((IFigure) children.get(i)).validate();
    }

    /**
     * A search which does not filter any figures. since 3.0
     */
    protected static final class IdentitySearch implements TreeSearch {
        /**
         * The singleton instance.
         */
        public static final TreeSearch INSTANCE = new IdentitySearch();

        private IdentitySearch() {
        }

        /**
         * Always returns <code>true</code>.
         * 
         * @see TreeSearch#accept(IFigure)
         */
        @Override
        public boolean accept(IFigure f) {
            return true;
        }

        /**
         * Always returns <code>false</code>.
         * 
         * @see TreeSearch#prune(IFigure)
         */
        @Override
        public boolean prune(IFigure f) {
            return false;
        }
    }

    final class LayoutNotifier implements LayoutManager {

        LayoutManager realLayout;
        List listeners = new ArrayList(1);

        LayoutNotifier(LayoutManager layout, LayoutListener listener) {
            realLayout = layout;
            listeners.add(listener);
        }

        @Override
        public Object getConstraint(IFigure child) {
            if (realLayout != null)
                return realLayout.getConstraint(child);
            return null;
        }

        @Override
        public Dimension getMinimumSize(IFigure container, int wHint, int hHint) {
            if (realLayout != null)
                return realLayout.getMinimumSize(container, wHint, hHint);
            return null;
        }

        @Override
        public Dimension getPreferredSize(IFigure container, int wHint,
                int hHint) {
            if (realLayout != null)
                return realLayout.getPreferredSize(container, wHint, hHint);
            return null;
        }

        @Override
        public void invalidate() {
            for (int i = 0; i < listeners.size(); i++)
                ((LayoutListener) listeners.get(i)).invalidate(Figure.this);

            if (realLayout != null)
                realLayout.invalidate();
        }

        @Override
        public void layout(IFigure container) {
            boolean consumed = false;
            for (int i = 0; i < listeners.size(); i++)
                consumed |= ((LayoutListener) listeners.get(i))
                        .layout(container);

            if (realLayout != null && !consumed)
                realLayout.layout(container);
            for (int i = 0; i < listeners.size(); i++)
                ((LayoutListener) listeners.get(i)).postLayout(container);
        }

        @Override
        public void remove(IFigure child) {
            for (int i = 0; i < listeners.size(); i++)
                ((LayoutListener) listeners.get(i)).remove(child);
            if (realLayout != null)
                realLayout.remove(child);
        }

        @Override
        public void setConstraint(IFigure child, Object constraint) {
            for (int i = 0; i < listeners.size(); i++)
                ((LayoutListener) listeners.get(i)).setConstraint(child,
                        constraint);
            if (realLayout != null)
                realLayout.setConstraint(child, constraint);
        }
    }

    /**
     * Iterates over a Figure's children.
     */
    public static class FigureIterator {
        private List list;
        private int index;

        /**
         * Constructs a new FigureIterator for the given Figure.
         * 
         * @param figure
         *            The Figure whose children to iterate over
         */
        public FigureIterator(IFigure figure) {
            list = figure.getChildren();
            index = list.size();
        }

        /**
         * Returns the next Figure.
         * 
         * @return The next Figure
         */
        public IFigure nextFigure() {
            return (IFigure) list.get(--index);
        }

        /**
         * Returns <code>true</code> if there's another Figure to iterate over.
         * 
         * @return <code>true</code> if there's another Figure to iterate over
         */
        public boolean hasNext() {
            return index > 0;
        }
    }

    /**
     * An UpdateManager that does nothing.
     */
    protected static final UpdateManager NO_MANAGER = new UpdateManager() {
        @Override
        public void addDirtyRegion(IFigure figure, int x, int y, int w, int h) {
        }

        @Override
        public void addInvalidFigure(IFigure f) {
        }

        @Override
        public void performUpdate() {
        }

        @Override
        public void performUpdate(Rectangle region) {
        }

        @Override
        public void setRoot(IFigure root) {
        }

        @Override
        public void setGraphicsSource(GraphicsSource gs) {
        }
    };

}
