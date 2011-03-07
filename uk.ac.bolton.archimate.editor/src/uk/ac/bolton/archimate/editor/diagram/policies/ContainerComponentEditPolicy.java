/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.policies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.jface.viewers.IStructuredSelection;

import uk.ac.bolton.archimate.editor.diagram.commands.AddDiagramArchimateConnectionCommand;
import uk.ac.bolton.archimate.editor.diagram.commands.AddDiagramModelReferenceCommand;
import uk.ac.bolton.archimate.editor.diagram.commands.AddDiagramObjectCommand;
import uk.ac.bolton.archimate.editor.diagram.commands.DiagramCommandFactory;
import uk.ac.bolton.archimate.editor.diagram.dnd.NativeDropRequest;
import uk.ac.bolton.archimate.editor.diagram.figures.IContainerFigure;
import uk.ac.bolton.archimate.editor.model.DiagramModelUtils;
import uk.ac.bolton.archimate.editor.model.commands.NonNotifyingCompoundCommand;
import uk.ac.bolton.archimate.editor.preferences.ConnectionPreferences;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IArchimateModelElement;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelContainer;
import uk.ac.bolton.archimate.model.IRelationship;
import uk.ac.bolton.archimate.model.util.ArchimateModelUtils;


/**
 * A policy to handle a Container's Delete and DND commands
 * 
 * @author Phillip Beauvoir
 */
public class ContainerComponentEditPolicy extends ComponentEditPolicy {

    @Override
    public EditPart getTargetEditPart(Request request) {
        // We support Native DND
        if(NativeDropRequest.ID.equals(request.getType())) {
            return getHost();
        }
        return super.getTargetEditPart(request);
    }
    
    @Override
    public Command getCommand(Request request) {
        // We support Native DND
        if(NativeDropRequest.ID.equals(request.getType())) {
            return getDropCommand((NativeDropRequest)request);
        }
        
        return super.getCommand(request);
    }
    
    // --------------------------------------------------------------------------------------
    // Create a Command for dropping and dragging elements from Tree to diagram/container
    // --------------------------------------------------------------------------------------
    
    protected List<IArchimateElement> elementsToAdd;
    protected List<IRelationship> relationsToAdd;
    protected List<IDiagramModel> diagramRefsToAdd;
    
    protected IDiagramModelContainer targetContainer;
    protected IDiagramModel targetDiagramModel;
    
    /**
     * @param request
     * @return A command for when a native drop event occurs on the Diagram Container
     */
    protected Command getDropCommand(NativeDropRequest request) {
        if(!(request.getData() instanceof IStructuredSelection)) {
            return null;
        }
        
        // XY drop point
        Point pt = request.getDropLocation();

        // Translate drop point
        IFigure figure = ((GraphicalEditPart)getHost()).getFigure();
        if(figure instanceof IContainerFigure) {
            ((IContainerFigure)figure).translateMousePointToRelative(pt);
        }
        // Translate to relative content pane
        else {
            IFigure contentPane = ((GraphicalEditPart)getHost()).getContentPane();
            contentPane.translateToRelative(pt);
        }

        int origin = pt.x;
        int x = pt.x;
        int y = pt.y;

        targetContainer = (IDiagramModelContainer)getHost().getModel();
        targetDiagramModel = targetContainer.getDiagramModel();
        
        elementsToAdd = new ArrayList<IArchimateElement>();
        relationsToAdd = new ArrayList<IRelationship>();
        diagramRefsToAdd = new ArrayList<IDiagramModel>();
        
        // Gather an actual list of elements dragged onto the container, omitting duplicates and anything already on the diagram
        Object[] objects = ((IStructuredSelection)request.getData()).toArray();
        getElementsToAdd(objects);

        // Compound Command - it has to be Non-Notifying or it's way too slow (tested with Bill's UoB model!)
        CompoundCommand result = new NonNotifyingCompoundCommand("Add Elements");

        // Now add the Commands adding the Elements first
        for(IArchimateElement element : elementsToAdd) {
            // Add Diagram object
            result.add(new AddDiagramObjectCommand(targetContainer, element, x, y));

            // Increase x,y
            x += 150;
            if(x > origin + 400) {
                x = origin;
                y += 100;
            }
        }

        // Then any Diagram Model Ref Commands
        for(IDiagramModel diagramModel : diagramRefsToAdd) {
            result.add(new AddDiagramModelReferenceCommand(targetContainer, diagramModel, x, y));
            
            x += 150;
            if(x > origin + 400) {
                x = origin;
                y += 100;
            }
        }

        // Then the Relationship Commands for adding connections
        for(IRelationship relation : relationsToAdd) {
            /*
             *  If this would result in a nested connection don't add it AND
             *  Only add connection if *both* src and target elements are either in elementsToAdd or on the Diagram already
             */
            if(!wouldBeNestedConnection(relation) && canAddConnection(relation)) {
                result.add(new AddDiagramArchimateConnectionCommand(targetDiagramModel, relation));   
            }
        }
        
        // Then, if adding to a container type, ask whether to add new relations...
        Command subCommand = createAddRelationsCommand();
        if(subCommand != null) {
            result.add(subCommand);
        }

        return result; // return the full compound command
    }
    
