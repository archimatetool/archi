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
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import com.archimatetool.editor.diagram.commands.LineWidthCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ILineObject;


/**
 * Line Width Composite
 * 
 * @author Phillip Beauvoir
 */
class LineWidthComposite {
    
    private static final String[] comboLineWidthItems = {
            Messages.LineWidthSection_1,
            Messages.LineWidthSection_2,
            Messages.LineWidthSection_3
    };
    
    private Combo fComboLineWidth;
    private AbstractECorePropertySection section;
    private Composite composite;
    
    LineWidthComposite(AbstractECorePropertySection section, Composite parent) {
        this.section = section;
        composite = section.createComposite(parent, 2, false);
        createLineWidthControl(composite);
    }
    
    Composite getComposite() {
        return composite;
    }

    private void createLineWidthControl(Composite parent) {
        section.createLabel(parent, Messages.LineWidthSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        fComboLineWidth = new Combo(parent, SWT.READ_ONLY);
        section.getWidgetFactory().adapt(fComboLineWidth, true, true);
        fComboLineWidth.setItems(comboLineWidthItems);
        
        fComboLineWidth.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
            CompoundCommand result = new CompoundCommand();

            for(EObject obj : section.getEObjects()) {
                if(isValidObject(obj)) {
                    Command cmd = new LineWidthCommand((ILineObject)obj, fComboLineWidth.getSelectionIndex() + 1);
                    if(cmd.canExecute()) {
                        result.add(cmd);
                    }
                }
            }

            section.executeCommand(result.unwrap());
        }));
    }
    
    void updateControl() {
        ILineObject lineObject = (ILineObject)section.getFirstSelectedObject();
        
        int lineWidth = lineObject.getLineWidth();
        lineWidth = lineWidth < 1 ? 1 : lineWidth > 3 ? 3 : lineWidth;
        
        fComboLineWidth.select(lineWidth - 1);
        
        fComboLineWidth.setEnabled(!section.isLocked(lineObject));
    }
    
    /**
     * In case of multi-selection we should check this
     */
    private boolean isValidObject(EObject eObject) {
        return section.isAlive(eObject) && 
                section.getFilter().shouldExposeFeature(eObject, IArchimatePackage.Literals.LINE_OBJECT__LINE_WIDTH.getName());
    }

    void dispose() {
        composite.dispose();
        composite = null;
        section = null;
        fComboLineWidth = null;
    }
}
