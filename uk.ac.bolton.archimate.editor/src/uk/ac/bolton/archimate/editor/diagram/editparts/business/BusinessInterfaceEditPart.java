/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.editparts.business;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.gef.ConnectionEditPart;

import uk.ac.bolton.archimate.editor.diagram.editparts.AbstractArchimateEditableTextFlowEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.OrthogonalAnchor;
import uk.ac.bolton.archimate.editor.diagram.figures.business.BusinessInterfaceFigure;
import uk.ac.bolton.archimate.editor.preferences.IPreferenceConstants;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import uk.ac.bolton.archimate.model.IArchimatePackage;

/**
 * Business Interface Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class BusinessInterfaceEditPart
extends AbstractArchimateEditableTextFlowEditPart {
    
    @Override
    protected IFigure createFigure() {
        return new BusinessInterfaceFigure(getModel());
    }
    
    @Override
    protected ConnectionAnchor getDefaultConnectionAnchor() {
        if(fDefaultConnectionAnchor == null) {
            switch(getModel().getType()) {
                case 1:
                    fDefaultConnectionAnchor = new EllipseAnchor(getFigure());
                    break;

                default:
                    fDefaultConnectionAnchor = new ChopboxAnchor(getFigure());
                    break;
            }
        }
        
        return fDefaultConnectionAnchor;
    }

    @Override
    protected ConnectionAnchor getConnectionAnchor(ConnectionEditPart connection) {
        if(Preferences.STORE.getBoolean(IPreferenceConstants.USE_ORTHOGONAL_ANCHOR)) {
            if(fActiveConnectionAnchor == null) {
                fActiveConnectionAnchor = new OrthogonalAnchor(this, connection);
            }
            
            return fActiveConnectionAnchor;
        }
        
        return getDefaultConnectionAnchor();
    }

    @Override
    protected void eCoreChanged(Notification msg) {
        super.eCoreChanged(msg);
        
        // Update Connection Anchors
        if(msg.getFeature() == IArchimatePackage.Literals.DIAGRAM_MODEL_ARCHIMATE_OBJECT__TYPE) {
            resetConnectionAnchors();
        }
    }

}