    /**
     * If adding to a container type, ask whether to add new relations...
     * We'll add this as a sub-command to be executed when the main Command executes so that the user
     * Can see the objects added to the view before answering the relations dialog.
     */
    protected Command createAddRelationsCommand() {
        Command command = null;
        
        if(ConnectionPreferences.createRelationWhenAddingModelTreeElement()) {
            if(targetContainer instanceof IDiagramModelArchimateObject) {
                command = new Command() {
                    private Command fSubCommand;

                    @Override
                    public void execute() {
                        IArchimateElement parentElement = ((IDiagramModelArchimateObject)targetContainer).getArchimateElement();
                        fSubCommand = DiagramCommandFactory.createNewNestedRelationCommandWithDialog(parentElement,
                                        elementsToAdd.toArray(new IArchimateElement[elementsToAdd.size()]));
                        if(fSubCommand != null) {
                            fSubCommand.execute();
                        }
                    }

                    @Override
                    public void undo() {
                        if(fSubCommand != null) {
                            fSubCommand.undo();
                        }
                    }

                    @Override
                    public void redo() {
                        if(fSubCommand != null) {
                            fSubCommand.redo();
                        }
                    }
                };
            }
        }
        
        return command;
    }
    
    /**
     * Check to see if a relation can be added as a connection
     */
    protected boolean canAddConnection(IRelationship relation) {
        // Source Element is in the list of elements to be added or already on the diagram
        boolean srcElementPresent = elementsToAdd.contains(relation.getSource()) || DiagramModelUtils.findDiagramModelComponentForElement(targetDiagramModel, relation.getSource()) != null;
        // Target Element is in the list of elements to be added or already on the diagram
        boolean tgtElementPresent = elementsToAdd.contains(relation.getTarget()) || DiagramModelUtils.findDiagramModelComponentForElement(targetDiagramModel, relation.getTarget()) != null;
        // Both are present
        return srcElementPresent && tgtElementPresent;
    }
    
    /**
     * Check to see if a relation would be a nested connection if it was inside of target container
     */
    protected boolean wouldBeNestedConnection(IRelationship relation) {
        // If relation.getTarget() is inside of targetContainer
        if(ConnectionPreferences.useNestedConnections()) {
            if(targetContainer instanceof IDiagramModelArchimateObject &&
                    targetContainer.getChildren().contains(relation.getTarget()) && DiagramModelUtils.isNestedConnectionTypeRelationship(relation)) {
                return ((IDiagramModelArchimateObject)targetContainer).getArchimateElement() == relation.getSource();
            }
        }
        return false;
    }

    /**
     * Gather the elements and relationships that will be added to the diagram
     */
    protected void getElementsToAdd(Object[] objects) {
        IArchimateModel targetArchimateModel = targetDiagramModel.getArchimateModel();
        
        for(Object object : objects) {
            // Check
            if(!canDropElement(object)) {
                continue;
            }
            
            // Can only add to the same model
            if(object instanceof IArchimateModelElement) {
                IArchimateModel sourceArchimateModel = ((IArchimateModelElement)object).getArchimateModel();
                if(sourceArchimateModel != targetArchimateModel) {
                    continue;
                }
            }
            
            // Selected Archimate Elements (and any related Relationships)
            if(object instanceof IArchimateElement && !(object instanceof IRelationship)) {
                addElement((IArchimateElement)object);
            }
        
            // Selected Relationships (and any connected Elements)
            else if(object instanceof IRelationship) {
                IRelationship relationship = (IRelationship)object;
                // Not already on diagram
                if(!DiagramModelUtils.isElementReferencedInDiagram(targetDiagramModel, relationship)) {
                    if(!relationsToAdd.contains(relationship)) { // check it here, not in addElement() (was a bug)
                        relationsToAdd.add(relationship);
                    }

                    // Connected Source Element
                    addElement(relationship.getSource());

                    // Connected Target Element
                    addElement(relationship.getTarget());
                }
            }
            
            // Selected Diagram Models (References)
            else if(object instanceof IDiagramModel && object != targetDiagramModel) { // not already on diagram
                if(!diagramRefsToAdd.contains(object)) {
                    diagramRefsToAdd.add((IDiagramModel)object);
                }
            }
        }
    }
    
    /**
     * Add an element and any of its relationships
     */
    protected void addElement(IArchimateElement element) {
        // Not already added or already on diagram
        if(!elementsToAdd.contains(element) && DiagramModelUtils.findDiagramModelComponentForElement(targetDiagramModel, element) == null) {  
            elementsToAdd.add(element);
            // And its relationships
            for(IRelationship relationship : ArchimateModelUtils.getRelationships(element)) {
                if(!relationsToAdd.contains(relationship) && !DiagramModelUtils.isElementReferencedInDiagram(targetDiagramModel, relationship)) { // not already on diagram
                    relationsToAdd.add(relationship);
                }
            }
        }
    }
    
    /**
     * @param element
     * @return Whether we can DND an element onto the Container
     */
    protected boolean canDropElement(Object element) {
        return (element instanceof IArchimateElement) || (element instanceof IDiagramModel);
    }
}
