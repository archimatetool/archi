package com.archimatetool.editor.diagram.figures;

/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
import org.eclipse.draw2d.geometry.Point;

/**
 * A polar point in a standard polar coordinates system.
 * Adapted by Jaiguru to use double instead of int for r.
 * 
 * @author Xihui Chen
 *
 */
public class PolarPoint {
	
	/**
	 * The radial coordinate 
	 */
	public double r;
	
	/**
	 * The angular coordinate in radians
	 */
	public double theta;	
	
	/**
	 * @param r The radial coordinate 
	 * @param theta The angular coordinate in radians
	 */
	public PolarPoint(double r, double theta) {
		this.r = r;
		this.theta = theta;
	}
	
	/**
	 * convert a point to polar point
	 * @param pole the pole of the polar coordinate system.
	 * @param point the point to be converted
	 */
	public PolarPoint(Point pole, Point point) {
		int x = point.x - pole.x;
		int y = point.y - pole.y;
		
		this.r = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		
		this.theta = Math.acos(x/r);
		if(y > 0)
			this.theta = 2*Math.PI - this.theta;
	}
	
	/**
	 * Transform the polar point to the {@link Point} in rectangular coordinates. 
	 * The rectangular coordinates has the same origin as the polar coordinates.
	 * @return the point in rectangular coordinates
	 */
	public Point toPoint() {
		int x = (int) Math.round(r * Math.cos(theta));
		int y = (int) Math.round(-r * Math.sin(theta));
		return new Point(x, y);		
	}	
	
	/**
	 * Transform the polar point to the {@link Point} in the absolute coordinate system.
	 * @param pole the pole of the polar coordinate system.
	 * @return the point in absolute coordinate system.
	 */
	public Point toAbsolutePoint(Point pole) {
		return toPoint().getTranslated(pole);
	}
}