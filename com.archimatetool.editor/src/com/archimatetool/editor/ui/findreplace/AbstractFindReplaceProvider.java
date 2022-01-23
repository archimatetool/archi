/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.findreplace;

import java.util.regex.Pattern;

import com.archimatetool.editor.utils.StringUtils;


/**
 * Abstract Find/Replace Provider
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractFindReplaceProvider implements IFindReplaceProvider {

    private int parameter = 0;
    
    @Override
    public boolean canFind(String toFind) {
        return StringUtils.isSet(toFind);
    }
    
    @Override
    public boolean canFindAll(String toFind) {
        return StringUtils.isSet(toFind);
    }
    
    @Override
    public boolean canReplace(String toFind, String toReplaceWith) {
        return canFind(toFind) && toReplaceWith != null;
    }
    
    @Override
    public boolean canReplaceAll(String toFind, String toReplaceWith) {
        return canReplace(toFind, toReplaceWith);
    }
    
    @Override
    public int getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(int op, boolean value) {
        if(value) {
            parameter |= op;
        }
        else {
            parameter &= ~op;
        }
    }

    @Override
    public void setParameter(int parameter) {
        this.parameter = parameter;
    }
    
    @Override
    public boolean understandsParameter(int parameter) {
        return true;
    }

    public boolean isAll() {
        return (getParameter() & PARAM_ALL) != 0;
    }

    public boolean isForward() {
        return (getParameter() & PARAM_FORWARD) != 0;
    }

    public boolean isCaseSensitive() {
        return (getParameter() & PARAM_CASE_SENSITIVE) != 0;
    }

    public boolean isWholeWord() {
        return (getParameter() & PARAM_WHOLE_WORD) != 0;
    }

    protected boolean isAllModels() {
        return (getParameter() & PARAM_ALL_MODELS) != 0;
    }
    
    protected boolean isIncludeFolders() {
        return (getParameter() & PARAM_INCLUDE_FOLDERS) != 0;
    }

    protected boolean isIncludeRelations() {
        return (getParameter() & PARAM_INCLUDE_RELATIONS) != 0;
    }

    /**
     * @param oldString The old string
     * @param toFind The string to find in the old string
     * @param toReplaceWith The string to replace with
     * @return A new string based on the old string, replacing the part toFind with toReplaceWith
     */
    protected String getReplacedString(String oldString, String toFind, String toReplaceWith) {
        toFind = Pattern.quote(toFind);

        if(!isCaseSensitive()) {
            toFind = "(?iu:" + toFind + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        return oldString.replaceAll(toFind, toReplaceWith);
    }
    
    /**
     * @param searchString The string to search on
     * @return The regex pattern to search on given the set search parameters
     */
    protected String getSearchStringPattern(String searchString) {
        searchString = Pattern.quote(searchString);
        
        // Match whole word
        if(isWholeWord()) {
            searchString = "\\b" + searchString + "\\b"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        // (?s) = Ignore line-breaks, .* = contain string
        searchString = "(?s).*" + searchString + ".*"; //$NON-NLS-1$ //$NON-NLS-2$
        
        // Ignore case and unicode
        if(!isCaseSensitive()) {
            searchString = "(?iu:" + searchString + ")"; //$NON-NLS-1$ //$NON-NLS-2$
        }
        
        return searchString;
    }
    
}
