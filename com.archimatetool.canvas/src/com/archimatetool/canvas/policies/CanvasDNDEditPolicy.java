/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.policies;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.URLTransfer;
import org.eclipse.swt.graphics.ImageData;

import com.archimatetool.canvas.model.ICanvasFactory;
import com.archimatetool.canvas.model.ICanvasModelImage;
import com.archimatetool.editor.diagram.commands.AddDiagramModelReferenceCommand;
import com.archimatetool.editor.diagram.commands.AddDiagramObjectCommand;
import com.archimatetool.editor.diagram.dnd.AbstractDNDEditPolicy;
import com.archimatetool.editor.diagram.dnd.DiagramDropRequest;
import com.archimatetool.editor.model.IArchiveManager;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.model.IDiagramModel;



/**
 * A policy to handle a Canvas View's DND commands
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class CanvasDNDEditPolicy extends AbstractDNDEditPolicy {
    
    @Override
    protected Command getDropCommand(DiagramDropRequest request) {
        // Local DND from model tree
        if(request.getTransferType() == LocalSelectionTransfer.getTransfer()) {
            return getLocalDropCommand(request);
        }

        Command[] cmd = new Command[1];
        
        BusyIndicator.showWhile(null, new Runnable() {
            @Override
            public void run() {
                // Dragged files from desktop or Windows Browser or Linux embedded Browser
                if(request.getTransferType() == FileTransfer.getInstance()) {
                    cmd[0] = getFileDropCommand(request);
                }
                // Dragged URL link or image from Mac/Linux external Browser
                else if(request.getTransferType() == URLTransfer.getInstance()) {
                    try {
                        cmd[0] = getURLDropCommand(request);
                    }
                    catch(Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        
        
        return cmd[0];
    }
    
    /**
     * Get a Command based on local file transfer
     */
    private Command getFileDropCommand(DiagramDropRequest request) {
        String[] paths = (String[])request.getData();
        if(paths == null) {
            return null;
        }
        
        List<File> files = new ArrayList<>();
        
        for(String path : paths) {
            if(isImagePath(path)) {
                File file = new File(path);
                if(file.exists() && file.canRead()) {
                    files.add(file);
                }
            }
        }
        
        return createImageFileDropCommand(files, getDropLocation(request));
    }
    
    /**
     * Get a Command based on URL transfer
     */
    private Command getURLDropCommand(DiagramDropRequest request) throws IOException, URISyntaxException {
        String s = (String)request.getData();
        if(s == null) {
            return null;
        }
        
        // Bug on Linux - URL is duplicated with a newline, or image info is appended after a newline
        if(PlatformUtils.isLinux()) {
            s = s.split("\n")[0];
        }
                       
        s = s.replaceAll(" ", "%20");
        
        if(!isImagePath(s)) {
            return null;
        }
        
        File file = null;
        URI uri = new URI(s);
        
        // Local file:/// URI for a file that exists
        if("file".equals(uri.getScheme())) {
            file = new File(uri);
        }
        // Online URL that we should download to a temporary file
        else {
            // Download and save to temporary file
            file = File.createTempFile("archi-", s.substring(s.lastIndexOf(".")));
            file.deleteOnExit();
            
            try(FileOutputStream fos = new FileOutputStream(file)) {
                try(ReadableByteChannel rbc = Channels.newChannel(uri.toURL().openStream())) {
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                }
            }
        }
        
        List<File> files = new ArrayList<>();
        
        if(file.exists() && file.canRead()) {
            files.add(file);
        }
        
        return createImageFileDropCommand(files, getDropLocation(request));
    }
    
    /**
     * Create a Command based on adding image files. The files must exist.
     */
    private Command createImageFileDropCommand(List<File> files, Point pt) {
        // This can be null for example DND from Windows IE Browser
        if(files == null || files.isEmpty()) {
            return null;
        }

        int origin = pt.x;
        int x = pt.x;
        int y = pt.y;

        IArchiveManager archiveManager = (IArchiveManager)getTargetContainer().getAdapter(IArchiveManager.class);
        
        CompoundCommand result = new CompoundCommand(Messages.CanvasDNDEditPolicy_0);

        for(File file : files) {
            String pathName;
            try {
                pathName = archiveManager.addImageFromFile(file);
            }
            catch(IOException ex) {
                ex.printStackTrace();
                continue;
            }
            
            // Get width and height of the image
            ImageData imageData = archiveManager.createImageData(pathName);
            if(imageData == null) {
                continue;
            }
            
            int width = imageData.width;
            int height = imageData.height;

            ICanvasModelImage canvasModelImage = ICanvasFactory.eINSTANCE.createCanvasModelImage();
            canvasModelImage.setName(Messages.CanvasDNDEditPolicy_1);
            canvasModelImage.setBounds(x, y, width, height);
            canvasModelImage.setImagePath(pathName);

            result.add(new AddDiagramObjectCommand(getTargetContainer(), canvasModelImage));

            // Increase x,y like a Carriage Return
            x += width + 10;
            if(x > origin + 1000) {
                x = origin;
                y += height + 10;
            }
        }
        
        return result;
    }
    
    private boolean isImagePath(String path) {
        Set<String> extensions = Set.of(".png", ".bmp", ".gif", ".jpg", ".jpeg", ".tif", ".tiff", ".ico");
        return extensions.stream()
                         .anyMatch(path.toLowerCase()::endsWith);
    }
        
    /**
     * Handle dragging elements from the Models Tree
     */
    private Command getLocalDropCommand(DiagramDropRequest request) {
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
        CompoundCommand result = new CompoundCommand(Messages.CanvasDNDEditPolicy_2);
        
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
    private List<IDiagramModel> getDiagramRefsToAdd(Object[] objects) {
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
