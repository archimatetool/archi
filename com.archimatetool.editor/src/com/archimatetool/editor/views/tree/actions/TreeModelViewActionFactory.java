/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.actions;

import java.util.ArrayList;
import java.util.List;

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
        List<IAction> list = new ArrayList<IAction>();
        
        // If we have selected a leaf object, go up to its parent
        if(selected instanceof IArchimateConcept || selected instanceof IDiagramModel) {
            selected = ((EObject)selected).eContainer();
        }
        
        // We haven't got a parent folder
        if(!(selected instanceof IFolder)) {
            return list;
        }
        
        IFolder selectedFolder = (IFolder)selected;
        
        list.add(createNewFolderAction((IFolder)selected));
        list.add(null);
        
        // Find the topmost folder type
        IFolder topMostFolder = selectedFolder;
        while(topMostFolder.eContainer() instanceof IFolder) {
            topMostFolder = (IFolder)topMostFolder.eContainer();
        }

        switch(topMostFolder.getType()) {
            case STRATEGY:
                for(EClass eClass : ArchimateModelUtils.getStrategyClasses()) {
                    IAction action = createNewElementAction(selectedFolder, eClass);
                    list.add(action);
                }
                break;

            case BUSINESS:
                for(EClass eClass : ArchimateModelUtils.getBusinessClasses()) {
                    IAction action = createNewElementAction(selectedFolder, eClass);
                    list.add(action);
                }
                break;

            case APPLICATION:
                for(EClass eClass : ArchimateModelUtils.getApplicationClasses()) {
                    IAction action = createNewElementAction(selectedFolder, eClass);
                    list.add(action);
                }
                break;

            case MOTIVATION:
                for(EClass eClass : ArchimateModelUtils.getMotivationClasses()) {
                    IAction action = createNewElementAction(selectedFolder, eClass);
                    list.add(action);
                }
                break;

            case IMPLEMENTATION_MIGRATION:
                for(EClass eClass : ArchimateModelUtils.getImplementationMigrationClasses()) {
                    IAction action = createNewElementAction(selectedFolder, eClass);
                    list.add(action);
                }
                break;

            case TECHNOLOGY:
                // Technology
                for(EClass eClass : ArchimateModelUtils.getTechnologyClasses()) {
                    IAction action = createNewElementAction(selectedFolder, eClass);
                    list.add(action);
                }
                
                list.add(null);
                
                // Physical
                for(EClass eClass : ArchimateModelUtils.getPhysicalClasses()) {
                    IAction action = createNewElementAction(selectedFolder, eClass);
                    list.add(action);
                }
                break;

            case OTHER:
                // Grouping and Location
                for(EClass eClass : ArchimateModelUtils.getOtherClasses()) {
                    IAction action = createNewElementAction(selectedFolder, eClass);
                    list.add(action);
                }
                
                list.add(null);
                
                // Connectors
                for(EClass eClass : ArchimateModelUtils.getConnectorClasses()) {
                    IAction action = createNewElementAction(selectedFolder, eClass);
                    list.add(action);
                }
                break;
                
            case DIAGRAMS:
                list.add(createNewArchimateDiagramAction(selectedFolder));
                list.add(createNewSketchAction(selectedFolder));
                break;

            default:
                break;
        }
        
        // Remove any trailing separator
        if(list.get(list.size() - 1) == null) {
            list.remove(list.size() - 1);
        }

        return list;
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
                int defaultBackground = ArchiPlugin.PREFERENCES.getInt(IPreferenceConstants.SKETCH_DEFAULT_BACKGROUND);
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
