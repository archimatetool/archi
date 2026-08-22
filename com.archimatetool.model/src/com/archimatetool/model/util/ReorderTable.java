package com.archimatetool.model.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * Run this on a new version of the relations matrix to put it in alphapebetical order
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class ReorderTable {
    
    public static void main(String[] args) {
        File file = new File("model/relationships.xml");
        
        System.out.println("Sorting relationships.xml...");
        
        try {
            Document doc = new SAXBuilder().build(new FileInputStream(file));
            
            // Sort source concepts
            doc.getRootElement().sortChildren(Comparator.comparing(element -> element.getAttributeValue("concept")));
            
            for(Element sourceElement : doc.getRootElement().getChildren("source")) {
                // Sort target concepts
                sourceElement.sortChildren(Comparator.comparing(element -> element.getAttributeValue("concept")));
                
                // Sort letters
                for(Element targetElement : sourceElement.getChildren("target")) {
                    String att = targetElement.getAttributeValue("relations");
                    
                    String sorted = Arrays.stream(att.split(""))
                                          .sorted(String.CASE_INSENSITIVE_ORDER)
                                          .collect(Collectors.joining());
                    
                    targetElement.setAttribute("relations", sorted);
                }
            }
            
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat().setTextMode(Format.TextMode.PRESERVE));
            
            try(FileOutputStream out = new FileOutputStream(file)) {
                outputter.output(doc, out);
            }
            
            System.out.println("Done!");
        }
        catch(JDOMException | IOException ex) {
            ex.printStackTrace();
        }
    }
    
}
