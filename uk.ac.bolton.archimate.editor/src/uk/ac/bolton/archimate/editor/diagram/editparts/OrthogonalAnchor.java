/*******************************************************************************
 * Copyright (c) 2012 Jean-Baptiste Sarrodie, France.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.editparts;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import uk.ac.bolton.archimate.editor.diagram.editparts.AbstractConnectedEditPart;
import uk.ac.bolton.archimate.editor.diagram.figures.business.BusinessInterfaceFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.business.BusinessValueFigure;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.GraphicalEditPart;
import java.util.Iterator;

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
	private AbstractConnectedEditPart parentConnection;

	/**
	 * Constructs a new OrthogonalAnchor.
	 */
	protected OrthogonalAnchor() {
	}
	
	/**
	 * Constructs an OrthogonalAnchor with the given <i>parent</i> EditPart.
	 * 
	 * @param parent
	 *            The parent EditPart
	 */
	public OrthogonalAnchor(AbstractConnectedEditPart parent) {
		super(parent.getFigure());
		parentConnection = parent;
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
		@SuppressWarnings("rawtypes")
        Iterator connectionIt;
		IFigure remoteFig = null;
		Rectangle remoteFigBBox = new Rectangle();
		Rectangle figureBBox = new Rectangle();
		boolean inFigure = false;
		double anchorX;
		double anchorY;
		double cosPi4 = Math.cos(Math.PI / 4);
		
		// Anchor will be determined differently if the reference point is a BendPoint or a Figure
		// Check outgoing connections
		connectionIt = parentConnection.getSourceConnections().iterator();
		while (!inFigure && connectionIt.hasNext()) {
			remoteFig = ((GraphicalEditPart) ((ConnectionEditPart)connectionIt.next()).getTarget()).getFigure();
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
		// If needed, check incoming connections
		connectionIt = parentConnection.getTargetConnections().iterator();
		while (!inFigure && connectionIt.hasNext()) {
			remoteFig = ((GraphicalEditPart) ((ConnectionEditPart)connectionIt.next()).getSource()).getFigure();
			remoteFigBBox.setBounds(remoteFig.getBounds());
			remoteFig.translateToAbsolute(remoteFigBBox);
			// Same workaround...
			Rectangle remoteFigCenter = remoteFigBBox.getCopy();
			remoteFigCenter.shrink((remoteFigCenter.width-5)/2, (remoteFigCenter.height-5)/2);
			inFigure = remoteFigCenter.contains(reference);
		}
       
		// figureBBox contains the current figure bounding box
		figureBBox.setBounds(getBox());
		getOwner().translateToAbsolute(figureBBox);
		figureBBox = isEllipse(getOwner()) ? figureBBox.shrink(remoteFigBBox.width * (1-cosPi4)/2, figureBBox.height * (1-cosPi4)/2): figureBBox;
		
		if (inFigure) {
			// Figure to Figure case: compute a new reference point if it is contained in a figure.
			// remoteFigBBox contains the remote figure bounding box.
			// (in case of Ellipse, shrink it to get inner box)
			remoteFigBBox = isEllipse(remoteFig) ? remoteFigBBox.shrink(remoteFigBBox.width * (1-cosPi4)/2, remoteFigBBox.height * (1-cosPi4)/2): remoteFigBBox;

			anchorX = Math.round((Math.max(remoteFigBBox.x, figureBBox.x) + 
					Math.min(remoteFigBBox.x + remoteFigBBox.width, figureBBox.x + figureBBox.width)) / 2);
			anchorY = Math.round((Math.max(remoteFigBBox.y, figureBBox.y) + 
					Math.min(remoteFigBBox.y + remoteFigBBox.height, figureBBox.y + figureBBox.height)) / 2);
		} else {
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
		if (isEllipse(getOwner())) {
			// offsetX and offsetY will be use to get coordinates relative to ellipse center
			double offsetX = figureBBox.x + figureBBox.width/2.0;
			double offsetY = figureBBox.y + figureBBox.height/2.0;
			if (Math.abs(anchorX - offsetX) == figureBBox.width/2.0) {
				// Right or left ellipse side: have to fix anchorX
				anchorX =  (int) (offsetX + Math.signum(anchorX - offsetX) * Math.round(
					Math.cos(
						Math.asin(Math.abs(anchorY - offsetY) / ((figureBBox.height/2.0) / cosPi4))
					) * ((figureBBox.width/2.0) / cosPi4)
				));
			} else {
				// Top or bottom ellipse side: have to fix anchorY
				anchorY = (int) (offsetY + Math.signum(anchorY - offsetY) * Math.round(
						Math.sin(
							Math.acos(Math.abs(anchorX - offsetX) / ((figureBBox.width/2.0) / cosPi4))
						) * ((figureBBox.height/2.0) / cosPi4)
				));
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
		if (figure instanceof BusinessValueFigure) return true;
		
		if (figure instanceof BusinessInterfaceFigure) {
			return (((IDiagramModelArchimateObject)((BusinessInterfaceFigure)figure).getDiagramModelObject()).getType() != 0);
		}
			
		return false;
	}
}
