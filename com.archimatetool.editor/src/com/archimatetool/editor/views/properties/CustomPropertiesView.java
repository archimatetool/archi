/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.properties;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.views.properties.PropertySheet;



/**
 * Custom Properties View to remove the pinned and new instance options
 * 
 * @author Phillip Beauvoir
 */
public class CustomPropertiesView extends PropertySheet implements ICustomPropertiesView {

    @Override
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        
        // Remove the Pin item
        IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
        menuManager.removeAll();
//        for(IContributionItem item : menuManager.getItems()) {
//            if(item instanceof ActionContributionItem) {
//                if(((ActionContributionItem)item).getAction() instanceof PinPropertySheetAction) {
//                    menuManager.remove(item);
//                    break;
//                }
//            }
//        }
        
        IToolBarManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
        toolBarManager.removeAll();
//        for(IContributionItem item : toolBarManager.getItems()) {
//            if(item instanceof ActionContributionItem) {
//                if(((ActionContributionItem)item).getAction() instanceof PinPropertySheetAction) {
//                    toolBarManager.remove(item);
//                    break;
//                }
//            }
//        }
    }
    
    @Override
    protected ISaveablePart getSaveablePart() {
        /*
         * Eclipse 4.5 and 4.6 calls this to set the Properties View to dirty and shows an asterisk on the title bar.
         * This is so stupid. Really, really stupid.
         * See https://bugs.eclipse.org/bugs/show_bug.cgi?id=372799
         */
        return null;
    }
        
    @Override
    public boolean isPinned() {
        return false;
    }
    
    @Override
    public void dispose() {
        // HACK: If the Properties View is open and no other View is providing a Tabbed Property View
        // then we see the default table in the Properties View and an enabled "copy" action.
        // If the Properties View is then closed the global "copy" action is still enabled and
        // will cause a "Widget is disposed" exception if selected. So we disable it.
        IAction copyAction = getViewSite().getActionBars().getGlobalActionHandler("copy"); //$NON-NLS-1$
        if(copyAction != null) {
            copyAction.setEnabled(false);
        }
        
        super.dispose();
    }
}
