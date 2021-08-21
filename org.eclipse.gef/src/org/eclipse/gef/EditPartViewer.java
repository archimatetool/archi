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

import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.ISelection;

import org.eclipse.draw2d.geometry.Point;

/**
 * An adapter on an SWT {@link org.eclipse.swt.widgets.Control} that manages the
 * {@link org.eclipse.gef.EditPart EditParts}. The viewer is responsible for the
 * editpart lifecycle. Editparts have <i>visuals</i>, such as
 * <code>TreeItems</code> or <code>Figures</code>, which are hosted by the
 * viewer and its control. The viewer provides targeting of editparts via their
 * visuals.
 * 
 * <P>
 * A viewer is a {@link org.eclipse.jface.viewers.ISelectionProvider}. It
 * maintains a list of selected editparts. The last member of this list is the
 * <i>primary</i> member of the selection. The list should never be empty; when
 * no editparts are selected, the viewer's contents editpart is used.
 * 
 * <P>
 * A viewer is populated by setting its <i>contents</i>. This can be done by
 * passing the model corresponding to the contents. The viewer's
 * {@link org.eclipse.gef.EditPartFactory EditPartFactory} is then used to
 * create the contents editpart, and add it to the <i>root</i> editpart.
 * Alternatively, the contents editpart itself can be provided. Once the
 * contents editpart is parented, it will populate the rest of the viewer by
 * calling its {@link EditPart#refresh()} method.
 * 
 * <P>
 * The Root editpart does not correspond to anything in the model, it is used to
 * bootstrap the viewer, and to parent the contents. Depending on the type of
 * viewer being used, it may be common to replace the root editpart. See
 * implementations of {@link org.eclipse.gef.RootEditPart}.
 * 
 * <P>
 * An editpart's lifecycle is managed by the viewer. When the Viewer is
 * realized, meaning it has an SWT <code>Control</code>, it activates its root,
 * which in turn activates all editparts. Editparts are deactivated when they
 * are removed from the viewer. When the viewer's control is disposed, all
 * editparts are similarly deactivated by decativating the root.
 * 
 * <P>
 * A Viewer has an arbitrary collection of keyed properties that can be set and
 * queried. A value of <code>null</code> is used to remove a key from the
 * property map. A viewer will fire property change notification whenever these
 * values are updated.
 * 
 * <P>
 * WARNING: This interface is not intended to be implemented. Clients should
 * extend {@link org.eclipse.gef.ui.parts.AbstractEditPartViewer}.
 */
