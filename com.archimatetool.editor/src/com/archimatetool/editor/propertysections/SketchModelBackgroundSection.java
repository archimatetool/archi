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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.sketch.ISketchEditor;
import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.ISketchModel;



/**
 * Property Section for a Sketch Model's Background type
 * 
 * @author Phillip Beauvoir
 */
public class SketchModelBackgroundSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.sketchModelDiagramSection"; //$NON-NLS-1$

    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof ISketchModel;
        }

        @Override
        public Class<?> getAdaptableType() {
            return ISketchModel.class;
        }
    }

    private Combo fComboBackground;
    
    @Override
    protected void createControls(Composite parent) {
        createBackgroundControl(parent);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void createBackgroundControl(Composite parent) {
        // Label
        createLabel(parent, Messages.SketchModelBackgroundSection_0, ITabbedLayoutConstants.BIG_LABEL_WIDTH, SWT.CENTER);
        
        // Combo
        fComboBackground = new Combo(parent, SWT.READ_ONLY);
        fComboBackground.setItems(ISketchEditor.BACKGROUNDS);
        getWidgetFactory().adapt(fComboBackground, true, true);
        
        fComboBackground.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                CompoundCommand result = new CompoundCommand();

                for(EObject sketchModel : getEObjects()) {
                    if(isAlive(sketchModel)) {
                        Command cmd = new EObjectFeatureCommand(Messages.SketchModelBackgroundSection_1,
                                sketchModel,
                                IArchimatePackage.Literals.SKETCH_MODEL__BACKGROUND,
                                fComboBackground.getSelectionIndex());
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }

                executeCommand(result.unwrap());
            }
        });
        
        GridData gd = new GridData(SWT.NONE, SWT.NONE, true, false);
        gd.minimumWidth = ITabbedLayoutConstants.COMBO_WIDTH;
        fComboBackground.setLayoutData(gd);
    }

    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            
            if(feature == IArchimatePackage.Literals.SKETCH_MODEL__BACKGROUND) {
                update();
            }
        }
    }

    @Override
    protected void update() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        fComboBackground.select(((ISketchModel)getFirstSelectedObject()).getBackground());
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
