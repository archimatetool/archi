/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.services;

import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.views.properties.ICustomPropertiesView;



/**
 * Manager for creating, showing and selecting View parts
 * 
 * @author Phillip Beauvoir
 */
public class ViewManager {
    
    //public static String PROPERTIES_VIEW = "org.eclipse.ui.views.PropertySheet";
    public static String PROPERTIES_VIEW = ICustomPropertiesView.ID;
    public static String OUTLINE_VIEW = "org.eclipse.ui.views.ContentOutline"; //$NON-NLS-1$
    
    /**
     * Attempt to show the given View if hidden, or bring it to focus
     * @param viewID The ID of the View to show
     * @return The IViewPart or null
     */
    public static IViewPart showViewPart(String viewID, boolean activate) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart viewPart = null;
        try {
            viewPart = page.showView(viewID, null, activate ? IWorkbenchPage.VIEW_ACTIVATE : IWorkbenchPage.VIEW_VISIBLE);
        }
        catch(PartInitException ex) {
            ex.printStackTrace();
        }
        
        return viewPart;
    }
    
    /**
     * Hide a View Part
     * @param viewID The ID of the View to hide
     */
    public static void hideViewPart(String viewID) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewReference ref = page.findViewReference(viewID);
        if(ref != null) {
            page.hideView(ref);
        }
    }
    
    /**
     * Hide or show a view part in toggle manner
     * 
     * @param viewID
     */
    public static void toggleViewPart(String viewID, boolean activate) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart viewPart = page.findView(viewID);
        
        if(viewPart == null) {
            showViewPart(viewID, activate);
        }
        else {
            hideViewPart(viewID);
        }
    }
    
    /**
     * Attempt to find the given View
     * @param viewID The ID of the VIew to find
     * @return The IViewPart or null if not found
     */
    public static IViewPart findViewPart(String viewID) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        return page.findView(viewID);
    }
    
    /**
     * Attempt to show the given additional View if hidden as another instance, or bring it to focus
     * @param viewID
     * @param secondaryID
     * @return The IViewPart or null
     */
    public static IViewPart showAdditionalViewPart(String viewID, String secondaryID) {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        IViewPart viewPart = null;
        try {
            viewPart = page.showView(viewID, secondaryID, IWorkbenchPage.VIEW_ACTIVATE);
        }
        catch(PartInitException ex) {
            ex.printStackTrace();
        }
        return viewPart;
    }
    
}
