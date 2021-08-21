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

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleControlListener;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.accessibility.AccessibleListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Control;

/**
 * The SWTEventDispatcher provides draw2d with the ability to dispatch SWT
 * Events. The {@link org.eclipse.draw2d.LightweightSystem} adds SWT event
 * listeners on its Canvas. When the Canvas receives an SWT event, it calls the
 * appropriate dispatcher method in SWTEventDispatcher.
 */
public class SWTEventDispatcher extends EventDispatcher {

    /**
     * Used to tell if any button is pressed without regard to the specific
     * button.
     * 
     * @deprecated Use {@link SWT#BUTTON_MASK} instead.
     */
    protected static final int ANY_BUTTON = SWT.BUTTON_MASK;

    private boolean figureTraverse = true;

    private boolean captured;
    private IFigure root;
    private IFigure mouseTarget;
    private IFigure cursorTarget;
    private IFigure focusOwner;
    private IFigure hoverSource;

    private MouseEvent currentEvent;
    private Cursor cursor;
    /** The control this dispatcher is listening to. */
    protected org.eclipse.swt.widgets.Control control;

    private ToolTipHelper toolTipHelper;
    private FocusTraverseManager focusManager = new FocusTraverseManager();

    /**
     * Implements {@link EventDispatcher.AccessibilityDispatcher} but does
     * nothing in the implementation.
     */
    protected class FigureAccessibilityDispatcher extends
            AccessibilityDispatcher {
        /** @see AccessibleControlListener#getChildAtPoint(AccessibleControlEvent) */
        @Override
        public void getChildAtPoint(AccessibleControlEvent e) {
        }

        /** @see AccessibleControlListener#getChildCount(AccessibleControlEvent) */
        @Override
        public void getChildCount(AccessibleControlEvent e) {
        }

        /** @see AccessibleControlListener#getChildren(AccessibleControlEvent) */
        @Override
        public void getChildren(AccessibleControlEvent e) {
        }

        /** @see AccessibleControlListener#getDefaultAction(AccessibleControlEvent) */
        @Override
        public void getDefaultAction(AccessibleControlEvent e) {
        }

        /** @see AccessibleListener#getDescription(AccessibleEvent) */
        @Override
        public void getDescription(AccessibleEvent e) {
        }

        /** @see AccessibleControlListener#getFocus(AccessibleControlEvent) */
        @Override
        public void getFocus(AccessibleControlEvent e) {
        }

        /** @see AccessibleListener#getHelp(AccessibleEvent) */
        @Override
        public void getHelp(AccessibleEvent e) {
        }

        /** @see AccessibleListener#getKeyboardShortcut(AccessibleEvent) */
        @Override
        public void getKeyboardShortcut(AccessibleEvent e) {
        }

        /** @see AccessibleControlListener#getLocation(AccessibleControlEvent) */
        @Override
        public void getLocation(AccessibleControlEvent e) {
        }

        /** @see AccessibleListener#getName(AccessibleEvent) */
        @Override
        public void getName(AccessibleEvent e) {
        }

        /** @see AccessibleControlListener#getRole(AccessibleControlEvent) */
        @Override
        public void getRole(AccessibleControlEvent e) {
        }

        /** @see AccessibleControlListener#getSelection(AccessibleControlEvent) */
        @Override
        public void getSelection(AccessibleControlEvent e) {
        }

        /** @see AccessibleControlListener#getState(AccessibleControlEvent) */
        @Override
        public void getState(AccessibleControlEvent e) {
        }

        /** @see AccessibleControlListener#getValue(AccessibleControlEvent) */
        @Override
        public void getValue(AccessibleControlEvent e) {
        }
    }

    /**
     * @see EventDispatcher#dispatchFocusGained(org.eclipse.swt.events.FocusEvent)
     */
    @Override
    public void dispatchFocusGained(org.eclipse.swt.events.FocusEvent e) {
        IFigure currentFocusOwner = getFocusTraverseManager()
                .getCurrentFocusOwner();

        /*
         * Upon focus gained, if there is no current focus owner, set focus on
         * first focusable child.
         */
        if (currentFocusOwner == null)
            currentFocusOwner = getFocusTraverseManager()
                    .getNextFocusableFigure(root, focusOwner);
        setFocus(currentFocusOwner);
    }

    /**
     * @see EventDispatcher#dispatchFocusLost(org.eclipse.swt.events.FocusEvent)
     */
    @Override
    public void dispatchFocusLost(org.eclipse.swt.events.FocusEvent e) {
        setFocus(null);
    }

