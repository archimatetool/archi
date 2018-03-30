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
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.viewpoints.IViewpoint;
import com.archimatetool.model.viewpoints.ViewpointManager;



/**
 * Property Section for an Archimate Element
 * 
 * @author Phillip Beauvoir
 */
public class ViewpointSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.diagramModelSection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IArchimateDiagramModel;
        }

        @Override
        public Class<?> getAdaptableType() {
            return IArchimateDiagramModel.class;
        }
    }

    private ComboViewer fComboViewer;
    
    /**
     * Set this to true when updating control to stop recursive update
     */
    private boolean fIsRefreshing;
    
    @Override
    protected void createControls(Composite parent) {
        createLabel(parent, Messages.ViewpointSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        fComboViewer = new ComboViewer(new Combo(parent, SWT.READ_ONLY | SWT.BORDER));
        fComboViewer.getCombo().setVisibleItemCount(12);
        fComboViewer.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        getWidgetFactory().adapt(fComboViewer.getControl(), true, true);
        
        fComboViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                if(fIsRefreshing) { // A Viewer will get a selectionChanged event when setting it
                    return;
                }
                
                IViewpoint viewPoint = (IViewpoint)((IStructuredSelection)event.getSelection()).getFirstElement();
                if(viewPoint != null) {
                    CompoundCommand result = new CompoundCommand();
                    
                    for(EObject diagramModel : getEObjects()) {
                        if(isAlive(diagramModel)) {
                            Command cmd = new EObjectFeatureCommand(Messages.ViewpointSection_1,
                                    diagramModel, IArchimatePackage.Literals.ARCHIMATE_DIAGRAM_MODEL__VIEWPOINT,
                                    viewPoint.getID());
                            if(cmd.canExecute()) {
                                result.add(cmd);
                            }
                        }
                    }

                    executeCommand(result.unwrap());
                }
            }
        });
        
        fComboViewer.setContentProvider(new IStructuredContentProvider() {
            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
            
            @Override
            public void dispose() {
            }
            
            @Override
            public Object[] getElements(Object inputElement) {
                return ViewpointManager.INSTANCE.getAllViewpoints().toArray();
            }
        });
        
        fComboViewer.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {
                return ((IViewpoint)element).getName();
            }
        });
        
        fComboViewer.setInput(""); //$NON-NLS-1$

        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }

    @Override
    protected void notifyChanged(Notification msg) {
        if(msg.getNotifier() == getFirstSelectedObject()) {
            Object feature = msg.getFeature();
            
            if(feature == IArchimatePackage.Literals.ARCHIMATE_DIAGRAM_MODEL__VIEWPOINT) {
                update();
            }
        }
    }

    @Override
    protected void update() {
        if(fIsExecutingCommand) {
           return; 
        }
        
        String id = ((IArchimateDiagramModel)getFirstSelectedObject()).getViewpoint();
        IViewpoint viewPoint = ViewpointManager.INSTANCE.getViewpoint(id);
        
        fIsRefreshing = true; // A Viewer will get a selectionChanged event when setting it
        fComboViewer.setSelection(new StructuredSelection(viewPoint));
        fIsRefreshing = false;
    }

    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
