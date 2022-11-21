package com.archimatetool.editor.actions;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.ui.views.palette.PaletteView;

import com.archimatetool.editor.ui.services.ViewManager;



/**
 * Command Handler to show Palette View
 * 
 * @author Phillip Beauvoir
 */
public class ShowPaletteViewHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        ViewManager.toggleViewPart(PaletteView.ID, true);
        return null;
    }

}
