/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.canvas;

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
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.ExtensionContributionFactory;
import org.eclipse.ui.menus.IContributionRoot;
import org.eclipse.ui.services.IServiceLocator;

import uk.ac.bolton.archimate.canvas.model.ICanvasFactory;
import uk.ac.bolton.archimate.canvas.model.ICanvasModel;
import uk.ac.bolton.archimate.canvas.preferences.IPreferenceConstants;
import uk.ac.bolton.archimate.canvas.templates.wizard.NewCanvasFromTemplateWizard;
import uk.ac.bolton.archimate.editor.ui.components.ExtendedWizardDialog;
import uk.ac.bolton.archimate.editor.views.tree.commands.NewDiagramCommand;
import uk.ac.bolton.archimate.model.FolderType;
import uk.ac.bolton.archimate.model.IDiagramModel;
import uk.ac.bolton.archimate.model.IFolder;


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
        boolean enabled = CanvasEditorPlugin.INSTANCE.getPreferenceStore().getBoolean(IPreferenceConstants.CANVAS_EDITOR_ENABLED);
        if(!enabled) {
            return;
        }
        
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
            return "Blank Canvas";
        }
        
        @Override
        public void run() {
            if(fCurrentFolder != null) {
                ICanvasModel canvasModel = ICanvasFactory.eINSTANCE.createCanvasModel();
                canvasModel.setName("New Canvas");

                // Execute Command
                Command cmd = new NewDiagramCommand(fCurrentFolder, canvasModel, "New Canvas");
                CommandStack commandStack = (CommandStack)fCurrentFolder.getAdapter(CommandStack.class);
                commandStack.execute(cmd);
            }
            else {
                System.err.println("Folder was null in " + getClass());
            }
        }
        
        @Override
        public String getId() {
            return "newCanvasAction";
        };
        
        @Override
        public ImageDescriptor getImageDescriptor() {
            return ICanvasImages.ImageFactory.getImageDescriptor(ICanvasImages.ICON_CANVAS_BLANK_16);
        }
    };
    
    /**
     * Action to create Canvas based on Template
     */
    private class NewCanvasFromTemplateAction extends Action {
        @Override
        public String getText() {
            return "Canvas from Template...";
        }
        
        @Override
        public void run() {
            if(fCurrentFolder != null) {
                WizardDialog dialog = new ExtendedWizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
                                      new NewCanvasFromTemplateWizard(fCurrentFolder),
                                      "NewCanvasFromTemplateWizard");
                dialog.open();
            }
            else {
                System.err.println("Folder was null in " + getClass());
            }
        }
        
        @Override
        public String getId() {
            return "newCanvasFromTemplateAction";
        };
        
        @Override
        public ImageDescriptor getImageDescriptor() {
            return ICanvasImages.ImageFactory.getImageDescriptor(ICanvasImages.ICON_CANVAS_MODEL_16);
        }
    };

    /**
     * Evaluate whether the popup menu is on a Diagram Folder or a child of a Diagram Folder
     */
    private Expression diagramFolderExpression = new Expression() {
        @Override
        public EvaluationResult evaluate(IEvaluationContext context) throws CoreException {
            fCurrentFolder = null;
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
