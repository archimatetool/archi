/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;

import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.model.IBounds;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelObject;


/**
 * Utils for XML Exchange
 * 
 * @author Phillip Beauvoir
 */
public final class XMLExchangeUtils {
    
    /**
     * Calculate the overall negative offset for a diagram.
     * The exchange format diagram starts at origin 0,0 with no negative coordinates allowed.
     * Archi diagram nodes can have negative coordinates, so this is the offset to apply to nodes and bendpoints.
     * @param dm The diagram model
     * @return The Point offset
     */
    public static final Point getNegativeOffsetForDiagram(IDiagramModel dm) {
        Point extremePoint = new Point();
        
        for(IDiagramModelObject dmo : dm.getChildren()) {
            // Node bounds
            IBounds bounds = dmo.getBounds().getCopy();
            
            extremePoint.x = Math.min(bounds.getX(), extremePoint.x);
            extremePoint.y = Math.min(bounds.getY(), extremePoint.y);
            
            // Bendpoint bounds
            for(Iterator<EObject> iter = dmo.eAllContents(); iter.hasNext();) {
                EObject eObject = iter.next();
                // Connection
                if(eObject instanceof IDiagramModelConnection) {
                    IDiagramModelConnection connection = (IDiagramModelConnection)eObject;
                    
                    List<Point> points = DiagramModelUtils.getAbsoluteBendpointPositions(connection);
                    for(Point pt : points) {
                        extremePoint.x = Math.min(extremePoint.x, pt.x);
                        extremePoint.y = Math.min(extremePoint.y, pt.y);
                    }
                }
            }
        }
        
        return extremePoint;
    }
}
