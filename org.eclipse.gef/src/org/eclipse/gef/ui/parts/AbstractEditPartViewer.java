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
package org.eclipse.gef.ui.parts;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jface.util.TransferDragSourceListener;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;

import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.SelectionManager;

/**
 * The base implementation for EditPartViewer.
 * 
 * @author hudsonr
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractEditPartViewer implements EditPartViewer {

    private DisposeListener disposeListener;

    private SelectionManager selectionModel;

    /**
     * The raw list of selected editparts.
     */
    protected final List selection = new ArrayList();

    /**
     * The unmodifiable list of selected editparts.
     */
    protected final List constantSelection = Collections
            .unmodifiableList(selection);

    /**
     * The list of selection listeners.
     * 
     * @deprecated
     */
    protected List selectionListeners = new ArrayList(1);

    /**
     * The editpart specifically set to have focus. Note that if this value is
     * <code>null</code>, the focus editpart is still implied to be the part
     * with primary selection. Subclasses should call the accessor:
     * {@link #getFocusEditPart()} whenever possible.
     * 
     * @deprecated
     */
    protected EditPart focusPart;

    private EditPartFactory factory;
    private Map mapIDToEditPart = new HashMap();
    private Map mapVisualToEditPart = new HashMap();
    private Map properties;
    private Control control;
    private ResourceManager resources;
    private EditDomain domain;
    private RootEditPart rootEditPart;
    private MenuManager contextMenu;

    private DragSource dragSource;
    private org.eclipse.gef.dnd.DelegatingDragAdapter dragAdapter = new org.eclipse.gef.dnd.DelegatingDragAdapter();

    private DropTarget dropTarget;
    private org.eclipse.gef.dnd.DelegatingDropAdapter dropAdapter = new org.eclipse.gef.dnd.DelegatingDropAdapter();

    private KeyHandler keyHandler;
    private PropertyChangeSupport changeSupport;

    /**
     * Constructs the viewer and calls {@link #init()}.
     */
    public AbstractEditPartViewer() {
        setSelectionManager(SelectionManager.createDefault());
        init();
    }

    /**
     * @see EditPartViewer#setSelectionManager(SelectionManager)
     */
    @Override
    public void setSelectionManager(SelectionManager model) {
        Assert.isNotNull(model);
        if (selectionModel != null)
            selectionModel.internalUninstall();
        selectionModel = model;
        model.internalInitialize(this, selection, new Runnable() {
            @Override
            public void run() {
                fireSelectionChanged();
            }
        });
        if (getControl() != null)
            model.internalHookControl(getControl());
    }

    /**
     * @see EditPartViewer#addDragSourceListener(org.eclipse.gef.dnd.TransferDragSourceListener)
     */
    @SuppressWarnings("deprecation")
    @Override
    public void addDragSourceListener(
            org.eclipse.gef.dnd.TransferDragSourceListener listener) {
        addDragSourceListener((TransferDragSourceListener) listener);
    }

    /**
     * @see EditPartViewer#addDragSourceListener(TransferDragSourceListener)
     */
    @Override
    public void addDragSourceListener(TransferDragSourceListener listener) {
        getDelegatingDragAdapter().addDragSourceListener(listener);
        refreshDragSourceAdapter();
    }

    /**
     * @see EditPartViewer#addDropTargetListener(org.eclipse.gef.dnd.TransferDropTargetListener)
     */
    @SuppressWarnings("deprecation")
    @Override
    public void addDropTargetListener(
            org.eclipse.gef.dnd.TransferDropTargetListener listener) {
        addDropTargetListener((TransferDropTargetListener) listener);
    }

    /**
     * @see EditPartViewer#addDropTargetListener(TransferDropTargetListener)
     */
    @Override
    public void addDropTargetListener(TransferDropTargetListener listener) {
        getDelegatingDropAdapter().addDropTargetListener(listener);
        refreshDropTargetAdapter();
    }

    /**
     * @see EditPartViewer#addPropertyChangeListener(PropertyChangeListener)
     */
    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport == null)
            changeSupport = new PropertyChangeSupport(this);
        changeSupport.addPropertyChangeListener(listener);
    }

    /**
     * @see ISelectionProvider#addSelectionChangedListener(ISelectionChangedListener)
     */
    @Override
    public void addSelectionChangedListener(ISelectionChangedListener listener) {
        selectionListeners.add(listener);
    }

    /**
     * @see EditPartViewer#appendSelection(EditPart)
     */
    @Override
    public void appendSelection(EditPart editpart) {
        selectionModel.appendSelection(editpart);
    }

    /**
     * @see EditPartViewer#createControl(Composite)
     */
    @Override
    public abstract Control createControl(Composite parent);

    /**
     * @see EditPartViewer#deselect(EditPart)
     */
    @Override
    public void deselect(EditPart editpart) {
        selectionModel.deselect(editpart);
    }

    /**
     * @see EditPartViewer#deselectAll()
     */
    @Override
    public void deselectAll() {
        selectionModel.deselectAll();
    }

    /**
     * Called if and when the <code>Control</code> is disposed. Subclasses may
     * extend this method to perform additional cleanup.
     * 
     * @param e
     *            the disposeevent
     */
    protected void handleDispose(DisposeEvent e) {
        if (resources != null)
            resources.dispose();
        setControl(null);
    }

    /**
     * @see EditPartViewer#findObjectAt(Point)
     */
    @Override
    public final EditPart findObjectAt(Point pt) {
        return findObjectAtExcluding(pt, Collections.EMPTY_SET);
    }

    /**
     * @see EditPartViewer#findObjectAtExcluding(Point, Collection)
     */
    @Override
    public final EditPart findObjectAtExcluding(Point pt, Collection exclude) {
        return findObjectAtExcluding(pt, exclude, null);
    }

    /**
     * Fires selection changed to the registered listeners at the time called.
     */
    protected void fireSelectionChanged() {
        Object listeners[] = selectionListeners.toArray();
        SelectionChangedEvent event = new SelectionChangedEvent(this,
                getSelection());
        for (int i = 0; i < listeners.length; i++)
            ((ISelectionChangedListener) listeners[i]).selectionChanged(event);
    }

    /**
     * @see EditPartViewer#flush()
     */
    @Override
    public void flush() {
    }

    /**
     * @see EditPartViewer#getContextMenu()
     */
    @Override
    public MenuManager getContextMenu() {
        return contextMenu;
    }

    /**
     * @see EditPartViewer#getContents()
     */
    @Override
    public EditPart getContents() {
        return getRootEditPart().getContents();
    }

    /**
     * @see EditPartViewer#getControl()
     */
    @Override
    public Control getControl() {
        return control;
    }

    /**
     * Returns <code>null</code> or the DelegatingDragAdapater. The adapter is
     * created automatically when
     * {@link #addDragSourceListener(TransferDragSourceListener)} is called.
     * 
     * @return <code>null</code> or the adapter
     */
    protected org.eclipse.gef.dnd.DelegatingDragAdapter getDelegatingDragAdapter() {
        return dragAdapter;
    }

    /**
     * Returns <code>null</code> or the DelegatingDropAdapater. The adapter is
     * created automatically when
     * {@link #addDropTargetListener(TransferDropTargetListener)} is called.
     * 
     * @return <code>null</code> or the adapter
     */
    protected org.eclipse.gef.dnd.DelegatingDropAdapter getDelegatingDropAdapter() {
        return dropAdapter;
    }

    /**
     * Returns <code>null</code> or the DragSource. The drag source is created
     * automatically when
     * {@link #addDragSourceListener(TransferDragSourceListener)} is called.
     * 
     * @return <code>null</code> or the drag source
     */
    protected DragSource getDragSource() {
        return dragSource;
    }

    /**
     * Returns <code>null</code> or the DropTarget. The drop target is created
     * automatically when
     * {@link #addDropTargetListener(TransferDropTargetListener)} is called.
     * 
     * @return <code>null</code> or the drop target
     */
    protected DropTarget getDropTarget() {
        return dropTarget;
    }

    /**
     * @see EditPartViewer#getEditDomain()
     */
    @Override
    public EditDomain getEditDomain() {
        return domain;
    }

    /**
     * @see EditPartViewer#getEditPartFactory()
     */
    @Override
    public EditPartFactory getEditPartFactory() {
        return factory;
    }

    /**
     * @see EditPartViewer#getEditPartRegistry()
     */
    @Override
    public Map getEditPartRegistry() {
        return mapIDToEditPart;
    }

    /**
     * @see EditPartViewer#getFocusEditPart()
     */
    @Override
    public EditPart getFocusEditPart() {
        if (focusPart != null)
            return focusPart;
        if (getSelectedEditParts().isEmpty()) {
            if (getContents() != null)
                return getContents();
            else
                return getRootEditPart();
        }
        List selection = getSelectedEditParts();
        return (EditPart) selection.get(selection.size() - 1);
    }

    /**
     * @see EditPartViewer#getKeyHandler()
     */
    @Override
    public KeyHandler getKeyHandler() {
        return keyHandler;
    }

    /**
     * @see EditPartViewer#getProperty(String)
     */
    @Override
    public Object getProperty(String key) {
        if (properties != null)
            return properties.get(key);
        return null;
    }

    /**
     * @see org.eclipse.gef.EditPartViewer#getResourceManager()
     */
    @Override
    public ResourceManager getResourceManager() {
        if (resources != null)
            return resources;
        Assert.isNotNull(getControl());
        resources = new LocalResourceManager(JFaceResources.getResources());
        return resources;
    }

    /**
     * @see EditPartViewer#getRootEditPart()
     */
    @Override
    public RootEditPart getRootEditPart() {
        return rootEditPart;
    }

    /**
     * @see EditPartViewer#getSelectedEditParts()
     */
    @Override
    public List getSelectedEditParts() {
        return constantSelection;
    }

    /**
     * Returns an ISelection containing a list of one or more EditPart. Whenever
     * {@link #getSelectedEditParts()} returns an empty list, the
     * <i>contents</i> editpart ({@link #getContents()}) is returned as the
     * current selection.
     * 
     * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
     */
    @Override
    public ISelection getSelection() {
        return selectionModel.getSelection();
    }

    /**
     * @see EditPartViewer#getSelectionManager()
     */
    @Override
    public SelectionManager getSelectionManager() {
        return selectionModel;
    }

    /**
     * @see EditPartViewer#getVisualPartMap()
     */
    @Override
    public Map getVisualPartMap() {
        return mapVisualToEditPart;
    }

    /**
     * Called once the control has been set.
     * 
     * @see #unhookControl()
     */
    protected void hookControl() {
        Control control = getControl();
        Assert.isTrue(control != null);
        getSelectionManager().internalHookControl(control);
        control.addDisposeListener(disposeListener = new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                handleDispose(e);
            }
        });
        if (getRootEditPart() != null)
            getRootEditPart().activate();
        refreshDragSourceAdapter();
        refreshDropTargetAdapter();
        if (contextMenu != null)
            control.setMenu(contextMenu.createContextMenu(getControl()));
    }

    /**
     * Called whenever the {@link #getDragSource() drag source} is automatically
     * created.
     */
    protected void hookDragSource() {
        dragSource.addDragListener(getDelegatingDragAdapter());
    }

    /**
     * Called whenever the {@link #getDropTarget() drop target} is automatically
     * created.
     */
    protected void hookDropTarget() {
        getDropTarget().addDropListener(getDelegatingDropAdapter());
    }

    /**
     * Called from the constructor. Subclasses may extend this method.
     */
    protected void init() {
    }

    private void primDeselectAll() {
        EditPart part;
        List list = primGetSelectedEditParts();
        for (int i = 0; i < list.size(); i++) {
            part = (EditPart) list.get(i);
            part.setSelected(EditPart.SELECTED_NONE);
        }
        list.clear();
    }

    /**
     * Returns the modifiable List of selected EditParts.
     * 
     * @return the internal list of selected editparts
     */
    protected List primGetSelectedEditParts() {
        return selection;
    }

    /**
     * Creates or disposes a DragSource as needed, and sets the supported
     * transfer types. Clients should not need to call or override this method.
     */
    protected void refreshDragSourceAdapter() {
        if (getControl() == null)
            return;
        if (getDelegatingDragAdapter().isEmpty())
            setDragSource(null);
        else {
            if (getDragSource() == null)
                setDragSource(new DragSource(getControl(), DND.DROP_MOVE
                        | DND.DROP_COPY | DND.DROP_LINK));
            getDragSource().setTransfer(
                    getDelegatingDragAdapter().getTransfers());
        }
    }

    /**
     * Creates or disposes a DropTarget as needed, and sets the supported
     * transfer types. Clients should not need to call or override this method.
     */
    protected void refreshDropTargetAdapter() {
        if (getControl() == null)
            return;
        if (getDelegatingDropAdapter().isEmpty())
            setDropTarget(null);
        else {
            if (getDropTarget() == null)
                setDropTarget(new DropTarget(getControl(), DND.DROP_MOVE
                        | DND.DROP_COPY | DND.DROP_LINK));
            getDropTarget().setTransfer(
                    getDelegatingDropAdapter().getTransfers());
        }
    }

    /**
     * @see EditPartViewer#registerAccessibleEditPart(AccessibleEditPart)
     */
    @Override
    public void registerAccessibleEditPart(AccessibleEditPart acc) {
    }

    /**
     * @see EditPartViewer#removeDragSourceListener(org.eclipse.gef.dnd.TransferDragSourceListener)
     * @deprecated
     */
    @Override
    public void removeDragSourceListener(
            org.eclipse.gef.dnd.TransferDragSourceListener listener) {
        removeDragSourceListener((TransferDragSourceListener) listener);
    }

    /**
     * @see EditPartViewer#removeDragSourceListener(TransferDragSourceListener)
     */
    @Override
    public void removeDragSourceListener(TransferDragSourceListener listener) {
        getDelegatingDragAdapter().removeDragSourceListener(listener);
        if (getDelegatingDragAdapter().isEmpty())
            refreshDragSourceAdapter();
    }

    /**
     * @see EditPartViewer#removeDropTargetListener(org.eclipse.gef.dnd.TransferDropTargetListener)
     * @deprecated
     */
    @Override
    public void removeDropTargetListener(
            org.eclipse.gef.dnd.TransferDropTargetListener listener) {
        removeDropTargetListener((TransferDropTargetListener) listener);
    }

    /**
     * @see EditPartViewer#removeDropTargetListener(TransferDropTargetListener)
     */
    @Override
    public void removeDropTargetListener(TransferDropTargetListener listener) {
        getDelegatingDropAdapter().removeDropTargetListener(listener);
        if (getDelegatingDropAdapter().isEmpty())
            refreshDropTargetAdapter();
    }

    /**
     * @see EditPartViewer#removePropertyChangeListener(PropertyChangeListener)
     */
    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport != null) {
            changeSupport.removePropertyChangeListener(listener);
            if (changeSupport.getPropertyChangeListeners().length == 0)
                changeSupport = null;
        }
    }

    /**
     * @see ISelectionProvider#removeSelectionChangedListener(ISelectionChangedListener)
     */
    @Override
    public void removeSelectionChangedListener(ISelectionChangedListener l) {
        selectionListeners.remove(l);
    }

    /**
     * @see EditPartViewer#reveal(EditPart)
     */
    @Override
    public void reveal(EditPart part) {
    }

    /**
     * @see EditPartViewer#select(EditPart)
     */
    @Override
    public void select(EditPart editpart) {
        // If selection isn't changing, do nothing.
        if ((getSelectedEditParts().size() == 1)
                && (getSelectedEditParts().get(0) == editpart))
            return;
        primDeselectAll();
        appendSelection(editpart); // fireSelectionChanged() is called here
    }

    /**
     * @see EditPartViewer#setContextMenu(MenuManager)
     */
    @Override
    public void setContextMenu(MenuManager manager) {
        if (contextMenu != null)
            contextMenu.dispose();
        contextMenu = manager;
        if (getControl() != null && !getControl().isDisposed())
            getControl().setMenu(contextMenu.createContextMenu(getControl()));
    }

    /**
     * @see EditPartViewer#setContents(EditPart)
     */
    @Override
    public void setContents(EditPart editpart) {
        getRootEditPart().setContents(editpart);
    }

    /**
     * @see EditPartViewer#setContents(Object)
     */
    @Override
    public void setContents(Object contents) {
        Assert.isTrue(getEditPartFactory() != null,
                "An EditPartFactory is required to call setContents(Object)");//$NON-NLS-1$
        setContents(getEditPartFactory().createEditPart(null, contents));
    }

    /**
     * @see EditPartViewer#setControl(Control)
     */
    @Override
    public void setControl(Control control) {
        if (this.control != null)
            unhookControl();
        this.control = control;
        if (control != null)
            hookControl();
    }

    /**
     * @see EditPartViewer#setCursor(Cursor)
     */
    @Override
    public void setCursor(Cursor cursor) {
        if (getControl() == null || getControl().isDisposed())
            return;
        getControl().setCursor(cursor);
    }

    /**
     * Sets the drag source. Called from {@link #refreshDragSourceAdapter()}.
     * 
     * @param source
     *            <code>null</code> or a drag source
     */
    protected void setDragSource(DragSource source) {
        if (dragSource != null)
            dragSource.dispose();
        dragSource = source;
        if (dragSource != null)
            hookDragSource();
    }

    /**
     * Sets the drop target. Called from {@link #refreshDropTargetAdapter()}.
     * 
     * @param target
     *            dropTarget <code>null</code> or a drop target
     */
    protected void setDropTarget(DropTarget target) {
        if (dropTarget != null)
            dropTarget.dispose();
        dropTarget = target;
        if (dropTarget != null)
            hookDropTarget();
    }

    /**
     * @see EditPartViewer#setEditDomain(EditDomain)
     */
    @Override
    public void setEditDomain(EditDomain editdomain) {
        this.domain = editdomain;
    }

    /**
     * @see EditPartViewer#setEditPartFactory(org.eclipse.gef.EditPartFactory)
     */
    @Override
    public void setEditPartFactory(EditPartFactory factory) {
        this.factory = factory;
    }

    /**
     * @see EditPartViewer#setFocus(EditPart)
     */
    @Override
    public void setFocus(EditPart part) {
        focusPart = part;
        getSelectionManager().setFocus(part);
    }

    /**
     * @see EditPartViewer#setKeyHandler(KeyHandler)
     */
    @Override
    public void setKeyHandler(KeyHandler handler) {
        keyHandler = handler;
    }

    /**
     * @see EditPartViewer#setProperty(String, Object)
     */
    @Override
    public void setProperty(String key, Object value) {
        if (properties == null)
            properties = new HashMap();
        Object old;
        if (value == null)
            old = properties.remove(key);
        else
            old = properties.put(key, value);

        if (changeSupport != null)
            changeSupport.firePropertyChange(key, old, value);
    }

    /**
     * @see EditPartViewer#setRootEditPart(RootEditPart)
     */
    @Override
    public void setRootEditPart(RootEditPart editpart) {
        if (rootEditPart != null) {
            if (rootEditPart.isActive())
                rootEditPart.deactivate();
            rootEditPart.setViewer(null);
        }
        rootEditPart = editpart;
        rootEditPart.setViewer(this);
        if (getControl() != null)
            rootEditPart.activate();
    }

    /**
     * @see EditPartViewer#setRouteEventsToEditDomain(boolean)
     */
    @Override
    public void setRouteEventsToEditDomain(boolean value) {
    }

    /**
     * Sets the selection to the given selection and fires selection changed.
     * The ISelection should be an {@link IStructuredSelection} or it will be
     * ignored.
     * 
     * @see ISelectionProvider#setSelection(ISelection)
     */
    @Override
    public void setSelection(ISelection newSelection) {
        selectionModel.setSelection(newSelection);
    }

    /**
     * Called when the control is being set to <code>null</code>, but before it
     * is null.
     */
    protected void unhookControl() {
        Assert.isTrue(getControl() != null);

        if (disposeListener != null) {
            getControl().removeDisposeListener(disposeListener);
            disposeListener = null;
        }
        if (getContextMenu() != null)
            getContextMenu().dispose();
        if (getRootEditPart() != null)
            getRootEditPart().deactivate();
    }

    /**
     * Does nothing by default. Subclasses needing to add accessibility support
     * should override this method.
     * 
     * @see EditPartViewer#unregisterAccessibleEditPart(AccessibleEditPart)
     */
    @Override
    public void unregisterAccessibleEditPart(AccessibleEditPart acc) {
    }

}
