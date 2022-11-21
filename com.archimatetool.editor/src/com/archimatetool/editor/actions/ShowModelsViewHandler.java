package com.archimatetool.editor.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.archimatetool.editor.ui.services.ViewManager;
import com.archimatetool.editor.views.tree.ITreeModelView;



/**
 * Command Handler to show Models View
 * 
 * @author Phillip Beauvoir
 */
public class ShowModelsViewHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        ViewManager.toggleViewPart(ITreeModelView.ID, true);
        return null;
    }

}
