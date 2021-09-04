/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.zest;

import org.eclipse.draw2d.Viewport;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.viewers.GraphViewer;

import com.archimatetool.editor.diagram.util.AnimationUtil;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ColorFactory;



/**
 * Zest Graph Viewer
 * 
 * @author Phillip Beauvoir
 */
public class ZestGraphViewer extends GraphViewer {
    
    /**
     * Application Preferences Listener
     */
    private IPropertyChangeListener prefsListener = event -> {
        if(AnimationUtil.supportsAnimation()) {
            if(IPreferenceConstants.ANIMATE_VISUALISER_NODES.equals(event.getProperty())) {
                getGraphControl().setAnimationEnabled(Preferences.STORE.getBoolean(IPreferenceConstants.ANIMATE_VISUALISER_NODES));
            }
            else if(IPreferenceConstants.ANIMATE_VISUALISER_TIME.equals(event.getProperty())) {
                getGraphControl().setAnimationTime(Preferences.STORE.getInt(IPreferenceConstants.ANIMATE_VISUALISER_TIME));
            }
        }
    };
    
    public ZestGraphViewer(Composite composite, int style) {
        super(composite, style);
        setContentProvider(new ZestViewerContentProvider());
        setLabelProvider(new ZestViewerLabelProvider());
        
        // Animate nodes
        if(AnimationUtil.supportsAnimation()) {
            getGraphControl().setAnimationEnabled(Preferences.STORE.getBoolean(IPreferenceConstants.ANIMATE_VISUALISER_NODES));
            getGraphControl().setAnimationTime(Preferences.STORE.getInt(IPreferenceConstants.ANIMATE_VISUALISER_TIME));
        }
        
        // Preference listener
        Preferences.STORE.addPropertyChangeListener(prefsListener);
        
        // Un-Preference listener
        getGraphControl().addDisposeListener(e -> {
            Preferences.STORE.removePropertyChangeListener(prefsListener);
        });
        
        // Mouse Wheel listener
        getGraphControl().addMouseWheelListener(new MouseWheelListener() {
            // Scrolling down scrolls to the right
            private static final int DIRECTION = -1;
            
            // How many pixels to scroll
            private static final int DELTA = 30;
            
            @Override
            public void mouseScrolled(MouseEvent event) {
                // Zoom in and out with Ctrl Key and mouse wheel - need better icons for this to look good
//                if((event.stateMask & SWT.MOD1) != 0) {
//                    if(event.count < 0) {
//                        getZoomManager().zoomOut();
//                    }
//                    else if(event.count > 0) {
//                        getZoomManager().zoomIn();
//                    }
//                }
                
                // Scroll left/right with mouse wheel and Shift key
                if((event.stateMask & SWT.MOD2) != 0) {
                    Viewport viewPort = getGraphControl().getViewport();
                    viewPort.setViewLocation(viewPort.getViewLocation().translate(event.count * DELTA * DIRECTION, 0));
                }
            }
        });
        
        // Set CSS class name for Themes
        getGraphControl().setData("org.eclipse.e4.ui.css.CssClassName", "ArchiGraph"); //$NON-NLS-1$ //$NON-NLS-2$
        
        // Set background color in case theming is disabled
        getGraphControl().setBackground(ColorFactory.get(255, 255, 255));
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
