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
package org.eclipse.gef.editpolicies;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.XYAnchor;
import org.eclipse.draw2d.geometry.Point;

/**
 * Helps display connection feedback during drags of the connection ends. This
 * class is used internally by the provided EditPolicy implementation.
 * 
 * @author hudsonr
 */
public class FeedbackHelper {

    private Connection connection;
    private XYAnchor dummyAnchor;
    private boolean isMovingStartAnchor = false;

    /**
     * Constructs a new FeedbackHelper.
     */
    public FeedbackHelper() {
        dummyAnchor = new XYAnchor(new Point(10, 10));
    }

    /**
     * Returns the connection being used to show feedback.
     * 
     * @return the connection
     */
    protected Connection getConnection() {
        return connection;
    }

    /**
     * Returns true is the feedback is moving the source anchor
     * 
     * @return <code>true</code> if moving start
     */
    protected boolean isMovingStartAnchor() {
        return isMovingStartAnchor;
    }

    /**
     * Sets the connection.
     * 
     * @param c
     *            connection
     */
    public void setConnection(Connection c) {
        connection = c;
    }

    /**
     * Sets if moving start of connection.
     * 
     * @param value
     *            <code>true</code> if the starts is being moved
     */
    public void setMovingStartAnchor(boolean value) {
        isMovingStartAnchor = value;
    }

    /**
     * Sets the anchor for the end being moved.
     * 
     * @param anchor
     *            the new anchor
     */
    protected void setAnchor(ConnectionAnchor anchor) {
        if (isMovingStartAnchor())
            getConnection().setSourceAnchor(anchor);
        else
            getConnection().setTargetAnchor(anchor);
    }

    /**
     * Updates the feedback based on the given anchor or point. The anchor is
     * used unless <code>null</code>. The point is used when there is no anchor.
     * 
     * @param anchor
     *            <code>null</code> or an anchor
     * @param p
     *            the point to use when there is no anchor
     */
    public void update(ConnectionAnchor anchor, Point p) {
        if (anchor != null)
            setAnchor(anchor);
        else {
            dummyAnchor.setLocation(p);
            setAnchor(dummyAnchor);
        }
    }

}
