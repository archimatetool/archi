/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import org.eclipse.gef.Disposable;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.jface.action.Action;

import com.archimatetool.editor.ui.IArchiImages;


/**
 * Zoom "Actual Size" Action
 * 
 * @author Phillip Beauvoir
 */
public class ZoomNormalAction extends Action implements ZoomListener, Disposable {
    
    public static final String ID = "org.eclipse.gef.zoom_normal"; //$NON-NLS-1$
    public static final String TEXT = Messages.ZoomNormalAction_0;

    /**
     * The ZoomManager used to zoom in or out
     */
    protected ZoomManager zoomManager;

    public ZoomNormalAction(ZoomManager zoomManager) {
        setToolTipText(TEXT);
        setText(TEXT);
        setId(ID);
        setActionDefinitionId(ID);
        setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ZOOM_NORMAL));
        
        this.zoomManager = zoomManager;
        zoomManager.addZoomListener(this);
        setEnabled(zoomManager.getZoom() != 1.0);
    }

    @Override
    public void run() {
        zoomManager.setZoom(1.0);
    }
    
    @Override
    public void zoomChanged(double zoom) {
        setEnabled(zoomManager.getZoom() != 1.0);
    }

    @Override
    public void dispose() {
        zoomManager.removeZoomListener(this);
    }

}
