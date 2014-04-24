package com.archimatetool.editor.diagram.actions;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
/**
 * Merge Action
 * 
 * This is almost identical to a paste, except it merges the selection into the current diagram, by issuing a MergeCommand.
 * 
 * @author Mads Bondo Dydensborg
 */
public class MergeAction extends PasteAction {

	public static final String ID = "MergeAction"; //$NON-NLS-1$
    public static final String TEXT = Messages.MergeAction_0;
	
	public MergeAction(IWorkbenchPart part, GraphicalViewer viewer) {
		super(part, viewer);
        setText(TEXT);
        setId(ID);
        // TODO: Could be changed to something else than "Paste", if supported.
        ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
        setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
        setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
        setEnabled(false);
	}
	
	// Almost identical to Paste, except it uses a merge command.
	@Override
	public void run() {
		Object obj = Clipboard.getDefault().getContents();
        
        if(obj instanceof CopySnapshot) {
            CopySnapshot clipBoardCopy = (CopySnapshot)obj;
            execute(clipBoardCopy.getMergeCommand(getTargetDiagramModel(), fGraphicalViewer, fMousePosition));
            fMousePosition = null;
        }
  
	}
	

}
