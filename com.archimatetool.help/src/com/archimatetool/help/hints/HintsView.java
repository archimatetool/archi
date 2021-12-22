/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.help.hints;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Hashtable;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
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
import org.eclipse.swt.browser.LocationAdapter;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.services.ComponentSelectionManager;
import com.archimatetool.editor.ui.services.IComponentSelectionListener;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.help.ArchiHelpPlugin;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IDiagramModelComponent;



/**
 * Hints View
 * 
 * @author Phillip Beauvoir
 */
public class HintsView
extends ViewPart
implements IContextProvider, IHintsView, ISelectionListener, IComponentSelectionListener {
    
    
    // CSS string
    private String cssString = ""; //$NON-NLS-1$

    private Browser fBrowser;
    
    private IAction fActionPinContent;
    
    /*
     * Lookup table mapping class/interface name + key (if any) to Hint
     */
    private Hashtable<String, Hint> fLookupTable = new Hashtable<String, Hint>();
    
    private String fLastPath;
    
    private CLabel fTitleLabel;
    
    private boolean fPageLoaded;
    
    private static class PinAction extends Action {
        PinAction() {
            super(Messages.HintsView_0, IAction.AS_CHECK_BOX);
            setToolTipText(Messages.HintsView_1);
            setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_PIN));
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

    public HintsView() {
        // Load CSS String
        try {
            File cssFile = new File(ArchiHelpPlugin.INSTANCE.getHintsFolder(), "style.css"); //$NON-NLS-1$
            cssString = new String(Files.readAllBytes(cssFile.toPath()));
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public void createPartControl(Composite parent) {
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        layout.verticalSpacing = 0;
        parent.setLayout(layout);
        
        if(!JFaceResources.getFontRegistry().hasValueFor("HintsTitleFont")) { //$NON-NLS-1$
            FontData[] fontData = JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT).getFontData();
            fontData[0].setHeight(fontData[0].getHeight() + 4);
            JFaceResources.getFontRegistry().put("HintsTitleFont", fontData); //$NON-NLS-1$
        }
        
        fTitleLabel = new CLabel(parent, SWT.NULL);
        fTitleLabel.setFont(JFaceResources.getFont("HintsTitleFont")); //$NON-NLS-1$
        // Use CSS styling for label color in case of Dark Theme
        fTitleLabel.setData("style", "background-color: RGB(220, 235, 235); color: #000;"); //$NON-NLS-1$ //$NON-NLS-2$
        // fTitleLabel.setBackground(ColorFactory.get(220, 235, 235));
        
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        fTitleLabel.setLayoutData(gd);

        /*
         * It's possible that the system might not be able to create the Browser
         */
        fBrowser = createBrowser(parent);
        if(fBrowser == null) {
            // Create a message and show that instead
            fTitleLabel.setText(Messages.HintsView_2);
            Text text = new Text(parent, SWT.MULTI | SWT.WRAP);
            text.setLayoutData(new GridData(GridData.FILL_BOTH));
            text.setText(Messages.HintsView_3);
            text.setForeground(new Color(255, 45, 45));

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
            browser = new Browser(parent, SWT.NONE);
            
            // Don't allow external hosts if set
            browser.addLocationListener(new LocationAdapter() {
                @Override
                public void changing(LocationEvent e) {
                    if(!ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.HINTS_BROWSER_EXTERNAL_HOSTS_ENABLED)) {
                        e.doit = e.location != null &&
                                (e.location.startsWith("file:") //$NON-NLS-1$
                                || e.location.startsWith("data:") //$NON-NLS-1$
                                || e.location.startsWith("about:")); //$NON-NLS-1$
                    }
                }
            });
        }
        catch(SWTError error) {
        	error.printStackTrace();
            
        	// Remove junk child controls that might be created with failed load
        	for(Control child : parent.getChildren()) {
                if(child != fTitleLabel) {
                    child.dispose();
                }
            }
        }
        
        return browser;
    }

    @Override
    public void setFocus() {
        /*
         * Need to do this otherwise we get a:
         * 
         * "java.lang.RuntimeException: WARNING: Prevented recursive attempt to activate part org.eclipse.ui.views.PropertySheet
         * while still in the middle of activating part *.hintsView"
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
        showHintForSelected(component, selection);
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if(selection instanceof IStructuredSelection && !selection.isEmpty()) {
            Object selected = ((IStructuredSelection)selection).getFirstElement();
            showHintForSelected(part, selected);
        }
    }
    
    private void showHintForSelected(Object source, Object selected) {
        if(fBrowser == null) {
            return;
        }
        
        if(fActionPinContent.isChecked()) {
            return;
        }
        
        // Check if it's a IHelpHintProvider first as this over-rides other types
        Object actualObject = (selected instanceof IHelpHintProvider) ? selected : null;

        // Adaptable, dig in to get to get the actual object...
        if(actualObject == null && selected instanceof IAdaptable) {
            // ArchiMate concept (in EditPart)
            actualObject = ((IAdaptable)selected).getAdapter(IArchimateConcept.class);
            
            // Diagram Component (in EditPart)
            if(actualObject == null) {
                actualObject = ((IAdaptable)selected).getAdapter(IDiagramModelComponent.class);
            }
        }
        // Default
        else {
            actualObject = selected;
        }
        
        // Enable JS
        fBrowser.setJavascriptEnabled(ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.HINTS_BROWSER_JS_ENABLED));
        
        // This is a Hint Provider so this takes priority...
        if(actualObject instanceof IHelpHintProvider) {
            showHintForHintProvider(source, (IHelpHintProvider)actualObject);
        }
        // Object
        else {
            showHintForObject(source, actualObject);
        }
    }
    
    private void showHintForObject(Object source, Object object) {
        if(object == null) {
            return;
        }
        
        String className;
        
        // This will be from the Palette hover/select or the Magic Connector hover
        if(object instanceof EClass) {
            className = ((EClass)object).getName();
        }
        // Object Instance
        else {
            className = object.getClass().getSimpleName();
        }
        
        // Archimate Diagram Model so append the Viewpoint name
        if(object instanceof IArchimateDiagramModel) {
            className += ((IArchimateDiagramModel)object).getViewpoint();
        }
        
        Hint hint = fLookupTable.get(className);

        if(hint != null) {
            if(fLastPath != hint.path) {
                // Title
                fTitleLabel.setText(hint.title);

                // Load page
                fPageLoaded = false;
                fBrowser.setUrl("file:///" + hint.path); //$NON-NLS-1$
                fLastPath = hint.path;

                // Kludge for Mac/Safari when displaying hint on mouse rollover menu item in MagicConnectionCreationTool
                if(PlatformUtils.isMac() && source instanceof MenuItem) {
                    _doMacWaitKludge();
                }
            }
        }
        else {
            showBlankHint();
        }
    }
    
    private void showHintForHintProvider(Object source, IHelpHintProvider provider) {
        String title = provider.getHelpHintTitle();
        String text = provider.getHelpHintContent();
        
        if(StringUtils.isSet(title) || StringUtils.isSet(text)) {
            fTitleLabel.setText(title);
            text = makeHTMLEntry(text);
            fBrowser.setText(text);
            fLastPath = ""; //$NON-NLS-1$
        }
        // No user hint, so show inbuilt hint
        else {
            showHintForObject(source, provider);
        }
    }
    
    private void showBlankHint() {
        fBrowser.setText(""); //$NON-NLS-1$
        fLastPath = ""; //$NON-NLS-1$
        fTitleLabel.setText(""); //$NON-NLS-1$
    }
    
    /**
     * HTML-ify some text
     */
    private String makeHTMLEntry(String text) {
        if(text == null) {
            return ""; //$NON-NLS-1$
        }
        
        StringBuffer html = new StringBuffer();
        html.append("<html><head>"); //$NON-NLS-1$
        
        html.append("<style>"); //$NON-NLS-1$
        html.append(cssString);
        html.append("</style>"); //$NON-NLS-1$
        
        html.append("</head>"); //$NON-NLS-1$
        
        html.append("<body>"); //$NON-NLS-1$
        html.append(text);
        html.append("</body>"); //$NON-NLS-1$
        
        html.append("</html>"); //$NON-NLS-1$
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
    
    private void createFileMap() {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        for(IConfigurationElement configurationElement : registry.getConfigurationElementsFor(EXTENSION_POINT_ID)) {
            String className = configurationElement.getAttribute("className"); //$NON-NLS-1$
            String fileName = configurationElement.getAttribute("file"); //$NON-NLS-1$
            String title = configurationElement.getAttribute("title"); //$NON-NLS-1$
            String key = configurationElement.getAttribute("key"); //$NON-NLS-1$
            
            String id = configurationElement.getNamespaceIdentifier();
            Bundle bundle = Platform.getBundle(id);
            URL url = FileLocator.find(bundle, new Path("$nl$/" + fileName), null); //$NON-NLS-1$
            
            try {
                url = FileLocator.resolve(url);
            }
            catch(IOException ex) {
                ex.printStackTrace();
                continue;
            }
            
            if(url == null) {
                continue;
            }
            
            File f = new File(url.getPath());
            
            Hint hint = new Hint(title, f.getPath());
            
            if(key != null) {
                className += key;
            }
            
            fLookupTable.put(className, hint);
        }
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
    
    @Override
    public int getContextChangeMask() {
        return NONE;
    }

    @Override
    public IContext getContext(Object target) {
        return HelpSystem.getContext(HELP_ID);
    }

    @Override
    public String getSearchExpression(Object target) {
        return Messages.HintsView_4;
    }
}