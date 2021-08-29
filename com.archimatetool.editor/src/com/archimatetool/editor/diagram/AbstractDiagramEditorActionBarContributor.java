/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.AlignmentRetargetAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.MatchHeightRetargetAction;
import org.eclipse.gef.ui.actions.MatchSizeRetargetAction;
import org.eclipse.gef.ui.actions.MatchWidthRetargetAction;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.actions.ZoomInRetargetAction;
import org.eclipse.gef.ui.actions.ZoomOutRetargetAction;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.LabelRetargetAction;
import org.eclipse.ui.actions.RetargetAction;

import com.archimatetool.editor.actions.ArchiActionFactory;
import com.archimatetool.editor.diagram.actions.BorderColorAction;
import com.archimatetool.editor.diagram.actions.BringForwardAction;
import com.archimatetool.editor.diagram.actions.BringToFrontAction;
import com.archimatetool.editor.diagram.actions.ConnectionLineWidthAction;
import com.archimatetool.editor.diagram.actions.ConnectionRouterAction;
import com.archimatetool.editor.diagram.actions.DefaultEditPartSizeAction;
import com.archimatetool.editor.diagram.actions.ExportAsImageAction;
import com.archimatetool.editor.diagram.actions.ExportAsImageToClipboardAction;
import com.archimatetool.editor.diagram.actions.FillColorAction;
import com.archimatetool.editor.diagram.actions.FontAction;
import com.archimatetool.editor.diagram.actions.FontColorAction;
import com.archimatetool.editor.diagram.actions.FullScreenAction;
import com.archimatetool.editor.diagram.actions.LineColorAction;
import com.archimatetool.editor.diagram.actions.OutlineOpacityAction;
import com.archimatetool.editor.diagram.actions.LockObjectAction;
import com.archimatetool.editor.diagram.actions.OpacityAction;
import com.archimatetool.editor.diagram.actions.ResetAspectRatioAction;
import com.archimatetool.editor.diagram.actions.SendBackwardAction;
import com.archimatetool.editor.diagram.actions.SendToBackAction;
import com.archimatetool.editor.diagram.actions.TextAlignmentAction;
import com.archimatetool.editor.diagram.actions.TextPositionAction;
import com.archimatetool.editor.diagram.actions.ZoomNormalAction;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.components.GlobalActionDisablementHandler;
import com.archimatetool.editor.utils.PlatformUtils;



