/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram.directedit;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;

import com.archimatetool.editor.diagram.AbstractDiagramEditor;
import com.archimatetool.editor.ui.UIUtils;
import com.archimatetool.editor.ui.components.GlobalActionDisablementHandler;



/**
 * Direct Edit Manager that updates Global Action Handlers
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractDirectEditManager extends DirectEditManager {
    
    private GlobalActionDisablementHandler fGlobalActionHandler;
    private VerifyListener verifyListener;

    public AbstractDirectEditManager(GraphicalEditPart source, Class<?> editorType, CellEditorLocator locator) {
        super(source, editorType, locator);
    }

    @Override
    protected void initCellEditor() {
        // Hook into the global Action Handlers and null them
        fGlobalActionHandler = new GlobalActionDisablementHandler();
        fGlobalActionHandler.clearGlobalActions();
        
        // Filter out any illegal xml characters
        UIUtils.applyInvalidCharacterFilter(getTextControl());
        
        // Deactivate key binding context
        IEditorPart part = ((DefaultEditDomain)getEditPart().getViewer().getEditDomain()).getEditorPart();
        if(part instanceof AbstractDiagramEditor editor) {
            editor.deactivateContext();
        }
    }
    
    protected void setNormalised() {
        if(verifyListener == null) {
            verifyListener = new VerifyListener() {
                @Override
                public void verifyText(VerifyEvent event) {
                    event.text = event.text.replaceAll("(\r\n|\r|\n)", ""); //$NON-NLS-1$ //$NON-NLS-2$
                }
            };
            
            getTextControl().addVerifyListener(verifyListener);
        }
    }
    
    protected Text getTextControl() {
        return (Text)getCellEditor().getControl();
    }
    
    @Override
    protected void unhookListeners() {
        super.unhookListeners();
        
        if(verifyListener != null) {
            getTextControl().removeVerifyListener(verifyListener);
        }
    }
    
    @Override
    protected void bringDown() {
        // Restore the global Action Handlers
        fGlobalActionHandler.restoreGlobalActions();
        
        // Reactivate key binding context
        IEditorPart part = ((DefaultEditDomain)getEditPart().getViewer().getEditDomain()).getEditorPart();
        if(part instanceof AbstractDiagramEditor editor) {
            editor.activateContext();
        }
        
        super.bringDown();
    }
}
