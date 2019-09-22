/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.csv;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Constant definitions for CSV
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public interface CSVConstants {
    
    String ARCHIMATE_MODEL_TYPE = "ArchimateModel";
    
    String[] MODEL_ELEMENTS_HEADER = {
            "ID", "Type", "Name", "Documentation"
    };
    
    String[] RELATIONSHIPS_HEADER = {
        "ID", "Type", "Name", "Documentation", "Source", "Target"
    };

    String[] PROPERTIES_HEADER = {
        "ID", "Key", "Value"
    };
    
    String ELEMENTS_FILENAME = "elements";
    String RELATIONS_FILENAME = "relations";
    String PROPERTIES_FILENAME = "properties";
    String FILE_EXTENSION = ".csv";
    
    String CRLF = "\r\n";
    
    char[] DELIMITERS = { ',', ';', '\t' };
    String[] DELIMITER_NAMES = { "comma", "semicolon", "tab" };
    
    String[] ENCODINGS = { "UTF-8", "UTF-8 BOM", "ANSI" };
    
    String INFLUENCE_STRENGTH = "Influence_Strength";
    
    String ACCESS_TYPE = "Access_Type";
    List<String> ACCESS_TYPES = new ArrayList<String>(Arrays.asList(new String[] { "Write", "Read", "Access", "ReadWrite" }));
    
    String ASSOCIATION_DIRECTED = "Directed";
    
    String JUNCTION_TYPE = "Junction_Type";
    String JUNCTION_OR = "Or";
    String JUNCTION_AND = "And";
}
