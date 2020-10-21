/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gef.AutoexposeHelper;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;

import com.archimatetool.editor.diagram.util.ExtendedViewportAutoexposeHelper;

/**
 * Extension of ScalableFreeformRootEditPart so we can hack into it
 * 
 * @author Phillip Beauvoir
 */
public class ExtendedScalableFreeformRootEditPart extends ScalableFreeformRootEditPart {
    
    @SuppressWarnings("rawtypes")
    @Override
    public Object getAdapter(Class adapter) {
        if(adapter == AutoexposeHelper.class) {
            return new ExtendedViewportAutoexposeHelper(this, new Insets(50), false);
        }
        return super.getAdapter(adapter);
    }
    
    /**
     * Override this to use our ExtendedScalableFreeformLayeredPane
     */
    @Override
    protected ScalableFreeformLayeredPane createScaledLayers() {
        ScalableFreeformLayeredPane layers = new ExtendedScalableFreeformLayeredPane();
        layers.add(createGridLayer(), GRID_LAYER);
        layers.add(getPrintableLayers(), PRINTABLE_LAYERS);
        layers.add(new FeedbackLayer(), SCALED_FEEDBACK_LAYER);
        return layers;
    }
    
    /**
     * This one doesn't use ScaledGraphics which clips text at higher zoom levels
     */
    class ExtendedScalableFreeformLayeredPane extends ScalableFreeformLayeredPane {
        @Override
        protected void paintClientArea(Graphics graphics) {
            if(getChildren().isEmpty()) {
                return;
            }
            if(getScale() == 1.0) {
                super.paintClientArea(graphics);
            }
            else {
                boolean optimizeClip = getBorder() == null || getBorder().isOpaque();
                if(!optimizeClip) {
                    graphics.clipRect(getBounds().getShrinked(getInsets()));
                }
                graphics.scale(getScale());
                graphics.pushState();
                paintChildren(graphics);
                graphics.popState();
                graphics.restoreState();
            }
        }
    }
    
    protected class FeedbackLayer extends FreeformLayer {
        public FeedbackLayer() {
            setEnabled(false);
        }
    }
}
