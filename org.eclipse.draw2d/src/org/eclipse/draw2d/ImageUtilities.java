/*******************************************************************************
 * Copyright (c) 2003, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.geometry.Dimension;

/**
 * @author Pratik Shah
 * @since 3.0
 */
public class ImageUtilities {

    /**
     * Returns a new Image with the given String rotated left (by 90 degrees).
     * The String will be rendered using the provided colors and fonts. The
     * client is responsible for disposing the returned Image. Strings cannot
     * contain newline or tab characters.
     * 
     * @param string
     *            the String to be rendered
     * @param font
     *            the font
     * @param foreground
     *            the text's color
     * @param background
     *            the background color
     * @return an Image which must be disposed
     */
    public static Image createRotatedImageOfString(String string, Font font,
            Color foreground, Color background) {
        Display display = Display.getDefault();

        FontMetrics metrics = FigureUtilities.getFontMetrics(font);
        Dimension strSize = FigureUtilities.getStringExtents(string, font);
        Image srcImage = new Image(display, strSize.width, metrics.getAscent()
                + metrics.getDescent() + metrics.getLeading());
        GC gc = new GC(srcImage);
        gc.setFont(font);
        gc.setForeground(foreground);
        gc.setBackground(background);
        gc.fillRectangle(srcImage.getBounds());
        gc.drawString(string, 0, 0);
        Image result = createRotatedImage(srcImage);
        gc.dispose();
        srcImage.dispose();
        return result;
    }

    /**
     * Returns a new Image that is the given Image rotated left by 90 degrees.
     * The client is responsible for disposing the returned Image.
     * 
     * @param srcImage
     *            the Image that is to be rotated left
     * @return the rotated Image (the client is responsible for disposing it)
     */
    public static Image createRotatedImage(Image srcImage) {
        Display display = Display.getDefault();

        ImageData srcData = srcImage.getImageData();
        ImageData destData;
        if (srcData.depth < 8)
            destData = rotatePixelByPixel(srcData);
        else
            destData = rotateOptimized(srcData);

        return new Image(display, destData);
    }

    /**
     * Creates an ImageData representing the given <code>Image</code> shaded
     * with the given <code>Color</code>.
     * 
     * @param fromImage
     *            Image that has to be shaded
     * @param shade
     *            The Color to be used for shading
     * @return A new ImageData that can be used to create an Image.
     */
    public static ImageData createShadedImage(Image fromImage, Color shade) {
        org.eclipse.swt.graphics.Rectangle r = fromImage.getBounds();
        ImageData data = fromImage.getImageData();
        PaletteData palette = data.palette;
        if (!palette.isDirect) {
            /* Convert the palette entries */
            RGB[] rgbs = palette.getRGBs();
            for (int i = 0; i < rgbs.length; i++) {
                if (data.transparentPixel != i) {
                    RGB color = rgbs[i];
                    color.red = determineShading(color.red, shade.getRed());
                    color.blue = determineShading(color.blue, shade.getBlue());
                    color.green = determineShading(color.green,
                            shade.getGreen());
                }
            }
            data.palette = new PaletteData(rgbs);
        } else {
            /* Convert the pixels. */
            int[] scanline = new int[r.width];
            int redMask = palette.redMask;
            int greenMask = palette.greenMask;
            int blueMask = palette.blueMask;
            int redShift = palette.redShift;
            int greenShift = palette.greenShift;
            int blueShift = palette.blueShift;
            for (int y = 0; y < r.height; y++) {
                data.getPixels(0, y, r.width, scanline, 0);
                for (int x = 0; x < r.width; x++) {
                    int pixel = scanline[x];
                    int red = pixel & redMask;
                    red = (redShift < 0) ? red >>> -redShift : red << redShift;
                    int green = pixel & greenMask;
                    green = (greenShift < 0) ? green >>> -greenShift
                            : green << greenShift;
                    int blue = pixel & blueMask;
                    blue = (blueShift < 0) ? blue >>> -blueShift
                            : blue << blueShift;
                    red = determineShading(red, shade.getRed());
                    blue = determineShading(blue, shade.getBlue());
                    green = determineShading(green, shade.getGreen());
                    red = (redShift < 0) ? red << -redShift : red >> redShift;
                    red &= redMask;
                    green = (greenShift < 0) ? green << -greenShift
                            : green >> greenShift;
                    green &= greenMask;
                    blue = (blueShift < 0) ? blue << -blueShift
                            : blue >> blueShift;
                    blue &= blueMask;
                    scanline[x] = red | blue | green;
                }
                data.setPixels(0, y, r.width, scanline, 0);
            }
        }
        return data;
    }

    private static int determineShading(int origColor, int shadeColor) {
        return (origColor + shadeColor) / 2;
    }

    private static ImageData rotateOptimized(ImageData srcData) {
        int bytesPerPixel = Math.max(1, srcData.depth / 8);
        int destBytesPerLine = ((srcData.height * bytesPerPixel - 1)
                / srcData.scanlinePad + 1)
                * srcData.scanlinePad;
        byte[] newData = new byte[destBytesPerLine * srcData.width];
        for (int srcY = 0; srcY < srcData.height; srcY++) {
            for (int srcX = 0; srcX < srcData.width; srcX++) {
                int destX = srcY;
                int destY = srcData.width - srcX - 1;
                int destIndex = (destY * destBytesPerLine)
                        + (destX * bytesPerPixel);
                int srcIndex = (srcY * srcData.bytesPerLine)
                        + (srcX * bytesPerPixel);
                System.arraycopy(srcData.data, srcIndex, newData, destIndex,
                        bytesPerPixel);
            }
        }
        return new ImageData(srcData.height, srcData.width, srcData.depth,
                srcData.palette, srcData.scanlinePad, newData);
    }

    private static ImageData rotatePixelByPixel(ImageData srcData) {
        ImageData destData = new ImageData(srcData.height, srcData.width,
                srcData.depth, srcData.palette);
        for (int y = 0; y < srcData.height; y++) {
            for (int x = 0; x < srcData.width; x++) {
                destData.setPixel(y, srcData.width - x - 1,
                        srcData.getPixel(x, y));
            }
        }
        return destData;
    }

}
