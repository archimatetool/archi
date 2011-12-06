/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.templates.dialog;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.editor.ui.ImageFactory;
import uk.ac.bolton.archimate.templates.model.ITemplate;
import uk.ac.bolton.archimate.templates.model.ITemplateGroup;


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
        setContentProvider(new TemplatesTreeViewerContentProvider());
        setLabelProvider(new TemplatesTreeViewerLabelProvider());
        setSorter(new ViewerSorter());
    }

    /**
     *  Content Provider
     */
    private class TemplatesTreeViewerContentProvider implements ITreeContentProvider {
        
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        public void dispose() {
        }
        
        public Object[] getElements(Object parent) {
            return getChildren(parent);
        }

        public Object[] getChildren(Object parentElement) {
            if(parentElement instanceof List<?>) {
                return ((List<?>)parentElement).toArray();
            }
            
            if(parentElement instanceof ITemplateGroup) {
                return ((ITemplateGroup)parentElement).getTemplates().toArray();
            }
            
            return new Object[0];
        }

        public Object getParent(Object element) {
            return null;
        }

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
            return "";
        }
        
        @Override
        public Image getImage(Object element) {
            if(element instanceof ITemplateGroup) {
                return IArchimateImages.ImageFactory.getImage(ImageFactory.ECLIPSE_IMAGE_FOLDER);
            }
            if(element instanceof ITemplate) {
                return ((ITemplate)element).getImage();
            }
            
            return null;
        }
    }
}
