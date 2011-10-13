/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.views.tree.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;

import uk.ac.bolton.archimate.editor.ui.ArchimateNames;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.editor.ui.ImageFactory;
import uk.ac.bolton.archimate.editor.views.tree.commands.NewDiagramCommand;
import uk.ac.bolton.archimate.editor.views.tree.commands.NewElementCommand;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateFactory;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IFolder;
import uk.ac.bolton.archimate.model.ISketchModel;
import uk.ac.bolton.archimate.model.util.ArchimateModelUtils;


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

        // If we have selected a leaf object, go up to parent
        if(selected instanceof IArchimateElement || selected instanceof IDiagramModel) {
            selected = ((EObject)selected).eContainer();
        }
        
        // We want a folder
        if(!(selected instanceof IFolder)) {
            return list;
        }
        
        IFolder folder = (IFolder)selected;
        
        // Find topmost folder type
        IFolder f = folder;
        while(f.eContainer() instanceof IFolder) {
            f = (IFolder)f.eContainer();
        }

        switch(f.getType()) {
            case BUSINESS:
                for(EClass eClass : ArchimateModelUtils.getBusinessClasses()) {
                    IAction action = createNewElementAction(folder, eClass);
                    list.add(action);
                }
                break;

            case APPLICATION:
                for(EClass eClass : ArchimateModelUtils.getApplicationClasses()) {
                    IAction action = createNewElementAction(folder, eClass);
                    list.add(action);
                }
                break;

            case TECHNOLOGY:
                for(EClass eClass : ArchimateModelUtils.getTechnologyClasses()) {
                    IAction action = createNewElementAction(folder, eClass);
                    list.add(action);
                }
                break;

            case CONNECTORS:
                for(EClass eClass : ArchimateModelUtils.getConnectorClasses()) {
                    IAction action = createNewElementAction(folder, eClass);
                    list.add(action);
                }
                break;
                
            case DIAGRAMS:
                list.add(createNewArchimateDiagramAction(folder));
                list.add(createNewSketchAction(folder));
                break;

            default:
                break;
        }

        return list;
    }

    private IAction createNewElementAction(final IFolder folder, final EClass eClass) {
        IAction action = new Action(ArchimateNames.getDefaultName(eClass)) {
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

        action.setImageDescriptor(ImageFactory.getImageDescriptor(eClass));
        return action;
    }
    
    private IAction createNewArchimateDiagramAction(final IFolder folder) {
        IAction action = new Action("&ArchiMate View") {
            @Override
            public void run() {
                // Create a new Diagram Model, set its name
                IDiagramModel diagramModel = IArchimateFactory.eINSTANCE.createArchimateDiagramModel();
                diagramModel.setName("New ArchiMate View");
                
                // Execute Command
                Command cmd = new NewDiagramCommand(folder, diagramModel, "New ArchiMate View");
                CommandStack commandStack = (CommandStack)folder.getAdapter(CommandStack.class);
                commandStack.execute(cmd);
            }
        };

        action.setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_DIAGRAM_16));
        return action;
    }
    
    private IAction createNewSketchAction(final IFolder folder) {
        IAction action = new Action("&Sketch") {
            @Override
            public void run() {
                // Create a new Diagram Model, set its name
                ISketchModel sketchModel = IArchimateFactory.eINSTANCE.createSketchModel();
                sketchModel.setName("New Sketch");
                
                // Execute Command
                Command cmd = new NewDiagramCommand(folder, sketchModel, "New Sketch");
                CommandStack commandStack = (CommandStack)folder.getAdapter(CommandStack.class);
                commandStack.execute(cmd);
            }
        };

        action.setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_SKETCH_16));
        return action;
    }
}
