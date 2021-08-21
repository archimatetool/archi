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
package org.eclipse.gef.editparts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;

import org.eclipse.draw2d.EventListenerList;

import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.EditPartListener;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;

/**
 * The baseline implementation for the {@link EditPart} interface.
 * <P>
 * Since this is the default implementation of an interface, this document deals
 * with proper sub-classing of this implementation. This class is not the API.
 * For documentation on proper usage of the public API, see the documentation
 * for the interface itself: {@link EditPart}.
 * <P>
 * This class assumes no visual representation. Subclasses
 * {@link AbstractGraphicalEditPart} and {@link AbstractTreeEditPart} add
 * support for {@link org.eclipse.draw2d.IFigure Figures} and
 * {@link org.eclipse.swt.widgets.TreeItem TreeItems} respectively.
 * <P>
 * AbstractEditPart provides support for children. All AbstractEditPart's can
 * potentially be containers for other EditParts.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractEditPart implements EditPart, RequestConstants,
        IAdaptable {

    /**
     * This flag is set during {@link #activate()}, and reset on
     * {@link #deactivate()}
     */
    protected static final int FLAG_ACTIVE = 1;
    /**
     * This flag indicates that the EditPart has focus.
     */
    protected static final int FLAG_FOCUS = 2;

    /**
     * The left-most bit that is reserved by this class for setting flags.
     * Subclasses may define additional flags starting at
     * <code>(MAX_FLAG << 1)</code>.
     */
    protected static final int MAX_FLAG = FLAG_FOCUS;

    private Object model;
    private int flags;
    private EditPart parent;
    private int selected;

    private Object policies[];

    /**
     * The List of children EditParts
     */
    protected List children;

    /**
     * call getEventListeners(Class) instead.
     */
    EventListenerList eventListeners = new EventListenerList();

    /**
     * Iterates over a <code>List</code> of EditPolcies, skipping any
     * <code>null</code> values encountered.
     */
    protected static class EditPolicyIterator {
        private Object list[];
        private int offset = 0;
        private final int length;

        EditPolicyIterator(Object list[]) {
            this.list = list;
            length = (list == null) ? 0 : list.length;
        }

        /**
         * Constructs an Iterator for the given <code>List</code>.
         * 
         * @deprecated this constructor should not be used
         * @param list
         *            the list of policies.
         */
        public EditPolicyIterator(List list) {
            this(list.toArray());
        }

        /**
         * Returns the next non-<code>null</code> EditPolicy.
         * 
         * @return the next non-<code>null</code> EditPolicy.
         */
        public EditPolicy next() {
            if (offset < length)
                return (EditPolicy) list[offset++];
            return null;
        }

        /**
         * Returns <code>true</code> if there is a next edit policy.
         * 
         * @return <code>true</code> if there is a next policy
         */
        public boolean hasNext() {
            if (list == null)
                return false;

            while (offset < list.length
                    && !(list[offset] instanceof EditPolicy))
                offset++;
            return offset < list.length;
        }
    }

    /**
     * Activates this EditPart, which in turn activates its children and
     * EditPolicies. Subclasses should <em>extend</em> this method to add
     * listeners to the model. Activation indicates that the EditPart is
     * realized in an EditPartViewer. <code>deactivate()</code> is the inverse,
     * and is eventually called on all EditParts.
     * 
     * @see EditPart#activate()
     * @see #deactivate()
     */
    @Override
    public void activate() {
        setFlag(FLAG_ACTIVE, true);

        activateEditPolicies();

        List c = getChildren();
        for (int i = 0; i < c.size(); i++)
            ((EditPart) c.get(i)).activate();

        fireActivated();
    }

    /**
     * Activates all EditPolicies installed on this part. There is no reason to
     * override this method.
     * 
     * @see #activate()
     */
    protected void activateEditPolicies() {
        EditPolicyIterator i = getEditPolicyIterator();
        while (i.hasNext())
            i.next().activate();
    }

    /**
     * Adds a child <code>EditPart</code> to this EditPart. This method is
     * called from {@link #refreshChildren()}. The following events occur in the
     * order listed:
     * <OL>
     * <LI>The child is added to the {@link #children} List, and its parent is
     * set to <code>this</code>
     * <LI>{@link #addChildVisual(EditPart, int)} is called to add the child's
     * visual
     * <LI>{@link EditPart#addNotify()} is called on the child.
     * <LI><code>activate()</code> is called if this part is active
     * <LI><code>EditPartListeners</code> are notified that the child has been
     * added.
     * </OL>
     * <P>
     * Subclasses should implement {@link #addChildVisual(EditPart, int)}.
     * 
     * @param child
     *            The <code>EditPart</code> to add
     * @param index
     *            The index
     * @see #addChildVisual(EditPart, int)
     * @see #removeChild(EditPart)
     * @see #reorderChild(EditPart,int)
     */
    protected void addChild(EditPart child, int index) {
        Assert.isNotNull(child);
        if (index == -1)
            index = getChildren().size();
        if (children == null)
            children = new ArrayList(2);

        children.add(index, child);
        child.setParent(this);
        addChildVisual(child, index);
        child.addNotify();

        if (isActive())
            child.activate();
        fireChildAdded(child, index);
    }

    /**
     * Performs the addition of the child's <i>visual</i> to this EditPart's
     * Visual. The provided subclasses {@link AbstractGraphicalEditPart} and
     * {@link AbstractTreeEditPart} already implement this method correctly, so
     * it is unlikely that this method should be overridden.
     * 
     * @param child
     *            The EditPart being added
     * @param index
     *            The child's position
     * @see #addChild(EditPart, int)
     * @see AbstractGraphicalEditPart#removeChildVisual(EditPart)
     */
    protected abstract void addChildVisual(EditPart child, int index);

    /**
     * Adds an EditPartListener.
     * 
     * @param listener
     *            the listener
     */
    @Override
    public void addEditPartListener(EditPartListener listener) {
        eventListeners.addListener(EditPartListener.class, listener);
    }

    /**
     * @see EditPart#addNotify()
     */
    @Override
    public void addNotify() {
        register();
        createEditPolicies();
        List children = getChildren();
        for (int i = 0; i < children.size(); i++)
            ((EditPart) children.get(i)).addNotify();
        refresh();
    }

    /**
     * Create the child <code>EditPart</code> for the given model object. This
     * method is called from {@link #refreshChildren()}.
     * <P>
     * By default, the implementation will delegate to the
     * <code>EditPartViewer</code>'s {@link EditPartFactory}. Subclasses may
     * override this method instead of using a Factory.
     * 
     * @param model
     *            the Child model object
     * @return The child EditPart
     */
    protected EditPart createChild(Object model) {
        return getViewer().getEditPartFactory().createEditPart(this, model);
    }

    /**
     * Creates the initial EditPolicies and/or reserves slots for dynamic ones.
     * Should be implemented to install the inital EditPolicies based on the
     * model's initial state. <code>null</code> can be used to reserve a "slot",
     * should there be some desire to guarantee the ordering of EditPolcies.
     * 
     * @see EditPart#installEditPolicy(Object, EditPolicy)
     */
    protected abstract void createEditPolicies();

    /**
     * Deactivates this EditPart, and in turn deactivates its children and
     * EditPolicies. Subclasses should <em>extend</em> this method to remove any
     * listeners established in {@link #activate()}
     * 
     * @see EditPart#deactivate()
     * @see #activate()
     */
    @Override
    public void deactivate() {
        List c = getChildren();
        for (int i = 0; i < c.size(); i++)
            ((EditPart) c.get(i)).deactivate();

        deactivateEditPolicies();

        setFlag(FLAG_ACTIVE, false);
        fireDeactivated();
    }

    /**
     * Deactivates all installed EditPolicies.
     */
    protected void deactivateEditPolicies() {
        EditPolicyIterator i = getEditPolicyIterator();
        while (i.hasNext())
            i.next().deactivate();
    }

    /**
     * This method will log a message to GEF's trace/debug system if the
     * corresponding flag for EditParts is set to true.
     * 
     * @param message
     *            a debug message
     * @deprecated in 3.1
     */
    protected final void debug(String message) {
    }

    /**
     * This method will log the message to GEF's trace/debug system if the
     * corrseponding flag for FEEDBACK is set to true.
     * 
     * @param message
     *            Message to be passed
     * @deprecated in 3.1
     */
    protected final void debugFeedback(String message) {
    }

    /**
     * Erases source feedback for the given <code>Request</code>. By default,
     * this responsibility is delegated to this part's <code>EditPolicies</code>
     * . Subclasses should rarely extend this method.
     * <P>
     * <table>
     * <tr>
     * <td><img src="../doc-files/important.gif"/>
     * <td>It is recommended that feedback be handled by EditPolicies, and not
     * directly by the EditPart.
     * </tr>
     * </table>
     * 
     * @param request
     *            identifies the type of feedback to erase.
     * @see #showSourceFeedback(Request)
     */
    @Override
    public void eraseSourceFeedback(Request request) {
        if (isActive()) {
            EditPolicyIterator iter = getEditPolicyIterator();
            while (iter.hasNext())
                iter.next().eraseSourceFeedback(request);
        }
    }

    /**
     * Erases target feedback for the given <code>Request</code>. By default,
     * this responsibility is delegated to this part's EditPolicies. Subclasses
     * should rarely extend this method.
     * <P>
     * <table>
     * <tr>
     * <td><img src="../doc-files/important.gif"/>
     * <td>It is recommended that feedback be handled by EditPolicies, and not
     * directly by the EditPart.
     * </tr>
     * </table>
     * 
     * @param request
     *            Command requesting the erase.
     * @see #showTargetFeedback(Request)
     */
    @Override
    public void eraseTargetFeedback(Request request) {
        if (isActive()) {
            EditPolicyIterator iter = getEditPolicyIterator();
            while (iter.hasNext())
                iter.next().eraseTargetFeedback(request);
        }
    }

    /**
     * Notifies <code>EditPartListeners</code> that this EditPart has been
     * activated.
     */
    protected void fireActivated() {
        Iterator listeners = getEventListeners(EditPartListener.class);
        while (listeners.hasNext())
            ((EditPartListener) listeners.next()).partActivated(this);
    }

    /**
     * Notifies <code>EditPartListeners</code> that a child has been added.
     * 
     * @param child
     *            <code>EditPart</code> being added as child.
     * @param index
     *            Position child is being added into.
     */
    protected void fireChildAdded(EditPart child, int index) {
        Iterator listeners = getEventListeners(EditPartListener.class);
        while (listeners.hasNext())
            ((EditPartListener) listeners.next()).childAdded(child, index);
    }

    /**
     * Notifies <code>EditPartListeners </code> that this EditPart has been
     * deactivated.
     */
    protected void fireDeactivated() {
        Iterator listeners = getEventListeners(EditPartListener.class);
        while (listeners.hasNext())
            ((EditPartListener) listeners.next()).partDeactivated(this);
    }

    /**
     * Notifies <code>EditPartListeners</code> that a child is being removed.
     * 
     * @param child
     *            <code>EditPart</code> being removed.
     * @param index
     *            Position of the child in children list.
     */
    protected void fireRemovingChild(EditPart child, int index) {
        Iterator listeners = getEventListeners(EditPartListener.class);
        while (listeners.hasNext())
            ((EditPartListener) listeners.next()).removingChild(child, index);
    }

    /**
     * Notifies <code>EditPartListeners</code> that the selection has changed.
     */
    protected void fireSelectionChanged() {
        Iterator listeners = getEventListeners(EditPartListener.class);
        while (listeners.hasNext())
            ((EditPartListener) listeners.next()).selectedStateChanged(this);
    }

    /**
     * Returns the <code>AccessibleEditPart</code> adapter for this EditPart.
     * The <B>same</B> adapter instance must be used throughout the editpart's
     * existance. Each adapter has a unique ID which is registered during
     * {@link #register()}. Accessibility clients can only refer to this
     * editpart via that ID.
     * 
     * @return <code>null</code> or an AccessibleEditPart adapter
     */
    protected AccessibleEditPart getAccessibleEditPart() {
        return null;
    }

    /**
     * Returns the specified adapter if recognized. Delegates to the workbench
     * adapter mechanism.
     * <P>
     * Additional adapter types may be added in the future. Subclasses should
     * extend this method as needed.
     * 
     * @see IAdaptable#getAdapter(java.lang.Class)
     */
    @Override
    public Object getAdapter(Class key) {
        if (AccessibleEditPart.class == key)
            return getAccessibleEditPart();
        return Platform.getAdapterManager().getAdapter(this, key);
    }

    /**
     * @see org.eclipse.gef.EditPart#getChildren()
     */
    @Override
    public List getChildren() {
        if (children == null)
            return Collections.EMPTY_LIST;
        return children;
    }

    /**
     * Subclasses should rarely extend this method. The default implementation
     * combines the contributions from each installed <code>EditPolicy</code>.
     * This method is implemented indirectly using EditPolicies.
     * <P>
     * <table>
     * <tr>
     * <td><img src="../doc-files/important.gif"/>
     * <td>It is recommended that Command creation be handled by EditPolicies,
     * and not directly by the EditPart.
     * </tr>
     * </table>
     * 
     * @see EditPart#getCommand(Request)
     * @see EditPolicy#getCommand(Request)
     * @param request
     *            the Request
     * @return a Command
     */
    @Override
    public Command getCommand(Request request) {
        Command command = null;
        EditPolicyIterator i = getEditPolicyIterator();
        while (i.hasNext()) {
            if (command != null)
                command = command.chain(i.next().getCommand(request));
            else
                command = i.next().getCommand(request);
        }
        return command;
    }

    /**
     * Returns an iterator for the specified type of listener
     * 
     * @param clazz
     *            the Listener type over which to iterate
     * @return Iterator
     */
    protected final Iterator getEventListeners(Class clazz) {
        return eventListeners.getListeners(clazz);
    }

    /**
     * @see org.eclipse.gef.EditPart#getEditPolicy(Object)
     */
    @Override
    public EditPolicy getEditPolicy(Object key) {
        if (policies != null)
            for (int i = 0; i < policies.length; i += 2) {
                if (key.equals(policies[i]))
                    return (EditPolicy) policies[i + 1];
            }
        return null;
    }

    /**
     * Used internally to iterate over the installed EditPolicies. While
     * EditPolicy slots may be reserved with <code>null</code>, the iterator
     * only returns the non-null ones.
     * 
     * @return an EditPolicyIterator
     */
    protected final EditPolicyIterator getEditPolicyIterator() {
        return new EditPolicyIterator(policies);
    }

    /**
     * Returns the boolean value of the given flag. Specifically, returns
     * <code>true</code> if the bitwise AND of the specified flag and the
     * internal flags field is non-zero.
     * 
     * @param flag
     *            Bitmask indicating which flag to return
     * @return the requested flag's value
     * @see #setFlag(int,boolean)
     */
    protected final boolean getFlag(int flag) {
        return (flags & flag) != 0;
    }

    /**
     * @see org.eclipse.gef.EditPart#getModel()
     */
    @Override
    public Object getModel() {
        return model;
    }

    /**
     * Returns a <code>List</code> containing the children model objects. If
     * this EditPart's model is a container, this method should be overridden to
     * returns its children. This is what causes children EditParts to be
     * created.
     * <P>
     * Callers must not modify the returned List. Must not return
     * <code>null</code>.
     * 
     * @return the List of children
     */
    protected List getModelChildren() {
        return Collections.EMPTY_LIST;
    }

    /**
     * @see org.eclipse.gef.EditPart#getParent()
     */
    @Override
    public EditPart getParent() {
        return parent;
    }

    /**
     * @see org.eclipse.gef.EditPart#getRoot()
     */
    @Override
    public RootEditPart getRoot() {
        if (getParent() == null) {
            return null;
        }
        return getParent().getRoot();
    }

    /**
     * @see org.eclipse.gef.EditPart#getSelected()
     */
    @Override
    public int getSelected() {
        return selected;
    }

    /**
     * Returns the <code>EditPart</code> which is the target of the
     * <code>Request</code>. The default implementation delegates this method to
     * the installed EditPolicies. The first non-<code>null</code> result
     * returned by an EditPolicy is returned. Subclasses should rarely extend
     * this method.
     * <P>
     * <table>
     * <tr>
     * <td><img src="../doc-files/important.gif"/>
     * <td>It is recommended that targeting be handled by EditPolicies, and not
     * directly by the EditPart.
     * </tr>
     * </table>
     * 
     * @param request
     *            Describes the type of target desired.
     * @return <code>null</code> or the <i>target</i> <code>EditPart</code>
     * @see EditPart#getTargetEditPart(Request)
     * @see EditPolicy#getTargetEditPart(Request)
     */
    @Override
    public EditPart getTargetEditPart(Request request) {
        EditPolicyIterator i = getEditPolicyIterator();
        EditPart editPart;
        while (i.hasNext()) {
            editPart = i.next().getTargetEditPart(request);
            if (editPart != null)
                return editPart;
        }

        if (RequestConstants.REQ_SELECTION == request.getType()) {
            if (isSelectable())
                return this;
        }

        return null;
    }

    /**
     * @see org.eclipse.gef.EditPart#getViewer()
     */
    @Override
    public EditPartViewer getViewer() {
        RootEditPart root = getRoot();
        if (root == null) {
            return null;
        }
        return root.getViewer();
    }

    /**
     * @see org.eclipse.gef.EditPart#hasFocus()
     */
    @Override
    public boolean hasFocus() {
        return getFlag(FLAG_FOCUS);
    }

    /**
     * @see EditPart#installEditPolicy(Object, EditPolicy)
     */
    @Override
    public void installEditPolicy(Object key, EditPolicy editPolicy) {
        Assert.isNotNull(key, "Edit Policies must be installed with keys");//$NON-NLS-1$
        if (policies == null) {
            policies = new Object[2];
            policies[0] = key;
            policies[1] = editPolicy;
        } else {
            int index = 0;
            while (index < policies.length && !key.equals(policies[index]))
                index += 2;
            if (index < policies.length) {
                index++;
                EditPolicy old = (EditPolicy) policies[index];
                if (old != null && isActive())
                    old.deactivate();
                policies[index] = editPolicy;
            } else {
                Object newPolicies[] = new Object[policies.length + 2];
                System.arraycopy(policies, 0, newPolicies, 0, policies.length);
                policies = newPolicies;
                policies[index] = key;
                policies[index + 1] = editPolicy;
            }
        }

        if (editPolicy != null) {
            editPolicy.setHost(this);
            if (isActive())
                editPolicy.activate();
        }
    }

    /**
     * @return <code>true</code> if this EditPart is active.
     */
    @Override
    public boolean isActive() {
        return getFlag(FLAG_ACTIVE);
    }

    /**
     * By default, an EditPart is regarded to be selectable.
     * 
     * @see org.eclipse.gef.EditPart#isSelectable()
     */
    @Override
    public boolean isSelectable() {
        return true;
    }

    /**
     * Subclasses should extend this method to handle Requests. For now, the
     * default implementation does not handle any requests.
     * 
     * @see EditPart#performRequest(Request)
     */
    @Override
    public void performRequest(Request req) {
    }

    /**
     * Refreshes all properties visually displayed by this EditPart. The default
     * implementation will call {@link #refreshChildren()} to update its
     * structural features. It also calls {@link #refreshVisuals()} to update
     * its own displayed properties. Subclasses should extend this method to
     * handle additional types of structural refreshing.
     */
    @Override
    public void refresh() {
        refreshVisuals();
        refreshChildren();
    }

    /**
     * Updates the set of children EditParts so that it is in sync with the
     * model children. This method is called from {@link #refresh()}, and may
     * also be called in response to notification from the model. This method
     * requires linear time to complete. Clients should call this method as few
     * times as possible. Consider also calling {@link #removeChild(EditPart)}
     * and {@link #addChild(EditPart, int)} which run in constant time.
     * <P>
     * The update is performed by comparing the existing EditParts with the set
     * of model children returned from {@link #getModelChildren()}. EditParts
     * whose models no longer exist are {@link #removeChild(EditPart) removed}.
     * New models have their EditParts {@link #createChild(Object) created}.
     * <P>
     * This method should <em>not</em> be overridden.
     * 
     * @see #getModelChildren()
     */
    protected void refreshChildren() {
        int i;
        EditPart editPart;
        Object model;

        List children = getChildren();
        int size = children.size();
        Map modelToEditPart = Collections.EMPTY_MAP;
        if (size > 0) {
            modelToEditPart = new HashMap(size);
            for (i = 0; i < size; i++) {
                editPart = (EditPart) children.get(i);
                modelToEditPart.put(editPart.getModel(), editPart);
            }
        }

        List modelObjects = getModelChildren();
        for (i = 0; i < modelObjects.size(); i++) {
            model = modelObjects.get(i);

            // Do a quick check to see if editPart[i] == model[i]
            if (i < children.size()
                    && ((EditPart) children.get(i)).getModel() == model)
                continue;

            // Look to see if the EditPart is already around but in the
            // wrong location
            editPart = (EditPart) modelToEditPart.get(model);

            if (editPart != null)
                reorderChild(editPart, i);
            else {
                // An EditPart for this model doesn't exist yet. Create and
                // insert one.
                editPart = createChild(model);
                addChild(editPart, i);
            }
        }

        // remove the remaining EditParts
        size = children.size();
        if (i < size) {
            List trash = new ArrayList(size - i);
            for (; i < size; i++)
                trash.add(children.get(i));
            for (i = 0; i < trash.size(); i++) {
                EditPart ep = (EditPart) trash.get(i);
                removeChild(ep);
            }
        }
    }

    /**
     * Refreshes this EditPart's <i>visuals</i>. This method is called by
     * {@link #refresh()}, and may also be called in response to notifications
     * from the model. This method does nothing by default. Subclasses may
     * override.
     */
    protected void refreshVisuals() {
    }

    /**
     * Registers itself in the viewer's various registries. If your EditPart has
     * a 1-to-1 relationship with a visual object and a 1-to-1 relationship with
     * a model object, the default implementation should be sufficent.
     * 
     * @see #unregister()
     * @see EditPartViewer#getVisualPartMap()
     * @see EditPartViewer#getEditPartRegistry()
     */
    protected void register() {
        registerModel();
        registerVisuals();
        registerAccessibility();
    }

    /**
     * Registers the <code>AccessibleEditPart</code> adapter.
     * 
     * @see #getAccessibleEditPart()
     */
    protected final void registerAccessibility() {
        if (getAccessibleEditPart() != null)
            getViewer().registerAccessibleEditPart(getAccessibleEditPart());
    }

    /**
     * Registers the <i>model</i> in the
     * {@link EditPartViewer#getEditPartRegistry()}. Subclasses should only
     * extend this method if they need to register this EditPart in additional
     * ways.
     */
    protected void registerModel() {
        getViewer().getEditPartRegistry().put(getModel(), this);
    }

    /**
     * Registers the <i>visuals</i> in the
     * {@link EditPartViewer#getVisualPartMap()}. Subclasses should override
     * this method for the visual part they support.
     * {@link AbstractGraphicalEditPart} and {@link AbstractTreeEditPart}
     * already do this.
     */
    protected void registerVisuals() {
    }

    /**
     * Removes a child <code>EditPart</code>. This method is called from
     * {@link #refreshChildren()}. The following events occur in the order
     * listed:
     * <OL>
     * <LI><code>EditPartListeners</code> are notified that the child is being
     * removed
     * <LI><code>deactivate()</code> is called if the child is active
     * <LI>{@link EditPart#removeNotify()} is called on the child.
     * <LI>{@link #removeChildVisual(EditPart)} is called to remove the child's
     * visual object.
     * <LI>The child's parent is set to <code>null</code>
     * </OL>
     * <P>
     * Subclasses should implement {@link #removeChildVisual(EditPart)}.
     * 
     * @param child
     *            EditPart being removed
     * @see #addChild(EditPart,int)
     */
    protected void removeChild(EditPart child) {
        Assert.isNotNull(child);
        int index = getChildren().indexOf(child);
        if (index < 0)
            return;
        fireRemovingChild(child, index);
        if (isActive())
            child.deactivate();
        child.removeNotify();
        removeChildVisual(child);
        child.setParent(null);
        getChildren().remove(child);
    }

    /**
     * Removes the childs visual from this EditPart's visual. Subclasses should
     * implement this method to support the visual type they introduce, such as
     * Figures or TreeItems.
     * 
     * @param child
     *            the child EditPart
     */
    protected abstract void removeChildVisual(EditPart child);

    /**
     * No reason to override
     * 
     * @see EditPart#removeEditPartListener(EditPartListener)
     */
    @Override
    public void removeEditPartListener(EditPartListener listener) {
        eventListeners.removeListener(EditPartListener.class, listener);
    }

    /**
     * No reason to override
     * 
     * @see EditPart#removeEditPolicy(Object)
     */
    @Override
    public void removeEditPolicy(Object key) {
        if (policies == null)
            return;
        for (int i = 0; i < policies.length; i += 2) {
            if (key.equals(policies[i])) {
                i++;
                EditPolicy policy = (EditPolicy) policies[i];
                policies[i] = null;
                if (isActive() && policy != null)
                    policy.deactivate();
            }
        }
    }

    /**
     * Removes all references from the <code>EditPartViewer</code> to this
     * EditPart. This includes:
     * <UL>
     * <LI>deselecting this EditPart if selected
     * <LI>setting the Viewer's focus to <code>null</code> if this EditPart has
     * <i>focus</i>
     * <LI>{@link #unregister()} this EditPart
     * </UL>
     * <P>
     * In addition, <code>removeNotify()</code> is called recursively on all
     * children EditParts. Subclasses should <em>extend</em> this method to
     * perform any additional cleanup.
     * 
     * @see EditPart#removeNotify()
     */
    @Override
    public void removeNotify() {
        if (getSelected() != SELECTED_NONE)
            getViewer().deselect(this);
        if (hasFocus())
            getViewer().setFocus(null);

        List children = getChildren();
        for (int i = 0; i < children.size(); i++)
            ((EditPart) children.get(i)).removeNotify();
        unregister();
    }

    /**
     * Moves a child <code>EditPart</code> into a lower index than it currently
     * occupies. This method is called from {@link #refreshChildren()}.
     * 
     * @param editpart
     *            the child being reordered
     * @param index
     *            new index for the child
     */
    protected void reorderChild(EditPart editpart, int index) {
        removeChildVisual(editpart);
        List children = getChildren();
        children.remove(editpart);
        children.add(index, editpart);
        addChildVisual(editpart, index);
    }

    /**
     * Sets the value of the specified flag. Flag values are decalared as static
     * constants. Subclasses may define additional constants above
     * {@link #MAX_FLAG}.
     * 
     * @param flag
     *            Flag being set
     * @param value
     *            Value of the flag to be set
     * @see #getFlag(int)
     */
    protected final void setFlag(int flag, boolean value) {
        if (value)
            flags |= flag;
        else
            flags &= ~flag;
    }

    /**
     * Called by {@link EditPartViewer} to indicate that the {@link EditPart}
     * has gained or lost keyboard focus. Focus is considered to be part of the
     * selected state. Therefore, only selectable {@link EditPart}s are able to
     * obtain focus, and the method may thus only be called with a value of
     * <code>true</code> in case the receiver is selectable, i.e.
     * {@link #isSelectable()} returns <code>true</code>.
     * 
     * The method should rarely be overridden. Instead, EditPolicies that are
     * selection-aware listen for notifications about the change of focus via
     * {@link EditPartListener#selectedStateChanged(EditPart)}.
     * 
     * @see EditPart#setFocus(boolean)
     * @see EditPartListener#selectedStateChanged(EditPart)
     * @see SelectionEditPolicy
     */
    @Override
    public void setFocus(boolean value) {
        // only selectable edit parts may obtain focus
        Assert.isLegal(
                isSelectable() || !value,
                "An EditPart has to be selectable (isSelectable() == true) in order to obtain focus."); //$NON-NLS-1$

        if (hasFocus() == value)
            return;
        setFlag(FLAG_FOCUS, value);
        fireSelectionChanged();
    }

    /**
     * Set the primary model object that this EditPart represents. This method
     * is used by an <code>EditPartFactory</code> when creating an EditPart.
     * 
     * @see EditPart#setModel(Object)
     */
    @Override
    public void setModel(Object model) {
        this.model = model;
    }

    /**
     * Sets the parent EditPart. There is no reason to override this method.
     * 
     * @see EditPart#setParent(EditPart)
     */
    @Override
    public void setParent(EditPart parent) {
        this.parent = parent;
    }

    /**
     * Sets the selected state for this EditPart, which may be one of:
     * <ul>
     * <li>{@link EditPart#SELECTED_PRIMARY}</li>
     * <li>{@link EditPart#SELECTED}</li>
     * <li>{@link EditPart#SELECTED_NONE}</li>.
     * </ul>
     * 
     * As only selectable {@link EditPart}s may get selected, the method may
     * only be called with a selected value of {@link EditPart#SELECTED} or
     * {@link EditPart#SELECTED_PRIMARY} in case the receiver is selectable,
     * i.e. {@link #isSelectable()} returns <code>true</code>.
     * 
     * The method should rarely be overridden. Instead, EditPolicies that are
     * selection-aware listen for notifications about the change of selection
     * state via {@link EditPartListener#selectedStateChanged(EditPart)}.
     * 
     * @see EditPart#setSelected(int)
     * @see EditPartListener#selectedStateChanged(EditPart)
     * @see SelectionEditPolicy
     * 
     * @param value
     *            the selected value
     */
    @Override
    public void setSelected(int value) {
        // only selectable edit parts may get selected.
        Assert.isLegal(
                isSelectable() || value == SELECTED_NONE,
                "An EditPart has to be selectable (isSelectable() == true) in order to get selected."); //$NON-NLS-1$

        if (selected == value)
            return;
        selected = value;
        fireSelectionChanged();
    }

    /**
     * Shows or updates source feedback for the given <code>Request</code>. By
     * default, this responsibility is delegated to this part's EditPolicies.
     * Subclasses should rarely extend this method.
     * <P>
     * <table>
     * <tr>
     * <td><img src="../doc-files/important.gif"/>
     * <td>It is recommended that feedback be handled by EditPolicies, and not
     * directly by the EditPart.
     * </tr>
     * </table>
     * 
     * @see EditPolicy#showSourceFeedback(Request)
     * @see EditPart#showSourceFeedback(Request)
     * @param request
     *            the Request
     */
    @Override
    public void showSourceFeedback(Request request) {
        if (!isActive())
            return;
        EditPolicyIterator i = getEditPolicyIterator();
        while (i.hasNext())
            i.next().showSourceFeedback(request);
    }

    /**
     * Shows or updates target feedback for the given <code>Request</code>. By
     * default, this responsibility is delegated to this part's EditPolicies.
     * Subclasses should rarely extend this method.
     * <P>
     * <table>
     * <tr>
     * <td><img src="../doc-files/important.gif"/>
     * <td>It is recommended that feedback be handled by EditPolicies, and not
     * directly by the EditPart.
     * </tr>
     * </table>
     * 
     * @see EditPolicy#showTargetFeedback(Request)
     * @see EditPart#showTargetFeedback(Request)
     * @param request
     *            the Request
     */
    @Override
    public void showTargetFeedback(Request request) {
        if (!isActive())
            return;
        EditPolicyIterator i = getEditPolicyIterator();
        while (i.hasNext())
            i.next().showTargetFeedback(request);
    }

    /**
     * Describes this EditPart for developmental debugging purposes.
     * 
     * @return a description
     */
    @Override
    public String toString() {
        String c = getClass().getName();
        c = c.substring(c.lastIndexOf('.') + 1);
        return c + "( " + getModel() + " )";//$NON-NLS-2$//$NON-NLS-1$
    }

    /**
     * Returns <code>true</code> if this <code>EditPart</code> understand the
     * given <code>Request</code>. By default, this responsibility is delegated
     * to this part's installed EditPolicies.
     * <P>
     * <table>
     * <tr>
     * <td><img src="../doc-files/important.gif"/>
     * <td>It is recommended that EditPolicies implement
     * <code>understandsRequest()</code>
     * </tr>
     * </table>
     * 
     * @see EditPart#understandsRequest(Request)
     */
    @Override
    public boolean understandsRequest(Request req) {
        EditPolicyIterator iter = getEditPolicyIterator();
        while (iter.hasNext()) {
            if (iter.next().understandsRequest(req))
                return true;
        }
        return false;
    }

    /**
     * Undoes any registration performed by {@link #register()}. The provided
     * base classes will correctly unregister their visuals.
     */
    protected void unregister() {
        unregisterAccessibility();
        unregisterVisuals();
        unregisterModel();
    }

    /**
     * Unregisters the {@link #getAccessibleEditPart() AccessibleEditPart}
     * adapter.
     */
    protected final void unregisterAccessibility() {
        if (getAccessibleEditPart() != null)
            getViewer().unregisterAccessibleEditPart(getAccessibleEditPart());
    }

    /**
     * Unregisters the <i>model</i> in the
     * {@link EditPartViewer#getEditPartRegistry()}. Subclasses should only
     * extend this method if they need to unregister this EditPart in additional
     * ways.
     */
    protected void unregisterModel() {
        Map registry = getViewer().getEditPartRegistry();
        if (registry.get(getModel()) == this)
            registry.remove(getModel());
    }

    /**
     * Unregisters the <i>visuals</i> in the
     * {@link EditPartViewer#getVisualPartMap()}. Subclasses should override
     * this method for the visual part they support.
     * {@link AbstractGraphicalEditPart} and {@link AbstractTreeEditPart}
     * already do this.
     */
    protected void unregisterVisuals() {
    }

}
