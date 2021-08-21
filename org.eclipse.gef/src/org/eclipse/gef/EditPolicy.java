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

import org.eclipse.gef.commands.Command;

/**
 * A pluggable contribution implementing a portion of an EditPart's behavior.
 * EditPolicies contribute to the overall <i>editing behavior</i> of an
 * EditPart. Editing behavior is defined as one or more of the following:
 * <ul>
 * <li><b>Command Creation </b>- Returning a <code>Command</code> in response to
 * {@link #getCommand(Request)}
 * <li><b>Feedback Management</b> - Showing/erasing source and/or target
 * feedback in response to Requests.
 * <li><b>Delegation/Forwarding</b> - Collecting contributions from other
 * EditParts (and therefore their EditPolicies). In response to a given
 * <code>Request</code>, an EditPolicy may create a derived Request and forward
 * it to other EditParts. For example, during the deletion of a composite
 * EditPart, that composite may consult its children for contributions to the
 * delete command. Then, if the children have any additional work to do, they
 * will return additional comands to be executed.
 * </ul>
 * <P>
 * EditPolicies should determine an EditPart's editing capabilities. It is
 * possible to implement an EditPart such that it handles all editing
 * responsibility. However, it is much more flexible and object-oriented to use
 * EditPolicies. Using policies, you can pick and choose the editing behavior
 * for an EditPart without being bound to its class hierarchy. Code reuse is
 * increased, and code management is easier.
 * <p>
 * IMPORTANT: This interface is <EM>not</EM> intended to be implemented by
 * clients. Clients should inherit from
 * {@link org.eclipse.gef.editpolicies.AbstractEditPolicy}. New methods may be
 * added in the future.
 */

public interface EditPolicy {

    /**
     * The key used to install a <i>component</i> EditPolicy. A <i>component</i>
     * is defined as anything in the model. This EditPolicy should handle the
     * fundamental operations that do not fit under any other EditPolicy role.
     * For example, delete is a fundamental operation. Generally the component
     * EditPolicy knows only about the model, and can be used in any type of
     * EditPartViewer.
     */
    String COMPONENT_ROLE = "ComponentEditPolicy"; //$NON-NLS-1$

    /**
     * The key used to install a <i>connection endpoint</i> EditPolicy. A
     * <i>connection endpoint</i> EditPolicy is usually a
     * {@link org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy} subclass.
     * Besides rendering selection by displaying <code>Handle</code>s at then
     * ends of the connection, the EditPolicy also understands how to move the
     * endpoints of the connection. If the endpoints are moveable, the
     * EditPolicy will show feedback and provide <code>Commands</code> to
     * perform the move.
     */
    String CONNECTION_ENDPOINTS_ROLE = "Connection Endpoint Policy"; //$NON-NLS-1$

    /**
     * The key used to install a <i>bendpoint</i> EditPolicy. A <i>bendpoint</i>
     * EditPolicy is an optional EditPolicy for connections that are visibile.
     * As with {@link #CONNECTION_ENDPOINTS_ROLE endpoints}, bendpoint
     * EditPolicies are porbably
     * {@link org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy}.
     */
    String CONNECTION_BENDPOINTS_ROLE = "Connection Bendpoint Policy"; //$NON-NLS-1$

    /**
     * The key used to install a <i>connection</i> EditPolicy. The behavior of a
     * <code>ConnectionEditPart</code> may be implemented in its
     * <i>component</i> EditPolicy,
     */
    String CONNECTION_ROLE = "ConnectionEditPolicy"; //$NON-NLS-1$

    /**
     * The key used to install a <i>container</i> EditPolicy.
     */
    String CONTAINER_ROLE = "ContainerEditPolicy"; //$NON-NLS-1$

    /**
     * The key used to install a <i>direct edit</i> EditPolicy.
     */
    String DIRECT_EDIT_ROLE = "DirectEditPolicy"; //$NON-NLS-1$

    /**
     * The key used to install a <i>graphical node</i> EditPolicy.
     */
    String GRAPHICAL_NODE_ROLE = "GraphicalNodeEditPolicy"; //$NON-NLS-1$

    /**
     * The key used to install a <i>layout</i> EditPolicy.
     */
    String LAYOUT_ROLE = "LayoutEditPolicy"; //$NON-NLS-1$

    /**
     * The key used to install a <i>node</i> EditPolicy.
     */
    String NODE_ROLE = "NodeEditPolicy"; //$NON-NLS-1$

    /**
     * The key used to install a <i>primary drag</i> EditPolicy.
     */
    String PRIMARY_DRAG_ROLE = "PrimaryDrag Policy"; //$NON-NLS-1$

    /**
     * The key used to install a <i>selection feedback</i> EditPolicy.
     */
    String SELECTION_FEEDBACK_ROLE = "Selection Feedback"; //$NON-NLS-1$

    /**
     * The key used to install a <i>tree container</i> EditPolicy.
     */
    String TREE_CONTAINER_ROLE = "TreeContainerEditPolicy"; //$NON-NLS-1$

