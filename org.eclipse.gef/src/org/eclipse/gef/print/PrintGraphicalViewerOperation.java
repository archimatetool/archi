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
package org.eclipse.gef.print;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.printing.Printer;

import org.eclipse.jface.viewers.StructuredSelection;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PrintFigureOperation;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;

/**
 * @author danlee
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class PrintGraphicalViewerOperation extends PrintFigureOperation {

    private GraphicalViewer viewer;
    private List selectedEditParts;

    /**
     * Constructor for PrintGraphicalViewerOperation
     * 
     * @param p
     *            The Printer to print to
     * @param g
     *            The viewer containing what is to be printed NOTE: The
     *            GraphicalViewer to be printed must have a
     *            {@link org.eclipse.draw2d.Layer Layer} with the
     *            {@link org.eclipse.gef.LayerConstants PRINTABLE_LAYERS} key.
     */
    public PrintGraphicalViewerOperation(Printer p, GraphicalViewer g) {
        super(p);
        viewer = g;
        LayerManager lm = (LayerManager) viewer.getEditPartRegistry().get(
                LayerManager.ID);
        IFigure f = lm.getLayer(LayerConstants.PRINTABLE_LAYERS);
        setPrintSource(f);
    }

    /**
     * Returns the viewer.
     * 
     * @return GraphicalViewer
     */
    public GraphicalViewer getViewer() {
        return viewer;
    }

    /**
     * @see org.eclipse.draw2d.PrintOperation#preparePrintSource()
     */
    @Override
    protected void preparePrintSource() {
        super.preparePrintSource();
        selectedEditParts = new ArrayList(viewer.getSelectedEditParts());
        viewer.deselectAll();
    }

    /**
     * @see org.eclipse.draw2d.PrintOperation#restorePrintSource()
     */
    @Override
    protected void restorePrintSource() {
        super.restorePrintSource();
        viewer.setSelection(new StructuredSelection(selectedEditParts));
    }

    /**
     * Sets the viewer.
     * 
     * @param viewer
     *            The viewer to set
     */
    public void setViewer(GraphicalViewer viewer) {
        this.viewer = viewer;
    }

}
