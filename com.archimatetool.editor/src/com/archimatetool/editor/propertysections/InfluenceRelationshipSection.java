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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IInfluenceRelationship;



/**
 * Property Section for an Influence Relationship
 * 
 * @author Phillip Beauvoir
 */
public class InfluenceRelationshipSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IInfluenceRelationship;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IInfluenceRelationship.class;
        }
    }

    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            
            if(feature == IArchimatePackage.Literals.INFLUENCE_RELATIONSHIP__STRENGTH) {
                update();
            }
        }
    }
    
    private PropertySectionTextControl fTextStrength;

    @Override
    protected void createControls(Composite parent) {
        createLabel(parent, Messages.InfluenceRelationshipSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        Text text = createSingleTextControl(parent, SWT.NONE);
        
        fTextStrength = new PropertySectionTextControl(text, IArchimatePackage.Literals.INFLUENCE_RELATIONSHIP__STRENGTH) {
            @Override
            protected void textChanged(String oldText, String newText) {
                CompoundCommand result = new CompoundCommand();

                for(EObject relationship : getEObjects()) {
                    if(isAlive(relationship)) {
                        Command cmd = new EObjectFeatureCommand(Messages.InfluenceRelationshipSection_1, relationship,
                                IArchimatePackage.Literals.INFLUENCE_RELATIONSHIP__STRENGTH, newText);
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }

                executeCommand(result.unwrap());
            }
        };
        
        fTextStrength.setHint(Messages.InfluenceRelationshipSection_2);

        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }

    @Override
    protected void update() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        fTextStrength.refresh(getFirstSelectedObject());
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
