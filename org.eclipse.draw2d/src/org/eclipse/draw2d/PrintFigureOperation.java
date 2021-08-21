/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Sven Müller - Added tiling support
 *******************************************************************************/
package org.eclipse.draw2d;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Class responsible for printing Figures.
 * 
 * @author Dan Lee
 * @author Eric Bordeau
 * @author Sven Müller
 */
public class PrintFigureOperation extends PrintOperation {

    /**
     * The default print mode. Prints at 100% scale and tiles horizontally
     * and/or vertically, if necessary.
     */
    public static final int TILE = 1;
    /**
     * A print mode that scales the printer graphics so that the entire printed
     * image fits on one page.
     */
    public static final int FIT_PAGE = 2;
    /**
     * A print mode that scales the printer graphics so that the width of the
     * printed image fits on one page and tiles vertically, if necessary.
     */
    public static final int FIT_WIDTH = 3;
    /**
     * A print mode that scales the printer graphics so that the height of the
     * printed image fits on one page and tiles horizontally, if necessary.
     */
    public static final int FIT_HEIGHT = 4;

    private IFigure printSource;
    private Color oldBGColor;
    private int printMode = TILE;

    /**
     * Constructor for PrintFigureOperation.
     * <p>
     * Note: Descendants must call setPrintSource(IFigure) to set the IFigure
     * that is to be printed.
     * 
     * @see org.eclipse.draw2d.PrintOperation#PrintOperation(Printer)
     */
    protected PrintFigureOperation(Printer p) {
        super(p);
    }

    /**
     * Constructor for PrintFigureOperation.
     * 
     * @param p
     *            Printer to print on
     * @param srcFigure
     *            Figure to print
     */
    public PrintFigureOperation(Printer p, IFigure srcFigure) {
        super(p);
        setPrintSource(srcFigure);
    }

    /**
     * @return SWT.RIGHT_TO_LEFT if the print source is mirrored;
     *         SWT.LEFT_TO_RIGHT otherwise
     * @see org.eclipse.draw2d.PrintOperation#getGraphicsOrientation()
     */
    @Override
    int getGraphicsOrientation() {
        return getPrintSource().isMirrored() ? SWT.RIGHT_TO_LEFT
                : SWT.LEFT_TO_RIGHT;
    }

    /**
     * Returns the current print mode. The print mode is one of:
     * {@link #FIT_HEIGHT}, {@link #FIT_PAGE}, or {@link #FIT_WIDTH}.
     * 
     * @return the print mode
     */
    protected int getPrintMode() {
        return printMode;
    }

    /**
     * Returns the printSource.
     * 
     * @return IFigure The source IFigure
     */
    protected IFigure getPrintSource() {
        return printSource;
    }

    /**
     * @see org.eclipse.draw2d.PrintOperation#preparePrintSource()
     */
    @Override
    protected void preparePrintSource() {
        oldBGColor = getPrintSource().getLocalBackgroundColor();
        getPrintSource().setBackgroundColor(ColorConstants.white);
    }

    /**
     * Prints the pages based on the current print mode.
     * 
     * @see org.eclipse.draw2d.PrintOperation#printPages()
     */
    @Override
    protected void printPages() {
        Graphics graphics = getFreshPrinterGraphics();
        IFigure figure = getPrintSource();
        setupPrinterGraphicsFor(graphics, figure);
        Rectangle bounds = figure.getBounds();
        int x = bounds.x, y = bounds.y;
        Rectangle clipRect = new Rectangle();
        while (y < bounds.y + bounds.height) {
            while (x < bounds.x + bounds.width) {
                graphics.pushState();
                getPrinter().startPage();
                graphics.translate(-x, -y);
                graphics.getClip(clipRect);
                clipRect.setLocation(x, y);
                graphics.clipRect(clipRect);
                figure.paint(graphics);
                getPrinter().endPage();
                graphics.popState();
                x += clipRect.width;
            }
            x = bounds.x;
            y += clipRect.height;
        }
    }

    /**
     * @see org.eclipse.draw2d.PrintOperation#restorePrintSource()
     */
    @Override
    protected void restorePrintSource() {
        getPrintSource().setBackgroundColor(oldBGColor);
        oldBGColor = null;
    }

    /**
     * Sets the print mode. Possible values are {@link #TILE},
     * {@link #FIT_HEIGHT}, {@link #FIT_WIDTH} and {@link #FIT_PAGE}.
     * 
     * @param mode
     *            the print mode
     */
    public void setPrintMode(int mode) {
        printMode = mode;
    }

    /**
     * Sets the printSource.
     * 
     * @param printSource
     *            The printSource to set
     */
    protected void setPrintSource(IFigure printSource) {
        this.printSource = printSource;
    }

    /**
     * Sets up Graphics object for the given IFigure.
     * 
     * @param graphics
     *            The Graphics to setup
     * @param figure
     *            The IFigure used to setup graphics
     */
    protected void setupPrinterGraphicsFor(Graphics graphics, IFigure figure) {
        double dpiScale = (double) getPrinter().getDPI().x
                / Display.getCurrent().getDPI().x;

        Rectangle printRegion = getPrintRegion();
        // put the print region in display coordinates
        printRegion.width /= dpiScale;
        printRegion.height /= dpiScale;

        Rectangle bounds = figure.getBounds();
        double xScale = (double) printRegion.width / bounds.width;
        double yScale = (double) printRegion.height / bounds.height;
        switch (getPrintMode()) {
        case FIT_PAGE:
            graphics.scale(Math.min(xScale, yScale) * dpiScale);
            break;
        case FIT_WIDTH:
            graphics.scale(xScale * dpiScale);
            break;
        case FIT_HEIGHT:
            graphics.scale(yScale * dpiScale);
            break;
        default:
            graphics.scale(dpiScale);
        }
        graphics.setForegroundColor(figure.getForegroundColor());
        graphics.setBackgroundColor(figure.getBackgroundColor());
        graphics.setFont(figure.getFont());
    }

}