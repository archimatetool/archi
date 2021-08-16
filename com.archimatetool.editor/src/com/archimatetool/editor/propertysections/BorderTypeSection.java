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

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IBorderType;


/**
 * Property Section for a Border Type
 * 
 * @author Phillip Beauvoir
 */
public abstract class BorderTypeSection extends AbstractECorePropertySection {
    
    protected Combo fComboBorderType;

    protected static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    protected abstract String[] getComboItems();
    
    @Override
    protected void createControls(Composite parent) {
        createLabel(parent, Messages.BorderTypeSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        // Combo
        fComboBorderType = new Combo(parent, SWT.READ_ONLY);
        getWidgetFactory().adapt(fComboBorderType, true, true);
        
        fComboBorderType.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                CompoundCommand result = new CompoundCommand();

                for(EObject eObject : getEObjects()) {
                    if(isAlive(eObject)) {
                        Command cmd = new EObjectFeatureCommand(Messages.BorderTypeSection_1, eObject,
                                IArchimatePackage.Literals.BORDER_TYPE__BORDER_TYPE, fComboBorderType.getSelectionIndex());
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
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            
            if(feature == IArchimatePackage.Literals.BORDER_TYPE__BORDER_TYPE ||
                    feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                update();
            }
        }
    }

    @Override
    protected void update() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        fComboBorderType.setItems(getComboItems());
        fComboBorderType.select(((IBorderType)getFirstSelectedObject()).getBorderType());
        fComboBorderType.setEnabled(!isLocked(getFirstSelectedObject()));
    }
    
}
