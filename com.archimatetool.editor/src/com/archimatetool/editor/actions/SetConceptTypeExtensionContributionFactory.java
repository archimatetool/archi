/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.actions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.menus.ExtensionContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.services.IServiceLocator;

import com.archimatetool.editor.model.commands.SetConceptTypeCommandFactory;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.util.ArchimateModelUtils;



/**
 * Change Concept Type ExtensionContributionFactory
 * Add context menu items to Tree and Diagrams
 * 
 * @author Phillip Beauvoir
 */
public class SetConceptTypeExtensionContributionFactory extends ExtensionContributionFactory {
    
    public SetConceptTypeExtensionContributionFactory() {
    }

    @Override
    public void createContributionItems(IServiceLocator serviceLocator, IContributionRoot additions) {
        ISelectionService selectionService = serviceLocator.getService(ISelectionService.class);
        
        IStructuredSelection selection = (IStructuredSelection)selectionService.getSelection();
        if(selection == null) {
            return;
        }
        
        // Get all selected concepts
        
        Set<IArchimateElement> selectedElements = new HashSet<>();
        Set<IArchimateRelationship> selectedRelations = new HashSet<>();
        
        for(Object o : selection) {
            IArchimateConcept concept = null;
            if(o instanceof IArchimateConcept) {
                concept = (IArchimateConcept)o;
            }
            else if(o instanceof IAdaptable adaptable) {
                concept = adaptable.getAdapter(IArchimateConcept.class);
            }
            if(concept instanceof IArchimateElement && !(concept.eClass() == IArchimatePackage.eINSTANCE.getJunction())) { // Not Junctions
                selectedElements.add((IArchimateElement)concept);
            }
            else if(concept instanceof IArchimateRelationship relationship) {
                selectedRelations.add(relationship);
            }
        }
        
        if(selectedElements.isEmpty() && selectedRelations.isEmpty()) {
            return;
        }
        
        MenuManager menuManager = new MenuManager(Messages.SetConceptTypeExtensionContributionFactory_0);

        if(!selectedElements.isEmpty()) {
            MenuManager subMenu = new MenuManager(FolderType.STRATEGY.getLabel());
            menuManager.add(subMenu);
            for(EClass eClass : ArchimateModelUtils.getStrategyClasses()) {
                subMenu.add(createElementTypeAction(eClass, selectedElements));
            }
            
            subMenu = new MenuManager(FolderType.BUSINESS.getLabel());
            menuManager.add(subMenu);
            for(EClass eClass : ArchimateModelUtils.getBusinessClasses()) {
                subMenu.add(createElementTypeAction(eClass, selectedElements));
            }
            
            subMenu = new MenuManager(FolderType.APPLICATION.getLabel());
            menuManager.add(subMenu);
            for(EClass eClass : ArchimateModelUtils.getApplicationClasses()) {
                subMenu.add(createElementTypeAction(eClass, selectedElements));
            }
            
            subMenu = new MenuManager(StringUtils.escapeAmpersandsInText(FolderType.TECHNOLOGY.getLabel()));
            menuManager.add(subMenu);
            for(EClass eClass : ArchimateModelUtils.getTechnologyClasses()) {
                subMenu.add(createElementTypeAction(eClass, selectedElements));
            }
            subMenu.add(new Separator());
            for(EClass eClass : ArchimateModelUtils.getPhysicalClasses()) {
                subMenu.add(createElementTypeAction(eClass, selectedElements));
            }
            
            subMenu = new MenuManager(FolderType.MOTIVATION.getLabel());
            menuManager.add(subMenu);
            for(EClass eClass : ArchimateModelUtils.getMotivationClasses()) {
                subMenu.add(createElementTypeAction(eClass, selectedElements));
            }
            
            subMenu = new MenuManager(StringUtils.escapeAmpersandsInText(FolderType.IMPLEMENTATION_MIGRATION.getLabel()));
            menuManager.add(subMenu);
            for(EClass eClass : ArchimateModelUtils.getImplementationMigrationClasses()) {
                subMenu.add(createElementTypeAction(eClass, selectedElements));
            }
            
            subMenu = new MenuManager(FolderType.OTHER.getLabel());
            menuManager.add(subMenu);
            for(EClass eClass : ArchimateModelUtils.getOtherClasses()) {
                subMenu.add(createElementTypeAction(eClass, selectedElements));
            }
        }
        
        if(!selectedRelations.isEmpty()) {
            MenuManager subMenu = menuManager;
            
            if(!selectedElements.isEmpty()) {
                subMenu = new MenuManager(Messages.SetConceptTypeExtensionContributionFactory_1);
                menuManager.add(subMenu);
            }
            
            for(EClass eClass : ArchimateModelUtils.getRelationsClasses()) {
                subMenu.add(createRelationTypeAction(eClass, selectedRelations));
            }
        }

        additions.addContributionItem(menuManager, null);
    }
    
