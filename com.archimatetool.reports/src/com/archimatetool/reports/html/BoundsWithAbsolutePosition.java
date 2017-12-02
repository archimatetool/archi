/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.reports.html;

import com.archimatetool.model.IBounds;

/**
 * Extension of Bounds
 * to explicitly access the absolute top-left and bottom right coordinates
 * of an element in a generated image
 * should behave exactly the same as a Bounds-Object in other contexts
 *
 * @author schlechter
 *
 */
public class BoundsWithAbsolutePosition {

    private int x, y, width, height;

	public BoundsWithAbsolutePosition(IBounds bounds, double scale) {
        x = (int)(bounds.getX() * scale);
        y = (int)(bounds.getY() * scale);
        width = (int)(bounds.getWidth() * scale);
        height = (int)(bounds.getHeight() * scale);
    }
	
    /**
     * Set x, y offset
     * @param offsetX
     * @param offsetY
     */
    public void setOffset(int offsetX, int offsetY) {
        x += offsetX;
        y += offsetY;
    }

	public int getX1() {
		return x;
	}

	public int getX2() {
		return getX1() + width;
	}

	public int getY1() {
		return y;
	}

	public int getY2() {
		return getY1() + height;
	}
}
