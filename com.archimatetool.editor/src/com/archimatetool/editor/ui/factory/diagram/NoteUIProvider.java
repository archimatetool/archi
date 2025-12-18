/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.diagram;

import java.util.Set;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.editparts.diagram.LegendEditPart;
import com.archimatetool.editor.diagram.editparts.diagram.NoteEditPart;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.factory.AbstractGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.textrender.TextRenderer;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.ITextAlignment;



/**
 * Note UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class NoteUIProvider extends AbstractGraphicalObjectUIProvider {

    @Override
    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getDiagramModelNote();
    }
    
    @Override
    public EditPart createEditPart() {
        return isLegend() ? new LegendEditPart() : new NoteEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.NoteUIProvider_0;
    }

    @Override
    public Dimension getDefaultSize() {
        return isLegend() ? new Dimension(210, 320) : new Dimension(185, 80);
    }

    @Override
    public Image getImage() {
        return isLegend() ? IArchiImages.ImageFactory.getImage(IArchiImages.ICON_LEGEND) : IArchiImages.ImageFactory.getImage(IArchiImages.ICON_NOTE);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return isLegend() ? IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_LEGEND) : IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_NOTE);
    }
    
    @Override
    public int getDefaultTextAlignment() {
        return ITextAlignment.TEXT_ALIGNMENT_LEFT;
    }
    
    public boolean isLegend() {
        return getInstance() instanceof IDiagramModelNote note && note.isLegend();
    }
    
    // Features to not use if this note is displaying a legend
    private final static Set<String> unsupportedLegendFeatures = Set.of(
            IArchimatePackage.Literals.TEXT_CONTENT__CONTENT.getName(),
            TextRenderer.FEATURE_NAME,
            IArchimatePackage.Literals.DIAGRAM_MODEL_IMAGE_PROVIDER__IMAGE_PATH.getName(),
            IArchimatePackage.Literals.ICONIC__IMAGE_POSITION.getName(),
            IArchimatePackage.Literals.TEXT_ALIGNMENT__TEXT_ALIGNMENT.getName(),
            IArchimatePackage.Literals.TEXT_POSITION__TEXT_POSITION.getName(),
            IArchimatePackage.Literals.BORDER_TYPE__BORDER_TYPE.getName()
            );
    
    @Override
    public boolean shouldExposeFeature(String featureName) {
        if(featureName == null) {
            return false;
        }
        
        return isLegend() ? !unsupportedLegendFeatures.contains(featureName) : super.shouldExposeFeature(featureName);
    }
}
