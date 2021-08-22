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
package org.eclipse.draw2d.parts;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.ScaledGraphics;
import org.eclipse.draw2d.UpdateListener;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A Thumbnail is a Figure that displays an image of its source Figure at a
 * smaller size. The Thumbnail will maintain the aspect ratio of the source
 * Figure.
 * 
 * @author Eric Bordeau
 * @author Alexander Nyßen (anyssen)
 */
@SuppressWarnings("rawtypes")
public class Thumbnail extends Figure implements UpdateListener {
    
    // Bug on Mac - images are cached
    // See https://github.com/archimatetool/archi/issues/401 
    // See https://bugs.eclipse.org/bugs/show_bug.cgi?id=543796
    // This affects macOS 10.14 and greater but we'll patch it for all macOS versions
    private static final boolean useMacFix = "cocoa".equals(SWT.getPlatform()); //$NON-NLS-1$
    
    /**
     * This updates the Thumbnail by breaking the thumbnail {@link Image} into
     * several tiles and updating each tile individually.
     */
    class ThumbnailUpdater implements Runnable {
        private static final int MIN_TILE_SIZE = 256;
        private static final int MAX_NUMBER_OF_TILES = 16;
        private int currentHTile, currentVTile;
        private int hTiles, vTiles;
        private Dimension tileSize;
        private Dimension sourceSize; // the source size that was used for the
                                        // tileSize computation
        private boolean isActive = true;

        private boolean isRunning = false;
        private Image tileImage;
        private Dimension tileImageSize;
        // GC and Graphics to let the source figure paint on the tile image
        private GC tileGC;
        private SWTGraphics tileGCGraphics;
        private ScaledGraphics tileGraphics;
        // GC used to copy from the tile image into the thumbnail image
        private GC thumbnailGC;
        
        // Actual graphics used. Can be tileGCGraphics or tileGraphics depending on useScaledGraphics setting
        private Graphics theGraphics;

        /**
         * Stops the updater and disposes of any resources.
         */
        public void deactivate() {
            setActive(false);
            stop();
            if (thumbnailImage != null) {
                thumbnailImage.dispose();
                thumbnailImage = null;
                thumbnailImageSize = null;
            }
        }

        /**
         * Returns the current horizontal tile index.
         * 
         * @return current horizontal tile index.
         */
        protected int getCurrentHTile() {
            return currentHTile;
        }

        /**
         * Returns the current vertical tile index.
         * 
         * @return current vertical tile index.
         */
        protected int getCurrentVTile() {
            return currentVTile;
        }

        /**
         * Returns <code>true</code> if this ThumbnailUpdater is active. An
         * inactive updater has disposed of its {@link Image}. The updater may
         * be active and not currently running.
         * 
         * @return <code>true</code> if this ThumbnailUpdater is active
         */
        public boolean isActive() {
            return isActive;
        }

        /**
         * Returns <code>true</code> if this is currently running and updating
         * at least one tile on the thumbnail {@link Image}.
         * 
         * @return <code>true</code> if this is currently running
         */
        public boolean isRunning() {
            return isRunning;
        }

        /**
         * Resets the number of vertical and horizontal tiles, as well as the
         * tile size and current tile index.
         */
        public void resetTileValues() {
            // Keep track of source size that matches the computed tile size.
            sourceSize = getSourceRectangle().getSize();

            // Compute number of horizontal and vertical tiles and the size of
            // each tile (while the last tile in horizontal and vertical
            // direction may be smaller); ensure that all tiles except those on
            // the bottom and right border will have at least a size of
            // MIN_TILE_SIZE size and that at most MAX_NUMBER_OF_TILES tiles
            // will be created.
            hTiles = Math.min(
                    (int) Math.ceil((float) sourceSize.width
                            / (float) MIN_TILE_SIZE), MAX_NUMBER_OF_TILES);
            vTiles = Math.min(
                    (int) Math.ceil((float) sourceSize.height
                            / (float) MIN_TILE_SIZE), MAX_NUMBER_OF_TILES);
            tileSize = new Dimension((int) Math.ceil((float) sourceSize.width
                    / (float) hTiles),
                    (int) Math.ceil((float) sourceSize.height / (float) vTiles));

            // Reset the current indices so that the next update will start with
            // the first tile in horizontal and vertical direction
            currentHTile = 0;
            currentVTile = 0;
        }

        /**
         * Restarts the updater.
         */
        public void restart() {
            stop();
            start();
        }

