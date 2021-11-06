/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.templates.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.INameable;


/**
 * Tree Viewer to Preview Model Views
 * 
 * @author Phillip Beauvoir
 */
public class ModelViewsTreeViewer extends TreeViewer {

    public ModelViewsTreeViewer(Composite parent, int style) {
        super(parent, style | SWT.BORDER);
        
        // Mac Silicon Item height
        UIUtils.fixMacSiliconItemHeight(getTree());
        
        setContentProvider(new ModelViewsTreeViewerContentProvider());
        setLabelProvider(new ModelViewsTreeViewerLabelProvider());
        
        // Sort
        setComparator(new ViewerComparator() {
             @Override
            public int category(Object element) {
                if(element instanceof IFolder) {
                    return 0;
                }
                if(element instanceof EObject) {
                    return 1;
                }
                return 0;
            }
        });

    }
 
    /**
     *  Content Provider
     */
    private class ModelViewsTreeViewerContentProvider implements ITreeContentProvider {
        
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
            if(parentElement instanceof IFolder) {
                List<Object> list = new ArrayList<Object>();
                
                // Folders
                list.addAll(((IFolder)parentElement).getFolders());
                
                // Elements
                list.addAll(((IFolder)parentElement).getElements());
                
                return list.toArray();
            }
            
            return new Object[0];
        }

        @Override
        public Object getParent(Object element) {
            if(element instanceof EObject) {
                return ((EObject)element).eContainer();
            }
            return null;
        }

        @Override
        public boolean hasChildren(Object element) {
            if(element instanceof IFolder) {
                return getChildren(element).length > 0;
            }
            
            return false;
        }
    }

    private class ModelViewsTreeViewerLabelProvider extends LabelProvider {
        
        @Override
        public String getText(Object element) {
            if(element instanceof INameable) {
                return ((INameable)element).getName();
            }
            return ""; //$NON-NLS-1$
        }
        
        @Override
        public Image getImage(Object element) {
            return ArchiLabelProvider.INSTANCE.getImage(element);
        }
    }
}