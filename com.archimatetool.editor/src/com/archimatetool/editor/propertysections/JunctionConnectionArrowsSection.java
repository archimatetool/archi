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
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IFeatures;
import com.archimatetool.model.IJunction;



/**
 * Junction Line Arrows Section
 * 
 * @author Phillip Beauvoir
 */
public class JunctionConnectionArrowsSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IDiagramModelArchimateObject &&
                    ((IDiagramModelArchimateObject)object).getArchimateElement() instanceof IJunction;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelArchimateObject.class;
        }
    }

    private Button buttonHideArrowHeads;
    
    @Override
    protected void createControls(Composite parent) {
        createHideArrowsControl(parent);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void createHideArrowsControl(Composite parent) {
        createLabel(parent, Messages.JunctionConnectionArrowsSection_0 + ":", ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER); //$NON-NLS-1$
        
        buttonHideArrowHeads = getWidgetFactory().createButton(parent, null, SWT.CHECK);
        
        buttonHideArrowHeads.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                CompoundCommand result = new CompoundCommand();

                for(EObject junctionObject : getEObjects()) {
                    if(isAlive(junctionObject)) {
                        Command cmd = new FeatureCommand(Messages.JunctionConnectionArrowsSection_1,
                                                        (IFeatures)junctionObject,
                                                        IDiagramModelArchimateObject.FEATURE_HIDE_JUNCTION_ARROWS,
                                                        buttonHideArrowHeads.getSelection(),
                                                        IDiagramModelArchimateObject.FEATURE_HIDE_JUNCTION_ARROWS_DEFAULT);
                        
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }

                executeCommand(result.unwrap());
            }
        });
    }
    
    @Override
    protected void notifyChanged(Notification msg) {
        // Notifier is the Feature
        if(isFeatureNotification(msg, IDiagramModelArchimateObject.FEATURE_HIDE_JUNCTION_ARROWS)) {
            refreshButton();
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
        
        IDiagramModelArchimateObject lastSelected = (IDiagramModelArchimateObject)getFirstSelectedObject();
        
        buttonHideArrowHeads.setSelection(lastSelected.getFeatures()
                                                      .getBoolean(IDiagramModelArchimateObject.FEATURE_HIDE_JUNCTION_ARROWS,
                                                          IDiagramModelArchimateObject.FEATURE_HIDE_JUNCTION_ARROWS_DEFAULT));
        
        buttonHideArrowHeads.setEnabled(!isLocked(lastSelected));
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
