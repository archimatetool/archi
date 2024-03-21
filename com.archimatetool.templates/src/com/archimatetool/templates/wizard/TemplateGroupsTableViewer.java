/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.templates.wizard;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.archimatetool.templates.model.ITemplateGroup;
import com.archimatetool.templates.model.TemplateManager;


/**
 * Templates Groups Table Viewer
 * 
 * @author Phillip Beauvoir
 */
public class TemplateGroupsTableViewer extends TableViewer {

    public TemplateGroupsTableViewer(Composite parent, int style) {
        super(parent, SWT.FULL_SELECTION | style);
        
        setColumns();
        setContentProvider(new CategoriesTableViewerContentProvider());
        setLabelProvider(new CategoriesTableViewerLabelCellProvider());
        setComparator(new ViewerComparator(Collator.getInstance()));
    }
    
    /**
     * Set up the columns
     */
    private void setColumns() {
        Table table = getTable();
        table.setHeaderVisible(false);
        
        // Use layout from parent container
        TableColumnLayout layout = (TableColumnLayout)getControl().getParent().getLayout();
        TableViewerColumn column = new TableViewerColumn(this, SWT.NONE);
        layout.setColumnData(column.getColumn(), new ColumnWeightData(100, false));
    }

    class CategoriesTableViewerContentProvider implements IStructuredContentProvider {
        
        @Override
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        @Override
        public void dispose() {
        }
        
        @Override
        public Object[] getElements(Object parent) {
            if(parent instanceof TemplateManager) {
                List<ITemplateGroup> list = new ArrayList<ITemplateGroup>();
                list.add(((TemplateManager)parent).AllUserTemplatesGroup);
                list.addAll(((TemplateManager)parent).getUserTemplateGroups());
                return list.toArray();
            }
            if(parent instanceof List<?>) {
                return ((List<?>)parent).toArray();
            }
            if(parent instanceof Object[]) {
                return (Object[])parent;
            }

            return new Object[0];
        }
    }

    class CategoriesTableViewerLabelCellProvider
    extends LabelProvider {
        @Override
        public String getText(Object element) {
            if(element instanceof ITemplateGroup) {
                return ((ITemplateGroup)element).getName();
            }
            return super.getText(element);
        }
     }
}