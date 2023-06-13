package com.archimatetool.editor.actions;

import java.util.HashSet;
import java.util.Hashtable;
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

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.model.commands.NonNotifyingCompoundCommand;
import com.archimatetool.editor.model.commands.SetConceptTypeCommandFactory;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
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
            else if(o instanceof IAdaptable) {
                concept = ((IAdaptable)o).getAdapter(IArchimateConcept.class);
            }
            if(concept instanceof IArchimateElement && !(concept.eClass() == IArchimatePackage.eINSTANCE.getJunction())) { // Not Junctions
                selectedElements.add((IArchimateElement)concept);
            }
            else if(concept instanceof IArchimateRelationship) {
                selectedRelations.add((IArchimateRelationship)concept);
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
                    if(!SetConceptTypeCommandFactory.isValidTypeForConcept(eClass, element)) {
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
        for(IArchimateRelationship r : relations) {
            if(!r.eClass().equals(eClass) && ArchimateModelUtils.isValidRelationship(r.getSource().eClass(), r.getTarget().eClass(), eClass)) {
                action.setEnabled(true);
            }
        }
        
        action.setImageDescriptor(ArchiLabelProvider.INSTANCE.getImageDescriptor(eClass));
        return action;
    }
    
    private void changeElementTypes(EClass eClass, Set<IArchimateElement> elements) {
        /*
         * If changing types from more than one model we need to use the
         * Command Stack allocated to each model. And then allocate one CompoundCommand per Command Stack.
         */
        Hashtable<CommandStack, CompoundCommand> commandMap = new Hashtable<CommandStack, CompoundCommand>();

        for(IArchimateElement element : elements) {
            CompoundCommand compoundCmd = getCompoundCommand(element, commandMap);
            if(compoundCmd != null) {
                compoundCmd.add(SetConceptTypeCommandFactory.createSetElementTypeCommand(eClass, element,
                        ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.ADD_DOCUMENTATION_NOTE_ON_RELATION_CHANGE)));
            }
        }
        
        // Execute the Commands on the CommandStack(s) - there could be more than one if more than one model open in the Tree
        for(Entry<CommandStack, CompoundCommand> entry : commandMap.entrySet()) {
            entry.getKey().execute(entry.getValue());
        }
    }
    
    private void changeRelationTypes(EClass eClass, Set<IArchimateRelationship> relations) {
        /*
         * If changing types from more than one model we need to use the
         * Command Stack allocated to each model. And then allocate one CompoundCommand per Command Stack.
         */
        Hashtable<CommandStack, CompoundCommand> commandMap = new Hashtable<CommandStack, CompoundCommand>();

        for(IArchimateRelationship relation : relations) {
            CompoundCommand compoundCmd = getCompoundCommand(relation, commandMap);
            if(compoundCmd != null) {
                compoundCmd.add(SetConceptTypeCommandFactory.createSetRelationTypeCommand(eClass, relation));
            }
        }
        
        // Execute the Commands on the CommandStack(s) - there could be more than one if more than one model open in the Tree
        for(Entry<CommandStack, CompoundCommand> entry : commandMap.entrySet()) {
            entry.getKey().execute(entry.getValue());
        }
    }

    /**
     * Get, and if need be create, a CompoundCommand to which to change the type for each concept in a model
     */
    private CompoundCommand getCompoundCommand(IArchimateConcept concept, Hashtable<CommandStack, CompoundCommand> commandMap) {
        // Get the Command Stack registered to the object
        CommandStack stack = (CommandStack)concept.getAdapter(CommandStack.class);
        if(stack == null) {
            System.err.println("CommandStack was null in getCompoundCommand"); //$NON-NLS-1$
            return null;
        }
        
        // Now get or create a Compound Command
        CompoundCommand compoundCommand = commandMap.get(stack);
        if(compoundCommand == null) {
            compoundCommand = new NonNotifyingCompoundCommand();
            commandMap.put(stack, compoundCommand);
        }
        
        return compoundCommand;
    }
}
