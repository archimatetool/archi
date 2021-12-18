package org.opengroup.archimate.xmlexchange;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.eclipse.ui.plugin.AbstractUIPlugin;


/**
 * Activitor
 * 
 * @author Phillip Beauvoir
 */
public class XMLExchangePlugin extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "org.opengroup.archimate.xmlexchange"; //$NON-NLS-1$
    
    public static final String XSD_FOLDER = "xsd/"; //$NON-NLS-1$
    public static final String ARCHIMATE3_DIAGRAM_XSD = "archimate3_Diagram.xsd"; //$NON-NLS-1$
    public static final String ARCHIMATE3_MODEL_XSD = "archimate3_Model.xsd"; //$NON-NLS-1$
    public static final String ARCHIMATE3_VIEW_XSD = "archimate3_View.xsd"; //$NON-NLS-1$
    public static final String DUBLINCORE_XSD = "dc.xsd"; //$NON-NLS-1$
    public static final String XML_XSD = "xml.xsd"; //$NON-NLS-1$

    /**
     * The shared instance
     */
    public static XMLExchangePlugin INSTANCE;

    public XMLExchangePlugin() {
        INSTANCE = this;
    }
    
    public void copyXSDFile(String xsdFile, File outputFile) throws IOException {
        InputStream in = getBundleInputStream(XSD_FOLDER + xsdFile);
        Files.copy(in, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        in.close();
    }

    public InputStream getBundleInputStream(String bundleFileName) throws IOException {
        URL url = getBundle().getEntry(bundleFileName);
        return url.openStream();
    }
}
