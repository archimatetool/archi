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
	protected void outlineShape(Graphics g) {
		boolean enabled = Preferences.STORE.getBoolean(IPreferenceConstants.USE_ROUNDED_CONNECTION);
		if (!enabled)
			super.outlineShape(g);
		else
			outlineShapeRounded(g);
	}	

	protected void outlineShapeRounded(Graphics g) {
		// ps contains original list of bendpoints
		PointList ps = getPoints();
		// ps_refined will contains list of bendpoints and points added to simulate an arc
		PointList ps_refined = new PointList();

		if (ps.size() == 0) {
			return;
		}
		
		// Start (bend)point
		Point src = ps.getPoint(0);
		ps_refined.addPoint(src);
		
		for (int i = 1; i < ps.size(); i++) {
			// Current bendpoint
			Point bp = ps.getPoint(i);

			// If last bendpoint, add it to the list and stop
			if (i == ps.size() - 1) {
				ps_refined.addPoint(bp);
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
			
			// Create ellipse approximation
			// based on generic polar equation of circle with r=1 and center(Sqrt(2), PI/4)
			ps_refined.addPoint(bpsrc);
			for (double a = 1; a < MAX_ITER; a++) {
				Point tmp;
				if (src2tgt)
					tmp = (new PolarPoint(bp_radius * get_r(PI12 * a/MAX_ITER), src_p.theta + arc * a/MAX_ITER)).toPoint().translate(bp);
				else
					tmp = (new PolarPoint(bp_radius * get_r(PI12 - PI12 * a/MAX_ITER), src_p.theta - arc * a/MAX_ITER)).toPoint().translate(bp);
				ps_refined.addPoint(tmp);
			}
			ps_refined.addPoint(bptgt);
			
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
}
