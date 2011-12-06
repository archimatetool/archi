/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.templates.dialog;

import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import uk.ac.bolton.archimate.templates.model.ITemplate;

/**
 * Templates Table Viewer
 * 
 * @author Phillip Beauvoir
 */
public class TemplatesTableViewer extends TableViewer {

    public TemplatesTableViewer(Composite parent, int style) {
        super(parent, SWT.FULL_SELECTION | style);
        
        setColumns();
        setContentProvider(new TemplatesTableViewerContentProvider());
        setLabelProvider(new TemplatesTableViewerLabelCellProvider());
        setSorter(new ViewerSorter());
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

    class TemplatesTableViewerContentProvider implements IStructuredContentProvider {
        
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        public void dispose() {
        }
        
        public Object[] getElements(Object parent) {
            if(parent instanceof List<?>) {
                return ((List<?>)parent).toArray();
            }
            if(parent instanceof Object[]) {
                return (Object[])parent;
            }

            return new Object[0];
        }
    }

    class TemplatesTableViewerLabelCellProvider
    extends LabelProvider {
        @Override
        public String getText(Object element) {
            if(element instanceof ITemplate) {
                return ((ITemplate)element).getName();
            }
            return super.getText(element);
        }
        
        @Override
        public Image getImage(Object element) {
            if(element instanceof ITemplate) {
                return ((ITemplate)element).getImage();
            }
            return null;
        }
     }
}