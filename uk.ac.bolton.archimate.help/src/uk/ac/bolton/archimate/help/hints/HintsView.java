/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.help.hints;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
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
import org.eclipse.swt.SWTError;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;

import uk.ac.bolton.archimate.editor.model.viewpoints.ViewpointsManager;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.editor.ui.services.ComponentSelectionManager;
import uk.ac.bolton.archimate.editor.ui.services.IComponentSelectionListener;
import uk.ac.bolton.archimate.editor.utils.PlatformUtils;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.help.ArchimateEditorHelpPlugin;
import uk.ac.bolton.archimate.model.IApplicationLayerElement;
import uk.ac.bolton.archimate.model.IArchimateDiagramModel;
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
    
    static File cssFile = new File(ArchimateEditorHelpPlugin.INSTANCE.getHintsFolder(), "style.css");

    private Browser fBrowser;
    
    private IAction fActionPinContent;
    
    private Hashtable<String, Hint> fLookupTable = new Hashtable<String, Hint>();
    
    private String fLastPath;
    
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
     * Hint Class
     */
    private static class Hint {
        String title;
        String path;
        
        Hint(String title, String path) {
            this.title = title;
            this.path = path;
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

        /*
         * It's possible that the system might not be able to create the Browser
         */
        fBrowser = createBrowser(parent);
        if(fBrowser == null) {
            return;
        }
        
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
    
    /**
     * Create the Browser if possible
     */
    private Browser createBrowser(Composite parent) {
        Browser browser = null;
        try {
            // On Eclipse 3.6 set this
            if(PlatformUtils.isGTK()) {
                System.setProperty("org.eclipse.swt.browser.UseWebKitGTK", "true");
            }
            browser = new Browser(parent, SWT.NONE);
        }
        catch(SWTError error) {
        	error.printStackTrace();
            // Create a message and show that instead
            fTitleLabel.setText("Hints View Error");
            fTitleLabel.setBackground(new Color[]{ColorFactory.COLOR_GROUP, ColorConstants.white}, new int[]{80}, false);
            Text text = new Text(parent, SWT.MULTI | SWT.WRAP);
            text.setLayoutData(new GridData(GridData.FILL_BOTH));
            text.setText("Cannot create Browser component.\nIf you are running on Linux, try installing xulrunner-1.9.2 and/or libwebkit-1.0-2.");
        }
        
        return browser;
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
        if(fBrowser != null) {
            fBrowser.setFocus();
        }
        else if(fTitleLabel != null) {
            fTitleLabel.setFocus();
        }
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
        
        // Hint Provider, if set
        if(object instanceof IHelpHintProvider) {
            String title = ((IHelpHintProvider)object).getHelpHintTitle();
            String text = ((IHelpHintProvider)object).getHelpHintContent();
            if(StringUtils.isSet(title) || StringUtils.isSet(text)) {
                fTitleLabel.setText(title);
                Color color = getTitleColor(object);
                fTitleLabel.setBackground(new Color[] { color, ColorConstants.white }, new int[] { 80 }, false);
                text = makeHTMLEntry(text);
                fBrowser.setText(text);
                fLastPath = "";
                return;
            }
        }

        // Convert Archimate Diagram Model object to Viewpoint object
        if(object instanceof IArchimateDiagramModel) {
            int index = ((IArchimateDiagramModel)object).getViewpoint();
            object = ViewpointsManager.INSTANCE.getViewpoint(index);
        }
        
        Hint hint = getHintFromObject(object);
        if(hint != null) {
            if(fLastPath != hint.path) {
                // Title and Color
                Color color = getTitleColor(object);
                fTitleLabel.setBackground(new Color[] { color, ColorConstants.white }, new int[] { 80 }, false);
                fTitleLabel.setText(hint.title);

                // Load page
                fPageLoaded = false;
                fBrowser.setUrl(hint.path);
                fLastPath = hint.path;

                // Kludge for Mac/Safari when displaying hint on mouse rollover menu item in MagicConnectionCreationTool
                if(PlatformUtils.isMac() && source instanceof MenuItem) {
                    _doMacWaitKludge();
                }
            }
        }
        else {
            fBrowser.setText("");
            fLastPath = "";
            fTitleLabel.setText("");
            fTitleLabel.setBackground(ColorConstants.white);
        }
    }
    
    /**
     * HTML-ify some text
     */
    private String makeHTMLEntry(String text) {
        if(text == null) {
            return "";
        }
        
        StringBuffer html = new StringBuffer();
        html.append("<html><head>");
        
        html.append("<link rel=\"stylesheet\" href=\"");
        html.append(cssFile.getPath());
        html.append("\" type=\"text/css\">");
        
        html.append("</head>");
        
        html.append("<body>");
        html.append(text);
        html.append("</body>");
        
        html.append("</html>");
        return html.toString();
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
    
    private Hint getHintFromObject(Object object) {
        if(object == null) {
            return null;
        }
        
        Hint hint = null;
        
        // Is it in the lookup?
        hint = fLookupTable.get(object.getClass().getName());
        if(hint != null) {
            return hint;
        }
        
        // It's a Class
        if(object instanceof Class<?>) {
            return fLookupTable.get(((Class<?>)object).getName());
        }
        
        // Look for Java interface
        Class<?> clazzes[] = object.getClass().getInterfaces();
        for(Class<?> interf : clazzes) {
            hint = fLookupTable.get(interf.getName());
            if(hint != null) {
                return hint;
            }
        }
        
        return null;
    }
    
    private void createFileMap() {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        for(IConfigurationElement configurationElement : registry.getConfigurationElementsFor(EXTENSION_POINT_ID)) {
            String className = configurationElement.getAttribute("class");
            String fileName = configurationElement.getAttribute("file");
            String title = configurationElement.getAttribute("title");
            
            String id = configurationElement.getNamespaceIdentifier();
            Bundle bundle = Platform.getBundle(id);
            URL url = bundle.getEntry(fileName);
            try {
                url = FileLocator.resolve(url);
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
            File f = new File(url.getPath());
            
            Hint hint = new Hint(title, f.getPath());
            fLookupTable.put(className, hint);
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