/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas.propertysections;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.canvas.model.ICanvasPackage;
import com.archimatetool.canvas.model.INotesContent;
import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.propertysections.AbstractECorePropertySection;
import com.archimatetool.editor.propertysections.IObjectFilter;
import com.archimatetool.editor.propertysections.ITabbedLayoutConstants;
import com.archimatetool.editor.propertysections.ObjectFilter;
import com.archimatetool.editor.propertysections.PropertySectionTextControl;
import com.archimatetool.editor.ui.components.StyledTextControl;
import com.archimatetool.model.IArchimatePackage;



/**
 * Property Section for Notes
 * 
 * @author Phillip Beauvoir
 */
public class NotesSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof INotesContent;
        }

        @Override
        public Class<?> getAdaptableType() {
            return INotesContent.class;
        }
    }

    
    private PropertySectionTextControl fTextNotesControl;
    
    @Override
    protected void createControls(Composite parent) {
        createLabel(parent, Messages.NotesSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.NONE);
        
        StyledTextControl styledTextControl = createStyledTextControl(parent, SWT.NONE);
        styledTextControl.setMessage(Messages.NotesSection_2);
        
        fTextNotesControl = new PropertySectionTextControl(styledTextControl.getControl(), ICanvasPackage.Literals.NOTES_CONTENT__NOTES) {
            @Override
            protected void textChanged(String oldText, String newText) {
                CompoundCommand result = new CompoundCommand();

                for(EObject notesContent : getEObjects()) {
                    if(isAlive(notesContent)) {
                        Command cmd = new EObjectFeatureCommand(Messages.NotesSection_1, notesContent,
                                ICanvasPackage.Literals.NOTES_CONTENT__NOTES, newText);
                        
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }

                executeCommand(result.unwrap());
            }
        };
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(fTextNotesControl.getTextControl(), HELP_ID);
    }

    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            // Model event
            if(feature == ICanvasPackage.Literals.NOTES_CONTENT__NOTES 
                    || feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                update();
            }
        }
    }

    @Override
    protected void update() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        fTextNotesControl.refresh(getFirstSelectedObject());
        
        fTextNotesControl.setEditable(!isLocked(getFirstSelectedObject()));
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
    
    @Override
    public boolean shouldUseExtraSpace() {
        return true;
    }
}
