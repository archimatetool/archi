/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.archimatetool.model.IDiagramModelArchimateObject;

import junit.framework.JUnit4TestAdapter;



/**
 * WordWrapRendererTests Tests
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class WordWrapRendererTests extends AbstractTextRendererTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(WordWrapRendererTests.class);
    }
    
    private WordWrapRenderer renderer = new WordWrapRenderer();
    
    @Override
    protected WordWrapRenderer getRenderer() {
        return renderer;
    }
    
    @Test
    public void render_WordWrapTextNotSplit() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        assertEquals("HelloWorldNotBrokenLines", renderer.render(dmo, "${wordwrap:6:HelloWorldNotBrokenLines}"));
    }
    
    @Test
    public void render_WordWrapTextSplit() {
        IDiagramModelArchimateObject dmo = TextRendererTests.createDiagramModelObject();
        String text = "Hello World these are broken lines";
        
        assertEquals("Hello \nWorld \nthese \nare \nbroken \nlines", renderer.render(dmo, "${wordwrap:6:" + text + "}"));
        assertEquals("Hello World \nthese are \nbroken lines", renderer.render(dmo, "${wordwrap:13:" + text + "}"));
        assertEquals("Hello World these\nare broken lines", renderer.render(dmo, "${wordwrap:17:" + text + "}"));
    }
}