        /**
         * Updates the current tile on the Thumbnail. An area of the source
         * Figure is painted to an {@link Image}. That Image is then drawn on
         * the Thumbnail. Scaling of the source Image is done inside
         * {@link GC#drawImage(Image, int, int, int, int, int, int, int, int)}
         * since the source and target sizes are different. The current tile
         * indexes are incremented and if more updating is necesary, this
         * {@link Runnable} is called again in a
         * {@link Display#timerExec(int, Runnable)}. If no more updating is
         * required, {@link #stop()} is called.
         */
        @Override
        public void run() {
            if (!isActive() || !isRunning() || theGraphics == null)
                return;

            int v = getCurrentVTile();
            int sy1 = v * tileSize.height;
            int sy2 = Math.min((v + 1) * tileSize.height, sourceSize.height);

            int h = getCurrentHTile();
            int sx1 = h * tileSize.width;
            int sx2 = Math.min((h + 1) * tileSize.width, sourceSize.width);
            
            // Mac hack - create new Tile Graphics instances
            if(useMacFix) {
                createTileGraphics();
            }

            theGraphics.pushState();
            // clear the background (by filling with the background color)
            Rectangle rect = new Rectangle(0, 0, sx2 - sx1, sy2 - sy1);
            theGraphics.fillRectangle(rect);

            // Let the source figure paint into the tile image.
            // IMPORTANT (fix for bug #309912): we do not let the source figure
            // paint directly into the thumbnail image, because we cannot ensure
            // that it paints completely inside the current tile area (it may
            // set its own clip inside paint(Graphics) and overwrite areas of
            // tiles that have already been rendered. By providing an own tile
            // image and copying from it into the thumbnail image, we are safe.
            org.eclipse.draw2d.geometry.Point p = getSourceRectangle()
                    .getLocation();
            theGraphics.translate(-p.x * getScaleX() - sx1, -p.y * getScaleY()
                    - sy1);
            theGraphics.scale(getScaleX());
            sourceFigure.paint(theGraphics);
            theGraphics.popState();

            // Copy the painted tile image into the thumbnail image.
            thumbnailGC.drawImage(tileImage, 0, 0, sx2 - sx1, sy2 - sy1, sx1,
                    sy1, sx2 - sx1, sy2 - sy1);

            if (getCurrentHTile() < (hTiles - 1))
                setCurrentHTile(getCurrentHTile() + 1);
            else {
                setCurrentHTile(0);
                if (getCurrentVTile() < (vTiles - 1))
                    setCurrentVTile(getCurrentVTile() + 1);
                else
                    setCurrentVTile(0);
            }

            if (getCurrentHTile() != 0 || getCurrentVTile() != 0) {
                Display.getCurrent().asyncExec(this);
            } else if (isDirty()) {
                setDirty(false);
                Display.getCurrent().asyncExec(this);
                repaint();
            } else {
                stop();
                repaint();
            }
        }

        /**
         * Sets the active flag.
         * 
         * @param value
         *            The active value
         */
        public void setActive(boolean value) {
            isActive = value;
        }

        /**
         * Sets the current horizontal tile index.
         * 
         * @param count
         *            current horizontal tile index
         */
        protected void setCurrentHTile(int count) {
            currentHTile = count;
        }

        /**
         * Sets the current vertical tile index.
         * 
         * @param count
         *            current vertical tile index
         */
        protected void setCurrentVTile(int count) {
            currentVTile = count;
        }

        /**
         * Starts this updater. This method initializes all the necessary
         * resources and puts this {@link Runnable} on the asynch queue. If this
         * updater is not active or is already running, this method just
         * returns.
         */
        public void start() {
            if (!isActive() || isRunning())
                return;

            isRunning = true;
            setDirty(false);

            resetTileValues();

            if (!targetSize.equals(thumbnailImageSize)) {
                resetThumbnailImage();
            }

            if (targetSize.isEmpty())
                return;

            thumbnailGC = new GC(thumbnailImage, SWT.NONE);

            if (!tileSize.equals(tileImageSize)) {
                resetTileImage();
            }

            createTileGraphics();

            setScales(targetSize.width / (float) sourceSize.width,
                    targetSize.height / (float) sourceSize.height);

            Display.getCurrent().asyncExec(this);
        }
        
