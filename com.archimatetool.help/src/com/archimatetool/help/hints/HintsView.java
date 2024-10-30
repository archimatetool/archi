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
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.IContributedContentsView;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.Bundle;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.ThemeUtils;
import com.archimatetool.editor.ui.services.ComponentSelectionManager;
import com.archimatetool.editor.ui.services.IComponentSelectionListener;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.help.ArchiHelpPlugin;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IHelpHintProvider;



/**
 * Hints View
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class HintsView
extends ViewPart
implements IContextProvider, IHintsView, ISelectionListener, IComponentSelectionListener, IContributedContentsView  {
    
    
    // CSS string
    private String cssString = "";

    private Browser fBrowser;
    
    private IAction fActionPinContent;
    
    /*
     * Lookup table mapping class/interface name + key (if any) to Hint
     */
    private Hashtable<String, Hint> fLookupTable = new Hashtable<String, Hint>();
    
    private String fLastPath;
    
    private CLabel fTitleLabel;
    
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
        // Load CSS for Hint Providers
        try {
            cssString = Files.readString(java.nio.file.Path.of(ArchiHelpPlugin.INSTANCE.getHintsFolder().getPath(), "style.css"));
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
        
        if(!JFaceResources.getFontRegistry().hasValueFor("HintsTitleFont")) {
            FontData[] fontData = JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT).getFontData();
            fontData[0].setHeight(fontData[0].getHeight() + 4);
            JFaceResources.getFontRegistry().put("HintsTitleFont", fontData);
        }
        
        fTitleLabel = new CLabel(parent, SWT.NULL);
        fTitleLabel.setFont(JFaceResources.getFont("HintsTitleFont"));

        // Use CSS styling for label color
        if(!ThemeUtils.isDarkTheme()) {
            fTitleLabel.setData("style", "background-color: RGB(220, 235, 235); color: #000;");
        }
        
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
        try {
            final Browser browser = new Browser(parent, SWT.NONE);
            
            // Don't allow external hosts if set
            browser.addLocationListener(LocationListener.changingAdapter(e -> {
                if(!ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.HINTS_BROWSER_EXTERNAL_HOSTS_ENABLED)) {
                    e.doit = isLocalURL(e.location); // can link to local locations
                }
            }));
            
            browser.addProgressListener(ProgressListener.completedAdapter(e -> {
                // If this is a local URL and we're using dark theme apply dark css
                if(browser.getJavascriptEnabled() && ThemeUtils.isDarkTheme() && isLocalURL(browser.getUrl())) {
                    browser.execute("document.body.classList.add('dark-mode');");
                }
            }));
            
            // Start with skeleton html
            browser.setText(makeHTMLEntry(""));
            
            return browser;
        }
        catch(SWTError error) {
        	error.printStackTrace();
            
        	// Remove junk child controls that might be created with failed load
        	for(Control child : parent.getChildren()) {
                if(child != fTitleLabel) {
                    child.dispose();
                }
            }
        	
        	return null;
        }
    }

    @Override
    public void setFocus() {
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
        
        Object actualObject = selected;

        // Adaptable, dig in to get to get the actual object
        // Actual object could be IArchimateConcept or IDiagramModelComponent
        if(selected instanceof IAdaptable) {
            // ArchiMate concept (in EditPart)
            actualObject = ((IAdaptable)selected).getAdapter(IArchimateConcept.class);
            
            // Diagram Component (in EditPart)
            if(actualObject == null) {
                actualObject = ((IAdaptable)selected).getAdapter(IDiagramModelComponent.class);
            }
        }
        
        String title = "", content = null, path = null;
        Hint hint = getHintForObject(actualObject);
        
        // We have a hint so these are the defaults
        if(hint != null) {
            title = hint.title;
            path = hint.path;
        }
        
        // This is a Help Hint Provider
        IHelpHintProvider provider = (IHelpHintProvider)(actualObject instanceof IHelpHintProvider ? actualObject : selected instanceof IHelpHintProvider ? selected : null);
        if(provider != null) {
            // Title set
            if(StringUtils.isSet(provider.getHintTitle())) {
                title = provider.getHintTitle();
            }
            
            // Content set
            if(StringUtils.isSet(provider.getHintContent())) {
                content = makeHTMLEntry(provider.getHintContent());
            }
        }

        // Set Title
        fTitleLabel.setText(title);
        
        // Enable/Disable JS
        fBrowser.setJavascriptEnabled(ArchiPlugin.PREFERENCES.getBoolean(IPreferenceConstants.HINTS_BROWSER_JS_ENABLED));

        // We have some content
        if(content != null) {
            fBrowser.setText(content);
            fLastPath = "";
        }
        // We have a hint path
        else if(path != null) {
            if(fLastPath != path) { // optimise
                fBrowser.setUrl("file:///" + path);
                fLastPath = path;
            }
        }
        // Blank
        else {
            fBrowser.setText("");
            fLastPath = "";
        }
    }
    
    private Hint getHintForObject(Object object) {
        if(object == null) {
            return null;
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
        
        return fLookupTable.get(className);
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
        
        html.append("<style>");
        html.append(cssString);
        html.append("</style>");
        
        html.append("</head>");
        
        html.append("<body>");
        html.append(text);
        html.append("</body>");
        
        html.append("</html>");
        return html.toString();
    }
    
    /**
     * @return true if URL is a local file or text
     */
    private boolean isLocalURL(String url) {
        return url != null && (url.startsWith("file:") || url.startsWith("data:") || url.startsWith("about:"));
    }
    
    private void createFileMap() {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        for(IConfigurationElement configurationElement : registry.getConfigurationElementsFor(EXTENSION_POINT_ID)) {
            String className = configurationElement.getAttribute("className");
            String fileName = configurationElement.getAttribute("file");
            String title = configurationElement.getAttribute("title");
            String key = configurationElement.getAttribute("key");
            
            String id = configurationElement.getNamespaceIdentifier();
            Bundle bundle = Platform.getBundle(id);
            URL url = FileLocator.find(bundle, new Path("$nl$/" + fileName), null);
            
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
    
    /**
     * Return null so that the Properties View displays "The active part does not provide properties" instead of a table
     */
    @Override
    public IWorkbenchPart getContributingPart() {
        return null;
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