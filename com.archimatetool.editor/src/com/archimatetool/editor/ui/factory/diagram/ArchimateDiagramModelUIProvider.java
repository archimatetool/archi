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
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.IGraphicsIcon;
import com.archimatetool.editor.ui.factory.AbstractObjectUIProvider;
import com.archimatetool.editor.ui.factory.IDiagramModelUIProvider;
import com.archimatetool.model.IArchimatePackage;



/**
 * Archimate Diagram Model UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateDiagramModelUIProvider extends AbstractObjectUIProvider implements IDiagramModelUIProvider {
    
    private IGraphicsIcon graphicsIcon = new ArchimateDiagramModelGraphicsIcon();

    @Override
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
        return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_DIAGRAM);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_DIAGRAM);
    }
    
    @Override
    public IGraphicsIcon getGraphicsIcon() {
        return graphicsIcon;
    }
}
