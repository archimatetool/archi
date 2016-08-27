/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;

import com.archimatetool.model.IAccessRelationship;
import com.archimatetool.model.IAggregationRelationship;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IAssignmentRelationship;
import com.archimatetool.model.IAssociationRelationship;
import com.archimatetool.model.ICompositionRelationship;
import com.archimatetool.model.IRealizationRelationship;
import com.archimatetool.model.IServingRelationship;



/**
 * Derived Relations Utils
 * 
 * @author Phillip Beauvoir
 */
public class DerivedRelationsUtils {
    
    public static class TooComplicatedException extends Exception {

    }
    
    static List<EClass> weaklist = new ArrayList<EClass>();
    
    static {
        weaklist.add(IArchimatePackage.eINSTANCE.getAssociationRelationship());
        weaklist.add(IArchimatePackage.eINSTANCE.getAccessRelationship());
        weaklist.add(IArchimatePackage.eINSTANCE.getServingRelationship());
        weaklist.add(IArchimatePackage.eINSTANCE.getRealizationRelationship());
        weaklist.add(IArchimatePackage.eINSTANCE.getAssignmentRelationship());
        weaklist.add(IArchimatePackage.eINSTANCE.getAggregationRelationship());
        weaklist.add(IArchimatePackage.eINSTANCE.getCompositionRelationship());
    }
    
