/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.model.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;

import uk.ac.bolton.archimate.model.IAccessRelationship;
import uk.ac.bolton.archimate.model.IAggregationRelationship;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateFactory;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IAssignmentRelationship;
import uk.ac.bolton.archimate.model.IAssociationRelationship;
import uk.ac.bolton.archimate.model.IBusinessActor;
import uk.ac.bolton.archimate.model.IBusinessRole;
import uk.ac.bolton.archimate.model.ICompositionRelationship;
import uk.ac.bolton.archimate.model.IRealisationRelationship;
import uk.ac.bolton.archimate.model.IRelationship;
import uk.ac.bolton.archimate.model.IUsedByRelationship;


/**
 * Derived Relations Utils
 * 
 * @author Phillip Beauvoir
 */
public class DerivedRelationsUtils {
    
    static List<EClass> weaklist = new ArrayList<EClass>();
    
    static {
        weaklist.add(IArchimatePackage.eINSTANCE.getAssociationRelationship());
        weaklist.add(IArchimatePackage.eINSTANCE.getAccessRelationship());
        weaklist.add(IArchimatePackage.eINSTANCE.getUsedByRelationship());
        weaklist.add(IArchimatePackage.eINSTANCE.getRealisationRelationship());
        weaklist.add(IArchimatePackage.eINSTANCE.getAssignmentRelationship());
        weaklist.add(IArchimatePackage.eINSTANCE.getAggregationRelationship());
        weaklist.add(IArchimatePackage.eINSTANCE.getCompositionRelationship());
    }
    
