/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.diagram.IArchimateDiagramEditor;
import com.archimatetool.editor.diagram.IDiagramModelEditor;
import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IDiagramModel;



/**
 * Property Section for "Used in Views"
 * 
 * @author Phillip Beauvoir
 */
public class UsedInViewsSection extends AbstractArchimatePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.usedInViewsSection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        protected boolean isRequiredType(Object object) {
            return object instanceof IArchimateConcept;
        }

        @Override
        protected Class<?> getAdaptableType() {
            return IArchimateConcept.class;
        }
    }

    private IArchimateConcept fArchimateConcept;
    
    private TableViewer fTableViewer;
    
    @Override
    protected void createControls(Composite parent) {
        createTableControl(parent);
    }
    
    private void createTableControl(Composite parent) {
        createLabel(parent, Messages.UsedInViewsSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.NONE);
        
        // Table
        Composite tableComp = createTableComposite(parent, SWT.NONE);
        TableColumnLayout tableLayout = (TableColumnLayout)tableComp.getLayout();
        fTableViewer = new TableViewer(tableComp, SWT.BORDER | SWT.FULL_SELECTION);
        
        // Column
        TableViewerColumn column = new TableViewerColumn(fTableViewer, SWT.NONE, 0);
        tableLayout.setColumnData(column.getColumn(), new ColumnWeightData(100, false));
        
        // On Mac shows alternate table row colours
        fTableViewer.getTable().setLinesVisible(true);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(fTableViewer.getTable(), HELP_ID);

        fTableViewer.setContentProvider(new IStructuredContentProvider() {
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
            
            public void dispose() {
            }
            
            public Object[] getElements(Object inputElement) {
                return DiagramModelUtils.findReferencedDiagramsForArchimateConcept((IArchimateConcept)inputElement).toArray();
            }
        });
        
        fTableViewer.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {
                return ((IDiagramModel)element).getName();
            }
            
            @Override
            public Image getImage(Object element) {
                return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_DIAGRAM);
            }
        });
        
        fTableViewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                if(!isAlive()) {
                    return;
                }
                Object o = ((IStructuredSelection)event.getSelection()).getFirstElement();
                if(o instanceof IDiagramModel) {
                    IDiagramModel diagramModel = (IDiagramModel)o;
                    IDiagramModelEditor editor = EditorManager.openDiagramEditor(diagramModel);
                    if(editor instanceof IArchimateDiagramEditor) {
                        ((IArchimateDiagramEditor)editor).selectArchimateConcepts(new IArchimateConcept[] { fArchimateConcept });
                    }
                }
            }
        });
        
        fTableViewer.setComparator(new ViewerComparator());
    }
    
    @Override
    protected void setElement(Object element) {
        fArchimateConcept = (IArchimateConcept)new Filter().adaptObject(element);
        if(fArchimateConcept == null) {
            System.err.println("UsedInViewsSection failed to get element for " + element); //$NON-NLS-1$
        }
        
        refreshControls();
    }
    
    protected void refreshControls() {
        fTableViewer.setInput(fArchimateConcept);
    }
    
    @Override
    protected Adapter getECoreAdapter() {
        return null;
    }

    @Override
    protected EObject getEObject() {
        return fArchimateConcept;
    }
    
    @Override
    public boolean shouldUseExtraSpace() {
        return true;
    }
}
