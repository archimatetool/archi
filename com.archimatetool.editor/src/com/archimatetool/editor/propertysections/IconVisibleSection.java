/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.commands.FeatureCommand;
import com.archimatetool.editor.ui.factory.IGraphicalObjectUIProvider;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IFeatures;



/**
 * Property Section for Icon visibility
 * 
 * @author Phillip Beauvoir
 */
public class IconVisibleSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IDiagramModelObject && hasIcon(object);
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelObject.class;
        }
        
        private boolean hasIcon(Object object) {
            IObjectUIProvider provider = ObjectUIFactory.INSTANCE.getProvider((EObject)object);
            return provider instanceof IGraphicalObjectUIProvider && ((IGraphicalObjectUIProvider)provider).hasIcon();
        }
    }

    private Button fIconVisibleButton; // Draw2D icon
    
    @Override
    protected void createControls(Composite parent) {
        createLabel(parent, Messages.IconVisibleSection_0 + ":", ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER); //$NON-NLS-1$
        
        fIconVisibleButton = new Button(parent, SWT.CHECK);
        fIconVisibleButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                CompoundCommand result = new CompoundCommand();

                for(EObject selected : getEObjects()) {
                    if(isAlive(selected)) {
                        Command cmd = new FeatureCommand(Messages.IconVisibleSection_0, (IFeatures)selected,
                                IDiagramModelObject.FEATURE_ICON_VISIBLE, fIconVisibleButton.getSelection(), IDiagramModelObject.FEATURE_ICON_VISIBLE_DEFAULT);
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }

                executeCommand(result.unwrap());
            }
        });
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }

    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            if(isFeatureNotification(msg, IDiagramModelObject.FEATURE_ICON_VISIBLE)) {
                refreshButton();
            }
        }
    }
    
    @Override
    protected void update() {
        refreshButton();
    }
    
    protected void refreshButton() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        IDiagramModelObject lastSelectedObject = (IDiagramModelObject)getFirstSelectedObject();
        
        fIconVisibleButton.setSelection(lastSelectedObject.isIconVisible());
        
        fIconVisibleButton.setEnabled(!isLocked(lastSelectedObject));
    }
}
