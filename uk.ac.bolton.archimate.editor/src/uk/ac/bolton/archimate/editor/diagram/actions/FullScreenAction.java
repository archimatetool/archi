/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.keys.IBindingService;

import uk.ac.bolton.archimate.editor.diagram.FloatingPalette;
import uk.ac.bolton.archimate.editor.diagram.IDiagramModelEditor;
import uk.ac.bolton.archimate.editor.utils.PlatformUtils;


/**
 * Full Screen Action
 * 
 * @author Phillip Beauvoir
 */
public class FullScreenAction extends WorkbenchPartAction {
    
    public static final String ID = "uk.ac.bolton.archimate.editor.action.fullScreen";
    public static final String TEXT = "Full Screen";
    
    private GraphicalViewer fGraphicalViewer;
    private Shell fNewShell;
    private Composite fOldParent;
    private PaletteViewer fOldPaletteViewer;

    private FloatingPalette fFloatingPalette;
    
    private class KeyBinding {
        public KeyBinding(int modKeys, int key, IAction action) {
            this.modKeys = modKeys;
            this.key = key;
            this.action = action;
        }
        
        int modKeys;
        int key;
        IAction action;
    }
    
    private List<KeyBinding> keyBindings = new ArrayList<KeyBinding>();

    private KeyListener keyListener = new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent e) {
            // Escape pressed, close this Shell
            if(e.keyCode == SWT.ESC) {
                e.doit = false; // Stop OS X Lion receiving this for its Full Screen
                close();
            }
            
            // Other key, find the action
            IAction action = getKeyAction(e);
            if(action != null && action.isEnabled()) {
                action.run();
            }
        }

        private IAction getKeyAction(KeyEvent e) {
            int mod = e.stateMask;
            int key = Character.toLowerCase(e.keyCode);

            for(KeyBinding kb : keyBindings) {
                if(mod == kb.modKeys && key == kb.key) {
                    return kb.action;
                }
            }
            
            return null;
        }
    };
    
    private IMenuListener contextMenuListener = new IMenuListener() {
        @Override
        public void menuAboutToShow(IMenuManager manager) {
            // Remove Actions that lead to loss of Shell focus
            manager.remove(SelectElementInTreeAction.ID);
            manager.remove(ActionFactory.PROPERTIES.getId());
            
            if(!fFloatingPalette.isOpen()) {
                manager.add(new Action("Show Palette") {
                    @Override
                    public void run() {
                        fFloatingPalette.open();
                    };
                });
            }
            
            manager.add(new Action("Close Full Screen") {
                @Override
                public void run() {
                    close();
                };
                
                @Override
                public int getAccelerator() {
                    return SWT.ESC;
                };
            });
        }
    };
    
    
    public FullScreenAction(IWorkbenchPart part) {
        super(part);
        setText(TEXT);
        setId(ID);
        setActionDefinitionId(getId()); // register key binding
    }
    
    @Override
    public void run() {
        fGraphicalViewer = (GraphicalViewer)getWorkbenchPart().getAdapter(GraphicalViewer.class);
        fOldParent = fGraphicalViewer.getControl().getParent();
        fOldPaletteViewer = fGraphicalViewer.getEditDomain().getPaletteViewer();
        
        // Set Property so clients know this is in full screen mode
        fGraphicalViewer.setProperty("full_screen", true);
        
        addKeyBindings();
        
        // Add key and menu listeners
        fGraphicalViewer.getContextMenu().addMenuListener(contextMenuListener);
        fGraphicalViewer.getControl().addKeyListener(keyListener);

        // Create new Shell
        // SWT.SHELL_TRIM is best option for all platforms. GTK needs it for full-size shell and OS X Lion has a bug with SWT.RESIZE
        int style = SWT.APPLICATION_MODAL | SWT.SHELL_TRIM ;
        if(PlatformUtils.isMac()) {
            style |= SWT.ON_TOP; // SWT.ON_TOP is needed on Mac to ensure Focus click-through
        }
        fNewShell = new Shell(Display.getCurrent(), style); 
        fNewShell.setFullScreen(true);
        fNewShell.setMaximized(true);
        fNewShell.setLayout(new FillLayout());
        
        // Set the Viewer's control's parent to be the new Shell
        fGraphicalViewer.getControl().setParent(fNewShell);
        fNewShell.layout();
        fNewShell.open();
        
        fFloatingPalette = new FloatingPalette((IDiagramModelEditor)((DefaultEditDomain)fGraphicalViewer.getEditDomain()).getEditorPart(),
                fNewShell);
        if(fFloatingPalette.getPaletteState().isOpen) {
            fFloatingPalette.open();
        }
        
        // Hide the old Shell
        fOldParent.getShell().setVisible(false);
    }
    
    private void close() {
        fFloatingPalette.close();
        
        fGraphicalViewer.getContextMenu().removeMenuListener(contextMenuListener);
        fGraphicalViewer.getControl().removeKeyListener(keyListener);
        
        fGraphicalViewer.getEditDomain().setPaletteViewer(fOldPaletteViewer);
        fGraphicalViewer.getControl().setParent(fOldParent);
        fOldParent.layout();
        
        fNewShell.dispose();
        
        fOldParent.getShell().setVisible(true);
        fGraphicalViewer.getControl().setFocus();
        
        // Reset Property
        fGraphicalViewer.setProperty("full_screen", null);
    }

    /**
     * Add common Key bindings to Actions
     */
    private void addKeyBindings() {
        if(keyBindings.isEmpty()) {
            IEditorPart editor = ((DefaultEditDomain)fGraphicalViewer.getEditDomain()).getEditorPart();
            ActionRegistry registry = (ActionRegistry)editor.getAdapter(ActionRegistry.class);
            IBindingService service = (IBindingService)editor.getEditorSite().getService(IBindingService.class);
            
            addKeyBinding(registry, service, ActionFactory.SELECT_ALL);
            addKeyBinding(registry, service, ActionFactory.UNDO);
            addKeyBinding(registry, service, ActionFactory.REDO);
            addKeyBinding(registry, service, ActionFactory.DELETE);
            addKeyBinding(registry, service, ActionFactory.CUT);
            addKeyBinding(registry, service, ActionFactory.COPY);
            addKeyBinding(registry, service, ActionFactory.PASTE);
            addKeyBinding(registry, service, ActionFactory.RENAME);
        }
    }
    
    /**
     * Add a Key binding mapped to an Action
     */
    private void addKeyBinding(ActionRegistry registry, IBindingService service, ActionFactory actionFactory) {
        KeySequence seq = (KeySequence)service.getBestActiveBindingFor(actionFactory.getCommandId());
        if(seq != null && seq.getKeyStrokes().length > 0) {
            KeyStroke ks = seq.getKeyStrokes()[0];
            keyBindings.add(new KeyBinding(ks.getModifierKeys(), Character.toLowerCase(ks.getNaturalKey()), registry.getAction(actionFactory.getId())));
        }
    }

    @Override
    protected boolean calculateEnabled() {
        return true;
    }
    
    @Override
    public void dispose() {
        fGraphicalViewer = null;
        keyBindings = null;
        fNewShell = null;
        fOldParent = null;
    }
}
