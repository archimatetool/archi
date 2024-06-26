/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.export.svg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.batik.dom.GenericDocument;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;


@SuppressWarnings("nls")
public class AbstractExportProviderTests {
    
    protected AbstractExportProvider provider;
    protected Shell shell;
    protected IFigure rootFigure;
    
    public void runOnceBeforeEachTest() {
        shell = new Shell();
        rootFigure = new FreeformLayer();
        rootFigure.setBounds(new Rectangle(0, 0, 500, 500));
    }
    
    @AfterEach
    public void runOnceAfterEachTest() {
        shell.dispose();
    }

    @Test
    public void testGetViewportBounds() {
        provider.init(null, shell, rootFigure);

        // Default blank size
        assertEquals(new Rectangle(0, 0, 100, 100), provider.getViewportBounds(rootFigure));
        
        // Add a child figure
        IFigure childFigure = new Figure();
        rootFigure.add(childFigure);

        // Bounds is expanded by 10 pixesls each side
        childFigure.setBounds(new Rectangle(0, 0, 100, 50));
        assertEquals(new Rectangle(-10, -10, 120, 70), provider.getViewportBounds(rootFigure));
        
        // Bounds is small figure and expanded by 10 pixesls each side
        childFigure.setBounds(new Rectangle(200, 200, 128, 52));
        assertEquals(new Rectangle(190, 190, 148, 72), provider.getViewportBounds(rootFigure));
    }

    @Test
    public void testCreateDocument() {
        Document document = provider.createDocument();
        assertNotNull(document);
        assertNotNull(document.getDocumentElement());
    }
    
    @Test
    public void testCreateContext() {
        provider.init(null, shell, rootFigure);
        SVGGeneratorContext ctx = provider.createContext(new GenericDocument(null, null), true);
        assertNotNull(ctx);
        assertTrue(ctx.isEmbeddedFontsOn());
        assertTrue(ctx.getComment().startsWith("Generated by Archi"));
    }
    
    @Test
    public void testSetViewBoxAttribute() {
        Document document = provider.createDocument();
        provider.setViewBoxAttribute(document.getDocumentElement(), 12, 13, 14, 15);
        assertEquals("12 13 14 15", document.getDocumentElement().getAttribute("viewBox"));
    }
}
