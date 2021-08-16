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

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IJunction;



/**
 * Property Section for a Junction
 * 
 * @author Phillip Beauvoir
 */
public class JunctionTypeSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IJunction;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IJunction.class;
        }
    }

    private Combo fComboType;
    
    private static final String[] fComboTypeItems = {
        Messages.JunctionTypeSection_0,
        Messages.JunctionTypeSection_1,
    };
    
    // Map original values to combo box positions
    private static final String[] fTypeValues = {
        IJunction.AND_JUNCTION_TYPE,
        IJunction.OR_JUNCTION_TYPE,
    };
    
    @Override
    protected void createControls(Composite parent) {
        createLabel(parent, Messages.JunctionTypeSection_2, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        fComboType = new Combo(parent, SWT.READ_ONLY);
        fComboType.setItems(fComboTypeItems);
        fComboType.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        getWidgetFactory().adapt(fComboType, true, true);
        
        fComboType.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                CompoundCommand result = new CompoundCommand();

                for(EObject junction : getEObjects()) {
                    if(isAlive(junction)) {
                        Command cmd = new EObjectFeatureCommand(Messages.JunctionTypeSection_3,
                                junction,
                                IArchimatePackage.Literals.JUNCTION__TYPE,
                                fTypeValues[fComboType.getSelectionIndex()]);
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }

                executeCommand(result.unwrap());
            }
        });

        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }

    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            
            if(feature == IArchimatePackage.Literals.JUNCTION__TYPE) {
                update();
            }
        }
    }

    @Override
    protected void update() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        String type = ((IJunction)getFirstSelectedObject()).getType();
        
        if(type == null) {
            type = IJunction.AND_JUNCTION_TYPE;
        }
        
        switch(type) {
            case IJunction.AND_JUNCTION_TYPE:
            default:
                fComboType.select(0);
                break;

            case IJunction.OR_JUNCTION_TYPE:
                fComboType.select(1);
                break;
        }
    }

    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }

}
