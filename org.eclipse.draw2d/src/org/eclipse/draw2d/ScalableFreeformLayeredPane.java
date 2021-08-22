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

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.geometry.Translatable;

/**
 * @author hudsonr
 * @since 2.1
 */
public class ScalableFreeformLayeredPane extends FreeformLayeredPane implements
        ScalableFigure {

    private double scale = 1.0;
    
    private boolean useScaledGraphics = true;

    public ScalableFreeformLayeredPane() {
    }

    public ScalableFreeformLayeredPane(boolean useScaledGraphics) {
        this.useScaledGraphics = useScaledGraphics;
    }

    /**
     * @see org.eclipse.draw2d.Figure#getClientArea()
     */
    @Override
    public Rectangle getClientArea(Rectangle rect) {
        super.getClientArea(rect);
        rect.width /= scale;
        rect.height /= scale;
        rect.x /= scale;
        rect.y /= scale;
        return rect;
    }

    /**
     * Returns the current zoom scale level.
     * 
     * @return the scale
     */
    @Override
    public double getScale() {
        return scale;
    }

    /**
     * @see org.eclipse.draw2d.IFigure#isCoordinateSystem()
     */
    @Override
    public boolean isCoordinateSystem() {
        return true;
    }

    /**
     * @see org.eclipse.draw2d.Figure#paintClientArea(Graphics)
     */
    @Override
    protected void paintClientArea(Graphics graphics) {
        if (getChildren().isEmpty())
            return;
        if (scale == 1.0) {
            super.paintClientArea(graphics);
        } else {
            boolean optimizeClip = getBorder() == null
                    || getBorder().isOpaque();

            if(useScaledGraphics) {
                ScaledGraphics g = new ScaledGraphics(graphics);
                if (!optimizeClip)
                    g.clipRect(getBounds().getShrinked(getInsets()));
                g.scale(scale);
                g.pushState();
                paintChildren(g);
                g.dispose();
            }
            else {
                if (!optimizeClip)
                    graphics.clipRect(getBounds().getShrinked(getInsets()));
                graphics.scale(scale);
                graphics.pushState();
                paintChildren(graphics);
                graphics.popState();
            }
            
            graphics.restoreState();
        }
    }

    /**
     * Sets the zoom level
     * 
     * @param newZoom
     *            The new zoom level
     */
    @Override
    public void setScale(double newZoom) {
        if (scale == newZoom)
            return;
        scale = newZoom;
        superFireMoved(); // For AncestorListener compatibility
        getFreeformHelper().invalidate();
        repaint();
    }

    /**
     * @see org.eclipse.draw2d.Figure#translateToParent(Translatable)
     */
    @Override
    public void translateToParent(Translatable t) {
        t.performScale(scale);
    }

    /**
     * @see org.eclipse.draw2d.Figure#translateFromParent(Translatable)
     */
    @Override
    public void translateFromParent(Translatable t) {
        t.performScale(1 / scale);
    }

    /**
     * @see org.eclipse.draw2d.Figure#useLocalCoordinates()
     */
    @Override
    protected final boolean useLocalCoordinates() {
        return false;
    }

}
