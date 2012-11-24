/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.actions;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import uk.ac.bolton.archimate.model.IDiagramModel;


/**
 * Paste Action
 * 
 * @author Phillip Beauvoir
 */
public class PasteAction extends SelectionAction {
    
    private GraphicalViewer fGraphicalViewer;
    
    private int fXMousePos = -1, fYMousePos = -1;
    
    public PasteAction(IWorkbenchPart part, GraphicalViewer viewer) {
        super(part);
        
        fGraphicalViewer = viewer;
        
        setText(Messages.PasteAction_0);
        setId(ActionFactory.PASTE.getId());
        ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
        setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
        setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
        setEnabled(false);
        addMouseListener();
    }
    
    @Override
    protected boolean calculateEnabled() {
        Object obj = Clipboard.getDefault().getContents();
        
        if(obj instanceof CopySnapshot) {
            CopySnapshot clipBoardCopy = (CopySnapshot)obj;
            return clipBoardCopy.canPasteToDiagram(getTargetDiagramModel());
        }
        
        return false;
    }

    @Override
    public void run() {
        Object obj = Clipboard.getDefault().getContents();
        
        if(obj instanceof CopySnapshot) {
            CopySnapshot clipBoardCopy = (CopySnapshot)obj;
            execute(clipBoardCopy.getPasteCommand(getTargetDiagramModel(), fGraphicalViewer, fXMousePos, fYMousePos));
            setMouseClickPosition(-1, -1);
        }
    }
    
    private IDiagramModel getTargetDiagramModel() {
        IDiagramModel diagramModel = (IDiagramModel)getWorkbenchPart().getAdapter(IDiagramModel.class);
        if(diagramModel == null) {
            System.err.println("DiagramModel was null in " + getClass()); //$NON-NLS-1$
        }
        return diagramModel;
    }
    
    /**
     * Set the context menu position so that we can paste at this origin
     * @param x
     * @param y
     */
    void setMouseClickPosition(int x, int y) {
        fXMousePos = x;
        fYMousePos = y;
    }
    
    /**
     * Listen to mouse click position so that the Paste Action can paste objects at that point
     */
    private void addMouseListener() {
        // Context menu click
//        viewer.getControl().addMenuDetectListener(new MenuDetectListener() {
//            public void menuDetected(MenuDetectEvent e) {
//                Point pt = viewer.getControl().toControl(e.x, e.y);
//                setMouseClickPosition(e.x, e.y);
//            }
//        });
        
        // Mouse down
        fGraphicalViewer.getControl().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseDown(MouseEvent e) {
                setMouseClickPosition(e.x, e.y);
            }
        });
    }
}
