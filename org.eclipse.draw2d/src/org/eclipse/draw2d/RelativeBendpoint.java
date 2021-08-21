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
package org.eclipse.draw2d;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;

/**
 * RelativeBendpoint is a Bendpoint that calculates its location based on its
 * distance from the start and end points of the {@link Connection}, as well as
 * its weight. See {@link #setWeight(float)} for a description of what behavior
 * different weights will provide.
 */
public class RelativeBendpoint implements Bendpoint {

    private Connection connection;
    private float weight = 0.5f;
    private Dimension d1, d2;

    /**
     * Constructs a new RelativeBendpoint.
     * 
     * @since 2.0
     */
    public RelativeBendpoint() {
    }

    /**
     * Constructs a new RelativeBendpoint and associates it with the given
     * Connection.
     * 
     * @param conn
     *            The Connection this Bendpoint is associated with
     * @since 2.0
     */
    public RelativeBendpoint(Connection conn) {
        setConnection(conn);
    }

    /**
     * Returns the Connection this Bendpoint is associated with.
     * 
     * @return The Connection this Bendpoint is associated with
     * @since 2.0
     */
    protected Connection getConnection() {
        return connection;
    }

    /**
     * Calculates and returns this Bendpoint's new location.
     * 
     * @return This Bendpoint's new location
     * @since 2.0
     */
    @Override
    public Point getLocation() {
        PrecisionPoint a1 = new PrecisionPoint(getConnection()
                .getSourceAnchor().getReferencePoint());
        PrecisionPoint a2 = new PrecisionPoint(getConnection()
                .getTargetAnchor().getReferencePoint());

        getConnection().translateToRelative(a1);
        getConnection().translateToRelative(a2);

        return new PrecisionPoint(
                (a1.preciseX() + d1.preciseWidth()) * (1.0 - weight) + weight
                        * (a2.preciseX() + d2.preciseWidth()),
                (a1.preciseY() + d1.preciseHeight()) * (1.0 - weight) + weight
                        * (a2.preciseY() + d2.preciseHeight()));
    }

    /**
     * Sets the Connection this bendpoint should be associated with.
     * 
     * @param conn
     *            The Connection this bendpoint should be associated with
     * @since 2.0
     */
    public void setConnection(Connection conn) {
        connection = conn;
    }

    /**
     * Sets the Dimensions representing the X and Y distances this Bendpoint is
     * from the start and end points of the Connection. These Dimensions are
     * generally set once and are used in calculating the Bendpoint's location.
     * 
     * @param dim1
     *            The X and Y distances this Bendpoint is from the start of the
     *            Connection
     * @param dim2
     *            The X and Y distances this Bendpoint is from the end of the
     *            Connection
     * @since 2.0
     */
    public void setRelativeDimensions(Dimension dim1, Dimension dim2) {
        d1 = dim1;
        d2 = dim2;
    }

    /**
     * Sets the weight this Bendpoint should use to calculate its location. The
     * weight should be between 0.0 and 1.0. A weight of 0.0 will cause the
     * Bendpoint to follow the start point, while a weight of 1.0 will cause the
     * Bendpoint to follow the end point. A weight of 0.5 (the default) will
     * cause the Bendpoint to maintain its original aspect ratio between the
     * start and end points.
     * 
     * @param w
     *            The weight
     * @since 2.0
     */
    public void setWeight(float w) {
        weight = w;
    }

}
