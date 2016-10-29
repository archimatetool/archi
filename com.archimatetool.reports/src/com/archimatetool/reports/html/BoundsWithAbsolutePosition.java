package com.archimatetool.reports.html;

import com.archimatetool.model.IBounds;
import com.archimatetool.model.impl.Bounds;

/**
 * Extension of Bounds
 * to explicitly access the absolute top-left and bottom right coordinates
 * of an element in a generated image
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
		BoundsWithAbsolutePosition b = new BoundsWithAbsolutePosition(this);
		b.setOffset(offsetX, offsetY);
		return b;
	}
}
