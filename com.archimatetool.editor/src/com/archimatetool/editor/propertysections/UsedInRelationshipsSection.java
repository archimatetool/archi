/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import java.text.Collator;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.ThemeUtils;
import com.archimatetool.editor.ui.services.ViewManager;
import com.archimatetool.editor.views.tree.ITreeModelView;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.util.ArchimateModelUtils;



/**
 * Property Section for "Model Relations"
 * 
 * @author Phillip Beauvoir
 */
public class UsedInRelationshipsSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.usedInRelationshipsSection"; //$NON-NLS-1$
    
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

    private IArchimateConcept archimateConcept;
    private TableViewer tableViewer;
    
    @Override
    protected void createControls(Composite parent) {
        createTableControl(parent);
    }
    
    private void createTableControl(Composite parent) {
        createLabel(parent, Messages.UsedInRelationshipsSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.NONE);
        
        // Table
        Composite tableComp = createTableComposite(parent, SWT.NONE);
        TableColumnLayout tableLayout = (TableColumnLayout)tableComp.getLayout();
        tableViewer = new TableViewer(tableComp, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
        
        // Set CSS ID
        ThemeUtils.registerCssId(tableViewer.getTable(), "AnalysisTable"); //$NON-NLS-1$
        
        // Set font in case CSS theming is disabled
        ThemeUtils.setFontIfCssThemingDisabled(tableViewer.getTable(), IPreferenceConstants.ANALYSIS_TABLE_FONT);

        // Column
        TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NONE, 0);
        tableLayout.setColumnData(column.getColumn(), new ColumnWeightData(100, false));
        
        // On Mac shows alternate table row colours
        tableViewer.getTable().setLinesVisible(true);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(tableViewer.getTable(), HELP_ID);

        tableViewer.setContentProvider(new IStructuredContentProvider() {
            @Override
            public Object[] getElements(Object inputElement) {
                return ArchimateModelUtils.getAllRelationshipsForConcept((IArchimateConcept)inputElement).toArray();
            }
        });
        
        tableViewer.setLabelProvider(new UsedInRelationshipsTableLabelProvider());
        
        tableViewer.addDoubleClickListener(event -> {
            if(((IStructuredSelection)event.getSelection()).getFirstElement() instanceof IArchimateRelationship relation && isAlive(relation)) {
                ITreeModelView view = (ITreeModelView)ViewManager.findViewPart(ITreeModelView.ID);
                if(view != null) {
                    view.getViewer().setSelection(new StructuredSelection(relation), true);
                }
            }
        });
        
        tableViewer.setComparator(new ViewerComparator(Collator.getInstance()));
        
        // DND
        tableViewer.addDragSupport(DND.DROP_COPY | DND.DROP_MOVE, new Transfer[] { LocalSelectionTransfer.getTransfer() },
                new DragSourceListener() {
            @Override
            public void dragStart(DragSourceEvent event) {
                // Drag started from the Table
                LocalSelectionTransfer.getTransfer().setSelection(tableViewer.getSelection());
                event.doit = true;
            }
            
            @Override
            public void dragSetData(DragSourceEvent event) {
                // For consistency set the data to the selection even though
                // the selection is provided by the LocalSelectionTransfer
                // to the drop target adapter.
                event.data = LocalSelectionTransfer.getTransfer().getSelection();
            }
            
            @Override
            public void dragFinished(DragSourceEvent event) {
                LocalSelectionTransfer.getTransfer().setSelection(null);
            }
        });
    }
    
    @Override
    protected void update() {
        archimateConcept = (IArchimateConcept)getFirstSelectedObject();
        tableViewer.setInput(archimateConcept);
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
    
    @Override
    public boolean shouldUseExtraSpace() {
        return true;
    }
    
    private static class UsedInRelationshipsTableLabelProvider extends LabelProvider implements IFontProvider {
        Font fontItalic = JFaceResources.getFontRegistry().getItalic(JFaceResources.DEFAULT_FONT);
        
        @Override
        public String getText(Object element) {
            IArchimateRelationship relationship = (IArchimateRelationship)element;
            String name = ArchiLabelProvider.INSTANCE.getLabel(relationship) + " ("; //$NON-NLS-1$
            name += ArchiLabelProvider.INSTANCE.getLabel(relationship.getSource());
            name += " - "; //$NON-NLS-1$
            name += ArchiLabelProvider.INSTANCE.getLabel(relationship.getTarget());
            name += ")"; //$NON-NLS-1$
            return ArchimateModelUtils.getParentFolderHierarchyAsString(relationship, '/') + name;
        }
        
        @Override
        public Image getImage(Object element) {
            return ArchiLabelProvider.INSTANCE.getImage(element);
        }

        @Override
        public Font getFont(Object element) {
            // Italicise unused elements
            if(ArchiPlugin.getInstance().getPreferenceStore().getBoolean(IPreferenceConstants.HIGHLIGHT_UNUSED_ELEMENTS_IN_MODEL_TREE)
                    && element instanceof IArchimateConcept concept
                    && !DiagramModelUtils.isArchimateConceptReferencedInDiagrams(concept)) {
                return fontItalic;
            }
            
            return null;
        }
    }
}
