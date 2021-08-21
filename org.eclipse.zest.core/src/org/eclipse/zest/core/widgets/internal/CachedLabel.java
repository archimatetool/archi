/*******************************************************************************
 * Copyright 2005-2006, CHISEL Group, University of Victoria, Victoria, BC,
 * Canada. All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: The Chisel Group, University of Victoria
 ******************************************************************************/
package org.eclipse.zest.core.widgets.internal;

import org.eclipse.draw2d.Animation;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.ScaledGraphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * A cached label to improve performance of text drawing under linux
 * 
 * @author Ian Bull
 * 
 */
public abstract class CachedLabel extends Label {

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.Label#paintFigure(org.eclipse.draw2d.Graphics)
     */
    Image cachedImage = null;
    boolean cacheLabel = false;
    boolean invalidationRequired = false;

    /**
     * CachedLabel constructor.
     * 
     * @param cacheLabel
     *            Should the label be cached, or should the text be re-layedout
     *            each time
     */
    public CachedLabel(boolean cacheLabel) {
        this.cacheLabel = cacheLabel;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.Label#setIcon(org.eclipse.swt.graphics.Image)
     */
    @Override
    public void setIcon(Image image) {
        updateInvalidation();
        super.setIcon(image);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.Figure#setForegroundColor(org.eclipse.swt.graphics.Color)
     */
    @Override
    public void setForegroundColor(Color fg) {
        updateInvalidation();
        super.setForegroundColor(fg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.Figure#setBackgroundColor(org.eclipse.swt.graphics.Color)
     */
    @Override
    public void setBackgroundColor(Color bg) {
        updateInvalidation();
        super.setBackgroundColor(bg);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.Figure#setFont(org.eclipse.swt.graphics.Font)
     */
    @Override
    public void setFont(Font f) {
        updateInvalidation();
        super.setFont(f);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.Label#setText(java.lang.String)
     */
    @Override
    public void setText(String s) {
        updateInvalidation();
        super.setText(s);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.Figure#setSize(int, int)
     */
    @Override
    public void setSize(int w, int h) {
        updateInvalidation();

        if (cachedImage != null && shouldInvalidateCache()) {
            cleanImage();
        }
        super.setSize(w, h);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.Figure#setBounds(org.eclipse.draw2d.geometry.Rectangle)
     */
    @Override
    public void setBounds(Rectangle rect) {
        boolean resize = (rect.width != bounds.width) || (rect.height != bounds.height);

        if (resize && Animation.isAnimating()) {
            updateInvalidation();
        }
        if (resize && shouldInvalidateCache() && cachedImage != null) {
            cleanImage();
        }

        super.setBounds(rect);
    }

    /**
     * Override this method to return the background colour for the text Note:
     * Text must have a background color since it is being stored in an image
     * (You can set it to white if you want)
     * 
     * @return
     */
    protected abstract Color getBackgroundTextColor();

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.draw2d.Label#paintFigure(org.eclipse.draw2d.Graphics)
     */
    static Rectangle tempRect = new Rectangle();

    @Override
    protected void paintFigure(Graphics graphics) {
        if (graphics.getClass() == ScaledGraphics.class) {
            if (((ScaledGraphics) graphics).getAbsoluteScale() < 0.30) {
                return;
            }
        }
        if (!cacheLabel) {
            if (isOpaque()) {
                super.paintFigure(graphics);
            }
            Rectangle bounds = getBounds();
            graphics.translate(bounds.x, bounds.y);
            if (getIcon() != null) {
                graphics.drawImage(getIcon(), getIconLocation());
            }
            if (!isEnabled()) {
                graphics.translate(1, 1);
                graphics.setForegroundColor(ColorConstants.buttonLightest);
                graphics.drawText(getSubStringText(), getTextLocation());
                graphics.translate(-1, -1);
                graphics.setForegroundColor(ColorConstants.buttonDarker);
            }
            graphics.drawText(getText(), getTextLocation());
            graphics.translate(-bounds.x, -bounds.y);
            return;
        }

        if (isOpaque()) {
            graphics.fillRectangle(getBounds());
        }
        Rectangle bounds = getBounds();
        graphics.translate(bounds.x, bounds.y);

        Image icon = getIcon();

        if (icon != null) {
            graphics.drawImage(icon, getIconLocation());
        }

        int width = getSubStringTextSize().width;
        int height = getSubStringTextSize().height;

        if (cachedImage == null || shouldInvalidateCache()) {
            invalidationRequired = false;
            cleanImage();
            cachedImage = new Image(Display.getCurrent(), width, height);

            // @tag TODO : Dispose of the image properly
            //ZestPlugin.getDefault().addImage(cachedImage.toString(), cachedImage);

            GC gc = new GC(cachedImage);

            Graphics graphics2 = new SWTGraphics(gc);
            graphics2.setBackgroundColor(getBackgroundTextColor());
            graphics2.fillRectangle(0, 0, width, height);
            graphics2.setForegroundColor(getForegroundColor());
            //graphics2.drawText(getSubStringText(), new Point(0, 0));
            graphics2.drawText(getText(), new Point(0, 0));
            gc.dispose();

        }
        graphics.drawImage(cachedImage, getTextLocation());
        graphics.translate(-bounds.x, -bounds.y);
        this.paintBorder(graphics);

    }

    /**
     * Determine if the image should be remade or not
     * 
     * @return
     */
    private boolean shouldInvalidateCache() {
        if (invalidationRequired && !Animation.isAnimating()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Notifies the cache that the image will need updating.
     */
    private void updateInvalidation() {
        invalidationRequired = true;
    }

    protected void cleanImage() {
        if (cachedImage != null) {

            //ZestPlugin.getDefault().removeImage(cachedImage.toString());
            cachedImage.dispose();
            cachedImage = null;
        }
    }
}
