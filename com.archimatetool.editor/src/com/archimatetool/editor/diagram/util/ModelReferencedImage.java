/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.util;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

/**
 * Image with bounds information
 * 
 * @author schlechter
 */
public class ModelReferencedImage {

	private final Image image;
	private final Rectangle bounds;

	public ModelReferencedImage(Image image, Rectangle bounds) {
		this.image = image;
		this.bounds = bounds;
	}

	public Image getImage() {
		return image;
	}

	public Rectangle getBounds() {
		return bounds;
	}

}