    /**
     * @see EventDispatcher#dispatchKeyPressed(org.eclipse.swt.events.KeyEvent)
     */
    @Override
    public void dispatchKeyPressed(org.eclipse.swt.events.KeyEvent e) {
        if (focusOwner != null) {
            KeyEvent event = new KeyEvent(this, focusOwner, e);
            focusOwner.handleKeyPressed(event);
        }
    }

    /**
     * @see EventDispatcher#dispatchKeyReleased(org.eclipse.swt.events.KeyEvent)
     */
    @Override
    public void dispatchKeyReleased(org.eclipse.swt.events.KeyEvent e) {
        if (focusOwner != null) {
            KeyEvent event = new KeyEvent(this, focusOwner, e);
            focusOwner.handleKeyReleased(event);
        }
    }

    /**
     * @see EventDispatcher#dispatchKeyTraversed(TraverseEvent)
     */
    @Override
    public void dispatchKeyTraversed(TraverseEvent e) {
        if (!figureTraverse)
            return;
        IFigure nextFigure = null;

        if (e.detail == SWT.TRAVERSE_TAB_NEXT)
            nextFigure = getFocusTraverseManager().getNextFocusableFigure(root,
                    focusOwner);
        else if (e.detail == SWT.TRAVERSE_TAB_PREVIOUS)
            nextFigure = getFocusTraverseManager().getPreviousFocusableFigure(
                    root, focusOwner);

        if (nextFigure != null) {
            e.doit = false;
            setFocus(nextFigure);
        }
    }

    /**
     * @see EventDispatcher#dispatchMouseHover(org.eclipse.swt.events.MouseEvent)
     */
    @Override
    public void dispatchMouseHover(org.eclipse.swt.events.MouseEvent me) {
        receive(me);
        if (mouseTarget != null)
            mouseTarget.handleMouseHover(currentEvent);
        /*
         * Check Tooltip source. Get Tooltip source's Figure. Set that tooltip
         * as the lws contents on the helper.
         */
        if (hoverSource != null) {
            toolTipHelper = getToolTipHelper();
            IFigure tip = hoverSource.getToolTip();
            Control control = (Control) me.getSource();
            org.eclipse.swt.graphics.Point absolute;
            absolute = control.toDisplay(new org.eclipse.swt.graphics.Point(
                    me.x, me.y));
            toolTipHelper.displayToolTipNear(hoverSource, tip, absolute.x,
                    absolute.y);
        }
    }

    /**
     * @see EventDispatcher#dispatchMouseDoubleClicked(org.eclipse.swt.events.MouseEvent)
     */
    @Override
    public void dispatchMouseDoubleClicked(org.eclipse.swt.events.MouseEvent me) {
        receive(me);
        if (mouseTarget != null)
            mouseTarget.handleMouseDoubleClicked(currentEvent);
    }

    /**
     * @see EventDispatcher#dispatchMouseEntered(org.eclipse.swt.events.MouseEvent)
     */
    @Override
    public void dispatchMouseEntered(org.eclipse.swt.events.MouseEvent me) {
        receive(me);
    }

    /**
     * @see EventDispatcher#dispatchMouseExited(org.eclipse.swt.events.MouseEvent)
     */
    @Override
    public void dispatchMouseExited(org.eclipse.swt.events.MouseEvent me) {
        setHoverSource(null, me);
        if (mouseTarget != null) {
            currentEvent = new MouseEvent(this, mouseTarget, me);
            mouseTarget.handleMouseExited(currentEvent);
            releaseCapture();
            mouseTarget = null;
        }
    }

    /**
     * @see EventDispatcher#dispatchMousePressed(org.eclipse.swt.events.MouseEvent)
     */
    @Override
    public void dispatchMousePressed(org.eclipse.swt.events.MouseEvent me) {
        receive(me);
        if (mouseTarget != null) {
            mouseTarget.handleMousePressed(currentEvent);
            if (currentEvent.isConsumed())
                setCapture(mouseTarget);
        }
    }

    /**
     * @see EventDispatcher#dispatchMouseMoved(org.eclipse.swt.events.MouseEvent)
     */
    @Override
    public void dispatchMouseMoved(org.eclipse.swt.events.MouseEvent me) {
        receive(me);
        if (mouseTarget != null) {
            if ((me.stateMask & SWT.BUTTON_MASK) != 0)
                mouseTarget.handleMouseDragged(currentEvent);
            else
                mouseTarget.handleMouseMoved(currentEvent);
        }
    }

    /**
     * @see EventDispatcher#dispatchMouseReleased(org.eclipse.swt.events.MouseEvent)
     */
    @Override
    public void dispatchMouseReleased(org.eclipse.swt.events.MouseEvent me) {
        receive(me);
        if (mouseTarget != null) {
            mouseTarget.handleMouseReleased(currentEvent);
        }
        releaseCapture();
        receive(me);
    }

