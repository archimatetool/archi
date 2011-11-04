/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.dnd;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.Request;
import org.eclipse.swt.dnd.Transfer;

/**
 * DDN Diagram Drop Request
 * 
 * @author Phillip Beauvoir
 */
public class DiagramDropRequest extends Request {

    private Object fData;
    private Point fPoint;
    private Transfer fTransferType;

    public static final String REQ_DIAGRAM_DROP = "$DiagramDropRequest";//$NON-NLS-1$

    public DiagramDropRequest(Transfer transferType) {
        super(REQ_DIAGRAM_DROP);
        fTransferType = transferType;
    }

    public Transfer getTransferType() {
        return fTransferType;
    }
    
    public Object getData() {
        return fData;
    }

    public void setData(Object data) {
        fData = data;
    }

    public void setDropLocation(Point point) {
        fPoint = point;
    }

    public Point getDropLocation() {
        return fPoint;
    }
}
