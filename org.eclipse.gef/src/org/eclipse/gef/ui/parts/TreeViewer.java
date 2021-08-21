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
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

import org.eclipse.jface.viewers.StructuredSelection;

import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.TreeEditPart;
import org.eclipse.gef.editparts.RootTreeEditPart;

/**
 * An EditPartViewer implementation based on a
 * {@link org.eclipse.swt.widgets.Tree}.
 * 
 * @author hudsonr
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class TreeViewer extends AbstractEditPartViewer {

    private boolean ignore = false;

    class EventDispatcher implements MouseListener, MouseMoveListener,
            KeyListener, MouseTrackListener, FocusListener {
        protected static final int ANY_BUTTON = SWT.BUTTON1 | SWT.BUTTON2
                | SWT.BUTTON3;

        @Override
        public void keyPressed(KeyEvent kee) {
            getEditDomain().keyDown(kee, TreeViewer.this);
        }

        @Override
        public void keyReleased(KeyEvent kee) {
            getEditDomain().keyUp(kee, TreeViewer.this);
        }

        @Override
        public void mouseDoubleClick(MouseEvent me) {
            getEditDomain().mouseDoubleClick(me, TreeViewer.this);
        }

        @Override
        public void mouseDown(MouseEvent me) {
            getEditDomain().mouseDown(me, TreeViewer.this);
        }

        @Override
        public void mouseEnter(MouseEvent me) {
            getEditDomain().viewerEntered(me, TreeViewer.this);
        }

        @Override
        public void mouseExit(MouseEvent me) {
            getEditDomain().viewerExited(me, TreeViewer.this);
        }

        @Override
        public void mouseHover(MouseEvent me) {
            getEditDomain().mouseHover(me, TreeViewer.this);
        }

        @Override
        public void mouseMove(MouseEvent me) {
            if ((me.stateMask & ANY_BUTTON) != 0)
                getEditDomain().mouseDrag(me, TreeViewer.this);
            else
                getEditDomain().mouseMove(me, TreeViewer.this);
        }

        @Override
        public void mouseUp(MouseEvent me) {
            getEditDomain().mouseUp(me, TreeViewer.this);
        }

        @Override
        public void focusGained(FocusEvent event) {
            getEditDomain().focusGained(event, TreeViewer.this);
        }

        @Override
        public void focusLost(FocusEvent event) {
            getEditDomain().focusLost(event, TreeViewer.this);
        }
    }

    private EventDispatcher dispatcher;

    /**
     * Constructs a TreeViewer with the default root editpart.
     */
    public TreeViewer() {
        dispatcher = new EventDispatcher();
        RootTreeEditPart rep = new RootTreeEditPart();
        setRootEditPart(rep);
        addDragSourceListener(new TreeViewerTransferDragListener(this));
        addDropTargetListener(new TreeViewerTransferDropListener(this));
    }

    /**
     * Creates the default tree and sets it as the control. The default styles
     * will show scrollbars as needed, and allows for multiple selection.
     * 
     * @param parent
     *            The parent for the Tree
     * @return the control
     */
    @Override
    public Control createControl(Composite parent) {
        Tree tree = new Tree(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
        setControl(tree);
        return tree;
    }

    /**
     * @see org.eclipse.gef.EditPartViewer#findObjectAtExcluding(Point,
     *      Collection, EditPartViewer.Conditional)
     */
    @Override
    public EditPart findObjectAtExcluding(Point pt, Collection exclude,
            Conditional condition) {
        if (getControl() == null)
            return null;

        Tree tree = (Tree) getControl();
        Rectangle area = tree.getClientArea();
        if (pt.x < area.x || pt.y < area.y || pt.x >= area.x + area.width
                || pt.y >= area.y + area.height)
            return null;

        EditPart result = null;
        TreeItem tie = tree.getItem(new org.eclipse.swt.graphics.Point(pt.x,
                pt.y));

        if (tie != null) {
            result = (EditPart) tie.getData();
        } else {
            result = (EditPart) tree.getData();
        }
        while (result != null) {
            if ((condition == null || condition.evaluate(result))
                    && !exclude.contains(result))
                return result;
            result = result.getParent();
        }
        return null;
    }

    /**
     * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#fireSelectionChanged()
     */
    @Override
    protected void fireSelectionChanged() {
        super.fireSelectionChanged();
        showSelectionInTree();
    }

    /**
     * "Hooks up" a Control, i.e. sets it as the control for the
     * RootTreeEditPart, adds necessary listener for proper operation, etc.
     */
    @Override
    protected void hookControl() {
        if (getControl() == null)
            return;

        final Tree tree = (Tree) getControl();
        tree.addFocusListener(dispatcher);
        tree.addMouseListener(dispatcher);
        tree.addMouseMoveListener(dispatcher);
        tree.addKeyListener(dispatcher);
        tree.addMouseTrackListener(dispatcher);
        tree.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TreeItem[] ties = tree.getSelection();
                Object newSelection[] = new Object[ties.length];
                for (int i = 0; i < ties.length; i++)
                    newSelection[i] = ties[i].getData();
                ignore = true;
                setSelection(new StructuredSelection(newSelection));
                ignore = false;
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        TreeEditPart tep = (TreeEditPart) getRootEditPart();
        tep.setWidget(tree);
        super.hookControl();
    }

    /**
     * @see org.eclipse.gef.ui.parts.AbstractEditPartViewer#reveal(org.eclipse.gef.EditPart)
     */
    @Override
    public void reveal(EditPart part) {
        if (!(part instanceof TreeEditPart))
            return;
        TreeEditPart treePart = (TreeEditPart) part;
        Tree tree = (Tree) getControl();
        Widget widget = treePart.getWidget();
        if (widget instanceof TreeItem)
            tree.showItem((TreeItem) widget);
    }

    private void showSelectionInTree() {
        if (ignore || getControl() == null || getControl().isDisposed())
            return;
        List selection = getSelectedEditParts();
        Tree tree = (Tree) getControl();
        List treeParts = new ArrayList();
        for (int i = 0; i < selection.size(); i++) {
            TreeEditPart part = (TreeEditPart) selection.get(i);
            if (part.getWidget() instanceof TreeItem)
                treeParts.add(part);
        }
        TreeItem[] treeItems = new TreeItem[treeParts.size()];
        for (int i = 0; i < treeParts.size(); i++) {
            TreeEditPart part = (TreeEditPart) treeParts.get(i);
            treeItems[i] = (TreeItem) part.getWidget();
        }
        tree.setSelection(treeItems);
    }

    /**
     * Unhooks a control so that it can be reset. This method deactivates the
     * contents, removes the Control as being the Control of the
     * RootTreeEditPart, etc. It does not remove the listeners because it is
     * causing errors, although that would be a desirable outcome.
     */
    @Override
    protected void unhookControl() {
        if (getControl() == null)
            return;
        super.unhookControl();
        // Ideally, you would want to remove the listeners here
        TreeEditPart tep = (TreeEditPart) getRootEditPart();
        tep.setWidget(null);
    }

}
