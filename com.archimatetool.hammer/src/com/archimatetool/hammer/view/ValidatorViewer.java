/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.view;

import java.text.Collator;
import java.util.List;

import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeColumn;

import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.hammer.validation.issues.IIssue;
import com.archimatetool.hammer.validation.issues.IIssueCategory;




/**
 * Tree Viewer for Valaditor View
 * 
 * @author Phillip Beauvoir
 */
public class ValidatorViewer extends TreeViewer {
    
    public static int ASCENDING = 1;
    public static int DESCENDING = -1;
    
    private String[] columnNames = {
            Messages.ValidatorViewer_0,
            Messages.ValidatorViewer_1,
            Messages.ValidatorViewer_2
    };
    
    private int[] columnWeights = {
            20,
            60,
            20
    };
    
    private int[] columnSort = {
            DESCENDING, // Set to opposite of what user will initially press
            DESCENDING,
            DESCENDING
    };
    
    public ValidatorViewer(Composite parent, int style) {
        super(parent, style | SWT.MULTI | SWT.FULL_SELECTION);
        
        // Mac Silicon Item height
        UIUtils.fixMacSiliconItemHeight(getTree());
        
        setContentProvider(new ValidatorViewerContentProvider());
        setLabelProvider(new ValidatorViewerLabelProvider());
        
        getTree().setHeaderVisible(true);
        getTree().setLinesVisible(true);
        
        // Use layout from parent container
        TreeColumnLayout layout = (TreeColumnLayout)getControl().getParent().getLayout();
        
        for(int i = 0; i < columnNames.length; i++) {
            final TreeColumn column = new TreeColumn(getTree(), SWT.NONE);
            column.setText(columnNames[i]);
            column.setData(i);
            layout.setColumnData(column, new ColumnWeightData(columnWeights[i], true));
            
            column.addListener(SWT.Selection, new Listener() {
                @Override
                public void handleEvent(Event event) {
                    int index = (int)column.getData();
                    columnSort[index] *= -1;
                    setColumnSorting(column, columnSort[index]);
                    sort(index);
                }
            });
        }
    }
    
    private void setColumnSorting(TreeColumn column, int order) {
        getTree().setSortColumn(column);
        getTree().setSortDirection(order == ASCENDING ? SWT.UP : SWT.DOWN);
    }
    
    private void sort(final int columnIndex) {
        setComparator(new ViewerComparator(Collator.getInstance()) {
            @Override
            public int compare(Viewer viewer, Object e1, Object e2) {
                if(e1 instanceof IIssueCategory && e2 instanceof IIssueCategory) {
                    return 0;
                }
                
                ITableLabelProvider provider = (ITableLabelProvider)getLabelProvider();
                String s1 = provider.getColumnText(e1, columnIndex);
                String s2 = provider.getColumnText(e2, columnIndex);
                
                if(s1 == null) {
                    s1 = ""; //$NON-NLS-1$
                }
                if(s2 == null) {
                    s2 = ""; //$NON-NLS-1$
                }
                
                return columnSort[columnIndex] == ASCENDING ? getComparator().compare(s1, s2) : getComparator().compare(s2, s1);
            }
        });
    }
    
    // ========================= Model Provoders =====================================
    
    /**
     *  Content Provider
     */
    class ValidatorViewerContentProvider implements ITreeContentProvider {
        
        @Override
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        @Override
        public void dispose() {
        }
        
        @Override
        public Object[] getElements(Object parent) {
            return getChildren(parent);
        }

        @Override
        public Object[] getChildren(Object parentElement) {
            if(parentElement instanceof IIssueCategory) {
                return ((IIssueCategory)parentElement).getIssues().toArray();
            }
            
            if(parentElement instanceof List<?>) {
                return ((List<?>)parentElement).toArray();
            }
            
            return new Object[0];
        }

        @Override
        public Object getParent(Object element) {
            return null;
        }

        @Override
        public boolean hasChildren(Object element) {
        	return getChildren(element).length > 0;
        }
    }
    
    /**
     * Label Provider
     */
    class ValidatorViewerLabelProvider extends LabelProvider implements ITableLabelProvider {
        
        @Override
        public String getColumnText(Object element, int columnIndex) {
            if(element instanceof IIssueCategory) {
                if(columnIndex == 0) {
                    IIssueCategory category = (IIssueCategory)element;
                    int size = category.getIssues().size();
                    String text = (size == 1) ? Messages.ValidatorViewer_3 : Messages.ValidatorViewer_4;
                    return category.getName() + " (" + size + " " + text + ")";  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
                }
                return ""; //$NON-NLS-1$
            }
            
            if(element instanceof IIssue) {
                IIssue issue = (IIssue)element;
                
                switch(columnIndex) {
                    case 0:
                        return issue.getName();

                    case 1:
                        return issue.getDescription();

                    case 2:
                        return ArchiLabelProvider.INSTANCE.getLabel(issue.getObject());

                    default:
                        break;
                }
            }
            
            return ""; //$NON-NLS-1$
        }
        
        @Override
        public Image getColumnImage(Object element, int columnIndex) {
            if(columnIndex == 0) {
                if(element instanceof IIssue) {
                    return ((IIssue)element).getImage();
                }
                if(element instanceof IIssueCategory) {
                    return ((IIssueCategory)element).getImage();
                }
            }
            
            return null;
        }
    }
}
