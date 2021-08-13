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
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.diagram.util.DiagramUtils;
import com.archimatetool.editor.ui.factory.IArchimateElementUIProvider;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.ITextAlignment;


/**
 * Factory class to create preview images of Figures
 * If the Figure has an alternate figures it will return that
 * 
 * @author Phillip Beauvoir
 */
public class FigureImagePreviewFactory {
    
    /**
     * Image Registry
     * We need to check Display.getCurrent() because it can be null if running headless (tests, scripting, command line)
     */
    static ImageRegistry imageRegistry = new ImageRegistry(Display.getCurrent() != null ? Display.getCurrent() : Display.getDefault());
    
    /**
     * @param eClass
     * @return A preview image of the graphical Figure used by eClass of type
     */
    public static Image getPreviewImage(EClass eClass, int type) {
        // Only two types
        if(type < 0 || type > 1) {
            return null;
        }
        
        IGraphicalObjectUIProvider provider = (IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProviderForClass(eClass);

        if(!(provider instanceof IArchimateElementUIProvider)) {
            return null;
        }
        
        // No alternate figure
        if(type > 0 && !((IArchimateElementUIProvider)provider).hasAlternateFigure()) {
            return null;
        }
        
        String key = eClass.getName() + type;
        
        Image image = imageRegistry.get(key);
        
        if(image == null) {
            IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
            dmo.setArchimateElement((IArchimateElement)IArchimateFactory.eINSTANCE.create(eClass));
            dmo.setName(provider.getDefaultName());
            dmo.setType(type);
            
            // Special case for text alignment
            if(eClass == IArchimatePackage.eINSTANCE.getGrouping()) {
                dmo.setTextAlignment(ITextAlignment.TEXT_ALIGNMENT_LEFT);
            }
            
            // Use inbuilt default colours
            dmo.setFillColor(ColorFactory.convertColorToString(ColorFactory.getInbuiltDefaultFillColor(dmo)));
            dmo.setLineColor(ColorFactory.convertColorToString(ColorFactory.getInbuiltDefaultLineColor(dmo)));

            GraphicalEditPart editPart = (GraphicalEditPart)provider.createEditPart();
            editPart.setModel(dmo);
            
            IDiagramModelObjectFigure figure = (IDiagramModelObjectFigure)editPart.getFigure();
            figure.setSize(new Dimension(120, 55));
            figure.refreshVisuals();
            figure.validate();

            image = ImageFactory.getAutoScaledImage(DiagramUtils.createImage(figure, 1, 0));
            imageRegistry.put(key, image);
        }
        
        return image;
    }
}
