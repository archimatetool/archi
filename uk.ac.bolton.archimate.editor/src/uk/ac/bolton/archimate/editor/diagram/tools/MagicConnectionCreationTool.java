/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.tools;

import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.tools.ConnectionCreationTool;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ArmEvent;
import org.eclipse.swt.events.ArmListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import uk.ac.bolton.archimate.editor.diagram.ArchimateDiagramModelFactory;
import uk.ac.bolton.archimate.editor.diagram.editparts.AbstractBaseEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.IArchimateEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.diagram.GroupEditPart;
import uk.ac.bolton.archimate.editor.diagram.figures.IContainerFigure;
import uk.ac.bolton.archimate.editor.model.viewpoints.IViewpoint;
import uk.ac.bolton.archimate.editor.model.viewpoints.ViewpointsManager;
import uk.ac.bolton.archimate.editor.preferences.IPreferenceConstants;
import uk.ac.bolton.archimate.editor.preferences.Preferences;
import uk.ac.bolton.archimate.editor.ui.ArchimateLabelProvider;
import uk.ac.bolton.archimate.editor.ui.ArchimateNames;
import uk.ac.bolton.archimate.editor.ui.IArchimateImages;
import uk.ac.bolton.archimate.editor.ui.services.ComponentSelectionManager;
import uk.ac.bolton.archimate.model.IArchimateDiagramModel;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateConnection;
import uk.ac.bolton.archimate.model.IDiagramModelArchimateObject;
import uk.ac.bolton.archimate.model.IDiagramModelContainer;
import uk.ac.bolton.archimate.model.util.ArchimateModelUtils;


/**
 * Magic Connection Creation Tool
 * 
 * @author Phillip Beauvoir
 */
public class MagicConnectionCreationTool extends ConnectionCreationTool {
    
    private static Cursor cursor = new Cursor(
            null,
            IArchimateImages.ImageFactory.getImageDescriptor(IArchimateImages.CURSOR_IMG_MAGIC_CONNECTOR).getImageData(),
            0,
            0);

    
    /**
     * Flags to update Factory elements when hovering on menu items
     */
    private boolean fArmOnElements, fArmOnConnections;
    
    public MagicConnectionCreationTool() {
       setDefaultCursor(cursor);
       setDisabledCursor(cursor);
    }
    
    @Override
    protected boolean handleCreateConnection() {
        // Clear the connection factory first
        getFactory().clear();
        
        // Do this first, here (we have to!)
        Command endCommand = getCommand();
        setCurrentCommand(endCommand);
        
        // Get this now!
        CreateConnectionRequest request = (CreateConnectionRequest)getTargetRequest();
        
        EditPart sourceEditPart = request.getSourceEditPart();
        EditPart targetEditPart = request.getTargetEditPart();
        
        if(sourceEditPart == null || sourceEditPart == targetEditPart) {
            eraseSourceFeedback();
            return false;
        }
        
        IDiagramModelArchimateObject sourceDiagramModelObject = (IDiagramModelArchimateObject)sourceEditPart.getModel();
        
        // If targetEditPart is null then user clicked on the canvas or in a non-Archimate Editpart
        if(targetEditPart == null) {
            return createElementAndConnection(sourceDiagramModelObject, request.getLocation());
        }
        
        // User clicked on Archimate target edit part
        if(targetEditPart.getModel() instanceof IDiagramModelArchimateObject) {
            IDiagramModelArchimateObject targetDiagramModelObject = (IDiagramModelArchimateObject)targetEditPart.getModel();
            return createConnection(request, sourceDiagramModelObject, targetDiagramModelObject);
        }
        
        eraseSourceFeedback();
        return false;
    }
    
    @Override
    protected void setTargetEditPart(EditPart editpart) {
        // Set this to null if it's not an Archimate target editpart so we can handle it as if we clicked on the canvas
        // This also disables unwanted connection target feedback
        if(!(editpart instanceof IArchimateEditPart)) {
            editpart = null;
        }
        super.setTargetEditPart(editpart);
    }
    
    /**
     * Create just a new connection between source and target elements
     */
    private boolean createConnection(CreateConnectionRequest request, IDiagramModelArchimateObject sourceDiagramModelObject,
            IDiagramModelArchimateObject targetDiagramModelObject) {
        
        fArmOnConnections = false;
        
        Menu menu = new Menu(getCurrentViewer().getControl());
        addConnectionActions(menu, sourceDiagramModelObject.getArchimateElement(), targetDiagramModelObject.getArchimateElement());
        menu.setVisible(true);
        
        // Modal menu
        Display display = menu.getDisplay();
        while(!menu.isDisposed() && menu.isVisible()) {
            if(!display.readAndDispatch()) {
                display.sleep();
            }
        }
        
        if(!menu.isDisposed()) {
            menu.dispose();
        }
        
        eraseSourceFeedback();

        // No selection
        if(getFactory().getObjectType() == null) {
            getFactory().clear();
            return false;
        }
        
        executeCurrentCommand();
        
        // Clear the factory type
        getFactory().clear();
        return true;
    }
    
