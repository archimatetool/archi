/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;

import com.archimatetool.model.IArchimatePackage;

/**
 * Using this class allows us to hook into EMF to return different class types and attributes
 * Used for legacy models
 * 
 * @author Phillip Beauvoir
 */
public class ConverterExtendedMetadata extends BasicExtendedMetaData {
    /*
     * Backwards compatibility for previous versions
     */
    @Override
    public EClassifier getType(EPackage ePackage, String name) {
        if(ePackage == IArchimatePackage.eINSTANCE) {
            switch(name) {
                // From very old models
                case "DiagramModel": //$NON-NLS-1$
                    return IArchimatePackage.Literals.ARCHIMATE_DIAGRAM_MODEL;

                case "UsedByRelationship": //$NON-NLS-1$
                    return IArchimatePackage.Literals.SERVING_RELATIONSHIP;

                case "RealisationRelationship": //$NON-NLS-1$
                    return IArchimatePackage.Literals.REALIZATION_RELATIONSHIP;

                case "SpecialisationRelationship": //$NON-NLS-1$
                    return IArchimatePackage.Literals.SPECIALIZATION_RELATIONSHIP;

                case "AndJunction": //$NON-NLS-1$
                case "OrJunction": //$NON-NLS-1$
                    return IArchimatePackage.Literals.JUNCTION;

                case "CommunicationPath": //$NON-NLS-1$
                    return IArchimatePackage.Literals.PATH;

                case "Network": //$NON-NLS-1$
                    return IArchimatePackage.Literals.COMMUNICATION_NETWORK;

                case "InfrastructureInterface" : //$NON-NLS-1$
                    return IArchimatePackage.Literals.TECHNOLOGY_INTERFACE;
                    
                case "InfrastructureFunction" : //$NON-NLS-1$
                    return IArchimatePackage.Literals.TECHNOLOGY_FUNCTION;
                    
                case "InfrastructureService" : //$NON-NLS-1$
                    return IArchimatePackage.Literals.TECHNOLOGY_SERVICE;
                
                // Bogus concept removed in Archi 4.0
                case "BusinessActivity" : //$NON-NLS-1$
                    return IArchimatePackage.Literals.BUSINESS_PROCESS;
                    
                default:
                    break;
            } 
        }

        return super.getType(ePackage, name);
    }
    
    @Override
    public EStructuralFeature getAttribute(EClass eClass, String namespace, String name) {
        if("relationship".equals(name)) { //$NON-NLS-1$
            return IArchimatePackage.Literals.DIAGRAM_MODEL_ARCHIMATE_CONNECTION__ARCHIMATE_RELATIONSHIP;
        }
        
        return super.getAttribute(eClass, namespace, name);
    }

}
