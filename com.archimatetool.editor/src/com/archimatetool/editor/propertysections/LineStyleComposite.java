/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

import com.archimatetool.editor.diagram.commands.DiagramModelObjectLineStyleCommand;
import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.IDiagramModelObject;



/**
 * Diagram Object Line Style Composite
 * 
 * @author Phillip Beauvoir
 */
class LineStyleComposite {
    
    private Button button;
    private AbstractECorePropertySection section;
    private Composite composite;
    
    LineStyleComposite(AbstractECorePropertySection section, Composite parent) {
        this.section = section;
        composite = section.createComposite(parent, 2, false);
        createControls(composite);
    }
    
    Composite getComposite() {
        return composite;
    }

    private void createControls(Composite parent) {
        section.createLabel(parent, Messages.LineStyleComposite_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        
        button = section.getWidgetFactory().createButton(parent, null, SWT.PUSH);
        button.setLayoutData(GridDataFactory.swtDefaults().hint(50, SWT.DEFAULT).create()); // a bit more width
        
        button.addSelectionListener(widgetSelectedAdapter(event -> {
            MenuManager menuManager = new MenuManager();
            
            menuManager.add(createAction(Messages.LineStyleComposite_1, IDiagramModelObject.LINE_STYLE_SOLID, IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.LINE_SOLID)));
            menuManager.add(createAction(Messages.LineStyleComposite_2, IDiagramModelObject.LINE_STYLE_DASHED, IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.LINE_DASHED)));
            menuManager.add(createAction(Messages.LineStyleComposite_3, IDiagramModelObject.LINE_STYLE_DOTTED, IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.LINE_DOTTED)));
            menuManager.add(createAction(Messages.LineStyleComposite_4, IDiagramModelObject.LINE_STYLE_NONE, IArchiImages.ImageFactory.getImageDescriptor(IArchiImages.LINE_NONE)));
            
            Menu menu = menuManager.createContextMenu(button.getShell());
            Rectangle buttonBounds = button.getBounds();
            Point p = button.getParent().toDisplay(buttonBounds.x, buttonBounds.y + buttonBounds.height);
            menu.setLocation(p);
            menu.setVisible(true);
        }));
    }
    
    private IAction createAction(String text, final int value, final ImageDescriptor imageDesc) {
        IAction action = new Action(text, IAction.AS_RADIO_BUTTON) {
            @Override
            public void run() {
                CompoundCommand result = new CompoundCommand();

                for(EObject object : section.getEObjects()) {
                    if(isValidObject(object) && object instanceof IDiagramModelObject dmo) {
                        Command cmd = new DiagramModelObjectLineStyleCommand(dmo, value);
                        if(cmd.canExecute()) {
                            result.add(cmd);
                        }
                    }
                }

                section.executeCommand(result.unwrap());
            }
            
            @Override
            public ImageDescriptor getImageDescriptor() {
                return imageDesc;
            }
        };
        
        int lineStyle = getLineStyle((IDiagramModelObject)section.getFirstSelectedObject());
        action.setChecked(lineStyle == value);
        
        return action;
    }
    
    /**
     * In case of multi-selection we should check this
     */
    private boolean isValidObject(EObject eObject) {
        return section.isAlive(eObject) && 
                section.getFilter().shouldExposeFeature(eObject, IDiagramModelObject.FEATURE_LINE_STYLE);
    }

    /**
     * Get the actual line style for the IDiagramModelObject.
     * If the value is IDiagramModelObject.LINE_STYLE_DEFAULT then the actual value will be
     * either IDiagramModelObject.LINE_STYLE_SOLID or IDiagramModelObject.LINE_STYLE_DASHED
     */
    private int getLineStyle(IDiagramModelObject dmo) {
        return (int)ObjectUIFactory.INSTANCE.getProvider(dmo).getFeatureValue(IDiagramModelObject.FEATURE_LINE_STYLE);
    }
    
    void updateControl() {
        IDiagramModelObject dmo = (IDiagramModelObject)section.getFirstSelectedObject();
        int lineStyle = getLineStyle(dmo);
        
        switch(lineStyle) {
            case IDiagramModelObject.LINE_STYLE_SOLID:
            default:
                button.setImage(IArchiImages.ImageFactory.getImage(IArchiImages.LINE_SOLID));
                break;
            case IDiagramModelObject.LINE_STYLE_DASHED:
                button.setImage(IArchiImages.ImageFactory.getImage(IArchiImages.LINE_DASHED));
                break;
            case IDiagramModelObject.LINE_STYLE_DOTTED:
                button.setImage(IArchiImages.ImageFactory.getImage(IArchiImages.LINE_DOTTED));
                break;
            case IDiagramModelObject.LINE_STYLE_NONE:
                button.setImage(IArchiImages.ImageFactory.getImage(IArchiImages.LINE_NONE));
                break;
        }
        
        button.setEnabled(!section.isLocked(dmo));
    }
    
    void dispose() {
        composite.dispose();
        composite = null;
        section = null;
        button = null;
    }
}
