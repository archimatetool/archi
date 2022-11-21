package com.archimatetool.editor.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.archimatetool.editor.ui.services.ViewManager;
import com.archimatetool.editor.views.properties.ICustomPropertiesView;



/**
 * Command Handler to show Properties View
 * 
 * @author Phillip Beauvoir
 */
public class ShowPropertiesViewHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        ViewManager.toggleViewPart(ICustomPropertiesView.ID, true);
        return null;
    }

}
