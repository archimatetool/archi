/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.IFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IInterfaceElement;



/**
 * Property Section for an Archimate Element
 * 
 * @author Phillip Beauvoir
 */
public class InterfaceElementSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter implements IFilter {
        @Override
        public boolean select(Object object) {
            return object instanceof IInterfaceElement ||
                (object instanceof IAdaptable &&
                        ((IAdaptable)object).getAdapter(IArchimateElement.class) instanceof IInterfaceElement);
        }
    }

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Element Interface event (Undo/Redo and here!)
            if(feature == IArchimatePackage.Literals.INTERFACE_ELEMENT__INTERFACE_TYPE) {
                refreshControls();
                fPage.labelProviderChanged(null); // Update icon on Main label
            }
        }
    };
    
    private IInterfaceElement fInterfaceElement;

    private Combo fComboInterfaceType;
    
    private static final String[] fComboInterfaceItems = {
        Messages.InterfaceElementSection_0,
        Messages.InterfaceElementSection_1
    };
    
    @Override
    protected void createControls(Composite parent) {
        createLabel(parent, Messages.InterfaceElementSection_2, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        fComboInterfaceType = new Combo(parent, SWT.READ_ONLY);
        fComboInterfaceType.setItems(fComboInterfaceItems);
        fComboInterfaceType.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fComboInterfaceType.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(isAlive()) {
                    fIsExecutingCommand = true;
                    getCommandStack().execute(new EObjectFeatureCommand(Messages.InterfaceElementSection_3,
                            fInterfaceElement, IArchimatePackage.Literals.INTERFACE_ELEMENT__INTERFACE_TYPE,
                            fComboInterfaceType.getSelectionIndex()));
                    fIsExecutingCommand = false;
                }
            }
        });

        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }

    @Override
    protected void setElement(Object element) {
        // IArchimateElement
        if(element instanceof IInterfaceElement) {
            fInterfaceElement = (IInterfaceElement)element;
        }
        // IArchimateElement in a GEF Edit Part
        else if(element instanceof IAdaptable) {
            fInterfaceElement = (IInterfaceElement)((IAdaptable)element).getAdapter(IArchimateElement.class);
        }
        else {
            System.err.println("InterfaceElementSection wants to display for " + element); //$NON-NLS-1$
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        int type = fInterfaceElement.getInterfaceType();
        fComboInterfaceType.select(type);
    }

    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }
    
    @Override
    protected EObject getEObject() {
        return fInterfaceElement;
    }
}