        /**
         * Create a new TileGCGraphics, SWTGraphics, ScaledGraphics and so on
         */
        private void createTileGraphics() {
            // For the Mac hack we have to create a new GC instance to flush the previous tile image...
            if(tileGC != null && !tileGC.isDisposed()) {
                tileGC.dispose();
            }
            tileGC = new GC(tileImage,
                    sourceFigure.isMirrored() ? SWT.RIGHT_TO_LEFT : SWT.NONE);

            // ...and this means we need a new SWTGraphics instance
            if(tileGCGraphics != null) {
                tileGCGraphics.dispose();
            }
            tileGCGraphics = new SWTGraphics(tileGC);
            
            // ...and a new ScaledGraphics instance
            if(tileGraphics != null) {
                tileGraphics.dispose();
            }
            if(useScaledGraphics) {
                tileGraphics = new ScaledGraphics(tileGCGraphics);
                theGraphics = tileGraphics;
            }
            else {
                theGraphics = tileGCGraphics;
            }

            Color color = sourceFigure.getForegroundColor();
            if (color != null)
                theGraphics.setForegroundColor(color);
            color = sourceFigure.getBackgroundColor();
            if (color != null)
                theGraphics.setBackgroundColor(color);
            theGraphics.setFont(sourceFigure.getFont());
        }

        private void resetThumbnailImage() {
            if (thumbnailImage != null)
                thumbnailImage.dispose();

            if (!targetSize.isEmpty()) {
                thumbnailImage = new Image(Display.getDefault(),
                        targetSize.width, targetSize.height);
                thumbnailImageSize = new Dimension(targetSize);
            } else {
                thumbnailImage = null;
                thumbnailImageSize = new Dimension(0, 0);
            }
        }

        private void resetTileImage() {
            if (tileImage != null)
                tileImage.dispose();

            if (!tileSize.isEmpty()) {
                tileImage = new Image(Display.getDefault(), tileSize.width,
                        tileSize.height);
                tileImageSize = new Dimension(tileSize);
            } else {
                tileImage = null;
                tileImageSize = new Dimension(0, 0);
            }
        }

        /**
         * Stops this updater. Also disposes of resources (except the thumbnail
         * image which is still needed for painting).
         */
        public void stop() {
            isRunning = false;
            if (tileGraphics != null) {
                tileGraphics.dispose();
                tileGraphics = null;
            }
            if (tileGCGraphics != null) {
                tileGCGraphics.dispose();
                tileGCGraphics = null;
            }
            if (tileGC != null) {
                tileGC.dispose();
                tileGC = null;
            }
            if (thumbnailGC != null) {
                thumbnailGC.dispose();
                thumbnailGC = null;
            }
            if (tileImage != null) {
                tileImage.dispose();
                tileImage = null;
                tileImageSize = null;
            }
            
            theGraphics = null;
            
            // Don't dispose of the thumbnail image since it is needed to paint
            // the figure when the source is not dirty (i.e. showing/hiding the
            // dock).
        }
    }

    private boolean isDirty;
    private float scaleX;
    private float scaleY;

    private IFigure sourceFigure;
    Dimension targetSize = new Dimension(0, 0);
    private Image thumbnailImage;

    private Dimension thumbnailImageSize;
    private ThumbnailUpdater updater = new ThumbnailUpdater();
    
    private boolean useScaledGraphics = true;

    /**
     * Creates a new Thumbnail. The source Figure must be set separately if you
     * use this constructor.
     */
    public Thumbnail() {
        super();
    }

    /**
     * Creates a new Thumbnail with the given IFigure as its source figure.
     * 
     * @param fig
     *            The source figure
     */
    public Thumbnail(IFigure fig) {
        this();
        setSource(fig);
    }
    
    /**
     * Set whether to use a ScaledGraphics instance for rendering
     * @param useScaledGraphics
     */
    public void setUseScaledGraphics(boolean useScaledGraphics) {
        this.useScaledGraphics = useScaledGraphics;
    }

    private Dimension adjustToAspectRatio(Dimension size,
            boolean adjustToMaxDimension) {
        Dimension sourceSize = getSourceRectangle().getSize();
        Dimension borderSize = new Dimension(getInsets().getWidth(),
                getInsets().getHeight());
        size.expand(borderSize.getNegated());
        int width, height;
        if (adjustToMaxDimension) {
            width = Math.max(size.width, (int) (size.height * sourceSize.width
                    / (float) sourceSize.height + 0.5));
            height = Math.max(size.height, (int) (size.width
                    * sourceSize.height / (float) sourceSize.width + 0.5));
        } else {
            width = Math.min(size.width, (int) (size.height * sourceSize.width
                    / (float) sourceSize.height + 0.5));
            height = Math.min(size.height, (int) (size.width
                    * sourceSize.height / (float) sourceSize.width + 0.5));
        }
        size.width = width;
        size.height = height;
        return size.expand(borderSize);
    }

    /**
     * Deactivates this Thumbnail.
     */
    public void deactivate() {
        sourceFigure.getUpdateManager().removeUpdateListener(this);
        updater.deactivate();
    }

