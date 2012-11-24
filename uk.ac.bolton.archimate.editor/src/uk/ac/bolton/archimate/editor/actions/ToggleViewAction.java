/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.editor.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

import uk.ac.bolton.archimate.editor.ui.services.ViewManager;


/**
 * Toggle View Action
 * 
 * @author Phillip Beauvoir
 */
public class ToggleViewAction extends Action {

    private String fViewID;
    
    public ToggleViewAction(String viewName, String viewID, String actionID, ImageDescriptor imageDescriptor) {
        super(viewName);
        fViewID = viewID;
        setImageDescriptor(imageDescriptor);
        setId(actionID);
        setActionDefinitionId(actionID);
    }
    
    @Override
    public String getToolTipText() {
        return getText();
    }
    
    @Override
    public void run() {
        ViewManager.toggleViewPart(fViewID, false);
    }
    
}
