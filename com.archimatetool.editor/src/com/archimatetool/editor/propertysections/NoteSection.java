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
import com.archimatetool.model.IDiagramModelNote;



/**
 * Property Section for a Note
 * 
 * @author Phillip Beauvoir
 */
public class NoteSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IDiagramModelNote;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelNote.class;
        }
    }
    
    private Combo fComboBorderType;
    
    private String[] comboItems = {
            Messages.NoteSection_0,
            Messages.NoteSection_1,
            Messages.NoteSection_2
    };
    
    @Override
    protected void createControls(Composite parent) {
        createLabel(parent, Messages.NoteSection_3, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        // Combo
        fComboBorderType = new Combo(parent, SWT.READ_ONLY);
        fComboBorderType.setItems(comboItems);
        fComboBorderType.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                CompoundCommand result = new CompoundCommand();

                for(EObject note : getEObjects()) {
                    if(isAlive(note)) {
                        Command cmd = new EObjectFeatureCommand(Messages.NoteSection_4, note,
                                IArchimatePackage.Literals.DIAGRAM_MODEL_NOTE__BORDER_TYPE, fComboBorderType.getSelectionIndex());
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
            
            if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_NOTE__BORDER_TYPE ||
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
        
        fComboBorderType.select(((IDiagramModelNote)getFirstSelectedObject()).getBorderType());
        fComboBorderType.setEnabled(!isLocked(getFirstSelectedObject()));
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
