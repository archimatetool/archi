/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas;

import java.util.List;

import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.ExtensionContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.services.IServiceLocator;

import com.archimatetool.canvas.model.ICanvasModel;
import com.archimatetool.canvas.preferences.IPreferenceConstants;
import com.archimatetool.canvas.templates.wizard.SaveCanvasAsTemplateWizard;
import com.archimatetool.editor.ui.components.ExtendedWizardDialog;



/**
 * Canvas Editor ExtensionContributionFactory
 * Add context menu items to TreeModelView
 * 
 * @author Phillip Beauvoir
 */
public class SaveAsCanvasExtensionContributionFactory extends ExtensionContributionFactory {
    
    private ICanvasModel fCurrentCanvasModel;
    
    public SaveAsCanvasExtensionContributionFactory() {
    }

    @Override
    public void createContributionItems(IServiceLocator serviceLocator, IContributionRoot additions) {
        boolean enabled = CanvasEditorPlugin.INSTANCE.getPreferenceStore().getBoolean(IPreferenceConstants.CANVAS_EDITOR_ENABLED);
        if(!enabled) {
            return;
        }

        // New Template from Canvas
        IContributionItem item = new ActionContributionItem(new SaveCanvasAsTemplateAction());
        additions.addContributionItem(item, canvasExpression);
    }
    
    /**
     * Action to Save Canvas as Template
     */
    private class SaveCanvasAsTemplateAction extends Action {
        @Override
        public String getText() {
            return Messages.SaveAsCanvasExtensionContributionFactory_0;
        }

        @Override
        public void run() {
            if(fCurrentCanvasModel != null) {
                WizardDialog dialog = new ExtendedWizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                                      new SaveCanvasAsTemplateWizard(fCurrentCanvasModel),
                                      "SaveCanvasAsTemplateWizard"); //$NON-NLS-1$
                dialog.open();
            }
            else {
                System.err.println("Canvas was null in " + getClass()); //$NON-NLS-1$
            }
        }
        
        @Override
        public String getId() {
            return "saveCanvasAsTemplateAction"; //$NON-NLS-1$
        };
        
        @Override
        public ImageDescriptor getImageDescriptor() {
            return ICanvasImages.ImageFactory.getImageDescriptor(ICanvasImages.ICON_CANVAS_MODEL);
        }
    };

    /**
     * Evaluate whether the popup menu is on a Canvas
     */
    private Expression canvasExpression = new Expression() {
        @Override
        public EvaluationResult evaluate(IEvaluationContext context) throws CoreException {
            fCurrentCanvasModel = null;
            Object o = context.getDefaultVariable();
            if(o instanceof List<?> && ((List<?>)o).size() > 0) {
                o = ((List<?>)o).get(0);
                if(o instanceof ICanvasModel) {
                    fCurrentCanvasModel = (ICanvasModel)o;
                }
            }
            return fCurrentCanvasModel != null ? EvaluationResult.TRUE : EvaluationResult.FALSE;
        }
    };
}
