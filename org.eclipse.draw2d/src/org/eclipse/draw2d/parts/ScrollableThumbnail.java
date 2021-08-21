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
package org.eclipse.draw2d.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureListener;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.KeyEvent;
import org.eclipse.draw2d.KeyListener;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A scaled image representation of a Figure. If the source Figure is not
 * completely visible, a SelectorFigure is placed over the thumbnail
 * representing the viewable area and can be dragged around to scroll the source
 * figure.
 */
public class ScrollableThumbnail extends Thumbnail {

    private class ClickScrollerAndDragTransferrer extends
            MouseMotionListener.Stub implements MouseListener {
        private boolean dragTransfer;

        @Override
        public void mouseDoubleClicked(MouseEvent me) {
        }

        @Override
        public void mouseDragged(MouseEvent me) {
            if (dragTransfer)
                syncher.mouseDragged(me);
        }

        @Override
        public void mousePressed(MouseEvent me) {
            if (!(ScrollableThumbnail.this.getClientArea().contains(me
                    .getLocation())))
                return;
            Dimension selectorCenter = selector.getBounds().getSize()
                    .scale(0.5f);
            Point scrollPoint = me
                    .getLocation()
                    .getTranslated(getLocation().getNegated())
                    .translate(selectorCenter.negate())
                    .scale(1.0f / getViewportScaleX(),
                            1.0f / getViewportScaleY())
                    .translate(viewport.getHorizontalRangeModel().getMinimum(),
                            viewport.getVerticalRangeModel().getMinimum());
            viewport.setViewLocation(scrollPoint);
            syncher.mousePressed(me);
            dragTransfer = true;
        }

        @Override
        public void mouseReleased(MouseEvent me) {
            syncher.mouseReleased(me);
            dragTransfer = false;
        }
    }

    private class ScrollSynchronizer extends MouseMotionListener.Stub implements
            MouseListener {
        private Point startLocation;
        private Point viewLocation;

        @Override
        public void mouseDoubleClicked(MouseEvent me) {
        }

        @Override
        public void mouseDragged(MouseEvent me) {
            if (startLocation != null) {
                Dimension d = me.getLocation().getDifference(startLocation);
                d.scale(1.0f / getViewportScaleX(), 1.0f / getViewportScaleY());
                viewport.setViewLocation(viewLocation.getTranslated(d));
                me.consume();
            }
        }

        @Override
        public void mousePressed(MouseEvent me) {
            startLocation = me.getLocation();
            viewLocation = viewport.getViewLocation();
            me.consume();
        }

        @Override
        public void mouseReleased(MouseEvent me) {
        }
    }

    private class SelectorFigure extends Figure {

        private Rectangle iBounds;

        private ImageData iData;

        public SelectorFigure() {
            PaletteData pData = new PaletteData(0xFF, 0xFF00, 0xFF0000);
            RGB rgb = ColorConstants.menuBackgroundSelected.getRGB();
            int fillColor = pData.getPixel(rgb);
            iData = new ImageData(1, 1, 24, pData);
            iData.setPixel(0, 0, fillColor);
            iData.setAlpha(0, 0, 55);
            iBounds = new Rectangle(0, 0, 1, 1);
        }

        @Override
        public void paintFigure(Graphics g) {
            Rectangle bounds = getBounds().getCopy();

            // Avoid drawing images that are 0 in dimension
            if (bounds.width < 5 || bounds.height < 5)
                return;

            // Don't paint the selector figure if the entire source is visible.
            Dimension thumbnailSize = new Dimension(getThumbnailImage());
            // expand to compensate for rounding errors in calculating bounds
            Dimension size = getSize().getExpanded(1, 1);
            if (size.contains(thumbnailSize))
                return;

            bounds.height--;
            bounds.width--;

            Image image = new Image(Display.getCurrent(), iData);
            g.drawImage(image, iBounds, bounds);
            image.dispose();

            g.setForegroundColor(ColorConstants.menuBackgroundSelected);
            g.drawRectangle(bounds);
        }
    }

