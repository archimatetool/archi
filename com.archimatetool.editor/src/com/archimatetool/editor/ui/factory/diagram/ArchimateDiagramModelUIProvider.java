/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.diagram;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.editparts.ArchimateDiagramPart;
import com.archimatetool.editor.diagram.figures.diagram.ArchimateDiagramModelGraphicsIcon;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.editor.ui.IGraphicsIcon;
import com.archimatetool.editor.ui.factory.AbstractObjectUIProvider;
import com.archimatetool.model.IArchimatePackage;



/**
 * Archimate Diagram Model UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateDiagramModelUIProvider extends AbstractObjectUIProvider {
    
    private IGraphicsIcon graphicsIcon = new ArchimateDiagramModelGraphicsIcon();

    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getArchimateDiagramModel();
    }
    
    @Override
    public EditPart createEditPart() {
        return new ArchimateDiagramPart();
    }
    
    @Override
    public String getDefaultName() {
        return Messages.ArchimateDiagramModelUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_DIAGRAM_16);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_DIAGRAM_16);
    }
    
    @Override
    public IGraphicsIcon getGraphicsIcon() {
        return graphicsIcon;
    }
}
