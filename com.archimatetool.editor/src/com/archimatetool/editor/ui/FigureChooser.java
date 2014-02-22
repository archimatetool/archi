/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.graphics.Image;

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimatePackage;


/**
 * Convenience class to provide information on alternate figures
 * 
 * @author Phillip Beauvoir
 */
public class FigureChooser {
    
    static List<EClass> FIGURE_CLASSES = new ArrayList<EClass>();
    static {
        FIGURE_CLASSES.add(IArchimatePackage.eINSTANCE.getApplicationComponent());
        FIGURE_CLASSES.add(IArchimatePackage.eINSTANCE.getDevice());
        FIGURE_CLASSES.add(IArchimatePackage.eINSTANCE.getNode());
        FIGURE_CLASSES.add(IArchimatePackage.eINSTANCE.getBusinessProcess());
        
        FIGURE_CLASSES.add(IArchimatePackage.eINSTANCE.getBusinessInterface());
        FIGURE_CLASSES.add(IArchimatePackage.eINSTANCE.getApplicationInterface());
        FIGURE_CLASSES.add(IArchimatePackage.eINSTANCE.getInfrastructureInterface());
        
        FIGURE_CLASSES.add(IArchimatePackage.eINSTANCE.getBusinessService());
        FIGURE_CLASSES.add(IArchimatePackage.eINSTANCE.getApplicationService());
        FIGURE_CLASSES.add(IArchimatePackage.eINSTANCE.getInfrastructureService());
    };
    
    public static boolean hasAlternateFigure(IArchimateElement element) {
        return FIGURE_CLASSES.contains(element.eClass());
    }
    
    public static Image[] getFigurePreviewImagesForElement(IArchimateElement element) {
        return getFigurePreviewImagesForClass(element.eClass());
    }
    
    public static Image[] getFigurePreviewImagesForClass(EClass eClass) {
        Image[] images = new Image[2];
        
        if(eClass == IArchimatePackage.eINSTANCE.getBusinessInterface()) {
            images[0] = IArchimateImages.ImageFactory.getImage(IArchimateImages.FIGURE_BUSINESS_INTERFACE1);
            images[1] = IArchimateImages.ImageFactory.getImage(IArchimateImages.FIGURE_BUSINESS_INTERFACE2);
        }
        else if(eClass == IArchimatePackage.eINSTANCE.getApplicationInterface()) {
            images[0] = IArchimateImages.ImageFactory.getImage(IArchimateImages.FIGURE_APPLICATION_INTERFACE1);
            images[1] = IArchimateImages.ImageFactory.getImage(IArchimateImages.FIGURE_APPLICATION_INTERFACE2);
        }
        else if(eClass == IArchimatePackage.eINSTANCE.getInfrastructureInterface()) {
            images[0] = IArchimateImages.ImageFactory.getImage(IArchimateImages.FIGURE_TECHNOLOGY_INTERFACE1);
            images[1] = IArchimateImages.ImageFactory.getImage(IArchimateImages.FIGURE_TECHNOLOGY_INTERFACE2);
        }
        else if(eClass == IArchimatePackage.eINSTANCE.getApplicationComponent()) {
            images[0] = IArchimateImages.ImageFactory.getImage(IArchimateImages.FIGURE_APPLICATION_COMPONENT1);
            images[1] = IArchimateImages.ImageFactory.getImage(IArchimateImages.FIGURE_APPLICATION_COMPONENT2);
        }
        else if(eClass == IArchimatePackage.eINSTANCE.getDevice()) {
            images[0] = IArchimateImages.ImageFactory.getImage(IArchimateImages.FIGURE_TECHNOLOGY_DEVICE1);
            images[1] = IArchimateImages.ImageFactory.getImage(IArchimateImages.FIGURE_TECHNOLOGY_DEVICE2);
        }
        else if(eClass == IArchimatePackage.eINSTANCE.getNode()) {
            images[0] = IArchimateImages.ImageFactory.getImage(IArchimateImages.FIGURE_TECHNOLOGY_NODE1);
            images[1] = IArchimateImages.ImageFactory.getImage(IArchimateImages.FIGURE_TECHNOLOGY_NODE2);
        }
        else if(eClass == IArchimatePackage.eINSTANCE.getBusinessProcess()) {
            images[0] = IArchimateImages.ImageFactory.getImage(IArchimateImages.FIGURE_BUSINESS_PROCESS1);
            images[1] = IArchimateImages.ImageFactory.getImage(IArchimateImages.FIGURE_BUSINESS_PROCESS2);
        }
        else if(eClass == IArchimatePackage.eINSTANCE.getBusinessService()) {
            images[0] = IArchimateImages.ImageFactory.getImage(IArchimateImages.FIGURE_BUSINESS_SERVICE1);
            images[1] = IArchimateImages.ImageFactory.getImage(IArchimateImages.FIGURE_BUSINESS_SERVICE2);
        }
        else if(eClass == IArchimatePackage.eINSTANCE.getApplicationService()) {
            images[0] = IArchimateImages.ImageFactory.getImage(IArchimateImages.FIGURE_APPLICATION_SERVICE1);
            images[1] = IArchimateImages.ImageFactory.getImage(IArchimateImages.FIGURE_APPLICATION_SERVICE2);
        }
        else if(eClass == IArchimatePackage.eINSTANCE.getInfrastructureService()) {
            images[0] = IArchimateImages.ImageFactory.getImage(IArchimateImages.FIGURE_TECHNOLOGY_SERVICE1);
            images[1] = IArchimateImages.ImageFactory.getImage(IArchimateImages.FIGURE_TECHNOLOGY_SERVICE2);
        }
        
        return images;
    }
    
