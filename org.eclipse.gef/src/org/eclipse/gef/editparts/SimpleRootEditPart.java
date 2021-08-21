/*******************************************************************************
 * Copyright (c) 2005, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.gef.editparts;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

/**
 * Default implementation of RootEditPart for GraphicalViewers.
 * 
 * @author Pratik Shah
 * @since 3.2
 */
public class SimpleRootEditPart extends AbstractGraphicalEditPart implements
        RootEditPart {

    private EditPart contents;
    private EditPartViewer viewer;

    /**
     * No editpolicies are installed on a RootEditPart by default.
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
     */
    @Override
    protected void createEditPolicies() {
    }

    /**
     * The default root figure is a figure with a stack layout.
     * 
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
     */
    @Override
    protected IFigure createFigure() {
        Figure figure = new Figure();
        figure.setLayoutManager(new StackLayout());
        return figure;
    }

    /**
     * The RootEditPart should never be asked for a command. This implementation
     * returns an unexecutable command.
     * 
     * @see EditPart#getCommand(Request)
     */
    @Override
    public Command getCommand(Request req) {
        return UnexecutableCommand.INSTANCE;
    }

    /**
     * @see RootEditPart#getContents()
     */
    @Override
    public EditPart getContents() {
        return contents;
    }

    /**
     * @see EditPart#getRoot()
     */
    @Override
    public RootEditPart getRoot() {
        return this;
    }

    /**
     * @see EditPart#getViewer()
     */
    @Override
    public EditPartViewer getViewer() {
        return viewer;
    }

    /**
     * Overridden to do nothing, child is set using setContents(EditPart)
     * 
     * @see AbstractEditPart#refreshChildren()
     */
    @Override
    protected void refreshChildren() {
    }

    /**
     * @see RootEditPart#setContents(EditPart)
     */
    @Override
    public void setContents(EditPart editpart) {
        if (contents == editpart)
            return;
        if (contents != null)
            removeChild(contents);
        contents = editpart;
        if (contents != null)
            addChild(contents, 0);
    }

    /**
     * @see RootEditPart#setViewer(EditPartViewer)
     */
    @Override
    public void setViewer(EditPartViewer newViewer) {
        if (viewer == newViewer)
            return;
        if (viewer != null)
            unregister();
        viewer = newViewer;
        if (viewer != null)
            register();
    }

}