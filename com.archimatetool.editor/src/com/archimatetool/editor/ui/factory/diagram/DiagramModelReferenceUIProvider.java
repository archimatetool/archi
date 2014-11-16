/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.diagram;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.editparts.diagram.DiagramModelReferenceEditPart;
import com.archimatetool.editor.ui.ArchimateLabelProvider;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.editor.ui.factory.AbstractElementUIProvider;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelReference;



/**
 * Diagram Model Reference UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class DiagramModelReferenceUIProvider extends AbstractElementUIProvider {

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
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_DIAGRAM_16);
    }

    @Override
    public Dimension getDefaultSize() {
        return new Dimension(120, 55);
    }

    @Override
    public Image getImage(EObject instance) {
        if(instance instanceof IDiagramModelReference) {
            IDiagramModel dm = ((IDiagramModelReference)instance).getReferencedModel();
            return ArchimateLabelProvider.INSTANCE.getImage(dm);
        }
        
        return getImage();
    }
    
    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_DIAGRAM_16);
    }

    @Override
    public Color getDefaultColor() {
        return ColorFactory.get(220, 235, 235);
    }
}
