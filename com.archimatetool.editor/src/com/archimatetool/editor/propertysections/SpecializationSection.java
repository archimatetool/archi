/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.commands.SetProfileCommand;
import com.archimatetool.editor.tools.ProfilesManagerDialog;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IProfile;
import com.archimatetool.model.util.ArchimateModelUtils;
import com.archimatetool.model.util.LightweightEContentAdapter;



/**
 * Property Section for a Specialization
 * 
 * @author Phillip Beauvoir
 */
public class SpecializationSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IArchimateConcept;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IArchimateConcept.class;
        }
    }

    private ComboViewer fComboViewer;
    
    /**
     * Set this to true when updating control to stop recursive update
     */
    private boolean fIsRefreshing;
    
    /**
     * Dummy Profile representing "none"
     */
    private IProfile NONE_PROFILE;
    
    /**
     * Model that we are listening to changes on
     * Store this model in case the selected object is deleted
     */
    private IArchimateModel fModel;

    /**
     * Adapter to listen to Model's Profile changes
     */
    private LightweightEContentAdapter eAdapter = new LightweightEContentAdapter(this::notifyChanged, IProfile.class);
    
    
    @Override
    protected void createControls(Composite parent) {
        NONE_PROFILE = IArchimateFactory.eINSTANCE.createProfile();
        NONE_PROFILE.setName(Messages.SpecializationSection_0);
        
        createLabel(parent, Messages.SpecializationSection_1, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        Composite comp = createComposite(parent, 2);
        comp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        fComboViewer = new ComboViewer(new Combo(comp, SWT.READ_ONLY | SWT.BORDER));
        fComboViewer.getCombo().setVisibleItemCount(12);
        fComboViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        getWidgetFactory().adapt(fComboViewer.getControl(), true, true);
        
        fComboViewer.addSelectionChangedListener(event -> {
            if(fIsRefreshing) { // A Viewer will get a selectionChanged event when setting it
                return;
            }

            IProfile profile = (IProfile)((IStructuredSelection)event.getSelection()).getFirstElement();
            if(profile != null) {
                // None Profile is null
                if(profile == NONE_PROFILE) {
                    profile = null;
                }
                
                CompoundCommand result = new CompoundCommand();

                for(EObject object : getEObjects()) {
                    if(isAlive(object)) {
                        Command cmd = new SetProfileCommand((IArchimateConcept)object, profile);
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }

                executeCommand(result.unwrap());
            }
        });
        
        fComboViewer.setContentProvider(new IStructuredContentProvider() {
            /**
             * Return a list of suitable Profiles in the model given the concept type of the first selected object
             */
            @Override
            public Object[] getElements(Object inputElement) {
                IArchimateConcept firstSelected = (IArchimateConcept)getFirstSelectedObject();
                
                if(firstSelected == null) {
                    return new Object[0];
                }
                
                List<IProfile> list = ArchimateModelUtils.findProfilesForConceptType(firstSelected.getArchimateModel(), firstSelected.eClass());
                
                // Sort the Profiles by name
                Collections.sort(list, new Comparator<IProfile>() {
                    @Override
                    public int compare(IProfile p1, IProfile p2) {
                        return p1.getName().compareToIgnoreCase(p2.getName());
                    }
                });

                // Add the "none" Profile at the top
                list.add(0, NONE_PROFILE);
                
                return list.toArray();
            }
        });
        
        fComboViewer.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {
                return ((IProfile)element).getName();
            }
        });
        
        fComboViewer.setInput(""); //$NON-NLS-1$
        
        // Open Profiles Manager Dialog button
        Button button = getWidgetFactory().createButton(comp, null, SWT.PUSH);
        button.setText(" ... "); //$NON-NLS-1$
        button.setToolTipText(Messages.SpecializationSection_2);
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                IArchimateModelObject selected = getFirstSelectedObject();
                if(selected != null && selected.getArchimateModel() != null) {
                    ProfilesManagerDialog dialog = new ProfilesManagerDialog(getPart().getSite().getShell(), selected.getArchimateModel());
                    dialog.setDefaultClass(selected.eClass());
                    dialog.open();
                }
            }
        });
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }

    @Override
    protected void notifyChanged(Notification msg) {
        Object feature = msg.getFeature();
        
        // If model profiles changed or this concept's profile changed
        if(feature == IArchimatePackage.Literals.ARCHIMATE_MODEL__PROFILES
                || feature == IArchimatePackage.Literals.PROFILES__PROFILES
                || msg.getNotifier() instanceof IProfile) {
            update();
        }
    }

    @Override
    protected void update() {
        IArchimateModelObject firstSelected = getFirstSelectedObject();

        // Check also if the selected object has been deleted in case the Properties View is still showing the object if it has the focus
        if(fIsExecutingCommand || !isAlive(getFirstSelectedObject())) {
            return;
        }
        
        fComboViewer.refresh();
        
        if(firstSelected instanceof IArchimateConcept) {
            fIsRefreshing = true; // A Viewer will get a selectionChanged event when setting it
            
            EList<IProfile> profiles = ((IArchimateConcept)firstSelected).getProfiles();
            
            if(!profiles.isEmpty()) {
                fComboViewer.setSelection(new StructuredSelection(profiles.get(0)));
            }
            else {
                fComboViewer.setSelection(new StructuredSelection(NONE_PROFILE));
            }

            fIsRefreshing = false;
        }
    }

    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
    
    @Override
    protected void addAdapter() {
        super.addAdapter();
        
        // Add our adapter to the parent model to listen to its profile changes so we can update the combo
        IArchimateModelObject selected = getFirstSelectedObject();
        if(selected != null && selected.getArchimateModel() != null && !selected.getArchimateModel().eAdapters().contains(eAdapter)) {
            fModel = selected.getArchimateModel();
            fModel.eAdapters().add(eAdapter);
        }
    }
    
    @Override
    protected void removeAdapter() {
        super.removeAdapter();
        
        // Remove our adapter from the model
        if(fModel != null) {
            fModel.eAdapters().remove(eAdapter);
            fModel = null;
        }
    }
    
    @Override
    public void dispose() {
        super.dispose(); // super first
        eAdapter = null;
        fModel = null;
    }
}
