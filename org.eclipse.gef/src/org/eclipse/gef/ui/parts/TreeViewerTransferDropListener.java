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
import java.util.Iterator;
import java.util.List;

import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.gef.requests.ChangeBoundsRequest;

@SuppressWarnings({"rawtypes", "unchecked"})
class TreeViewerTransferDropListener extends AbstractTransferDropTargetListener {

    public TreeViewerTransferDropListener(EditPartViewer viewer) {
        super(viewer, TreeViewerTransfer.getInstance());
        setEnablementDeterminedByCommand(true);
    }

    @Override
    protected Request createTargetRequest() {
        ChangeBoundsRequest request = new ChangeBoundsRequest(
                RequestConstants.REQ_MOVE);
        request.setEditParts((List) TreeViewerTransfer.getInstance()
                .getObject());
        return request;
    }

    @Override
    protected Command getCommand() {
        CompoundCommand command = new CompoundCommand();

        Iterator iter = ((List) TreeViewerTransfer.getInstance().getObject())
                .iterator();

        Request request = getTargetRequest();
        request.setType(isMove() ? RequestConstants.REQ_MOVE
                : RequestConstants.REQ_ORPHAN);

        while (iter.hasNext()) {
            EditPart editPart = (EditPart) iter.next();
            command.add(editPart.getCommand(request));
        }

        // If reparenting, add all editparts to target editpart.
        if (!isMove()) {
            request.setType(RequestConstants.REQ_ADD);
            if (getTargetEditPart() == null)
                command.add(UnexecutableCommand.INSTANCE);
            else
                command.add(getTargetEditPart().getCommand(getTargetRequest()));
        }
        return command;
    }

    protected String getCommandName() {
        if (isMove())
            return RequestConstants.REQ_MOVE;
        return RequestConstants.REQ_ADD;
    }

    @Override
    protected Collection getExclusionSet() {
        List selection = getViewer().getSelectedEditParts();
        List exclude = new ArrayList(selection);
        exclude.addAll(includeChildren(selection));
        return exclude;
    }

    @Override
    protected void handleDragOver() {
        if (TreeViewerTransfer.getInstance().getViewer() != getViewer()) {
            getCurrentEvent().detail = DND.DROP_NONE;
            return;
        }
        getCurrentEvent().feedback = DND.FEEDBACK_SCROLL | DND.FEEDBACK_EXPAND;
        super.handleDragOver();
    }

    protected EditPart getSourceEditPart() {
        List selection = (List) TreeViewerTransfer.getInstance().getObject();
        if (selection == null || selection.isEmpty()
                || !(selection.get(0) instanceof EditPart))
            return null;
        return (EditPart) selection.get(0);
    }

    protected List includeChildren(List list) {
        List result = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            List children = ((EditPart) list.get(i)).getChildren();
            result.addAll(children);
            result.addAll(includeChildren(children));
        }
        return result;
    }

    @Override
    public boolean isEnabled(DropTargetEvent event) {
        if (event.detail != DND.DROP_MOVE)
            return false;
        return super.isEnabled(event);
    }

    protected boolean isMove() {
        EditPart source = getSourceEditPart();
        List selection = (List) TreeViewerTransfer.getInstance().getObject();
        for (int i = 0; i < selection.size(); i++) {
            EditPart ep = (EditPart) selection.get(i);
            if (ep.getParent() != source.getParent())
                return false;
        }
        return source.getParent() == getTargetEditPart();
    }

    @Override
    protected void updateTargetRequest() {
        ChangeBoundsRequest request = (ChangeBoundsRequest) getTargetRequest();
        request.setLocation(getDropLocation());
        request.setType(getCommandName());
    }

}
