/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.zest;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.widgets.ZestStyles;

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
            if(IPreferenceConstants.ANIMATE_VISUALISER_NODES.equals(event.getProperty())) {
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
        
        if(!Preferences.STORE.getBoolean(IPreferenceConstants.ANIMATE_VISUALISER_NODES)) {
            setNodeStyle(ZestStyles.NODES_NO_LAYOUT_ANIMATION);
        }
        
        Preferences.STORE.addPropertyChangeListener(prefsListener);
        
        getGraphControl().addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                Preferences.STORE.removePropertyChangeListener(prefsListener);
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
