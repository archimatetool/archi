/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.archimatetool.model.IDiagramModel;



/**
 * Paste Action
 * 
 * @author Phillip Beauvoir
 */
public class PasteAction extends SelectionAction {
    
    private GraphicalViewer fGraphicalViewer;
    
    private Point fMousePosition = null;
    
    private IWindowListener windowListener = new IWindowListener() {
        public final void windowActivated(IWorkbenchWindow window) {
            refresh();
        }

        public final void windowClosed(IWorkbenchWindow window) {
        }

        public final void windowDeactivated(IWorkbenchWindow window) {
        }

        public final void windowOpened(IWorkbenchWindow window) {
        }
    };
    
    private MouseListener mouseListener = new MouseListener() {
        @Override
        public void mousePressed(MouseEvent me) {
            Point pt = new Point(me.x, me.y);
            ((IFigure)me.getSource()).translateFromParent(pt);
            fMousePosition = pt;
        }

        @Override
        public void mouseReleased(MouseEvent me) {
        }

        @Override
        public void mouseDoubleClicked(MouseEvent me) {
        }
    };

    
    public PasteAction(IWorkbenchPart part, GraphicalViewer viewer) {
        super(part);
        
        fGraphicalViewer = viewer;
        
        setText(Messages.PasteAction_0);
        setId(ActionFactory.PASTE.getId());
        ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
        setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
        setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
        setEnabled(false);
 
        /**
         * Listen to window activation to udpate Paste Action if clipboard contents has changed
         */
        getWorkbenchPart().getSite().getWorkbenchWindow().getWorkbench().addWindowListener(windowListener);
        
        /**
         * Listen to mouse click position so that the Paste Action can paste objects at that point
         */
        ((GraphicalEditPart)fGraphicalViewer.getRootEditPart()).getFigure().addMouseListener(mouseListener);
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
            // TODO: MUST CHANGE THIS, THIS IS JUST TESTING!
            // execute(clipBoardCopy.getPasteCommand(getTargetDiagramModel(), fGraphicalViewer, fMousePosition));
            execute(clipBoardCopy.getMergeCommand(getTargetDiagramModel(), fGraphicalViewer, fMousePosition));
            fMousePosition = null;
        }
    }
    
    void reset() {
        update();
        fMousePosition = null;
    }
    
    private IDiagramModel getTargetDiagramModel() {
        IDiagramModel diagramModel = (IDiagramModel)getWorkbenchPart().getAdapter(IDiagramModel.class);
        if(diagramModel == null) {
            System.err.println("DiagramModel was null in " + getClass()); //$NON-NLS-1$
        }
        return diagramModel;
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        getWorkbenchPart().getSite().getWorkbenchWindow().getWorkbench().removeWindowListener(windowListener);
        ((GraphicalEditPart)fGraphicalViewer.getRootEditPart()).getFigure().removeMouseListener(mouseListener);
        
        fGraphicalViewer = null;
    }
}
