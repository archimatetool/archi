/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.Command;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.WorkbenchPartAction;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.bindings.keys.KeySequence;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchCommandConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.keys.IBindingService;

import com.archimatetool.editor.actions.ArchiActionFactory;
import com.archimatetool.editor.diagram.FloatingPalette;
import com.archimatetool.editor.diagram.IDiagramModelEditor;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.components.PartListenerAdapter;
import com.archimatetool.editor.utils.PlatformUtils;



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
    private List<KeyBinding> keyBindings;
    
    private static class KeyBinding {
        public KeyBinding(int modKeys, int key, Object actionOrCommandId) {
            this.modKeys = modKeys;
            this.key = key;
            this.actionOrCommandId = actionOrCommandId;
        }
        
        int modKeys;
        int key;
        Object actionOrCommandId;
    }
    
    private Listener keyListener = (e) -> {
        // Escape pressed, close this Shell
        if(e.keyCode == SWT.ESC) {
            e.doit = false; // Consume key press
            close();
        }

        // Find the Action and run it
        Object o = getKeyActionOrCommandId(e);
        if(o instanceof IAction action && action.isEnabled()) {
            action.run();
        }
        // Or find the Command and run it
        else if(o instanceof String commandId) {
            ICommandService commandService = getWorkbenchPart().getSite().getService(ICommandService.class);
            Command command = commandService.getCommand(commandId);
            if(command.isDefined() && command.isEnabled() ) {
                try {
                    IHandlerService handlerService = getWorkbenchPart().getSite().getService(IHandlerService.class);
                    handlerService.executeCommand(commandId, null);
                }
                catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    };
    
    private IMenuListener contextMenuListener = (manager) -> {
        manager.add(new Separator());

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
    };
    
    private IPartListener partListener = new PartListenerAdapter() {
        /*
         * If the workbench part that was closed (typically from an "Undo New View" Action) is this part then close full screen.
         * Important to use partDeactivated event rather than partClosed event so we can regain proper focus so that the Undo stack is reset.
         */
        @Override
        public void partDeactivated(IWorkbenchPart part) {
            if(part == getWorkbenchPart()) {
                close();
            }
        }
        
        /*
         * If a part is opened (such as the Properties View) and it is in the same space as the editor then the
         * full screen viewport shrinks. So close the full screen.
         */
        @Override
        public void partOpened(IWorkbenchPart part) {
            close();
        };
    };
    
    public FullScreenAction(IWorkbenchPart part) {
        super(part);
        setText(TEXT);
        setId(ID);
        setActionDefinitionId(ID); // register key binding
    }
    
    @Override
    public void run() {
        fGraphicalViewer = getWorkbenchPart().getAdapter(GraphicalViewer.class);
        fOldParent = fGraphicalViewer.getControl().getParent();
        fOldPaletteViewer = fGraphicalViewer.getEditDomain().getPaletteViewer();
        
        addKeyBindings();
        
        // Add key and menu listeners
        fGraphicalViewer.getContextMenu().addMenuListener(contextMenuListener);
        fGraphicalViewer.getControl().addListener(SWT.KeyDown, keyListener);

        // Create new Shell
        // SWT.RESIZE is needed for Linux Wayland
        int style = PlatformUtils.isWindows() || PlatformUtils.isLinuxX11() ? SWT.NONE : SWT.RESIZE;
        fNewShell = new Shell(Display.getCurrent(), style);
        
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
        // Safety check
        if(fGraphicalViewer == null) {
            return;
        }
        
        fFloatingPalette.close();
        
        fGraphicalViewer.getContextMenu().removeMenuListener(contextMenuListener);
        fGraphicalViewer.getControl().removeListener(SWT.KeyDown, keyListener);

        // Remove Listen to Parts being closed
        getWorkbenchPart().getSite().getWorkbenchWindow().getPartService().removePartListener(partListener);
        
        fGraphicalViewer.getEditDomain().setPaletteViewer(fOldPaletteViewer);
        fGraphicalViewer.getControl().setParent(fOldParent);
        fOldParent.layout();
        
        // Enable the old parent shell
        fOldParent.getShell().setEnabled(true);

        // Focus
        getWorkbenchPart().getSite().getWorkbenchWindow().getShell().setFocus();

        dispose(); // Do this last
    }

    /**
     * Add common Key bindings to Actions
     */
    private void addKeyBindings() {
        keyBindings = new ArrayList<KeyBinding>();
        
        ActionRegistry registry = getWorkbenchPart().getAdapter(ActionRegistry.class);
        IBindingService service = getWorkbenchPart().getSite().getService(IBindingService.class);

        // Actions registered in the GEF ActionRegistry
        addActionKeyBinding(registry, service, ActionFactory.SELECT_ALL);
        addActionKeyBinding(registry, service, ActionFactory.UNDO);
        addActionKeyBinding(registry, service, ActionFactory.REDO);
        addActionKeyBinding(registry, service, ActionFactory.DELETE);
        addActionKeyBinding(registry, service, ActionFactory.CUT);
        addActionKeyBinding(registry, service, ActionFactory.COPY);
        addActionKeyBinding(registry, service, ActionFactory.PASTE);
        addActionKeyBinding(registry, service, ArchiActionFactory.PASTE_SPECIAL);
        addActionKeyBinding(registry, service, ActionFactory.RENAME);

        addActionKeyBinding(registry, service, GEFActionConstants.ZOOM_IN, GEFActionConstants.ZOOM_IN);
        addActionKeyBinding(registry, service, GEFActionConstants.ZOOM_OUT, GEFActionConstants.ZOOM_OUT);
        addActionKeyBinding(registry, service, ZoomNormalAction.ID, ZoomNormalAction.ID);

        // Commands
        addCommandKeyBinding(service, IWorkbenchCommandConstants.FILE_SAVE);
    }
    
    /**
     * Add a Key binding mapped to a Command Id
     */
    private void addCommandKeyBinding(IBindingService service, String commandId) {
        KeyStroke ks = getKeyStroke(service, commandId);
        if(ks != null) {
            keyBindings.add(new KeyBinding(ks.getModifierKeys(), Character.toLowerCase(ks.getNaturalKey()), commandId));
        }
    }
    
    /**
     * Add a Key binding mapped to a GEF Action
     */
    private void addActionKeyBinding(ActionRegistry registry, IBindingService service, ActionFactory actionFactory) {
        addActionKeyBinding(registry, service, actionFactory.getId(), actionFactory.getCommandId());
    }
    
    /**
     * Add a Key binding mapped to a GEF Action by its Action Id and Command Id 
     */
    private void addActionKeyBinding(ActionRegistry registry, IBindingService service, String actionId, String commandId) {
        KeyStroke ks = getKeyStroke(service, commandId);
        if(ks != null) {
            keyBindings.add(new KeyBinding(ks.getModifierKeys(), Character.toLowerCase(ks.getNaturalKey()), registry.getAction(actionId)));
        }
    }
    
    /**
     * Get a registered key binding from the key event
     */
    private Object getKeyActionOrCommandId(Event e) {
        // Can be null after dispose() is called
        if(keyBindings == null) {
            return null;
        }

        int mod = e.stateMask;
        int key = Character.toLowerCase(e.keyCode);

        for(KeyBinding kb : keyBindings) {
            if(mod == kb.modKeys && key == kb.key) {
                return kb.actionOrCommandId;
            }
        }
        
        return null;
    }
    
    /**
     * Get a Key Stroke for a Command Id
     */
    private KeyStroke getKeyStroke(IBindingService service, String commandId) {
        KeySequence seq = (KeySequence)service.getBestActiveBindingFor(commandId);
        return seq != null && seq.getKeyStrokes().length > 0 ? seq.getKeyStrokes()[0] : null;
    }

    @Override
    protected boolean calculateEnabled() {
        return true;
    }
    
    @Override
    public void dispose() {
        if(fNewShell != null) {
            fNewShell.dispose();
        }
        
        fGraphicalViewer = null;
        keyBindings = null;
        fNewShell = null;
        fOldParent = null;
        fFloatingPalette = null;
    }
}
