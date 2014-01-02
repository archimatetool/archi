/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.diagram.dialog.NewNestedRelationDialog;
import com.archimatetool.editor.diagram.dialog.NewNestedRelationsDialog;
import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.preferences.ConnectionPreferences;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IRelationship;
import com.archimatetool.model.util.ArchimateModelUtils;



/**
 * DiagramCommandFactory
 * 
 * @author Phillip Beauvoir
 */
public final class DiagramCommandFactory {

    /**
     * @param object
     * @return A new Delete Diagram Object Command
     */
    public static Command createDeleteDiagramObjectCommand(IDiagramModelObject object) {
        CompoundCommand result = new CompoundCommand();
        __addDeleteCommands(object, result);
        return result.unwrap();
    }
    
    /**
     * Recurse and add child delete commands.
     * We have to do this because if the object has children with connections going outside these need explicit Delete Commands too
     * otherwise we end up with trailing connections...
     * @param container
     * @param result
     */
    private static void __addDeleteCommands(IDiagramModelObject object, CompoundCommand result) {
        result.add(new DeleteDiagramObjectCommand(object));

        if(object instanceof IDiagramModelContainer) {
            for(IDiagramModelObject child : ((IDiagramModelContainer)object).getChildren()) {
                __addDeleteCommands(child, result);
            }
        }
    }
    
    /**
     * @param connection
     * @return A new Delete Diagram Connection Command
     */
    public static Command createDeleteDiagramConnectionCommand(IDiagramModelConnection connection) {
        return new DeleteDiagramConnectionCommand(connection);
    }
    
    /**
     * Create a Command or CompoundCommand to create new Relations between parentElement and childElements.
     * This is used when the user drags elements into a (parent) element and nested relations are required.
     * @param parentElement
     * @param childElements
     * @return The Command or null
     */
    public static Command createNewNestedRelationCommandWithDialog(IArchimateElement parentElement, IArchimateElement[] childElements) {
        Command command = null;
        
        List<IArchimateElement> children = new ArrayList<IArchimateElement>();
        
        // Remove any that already have a relationship
        for(IArchimateElement element : childElements) {
            if(__canAddNewRelationship(parentElement, element)) {
                children.add(element);
            }
        }
        
        // One
        if(children.size() == 1) {
            NewNestedRelationDialog dialog = new NewNestedRelationDialog(Display.getCurrent().getActiveShell(),
                                                 parentElement, children.get(0));
            if(dialog.open() == Window.OK) {
                EClass eClass = dialog.getSelectedType();
                if(eClass != null) {
                    command = new CreateRelationCommand(parentElement, children.get(0), eClass);
                }
            }
        }
        
        // More than one
        else if(children.size() > 1) {
            NewNestedRelationsDialog dialog = new NewNestedRelationsDialog(Display.getCurrent().getActiveShell(),
                                                parentElement, children);
            if(dialog.open() == Window.OK) {
                IArchimateElement[] elements = dialog.getSelectedElements();
                if(elements != null) {
                    command = new CompoundCommand();
                    EClass[] types = dialog.getSelectedTypes();
                    for(int i = 0; i < elements.length; i++) {
                        ((CompoundCommand)command).add(new CreateRelationCommand(parentElement, elements[i], types[i]));
                    }
                }
            }
        }
        
        return command;
    }
    
    /**
     * @param parent
     * @param child
     * @return true if a new relation can/should be added between parent and child when adding an element to a View
     */
    private static boolean __canAddNewRelationship(IArchimateElement parent, IArchimateElement child) {
        // Not certain types
        if(!DiagramModelUtils.isNestedConnectionTypeElement(parent) || !DiagramModelUtils.isNestedConnectionTypeElement(child)) {
            return false;
        }
        
        // Not if there is already a relationship of a certain type between the two
        for(IRelationship relation : ArchimateModelUtils.getSourceRelationships(parent)) {
            if(relation.getTarget() == child) {
                for(EClass eClass : ConnectionPreferences.getRelationsClassesForNewRelations()) {
                    if(relation.eClass() == eClass) {
                        return false;
                    }
                }
            }
        }
        
        // Not if there is already *any* relationship between the two
//        for(IRelationship relation : ArchimateModelUtils.getSourceRelationships(parent)) {
//            if(relation.getTarget() == child) {
//                return false;
//            }
//        }
        
        // Check valid relations
        for(EClass eClass : ConnectionPreferences.getRelationsClassesForNewRelations()) {
            if(ArchimateModelUtils.isValidRelationship(parent, child, eClass)) {
                return true;
            }
        }
        
        return false;
    }
}
