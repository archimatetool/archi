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
import com.archimatetool.model.IAccessRelationship;
import com.archimatetool.model.IArchimatePackage;



/**
 * Property Section for an Access Relationship
 * 
 * @author Phillip Beauvoir
 */
public class AccessRelationshipSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IAccessRelationship;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IAccessRelationship.class;
        }
    }

    private Combo fComboType;
    
    private static final String[] fComboTypeItems = {
        Messages.AccessRelationshipSection_0,
        Messages.AccessRelationshipSection_1,
        Messages.AccessRelationshipSection_2,
        Messages.AccessRelationshipSection_3
    };
    
    // Map original values to combo box positions
    private static final int[] fTypeValues = {
        IAccessRelationship.UNSPECIFIED_ACCESS,
        IAccessRelationship.READ_ACCESS,
        IAccessRelationship.WRITE_ACCESS,
        IAccessRelationship.READ_WRITE_ACCESS
    };
    
    @Override
    protected void createControls(Composite parent) {
        createLabel(parent, Messages.AccessRelationshipSection_4, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        fComboType = new Combo(parent, SWT.READ_ONLY);
        getWidgetFactory().adapt(fComboType, true, true);
        fComboType.setItems(fComboTypeItems);
        fComboType.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fComboType.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                CompoundCommand result = new CompoundCommand();

                for(EObject relationship : getEObjects()) {
                    if(isAlive(relationship)) {
                        Command cmd = new EObjectFeatureCommand(Messages.AccessRelationshipSection_5, relationship,
                                IArchimatePackage.Literals.ACCESS_RELATIONSHIP__ACCESS_TYPE, fTypeValues[fComboType.getSelectionIndex()]);
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
            if(feature == IArchimatePackage.Literals.ACCESS_RELATIONSHIP__ACCESS_TYPE) {
                update();
            }
        }
    }

    @Override
    protected void update() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        int type = ((IAccessRelationship)getFirstSelectedObject()).getAccessType();
        
        if(type < IAccessRelationship.WRITE_ACCESS || type > IAccessRelationship.READ_WRITE_ACCESS) {
            type = IAccessRelationship.WRITE_ACCESS;
        }
        
        type = fTypeValues[type]; // map theirs to ours
        
        fComboType.select(type);
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
