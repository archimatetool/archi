/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.canvas.policies;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.dnd.FileTransfer;

import uk.ac.bolton.archimate.canvas.model.ICanvasFactory;
import uk.ac.bolton.archimate.canvas.model.ICanvasModelImage;
import uk.ac.bolton.archimate.editor.diagram.commands.AddDiagramModelReferenceCommand;
import uk.ac.bolton.archimate.editor.diagram.commands.AddDiagramObjectCommand;
import uk.ac.bolton.archimate.editor.diagram.dnd.AbstractDNDEditPolicy;
import uk.ac.bolton.archimate.editor.diagram.dnd.DiagramDropRequest;
import uk.ac.bolton.archimate.editor.model.IArchiveManager;
import uk.ac.bolton.archimate.editor.model.ICachedImage;
import uk.ac.bolton.archimate.model.IDiagramModel;


/**
 * A policy to handle a Canvas View's DND commands
 * 
 * @author Phillip Beauvoir
 */
public class CanvasDNDEditPolicy extends AbstractDNDEditPolicy {
    
    @Override
    protected Command getDropCommand(DiagramDropRequest request) {
        // Dragged image files from desktop
        if(request.getTransferType() == FileTransfer.getInstance()) {
            final Command[] cmd = new Command[1];
            final DiagramDropRequest _request = request;
            
            BusyIndicator.showWhile(null, new Runnable() {
                @Override
                public void run() {
                    cmd[0] = getFileDropCommand(_request);
                }
            });
            
            return cmd[0];
        }
        // Local DND from model tree
        else if(request.getTransferType() == LocalSelectionTransfer.getTransfer()) {
            return getLocalDropCommand(request);
        }
        
        return null;
    }
    
    /**
     * @param request
     * @return
     */
    protected Command getFileDropCommand(DiagramDropRequest request) {
        String[] files = (String[])request.getData();
        
        // XY drop point
        Point pt = getDropLocation(request);

        int origin = pt.x;
        int x = pt.x;
        int y = pt.y;

        IArchiveManager archiveManager = (IArchiveManager)getTargetContainer().getAdapter(IArchiveManager.class);
        
        CompoundCommand result = new CompoundCommand("Add Images");

        for(String s : files) {
            File file = new File(s);
            if(!file.canRead()) {
                continue;
            }
            String name = file.getName().toLowerCase();
            if(!(name.endsWith(".png") || name.endsWith(".bmp") || name.endsWith(".gif") ||
                    name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".tif") ||
                    name.endsWith(".tiff") || name.endsWith(".ico"))) {
                continue;
            }

            ICanvasModelImage canvasModelImage = ICanvasFactory.eINSTANCE.createCanvasModelImage();
            canvasModelImage.setName("Image");
            String pathName;
            try {
                pathName = archiveManager.addImageFromFile(file);
            }
            catch(IOException ex) {
                ex.printStackTrace();
                continue;
            }
            canvasModelImage.setImagePath(pathName);

            // Get width and height of image
            ICachedImage cachedImage = archiveManager.getImage(pathName);
            int image_width = cachedImage.getImage().getBounds().width;
            int image_height = cachedImage.getImage().getBounds().height;
            cachedImage.release();

            canvasModelImage.setBounds(x, y, -1, -1);

            result.add(new AddDiagramObjectCommand(getTargetContainer(), canvasModelImage));

            // Increase x,y like a Carriage Return
            x += image_width + 10;
            if(x > origin + 1000) {
                x = origin;
                y += image_height + 10;
            }
        }
        
        return result;
    }
    
    /**
     * Handle dragging elements from the Models Tree
     */
    protected Command getLocalDropCommand(DiagramDropRequest request) {
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
        CompoundCommand result = new CompoundCommand("Add Elements");
        
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
     * Gather the diagram refs that will be added to the diagram
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
