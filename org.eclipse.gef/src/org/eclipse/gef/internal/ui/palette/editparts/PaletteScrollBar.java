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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.Clickable;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RangeModel;
import org.eclipse.draw2d.ScrollBar;
import org.eclipse.draw2d.ScrollBarLayout;
import org.eclipse.draw2d.Toggle;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.internal.ui.palette.PaletteColorUtil;

public final class PaletteScrollBar extends ScrollBar {

    private static final PointList OUTER_DOWN_TRIANGLE = new PointList(3);
    private static final PointList INNER_DOWN_TRIANGLE = new PointList(3);
    private static final PointList OUTER_UP_TRIANGLE = new PointList(3);
    private static final PointList INNER_UP_TRIANGLE = new PointList(3);

    public static final int BUTTON_HEIGHT = 7;
    private static final int BUTTON_WIDTH = 76;
    private static final int SCROLL_TIME = 200;

    private static final Image TRANSPARENCY;

    static {
        Display display = Display.getCurrent();
        PaletteData pData = new PaletteData(0xFF, 0xFF00, 0xFF0000);
        RGB rgb = display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW)
                .getRGB();
        int fillColor = pData.getPixel(rgb);
        ImageData iData = new ImageData(1, 1, 24, pData);
        iData.setPixel(0, 0, fillColor);
        iData.setAlpha(0, 0, 200);
        TRANSPARENCY = new Image(display, iData);

        OUTER_DOWN_TRIANGLE.addPoint(new Point(34, 2));
        OUTER_DOWN_TRIANGLE.addPoint(new Point(38, 6));
        OUTER_DOWN_TRIANGLE.addPoint(new Point(42, 2));

        INNER_DOWN_TRIANGLE.addPoint(new Point(35, 2));
        INNER_DOWN_TRIANGLE.addPoint(new Point(37, 5));
        INNER_DOWN_TRIANGLE.addPoint(new Point(41, 2));

        OUTER_UP_TRIANGLE.addPoint(new Point(33, 5));
        OUTER_UP_TRIANGLE.addPoint(new Point(38, 0));
        OUTER_UP_TRIANGLE.addPoint(new Point(42, 5));

        INNER_UP_TRIANGLE.addPoint(new Point(34, 5));
        INNER_UP_TRIANGLE.addPoint(new Point(38, 1));
        INNER_UP_TRIANGLE.addPoint(new Point(42, 5));

    }

    protected Label downLabel;

    protected Label upLabel;

    public PaletteScrollBar() {
        super();
    }

    @Override
    public boolean containsPoint(int x, int y) {
        return findDescendantAtExcluding(x, y, IdentitySearch.INSTANCE) != null;
    }

    @Override
    protected Clickable createDefaultDownButton() {
        return createTransparentArrowButton(true);
    }

    @Override
    protected Clickable createDefaultUpButton() {
        return createTransparentArrowButton(false);
    }

    /**
     * Creates the figure used for the scrollbar button.
     * 
     * @param down
     *            true if the arrow should be pointing down; false, if it should
     *            be pointing up.
     * @return a new <code>Toggle</code> figure for the scroll bar button
     */
    private Toggle createTransparentArrowButton(final boolean down) {
        Toggle button = new Toggle() {
            @Override
            protected void paintFigure(Graphics g) {
                // paint background
                if (!getModel().isMouseOver())
                    g.drawImage(TRANSPARENCY, new Rectangle(0, 0, 1, 1),
                            getBounds());
                else {
                    g.setBackgroundColor(getModel().isArmed() ? PaletteColorUtil
                            .getSelectedColor() : PaletteColorUtil
                            .getHoverColor());
                    g.fillRectangle(getBounds());
                }

                // paint triangle
                g.translate(getLocation());
                PointList outerPoints = transpose(down ? OUTER_DOWN_TRIANGLE
                        : OUTER_UP_TRIANGLE);
                PointList innerPoints = transpose(down ? INNER_DOWN_TRIANGLE
                        : INNER_UP_TRIANGLE);
                g.setBackgroundColor(PaletteColorUtil.WIDGET_LIST_BACKGROUND);
                g.fillPolygon(outerPoints);
                g.setBackgroundColor(PaletteColorUtil.WIDGET_DARK_SHADOW);
                g.fillPolygon(innerPoints);
                g.translate(getLocation().getNegated());
            }
        };
        button.setRolloverEnabled(true);
        button.setRequestFocusEnabled(false);
        return button;
    }

    /**
     * Transposes a list of points using the <code>transposer</code>.
     * 
     * @param origPoints
     *            the original list of points
     * @return a new list of transposed points
     */
    private PointList transpose(PointList origPoints) {
        PointList transposedPoints = new PointList(origPoints.size());
        for (int i = 0; i < origPoints.size(); i++) {
            transposedPoints.addPoint(transposer.t(origPoints.getPoint(i)));
        }
        return transposedPoints;
    }

    @Override
    public IFigure findFigureAt(int x, int y, TreeSearch search) {
        IFigure result = super.findFigureAt(x, y, search);
        if (result != this)
            return result;
        return null;
    }

    @Override
    public Dimension getPreferredSize(int wHint, int hHint) {
        return new Dimension(wHint, hHint);
    }

    @Override
    protected void initialize() {
        super.initialize();
        setLayoutManager(new ScrollBarLayout(transposer) {
            @SuppressWarnings("deprecation")
            @Override
            protected Rectangle layoutButtons(ScrollBar scrollBar) {
                Rectangle bounds = transposer.t(scrollBar.getClientArea());
                Dimension buttonSize = new Dimension(BUTTON_WIDTH,
                        BUTTON_HEIGHT);

                getButtonUp().setBounds(
                        transposer.t(new Rectangle(bounds.getTop()
                                .getTranslated(-(buttonSize.width / 2), 0),
                                buttonSize)));
                Rectangle r = new Rectangle(bounds.getBottom().getTranslated(
                        -(buttonSize.width / 2), -buttonSize.height),
                        buttonSize);
                getButtonDown().setBounds(transposer.t(r));
                Rectangle trackBounds = bounds.getCropped(new Insets(
                        buttonSize.height, 0, buttonSize.height, 0));
                RangeModel model = scrollBar.getRangeModel();
                getButtonUp()
                        .setVisible(model.getValue() != model.getMinimum());
                getButtonDown().setVisible(
                        model.getValue() != model.getMaximum()
                                - model.getExtent());
                return trackBounds;
            }
        });
        setPageUp(null);
        setPageDown(null);
        setThumb(null);
        setOpaque(false);
    }

    @Override
    protected void stepDown() {
        timedStep(false);
    }

    @Override
    protected void stepUp() {
        timedStep(true);
    }

    protected void timedStep(boolean up) {
        int increment = Math.max(getExtent() * 1 / 2, getStepIncrement());
        int value = getValue();
        long startTime = System.currentTimeMillis();
        long elapsedTime = System.currentTimeMillis() - startTime;
        while (elapsedTime < SCROLL_TIME) {
            int step = (int) (increment * elapsedTime / SCROLL_TIME);
            step = up ? value - step : value + step;
            setValue(step);
            getUpdateManager().performUpdate();
            elapsedTime = System.currentTimeMillis() - startTime;
        }
    }

    protected void updateDownLabel() {
        getButtonDown().setVisible(getValue() < (getMaximum() - getExtent()));
    }

    protected void updateUpLabel() {
        getButtonUp().setVisible(getValue() > getMinimum());
    }

}
