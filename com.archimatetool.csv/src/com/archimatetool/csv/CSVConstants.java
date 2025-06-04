/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.csv;

import java.util.List;

/**
 * Constant definitions for CSV
 * 
 * @author Phillip Beauvoir
 */
public interface CSVConstants {
    
    String ARCHIMATE_MODEL_TYPE = "ArchimateModel"; //$NON-NLS-1$
    
    @SuppressWarnings("nls")
    String[] MODEL_ELEMENTS_HEADER = {
            "ID", "Type", "Name", "Documentation", "Specialization"
    };
    
    @SuppressWarnings("nls")
    String[] RELATIONSHIPS_HEADER = {
        "ID", "Type", "Name", "Documentation", "Source", "Target", "Specialization"
    };

    @SuppressWarnings("nls")
    String[] PROPERTIES_HEADER = {
        "ID", "Key", "Value"
    };
    
    String ELEMENTS_FILENAME = "elements"; //$NON-NLS-1$
    String RELATIONS_FILENAME = "relations"; //$NON-NLS-1$
    String PROPERTIES_FILENAME = "properties"; //$NON-NLS-1$
    String FILE_EXTENSION = ".csv"; //$NON-NLS-1$
    
    String CRLF = "\r\n"; //$NON-NLS-1$
    
    char[] DELIMITERS = { ',', ';', '\t' };
    String[] DELIMITER_NAMES = { Messages.CSVConstants_0, Messages.CSVConstants_1, Messages.CSVConstants_2 };
    
    @SuppressWarnings("nls")
    String[] ENCODINGS = { "UTF-8", "UTF-8 BOM", "ANSI" };
    
    String INFLUENCE_STRENGTH = "Influence_Strength"; //$NON-NLS-1$
    
    String ACCESS_TYPE = "Access_Type"; //$NON-NLS-1$

    @SuppressWarnings("nls")
    List<String> ACCESS_TYPES = 
            List.of("Write", "Read", "Access", "ReadWrite");
    
    String ASSOCIATION_DIRECTED = "Directed"; //$NON-NLS-1$
    
    String JUNCTION_TYPE = "Junction_Type"; //$NON-NLS-1$
    String JUNCTION_OR = "Or"; //$NON-NLS-1$
    String JUNCTION_AND ="And"; //$NON-NLS-1$
}
