package uk.ac.bolton.archimate.editor.diagram.figures.connections.roundedbendpoint;

import java.util.Comparator;
import org.eclipse.draw2d.geometry.Point;

public class PointCompare implements Comparator<Point> {
	@Override
	public int compare(final Point a, final Point b) {
	    if (a.x < b.x) {
	        return -1;
	    }
	    else if (a.x > b.x) {
	        return 1;
	    }
	    else if (a.y < b.y) {
	        return -1;
	    }
	    else if (a.y > b.y) {
	        return 1;
	    } else
	    	return 0;
	}
}
