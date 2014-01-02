/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.editparts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.rulers.RulerProvider;


/**
 * SnapAdapter for Snap things
 * 
 * @author Phillip Beauvoir
 */
public class SnapEditPartAdapter {
    
    private GraphicalEditPart fEditPart;

    public SnapEditPartAdapter(GraphicalEditPart editPart) {
        fEditPart = editPart;
    }
    
    public SnapToHelper getSnapToHelper() {
        List<SnapToHelper> snapStrategies = new ArrayList<SnapToHelper>();
        
        // Snap to Ruler Guides
        Boolean val = (Boolean)fEditPart.getViewer().getProperty(RulerProvider.PROPERTY_RULER_VISIBILITY);
        if(val != null && val.booleanValue()) {
            snapStrategies.add(new SnapToGuides(fEditPart));
        }
        
        // Snap to Geometry
        val = (Boolean)fEditPart.getViewer().getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED);
        if(val != null && val.booleanValue()) {
            snapStrategies.add(new SnapToGeometry(fEditPart));
        }
        
        // Snap to Grid
        val = (Boolean)fEditPart.getViewer().getProperty(SnapToGrid.PROPERTY_GRID_ENABLED);
        if(val != null && val.booleanValue()) {
            snapStrategies.add(new SnapToGrid(fEditPart));
        }

        if(snapStrategies.size() == 0) {
            return null;
        }
        
        if(snapStrategies.size() == 1) {
            return snapStrategies.get(0);
        }

        SnapToHelper ss[] = new SnapToHelper[snapStrategies.size()];
        
        for(int i = 0; i < snapStrategies.size(); i++) {
            ss[i] = snapStrategies.get(i);
        }
        
        return new CompoundSnapToHelper(ss);
    }
}
