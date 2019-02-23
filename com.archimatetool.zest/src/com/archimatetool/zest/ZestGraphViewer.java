/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.zest;

import org.eclipse.draw2d.Viewport;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.ZestStyles;



/**
 * Zest Graph Viewer
 * 
 * @author Phillip Beauvoir
 */
public class ZestGraphViewer extends GraphViewer {
    
    public ZestGraphViewer(Composite composite, int style) {
        super(composite, style);
        setContentProvider(new ZestViewerContentProvider());
        setLabelProvider(new ZestViewerLabelProvider());
        
        // Don't animate nodes
        setNodeStyle(ZestStyles.NODES_NO_LAYOUT_ANIMATION);
        
        // Mouse Wheel listener
        getGraphControl().addMouseWheelListener(new MouseWheelListener() {
            final int DELTA = 30;
            
            @Override
            public void mouseScrolled(MouseEvent e) {
                // Zoom in and out with Ctrl Key and mouse wheel - need better icons for this to look good
//                if((e.stateMask & SWT.MOD1) != 0) {
//                    if(e.count > 0) {
//                        getZoomManager().zoomOut();
//                    }
//                    else if(e.count < 0) {
//                        getZoomManager().zoomIn();
//                    }
//                }
                
                // Scroll left/right with mouse wheel and Shift key
                if((e.stateMask & SWT.MOD2) != 0) {
                    Viewport viewPort = getGraphControl().getViewport();
                    viewPort.setViewLocation(viewPort.getViewLocation().translate(DELTA * e.count, 0));
                }
            }
        });
    }
    
    void doApplyLayout() {
        super.applyLayout();
    }

    @Override
    public void applyLayout() {
        // This stops Zest laying out when we don't want it to
        //super.applyLayout();
    }

}
