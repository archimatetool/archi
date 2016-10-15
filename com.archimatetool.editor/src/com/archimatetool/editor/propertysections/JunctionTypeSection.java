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
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IJunction;



/**
 * Property Section for a Junction
 * 
 * @author Phillip Beauvoir
 */
public class JunctionTypeSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        protected boolean isRequiredType(Object object) {
            return object instanceof IJunction;
        }

        @Override
        protected Class<?> getAdaptableType() {
            return IJunction.class;
        }
    }

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Element type event (Undo/Redo and here)
            if(feature == IArchimatePackage.Literals.JUNCTION__TYPE) {
                refreshControls();
            }
        }
    };
    
    private IJunction fJunction;

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
        fComboType.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(isAlive()) {
                    fIsExecutingCommand = true;
                    getCommandStack().execute(new EObjectFeatureCommand(Messages.AccessRelationshipSection_5,
                                                        fJunction,
                                                        IArchimatePackage.Literals.JUNCTION__TYPE,
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
        fJunction = (IJunction)new Filter().adaptObject(element);
        if(fJunction == null) {
            System.err.println(getClass() + " failed to get element for " + element); //$NON-NLS-1$
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        String type = fJunction.getType();
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
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }
    
    @Override
    protected EObject getEObject() {
        return fJunction;
    }
}
