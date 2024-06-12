/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import java.text.Collator;

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
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.diagram.IDiagramModelEditor;
import com.archimatetool.editor.model.DiagramModelUtils;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.editor.ui.textrender.TextRenderer;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IDiagramModel;



/**
 * Property Section for "Used in Views"
 * 
 * @author Phillip Beauvoir
 */
public class UsedInViewsSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.usedInViewsSection"; //$NON-NLS-1$
    
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
        
        // Font
        UIUtils.setFontFromPreferences(fTableViewer.getTable(), IPreferenceConstants.ANALYSIS_TABLE_FONT, true);
        
        // Column
        TableViewerColumn column = new TableViewerColumn(fTableViewer, SWT.NONE, 0);
        tableLayout.setColumnData(column.getColumn(), new ColumnWeightData(100, false));
        
        // On Mac shows alternate table row colours
        fTableViewer.getTable().setLinesVisible(true);
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(fTableViewer.getTable(), HELP_ID);

        fTableViewer.setContentProvider(new IStructuredContentProvider() {
            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }
            
            @Override
            public void dispose() {
            }
            
            @Override
            public Object[] getElements(Object inputElement) {
                return DiagramModelUtils.findReferencedDiagramsForArchimateConcept((IArchimateConcept)inputElement).toArray();
            }
        });
        
        fTableViewer.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {
                IDiagramModel dm = (IDiagramModel)element;
                
                // Display label according to ancestor folder's label expression, if present and preference is set
                if(ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.USE_LABEL_EXPRESSIONS_IN_ANALYSIS_TABLE)) {
                    String expression = TextRenderer.getDefault().getFormatExpressionFromAncestorFolder(dm);
                    if(expression != null) {
                        String text = StringUtils.normaliseNewLineCharacters(TextRenderer.getDefault().renderWithExpression(dm, expression));
                        if(text != null) {
                            return text;
                        }
                    }
                }
                
                return dm.getName();
            }
            
            @Override
            public Image getImage(Object element) {
                return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_DIAGRAM);
            }
        });
        
        fTableViewer.addDoubleClickListener(new IDoubleClickListener() {
            @Override
            public void doubleClick(DoubleClickEvent event) {
                if(!isAlive(fArchimateConcept)) {
                    return;
                }
                IDiagramModel diagramModel = (IDiagramModel)((IStructuredSelection)event.getSelection()).getFirstElement();
                if(diagramModel != null) {
                    IDiagramModelEditor editor = EditorManager.openDiagramEditor(diagramModel, false);
                    if(editor != null) {
                        // Needs to be asyncExec to allow EditorPart to open if it is currently closed
                        getPart().getSite().getShell().getDisplay().asyncExec(()-> { 
                            editor.selectObjects(new Object[] { fArchimateConcept });
                        });
                    }
                }
            }
        });
        
        fTableViewer.setComparator(new ViewerComparator(Collator.getInstance()));
    }
    
    @Override
    protected void update() {
        fArchimateConcept = (IArchimateConcept)getFirstSelectedObject();
        fTableViewer.setInput(fArchimateConcept);
        
        // avoid bogus horizontal scrollbar cheese
        Display.getCurrent().asyncExec(() -> {
            if(!fTableViewer.getTable().isDisposed()) {
                fTableViewer.getTable().getParent().layout();
            }
        });
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
    
    @Override
    public boolean shouldUseExtraSpace() {
        return true;
    }
}
