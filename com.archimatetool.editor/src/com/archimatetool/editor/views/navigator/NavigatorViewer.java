/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.navigator;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModelObject;
import com.archimatetool.model.IArchimateRelationship;




/**
 * Tree Viewer for Navigator View
 * 
 * @author Phillip Beauvoir
 */
public class NavigatorViewer extends TreeViewer {
    
    private boolean fShowTargetElements = true;
    
    public NavigatorViewer(Composite parent, int style) {
        super(parent, style | SWT.MULTI);
        
        UIUtils.setFontFromPreferences(getTree(), IPreferenceConstants.NAVIGATOR_TREE_FONT, true);
        
        setContentProvider(new NavigatorViewerContentProvider());
        setLabelProvider(new NavigatorViewerLabelProvider());
        setAutoExpandLevel(3);
        
        setComparator(new ViewerComparator(Collator.getInstance()));
    }
    
    /**
     * @return The input object from the array
     */
    IArchimateModelObject getActualInput() {
        Object input = getInput();
        
        if(input instanceof Object[] && ((Object[])input).length == 1) {
            input = ((Object[])input)[0];
        }
        
        return input instanceof IArchimateModelObject ? (IArchimateModelObject)input : null;
    }
    
    void setShowTargetElements(boolean set) {
        if(fShowTargetElements != set) {
            fShowTargetElements = set;
            refresh();
            expandToLevel(3);
        }
    }
    
    /**
     * Refresh the tree and restore expanded tree nodes
     */
    void refreshTreePreservingExpandedNodes() {
        try {
            Object[] expanded = getExpandedElements();
            getControl().setRedraw(false);
            refresh();
            setExpandedElements(expanded);
        }
        finally {
            getControl().setRedraw(true);
        }
    }
    
    /**
     *  Content Provider
     */
    private class NavigatorViewerContentProvider implements ITreeContentProvider {
        
        @Override
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }
        
        @Override
        public void dispose() {
        }
        
        @Override
        public Object[] getElements(Object parent) {
            if(parent instanceof Object[]) {
                IArchimateModelObject input = getActualInput();
                if(input != null && input.eContainer() != null) { // Check if it was deleted
                    return (Object[])parent;
                }
            }
            
            return new Object[0];
        }

        @Override
        public Object[] getChildren(Object parent) {
            if(parent instanceof IArchimateRelationship) {
            	IArchimateRelationship relation = (IArchimateRelationship)parent;
                
            	List<IArchimateConcept> results = new ArrayList<>();
            	
            	if(fShowTargetElements) {
            	    results.addAll(relation.getSourceRelationships());
            	    results.add(relation.getTarget());
            	}
            	else {
            	    results.addAll(relation.getTargetRelationships());
            	    results.add(relation.getSource());
            	}
                
                return results.toArray();
            }
            else if(parent instanceof IArchimateElement) {
                IArchimateElement element = (IArchimateElement)parent;
                if(fShowTargetElements) {
                    return element.getSourceRelationships().toArray();
                }
                else {
                    return element.getTargetRelationships().toArray();
                }
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
    private static class NavigatorViewerLabelProvider extends LabelProvider {
        @Override
        public String getText(Object element) {
            return ArchiLabelProvider.INSTANCE.getLabelNormalised(element);
        }
        
        @Override
        public Image getImage(Object element) {
            return ArchiLabelProvider.INSTANCE.getImage(element);
        }
    }
    
}
