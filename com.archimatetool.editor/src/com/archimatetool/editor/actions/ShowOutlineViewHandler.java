package com.archimatetool.editor.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.archimatetool.editor.ui.services.ViewManager;



/**
 * Command Handler to show Outline View
 * 
 * @author Phillip Beauvoir
 */
public class ShowOutlineViewHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        ViewManager.toggleViewPart(ViewManager.OUTLINE_VIEW, true);
        return null;
    }

}
