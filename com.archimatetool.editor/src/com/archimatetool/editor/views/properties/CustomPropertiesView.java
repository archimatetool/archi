/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.properties;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.PropertyShowInContext;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.model.IArchimateModel;



/**
 * Custom Properties View to remove the pinned and new instance options
 * 
 * @author Phillip Beauvoir
 */
public class CustomPropertiesView extends PropertySheet implements ICustomPropertiesView, ITabbedPropertySheetPageContributor {

    @Override
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        
        // Remove the Pin item
        IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
        removePinPropertySheetAction(menuManager);
        
        IToolBarManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
        removePinPropertySheetAction(toolBarManager);
        
        // Add an action to allow single column layouts
        IAction action = new Action(Messages.CustomPropertiesView_0, IAction.AS_CHECK_BOX) {};
        menuManager.add(action);
        action.setChecked(ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.PROPERTIES_SINGLE_COLUMN));
        
        action.addPropertyChangeListener(event -> {
            if(IAction.CHECKED.equals(event.getProperty())) {
                ArchiPlugin.PREFERENCES.setValue(IPreferenceConstants.PROPERTIES_SINGLE_COLUMN, action.isChecked());
                
                // Scroll bars need resizing
                if(getCurrentPage() instanceof TabbedPropertySheetPage) {
                    ((TabbedPropertySheetPage)getCurrentPage()).resizeScrolledComposite();
                }
            }
        });
    }
    
    private void removePinPropertySheetAction(IContributionManager manager) {
        manager.removeAll();
        
//        for(IContributionItem item : manager.getItems()) {
//            if(item instanceof ActionContributionItem) {
//                if(((ActionContributionItem)item).getAction() instanceof PinPropertySheetAction) {
//                    manager.remove(item);
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
    public void setFocus() {
        /*
         * Trap SWT Widget disposed exception so it doesn't clog up the error log.
         * 
         * This can happen under certain conditions (and it may be only on Windows):
         * 
         * 1. The Properties View is open
         * 3. An object is selected in a Diagram Editor that creates an additional Property Section (such as InfluenceRelationshipSection)
         * 3. Another View is opened which is stacked in the same stack just to the right of the Properties View
         * 4. Another object is selected in the Diagram editor that does not have the additional Property Section
         * 5. The other view is closed (with the close x button) putting the focus on the Properties View
         * 
         * setFocus() will then try to put the focus on this section which by now has been disposed
         */
        try {
            super.setFocus();
        }
        catch(SWTException ex) {
            if(ex.code != SWT.ERROR_WIDGET_DISPOSED) {
                throw ex;
            }
        }
    }
    
    @Override
    public boolean isPinned() {
        return false;
    }
    
    @Override
    public <T> T getAdapter(Class<T> adapter) {
        /*
         * If this View returns a TabbedPropertySheetPage then we don't get the nasty default table view
         */
        if(adapter == IPropertySheetPage.class) {
            return adapter.cast(new TabbedPropertySheetPage(this));
        }
        
        /*
         * Return the IArchimateModel in context of the part in context
         */
        if(adapter == IArchimateModel.class) {
            PropertyShowInContext context = (PropertyShowInContext)getShowInContext();
            return context.getPart() == null ? null : adapter.cast(context.getPart().getAdapter(IArchimateModel.class));
        }
        
        return super.getAdapter(adapter);
    }

    @Override
    public String getContributorId() {
        return ArchiPlugin.PLUGIN_ID;
    }

}
