/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.navigator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.ui.ArchiLabelProvider;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateRelationship;




/**
 * Tree Viewer for Navigator View
 * 
 * @author Phillip Beauvoir
 */
public class NavigatorViewer extends TreeViewer {
    
    private boolean fShowTargetElements = true;
    
    /**
     * Application Preferences Listener
     */
    private IPropertyChangeListener prefsListener = new IPropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent event) {
            if(event.getProperty() == IPreferenceConstants.NAVIGATOR_TREE_FONT) {
                UIUtils.setFontFromPreferences(getTree(), IPreferenceConstants.NAVIGATOR_TREE_FONT, false);
                refresh();
            }
        }
    };

    
    public NavigatorViewer(Composite parent, int style) {
        super(parent, style | SWT.MULTI);
        
        UIUtils.setFontFromPreferences(getTree(), IPreferenceConstants.NAVIGATOR_TREE_FONT, false);
        
        setContentProvider(new NavigatorViewerContentProvider());
        setLabelProvider(new NavigatorViewerLabelProvider());
        setAutoExpandLevel(3);
        
        setComparator(new ViewerComparator());
        
        // Listen to Preferences
        Preferences.STORE.addPropertyChangeListener(prefsListener);
        
        getTree().addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                Preferences.STORE.removePropertyChangeListener(prefsListener);
            }
        });
    }
    
    public Object getActualInput() {
        return getActualInput(getInput());
    }
    
    public Object getActualInput(Object input) {
        if(input instanceof Object[] && ((Object[])input).length == 1) {
            input = ((Object[])input)[0];
        }
        return input;
    }
    
    public void setShowTargetElements(boolean set) {
        if(fShowTargetElements != set) {
            fShowTargetElements = set;
            refresh();
            expandToLevel(3);
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
                // Check if it was deleted
                Object input = getActualInput(parent);
                if(input instanceof EObject && ((EObject)input).eContainer() == null) {
                    return new Object[0];
                }
                
                return (Object[])parent;
            }
            return new Object[0];
        }

        @Override
        public Object[] getChildren(Object parent) {
            if(parent instanceof IArchimateRelationship) {
                IArchimateRelationship relation = (IArchimateRelationship)parent;
                if(fShowTargetElements) {
                    return new Object[] { relation.getTarget() };
                }
                else {
                    return new Object[] { relation.getSource() };
                }
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
    private class NavigatorViewerLabelProvider extends LabelProvider {
        
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
