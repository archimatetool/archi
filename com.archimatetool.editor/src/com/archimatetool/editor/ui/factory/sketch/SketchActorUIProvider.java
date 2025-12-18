/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.sketch;

import java.util.Set;

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
    
    // Features to expose
    private final static Set<String> supportedFeatures = Set.of(
            IArchimatePackage.Literals.PROPERTIES__PROPERTIES.getName(),
            IArchimatePackage.Literals.DIAGRAM_MODEL_OBJECT__FILL_COLOR.getName(),
            IArchimatePackage.Literals.LINE_OBJECT__LINE_WIDTH.getName(),
            IArchimatePackage.Literals.FONT_ATTRIBUTE__FONT.getName(),
            IArchimatePackage.Literals.FONT_ATTRIBUTE__FONT_COLOR.getName()
            );

    @Override
    public boolean shouldExposeFeature(String featureName) {
        return featureName == null ? false : supportedFeatures.contains(featureName);
    }
}
