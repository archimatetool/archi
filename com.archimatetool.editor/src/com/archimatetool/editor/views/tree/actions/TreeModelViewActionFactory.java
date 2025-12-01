/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.actions;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.editor.views.tree.commands.NewDiagramCommand;
import com.archimatetool.editor.views.tree.commands.NewElementCommand;
import com.archimatetool.editor.views.tree.commands.NewFolderCommand;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IProfile;
import com.archimatetool.model.ISketchModel;
import com.archimatetool.model.util.ArchimateModelUtils;



/**
 * Factory for Tree Model Viewer to create "New" type actions
 * Each Action will create a new Ecore Archimate Element and add it to the Ecore Model
 * 
 * Also returns Images for Ecore types and Tree Model types
 * 
 * @author Phillip Beauvoir
 */
public class TreeModelViewActionFactory {

    public static final TreeModelViewActionFactory INSTANCE = new TreeModelViewActionFactory();

    private TreeModelViewActionFactory() {
    }

    /**
     * @param selected
     * @return A List (perhaps empty) of Actions for a given selected object
     */
    public List<IAction> getNewObjectActions(Object selected) {
        List<IAction> actionList = new ArrayList<IAction>();
        
        // If we have selected a leaf object, go up to its parent
        if(selected instanceof IArchimateConcept || selected instanceof IDiagramModel) {
            selected = ((EObject)selected).eContainer();
        }
        
        // We haven't got a parent folder
        if(!(selected instanceof IFolder)) {
            return actionList;
        }
        
        IFolder selectedFolder = (IFolder)selected;
        
        actionList.add(createNewFolderAction((IFolder)selected));
        actionList.add(null);
        
        // Find the topmost folder type
        IFolder topMostFolder = selectedFolder;
        while(topMostFolder.eContainer() instanceof IFolder) {
            topMostFolder = (IFolder)topMostFolder.eContainer();
        }
        
        switch(topMostFolder.getType()) {
            case STRATEGY:
                actionList.addAll(createConceptActions(ArchimateModelUtils.getStrategyClasses(), selectedFolder));
                actionList.add(null);
                actionList.addAll(createSpecializationActions(ArchimateModelUtils.getStrategyClasses(), selectedFolder));
                break;

            case BUSINESS:
                actionList.addAll(createConceptActions(ArchimateModelUtils.getBusinessClasses(), selectedFolder));
                actionList.add(null);
                actionList.addAll(createSpecializationActions(ArchimateModelUtils.getBusinessClasses(), selectedFolder));
                break;

            case APPLICATION:
                actionList.addAll(createConceptActions(ArchimateModelUtils.getApplicationClasses(), selectedFolder));
                actionList.add(null);
                actionList.addAll(createSpecializationActions(ArchimateModelUtils.getApplicationClasses(), selectedFolder));
                break;

            case MOTIVATION:
                actionList.addAll(createConceptActions(ArchimateModelUtils.getMotivationClasses(), selectedFolder));
                actionList.add(null);
                actionList.addAll(createSpecializationActions(ArchimateModelUtils.getMotivationClasses(), selectedFolder));
                break;

            case IMPLEMENTATION_MIGRATION:
                actionList.addAll(createConceptActions(ArchimateModelUtils.getImplementationMigrationClasses(), selectedFolder));
                actionList.add(null);
                actionList.addAll(createSpecializationActions(ArchimateModelUtils.getImplementationMigrationClasses(), selectedFolder));
                break;

            case TECHNOLOGY:
                // Technology
                actionList.addAll(createConceptActions(ArchimateModelUtils.getTechnologyClasses(), selectedFolder));
                actionList.add(null);
                // Physical
                actionList.addAll(createConceptActions(ArchimateModelUtils.getPhysicalClasses(), selectedFolder));
                
                // Specializations
                actionList.add(null);
                actionList.addAll(createSpecializationActions(ArchimateModelUtils.getPhysicalClasses(), selectedFolder));
                actionList.addAll(createSpecializationActions(ArchimateModelUtils.getTechnologyClasses(), selectedFolder));
                break;

            case OTHER:
                // Grouping and Location
                actionList.addAll(createConceptActions(ArchimateModelUtils.getOtherClasses(), selectedFolder));
                actionList.add(null);
                // Connectors
                actionList.addAll(createConceptActions(ArchimateModelUtils.getConnectorClasses(), selectedFolder));
                
                // Specializations
                actionList.add(null);
                actionList.addAll(createSpecializationActions(ArchimateModelUtils.getOtherClasses(), selectedFolder));
                actionList.addAll(createSpecializationActions(ArchimateModelUtils.getConnectorClasses(), selectedFolder));
                break;
                
            case DIAGRAMS:
                actionList.add(createNewArchimateDiagramAction(selectedFolder));
                actionList.add(createNewSketchAction(selectedFolder));
                break;

            default:
                break;
        }
        
        // Remove any trailing separator
        if(actionList.get(actionList.size() - 1) == null) {
            actionList.remove(actionList.size() - 1);
        }

        return actionList;
    }
    
