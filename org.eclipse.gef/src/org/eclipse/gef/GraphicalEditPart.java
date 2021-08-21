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
package org.eclipse.gef;

import java.util.List;

import org.eclipse.draw2d.IFigure;

/**
 * A Specialization of {@link EditPart} for use with {@link GraphicalViewer
 * GraphicalViewers}. The <i>visual part</i> of a GraphicalEditPart is a
 * {@link org.eclipse.draw2d.IFigure Figure}.
 * <p>
 * IMPORTANT: This interface is not intended to be implemented by clients.
 * Clients should inherit from
 * {@link org.eclipse.gef.editparts.AbstractGraphicalEditPart}. New methods may
 * be added in the future.
 */
@SuppressWarnings("rawtypes")
public interface GraphicalEditPart extends EditPart {

    /**
     * Adds a NodeListener to the EditPart. Duplicate calls result in duplicate
     * notification.
     * 
     * @param listener
     *            the Listener
     */
    void addNodeListener(NodeListener listener);

    /**
     * Returns the primary Figure representing this GraphicalEditPart. The
     * parent will add this Figure to its <i>content pane</i>. The Figure may be
     * a composition of several Figures.
     * 
     * @return this EditPart's Figure
     */
    IFigure getFigure();

    /**
     * Returns the <i>source</i> connections for this GraphicalEditPart. This
     * method should only be called by the EditPart itself, and its helpers such
     * as EditPolicies.
     * 
     * @return the source connections
     */
    List getSourceConnections();

    /**
     * Returns the <i>target</i> connections for this GraphicalEditPart. This
     * method should only be called by the EditPart itself, and its helpers such
     * as EditPolicies.
     * 
     * @return the target connections
     */
    List getTargetConnections();

    /**
     * The Figure into which childrens' Figures will be added. May return the
     * same Figure as {@link #getFigure()}. The GraphicalEditPart's
     * {@link #getFigure() primary Figure} may be composed of multiple figures.
     * This is the figure in that composition that will contain children's
     * figures.
     * 
     * @return the <i>content pane</i> Figure
     */
    IFigure getContentPane();

    /**
     * Removes the first occurance of the specified listener from the list of
     * listeners. Does nothing if the listener was not present.
     * 
     * @param listener
     *            the listener being removed
     */
    void removeNodeListener(NodeListener listener);

    /**
     * Sets the specified constraint for a child's Figure on the
     * {@link #getContentPane() content pane} figure for this GraphicalEditPart.
     * The constraint will be applied to the content pane's
     * {@link org.eclipse.draw2d.LayoutManager}. <code>revalidate()</code> is
     * called on the content pane, which will cause it to layout during the next
     * update.
     * 
     * @param child
     *            the <i>child</i> GraphicalEditPart whose constraint is being
     *            set
     * @param figure
     *            the Figure whose constraint is being set
     * @param constraint
     *            the constraint for the draw2d
     *            {@link org.eclipse.draw2d.LayoutManager}
     */
    void setLayoutConstraint(EditPart child, IFigure figure, Object constraint);

}
