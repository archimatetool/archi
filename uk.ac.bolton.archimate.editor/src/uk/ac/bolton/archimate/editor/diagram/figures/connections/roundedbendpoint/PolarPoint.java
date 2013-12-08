package uk.ac.bolton.archimate.editor.diagram.figures.connections.roundedbendpoint;

/*******************************************************************************
 * Copyright (c) 2010 Oak Ridge National Laboratory.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

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
	
	
	
	
//	@Override
//	public boolean equals(Object obj) {
//		if(obj instanceof PolarPoint) {
//			PolarPoint p = (PolarPoint)obj;
//			return p.r == r && p.theta == theta;
//		}
//		return false;
//	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (int) (prime * result + r);
		long temp;
		temp = Double.doubleToLongBits(theta);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PolarPoint other = (PolarPoint) obj;
		if (r != other.r)
			return false;
		if (Double.doubleToLongBits(theta) != Double
				.doubleToLongBits(other.theta))
			return false;
		return true;
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
	 * It is assumed that the origin of the polar coordinate system is the central point of 
	 * the rectangle. 
	 * @param rect the paint area of the figure 
	 * @return the point in absolute coordinate system.
	 */
	public Point toAbsolutePoint(Rectangle rect) {
		Point p = this.toPoint();
		return p.translate(rect.width/2, rect.height/2).translate(
				rect.x, rect.y);
	}
	
	/**Transform the polar point to the {@link Point} in the relative coordinate system, 
	 * whose origin is (rect.x, rect.y). 
	 * It is assumed that the origin of the polar coordinate system is the central point of 
	 * the rectangle. 
	 * @param rect the paint area of the figure
	 * @return the point in relative coordinate system.
	 */
	public Point toRelativePoint(Rectangle rect) {
		Point p = this.toPoint();
		return p.translate(rect.width/2, rect.height/2);		
	}
	
	
	/**
	 * convert a point to polar point
	 * @param pole the pole of the polar coordinate system.
	 * @param point the point to be converted
	 * @return the corresponding polar point.
	 */
	public static PolarPoint point2PolarPoint(Point pole, Point point) {
		int x = point.x - pole.x;
		int y = point.y - pole.y;
		
		double r = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		
		double theta = Math.acos(x/r);
		if(y >0)
			theta = 2*Math.PI - theta;
		return new PolarPoint(r, theta);
	}
	
	/**rotate the x axis of the polar coordinate system to the axisDirection
	 * @param axisDirection the direction of the new axis
	 * @param inRadians true if the axisDirection is in radians, false if in degrees.
	 */
	public void rotateAxis(double axisDirection, boolean inRadians) {
		if(!inRadians)
			axisDirection = axisDirection * Math.PI/180.0;
		theta -= axisDirection;
		if(theta < 0) 
			theta += 2*Math.PI;
	}
	
	@Override
	public String toString() {
		return "(" + r + ", " + theta * 180.0/Math.PI + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}
}