    private FigureListener figureListener = new FigureListener() {
        @Override
        public void figureMoved(IFigure source) {
            reconfigureSelectorBounds();
        }
    };
    private KeyListener keyListener = new KeyListener.Stub() {
        @Override
        public void keyPressed(KeyEvent ke) {
            int moveX = viewport.getClientArea().width / 4;
            int moveY = viewport.getClientArea().height / 4;
            if (ke.keycode == SWT.HOME
                    || (isMirrored() ? ke.keycode == SWT.ARROW_RIGHT
                            : ke.keycode == SWT.ARROW_LEFT))
                viewport.setViewLocation(viewport.getViewLocation().translate(
                        -moveX, 0));
            else if (ke.keycode == SWT.END
                    || (isMirrored() ? ke.keycode == SWT.ARROW_LEFT
                            : ke.keycode == SWT.ARROW_RIGHT))
                viewport.setViewLocation(viewport.getViewLocation().translate(
                        moveX, 0));
            else if (ke.keycode == SWT.ARROW_UP || ke.keycode == SWT.PAGE_UP)
                viewport.setViewLocation(viewport.getViewLocation().translate(
                        0, -moveY));
            else if (ke.keycode == SWT.ARROW_DOWN
                    || ke.keycode == SWT.PAGE_DOWN)
                viewport.setViewLocation(viewport.getViewLocation().translate(
                        0, moveY));
        }
    };

    private PropertyChangeListener propListener = new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            reconfigureSelectorBounds();
        }
    };

    private ScrollSynchronizer syncher;
    private IFigure selector;
    private Viewport viewport;

    /**
     * Creates a new ScrollableThumbnail.
     */
    public ScrollableThumbnail() {
        super();
        initialize();
    }

    /**
     * Creates a new ScrollableThumbnail that synchs with the given Viewport.
     * 
     * @param port
     *            The Viewport
     */
    public ScrollableThumbnail(Viewport port) {
        super();
        setViewport(port);
        initialize();
    }

    /**
     * @see Thumbnail#deactivate()
     */
    @Override
    public void deactivate() {
        unhookViewport();
        unhookSelector();
        super.deactivate();
    }

    private double getViewportScaleX() {
        return (double) targetSize.width
                / viewport.getContents().getBounds().width;
    }

    private double getViewportScaleY() {
        return (double) targetSize.height
                / viewport.getContents().getBounds().height;
    }

    private void hookSelector() {
        selector.addMouseListener(syncher = new ScrollSynchronizer());
        selector.addMouseMotionListener(syncher);
        selector.addKeyListener(keyListener);
        add(selector);
    }

    private void hookViewport() {
        viewport.addPropertyChangeListener(Viewport.PROPERTY_VIEW_LOCATION,
                propListener);
        viewport.addFigureListener(figureListener);
    }

    private void initialize() {
        selector = new SelectorFigure();
        selector.setFocusTraversable(true);
        hookSelector();
        ClickScrollerAndDragTransferrer transferrer = new ClickScrollerAndDragTransferrer();
        addMouseListener(transferrer);
        addMouseMotionListener(transferrer);
    }

    private void reconfigureSelectorBounds() {
        Rectangle rect = new Rectangle();
        Point offset = viewport.getViewLocation();
        offset.x -= viewport.getHorizontalRangeModel().getMinimum();
        offset.y -= viewport.getVerticalRangeModel().getMinimum();
        rect.setLocation(offset);
        rect.setSize(viewport.getClientArea().getSize());
        rect.scale(getViewportScaleX(), getViewportScaleY());
        rect.translate(getClientArea().getLocation());
        selector.setBounds(rect);
    }

    /**
     * Reconfigures the SelectorFigure's bounds if the scales have changed.
     * 
     * @param scaleX
     *            The X scale
     * @param scaleY
     *            The Y scale
     * @see org.eclipse.draw2d.parts.Thumbnail#setScales(float, float)
     */
    @Override
    protected void setScales(float scaleX, float scaleY) {
        if (scaleX == getScaleX() && scaleY == getScaleY())
            return;

        super.setScales(scaleX, scaleY);
        reconfigureSelectorBounds();
    }

    /**
     * Sets the Viewport that this ScrollableThumbnail will synch with.
     * 
     * @param port
     *            The Viewport
     */
    public void setViewport(Viewport port) {
        viewport = port;
        hookViewport();
    }

    private void unhookSelector() {
        selector.removeKeyListener(keyListener);
        selector.removeMouseMotionListener(syncher);
        selector.removeMouseListener(syncher);
        remove(selector);
    }

    private void unhookViewport() {
        viewport.removePropertyChangeListener(Viewport.PROPERTY_VIEW_LOCATION,
                propListener);
        viewport.removeFigureListener(figureListener);
    }

}
