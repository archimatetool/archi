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
package org.eclipse.gef.tools;

import java.lang.reflect.Constructor;

import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.AncestorListener;
import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.requests.DirectEditRequest;

/**
 * Manages the direct edit operation by creating and maintaining the
 * {@link org.eclipse.jface.viewers.CellEditor} and executing the resulting
 * command if the cell editor value has changed.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class DirectEditManager {

    private static final Color BLUE = ColorConstants.menuBackgroundSelected;
    private static final Border BORDER_FRAME = new DirectEditBorder();

    private AncestorListener ancestorListener;
    private EditPartListener editPartListener;
    private ControlListener controlListener;
    private IFigure cellEditorFrame;
    private ICellEditorListener cellEditorListener;
    private boolean showingFeedback;
    private boolean dirty;
    private DirectEditRequest request;
    private CellEditorLocator locator;
    private GraphicalEditPart source;
    private CellEditor ce;
    private Class editorType;
    private boolean committing = false;
    private Object feature;

    /**
     * Constructs a new DirectEditManager for the given source edit part. The
     * cell editor will be created by instantiating the type <i>editorType</i>.
     * The cell editor will be placed using the given CellEditorLocator.
     * 
     * @param source
     *            the source edit part
     * @param editorType
     *            the cell editor type
     * @param locator
     *            the locator
     */
    public DirectEditManager(GraphicalEditPart source, Class editorType,
            CellEditorLocator locator) {
        this.source = source;
        this.locator = locator;
        this.editorType = editorType;
    }

    /**
     * Constructs a new DirectEditManager for the given source edit part. The
     * cell editor will be created by instantiating the type <i>editorType</i>.
     * The cell editor will be placed using the given CellEditorLocator.
     * 
     * @param source
     *            the source edit part
     * @param editorType
     *            the cell editor type
     * @param locator
     *            the locator
     * @param feature
     *            If the EditPart supports direct editing of multiple features,
     *            this parameter can be used to discriminate among them.
     * @since 3.2
     */
    public DirectEditManager(GraphicalEditPart source, Class editorType,
            CellEditorLocator locator, Object feature) {
        this(source, editorType, locator);
        this.feature = feature;
    }

    /**
     * Cleanup is done here. Any feedback is erased and listeners unhooked. If
     * the cell editor is not <code>null</code>, it will be
     * {@link CellEditor#deactivate() deativated}, {@link CellEditor#dispose()
     * disposed}, and set to <code>null</code>.
     */
    protected void bringDown() {
        eraseFeedback();
        unhookListeners();
        if (getCellEditor() != null) {
            getCellEditor().deactivate();
            getCellEditor().dispose();
            setCellEditor(null);
        }
        request = null;
        dirty = false;
    }

    /**
     * Commits the current value of the cell editor by getting a {@link Command}
     * from the source edit part and executing it via the {@link CommandStack}.
     * Finally, {@link #bringDown()} is called to perform and necessary cleanup.
     */
    protected void commit() {
        if (committing)
            return;
        committing = true;
        try {
            eraseFeedback();
            if (isDirty()) {
                CommandStack stack = getEditPart().getViewer().getEditDomain()
                        .getCommandStack();
                stack.execute(getEditPart().getCommand(getDirectEditRequest()));
            }
        } finally {
            bringDown();
            committing = false;
        }
    }

    /**
     * Creates the cell editor on the given composite. The cell editor is
     * created by instantiating the cell editor type passed into this
     * DirectEditManager's constuctor.
     * 
     * @param composite
     *            the composite to create the cell editor on
     * @return the newly created cell editor
     */
    protected CellEditor createCellEditorOn(Composite composite) {
        try {
            Constructor constructor = editorType
                    .getConstructor(new Class[] { Composite.class });
            return (CellEditor) constructor
                    .newInstance(new Object[] { composite });
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Creates and returns the DirectEditRequest.
     * 
     * @return the direct edit request
     */
    protected DirectEditRequest createDirectEditRequest() {
        DirectEditRequest req = new DirectEditRequest();
        req.setCellEditor(getCellEditor());
        req.setDirectEditFeature(getDirectEditFeature());
        return req;
    }

    /**
     * Asks the source edit part to erase source feedback.
     */
    protected void eraseFeedback() {
        if (showingFeedback) {
            LayerManager.Helper.find(getEditPart())
                    .getLayer(LayerConstants.FEEDBACK_LAYER)
                    .remove(getCellEditorFrame());
            cellEditorFrame = null;
            getEditPart().eraseSourceFeedback(getDirectEditRequest());
            showingFeedback = false;
        }
    }

    /**
     * Returns the cell editor.
     * 
     * @return the cell editor
     */
    protected CellEditor getCellEditor() {
        return ce;
    }

    /**
     * Returns the IFigure, which is used to 'embed' the cell editor control.
     * 
     * @return An {@link IFigure} to be used as frame around the cell editor
     *         control.
     * @since 3.9
     */
    protected IFigure getCellEditorFrame() {
        if (cellEditorFrame != null)
            return cellEditorFrame;
        cellEditorFrame = new Figure();
        cellEditorFrame.setBorder(BORDER_FRAME);
        return cellEditorFrame;
    }

    private Control getControl() {
        return ce.getControl();
    }

    /**
     * @return <code>Object</code> that can be used if the EditPart supports
     *         direct editing of multiple features, this parameter can be used
     *         to discriminate among them.
     * @since 3.2
     */
    protected Object getDirectEditFeature() {
        return feature;
    }

    /**
     * Returns the direct edit request, creating it if needed.
     * 
     * @return the direct edit request
     */
    protected DirectEditRequest getDirectEditRequest() {
        if (request == null)
            request = createDirectEditRequest();
        return request;
    }

    /**
     * Returns the source edit part.
     * 
     * @return the source edit part
     */
    protected GraphicalEditPart getEditPart() {
        return source;
    }

    protected CellEditorLocator getLocator() {
        return locator;
    }

    protected void handleValueChanged() {
        setDirty(true);
        showFeedback();
        placeCellEditor();
    }

    protected void hookListeners() {
        ancestorListener = new AncestorListener.Stub() {
            @Override
            public void ancestorMoved(IFigure ancestor) {
                placeCellEditor();
            }
        };
        getEditPart().getFigure().addAncestorListener(ancestorListener);

        Control control = getControl();

        controlListener = new ControlAdapter() {
            @Override
            public void controlMoved(ControlEvent e) {
                // This must be handled async because during scrolling, the
                // CellEditor moves
                // first, but then afterwards the viewport Scrolls, which would
                // cause the
                // shadow to move twice
                Display.getCurrent().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        placeCellEditorFrame();
                    }
                });
            }

            @Override
            public void controlResized(ControlEvent e) {
                placeCellEditorFrame();
            }
        };
        control.addControlListener(controlListener);

        cellEditorListener = new ICellEditorListener() {
            @Override
            public void applyEditorValue() {
                commit();
            }

            @Override
            public void cancelEditor() {
                bringDown();
            }

            @Override
            public void editorValueChanged(boolean old, boolean newState) {
                handleValueChanged();
            }
        };
        getCellEditor().addListener(cellEditorListener);

        editPartListener = new EditPartListener.Stub() {
            @Override
            public void partDeactivated(EditPart editpart) {
                bringDown();
            }
        };
        getEditPart().addEditPartListener(editPartListener);
    }

    /**
     * Initializes the cell editor. Subclasses should implement this to set the
     * initial text and add things such as {@link VerifyListener
     * VerifyListeners}, if needed.
     */
    protected abstract void initCellEditor();

    /**
     * Returns <code>true</code> if the cell editor's value has been changed.
     * 
     * @return <code>true</code> if the cell editor is dirty
     */
    protected boolean isDirty() {
        return dirty;
    }

    private void placeCellEditorFrame() {
        if (showingFeedback) {
            IFigure shadow = getCellEditorFrame();
            Rectangle rect = new Rectangle(getCellEditor().getControl()
                    .getBounds());
            rect.expand(shadow.getInsets());
            shadow.translateToRelative(rect);
            shadow.setBounds(rect);
        }
    }

    private void placeCellEditor() {
        getLocator().relocate(getCellEditor());
    }

    /**
     * Sets the cell editor to the given editor.
     * 
     * @param editor
     *            the cell editor
     */
    protected void setCellEditor(CellEditor editor) {
        ce = editor;
        if (ce == null)
            return;
        hookListeners();
    }

    /**
     * Sets the dirty property.
     * 
     * @param value
     *            the dirty property
     */
    protected void setDirty(boolean value) {
        dirty = value;
    }

    /**
     * Sets the source edit part.
     * 
     * @param source
     *            the source edit part
     */
    protected void setEditPart(GraphicalEditPart source) {
        this.source = source;
        // source.addEditPartListener();
    }

    /**
     * Sets the CellEditorLocator used to place the cell editor in the correct
     * location.
     * 
     * @param locator
     *            the locator
     */
    public void setLocator(CellEditorLocator locator) {
        this.locator = locator;
    }

    /**
     * Shows the cell editor when direct edit is started. Calls
     * {@link #initCellEditor()}, {@link CellEditor#activate()}, and
     * {@link #showFeedback()}.
     */
    public void show() {
        if (getCellEditor() != null)
            return;
        Composite composite = (Composite) source.getViewer().getControl();
        setCellEditor(createCellEditorOn(composite));
        if (getCellEditor() == null)
            return;
        initCellEditor();
        getCellEditor().activate();
        placeCellEditor();
        getControl().setVisible(true);
        getCellEditor().setFocus();
        showFeedback();
    }

    private void showCellEditorFrame() {
        LayerManager.Helper.find(getEditPart())
                .getLayer(LayerConstants.FEEDBACK_LAYER)
                .add(getCellEditorFrame());
        placeCellEditorFrame();
    }

    /**
     * Asks the source edit part to show source feedback.
     */
    public void showFeedback() {
        if (!showingFeedback)
            showCellEditorFrame();
        showingFeedback = true;
        showCellEditorFrame();
        getEditPart().showSourceFeedback(getDirectEditRequest());
    }

    /**
     * Unhooks listeners. Called from {@link #bringDown()}.
     */
    protected void unhookListeners() {
        getEditPart().getFigure().removeAncestorListener(ancestorListener);
        getEditPart().removeEditPartListener(editPartListener);
        ancestorListener = null;
        editPartListener = null;

        if (getCellEditor() == null)
            return;
        getCellEditor().removeListener(cellEditorListener);
        cellEditorListener = null;

        Control control = getCellEditor().getControl();
        if (control == null || control.isDisposed())
            return;
        control.removeControlListener(controlListener);
        controlListener = null;
    }

    /**
     * The border used to 'frame' the cell editor.
     * 
     * @since 3.9
     */
    protected static class DirectEditBorder extends AbstractBorder {
        private static final Insets insets = new Insets(1, 2, 2, 2);

        @Override
        public Insets getInsets(IFigure figure) {
            return insets;
        }

        @Override
        public void paint(IFigure figure, Graphics graphics, Insets insets) {
            Rectangle rect = getPaintRectangle(figure, insets);
            graphics.setForegroundColor(ColorConstants.white);
            graphics.drawLine(rect.x, rect.y, rect.x, rect.bottom());
            rect.x++;
            rect.width--;
            rect.resize(-1, -1);
            graphics.setForegroundColor(ColorConstants.black);
            graphics.drawLine(rect.x + 2, rect.bottom(), rect.right(),
                    rect.bottom());
            graphics.drawLine(rect.right(), rect.bottom(), rect.right(),
                    rect.y + 2);

            rect.resize(-1, -1);
            graphics.setForegroundColor(BLUE);
            graphics.drawRectangle(rect);
        }
    }

}