    private List<IAction> createConceptActions(EClass[] classes, IFolder folder) {
        List<IAction> actions = new ArrayList<>();
        
        // Add actions for ArchiMate classes
        for(EClass eClass : classes) {
            IAction action = createNewElementAction(folder, eClass);
            actions.add(action);
        }
        
        return actions;
    }
    
    private List<IAction> createSpecializationActions(EClass[] classes, IFolder folder) {
        List<IAction> actions = new ArrayList<>();
        
        if(!ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.SHOW_SPECIALIZATIONS_IN_MODEL_TREE_MENU)
                || folder.getArchimateModel().getProfiles().isEmpty()) {
            return actions;
        }
        
        List<IProfile> profiles = new ArrayList<>(folder.getArchimateModel().getProfiles());
        
        Collator collator = Collator.getInstance();
        profiles.sort((p1, p2) -> collator.compare(p1.getName(), p2.getName()));
        
        Set<EClass> classesSet = Set.of(classes);
        
        for(IProfile profile : profiles) {
            if(classesSet.contains(profile.getConceptClass())) {
                IAction action = createNewSpecializationAction(folder, profile);
                actions.add(action);
            }
        }
        
        return actions;
    }
    
    private IAction createNewElementAction(final IFolder folder, final EClass eClass) {
        IAction action = new Action(ArchiLabelProvider.INSTANCE.getDefaultName(eClass)) {
            @Override
            public void run() {
                // Create a new Archimate Element, set its name
                IArchimateElement element = (IArchimateElement)IArchimateFactory.eINSTANCE.create(eClass);
                element.setName(getText());
                // Execute Command
                Command cmd = new NewElementCommand(folder, element);
                CommandStack commandStack = (CommandStack)folder.getAdapter(CommandStack.class);
                commandStack.execute(cmd);
            }
        };

        action.setImageDescriptor(ArchiLabelProvider.INSTANCE.getImageDescriptor(eClass));
        return action;
    }
    
    private IAction createNewSpecializationAction(final IFolder folder, IProfile profile) {
        IAction action = new Action(profile.getName()) {
            @Override
            public void run() {
                // Create a new Archimate Element, set its name and profile
                IArchimateElement element = (IArchimateElement)IArchimateFactory.eINSTANCE.create(profile.getConceptClass());
                element.setName(profile.getName());
                element.getProfiles().add(profile);
                // Execute Command
                Command cmd = new NewElementCommand(folder, element);
                CommandStack commandStack = (CommandStack)folder.getAdapter(CommandStack.class);
                commandStack.execute(cmd);
            }
        };

        action.setImageDescriptor(profile.getImagePath() != null ? 
                ArchiLabelProvider.INSTANCE.getImageDescriptorForSpecialization(profile) : ArchiLabelProvider.INSTANCE.getImageDescriptor(profile.getConceptClass()));
        return action;
    }

    
    private IAction createNewArchimateDiagramAction(final IFolder folder) {
        IAction action = new Action(Messages.TreeModelViewActionFactory_0) {
            @Override
            public void run() {
                // Create a new Diagram Model, set its name
                IDiagramModel diagramModel = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
                diagramModel.setName(Messages.TreeModelViewActionFactory_1);
                
                // Execute Command
                Command cmd = new NewDiagramCommand(folder, diagramModel, Messages.TreeModelViewActionFactory_1);
                CommandStack commandStack = (CommandStack)folder.getAdapter(CommandStack.class);
                commandStack.execute(cmd);
            }
        };

        action.setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_DIAGRAM));
        return action;
    }
    
    private IAction createNewSketchAction(final IFolder folder) {
        IAction action = new Action(Messages.TreeModelViewActionFactory_2) {
            @Override
            public void run() {
                // Create a new Diagram Model, set its name
                ISketchModel sketchModel = IArchimateFactory.eINSTANCE.createSketchModel();
                sketchModel.setName(Messages.TreeModelViewActionFactory_3);
                
                // Defaults
                int defaultBackground = ArchiPlugin.getInstance().getPreferenceStore().getInt(IPreferenceConstants.SKETCH_DEFAULT_BACKGROUND);
                sketchModel.setBackground(defaultBackground);
                
                // Execute Command
                Command cmd = new NewDiagramCommand(folder, sketchModel, Messages.TreeModelViewActionFactory_3);
                CommandStack commandStack = (CommandStack)folder.getAdapter(CommandStack.class);
                commandStack.execute(cmd);
            }
        };

        action.setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_SKETCH));
        return action;
    }
    
    private IAction createNewFolderAction(final IFolder folder) {
        IAction action = new Action(Messages.NewFolderAction_0) {
            @Override
            public void run() {
                // Create a new Folder, set its name
                IFolder newFolder = IArchimateFactory.eINSTANCE.createFolder();
                newFolder.setName(Messages.NewFolderAction_1);
                newFolder.setType(FolderType.USER);
                
                // Execute Command
                Command cmd = new NewFolderCommand(folder, newFolder);
                CommandStack commandStack = (CommandStack)folder.getAdapter(CommandStack.class);
                commandStack.execute(cmd);
            }
        };

        IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProvider(folder);
        action.setImageDescriptor(provider.getImageDescriptor());
        return action;
    }
}