/**
 * Action Bar contributor for Diagram Editor
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractDiagramEditorActionBarContributor
extends ActionBarContributor {

    protected ZoomComboContributionItem fZoomCombo;
    
    protected String GROUP_EDIT_MENU = "group_editMenu"; //$NON-NLS-1$
    protected String GROUP_TOOLBAR_END = "group_toolbarEnd"; //$NON-NLS-1$
    protected String GROUP_POSITION = "group_position"; //$NON-NLS-1$
    private String GROUP_CONNECTIONS = "group_connections"; //$NON-NLS-1$
    
    @Override
    protected void buildActions() {
        // Zoom in
        ZoomInRetargetAction zoomInAction = new ZoomInRetargetAction();
        zoomInAction.setText(Messages.AbstractDiagramEditorActionBarContributor_9); // Externalise these
        zoomInAction.setToolTipText(Messages.AbstractDiagramEditorActionBarContributor_10);
        zoomInAction.setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ZOOM_IN));
        addRetargetAction(zoomInAction);
        
        // Zoom out
        ZoomOutRetargetAction zoomOutAction = new ZoomOutRetargetAction();
        zoomOutAction.setText(Messages.AbstractDiagramEditorActionBarContributor_11); // Externalise these
        zoomOutAction.setToolTipText(Messages.AbstractDiagramEditorActionBarContributor_12);
        zoomOutAction.setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ZOOM_OUT));
        addRetargetAction(zoomOutAction);
        
        // Zoom normal
        RetargetAction retargetAction = new RetargetAction(ZoomNormalAction.ID, ZoomNormalAction.TEXT);
        retargetAction.setActionDefinitionId(ZoomNormalAction.ID);
        retargetAction.setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ZOOM_NORMAL));
        retargetAction.setText(ZoomNormalAction.TEXT);
        retargetAction.setToolTipText(ZoomNormalAction.TEXT);
        addRetargetAction(retargetAction);
        
        // Alignment Actions
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.LEFT));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.CENTER));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.RIGHT));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.TOP));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.MIDDLE));
        addRetargetAction(new AlignmentRetargetAction(PositionConstants.BOTTOM));
        
        // Match width/height/size
        addRetargetAction(new MatchWidthRetargetAction());
        addRetargetAction(new MatchHeightRetargetAction());
        addRetargetAction(new MatchSizeRetargetAction());
        
        addRetargetAction(new RetargetAction(SnapToGrid.PROPERTY_GRID_ENABLED, 
                Messages.AbstractDiagramEditorActionBarContributor_0, IAction.AS_CHECK_BOX));

        addRetargetAction(new RetargetAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY, 
                Messages.AbstractDiagramEditorActionBarContributor_1, IAction.AS_CHECK_BOX));
        
        addRetargetAction(new RetargetAction(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY, 
                Messages.AbstractDiagramEditorActionBarContributor_2, IAction.AS_CHECK_BOX));
        
        //addRetargetAction(new RetargetAction(GEFActionConstants.TOGGLE_RULER_VISIBILITY, 
        //        "Ruler", IAction.AS_CHECK_BOX));
        
        // Default Size
        retargetAction = new RetargetAction(DefaultEditPartSizeAction.ID, DefaultEditPartSizeAction.TEXT);
        retargetAction.setActionDefinitionId(DefaultEditPartSizeAction.ID);
        retargetAction.setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_DEFAULT_SIZE));
        addRetargetAction(retargetAction);
        
        // Reset Aspect Ratio
        retargetAction = new RetargetAction(ResetAspectRatioAction.ID, ResetAspectRatioAction.TEXT);
        retargetAction.setImageDescriptor(IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.ICON_ASPECT_RATIO));
        addRetargetAction(retargetAction);
        
        // Export as Image
        addRetargetAction(new RetargetAction(ExportAsImageAction.ID, ExportAsImageAction.TEXT));
        
        // Export as Image to Clipboard
        retargetAction = new RetargetAction(ExportAsImageToClipboardAction.ID, ExportAsImageToClipboardAction.TEXT);
        retargetAction.setActionDefinitionId(ExportAsImageToClipboardAction.ID); // key binding
        addRetargetAction(retargetAction);
        
        // Fill color, line width, font, color, opacity
        addRetargetAction(new RetargetAction(FillColorAction.ID, FillColorAction.TEXT));
        addRetargetAction(new RetargetAction(ConnectionLineWidthAction.ID, ConnectionLineWidthAction.TEXT));
        addRetargetAction(new RetargetAction(LineColorAction.ID, LineColorAction.TEXT));
        addRetargetAction(new RetargetAction(FontAction.ID, FontAction.TEXT));
        addRetargetAction(new RetargetAction(FontColorAction.ID, FontColorAction.TEXT));
        addRetargetAction(new RetargetAction(OpacityAction.ID, OpacityAction.TEXT));
        addRetargetAction(new RetargetAction(OutlineOpacityAction.ID, OutlineOpacityAction.TEXT));
        
        // Text Alignments
        for(RetargetAction action : TextAlignmentAction.createRetargetActions()) {
            addRetargetAction(action);
        }
        
        // Text Positions
        for(RetargetAction action : TextPositionAction.createRetargetActions()) {
            addRetargetAction(action);
        }

        // Order Actions
        addRetargetAction(new RetargetAction(BringToFrontAction.ID, BringToFrontAction.TEXT));
        addRetargetAction(new RetargetAction(BringForwardAction.ID, BringForwardAction.TEXT));
        addRetargetAction(new RetargetAction(SendToBackAction.ID, SendToBackAction.TEXT));
        addRetargetAction(new RetargetAction(SendBackwardAction.ID, SendBackwardAction.TEXT));
        
        // Connection Routers
        addRetargetAction(new RetargetAction(ConnectionRouterAction.BendPointConnectionRouterAction.ID,
                ConnectionRouterAction.CONNECTION_ROUTER_BENDPONT, IAction.AS_RADIO_BUTTON));
// Doesn't work with Connection to Connection
//      addRetargetAction(new RetargetAction(ConnectionRouterAction.ShortestPathConnectionRouterAction.ID,
//              ConnectionRouterAction.CONNECTION_ROUTER_SHORTEST_PATH, IAction.AS_RADIO_BUTTON));
        addRetargetAction(new RetargetAction(ConnectionRouterAction.ManhattanConnectionRouterAction.ID,
                ConnectionRouterAction.CONNECTION_ROUTER_MANHATTAN, IAction.AS_RADIO_BUTTON));
        
        // Full Screen
        if(!PlatformUtils.isMac()) {
            retargetAction = new RetargetAction(FullScreenAction.ID, FullScreenAction.TEXT);
            retargetAction.setActionDefinitionId(FullScreenAction.ID);
            addRetargetAction(retargetAction);
        }
        
        // Border Color
        addRetargetAction(new RetargetAction(BorderColorAction.ID, BorderColorAction.TEXT));
        
        // Lock
        addRetargetAction(new LabelRetargetAction(LockObjectAction.ID, Messages.AbstractDiagramEditorActionBarContributor_3));
    }

    @Override
    protected void declareGlobalActionKeys() {
        addGlobalActionKey(ActionFactory.DELETE.getId());
        addGlobalActionKey(ActionFactory.CUT.getId());
        addGlobalActionKey(ActionFactory.COPY.getId());
        addGlobalActionKey(ActionFactory.PASTE.getId());
        addGlobalActionKey(ArchiActionFactory.PASTE_SPECIAL.getId());
        addGlobalActionKey(ActionFactory.UNDO.getId());
        addGlobalActionKey(ActionFactory.REDO.getId());
        addGlobalActionKey(ActionFactory.SELECT_ALL.getId());
        addGlobalActionKey(ActionFactory.PRINT.getId());
        addGlobalActionKey(ActionFactory.PROPERTIES.getId());
        addGlobalActionKey(ActionFactory.RENAME.getId());
        addGlobalActionKey(ActionFactory.FIND.getId());
    }

    @Override
    public void contributeToMenu(IMenuManager menuManager) {
        contributeToFileMenu(menuManager);
        contributeToEditMenu(menuManager);
        createViewMenu(menuManager);
    }
    
    /**
     * Create the "View" Menu
     */
    protected IMenuManager createViewMenu(IMenuManager menuManager) {
        IMenuManager viewMenu = new MenuManager(Messages.AbstractDiagramEditorActionBarContributor_4);
        
        viewMenu.add(getAction(GEFActionConstants.ZOOM_IN));
        viewMenu.add(getAction(GEFActionConstants.ZOOM_OUT));
        viewMenu.add(getAction(ZoomNormalAction.ID));
        viewMenu.add(new Separator());
        
        viewMenu.add(getAction(SnapToGrid.PROPERTY_GRID_ENABLED));
        viewMenu.add(getAction(GEFActionConstants.TOGGLE_GRID_VISIBILITY));
        viewMenu.add(getAction(GEFActionConstants.TOGGLE_SNAP_TO_GEOMETRY));
        //viewMenu.add(getAction(GEFActionConstants.TOGGLE_RULER_VISIBILITY));
        viewMenu.add(new Separator());
        
        IMenuManager orderMenu = new MenuManager(Messages.AbstractDiagramEditorActionBarContributor_5, "menu_order"); //$NON-NLS-1$
        viewMenu.add(orderMenu);
        orderMenu.add(getAction(BringToFrontAction.ID));
        orderMenu.add(getAction(BringForwardAction.ID));
        orderMenu.add(getAction(SendToBackAction.ID));
        orderMenu.add(getAction(SendBackwardAction.ID));
        
        viewMenu.add(new GroupMarker(GROUP_POSITION));
        IMenuManager alignmentMenu = new MenuManager(Messages.AbstractDiagramEditorActionBarContributor_6, "menu_position"); //$NON-NLS-1$
        viewMenu.add(alignmentMenu);
        alignmentMenu.add(getAction(GEFActionConstants.ALIGN_LEFT));
        alignmentMenu.add(getAction(GEFActionConstants.ALIGN_CENTER));
        alignmentMenu.add(getAction(GEFActionConstants.ALIGN_RIGHT));
        
        alignmentMenu.add(new Separator());
        
        alignmentMenu.add(getAction(GEFActionConstants.ALIGN_TOP));
        alignmentMenu.add(getAction(GEFActionConstants.ALIGN_MIDDLE));
        alignmentMenu.add(getAction(GEFActionConstants.ALIGN_BOTTOM));
        
        alignmentMenu.add(new Separator());
        
        alignmentMenu.add(getAction(GEFActionConstants.MATCH_WIDTH));
        alignmentMenu.add(getAction(GEFActionConstants.MATCH_HEIGHT));
        alignmentMenu.add(getAction(GEFActionConstants.MATCH_SIZE));
        
        alignmentMenu.add(new Separator());
        alignmentMenu.add(getAction(DefaultEditPartSizeAction.ID));
        alignmentMenu.add(getAction(ResetAspectRatioAction.ID));
        
        viewMenu.add(new Separator(GROUP_CONNECTIONS ));
        IMenuManager connectionMenu = new MenuManager(Messages.AbstractDiagramEditorActionBarContributor_7, "menu_connection_router"); //$NON-NLS-1$
        viewMenu.add(connectionMenu);
        connectionMenu.add(getAction(ConnectionRouterAction.BendPointConnectionRouterAction.ID));
// Doesn't work with Connection to Connection
//      connectionMenu.add(getAction(ConnectionRouterAction.ShortestPathConnectionRouterAction.ID));
        connectionMenu.add(getAction(ConnectionRouterAction.ManhattanConnectionRouterAction.ID));
        viewMenu.add(new Separator());

        if(!PlatformUtils.isMac()) {
            viewMenu.add(getAction(FullScreenAction.ID));
            viewMenu.add(new Separator());
        }
        
        menuManager.insertAfter(IWorkbenchActionConstants.M_EDIT, viewMenu);
        
        return viewMenu;
    }
    
    protected IMenuManager contributeToFileMenu(IMenuManager menuManager) {
        IMenuManager fileMenu = (IMenuManager)menuManager.find(IWorkbenchActionConstants.M_FILE);
        
        // Export menu items
        IMenuManager exportMenu = menuManager.findMenuUsingPath(IWorkbenchActionConstants.M_FILE + "/export_menu"); //$NON-NLS-1$
        exportMenu.add(getAction(ExportAsImageAction.ID));
        
        return fileMenu;
    }
    
    protected IMenuManager contributeToEditMenu(IMenuManager menuManager) {
        IMenuManager editMenu = (IMenuManager)menuManager.find(IWorkbenchActionConstants.M_EDIT);
        editMenu.insertAfter(ArchiActionFactory.RENAME.getId(), new Separator(GROUP_EDIT_MENU));
        
        // Copy as Image to Clipboard
        editMenu.insertAfter(ActionFactory.COPY.getId(), getAction(ExportAsImageToClipboardAction.ID));
        
        // Fill Color
        editMenu.appendToGroup(GROUP_EDIT_MENU, getAction(FillColorAction.ID));
        
        // Fill opacity
        editMenu.appendToGroup(GROUP_EDIT_MENU, getAction(OpacityAction.ID));

        // Outline opacity
        editMenu.appendToGroup(GROUP_EDIT_MENU, getAction(OutlineOpacityAction.ID));

        // Connection Line Width and Color
        editMenu.appendToGroup(GROUP_EDIT_MENU, getAction(ConnectionLineWidthAction.ID));
        editMenu.appendToGroup(GROUP_EDIT_MENU, getAction(LineColorAction.ID));
        
        // Font
        editMenu.appendToGroup(GROUP_EDIT_MENU, getAction(FontAction.ID));
        editMenu.appendToGroup(GROUP_EDIT_MENU, getAction(FontColorAction.ID));
        
        // Text Alignment
        IMenuManager textAlignmentMenu = new MenuManager(Messages.AbstractDiagramEditorActionBarContributor_8);
        textAlignmentMenu.add(getAction(TextAlignmentAction.ACTION_LEFT_ID));
        textAlignmentMenu.add(getAction(TextAlignmentAction.ACTION_CENTER_ID));
        textAlignmentMenu.add(getAction(TextAlignmentAction.ACTION_RIGHT_ID));
        editMenu.appendToGroup(GROUP_EDIT_MENU, textAlignmentMenu);

        return editMenu;
    }
    
    @Override
    public void contributeToToolBar(IToolBarManager toolBarManager) {
        // Add the Zoom Manager Combo
        fZoomCombo = new ZoomComboContributionItem(getPage()) {
            // Hook into the Combo so we can disable global edit action handlers when it gets the focus
            private GlobalActionDisablementHandler globalActionHandler;
            
            @Override
            protected Control createControl(Composite parent) {
                Combo combo = (Combo)super.createControl(parent);
                
                combo.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        globalActionHandler = new GlobalActionDisablementHandler(getActionBars());
                        globalActionHandler.clearGlobalActions();
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        if(globalActionHandler != null) {
                            globalActionHandler.restoreGlobalActions();
                        }
                    }
                });
                
                return combo;
            }
            
            @Override
            protected int computeWidth(Control control) {
                return Math.min(super.computeWidth(control), 100); // On Linux GTK this can get too wide
            }
        };
        
        toolBarManager.add(fZoomCombo);

        toolBarManager.add(new Separator());
        toolBarManager.add(getAction(GEFActionConstants.ALIGN_LEFT));
        toolBarManager.add(getAction(GEFActionConstants.ALIGN_CENTER));
        toolBarManager.add(getAction(GEFActionConstants.ALIGN_RIGHT));
        toolBarManager.add(new Separator());
        toolBarManager.add(getAction(GEFActionConstants.ALIGN_TOP));
        toolBarManager.add(getAction(GEFActionConstants.ALIGN_MIDDLE));
        toolBarManager.add(getAction(GEFActionConstants.ALIGN_BOTTOM));
        toolBarManager.add(new Separator());   
        toolBarManager.add(getAction(GEFActionConstants.MATCH_WIDTH));
        toolBarManager.add(getAction(GEFActionConstants.MATCH_HEIGHT));
        toolBarManager.add(getAction(GEFActionConstants.MATCH_SIZE));
        toolBarManager.add(new Separator());
        toolBarManager.add(getAction(DefaultEditPartSizeAction.ID));
        toolBarManager.add(getAction(ResetAspectRatioAction.ID));
        toolBarManager.add(new GroupMarker(GROUP_TOOLBAR_END));
    }

}
