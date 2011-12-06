/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.views.tree;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

import uk.ac.bolton.archimate.editor.model.DiagramModelUtils;
import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.editor.ui.ArchimateLabelProvider;
import uk.ac.bolton.archimate.editor.views.tree.search.SearchFilter;
import uk.ac.bolton.archimate.model.FolderType;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IFolder;
import uk.ac.bolton.archimate.model.IRelationship;



/**
 * Tree Viewer for Model Tree View
 * 
 * @author Phillip Beauvoir
 */
public class TreeModelViewer extends TreeViewer {
    
    private TreeCellEditor fCellEditor;
    
    /**
     * Show elements as grey if not in Viewpoint
     */
    private TreeViewpointFilterProvider fViewpointFilterProvider;
    
    public TreeModelViewer(Composite parent, int style) {
        super(parent, style | SWT.MULTI);
        
        setContentProvider(new ModelTreeViewerContentProvider());
        setLabelProvider(new ModelTreeViewerLabelProvider());
        
        setUseHashlookup(true);
        
        // Sort
        setSorter(new ViewerSorter() {
            @SuppressWarnings("unchecked")
            @Override
            public int compare(Viewer viewer, Object e1, Object e2) {
                int cat1 = category(e1);
                int cat2 = category(e2);

                if(cat1 != cat2) {
                    return cat1 - cat2;
                }
                
                // Only user folders are sorted
                if((e1 instanceof IFolder && e2 instanceof IFolder) && (((IFolder)e1).getType() != FolderType.USER 
                        || ((IFolder)e2).getType() != FolderType.USER)) {
                    return 0;
                }
                
                String name1 = ArchimateLabelProvider.INSTANCE.getLabel(e1);
                String name2 = ArchimateLabelProvider.INSTANCE.getLabel(e2);
                
                if(name1 == null) {
                    name1 = "";//$NON-NLS-1$
                }
                if(name2 == null) {
                    name2 = "";//$NON-NLS-1$
                }
                
                return getComparator().compare(name1, name2);
            }
            
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
        
        // Cell Editor
        fCellEditor = new TreeCellEditor(getTree());
        
        // Filter
        fViewpointFilterProvider = new TreeViewpointFilterProvider(this);
    }
    
    /**
     * Edit an element on the tree
     * @param element the element to be edited
     */
    public void editElement(Object element) {
        TreeItem item = findTreeItem(element);
        if(item != null) {
            fCellEditor.editItem(item);
        }
    }
    
    @Override
    public void refresh(Object element) {
        if(fCellEditor != null && fCellEditor.isEditing()) {
            fCellEditor.cancelEditing();
        }
        super.refresh(element);
    }
    
    @Override
    public void refresh(Object element, boolean updateLabels) {
        if(fCellEditor != null && fCellEditor.isEditing()) {
            fCellEditor.cancelEditing();
        }
        super.refresh(element, updateLabels);
    }
    
    /**
     * Finds the widget which represents the given element.
     * @param element the element
     * @return the TreeItem or null
     */
    public TreeItem findTreeItem(Object element) {
        Widget item = findItem(element);
        return (TreeItem)(item instanceof TreeItem ? item : null);
    }
    
    /**
     * @return The Search Filter or null if not filtering
     */
    protected SearchFilter getSearchFilter() {
        for(ViewerFilter filter : getFilters()) {
            if(filter instanceof SearchFilter) {
                return (SearchFilter)filter;
            }
        }
        
        return null;
    }
    
    /**
     *  Content Provider
     */
    private class ModelTreeViewerContentProvider implements ITreeContentProvider {
        
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        public void dispose() {
        }
        
        public Object[] getElements(Object parent) {
            return getChildren(parent);
        }

        public Object[] getChildren(Object parentElement) {
            if(parentElement instanceof IEditorModelManager) {
            	return ((IEditorModelManager)parentElement).getModels().toArray();
            }
            
            if(parentElement instanceof IArchimateModel) {
            	return ((IArchimateModel)parentElement).getFolders().toArray();
            }

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

        public Object getParent(Object element) {
            if(element instanceof EObject) {
                return ((EObject)element).eContainer();
            }
            return null;
        }

        public boolean hasChildren(Object element) {
        	return getFilteredChildren(element).length > 0;
        }
    }
    
    /**
     * Label Provider
     */
    private class ModelTreeViewerLabelProvider extends LabelProvider implements IFontProvider, IColorProvider {
        Font fontItalic = JFaceResources.getFontRegistry().getItalic("");
        Font fontBold = JFaceResources.getFontRegistry().getBold("");
        
        @Override
        public String getText(Object element) {
            String name = ArchimateLabelProvider.INSTANCE.getLabel(element);
            
            // If a dirty model show asterisk
            if(element instanceof IArchimateModel) {
                IArchimateModel model = (IArchimateModel)element;
                if(IEditorModelManager.INSTANCE.isModelDirty(model)) {
                    name = "*" + name;
                }
            }
            
            if(element instanceof IRelationship) {
                IRelationship relationship = (IRelationship)element;
                name += " (";
                name += ArchimateLabelProvider.INSTANCE.getLabel(relationship.getSource());
                name += " - ";
                name += ArchimateLabelProvider.INSTANCE.getLabel(relationship.getTarget());
                name += ")";
            }
            
            return name;
        }
        
        @Override
        public Image getImage(Object element) {
            return ArchimateLabelProvider.INSTANCE.getImage(element);
        }
        
        @Override
        public Font getFont(Object element) {
            SearchFilter filter = getSearchFilter();
            if(filter != null && filter.isFiltering() && filter.matchesFilter(element)) {
                return fontBold;
            }
            
            if(element instanceof IArchimateElement) {
                if(!DiagramModelUtils.isElementReferencedInDiagrams((IArchimateElement)element)) {
                    return fontItalic;
                }
            }
            
            return null;
        }

        @Override
        public Color getForeground(Object element) {
            return fViewpointFilterProvider.getTextColor(element);
        }

        @Override
        public Color getBackground(Object element) {
            return null;
        }
    }
}
