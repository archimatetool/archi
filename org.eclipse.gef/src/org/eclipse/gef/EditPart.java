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

import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.gef.commands.Command;

/**
 * EditParts are the building blocks of GEF Viewers. As the <I>Controller</I>,
 * an EditPart ties the application's model to a visual representation.
 * EditParts are responsible for making changes to the model. EditParts
 * typically control a single model object or a coupled set of object. Visual
 * representations include {@link org.eclipse.draw2d.IFigure Figures} and
 * {@link org.eclipse.swt.widgets.TreeItem TreeItems}. Model objects are often
 * composed of other objects that the User will interact with. Similarly,
 * EditParts can be composed of or have references to other EditParts.
 * <P>
 * The creator of an EditPart should call only setModel(Object). The remaining
 * API is used mostly by Tools, EditPolicies, and other EditParts. CHANGES are
 * made to the model, not the EditPart.
 * <P>
 * Most interaction with EditParts is achieved using {@link Request Requests}. A
 * Request specifies the type of interaction. Requests are used in
 * {@link #getTargetEditPart(Request) targeting}, filtering the selection (using
 * {@link #understandsRequest(Request)}), graphical
 * {@link #showSourceFeedback(Request)} feedback, and most importantly,
 * {@link #getCommand(Request) obtaining} commands. Only {@link Command
 * Commands} should change the model.
 * <p>
 * IMPORTANT: This interface is <EM>not</EM> intended to be implemented by
 * clients. Clients should inherit from
 * {@link org.eclipse.gef.editparts.AbstractEditPart}. New methods may be added
 * in the future.
 */
@SuppressWarnings("rawtypes")
public interface EditPart extends IAdaptable {

    /**
     * Used to indicate no selection
     */
    int SELECTED_NONE = 0;

    /**
     * Used to indicate non-primary selection
     */
    int SELECTED = 1;

    /**
     * Used to indicate primary selection, or "Anchor" selection. Primary
     * selection is defined as the last object selected.
     */
    int SELECTED_PRIMARY = 2;

    /**
     * Activates the EditPart. EditParts that observe a dynamic model or support
     * editing must be <i>active</i>. Called by the managing EditPart, or the
     * Viewer in the case of the {@link RootEditPart}. This method may be called
     * again once {@link #deactivate()} has been called.
     * <P>
     * During activation the receiver should:
     * <UL>
     * <LI>begin to observe its model if appropriate, and should continue the
     * observation until {@link #deactivate()} is called.
     * <LI>activate all of its EditPolicies. EditPolicies may also observe the
     * model, although this is rare. But it is common for EditPolicies to
     * contribute additional visuals, such as selection handles or feedback
     * during interactions. Therefore it is necessary to tell the EditPolicies
     * when to start doing this, and when to stop.
     * <LI>call activate() on the EditParts it manages. This includes its
     * children, and for GraphicalEditParts, its <i>source connections</i>.
     * </UL>
     */
    void activate();

    /**
     * Adds a listener to the EditPart. Duplicate calls result in duplicate
     * notification.
     * 
     * @param listener
     *            the Listener
     */
    void addEditPartListener(EditPartListener listener);

    /**
     * Called <em>after</em> the EditPart has been added to its parent. This is
     * used to indicate to the EditPart that it should refresh itself for the
     * first time.
     */
    void addNotify();

    /**
     * Deactivates the EditPart. EditParts that observe a dynamic model or
     * support editing must be <i>active</i>. <code>deactivate()</code> is
     * guaranteed to be called when an EditPart will no longer be used. Called
     * by the managing EditPart, or the Viewer in the case of the
     * {@link RootEditPart}. This method may be called multiple times.
     * <P>
     * During deactivation the receiver should:
     * <UL>
     * <LI>remove all listeners that were added in {@link #activate}
     * <LI>deactivate all of its EditPolicies. EditPolicies may be contributing
     * additional visuals, such as selection handles or feedback during
     * interactions. Therefore it is necessary to tell the EditPolicies when to
     * start doing this, and when to stop.
     * <LI>call deactivate() on the EditParts it manages. This includes its
     * children, and for <code>GraphicalEditParts</code>, its <i>source
     * connections</i>.
     * </UL>
     */
    void deactivate();

    /**
     * Erases <i>source</i> feedback for the specified {@link Request}. A
     * Request is used to describe the type of source feedback that should be
     * erased. This method should only be called once to erase feedback. It
     * should only be called in conjunction with a prior call to
     * {@link #showSourceFeedback(Request)}.
     * 
     * @param request
     *            the type of feedback that is being erased
     */
    void eraseSourceFeedback(Request request);

