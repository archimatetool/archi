/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.factory.sketch;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.sketch.editparts.SketchDiagramPart;
import com.archimatetool.editor.diagram.sketch.figures.SketchModelGraphicsIcon;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.IGraphicsIcon;
import com.archimatetool.editor.ui.factory.AbstractObjectUIProvider;
import com.archimatetool.editor.ui.factory.IDiagramModelUIProvider;
import com.archimatetool.model.IArchimatePackage;



/**
 * Sketch Diagram Model UI Provider
 * 
 * @author Phillip Beauvoir
 */
public class SketchModelUIProvider extends AbstractObjectUIProvider implements IDiagramModelUIProvider {
    
    private IGraphicsIcon graphicsIcon = new SketchModelGraphicsIcon();

    @Override
    public EClass providerFor() {
        return IArchimatePackage.eINSTANCE.getSketchModel();
    }
    
    @Override
    public EditPart createEditPart() {
        return new SketchDiagramPart();
    }
    
    @Override
    public String getDefaultName() {
        return Messages.SketchModelUIProvider_0;
    }

    @Override
    public Image getImage() {
        return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_SKETCH);
    }

    @Override
    public ImageDescriptor getImageDescriptor() {
        return IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_SKETCH);
    }
    
    @Override
    public IGraphicsIcon getGraphicsIcon() {
        return graphicsIcon;
    }
}
