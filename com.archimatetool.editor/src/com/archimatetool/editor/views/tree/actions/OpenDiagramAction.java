/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.views.tree.actions;

import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.model.IDiagramModel;



/**
 * Open Diagram(s) Action
 * 
 * @author Phillip Beauvoir
 */
public class OpenDiagramAction extends ViewerAction {
    
    public OpenDiagramAction(ISelectionProvider selectionProvider) {
        super(selectionProvider);
        setText(Messages.OpenDiagramAction_0);
        setActionDefinitionId("com.archimatetool.editor.action.openDiagram"); //$NON-NLS-1$
        setEnabled(false);
    }
    
    @Override
    public void run() {
        // Get all selected diagram models
        List<IDiagramModel> diagramModels = getSelection().stream()
                                                          .filter(IDiagramModel.class::isInstance)
                                                          .map(IDiagramModel.class::cast)
                                                          .toList();
        
        // If there are many views to open ask for confirmation
        if(diagramModels.size() >= ArchiPlugin.getInstance().getPreferenceStore().getInt(IPreferenceConstants.MAX_DIAGRAMS_TO_OPEN_AT_ONCE)) {
            if(!MessageDialog.openConfirm(null, Messages.OpenDiagramAction_1, NLS.bind(Messages.OpenDiagramAction_2, diagramModels.size()))) {
                return;
            }
            
            // On Mac the message dialog doesn't close immediately so do this
            if(PlatformUtils.isMac()) {
                Display.getCurrent().readAndDispatch();
            }
        }
        
        // Activate only the last selected view. This is much faster if many views are opened
        for(int i = 0; i < diagramModels.size(); i++) {
            EditorManager.openDiagramEditor(diagramModels.get(i), i == diagramModels.size() - 1);
        }
    }
    
    @Override
    public void update() {
        setEnabled(getSelection().stream()
                                 .anyMatch(IDiagramModel.class::isInstance));
    }

}
