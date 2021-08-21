/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.editparts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.SnapToGrid;

/**
 * This is a layer that displays the grid. The default grid color is
 * {@link org.eclipse.draw2d.ColorConstants#lightGray light gray}. To change the
 * grid color, set the foreground color for this layer.
 * 
 * @author Pratik Shah
 * @since 3.0
 */
public class GridLayer extends FreeformLayer {

    /**
     * Field indicating the horizontal grid spacing
     */
    protected int gridX = SnapToGrid.DEFAULT_GRID_SIZE;
    /**
     * Field for the vertical grid spacing
     */
    protected int gridY = SnapToGrid.DEFAULT_GRID_SIZE;

    /**
     * Field indicating what the grid origin is. This is used simply to
     * determine the offset from 0,0.
     */
    protected Point origin = new Point();

    /**
     * Constructor Sets the default grid color: ColorConstants.lightGray
     */
    public GridLayer() {
        super();
        setForegroundColor(ColorConstants.lightGray);
    }

    /**
     * Overridden to indicate no preferred size. The grid layer should not
     * affect the size of the layered pane in which it is placed.
     * 
     * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
     */
    @Override
    public Dimension getPreferredSize(int wHint, int hHint) {
        return new Dimension();
    }

    /**
     * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
     */
    @Override
    protected void paintFigure(Graphics graphics) {
        super.paintFigure(graphics);
        paintGrid(graphics);
    }

    /**
     * Paints the grid. Sub-classes can override to customize the grid's look.
     * If this layer is being used with SnapToGrid, this method will only be
     * invoked when the {@link SnapToGrid#PROPERTY_GRID_VISIBLE visibility}
     * property is set to true.
     * 
     * @param g
     *            The Graphics object to be used to do the painting
     * @see FigureUtilities#paintGrid(Graphics, IFigure, Point, int, int)
     */
    protected void paintGrid(Graphics g) {
        FigureUtilities.paintGrid(g, this, origin, gridX, gridY);
    }

    /**
     * Sets the origin of the grid. The origin is used only to determine the
     * offset from 0,0.
     * 
     * @param p
     *            the origin
     */
    public void setOrigin(Point p) {
        if (p == null)
            p = new Point();
        if (!origin.equals(p)) {
            origin = p;
            repaint();
        }
    }

    /**
     * Sets the horizontal and vertical spacing of the grid. A grid spacing of 0
     * will be replaced with the {@link SnapToGrid#DEFAULT_GRID_SIZE default}
     * spacing. A negative spacing will cause no grid lines to be drawn for that
     * dimension.
     * 
     * @param spacing
     *            A Dimension representing the horizontal (width) and vertical
     *            (height) gaps
     */
    public void setSpacing(Dimension spacing) {
        if (spacing == null)
            spacing = new Dimension(SnapToGrid.DEFAULT_GRID_SIZE,
                    SnapToGrid.DEFAULT_GRID_SIZE);
        if (!spacing.equals(gridX, gridY)) {
            gridX = spacing.width != 0 ? spacing.width : gridX;
            gridY = spacing.height != 0 ? spacing.height : gridY;
            repaint();
        }
    }

}
