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
public class AccessRelationshipSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        boolean isRequiredType(Object object) {
            return object instanceof IAccessRelationship;
        }

        @Override
        Class<?> getAdaptableType() {
            return IAccessRelationship.class;
        }
    }

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Element Access type event (Undo/Redo and here)
            if(feature == IArchimatePackage.Literals.ACCESS_RELATIONSHIP__ACCESS_TYPE) {
                refreshControls();
            }
        }
    };
    
    private IAccessRelationship fAccessRelationship;

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
        fComboType.setItems(fComboTypeItems);
        fComboType.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fComboType.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(isAlive()) {
                    fIsExecutingCommand = true;
                    getCommandStack().execute(new EObjectFeatureCommand(Messages.AccessRelationshipSection_5,
                                                        fAccessRelationship,
                                                        IArchimatePackage.Literals.ACCESS_RELATIONSHIP__ACCESS_TYPE,
                                                        fTypeValues[fComboType.getSelectionIndex()]));
                    fIsExecutingCommand = false;
                }
            }
        });

        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }

    @Override
    protected void setElement(Object element) {
        fAccessRelationship = (IAccessRelationship)new Filter().adaptObject(element);
        if(fAccessRelationship == null) {
            System.err.println(getClass() + " failed to get element for " + element); //$NON-NLS-1$
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        int type = fAccessRelationship.getAccessType();
        if(type < IAccessRelationship.WRITE_ACCESS || type > IAccessRelationship.READ_WRITE_ACCESS) {
            type = IAccessRelationship.WRITE_ACCESS;
        }
        type = fTypeValues[type]; // map theirs to ours
        fComboType.select(type);
    }

    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }
    
    @Override
    protected EObject getEObject() {
        return fAccessRelationship;
    }
}
