/**
  Copyright (c) 2012 Jean-Baptiste Sarrodie, France.

  Permission is hereby granted, free of charge, to any person
  obtaining a copy of this software and associated documentation
  files (the "Software"), to deal in the Software without
  restriction, including without limitation the rights to use,
  copy, modify, merge, publish, distribute, sublicense, and/or sell
  copies of the Software, and to permit persons to whom the
  Software is furnished to do so, subject to the following
  conditions:

  The above copyright notice and this permission notice shall be
  included in all copies or substantial portions of the Software.

  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
  EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
  OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
  NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
  HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
  WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
  OTHER DEALINGS IN THE SOFTWARE.
*/

package com.archimatetool.editor.diagram.figures.connections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.figures.PolarPoint;
import com.archimatetool.editor.preferences.IPreferenceConstants;

/**
 * Implementation of a connection which can draw curved bendpoints
 * and jumps at crossing points. Base on a example found on
 * http://www.eclipse.org/forums/index.php/t/33583/
 * Fully rewritten for Archi to work in all cases (any angle)
 * and use properties.
 * 
 * @author Jean-Baptiste Sarrodie (aka Jaiguru)
 */
public class RoundedPolylineConnection extends PolylineConnection {
	// Maximum radius length from line-curves
	final double CURVE_MAX_RADIUS = 14.0;
	// Radius from line-jumps
	final double JUMP_MAX_RADIUS = 5.0;
	// Number of intermediate points for circle and ellipse approximation
	final double MAX_ITER = 6.0;
	// Constants
	final double SQRT2 = Math.sqrt(2.0);
	final double PI34 = Math.PI * 3.0 / 4.0;
	final double PI2 = Math.PI * 2.0;
	final double PI12 = Math.PI * 1.0 / 2.0;

	@Override
	public Rectangle getBounds() {
		if (ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.USE_LINE_JUMPS))
			return super.getBounds().getCopy().expand(10, 10);
		else
			return super.getBounds();
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	protected void outlineShape(Graphics g) {
		// Original list of bendpoints
		PointList bendpoints = getPoints();
		// List of bendpoints and points added to draw line-curves and line-jumps
		PointList linepoints = new PointList();
		// List of all connections on current diagram
		ArrayList connections = getAllConnections();

		if (bendpoints.size() == 0) {
			return;
		}
		
		// Start point is the first "previous" point
		Point prev = bendpoints.getPoint(0);
		
		// Main loop: check all bendpoints and add curve is needed 
		for (int i = 1; i < bendpoints.size(); i++) {
			// Current bendpoint
			Point bp = bendpoints.getPoint(i);

			// If last bendpoint, define points for line segment
			// and then draw polyline
			if (i == bendpoints.size() - 1) {
				addSegment(g, prev, bp, connections, linepoints);
				continue;
			}

			// Next bendpoint
			Point next = bendpoints.getPoint(i + 1);
			
			// If line-curves are enabled draw bendpoints using ellipse approximation
			if(ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.USE_LINE_CURVES)) {
				// Switch to polar coordinates
				PolarPoint prev_p = new PolarPoint(bp, prev);
				PolarPoint next_p = new PolarPoint(bp, next);
				
				// Compute arc angle between source and target
				// and be sure that arc angle is positive and less than PI
				double arc = next_p.theta - prev_p.theta;
				arc = (arc + PI2) % (PI2);
				
				// Do we have to go from previous to next or the opposite
				boolean prev2next = arc < Math.PI ? true : false;
				arc = prev2next ? arc : PI2 - arc;
				
				// Check bendpoint radius against source and target
				double bp_radius = CURVE_MAX_RADIUS;
				bp_radius = Math.min(bp_radius, prev_p.r / 2.0);
				bp_radius = Math.min(bp_radius, next_p.r / 2.0);
				
				// Find center of bendpoint arc
				PolarPoint center_p = new PolarPoint(bp_radius, (prev2next ? prev_p.theta : next_p.theta) + arc / 2.0);
				Point center = center_p.toAbsolutePoint(bp);
				
				// Compute source and target of bendpoint arc
				double arc_radius = bp_radius * Math.sin(arc / 2.0);
				double start_angle = (Math.PI + arc) / 2.0 + center_p.theta;
				double full_angle = Math.PI - arc;

				Point bpprev;
				Point bpnext;
				if (!prev2next) {
					bpprev = new PolarPoint(arc_radius, start_angle).toAbsolutePoint(center);
					bpnext = new PolarPoint(arc_radius, start_angle + full_angle).toAbsolutePoint(center);
				} else {
					bpprev = new PolarPoint(arc_radius, start_angle + full_angle).toAbsolutePoint(center);
					bpnext = new PolarPoint(arc_radius, start_angle).toAbsolutePoint(center);
				}
				
				// Now that bendpoint position has been refined we can add line segment
				addSegment(g, prev, bpprev, connections, linepoints);
				
				// Create circle approximation
				for (double a = 1; a < MAX_ITER; a++) {
					if (prev2next)
						linepoints.addPoint(new PolarPoint(arc_radius, start_angle + full_angle * (1 - a/ MAX_ITER)).toAbsolutePoint(center));
					else
						linepoints.addPoint(new PolarPoint(arc_radius, start_angle + full_angle * a / MAX_ITER).toAbsolutePoint(center));
				}
				
				// Prepare next iteration
				prev = bpnext;
			} else {
				// Add line segment
				addSegment(g, prev, bp, connections, linepoints);
				// Prepare next iteration
				prev = bp;
			}
		}
		
