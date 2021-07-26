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

package com.archimatetool.editor.diagram.editparts;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import com.archimatetool.editor.diagram.figures.AbstractDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.figures.FigureUtils;
import com.archimatetool.editor.diagram.figures.IFigureDelegate;
import com.archimatetool.editor.diagram.figures.IRoundedRectangleFigure;
import com.archimatetool.editor.diagram.figures.elements.ValueFigure;


/**
 * The OrthogonalAnchor allows horizontal or vertical
 * connection whenever possible.
 * 
 * For this anchor to work, it has to be used for all
 * figures. Handles (rounded)rectangle and ellipse cases.
 * 
 * @author Jean-Baptiste Sarrodie (aka Jaiguru)
 */
public class OrthogonalAnchor extends ChopboxAnchor {
	// Some constants
	private static final int RECONREQ_SRC = 1;
	private static final int RECONREQ_TGT = 2;
	private static final int CRCONREQ_SRC = 3;
	private static final int CRCONREQ_TGT = 4;
	private static final int CONNECTION_SRC = 5;
	private static final int CONNECTION_TGT = 6;
	
	private static final int LEFT = 1;
	private static final int LEFT_CORNER = 2;
	private static final int MIDDLE = 4;
	private static final int RIGHT_CORNER = 8;
	private static final int RIGHT = 16;
	private static final int TOP = 32;
	private static final int TOP_CORNER = 64;
	private static final int CENTER = 128;
	private static final int BOTTOM_CORNER = 256;
	private static final int BOTTOM = 512;
	
	private static final double COSPI4 = Math.cos(Math.PI / 4);
	
    // If known, connection owning this anchor
    private ConnectionEditPart fAnchorConnection = null;
    // If known, Request linked to this anchor
    private Request fRequest = null;
    // Remote figure
    private IFigure fRemoteFig = null;
    // Remote figure during a ReconnectRequest 
    private IFigure fAlternateRemoteFig = null;
    // Type of anchor
    private int fAnchorType = 0;
    

	/**
	 * Constructs an OrthogonalAnchor with the given <i>owner</i> figure
	 * and the <i>connection</i> using it.
	 * 
	 * @param owner
	 *            The Owner figure
	 * @param connection
	 *            The connection
	 * @param isSource
	 *            <i>True</i> if this anchor is the connection's source
	 */
	public OrthogonalAnchor(IFigure owner, ConnectionEditPart connection, boolean isSource) {
		super(owner);
		fAnchorConnection = connection;
		fAnchorType = isSource ? CONNECTION_SRC : CONNECTION_TGT;
	}

	/**
	 * Constructs an OrthogonalAnchor with the given <i>owner</i> figure
	 * and the <i>request</i> using it.
	 * 
	 * @param owner
	 *            The Owner figure
	 * @param request
	 *            The connection's request
	 * @param isSource
	 *            <i>True</i> if this anchor is the connection's source
	 */
	public OrthogonalAnchor(IFigure owner, Request request, boolean isSource) {
		super(owner);
		fRequest = request;
		if(request instanceof CreateConnectionRequest) {
			fAnchorType = isSource ? CRCONREQ_SRC : CRCONREQ_TGT;
		}
		else if(request instanceof ReconnectRequest) {
			fAnchorType = isSource ? RECONREQ_SRC : RECONREQ_TGT;
		}
	}

	/**
	 * Define the figure used during a ReconnectRequest.
	 * 
	 * @param figure
	 *            The remote figure
	 */
	public void setAlternateRemoteFig(IFigure figure) {
		fAlternateRemoteFig = figure;
	}
	
