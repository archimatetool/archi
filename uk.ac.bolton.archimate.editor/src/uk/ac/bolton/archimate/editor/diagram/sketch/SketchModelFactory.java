/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.diagram.sketch;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.IEditorPart;

import uk.ac.bolton.archimate.editor.diagram.ICreationFactory;
import uk.ac.bolton.archimate.editor.ui.ColorFactory;
import uk.ac.bolton.archimate.model.IArchimateFactory;
import uk.ac.bolton.archimate.model.IDiagramModelConnection;
import uk.ac.bolton.archimate.model.IDiagramModelGroup;
import uk.ac.bolton.archimate.model.ISketchModelActor;
import uk.ac.bolton.archimate.model.ISketchModelSticky;


/**
 * Sketch Model Factory for creating objects from the Palette in the Sketch Editor
 * 
 * @author Phillip Beauvoir
 */
public class SketchModelFactory implements ICreationFactory {
    
    private EClass fTemplate;
    private Object fParam;
    
    public SketchModelFactory(EClass template) {
        this(template, null);
    }
    
    public boolean isUsedFor(IEditorPart editor) {
        return editor instanceof ISketchEditor;
    }
    
    /**
     * Constructor for creating a new Ecore type model with a parameter
     * @param eClass
     * @param param
     */
    public SketchModelFactory(EClass template, Object param) {
        fTemplate = template;
        fParam = param;
    }
    
    public Object getNewObject() {
        Object object = IArchimateFactory.eINSTANCE.create(fTemplate);
        
        // Actor
        if(object instanceof ISketchModelActor) {
            ((ISketchModelActor)object).setName("Actor");
        }
        
        // Sticky
        else if(object instanceof ISketchModelSticky) {
            ((ISketchModelSticky)object).setName("Sticky");
            if(fParam instanceof Color) {
                String color = ColorFactory.convertRGBToString(((Color)fParam).getRGB());
                ((ISketchModelSticky)object).setFillColor(color);
            }
        }
        
        // Group
        else if(object instanceof IDiagramModelGroup) {
            ((IDiagramModelGroup)object).setName("Group");
        }
        
        // Connection
        else if(object instanceof IDiagramModelConnection) {
            if(fParam instanceof Integer) {
                ((IDiagramModelConnection)object).setType((Integer)fParam);
            }
        }
        
        return object;
    }

    public Object getObjectType() {
        return fTemplate;
    }
}
