/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.findreplace;



/**
 * Find/Replace Provider
 * 
 * @author Phillip Beauvoir
 */
public interface IFindReplaceProvider {

    /*
     * Find/Replace parameters
     */
    int PARAM_NONE = 0;
    int PARAM_CASE_SENSITIVE = 1;
    int PARAM_WHOLE_WORD = 1 << 1;
    int PARAM_FORWARD = 1 << 2;
    int PARAM_BACKWARD = 1 << 3;
    int PARAM_SELECTED_MODEL = 1 << 4;
    int PARAM_ALL_MODELS = 1 << 5;
    int PARAM_INCLUDE_FOLDERS = 1 << 6;
    int PARAM_INCLUDE_RELATIONS = 1 << 7;
    int PARAM_ALL = 1 << 16;
    
    /**
     * @return The current set parameter
     */
    int getParameter();
    
    /**
     * Set a parameter value
     * @param op
     * @param value
     */
    void setParameter(int op, boolean value);

    /**
     * Set the parameter word
     * @param parameter
     */
    void setParameter(int parameter);
    
    /**
     * @param parameter
     * @return True if this provider understands a given parameter
     */
    boolean understandsParameter(int parameter);

    /**
     * @param toFind
     * @return True if the provider is capable of finding the given text
     */
    boolean canFind(String toFind);

    /**
     * @param toFind
     * @return True if the provider is capable of finding all instances of the given text
     */
    boolean canFindAll(String toFind);
    
    /**
     * @param toFind
     * @param toReplaceWith
     * @return True if the provider is capable of replacing the given text
     */
    boolean canReplace(String toFind, String toReplaceWith);
    
    /**
     * @param toFind
     * @param toReplaceWith
     * @return True if the provider is capable of replacing all instances of the given text
     */
    boolean canReplaceAll(String toFind, String toReplaceWith);
    
    /**
     * Find the next instance of the given text or, if PARAM_ALL is set, all instances
     * @param toFind
     * @return True if the String has been found
     */
    boolean find(String toFind);
    
    /**
     * Replace the next instance of the given text or, if PARAM_ALL is set, all instances
     * @param toFind
     * @param toReplaceWith
     * @return True if the String has been replaced
     */
    boolean replace(String toFind, String toReplaceWith);
}
