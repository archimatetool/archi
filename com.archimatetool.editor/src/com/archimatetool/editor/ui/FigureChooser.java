/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.diagram.figures.IDiagramModelArchimateObjectFigure;
import com.archimatetool.editor.diagram.util.DiagramUtils;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateObject;


/**
 * Convenience class to provide information on alternate figures
 * 
 * @author Phillip Beauvoir
 */
public class FigureChooser {
    
    static ImageRegistry imageRegistry = new ImageRegistry();
    
    static List<EClass> FIGURE_CLASSES = new ArrayList<EClass>();
    static {
        FIGURE_CLASSES.add(IArchimatePackage.eINSTANCE.getApplicationComponent());
        FIGURE_CLASSES.add(IArchimatePackage.eINSTANCE.getDevice());
        FIGURE_CLASSES.add(IArchimatePackage.eINSTANCE.getNode());
        FIGURE_CLASSES.add(IArchimatePackage.eINSTANCE.getBusinessProcess());
        
        FIGURE_CLASSES.add(IArchimatePackage.eINSTANCE.getBusinessInterface());
        FIGURE_CLASSES.add(IArchimatePackage.eINSTANCE.getApplicationInterface());
        FIGURE_CLASSES.add(IArchimatePackage.eINSTANCE.getTechnologyInterface());
        
        FIGURE_CLASSES.add(IArchimatePackage.eINSTANCE.getBusinessService());
        FIGURE_CLASSES.add(IArchimatePackage.eINSTANCE.getApplicationService());
        FIGURE_CLASSES.add(IArchimatePackage.eINSTANCE.getTechnologyService());
    };
    
    public static boolean hasAlternateFigure(IArchimateElement element) {
        return FIGURE_CLASSES.contains(element.eClass());
    }
    
    public static Image[] getFigurePreviewImagesForClass(EClass eClass) {
        Image[] images = new Image[2];
        
        images[0] = getImage(eClass, 0);
        images[1] = getImage(eClass, 1);
        
        return images;
    }
    
    private static Image getImage(EClass eClass, int type) {
        String key = eClass.getName() + type;
        
        Image image = imageRegistry.get(key);
        
        if(image == null) {
            IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProvider(eClass);
            if(provider != null) {
                IDiagramModelArchimateObject dmo = IArchimateFactory.eINSTANCE.createDiagramModelArchimateObject();
                dmo.setArchimateElement((IArchimateElement)IArchimateFactory.eINSTANCE.create(eClass));
                dmo.setName(provider.getDefaultName());
                dmo.setFillColor(ColorFactory.convertColorToString(provider.getDefaultColor()));
                dmo.setType(type);
                
                GraphicalEditPart editPart = (GraphicalEditPart)provider.createEditPart();
                editPart.setModel(dmo);
                
                IDiagramModelArchimateObjectFigure figure = (IDiagramModelArchimateObjectFigure)editPart.getFigure();
                figure.setSize(148, 70);
                figure.refreshVisuals();
                figure.validate();
                
                image = DiagramUtils.createImage(figure, 1, 0);
                imageRegistry.put(key, image);
            }
        }
        
        return image;
    }
    
    public static String getDefaultFigurePreferenceKeyForClass(EClass eClass) {
        return eClass.getName() + "Figure"; //$NON-NLS-1$
    }
    
    /**
     * @return The default figure type to use for a new Diagram Element
     */
    public static int getDefaultFigureTypeForNewDiagramElement(IArchimateElement element) {
        return Preferences.STORE.getInt(getDefaultFigurePreferenceKeyForClass(element.eClass()));
    }

}
