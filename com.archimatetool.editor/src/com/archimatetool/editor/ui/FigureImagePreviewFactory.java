/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.util.DiagramUtils;
import com.archimatetool.editor.ui.factory.IArchimateElementUIProvider;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelArchimateObject;


/**
 * Factory class to create preview images of Figures
 * If the Figure has an alternate figures it will return that
 * 
 * @author Phillip Beauvoir
 */
public class FigureImagePreviewFactory {
    
    static ImageRegistry imageRegistry = new ImageRegistry();
    
    /**
     * @param eClass
     * @return A preview image of the graphical Figure used by eClass
     */
    public static Image getFigurePreviewImageForClass(EClass eClass) {
        IGraphicalObjectUIProvider provider = (IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProviderForClass(eClass);
        
        if(provider instanceof IArchimateElementUIProvider ) {
            return getPreviewImage((IArchimateElementUIProvider)provider, 0);
        }
        
        return null;
    }
    
    /**
     * @param eClass
     * @return A preview image of the alternate graphical Figure used by eClass.
     *         If there is no alternate graphical Figure return null
     */
    public static Image getAlternateFigurePreviewImageForClass(EClass eClass) {
        IGraphicalObjectUIProvider provider = (IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProviderForClass(eClass);
        
        if(provider instanceof IArchimateElementUIProvider && ((IArchimateElementUIProvider)provider).hasAlternateFigure()) {
            return getPreviewImage((IArchimateElementUIProvider)provider, 1);
        }
        
        return null;
    }

    private static Image getPreviewImage(IArchimateElementUIProvider provider, int type) {
        EClass eClass = provider.providerFor();
        
        String key = eClass.getName() + type;
        
        Image image = imageRegistry.get(key);
        
        if(image == null) {
            IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
            dmo.setArchimateElement((IArchimateElement)IArchimateFactory.eINSTANCE.create(eClass));
            dmo.setName(provider.getDefaultName());
            dmo.setTextPosition(provider.getDefaultTextPosition());
            dmo.setTextAlignment(provider.getDefaultTextAlignment());
            ColorFactory.setDefaultColors(dmo);
            dmo.setType(type);

            provider.setInstance(dmo);

            GraphicalEditPart editPart = (GraphicalEditPart)provider.createEditPart();
            editPart.setModel(dmo);
            
            IDiagramModelObjectFigure figure = (IDiagramModelObjectFigure)editPart.getFigure();
            figure.setSize(new Dimension(120, 55));
            figure.refreshVisuals();
            figure.validate();

            image = DiagramUtils.createImage(figure, 1, 0);
            imageRegistry.put(key, image);
        }
        
        return image;
    }
}
