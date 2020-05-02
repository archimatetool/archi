/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.ExtensionContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.services.IServiceLocator;

import com.archimatetool.canvas.preferences.IPreferenceConstants;
import com.archimatetool.canvas.templates.wizard.NewCanvasFromTemplateWizard;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.ui.components.ExtendedWizardDialog;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.editor.ui.services.UIRequestManager;
import com.archimatetool.editor.views.tree.TreeEditElementRequest;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.ModelVersion;
import com.archimatetool.model.util.UUIDFactory;



/**
 * New Model with Canvas ExtensionContributionFactory
 * Add menu item to New menu
 * 
 * @author Phillip Beauvoir
 */
public class NewModelWithCanvasExtensionContributionFactory extends ExtensionContributionFactory {
    
    // TODO Do we really want this feature? It's the same as create new model, then add new canvas from template
    private final static boolean ENABLED = true;
    
    public NewModelWithCanvasExtensionContributionFactory() {
    }

    @Override
    public void createContributionItems(IServiceLocator serviceLocator, IContributionRoot additions) {
        if(!ENABLED) {
            return;
        }
        
        IContributionItem item = new ActionContributionItem(new NewModelWithCanvasAction());
        additions.addContributionItem(item, visibleExpression);
    }

    /**
     * Action to create new Model with Canvas
     */
    private class NewModelWithCanvasAction extends Action {
        @Override
        public String getText() {
            return Messages.NewModelWithCanvasExtensionContributionFactory_0;
        }
        
        @Override
        public void run() {
            // Open the wizard to get a new Model with Canvas from a template
            NewCanvasFromTemplateWizard wizard = new NewCanvasFromTemplateWizard();
            WizardDialog dialog = new ExtendedWizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                                  wizard,
                                  "NewCanvasFromTemplateWizard"); //$NON-NLS-1$
            
            if(dialog.open() == Window.OK) {
                try {
                    File file = wizard.getTempModelFile();
                    if(file != null) {
                        IArchimateModel model = IEditorModelManager.INSTANCE.openModel(file);
                        if(model != null) {
                            // Name
                            model.setName(Messages.NewModelWithCanvasExtensionContributionFactory_1);
                            
                            // Set latest model version (need to do this in case we immediately save as Template)
                            model.setVersion(ModelVersion.VERSION);
                            
                            // Set file to null
                            model.setFile(null);
                            
                            // New IDs
                            UUIDFactory.generateNewIDs(model);
                            
                            // Open Canvas
                            EditorManager.openDiagramEditor(model.getDefaultDiagramModel(), false);
                            
                            // Edit in-place in Tree
                            UIRequestManager.INSTANCE.fireRequestAsync(new TreeEditElementRequest(this, model));
                        }
                        
                        file.delete();
                    }
                }
                catch(IOException ex) {
                    ex.printStackTrace();
                    MessageDialog.openError(Display.getCurrent().getActiveShell(),
                            Messages.NewModelWithCanvasExtensionContributionFactory_2, ex.getMessage());
                }
            }
        }
        
        @Override
        public String getId() {
            return "newModelWithCanvasAction"; //$NON-NLS-1$
        };
        
        @Override
        public ImageDescriptor getImageDescriptor() {
            return ICanvasImages.ImageFactory.getImageDescriptor(ICanvasImages.ICON_NEWFILE);
        }
    };
    
    /**
     * Evaluate whether the menu item is visible
     */
    private Expression visibleExpression = new Expression() {
        @Override
        public EvaluationResult evaluate(IEvaluationContext context) throws CoreException {
            boolean isVisible = CanvasEditorPlugin.INSTANCE.getPreferenceStore().getBoolean(IPreferenceConstants.CANVAS_EDITOR_ENABLED);
            return isVisible ? EvaluationResult.TRUE : EvaluationResult.FALSE;
        }
    };
}