    /**
     * @param relation
     * @return true if relation is in a derived chain of relationships
     */
    public static boolean isInDerivedChain(IArchimateRelationship relation) {
        if(!isStructuralRelationship(relation)) {
            return false;
        }
        
        // Get relations from source element
        IArchimateConcept source = relation.getSource();
        if(source != null && source.getArchimateModel() != null) { // An important guard because the element might have been deleted
            // Source has structural target relations
            for(IArchimateRelationship rel : source.getTargetRelationships()) {
                if(rel != relation) {
                    if(isStructuralRelationship(rel) && rel.getSource() != relation.getTarget()) {
                        return true;
                    }
                }
            }
            
            // Bi-directional relations
            for(IArchimateRelationship rel : source.getSourceRelationships()) {
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
        IArchimateConcept target = relation.getTarget();
        if(target != null && target.getArchimateModel() != null) { // An important guard because the element might have been deleted
            // Target has structural source relations
            for(IArchimateRelationship rel : target.getSourceRelationships()) {
                if(rel != relation) {
                    if(isStructuralRelationship(rel) && rel.getTarget() != relation.getSource()) {
                        return true;
                    }
                }
            }
            
            // Bi-directional relations
            for(IArchimateRelationship rel : target.getTargetRelationships()) {
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
    public static boolean isBidirectionalRelationship(IArchimateRelationship relation) {
        return relation instanceof IAssociationRelationship;
    }
    
    /**
     * @param relation
     * @return True if relation is a structural relationship
     */
    public static boolean isStructuralRelationship(IArchimateRelationship relation) {
        return relation instanceof IAssociationRelationship || relation instanceof IAccessRelationship ||
                relation instanceof IServingRelationship || relation instanceof IRealizationRelationship ||
                relation instanceof IAssignmentRelationship || relation instanceof IAggregationRelationship
                || relation instanceof ICompositionRelationship;
    }
    
    /**
     * @param element1
     * @param element2
     * @return True if element1 has a direct Structural relationship to element2
     */
    public static boolean hasDirectStructuralRelationship(IArchimateElement element1, IArchimateElement element2) {
        for(IArchimateRelationship relation : element1.getSourceRelationships()) {
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
     * @throws TooComplicatedException 
     */
    public static List<List<IArchimateRelationship>> getDerivedRelationshipChains(IArchimateElement element1, IArchimateElement element2) throws TooComplicatedException {
        if(element1 == null || element2 == null) {
            return null;
        }
        
        // Traverse from element1 to element2
        List<List<IArchimateRelationship>> chains = findChains(element1, element2);
        
        if(chains.isEmpty()) {
            return null;
        }
        
        List<List<IArchimateRelationship>> result = new ArrayList<List<IArchimateRelationship>>(); 
        
        // Check validity of weakest relationship in each chain and remove chain if the weakest relationship is not valid
        for(List<IArchimateRelationship> chain : chains) {
            EClass relationshipClass = getWeakestType(chain);
            boolean isValid = ArchimateModelUtils.isValidRelationship(element1, element2, relationshipClass);
            if(isValid) {
                result.add(chain);
            }
            else {
                System.err.println("Found invalid chain:"); //$NON-NLS-1$
                _printChain(chain, element2);
            }
        }
        
        return result.isEmpty() ? null : result;
    }
    
    /**
     * Create a derived relation between two elements
     * @param element1
     * @param element2
     * @return the derived relationship or null
     * @throws TooComplicatedException 
     */
    public static IArchimateRelationship createDerivedRelationship(IArchimateElement element1, IArchimateElement element2) throws TooComplicatedException {
        if(element1 == null || element2 == null) {
            return null;
        }
        
        //System.out.println("-----------------------------------");
        //System.out.println("Starting hunt from " + element1.getName() + " --> " + element2.getName());
        //System.out.println("-----------------------------------");
        
        // Traverse from element1 to element2
        List<List<IArchimateRelationship>> chains = findChains(element1, element2);
        
        if(chains.isEmpty()) {
            return null;
        }
        
        int weakest = weaklist.size() - 1;
        
        // You are the weakest link...goodbye.
        for(List<IArchimateRelationship> chain : chains) {
            for(IArchimateRelationship rel : chain) {
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
        
        return (IArchimateRelationship)IArchimateFactory.eINSTANCE.create(relationshipClass);
    }
    
    /**
     * @param chain
     * @return The weakest type of relationship in a chain of relationships
     */
    public static EClass getWeakestType(List<IArchimateRelationship> chain) {
        int weakest = weaklist.size() - 1;
        
        for(IArchimateRelationship rel : chain) {
            int index = weaklist.indexOf(rel.eClass());
            if(index < weakest) {
                weakest = index;
            }
        }
        
        return weaklist.get(weakest);
    }
    
   
    // =================================================================================== 
    // TRAVERSE PATHS
    // ===================================================================================
    
    // Too complicated
    private static final int ITERATION_LIMIT = 20000;
    
    private static IArchimateElement finalTarget;
    private static List<IArchimateRelationship> temp_chain;
    private static List<List<IArchimateRelationship>> chains;
    private static int weakestFound;
    private static int iterations;
    
    /**
     * @param sourceElement
     * @param targetElement
     * @return Find all the chains between element and finalTarget
     * @throws TooComplicatedException 
     */
    private static List<List<IArchimateRelationship>> findChains(IArchimateElement sourceElement, IArchimateElement targetElement) throws TooComplicatedException {
        finalTarget = targetElement;
        temp_chain = new ArrayList<IArchimateRelationship>();
        chains = new ArrayList<List<IArchimateRelationship>>();
        weakestFound = weaklist.size();
        iterations = 0;
        
        // Easy win check
        if(!_hasTargetElementValidRelations(targetElement)) {
            return chains;
        }
        
        _traverse(sourceElement);
        
        return chains;
    }
    
    private static void _traverse(IArchimateConcept concept) throws TooComplicatedException {
        // We found the lowest weakest so no point going on
        if(weakestFound == 0) {
            return;
        }
        
        // Too deep
        if(++iterations > ITERATION_LIMIT) {
            throw new TooComplicatedException();
        }
        
        //System.out.println("TRAVERSING FROM: " + element.getName());
        
        /*
         * Traverse thru source relationships first
         */
        for(IArchimateRelationship rel : concept.getSourceRelationships()) {
            if(isStructuralRelationship(rel)) {
                _addRelationshipToTempChain(rel, true);
            }
        }
        
        /*
         * Then thru the Bi-directional target relationships
         */
        for(IArchimateRelationship rel : concept.getTargetRelationships()) {
            if(isBidirectionalRelationship(rel)) {
                _addRelationshipToTempChain(rel, false);
            }
        }
    }

    private static void _addRelationshipToTempChain(IArchimateRelationship relation, boolean forwards) throws TooComplicatedException {
        // Reached the same relationship so go back one (this guards against a loop)
        if(temp_chain.contains(relation)) {
            //System.out.println("Reached same relationship in chain: " + relation.getName());
            return;
        }
        
        // If we get the target element we are traversing fowards, otherwise backwards from a bi-directional check
        IArchimateConcept concept = forwards ? relation.getTarget() : relation.getSource();
        
        // Arrived at target
        if(finalTarget == concept) {
            if(temp_chain.size() > 0) { // Only chains of length 2 or greater
                //System.out.println("Reached target from: " + element.getName());
                List<IArchimateRelationship> chain = new ArrayList<IArchimateRelationship>(temp_chain); // make a copy because temp_chain will have relation removed, below
                chain.add(relation);
                
                // Duplicate check - there must be a loop?
                if(_containsChain(chain, chains)) {
                    System.err.println("Duplicate chain:"); //$NON-NLS-1$
                    _printChain(chain, finalTarget);
                }

                EClass weakest = getWeakestType(chain);
                int index = weaklist.indexOf(weakest);
                if(index < weakestFound) {
                    weakestFound = index;
                }

                chains.add(chain);
            }
        }
        // Move onto next element in chain
        else {
            //System.out.println("Adding to temp chain: " + relation.getName());
            temp_chain.add(relation);
            _traverse(concept);
            temp_chain.remove(relation); // back up
        }
    }
    
    /*
     * This is an easy win check. Traversing will soon find if the source Element has no connections but it may take some
     * time to traverse to eventually find out that the target element had none. If the targte element has no incoming or 
     * bi-directional relationships then don't bother traversing.
     */
    private static boolean _hasTargetElementValidRelations(IArchimateElement targetElement) {
        for(IArchimateRelationship relation : targetElement.getSourceRelationships()) {
            if(isBidirectionalRelationship(relation)) {
                return true;
            }
        }
        
        for(IArchimateRelationship relation : targetElement.getTargetRelationships()) {
            if(isStructuralRelationship(relation)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Check if chain already exists in list of collected chains
     */
    private static boolean _containsChain(List<IArchimateRelationship> chain, List<List<IArchimateRelationship>> chains) {
        for(List<IArchimateRelationship> stored_chain : chains) {
            if(stored_chain.size() == chain.size()) { // check only on same length
                boolean result = true; // assume the same
                for(int i = 0; i < chain.size(); i++) {
                    if(stored_chain.get(i) != chain.get(i)) { // relation is different so not the same
                        result = false;
                        break;
                    }
                }
                if(result) { // was the same
                    return true;
                }
            }
        }
        
        return false;
    }
    
    // =================================================================================== 
    // DEBUGGING PRINT
    // =================================================================================== 
    
    private static void _printChain(List<IArchimateRelationship> chain, IArchimateElement finalTarget) {
        String s = chain.get(0).getSource().getName();
        s += " --> "; //$NON-NLS-1$
        for(int i = 1; i < chain.size(); i++) {
            IArchimateRelationship relation = chain.get(i);
            s += _getRelationshipText(chain, relation);
            if(isBidirectionalRelationship(relation)) {
                s += " <-> "; //$NON-NLS-1$
            }
            else {
                s += " --> "; //$NON-NLS-1$
            }
        }
        s += finalTarget.getName();
        
        System.out.println(s);
    }
    
    private static String _getRelationshipText(List<IArchimateRelationship> chain, IArchimateRelationship relation) {
        if(isBidirectionalRelationship(relation)) {
            int index = chain.indexOf(relation);
            if(index > 0) {
                IArchimateRelationship previous = chain.get(index - 1);
                if(relation.getTarget() == previous.getTarget()) {
                    return relation.getTarget().getName();
                }
            }
            return relation.getSource().getName();
        }
        else {
            return relation.getSource().getName();
        }
    }
}
