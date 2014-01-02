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
import org.eclipse.gef.EditPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.commands.TextAlignmentCommand;
import com.archimatetool.editor.diagram.editparts.ITextAlignedEditPart;
import com.archimatetool.editor.ui.IArchimateImages;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IFontAttribute;
import com.archimatetool.model.ILockable;



/**
 * Property Section for a Diagram Model Object Appearance->Text Alignment
 * 
 * @author Phillip Beauvoir
 */
public class TextAlignmentSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Alignment event (From Undo/Redo and here)
            if(feature == IArchimatePackage.Literals.FONT_ATTRIBUTE__TEXT_ALIGNMENT ||
                    feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                refreshControls();
            }
        }
    };
    
    private IFontAttribute fFontObject;
    
    private Button[] fAlignmentButtons = new Button[3];
    
    @Override
    protected void createControls(final Composite parent) {
        SelectionAdapter adapter = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                for(int i = 0; i < fAlignmentButtons.length; i++) {
                    // Select/deselects
                    fAlignmentButtons[i].setSelection(e.widget == fAlignmentButtons[i]);
                    
                    // Command
                    if(fAlignmentButtons[i] == e.widget) {
                        int alignment = (Integer)fAlignmentButtons[i].getData();
                        if(fFontObject.getTextAlignment() != alignment) {
                            if(isAlive()) {
                                fIsExecutingCommand = true;
                                getCommandStack().execute(new TextAlignmentCommand(fFontObject, alignment));
                                fIsExecutingCommand = false;
                            }
                        }
                    }
                }
            }
        };
        
        createLabel(parent, Messages.TextAlignmentSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        Composite client = createComposite(parent, 3);
        
        for(int i = 0; i < fAlignmentButtons.length; i++) {
            fAlignmentButtons[i] = new Button(client, SWT.TOGGLE | SWT.FLAT);
            getWidgetFactory().adapt(fAlignmentButtons[i], true, true); // Need to do it this way for Mac
            fAlignmentButtons[i].addSelectionListener(adapter);
        }
        
        // Left Button
        fAlignmentButtons[0].setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_ALIGN_TEXT_LEFT));
        fAlignmentButtons[0].setData(IFontAttribute.TEXT_ALIGNMENT_LEFT);

        // Center Button
        fAlignmentButtons[1].setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_ALIGN_TEXT_CENTER));
        fAlignmentButtons[1].setData(IFontAttribute.TEXT_ALIGNMENT_CENTER);

        // Right Button
        fAlignmentButtons[2].setImage(IArchimateImages.ImageFactory.getImage(IArchimateImages.ICON_ALIGN_TEXT_RIGHT));
        fAlignmentButtons[2].setData(IFontAttribute.TEXT_ALIGNMENT_RIGHT);
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    protected void setElement(Object element) {
        if(element instanceof ITextAlignedEditPart && ((EditPart)element).getModel() instanceof IFontAttribute) {
            fFontObject = (IFontAttribute)((EditPart)element).getModel();
            if(fFontObject == null) {
                throw new RuntimeException("Font Object was null"); //$NON-NLS-1$
            }
        }
        else {
            throw new RuntimeException("Should have been an IFontAttribute"); //$NON-NLS-1$
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        for(int i = 0; i < fAlignmentButtons.length; i++) {
            fAlignmentButtons[i].setSelection(fAlignmentButtons[i] == getAlignmentButton());
            boolean enabled = fFontObject instanceof ILockable ? !((ILockable)fFontObject).isLocked() : true;
            fAlignmentButtons[i].setEnabled(enabled);
        }
    }
    
    private Button getAlignmentButton() {
        int alignment = fFontObject.getTextAlignment();
        
        switch(alignment) {
            case IFontAttribute.TEXT_ALIGNMENT_LEFT:
                return fAlignmentButtons[0];

            case IFontAttribute.TEXT_ALIGNMENT_CENTER:
                return fAlignmentButtons[1];

            case IFontAttribute.TEXT_ALIGNMENT_RIGHT:
                return fAlignmentButtons[2];

            default:
                return fAlignmentButtons[1];
        }
    }
    
    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }

    @Override
    protected EObject getEObject() {
        return fFontObject;
    }
}
