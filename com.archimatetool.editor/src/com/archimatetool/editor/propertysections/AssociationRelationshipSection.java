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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IAssociationRelationship;



/**
 * Property Section for an Association Relationship
 * 
 * @author Phillip Beauvoir
 */
public class AssociationRelationshipSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IAssociationRelationship;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IAssociationRelationship.class;
        }
    }

    private Button fButtonDirected;
    
    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            
            if(feature == IArchimatePackage.Literals.ASSOCIATION_RELATIONSHIP__DIRECTED) {
                update();
            }
        }
    }
    
    @Override
    protected void createControls(Composite parent) {
        createLabel(parent, Messages.AssociationRelationshipSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        fButtonDirected = getWidgetFactory().createButton(parent, null, SWT.CHECK);
        
        fButtonDirected.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                CompoundCommand result = new CompoundCommand();

                for(EObject relationship : getEObjects()) {
                    if(isAlive(relationship)) {
                        Command cmd = new EObjectFeatureCommand(Messages.AssociationRelationshipSection_1, relationship,
                                IArchimatePackage.Literals.ASSOCIATION_RELATIONSHIP__DIRECTED, fButtonDirected.getSelection());
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
    protected void update() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        fButtonDirected.setSelection(((IAssociationRelationship)getFirstSelectedObject()).isDirected());
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
