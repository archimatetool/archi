/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.commands.TextAlignmentCommand;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ILockable;
import com.archimatetool.model.ITextAlignment;



/**
 * Property Section for a Text Alignment object
 * 
 * @author Phillip Beauvoir
 */
public class TextAlignmentSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    private static EAttribute FEATURE = IArchimatePackage.Literals.TEXT_ALIGNMENT__TEXT_ALIGNMENT;
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        protected boolean isRequiredType(Object object) {
            return (object instanceof ITextAlignment) && shouldExposeFeature((EObject)object, FEATURE);
        }

        @Override
        protected Class<?> getAdaptableType() {
            return ITextAlignment.class;
        }
    }

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Alignment event (From Undo/Redo and here)
            if(feature == FEATURE || feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                refreshControls();
            }
        }
    };
    
    private ITextAlignment fTextAlignmentObject;
    
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
                        if(fTextAlignmentObject.getTextAlignment() != alignment) {
                            if(isAlive()) {
                                fIsExecutingCommand = true;
                                getCommandStack().execute(new TextAlignmentCommand(fTextAlignmentObject, alignment));
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
        fAlignmentButtons[0].setImage(IArchiImages.ImageFactory.getImage(IArchiImages.ICON_ALIGN_TEXT_LEFT));
        fAlignmentButtons[0].setData(ITextAlignment.TEXT_ALIGNMENT_LEFT);

        // Center Button
        fAlignmentButtons[1].setImage(IArchiImages.ImageFactory.getImage(IArchiImages.ICON_ALIGN_TEXT_CENTER));
        fAlignmentButtons[1].setData(ITextAlignment.TEXT_ALIGNMENT_CENTER);

        // Right Button
        fAlignmentButtons[2].setImage(IArchiImages.ImageFactory.getImage(IArchiImages.ICON_ALIGN_TEXT_RIGHT));
        fAlignmentButtons[2].setData(ITextAlignment.TEXT_ALIGNMENT_RIGHT);
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    @Override
    protected void setElement(Object element) {
        fTextAlignmentObject = (ITextAlignment)new Filter().adaptObject(element);
        if(fTextAlignmentObject == null) {
            System.err.println(getClass() + " failed to get element for " + element); //$NON-NLS-1$
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        for(int i = 0; i < fAlignmentButtons.length; i++) {
            fAlignmentButtons[i].setSelection(fAlignmentButtons[i] == getAlignmentButton());
            boolean enabled = fTextAlignmentObject instanceof ILockable ? !((ILockable)fTextAlignmentObject).isLocked() : true;
            fAlignmentButtons[i].setEnabled(enabled);
        }
    }
    
    private Button getAlignmentButton() {
        int alignment = fTextAlignmentObject.getTextAlignment();
        
        switch(alignment) {
            case ITextAlignment.TEXT_ALIGNMENT_LEFT:
                return fAlignmentButtons[0];

            case ITextAlignment.TEXT_ALIGNMENT_CENTER:
                return fAlignmentButtons[1];

            case ITextAlignment.TEXT_ALIGNMENT_RIGHT:
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
        return fTextAlignmentObject;
    }
}