    /**
     * @see EventDispatcher#getAccessibilityDispatcher()
     */
    @Override
    protected AccessibilityDispatcher getAccessibilityDispatcher() {
        return null;
    }

    /**
     * Returns the current mouse event.
     * 
     * @return the current mouse event; can be <code>null</code>
     */
    protected MouseEvent getCurrentEvent() {
        return currentEvent;
    }

    private IFigure getCurrentToolTip() {
        if (hoverSource != null)
            return hoverSource.getToolTip();
        else
            return null;
    }

    /**
     * Returns the figure that the cursor is over.
     * 
     * @return the cursor target
     */
    protected IFigure getCursorTarget() {
        return cursorTarget;
    }

    /**
     * Returns the ToolTipHelper used to display tooltips on hover events.
     * 
     * @return the ToolTipHelper
     */
    protected ToolTipHelper getToolTipHelper() {
        if (toolTipHelper == null)
            toolTipHelper = new ToolTipHelper(control);
        return toolTipHelper;
    }

    /**
     * Returns the FocusTraverseManager which is used to determine which figure
     * will get focus when a TAB or ALT+TAB key sequence occurs.
     * 
     * @return the FocusTraverseManager
     */
    protected final FocusTraverseManager getFocusTraverseManager() {
        if (focusManager == null) {
            focusManager = new FocusTraverseManager();
        }
        return focusManager;
    }

    /**
     * @see EventDispatcher#getFocusOwner()
     * @since 3.6
     */
    @Override
    public IFigure getFocusOwner() {
        return focusOwner;
    }

    /**
     * Returns the figure that is the target of mouse events. This may not be
     * the figure beneath the cursor because another figure may have captured
     * the mouse and will continue to get mouse events until capture is
     * released.
     * 
     * @return the mouse target
     */
    protected IFigure getMouseTarget() {
        return mouseTarget;
    }

    /**
     * Returns the root figure for this dispatcher.
     * 
     * @return the root figure
     */
    protected IFigure getRoot() {
        return root;
    }

    /**
     * @see EventDispatcher#isCaptured()
     */
    @Override
    public boolean isCaptured() {
        return captured;
    }

    private void receive(org.eclipse.swt.events.MouseEvent me) {
        currentEvent = null;
        updateFigureUnderCursor(me);
        if (captured) {
            if (mouseTarget != null)
                currentEvent = new MouseEvent(this, mouseTarget, me);
        } else {
            IFigure f = root.findMouseEventTargetAt(me.x, me.y);
            if (f == mouseTarget) {
                if (mouseTarget != null)
                    currentEvent = new MouseEvent(this, mouseTarget, me);
                return;
            }
            if (mouseTarget != null) {
                currentEvent = new MouseEvent(this, mouseTarget, me);
                mouseTarget.handleMouseExited(currentEvent);
            }
            setMouseTarget(f);
            if (mouseTarget != null) {
                currentEvent = new MouseEvent(this, mouseTarget, me);
                mouseTarget.handleMouseEntered(currentEvent);
            }
        }
    }

    /**
     * @see EventDispatcher#releaseCapture()
     */
    @Override
    protected void releaseCapture() {
        captured = false;
    }

    /**
     * @see EventDispatcher#requestFocus(IFigure)
     */
    @Override
    public void requestFocus(IFigure fig) {
        setFocus(fig);
    }

    /**
     * @see EventDispatcher#requestRemoveFocus(IFigure)
     */
    @Override
    public void requestRemoveFocus(IFigure fig) {
        if (getFocusOwner() == fig)
            setFocus(null);
        if (mouseTarget == fig)
            mouseTarget = null;
        if (cursorTarget == fig)
            cursorTarget = null;
        if (hoverSource == fig)
            hoverSource = null;
        getFocusTraverseManager().setCurrentFocusOwner(null);
    }

    /**
     * @see EventDispatcher#setCapture(IFigure)
     */
    @Override
    protected void setCapture(IFigure figure) {
        captured = true;
        mouseTarget = figure;
    }

    /**
     * @see EventDispatcher#setControl(Control)
     */
    @Override
    public void setControl(Control c) {
        if (c == control)
            return;
        if (control != null && !control.isDisposed())
            throw new RuntimeException(
                    "Can not set control again once it has been set"); //$NON-NLS-1$
        if (c != null)
            c.addDisposeListener(new org.eclipse.swt.events.DisposeListener() {
                @Override
                public void widgetDisposed(DisposeEvent e) {
                    if (toolTipHelper != null)
                        toolTipHelper.dispose();
                }
            });
        control = c;
    }

    /**
     * Sets the mouse cursor.
     * 
     * @param c
     *            the new cursor
     */
    protected void setCursor(Cursor c) {
        if (c == null && cursor == null) {
            return;
        } else if ((c != cursor) || (c != null && !c.equals(cursor))) {
            cursor = c;
            if (control != null && !control.isDisposed())
                control.setCursor(c);
        }
    }

