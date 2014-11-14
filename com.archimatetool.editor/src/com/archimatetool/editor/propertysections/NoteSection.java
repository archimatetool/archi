/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.ILockable;



/**
 * Property Section for a Note
 * 
 * @author Phillip Beauvoir
 */
public class NoteSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        protected boolean isRequiredType(Object object) {
            return object instanceof IDiagramModelNote;
        }

        @Override
        protected Class<?> getAdaptableType() {
            return IDiagramModelNote.class;
        }
    }

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Model Name event (Undo/Redo and here!)
            if(feature == IArchimatePackage.Literals.DIAGRAM_MODEL_NOTE__BORDER_TYPE ||
                    feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                refreshControls();
            }
        }
    };
    
    private IDiagramModelNote fNote;
    
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
                if(isAlive()) {
                    fIsExecutingCommand = true;
                    getCommandStack().execute(new EObjectFeatureCommand(Messages.NoteSection_4, fNote,
                            IArchimatePackage.Literals.DIAGRAM_MODEL_NOTE__BORDER_TYPE, fComboBorderType.getSelectionIndex()));
                    fIsExecutingCommand = false;
                }
            }
        });
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }

    @Override
    protected void setElement(Object element) {
        fNote = (IDiagramModelNote)new Filter().adaptObject(element);
        if(fNote == null) {
            System.err.println(getClass() + " failed to get element for " + element); //$NON-NLS-1$
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        if(fIsExecutingCommand) {
            return; 
        }
        fComboBorderType.select(fNote.getBorderType());
        
        boolean enabled = fNote instanceof ILockable ? !((ILockable)fNote).isLocked() : true;
        fComboBorderType.setEnabled(enabled);
    }
    
    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fNote;
    }
}
