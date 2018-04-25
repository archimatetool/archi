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

import com.archimatetool.editor.diagram.commands.TextPositionCommand;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ITextPosition;



/**
 * Property Section for a Text Position
 * 
 * @author Phillip Beauvoir
 */
public class TextPositionSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    private static EAttribute FEATURE = IArchimatePackage.Literals.TEXT_POSITION__TEXT_POSITION;
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return (object instanceof ITextPosition) && shouldExposeFeature((EObject)object, FEATURE);
        }

        @Override
        public Class<?> getAdaptableType() {
            return ITextPosition.class;
        }
    }

    private Button[] fPositionButtons = new Button[3];
    
    @Override
    protected void createControls(Composite parent) {
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
            getWidgetFactory().adapt(fPositionButtons[i], true, true); // Need to do it this way for Mac
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
        
        for(int i = 0; i < fPositionButtons.length; i++) {
            fPositionButtons[i].setSelection(fPositionButtons[i] == getPositionButton());
            fPositionButtons[i].setEnabled(!isLocked(getFirstSelectedObject()));
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
