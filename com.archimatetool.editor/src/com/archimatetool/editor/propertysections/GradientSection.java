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
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IFeatures;



/**
 * Gradient Section
 * 
 * @author Phillip Beauvoir
 */
public class GradientSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return (object instanceof IDiagramModelObject) && shouldExposeFeature((EObject)object, IDiagramModelObject.FEATURE_GRADIENT);
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelObject.class;
        }
    }

    private Combo fGradientCombo;
    
    private String[] GRADIENT_STYLES = {
            Messages.GradientSection_2,
            Messages.GradientSection_3,
            Messages.GradientSection_4,
            Messages.GradientSection_5,
            Messages.GradientSection_6,
    };
    @Override
    protected void createControls(Composite parent) {
        createGradientControl(parent);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void createGradientControl(Composite parent) {
        createLabel(parent, Messages.GradientSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        fGradientCombo = new Combo(parent, SWT.READ_ONLY);
        getWidgetFactory().adapt(fGradientCombo, true, true);
        fGradientCombo.setItems(GRADIENT_STYLES);
        
        fGradientCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                CompoundCommand result = new CompoundCommand();

                for(EObject object : getEObjects()) {
                    if(isAlive(object)) {
                        Command cmd = new FeatureCommand(Messages.GradientSection_1, (IFeatures)object,
                                IDiagramModelObject.FEATURE_GRADIENT, fGradientCombo.getSelectionIndex() - 1, IDiagramModelObject.FEATURE_GRADIENT_DEFAULT);
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
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            if(feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                update();
            }
        }
        
        // Notifier is the Feature
        if(isFeatureNotification(msg, IDiagramModelObject.FEATURE_GRADIENT)) {
            refreshGradientButton();
        }
    }

    @Override
    protected void update() {
        refreshGradientButton();
    }
    
    protected void refreshGradientButton() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        IDiagramModelObject lastSelected = (IDiagramModelObject)getFirstSelectedObject();
        fGradientCombo.select(lastSelected.getGradient() + 1);
        
        fGradientCombo.setEnabled(!isLocked(lastSelected));
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
