/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.ui;

import org.eclipse.emf.ecore.EClass;

import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IRelationship;



/**
 * Archimate Names
 * 
 * @author Phillip Beauvoir
 */
public final class ArchimateNames {
    
    /**
     * Get a default human-readable name for an EClass
     * @param eClass
     * @return A name or null
     */
    public static final String getDefaultName(EClass eClass) {
        return getDefaultName(eClass, false);
    }
    
    /**
     * Get a default human-readable short name for an EClass
     * @param eClass
     * @return A name or null
     */
    public static final String getDefaultShortName(EClass eClass) {
        return getDefaultName(eClass, true);
    }
    
    private static final String getDefaultName(EClass eClass, boolean shortName) {
        switch(eClass.getClassifierID()) {
            // Business
            case IArchimatePackage.BUSINESS_ACTIVITY:
                return shortName ? "Activity" : "Business Activity";
            case IArchimatePackage.BUSINESS_ACTOR:
                return shortName ? "Actor" : "Business Actor";
            case IArchimatePackage.BUSINESS_COLLABORATION:
                return shortName ? "Collaboration" : "Business Collaboration";
            case IArchimatePackage.CONTRACT:
                return "Contract";
            case IArchimatePackage.BUSINESS_EVENT:
                return shortName ? "Event" : "Business Event";
            case IArchimatePackage.BUSINESS_FUNCTION:
                return shortName ? "Function" : "Business Function";
            case IArchimatePackage.BUSINESS_INTERACTION:
                return shortName ? "Interaction" : "Business Interaction";
            case IArchimatePackage.BUSINESS_INTERFACE:
                return shortName ? "Interface" : "Business Interface";
            case IArchimatePackage.MEANING:
                return "Meaning";
            case IArchimatePackage.BUSINESS_OBJECT:
                return shortName ? "Object" : "Business Object";
            case IArchimatePackage.BUSINESS_PROCESS:
                return shortName ? "Process" : "Business Process";
            case IArchimatePackage.PRODUCT:
                return "Product";
            case IArchimatePackage.REPRESENTATION:
                return "Representation";
            case IArchimatePackage.BUSINESS_ROLE:
                return shortName ? "Role" : "Business Role";
            case IArchimatePackage.BUSINESS_SERVICE:
                return shortName ? "Service" : "Business Service";
            case IArchimatePackage.VALUE:
                return "Value";
                
            // Application
            case IArchimatePackage.APPLICATION_COLLABORATION:
                return shortName ? "Collaboration" : "Application Collaboration";
            case IArchimatePackage.APPLICATION_COMPONENT:
                return shortName ? "Component" : "Application Component";
            case IArchimatePackage.APPLICATION_FUNCTION:
                return shortName ? "Function" : "Application Function";
            case IArchimatePackage.APPLICATION_INTERACTION:
                return shortName ? "Interaction" : "Application Interaction";
            case IArchimatePackage.APPLICATION_INTERFACE:
                return shortName ? "Interface" : "Application Interface";
            case IArchimatePackage.DATA_OBJECT:
                return "Data Object";
            case IArchimatePackage.APPLICATION_SERVICE:
                return shortName ? "Service" : "Application Service";
                
            // Technology
            case IArchimatePackage.ARTIFACT:
                return "Artifact";
            case IArchimatePackage.COMMUNICATION_PATH:
                return "Communication Path";
            case IArchimatePackage.NETWORK:
                return "Network";
            case IArchimatePackage.INFRASTRUCTURE_INTERFACE:
                return "Infrastructure Interface";
            case IArchimatePackage.INFRASTRUCTURE_SERVICE:
                return "Infrastructure Service";
            case IArchimatePackage.NODE:
                return "Node";
            case IArchimatePackage.SYSTEM_SOFTWARE:
                return "System Software";
            case IArchimatePackage.DEVICE:
                return "Device";
                
            // Junctions
            case IArchimatePackage.JUNCTION:
                return "Junction";
            case IArchimatePackage.AND_JUNCTION:
                return "And Junction";
            case IArchimatePackage.OR_JUNCTION:
                return "Or Junction";
                
            // Relationships
            case IArchimatePackage.ACCESS_RELATIONSHIP:
                return shortName ? "Access": "Access relation";
            case IArchimatePackage.AGGREGATION_RELATIONSHIP:
                return shortName ? "Aggregation" : "Aggregation relation";
            case IArchimatePackage.ASSIGNMENT_RELATIONSHIP:
                return shortName ? "Assignment" : "Assignment relation";
            case IArchimatePackage.ASSOCIATION_RELATIONSHIP:
                return shortName ? "Association" : "Association relation";
            case IArchimatePackage.COMPOSITION_RELATIONSHIP:
                return shortName ? "Composition" : "Composition relation";
            case IArchimatePackage.FLOW_RELATIONSHIP:
                return shortName ? "Flow" : "Flow relation";
            case IArchimatePackage.REALISATION_RELATIONSHIP:
                return shortName ? "Realisation" : "Realisation relation";
            case IArchimatePackage.SPECIALISATION_RELATIONSHIP:
                return shortName ? "Specialisation" : "Specialisation relation";
            case IArchimatePackage.TRIGGERING_RELATIONSHIP:
                return shortName ? "Triggering" : "Triggering relation";
            case IArchimatePackage.USED_BY_RELATIONSHIP:
                return shortName ? "Used By" : "Used By relation";
                

        }
        return "";
    }
    
    public static final String getRelationshipSentence(IRelationship relation) {
        String action = "";
        
        if(relation != null) {
            if(relation.getSource() != null && relation.getTarget() != null) {
                String nameSource = ArchimateLabelProvider.INSTANCE.getLabel(relation.getSource());
                String nameTarget = ArchimateLabelProvider.INSTANCE.getLabel(relation.getTarget());
                
                switch(relation.eClass().getClassifierID()) {
                    case IArchimatePackage.SPECIALISATION_RELATIONSHIP:
                        action = "is a specialisation of";
                        break;

                    case IArchimatePackage.COMPOSITION_RELATIONSHIP:
                        action = "is composed of";
                        break;

                    case IArchimatePackage.AGGREGATION_RELATIONSHIP:
                        action = "aggregates";
                        break;

                    case IArchimatePackage.TRIGGERING_RELATIONSHIP:
                        action = "triggers";
                        break;

                    case IArchimatePackage.FLOW_RELATIONSHIP:
                        action = "flows to";
                        break;

                    case IArchimatePackage.ACCESS_RELATIONSHIP:
                        action = "accesses";
                        break;

                    case IArchimatePackage.ASSOCIATION_RELATIONSHIP:
                        action = "is associated with";
                        break;

                    case IArchimatePackage.ASSIGNMENT_RELATIONSHIP:
                        action = "is assigned to";
                        break;

                    case IArchimatePackage.REALISATION_RELATIONSHIP:
                        action = "realises";
                        break;

                    case IArchimatePackage.USED_BY_RELATIONSHIP:
                        action = "is used by";
                        break;

                    default:
                        break;
                }
                
                action = nameSource + " " + action + " " + nameTarget;
            }
        }
        
        return action;
    }
}
