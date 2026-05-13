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
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.commands.FeatureCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IFeatures;



/**
 * Connection Label Section
 * 
 * @author Phillip Beauvoir
 */
public class DiagramConnectionLabelSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IDiagramModelConnection;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelConnection.class;
        }
    }

    private Button fButtonDisplayName;
    
    @Override
    protected void createControls(Composite parent) {
        createDisplayNameControl(parent);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void createDisplayNameControl(Composite parent) {
        createLabel(parent, Messages.DiagramConnectionLabelSection_0 + ":", ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER); //$NON-NLS-1$
        
        fButtonDisplayName = getWidgetFactory().createButton(parent, null, SWT.CHECK);
        
        fButtonDisplayName.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
            CompoundCommand result = new CompoundCommand();

            for(EObject connection : getEObjects()) {
                if(isAlive(connection)) {
                    Command cmd = new FeatureCommand(Messages.DiagramConnectionLabelSection_0, (IFeatures)connection,
                            IDiagramModelConnection.FEATURE_NAME_VISIBLE, fButtonDisplayName.getSelection(), IDiagramModelConnection.FEATURE_NAME_VISIBLE_DEFAULT);
                    if(cmd.canExecute()) {
                        result.add(cmd);
                    }
                }
            }

            executeCommand(result.unwrap());
        }));
    }
    
    @Override
    protected void notifyChanged(Notification msg) {
        Object feature = msg.getFeature();

        if(isFeatureNotification(msg, IDiagramModelConnection.FEATURE_NAME_VISIBLE)) {
            refreshNameVisibleButton();
        }
        else if(feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
            update();
        }
    }

    @Override
    protected void update() {
        refreshNameVisibleButton();
    }
    
    private void refreshNameVisibleButton() {
        if(isExecutingCommand()) {
            return; 
        }
        
        IDiagramModelConnection lastSelectedConnection = (IDiagramModelConnection)getFirstSelectedObject();
        fButtonDisplayName.setSelection(lastSelectedConnection.isNameVisible());
        fButtonDisplayName.setEnabled(!isLocked(lastSelectedConnection));
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
