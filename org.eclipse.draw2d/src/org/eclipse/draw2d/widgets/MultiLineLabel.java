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
package org.eclipse.draw2d.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.ACC;
import org.eclipse.swt.accessibility.AccessibleAdapter;
import org.eclipse.swt.accessibility.AccessibleControlAdapter;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FocusEvent;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.TextFlow;

/**
 * A widget for displaying a multi-line string. The label will have a vertical
 * or horizontal scrollbar when needed. Unlike the platform Label, this label is
 * focusable and accessible to screen-readers.
 * 
 * @author hudsonr
 */
public final class MultiLineLabel extends FigureCanvas {

    private TextFlow textFlow;
    static final Border MARGIN = new MarginBorder(2);
    private Image image;

    class FocusableViewport extends Viewport {
        FocusableViewport() {
            super(true);
            setFocusTraversable(true);
            setBorder(MARGIN);
        }

        @Override
        public void handleFocusGained(FocusEvent event) {
            super.handleFocusGained(event);
            repaint();
        }

        @Override
        public void handleFocusLost(FocusEvent event) {
            super.handleFocusLost(event);
            repaint();
        }

        @Override
        protected void paintBorder(Graphics graphics) {
            super.paintBorder(graphics);
            if (hasFocus()) {
                graphics.setForegroundColor(ColorConstants.black);
                graphics.setBackgroundColor(ColorConstants.white);
                graphics.drawFocus(getBounds().getResized(-1, -1));
            }
        }
    }

    /**
     * Constructs a new MultiLineLabel with the given parent.
     * 
     * @param parent
     *            the parent
     */
    public MultiLineLabel(Composite parent) {
        super(parent);
        setViewport(new FocusableViewport());

        FlowPage page = new FlowPage();
        textFlow = new TextFlow();
        page.add(textFlow);

        setContents(page);
        getViewport().setContentsTracksWidth(true);
        addAccessibility();
    }

    private void addAccessibility() {
        getAccessible().addAccessibleControlListener(
                new AccessibleControlAdapter() {
                    @Override
                    public void getRole(AccessibleControlEvent e) {
                        e.detail = ACC.ROLE_LABEL;
                    }

                    @Override
                    public void getState(AccessibleControlEvent e) {
                        e.detail = ACC.STATE_READONLY;
                    }
                });
        getAccessible().addAccessibleListener(new AccessibleAdapter() {
            @Override
            public void getName(AccessibleEvent e) {
                e.result = getText();
            }
        });
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Point p = getViewport().getViewLocation();
                int dy = getFont().getFontData()[0].getHeight();
                int dx = dy * 3 / 2;
                boolean mirrored = (e.widget.getStyle() & SWT.MIRRORED) != 0;
                if (e.keyCode == SWT.ARROW_DOWN) {
                    scrollToY(p.y + dy / 2);
                    scrollToY(p.y + dy);
                    scrollToY(p.y + dy * 3 / 2);
                    scrollToY(p.y + dy * 2);
                } else if (e.keyCode == SWT.ARROW_UP) {
                    scrollToY(p.y - dy / 2);
                    scrollToY(p.y - dy);
                    scrollToY(p.y - dy * 3 / 2);
                    scrollToY(p.y - dy * 2);
                } else if ((!mirrored && e.keyCode == SWT.ARROW_RIGHT)
                        || (mirrored && e.keyCode == SWT.ARROW_LEFT)) {
                    scrollToX(p.x + dx);
                    scrollToX(p.x + dx * 2);
                    scrollToX(p.x + dx * 3);
                } else if ((!mirrored && e.keyCode == SWT.ARROW_LEFT)
                        || (mirrored && e.keyCode == SWT.ARROW_RIGHT)) {
                    scrollToX(p.x - dx);
                    scrollToX(p.x - dx * 2);
                    scrollToX(p.x - dx * 3);
                }
            }
        });
    }

    /**
     * @see org.eclipse.swt.widgets.Control#setEnabled(boolean)
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        textFlow.setEnabled(getEnabled());
    }

    /**
     * @return the Image for this label, or <code>null</code> if there is none
     * @see #setImage(Image)
     */
    public Image getImage() {
        return image;
    }

    /**
     * Returns the text in this label.
     * 
     * @return the text
     */
    public String getText() {
        return textFlow.getText();
    }

    /**
     * @see org.eclipse.swt.widgets.Canvas#setFont(org.eclipse.swt.graphics.Font)
     */
    @Override
    public void setFont(Font font) {
        super.setFont(font);
        textFlow.revalidate();
    }

    /**
     * @param image
     *            The <code>Image</code> to be used for this label. It can be
     *            <code>null</code>.
     */
    public void setImage(Image image) {
        this.image = image;
        if (image != null)
            setBorder(new ImageBorder(image));
        else
            setBorder(null);
    }

    /**
     * Sets the text for this label.
     * 
     * @param text
     *            the new text
     */
    public void setText(String text) {
        textFlow.setText(text);
    }

}