    /**
     * Returns the preferred size of this Thumbnail. The preferred size will be
     * calculated in a way that maintains the source Figure's aspect ratio.
     * 
     * @param wHint
     *            The width hint
     * @param hHint
     *            The height hint
     * @return The preferred size
     */
    @Override
    public Dimension getPreferredSize(int wHint, int hHint) {
        if (prefSize == null)
            return adjustToAspectRatio(getBounds().getSize(), false);

        Dimension preferredSize = adjustToAspectRatio(prefSize.getCopy(), true);

        if (maxSize == null)
            return preferredSize;

        Dimension maximumSize = adjustToAspectRatio(maxSize.getCopy(), true);
        if (preferredSize.contains(maximumSize))
            return maximumSize;
        else
            return preferredSize;
    }

    /**
     * Returns the scale factor on the X-axis.
     * 
     * @return X scale
     */
    protected float getScaleX() {
        return scaleX;
    }

    /**
     * Returns the scale factor on the Y-axis.
     * 
     * @return Y scale
     */
    protected float getScaleY() {
        return scaleY;
    }

    /**
     * Returns the source figure being used to generate a thumbnail.
     * 
     * @return the source figure
     */
    protected IFigure getSource() {
        return sourceFigure;
    }

    /**
     * Returns the rectangular region relative to the source figure which will
     * be the basis of the thumbnail. The value may be returned by reference and
     * should not be modified by the caller.
     * 
     * @since 3.1
     * @return the region of the source figure being used for the thumbnail
     */
    protected Rectangle getSourceRectangle() {
        return sourceFigure.getBounds();
    }

    /**
     * Returns the scaled Image of the source Figure. If the Image needs to be
     * updated, the ThumbnailUpdater will notified.
     * 
     * @return The thumbnail image
     */
    protected Image getThumbnailImage() {
        Dimension oldSize = targetSize;
        targetSize = getPreferredSize();
        targetSize.expand(new Dimension(getInsets().getWidth(), getInsets()
                .getHeight()).negate());
        setScales(targetSize.width / (float) getSourceRectangle().width,
                targetSize.height / (float) getSourceRectangle().height);
        if ((isDirty()) && !updater.isRunning())
            updater.start();
        else if (oldSize != null && !targetSize.equals(oldSize)) {
            revalidate();
            updater.restart();
        }

        return thumbnailImage;
    }

    /**
     * Returns <code>true</code> if the source figure has changed.
     * 
     * @return <code>true</code> if the source figure has changed
     */
    protected boolean isDirty() {
        return isDirty;
    }

    /**
     * @see org.eclipse.draw2d.UpdateListener#notifyPainting(Rectangle, Map)
     */
    @Override
    public void notifyPainting(Rectangle damage, Map dirtyRegions) {
        Iterator dirtyFigures = dirtyRegions.keySet().iterator();
        while (dirtyFigures.hasNext()) {
            IFigure current = (IFigure) dirtyFigures.next();
            while (current != null) {
                if (current == getSource()) {
                    setDirty(true);
                    repaint();
                    return;
                }
                current = current.getParent();
            }
        }
    }

    /**
     * @see org.eclipse.draw2d.UpdateListener#notifyValidating()
     */
    @Override
    public void notifyValidating() {
        // setDirty(true);
        // revalidate();
    }

    /**
     * @see org.eclipse.draw2d.Figure#paintFigure(Graphics)
     */
    @Override
    protected void paintFigure(Graphics graphics) {
        Image thumbnail = getThumbnailImage();
        if (thumbnail == null)
            return;
        graphics.drawImage(thumbnail, getClientArea().getLocation());
    }

    /**
     * Sets the dirty flag.
     * 
     * @param value
     *            The dirty value
     */
    public void setDirty(boolean value) {
        isDirty = value;
    }

    /**
     * Sets the X and Y scales for the Thumbnail. These scales represent the
     * ratio between the source figure and the Thumbnail.
     * 
     * @param x
     *            The X scale
     * @param y
     *            The Y scale
     */
    protected void setScales(float x, float y) {
        scaleX = x;
        scaleY = y;
    }

    /**
     * Sets the source Figure. Also sets the scales and creates the necessary
     * update manager.
     * 
     * @param fig
     *            The source figure
     */
    public void setSource(IFigure fig) {
        if (sourceFigure == fig)
            return;
        if (sourceFigure != null)
            sourceFigure.getUpdateManager().removeUpdateListener(this);
        sourceFigure = fig;
        if (sourceFigure != null) {
            setScales((float) getSize().width
                    / (float) getSourceRectangle().width,
                    (float) getSize().height
                            / (float) getSourceRectangle().height);
            sourceFigure.getUpdateManager().addUpdateListener(this);
            repaint();
        }
    }

}
