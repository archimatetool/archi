/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModel;



/**
 * Example Exporter of Archimate model
 * Exports all concepts in a selected model to a text file in format:
 * "Name","Type","Documentation"
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class MyExporter {
    
    public void export(File file, IArchimateModel model) throws IOException {
        // Write all concepts' name, type and documentation to file
        try(OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file))) {
            for(IArchimateConcept concept : getConcepts(model)) {
                String string = normalise(concept.getName()) + ", " + normalise(concept.eClass().getName()) + ", " + normalise(concept.getDocumentation());
                writer.write(string + "\n");
            }
        }
    }
    
    private List<IArchimateConcept> getConcepts(IArchimateModel model) {
        List<IArchimateConcept> concepts = new ArrayList<>();

        // Get all concepts in the model
        for(Iterator<EObject> iter = model.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject instanceof IArchimateConcept concept) {
                concepts.add(concept);
            }
        }

        // Sort them
        Collator collator = Collator.getInstance();
        concepts.sort(Comparator
                .comparing(IArchimateConcept::getName, (n1, n2) -> collator.compare(StringUtils.safeString(n1), StringUtils.safeString(n2)))
                .thenComparing(IArchimateConcept::eClass, (c1, c2) -> collator.compare(c1.getName(), c2.getName())));
        
        return concepts;
    }
    
    /**
     * Check text for null, replace newlines with a space, and surround text with quotes
     */
    private String normalise(String text) {
        if(text == null) {
            return "";
        }
        
        text = StringUtils.normaliseNewLineCharacters(text);
        text = "\"" + text + "\"";
        
        return text;
    }
}