    /**
     * @param dmo
     * @return The default figure type to use for a new Diagram Element
     */
    public static int getDefaultFigureTypeForNewDiagramElement(IArchimateElement element) {
        switch(element.eClass().getClassifierID()) {
            case IArchimatePackage.BUSINESS_INTERFACE:
                return Preferences.STORE.getInt(IPreferenceConstants.BUSINESS_INTERFACE_FIGURE);
                
            case IArchimatePackage.BUSINESS_SERVICE:
                return Preferences.STORE.getInt(IPreferenceConstants.BUSINESS_SERVICE_FIGURE);

            case IArchimatePackage.BUSINESS_PROCESS:
                return Preferences.STORE.getInt(IPreferenceConstants.BUSINESS_PROCESS_FIGURE);

            case IArchimatePackage.APPLICATION_INTERFACE:
                return Preferences.STORE.getInt(IPreferenceConstants.APPLICATION_INTERFACE_FIGURE);
                
            case IArchimatePackage.APPLICATION_SERVICE:
                return Preferences.STORE.getInt(IPreferenceConstants.APPLICATION_SERVICE_FIGURE);

            case IArchimatePackage.INFRASTRUCTURE_INTERFACE:
                return Preferences.STORE.getInt(IPreferenceConstants.TECHNOLOGY_INTERFACE_FIGURE);
                
            case IArchimatePackage.INFRASTRUCTURE_SERVICE:
                return Preferences.STORE.getInt(IPreferenceConstants.TECHNOLOGY_SERVICE_FIGURE);

            case IArchimatePackage.APPLICATION_COMPONENT:
                return Preferences.STORE.getInt(IPreferenceConstants.APPLICATION_COMPONENT_FIGURE);
                
            case IArchimatePackage.NODE:
                return Preferences.STORE.getInt(IPreferenceConstants.TECHNOLOGY_NODE_FIGURE);
                
            case IArchimatePackage.DEVICE:
                return Preferences.STORE.getInt(IPreferenceConstants.TECHNOLOGY_DEVICE_FIGURE);

            default:
                return 0;
        }
    }

}
