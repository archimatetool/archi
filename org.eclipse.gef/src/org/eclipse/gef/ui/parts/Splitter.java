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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;

import org.eclipse.draw2d.ColorConstants;

class Splitter extends Composite {

    public static final int DEFAULT_SASH_WIDTH = 5;
    private static final int DRAG_MINIMUM = 62;
    private static final String MAINTAIN_SIZE = "maintain size"; //$NON-NLS-1$

    private int fixedSize = 150;
    private int orientation = SWT.VERTICAL;
    private Sash[] sashes = new Sash[0];
    private Control[] controls = new Control[0];
    private Control maxControl = null;
    private Listener sashListener;
    private int sashWidth = DEFAULT_SASH_WIDTH;

    /**
     * PropertyChangeSupport
     */
    protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    public void addFixedSizeChangeListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
    }

    protected void firePropertyChange(int oldValue, int newValue) {
        listeners.firePropertyChange(MAINTAIN_SIZE, oldValue, newValue);
    }

    public void removeFixedSizeChangeListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);
    }

    public int getFixedSize() {
        return fixedSize;
    }

    public void setFixedSize(int newSize) {
        if (newSize == fixedSize) {
            return;
        }

        firePropertyChange(fixedSize, fixedSize = newSize);
    }

    class SashPainter implements Listener {
        @Override
        public void handleEvent(Event e) {
            paint((Sash) e.widget, e.gc);
        }
    }

    public Splitter(Composite parent, int style) {
        super(parent, checkStyle(style));

        if ((style & SWT.HORIZONTAL) != 0)
            orientation = SWT.HORIZONTAL;

        this.addListener(SWT.Resize, new Listener() {
            @Override
            public void handleEvent(Event e) {
                layout(true);
            }
        });

        sashListener = new Listener() {
            @Override
            public void handleEvent(Event e) {
                onDragSash(e);
            }
        };
    }

    private static int checkStyle(int style) {
        int mask = SWT.BORDER;
        return style & mask;
    }

    @Override
    public Point computeSize(int wHint, int hHint, boolean changed) {

        Control[] controls = getControls(true);
        if (controls.length == 0)
            return new Point(wHint, hHint);

        int width = 0;
        int height = 0;
        boolean vertical = (getStyle() & SWT.VERTICAL) != 0;
        if (vertical) {
            width = wHint;
            height += (controls.length - 1) * getSashWidth();
        } else {
            height = hHint;
            width += controls.length * getSashWidth();
        }
        for (int i = 0; i < controls.length; i++) {
            if (vertical) {
                Point size = controls[i].computeSize(wHint, SWT.DEFAULT);
                height += size.y;
            } else {
                Point size = controls[i].computeSize(SWT.DEFAULT, hHint);
                if (controls[i].getData(MAINTAIN_SIZE) != null) {
                    size.x = fixedSize;
                }
                width += size.x;
            }
        }
        // if (wHint != SWT.DEFAULT) width = wHint;
        // if (hHint != SWT.DEFAULT) height = hHint;

        return new Point(width, height);
    }

    /**
     * Answer SWT.HORIZONTAL if the controls in the SashForm are laid out side
     * by side. Answer SWT.VERTICAL if the controls in the SashForm are laid out
     * top to bottom.
     */
    @Override
    public int getOrientation() {
        return orientation;
    }

    public int getSashWidth() {
        return sashWidth;
    }

    /**
     * Answer the control that currently is maximized in the SashForm. This
     * value may be null.
     */
    public Control getMaximizedControl() {
        return this.maxControl;
    }

    private Control[] getControls(boolean onlyVisible) {
        Control[] children = getChildren();
        Control[] controls = new Control[0];
        for (int i = 0; i < children.length; i++) {
            if (children[i] instanceof Sash)
                continue;
            if (onlyVisible && !children[i].getVisible())
                continue;

            Control[] newControls = new Control[controls.length + 1];
            System.arraycopy(controls, 0, newControls, 0, controls.length);
            newControls[controls.length] = children[i];
            controls = newControls;
        }
        return controls;
    }

    @Override
    public void layout(boolean changed) {
        Rectangle area = getClientArea();
        if (area.width == 0 || area.height == 0)
            return;

        Control[] newControls = getControls(true);
        if (controls.length == 0 && newControls.length == 0)
            return;
        controls = newControls;

        if (maxControl != null && !maxControl.isDisposed()) {
            for (int i = 0; i < controls.length; i++) {
                if (controls[i] != maxControl) {
                    controls[i].setBounds(-200, -200, 0, 0);
                } else {
                    controls[i].setBounds(area);
                }
            }
            return;
        }

        // keep just the right number of sashes
        if (sashes.length < controls.length - 1) {
            Sash[] newSashes = new Sash[controls.length - 1];
            System.arraycopy(sashes, 0, newSashes, 0, sashes.length);
            int sashOrientation = (orientation == SWT.HORIZONTAL) ? SWT.VERTICAL
                    : SWT.HORIZONTAL;
            for (int i = sashes.length; i < newSashes.length; i++) {
                newSashes[i] = new Sash(this, sashOrientation);
                newSashes[i].setBackground(ColorConstants.button);
                newSashes[i].addListener(SWT.Paint, new SashPainter());
                newSashes[i].addListener(SWT.Selection, sashListener);
            }
            sashes = newSashes;
        }
        if (sashes.length > controls.length - 1) {
            if (controls.length == 0) {
                for (int i = 0; i < sashes.length; i++) {
                    sashes[i].dispose();
                }
                sashes = new Sash[0];
            } else {
                Sash[] newSashes = new Sash[controls.length - 1];
                System.arraycopy(sashes, 0, newSashes, 0, newSashes.length);
                for (int i = controls.length - 1; i < sashes.length; i++) {
                    sashes[i].dispose();
                }
                sashes = newSashes;
            }
        }

        if (controls.length == 0)
            return;

        // @TODO
        // This only works if the orientation is horizontal, there are two
        // children and one
        // of them has been set to maintain its size.
        int x = area.x;
        int width;
        for (int i = 0; i < controls.length; i++) {
            Control control = controls[i];
            if (control.getData(MAINTAIN_SIZE) != null) {
                width = fixedSize;
                if (width > area.width) {
                    width = area.width - getSashWidth();
                }
                control.setBounds(x, area.y, width, area.height);
                x += width + getSashWidth();
            } else {
                width = Math.max(area.width - fixedSize - getSashWidth(), 0);
                control.setBounds(x, area.y, width, area.height);
                x += (width + getSashWidth());
            }
        }
        if (sashes.length > 0)
            sashes[0].setBounds(
                    controls[0].getBounds().x + controls[0].getBounds().width,
                    area.y, getSashWidth(), area.height);
    }

    @SuppressWarnings("deprecation")
    public void maintainSize(Control c) {
        Control[] controls = getControls(false);
        for (int i = 0; i < controls.length; i++) {
            Control ctrl = controls[i];
            if (ctrl == c) {
                ctrl.setData(MAINTAIN_SIZE, new Boolean(true));
                break;
            }
        }
    }

    void paint(Sash sash, GC gc) {
        if (getSashWidth() == 0)
            return;
        Point size = sash.getSize();
        if (getOrientation() == SWT.HORIZONTAL) {
            gc.setForeground(ColorConstants.buttonDarker);
            gc.drawLine(getSashWidth() - 1, 0, getSashWidth() - 1, size.y);
            gc.setForeground(ColorConstants.buttonLightest);
            gc.drawLine(0, 0, 0, size.y);
        } else {
            gc.setForeground(ColorConstants.buttonDarker);
            gc.drawLine(0, 0, size.x, 0);
            gc.drawLine(0, getSashWidth() - 1, size.x, getSashWidth() - 1);
            gc.setForeground(ColorConstants.buttonLightest);
            gc.drawLine(0, 1, size.x, 1);
        }
    }

    private void onDragSash(Event event) {
        if (event.detail == SWT.DRAG) {
            // constrain feedback
            Rectangle area = getClientArea();
            if (orientation == SWT.HORIZONTAL) {
                if (controls[0].getData(MAINTAIN_SIZE) != null) {
                    event.x = Math.max(event.x, DRAG_MINIMUM);
                } else {
                    event.x = Math.min(event.x, area.width - DRAG_MINIMUM
                            - getSashWidth());
                }
            } else {
                event.y = Math.min(event.y, area.height - DRAG_MINIMUM
                        - getSashWidth());
            }
            return;
        }

        Sash sash = (Sash) event.widget;
        int sashIndex = -1;
        for (int i = 0; i < sashes.length; i++) {
            if (sashes[i] == sash) {
                sashIndex = i;
                break;
            }
        }
        if (sashIndex == -1)
            return;

        Control c1 = controls[sashIndex];
        Control c2 = controls[sashIndex + 1];
        Rectangle b1 = c1.getBounds();
        Rectangle b2 = c2.getBounds();
        controls = getControls(false);

        Rectangle sashBounds = sash.getBounds();
        if (orientation == SWT.HORIZONTAL) {
            int shift = event.x - sashBounds.x;
            b1.width += shift;
            b2.x += shift;
            b2.width -= shift;
        } else {
            int shift = event.y - sashBounds.y;
            b1.height += shift;
            b2.y += shift;
            b2.height -= shift;
        }

        c1.setBounds(b1);
        sash.setBounds(event.x, event.y, event.width, event.height);
        c2.setBounds(b2);
        // @TODO
        // This will only work when you have two controls, one of whom is set to
        // maintain size
        if (c1.getData(MAINTAIN_SIZE) != null) {
            setFixedSize(c1.getBounds().width);
        } else {
            setFixedSize(c2.getBounds().width);
        }
    }

    /**
     * If orientation is SWT.HORIZONTAL, lay the controls in the SashForm out
     * side by side. If orientation is SWT.VERTICAL, lay the controls in the
     * SashForm out top to bottom.
     */
    @Override
    public void setOrientation(int orientation) {
        if (this.orientation == orientation)
            return;
        if (orientation != SWT.HORIZONTAL && orientation != SWT.VERTICAL) {
            SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        }
        this.orientation = orientation;

        int sashOrientation = (orientation == SWT.HORIZONTAL) ? SWT.VERTICAL
                : SWT.HORIZONTAL;
        for (int i = 0; i < sashes.length; i++) {
            sashes[i].dispose();
            sashes[i] = new Sash(this, sashOrientation);
            sashes[i].setBackground(ColorConstants.buttonLightest);
            sashes[i].addListener(SWT.Selection, sashListener);
        }
        layout();
    }

    public void setSashWidth(int width) {
        sashWidth = width;
    }

    @Override
    public void setLayout(Layout layout) {
        // SashForm does not use Layout
    }

    /**
     * Specify the control that should take up the entire client area of the
     * SashForm. If one control has been maximized, and this method is called
     * with a different control, the previous control will be minimized and the
     * new control will be maximized.. if the value of control is null, the
     * SashForm will minimize all controls and return to the default layout
     * where all controls are laid out separated by sashes.
     */
    public void setMaximizedControl(Control control) {
        if (control == null) {
            if (maxControl != null) {
                this.maxControl = null;
                layout();
                for (int i = 0; i < sashes.length; i++) {
                    sashes[i].setVisible(true);
                }
            }
            return;
        }

        for (int i = 0; i < sashes.length; i++) {
            sashes[i].setVisible(false);
        }
        maxControl = control;
        layout();

    }

}
