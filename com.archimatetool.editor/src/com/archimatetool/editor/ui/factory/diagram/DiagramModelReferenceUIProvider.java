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
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.editparts.diagram.DiagramModelReferenceEditPart;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.factory.AbstractGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IDiagramModelReference;



/**
 * Diagram Model Reference UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class DiagramModelReferenceUIProvider extends AbstractGraphicalObjectUIProvider {
    
    private static Color defaultColor = new Color(220, 235, 235);
    
    @Override
    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getDiagramModelReference();
    }
    
    @Override
    public EditPart createEditPart() {
        return new DiagramModelReferenceEditPart();
    }

    @Override
    public String getDefaultName() {
        return Messages.DiagramModelReferenceUIProvider_0;
    }

    @Override
    public Image getImage() {
        // Get this from the real Provider
        if(getInstance() instanceof IDiagramModelReference ref) {
            IDiagramModel dm = ref.getReferencedModel();
            
            if(dm != null) {
                IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProvider(dm);
                if(provider != null) {
                    return provider.getImage();
                }
            }
        }
        
        return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_DIAGRAM);
    }

    @Override
    public Dimension getDefaultSize() {
        return IGraphicalObjectUIProvider.defaultSize();
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_DIAGRAM);
    }

    @Override
    public Color getDefaultColor() {
        return defaultColor;
    }
    
    // Unsupported
    private final static Set<String> unsupportedFeatures = Set.of(
            IDiagramModelObject.FEATURE_ICON_COLOR
            );

    @Override
    public boolean shouldExposeFeature(String featureName) {
        return featureName == null ? false : !unsupportedFeatures.contains(featureName);
    }
    
    @Override
    public boolean hasIcon() {
        return true;
    }
}
