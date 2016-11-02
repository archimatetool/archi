package com.archimatetool.editor.diagram.util;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

public class ModelReferencedImage {

	private final Image image;
	private final Rectangle offset;

	public ModelReferencedImage(Image image, Rectangle offset) {
		this.image = image;
		this.offset = offset;
	}

	public Image getImage() {
		return image;
	}

	public Rectangle getOffset() {
		return offset;
	}

}
