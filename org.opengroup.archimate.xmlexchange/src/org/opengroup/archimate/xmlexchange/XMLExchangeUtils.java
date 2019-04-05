/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;

import com.archimatetool.model.IBounds;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelBendpoint;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelObject;


/**
 * Utils for XML Exchange
 * 
 * @author Phillip Beauvoir
 */
public final class XMLExchangeUtils {
    
    /**
     * 
     * TODO for connections
     * @param dmc
     * @return
     */
    public static final IBounds getAbsoluteBounds(IDiagramModelComponent dmc) {
        if(dmc instanceof IDiagramModelObject) {
            return getAbsoluteBounds((IDiagramModelObject)dmc);
        }
        
        // TODO - how to calculate the bounds from this?????
        else if(dmc instanceof IDiagramModelConnection) {
            
        }
        
        return null;
    }

    /**
     * Return the absolute bounds of a diagram model object
     * @param dmo The DiagramModelObject
     * @return The absolute bounds of a diagram model object
     */
    public static final IBounds getAbsoluteBounds(IDiagramModelObject dmo) {
        IBounds bounds = dmo.getBounds().getCopy();
        
        EObject container = dmo.eContainer();
        while(container instanceof IDiagramModelObject) {
            IDiagramModelObject parent = (IDiagramModelObject)container;
            IBounds parentBounds = parent.getBounds().getCopy();
            
            bounds.setX(bounds.getX() + parentBounds.getX());
            bounds.setY(bounds.getY() + parentBounds.getY());
            
            container = container.eContainer();
        }

        return bounds;
    }

    /**
     * Convert the given absolute bounds to the relative bounds in relation to a parent IDiagramModelObject
     * @param absoluteBounds The absolute bounds
     * @param dmo The DiagramModelObject that is the parent into which we want to get the relative bounds for
     * @return the relative bounds of a diagram model object
     */
    public static final IBounds getRelativeBounds(IBounds absoluteBounds, IDiagramModelObject parent) {
        IBounds bounds = absoluteBounds.getCopy();
        
        do {
            IBounds parentBounds = parent.getBounds();
            
            bounds.setX(bounds.getX() - parentBounds.getX());
            bounds.setY(bounds.getY() - parentBounds.getY());
            
            parent = (parent.eContainer() instanceof IDiagramModelObject) ? (IDiagramModelObject)parent.eContainer() : null;
        }
        while(parent != null);

        return bounds;
    }
    
    /**
     * For exporting get the actual bendpoint positions
     * @param connection
     * @return
     */
    public static List<Point> getActualBendpointPositions(IDiagramModelConnection connection) {
        List<Point> points = new ArrayList<Point>();
        
        // TODO: Doesn't work for connection->connection
        if(connection.getSource() instanceof IDiagramModelConnection || connection.getTarget() instanceof IDiagramModelConnection) {
            return points;
        }
        
        double bpindex = 1; // index count + 1
        double bpcount = connection.getBendpoints().size() + 1; // number of bendpoints + 1
        
        for(IDiagramModelBendpoint bendpoint : connection.getBendpoints()) {
            // The weight of this Bendpoint should use to calculate its location.
            // The weight should be between 0.0 and 1.0. A weight of 0.0 will
            // cause the Bendpoint to follow the start point, while a weight
            // of 1.0 will cause the Bendpoint to follow the end point
            double bpweight = bpindex / bpcount;
            
            IBounds srcBounds = XMLExchangeUtils.getAbsoluteBounds(connection.getSource()); // get bounds of source node
            double startX = (srcBounds.getX() + (srcBounds.getWidth() / 2)) + bendpoint.getStartX();
            startX *= (1.0 - bpweight);
            double startY = (srcBounds.getY() + (srcBounds.getHeight() / 2)) + bendpoint.getStartY();
            startY *= (1.0 - bpweight);
            
            IBounds tgtBounds = XMLExchangeUtils.getAbsoluteBounds(connection.getTarget()); // get bounds of target node
            double endX = (tgtBounds.getX() + (tgtBounds.getWidth() / 2)) + bendpoint.getEndX();
            endX *= bpweight;
            double endY = (tgtBounds.getY() + (tgtBounds.getHeight() / 2)) + bendpoint.getEndY();
            endY *= bpweight;
            
            int x = (int)(startX + endX);
            int y = (int)(startY + endY);
            
            points.add(new Point(x, y));
            
            bpindex++;
        }
        
        return points;
    }
    
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
                    
                    List<Point> points = getActualBendpointPositions(connection);
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
