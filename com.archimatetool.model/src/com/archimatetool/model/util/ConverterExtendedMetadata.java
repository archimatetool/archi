/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import java.util.Map;

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
@SuppressWarnings("nls")
public class ConverterExtendedMetadata extends BasicExtendedMetaData {
    
    private static final Map<String, EClassifier> TYPE_MAP = Map.ofEntries(
        // ArchiMate 2.x
        Map.entry("UsedByRelationship", IArchimatePackage.Literals.SERVING_RELATIONSHIP),
        Map.entry("CommunicationPath", IArchimatePackage.Literals.PATH),
        Map.entry("Network", IArchimatePackage.Literals.COMMUNICATION_NETWORK),
        Map.entry("InfrastructureInterface", IArchimatePackage.Literals.TECHNOLOGY_INTERFACE),
        Map.entry("InfrastructureFunction", IArchimatePackage.Literals.TECHNOLOGY_FUNCTION),
        Map.entry("InfrastructureService", IArchimatePackage.Literals.TECHNOLOGY_SERVICE),

        // Use "z" instead of "s"
        Map.entry("RealisationRelationship", IArchimatePackage.Literals.REALIZATION_RELATIONSHIP),
        Map.entry("SpecialisationRelationship", IArchimatePackage.Literals.SPECIALIZATION_RELATIONSHIP),

        // From very old models
        Map.entry("DiagramModel", IArchimatePackage.Literals.ARCHIMATE_DIAGRAM_MODEL),

        // Since Archi 4.0 we have one Junction type with And/Or attribute
        Map.entry("AndJunction", IArchimatePackage.Literals.JUNCTION),
        Map.entry("OrJunction", IArchimatePackage.Literals.JUNCTION),

        // Bogus concept removed in Archi 4.0
        Map.entry("BusinessActivity", IArchimatePackage.Literals.BUSINESS_PROCESS)
    );
    
    @Override
    public EClassifier getType(EPackage ePackage, String name) {
        if(ePackage == IArchimatePackage.eINSTANCE) {
            EClassifier eClassifier = TYPE_MAP.get(name);
            if(eClassifier != null) {
                return eClassifier;
            }
        }

        return super.getType(ePackage, name);
    }
    
    @Override
    public EStructuralFeature getAttribute(EClass eClass, String namespace, String name) {
        // This was used as a relationship reference in a DiagramModelArchimateConnection
        if("relationship".equals(name) && eClass == IArchimatePackage.eINSTANCE.getDiagramModelArchimateConnection()) {
            return IArchimatePackage.Literals.DIAGRAM_MODEL_ARCHIMATE_CONNECTION__ARCHIMATE_RELATIONSHIP;
        }
        
        return super.getAttribute(eClass, namespace, name);
    }

}
