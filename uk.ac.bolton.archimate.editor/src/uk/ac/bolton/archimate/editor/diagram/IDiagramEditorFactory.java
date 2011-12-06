/*******************************************************************************
 * Copyright (c) 2011 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram;

import org.eclipse.gef.EditPartFactory;
import org.eclipse.ui.IEditorInput;

import uk.ac.bolton.archimate.model.IDiagramModel;


/**
 * IDiagramEditorInfo for extensions
 * 
 * @author Phillip Beauvoir
 */
public interface IDiagramEditorFactory {
    
    /**
     * @param model
     * @return true if this is a Factory for this type of model
     */
    boolean isFactoryFor(IDiagramModel model);
    
    /**
     * @return a new GEF EditPartFactory for this Editor
     */
    EditPartFactory createEditPartFactory();

    /**
     * @return The Editor ID
     */
    String getEditorID();
    
    /**
     * @return new Editor Input for model
     */
    IEditorInput createEditorInput(IDiagramModel model);

}
