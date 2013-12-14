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

package uk.ac.bolton.archimate.editor.diagram.figures.connections.roundedbendpoint;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import uk.ac.bolton.archimate.editor.preferences.IPreferenceConstants;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author Jean-Baptiste Sarrodie (aka Jaiguru)
 * 
 * Base on a example found on http://www.eclipse.org/forums/index.php/t/33583/
 * fully rewritten for Archi to work in all cases (any angle).
 */
public class RoundedPolylineConnection extends PolylineConnection {
	// radius is the maximum radius length
	final double RADIUS = 12;
	// max_iter is the number of intermediate points to add
	final double MAX_ITER = 10;
	// some 
	final double SQRT2 = Math.sqrt(2.0);
	final double PI34 = Math.PI * 3.0 / 4.0;
	final double PI2 = Math.PI * 2.0;
	final double PI12 = Math.PI * 1.0 / 2.0;

	@Override
	public Rectangle getBounds() {
		boolean enabled = Preferences.STORE.getBoolean(IPreferenceConstants.USE_ROUNDED_CONNECTION);
		if (!enabled)
			return super.getBounds();
		else
			return super.getBounds().getCopy().expand(10, 10)	;
	}
	
	@Override
	protected void outlineShape(Graphics g) {
		boolean enabled = Preferences.STORE.getBoolean(IPreferenceConstants.USE_ROUNDED_CONNECTION);
		if (!enabled)
			super.outlineShape(g);
		else
			outlineShapeRounded(g);
	}	

	@SuppressWarnings("rawtypes")
	protected void outlineShapeRounded(Graphics g) {
		// ps contains original list of bendpoints
		PointList ps = getPoints();
		// ps_refined will contains list of bendpoints and points added to simulate an arc
		PointList ps_refined = new PointList();
				
		ArrayList connections = collectConnections();

		if (ps.size() == 0) {
			return;
		}
		
		// Start (bend)point
		Point src = ps.getPoint(0);
		//ps_refined.addPoint(src);
		
		for (int i = 1; i < ps.size(); i++) {
			// ps_refined will contains list of bendpoints and points added to simulate an arc
			//PointList ps_refined = new PointList();
			
			// Current bendpoint
			Point bp = ps.getPoint(i);

			// If last bendpoint, add it to the list and stop
			if (i == ps.size() - 1) {
				//ps_refined.addPoint(bp);
				drawLine(g, src, bp, connections, ps_refined);
				continue;
			}

			// target bendpoint
			Point tgt = ps.getPoint(i + 1);
			
			// Switch to polar coordinates
			PolarPoint src_p = PolarPoint.point2PolarPoint(bp, src);
			PolarPoint tgt_p = PolarPoint.point2PolarPoint(bp, tgt);
			
			// Calculate arc angle between source and target
			// and be sure that arc angle is positive and less than PI
			double arc = tgt_p.theta - src_p.theta;
			arc = (arc + PI2) % (PI2);
			// Do we have to go from source to target or the opposite
			boolean src2tgt = arc < Math.PI ? true : false;
			arc = src2tgt ? arc : PI2 - arc;
			
			// Check bendpoint radius against source and target
			double bp_radius = RADIUS;
			bp_radius = bp_radius * (2.0 - 2.0 * arc / Math.PI);
			bp_radius = Math.min(bp_radius, src_p.r / 2.0);
			bp_radius = Math.min(bp_radius, tgt_p.r / 2.0);
			
			// Compute source and target of bendpoint arc
			PolarPoint bpsrc_p = new PolarPoint(bp_radius, src_p.theta);
			PolarPoint bptgt_p = new PolarPoint(bp_radius, tgt_p.theta);
 
			// Switch back to rectangular coordinates
			Point bpsrc = bpsrc_p.toPoint().translate(bp);
			Point bptgt = bptgt_p.toPoint().translate(bp);
			
			drawLine(g, src, bpsrc, connections, ps_refined);
			
			// Create ellipse approximation
			// based on generic polar equation of circle with r=1 and center(Sqrt(2), PI/4)
			//ps_refined.addPoint(bpsrc);
			for (double a = 1; a < MAX_ITER; a++) {
				Point tmp;
				if (src2tgt)
					tmp = (new PolarPoint(bp_radius * get_r(PI12 * a/MAX_ITER), src_p.theta + arc * a/MAX_ITER)).toPoint().translate(bp);
				else
					tmp = (new PolarPoint(bp_radius * get_r(PI12 - PI12 * a/MAX_ITER), src_p.theta - arc * a/MAX_ITER)).toPoint().translate(bp);
				ps_refined.addPoint(tmp);
			}
			//ps_refined.addPoint(bptgt);
			
			// Prepare next iteration
			src = bptgt;
		}
		// Finally draw the polyLine
		g.drawPolyline(ps_refined);
	}

