/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.actions;

import java.util.List;

import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IPartService;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.menus.IWorkbenchContribution;
import org.eclipse.ui.services.IServiceLocator;

import com.archimatetool.editor.views.tree.ITreeModelView;

/**
 * Menu ContributionItem that adds "New" menu actions to the tree<p>
 * 
 * One of the reasons for doing it this way (and adding the "New" menu extension in plugin.xml) rather than coding it in
 * TreeModelView#fillContextMenu(IMenuManager) is due to an Eclipse bug that causes contributed menu items in a sub-menu to appear twice.<p>
 * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=485931<p>
 * This was happening in NewCanvasExtensionContributionFactory where I added a horrible workaround.
 * 
 * @author Phillip Beauvoir
 */
public class NewMenuContributionItem extends ContributionItem implements IWorkbenchContribution {
    
    private MenuManager menuManager;
    
    private ISelectionService selectionService;
    private IPartService partService;
    
    @Override
    public void fill(Menu menu, int index) {
        if(menuManager != null) {
            menuManager.dispose();
        }
        
        Object selected = null;
        
        // If we have a tree selection...
        IStructuredSelection selection = getCurrentSelection();
        if(selection != null) {
            selected = selection.getFirstElement();
        }
        // No, look for current tree input
        if(selected == null) {
            selected = getTreeInput();
        }
        if(selected == null) {
            return;
        }

        List<IAction> actions = TreeModelViewActionFactory.INSTANCE.getNewObjectActions(selected);
        
        menuManager = new MenuManager();
        
        for(IAction action : actions) {
            if(action != null) {
                menuManager.add(action);
            }
            else {
                menuManager.add(new Separator());
            }
        }
        
        // Hide the "New" sub-menu if there's nothing to show
        setVisible(!actions.isEmpty());
        
        for(IContributionItem item : menuManager.getItems()) {
            item.fill(menu, index++);
        }
    }

    @Override
    public boolean isDynamic() {
        // This is important!
        return true;
    }

    private Object getTreeInput() {
        if(partService.getActivePart() instanceof ITreeModelView) {
            return ((ITreeModelView)partService.getActivePart()).getViewer().getInput();
        }
        return null;
    }
    
    private IStructuredSelection getCurrentSelection() {
        return (IStructuredSelection)selectionService.getSelection(ITreeModelView.ID);
    }

    @Override
    public void initialize(IServiceLocator serviceLocator) {
        this.selectionService = serviceLocator.getService(ISelectionService.class);
        this.partService = serviceLocator.getService(IPartService.class);
    }    
}

