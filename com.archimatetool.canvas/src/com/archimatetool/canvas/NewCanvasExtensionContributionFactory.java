/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.canvas;

import java.io.IOException;
import java.util.List;

import org.eclipse.core.expressions.EvaluationResult;
import org.eclipse.core.expressions.Expression;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
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

import com.archimatetool.canvas.model.ICanvasFactory;
import com.archimatetool.canvas.model.ICanvasModel;
import com.archimatetool.canvas.preferences.IPreferenceConstants;
import com.archimatetool.canvas.templates.wizard.NewCanvasFromTemplateWizard;
import com.archimatetool.editor.ui.components.ExtendedWizardDialog;
import com.archimatetool.editor.views.tree.commands.NewDiagramCommand;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IFolder;



/**
 * Canvas Editor ExtensionContributionFactory
 * Add context menu items to TreeModelView
 * 
 * @author Phillip Beauvoir
 */
public class NewCanvasExtensionContributionFactory extends ExtensionContributionFactory {
    
    private IFolder fCurrentFolder;

    public NewCanvasExtensionContributionFactory() {
    }

    @Override
    public void createContributionItems(IServiceLocator serviceLocator, IContributionRoot additions) {
        // New Blank Canvas
        IContributionItem item = new ActionContributionItem(new NewCanvasAction());
        additions.addContributionItem(item, diagramFolderExpression);
        
        // New Canvas from Template
        item = new ActionContributionItem(new NewCanvasFromTemplateAction());
        additions.addContributionItem(item, diagramFolderExpression);
    }

    /**
     * @return true if folder should hold diagrams
     */
    private boolean isDiagramFolder(IFolder folder) {
        if(folder.getType() == FolderType.DIAGRAMS) {
            return true;
        }
        
        while(folder.eContainer() instanceof IFolder) {
            folder = (IFolder)folder.eContainer();
            if(folder.getType() == FolderType.DIAGRAMS) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Action to create new Blank Canvas
     */
    private class NewCanvasAction extends Action {
        @Override
        public String getText() {
            return Messages.NewCanvasExtensionContributionFactory_0;
        }
        
        @Override
        public void run() {
            if(fCurrentFolder != null) {
                ICanvasModel canvasModel = ICanvasFactory.eINSTANCE.createCanvasModel();
                canvasModel.setName(Messages.NewCanvasExtensionContributionFactory_1);

                // Execute Command
                Command cmd = new NewDiagramCommand(fCurrentFolder, canvasModel, Messages.NewCanvasExtensionContributionFactory_1);
                CommandStack commandStack = (CommandStack)fCurrentFolder.getAdapter(CommandStack.class);
                commandStack.execute(cmd);
            }
            else {
                System.err.println("Folder was null in " + getClass()); //$NON-NLS-1$
            }
        }
        
        @Override
        public String getId() {
            return "newCanvasAction"; //$NON-NLS-1$
        };
        
        @Override
        public ImageDescriptor getImageDescriptor() {
            return ICanvasImages.ImageFactory.getImageDescriptor(ICanvasImages.ICON_CANVAS_BLANK);
        }
    };
    
    /**
     * Action to create new Canvas based on Template
     */
    private class NewCanvasFromTemplateAction extends Action {
        @Override
        public String getText() {
            return Messages.NewCanvasExtensionContributionFactory_2;
        }
        
        @Override
        public void run() {
            if(fCurrentFolder != null) {
                NewCanvasFromTemplateWizard wizard = new NewCanvasFromTemplateWizard();
                WizardDialog dialog = new ExtendedWizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                                      wizard,
                                      "NewCanvasFromTemplateWizard"); //$NON-NLS-1$
                
                if(dialog.open() == Window.OK) {
                    try {
                        ICanvasModel canvasModel = wizard.getNewCanvasModel(fCurrentFolder.getArchimateModel());
                        if(canvasModel != null) {
                            Command cmd = new NewDiagramCommand(fCurrentFolder, canvasModel, Messages.NewCanvasExtensionContributionFactory_3);
                            CommandStack commandStack = (CommandStack)fCurrentFolder.getAdapter(CommandStack.class);
                            commandStack.execute(cmd);
                        }
                    }
                    catch(IOException ex) {
                        ex.printStackTrace();
                        MessageDialog.openError(Display.getCurrent().getActiveShell(),
                                Messages.NewCanvasExtensionContributionFactory_1, ex.getMessage());
                    }
                }
            }
            else {
                System.err.println("Folder was null in " + getClass()); //$NON-NLS-1$
            }
        }
        
        @Override
        public String getId() {
            return "newCanvasFromTemplateAction"; //$NON-NLS-1$
        };
        
        @Override
        public ImageDescriptor getImageDescriptor() {
            return ICanvasImages.ImageFactory.getImageDescriptor(ICanvasImages.ICON_CANVAS_MODEL);
        }
    };

    /**
     * Evaluate whether the popup menu is on a Diagram Folder or a child of a Diagram Folder
     */
    private Expression diagramFolderExpression = new Expression() {
        @Override
        public EvaluationResult evaluate(IEvaluationContext context) throws CoreException {
            fCurrentFolder = null;
            
            // Evaluate visibility here (otherwise we might see an empty "New" menu item if Canvas is not enabled)
            if(!CanvasEditorPlugin.INSTANCE.getPreferenceStore().getBoolean(IPreferenceConstants.CANVAS_EDITOR_ENABLED)) {
                return EvaluationResult.FALSE;
            }
            
            Object o = context.getDefaultVariable();
            
            if(o instanceof List<?> && ((List<?>)o).size() > 0) {
                o = ((List<?>)o).get(0);
                
                if(o instanceof IFolder && isDiagramFolder((IFolder)o)) {
                    fCurrentFolder = (IFolder)o;
                }
                else if(o instanceof IDiagramModel) {
                    fCurrentFolder = (IFolder)((IDiagramModel)o).eContainer();
                }
            }
            
            return fCurrentFolder != null ? EvaluationResult.TRUE : EvaluationResult.FALSE;
        }
    };
}
