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

import org.eclipse.swt.widgets.Display;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.internal.GEFMessages;

/**
 * An action to perform direct editing on a selected part. The DirectEditAction
 * provides the function of sending a <code>Request</code> of the type
 * {@link RequestConstants#REQ_DIRECT_EDIT} to a single EditPart. The request
 * will be sent to the editpart using
 * {@link org.eclipse.gef.EditPart#performRequest(Request)}.
 * <P>
 * This action will be enabled whenever the current selection is one EditPart,
 * and that editpart returns <code>true</code> for
 * {@link EditPart#understandsRequest(Request)}, using the
 * {@link #getDirectEditRequest()}.
 * <P>
 * Clients can control the request that is sent by calling
 * {@link #setDirectEditRequest(Request)}. The
 * {@link org.eclipse.gef.requests.DirectEditRequest} class allows the client to
 * specify the <i>feature</i> that should be edited when invoked.
 * 
 * @author hudsonr
 */
public class DirectEditAction extends SelectionAction {

    /** @deprecated */
    public static final String ID = GEFActionConstants.DIRECT_EDIT;

    private Request directEditRequest = new Request(
            RequestConstants.REQ_DIRECT_EDIT);

    /**
     * Same as {@link #DirectEditAction(IWorkbenchPart)}.
     * 
     * @param editor
     *            the editor
     */
    public DirectEditAction(IEditorPart editor) {
        this((IWorkbenchPart) editor);
    }

    /**
     * Constructs a DirectEditAction using the specified part.
     * 
     * @param part
     *            the workbench part
     */
    public DirectEditAction(IWorkbenchPart part) {
        super(part);
    }

    /**
     * returns <code>true</code> if there is exactly 1 EditPart selected that
     * understand a request of type: {@link RequestConstants#REQ_DIRECT_EDIT}.
     * 
     * @return <code>true</code> if enabled
     */
    @Override
    protected boolean calculateEnabled() {
        if (getSelectedObjects().size() == 1
                && (getSelectedObjects().get(0) instanceof EditPart)) {
            EditPart part = (EditPart) getSelectedObjects().get(0);
            return part.understandsRequest(getDirectEditRequest());
        }
        return false;
    }

    /**
     * Returns the <code>Request</code> being used.
     * 
     * @return the request
     */
    protected Request getDirectEditRequest() {
        return directEditRequest;
    }

    /**
     * @see org.eclipse.jface.action.IAction#run()
     */
    @Override
    public void run() {
        try {
            EditPart part = (EditPart) getSelectedObjects().get(0);
            part.performRequest(getDirectEditRequest());
        } catch (ClassCastException e) {
            Display.getCurrent().beep();
        } catch (IndexOutOfBoundsException e) {
            Display.getCurrent().beep();
        }
    }

    /**
     * Clients may override the request that is used to perform the direct edit.
     * 
     * @param req
     *            the new request to be used
     */
    public void setDirectEditRequest(Request req) {
        directEditRequest = req;
    }

    /**
     * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#init()
     */
    @Override
    protected void init() {
        super.init();
        setText(GEFMessages.RenameAction_Label);
        setToolTipText(GEFMessages.RenameAction_Tooltip);
        setId(GEFActionConstants.DIRECT_EDIT);
    }

}