		// Finally draw the polyLine
		g.drawPolyline(linepoints);
	}
	
	@SuppressWarnings({ "rawtypes" })
	private void addSegment(Graphics g, Point start, Point end, ArrayList connections, PointList linepoints){
		// List of crossing points
		ArrayList<Point> crosspoints = new ArrayList<Point>();
		//
		int radius = (int) JUMP_MAX_RADIUS;
		
		// Add start point to the list
		linepoints.addPoint(start);
		
		// If line-jumps are enabled, draw them using half circles
		if (ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.USE_LINE_JUMPS)) {
			// Compute angle between line segment and horizontal line
			PolarPoint end_p = new PolarPoint(start, end);
			double angle = end_p.theta % Math.PI;
			boolean reverse = (end_p.theta != angle);
			
			// For each other connection, check if a crossing point exist.
			// If yes, add it to the list
			for (Iterator I = connections.iterator(); I.hasNext();) {
				RoundedPolylineConnection conn = (RoundedPolylineConnection) I.next();
				PointList bendpoints = conn.getPoints();
				
				// Iterate on connection segments
				for (int j = 0; j < bendpoints.size() - 1; j++) {
					Point bp = bendpoints.getPoint(j);
					Point next = bendpoints.getPoint(j + 1);
					Point crosspoint = lineIntersect(start, end, bp, next);
					// Check if crossing point found and not too close from ends
					if (crosspoint != null
						&& (new PolarPoint(crosspoint, start)).r > JUMP_MAX_RADIUS
						&& (new PolarPoint(crosspoint, end)).r > JUMP_MAX_RADIUS
						&& (new PolarPoint(crosspoint, bp)).r > JUMP_MAX_RADIUS
						&& (new PolarPoint(crosspoint, next)).r > JUMP_MAX_RADIUS) {
						double con_angle = ((new PolarPoint(bp, next)).theta % Math.PI);
						if (angle > con_angle && !crosspoints.contains(crosspoint))
							crosspoints.add(crosspoint);
					}
				}
			}
	
			// If crossing points found, render them using a half circle
			if (crosspoints.size() != 0) {
				// Sort crosspoints from start to end
				crosspoints.add(start);
				Collections.sort(crosspoints, new PointCompare());
				if (crosspoints.get(0) != start) Collections.reverse(crosspoints);
				// Do not add start point to the list a second time, so start at i=1
				for (int i = 1; i < crosspoints.size(); i++ ) {
					for (double a = 0; a <= MAX_ITER; a++) {
						if (reverse)
							linepoints.addPoint((new PolarPoint(radius, angle - a*Math.PI/MAX_ITER)).toAbsolutePoint(crosspoints.get(i)));
						else
							linepoints.addPoint((new PolarPoint(radius, angle - Math.PI + a*Math.PI/MAX_ITER)).toAbsolutePoint(crosspoints.get(i)));
					}
				}
			}
		}
		
		// Add end point to the list
		linepoints.addPoint(end);
	}

	@SuppressWarnings("rawtypes")
	private ArrayList getAllConnections() {
		ArrayList result = new ArrayList();
		getAllConnections(getRoot(), result);
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getAllConnections(IFigure figure, ArrayList list) {
		for (Iterator I = figure.getChildren().iterator(); I.hasNext();) {
			IFigure child = (IFigure) I.next();
			if (child == this)
				continue;
			getAllConnections(child, list);

			if (!(child instanceof RoundedPolylineConnection))
				continue;
			list.add(child);
		}
	}

	private IFigure getRoot() {
		IFigure figure = this;
		while (figure.getParent() != null)
			figure = figure.getParent();
		return figure;
	}
	
	/**
	 * Calculation of intersection between line segments
	 * Based on this thread:
	 * http://stackoverflow.com/questions/16314069/calculation-of-intersections-between-line-segments 
	 */
	private static Point lineIntersect(Point p1, Point p2, Point p3, Point p4) {
		double denom = (p4.y - p3.y) * (p2.x - p1.x) - (p4.x - p3.x) * (p2.y - p1.y);
		if (denom == 0.0) { // Lines are parallel.
			return null;
		}
		double ua = ((p4.x - p3.x) * (p1.y - p3.y) - (p4.y - p3.y) * (p1.x - p3.x))/denom;
		double ub = ((p2.x - p1.x) * (p1.y - p3.y) - (p2.y - p1.y) * (p1.x - p3.x))/denom;
		if (ua >= 0.0f && ua <= 1.0f && ub >= 0.0f && ub <= 1.0f) {
			// Get the intersection point.
			Point c = new Point((int) (p1.x + ua*(p2.x - p1.x)), (int) (p1.y + ua*(p2.y - p1.y)));
			Rectangle r1 = new Rectangle(p1, p2);
			Rectangle r2 = new Rectangle(p3, p4);
			if (r1.contains(c) && r2.contains(c)
					&& !c.equals(p1.x, p1.y)
					&& !c.equals(p2.x, p2.y)
					&& !c.equals(p3.x, p3.y)
					&& !c.equals(p4.x, p4.y))
				return c;
			else
				return null;
		}

		return null;
	}
	
	/**
	 * Allow points comparison
	 * Originally based on this thread (works only for aligned points) :
	 * http://stackoverflow.com/questions/4199509/java-how-to-sort-an-arraylist-of-point-objects
	 * Changed in Nov. 2017 to work for loosely aligned points
	 */
	private static class PointCompare implements Comparator<Point> {
		@Override
		public int compare(final Point a, final Point b) {
			int delta_x = a.x - b.x;
			int delta_y = a.y - b.y;
			if (Math.abs(delta_x) > Math.abs(delta_y)) {
				return (int) Math.signum(delta_x);
			} else {
				return (int) Math.signum(delta_y);
			}
		}
	}
}