    /**
     * Enables key traversal via TAB and ALT+TAB if <i>traverse</i> is
     * <code>true</code>. Disables it otherwise.
     * 
     * @param traverse
     *            whether key traversal should be enabled
     */
    public void setEnableKeyTraversal(boolean traverse) {
        figureTraverse = traverse;
    }

    /**
     * Sets the figure under the mouse cursor.
     * 
     * @param f
     *            the new figure under the cursor
     */
    protected void setFigureUnderCursor(IFigure f) {
        if (cursorTarget == f)
            return;
        cursorTarget = f;
        updateCursor();
    }

    /**
     * Sets the focus figure. If the figure currently with focus is not
     * <code>null</code>, {@link IFigure#handleFocusLost(FocusEvent)} is called
     * on the current focused figure. If the new focus figure is not
     * <code>null</code>, this will call
     * {@link IFigure#handleFocusGained(FocusEvent)} on the new focused figure.
     * 
     * @param fig
     *            the new focus figure
     */
    protected void setFocus(IFigure fig) {
        if (fig == focusOwner)
            return;
        FocusEvent fe = new FocusEvent(focusOwner, fig);
        IFigure oldOwner = focusOwner;
        focusOwner = fig;
        if (oldOwner != null)
            oldOwner.handleFocusLost(fe);
        if (fig != null)
            getFocusTraverseManager().setCurrentFocusOwner(fig);
        if (focusOwner != null)
            focusOwner.handleFocusGained(fe);
    }

    /**
     * Sets the figure that the mouse cursor is hovering over.
     * 
     * @param figure
     *            the new hover source
     * @param me
     *            the mouse event
     */
    protected void setHoverSource(Figure figure,
            org.eclipse.swt.events.MouseEvent me) {
        hoverSource = figure;
        if (figure != null) {
            Control control = (Control) me.getSource();
            org.eclipse.swt.graphics.Point absolute;
            absolute = control.toDisplay(new org.eclipse.swt.graphics.Point(
                    me.x, me.y));
            toolTipHelper = getToolTipHelper();
            toolTipHelper.updateToolTip(hoverSource, getCurrentToolTip(),
                    absolute.x, absolute.y);
        } else if (toolTipHelper != null) {
            // Update with null to clear hoverSource in ToolTipHelper
            toolTipHelper.updateToolTip(hoverSource, getCurrentToolTip(), me.x,
                    me.y);
        }
    }

    /**
     * Sets the given figure to be the target of future mouse events.
     * 
     * @param figure
     *            the new mouse target
     */
    protected void setMouseTarget(IFigure figure) {
        mouseTarget = figure;
    }

    /**
     * @see EventDispatcher#setRoot(IFigure)
     */
    @Override
    public void setRoot(IFigure figure) {
        root = figure;
    }

    /**
     * @see EventDispatcher#updateCursor()
     */
    @Override
    protected void updateCursor() {
        Cursor newCursor = null;
        if (cursorTarget != null)
            newCursor = cursorTarget.getCursor();
        setCursor(newCursor);
    }

    /**
     * Updates the figure under the cursor, unless the mouse is captured, in
     * which case all mouse events will be routed to the figure that captured
     * the mouse.
     * 
     * @param me
     *            the mouse event
     */
    protected void updateFigureUnderCursor(org.eclipse.swt.events.MouseEvent me) {
        if (!captured) {
            IFigure f = root.findFigureAt(me.x, me.y);
            setFigureUnderCursor(f);
            if (cursorTarget != hoverSource)
                updateHoverSource(me);
        }
    }

    /**
     * Updates the figure that will receive hover events. The hover source must
     * have a tooltip. If the figure under the mouse doesn't have a tooltip set,
     * this method will walk up the ancestor hierarchy until either a figure
     * with a tooltip is found or it gets to the root figure.
     * 
     * @param me
     *            the mouse event
     */
    protected void updateHoverSource(org.eclipse.swt.events.MouseEvent me) {
        /*
         * Derive source from figure under cursor. Set the source in
         * setHoverSource(); If figure.getToolTip() is null, get parent's
         * toolTip Continue parent traversal until a toolTip is found or root is
         * reached.
         */
        if (cursorTarget != null) {
            boolean sourceFound = false;
            Figure source = (Figure) cursorTarget;
            while (!sourceFound && source.getParent() != null) {
                if (source.getToolTip() != null)
                    sourceFound = true;
                else
                    source = (Figure) source.getParent();
            }
            setHoverSource(source, me);
        } else {
            setHoverSource(null, me);
        }
    }

}
