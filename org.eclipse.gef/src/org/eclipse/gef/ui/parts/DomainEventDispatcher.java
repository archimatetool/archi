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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleControlListener;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.accessibility.AccessibleListener;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Event;

import org.eclipse.draw2d.EventDispatcher;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.InputEvent;
import org.eclipse.draw2d.SWTEventDispatcher;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;

/**
 * A special event dispatcher that will route events to the {@link EditDomain}
 * when appropriate.
 * <p>
 * IMPORTANT: This class is <EM>not</EM> intended to be used or subclassed by
 * clients.
 * 
 * @author hudsonr
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class DomainEventDispatcher extends SWTEventDispatcher {

    /**
     * The edit domain
     */
    protected EditDomain domain;
    /**
     * The viewer on which this dispatcher is created.
     */
    protected EditPartViewer viewer;
    private boolean editorCaptured = false;
    private Cursor overrideCursor;
    private Map accessibles = new HashMap();
    private EditPartAccessibilityDispatcher accessibilityDispatcher;

    /**
     * Extended accessibility support for editpart.
     * 
     * @author hudsonr
     */
    protected class EditPartAccessibilityDispatcher extends
            AccessibilityDispatcher {
        @SuppressWarnings("deprecation")
        private AccessibleEditPart get(int childID) {
            if (childID == ACC.CHILDID_SELF || childID == ACC.CHILDID_NONE)
                if (getViewer().getContents() != null)
                    return getViewer().getContents()
                            .getAdapter(AccessibleEditPart.class);
                else
                    return null;
            return (AccessibleEditPart) accessibles.get(new Integer(childID));
        }

        /**
         * @see AccessibleControlListener#getChildAtPoint(AccessibleControlEvent)
         */
        @Override
        public void getChildAtPoint(AccessibleControlEvent e) {
            org.eclipse.swt.graphics.Point p = new org.eclipse.swt.graphics.Point(
                    e.x, e.y);
            p = getViewer().getControl().toControl(p);
            EditPart part = getViewer().findObjectAt(new Point(p.x, p.y));
            if (part == null)
                return;
            AccessibleEditPart acc = part
                    .getAdapter(AccessibleEditPart.class);
            if (acc != null)
                e.childID = acc.getAccessibleID();
        }

        /**
         * @see AccessibleControlListener#getChildCount(AccessibleControlEvent)
         */
        @Override
        public void getChildCount(AccessibleControlEvent e) {
            AccessibleEditPart acc = get(e.childID);
            if (acc != null)
                acc.getChildCount(e);
        }

        /**
         * @see AccessibleControlListener#getChildren(AccessibleControlEvent)
         */
        @Override
        public void getChildren(AccessibleControlEvent e) {
            AccessibleEditPart acc = get(e.childID);
            if (acc != null)
                acc.getChildren(e);
        }

        /**
         * @see AccessibleControlListener#getDefaultAction(AccessibleControlEvent)
         */
        @Override
        public void getDefaultAction(AccessibleControlEvent e) {
            AccessibleEditPart acc = get(e.childID);
            if (acc != null)
                acc.getDefaultAction(e);
        }

        /**
         * @see AccessibleListener#getDescription(AccessibleEvent)
         */
        @Override
        public void getDescription(AccessibleEvent e) {
            AccessibleEditPart acc = get(e.childID);
            if (acc != null)
                acc.getDescription(e);
        }

        /**
         * @see AccessibleControlListener#getFocus(AccessibleControlEvent)
         */
        @Override
        public void getFocus(AccessibleControlEvent e) {
            AccessibleEditPart acc = getViewer()
                    .getFocusEditPart().getAdapter(AccessibleEditPart.class);
            if (acc != null)
                e.childID = acc.getAccessibleID();
        }

        /**
         * @see AccessibleListener#getHelp(AccessibleEvent)
         */
        @Override
        public void getHelp(AccessibleEvent e) {
            AccessibleEditPart acc = get(e.childID);
            if (acc != null)
                acc.getHelp(e);
        }

        /**
         * @see AccessibleListener#getKeyboardShortcut(AccessibleEvent)
         */
        @Override
        public void getKeyboardShortcut(AccessibleEvent e) {
            AccessibleEditPart acc = get(e.childID);
            if (acc != null)
                acc.getKeyboardShortcut(e);
        }

        /**
         * @see AccessibleControlListener#getLocation(AccessibleControlEvent)
         */
        @Override
        public void getLocation(AccessibleControlEvent e) {
            AccessibleEditPart acc = get(e.childID);
            if (acc != null)
                acc.getLocation(e);
        }

        /**
         * @see AccessibleListener#getName(AccessibleEvent)
         */
        @Override
        public void getName(AccessibleEvent e) {
            AccessibleEditPart acc = get(e.childID);
            if (acc != null)
                acc.getName(e);
        }

        /**
         * @see AccessibleControlListener#getRole(AccessibleControlEvent)
         */
        @Override
        public void getRole(AccessibleControlEvent e) {
            AccessibleEditPart acc = get(e.childID);
            if (acc != null)
                acc.getRole(e);
        }

        /**
         * @see AccessibleControlListener#getSelection(AccessibleControlEvent)
         */
        @Override
        public void getSelection(AccessibleControlEvent e) {
        }

        /**
         * @see AccessibleControlListener#getState(AccessibleControlEvent)
         */
        @Override
        public void getState(AccessibleControlEvent e) {
            AccessibleEditPart acc = get(e.childID);
            if (acc != null)
                acc.getState(e);
        }

        /**
         * @see AccessibleControlListener#getValue(AccessibleControlEvent)
         */
        @Override
        public void getValue(AccessibleControlEvent e) {
            AccessibleEditPart acc = get(e.childID);
            if (acc != null)
                acc.getValue(e);
        }
    }

    /**
     * Constructs the dispatcher for the given domain and viewer.
     * 
     * @param d
     *            the domain
     * @param v
     *            the viewer
     */
    public DomainEventDispatcher(EditDomain d, EditPartViewer v) {
        domain = d;
        viewer = v;
        setEnableKeyTraversal(false);
    }

    /**
     * @see EventDispatcher#dispatchFocusGained(org.eclipse.swt.events.FocusEvent)
     */
    @Override
    public void dispatchFocusGained(FocusEvent event) {
        super.dispatchFocusGained(event);
        domain.focusGained(event, viewer);
    }

    /**
     * @see EventDispatcher#dispatchFocusLost(org.eclipse.swt.events.FocusEvent)
     */
    @Override
    public void dispatchFocusLost(FocusEvent event) {
        super.dispatchFocusLost(event);
        domain.focusLost(event, viewer);
        setRouteEventsToEditor(false);
    }

    /**
     * @see EventDispatcher#dispatchKeyPressed(org.eclipse.swt.events.KeyEvent)
     */
    @Override
    public void dispatchKeyPressed(org.eclipse.swt.events.KeyEvent e) {
        if (!editorCaptured) {
            super.dispatchKeyPressed(e);
            if (draw2dBusy())
                return;
        }
        if (okToDispatch())
            domain.keyDown(e, viewer);
    }

    /**
     * @see org.eclipse.draw2d.SWTEventDispatcher#dispatchKeyTraversed(org.eclipse.swt.events.TraverseEvent)
     */
    @Override
    public void dispatchKeyTraversed(TraverseEvent e) {
        if (!editorCaptured) {
            super.dispatchKeyTraversed(e);
            if (!e.doit)
                return;
        }
        if (okToDispatch())
            domain.keyTraversed(e, viewer);
    }

    /**
     * @see EventDispatcher#dispatchKeyReleased(org.eclipse.swt.events.KeyEvent)
     */
    @Override
    public void dispatchKeyReleased(org.eclipse.swt.events.KeyEvent e) {
        if (!editorCaptured) {
            super.dispatchKeyReleased(e);
            if (draw2dBusy())
                return;
        }
        if (okToDispatch())
            domain.keyUp(e, viewer);
    }

    /**
     * @see EventDispatcher#dispatchMouseDoubleClicked(org.eclipse.swt.events.MouseEvent)
     */
    @Override
    public void dispatchMouseDoubleClicked(org.eclipse.swt.events.MouseEvent me) {
        if (!editorCaptured) {
            super.dispatchMouseDoubleClicked(me);
            if (draw2dBusy())
                return;
        }
        if (okToDispatch())
            domain.mouseDoubleClick(me, viewer);
    }

    /**
     * @see EventDispatcher#dispatchMouseEntered(org.eclipse.swt.events.MouseEvent)
     */
    @Override
    public void dispatchMouseEntered(org.eclipse.swt.events.MouseEvent me) {
        if (!editorCaptured) {
            super.dispatchMouseEntered(me);
            if (draw2dBusy())
                return;
        }
        if (okToDispatch()) {
            domain.viewerEntered(me, viewer);
        }
    }

    /**
     * @see EventDispatcher#dispatchMouseExited(org.eclipse.swt.events.MouseEvent)
     */
    @Override
    public void dispatchMouseExited(org.eclipse.swt.events.MouseEvent me) {
        if (!editorCaptured) {
            super.dispatchMouseExited(me);
            if (draw2dBusy())
                return;
        }
        if (okToDispatch()) {
            domain.viewerExited(me, viewer);
        }
    }

    /**
     * @see EventDispatcher#dispatchMouseHover(org.eclipse.swt.events.MouseEvent)
     */
    @Override
    public void dispatchMouseHover(org.eclipse.swt.events.MouseEvent me) {
        if (!editorCaptured) {
            super.dispatchMouseHover(me);
            if (draw2dBusy())
                return;
        }
        if (okToDispatch())
            domain.mouseHover(me, viewer);
    }

    /**
     * @see EventDispatcher#dispatchMousePressed(org.eclipse.swt.events.MouseEvent)
     */
    @Override
    public void dispatchMousePressed(org.eclipse.swt.events.MouseEvent me) {
        if (!editorCaptured) {
            super.dispatchMousePressed(me);
            if (draw2dBusy())
                return;
        }
        if (okToDispatch()) {
            setFocus(null);
            control.forceFocus();
            setRouteEventsToEditor(true);
            domain.mouseDown(me, viewer);
        }
    }

    /**
     * @see EventDispatcher#dispatchMouseMoved(org.eclipse.swt.events.MouseEvent)
     */
    @SuppressWarnings("deprecation")
    @Override
    public void dispatchMouseMoved(org.eclipse.swt.events.MouseEvent me) {
        if (!editorCaptured) {
            super.dispatchMouseMoved(me);
            if (draw2dBusy())
                return;
        }
        if (okToDispatch()) {
            if ((me.stateMask & InputEvent.ANY_BUTTON) != 0)
                domain.mouseDrag(me, viewer);
            else
                domain.mouseMove(me, viewer);
        }
    }

    /**
     * @see EventDispatcher#dispatchMouseReleased(org.eclipse.swt.events.MouseEvent)
     */
    @Override
    public void dispatchMouseReleased(org.eclipse.swt.events.MouseEvent me) {
        if (!editorCaptured) {
            super.dispatchMouseReleased(me);
            if (draw2dBusy())
                return;
        }
        if (okToDispatch()) {
            setRouteEventsToEditor(false);
            domain.mouseUp(me, viewer);
            updateFigureUnderCursor(me);
        }
    }

    /**
     * Dispatches a drag finished event.
     * 
     * @param event
     *            the event
     * @param viewer
     *            the viewer on which the event occured.
     */
    public void dispatchNativeDragFinished(DragSourceEvent event,
            AbstractEditPartViewer viewer) {
        // $TODO delete the viewer parameter from the method
        domain.nativeDragFinished(event, viewer);
    }

    /**
     * Dispatches a drag started event.
     * 
     * @param event
     *            the event
     * @param viewer
     *            the viewer
     */
    public void dispatchNativeDragStarted(DragSourceEvent event,
            AbstractEditPartViewer viewer) {
        // $TODO delete the viewer parameter from the method
        setRouteEventsToEditor(false);
        domain.nativeDragStarted(event, viewer);
    }

    /**
     * Forwards the event to the EditDomain.
     * 
     * @see org.eclipse.draw2d.EventDispatcher#dispatchMouseWheelScrolled(org.eclipse.swt.widgets.Event)
     */
    @Override
    public void dispatchMouseWheelScrolled(Event evt) {
        if (!editorCaptured)
            super.dispatchMouseWheelScrolled(evt);

        if (evt.doit && okToDispatch())
            domain.mouseWheelScrolled(evt, viewer);
    }

    private boolean draw2dBusy() {
        if (getCurrentEvent() != null)
            if (getCurrentEvent().isConsumed())
                return true;
        if (isCaptured())
            return true;
        return false;
    }

    /**
     * Lazily creates and returns the accessibility dispatcher.
     * 
     * @see org.eclipse.draw2d.EventDispatcher#getAccessibilityDispatcher()
     */
    @Override
    protected AccessibilityDispatcher getAccessibilityDispatcher() {
        if (accessibilityDispatcher == null)
            accessibilityDispatcher = new EditPartAccessibilityDispatcher();
        return accessibilityDispatcher;
    }

    /**
     * Returns the viewer on which this dispatcher was created
     * 
     * @return the viewer for this dispatcher
     */
    protected final EditPartViewer getViewer() {
        return viewer;
    }

    private boolean okToDispatch() {
        return domain != null;
    }

    @SuppressWarnings("deprecation")
    void putAccessible(AccessibleEditPart acc) {
        accessibles.put(new Integer(acc.getAccessibleID()), acc);
    }

    @SuppressWarnings("deprecation")
    void removeAccessible(AccessibleEditPart acc) {
        accessibles.remove(new Integer(acc.getAccessibleID()));
    }

    /**
     * @see EventDispatcher#setCapture(IFigure)
     */
    @Override
    protected void setCapture(IFigure figure) {
        super.setCapture(figure);
        if (figure == null) {
            releaseCapture();
            setRouteEventsToEditor(true);
        }
    }

    /**
     * @see SWTEventDispatcher#setCursor(Cursor)
     */
    @Override
    protected void setCursor(Cursor newCursor) {
        if (overrideCursor == null)
            super.setCursor(newCursor);
        else
            super.setCursor(overrideCursor);
    }

    /**
     * Sets whether events should go directly to the edit domain.
     * 
     * @param value
     *            <code>true</code> if all events should go directly to the edit
     *            domain
     */
    public void setRouteEventsToEditor(boolean value) {
        editorCaptured = value;
    }

    /**
     * Sets the override cursor.
     * 
     * @param newCursor
     *            the cursor
     */
    public void setOverrideCursor(Cursor newCursor) {
        if (overrideCursor == newCursor)
            return;
        overrideCursor = newCursor;
        if (overrideCursor == null)
            updateCursor();
        else
            setCursor(overrideCursor);
    }

}
