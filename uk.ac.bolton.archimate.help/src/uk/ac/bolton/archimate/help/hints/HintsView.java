/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.help.hints;

import java.util.Hashtable;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.help.HelpSystem;
import org.eclipse.help.IContext;
import org.eclipse.help.IContextProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.editor.ui.ComponentSelectionManager;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.editor.ui.IComponentSelectionListener;
import uk.ac.bolton.archimate.editor.utils.PlatformUtils;
import uk.ac.bolton.archimate.help.ArchimateEditorHelpPlugin;
import uk.ac.bolton.archimate.model.IApplicationLayerElement;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IBusinessLayerElement;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.IDiagramModelGroup;
import uk.ac.bolton.archimate.model.IDiagramModelObject;
import uk.ac.bolton.archimate.model.ITechnologyLayerElement;


/**
 * Hints View
 * 
 * @author Phillip Beauvoir
 */
public class HintsView
extends ViewPart
implements IContextProvider, IHintsView, ISelectionListener, IComponentSelectionListener {
    
    private Browser fBrowser;
    
    private IAction fActionPinContent;
    
    private Hashtable<String, HintMapping> fLookupTable = new Hashtable<String, HintMapping>();
    
    private String fLastURL;
    
    private CLabel fTitleLabel;
    
    private boolean fPageLoaded;
    
    private class PinAction extends Action {
        PinAction() {
            super("Pin to selection", IAction.AS_CHECK_BOX);
            setToolTipText("Pins the content to the current selection");
            setImageDescriptor(IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.ICON_PIN_16));
        }
    }
    
    /*
     * Hints Mapping Class
     */
    private static class HintMapping {
        String title;
        String url;
        
        HintMapping(String title, String url) {
            this.title = title;
            this.url = url;
        }
    }

    
    @Override
    public void createPartControl(Composite parent) {
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;
        parent.setLayout(layout);
        
        if(!JFaceResources.getFontRegistry().hasValueFor("HintsTitleFont")) {
            FontData[] fontData = JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT).getFontData();
            fontData[0].setHeight(fontData[0].getHeight() + 4);
            JFaceResources.getFontRegistry().put("HintsTitleFont", fontData);
        }
        
        fTitleLabel = new CLabel(parent, SWT.NULL);
        fTitleLabel.setFont(JFaceResources.getFont("HintsTitleFont"));
        fTitleLabel.setBackground(ColorConstants.white);
        
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        fTitleLabel.setLayoutData(gd);

        fBrowser = new Browser(parent, SWT.NULL);
        gd = new GridData(GridData.FILL_BOTH);
        fBrowser.setLayoutData(gd);
        
        // Listen to Loading progress
        fBrowser.addProgressListener(new ProgressListener() {
            @Override
            public void completed(ProgressEvent event) {
                fPageLoaded = true;
            }
            
            @Override
            public void changed(ProgressEvent event) {
            }
        });
        
        // Listen to Diagram Editor Selections
        ComponentSelectionManager.INSTANCE.addSelectionListener(this);
        
        fActionPinContent = new PinAction();
        
        //IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
        //menuManager.add(fActionPinContent);

        IToolBarManager toolBarManager = getViewSite().getActionBars().getToolBarManager();
        toolBarManager.add(fActionPinContent);
        
        createFileMap();
        
        // Listen to workbench selections
        getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(this);
        
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
        
        // Initialise with whatever is selected in the workbench
        ISelection selection = getSite().getWorkbenchWindow().getSelectionService().getSelection();
        IWorkbenchPart part = getSite().getWorkbenchWindow().getPartService().getActivePart();
        selectionChanged(part, selection);
    }
    
    @Override
    public void setFocus() {
        /*
         * Need to do this otherwise we get a:
         * 
         * "java.lang.RuntimeException: WARNING: Prevented recursive attempt to activate part org.eclipse.ui.views.PropertySheet
         * while still in the middle of activating part uk.ac.bolton.archimate.help.hintsView"
         * 
         * But on Windows this leads to a SWTException if closing this View by shortcut key (Alt-4)
         */
        fBrowser.setFocus();
    }
    

    @Override
    public void componentSelectionChanged(Object component, Object selection) {
        selectionChanged(component, selection);
    }

    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if(selection instanceof IStructuredSelection && !selection.isEmpty()) {
            Object selected = ((IStructuredSelection)selection).getFirstElement();
            selectionChanged(part, selected);
        }
    }
    
    public void selectionChanged(Object source, Object selected) {
        if(fActionPinContent.isChecked()) {
            return;
        }
        
        Object object = null;

        // EClass (selected from Diagram Palette) so get Java class
        if(selected instanceof EClass) {
            EClass eClass = (EClass)selected;
            object = eClass.getInstanceClass();
        }
        // Adaptable, dig in to get to get Element...
        else if(selected instanceof IAdaptable) {
            object = ((IAdaptable)selected).getAdapter(IArchimateElement.class);
            if(object == null) {
                object = ((IAdaptable)selected).getAdapter(IDiagramModelObject.class);
            }
            if(object == null) {
                object = ((IAdaptable)selected).getAdapter(IDiagramModelConnection.class);
            }
            if(object == null) {
                object = ((IAdaptable)selected).getAdapter(IDiagramModel.class);
            }
        }
        // Default
        else {
            object = selected;
        }

        HintMapping mapping = getHintMappingFromJavaInterface(object);
        if(mapping != null) {
            if(fLastURL != mapping.url) {
                // Title and Color
                Color color = getTitleColor(object);
                fTitleLabel.setBackground(new Color[] { color, ColorConstants.white }, new int[] { 80 }, false);
                fTitleLabel.setText(mapping.title);

                // Load page
                fPageLoaded = false;
                fBrowser.setUrl(mapping.url);
                fLastURL = mapping.url;

                // Kludge for Mac/Safari when displaying hint on mouse rollover menu item in MagicConnectionCreationTool
                if(PlatformUtils.isMac() && source instanceof MenuItem) {
                    _doMacWaitKludge();
                }
            }
        }
        else {
            fBrowser.setText("");
            fLastURL = "";
            fTitleLabel.setText("");
            fTitleLabel.setBackground(ColorConstants.white);
        }
    }
    
    /**
     * If we are displaying a hint from a menu rollover in MagicConnectionCreationTool then the threading is different
     * and the Browser does not update. So we need to wait for a few sleep cycles.
     */
    private void _doMacWaitKludge() {
        // This works on Carbon and Cocoa...usually needs about 4-7 sleep cycles
        fBrowser.getDisplay().asyncExec(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while(!fPageLoaded && i++ < 20) { // Set an upper getout limit for safety
                    fBrowser.getDisplay().sleep();
                }
            }
        });
    }
    
    private HintMapping getHintMappingFromJavaInterface(Object object) {
        if(object == null) {
            return null;
        }
        
        // It's a Class
        if(object instanceof Class<?>) {
            return fLookupTable.get(((Class<?>)object).getName());
        }
        
        // It's an Object
        Class<?> clazzes[] = object.getClass().getInterfaces();
        for(Class<?> interf : clazzes) {
            HintMapping mapping = fLookupTable.get(interf.getName());
            if(mapping != null) {
                return mapping;
            }
        }
        
        return null;
    }
    
    private void createFileMap() {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtensionPoint extensionPoint = registry.getExtensionPoint(EXTENSION_POINT_ID);
        IExtension[] extensions = extensionPoint.getExtensions();
        for(IExtension extension : extensions) {
            IConfigurationElement[] elements = extension.getConfigurationElements();
            for(IConfigurationElement configurationElement : elements) {
                String className = configurationElement.getAttribute("class");
                String fileName = configurationElement.getAttribute("file");
                String title = configurationElement.getAttribute("title");
                String filePath = "file:///" + ArchimateEditorHelpPlugin.INSTANCE.getPluginFolder() + "/" + fileName;
                HintMapping mapping = new HintMapping(title, filePath);
                fLookupTable.put(className, mapping);
            }
        }
    }
    
    private Color getTitleColor(Object object) {
        Class<?> clazz;
        
        if(object instanceof IDiagramModelArchimateObject) {
            object = ((IDiagramModelArchimateObject)object).getArchimateElement();
        }
        else if(object instanceof IDiagramModelArchimateConnection) {
            object = ((IDiagramModelArchimateConnection)object).getRelationship();
        }
        
        if(object instanceof Class<?>) {
            clazz = (Class<?>)object;
        }
        else {
            clazz = object.getClass();
        }
        
        if(IDiagramModelGroup.class.isAssignableFrom(clazz)) {
            return ColorFactory.COLOR_GROUP;
        }
        
        if(IBusinessLayerElement.class.isAssignableFrom(clazz)) {
            return ColorFactory.COLOR_BUSINESS;
        }
        if(IApplicationLayerElement.class.isAssignableFrom(clazz)) {
            return ColorFactory.COLOR_APPLICATION;
        }
        if(ITechnologyLayerElement.class.isAssignableFrom(clazz)) {
            return ColorFactory.COLOR_TECHNOLOGY;
        }
        
        return ColorFactory.COLOR_DIAGRAM_MODEL_REF;
    }

    
    @Override
    public void dispose() {
        super.dispose();
        ComponentSelectionManager.INSTANCE.removeSelectionListener(this);
        getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
    }
    
    // =================================================================================
    //                       Contextual Help support
    // =================================================================================
    
    public int getContextChangeMask() {
        return NONE;
    }

    public IContext getContext(Object target) {
        return HelpSystem.getContext(HELP_ID);
    }

    public String getSearchExpression(Object target) {
        return "Hints Window";
    }
}