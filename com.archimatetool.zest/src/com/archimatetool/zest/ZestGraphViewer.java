/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.zest;

import org.eclipse.draw2d.Viewport;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.ZestStyles;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.util.AnimationUtil;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ThemeUtils;



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
            if(IPreferenceConstants.ANIMATE_VISUALISER_TIME.equals(event.getProperty())) {
                getGraphControl().setData(Graph.KEY_ANIMATION_TIME, ArchiPlugin.getInstance().getPreferenceStore().getInt(IPreferenceConstants.ANIMATE_VISUALISER_TIME));
            }
        }
    };
    
    @Override
    public int getNodeStyle() {
        // Animate nodes
        if(AnimationUtil.supportsAnimation()) {
            return ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.ANIMATE_VISUALISER_NODES) ?
                    super.getNodeStyle() : super.getNodeStyle() | ZestStyles.NODES_NO_LAYOUT_ANIMATION;
        }
        
        return super.getNodeStyle();
    }
    
    public ZestGraphViewer(Composite composite, int style) {
        super(composite, style);
        setContentProvider(new ZestViewerContentProvider());
        setLabelProvider(new ZestViewerLabelProvider());
        
        Graph graph = getGraphControl();
        
        // Animate nodes
        if(AnimationUtil.supportsAnimation()) {
            graph.setData(Graph.KEY_ANIMATION_TIME, ArchiPlugin.getInstance().getPreferenceStore().getInt(IPreferenceConstants.ANIMATE_VISUALISER_TIME));
        }
        
        // Preference listener
        ArchiPlugin.getInstance().getPreferenceStore().addPropertyChangeListener(prefsListener);
        
        // Un-Preference listener
        graph.addDisposeListener(e -> {
            ArchiPlugin.getInstance().getPreferenceStore().removePropertyChangeListener(prefsListener);
        });
        
        // Mouse scroll wheel
        graph.addListener(SWT.MouseVerticalWheel, event -> {
            // Zoom
            if((event.stateMask & SWT.MOD1) != 0) {
                switch(Integer.signum(event.count)) {
                    case  1 -> getZoomManager().zoomIn();
                    case -1 -> getZoomManager().zoomOut();
                }
            }
            
            // Scroll left/right with mouse wheel and Shift key
            else if((event.stateMask & SWT.MOD2) != 0) {
                Viewport viewPort = graph.getViewport();
                viewPort.setViewLocation(viewPort.getViewLocation().translate(event.count * -30, 0));
            }
        });
        
        // Set CSS ID
        ThemeUtils.registerCssId(getGraphControl(), "ArchiGraph"); //$NON-NLS-1$
        
        // Set background color in case CSS theming is disabled
        ThemeUtils.setBackgroundColorIfCssThemingDisabled(getGraphControl(), IPreferenceConstants.VISUALISER_BACKGROUND_COLOR);
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
