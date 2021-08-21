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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;

import org.eclipse.draw2d.ExclusionSearch;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ExposeHelper;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.Handle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelHelper;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.editparts.ScalableRootEditPart;

/**
 * An EditPartViewer implementation based on {@link org.eclipse.draw2d.IFigure
 * Figures}.
 * 
 * @author hudsonr
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class GraphicalViewerImpl extends AbstractEditPartViewer implements
        GraphicalViewer {

    private final LightweightSystem lws = createLightweightSystem();
    IFigure rootFigure;
    private DomainEventDispatcher eventDispatcher;
    private FocusListener lFocus;

    /**
     * Constructs a GraphicalViewerImpl with the default root editpart.
     */
    public GraphicalViewerImpl() {
        createDefaultRoot();
        setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.NONE),
                MouseWheelDelegateHandler.SINGLETON);
    }

    /**
     * @see org.eclipse.gef.EditPartViewer#createControl(org.eclipse.swt.widgets.Composite)
     */
    @Override
    public Control createControl(Composite composite) {
        setControl(new Canvas(composite, SWT.NO_BACKGROUND));
        return getControl();
    }

    /**
     * Creates the default root editpart. Called during construction.
     */
    protected void createDefaultRoot() {
        setRootEditPart(new ScalableRootEditPart());
    }

    /**
     * Creates the lightweight system used to host figures. Subclasses should
     * not need to override this method.
     * 
     * @return the lightweight system
     */
    protected LightweightSystem createLightweightSystem() {
        return new LightweightSystem();
    }

    /**
     * @see AbstractEditPartViewer#handleDispose(org.eclipse.swt.events.DisposeEvent)
     */
    @Override
    protected void handleDispose(DisposeEvent e) {
        super.handleDispose(e);
        getLightweightSystem().getUpdateManager().dispose();
    }

    /**
     * This method is invoked when this viewer's control gains focus. It gives
     * focus to the {@link AbstractEditPartViewer#focusPart focusPart}, if there
     * is one.
     * 
     * @param fe
     *            the focusEvent received by this viewer's control
     */
    @SuppressWarnings("deprecation")
    protected void handleFocusGained(FocusEvent fe) {
        if (focusPart != null)
            focusPart.setFocus(true);
    }

    /**
     * This method is invoked when this viewer's control loses focus. It removes
     * focus from the {@link AbstractEditPartViewer#focusPart focusPart}, if
     * there is one.
     * 
     * @param fe
     *            the focusEvent received by this viewer's control
     */
    @SuppressWarnings("deprecation")
    protected void handleFocusLost(FocusEvent fe) {
        if (focusPart != null)
            focusPart.setFocus(false);
    }

    /**
     * @see GraphicalViewer#findHandleAt(org.eclipse.draw2d.geometry.Point)
     */
    @Override
    public Handle findHandleAt(Point p) {
        LayerManager layermanager = (LayerManager) getEditPartRegistry().get(
                LayerManager.ID);
        if (layermanager == null)
            return null;
        List list = new ArrayList(3);
        list.add(layermanager.getLayer(LayerConstants.PRIMARY_LAYER));
        list.add(layermanager.getLayer(LayerConstants.CONNECTION_LAYER));
        list.add(layermanager.getLayer(LayerConstants.FEEDBACK_LAYER));
        IFigure handle = getLightweightSystem().getRootFigure()
                .findFigureAtExcluding(p.x, p.y, list);
        if (handle instanceof Handle)
            return (Handle) handle;
        return null;
    }

    /**
     * @see EditPartViewer#findObjectAtExcluding(Point, Collection,
     *      EditPartViewer.Conditional)
     */
    @Override
    public EditPart findObjectAtExcluding(Point pt, Collection exclude,
            final Conditional condition) {
        class ConditionalTreeSearch extends ExclusionSearch {
            ConditionalTreeSearch(Collection coll) {
                super(coll);
            }

            @Override
            public boolean accept(IFigure figure) {
                EditPart editpart = null;
                while (editpart == null && figure != null) {
                    editpart = (EditPart) getVisualPartMap().get(figure);
                    figure = figure.getParent();
                }
                return editpart != null
                        && (condition == null || condition.evaluate(editpart));
            }
        }
        IFigure figure = getLightweightSystem().getRootFigure().findFigureAt(
                pt.x, pt.y, new ConditionalTreeSearch(exclude));
        EditPart part = null;
        while (part == null && figure != null) {
            part = (EditPart) getVisualPartMap().get(figure);
            figure = figure.getParent();
        }
        if (part == null)
            return getContents();
        return part;
    }

    /**
     * Flushes and pending layouts and paints in the lightweight system.
     * 
     * @see org.eclipse.gef.EditPartViewer#flush()
     */
    @Override
    public void flush() {
        getLightweightSystem().getUpdateManager().performUpdate();
    }

    /**
     * Returns the event dispatcher
     * 
     * @deprecated This method should not be called by subclasses
     * @return the event dispatcher
     */
    protected DomainEventDispatcher getEventDispatcher() {
        return eventDispatcher;
    }

    /**
     * Convenience method for finding the layer manager.
     * 
     * @return the LayerManager
     */
    protected LayerManager getLayerManager() {
        return (LayerManager) getEditPartRegistry().get(LayerManager.ID);
    }

    /**
     * Returns the lightweight system.
     * 
     * @return the system
     */
    protected LightweightSystem getLightweightSystem() {
        return lws;
    }

    /**
     * Returns the root figure
     * 
     * @deprecated There is no reason to call this method $TODO delete this
     *             method
     * @return the root figure
     */
    protected IFigure getRootFigure() {
        return rootFigure;
    }

    /**
     * Extended to flush paints during drop callbacks.
     * 
     * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#hookDropTarget()
     */
    @Override
    protected void hookDropTarget() {
        // Allow the real drop targets to make their changes first.
        super.hookDropTarget();

        // Then force and update since async paints won't occurs during a Drag
        // operation
        getDropTarget().addDropListener(new DropTargetAdapter() {
            @Override
            public void dragEnter(DropTargetEvent event) {
                flush();
            }

            @Override
            public void dragLeave(DropTargetEvent event) {
                flush();
            }

            @Override
            public void dragOver(DropTargetEvent event) {
                flush();
            }
        });
    }

    /**
     * 
     * Extended to tell the lightweight system what its control is.
     * 
     * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#hookControl()
     */
    @Override
    protected void hookControl() {
        super.hookControl();
        getLightweightSystem().setControl((Canvas) getControl());
        getControl().addFocusListener(lFocus = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                handleFocusGained(e);
            }

            @Override
            public void focusLost(FocusEvent e) {
                handleFocusLost(e);
            }
        });
    }

    /**
     * Registers the accessible editpart with the event dispatcher.
     * 
     * @param acc
     *            the accessible
     */
    @Override
    public void registerAccessibleEditPart(AccessibleEditPart acc) {
        Assert.isNotNull(acc);
        DomainEventDispatcher domainEventDispatcher = getEventDispatcher();
        if (domainEventDispatcher != null) {
            domainEventDispatcher.putAccessible(acc);
        }
    }

    /**
     * Reveals the specified editpart by using {@link ExposeHelper}s. A
     * bottom-up scan through the parent-chain is performed, looking for expose
     * helpers along the way, and asking them to expose the given editpart.
     * 
     * @see org.eclipse.gef.EditPartViewer#reveal(EditPart)
     */
    @Override
    public void reveal(EditPart part) {
        if (part == null)
            return;
        EditPart current = part.getParent();
        while (current != null) {
            ExposeHelper helper = current
                    .getAdapter(ExposeHelper.class);
            if (helper != null)
                helper.exposeDescendant(part);
            current = current.getParent();
        }
        AccessibleEditPart acc = part
                .getAdapter(AccessibleEditPart.class);
        if (acc != null)
            getControl().getAccessible().setFocus(acc.getAccessibleID());
    }

    /**
     * Extended implementation to flush the viewer as the context menu is shown.
     * 
     * @see EditPartViewer#setContextMenu(org.eclipse.jface.action.MenuManager)
     */
    @Override
    public void setContextMenu(MenuManager contextMenu) {
        super.setContextMenu(contextMenu);
        if (contextMenu != null)
            contextMenu.addMenuListener(new IMenuListener() {
                @Override
                public void menuAboutToShow(IMenuManager manager) {
                    flush();
                }
            });
    }

    /**
     * @see org.eclipse.gef.EditPartViewer#setCursor(org.eclipse.swt.graphics.Cursor)
     */
    @Override
    public void setCursor(Cursor newCursor) {
        if (getEventDispatcher() != null)
            getEventDispatcher().setOverrideCursor(newCursor);
    }

    /**
     * Extends the drag source to handle figures which handle MouseDown events,
     * thereby aborting any DragDetect callbacks.
     * 
     * @see AbstractEditPartViewer#setDragSource(org.eclipse.swt.dnd.DragSource)
     */
    @Override
    protected void setDragSource(DragSource source) {
        super.setDragSource(source);

        class TheLastListener extends DragSourceAdapter {
            @Override
            public void dragStart(DragSourceEvent event) {
                // If the EventDispatcher has captured the mouse, don't perform
                // native drag.
                if (getEventDispatcher().isCaptured())
                    event.doit = false;
                if (event.doit) {
                    // A drag is going to occur, tell the EditDomain
                    getEventDispatcher().dispatchNativeDragStarted(event,
                            GraphicalViewerImpl.this);
                    /*
                     * The mouse down that came before the dragstart, or the
                     * dragstart event itself, may have caused selection or
                     * something that needs to be painted. paints will not get
                     * processed during DND, so flush.
                     */
                    flush();
                }
            }

            @Override
            public void dragFinished(DragSourceEvent event) {
                getEventDispatcher().dispatchNativeDragFinished(event,
                        GraphicalViewerImpl.this);
            }
        }

        /*
         * The DragSource may be set to null if there are no listeners. If there
         * are listeners, this should be *the* last listener because all other
         * listeners are hooked in super().
         */
        if (source != null)
            getDragSource().addDragListener(new TheLastListener());
    }

    /**
     * @see org.eclipse.gef.EditPartViewer#setEditDomain(org.eclipse.gef.EditDomain)
     */
    @Override
    public void setEditDomain(EditDomain domain) {
        super.setEditDomain(domain);
        // Set the new event dispatcher, even if the new domain is null. This
        // will dispose
        // the old event dispatcher.
        getLightweightSystem().setEventDispatcher(
                eventDispatcher = new DomainEventDispatcher(domain, this));
    }

    /**
     * @see org.eclipse.gef.EditPartViewer#setRootEditPart(org.eclipse.gef.RootEditPart)
     */
    @Override
    public void setRootEditPart(RootEditPart editpart) {
        super.setRootEditPart(editpart);
        setRootFigure(((GraphicalEditPart) editpart).getFigure());
    }

    /**
     * Sets the lightweight system's root figure.
     * 
     * @param figure
     *            the root figure
     * @deprecated This method should no longer be used.
     */
    protected void setRootFigure(IFigure figure) {
        rootFigure = figure;
        hookRootFigure();
    }

    /**
     * Hook the root figure into this viewer's {@link LightweightSystem}.
     * 
     * @since 3.8
     */
    protected void hookRootFigure() {
        getLightweightSystem().setContents(rootFigure);
    }

    /**
     * @see org.eclipse.gef.EditPartViewer#setRouteEventsToEditDomain(boolean)
     */
    @Override
    public void setRouteEventsToEditDomain(boolean value) {
        getEventDispatcher().setRouteEventsToEditor(value);
    }

    /**
     * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#unhookControl()
     */
    @Override
    protected void unhookControl() {
        super.unhookControl();
        if (lFocus != null) {
            getControl().removeFocusListener(lFocus);
            lFocus = null;
        }
    }

    /**
     * @see EditPartViewer#unregisterAccessibleEditPart(org.eclipse.gef.AccessibleEditPart)
     */
    @Override
    public void unregisterAccessibleEditPart(AccessibleEditPart acc) {
        Assert.isNotNull(acc);
        DomainEventDispatcher domainEventDispatcher = getEventDispatcher();
        if (domainEventDispatcher != null) {
            domainEventDispatcher.removeAccessible(acc);
        }
    }

    private static class MouseWheelDelegateHandler implements MouseWheelHandler {
        private static final MouseWheelHandler SINGLETON = new MouseWheelDelegateHandler();

        private MouseWheelDelegateHandler() {
        }

        /**
         * Delegates handling to the selected editpart's MouseWheelHelper.
         * 
         * @see org.eclipse.gef.MouseWheelHandler#handleMouseWheel(org.eclipse.swt.widgets.Event,
         *      org.eclipse.gef.EditPartViewer)
         */
        @Override
        public void handleMouseWheel(Event event, EditPartViewer viewer) {
            EditPart part = viewer.getFocusEditPart();
            do {
                MouseWheelHelper helper = part
                        .getAdapter(MouseWheelHelper.class);
                if (helper != null)
                    helper.handleMouseWheelScrolled(event);
                part = part.getParent();
            } while (event.doit && part != null);
        }
    }

}
