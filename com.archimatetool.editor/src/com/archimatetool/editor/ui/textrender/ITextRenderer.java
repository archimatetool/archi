/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.textrender;

import com.archimatetool.model.IArchimateModelObject;

/**
 * Interface for a text control renderer
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public interface ITextRenderer {
    
    String modelPrefix = "model";
    String viewPrefix = "view";
    String modelFolderPrefix = "mfolder";
    String viewFolderPrefix = "vfolder";
    String sourcePrefix = "source";
    String targetPrefix = "target";
    String parentPrefix = "parent";
    
    /**
     * Core prefixes of model folder, view folder, model or view, source or target
     * For example $model{name}
     */
    String corePrefixes = "mfolder|vfolder|model|view|source|target|parent";
    
    /**
     * Connection/relationship prefixes and source/target
     * For example $connection:source{name} $composition:target{name}
     * This is two non-capturing groups
     */
    String connectionPrefixes = "(?:connection:|triggering:|access:|specialization:|composition:|assignment:|aggregation:|realization:|serving:|influence:|flow:|association:)(?:source|target|msource|mtarget)";
    
    /**
     * This consists of one capturing group choice (group 1) of either
     * 1. one of the corePrefixes
     * or
     * 2. one of the connectionPrefixes
     */
    String allPrefixesGroup = "(" + corePrefixes + "|" + connectionPrefixes + ")?";
    
    /**
     * @param object The object whose text should be rendered
     * @param text The text that should be rendered
     * @return The result of the text rendering
     */
    String render(IArchimateModelObject object, String text);
}