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
import org.eclipse.swt.widgets.Combo;
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

    private Combo fIconVisibleCombo; // ArchiMate Draw2D icon
    
    private String[] VISIBLE_CHOICES = {
            Messages.IconVisibleSection_1,
            Messages.IconVisibleSection_2,
            Messages.IconVisibleSection_3
    };
    
    @Override
    protected void createControls(Composite parent) {
        createLabel(parent, Messages.IconVisibleSection_0 + ":", ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER); //$NON-NLS-1$
        
        fIconVisibleCombo = new Combo(parent, SWT.READ_ONLY);
        getWidgetFactory().adapt(fIconVisibleCombo, true, true);
        fIconVisibleCombo.setItems(VISIBLE_CHOICES);
        
        fIconVisibleCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                CompoundCommand result = new CompoundCommand();

                for(EObject selected : getEObjects()) {
                    if(isAlive(selected)) {
                        Command cmd = new FeatureCommand(Messages.IconVisibleSection_0, (IFeatures)selected,
                                IDiagramModelObject.FEATURE_ICON_VISIBLE, mapFromComboToValue(), IDiagramModelObject.FEATURE_ICON_VISIBLE_DEFAULT);
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
    
    private void refreshButton() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        IDiagramModelObject lastSelectedObject = (IDiagramModelObject)getFirstSelectedObject();
        
        fIconVisibleCombo.select(mapFromValueToCombo(lastSelectedObject.getIconVisibleState()));
        
        fIconVisibleCombo.setEnabled(!isLocked(lastSelectedObject));
    }
    
    /**
     * map from combo index value to model value
     */
    private int mapFromComboToValue() {
        switch(fIconVisibleCombo.getSelectionIndex()) {
            case 0:
            default:
                return IDiagramModelObject.ICON_VISIBLE_IF_NO_IMAGE_DEFINED;

            case 1:
                return IDiagramModelObject.ICON_VISIBLE_ALWAYS;

            case 2:
                return IDiagramModelObject.ICON_VISIBLE_NEVER;
        }
    }
    
    /**
     * map from model value to combo index value
     */
    private int mapFromValueToCombo(int value) {
        switch(value) {
            case IDiagramModelObject.ICON_VISIBLE_IF_NO_IMAGE_DEFINED:
            default:
                return 0;

            case IDiagramModelObject.ICON_VISIBLE_ALWAYS:
                return 1;

            case IDiagramModelObject.ICON_VISIBLE_NEVER:
                return 2;
        }
    }

}
