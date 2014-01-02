/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;

import com.archimatetool.editor.ui.services.ViewManager;


/**
 * Action to open Properties on an Edit Part.
 * 
 * @author Phillip Beauvoir
 */
public class PropertiesAction extends SelectionAction {

    public PropertiesAction(IWorkbenchPart part) {
        super(part);
        setText(Messages.PropertiesAction_0);
        setToolTipText(Messages.PropertiesAction_0);
        setActionDefinitionId(ActionFactory.PROPERTIES.getCommandId()); // Ensures key binding is displayed
        setId(ActionFactory.PROPERTIES.getId());
    }

    @Override
    public void run() {
        ViewManager.showViewPart(ViewManager.PROPERTIES_VIEW, true);
    }

    @Override
    protected boolean calculateEnabled() {
        return true;
    }

}
