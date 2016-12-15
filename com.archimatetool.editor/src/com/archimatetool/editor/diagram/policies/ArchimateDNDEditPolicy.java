/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.policies;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;

import com.archimatetool.editor.diagram.ArchimateDiagramModelFactory;
import com.archimatetool.editor.diagram.commands.AddDiagramModelReferenceCommand;
import com.archimatetool.editor.diagram.commands.AddDiagramObjectCommand;
import com.archimatetool.editor.diagram.commands.CreateNestedArchimateConnectionsWithDialogCommand;
import com.archimatetool.editor.diagram.dnd.AbstractDNDEditPolicy;
import com.archimatetool.editor.diagram.dnd.ArchimateDiagramTransferDropTargetListener;
import com.archimatetool.editor.diagram.dnd.DiagramDropRequest;
import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.model.commands.NonNotifyingCompoundCommand;
import com.archimatetool.editor.preferences.ConnectionPreferences;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IConnectable;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelArchimateConnection;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.util.ArchimateModelUtils;



/**
 * A policy to handle an Archimate Diagram's and Diagram's Container object's Native DND commands
 * Create a Command for dropping and dragging elements from Tree to diagram/container
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateDNDEditPolicy extends AbstractDNDEditPolicy {

    private List<IArchimateElement> fElementsToAdd;
    private List<IArchimateRelationship> fRelationsToAdd;
    private List<IDiagramModel> fDiagramRefsToAdd;
    
    @Override
    protected Command getDropCommand(DiagramDropRequest request) {
        if(!(request.getData() instanceof IStructuredSelection)) {
            return null;
        }
        
        // XY drop point
        Point pt = getDropLocation(request);

        int origin = pt.x;
        int x = pt.x;
        int y = pt.y;

        fElementsToAdd = new ArrayList<IArchimateElement>();
        fRelationsToAdd = new ArrayList<IArchimateRelationship>();
        fDiagramRefsToAdd = new ArrayList<IDiagramModel>();
        
        // Gather an actual list of elements dragged onto the container, omitting duplicates and anything already on the diagram
        Object[] objects = ((IStructuredSelection)request.getData()).toArray();
        getElementsToAdd(objects);
        
        // Store the Diagram Model Components that will be added in this list
        List<IDiagramModelArchimateComponent> diagramComponentsThatWereAdded = new ArrayList<IDiagramModelArchimateComponent>();

        // Create a Compound Command - it has to be Non-Notifying or it's too slow (tested with Bill's UoB model!)
        CompoundCommand result = new NonNotifyingCompoundCommand(Messages.ArchimateDNDEditPolicy_0);

        // Add the Commands adding the Elements first
        for(IArchimateElement element : fElementsToAdd) {
            // Add Diagram object
            IDiagramModelArchimateObject dmo = ArchimateDiagramModelFactory.createDiagramModelArchimateObject(element);
            
            // Set location
            dmo.getBounds().setLocation(x, y);
            
            // Store it
            diagramComponentsThatWereAdded.add(dmo);
            
            // Add Command
            result.add(new AddDiagramObjectCommand(getTargetContainer(), dmo));

            // Increase x,y
            x += 150;
            if(x > origin + 400) {
                x = origin;
                y += 100;
            }
        }

        // Then any Diagram Model Ref Commands
        for(IDiagramModel diagramModel : fDiagramRefsToAdd) {
            result.add(new AddDiagramModelReferenceCommand(getTargetContainer(), diagramModel, x, y));
            
            x += 150;
            if(x > origin + 400) {
                x = origin;
                y += 100;
            }
        }

        // Add selected Relations to create connections to those elements on the diagram that don't already have them
        for(IArchimateRelationship relation : fRelationsToAdd) {
            // Find existing source & target components on the diagram that the new connection will link to
            List<IDiagramModelArchimateComponent> sources = DiagramModelUtils.findDiagramModelComponentsForArchimateConcept(getTargetDiagramModel(), relation.getSource());
            List<IDiagramModelArchimateComponent> targets = DiagramModelUtils.findDiagramModelComponentsForArchimateConcept(getTargetDiagramModel(), relation.getTarget());

            for(IDiagramModelComponent dcSource : sources) {
                for(IDiagramModelComponent dcTarget : targets) {
                    if(dcSource instanceof IConnectable && dcTarget instanceof IConnectable) {
                        // Add a new connection between dcSource & dcTarget if there isn't already one on the diagram
                        if(dcTarget != dcSource && !DiagramModelUtils.hasDiagramModelArchimateConnection((IConnectable)dcSource, (IConnectable)dcTarget, relation)) {
                        	// Check that source or target is not a hiden connection 
                        	if(!(
                        			(dcSource instanceof IDiagramModelArchimateConnection
                        			&& DiagramModelUtils.shouldBeHiddenConnection((IDiagramModelArchimateConnection) dcSource))
                        			||
                        			(dcTarget instanceof IDiagramModelArchimateConnection
                                	&& DiagramModelUtils.shouldBeHiddenConnection((IDiagramModelArchimateConnection) dcTarget))
                        	)) {
                                AddDiagramArchimateConnectionCommand cmd = new AddDiagramArchimateConnectionCommand((IConnectable)dcSource, (IConnectable)dcTarget, relation);
                                result.add(cmd);
                                // Store it
                                diagramComponentsThatWereAdded.add(cmd.getConnection());
                        	}
                        }
                    }
                }
            }
        }
        
        // Whether to add connections to elements
        Boolean value = (Boolean)request.getExtendedData().get(ArchimateDiagramTransferDropTargetListener.ADD_ELEMENT_CONNECTIONS);
        boolean addConnectionsToElements = value != null && value.booleanValue();
        
        // Newly added concepts will need new connections to both existing and newly added concepts
        for(IDiagramModelArchimateComponent dmComponent : diagramComponentsThatWereAdded) {
            IArchimateConcept archimateConcept = dmComponent.getArchimateConcept();

            for(IArchimateRelationship relation : ArchimateModelUtils.getAllRelationshipsForConcept(archimateConcept)) {
                /*
                 * If the user holds down the Copy key (Ctrl on win/lnx, Alt on Mac) then linked connections
                 * are not added on drag and drop. However, any selected relations' linked objects are added.
                 */
                if(!addConnectionsToElements && !fRelationsToAdd.contains(relation)) {
                    continue;
                }

                // Find existing objects
                List<IDiagramModelArchimateComponent> sources = DiagramModelUtils.findDiagramModelComponentsForArchimateConcept(getTargetDiagramModel(), relation.getSource());
                List<IDiagramModelArchimateComponent> targets = DiagramModelUtils.findDiagramModelComponentsForArchimateConcept(getTargetDiagramModel(), relation.getTarget());

                // Add new ones too
                for(IDiagramModelArchimateComponent dmComponent2 : diagramComponentsThatWereAdded) {
                    if(dmComponent != dmComponent2) {
                        IArchimateConcept archimateConcept2 = dmComponent2.getArchimateConcept();
                        
                        if(archimateConcept2 == relation.getSource()) { // Only need to add sources, not targets
                            sources.add(dmComponent2);
                        }
                    }
                }

                // Make the Commands...
                for(IDiagramModelComponent dcSource : sources) {
                    if(dcSource instanceof IConnectable && archimateConcept == relation.getTarget()) {
                        result.add(new AddDiagramArchimateConnectionCommand((IConnectable)dcSource, (IConnectable)dmComponent, relation));
                    }
                }

                for(IDiagramModelComponent dcTarget : targets) {
                    if(dcTarget instanceof IConnectable && archimateConcept == relation.getSource()) {
                        result.add(new AddDiagramArchimateConnectionCommand((IConnectable)dmComponent, (IConnectable)dcTarget, relation));
                    }
                }
            }
        }
        
        // Then, if adding to an Archimate container type to create nesting, ask whether to add new relations if none exist...
        if(ConnectionPreferences.createRelationWhenAddingModelTreeElement() && getTargetContainer() instanceof IDiagramModelArchimateObject) {
            List<IDiagramModelArchimateObject> diagramObjectsThatWereAdded = new ArrayList<IDiagramModelArchimateObject>();
            for(IDiagramModelArchimateComponent dmc : diagramComponentsThatWereAdded) {
                if(dmc instanceof IDiagramModelArchimateObject) {
                    diagramObjectsThatWereAdded.add((IDiagramModelArchimateObject)dmc);
                }
            }
            
            Command cmd = new CreateNestedArchimateConnectionsWithDialogCommand((IDiagramModelArchimateObject)getTargetContainer(), diagramObjectsThatWereAdded);
            result.add(cmd);
        }
        
        return result; // return the full compound command
    }
    
    /**
     * Gather the elements and relationships that will be added to the diagram
     */
    private void getElementsToAdd(Object[] objects) {
        IArchimateModel targetArchimateModel = getTargetDiagramModel().getArchimateModel();
        
        for(Object object : objects) {
            // Check
            if(!canDropObject(object)) {
                continue;
            }
            
            // Can only add to the same model
            if(object instanceof IArchimateModelObject) {
                IArchimateModel sourceArchimateModel = ((IArchimateModelObject)object).getArchimateModel();
                if(sourceArchimateModel != targetArchimateModel) {
                    continue;
                }
            }
            
            // It's a selected Archimate Element
            if(object instanceof IArchimateElement) {
                addUniqueObjectToList(fElementsToAdd, (IArchimateElement)object);
            }
            // It's a selected relationship - also add any connected components
            else if(object instanceof IArchimateRelationship) {
                IArchimateRelationship relationship = (IArchimateRelationship)object;
                
                // A connection (C1) from an element (E) to another connection (C2) is managed in a specific manner:
                // - if C2 is in the diagram and not hidden, then add C1 (and E if not already there)
                // - if C2 is not in the diagram or is hidden then do nothings
                if(relationship.getSource() instanceof IArchimateRelationship || relationship.getTarget() instanceof IArchimateRelationship) {
                	IArchimateRelationship otherRelationship = relationship.getSource() instanceof IArchimateRelationship ?
                			(IArchimateRelationship) relationship.getSource()
                			: (IArchimateRelationship) relationship.getTarget();
                	
                	List<IDiagramModelArchimateConnection> otherConnections = DiagramModelUtils.findDiagramModelConnectionsForRelation(getTargetDiagramModel(), otherRelationship);
                	
                	if(!otherConnections.isEmpty()) {
                		for(IDiagramModelArchimateConnection otherConnection : otherConnections) {
                			if(!DiagramModelUtils.shouldBeHiddenConnection(otherConnection)) {
                				// We've just found C2 which is not hidden
                				// We can add C1 (and E if not already there)
                				addUniqueObjectToList(fRelationsToAdd, relationship);
            	                // Add relationship's connected concepts
            	                addRelationshipConcepts(relationship);
            	                break;
                			}
                		}
                	}
                }
                // Simple case: a relationship from an element to another element
                else {
	                addUniqueObjectToList(fRelationsToAdd, relationship);
	                // Add relationship's connected concepts
	                addRelationshipConcepts(relationship);
                }
            }
            // It's a selected diagram models (reference)
            else if(object instanceof IDiagramModel && object != getTargetDiagramModel()) { // not the same diagram
                addUniqueObjectToList(fDiagramRefsToAdd, (IDiagramModel)object);
            }
        }
    }
    
    /**
     * Add connected concepts
     * 
     * Don't forget that connections to connections have been managed in getElementsToAdd(). So
     * We don't have to add any other relationships here, only elements!
     * 
     * @param relationship
     */
    private void addRelationshipConcepts(IArchimateRelationship relationship) {
        // Connected Source Element if not on Diagram
        if(DiagramModelUtils.findDiagramModelComponentsForArchimateConcept(getTargetDiagramModel(), relationship.getSource()).isEmpty()) {
            if(relationship.getSource() instanceof IArchimateElement) {
                addUniqueObjectToList(fElementsToAdd, (IArchimateElement)relationship.getSource());
            }
//            else if(relationship.getSource() instanceof IArchimateRelationship) {
//                IArchimateRelationship source = (IArchimateRelationship)relationship.getSource();
//                addUniqueObjectToList(fRelationsToAdd, source);
//                addRelationshipConcepts(source);
//            }
        }
        
        // Connected Target Element if not on Diagram
        if(DiagramModelUtils.findDiagramModelComponentsForArchimateConcept(getTargetDiagramModel(), relationship.getTarget()).isEmpty()) {
            if(relationship.getTarget() instanceof IArchimateElement) {
                addUniqueObjectToList(fElementsToAdd, (IArchimateElement)relationship.getTarget());
            }
//            else if(relationship.getTarget() instanceof IArchimateRelationship) {
//                IArchimateRelationship target = (IArchimateRelationship)relationship.getTarget();
//                addUniqueObjectToList(fRelationsToAdd, target);
//                addRelationshipConcepts(target);
//            }
        }
        
        // Recursive case - ensure at least 2 connecting elements
        // TODO: I don't understand ??? (JB)
        if(relationship.getSource() instanceof IArchimateElement && relationship.getSource() == relationship.getTarget()) {
            int size = DiagramModelUtils.findDiagramModelObjectsForElement(getTargetDiagramModel(), (IArchimateElement)relationship.getSource()).size();
            for(IArchimateElement e : fElementsToAdd) {
                if(e == relationship.getSource()) {
                    size++;
                }
            }
            if(size < 2) {
                fElementsToAdd.add((IArchimateElement)relationship.getSource());
            }
        }
    }
    
    /**
     * Add a unique object to a list
     */
    private <T extends EObject> void addUniqueObjectToList(List<T> list, T element) {
        if(!list.contains(element)) {  
            list.add(element);
        }
    }
    
    /**
     * @param object
     * @return Whether we can DND an object onto the Container
     */
    private boolean canDropObject(Object object) {
        return (object instanceof IArchimateConcept) || (object instanceof IDiagramModel);
    }
    
    
    
    private static class AddDiagramArchimateConnectionCommand extends Command {
        private IDiagramModelArchimateConnection fConnection;
        private IConnectable fSource, fTarget;
        
        public AddDiagramArchimateConnectionCommand(IConnectable src, IConnectable tgt, IArchimateRelationship relationship) {
            setLabel(NLS.bind(Messages.ArchimateDNDEditPolicy_1, relationship.getName()));

            fSource = src;
            fTarget = tgt;
            fConnection = ArchimateDiagramModelFactory.createDiagramModelArchimateConnection(relationship);
        }
        
        public IDiagramModelArchimateConnection getConnection() {
            return fConnection;
        }

        @Override
        public void execute() {
            fConnection.connect(fSource, fTarget);
        }

        @Override
        public void undo() {
            fConnection.disconnect();
        }

        @Override
        public void redo() {
            fConnection.reconnect();
        }
    }
}