    /**
     * Erases <i>target</i> feedback for the specified {@link Request}. A
     * Request is used to describe the type of target feedback that should be
     * erased. This method should only be called once to erase feedback. It
     * should only be called in conjunction with a prior call to
     * {@link #showTargetFeedback(Request)}.
     * 
     * @param request
     *            the type of feedback that is being erased
     */
    void eraseTargetFeedback(Request request);

    /**
     * Returns the List of children <code>EditParts</code>. This method should
     * rarely be called, and is only made public so that helper objects of this
     * EditPart, such as EditPolicies, can obtain the children. The returned
     * List may be by reference, and should never be modified.
     * 
     * @return a <code>List</code> of children
     */
    List getChildren();

    /**
     * Returns the {@link Command} to perform the specified Request or
     * <code>null</code>.
     * 
     * @param request
     *            describes the Command being requested
     * @return <code>null</code> or a Command
     */
    Command getCommand(Request request);

    /**
     * Returns a {@link DragTracker} for dragging this EditPart. The
     * {@link org.eclipse.gef.tools.SelectionTool SelectionTool} is the only
     * Tool by default that calls this method. The SelectionTool will use a
     * {@link org.eclipse.gef.requests.SelectionRequest} to provide information
     * such as which mouse button is down, and what modifier keys are pressed.
     * 
     * @param request
     *            a <code>Request</code> indicating the context of the drag
     * @return <code>null</code> or a DragTracker
     */
    DragTracker getDragTracker(Request request);

    /**
     * @param key
     *            the key identifying the EditPolicy
     * @return <code>null</code> or the EditPolicy installed with the given key
     */
    EditPolicy getEditPolicy(Object key);

    /**
     * Returns the primary model object that this EditPart represents. EditParts
     * may correspond to more than one model object, or even no model object. In
     * practice, the Object returned is used by other EditParts to identify this
     * EditPart. In addition, EditPolicies probably rely on this method to build
     * Commands that operate on the model.
     * 
     * @return <code>null</code> or the primary model object
     */
    Object getModel();

    /**
     * Returns the parent <code>EditPart</code>. This method should only be
     * called internally or by helpers such as EditPolicies.
     * 
     * @return <code>null</code> or the parent {@link EditPart}
     */
    EditPart getParent();

    /**
     * Returns the {@link RootEditPart}. This method should only be called
     * internally or by helpers such as edit policies. The root can be used to
     * get the viewer.
     * 
     * @return <code>null</code> or the {@link RootEditPart}
     */
    RootEditPart getRoot();

    /**
     * Returns the selected state of this EditPart. This method should only be
     * called internally or by helpers such as EditPolicies.
     * 
     * @return one of:
     *         <UL>
     *         <LI> {@link #SELECTED}
     *         <LI> {@link #SELECTED_NONE}
     *         <LI> {@link #SELECTED_PRIMARY}
     *         </UL>
     */
    int getSelected();

    /**
     * Return the <code>EditPart</code> that should be used as the <i>target</i>
     * for the specified <code>Request</code>. Tools will generally call this
     * method with the mouse location so that the receiver can implement drop
     * targeting. Typically, if this EditPart is not the requested target (for
     * example, this EditPart is not a composite), it will forward the call to
     * its parent.
     * 
     * @param request
     *            the type of target being requested
     * @return <code>null</code> or the target
     */
    EditPart getTargetEditPart(Request request);

    /**
     * Convenience method for returning the <code>EditPartViewer</code> for this
     * part.
     * 
     * @return the {@link EditPartViewer} or <code>null</code>
     */
    EditPartViewer getViewer();

    /**
     * Returns true if this EditPart has <i>focus</i>. The focus EditPart is a
     * property of the EditPartViewer. The Viewer keeps this property in sync
     * with its focus.
     * 
     * @see EditPartViewer#getFocusEditPart()
     * @return true if the EditPart has focus
     */
    boolean hasFocus();

    /**
     * Installs an EditPolicy for a specified <i>role</i>. A <i>role</i> is is
     * simply an Object used to identify the EditPolicy. An example of a role is
     * layout. {@link EditPolicy#LAYOUT_ROLE} is generally used as the key for
     * this EditPolicy. <code>null</code> is a valid value for reserving a
     * location.
     * 
     * @param role
     *            an identifier used to key the EditPolicy
     * @param editPolicy
     *            the EditPolicy
     */
    void installEditPolicy(Object role, EditPolicy editPolicy);

    /**
     * returns <code>true</code> if the EditPart is active. Editparts are active
     * after {@link #activate()} is called, and until {@link #deactivate()} is
     * called.
     * 
     * @return <code>true</code> when active
     */
    boolean isActive();