	/**
	 * Rectangle figure case: Gets a rectangle from {@link #getBox()}
	 * and return the Point on this rectangle which makes a vertical or
	 * horizontal connection possible.
	 * <p>
	 * Ellipse figure case: Gets an inner rectangle from {@link #getBox()}
	 * and a bit of trigonometry, find the Point on this rectangle which
	 * makes a vertical or horizontal connection possible, and adjust it
	 * to be on the ellipse border.
	 * <p>
	 * If the reference point is inside another figure, adjust it to allow
	 * alignment on the middle of the overlapping section.
	 * 
	 * @param reference
	 *            The reference point
	 * @return The anchor location
	 */
	@Override
	public Point getLocation(Point reference) {
		IFigure remoteFig = null;
		Rectangle remoteFigBBox = new Rectangle();
		Rectangle figureBBox = new Rectangle();
		boolean inFigure = false;
		
		// figureBBox contains the current figure bounding box
		figureBBox.setBounds(getBox());
		getOwner().translateToAbsolute(figureBBox);
		
		// Anchor will be determined differently if the reference point is a BendPoint or a Figure
		// Gets remote figure and check if it contains the reference point
		updateRemoteFig();
					
		if(fRemoteFig != null) {
			remoteFig = fRemoteFig;
			// Some debug information to understand what happens
			// System.out.println(getOwner().getClass().getSimpleName() + " / remoteFig: " + remoteFig.getClass().getSimpleName());
			remoteFigBBox.setBounds(remoteFig.getBounds());
			remoteFig.translateToAbsolute(remoteFigBBox);
			// I don't know why, but when zooming out a simple
			// "inFigure = remoteFigBBox.getCenter().equals(reference);"
			// doesn't work. Workaround: check if reference is contained
			// within a 5x5 rectangle in the center of the figure.
			Rectangle remoteFigCenter = remoteFigBBox.getCopy();
			remoteFigCenter.shrink((remoteFigCenter.width-5)/2, (remoteFigCenter.height-5)/2);
			inFigure = remoteFigCenter.contains(reference);
		}
		
		if(!inFigure && fAlternateRemoteFig != null) {
			remoteFig = fAlternateRemoteFig;
			// Some debug information to understand what happens
			// System.out.println(getOwner().getClass().getSimpleName() + " / fAlternateRemoteFig: " + remoteFig.getClass().getSimpleName());
			remoteFigBBox.setBounds(remoteFig.getBounds());
			remoteFig.translateToAbsolute(remoteFigBBox);
			// I don't know why, but when zooming out a simple
			// "inFigure = remoteFigBBox.getCenter().equals(reference);"
			// doesn't work. Workaround: check if reference is contained
			// within a 5x5 rectangle in the center of the figure.
			Rectangle remoteFigCenter = remoteFigBBox.getCopy();
			remoteFigCenter.shrink((remoteFigCenter.width-5)/2, (remoteFigCenter.height-5)/2);
			inFigure = remoteFigCenter.contains(reference);
		}
       
		if(inFigure) {
			// Figure to Figure case: compute a new reference point if it is contained in a figure.
			reference.x = (Math.max(remoteFigBBox.x, figureBBox.x) + 
					Math.min(remoteFigBBox.x + remoteFigBBox.width, figureBBox.x + figureBBox.width)) / 2;
			reference.y = (Math.max(remoteFigBBox.y, figureBBox.y) + 
					Math.min(remoteFigBBox.y + remoteFigBBox.height, figureBBox.y + figureBBox.height)) / 2;
		}
		
		// Find reference point relative position
		int pos = 0;
		
		Dimension corner = getCornerDimensions(getOwner());
		corner = corner.scale(FigureUtils.getFigureScale(getOwner()));
		
		// Check X axis
		if (reference.x < figureBBox.x)
			pos = LEFT;
		else if (reference.x < figureBBox.x + corner.width / 2)
			pos = LEFT_CORNER;
		else if (reference.x < figureBBox.x + figureBBox.width - corner.width / 2)
			pos = MIDDLE;
		else if (reference.x < figureBBox.x + figureBBox.width)
			pos = RIGHT_CORNER;
		else
			pos = RIGHT;
		// Check Y axis
		if (reference.y < figureBBox.y)
			pos |= TOP;
		else if (reference.y < figureBBox.y + corner.height / 2)
			pos |= TOP_CORNER;
		else if (reference.y < figureBBox.y + figureBBox.height - corner.height / 2)
			pos |= CENTER;
		else if (reference.y < figureBBox.y + figureBBox.height)
			pos |= BOTTOM_CORNER;
		else
			pos |= BOTTOM;
		
		// Now compute anchor's position
		// But first resize figureBBox, this will avoid anchors 1px outside from right and bottom borders
		figureBBox.resize(-1, -1);
		switch (pos) {
		case LEFT | TOP:
			return (new Point(figureBBox.x + corner.width / 2 -
					(int) (COSPI4 * (corner.width / 2.0)),
					figureBBox.y + corner.height / 2 -
					(int) (COSPI4 * (corner.height / 2.0))
					));
		case LEFT_CORNER | TOP:
			return (new Point(reference.x, figureBBox.y + corner.height / 2 -
					(int) (Math.sin(Math.acos((figureBBox.x + corner.width / 2.0 - reference.x) / (corner.width / 2.0))) * (corner.height / 2.0))
					));
		case MIDDLE | TOP:
			return (new Point(reference.x, figureBBox.y));
		case RIGHT_CORNER | TOP:
			return (new Point(reference.x, figureBBox.y + corner.height / 2 -
					(int) (Math.sin(Math.acos((figureBBox.x + figureBBox.width - corner.width / 2.0 - reference.x) / (corner.width / 2.0))) * (corner.height / 2.0))
					));
		case RIGHT | TOP:
			return (new Point(figureBBox.x + figureBBox.width - corner.width / 2 +
					(int) (COSPI4 * (corner.width / 2.0)),
					figureBBox.y + corner.height / 2 -
					(int) (COSPI4 * (corner.height / 2.0))
					));
		case LEFT | TOP_CORNER:
			return (new Point(figureBBox.x + corner.width / 2 -
					(int) (Math.cos(Math.asin((figureBBox.y + corner.height / 2.0 - reference.y) / (corner.height / 2.0))) * (corner.width / 2.0)),
					reference.y));
		case RIGHT | TOP_CORNER:
			return (new Point(figureBBox.x + figureBBox.width - corner.width / 2 +
					(int) (Math.cos(Math.asin((figureBBox.y + corner.height / 2.0 - reference.y) / (corner.height / 2.0))) * (corner.width / 2.0)),
					reference.y));
		case LEFT | CENTER:
			return (new Point(figureBBox.x, reference.y));
		case RIGHT | CENTER:
			return (new Point(figureBBox.x + figureBBox.width, reference.y));
		case LEFT | BOTTOM_CORNER:
			return (new Point(figureBBox.x + corner.width / 2 -
					(int) (Math.cos(Math.asin((figureBBox.y + figureBBox.height - corner.height / 2.0 - reference.y) / (corner.height / 2.0))) * (corner.width / 2.0)),
					reference.y));
		case RIGHT | BOTTOM_CORNER:
			return (new Point(figureBBox.x + figureBBox.width - corner.width / 2 +
					(int) (Math.cos(Math.asin((figureBBox.y + figureBBox.height - corner.height / 2.0 - reference.y) / (corner.height / 2.0))) * (corner.width / 2.0)),
					reference.y));
		case LEFT | BOTTOM:
			return (new Point(figureBBox.x + corner.width / 2 -
					(int) (COSPI4 * (corner.width / 2.0)),
					figureBBox.y + figureBBox.height - corner.height / 2 +
					(int) (COSPI4 * (corner.height / 2.0))
					));
		case LEFT_CORNER | BOTTOM:
			return (new Point(reference.x, figureBBox.y + figureBBox.height - corner.height / 2 +
					(int) (Math.sin(Math.acos((figureBBox.x + corner.width / 2.0 - reference.x) / (corner.width / 2.0))) * (corner.height / 2.0))
					));
		case MIDDLE | BOTTOM:
			return (new Point(reference.x, figureBBox.y + figureBBox.height));
		case RIGHT_CORNER | BOTTOM:
			return (new Point(reference.x, figureBBox.y + figureBBox.height - corner.height / 2 +
					(int) (Math.sin(Math.acos((figureBBox.x + figureBBox.width - corner.width / 2.0 - reference.x) / (corner.width / 2.0))) * (corner.height / 2.0))
					));
		case RIGHT | BOTTOM:
			return (new Point(figureBBox.x + figureBBox.width - corner.width / 2 +
					(int) (COSPI4 * (corner.width / 2.0)),
					figureBBox.y + figureBBox.height - corner.height / 2 +
					(int) (COSPI4 * (corner.height / 2.0))
					));
		default:
			return figureBBox.getCenter();
		}
	}
	
