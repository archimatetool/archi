/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Research Group Software Construction,
 *     RWTH Aachen University, Germany - initial API and implementation
 */
package org.eclipse.draw2d;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A figure used to render a partly transparent copy of an original source
 * figure.
 * 
 * This class is pretty much based on a sample, posted within the GEF newsgroup
 * (http://dev.eclipse.org/newslists/news.eclipse.tools.gef/msg15158.html),
 * although we decided to not cache the ghost image itself (but only its image
 * data), so the figure does not have to be disposed (and may thus directly
 * extend {@link Figure} rather than {@link ImageFigure}).
 * 
 * @author Alexander Nyssen
 * @author Philip Ritzkopf
 * 
 * @since 3.6
 */
public class GhostImageFigure extends Figure {

    private int alpha = -1;
    private ImageData ghostImageData;

    /**
     * The single constructor.
     * 
     * @param source
     *            The original figure that will be used to render the ghost
     *            image.
     * @param alpha
     *            The desired transparency value, to be forwarded to
     *            {@link Graphics#setAlpha(int)}.
     * @param transparency
     *            The RBG value of the color that is to be regarded as
     *            transparent. May be <code>null</code>.
     */
    public GhostImageFigure(final IFigure source, int alpha, RGB transparency) {
        this.alpha = alpha;

        Rectangle sourceFigureRelativePrecisionBounds = new PrecisionRectangle(
                source.getBounds().getCopy());

        Image offscreenImage = new Image(Display.getCurrent(),
                sourceFigureRelativePrecisionBounds.width,
                sourceFigureRelativePrecisionBounds.height);

        GC gc = new GC(offscreenImage);
        SWTGraphics swtGraphics = new SWTGraphics(gc);
        swtGraphics.translate(-sourceFigureRelativePrecisionBounds.x,
                -sourceFigureRelativePrecisionBounds.y);
        source.paint(swtGraphics);

        ghostImageData = offscreenImage.getImageData();
        if (transparency != null) {
            ghostImageData.transparentPixel = ghostImageData.palette
                    .getPixel(transparency);
        }

        offscreenImage.dispose();
        swtGraphics.dispose();
        gc.dispose();
    }

    /**
     * @see Figure#paintFigure(Graphics)
     */
    @Override
    protected void paintFigure(Graphics graphics) {
        Image feedbackImage = new Image(Display.getCurrent(), ghostImageData);
        graphics.setAlpha(alpha);
        graphics.setClip(getBounds().getCopy());
        graphics.drawImage(feedbackImage, 0, 0, ghostImageData.width,
                ghostImageData.height, getBounds().x, getBounds().y,
                getBounds().width, getBounds().height);
        feedbackImage.dispose();
    }
}