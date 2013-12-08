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
public class RoundedPolylineConnection2 extends PolylineConnection {
	private double radius = 12;
	private double max_iter = 6;
	private double r2 = Math.sqrt(2.0);
	private double pi34 = 3.0 * Math.PI / 4.0;

	@Override
	protected void outlineShape(Graphics g) {
		boolean enabled = Preferences.STORE.getBoolean(IPreferenceConstants.USE_ROUNDED_CONNECTION);
		if (!enabled) {
			super.outlineShape(g);
			return;
		}
	
		PointList ps = getPoints();

		if (ps.size() == 0) {
			return;
		}
		
		// Start (bend)point
		Point src = ps.getPoint(0);
		
		for (int i = 1; i < ps.size(); i++) {
			// Current bendpoint
			Point bp = ps.getPoint(i);

			// If last bendpoint, draw connection
			if (i == ps.size() - 1) {
				g.drawLine(src, bp);
				continue;
			}

			// target bendpoint
			Point tgt = ps.getPoint(i + 1);
			
			// Switch to polar coordinates
			PolarPoint src_p = PolarPoint.point2PolarPoint(bp, src);
			PolarPoint tgt_p = PolarPoint.point2PolarPoint(bp, tgt);
			
			// Calculate arc angle between source and target
			// and be sure that arc angle is positive
			double arc = tgt_p.theta - src_p.theta;
			arc = (arc + 4.0*Math.PI) % (2.0*Math.PI);
			
			// Do we have to go from source to target or the opposite
			boolean src2tgt = arc < (Math.PI) ? true : false;
			arc = src2tgt ? arc : 2.0*Math.PI - arc;
			
			// Check dist against source and target
			double dist = radius;
			dist = dist * (2 - 2 * arc / Math.PI);
			dist = Math.min(dist, src_p.r / 2);
			dist = Math.min(dist, tgt_p.r / 2);
			
			// Compute source and target of bendpoint arc
			PolarPoint bpsrc_p = new PolarPoint((int) dist, src_p.theta);
			PolarPoint bptgt_p = new PolarPoint((int) dist, tgt_p.theta);
 
			// Switch back to rectangular coordinates
			Point bpsrc = bpsrc_p.toPoint().translate(bp);
			Point bptgt = bptgt_p.toPoint().translate(bp);
			
			// Draw line
			g.drawLine(src, bpsrc);
			
			// Create ellipse approximation
			PolarPoint s_p;
			Point s;
			Point t;
			if (src2tgt) {
				s_p = bpsrc_p;
				s = bpsrc;
				t = bptgt;
			} else {
				s_p = bptgt_p;
				s = bptgt;
				t = bpsrc;
			}
		
			Point p = s;
			for (double a = 1; a < max_iter; a++) {
				Point tmp = (new PolarPoint((int) (dist * get_r(Math.PI/2.0 * a/max_iter)), s_p.theta + arc * a/max_iter)).toPoint().translate(bp);
				g.drawLine(p, tmp);
				// Prepare next iteration
				p = tmp;
			}
			g.drawLine(p, t);
			// Prepare next iteration
			src = bptgt;
		}
	}

	private double get_r(double angle) {
		return (0 - r2 * Math.cos(angle + pi34) - Math.sqrt(1.0 - 2.0 * Math.pow(Math.sin(angle + pi34), 2)));
	}
}
