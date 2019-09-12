/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package org.opengroup.archimate.xmlexchange.wizard;

import java.io.File;
import java.util.Map;

import org.eclipse.jface.wizard.Wizard;



/**
 * Export to XML Wizard
 * 
 * @author Phillip Beauvoir
 */
public class ExportToXMLWizard extends Wizard {
    
    private String fModelName;
    
    private ExportToXMLPage fPage;
    private ExportToXMLPageMetadata fPageMetadata;
    
    private File file;
    private Map<String, String> metadata;
    private boolean doSaveOrganisation;
    private boolean doIncludeXSD;
    private String languageCode;
    private boolean doValidateAfterExport;
    
    public ExportToXMLWizard(String modelName) {
        fModelName = modelName;
        setWindowTitle(Messages.ExportToXMLWizard_0);
    }
    
    @Override
    public void addPages() {
        fPage = new ExportToXMLPage(fModelName);
        addPage(fPage);
        
        fPageMetadata = new ExportToXMLPageMetadata();
        addPage(fPageMetadata);
    }
    
    public File getFile() {
        return file;
    }
    
    public Map<String, String> getMetadata() {
        return metadata;
    }
    
    public boolean doSaveOrganisation() {
        return doSaveOrganisation;
    }

    public boolean doIncludeXSD() {
        return doIncludeXSD;
    }

    public boolean doValidateAfterExport() {
        return doValidateAfterExport;
    }
    
    public String getLanguageCode() {
        return languageCode;
    }
    
    @Override
    public boolean performFinish() {
        file = new File(fPage.getFileName());
        metadata = fPageMetadata.getMetadata();
        doSaveOrganisation = fPage.doSaveOrganisation();
        doIncludeXSD = fPage.doIncludeXSD();
        doValidateAfterExport = fPage.doValidateAfterExport();
        languageCode = fPage.getLanguageCode();
        
        fPage.storePreferences();
        fPageMetadata.storePreferences();
        
        return true;
    }
}