    private IAction createElementTypeAction(EClass eClass, Set<IArchimateElement> elements) {
        IAction action = new Action(ArchiLabelProvider.INSTANCE.getDefaultName(eClass)) {
            @Override
            public void run() {
                boolean hasInvalidConnections = false;
                
                for(IArchimateElement element : elements) {
                    if(!SetConceptTypeCommandFactory.isValidTypeForElement(eClass, element, elements)) {
                        hasInvalidConnections = true;
                    }
                }
                
                if(hasInvalidConnections && !MessageDialog.openConfirm(null, Messages.SetConceptTypeExtensionContributionFactory_0,
                        Messages.SetConceptTypeExtensionContributionFactory_2)) {
                    return;
                }
                
                changeElementTypes(eClass, elements);
            }
        };

        action.setEnabled(false);
        
        // Enable menu item if any selected element is different to the target type
        for(IArchimateElement e : elements) {
            if(!e.eClass().equals(eClass)) {
                action.setEnabled(true);
            }
        }
        
        action.setImageDescriptor(ArchiLabelProvider.INSTANCE.getImageDescriptor(eClass));
        return action;
    }
    
    private IAction createRelationTypeAction(EClass eClass, Set<IArchimateRelationship> relations) {
        IAction action = new Action(ArchiLabelProvider.INSTANCE.getDefaultName(eClass)) {
            @Override
            public void run() {
                changeRelationTypes(eClass, relations);
            }
        };
        
        action.setEnabled(false);
        
        // Enable menu item if any selected relation is different to the target type and is valid
        for(IArchimateRelationship relation : relations) {
            if(!relation.eClass().equals(eClass) && SetConceptTypeCommandFactory.isValidTypeForRelationship(eClass, relation)) {
                action.setEnabled(true);
            }
        }
        
        action.setImageDescriptor(ArchiLabelProvider.INSTANCE.getImageDescriptor(eClass));
        return action;
    }
    
    private void changeElementTypes(EClass eClass, Set<IArchimateElement> elements) {
        /*
         * If changing types from more than one model we need to use the
         * Command Stack allocated to each model. And then execute one CompoundCommand per Command Stack.
         */
        Map<IArchimateModel, Set<IArchimateElement>> elementsMap = new HashMap<>();
        
        for(IArchimateElement element : elements) {
            Set<IArchimateElement> elementsSet = elementsMap.get(element.getArchimateModel());
            if(elementsSet == null) {
                elementsSet = new HashSet<>();
                elementsMap.put(element.getArchimateModel(), elementsSet);
            }
            elementsSet.add(element);
        }
        
        for(Entry<IArchimateModel, Set<IArchimateElement>> entry : elementsMap.entrySet()) {
            CommandStack stack = (CommandStack)entry.getKey().getAdapter(CommandStack.class);
            if(stack != null) {
                CompoundCommand cmd = SetConceptTypeCommandFactory.createSetElementTypeCommand(eClass, entry.getValue());
                stack.execute(cmd);
            }
        }
    }
    
    private void changeRelationTypes(EClass eClass, Set<IArchimateRelationship> relations) {
        /*
         * If changing types from more than one model we need to use the
         * Command Stack allocated to each model. And then execute one CompoundCommand per Command Stack.
         */
        Map<IArchimateModel, Set<IArchimateRelationship>> relationsMap = new HashMap<>();
        
        for(IArchimateRelationship relation : relations) {
            Set<IArchimateRelationship> set = relationsMap.get(relation.getArchimateModel());
            if(set == null) {
                set = new HashSet<>();
                relationsMap.put(relation.getArchimateModel(), set);
            }
            set.add(relation);
        }
        
        for(Entry<IArchimateModel, Set<IArchimateRelationship>> entry : relationsMap.entrySet()) {
            CommandStack stack = (CommandStack)entry.getKey().getAdapter(CommandStack.class);
            if(stack != null) {
                CompoundCommand cmd = SetConceptTypeCommandFactory.createSetRelationTypeCommand(eClass, entry.getValue());
                stack.execute(cmd);
            }
        }
    }
}
