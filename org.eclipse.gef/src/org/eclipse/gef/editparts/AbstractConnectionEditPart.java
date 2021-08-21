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
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.XYAnchor;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.AccessibleAnchorProvider;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.tools.SelectEditPartTracker;

/**
 * The base implementation for {@link org.eclipse.gef.ConnectionEditPart}.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class AbstractConnectionEditPart extends
        AbstractGraphicalEditPart implements ConnectionEditPart, LayerConstants {

    private static final ConnectionAnchor DEFAULT_SOURCE_ANCHOR = new XYAnchor(
            new Point(10, 10));
    private static final ConnectionAnchor DEFAULT_TARGET_ANCHOR = new XYAnchor(
            new Point(100, 100));

    /**
     * Provides accessibility support for when connections are also themselves
     * nodes. If a connection is the source or target of another connection,
     * then its midpoint is used as the accessible anchor location.
     * 
     * @author hudsonr
     * @since 2.0
     */
    protected final class DefaultAccessibleAnchorProvider implements
            AccessibleAnchorProvider {
        /**
         * This class is internal, but is made protected so that JavaDoc will
         * see it.
         */
        DefaultAccessibleAnchorProvider() {
        }

        /**
         * @see AccessibleAnchorProvider#getSourceAnchorLocations()
         */
        @Override
        public List getSourceAnchorLocations() {
            List list = new ArrayList();
            if (getFigure() instanceof Connection) {
                Point p = ((Connection) getFigure()).getPoints().getMidpoint();
                getFigure().translateToAbsolute(p);
                list.add(p);
            }
            return list;
        }

        /**
         * @see AccessibleAnchorProvider#getTargetAnchorLocations()
         */
        @Override
        public List getTargetAnchorLocations() {
            return getSourceAnchorLocations();
        }
    }

    private EditPart sourceEditPart, targetEditPart;

    /**
     * Activates the Figure representing this, by setting up the start and end
     * connections, and adding the figure to the Connection Layer.
     * 
     * @see #deactivate()
     */
    protected void activateFigure() {
        getLayer(CONNECTION_LAYER).add(getFigure());
    }

    /**
     * @see org.eclipse.gef.EditPart#addNotify()
     */
    @Override
    public void addNotify() {
        activateFigure();
        super.addNotify();
    }

    /**
     * Returns a newly created Figure to represent these type of EditParts.
     * 
     * @return The created Figure.
     */
    @Override
    protected IFigure createFigure() {
        return new PolylineConnection();
    }

    /**
     * Deactivates the Figure representing this, by removing it from the
     * connection layer, and resetting the source and target connections to
     * <code>null</code>.
     */
    protected void deactivateFigure() {
        getLayer(CONNECTION_LAYER).remove(getFigure());
        getConnectionFigure().setSourceAnchor(null);
        getConnectionFigure().setTargetAnchor(null);
    }

    /**
     * <code>AbstractConnectionEditPart</code> extends getAdapter() to overrides
     * the {@link AccessibleAnchorProvider} adapter returned by the superclass.
     * When treating a connection as a node for other connections, it makes
     * sense to target its midpoint, and not the edge of its bounds.
     * 
     * @see AbstractConnectionEditPart.DefaultAccessibleAnchorProvider
     * @see AbstractGraphicalEditPart#getAdapter(Class)
     * @param adapter
     *            the adapter Class
     * @return the adapter
     */
    @Override
    public Object getAdapter(Class adapter) {
        if (adapter == AccessibleAnchorProvider.class)
            return new DefaultAccessibleAnchorProvider();
        return super.getAdapter(adapter);
    }

    /**
     * Convenience method for casting this GraphicalEditPart's Figure to a
     * {@link Connection}
     * 
     * @return the Figure as a Connection
     */
    public Connection getConnectionFigure() {
        return (Connection) getFigure();
    }

    /**
     * @see org.eclipse.gef.EditPart#getDragTracker(Request)
     */
    @Override
    public DragTracker getDragTracker(Request req) {
        return new SelectEditPartTracker(this);
    }

    /**
     * @see org.eclipse.gef.ConnectionEditPart#getSource()
     */
    @Override
    public EditPart getSource() {
        return sourceEditPart;
    }

    /**
     * @see org.eclipse.gef.ConnectionEditPart#getTarget()
     */
    @Override
    public EditPart getTarget() {
        return targetEditPart;
    }

    /**
     * Returns the <code>ConnectionAnchor</code> for the <i>source</i> end of
     * the connection. If the source is an instance of {@link NodeEditPart},
     * that interface will be used to determine the proper ConnectionAnchor. If
     * the source is not an instance of <code>NodeEditPart</code>, this method
     * should be overridden to return the correct ConnectionAnchor. Failure to
     * do this will cause a default anchor to be used so that the connection
     * figure will be made visible to the developer.
     * 
     * @return ConnectionAnchor for the source end of the Connection
     */
    protected ConnectionAnchor getSourceConnectionAnchor() {
        if (getSource() != null) {
            if (getSource() instanceof NodeEditPart) {
                NodeEditPart editPart = (NodeEditPart) getSource();
                return editPart.getSourceConnectionAnchor(this);
            }
            IFigure f = ((GraphicalEditPart) getSource()).getFigure();
            return new ChopboxAnchor(f);
        }
        return DEFAULT_SOURCE_ANCHOR;
    }

    /**
     * Returns the <code>ConnectionAnchor</code> for the <i>target</i> end of
     * the connection. If the target is an instance of {@link NodeEditPart},
     * that interface will be used to determine the proper ConnectionAnchor. If
     * the target is not an instance of <code>NodeEditPart</code>, this method
     * should be overridden to return the correct ConnectionAnchor. Failure to
     * do this will cause a default anchor to be used so that the connection
     * figure will be made visible to the developer.
     * 
     * @return ConnectionAnchor for the target end of the Connection
     */
    protected ConnectionAnchor getTargetConnectionAnchor() {
        if (getTarget() != null) {
            if (getTarget() instanceof NodeEditPart) {
                NodeEditPart editPart = (NodeEditPart) getTarget();
                return editPart.getTargetConnectionAnchor(this);
            }
            IFigure f = ((GraphicalEditPart) getTarget()).getFigure();
            return new ChopboxAnchor(f);
        }
        return DEFAULT_TARGET_ANCHOR;
    }

    /**
     * Extended here to also refresh the ConnectionAnchors.
     * 
     * @see org.eclipse.gef.EditPart#refresh()
     */
    @Override
    public void refresh() {
        refreshSourceAnchor();
        refreshTargetAnchor();
        super.refresh();
    }

    /**
     * Updates the source ConnectionAnchor. Subclasses should override
     * {@link #getSourceConnectionAnchor()} if necessary, and not this method.
     */
    protected void refreshSourceAnchor() {
        getConnectionFigure().setSourceAnchor(getSourceConnectionAnchor());
    }

    /**
     * Updates the target ConnectionAnchor. Subclasses should override
     * {@link #getTargetConnectionAnchor()} if necessary, and not this method.
     */
    protected void refreshTargetAnchor() {
        getConnectionFigure().setTargetAnchor(getTargetConnectionAnchor());
    }

    /**
     * Extended here to remove the ConnectionEditPart's connection figure from
     * the connection layer.
     * 
     * @see org.eclipse.gef.EditPart#removeNotify()
     */
    @Override
    public void removeNotify() {
        deactivateFigure();
        super.removeNotify();
    }

    /**
     * Extended to implement automatic addNotify and removeNotify handling.
     * 
     * @see org.eclipse.gef.EditPart#setParent(EditPart)
     */
    @Override
    public void setParent(EditPart parent) {
        boolean wasNull = getParent() == null;
        boolean becomingNull = parent == null;
        if (becomingNull && !wasNull)
            removeNotify();
        super.setParent(parent);
        if (wasNull && !becomingNull)
            addNotify();
    }

    /**
     * Sets the source EditPart of this connection.
     * 
     * @param editPart
     *            EditPart which is the source.
     */
    @Override
    public void setSource(EditPart editPart) {
        if (sourceEditPart == editPart)
            return;
        sourceEditPart = editPart;
        if (sourceEditPart != null)
            setParent(sourceEditPart.getRoot());
        else if (getTarget() == null)
            setParent(null);
        if (sourceEditPart != null && targetEditPart != null)
            refresh();
    }

    /**
     * Sets the target EditPart of this connection.
     * 
     * @param editPart
     *            EditPart which is the target.
     */
    @Override
    public void setTarget(EditPart editPart) {
        if (targetEditPart == editPart)
            return;
        targetEditPart = editPart;
        if (editPart != null)
            setParent(editPart.getRoot());
        else if (getSource() == null)
            setParent(null);
        if (sourceEditPart != null && targetEditPart != null)
            refresh();
    }

}
