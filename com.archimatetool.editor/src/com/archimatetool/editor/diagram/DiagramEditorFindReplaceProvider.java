/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.diagram;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;

import com.archimatetool.editor.diagram.editparts.AbstractDiagramPart;
import com.archimatetool.editor.model.commands.EObjectFeatureCommand;
import com.archimatetool.editor.model.commands.NonNotifyingCompoundCommand;
import com.archimatetool.editor.ui.findreplace.AbstractFindReplaceProvider;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IAdapter;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.INameable;
import com.archimatetool.model.ITextContent;


/**
 * Find/Replace Provider for Diagram Editor
 * 
 * @author Phillip Beauvoir
 */
public class DiagramEditorFindReplaceProvider extends AbstractFindReplaceProvider {
    
    private GraphicalViewer fGraphicalViewer;

    /**
     * If this is true then when user has selected items in the tree and chooses "Replace", these elements
     * are the ones for the replace operation (if they match the criteria).
     * If this is false then the classic "Replace" behaviour is used selecting the next/previous match and then replacing that one.
     */
    boolean replaceSelection = false;

    public DiagramEditorFindReplaceProvider(GraphicalViewer graphicalViewer) {
        fGraphicalViewer = graphicalViewer;
    }

    @Override
    public boolean find(String toFind) {
        // Find All
        if(isAll()) {
            List<EditPart> editParts = getAllMatchingEditParts(toFind);
            fGraphicalViewer.setSelection(new StructuredSelection(editParts));
            if(!editParts.isEmpty()) {
                fGraphicalViewer.reveal(editParts.get(0));
            }
            return !editParts.isEmpty();
        }
        // Find Next/Previous
        else {
            EditPart editPart = findNextEditPart(getFirstSelectedEditPart(), toFind);
            if(editPart != null) {
                fGraphicalViewer.setSelection(new StructuredSelection(editPart));
                fGraphicalViewer.reveal(editPart);
            }
            return (editPart != null);
        }
    }

    @Override
    public boolean replace(String toFind, String toReplaceWith) {
        // Replace All
        if(isAll()) {
            List<EditPart> editParts = getAllMatchingEditParts(toFind);
            if(!editParts.isEmpty()) {
                List<String> newStrings = new ArrayList<String>();
                
                for(EditPart editPart : editParts) {
                    String oldString = getStringFromEditPart(editPart);
                    String newString = getReplacedString(oldString, toFind, toReplaceWith);
                    newStrings.add(newString);
                }
                
                doReplaceStringCommands(editParts, newStrings);
                fGraphicalViewer.setSelection(new StructuredSelection(editParts));
                fGraphicalViewer.reveal(editParts.get(0));
            }
            return !editParts.isEmpty();
        }
        
        // Replace Next/Previous
        else {
            // Replace on selected EditParts
            if(replaceSelection) {
                List<EditPart> selected = getSelectedEditParts();
                if(!selected.isEmpty()) {
                    List<EditPart> editParts = new ArrayList<EditPart>();
                    List<String> newTexts = new ArrayList<String>();
                    
                    for(EditPart editPart : selected) {
                        if(matches(editPart, toFind)) {
                            editParts.add(editPart);
                            
                            String oldString = getStringFromEditPart(editPart);
                            String newString = getReplacedString(oldString, toFind, toReplaceWith);
                            newTexts.add(newString);
                        }
                    }
                    
                    if(!editParts.isEmpty()) {
                        doReplaceStringCommands(editParts, newTexts);
                        return true;
                    }
                }
            }
            
            // Replace on next single selection
            else {
                EditPart editPart = getFirstSelectedEditPart();
                if(matches(editPart, toFind)) {
                    String oldString = getStringFromEditPart(editPart);
                    doReplaceStringCommand(editPart, getReplacedString(oldString, toFind, toReplaceWith));
                    return true;
                }
            }
            
            // Else move forward
            return find(toFind);
        }
    }

    /**
     * Find the next/previous EditPart in the Viewer that matches the find criteria
     * @param startEditPart The EditPart to start the search from for next/previous
     * @param toFind The string to find
     * @return The next/previous EditPart if found, or null
     */
    EditPart findNextEditPart(EditPart startEditPart, String toFind) {
        // Get *all* EditParts in the viewer
        List<EditPart> editParts = getAllEditParts();
        
        if(editParts.isEmpty()) {
            return null;
        }
        
        // Increment for forward/backward
        int increment = isForward() ? 1 : -1;
        
        // Starting index (defaults for a null startEditPart)
        int startIndex = isForward() ? 0 : editParts.size() - 1;
        
        // Find starting point from startEditPart
        if(startEditPart != null) {
            startIndex = editParts.indexOf(startEditPart) + increment;
        }
        
        // Iterate through all elements forwards or backwards until we find the next matching edit part
        for(int i = startIndex; isForward() ? (i < editParts.size()) : (i >= 0); i += increment) {
            EditPart editPart = editParts.get(i);
            if(matches(editPart, toFind)) {
                return editPart;
            }
        }
        
        // Didn't find one so start back again (circular)
        if(startEditPart != null) {
            return findNextEditPart(null, toFind);
        }
        
        return null;
    }

