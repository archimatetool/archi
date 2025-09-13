/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.handlers.HandlerUtil;


/**
 * Command Action Handler for Import
 * 
 * @author Phillip Beauvoir
 */
public class XMLExchangeImportHandler extends AbstractHandler {
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        new XMLExchangeImportProvider().doImport(HandlerUtil.getActiveWorkbenchWindow(event));
        return null;
    }
}
