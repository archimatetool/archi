/*******************************************************************************
 * Copyright (c) 2006-2007 Nicolas Richeton.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors :
 *    Nicolas Richeton (nicolas.richeton@gmail.com) - initial API and implementation
 *    Richard Michalsky - bugs 195415,  195443
 *******************************************************************************/
package org.eclipse.nebula.widgets.gallery;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Custom DefaultGalleryItemRenderer 
 * 
 * This is so we can use smaller image sizes if the image is smaller than the suggested size.
 * This ensures that smaller images are not stretched to fit the Gallery Item.
 */
public class CustomDefaultGalleryItemRenderer extends DefaultGalleryItemRenderer {

	// Vars used during drawing (optimization)
	private boolean _drawBackground = false;
	private Color _drawBackgroundColor = null;
	private Image _drawImage = null;
	private Color _drawForegroundColor = null;
	
	@Override
    public void draw(GC gc, GalleryItem item, int index, int x, int y,
			int width, int height) {
		_drawImage = item.getImage();
		_drawForegroundColor = getForeground(item);

		// Set up the GC
		gc.setFont(getFont(item));

		// Create some room for the label.
		int useableHeight = height;
		int fontHeight = 0;
		if (item.getText() != null && !EMPTY_STRING.equals(item.getText())
				&& this.showLabels) {
			fontHeight = gc.getFontMetrics().getHeight();
			useableHeight -= fontHeight + 2;
		}

		int imageWidth = 0;
		int imageHeight = 0;
		int xShift = 0;
		int yShift = 0;
		Point size = null;

		if (_drawImage != null) {
			Rectangle itemImageBounds = _drawImage.getBounds();
			imageWidth = itemImageBounds.width;
			imageHeight = itemImageBounds.height;

			// Use our getBestSize method
			size = getBestSize(imageWidth, imageHeight,
					width - 8 - 2 * this.dropShadowsSize,
					useableHeight - 8 - 2 * this.dropShadowsSize);
			
			xShift = RendererHelper.getShift(width, size.x);
			yShift = RendererHelper.getShift(useableHeight, size.y);

			if (dropShadows) {
				Color c = null;
				for (int i = this.dropShadowsSize - 1; i >= 0; i--) {
					c = dropShadowsColors.get(i);
					gc.setForeground(c);

					gc.drawLine(x + width + i - xShift - 1,
							y + dropShadowsSize + yShift,
							x + width + i - xShift - 1,
							y + useableHeight + i - yShift);
					gc.drawLine(x + xShift + dropShadowsSize,
							y + useableHeight + i - yShift - 1,
							x + width + i - xShift,
							y - 1 + useableHeight + i - yShift);
				}
			}
		}

		// Draw background (rounded rectangles)

		// Checks if background has to be drawn
		_drawBackground = selected;
		_drawBackgroundColor = null;
		if (!_drawBackground && item.getBackground(true) != null) {
			_drawBackgroundColor = getBackground(item);

			if (!RendererHelper.isColorsEquals(_drawBackgroundColor,
					galleryBackgroundColor)) {
				_drawBackground = true;
			}
		}

		if (_drawBackground) {
			// Set colors
			if (selected) {
				gc.setBackground(selectionBackgroundColor);
				gc.setForeground(selectionBackgroundColor);
			} else if (_drawBackgroundColor != null) {
				gc.setBackground(_drawBackgroundColor);
			}

			// Draw
			if (showRoundedSelectionCorners) {
				gc.fillRoundRectangle(x, y, width, useableHeight,
						selectionRadius, selectionRadius);
			} else {
				gc.fillRectangle(x, y, width, height);
			}

			if (item.getText() != null && !EMPTY_STRING.equals(item.getText())
					&& showLabels) {
				gc.fillRoundRectangle(x, y + height - fontHeight, width,
						fontHeight, selectionRadius, selectionRadius);
			}
		}

		// Draw image
		if (_drawImage != null && size != null) {
			if (size.x > 0 && size.y > 0) {
				gc.drawImage(_drawImage, 0, 0, imageWidth, imageHeight,
						x + xShift, y + yShift, size.x, size.y);
				drawAllOverlays(gc, item, x, y, size, xShift, yShift);
			}

		}

		// Draw label
		if (item.getText() != null && !EMPTY_STRING.equals(item.getText())
				&& showLabels) {
			// Set colors
			if (selected) {
				// Selected : use selection colors.
				gc.setForeground(selectionForegroundColor);
				gc.setBackground(selectionBackgroundColor);
			} else {
				// Not selected, use item values or defaults.

				// Background
				if (_drawBackgroundColor != null) {
					gc.setBackground(_drawBackgroundColor);
				} else {
					gc.setBackground(backgroundColor);
				}

				// Foreground
				if (_drawForegroundColor != null) {
					gc.setForeground(_drawForegroundColor);
				} else {
					gc.setForeground(foregroundColor);
				}
			}

			// Create label
			String text = RendererHelper.createLabel(item.getText(), gc,
					width - 10);

			// Center text
			int textWidth = gc.textExtent(text).x;
			int textxShift = RendererHelper.getShift(width, textWidth);

			// Draw
			gc.drawText(text, x + textxShift, y + height - fontHeight, true);
		}
	}

	/**
	 * Get best-fit size for an image drawn in an area of maxWidth, maxHeight
	 * If the size of the image is smaller than calculated by RendererHelper.getBestSize() use the original image size
	 * This means that smaller images are not stretched to fit the Gallery Item.
	 * 
	 * @param originalWidth Original image width
	 * @param originalHeight Original image height
	 * @param maxWidth Max width
	 * @param maxHeight Max Height
	 * @return Best size
	 */
	protected Point getBestSize(int originalWidth, int originalHeight, int maxWidth, int maxHeight) {
	    Point size = RendererHelper.getBestSize(originalWidth, originalHeight, maxWidth, maxHeight);
        
        // Phillipus added these two lines...
        // If the size of the image is smaller than calculated by RendererHelper.getBestSize() use the original image size
        size.x = Math.min(size.x, originalWidth);
        size.y = Math.min(size.y, originalHeight);
        
        return size;
	}
}
