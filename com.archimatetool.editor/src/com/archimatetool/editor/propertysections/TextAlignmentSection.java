/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.commands.TextAlignmentCommand;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ITextAlignment;



/**
 * Property Section for a Text Alignment object
 * 
 * @author Phillip Beauvoir
 */
public class TextAlignmentSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    private static EAttribute FEATURE = IArchimatePackage.Literals.TEXT_ALIGNMENT__TEXT_ALIGNMENT;
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return (object instanceof ITextAlignment) && shouldExposeFeature((EObject)object, FEATURE.getName());
        }

        @Override
        public Class<?> getAdaptableType() {
            return ITextAlignment.class;
        }
    }

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
                        
                        CompoundCommand result = new CompoundCommand();
                        
                        for(EObject textAlignment : getEObjects()) {
                            if(((ITextAlignment)textAlignment).getTextAlignment() != alignment && isAlive(textAlignment)) {
                                Command cmd = new TextAlignmentCommand((ITextAlignment)textAlignment, alignment);
                                if(cmd.canExecute()) {
                                    result.add(cmd);
                                }
                            }
                        }

                        executeCommand(result.unwrap());
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
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();

            if(feature == FEATURE || feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                update();
            }
        }
    }

    @Override
    protected void update() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        for(int i = 0; i < fAlignmentButtons.length; i++) {
            fAlignmentButtons[i].setSelection(fAlignmentButtons[i] == getAlignmentButton());
            fAlignmentButtons[i].setEnabled(!isLocked(getFirstSelectedObject()));
        }
    }
    
    private Button getAlignmentButton() {
        int alignment = ((ITextAlignment)getFirstSelectedObject()).getTextAlignment();
        
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
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
