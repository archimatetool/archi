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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.commands.LineWidthCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ILineObject;



/**
 * Line Width Section
 * 
 * @author Phillip Beauvoir
 */
public final class LineWidthSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof ILineObject lo && shouldExposeFeature(lo, IArchimatePackage.Literals.LINE_OBJECT__LINE_WIDTH.getName());
        }

        @Override
        public Class<?> getAdaptableType() {
            return ILineObject.class;
        }
    }

    private Combo fComboLineWidth;
    
    public static final String[] comboLineWidthItems = {
            Messages.LineWidthSection_1,
            Messages.LineWidthSection_2,
            Messages.LineWidthSection_3
    };
    
    @Override
    protected void createControls(Composite parent) {
        createLineWidthComboControl(parent);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void createLineWidthComboControl(Composite parent) {
        createLabel(parent, Messages.LineWidthSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        fComboLineWidth = new Combo(parent, SWT.READ_ONLY);
        getWidgetFactory().adapt(fComboLineWidth, true, true);
        fComboLineWidth.setItems(comboLineWidthItems);
        
        fComboLineWidth.addSelectionListener(SelectionListener.widgetSelectedAdapter(e -> {
            CompoundCommand result = new CompoundCommand();

            for(EObject obj : getEObjects()) {
                if(isAlive(obj)) {
                    Command cmd = new LineWidthCommand((ILineObject)obj, fComboLineWidth.getSelectionIndex() + 1);
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
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            
            if(feature == IArchimatePackage.Literals.LINE_OBJECT__LINE_WIDTH) {
                refreshLineWidthCombo();
            }
            else if(feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                update();
            }
        }
    }

    @Override
    protected void update() {
        refreshLineWidthCombo();
    }
    
    private void refreshLineWidthCombo() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        ILineObject firstSelected = (ILineObject)getFirstSelectedObject();
        
        int lineWidth = firstSelected.getLineWidth();
        lineWidth = lineWidth < 1 ? 1 : lineWidth > 3 ? 3 : lineWidth;
        
        fComboLineWidth.select(lineWidth - 1);
        
        fComboLineWidth.setEnabled(!isLocked(firstSelected));
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