    /**
     * Returns <code>true</code> if the EditPart is selectable. A selectable
     * EditPart may be selected as a result of the
     * {@link org.eclipse.gef.tools.SelectionTool} receiving a mouse down, or as
     * a result of the User pressing a key to change selection.
     * 
     * @return <code>true</code> if the receiver can be selected
     */
    boolean isSelectable();

    /**
     * Performs the specified Request. This method can be used to send a generic
     * message to an EditPart. If the EditPart interprets this request to mean
     * make a change in the model, it should still use <code>Commands</code> and
     * the <code>CommandStack</code> so that the change is undoable. The
     * CommandStack is available from the <code>EditDomain</code>.
     * 
     * @param request
     *            the request to be performed
     */
    void performRequest(Request request);

    /**
     * Called to force a refresh of this EditPart. All visuals properties will
     * be updated, as well as structural features like children.
     */
    void refresh();

    /**
     * Removes the first occurance of the specified listener from the list of
     * listeners. Does nothing if the listener was not present.
     * 
     * @param listener
     *            the listener being removed
     */
    void removeEditPartListener(EditPartListener listener);

    /**
     * Removes the EditPolicy for the given <i>role</i>. The EditPolicy is
     * deactivated if it is active. The position for that role is maintained
     * with <code>null</code> in the place of the old EditPolicy.
     * 
     * @param role
     *            the key identifying the EditPolicy to be removed
     * @see #installEditPolicy(Object, EditPolicy)
     */
    void removeEditPolicy(Object role);

    /**
     * Called when the EditPart is being permanently removed from its
     * {@link EditPartViewer}. This indicates that the EditPart will no longer
     * be in the Viewer, and therefore should remove itself from the Viewer.
     * This method is <EM>not</EM> called when a Viewer is disposed. It is only
     * called when the EditPart is removed from its parent. This method is the
     * inverse of {@link #addNotify()}
     */
    void removeNotify();

    /**
     * Set the <i>focus</i> property to reflect the value in the EditPartViewer.
     * Focus is determined by the EditPartViewer.
     * <P>
     * <I>Focus</I> is considered to be part of the selected state, changing
     * this value will fire
     * {@link EditPartListener#selectedStateChanged(EditPart)}.
     * <P>
     * IMPORTANT: This method should only be called by the EditPartViewer.
     * 
     * @param hasFocus
     *            boolean indicating if this part has focus
     */
    void setFocus(boolean hasFocus);

    /**
     * <img src="doc-files/dblack.gif"/>Sets the model. This method is made
     * public to facilitate the use of {@link EditPartFactory EditPartFactories}
     * .
     * 
     * <P>
     * IMPORTANT: This method should only be called once.
     * 
     * @param model
     *            the Model
     */
    void setModel(Object model);

    /**
     * <img src="doc-files/dblack.gif"/>Sets the parent. This should only be
     * called by the parent EditPart.
     * 
     * @param parent
     *            the parent EditPart
     */
    void setParent(EditPart parent);

    /**
     * <img src="doc-files/dblack.gif"/> Sets the selected state property to
     * reflect the selection in the EditPartViewer. Fires
     * selectedStateChanged(EditPart) to any EditPartListeners. Selection is
     * maintained by the EditPartViewer.
     * <P>
     * IMPORTANT: This method should only be called by the EditPartViewer.
     * 
     * @param value
     *            an enum indicating the selected state
     * @see #getSelected()
     */
    void setSelected(int value);

    /**
     * Shows or updates source feedback for the given request. This method may
     * be called multiple times so that the feedback can be updated for changes
     * in the request, such as the mouse location changing.
     * 
     * @param request
     *            request describing the type of feedback
     */
    void showSourceFeedback(Request request);

    /**
     * Shows or updates target feedback for the given request. This method can
     * be called multiple times so that the feedback can be updated for changes
     * in the request, such as the mouse location changing.
     * 
     * @param request
     *            request describing the type of feedback
     */
    void showTargetFeedback(Request request);

    /**
     * Used to filter EditParts out of the current <i>selection</i>. If an
     * operation is going to be performed on the current selection, the
     * selection can first be culled to remove EditParts that do not participate
     * in the operation. For example, when aligning the left edges of
     * GraphicalEditParts, it makes sense to ignore any selected
     * ConnectionEditParts, as they cannot be aligned.
     * 
     * @param request
     *            a <code>Request</code> describing an operation of some type
     * @return <code>true</code> if Request is understood
     */
    boolean understandsRequest(Request request);

}
