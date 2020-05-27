/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.archimatetool.model.IArchimateFactory;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.IDiagramModelNote;

import junit.framework.JUnit4TestAdapter;



/**
 * TextContentRenderer Tests
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class TextContentRendererTests extends AbstractTextRendererTests {

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TextContentRendererTests.class);
    }
    
    private TextContentRenderer renderer = new TextContentRenderer();
    
    @Override
    protected TextContentRenderer getRenderer() {
        return renderer;
    }
    
    @Test
    public void render_Content() {
        String result = renderer.render(createNote("Note Content"), "${content}");
        assertEquals("Note Content", result);
    }
    
    @Test
    public void render_SourceTargetContent() {
        IDiagramModelConnection connection = IArchimateFactory.eINSTANCE.createDiagramModelConnection();
        IDiagramModelNote note1 = createNote("Source Note");
        IDiagramModelNote note2 = createNote("Target Note");
        connection.connect(note1, note2);
        
        String result = renderer.render(note2, "$connection:source{content}");
        assertEquals("Source Note", result);
        
        result = renderer.render(note1, "$connection:target{content}");
        assertEquals("Target Note", result);
    }

    private IDiagramModelNote createNote(String content) {
        IDiagramModelNote note = IArchimateFactory.eINSTANCE.createDiagramModelNote();
        note.setContent(content);
        return note;
    }
}
