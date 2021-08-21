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
package org.eclipse.draw2d;

import java.util.Map;

import org.eclipse.swt.graphics.GC;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Update managers handle the job of laying out and repainting figures. A
 * desirable implementation is to batches work to be done and collapses any
 * redundant work. For example, clients may be making multiple changes to
 * figures, which require laying out the same container or repainting the same
 * region.
 * <P>
 * The update manager receives requests to validate certain figures, and repaint
 * certain areas of figures. An update manager could process every request
 * synchronously, or it could batch these requests and process them
 * asynchronously.
 * <P>
 * The update process occurs in two phases. The first phase is laying out
 * invalid figures. This phase comes first because it usually introduces
 * additional damage regions. In some cases, while validating figures, new
 * invalid figures may be appended to the update manager. Of course, damage
 * regions will be reported too as figures are layed out.
 * <P>
 * The second phase is to repaint all damaged areas. The update manager will
 * typically batch, clip, and union, all rectangles and perform a single paint
 * of the overall damaged area.
 * 
 */
@SuppressWarnings("rawtypes")
public abstract class UpdateManager {

    private UpdateListener listeners[] = new UpdateListener[0];
    private boolean disposed;

    /**
     * Adds the dirty region defined by the coordinates on the IFigure
     * <b>figure</b>. The update manager should repaint the dirty region in a
     * timely fashion.
     * 
     * @param figure
     *            the dirty figure
     * @param x
     *            the x coordinate of the dirty region
     * @param y
     *            the y coordinate of the dirty region
     * @param w
     *            the width of the dirty region
     * @param h
     *            the height of the dirty region
     */
    public abstract void addDirtyRegion(IFigure figure, int x, int y, int w,
            int h);

    /**
     * @see #addDirtyRegion(IFigure, int, int, int, int)
     */
    public void addDirtyRegion(IFigure figure, Rectangle rect) {
        addDirtyRegion(figure, rect.x, rect.y, rect.width, rect.height);
    }

    /**
     * Causes an update to occur at some time, and the given runnable to be
     * executed following the update.
     * 
     * @since 3.1
     * @param run
     *            the runnable
     */
    public void runWithUpdate(Runnable run) {
    }

    /**
     * The receiver should call validate() on the IFigure <i>figure</i> in a
     * timely fashion.
     * 
     * @param figure
     *            the invalid figure
     */
    public abstract void addInvalidFigure(IFigure figure);

    /**
     * Adds the given listener to the list of listeners to be notified of
     * painting and validation.
     * 
     * @param listener
     *            the listener to add
     */
    public void addUpdateListener(UpdateListener listener) {
        if (listener == null)
            throw new IllegalArgumentException();
        if (listeners == null) {
            listeners = new UpdateListener[1];
            listeners[0] = listener;
        } else {
            int oldSize = listeners.length;
            UpdateListener newListeners[] = new UpdateListener[oldSize + 1];
            System.arraycopy(listeners, 0, newListeners, 0, oldSize);
            newListeners[oldSize] = listener;
            listeners = newListeners;
        }
    }

    /**
     * Called when the EditPartViewer is being disposed.
     */
    public void dispose() {
        disposed = true;
    }

    /**
     * Notifies listeners that painting is about to occur, passing them the
     * damaged rectangle and the map of dirty regions.
     * 
     * @param damage
     *            the damaged rectangle
     * @param dirtyRegions
     *            map of dirty regions to figures
     */
    protected void firePainting(Rectangle damage, Map dirtyRegions) {
        UpdateListener localListeners[] = listeners;
        for (int i = 0; i < localListeners.length; i++)
            localListeners[i].notifyPainting(damage, dirtyRegions);
    }

    /**
     * Notifies listeners that validation is about to occur.
     */
    protected void fireValidating() {
        UpdateListener localListeners[] = listeners;
        for (int i = 0; i < localListeners.length; i++)
            localListeners[i].notifyValidating();
    }

    /**
     * @return whether this update manager has been disposed.
     */
    protected boolean isDisposed() {
        return disposed;
    }

    /**
     * Forces an update to occur. Update managers will perform updates
     * automatically, but may do so asynchronously. Calling this method forces a
     * synchronous update.
     */
    public abstract void performUpdate();

    /**
     * Invoked by the {@link LightweightSystem} (
     * {@link LightweightSystem#paint(GC)}) to have the update manger paint its
     * contents. Delegates to {@link #performUpdate(Rectangle)} with the passed
     * in gc's clipping region ({@link GC#getClipping()}) by default. Subclasses
     * may override if they need to access the {@link GC} for updating.
     * 
     * @param gc
     *            The {@link GC} to be used for updating
     * @since 3.10
     */
    protected void paint(GC gc) {
        performUpdate(new Rectangle(gc.getClipping()));
    }

    /**
     * Performs an update on the given exposed rectangle.
     * 
     * @param exposed
     *            the exposed rectangle
     */
    public abstract void performUpdate(Rectangle exposed);

    /**
     * Removes one occurrence of the given UpdateListener by identity.
     * 
     * @param listener
     *            the listener to remove
     */
    public void removeUpdateListener(UpdateListener listener) {
        if (listener == null)
            throw new IllegalArgumentException();
        for (int index = 0; index < listeners.length; index++)
            if (listeners[index] == listener) {
                int newSize = listeners.length - 1;
                UpdateListener newListeners[] = null;
                if (newSize != 0) {
                    newListeners = new UpdateListener[newSize];
                    System.arraycopy(listeners, 0, newListeners, 0, index);
                    System.arraycopy(listeners, index + 1, newListeners, index,
                            newSize - index);
                } else {
                    newListeners = new UpdateListener[0];
                }
                listeners = newListeners;
                return;
            }
    }

    /**
     * Sets the GraphicsSource for this update manager.
     * 
     * @param gs
     *            the new GraphicsSource
     */
    public abstract void setGraphicsSource(GraphicsSource gs);

    /**
     * Sets the root figure.
     * 
     * @param figure
     *            the new root figure
     */
    public abstract void setRoot(IFigure figure);

    /**
     * Performs a partial update if supported (validation only). Fires
     * notification to listeners that validation has been performed. By default
     * this method calls {@link #performUpdate()}. Subclasses should override
     * this method to support validation without repainting.
     * 
     * @since 3.2
     */
    public void performValidation() {
        performUpdate();
    }

}
