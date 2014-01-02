/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas;

import java.io.IOException;

import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.ExtensionContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.services.IServiceLocator;

import com.archimatetool.canvas.preferences.IPreferenceConstants;
import com.archimatetool.canvas.templates.wizard.NewCanvasFromTemplateWizard;
import com.archimatetool.editor.model.IEditorModelManager;
import com.archimatetool.editor.ui.components.ExtendedWizardDialog;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IFolder;



/**
 * New Model with Canvas ExtensionContributionFactory
 * Add menu item to New menu
 * 
 * @author Phillip Beauvoir
 */
public class NewModelWithCanvasExtensionContributionFactory extends ExtensionContributionFactory {
    
    // TODO Do we really want this menu item?
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
            IArchimateModel model = IArchimateFactory.eINSTANCE.createArchimateModel();
            model.setName(Messages.NewModelWithCanvasExtensionContributionFactory_1);
            model.setDefaults();
            IEditorModelManager.INSTANCE.registerModel(model);
            
            IFolder folder = model.getFolder(FolderType.DIAGRAMS);
            
            WizardDialog dialog = new ExtendedWizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                    new NewCanvasFromTemplateWizard(folder),
                    "NewCanvasFromTemplateWizard"); //$NON-NLS-1$
            int result = dialog.open();
            
            // User cancelled
            if(result == Window.CANCEL) {
                try {
                    IEditorModelManager.INSTANCE.closeModel(model);
                }
                catch(IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        @Override
        public String getId() {
            return "newModelWithCanvasAction"; //$NON-NLS-1$
        };
        
        @Override
        public ImageDescriptor getImageDescriptor() {
            return ICanvasImages.ImageFactory.getImageDescriptor(ICanvasImages.ICON_NEWFILE_16);
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
