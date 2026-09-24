/*******************************************************************************
 * Copyright (c) 2026 Patrick Ziegler and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package com.archimatetool.editor.diagram.util;

import java.util.Objects;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.internal.InternalDraw2dUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageDataProvider;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * Implementation of Draw2D's image drawing capabilities.
 * <p>
 * <strong>Important:</strong>To make sure that the created image is properly
 * scaled on zoom changes, the {@link FigureCanvas} hosting the {@link IFigure}
 * must <b>not</b> be disposed.
 * </p>
 *
 * @since 3.23
 */
public class ImagePrintFigureOperation {
	private final Display display;
	private final Control control;
	private IFigure printSource;

	/**
	 * Constructor for PrintFigureOperation.
	 *
	 * @param control     control to to print on. Must not be {@code null}.
	 * @param printSource figure to print
	 */
	public ImagePrintFigureOperation(Control control, IFigure printSource) {
		this(control);
		setPrintSource(printSource);
	}

	/**
	 * Constructor for PrintFigureOperation.
	 * <p>
	 * Note: Descendants must call setPrintSource(IFigure) to set the IFigure that
	 * is to be printed.
	 * </p>
	 *
	 * @param control device to print on. Must not be {@code null}.
	 */
	protected ImagePrintFigureOperation(Control control) {
		Objects.requireNonNull(control, "Control must not be null"); //$NON-NLS-1$
		this.control = control;
		this.display = control.getDisplay();
	}

	/**
	 * This method contains all operations performed to {@link #printSource} prior
	 * to being printed.
	 *
	 * @param graphics The {@link Graphics} instance used for painting.
	 */
	protected void preparePrintSource(Graphics graphics) {
		// may be overridden by subclasses.
	}

	/**
	 * This method contains all operations performed to sourceFigure after being
	 * printed.
	 *
	 * @param graphics The {@link Graphics} instance used for painting.
	 */
	protected void restorePrintSource(Graphics graphics) {
		// may be overridden by subclasses.
	}

	/**
	 * Sets the printSource.
	 *
	 * @param printSource The printSource to set
	 */
	protected void setPrintSource(IFigure printSource) {
		this.printSource = printSource;
	}

	/**
	 * Returns the size of the image to create. Returns {@link IFigure#getSize()} by
	 * default. May be overridden by subclasses.
	 *
	 * @return The image size.
	 */
	protected Dimension getImageSize() {
		return printSource.getSize();
	}

	/**
	 * Calculates and returns the image data for the given monitor zoom. May be
	 * overridden by subclasses.
	 *
	 * @param zoom The monitor zoom for which the image data is created for.
	 * @return The image data for the given monitor zoom
	 */
	protected ImageData getImageDataAtZoom(int zoom) {
		Image image = createImageAtScale(zoom / 100.0);
		try {
			return image.getImageData(100);
		} finally {
			image.dispose();
		}
	}

	/**
	 * Returns whether Draw2D-based scaling is enabled. Depending on whether this
	 * method returns {@code true}, the image is drawn as if at 100% and then scaled
	 * to match the monitor zoom.
	 *
	 * @return {@code true} if Draw2D-based scaling is enabled.
	 */
	@SuppressWarnings({"static-method", "restriction"})
	protected boolean isDraw2DAutoScalingEnabled() {
		return InternalDraw2dUtils.isAutoScaleEnabled();
	}

	/**
	 * Sets the print job into motion.
	 */
	public Image run() {
		Objects.requireNonNull(printSource, "Print source must not be null"); //$NON-NLS-1$
		if (isDraw2DAutoScalingEnabled()) {
			return createAutoScaledImage();
		}
		return createImage();
	}

	/**
	 * Creates and returns an {@link Image} created as if at 100% zoom but scaled by
	 * the current zoom.
	 *
	 * @return The image if Draw2D-based auto-scaling is enabled.
	 */
	@SuppressWarnings("restriction")
    private Image createAutoScaledImage() {
		double scale = InternalDraw2dUtils.calculateScale(control);
		return createImageAtScale(scale);
	}

	/**
	 * Creates and returns an {@link Image} created at the current monitor zoom.
	 *
	 * @return The image if Draw2D-based auto-scaling is disabled.
	 */
	private Image createImage() {
		return createImageAtScale(1.0);
	}

	/**
	 * Creates and returns an {@link Image} capturing the contents of
	 * {@link #printSource}. The size of the image is determined by
	 * {@link #getImageSize()} and scaled by {@code scale} (if Draw2D-based scaling
	 * is enabled).
	 *
	 * @param scale The scaling factor. Only used if Draw2D-based scaling is enabled
	 * @return The created image.
	 */
	private Image createImageAtScale(double scale) {
		if (scale != 1.0 && !isDraw2DAutoScalingEnabled()) {
			throw new IllegalArgumentException("Scaling factor must be 1.0 if Draw2D-based scaling is disabled."); //$NON-NLS-1$
		}

		Dimension size100 = getImageSize();
		Dimension size = size100.getCopy();
		if (isDraw2DAutoScalingEnabled()) {
			// size.scale(scale) internally use Math.floor(), but SWT requires Math.round()
			size.width = (int) Math.round(size.width * scale);
			size.height = (int) Math.round(size.height * scale);
		}

		Image image = new Image(display, size.width, size.height);

		GC gc = new GC(image);
		if (isDraw2DAutoScalingEnabled()) {
			// Force image to be drawn as if at 100% zoom
			image.getImageData(100);
		}

		SWTGraphics graphics = new SWTGraphics(gc);
		if (isDraw2DAutoScalingEnabled()) {
			graphics.scale(scale);
		}

		try {
			preparePrintSource(graphics);
			printSource.paint(graphics);
		} finally {
			restorePrintSource(graphics);
		}

		graphics.dispose();
		gc.dispose();

		int monitorZoom = (int) (scale * 100);
		
		// Simulate new Image(display, imageData, monitorZoom)
		ImageData imageData = image.getImageData(100);
		image.dispose();
		// The image doesn't have image data for the current monitor zoom due to the
		// call to getImageData(100). This causes a blank image when painting and needs
		// to be corrected.
		Image imageTMP = new Image(display, (ImageDataProvider) zoom -> {
			if (zoom == monitorZoom) {
				return imageData;
			}
			if (zoom == 100) {
				// We must provide image data for 100% zoom, even if unused
				return imageData.scaledTo(size100.width, size100.height);
			}
			return null;
		});

		Image image100 = new Image(display, size100.width, size100.height);
		GC gc100 = new GC(image100);
		if (isDraw2DAutoScalingEnabled()) {
			// Force image to be drawn at monitor zoom
			image100.getImageData(monitorZoom);
		}
		gc100.setInterpolation(SWT.NONE);
		gc100.drawImage(imageTMP, 0, 0);
		gc100.dispose();
		imageTMP.dispose();

		return image100;
	}
}
