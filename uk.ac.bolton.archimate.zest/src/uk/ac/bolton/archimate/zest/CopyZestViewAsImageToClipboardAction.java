/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.zest;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.ImageTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;


/**
 * Copy Zest Diagram As Image to clipboard Action
 * 
 * @author Phillip Beauvoir
 */
public class CopyZestViewAsImageToClipboardAction extends Action {
    
    private ZestGraphViewer fGraphViewer;

    public CopyZestViewAsImageToClipboardAction(ZestGraphViewer graphViewer) {
        super(Messages.CopyZestViewAsImageToClipboardAction_0);
        fGraphViewer = graphViewer;
    }

    @Override
    public void run() {
        Image image = fGraphViewer.createImage();
        Clipboard cb = new Clipboard(Display.getDefault());
        cb.setContents(new Object[] { image.getImageData() }, new Transfer[] { ImageTransfer.getInstance() });
        image.dispose();
        
        MessageDialog.openInformation(Display.getDefault().getActiveShell(),
                Messages.CopyZestViewAsImageToClipboardAction_0,
                Messages.CopyZestViewAsImageToClipboardAction_1);
    }
    
    @Override
    public String getToolTipText() {
        return getText();
    }

}
