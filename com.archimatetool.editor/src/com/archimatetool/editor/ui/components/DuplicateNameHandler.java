/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.components;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.window.DefaultToolTip;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.preferences.IPreferenceConstants;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IFolder;

/**
 * Handler for alerting user to duplicate names when editing a concept name
 * 
 * @author Phillip Beauvoir
 */
public class DuplicateNameHandler {
    
    private DefaultToolTip toolTip;
    private Set<String> names;
    private Text textControl;
    private VerifyListener verifylistener;

    public DuplicateNameHandler(Text textControl, IArchimateConcept concept) {
        // Preference not set, ignore
        if(!ArchiPlugin.INSTANCE.getPreferenceStore().getBoolean(IPreferenceConstants.SHOW_DUPLICATE_NAME_WARNING)) {
            return;
        }
        
        names = getConceptNames(concept);
        if(names.isEmpty()) {
            return;
        }
        
        verifylistener = event -> {
            String currentText = ((Text)event.widget).getText();
            String newText = currentText.substring(0, event.start) + event.text + currentText.substring(event.end);
            
            hideToolTip();
            
            if(names.contains(newText)) {
                toolTip = new DefaultToolTip(textControl, ToolTip.NO_RECREATE, true);
                toolTip.setHideDelay(3000);
                toolTip.setText(NLS.bind(Messages.DuplicateNameHandler_0, newText));
                toolTip.show(new Point(0, -25));
            }
        };
        
        this.textControl = textControl;
        textControl.addVerifyListener(verifylistener);
    }
    
    /**
     * Create a list of all names in the model for a given concept type
     */
    private Set<String> getConceptNames(IArchimateConcept concept) {
        Set<String> names = new HashSet<>();
        
        IFolder folder = concept.getArchimateModel().getDefaultFolderForObject(concept);

        for(Iterator<EObject> iter = folder.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject.eClass() == concept.eClass() && eObject != concept) {
                names.add(((IArchimateConcept)eObject).getName());
            }
        }
        
        return names;
    }
    
    public void dispose() {
        hideToolTip();
        
        if(textControl != null && !textControl.isDisposed()) {
            textControl.removeVerifyListener(verifylistener);
            textControl = null;
        }
        
        names = null;
    }
    
    private void hideToolTip() {
        if(toolTip != null) {
            toolTip.hide();
            toolTip = null;
        }
    }
}
