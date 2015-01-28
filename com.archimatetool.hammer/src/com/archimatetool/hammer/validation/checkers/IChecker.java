/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.checkers;

import java.util.List;

import com.archimatetool.hammer.validation.issues.IIssue;



/**
 * Check one aspect of a model
 * 
 * @author Phillip Beauvoir
 */
public interface IChecker {
    List<IIssue> getIssues();
}
