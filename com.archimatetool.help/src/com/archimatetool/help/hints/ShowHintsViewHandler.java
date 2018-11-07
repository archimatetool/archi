/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.help.hints;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IViewPart;

import com.archimatetool.editor.ui.services.ViewManager;
import com.archimatetool.editor.utils.PlatformUtils;



/**
 * Command Action Handler to show Hints View
 * 
 * @author Phillip Beauvoir
 */
public class ShowHintsViewHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        
        // Eclipse Bug on Windows when using Alt-7 key command to close a View containing a Browser - it crashes!
        
        // Key code of non-zero indicates a key command
        if(PlatformUtils.isWindows() && event.getTrigger() instanceof Event && ((Event)event.getTrigger()).keyCode != 0) {
            IViewPart part = ViewManager.findViewPart(IHintsView.ID);
            
            // We are closing the View so put it on a thread for the workaround
            if(part != null) {
                Display.getCurrent().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        ViewManager.hideViewPart(IHintsView.ID);
                    }
                });
            }
            // We are opening the View
            else {
                ViewManager.showViewPart(IHintsView.ID, true);
            }
        }
        else {
            ViewManager.toggleViewPart(IHintsView.ID, true);
        }
        
        return null;
    }

}
