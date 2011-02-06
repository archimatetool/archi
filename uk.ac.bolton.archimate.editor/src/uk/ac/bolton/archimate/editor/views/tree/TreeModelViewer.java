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
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import uk.ac.bolton.archimate.editor.model.DiagramModelUtils;
import uk.ac.bolton.archimate.editor.model.IEditorModelManager;
import uk.ac.bolton.archimate.editor.preferences.IPreferenceConstants;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import uk.ac.bolton.archimate.editor.ui.ImageFactory;
import uk.ac.bolton.archimate.editor.views.tree.search.SearchFilter;
import uk.ac.bolton.archimate.model.FolderType;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IFolder;
import uk.ac.bolton.archimate.model.INameable;
import uk.ac.bolton.archimate.model.IRelationship;



/**
 * Tree Viewer for Model Tree View
 * 
 * @author Phillip Beauvoir
 */
public class TreeModelViewer extends TreeViewer {
    
    public static String getElementText(Object element) {
        String name = element.toString();
        
        if(element instanceof INameable) {
            name = ((INameable)element).getName();
        }
        
        return name;
    }

    public TreeModelViewer(Composite parent, int style) {
        super(parent, style | SWT.MULTI);
        
        setContentProvider(new ModelTreeViewerContentProvider());
        setLabelProvider(new ModelTreeViewerLabelProvider());
        
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
                
                String name1 = getElementText(e1);
                String name2 = getElementText(e2);
                
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
        
        // Listen to Prefs change to update filter preference
        Preferences.STORE.addPropertyChangeListener(new IPropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent event) {
                if(IPreferenceConstants.FILTER_SHOW_EMPTY_FOLDERS.equals(event.getProperty()) && isFiltering()) {
                    refresh();
                }
            }
        });
    }
    
    /**
     * @return True if this viewer is being filtered
     */
    public boolean isFiltering() {
        return getFilters().length > 0 && ((SearchFilter)getFilters()[0]).isFiltering();
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
            boolean doShowEmptyFolders = Preferences.doFilterShowEmptyFolders();
            
            if(parentElement instanceof IEditorModelManager) {
                // If filter is on and prefs set, filter out empty folders
                if(isFiltering() && !doShowEmptyFolders) {
                    List<Object> list = new ArrayList<Object>();
                    for(IArchimateModel model : ((IEditorModelManager)parentElement).getModels()) {
                        if(getFilteredChildren(model).length > 0) {
                            list.add(model);
                        }
                    }
                    return list.toArray();
                }
                else {
                    return ((IEditorModelManager)parentElement).getModels().toArray();
                }
            }
            
            if(parentElement instanceof IArchimateModel) {
                // If filter is on and prefs set, filter out empty folders
                if(isFiltering() && !doShowEmptyFolders) {
                    List<Object> list = new ArrayList<Object>();
                    for(IFolder folder : ((IArchimateModel)parentElement).getFolders()) {
                        if(getFilteredChildren(folder).length > 0) {
                            list.add(folder);
                        }
                    }
                    return list.toArray();
                }
                else {
                    return ((IArchimateModel)parentElement).getFolders().toArray();
                }
            }

            if(parentElement instanceof IFolder) {
                List<Object> list = new ArrayList<Object>();
                
                // If filter is on and prefs set, filter out empty folders
                if(isFiltering() && !doShowEmptyFolders) {
                    for(IFolder folder : ((IFolder)parentElement).getFolders()) {
                        if(getFilteredChildren(folder).length > 0) {
                            list.add(folder);
                        }
                    }
                }
                // Folders
                else {
                    list.addAll(((IFolder)parentElement).getFolders());
                }
                
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
            if(element instanceof IArchimateModel) {
                return !((IArchimateModel)element).getFolders().isEmpty();
            }
            
            if(element instanceof IFolder) {
                Object[] children = getFilteredChildren(element);
                return children.length > 0;
            }
            
            return false;
        }
        
    }
    
    /**
     * Label Provider
     */
    private class ModelTreeViewerLabelProvider extends LabelProvider implements IFontProvider {
        Font fontItalic = JFaceResources.getFontRegistry().getItalic("");
        
        @Override
        public String getText(Object element) {
            String name = getElementText(element);
            
            // If a dirty model show asterisk
            if(element instanceof IArchimateModel) {
                IArchimateModel model = (IArchimateModel)element;
                CommandStack stack = (CommandStack)model.getAdapter(CommandStack.class);
                if(stack.isDirty()) {
                    name = "*" + name;
                }
            }
            
            if(element instanceof IRelationship) {
                IRelationship relationship = (IRelationship)element;
                name += " (";
                name += relationship.getSource().getName();
                name += " - ";
                name += relationship.getTarget().getName();
                name += ")";
            }
            
            return name;
        }
        
        @Override
        public Image getImage(Object element) {
            if(element instanceof EObject) {
                return ImageFactory.getImage((EObject)element);
            }
            
            return null;
        }
        
        public Font getFont(Object element) {
            if(element instanceof IArchimateElement) {
                if(!DiagramModelUtils.isElementReferencedInDiagrams((IArchimateElement)element)) {
                    return fontItalic;
                }
            }
            
            return null;
        }
    }
    
}
