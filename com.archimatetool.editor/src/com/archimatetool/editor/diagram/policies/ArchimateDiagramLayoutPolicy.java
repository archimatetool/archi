/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.policies;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

import com.archimatetool.editor.diagram.commands.CreateDiagramArchimateObjectCommand;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IArchimatePackage;



/**
 * Policy for General Diagram
 * 
 * @author Phillip Beauvoir
 */
public class ArchimateDiagramLayoutPolicy
extends DiagramLayoutPolicy {
    
    @Override
    protected Command getCreateCommand(CreateRequest request) {
        Rectangle bounds = getConstraintFor(request);
        
        if(request.getNewObjectType() instanceof EClass) {
            EClass eClass = (EClass)request.getNewObjectType();
            
            // Archimate type object
            if(IArchimatePackage.eINSTANCE.getArchimateElement().isSuperTypeOf(eClass)) {
                return new CreateDiagramArchimateObjectCommand(getHost(), request, bounds);
            }
        }
        
        return super.getCreateCommand(request);
    }
    
    @Override
    protected Dimension getMaximumSizeFor(Object object) {
        // Junctions should not be bigger than their in-built default size
        if(object == IArchimatePackage.eINSTANCE.getJunction()) {
            IGraphicalObjectUIProvider provider = (IGraphicalObjectUIProvider)ObjectUIFactory.INSTANCE.getProviderForClass((EClass)object);
            return provider.getDefaultSize();
        }
        
        return super.getMaximumSizeFor(object);
    }
}

