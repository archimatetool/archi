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
package org.eclipse.gef.internal.ui.palette.editparts;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;

class EditPartTipHelper extends org.eclipse.draw2d.PopUpHelper {

    private static EditPartTipHelper currentHelper;
    private ShellListener shellListener;

    private static void setHelper(EditPartTipHelper helper) {
        if (currentHelper != null && currentHelper != helper
                && currentHelper.isShowing())
            currentHelper.hide();
        currentHelper = helper;
    }

    public EditPartTipHelper(Control c) {
        super(c, SWT.ON_TOP | SWT.TOOL | SWT.NO_TRIM);
    }

    /**
     * Sets the LightWeightSystem object's contents to the passed tooltip, and
     * displays the tip at the coordianates specified by tipPosX and tipPosY.
     * The given coordinates will be adjusted if the tip cannot be completely
     * visible on the screen.
     * 
     * @param tip
     *            The tool tip to be displayed.
     * @param tipPosX
     *            X coordiante of tooltip to be displayed
     * @param tipPosY
     *            Y coordinate of tooltip to be displayed
     */
    public void displayToolTipAt(IFigure tip, int tipPosX, int tipPosY) {
        if (tip != null) {
            // Adjust the position if the tip will not be completely visible on
            // the screen
            int shiftX = 0;
            int shiftY = 0;
            Dimension tipSize = tip.getPreferredSize();
            getShell();
            tipSize = tipSize.getExpanded(getShellTrimSize());
            org.eclipse.swt.graphics.Rectangle area = control.getDisplay()
                    .getClientArea();
            org.eclipse.swt.graphics.Point end = new org.eclipse.swt.graphics.Point(
                    tipPosX + tipSize.width, tipPosY + tipSize.height);
            if (!area.contains(end)) {
                shiftX = end.x - (area.x + area.width);
                shiftY = end.y - (area.y + area.height);
                shiftX = shiftX < 0 ? 0 : shiftX;
                shiftY = shiftY < 0 ? 0 : shiftY;
            }
            tipPosX -= shiftX;
            tipPosY -= shiftY;

            // Display the tip
            EditPartTipHelper.setHelper(this);
            getLightweightSystem().setContents(tip);
            setShellBounds(tipPosX, tipPosY, tipSize.width, tipSize.height);
            show();
            getShell().setCapture(true);
        }
    }

    @Override
    public void dispose() {
        if (shellListener != null) {
            control.getShell().removeShellListener(shellListener);
            shellListener = null;
        }
        super.dispose();
    }

    /**
     * @see org.eclipse.draw2d.PopUpHelper#hide()
     */
    @Override
    protected void hide() {
        super.hide();
        currentHelper = null;
    }

    @Override
    protected void hookShellListeners() {

        /*
         * If the cursor leaves the tip window, hide the tooltip and dispose of
         * its shell
         */
        getShell().addMouseTrackListener(new MouseTrackAdapter() {
            @Override
            public void mouseExit(MouseEvent e) {
                getShell().setCapture(false);
                dispose();
            }
        });
        /*
         * If the mouseExit listener does not get called, dispose of the shell
         * if the cursor is no longer in the tooltip. This occurs in the rare
         * case that a mouseEnter is not received on the tooltip when it
         * appears.
         */
        getShell().addMouseMoveListener(new MouseMoveListener() {
            @Override
            public void mouseMove(MouseEvent e) {
                Point eventPoint = getShell().toDisplay(new Point(e.x, e.y));
                if (!getShell().getBounds().contains(eventPoint)) {
                    if (isShowing())
                        getShell().setCapture(false);
                    dispose();
                }
            }
        });

        // This is to dispose of the tooltip when the user ALT-TABs to another
        // window.
        if (shellListener == null) {
            shellListener = new ShellAdapter() {
                @Override
                public void shellDeactivated(ShellEvent event) {
                    Display.getCurrent().asyncExec(new Runnable() {
                        @Override
                        public void run() {
                            Shell active = Display.getCurrent()
                                    .getActiveShell();
                            if (getShell() == active
                                    || control.getShell() == active
                                    || getShell().isDisposed())
                                return;
                            if (isShowing())
                                getShell().setCapture(false);
                            dispose();
                        }
                    });
                }
            };
            control.getShell().addShellListener(shellListener);
            getShell().addShellListener(shellListener);
        }

        /*
         * Workaround for GTK Bug - Control.setCapture(boolean) not implemented:
         * If the cursor is not over the shell when it is first painted, hide
         * the tooltip and dispose of the shell.
         */
        if (SWT.getPlatform().equals("gtk")) { //$NON-NLS-1$
            getShell().addPaintListener(new PaintListener() {
                @Override
                public void paintControl(PaintEvent event) {
                    Point cursorLoc = Display.getCurrent().getCursorLocation();
                    if (!getShell().getBounds().contains(cursorLoc)) {
                        // This must be run asynchronously. If not, other paint
                        // listeners may attempt to paint on a disposed control.
                        Display.getCurrent().asyncExec(new Runnable() {
                            @Override
                            public void run() {
                                if (isShowing())
                                    getShell().setCapture(false);
                                dispose();
                            }
                        });
                    }
                }
            });
        }
    }

    public static boolean isCurrent(EditPartTipHelper helper) {
        return currentHelper != null && helper == currentHelper;
    }

}
