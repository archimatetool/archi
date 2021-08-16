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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.commands.TextAlignmentCommand;
import com.archimatetool.editor.diagram.commands.TextPositionCommand;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.ITextAlignment;
import com.archimatetool.model.ITextPosition;



/**
 * Property Section for a Text Alignment and Poistion object
 * 
 * @author Phillip Beauvoir
 */
public class TextAlignmentSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return (object instanceof ITextAlignment && shouldExposeFeature((EObject)object, IArchimatePackage.Literals.TEXT_ALIGNMENT__TEXT_ALIGNMENT.getName()))
                    || (object instanceof ITextPosition && shouldExposeFeature((EObject)object, IArchimatePackage.Literals.TEXT_POSITION__TEXT_POSITION.getName()));
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelObject.class;
        }
    }

    private Button[] fAlignmentButtons = new Button[3];
    private Button[] fPositionButtons = new Button[3];
    
    @Override
    protected void createControls(final Composite parent) {
        ((GridLayout)parent.getLayout()).horizontalSpacing = 30;
        
        Composite group1 = createComposite(parent, 2, false);
        createTextAlignmentControls(group1);
        
        Composite group2 = createComposite(parent, 2, false);
        createTextPositionControls(group2);
        
        // Allow setting 1 or 2 columns
        GridLayoutColumnHandler.create(parent, 2).updateColumns();
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void createTextAlignmentControls(Composite parent) {
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
            if(!PlatformUtils.isLinux()) { // Doesn't show focus on Linux
                getWidgetFactory().adapt(fAlignmentButtons[i], true, true);
            }
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
    }
    
    private void createTextPositionControls(Composite parent) {
        SelectionAdapter adapter = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                for(int i = 0; i < fPositionButtons.length; i++) {
                    // Select/deselects
                    fPositionButtons[i].setSelection(e.widget == fPositionButtons[i]);
                    
                    // Commands
                    if(fPositionButtons[i] == e.widget) {
                        int position = (Integer)fPositionButtons[i].getData();
                        
                        CompoundCommand result = new CompoundCommand();
                        
                        for(EObject textPosition : getEObjects()) {
                            if(((ITextPosition)textPosition).getTextPosition() != position && isAlive(textPosition)) {
                                Command cmd = new TextPositionCommand((ITextPosition)textPosition, position);
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

        createLabel(parent, Messages.TextPositionSection_3 + ":", ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER); //$NON-NLS-1$
        
        Composite client = createComposite(parent, 3);
        
        for(int i = 0; i < fPositionButtons.length; i++) {
            fPositionButtons[i] = new Button(client, SWT.TOGGLE | SWT.FLAT);
            if(!PlatformUtils.isLinux()) { // Doesn't show focus on Linux
                getWidgetFactory().adapt(fPositionButtons[i], true, true);
            }
            fPositionButtons[i].addSelectionListener(adapter);
        }
        
        // Top Button
        fPositionButtons[0].setImage(IArchiImages.ImageFactory.getImage(IArchiImages.ICON_ALIGN_TEXT_TOP));
        fPositionButtons[0].setData(ITextPosition.TEXT_POSITION_TOP);

        // Middle Button
        fPositionButtons[1].setImage(IArchiImages.ImageFactory.getImage(IArchiImages.ICON_ALIGN_TEXT_MIDDLE));
        fPositionButtons[1].setData(ITextPosition.TEXT_POSITION_CENTRE);

        // Bottom Button
        fPositionButtons[2].setImage(IArchiImages.ImageFactory.getImage(IArchiImages.ICON_ALIGN_TEXT_BOTTOM));
        fPositionButtons[2].setData(ITextPosition.TEXT_POSITION_BOTTOM);
    }
    
    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();

            if(feature == IArchimatePackage.Literals.TEXT_ALIGNMENT__TEXT_ALIGNMENT) {
                updateTextAlignmentControls();
            }
            else if(feature == IArchimatePackage.Literals.TEXT_POSITION__TEXT_POSITION) {
                updateTextPositionControls();
            }
            else if(feature == IArchimatePackage.Literals.LOCKABLE__LOCKED) {
                update();
            }
        }
    }

    @Override
    protected void update() {
        updateTextAlignmentControls();
        updateTextPositionControls();
    }
    
    private void updateTextAlignmentControls() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        IArchimateModelObject firstSelected = getFirstSelectedObject();

        for(int i = 0; i < fAlignmentButtons.length; i++) {
            fAlignmentButtons[i].setSelection(fAlignmentButtons[i] == getAlignmentButton());
            fAlignmentButtons[i].setEnabled(!isLocked(firstSelected) && firstSelected instanceof ITextAlignment);
        }
    }
    
    private void updateTextPositionControls() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        IArchimateModelObject firstSelected = getFirstSelectedObject();
        
        for(int i = 0; i < fPositionButtons.length; i++) {
            fPositionButtons[i].setSelection(fPositionButtons[i] == getPositionButton());
            fPositionButtons[i].setEnabled(!isLocked(firstSelected) && firstSelected instanceof ITextPosition);
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
    
    private Button getPositionButton() {
        int position = ((ITextPosition)getFirstSelectedObject()).getTextPosition();
        
        switch(position) {
            case ITextPosition.TEXT_POSITION_TOP:
                return fPositionButtons[0];

            case ITextPosition.TEXT_POSITION_CENTRE:
                return fPositionButtons[1];

            case ITextPosition.TEXT_POSITION_BOTTOM:
                return fPositionButtons[2];

            default:
                return fPositionButtons[1];
        }
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
