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
import java.util.List;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.Graphics;
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
	private static final double CURVE_MAX_RADIUS = 14.0;

	// Radius from line-jumps
	private static final double JUMP_MAX_RADIUS = 5.0;

	// Number of intermediate points for circle and ellipse approximation
	private static final double MAX_ITER = 6.0;

	// Constants
	private static final double PI2 = Math.PI * 2.0;

	@Override
	public Rectangle getBounds() {
		return ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.USE_LINE_JUMPS) ?
		        super.getBounds().getCopy().expand(10, 10) : super.getBounds();
	}
	
	@Override
	protected void outlineShape(Graphics g) {
		// Original list of figure points - start, end, and bendpoints
		PointList points = getPoints();
        if(points.size() < 2) { // shouldn't happen
            return;
        }
        
		// List of bendpoints and points added to draw line-curves and line-jumps
		PointList linepoints = new PointList();
		
		// All connection figures on connection layer except this one
		List<Connection> connections = getOtherConnections();

		// Start point is the first "previous" point
		Point prev = points.getPoint(0);
		
		// Main loop: check all points and add curve as needed 
		for(int i = 1; i < points.size(); i++) {
			// Current bendpoint
			Point bp = points.getPoint(i);

			// If last point, define points for line segment and then draw polyline
			if(i == points.size() - 1) {
				addSegment(prev, bp, connections, linepoints);
				continue;
			}

			// Next bendpoint
			Point next = points.getPoint(i + 1);
			
			// If line-curves are enabled draw bendpoints using ellipse approximation
			if(ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.USE_LINE_CURVES)) {
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
				if(!prev2next) {
					bpprev = PolarPoint.SINGLETON.set(arc_radius, start_angle).toAbsolutePoint(center);
					bpnext = PolarPoint.SINGLETON.set(arc_radius, start_angle + full_angle).toAbsolutePoint(center);
				}
				else {
					bpprev = PolarPoint.SINGLETON.set(arc_radius, start_angle + full_angle).toAbsolutePoint(center);
					bpnext = PolarPoint.SINGLETON.set(arc_radius, start_angle).toAbsolutePoint(center);
				}
				
				// Now that bendpoint position has been refined we can add line segment
				addSegment(prev, bpprev, connections, linepoints);
				
				// Create circle approximation
				for(double a = 1; a < MAX_ITER; a++) {
					if(prev2next) {
					    linepoints.addPoint(PolarPoint.SINGLETON.set(arc_radius, start_angle + full_angle * (1 - a / MAX_ITER)).toAbsolutePoint(center));
					}
					else {
						linepoints.addPoint(PolarPoint.SINGLETON.set(arc_radius, start_angle + full_angle * a / MAX_ITER).toAbsolutePoint(center));
					}
				}
				
				// Prepare next iteration
				prev = bpnext;
			}
			else {
				// Add line segment
				addSegment(prev, bp, connections, linepoints);
				// Prepare next iteration
				prev = bp;
			}
		}
		
		// Finally draw the polyLine
		g.drawPolyline(linepoints);
	}
	
	private void addSegment(Point start, Point end, List<Connection> connections, PointList linepoints) {
		// List of crossing points
		List<Point> crosspoints = new ArrayList<>();
		
		// Add start point to the list
		linepoints.addPoint(start);
		
		// If line-jumps are enabled, draw them using half circles
		if(ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.USE_LINE_JUMPS)) {
			// Compute angle between line segment and horizontal line
			PolarPoint end_p = PolarPoint.SINGLETON.set(start, end);
			double angle = end_p.theta % Math.PI;
			boolean reverse = (end_p.theta != angle);
			
			// For each other connection, check if a crossing point exist.
			// If yes, add it to the list
			for(Connection conn : connections) {
				PointList bendpoints = conn.getPoints();
				
				// Iterate on connection segments
				for(int j = 0; j < bendpoints.size() - 1; j++) {
					Point bp = bendpoints.getPoint(j);
					Point next = bendpoints.getPoint(j + 1);
					Point crosspoint = lineIntersect(start, end, bp, next);
					// Check if crossing point found and not too close from ends
					if (crosspoint != null
					        && PolarPoint.SINGLETON.set(crosspoint, start).r > JUMP_MAX_RADIUS
					        && PolarPoint.SINGLETON.set(crosspoint, end).r > JUMP_MAX_RADIUS
					        && PolarPoint.SINGLETON.set(crosspoint, bp).r > JUMP_MAX_RADIUS
					        && PolarPoint.SINGLETON.set(crosspoint, next).r > JUMP_MAX_RADIUS) {
						double con_angle = PolarPoint.SINGLETON.set(bp, next).theta % Math.PI;

						if(angle > con_angle && !crosspoints.contains(crosspoint)) {
							crosspoints.add(crosspoint);
						}
					}
				}
			}
	
			// If crossing points found, render them using a half circle
			if(crosspoints.size() != 0) {
				// Sort crosspoints from start to end
				crosspoints.add(start);
				crosspoints.sort(PointCompare);

				if(crosspoints.get(0) != start) {
				    Collections.reverse(crosspoints);
				}
				
				// Do not add start point to the list a second time, so start at i=1
				for(int i = 1; i < crosspoints.size(); i++ ) {
					for(double a = 0; a <= MAX_ITER; a++) {
						if(reverse) {
							linepoints.addPoint(PolarPoint.SINGLETON.set(JUMP_MAX_RADIUS, angle - a * Math.PI / MAX_ITER).toAbsolutePoint(crosspoints.get(i)));
						}
						else {
							linepoints.addPoint(PolarPoint.SINGLETON.set(JUMP_MAX_RADIUS, angle - Math.PI + a * Math.PI / MAX_ITER).toAbsolutePoint(crosspoints.get(i)));
						}
					}
				}
			}
		}
		
		// Add end point to the list
		linepoints.addPoint(end);
	}

	/**
	 * @return all connections on the connection layer except this one
	 */
    private List<Connection> getOtherConnections() {
	    if(getParent() instanceof ConnectionLayer layer) {
	        return layer.getChildren().stream()
                                      .filter(child -> child != this)
                                      .filter(Connection.class::isInstance)
                                      .map(Connection.class::cast)
                                      .toList();
	    }
	    
	    return List.of();
	}
	
	/**
	 * Calculation of intersection between line segments
	 * Based on this thread:
	 * http://stackoverflow.com/questions/16314069/calculation-of-intersections-between-line-segments 
	 */
	private static Point lineIntersect(Point p1, Point p2, Point p3, Point p4) {
		double denom = (p4.y - p3.y) * (p2.x - p1.x) - (p4.x - p3.x) * (p2.y - p1.y);
		if(denom == 0.0) { // Lines are parallel.
			return null;
		}

		double ua = ((p4.x - p3.x) * (p1.y - p3.y) - (p4.y - p3.y) * (p1.x - p3.x)) / denom;
		double ub = ((p2.x - p1.x) * (p1.y - p3.y) - (p2.y - p1.y) * (p1.x - p3.x)) / denom;

		if(ua >= 0.0f && ua <= 1.0f && ub >= 0.0f && ub <= 1.0f) {
			// Get the intersection point
			Point c = new Point((int) (p1.x + ua * (p2.x - p1.x)), (int) (p1.y + ua * (p2.y - p1.y)));
			Rectangle r1 = new Rectangle(p1, p2);
			Rectangle r2 = new Rectangle(p3, p4);

			if(r1.contains(c) && r2.contains(c)
					&& !c.equals(p1.x, p1.y)
					&& !c.equals(p2.x, p2.y)
					&& !c.equals(p3.x, p3.y)
					&& !c.equals(p4.x, p4.y)) {
				return c;
			}
		}

		return null;
	}
	
	/**
	 * Allow points comparison
	 * Originally based on this thread (works only for aligned points) :
	 * http://stackoverflow.com/questions/4199509/java-how-to-sort-an-arraylist-of-point-objects
	 * Changed in Nov. 2017 to work for loosely aligned points.
	 * Modernised in May 2026.
	 */
	private static final Comparator<Point> PointCompare = (p1, p2) -> {
	    int delta_x = p1.x - p2.x;
	    int delta_y = p1.y - p2.y;
	    
	    return Math.abs(delta_x) > Math.abs(delta_y)
	            ? Integer.signum(delta_x)
	            : Integer.signum(delta_y);
	};
}
