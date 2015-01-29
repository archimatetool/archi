/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.view;

import org.eclipse.ui.IViewPart;



/**
 * Interface for Validator View
 * 
 * @author Phillip Beauvoir
 */
public interface IValidatorView extends IViewPart {

    String ID = "com.archimatetool.hammer.validatorView"; //$NON-NLS-1$
    String HELP_ID = "com.archimatetool.help.validatorViewHelp"; //$NON-NLS-1$
    String NAME = Messages.IValidatorView_0;
    
    void validateModel();
}