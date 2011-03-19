/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.editparts.diagram;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import uk.ac.bolton.archimate.editor.diagram.directedit.MultiLineCellEditor;
import uk.ac.bolton.archimate.editor.diagram.editparts.AbstractConnectedEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.IColoredEditPart;
import uk.ac.bolton.archimate.editor.diagram.editparts.ITextAlignedEditPart;
import uk.ac.bolton.archimate.editor.diagram.figures.IDiagramModelObjectFigure;
import uk.ac.bolton.archimate.editor.diagram.figures.diagram.NoteFigure;
import uk.ac.bolton.archimate.editor.diagram.policies.DiagramConnectionPolicy;
import uk.ac.bolton.archimate.editor.diagram.policies.PartComponentEditPolicy;
import uk.ac.bolton.archimate.editor.model.commands.EObjectFeatureCommand;
import uk.ac.bolton.archimate.editor.ui.ViewManager;
import uk.ac.bolton.archimate.editor.utils.StringUtils;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IDiagramModelNote;
import uk.ac.bolton.archimate.model.IFontAttribute;


/**
 * Note Edit Part
 * 
 * @author Phillip Beauvoir
 */
public class NoteEditPart extends AbstractConnectedEditPart
implements IColoredEditPart, ITextAlignedEditPart {
    
    private ConnectionAnchor fAnchor;
    private DirectEditManager fDirectManager;
    
    @Override
    protected void createEditPolicies() {
        // Allow parts to be connected
        installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new DiagramConnectionPolicy());
        
        // Add a policy to handle editing the Parts (for example, deleting a part)
        installEditPolicy(EditPolicy.COMPONENT_ROLE, new PartComponentEditPolicy());
        
        installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new NoteDirectEditPolicy());
    }

    @Override
    protected IFigure createFigure() {
        NoteFigure figure = new NoteFigure((IDiagramModelNote)getModel());
        return figure;
    }

    @Override
    protected void refreshFigure() {
        ((IDiagramModelObjectFigure)figure).refreshVisuals();
    }

    @Override
    public void performRequest(Request req) {
        if(req.getType() == RequestConstants.REQ_DIRECT_EDIT) {
            if(fDirectManager == null) {
                fDirectManager = new NoteDirectEditManager();
            }
            fDirectManager.show();
        }
        else if(req.getType() == RequestConstants.REQ_OPEN) {
            // Show Properties view
            ViewManager.showViewPart(ViewManager.PROPERTIES_VIEW, true);
        }
    }

    @Override
    protected ConnectionAnchor getConnectionAnchor() {
        if(fAnchor == null) {
            fAnchor = new ChopboxAnchor(getFigure());
        }
        return fAnchor;
    }

    /**
     * DirectEditManager
     */
    private class NoteDirectEditManager extends DirectEditManager {

        public NoteDirectEditManager() {
            super(NoteEditPart.this, MultiLineCellEditor.class, new NoteCellEditorLocator());
        }
        
        @Override
        protected CellEditor createCellEditorOn(Composite composite) {
            IDiagramModelNote note = (IDiagramModelNote)getModel();
            int alignment = note.getTextAlignment();
            if(alignment == IFontAttribute.TEXT_ALIGNMENT_CENTER) {
                alignment = SWT.CENTER;
            }
            else if(alignment == IFontAttribute.TEXT_ALIGNMENT_RIGHT) {
                alignment = SWT.RIGHT;
            }
            else {
                alignment = SWT.LEFT;
            }
            return new MultiLineCellEditor(composite, alignment);
        }

        @Override
        protected void initCellEditor() {
            String value = ((IDiagramModelNote)getModel()).getContent();
            getCellEditor().setValue(StringUtils.safeString(value));
            
            Text text = (Text)getCellEditor().getControl();
            text.selectAll();
            
            NoteFigure figure = (NoteFigure)getFigure();
            text.setFont(figure.getFont());
            text.setForeground(figure.getTextControl().getForegroundColor());
        }
    }

    /**
     * CellEditorLocator
     */
    private class NoteCellEditorLocator implements CellEditorLocator {
        public void relocate(CellEditor celleditor) {
            IFigure figure = getFigure();
            Text text = (Text)celleditor.getControl();
            Rectangle rect = figure.getBounds().getCopy();
            figure.translateToAbsolute(rect);
            text.setBounds(rect.x + 5, rect.y + 5, rect.width, rect.height);
        }
    }

    /**
     * DirectEditPolicy
     */
    private class NoteDirectEditPolicy extends DirectEditPolicy {

        @Override
        protected Command getDirectEditCommand(DirectEditRequest request) {
            String content = (String)request.getCellEditor().getValue();
            return new EObjectFeatureCommand("Content", (EObject)getModel(),
                    IArchimatePackage.Literals.TEXT_CONTENT__CONTENT, content);
        }

        @Override
        protected void showCurrentEditValue(DirectEditRequest request) {
        }
    }
}
