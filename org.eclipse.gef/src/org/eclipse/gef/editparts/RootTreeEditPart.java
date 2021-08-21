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

import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.TreeEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

/**
 * The root editpart for a <code>TreeViewer</code>. There is limited control of
 * a Tree, so this root implementation should work for all purposes. This
 * implementation does little more than hold onto the viewer, and pass the
 * <code>Tree</code> to the contents as its widget.
 * 
 * @author hudsonr
 */
public class RootTreeEditPart extends
        org.eclipse.gef.editparts.AbstractEditPart implements RootEditPart,
        TreeEditPart {

    private EditPartViewer viewer;
    private Widget widget;
    private TreeEditPart contents;

    /**
     * This is where the child gets added. No TreeItem is needed here because
     * the contents is actually represented by the Tree iteself.
     * 
     * @param childEditPart
     *            EditPart of child to be added.
     * @param index
     *            Position where it is to be added.
     */
    @Override
    protected void addChildVisual(EditPart childEditPart, int index) {
        ((TreeEditPart) childEditPart).setWidget(widget);
    }

    /**
     * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
     */
    @Override
    protected void createEditPolicies() {
    }

    /**
     * @see org.eclipse.gef.EditPart#getCommand(org.eclipse.gef.Request)
     */
    @Override
    public Command getCommand(Request request) {
        return UnexecutableCommand.INSTANCE;
    }

    /**
     * @see org.eclipse.gef.RootEditPart#getContents()
     */
    @Override
    public EditPart getContents() {
        return contents;
    }

    /**
     * This method will never be called on a tree root.
     * 
     * @see org.eclipse.gef.EditPart#getDragTracker(org.eclipse.gef.Request)
     */
    @Override
    public DragTracker getDragTracker(Request request) {
        return null;
    }

    /**
     * Returns <code>this</code>.
     * 
     * @see org.eclipse.gef.EditPart#getRoot()
     */
    @Override
    public RootEditPart getRoot() {
        return this;
    }

    /**
     * @see org.eclipse.gef.RootEditPart#getViewer()
     */
    @Override
    public EditPartViewer getViewer() {
        return viewer;
    }

    /**
     * The editpart holds onto the SWT Tree, which is also the contents' widget.
     * 
     * @see org.eclipse.gef.TreeEditPart#getWidget()
     */
    @Override
    public Widget getWidget() {
        return widget;
    }

    /**
     * Overridden to do nothing since the child is explicitly set.
     * 
     * @see org.eclipse.gef.editparts.AbstractEditPart#refreshChildren()
     */
    @Override
    protected void refreshChildren() {
    }

    /**
     * This is where the child gets removed. This method is overridden here so
     * that the AbstractTreeEditPart does not dispose the widget, which is the
     * Tree in this case. The tree is owned by the viewer, not the child.
     * 
     * @param childEditPart
     *            EditPart of child to be removed.
     */
    @Override
    protected void removeChildVisual(EditPart childEditPart) {
        ((TreeEditPart) childEditPart).setWidget(null);
    }

    /**
     * @see org.eclipse.gef.RootEditPart#setContents(org.eclipse.gef.EditPart)
     */
    @Override
    public void setContents(EditPart editpart) {
        if (contents != null) {
            if (getWidget() != null)
                ((Tree) getWidget()).removeAll();
            removeChild(contents);
        }
        contents = (TreeEditPart) editpart;

        if (contents != null)
            addChild(contents, -1);
    }

    /**
     * @see org.eclipse.gef.RootEditPart#setViewer(org.eclipse.gef.EditPartViewer)
     */
    @Override
    public void setViewer(EditPartViewer epviewer) {
        viewer = epviewer;
    }

    /**
     * Called by <code>TreeViewer</code> to set the <code>Tree</code> into the
     * root. The root simply holds onto this widget and passes it to the
     * contents when the contents is added.
     * 
     * @see org.eclipse.gef.TreeEditPart#setWidget(org.eclipse.swt.widgets.Widget)
     */
    @Override
    public void setWidget(Widget w) {
        widget = w;
        if (contents != null)
            contents.setWidget(w);
    }

}
