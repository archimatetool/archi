/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.sketch.policies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.archimatetool.editor.diagram.commands.AddDiagramModelReferenceCommand;
import com.archimatetool.editor.diagram.dnd.AbstractDNDEditPolicy;
import com.archimatetool.editor.diagram.dnd.DiagramDropRequest;
import com.archimatetool.model.IDiagramModel;



/**
 * A policy to handle a Skectch View's DND commands
 * 
 * @author Phillip Beauvoir
 */
public class SketchDNDEditPolicy extends AbstractDNDEditPolicy {
    
    @Override
    protected Command getDropCommand(DiagramDropRequest request) {
        if(!(request.getData() instanceof IStructuredSelection)) {
            return null;
        }
        
        // XY drop point
        Point pt = getDropLocation(request);

        // Origin
        int origin = pt.x;
        int x = pt.x;
        int y = pt.y;

        // Gather an actual list of elements dragged onto the container, omitting duplicates and anything already on the diagram
        Object[] objects = ((IStructuredSelection)request.getData()).toArray();
        List<IDiagramModel> list = getDiagramRefsToAdd(objects);

        // Compound Command
        CompoundCommand result = new CompoundCommand(Messages.SketchDNDEditPolicy_0);
        
        for(IDiagramModel diagramModel : list) {
            result.add(new AddDiagramModelReferenceCommand(getTargetContainer(), diagramModel, x, y));
            
            x += 150;
            if(x > origin + 400) {
                x = origin;
                y += 100;
            }
        }
        
        return result;
    }
    
    /**
     * Gather the elements that will be added to the diagram
     */
    protected List<IDiagramModel> getDiagramRefsToAdd(Object[] objects) {
        List<IDiagramModel> list = new ArrayList<IDiagramModel>();
        
        for(Object object : objects) {
            // Selected Diagram Models (References)
            if(object instanceof IDiagramModel && object != getTargetDiagramModel()) { // not the same diagram
                if(!list.contains(object)) {
                    list.add((IDiagramModel)object);
                }
            }
        }
        
        return list;
    }
}
