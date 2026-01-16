/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.export.svg;

import org.apache.batik.ext.awt.g2d.GraphicContext;
import org.apache.batik.svggen.DOMGroupManager;
import org.apache.batik.svggen.DOMTreeManager;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.eclipse.draw2d.IFigure;
import org.w3c.dom.Element;

import com.archimatetool.editor.diagram.figures.connections.IArchimateConnectionFigure;
import com.archimatetool.editor.diagram.figures.diagram.DiagramModelReferenceFigure;
import com.archimatetool.editor.diagram.figures.elements.IArchimateFigure;

/**
 * ExtendedSVGGraphics2D to add ID attributes to SVG elements
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class ExtendedSVGGraphics2D extends SVGGraphics2D  {
    
    // If this is true add Id attribute to top <g> tags
    private static final boolean ADD_ID_TO_GROUP = false;
    
    // If this is true add Id attribute to child element tags
    private static final boolean ADD_ID_TO_ELEMENT = true;
    
    // Id attributes for concepts and view references
    private static final String CONCEPT_ID = "data-conceptid";
    private static final String VIEW_ID = "data-viewid";
    
    // The current figure from which we can get the Id
    private IFigure currentFigure;
    
    public ExtendedSVGGraphics2D(SVGGeneratorContext generatorCtx, boolean textAsShapes) {
		super(generatorCtx, textAsShapes);
		
		if(ADD_ID_TO_GROUP) {
		    setDOMTreeManager(new CustomDOMTreeManager(getGraphicContext(), getGeneratorContext()));
		    // Need to set a DOMGroupManager with the new DOMTreeManager if setting a custom DOMTreeManager
		    setDOMGroupManager(new DOMGroupManager(getGraphicContext(), getDOMTreeManager()));
		}
		
		if(ADD_ID_TO_ELEMENT) {
	        setDOMGroupManager(new CustomDOMGroupManager(getGraphicContext(), getDOMTreeManager()));
		}
	}

    public void setCurrentFigure(IFigure figure) {
	    currentFigure = figure;
	}
	
    /**
     * Set attribute id of element from the current figure
     */
    private void setId(Element element) {
        if(currentFigure instanceof IArchimateFigure figure) {
            element.setAttribute(CONCEPT_ID, figure.getDiagramModelArchimateObject().getArchimateConcept().getId());
        }
        else if(currentFigure instanceof IArchimateConnectionFigure figure) {
            element.setAttribute(CONCEPT_ID, figure.getDiagramModelArchimateConnection().getArchimateConcept().getId());
        }
        else if(currentFigure instanceof DiagramModelReferenceFigure figure) {
            element.setAttribute(VIEW_ID, figure.getDiagramModelObject().getReferencedModel().getId());
        }
    }
    
    // Custom classes for DOMTreeManager and DOMGroupManager
    
    /**
     * DOMTreeManager that sets Ids on top level group <g> tags
     */
    private class CustomDOMTreeManager extends DOMTreeManager {
        public CustomDOMTreeManager(GraphicContext gc, SVGGeneratorContext generatorContext) {
            // Default maxGCOverrides is SVGGraphics2D.DEFAULT_MAX_GC_OVERRIDES (3)
            // but use 1 so there is a new <g> tag for each element.
            super(gc, generatorContext, 1);
        }
        
        @Override
        public void appendGroup(Element element, DOMGroupManager manager) {
            setId(element);
            super.appendGroup(element, manager);
        }
    }
    
    /**
     * DOMGroupManager that sets Ids on all all child element tags
     */
    private class CustomDOMGroupManager extends DOMGroupManager {
        public CustomDOMGroupManager(GraphicContext gc, DOMTreeManager domTreeManager) {
            super(gc, domTreeManager);
        }
        
        @Override
        public void addElement(Element element, short method) {
            setId(element);
            super.addElement(element, method);
        }
    }
}
