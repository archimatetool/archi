/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.reports.html;

import org.eclipse.emf.ecore.util.EcoreUtil;

import com.archimatetool.model.IBounds;
import com.archimatetool.model.impl.Bounds;

/**
 * Extension of Bounds
 * to explicitly access the absolute top-left and bottom right coordinates
 * of an element in a generated image
 * should behave exactly the same as a Bounds-Object in other contexts
 *
 * @author schlechter
 *
 */
public class BoundsWithAbsolutePosition extends Bounds implements IBounds {

	private int offsetX;
	private int offsetY;


	public BoundsWithAbsolutePosition(IBounds b) {
		this.setLocation(b.getX(), b.getY());
		this.setSize(b.getWidth(), b.getHeight());
	}

	public void setOffset(int offsetX, int offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public int getX1() {
		return offsetX+getX();
	}

	public int getX2() {
		return getX1()+getWidth();
	}

	public int getY1() {
		return offsetY+getY();
	}

	public int getY2() {
		return getY1()+getHeight();
	}

	@Override
	public IBounds getCopy() {
		return EcoreUtil.copy(this);
	}
}
