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

import com.archimatetool.editor.diagram.commands.LockObjectCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ILockable;



/**
 * Property Section for a Locked element
 * 
 * @author Phillip Beauvoir
 */
public class LockedSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof ILockable;
        }

        @Override
        public Class<?> getAdaptableType() {
            return ILockable.class;
        }
    }

    private Button fButtonLocked;
    
    @Override
    protected void createControls(Composite parent) {
        createLabel(parent, Messages.LockedSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        fButtonLocked = getWidgetFactory().createButton(parent, null, SWT.CHECK);
        
        fButtonLocked.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                CompoundCommand result = new CompoundCommand();

                for(EObject lockable : getEObjects()) {
                    if(isAlive(lockable)) {
                        Command cmd = new LockObjectCommand((ILockable)lockable, fButtonLocked.getSelection());
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
            if(feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                update();
            }
        }
    }
    
    @Override
    protected void update() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        fButtonLocked.setSelection(((ILockable)getFirstSelectedObject()).isLocked());
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
