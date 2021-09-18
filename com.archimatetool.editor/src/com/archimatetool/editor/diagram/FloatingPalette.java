/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.utils.StringUtils;



/**
 * Floating Palette for an Editor
 * 
 * @author Phillip Beauvoir
 */
public class FloatingPalette {

    private IDiagramModelEditor fEditor;
    private Shell fParentShell;
    private Shell fShell;
    private Composite fClient;
    private PalettePage fPalettePage;
    
    /**
     * State of the Palette
     */
    public static class PaletteState {
        public Rectangle bounds = new Rectangle(600, 150, 180, 750);
        public boolean isOpen = true;
        public boolean isTranslucent = true;
    }
    
    private PaletteState fPaletteState = new PaletteState();
    
    public FloatingPalette(IDiagramModelEditor editor, Shell parentShell) {
        fEditor = editor;
        fParentShell = parentShell;
        loadState(); // Need to do this now in order to get state
    }
    
    public void open() {
        loadState(); // Need to do this now in order to get bounds

        if(fShell == null || fShell.isDisposed()) {
            createShell();
        }
        
        fShell.open();
        fPaletteState.isOpen = true;
    }
    
    public void close() {
        if(fShell != null && !fShell.isDisposed()) {
            saveState(fShell); // Don't call this in DisposeListener as on Linux getBounds() returns bogus info
            fShell.dispose();
        }
    }
    
    public boolean isOpen() {
        return fShell != null && !fShell.isDisposed();
    }
    
    private void createShell() {
        fShell = new Shell(fParentShell, SWT.TOOL | SWT.RESIZE | SWT.CLOSE);
        
        if(fPaletteState.isTranslucent) {
            fShell.setAlpha(210);
        }
        
        checkSafeBounds(fParentShell);
        fShell.setBounds(fPaletteState.bounds);
        
        fShell.setImage(IArchiImages.ImageFactory.getImage(IArchiImages.ICON_APP));
        fShell.setText(Messages.FloatingPalette_0);
        
        // Disposed by system
        fShell.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                if(fClient != null) {
                    fClient.dispose();
                }
                if(fPalettePage != null) {
                    fPalettePage.dispose();
                }
                fShell = null;
            }
        });
        
        // Closed by user
        fShell.addListener(SWT.Close, new Listener() {
            @Override
            public void handleEvent(Event event) {
                fPaletteState.isOpen = false;
                saveState(fShell); // Don't call this in DisposeListener as on Linux getBounds() returns bogus info
            }
        });
        
        fShell.setLayout(new FillLayout());
        
        fClient = new Composite(fShell, SWT.NONE);
        fClient.setLayout(new FillLayout());
        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        fClient.setLayoutData(gd);
        
        fPalettePage = fEditor.getAdapter(PalettePage.class);
        fPalettePage.createControl(fClient);
    }
    
    /**
     * Ensure the bounds of the palette are reasonable
     */
    private void checkSafeBounds(Shell parent) {
        Rectangle parentBounds = parent.getBounds();
        Rectangle paletteBounds = fPaletteState.bounds;
        
        if(paletteBounds.x >= parentBounds.x + parentBounds.width) {
            paletteBounds.x = parentBounds.x + parentBounds.width - 150;
        }
        if(paletteBounds.y >= parentBounds.y + parentBounds.height) {
            paletteBounds.y = parentBounds.y + parentBounds.height - 750;
        }
        if(paletteBounds.width > 800) {
            paletteBounds.width = 150;
        }
        if(paletteBounds.height > 1500) {
            paletteBounds.height = 750;
        }
    }

    public PaletteState getPaletteState() {
        return fPaletteState;
    }

    private void saveState(Shell shell) {
        Rectangle bounds = shell.getBounds();
        String s = "" + bounds.x + "," + bounds.y + "," + bounds.width + "," + bounds.height + "," + fPaletteState.isOpen; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
        ArchiPlugin.PREFERENCES.setValue("pallete_floater_state", s); //$NON-NLS-1$
    }
    
    private void loadState() {
        String s = ArchiPlugin.PREFERENCES.getString("pallete_floater_state"); //$NON-NLS-1$
        if(StringUtils.isSet(s)) {
            try {
                String[] bits = s.split(","); //$NON-NLS-1$
                if(bits.length == 5) {
                    fPaletteState.bounds.x = Integer.valueOf(bits[0]);
                    fPaletteState.bounds.y = Integer.valueOf(bits[1]);
                    fPaletteState.bounds.width = Integer.valueOf(bits[2]);
                    fPaletteState.bounds.height = Integer.valueOf(bits[3]);
                    fPaletteState.isOpen = Boolean.parseBoolean(bits[4]);
                }
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