	/**
	 * Gets the remote figure. 
	 */
	private void updateRemoteFig() {
		// CreateConnectionRequest needs to refresh fRemoteFig each time
        switch(fAnchorType) {
            case CRCONREQ_SRC:
                fRemoteFig = (((CreateConnectionRequest)fRequest).getTargetEditPart() != null)
                        ? ((GraphicalEditPart)((CreateConnectionRequest)fRequest).getTargetEditPart()).getFigure()
                        : null;
                break;
            case CRCONREQ_TGT:
                fRemoteFig = (((CreateConnectionRequest)fRequest).getSourceEditPart() != null)
                        ? ((GraphicalEditPart)((CreateConnectionRequest)fRequest).getSourceEditPart()).getFigure()
                        : null;
                break;
        }
		
		// The other cases can be cached
		if(fRemoteFig != null) {
		    return;
		}
		
        switch(fAnchorType) {
            case CONNECTION_SRC:
            	fRemoteFig = (fAnchorConnection != null && fAnchorConnection.getTarget() != null)
            			? ((GraphicalEditPart)fAnchorConnection.getTarget()).getFigure()
            			: null;
                break;
            case CONNECTION_TGT:
                fRemoteFig = (fAnchorConnection != null && fAnchorConnection.getSource() != null)
                		? ((GraphicalEditPart)fAnchorConnection.getSource()).getFigure()
                		: null;
                break;
            case RECONREQ_SRC:
                fRemoteFig = (((ReconnectRequest)fRequest).getConnectionEditPart().getTarget() != null)
                        ? ((GraphicalEditPart)((ReconnectRequest)fRequest).getConnectionEditPart().getTarget()).getFigure()
                        : null;
                if(((Connection)((ReconnectRequest)fRequest).getConnectionEditPart().getFigure()).getTargetAnchor() instanceof OrthogonalAnchor)
                    ((OrthogonalAnchor)((Connection)((ReconnectRequest)fRequest).getConnectionEditPart().getFigure()).getTargetAnchor())
                            .setAlternateRemoteFig(getOwner());
                break;
            case RECONREQ_TGT:
                fRemoteFig = (((ReconnectRequest)fRequest).getConnectionEditPart().getSource() != null)
                        ? ((GraphicalEditPart)((ReconnectRequest)fRequest).getConnectionEditPart().getSource()).getFigure()
                        : null;
                if(((Connection)((ReconnectRequest)fRequest).getConnectionEditPart().getFigure()).getSourceAnchor() instanceof OrthogonalAnchor)
                    ((OrthogonalAnchor)((Connection)((ReconnectRequest)fRequest).getConnectionEditPart().getFigure()).getSourceAnchor())
                            .setAlternateRemoteFig(getOwner());
                break;
        }
    }
	
	/**
	 * Checks anchor's owner and find its corner dimensions.
	 * this is possible for roundedRectangles but also
	 * rectangle (null corner) and ellipse (full corner).
	 * 
	 * @return corner dimension
	 */
	private Dimension getCornerDimensions(IFigure figure) {
        // Default is pure rectangle
        Dimension corner = new Dimension(0, 0);
        
        // roundedRectangle case
        if(figure instanceof AbstractDiagramModelObjectFigure) {
            IFigureDelegate figureDelegate = ((AbstractDiagramModelObjectFigure)figure).getFigureDelegate();
            if(figureDelegate instanceof IRoundedRectangleFigure) {
                return ((IRoundedRectangleFigure)figureDelegate).getArc();
            }
        }
        
        if(figure instanceof IRoundedRectangleFigure) {
            // roundedRectangle case
            corner = ((IRoundedRectangleFigure)figure).getArc();
        }
        else if(figure instanceof RoundedRectangle) {
            // roundedRectangle case
            corner = ((RoundedRectangle)figure).getCornerDimensions();
        }
        else if(figure instanceof ValueFigure) {
            // ellipse case
            corner = figure.getSize();
        }
		
		return corner;
	}
}