    /**
     * Create an Element and a connection in one go when user clicks on the canvas or in a non-Archimate Editpart
     */
    private boolean createElementAndConnection(IDiagramModelArchimateObject sourceDiagramModelObject, Point location) {
        // Grab this now as it will disappear after menu is shown
        EditPartViewer viewer = getCurrentViewer();
        
        // Default parent
        IDiagramModelContainer parent = sourceDiagramModelObject.getDiagramModel();
        
        // What did we click on?
        GraphicalEditPart targetEditPart = (GraphicalEditPart)viewer.findObjectAt(getCurrentInput().getMouseLocation());
        
        // If we clicked on a Group EditPart use that as parent
        if(targetEditPart instanceof GroupEditPart) {
            parent = (IDiagramModelContainer)targetEditPart.getModel();
        }
        // Or did we click on something else? Then use the parent of that
        else if(targetEditPart instanceof AbstractBaseEditPart) {
            targetEditPart = (GraphicalEditPart)targetEditPart.getParent();
            parent = (IDiagramModelContainer)targetEditPart.getModel();
        }
        
        boolean elementsFirst = Preferences.isMagicConnectorPolarity();
        boolean modKeyPressed = getCurrentInput().isModKeyDown(SWT.MOD1);
        elementsFirst ^= modKeyPressed;
        
        Menu menu = new Menu(getCurrentViewer().getControl());
        if(elementsFirst) {
            fArmOnElements = true;
            fArmOnConnections = false;
            addElementActions(menu, sourceDiagramModelObject);
        }
        else {
            fArmOnConnections = true;
            fArmOnElements = false;
            addConnectionActions(menu, sourceDiagramModelObject);
        }
        menu.setVisible(true);
        
        // Modal menu
        Display display = menu.getDisplay();
        while(!menu.isDisposed() && menu.isVisible()) {
            if(!display.readAndDispatch()) {
                display.sleep();
            }
        }
        
        if(!menu.isDisposed()) {
            menu.dispose();
        }
        
        eraseSourceFeedback();
        
        // No selection
        if(getFactory().getElementType() == null || getFactory().getRelationshipType() == null) {
            getFactory().clear();
            return false;
        }
        
        // Create Compound Command first
        CompoundCommand cmd = new CreateElementCompoundCommand((FigureCanvas)viewer.getControl(), location.x, location.y);
        
        // If the EditPart's Figure is a Container, adjust the location to relative co-ords
        if(targetEditPart.getFigure() instanceof IContainerFigure) {
            ((IContainerFigure)targetEditPart.getFigure()).translateMousePointToRelative(location);
        }
        // Or compensate for scrolled parent figure
        else {
            IFigure contentPane = targetEditPart.getContentPane();
            contentPane.translateToRelative(location);
        }
        
        CreateNewDiagramObjectCommand cmd1 = new CreateNewDiagramObjectCommand(parent,
                getFactory().getElementType(), location);
        Command cmd2 = new CreateNewConnectionCommand(sourceDiagramModelObject, cmd1.getNewObject(),
                getFactory().getRelationshipType());
        cmd.add(cmd1);
        cmd.add(cmd2);
        
        executeCommand(cmd);
        
        // Clear the factory
        getFactory().clear();
        
        return true;
    }
    
    /**
     * Add Connection->Element Actions
     */
    private void addConnectionActions(Menu menu, IDiagramModelArchimateObject sourceDiagramModelObject) {
        for(EClass relationshipType : ArchimateModelUtils.getRelationsClasses()) {
            if(ArchimateModelUtils.isValidRelationshipStart(sourceDiagramModelObject.getArchimateElement(), relationshipType)) {
                MenuItem item = addConnectionAction(menu, relationshipType);
                Menu subMenu = new Menu(item);
                item.setMenu(subMenu);
                
                addConnectionActions(subMenu, sourceDiagramModelObject, ArchimateModelUtils.getBusinessClasses(), relationshipType);
                addConnectionActions(subMenu, sourceDiagramModelObject, ArchimateModelUtils.getApplicationClasses(), relationshipType);
                addConnectionActions(subMenu, sourceDiagramModelObject, ArchimateModelUtils.getTechnologyClasses(), relationshipType);
                addConnectionActions(subMenu, sourceDiagramModelObject, ArchimateModelUtils.getConnectorClasses(), relationshipType);
                
                // Remove the very last separator if there is one
                int itemCount = subMenu.getItemCount() - 1;
                if(itemCount > 0 && (subMenu.getItem(itemCount).getStyle() & SWT.SEPARATOR) != 0) {
                    subMenu.getItem(itemCount).dispose();
                }
                
                if(subMenu.getItemCount() == 0) {
                    item.dispose(); // Nothing there
                }
            }
        }
    }
    