@SuppressWarnings("rawtypes")
public interface EditPartViewer extends
        org.eclipse.jface.viewers.ISelectionProvider {

    /**
     * An object which evaluates an EditPart for an arbitrary property.
     * Conditionals are used when querying a viewer for an editpart.
     * 
     * @author hudsonr
     */
    interface Conditional {
        /**
         * Returns <code>true</code> if the editpart meets this condition.
         * 
         * @param editpart
         *            the editpart being evaluated
         * @return <code>true</code> if the editpart meets the condition
         */
        boolean evaluate(EditPart editpart);
    }

    /**
     * Provided for compatibility with existing code.
     * 
     * @param listener
     *            a drag source listener
     * @see #addDragSourceListener(TransferDragSourceListener)
     */
    @SuppressWarnings("deprecation")
    void addDragSourceListener(
            org.eclipse.gef.dnd.TransferDragSourceListener listener);

    /**
     * Adds a <code>TransferDragSourceListener</code> to this viewer. This has
     * the side-effect of creating a {@link org.eclipse.swt.dnd.DragSource} on
     * the viewer's Control. A Control can only have a single DragSource.
     * Clients must not create their own DragSource when using this method.
     * 
     * @param listener
     *            the listener
     */
    void addDragSourceListener(TransferDragSourceListener listener);

    /**
     * Provided for compatibility with existing code.
     * 
     * @param listener
     *            the listener
     * @see #addDropTargetListener(TransferDropTargetListener)
     */
    @SuppressWarnings("deprecation")
    void addDropTargetListener(
            org.eclipse.gef.dnd.TransferDropTargetListener listener);

    /**
     * Adds a <code>TransferDropTargetListener</code> to this viewer. This has
     * the side-effect of creating a {@link org.eclipse.swt.dnd.DropTarget} on
     * the viewer's Control. A Control can only have a single DropTarget.
     * Clients must not create their own DropTarget when using this method.
     * 
     * @param listener
     *            the listener
     */
    void addDropTargetListener(TransferDropTargetListener listener);

    /**
     * Adds a listener to be notified of viewer property changes.
     * 
     * @param listener
     *            the listener
     */
    void addPropertyChangeListener(PropertyChangeListener listener);

    /**
     * Appends the specified <code>EditPart</code> to the viewer's
     * <i>selection</i>. The EditPart becomes the new primary selection. Fires
     * selection changed to all
     * {@link org.eclipse.jface.viewers.ISelectionChangedListener}s.
     * 
     * @param editpart
     *            the EditPart to append
     */
    void appendSelection(EditPart editpart);

    /**
     * Optionally creates the default {@link org.eclipse.swt.widgets.Control
     * Control} using the default style. The Control can also be created
     * externally and then set into the Viewer.
     * 
     * @param composite
     *            the parent in which create the SWT <code>Control</code>
     * @see #setControl(Control)
     * @return the created Control for convenience
     */
    Control createControl(Composite composite);

    /**
     * Removes the specified <code>EditPart</code> from the current selection.
     * If the selection becomes empty, the viewer's {@link #getContents()
     * contents} becomes the current selected part. The last EditPart in the new
     * selection is made {@link EditPart#SELECTED_PRIMARY primary}.
     * <P>
     * Fires selection changed to
     * {@link org.eclipse.jface.viewers.ISelectionChangedListener}s.
     * 
     * @param editpart
     *            the <code>EditPart</code> to deselect
     */
    void deselect(EditPart editpart);

    /**
     * Deselects all EditParts. The viewer's {@link #getContents() contents}
     * becomes the current selection. Fires selection changed to
     * {@link org.eclipse.jface.viewers.ISelectionChangedListener}s.
     */
    void deselectAll();

    /**
     * Returns <code>null</code> or the <code>EditPart</code> associated with
     * the specified location. The location is relative to the client area of
     * the Viewer's <code>Control</code>. An EditPart is not directly visible.
     * It is targeted using its <i>visual part</i> which it registered using the
     * {@link #getVisualPartMap() visual part map}. What constitutes a <i>visual
     * part</i> is viewer-specific. Examples include Figures and TreeItems.
     * 
     * @param location
     *            The location
     * @return <code>null</code> or an EditPart
     */
    EditPart findObjectAt(Point location);

    /**
     * Returns <code>null</code> or the <code>EditPart</code> at the specified
     * location, excluding the specified set. This method behaves similarly to
     * {@link #findObjectAt(Point)}.
     * 
     * @param location
     *            The mouse location
     * @param exclusionSet
     *            The set of EditParts to be excluded
     * @return <code>null</code> or an EditPart
     */
    EditPart findObjectAtExcluding(Point location, Collection exclusionSet);

    /**
     * Returns <code>null</code> or the <code>EditPart</code> at the specified
     * location, using the given exclusion set and conditional. This method
     * behaves similarly to {@link #findObjectAt(Point)}.
     * 
     * @param location
     *            The mouse location
     * @param exclusionSet
     *            The set of EditParts to be excluded
     * @param conditional
     *            the Conditional used to evaluate a potential hit
     * @return <code>null</code> or an EditPart
     */
    EditPart findObjectAtExcluding(Point location, Collection exclusionSet,
            Conditional conditional);

    /**
     * Flushes all pending updates to the Viewer.
     */
    void flush();

    /**
     * Returns the <i>contents</i> of this Viewer. The contents is the EditPart
     * associated with the top-level model object. It is considered to be
     * "The Diagram". If the user has nothing selected, the <i>contents</i> is
     * implicitly the selected object.
     * <P>
     * The <i>Root</i> of the Viewer is different. By constrast, the root is
     * never selected or targeted, and does not correspond to something in the
     * model.
     * 
     * @see #getRootEditPart()
     * @return the <i>contents</i> <code>EditPart</code>
     */
    EditPart getContents();

    /**
     * Returns <code>null</code> or the MenuManager for this viewer. The menu
     * manager is set using {@link #setContextMenu(MenuManager)}.
     * 
     * @return <code>null</code> or a MenuManager
     */
    MenuManager getContextMenu();

    /**
     * Returns <code>null</code> or the SWT <code>Control</code> for this
     * viewer. The control is either set explicitly or can be created by the
     * viewer.
     * 
     * @see #setControl(Control)
     * @see #createControl(Composite)
     * @return the SWT <code>Control</code>
     */
    Control getControl();

    /**
     * Returns the {@link EditDomain EditDomain} to which this viewer belongs.
     * 
     * @return the viewer's EditDomain
     */
    EditDomain getEditDomain();

    /**
     * Returns the <code>EditPartFactory</code> for this viewer. The
     * EditPartFactory is used to create the <i>contents</i> EditPart when
     * {@link #setContents(Object)} is called. It is made available so that
     * other EditParts can use it to create their children or connection
     * editparts.
     * 
     * @return EditPartFactory
     */
    EditPartFactory getEditPartFactory();

    /**
     * Returns the {@link Map} for registering <code>EditParts</code> by
     * <i>Keys</i>. EditParts may register themselves using any method, and may
     * register themselves with multiple keys. The purpose of such registration
     * is to allow an EditPart to be found by other EditParts, or by listeners
     * of domain notifiers. By default, EditParts are registered by their model.
     * <P>
     * Some models use a "domain" notification system, in which all changes are
     * dispatched to a single listener. Such a listener might use this map to
     * lookup editparts for a given model, and then ask the editpart to update.
     * 
     * @return the registry map
     */
    Map getEditPartRegistry();

    /**
     * Returns the <i>focus</i> <code>EditPart</code>. Focus refers to keyboard
     * focus. This is the same concept as focus in a native Tree or Table. The
     * User can change focus using the keyboard without affecting the currently
     * selected objects. Never returns <code>null</code>.
     * 
     * @return the <i>focus</i> <code>EditPart</code>
     */
    EditPart getFocusEditPart();

    /**
     * Returns the <code>KeyHandler</code> for this viewer. The KeyHandler is
     * sent KeyEvents by the currently active <code>Tool</code>. This is
     * important, because only the current tool knows if it is in a state in
     * which keys should be ignored, such as during a drag. By default, only the
     * {@link org.eclipse.gef.tools.SelectionTool} forwards keysrokes. It does
     * not do so during a drag.
     * 
     * @return <code>null</code> or a KeyHandler
     */
    KeyHandler getKeyHandler();

    /**
     * Returns the value of the given property. Returns <code>null</code> if the
     * property has not been set, or has been set to null.
     * 
     * @param key
     *            the property's key
     * @return the given properties value or <code>null</code>.
     */
    Object getProperty(String key);

    /**
     * Returns <code>null</code>, or the ResourceManager for this Viewer. Once a
     * viewer has a Control, clients may access the viewer's resource manager.
     * Any resources constructed using this manager, but not freed, will be
     * freed when the viewer's control is disposed. This does not mean that
     * clients should be lazy about deallocating resources. If a resource is no
     * longer needed but the viewer is still in use, the client must deallocate
     * the resource.
     * <P>
     * Typical usage is by EditParts contained inside the viewer. EditParts
     * which are removed from the viewer should free their resources during
     * {@link EditPart#removeNotify()}. When the viewer is disposed,
     * <code>removeNotify()</code> is not called, but the viewer's resource
     * manager will be disposed anyway.
     * <P>
     * The viewer's default resource manager is linked to JFace's
     * {@link JFaceResources#getResources() global shared resources}.
     * 
     * @return the ResourceManager associated with this viewer
     * @since 3.3
     */
    ResourceManager getResourceManager();

    /**
     * Returns the <code>RootEditPart</code>. The RootEditPart is a special
     * EditPart that serves as the parent to the contents editpart. The
     * <i>root</i> is never selected. The root does not correspond to anything
     * in the model. The User does not interact with the root.
     * <P>
     * The RootEditPart has a single child: the {@link #getContents() contents}.
     * <P>
     * By defining the concept of "root", GEF allows the application's "real"
     * EditParts to be more homogeneous. For example, all non-root EditParts
     * have a parent. Also, it allows applications to change the type of root
     * being used without affecting their own editpart implementation hierarchy.
     * 
     * @see #getContents()
     * @see #setRootEditPart(RootEditPart)
     * @return the RootEditPart
     */
    RootEditPart getRootEditPart();

    /**
     * Returns an unmodifiable <code>List</code> containing zero or more
     * selected editparts. This list may be empty. In contrast, the inherited
     * method
     * {@link org.eclipse.jface.viewers.ISelectionProvider#getSelection()}
     * should not return an empty selection. When no editparts are selected,
     * generally the contents editpart is considered to be selected. This list
     * can be modified indirectly by calling other methods on the viewer.
     * 
     * @return a list containing zero or more editparts
     */
    List getSelectedEditParts();

    /**
     * This method is inherited from
     * {@link org.eclipse.jface.viewers.ISelectionProvider ISelectionProvider}.
     * This method should return a
     * {@link org.eclipse.jface.viewers.StructuredSelection} containing one or
     * more of the viewer's EditParts. If no editparts are selected, the
     * {@link #getContents() contents} editpart is returned.
     * 
     * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
     */
    @Override
    ISelection getSelection();

    /**
     * Returns the viewer's selection manager. The selection manager has
     * complete control over the viewer's representation of selection. It
     * provides the {@link ISelection} for the viewer, and manages all changes
     * to the current selection.
     * 
     * @return the selection manager
     * @since 3.2
     */
    SelectionManager getSelectionManager();

    /**
     * Returns the {@link Map} for associating <i>visual parts</i> with their
     * <code>EditParts</code>. This map is used for hit-testing. Hit testing is
     * performed by first determining which visual part is hit, and then mapping
     * that part to an <code>EditPart</code>. What consistutes a <i>visual
     * part</i> is viewer-specific. Examples include <code>Figures</code> and
     * <code>TreeItems</code>.
     * 
     * @return the visual part map
     */
    Map getVisualPartMap();

    /**
     * Used for accessibility purposes.
     * 
     * @param acc
     *            the AccessibleEditPart
     */
    void registerAccessibleEditPart(AccessibleEditPart acc);

    /**
     * Removes the specified drag source listener. If all listeners are removed,
     * the DragSource that was created will be disposed.
     * 
     * @see #addDragSourceListener(TransferDragSourceListener)
     * @param listener
     *            the listener
     * @deprecated
     */
    void removeDragSourceListener(
            org.eclipse.gef.dnd.TransferDragSourceListener listener);

    /**
     * Removes the specified drag source listener. If all listeners are removed,
     * the DragSource that was created will be disposed.
     * 
     * @see #addDragSourceListener(TransferDragSourceListener)
     * @param listener
     *            the listener
     */
    void removeDragSourceListener(TransferDragSourceListener listener);

    /**
     * Removes the specified drop target listener. If all listeners are removed,
     * the DropTarget that was created will be disposed.
     * 
     * @see #addDropTargetListener(TransferDropTargetListener)
     * @param listener
     * @deprecated
     */
    void removeDropTargetListener(
            org.eclipse.gef.dnd.TransferDropTargetListener listener);

    /**
     * Removes the specified drop target listener. If all listeners are removed,
     * the DropTarget that was created will be disposed.
     * 
     * @see #addDropTargetListener(TransferDropTargetListener)
     * @param listener
     *            the listener
     */
    void removeDropTargetListener(TransferDropTargetListener listener);

    /**
     * removes the first instance of the specified property listener.
     * 
     * @param listener
     *            the listener to remove
     */
    void removePropertyChangeListener(PropertyChangeListener listener);

    /**
     * Reveals the given EditPart if it is not visible.
     * 
     * @param editpart
     *            the EditPart to reveal
     */
    void reveal(EditPart editpart);

    /**
     * Replaces the current selection with the specified <code>EditPart</code>.
     * That part becomes the primary selection. Fires selection changed to
     * {@link org.eclipse.jface.viewers.ISelectionChangedListener}s.
     * 
     * @param editpart
     *            the new selection
     */
    void select(EditPart editpart);

    /**
     * Sets the contents for this Viewer. The contents can also be set using
     * {@link #setContents(Object)}.
     * 
     * @param editpart
     *            the contents
     * @see #getRootEditPart()
     */
    void setContents(EditPart editpart);

    /**
     * Creates an <code>EditPart</code> for the provided model object using the
     * <code>EditPartFactory</code>. That EditPart is then added to the
     * {@link #getRootEditPart() RootEditPart}, and becomes the viewer's
     * contents editpart.
     * 
     * @param contents
     *            the contents model object
     */
    void setContents(Object contents);

    /**
     * Sets the context <code>MenuManager</code> for this viewer. The
     * MenuManager will be asked to create a Menu, which will be used as the
     * context menu for this viewer's Control.
     * 
     * @param contextMenu
     *            the <code>ContextMenuProvider</code>
     */
    void setContextMenu(MenuManager contextMenu);

    /**
     * Sets the <code>Control</code> for this viewer. The viewer's control is
     * also set automatically if {@link #createControl(Composite)} is called.
     * 
     * @param control
     *            the Control
     */
    void setControl(Control control);

    /**
     * Sets the cursor for the viewer's <code>Control</code>. This method should
     * only be called by {@link Tool Tools}. <code>null</code> can be used to
     * indicate that the default cursor should be restored.
     * 
     * @param cursor
     *            <code>null</code> or a Cursor
     * @see #getControl()
     */
    void setCursor(Cursor cursor);

    /**
     * Sets the <code>EditDomain</code> for this viewer. The Viewer will route
     * all mouse and keyboard events to the EditDomain.
     * 
     * @param domain
     *            The EditDomain
     */
    void setEditDomain(EditDomain domain);

    /**
     * Sets the EditPartFactory.
     * 
     * @param factory
     *            the factory
     * @see #getEditPartFactory()
     */
    void setEditPartFactory(EditPartFactory factory);

    /**
     * Sets the <i>focus</i> EditPart.
     * 
     * @see #getFocusEditPart()
     * @param focus
     *            the FocusPart.
     */
    void setFocus(EditPart focus);

    /**
     * Sets the <code>KeyHandler</code>.
     * 
     * @param keyHandler
     *            the KeyHandler
     * @see #getKeyHandler()
     */
    void setKeyHandler(KeyHandler keyHandler);

    /**
     * Sets a property on this viewer. A viewer property is an arbitrary
     * key-value pair that can be observed via
     * {@link #addPropertyChangeListener(PropertyChangeListener)}. A
     * <code>null</code> value will remove the property from the viewer.
     * 
     * @param propertyName
     *            a unique string identifying the property
     * @param value
     *            the properties new value or <code>null</code> to remove
     * @since 3.0
     */
    void setProperty(String propertyName, Object value);

    /**
     * Sets the <i>root</i> of this viewer. The root should not be confused with
     * the <i>contents</i>.
     * 
     * @param root
     *            the RootEditPart
     * @see #getRootEditPart()
     * @see #getContents()
     */
    void setRootEditPart(RootEditPart root);

    /**
     * Turns on/off the routing of events directly to the Editor. If supported
     * by the viewer implementation, all Events should be routed to the
     * <code>EditDomain</code> rather than handled in the default way.
     * 
     * @param value
     *            true if the viewer should route events to the EditDomain
     */
    void setRouteEventsToEditDomain(boolean value);

    /**
     * Sets the selection manager for this viewer.
     * 
     * @param manager
     *            the new selection manager
     * @since 3.2
     */
    void setSelectionManager(SelectionManager manager);

    /**
     * Used for accessibility purposes.
     * 
     * @param acc
     *            the accessible part
     */
    void unregisterAccessibleEditPart(AccessibleEditPart acc);

}
