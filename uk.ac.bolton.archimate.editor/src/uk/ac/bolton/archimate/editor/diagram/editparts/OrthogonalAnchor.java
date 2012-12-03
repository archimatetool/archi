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

package uk.ac.bolton.archimate.editor.diagram.editparts;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import uk.ac.bolton.archimate.editor.diagram.figures.business.BusinessInterfaceFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.business.BusinessValueFigure;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;


/**
 * The OrthogonalAnchor allows horizontal or vertical
 * connection whenever possible.
 * 
 * For this anchor to work, it has to be used for all
 * figures (handle rectangle and ellipse cases).
 * 
 * @author Jean-Baptiste Sarrodie (aka Jaiguru)
 */
public class OrthogonalAnchor extends ChopboxAnchor {
	// Some constants
	final int RECONREQ_SRC = 1;
	final int RECONREQ_TGT = 2;
	final int CRCONREQ_SRC = 3;
	final int CRCONREQ_TGT = 4;
	final int CONNECTION_SRC = 5;
	final int CONNECTION_TGT = 6;
	final double COSPI4 = Math.cos(Math.PI / 4);
	
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
		double anchorX;
		double anchorY;
		
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
       
		// figureBBox contains the current figure bounding box
		figureBBox.setBounds(getBox());
		getOwner().translateToAbsolute(figureBBox);
		figureBBox = isEllipse(getOwner()) ? figureBBox.shrink(figureBBox.width * (1-COSPI4)/2, figureBBox.height * (1-COSPI4)/2): figureBBox;
		
		if(inFigure) {
			// Figure to Figure case: compute a new reference point if it is contained in a figure.
			// remoteFigBBox contains the remote figure bounding box.
			// (in case of Ellipse, shrink it to get inner box)
			remoteFigBBox = isEllipse(remoteFig) ? remoteFigBBox.shrink(remoteFigBBox.width * (1-COSPI4)/2, remoteFigBBox.height * (1-COSPI4)/2): remoteFigBBox;

			anchorX = (Math.max(remoteFigBBox.x, figureBBox.x) + 
					Math.min(remoteFigBBox.x + remoteFigBBox.width, figureBBox.x + figureBBox.width)) / 2;
			anchorY = (Math.max(remoteFigBBox.y, figureBBox.y) + 
					Math.min(remoteFigBBox.y + remoteFigBBox.height, figureBBox.y + figureBBox.height)) / 2;
		}
		else {
			// Figure to BendPoint case: copy reference point in the (to be computed) anchor
			anchorX = reference.x;
			anchorY = reference.y;
		}
		
		// Just a little bit of Math...
		anchorX = Math.max(anchorX, figureBBox.x);
		anchorX = Math.min(anchorX, figureBBox.x + figureBBox.width);
		anchorY = Math.max(anchorY, figureBBox.y);
		anchorY = Math.min(anchorY, figureBBox.y + figureBBox.height);
		
		// ...and a bit of Trigonometry for the Ellipse Case
		if(isEllipse(getOwner())) {
			// offsetX and offsetY will be use to get coordinates relative to ellipse center
			double offsetX = figureBBox.x + figureBBox.width/2.0;
			double offsetY = figureBBox.y + figureBBox.height/2.0;
			if(Math.abs(anchorX - offsetX) == figureBBox.width/2.0) {
				// Right or left ellipse side: have to fix anchorX
				anchorX =  (int) (offsetX + Math.signum(anchorX - offsetX) *
					Math.cos(
						Math.asin((anchorY - offsetY) / ((figureBBox.height/2.0) / COSPI4))
					) * ((figureBBox.width/2.0) / COSPI4)
				);
			} else {
				// Top or bottom ellipse side: have to fix anchorY
				anchorY = (int) (offsetY + Math.signum(anchorY - offsetY) *
						Math.sin(
							Math.acos((anchorX - offsetX) / ((figureBBox.width/2.0) / COSPI4))
						) * ((figureBBox.height/2.0) / COSPI4)
				);
			}
		}
		
		// That's it
		return new Point((int)anchorX, (int)anchorY);
	}
	
	/**
	 * Checks if a figure is an ellipse. Have to be updated
	 * each time a new case is introduced in Archi.
	 * 
	 * @param figure
	 *            a figure
	 * @return true if figure is an ellipse
	 */
    private boolean isEllipse(IFigure figure) {
        if(figure instanceof BusinessValueFigure) {
            return true;
        }

        if(figure instanceof BusinessInterfaceFigure) {
            return (((IDiagramModelArchimateObject)((BusinessInterfaceFigure)figure).getDiagramModelObject()).getType() != 0);
        }

        return false;
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
                fRemoteFig = ((GraphicalEditPart)fAnchorConnection.getTarget()).getFigure();
                break;
            case CONNECTION_TGT:
                fRemoteFig = ((GraphicalEditPart)fAnchorConnection.getSource()).getFigure();
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
}
