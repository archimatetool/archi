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
package org.eclipse.gef.ui.actions;

import java.util.List;

import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.internal.GEFMessages;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.CreationFactory;

/**
 * If the current object on the clipboard is a valid template, this action will
 * paste the template to the viewer.
 * 
 * @author Eric Bordeau, Pratik Shah
 * @see org.eclipse.gef.ui.actions.CopyTemplateAction
 * @since 3.8
 */
public class PasteTemplateAction extends SelectionAction {

    /**
     * Constructor for PasteTemplateAction.
     * 
     * @param editor
     */
    public PasteTemplateAction(IWorkbenchPart editor) {
        super(editor);
    }

    /**
     * @return <code>true</code> if {@link #createPasteCommand()} returns an
     *         executable command
     * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
     */
    @Override
    protected boolean calculateEnabled() {
        // TODO Workaround for Bug 82622/39369. Should be removed when 39369 is
        // fixed.
        return true;
    }

    /**
     * Creates and returns a command (which may be <code>null</code>) to create
     * a new EditPart based on the template on the clipboard.
     * 
     * @return the paste command
     */
    @SuppressWarnings("rawtypes")
    protected Command createPasteCommand() {
        Command result = null;
        List selection = getSelectedObjects();
        if (selection != null && selection.size() == 1) {
            Object obj = selection.get(0);
            if (obj instanceof GraphicalEditPart) {
                GraphicalEditPart gep = (GraphicalEditPart) obj;
                Object template = getClipboardContents();
                if (template != null) {
                    CreationFactory factory = getFactory(template);
                    if (factory != null) {
                        CreateRequest request = new CreateRequest();
                        request.setFactory(factory);
                        request.setLocation(getPasteLocation(gep));
                        result = gep.getCommand(request);
                    }
                }
            }
        }
        return result;
    }

    /**
     * Returns the template on the clipboard, if there is one. Note that the
     * template on the clipboard might be from a palette from another type of
     * editor.
     * 
     * @return the clipboard's contents
     */
    protected Object getClipboardContents() {
        return Clipboard.getDefault().getContents();
    }

    /**
     * Returns the appropriate Factory object to be used for the specified
     * template. This Factory is used on the CreateRequest that is sent to the
     * target EditPart. Note that the given template might be from a palette for
     * a different GEF-based editor. In that case, this method can return
     * <code>null</code>.
     * 
     * @param template
     *            the template Object; it will never be <code>null</code>
     * @return a Factory
     */
    protected CreationFactory getFactory(Object template) {
        if (template instanceof CreationFactory)
            return (CreationFactory) template;
        return null;
    }

    /**
     * @param container
     *            the parent of the new part that is being pasted
     * @return the location at which to insert
     */
    protected Point getPasteLocation(GraphicalEditPart container) {
        Point result = new Point(10, 10);
        IFigure fig = container.getContentPane();
        result.translate(fig.getClientArea(Rectangle.SINGLETON).getLocation());
        fig.translateToAbsolute(result);
        return result;
    }

    /**
     * @see org.eclipse.gef.ui.actions.EditorPartAction#init()
     */
    @Override
    protected void init() {
        setId(ActionFactory.PASTE.getId());
        setText(GEFMessages.PasteAction_Label);
    }

    /**
     * Executes the command returned by {@link #createPasteCommand()}.
     */
    @Override
    public void run() {
        execute(createPasteCommand());
    }

}
