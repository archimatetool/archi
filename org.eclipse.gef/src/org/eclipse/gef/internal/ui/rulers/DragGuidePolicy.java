/*******************************************************************************
 * Copyright (c) 2003, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.internal.ui.rulers;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * @author Pratik Shah
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class DragGuidePolicy extends GraphicalEditPolicy {

    private List attachedEditParts = null;
    private IFigure dummyGuideFigure, dummyLineFigure;
    private boolean dragInProgress = false;

    protected IFigure createDummyLineFigure() {
        return new Figure();
    }

    protected GuideFigure createDummyGuideFigure() {
        return new GuidePlaceHolder(getGuideEditPart().isHorizontal());
    }

    /*
     * Fix for Bug# 65885 If you undo guide creation while dragging that guide,
     * it was leaving behind drag feedback. This was because by the time
     * eraseSourceFeedback() was being called, the guide edit part had been
     * deactivated (and hence eraseSourceFeedback is never called on this
     * policy). So we make sure that this policy cleans up when it is
     * deactivated.
     */
    @Override
    public void deactivate() {
        removeFeedback();
        super.deactivate();
    }

    private void eraseAttachedPartsFeedback(Request request) {
        if (attachedEditParts != null) {
            ChangeBoundsRequest req = new ChangeBoundsRequest(request.getType());
            req.setEditParts(attachedEditParts);

            Iterator i = attachedEditParts.iterator();

            while (i.hasNext())
                ((EditPart) i.next()).eraseSourceFeedback(req);
            attachedEditParts = null;
        }
    }

    @Override
    public void eraseSourceFeedback(Request request) {
        getGuideEditPart().updateLocationOfFigures(
                getGuideEditPart().getZoomedPosition());
        getHostFigure().setVisible(true);
        getGuideEditPart().getGuideLineFigure().setVisible(true);
        removeFeedback();
        getGuideEditPart().setCurrentCursor(null);
        dragInProgress = false;

        eraseAttachedPartsFeedback(request);
    }

    private List getAttachedEditParts() {
        if (attachedEditParts == null)
            attachedEditParts = getGuideEditPart().getRulerProvider()
                    .getAttachedEditParts(
                            getHost().getModel(),
                            ((RulerEditPart) getHost().getParent())
                                    .getDiagramViewer());
        return attachedEditParts;
    }

    @Override
    public Command getCommand(Request request) {
        Command cmd;
        final ChangeBoundsRequest req = (ChangeBoundsRequest) request;
        if (isDeleteRequest(req)) {
            cmd = getGuideEditPart().getRulerProvider().getDeleteGuideCommand(
                    getHost().getModel());
        } else {
            int pDelta;
            if (getGuideEditPart().isHorizontal()) {
                pDelta = req.getMoveDelta().y;
            } else {
                pDelta = req.getMoveDelta().x;
            }
            if (isMoveValid(getGuideEditPart().getZoomedPosition() + pDelta)) {
                ZoomManager zoomManager = getGuideEditPart().getZoomManager();
                if (zoomManager != null) {
                    pDelta = (int) Math.round(pDelta / zoomManager.getZoom());
                }
                cmd = getGuideEditPart().getRulerProvider()
                        .getMoveGuideCommand(getHost().getModel(), pDelta);
            } else {
                cmd = UnexecutableCommand.INSTANCE;
            }
        }
        return cmd;
    }

    protected IFigure getDummyGuideFigure() {
        if (dummyGuideFigure == null) {
            dummyGuideFigure = createDummyGuideFigure();
        }
        return dummyGuideFigure;
    }

    protected IFigure getDummyLineFigure() {
        if (dummyLineFigure == null) {
            dummyLineFigure = createDummyLineFigure();
        }
        return dummyLineFigure;
    }

    protected GuideEditPart getGuideEditPart() {
        return (GuideEditPart) getHost();
    }

    protected boolean isDeleteRequest(ChangeBoundsRequest req) {
        int pos, max, min;
        if (getGuideEditPart().isHorizontal()) {
            pos = req.getLocation().x;
            Rectangle zone = getHostFigure().getBounds().getExpanded(
                    GuideEditPart.DELETE_THRESHOLD, 0);
            min = zone.x;
            max = min + zone.width;
        } else {
            pos = req.getLocation().y;
            Rectangle zone = getHostFigure().getBounds().getExpanded(0,
                    GuideEditPart.DELETE_THRESHOLD);
            min = zone.y;
            max = min + zone.height;
        }
        return pos < min || pos > max;
    }

    protected boolean isMoveValid(int zoomedPosition) {
        boolean result = true;
        ZoomManager zoomManager = getGuideEditPart().getZoomManager();
        int position = zoomedPosition;
        if (zoomManager != null) {
            position = (int) Math.round(position / zoomManager.getZoom());
        }
        Iterator guides = getGuideEditPart().getRulerProvider().getGuides()
                .iterator();
        while (guides.hasNext()) {
            Object guide = guides.next();
            if (guide != getGuideEditPart().getModel()) {
                int guidePos = getGuideEditPart().getRulerProvider()
                        .getGuidePosition(guide);
                if (Math.abs(guidePos - position) < GuideEditPart.MIN_DISTANCE_BW_GUIDES) {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    private void removeFeedback() {
        if (getDummyGuideFigure().getParent() != null) {
            getDummyGuideFigure().getParent().remove(getDummyGuideFigure());
        }
        if (getDummyLineFigure().getParent() != null) {
            getDummyLineFigure().getParent().remove(getDummyLineFigure());
        }
    }

    private void showAttachedPartsFeedback(ChangeBoundsRequest request) {
        ChangeBoundsRequest req = new ChangeBoundsRequest(request.getType());
        req.setEditParts(getAttachedEditParts());

        if (getGuideEditPart().isHorizontal())
            req.setMoveDelta(new Point(0, request.getMoveDelta().y));
        else
            req.setMoveDelta(new Point(request.getMoveDelta().x, 0));

        Iterator i = getAttachedEditParts().iterator();

        while (i.hasNext())
            ((EditPart) i.next()).showSourceFeedback(req);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void showSourceFeedback(Request request) {
        if (!dragInProgress) {
            dragInProgress = true;
            // add the placeholder guide figure to the ruler
            getHostFigure().getParent().add(getDummyGuideFigure(), 0);
            ((GraphicalEditPart) getHost().getParent()).setLayoutConstraint(
                    getHost(), getDummyGuideFigure(), new Integer(
                            getGuideEditPart().getZoomedPosition()));
            getDummyGuideFigure().setBounds(getHostFigure().getBounds());
            getDummyGuideFigure().validate();
            // add the invisible placeholder line figure to the primary viewer
            getGuideEditPart().getGuideLayer().add(getDummyLineFigure(), 0);
            getGuideEditPart().getGuideLayer().setConstraint(
                    getDummyLineFigure(),
                    new Boolean(getGuideEditPart().isHorizontal()));
            getDummyLineFigure().setBounds(
                    getGuideEditPart().getGuideLineFigure().getBounds());
            getDummyLineFigure().validate();
            // move the guide being dragged to the last index so that it's drawn
            // on
            // top of other guides
            List children = getHostFigure().getParent().getChildren();
            children.remove(getHostFigure());
            children.add(getHostFigure());
        }
        ChangeBoundsRequest req = (ChangeBoundsRequest) request;
        if (isDeleteRequest(req)) {
            getHostFigure().setVisible(false);
            getGuideEditPart().getGuideLineFigure().setVisible(false);
            getGuideEditPart().setCurrentCursor(Cursors.ARROW);
            eraseAttachedPartsFeedback(request);
        } else {
            int newPosition;
            if (getGuideEditPart().isHorizontal()) {
                newPosition = getGuideEditPart().getZoomedPosition()
                        + req.getMoveDelta().y;
            } else {
                newPosition = getGuideEditPart().getZoomedPosition()
                        + req.getMoveDelta().x;
            }
            getHostFigure().setVisible(true);
            getGuideEditPart().getGuideLineFigure().setVisible(true);
            if (isMoveValid(newPosition)) {
                getGuideEditPart().setCurrentCursor(null);
                getGuideEditPart().updateLocationOfFigures(newPosition);
                showAttachedPartsFeedback(req);
            } else {
                getGuideEditPart().setCurrentCursor(Cursors.NO);
                getGuideEditPart().updateLocationOfFigures(
                        getGuideEditPart().getZoomedPosition());
                eraseAttachedPartsFeedback(request);
            }
        }
    }

    @Override
    public boolean understandsRequest(Request req) {
        return req.getType().equals(REQ_MOVE);
    }

}
