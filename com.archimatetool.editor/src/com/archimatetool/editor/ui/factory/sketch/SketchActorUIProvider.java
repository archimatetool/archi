/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.sketch;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.sketch.editparts.SketchActorEditPart;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.factory.AbstractGraphicalObjectUIProvider;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Sketch Actor UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class SketchActorUIProvider extends AbstractGraphicalObjectUIProvider {

    @Override
    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getSketchModelActor();
    }
    
    @Override
    public EditPart createEditPart() {
        return new SketchActorEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.SketchActorUIProvider_0;
    }

    @Override
    public Dimension getDefaultSize() {
        return new Dimension(75, 100);
    }

    @Override
    public Image getImage() {
        return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_ACTOR);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_ACTOR);
    }

    @Override
    public Color getDefaultColor() {
        return ColorConstants.black;
    }
    
    @Override
    public boolean shouldExposeFeature(String featureName) {
        if(featureName == IArchimatePackage.Literals.TEXT_ALIGNMENT__TEXT_ALIGNMENT.getName() ||
                featureName == IArchimatePackage.Literals.LINE_OBJECT__LINE_COLOR.getName() ||
                featureName == IDiagramModelObject.FEATURE_LINE_ALPHA ||
                featureName == IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__ALPHA.getName() ||
                featureName == IDiagramModelObject.FEATURE_GRADIENT) {
            return false;
        }
        
        return true;
    }
}
