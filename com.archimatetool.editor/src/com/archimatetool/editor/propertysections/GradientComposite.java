/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import com.archimatetool.editor.model.commands.FeatureCommand;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IFeatures;


/**
 * Gradient Composite
 * 
 * @author Phillip Beauvoir
 */
class GradientComposite {
    
    private static String[] GRADIENT_STYLES = {
            Messages.GradientSection_2,
            Messages.GradientSection_3,
            Messages.GradientSection_4,
            Messages.GradientSection_5,
            Messages.GradientSection_6,
    };

    private Combo fGradientCombo;
    private AbstractECorePropertySection section;
    private Composite composite;
    
    GradientComposite(AbstractECorePropertySection section, Composite parent) {
        this.section = section;
        composite = section.createComposite(parent, 2, false);
        createGradientControl(composite);
    }

    Composite getComposite() {
        return composite;
    }
    
    private void createGradientControl(Composite parent) {
        section.createLabel(parent, Messages.GradientSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        fGradientCombo = new Combo(parent, SWT.READ_ONLY);
        section.getWidgetFactory().adapt(fGradientCombo, true, true);
        fGradientCombo.setItems(GRADIENT_STYLES);
        
        fGradientCombo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                CompoundCommand result = new CompoundCommand();

                for(EObject object : section.getEObjects()) {
                    if(isValidObject(object)) {
                        Command cmd = new FeatureCommand(Messages.GradientSection_1, (IFeatures)object,
                                IDiagramModelObject.FEATURE_GRADIENT, fGradientCombo.getSelectionIndex() - 1, IDiagramModelObject.FEATURE_GRADIENT_DEFAULT);
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }

                section.executeCommand(result.unwrap());
            }
        });
    }
    
    /**
     * In case of multi-selection we should check this
     */
    private boolean isValidObject(EObject eObject) {
        return section.isAlive(eObject) && 
                section.getFilter().shouldExposeFeature(eObject, IDiagramModelObject.FEATURE_GRADIENT);
    }
    
    void updateControl() {
        IDiagramModelObject lastSelected = (IDiagramModelObject)section.getFirstSelectedObject();
        fGradientCombo.select(lastSelected.getGradient() + 1);
        fGradientCombo.setEnabled(!section.isLocked(lastSelected));
    }
    
    void dispose() {
        composite.dispose();
        composite = null;
        section = null;
        fGradientCombo = null;
    }
}
