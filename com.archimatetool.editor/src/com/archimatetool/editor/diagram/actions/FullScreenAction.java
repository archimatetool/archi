/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

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
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.keys.IBindingService;

import com.archimatetool.editor.actions.ArchiActionFactory;
import com.archimatetool.editor.diagram.FloatingPalette;
import com.archimatetool.editor.diagram.IDiagramModelEditor;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.components.PartListenerAdapter;



/**
 * Full Screen Action
 * 
 * Works on Windows and Linux but not Mac >= 10.7
 * Mac's own full screen action conflicts starting from Eclipse 3.8 since Shell.setFullScreen(boolean) was changed
 * to use Mac's full screen.
 * 
 * @author Phillip Beauvoir
 */
public class FullScreenAction extends WorkbenchPartAction {
    
    public static final String ID = "com.archimatetool.editor.action.fullScreen"; //$NON-NLS-1$
    public static final String TEXT = Messages.FullScreenAction_0;
    
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
                e.doit = false; // Consume key press
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
                manager.add(new Action(Messages.FullScreenAction_1) {
                    @Override
                    public void run() {
                        fFloatingPalette.open();
                    };
                });
            }
            
            manager.add(new Action(Messages.FullScreenAction_2) {
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
    
    /*
     * If the workbench part that was closed (typically from an "Undo New View" Action) is this part then close full screen.
     * Important to use partDeactivated event rather than partClosed event so we can regain proper focus so that the Undo stack is reset.
     */
    private IPartListener partListener = new PartListenerAdapter() {
        @Override
        public void partDeactivated(IWorkbenchPart part) {
            if(part == getWorkbenchPart()) {
                close();
            }
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
        fGraphicalViewer = getWorkbenchPart().getAdapter(GraphicalViewer.class);
        fOldParent = fGraphicalViewer.getControl().getParent();
        fOldPaletteViewer = fGraphicalViewer.getEditDomain().getPaletteViewer();
        
        // Set Property so clients know this is in full screen mode
        fGraphicalViewer.setProperty("full_screen", true); //$NON-NLS-1$
        
        addKeyBindings();
        
        // Add key and menu listeners
        fGraphicalViewer.getContextMenu().addMenuListener(contextMenuListener);
        fGraphicalViewer.getControl().addKeyListener(keyListener);

        // Create new Shell
        fNewShell = new Shell(Display.getCurrent(), SWT.NONE); 
        
        // To put the full screen on the current monitor
        fNewShell.setLocation(fOldParent.getShell().getLocation());

        fNewShell.setFullScreen(true);
        fNewShell.setMaximized(true);
        fNewShell.setText(Display.getAppName());
        fNewShell.setLayout(new FillLayout());
        fNewShell.setImage(IArchiImages.ImageFactory.getImage(IArchiImages.ICON_APP_128));
        
        // On Ubuntu the min/max/close buttons are shown, so trap the close button
        fNewShell.addShellListener(new ShellAdapter() {
            @Override
            public void shellClosed(ShellEvent e) {
                close();
            }
        });
        
        // Set the Viewer's control's parent to be the new Shell
        fGraphicalViewer.getControl().setParent(fNewShell);
        fNewShell.layout();
        fNewShell.open();
        
        fFloatingPalette = new FloatingPalette((IDiagramModelEditor)((DefaultEditDomain)fGraphicalViewer.getEditDomain()).getEditorPart(),
                fNewShell);
        if(fFloatingPalette.getPaletteState().isOpen) {
            fFloatingPalette.open();
        }
        
        // Disable the old parent shell
        fOldParent.getShell().setEnabled(false);
        
        // Listen to Parts being closed
        getWorkbenchPart().getSite().getWorkbenchWindow().getPartService().addPartListener(partListener);
        
        // Set Focus on new Shell
        fNewShell.setFocus();
    }
    
    private void close() {
        fFloatingPalette.close();
        
        fGraphicalViewer.getContextMenu().removeMenuListener(contextMenuListener);
        fGraphicalViewer.getControl().removeKeyListener(keyListener);

        // Remove Listen to Parts being closed
        getWorkbenchPart().getSite().getWorkbenchWindow().getPartService().removePartListener(partListener);
        
        fGraphicalViewer.getEditDomain().setPaletteViewer(fOldPaletteViewer);
        fGraphicalViewer.getControl().setParent(fOldParent);
        fOldParent.layout();
        
        // Reset Property
        fGraphicalViewer.setProperty("full_screen", null); //$NON-NLS-1$

        // Enable the old parent shell
        fOldParent.getShell().setEnabled(true);

        // Focus
        getWorkbenchPart().getSite().getWorkbenchWindow().getShell().setFocus();

        fNewShell.dispose(); // Doing this last fixes a redraw issue on Windows e4 (toolbar gets munged)
    }

    /**
     * Add common Key bindings to Actions
     */
    private void addKeyBindings() {
        if(keyBindings.isEmpty()) {
            ActionRegistry registry = getWorkbenchPart().getAdapter(ActionRegistry.class);
            IBindingService service = getWorkbenchPart().getSite().getService(IBindingService.class);
            
            addKeyBinding(registry, service, ActionFactory.SELECT_ALL);
            addKeyBinding(registry, service, ActionFactory.UNDO);
            addKeyBinding(registry, service, ActionFactory.REDO);
            addKeyBinding(registry, service, ActionFactory.DELETE);
            addKeyBinding(registry, service, ActionFactory.CUT);
            addKeyBinding(registry, service, ActionFactory.COPY);
            addKeyBinding(registry, service, ActionFactory.PASTE);
            addKeyBinding(registry, service, ArchiActionFactory.PASTE_SPECIAL);
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