    /**
     * Activates this EditPolicy. The EditPolicy might need to hook listeners.
     * These listeners should be unhooked in <code>deactivate()</code>. The
     * EditPolicy might also contribute feedback/visuals immediately, such as
     * <i>selection handles</i> if the EditPart was selected at the time of
     * activation.
     * <P>
     * Activate is called after the <i>host</i> has been set, and that host has
     * been activated.
     * 
     * @see EditPart#activate()
     * @see #deactivate()
     * @see EditPart#installEditPolicy(Object, EditPolicy)
     */
    void activate();

    /**
     * Deactivates the EditPolicy, the inverse of {@link #activate()}.
     * Deactivate is called when the <i>host</i> is deactivated, or when the
     * EditPolicy is uninstalled from an active host. Deactivate unhooks any
     * listeners, and removes all feedback.
     * 
     * @see EditPart#deactivate()
     * @see #activate()
     * @see EditPart#removeEditPolicy(Object)
     */
    void deactivate();

    /**
     * Erases source feedback based on the given <code>Request</code>. Does
     * nothing if the EditPolicy does not apply to the given Request.
     * <P>
     * This method is declared on {@link EditPart#eraseSourceFeedback(Request)
     * EditPart}, and is redeclared here so that EditPart can delegate its
     * implementation to each of its EditPolicies.
     * 
     * @param request
     *            the Request
     */
    void eraseSourceFeedback(Request request);

    /**
     * Erases target feedback based on the given <code>Request</code>. Does
     * nothing if the EditPolicy does not apply to the given Request.
     * <P>
     * This method is declared on {@link EditPart#eraseTargetFeedback(Request)
     * EditPart}, and is redeclared here so that EditPart can delegate its
     * implementation to each of its EditPolicies.
     * 
     * @param request
     *            the Request
     * */
    void eraseTargetFeedback(Request request);

    /**
     * Returns the <code>Command</code> contribution for the given
     * <code>Request</code>, or <code>null</code>. <code>null</code> is treated
     * as a no-op by the caller, or an empty contribution. The EditPolicy must
     * return an {@link org.eclipse.gef.commands.UnexecutableCommand} if it
     * wishes to disallow the Request.
     * <P>
     * This method is declared on {@link EditPart#getCommand(Request) EditPart},
     * and is redeclared here so that EditPart can delegate its implementation
     * to each of its EditPolicies. The EditPart will combine each EditPolicy's
     * contribution into a {@link org.eclipse.gef.commands.CompoundCommand}.
     * 
     * @param request
     *            the Request
     * @return <code>null</code> or a Command contribution
     */
    Command getCommand(Request request);

    /**
     * @return the <i>host</i> EditPart on which this policy is installed.
     */
    EditPart getHost();

    /**
     * Returns <code>null</code> or the appropriate <code>EditPart</code> for
     * the specified <code>Request</code>. In general, this EditPolicy will
     * return its <i>host</i> EditPart if it understands the Request. Otherwise,
     * it will return <code>null</code>.
     * <P>
     * This method is declared on {@link EditPart#getTargetEditPart(Request)
     * EditPart}, and is redeclared here so that EditPart can delegate its
     * implementation to each of its EditPolicies. The first non-
     * <code>null</code> result returned by an EditPolicy is returned by the
     * EditPart.
     * 
     * @param request
     *            the Request
     * @return <code>null</code> or the appropriate target <code>EditPart</code>
     */
    EditPart getTargetEditPart(Request request);

    /**
     * Sets the host in which this EditPolicy is installed.
     * 
     * @param editpart
     *            the host EditPart
     */
    void setHost(EditPart editpart);

    /**
     * Shows or updates <i>source feedback</i> for the specified
     * <code>Request</code>. This method may be called repeatedly for the
     * purpose of updating feedback based on changes to the Request.
     * <P>
     * Does nothing if the EditPolicy does not recognize the given Request.
     * <P>
     * This method is declared on {@link EditPart#showSourceFeedback(Request)
     * EditPart}, and is redeclared here so that EditPart can delegate its
     * implementation to each of its EditPolicies.
     * 
     * @param request
     *            the Request
     */
    void showSourceFeedback(Request request);

    /**
     * Shows or updates <i>target feedback</i> for the specified
     * <code>Request</code>. This method may be called repeatedly for the
     * purpose of updating feedback based on changes to the Request.
     * <P>
     * Does nothing if the EditPolicy does not recognize the given request.
     * <P>
     * This method is declared on {@link EditPart#showTargetFeedback(Request)
     * EditPart}, and is redeclared here so that EditPart can delegate its
     * implementation to each of its EditPolicies.
     * 
     * @param request
     *            the Request
     */
    void showTargetFeedback(Request request);

    /**
     * Returns <code>true</code> if this EditPolicy understand the specified
     * request.
     * <P>
     * This method is declared on {@link EditPart#understandsRequest(Request)
     * EditPart}, and is redeclared here so that EditPart can delegate its
     * implementation to each of its EditPolicies. <code>EditPart</code> returns
     * <code>true</code> if any of its EditPolicies returns <code>true</code>.
     * In other words, it performs a logical OR.
     * 
     * @param request
     *            the Request
     * @return boolean <code>true</code> if the EditPolicy understands the
     *         specified request
     * @see EditPart#understandsRequest(Request)
     */
    boolean understandsRequest(Request request);

}
