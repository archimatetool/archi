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
public class InfluenceRelationshipSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        protected boolean isRequiredType(Object object) {
            return object instanceof IInfluenceRelationship;
        }

        @Override
        protected Class<?> getAdaptableType() {
            return IInfluenceRelationship.class;
        }
    }

    /*
     * Adapter to listen to changes made elsewhere (including Undo/Redo commands)
     */
    private Adapter eAdapter = new AdapterImpl() {
        @Override
        public void notifyChanged(Notification msg) {
            Object feature = msg.getFeature();
            // Strength event (Undo/Redo and here)
            if(feature == IArchimatePackage.Literals.INFLUENCE_RELATIONSHIP__STRENGTH) {
                refreshControls();
            }
        }
    };
    
    private IInfluenceRelationship fInfluenceRelationship;
    
    private PropertySectionTextControl fTextStrength;

    @Override
    protected void createControls(Composite parent) {
        createLabel(parent, Messages.InfluenceRelationshipSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        Text text = createSingleTextControl(parent, SWT.NONE);
        
        fTextStrength = new PropertySectionTextControl(text, IArchimatePackage.Literals.INFLUENCE_RELATIONSHIP__STRENGTH) {
            @Override
            protected void textChanged(String oldText, String newText) {
                if(isAlive()) {
                    fIsExecutingCommand = true;
                    getCommandStack().execute(new EObjectFeatureCommand(Messages.InfluenceRelationshipSection_1, fInfluenceRelationship,
                            IArchimatePackage.Literals.INFLUENCE_RELATIONSHIP__STRENGTH, newText));
                    fIsExecutingCommand = false;
                }
            }
        };
        
        fTextStrength.setHint(Messages.InfluenceRelationshipSection_2);

        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }

    @Override
    protected void setElement(Object element) {
        fInfluenceRelationship = (IInfluenceRelationship)new Filter().adaptObject(element);
        if(fInfluenceRelationship == null) {
            System.err.println(getClass() + " failed to get element for " + element); //$NON-NLS-1$
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        if(fIsExecutingCommand) {
            return; 
        }
        
        fTextStrength.refresh(fInfluenceRelationship);
    }

    @Override
    protected Adapter getECoreAdapter() {
        return eAdapter;
    }
    
    @Override
    protected EObject getEObject() {
        return fInfluenceRelationship;
    }
}
