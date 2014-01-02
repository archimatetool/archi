/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.properties;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.properties.PropertySheet;

import com.archimatetool.editor.views.IModelView;



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
    public boolean isPinned() {
        return false;
    }
    
    @Override
    protected void partHidden(IWorkbenchPart part) {
        // Don't lose Properties if it's one of our views
        if(part instanceof IModelView) {
            return;
        }
        super.partHidden(part);
    }
}
