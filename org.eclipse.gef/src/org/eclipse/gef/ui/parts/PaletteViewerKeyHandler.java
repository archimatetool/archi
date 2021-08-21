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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.internal.ui.palette.editparts.DrawerEditPart;
import org.eclipse.gef.internal.ui.palette.editparts.GroupEditPart;
import org.eclipse.gef.internal.ui.palette.editparts.IPaletteStackEditPart;
import org.eclipse.gef.internal.ui.palette.editparts.PaletteStackEditPart;
import org.eclipse.gef.internal.ui.palette.editparts.PinnablePaletteStackEditPart;
import org.eclipse.gef.internal.ui.palette.editparts.TemplateEditPart;
import org.eclipse.gef.internal.ui.palette.editparts.ToolEntryEditPart;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteStack;
import org.eclipse.gef.ui.palette.PaletteViewer;

/**
 * KeyHandler for the {@link org.eclipse.gef.ui.palette.PaletteViewer Palette}.
 * Handles selection traversal of Palette entries and collapse/expand of
 * categories.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class PaletteViewerKeyHandler extends GraphicalViewerKeyHandler {

    /**
     * Constructs a key handler for the specified palette viewer.
     * 
     * @param viewer
     *            the palette viewer
     */
    public PaletteViewerKeyHandler(PaletteViewer viewer) {
        super(viewer);
    }

    private boolean acceptCollapseDrawer(KeyEvent event) {
        boolean result = isViewerMirrored() ? event.keyCode == SWT.ARROW_RIGHT
                : event.keyCode == SWT.ARROW_LEFT;
        return result && isExpandedDrawer(getFocusEditPart());
    }

    private boolean acceptExpandDrawer(KeyEvent event) {
        boolean result = isViewerMirrored() ? event.keyCode == SWT.ARROW_LEFT
                : event.keyCode == SWT.ARROW_RIGHT;
        return result && isCollapsedDrawer(getFocusEditPart());
    }

    private boolean acceptIntoExpandedDrawer(KeyEvent event) {
        boolean result = event.keyCode == SWT.ARROW_DOWN;
        result = result
                || (isViewerMirrored() ? event.keyCode == SWT.ARROW_LEFT
                        : event.keyCode == SWT.ARROW_RIGHT);
        return result && isExpandedDrawer(getFocusEditPart());
    }

    private boolean acceptExpandStack(KeyEvent event) {
        return event.keyCode == SWT.ARROW_DOWN
                && (event.stateMask & SWT.ALT) > 0
                && isCollapsedStack(getFocusEditPart());
    }

    private boolean acceptCollapseStack(KeyEvent event) {
        return event.keyCode == SWT.ARROW_UP && (event.stateMask & SWT.ALT) > 0
                && isExpandedStack(getFocusEditPart());
    }

    private boolean acceptSetFocusOnDrawer(KeyEvent event) {
        boolean result = isViewerMirrored() ? event.keyCode == SWT.ARROW_RIGHT
                : event.keyCode == SWT.ARROW_LEFT;
        return (result || event.keyCode == SWT.ARROW_UP)
                && (getFocusEditPart().getParent() instanceof DrawerEditPart || (getFocusEditPart()
                        .getParent() instanceof IPaletteStackEditPart && getFocusEditPart()
                        .getParent().getParent() instanceof DrawerEditPart));
    }

    private boolean acceptNextContainer(KeyEvent event) {
        return event.keyCode == SWT.ARROW_DOWN;
    }

    private void buildNavigationList(EditPart palettePart, EditPart exclusion,
            ArrayList navList, EditPart stackPart) {
        if (palettePart != exclusion) {
            if (isCollapsedDrawer(palettePart)) {
                navList.add(palettePart);
                return;
            } else if (stackPart instanceof PaletteStackEditPart
                    && stackPart.getChildren().contains(palettePart)) {
                // we only want to add the top level item to the navlist
                if (((PaletteStack) stackPart.getModel()).getActiveEntry()
                        .equals(palettePart.getModel()))
                    navList.add(palettePart);
            } else if (stackPart instanceof PinnablePaletteStackEditPart
                    && stackPart.getChildren().contains(palettePart)) {
                // we only want to add the top level item to the navlist unless
                // the palette stack is expanded
                if (((PinnablePaletteStackEditPart) stackPart).isExpanded()
                        || ((PaletteStack) stackPart.getModel())
                                .getActiveEntry()
                                .equals(palettePart.getModel())) {
                    navList.add(palettePart);
                }
            } else if ((palettePart instanceof ToolEntryEditPart
                    || palettePart instanceof DrawerEditPart || palettePart instanceof TemplateEditPart)) {
                navList.add(palettePart);
            }
        }

        List children = palettePart.getChildren();
        for (int k = 0; k < children.size(); k++) {
            EditPart ep = (EditPart) children.get(k);
            if (ep instanceof IPaletteStackEditPart)
                stackPart = ep;
            buildNavigationList(ep, exclusion, navList, stackPart);
        }
    }

    private void collapseDrawer() {
        DrawerEditPart drawer = (DrawerEditPart) getFocusEditPart();
        drawer.setExpanded(false);
    }

    private void expandDrawer() {
        DrawerEditPart drawer = (DrawerEditPart) getFocusEditPart();
        drawer.setExpanded(true);
    }

    /**
     * Figures' navigation points are used to determine their direction compared
     * to one another, and the distance between them.
     * 
     * @param figure
     *            the figure whose navigation point is to be returned
     * @return the top-left of the given figure
     */
    @Override
    protected Point getNavigationPoint(IFigure figure) {
        return figure.getBounds().getTopLeft();
    }

    /**
     * @return a list of {@link org.eclipse.gef.EditPart EditParts} that can be
     *         traversed to from the current
     *         {@link GraphicalViewerKeyHandler#getFocusEditPart() focus part}
     */
    @Override
    protected List getNavigationSiblings() {
        ArrayList siblingsList = new ArrayList();
        EditPart focusPart = getFocusEditPart();
        EditPart parent = focusPart.getParent();
        if (parent == null) {
            siblingsList.add(focusPart);
            return siblingsList;
        }
        if (parent instanceof GroupEditPart
                || parent instanceof IPaletteStackEditPart) {
            EditPart grandParent = parent.getParent();
            buildNavigationList(grandParent, grandParent, siblingsList,
                    grandParent);
        } else
            buildNavigationList(parent, parent, siblingsList, parent);
        return siblingsList;
    }

    /**
     * Returns <code>true</code> if the passed Editpart is a collapsed
     * {@link org.eclipse.gef.ui.palette.DrawerEditPart Drawer}, false
     * otherwise.
     */
    boolean isCollapsedDrawer(EditPart part) {
        return part instanceof DrawerEditPart
                && !((DrawerEditPart) part).isExpanded();
    }

    /**
     * Returns <code>true</code> if the passed Editpart is an expanded
     * {@link org.eclipse.gef.ui.palette.DrawerEditPart Drawer}, false
     * otherwise.
     */
    boolean isExpandedDrawer(EditPart part) {
        return part instanceof DrawerEditPart
                && ((DrawerEditPart) part).isExpanded();
    }

    /**
     * Returns <code>true</code> if the passed focusPart is a collapsed pinnable
     * palette stack, false otherwise.
     */
    boolean isCollapsedStack(EditPart focusPart) {
        EditPart parent = focusPart.getParent();
        return parent instanceof PaletteStackEditPart
                || (parent instanceof PinnablePaletteStackEditPart && !((PinnablePaletteStackEditPart) parent)
                        .isExpanded());
    }

    /**
     * Returns <code>true</code> if the passed focusPart is an expanded pinnable
     * palette stack, false otherwise.
     */
    boolean isExpandedStack(EditPart focusPart) {
        EditPart parent = focusPart.getParent();
        return parent instanceof PaletteStackEditPart
                || (parent instanceof PinnablePaletteStackEditPart && ((PinnablePaletteStackEditPart) parent)
                        .isExpanded());
    }

    /**
     * Extends keyPressed to look for palette navigation keys.
     * 
     * @see org.eclipse.gef.KeyHandler#keyPressed(org.eclipse.swt.events.KeyEvent)
     */
    @Override
    public boolean keyPressed(KeyEvent event) {

        if (acceptExpandDrawer(event)) {
            expandDrawer();
            return true;
        }
        if (acceptCollapseDrawer(event)) {
            collapseDrawer();
            return true;
        }
        if (acceptExpandStack(event)) {
            expandStack();
            return true;
        }
        if (acceptCollapseStack(event)) {
            collapseStack(event);
            return true;
        }
        if (acceptIntoExpandedDrawer(event)) {
            if (navigateIntoExpandedDrawer(event))
                return true;
        }
        if (super.keyPressed(event))
            return true;
        if (acceptSetFocusOnDrawer(event)) {
            if (navigateToDrawer(event))
                return true;
        }
        if (acceptNextContainer(event)) {
            if (navigateToNextContainer(event))
                return true;
        }
        return false;
    }

    private boolean navigateIntoExpandedDrawer(KeyEvent event) {
        ArrayList potentials = new ArrayList();
        EditPart focusPart = getFocusEditPart();
        buildNavigationList(focusPart, focusPart, potentials, focusPart);
        if (!potentials.isEmpty()) {
            navigateTo((EditPart) potentials.get(0), event);
            return true;
        }
        return false;
    }

    /**
     * @see GraphicalViewerKeyHandler#navigateTo(EditPart, KeyEvent)
     */
    @Override
    protected void navigateTo(EditPart part, KeyEvent event) {
        if (part == null)
            return;
        if (part instanceof IPaletteStackEditPart) {
            PaletteEntry activeEntry = ((PaletteStack) part.getModel())
                    .getActiveEntry();
            part = (EditPart) getViewer().getEditPartRegistry()
                    .get(activeEntry);
        }
        getViewer().select(part);
        getViewer().reveal(part);
    }

    private boolean navigateToDrawer(KeyEvent event) {
        boolean found = false;
        EditPart parent = getFocusEditPart().getParent();
        while (parent != null && !found) {
            if (parent instanceof DrawerEditPart) {
                navigateTo(parent, event);
                found = true;
            }
            parent = parent.getParent();
        }
        return false;
    }

    private boolean navigateToNextContainer(KeyEvent event) {
        EditPart current = getFocusEditPart();
        while (current != null) {
            if (current instanceof DrawerEditPart
                    || current instanceof GroupEditPart) {
                List siblings = current.getParent().getChildren();
                int index = siblings.indexOf(current);
                if (index != -1 && siblings.size() > index + 1) {
                    EditPart part = (EditPart) siblings.get(index + 1);
                    if (part instanceof GroupEditPart
                            && part.getChildren().size() > 0) {
                        EditPart child = (EditPart) part.getChildren().get(0);
                        navigateTo(child, event);
                    } else
                        navigateTo(part, event);
                    return true;
                }
                return false;
            }
            current = current.getParent();
        }
        return false;
    }

    private void expandStack() {
        ((IPaletteStackEditPart) getFocusEditPart().getParent()).openMenu();
    }

    private void collapseStack(KeyEvent event) {
        ((PinnablePaletteStackEditPart) getFocusEditPart().getParent())
                .setExpanded(false);
        navigateTo(getFocusEditPart().getParent(), event);
    }

}
