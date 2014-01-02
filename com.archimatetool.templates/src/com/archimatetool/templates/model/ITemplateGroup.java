/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.templates.model;

import java.util.List;



/**
 * Interface for All Template Groups
 * 
 * @author Phillip Beauvoir
 */
public interface ITemplateGroup {
    
    /**
     * @return The Name of the group
     */
    String getName();
    
    /**
     * Set the Name of the Group
     */
    void setName(String name);
    
    /**
     * @return The templates in the group
     */
    List<ITemplate> getTemplates();
    
    /**
     * @return A copy of the templates in the group, sorted
     */
    List<ITemplate> getSortedTemplates();

    /**
     * Add a template
     * @param template
     */
    void addTemplate(ITemplate template);
    
    /**
     * Delete a template
     * @param template
     * @return
     */
    boolean removeTemplate(ITemplate template);
}
