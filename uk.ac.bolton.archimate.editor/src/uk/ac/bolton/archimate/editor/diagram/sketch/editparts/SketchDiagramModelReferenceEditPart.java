/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.diagram.sketch.editparts;

import org.eclipse.gef.EditPolicy;

import uk.ac.bolton.archimate.editor.diagram.editparts.diagram.DiagramModelReferenceEditPart;
import uk.ac.bolton.archimate.editor.diagram.policies.PartComponentEditPolicy;
import uk.ac.bolton.archimate.editor.diagram.sketch.policies.SketchConnectionPolicy;


/**
 * Diagram Model Reference Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class SketchDiagramModelReferenceEditPart extends DiagramModelReferenceEditPart {

    @Override
    protected void createEditPolicies() {
        // Allow parts to be connected
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new SketchConnectionPolicy());

        // Add a policy to handle editing the Parts (for example, deleting a part)
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new PartComponentEditPolicy());
    }

}