    @Override
    public boolean understandsParameter(int parameter) {
        return parameter != PARAM_INCLUDE_FOLDERS
                && parameter != PARAM_ALL_MODELS
                && parameter != PARAM_SELECTED_MODEL;
    }
    
    /**
     * @param toFind The string to find a match on. If this is null, then collect all EditParts in the viewer.
     * @return All elements in the Viewer that match the string, in sorted and filtered order
     */
    List<EditPart> getAllMatchingEditParts(String toFind) {
        return getMatchingEditParts(fGraphicalViewer.getContents(), toFind);
    }
    
    /**
     * @param editPart The EditPart to start searching from.
     * @param toFind The string to find a match on. If this is null, then collect all EditParts in the viewer.
     * @return A list of all matching EditParts in the viewer model as sorted and filtered by the Viewer model
     */
    List<EditPart> getMatchingEditParts(EditPart editPart, String toFind) {
        List<EditPart> list = new ArrayList<EditPart>();
        
        if(toFind == null) { // collect all
            list.add(editPart);
        }
        else if(matches(editPart, toFind)) {
            list.add(editPart);
        }

        // Add connections if the option is set
        if(isIncludeRelations() && (editPart instanceof GraphicalEditPart)) {
            for(Object connectionEditPart : ((GraphicalEditPart)editPart).getSourceConnections()) { // (only need to get source connections, not target)
                if(toFind == null) { // collect all
                    list.add((EditPart)connectionEditPart);
                }
                else if(matches((EditPart)connectionEditPart, toFind)) {
                    list.add((EditPart)connectionEditPart);
                }
            }
        }

        for(Object object : editPart.getChildren()) {
            list.addAll(getMatchingEditParts((EditPart)object, toFind));
        }
        
        return list;
    }
    
    /**
     * @return *all* EditParts in the Viewer of model type INameable, in sorted and filtered order
     */
    private List<EditPart> getAllEditParts() {
        return getAllMatchingEditParts(null);
    }

    /**
     * @return True if editPart is a matching type and toFind is found in the editPart's name or text content
     */
    private boolean matches(EditPart editPart, String toFind) {
        if(editPart instanceof AbstractDiagramPart || !(editPart instanceof GraphicalEditPart)
                || !editPart.isSelectable() || !StringUtils.isSet(toFind)) {
            return false;
        }
        
        String string = getStringFromEditPart(editPart);
        return string != null && string.matches(getSearchStringPattern(toFind));
    }
    
    private EditPart getFirstSelectedEditPart() {
        IStructuredSelection selection = (IStructuredSelection)fGraphicalViewer.getSelection();
        EditPart editPart = (EditPart)selection.getFirstElement();
        return (editPart instanceof AbstractDiagramPart) ? null : editPart;
    }
    
    @SuppressWarnings("unchecked")
    private List<EditPart> getSelectedEditParts() {
        IStructuredSelection selection = (IStructuredSelection)fGraphicalViewer.getSelection();
        return selection.toList();
    }

    void doReplaceStringCommand(EditPart editPart, String newText) {
        Command command = createCommand(editPart, newText);
        CommandStack stack = (CommandStack)((IAdapter)editPart.getModel()).getAdapter(CommandStack.class);
        if(command != null && stack != null) {
            stack.execute(command);
        }
    }
    
    void doReplaceStringCommands(List<EditPart> editParts, List<String> newTexts) {
        // Must match sizes
        if(editParts.size() != newTexts.size() || editParts.isEmpty()) {
            return;
        }

        CommandStack stack = (CommandStack)((IAdapter)editParts.get(0).getModel()).getAdapter(CommandStack.class);
        if(stack == null) {
            return;
        }

        CompoundCommand compoundCommand = new NonNotifyingCompoundCommand(Messages.DiagramEditorFindReplaceProvider_0);

        for(int i = 0; i < editParts.size(); i++) {
            EditPart editPart = editParts.get(i);
            String newText = newTexts.get(i);
            
            Command command = createCommand(editPart, newText);
            if(command != null) {
                compoundCommand.add(command);
            }
        }

        stack.execute(compoundCommand.unwrap());
    }
    
    Command createCommand(EditPart editPart, String newString) {
        // Text Content
        if(editPart.getModel() instanceof ITextContent) {
            return new EObjectFeatureCommand(Messages.DiagramEditorFindReplaceProvider_0, (EObject)editPart.getModel(),
                    IArchimatePackage.Literals.TEXT_CONTENT__CONTENT, newString);
        }

        // Name
        if(editPart.getModel() instanceof INameable) {
            return new EObjectFeatureCommand(Messages.DiagramEditorFindReplaceProvider_0, (EObject)editPart.getModel(),
                    IArchimatePackage.Literals.NAMEABLE__NAME, newString);
        }

        return null;
    }
    
    String getStringFromEditPart(EditPart editPart) {
        if(editPart.getModel() instanceof ITextContent) {
            return ((ITextContent)editPart.getModel()).getContent();
        }
        
        if(editPart.getModel() instanceof INameable) {
            return ((INameable)editPart.getModel()).getName();
        }
        
        return null;
    }
}
