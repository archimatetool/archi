package com.archimatetool.editor.diagram.policies.snaptogrid;

/**
 * snap-to-grid patch by Jean-Baptiste Sarrodie (aka Jaiguru)
 * 
 * This class has been extended to allow overriding
 * of classes ExtendedBendpointCreationHandle and ExtendedBendpointMoveHandle
 */

/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
//package org.eclipse.gef.editpolicies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.AutomaticRouter;
import org.eclipse.draw2d.Bendpoint;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy;
// snap-to-grid patch
// Use alternate BendpointCreationHandle and BendpointMoveHandle
//import org.eclipse.gef.handles.BendpointCreationHandle;
//import org.eclipse.gef.handles.BendpointMoveHandle;

/**
 * Used to add bendpoint handles on a {@link ConnectionEditPart}.
 * <P>
 * BendpointEditPolicy will automatically observe the
 * {@link org.eclipse.draw2d.Connection} figure. If the number of bends in the
 * <code>Connection</code> changes, the handles will be updated.
 */
@SuppressWarnings("all")
public abstract class ExtendedBendpointEditPolicy extends BendpointEditPolicy {

    private static final List NULL_CONSTRAINT = new ArrayList();

	private List createHandlesForAutomaticBendpoints() {
		List list = new ArrayList();
		ConnectionEditPart connEP = (ConnectionEditPart) getHost();
		PointList points = getConnection().getPoints();
		for (int i = 0; i < points.size() - 1; i++)
			list.add(new ExtendedBendpointCreationHandle(connEP, 0, i));

		return list;
	}

	private List createHandlesForUserBendpoints() {
		List list = new ArrayList();
		ConnectionEditPart connEP = (ConnectionEditPart) getHost();
		PointList points = getConnection().getPoints();
		List bendPoints = (List) getConnection().getRoutingConstraint();
		int bendPointIndex = 0;
		Point currBendPoint = null;

		if (bendPoints == null)
			bendPoints = NULL_CONSTRAINT;
		else if (!bendPoints.isEmpty())
			currBendPoint = ((Bendpoint) bendPoints.get(0)).getLocation();

		for (int i = 0; i < points.size() - 1; i++) {
			// Put a create handle on the middle of every segment
			list.add(new ExtendedBendpointCreationHandle(connEP, bendPointIndex, i));

			// If the current user bendpoint matches a bend location, show a
			// move handle
			if (i < points.size() - 1 && bendPointIndex < bendPoints.size()
					&& currBendPoint.equals(points.getPoint(i + 1))) {
				list.add(new ExtendedBendpointMoveHandle(connEP, bendPointIndex, i + 1));

				// Go to the next user bendpoint
				bendPointIndex++;
				if (bendPointIndex < bendPoints.size())
					currBendPoint = ((Bendpoint) bendPoints.get(bendPointIndex))
							.getLocation();
			}
		}

		return list;
	}

	/**
	 * Creates selection handles for the bendpoints. Explicit (user-defined)
	 * bendpoints will have {@link ExtendedBendpointMoveHandle}s on them with a single
	 * {@link ExtendedBendpointCreationHandle} between 2 consecutive explicit
	 * bendpoints. If implicit bendpoints (such as those created by the
	 * {@link AutomaticRouter}) are used, one {@link ExtendedBendpointCreationHandle} is
	 * placed in the middle of the Connection.
	 * 
	 * @see SelectionHandlesEditPolicy#createSelectionHandles()
	 */
	protected List createSelectionHandles() {
		List list = new ArrayList();
		if (isAutomaticallyBending())
			list = createHandlesForAutomaticBendpoints();
		else
			list = createHandlesForUserBendpoints();
		return list;
	}

	private boolean isAutomaticallyBending() {
		List constraint = (List) getConnection().getRoutingConstraint();
		PointList points = getConnection().getPoints();
		return ((points.size() > 2) && (constraint == null || constraint
				.isEmpty()));
	}
}