    /**
     * @param relation
     * @return true if relation is in a derived chain of relationships
     */
    public static boolean isInDerivedChain(IRelationship relation) {
        if(!isStructuralRelationship(relation)) {
            return false;
        }
        
        // Get relations from source element
        IArchimateElement source = relation.getSource();
        if(source != null && source.getArchimateModel() != null) { // An important guard because the element might have been deleted
            // Source has structural target relations
            for(IRelationship rel : ArchimateModelUtils.getTargetRelationships(source)) {
                if(rel != relation) {
                    if(isStructuralRelationship(rel) && rel.getSource() != relation.getTarget()) {
                        return true;
                    }
                }
            }
            
            // Bi-directional relations
            for(IRelationship rel : ArchimateModelUtils.getSourceRelationships(source)) {
                if(rel != relation) {
                    if(isBidirectionalRelationship(rel)) {
                        return true;
                    }
                    if(isStructuralRelationship(rel) && isBidirectionalRelationship(relation)) {
                        return true;
                    }
                }
            }
        }
        
        // Get relations from target element
        IArchimateElement target = relation.getTarget();
        if(target != null && target.getArchimateModel() != null) { // An important guard because the element might have been deleted
            // Target has structural source relations
            for(IRelationship rel : ArchimateModelUtils.getSourceRelationships(target)) {
                if(rel != relation) {
                    if(isStructuralRelationship(rel) && rel.getTarget() != relation.getSource()) {
                        return true;
                    }
                }
            }
            
            // Bi-directional relations
            for(IRelationship rel : ArchimateModelUtils.getTargetRelationships(target)) {
                if(rel != relation) {
                    if(isBidirectionalRelationship(rel)) {
                        return true;
                    }
                    if(isStructuralRelationship(rel) && isBidirectionalRelationship(relation)) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * @param relation
     * @return True if relation is bi-directional
     */
    public static boolean isBidirectionalRelationship(IRelationship relation) {
        //return relation instanceof IAssociationRelationship || relation instanceof IAssignmentRelationship;
        if(relation instanceof IAssignmentRelationship) {
            return (relation.getSource() instanceof IBusinessActor) && (relation.getTarget() instanceof IBusinessRole);
        }
        return false;
    }
    
    /**
     * @param relation
     * @return True if relation is a structural relationship
     */
    public static boolean isStructuralRelationship(IRelationship relation) {
        return relation instanceof IAssociationRelationship || relation instanceof IAccessRelationship ||
                relation instanceof IUsedByRelationship || relation instanceof IRealisationRelationship ||
                relation instanceof IAssignmentRelationship || relation instanceof IAggregationRelationship
                || relation instanceof ICompositionRelationship;
    }
    
    /**
     * @param element1
     * @param element2
     * @return True if element1 has a direct Structural relationship to element2
     */
    public static boolean hasDirectStructuralRelationship(IArchimateElement element1, IArchimateElement element2) {
        for(IRelationship relation : ArchimateModelUtils.getSourceRelationships(element1)) {
            if(relation.getTarget() == element2 && isStructuralRelationship(relation)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * @param element1
     * @param element2
     * @return The list of chains
     */
    public static List<List<IRelationship>> getDerivedRelationshipChains(IArchimateElement element1, IArchimateElement element2) {
        if(element1 == null || element2 == null) {
            return null;
        }
        
        // Traverse from element1 to element2
        List<List<IRelationship>> chains = _traverse(element1, element2,
                                            new ArrayList<IRelationship>(), new ArrayList<List<IRelationship>>());
        
        if(chains.isEmpty()) {
            return null;
        }
        
        List<List<IRelationship>> result = new ArrayList<List<IRelationship>>(); 
        
        // Check validity of weakest relationship in each chain and remove chain if the weakest relationship is not valid
        for(List<IRelationship> chain : chains) {
            EClass relationshipClass = getWeakestType(chain);
            boolean isValid = ArchimateModelUtils.isValidRelationship(element1, element2, relationshipClass);
            if(isValid) {
                result.add(chain);
            }
            else {
                System.err.println("Found invalid chain:");
                printChain(chain);
            }
        }
        
        return result.isEmpty() ? null : result;
    }
    
    /**
     * @param chain
     * @return The weakest type of relationship in a chain
     */
    public static EClass getWeakestType(List<IRelationship> chain) {
        int weakest = weaklist.size() - 1;
        
        for(IRelationship rel : chain) {
            int index = weaklist.indexOf(rel.eClass());
            if(index < weakest) {
                weakest = index;
            }
        }
        
        return weaklist.get(weakest);
    }
    
    /**
     * Create a derived relation between two elements
     * @param element1
     * @param element2
     * @return the derived relationship or null
     */
    public static IRelationship createDerivedRelationship(IArchimateElement element1, IArchimateElement element2) {
        if(element1 == null || element2 == null) {
            return null;
        }
        
        //System.out.println("-----------------------------------");
        //System.out.println("Starting hunt from " + element1.getName() + " --> " + element2.getName());
        //System.out.println("-----------------------------------");
        
        // Traverse from element1 to element2
        List<List<IRelationship>> chains = _traverse(element1, element2,
                                            new ArrayList<IRelationship>(), new ArrayList<List<IRelationship>>());
        
        if(chains.isEmpty()) {
            return null;
        }
        
        int weakest = weaklist.size() - 1;
        
        // You are the weakest link...goodbye.
        for(List<IRelationship> chain : chains) {
            for(IRelationship rel : chain) {
                //printChain(chain);
                int index = weaklist.indexOf(rel.eClass());
                if(index < weakest) {
                    weakest = index;
                }
            }
        }
        
        EClass relationshipClass = weaklist.get(weakest);
        //System.out.println("Weakest is: " + relationshipClass);
        
        /*
         * Check the validity of the relationship.
         */
        boolean isValid = ArchimateModelUtils.isValidRelationship(element1, element2, relationshipClass);
        if(!isValid) {
            return null;
        }
        
        return (IRelationship)IArchimateFactory.eINSTANCE.create(relationshipClass);
    }
    
    private static List<List<IRelationship>> _traverse(IArchimateElement element, IArchimateElement finalTarget,
                                    List<IRelationship> chain, List<List<IRelationship>> chains) {
        
        //System.out.println("TRAVERSING FROM: " + element.getName());
        
        /*
         * Traverse thru source relationships first
         */
        for(IRelationship rel : ArchimateModelUtils.getSourceRelationships(element)) {
            if(isStructuralRelationship(rel)) {
                if(chain.contains(rel)) {
                    //System.out.println("Reached same relationship in source: " + rel.getName());
                    continue;
                }
                
                IArchimateElement target = rel.getTarget();
                if(finalTarget == target) {
                    if(chain.size() > 0) { // Only chains of length 2 or greater
                        //System.out.println("Reached target from source: " + element.getName());
                        List<IRelationship> result = new ArrayList<IRelationship>(chain);
                        result.add(rel);
                        chains.add(result);
                    }
                }
                else {
                    //System.out.println("Adding from source: " + rel.getName());
                    chain.add(rel);
                    _traverse(target, finalTarget, chain, chains);
                    chain.remove(rel);
                }
            }
        }
        
        /*
         * Then thru the Bi-directional target relationships
         */
        for(IRelationship rel : ArchimateModelUtils.getTargetRelationships(element)) {
            if(isBidirectionalRelationship(rel)) {
                if(chain.contains(rel)) {
                    //System.out.println("Reached same relationship in bi-direct: " + rel.getName());
                    continue;
                }
                
                IArchimateElement source = rel.getSource();
                if(finalTarget == source) {
                    if(chain.size() > 0) { // Only chains of length 2 or greater
                        //System.out.println("Reached target from bi-direct: " + element.getName());
                        List<IRelationship> result = new ArrayList<IRelationship>(chain);
                        result.add(rel);
                        chains.add(result);
                    }
                }
                else {
                    //System.out.println("Adding from bi-direct: " + rel.getName());
                    chain.add(rel);
                    _traverse(source, finalTarget, chain, chains);
                    chain.remove(rel);
                }
            }
        }
        
        return chains;
    }

    
    private static void printChain(List<IRelationship> chain) {
        for(IRelationship rel : chain) {
            System.out.print(rel.getName() + ", ");
        }
        System.out.println();
    }
}
