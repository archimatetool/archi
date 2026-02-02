/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageDataProvider;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.diagram.figures.IDiagramModelObjectFigure;
import com.archimatetool.editor.ui.factory.IArchimateElementUIProvider;
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
    private static final ImageRegistry imageRegistry = new ImageRegistry(Display.getCurrent() != null ? Display.getCurrent() : Display.getDefault());
    
    private static final int FIGURE_WIDTH = 120;
    private static final int FIGURE_HEIGHT = 55;
    
    /**
     * @param eClass
     * @return A preview image of the graphical Figure used by eClass of type
     */
    public static Image getPreviewImage(EClass eClass, int type) {
        // Only two types
        if(type < 0 || type > 1) {
            return null;
        }
        
        if(!(ObjectUIFactory.INSTANCE.getProviderForClass(eClass) instanceof IArchimateElementUIProvider uiProvider)) {
            return null;
        }
        
        // No alternate figure
        if(type > 0 && !uiProvider.hasAlternateFigure()) {
            return null;
        }
        
        String key = eClass.getName() + type;
        
        Image image = imageRegistry.get(key);
        
        if(image == null) {
            IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
            dmo.setArchimateElement((IArchimateElement)IArchimateFactory.eINSTANCE.create(eClass));
            dmo.setName(uiProvider.getDefaultName());
            dmo.setType(type);
            dmo.setDeriveElementLineColor(false);
            
            // Special case for text alignment
            if(eClass == IArchimatePackage.eINSTANCE.getGrouping()) {
                dmo.setTextAlignment(ITextAlignment.TEXT_ALIGNMENT_LEFT);
            }
            
            // Use inbuilt default colours
            dmo.setFillColor(ColorFactory.convertColorToString(ColorFactory.getInbuiltDefaultFillColor(dmo)));
            dmo.setLineColor(ColorFactory.convertColorToString(ColorFactory.getInbuiltDefaultLineColor(dmo)));

            GraphicalEditPart editPart = (GraphicalEditPart)uiProvider.createEditPart();
            editPart.setModel(dmo);
            
            IDiagramModelObjectFigure figure = (IDiagramModelObjectFigure)editPart.getFigure();
            figure.setSize(new Dimension(FIGURE_WIDTH, FIGURE_HEIGHT));
            figure.refreshVisuals();
            figure.validate();
            
            image = new Image(Display.getDefault(), (ImageDataProvider) zoom -> {
                Image tmp = new Image(Display.getCurrent(), FIGURE_WIDTH, FIGURE_HEIGHT);
                GC gc = new GC(tmp);
                SWTGraphics graphics = new SWTGraphics(gc);
                figure.paint(graphics);
                ImageData imageData = tmp.getImageData(zoom);
                
                tmp.dispose();
                graphics.dispose();
                gc.dispose();

                return imageData;
            });
            
            // Use ImageGcDrawer in later Eclipse
//            image = new Image(Display.getDefault(), (gc, width, height) -> {
//                SWTGraphics graphics = new SWTGraphics(gc);
//                figure.paint(graphics);
//                graphics.dispose();
//            }, FIGURE_WIDTH, FIGURE_HEIGHT);

            imageRegistry.put(key, image);
        }
        
        return image;
    }
}
