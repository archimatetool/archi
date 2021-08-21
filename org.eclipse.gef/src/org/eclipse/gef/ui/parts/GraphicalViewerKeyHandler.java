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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;

/**
 * An extended KeyHandler which processes default keystrokes for common
 * navigation in a GraphicalViewer. This class can be used as a KeyHandler too;
 * Unrecognized keystrokes are sent to the super's implementation. This class
 * will process key events containing the following:
 * <UL>
 * <LI>Arrow Keys (UP, DOWN, LEFT, RIGHT) with optional SHIFT and CTRL modifiers
 * to navigate between siblings.
 * <LI>Arrow Keys (UP, DOWN) same as above, but with ALT modifier to navigate
 * into or out of a container.
 * <LI>'\'Backslash and '/' Slash keys with optional SHIFT and CTRL modifiers to
 * traverse connections.
 * </UL>
 * <P>
 * All processed key events will do nothing other than change the selection
 * and/or focus editpart for the viewer.
 * 
 * @author hudsonr
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class GraphicalViewerKeyHandler extends KeyHandler {

    int counter;

    /**
     * When navigating through connections, a "Node" EditPart is used as a
     * reference.
     */
    private WeakReference cachedNode;
    private GraphicalViewer viewer;

    /**
     * Constructs a key handler for the given viewer.
     * 
     * @param viewer
     *            the viewer
     */
    public GraphicalViewerKeyHandler(GraphicalViewer viewer) {
        this.viewer = viewer;
    }

    /**
     * @return <code>true</code> if key pressed indicates a connection
     *         traversal/selection
     */
    boolean acceptConnection(KeyEvent event) {
        return event.character == '/' || event.character == '?'
                || event.character == '\\' || event.character == '\u001c'
                || event.character == '|';
    }

    /**
     * @return <code>true</code> if the keys pressed indicate to traverse inside
     *         a container
     */
    boolean acceptIntoContainer(KeyEvent event) {
        return ((event.stateMask & SWT.ALT) != 0)
                && (event.keyCode == SWT.ARROW_DOWN);
    }

    /**
     * @return <code>true</code> if the keys pressed indicate to stop
     *         traversing/selecting connection
     */
    boolean acceptLeaveConnection(KeyEvent event) {
        int key = event.keyCode;
        if (getFocusEditPart() instanceof ConnectionEditPart)
            if ((key == SWT.ARROW_UP) || (key == SWT.ARROW_RIGHT)
                    || (key == SWT.ARROW_DOWN) || (key == SWT.ARROW_LEFT))
                return true;
        return false;
    }

    /**
     * @return <code>true</code> if the viewer's contents has focus and one of
     *         the arrow keys is pressed
     */
    boolean acceptLeaveContents(KeyEvent event) {
        int key = event.keyCode;
        return getFocusEditPart() == getViewer().getContents()
                && ((key == SWT.ARROW_UP) || (key == SWT.ARROW_RIGHT)
                        || (key == SWT.ARROW_DOWN) || (key == SWT.ARROW_LEFT));
    }

    /**
     * @return <code>true</code> if the keys pressed indicate to traverse to the
     *         parent of the currently focused EditPart
     */
    boolean acceptOutOf(KeyEvent event) {
        return ((event.stateMask & SWT.ALT) != 0)
                && (event.keyCode == SWT.ARROW_UP);
    }

    boolean acceptScroll(KeyEvent event) {
        return ((event.stateMask & SWT.CTRL) != 0
                && (event.stateMask & SWT.SHIFT) != 0 && (event.keyCode == SWT.ARROW_DOWN
                || event.keyCode == SWT.ARROW_LEFT
                || event.keyCode == SWT.ARROW_RIGHT || event.keyCode == SWT.ARROW_UP));
    }

    /**
     * Given a connection on a node, this method finds the next (or the
     * previous) connection of that node.
     * 
     * @param node
     *            The EditPart whose connections are being traversed
     * @param current
     *            The connection relative to which the next connection has to be
     *            found
     * @param forward
     *            <code>true</code> if the next connection has to be found;
     *            false otherwise
     */
    ConnectionEditPart findConnection(GraphicalEditPart node,
            ConnectionEditPart current, boolean forward) {
        List connections = new ArrayList(node.getSourceConnections());
        connections.addAll(node.getTargetConnections());
        connections = getValidNavigationTargets(connections);
        if (connections.isEmpty())
            return null;
        if (forward)
            counter++;
        else
            counter--;
        while (counter < 0)
            counter += connections.size();
        counter %= connections.size();
        return (ConnectionEditPart) connections.get(counter
                % connections.size());
    }

    private List getValidNavigationTargets(List candidateEditParts) {
        List validNavigationTargetEditParts = new ArrayList();
        for (int i = 0; i < candidateEditParts.size(); i++) {
            EditPart candidate = (EditPart) candidateEditParts.get(i);
            if (isValidNavigationTarget(candidate)) {
                validNavigationTargetEditParts.add(candidate);
            }
        }
        return validNavigationTargetEditParts;
    }

    /**
     * Given an absolute point (pStart) and a list of EditParts, this method
     * finds the closest EditPart (except for the one to be excluded) in the
     * given direction.
     * 
     * @param siblings
     *            List of sibling EditParts
     * @param pStart
     *            The starting point (must be in absolute coordinates) from
     *            which the next sibling is to be found.
     * @param direction
     *            PositionConstants
     * @param exclude
     *            The EditPart to be excluded from the search
     * 
     */
    @SuppressWarnings("deprecation")
    GraphicalEditPart findSibling(List siblings, Point pStart, int direction,
            EditPart exclude) {
        GraphicalEditPart epCurrent;
        GraphicalEditPart epFinal = null;
        IFigure figure;
        Point pCurrent;
        int distance = Integer.MAX_VALUE;

        Iterator iter = getValidNavigationTargets(siblings).iterator();
        while (iter.hasNext()) {
            epCurrent = (GraphicalEditPart) iter.next();
            if (epCurrent == exclude)
                continue;
            figure = epCurrent.getFigure();
            pCurrent = getNavigationPoint(figure);
            figure.translateToAbsolute(pCurrent);
            if (pStart.getPosition(pCurrent) != direction)
                continue;

            int d = pCurrent.getDistanceOrthogonal(pStart);
            if (d < distance) {
                distance = d;
                epFinal = epCurrent;
            }
        }
        return epFinal;
    }

    /**
     * Figures' navigation points are used to determine their direction compared
     * to one another, and the distance between them.
     * 
     * @return the center of the given figure
     */
    Point getNavigationPoint(IFigure figure) {
        return figure.getBounds().getCenter();
    }

    /**
     * Returns the cached node. It is possible that the node is not longer in
     * the viewer but has not been garbage collected yet.
     */
    private GraphicalEditPart getCachedNode() {
        if (cachedNode == null)
            return null;
        if (cachedNode.isEnqueued())
            return null;
        return (GraphicalEditPart) cachedNode.get();
    }

    /**
     * @return the EditPart that has focus
     */
    protected GraphicalEditPart getFocusEditPart() {
        return (GraphicalEditPart) getViewer().getFocusEditPart();
    }

    /**
     * Returns the list of editparts which are conceptually at the same level of
     * navigation as the currently focused editpart. By default, these are the
     * siblings of the focused part.
     * <p>
     * This implementation returns a list that contains the EditPart that has
     * focus.
     * </p>
     * 
     * @return a list of navigation editparts
     * @since 3.4
     */
    protected List getNavigationSiblings() {
        EditPart focusPart = getFocusEditPart();
        if (focusPart.getParent() != null)
            return focusPart.getParent().getChildren();
        List list = new ArrayList();
        list.add(focusPart);
        return list;
    }

    /**
     * Returns the viewer on which this key handler was created.
     * 
     * @return the viewer
     */
    protected GraphicalViewer getViewer() {
        return viewer;
    }

    /**
     * @return <code>true</code> if the viewer is mirrored
     * @since 3.4
     */
    protected boolean isViewerMirrored() {
        return (viewer.getControl().getStyle() & SWT.MIRRORED) != 0;
    }

    /**
     * Extended to process key events described above.
     * 
     * @see org.eclipse.gef.KeyHandler#keyPressed(org.eclipse.swt.events.KeyEvent)
     */
    @Override
    public boolean keyPressed(KeyEvent event) {
        // if CTRL + SPACE is pressed, event.character == ' ' does not hold;
        // therefore using the keyCode to decide whether SPACE was pressed (with
        // or without modifiers).
        if (event.keyCode == 32) {
            processSelect(event);
            return true;
        } else if (acceptIntoContainer(event)) {
            navigateIntoContainer(event);
            return true;
        } else if (acceptOutOf(event)) {
            navigateOut(event);
            return true;
        } else if (acceptConnection(event)) {
            navigateConnections(event);
            return true;
        } else if (acceptScroll(event)) {
            scrollViewer(event);
            return true;
        } else if (acceptLeaveConnection(event)) {
            navigateOutOfConnection(event);
            return true;
        } else if (acceptLeaveContents(event)) {
            navigateIntoContainer(event);
            return true;
        }

        switch (event.keyCode) {
        case SWT.ARROW_LEFT:
            if (navigateNextSibling(event,
                    isViewerMirrored() ? PositionConstants.EAST
                            : PositionConstants.WEST))
                return true;
            break;
        case SWT.ARROW_RIGHT:
            if (navigateNextSibling(event,
                    isViewerMirrored() ? PositionConstants.WEST
                            : PositionConstants.EAST))
                return true;
            break;
        case SWT.ARROW_UP:
            if (navigateNextSibling(event, PositionConstants.NORTH))
                return true;
            break;
        case SWT.ARROW_DOWN:
            if (navigateNextSibling(event, PositionConstants.SOUTH))
                return true;
            break;

        case SWT.HOME:
            if (navigateJumpSibling(event, PositionConstants.WEST))
                return true;
            break;
        case SWT.END:
            if (navigateJumpSibling(event, PositionConstants.EAST))
                return true;
            break;
        case SWT.PAGE_DOWN:
            if (navigateJumpSibling(event, PositionConstants.SOUTH))
                return true;
            break;
        case SWT.PAGE_UP:
            if (navigateJumpSibling(event, PositionConstants.NORTH))
                return true;
        }
        return super.keyPressed(event);
    }

    /**
     * This method navigates through connections based on the keys pressed.
     */
    void navigateConnections(KeyEvent event) {
        GraphicalEditPart focus = getFocusEditPart();
        ConnectionEditPart current = null;
        GraphicalEditPart node = getCachedNode();
        if (focus instanceof ConnectionEditPart) {
            current = (ConnectionEditPart) focus;
            if (node == null
                    || (node != current.getSource() && node != current
                            .getTarget())) {
                node = (GraphicalEditPart) current.getSource();
                counter = 0;
            }
        } else {
            node = focus;
        }

        setCachedNode(node);
        boolean forward = event.character == '/' || event.character == '?';
        ConnectionEditPart next = findConnection(node, current, forward);
        navigateTo(next, event);
    }

    private boolean isValidNavigationTarget(EditPart editPart) {
        return editPart.isSelectable();
    }

    /**
     * This method traverses to the closest child of the currently focused
     * EditPart, if it has one.
     */
    void navigateIntoContainer(KeyEvent event) {
        GraphicalEditPart focus = getFocusEditPart();
        List childList = getValidNavigationTargets(focus.getChildren());
        Point tl = focus.getContentPane().getBounds().getTopLeft();

        int minimum = Integer.MAX_VALUE;
        int current;
        GraphicalEditPart closestPart = null;

        for (int i = 0; i < childList.size(); i++) {
            GraphicalEditPart child = (GraphicalEditPart) childList.get(i);

            Rectangle childBounds = child.getFigure().getBounds();
            current = (childBounds.x - tl.x) + (childBounds.y - tl.y);
            if (current < minimum) {
                minimum = current;
                closestPart = child;
            }
        }
        if (closestPart != null)
            navigateTo(closestPart, event);
    }

    /**
     * Not yet implemented.
     */
    boolean navigateJumpSibling(KeyEvent event, int direction) {
        // TODO: Implement navigateJumpSibling() (for PGUP, PGDN, HOME and END
        // key events)
        return false;
    }

    /**
     * Traverses to the next sibling in the given direction.
     * 
     * @param event
     *            the KeyEvent for the keys that were pressed to trigger this
     *            traversal
     * @param direction
     *            PositionConstants.* indicating the direction in which to
     *            traverse
     */
    boolean navigateNextSibling(KeyEvent event, int direction) {
        return navigateNextSibling(event, direction, getNavigationSiblings());
    }

    /**
     * Traverses to the closest EditPart in the given list that is also in the
     * given direction.
     * 
     * @param event
     *            the KeyEvent for the keys that were pressed to trigger this
     *            traversal
     * @param direction
     *            PositionConstants.* indicating the direction in which to
     *            traverse
     */
    boolean navigateNextSibling(KeyEvent event, int direction, List list) {
        GraphicalEditPart epStart = getFocusEditPart();
        IFigure figure = epStart.getFigure();
        Point pStart = getNavigationPoint(figure);
        figure.translateToAbsolute(pStart);
        EditPart next = findSibling(list, pStart, direction, epStart);
        if (next == null)
            return false;
        navigateTo(next, event);
        return true;
    }

    /**
     * Navigates to the parent of the currently focused EditPart.
     */
    void navigateOut(KeyEvent event) {
        if (getFocusEditPart() == null
                || getFocusEditPart() == getViewer().getContents())
            return;

        EditPart editPart = getFocusEditPart().getParent();
        while (editPart != null && editPart != getViewer().getContents()
                && !isValidNavigationTarget(editPart)) {
            editPart = editPart.getParent();
        }

        if (editPart != null && isValidNavigationTarget(editPart)) {
            navigateTo(editPart, event);
        }
    }

    /**
     * Navigates to the source or target of the currently focused
     * ConnectionEditPart.
     */
    void navigateOutOfConnection(KeyEvent event) {
        GraphicalEditPart cached = getCachedNode();
        ConnectionEditPart conn = (ConnectionEditPart) getFocusEditPart();
        if (cached != null
                && (cached == conn.getSource() || cached == conn.getTarget()))
            navigateTo(cached, event);
        else
            navigateTo(conn.getSource(), event);
    }

    /**
     * Navigates to the given EditPart
     * 
     * @param part
     *            the EditPart to navigate to
     * @param event
     *            the KeyEvent that triggered this traversal
     */
    protected void navigateTo(EditPart part, KeyEvent event) {
        if (part == null)
            return;
        if ((event.stateMask & SWT.SHIFT) != 0) {
            getViewer().appendSelection(part);
            getViewer().setFocus(part);
        } else if ((event.stateMask & SWT.CONTROL) != 0)
            getViewer().setFocus(part);
        else
            getViewer().select(part);
        getViewer().reveal(part);
    }

    /**
     * This method is invoked when the user presses the space bar. It toggles
     * the selection of the EditPart that currently has focus.
     * 
     * @param event
     *            the key event received
     */
    protected void processSelect(KeyEvent event) {
        EditPart part = getViewer().getFocusEditPart();
        if (part != getViewer().getContents()) {
            if ((event.stateMask & SWT.CTRL) != 0
                    && part.getSelected() != EditPart.SELECTED_NONE)
                getViewer().deselect(part);
            else
                getViewer().appendSelection(part);

            getViewer().setFocus(part);
        }
    }

    void scrollViewer(KeyEvent event) {
        if (!(getViewer().getControl() instanceof FigureCanvas))
            return;
        FigureCanvas figCanvas = (FigureCanvas) getViewer().getControl();
        Point loc = figCanvas.getViewport().getViewLocation();
        Rectangle area = figCanvas.getViewport()
                .getClientArea(Rectangle.SINGLETON).scale(.1);
        switch (event.keyCode) {
        case SWT.ARROW_DOWN:
            figCanvas.scrollToY(loc.y + area.height);
            break;
        case SWT.ARROW_UP:
            figCanvas.scrollToY(loc.y - area.height);
            break;
        case SWT.ARROW_LEFT:
            if (isViewerMirrored())
                figCanvas.scrollToX(loc.x + area.width);
            else
                figCanvas.scrollToX(loc.x - area.width);
            break;
        case SWT.ARROW_RIGHT:
            if (isViewerMirrored())
                figCanvas.scrollToX(loc.x - area.width);
            else
                figCanvas.scrollToX(loc.x + area.width);
        }
    }

    private void setCachedNode(GraphicalEditPart node) {
        if (node == null)
            cachedNode = null;
        else
            cachedNode = new WeakReference(node);
    }

}
