/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.zest;

import org.eclipse.draw2d.Viewport;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.ZestStyles;

import com.archimatetool.editor.diagram.util.AnimationUtil;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;



/**
 * Zest Graph Viewer
 * 
 * @author Phillip Beauvoir
 */
public class ZestGraphViewer extends GraphViewer {
    
    /**
     * Application Preferences Listener
     */
    private IPropertyChangeListener prefsListener = new IPropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if(AnimationUtil.supportsAnimation() && IPreferenceConstants.ANIMATE_VISUALISER_NODES.equals(event.getProperty())) {
                Object input = getInput(); // save this
                setInput(null); // can't set node style if input is not null
                setNodeStyle(Preferences.STORE.getBoolean(IPreferenceConstants.ANIMATE_VISUALISER_NODES) ? 0 : ZestStyles.NODES_NO_LAYOUT_ANIMATION);
                setInput(input); // reset
                doApplyLayout(); // and layout again
            }
        }
    };
    
    public ZestGraphViewer(Composite composite, int style) {
        super(composite, style);
        setContentProvider(new ZestViewerContentProvider());
        setLabelProvider(new ZestViewerLabelProvider());
        
        // Don't animate nodes if set
        if(!AnimationUtil.supportsAnimation() || !Preferences.STORE.getBoolean(IPreferenceConstants.ANIMATE_VISUALISER_NODES)) {
            setNodeStyle(ZestStyles.NODES_NO_LAYOUT_ANIMATION);
        }
        
        // Preference listener
        Preferences.STORE.addPropertyChangeListener(prefsListener);
        
        // Un-Preference listener
        getGraphControl().addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                Preferences.STORE.removePropertyChangeListener(prefsListener);
            }
        });
        
        // Mouse Wheel listener
        getGraphControl().addMouseWheelListener(new MouseWheelListener() {
            final int DELTA = 30;
            
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