	private double get_r(double angle) {
		// return r value for defined angle (should be between 0 and PI/2)
		// based on generic polar equation of circle with r=1 and center(Sqrt(2), PI/4)
		return - SQRT2 * Math.cos(angle + PI34) - Math.sqrt(1.0 - 2.0 * Math.pow(Math.sin(angle + PI34), 2));
	}
	
	@SuppressWarnings({ "rawtypes" })
	private void drawLine(Graphics g, Point pp, Point p1, ArrayList connections, PointList ps_ref)
	{

		// ps contains list of jump points
		//PointList ps = new PointList();
		ArrayList<Point> ps = new ArrayList<Point>();
		//
		int radius = 5;
		
		PolarPoint line = PolarPoint.point2PolarPoint(pp, p1);	
		double line_angle = line.theta % Math.PI;
		boolean reverse = (line.theta != line_angle);
		
		
		for (Iterator I = connections.iterator(); I.hasNext();) {
			RoundedPolylineConnection conn = (RoundedPolylineConnection) I.next();
			PointList cps = conn.getPoints();
			
			for (int j = 0; j < cps.size() - 1; j++) {
				Point cp1 = cps.getPoint(j);
				Point cp2 = cps.getPoint(j + 1);
				Point tmp = lineIntersect(pp, p1, cp1, cp2);
				if (tmp != null) {
					PolarPoint con = PolarPoint.point2PolarPoint(cp1, cp2);
					double con_angle = con.theta % Math.PI;
					if (line_angle > con_angle)
						ps.add(tmp);
				}
			}
		}

		if (ps.size() == 0) {
			//g.drawLine(pp, p1);
			ps_ref.addPoint(pp);
			ps_ref.addPoint(p1);
			return;
		}
			
		//g.drawLine(pp, p1);
		ps.add(p1);
		ps.add(pp);
		
		Collections.sort(ps, new PointCompare());
		if (ps.get(0) == p1) Collections.reverse(ps);
		Point curr = ps.get(0);
		ps_ref.addPoint(curr);
		for (int i = 1; i < ps.size()-1; i++ ) {
			for (double a = 0; a <= MAX_ITER; a++) {
				if (reverse)
					ps_ref.addPoint((new PolarPoint(radius, line_angle - a*Math.PI/MAX_ITER)).toPoint().translate(ps.get(i)));
				else
					ps_ref.addPoint((new PolarPoint(radius, line_angle - Math.PI + a*Math.PI/MAX_ITER)).toPoint().translate(ps.get(i)));
			}
		}
		ps_ref.addPoint(ps.get(ps.size()-1));
		//g.drawLine(curr, ps.get(ps.size()-1));
	}

	@SuppressWarnings("rawtypes")
	private ArrayList collectConnections() {
		ArrayList result = new ArrayList();
		collectConnections(getRoot(), result);
		return result;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void collectConnections(IFigure figure, ArrayList list) {
		for (Iterator I = figure.getChildren().iterator(); I.hasNext();) {
			IFigure child = (IFigure) I.next();
			if (child == this)
				continue;
			collectConnections(child, list);

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
}