    private void addConnectionActions(Menu menu, IDiagramModelArchimateObject sourceDiagramModelObject, EClass[] list, EClass relationshipType) {
        boolean added = false;
        IArchimateElement sourceElement = sourceDiagramModelObject.getArchimateElement();
        
        for(EClass type : list) {
            // Check if allowed by Viewpoint
            if(!isAllowedTargetTypeInViewpoint(sourceDiagramModelObject, type)) {
                continue;
            }

            if(ArchimateModelUtils.isValidRelationship(sourceElement.eClass(), type, relationshipType)) {
                added = true;
                addElementAction(menu, type);
            }
        }
        
        if(added) {
            new MenuItem(menu, SWT.SEPARATOR);
        }
    }

    /**
     * Add Element to Connection Actions
     */
    private void addElementActions(Menu menu, IDiagramModelArchimateObject sourceDiagramModelObject) {
        MenuItem item = new MenuItem(menu, SWT.CASCADE);
        item.setText("Business");
        Menu subMenu = new Menu(item);
        item.setMenu(subMenu);
        addElementActions(subMenu, sourceDiagramModelObject, ArchimateModelUtils.getBusinessClasses());

        if(subMenu.getItemCount() == 0) {
            item.dispose(); // Nothing there
        }

        item = new MenuItem(menu, SWT.CASCADE);
        item.setText("Application");
        subMenu = new Menu(item);
        item.setMenu(subMenu);
        addElementActions(subMenu, sourceDiagramModelObject, ArchimateModelUtils.getApplicationClasses());

        if(subMenu.getItemCount() == 0) {
            item.dispose(); // Nothing there
        }

        item = new MenuItem(menu, SWT.CASCADE);
        item.setText("Technology");
        subMenu = new Menu(item);
        item.setMenu(subMenu);
        addElementActions(subMenu, sourceDiagramModelObject, ArchimateModelUtils.getTechnologyClasses());

        if(subMenu.getItemCount() == 0) {
            item.dispose(); // Nothing there
        }

        item = new MenuItem(menu, SWT.CASCADE);
        item.setText("Connectors");
        subMenu = new Menu(item);
        item.setMenu(subMenu);
        addElementActions(subMenu, sourceDiagramModelObject, ArchimateModelUtils.getConnectorClasses());

        if(subMenu.getItemCount() == 0) {
            item.dispose(); // Nothing there
        }
    }
    
    private void addElementActions(Menu menu, IDiagramModelArchimateObject sourceDiagramModelObject, EClass[] list) {
        IArchimateElement sourceElement = sourceDiagramModelObject.getArchimateElement();
        
        for(EClass type : list) {
            // Check if allowed by Viewpoint
            if(!isAllowedTargetTypeInViewpoint(sourceDiagramModelObject, type)) {
                continue;
            }
            
            MenuItem item = addElementAction(menu, type);
            Menu subMenu = new Menu(item);
            item.setMenu(subMenu);
            for(EClass typeRel : ArchimateModelUtils.getRelationsClasses()) {
                if(ArchimateModelUtils.isValidRelationship(sourceElement.eClass(), type, typeRel)) {
                    addConnectionAction(subMenu, typeRel);
                }
            }
            if(subMenu.getItemCount() == 0) {
                item.dispose(); // Nothing there
            }
        }
    }

