/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package uk.ac.bolton.archimate.help.hints;

import java.net.URL;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;


/**
 * Command Action Handler to show Web site
 * 
 * @author Phillip Beauvoir
 */
public class ShowWebsiteHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
        try {
            IWebBrowser browser = support.getExternalBrowser();
            if(browser != null) {
                URL url = new URL("http://archi.cetis.ac.uk"); //$NON-NLS-1$
                browser.openURL(url);
            }
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
