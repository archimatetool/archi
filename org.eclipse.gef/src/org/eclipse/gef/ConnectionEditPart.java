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

/**
 * A specialization of {@link GraphicalEditPart GraphicalEditPart} for
 * representing connections. ConnectionEditParts must join a <I>source</I> and
 * <I>target</I> EditPart. Its Figure is typically a line between two "nodes",
 * with possible decorations on that line.
 * <P>
 * In GEF, ConnectionEditParts are <EM><I>structural features</I></EM> of their
 * source and target "nodes", which are EditParts. However, the model does not
 * have this requirement. The application may store the connection model in any
 * way, or there may even be no real model. The burden is on the source and
 * target EditPart to obtain their appropriate connections in the methods
 * {@link org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections()
 * getModelSourceConnections()} and
 * {@link org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections()
 * getModelTargetConnections()}. How this is done is application specific.
 * <P>
 * Since ConnectionEditParts are features of their node EditPart, it is those
 * EditParts that must create and manage the connection. Creation is performed
 * by whichever end happens to "intialize" itself first. Therefore an end always
 * looks first in the
 * {@link org.eclipse.gef.EditPartViewer#getEditPartRegistry() EditPartRegistry}
 * to see if the connection was already created by the other end.
 * <P>
 * ConnectionEditParts are EditParts, and therefore can have children. This is a
 * common way to implement labels and other selectable decorations on
 * connections. Similarly, a ConnectionEditPart can also be a "node", meaning it
 * can serve as the source or target of some other ConnectionEditPart. This
 * makes connection to connection possible.
 * <P>
 * IMPORTANT: The need to display something as a line does not automatically
 * mean that a ConnectionEditPart is required. There are several situations in
 * which ConnectionEditParts should not be used. You should use
 * ConnectionEditParts in general if:
 * <UL>
 * <LI>The connection should be selectable by the user independant of its
 * "nodes".
 * <LI>The connection can be deleted, leaving the source and target intact.
 * <LI>The connection cannot exist without a source and target. A instance of
 * when this is <B>not</B> true is <I>assocations</I>. Associations are
 * top-level object that are children of the diagram. They are probably only
 * valid if they have a source and target, but many applications allow you to
 * create things in any order.
 * </UL>
 */
public interface ConnectionEditPart extends GraphicalEditPart {

    /**
     * @return the EditPart at the <i>source</i> end of this connection.
     */
    EditPart getSource();

    /**
     * @return the EditPart at the <i>target</i> end of this connection.
     */
    EditPart getTarget();

    /**
     * Sets the <i>source</i> of this connection.
     * 
     * @param source
     *            the source of this connection
     */
    void setSource(EditPart source);

    /**
     * Sets the<i>target</i> of this connection.
     * 
     * @param target
     *            the target of this connection
     */
    void setTarget(EditPart target);
}