    private MenuItem addElementAction(Menu menu, final EClass type) {
        final MenuItem item = new MenuItem(menu, SWT.CASCADE);
        item.setText(ArchimateNames.getDefaultName(type));
        item.setImage(ArchimateLabelProvider.INSTANCE.getImage(type));
        
        // Add hover listener to notify Hints View and also set element if elements first
        item.addArmListener(new ArmListener() {
            @Override
            public void widgetArmed(ArmEvent e) {
                if(fArmOnElements) {
                    getFactory().setElementType(type);
                }
                ComponentSelectionManager.INSTANCE.fireSelectionEvent(item, type);
            }
        });
        
        item.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                getFactory().setElementType(type);
            }
        });
        
        return item;
    }

    private void addConnectionActions(Menu menu, IArchimateElement sourceElement, IArchimateElement targetElement) {
        for(EClass type : ArchimateModelUtils.getValidRelationships(sourceElement, targetElement)) {
            addConnectionAction(menu, type);
        }
    }
    
    private MenuItem addConnectionAction(Menu menu, final EClass relationshipType) {
        final MenuItem item = new MenuItem(menu, SWT.CASCADE);
        item.setText(ArchimateNames.getDefaultName(relationshipType));
        item.setImage(ArchimateLabelProvider.INSTANCE.getImage(relationshipType));
        
        // Add hover listener to notify Hints View
        item.addArmListener(new ArmListener() {
            @Override
            public void widgetArmed(ArmEvent e) {
                if(fArmOnConnections) {
                    getFactory().setRelationshipType(relationshipType);
                }
                ComponentSelectionManager.INSTANCE.fireSelectionEvent(item, relationshipType);
            }
        });
        
        item.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                getFactory().setRelationshipType(relationshipType);
            }
        });
        
        return item;
    }
    
    /**
     * @return True if type is an allowed target type for a given Viewpoint
     */
    private boolean isAllowedTargetTypeInViewpoint(IDiagramModelArchimateObject diagramObject, EClass type) {
        if(!Preferences.STORE.getBoolean(IPreferenceConstants.VIEWPOINTS_HIDE_MAGIC_CONNECTOR_ELEMENTS)) {
            return true;
        }
        
        IArchimateDiagramModel dm = (IArchimateDiagramModel)diagramObject.getDiagramModel();
        int index = dm.getViewpoint();
        IViewpoint viewpoint = ViewpointsManager.INSTANCE.getViewpoint(index);
        return viewpoint == null ? true : viewpoint.isAllowedType(type);
    }
    
    @Override
    protected MagicConnectionModelFactory getFactory() {
        return (MagicConnectionModelFactory)super.getFactory();
    }
    
    /**
     * We need to explicitly set this since the source feedback is not always erased
     * (when pressing the Escape key after the popup menu is displayed)
     * @see org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy#eraseSourceFeedback(Request)
     */
    @Override
    protected void eraseSourceFeedback() {
        getSourceRequest().setType(RequestConstants.REQ_CONNECTION_END);
        super.eraseSourceFeedback();
    }
    
    
    // ======================================================================================================
    // COMMANDS
    // ======================================================================================================
    
    /**
     * Create Element Command
     */
    private static class CreateElementCompoundCommand extends CompoundCommand {
        PoofAnimater animater;
        
        CreateElementCompoundCommand(FigureCanvas canvas, int x, int y) {
            super("Create Element");
            animater = new PoofAnimater(canvas, x, y);
        }
        
        @Override
        public void undo() {
            super.undo();
            if(Preferences.doAnimateMagicConnector()) {
                animater.animate(false);
            }
        }
        
        @Override
        public void redo() {
            if(Preferences.doAnimateMagicConnector()) {
                animater.animate(true);
            }
            super.redo();
        }
    }
    
    /**
     * Create New DiagramObject Command
     */
    private static class CreateNewDiagramObjectCommand extends Command {
        private IDiagramModelContainer fParent;
        private IDiagramModelArchimateObject fChild;
        private EClass fTemplate;

        CreateNewDiagramObjectCommand(IDiagramModelContainer parent, EClass type, Point location) {
            fParent = parent;
            fTemplate = type;

            // Create this now
            fChild = (IDiagramModelArchimateObject)new ArchimateDiagramModelFactory(fTemplate).getNewObject();
            fChild.setBounds(location.x, location.y, -1, -1);
        }
        
        IDiagramModelArchimateObject getNewObject() {
            return fChild;
        }
        
        @Override
        public void execute() {
            redo();
        }

        @Override
        public void undo() {
            fParent.getChildren().remove(fChild);
            fChild.removeArchimateElementFromModel();
        }

        @Override
        public void redo() {
            fParent.getChildren().add(fChild);
            fChild.addArchimateElementToModel(null);
        }
        
        @Override
        public void dispose() {
            fParent = null;
            fChild = null;
            fTemplate = null;
        }
    }
    
    /**
     * Create New Connection Command
     */
    private static class CreateNewConnectionCommand extends Command {
        private IDiagramModelArchimateConnection fConnection;
        private IDiagramModelArchimateObject fSource;
        private IDiagramModelArchimateObject fTarget;
        private EClass fTemplate;
        
        CreateNewConnectionCommand(IDiagramModelArchimateObject source, IDiagramModelArchimateObject target, EClass type) {
            fSource = source;
            fTarget = target;
            fTemplate = type;
        }
        
        @Override
        public void execute() {
            fConnection = (IDiagramModelArchimateConnection)new ArchimateDiagramModelFactory(fTemplate).getNewObject();
            fConnection.connect(fSource, fTarget);
            fConnection.addRelationshipToModel(null);
        }
        
        @Override
        public void redo() {
            fConnection.reconnect();
            fConnection.addRelationshipToModel(null);
        }
        
        @Override
        public void undo() {
            fConnection.disconnect();
            fConnection.removeRelationshipFromModel();
        }

        @Override
        public void dispose() {
            fConnection = null;
            fSource = null;
            fTarget = null;
            fTemplate = null;
        }
    }
}
