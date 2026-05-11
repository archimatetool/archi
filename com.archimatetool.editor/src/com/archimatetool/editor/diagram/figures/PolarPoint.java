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
     * A singleton for use in short calculations.
     * Use to avoid creating new unnecessary objects.
     */
    public static final PolarPoint SINGLETON = new PolarPoint(0, 0);

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
	    set(r, theta);
	}
	
	/**
	 * convert a point to polar point
	 * @param pole the pole of the polar coordinate system.
	 * @param point the point to be converted
	 */
	public PolarPoint(Point pole, Point point) {
		set(pole, point);
	}
	
	/**
	 * Set values and convert a point to polar point
	 * @param pole the pole of the polar coordinate system.
	 * @param point the point to be converted
	 * @return this PolarPoint
	 */
	public PolarPoint set(Point pole, Point point) {
	    int x = point.x - pole.x;
        int y = point.y - pole.y;
        
        r = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        
        theta = Math.acos(x / r);
        
        if(y > 0) {
            theta = 2 * Math.PI - theta;
        }
        
        return this;
	}
	
    /**
     * Set values
     * @param r The radial coordinate 
     * @param theta The angular coordinate in radians
     * @return this PolarPoint
     */
    public PolarPoint set(double r, double theta) {
        this.r = r;
        this.theta = theta;
        return this;
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