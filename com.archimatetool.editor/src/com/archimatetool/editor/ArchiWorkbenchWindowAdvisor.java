/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.archimatetool.editor.actions.ArchiActionBarAdvisor;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.preferences.Preferences;
import com.archimatetool.editor.utils.PlatformUtils;



/**
 * Workbench Window Advisor
 * 
 * @author Phillip Beauvoir
 */
public class ArchiWorkbenchWindowAdvisor
extends WorkbenchWindowAdvisor {
    
    /**
     * Constructor
     * @param configurer
     */
    public ArchiWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
        
        // Status Line
        boolean doShowStatusLine = Preferences.STORE.getBoolean(IPreferenceConstants.SHOW_STATUS_LINE);
        configurer.setShowStatusLine(doShowStatusLine);
    }

    @Override
    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ArchiActionBarAdvisor(configurer);
    }
    
    @Override
    public void preWindowOpen() {
        // Load user fonts here
        loadFonts();
    }
    
    @Override
    public void postWindowOpen() {
        // Application specific launcher actions
        IPlatformLauncher launcher = ArchiPlugin.INSTANCE.getPlatformLauncher();
        if(launcher != null) {
            launcher.postWindowOpen(getWindowConfigurer().getWindow());
        }
    }
    
    private void loadFonts() {
        // Load AWT fonts
        // This is needed for Windows for SVG export, but not for Mac
        // Linux will load them if we load them *before* loading then into SWT but they still don't work for SVG
        if(PlatformUtils.isWindows()) {
            loadFontsForAWT(ArchiPlugin.INSTANCE.getLocalFontsFolder());
            loadFontsForAWT(ArchiPlugin.INSTANCE.getUserFontsFolder());

            // Load Windows fonts installed for user, not "for all users" - fixes SVG export
            loadFontsForAWT(new File(System.getProperty("user.home"), "AppData/Local/Microsoft/Windows/Fonts")); //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        // Load fonts in local fonts folder
        loadFonts(ArchiPlugin.INSTANCE.getLocalFontsFolder());
        
        // Load user fonts
        loadFonts(ArchiPlugin.INSTANCE.getUserFontsFolder());
    }

    // Scan a folder looking for fonts and load them
    private void loadFonts(File fontFolder) {
        if(fontFolder.exists()) {
            for(File font : fontFolder.listFiles()) {
                if(font.isFile()) {
                    Display.getDefault().loadFont(font.getAbsolutePath());
                }
            }
        }
    }

    /**
     * Load user fonts into AWT for SVG/PDF image export
     * Only needed for Windows. Mac doesn't need it and does nothing on Linux.
     */
    private void loadFontsForAWT(File fontFolder) {
        if(fontFolder.exists()) {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            
            for(File file : fontFolder.listFiles()) {
                if(file.isFile()) {
                    try {
                        Font font = Font.createFont(Font.TRUETYPE_FONT, file);
                        ge.registerFont(font);
                    }
                    catch(Exception ex) {
                    }
                }
            }
        }
    }

}
