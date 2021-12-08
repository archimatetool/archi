/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.templates.dialog;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.templates.model.ITemplate;
import com.archimatetool.templates.model.ITemplateGroup;



/**
 * Templates Tree Viewer
 * 
 * @author Phillip Beauvoir
 */
public class TemplatesTreeViewer extends TreeViewer {

    /**
     * @param parent
     * @param style
     */
    public TemplatesTreeViewer(Composite parent, int style) {
        super(parent, style);
        
        // Mac Silicon Item height
        UIUtils.fixMacSiliconItemHeight(getTree());
        
        setContentProvider(new TemplatesTreeViewerContentProvider());
        setLabelProvider(new TemplatesTreeViewerLabelProvider());
        setComparator(new ViewerComparator());
    }

    /**
     *  Content Provider
     */
    private class TemplatesTreeViewerContentProvider implements ITreeContentProvider {
        
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
            if(parentElement instanceof List<?>) {
                return ((List<?>)parentElement).toArray();
            }
            
            if(parentElement instanceof ITemplateGroup) {
                return ((ITemplateGroup)parentElement).getTemplates().toArray();
            }
            
            return new Object[0];
        }

        @Override
        public Object getParent(Object element) {
            return null;
        }

        @Override
        public boolean hasChildren(Object element) {
            if(element instanceof ITemplateGroup) {
                return getChildren(element).length > 0;
            }
            
            return false;
        }
    }

    private class TemplatesTreeViewerLabelProvider extends LabelProvider {
        
        @Override
        public String getText(Object element) {
            if(element instanceof ITemplateGroup) {
                return ((ITemplateGroup)element).getName();
            }
            if(element instanceof ITemplate) {
                return ((ITemplate)element).getName();
            }
            return ""; //$NON-NLS-1$
        }
        
        @Override
        public Image getImage(Object element) {
            if(element instanceof ITemplateGroup) {
                return IArchiImages.ImageFactory.getImage(IArchiImages.ICON_FOLDER_DEFAULT);
            }
            if(element instanceof ITemplate) {
                return ((ITemplate)element).getImage();
            }
            
            return null;
        }
    }
}
