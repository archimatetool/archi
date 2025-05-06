/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.templates.model;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

import org.eclipse.swt.graphics.Image;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.editor.utils.ZipUtils;
import com.archimatetool.jdom.JDOMUtils;




/**
 * Abstract Template
 * 
 * @author Phillip Beauvoir
 */
public abstract class AbstractTemplate implements ITemplate, ITemplateXMLTags {
    
    private String fID;
    private String fName = ""; //$NON-NLS-1$
    private String fDescription = ""; //$NON-NLS-1$
    private String fKeyThumbnailPath;

    private File fFile;
    
    private Map<Integer, Image> fThumbnails = new HashMap<>();
    private int fThumbnailCount = -1;
    
    public AbstractTemplate() {
    }
    
    public AbstractTemplate(File file) throws IOException {
        setFile(file);
    }

    @Override
    public String getName() {
        return fName;
    }
    
    @Override
    public void setName(String name) {
        fName = StringUtils.safeString(name);
    }

    @Override
    public String getDescription() {
        return fDescription;
    }
    
    @Override
    public void setDescription(String description) {
        fDescription = StringUtils.safeString(description);
    }
    
    @Override
    public int getThumbnailCount() {
        if(fThumbnailCount == -1) {
            fThumbnailCount = 0;
            
            try {
                for(String s : ZipUtils.getZipFileEntryNames(fFile)) {
                    if(s.endsWith(".png")) { //$NON-NLS-1$
                        fThumbnailCount++;
                    }
                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }
        
        return fThumbnailCount;
    }

    @Override
    public void setKeyThumbnailPath(String path) {
        fKeyThumbnailPath = path;
    }
    
    @Override
    public Image getKeyThumbnail() {
        if(fKeyThumbnailPath != null) {
            try {
                String imageNumber = fKeyThumbnailPath.replaceAll("[^0-9]+", ""); //$NON-NLS-1$ //$NON-NLS-2$
                int index = Integer.parseInt(imageNumber);
                return getThumbnail(index);
            }
            catch(Exception ex) {
            }
        }
        
        return IArchiImages.ImageFactory.getImage(IArchiImages.DEFAULT_MODEL_THUMB);
    }
    
    @Override
    public Image getThumbnail(int index) {
        Image image = fThumbnails.get(index);
        
        if(image == null) {
            image = loadThumbnailImage(getThumbnailEntryName(index));
            if(image != null) {
                fThumbnails.put(index, image);
            }
        }
        
        return image;
    }

    @Override
    public File getFile() {
        return fFile;
    }

    @Override
    public void setFile(File file) throws IOException {
        fFile = file;
        loadManifest(file);
    }

    @Override
    public String getID() {
        if(fID == null) {
            fID = UUID.randomUUID().toString();
        }
        
        return fID;
    }

    @Override
    public void setID(String id) {
        fID = id;
    }
    
    @Override
    public void save() throws IOException {
        if(fFile == null || !fFile.exists()) {
            return;
        }
        
        // Extract zip contents to temp folder
        File tmpFolder = Files.createTempDirectory("architemplate").toFile(); //$NON-NLS-1$
        ZipUtils.unpackZip(fFile, tmpFolder);
        
        // Save this manifest to the temp folder
        JDOMUtils.write2XMLFile(createManifestDocument(), new File(tmpFolder, TemplateManager.ZIP_ENTRY_MANIFEST));
        
        // Create a new zip file from the temp folder
        try(ZipOutputStream zOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(fFile)))) {
            ZipUtils.addFolderToZip(tmpFolder, zOut, null, null);
        }
        
        // Clean up
        FileUtils.deleteFolder(tmpFolder);
    }
    
    @Override
    public String createManifest() throws IOException {
        return JDOMUtils.write2XMLString(createManifestDocument());
    }
    
    private Document createManifestDocument() {
        Document doc = new Document();
        Element root = new Element(ITemplateXMLTags.XML_TEMPLATE_ELEMENT_MANIFEST);
        doc.setRootElement(root);
        
        // Type
        root.setAttribute(ITemplateXMLTags.XML_TEMPLATE_ATTRIBUTE_TYPE, getType());
        
        // Timestamp
        root.setAttribute(ITemplateXMLTags.XML_TEMPLATE_ATTRIBUTE_TIMESTAMP, Long.toString(System.currentTimeMillis()));
        
        // Name
        Element elementName = new Element(ITemplateXMLTags.XML_TEMPLATE_ELEMENT_NAME);
        elementName.setText(getName());
        root.addContent(elementName);
        
        // Description
        Element elementDescription = new Element(ITemplateXMLTags.XML_TEMPLATE_ELEMENT_DESCRIPTION);
        elementDescription.setText(getDescription());
        root.addContent(elementDescription);
        
        // Key thumbnail
        if(fKeyThumbnailPath != null) {
            Element elementKeyThumb = new Element(ITemplateXMLTags.XML_TEMPLATE_ELEMENT_KEY_THUMBNAIL);
            elementKeyThumb.setText(fKeyThumbnailPath);
            root.addContent(elementKeyThumb);
        }

        return doc;
    }
    
    private void loadManifest(File file) throws IOException {
        if(file == null || !file.exists()) {
            throw new IOException(Messages.AbstractTemplate_0);
        }
        
        // Manifest
        String manifest = ZipUtils.extractZipEntry(file, TemplateManager.ZIP_ENTRY_MANIFEST, Charset.forName("UTF-8")); //$NON-NLS-1$
        if(manifest == null) {
            throw new IOException(Messages.AbstractTemplate_2);
        }

        Document doc = null;
        try {
            doc = JDOMUtils.readXMLString(manifest);
        }
        catch(JDOMException ex) {
            throw new IOException(ex);
        }
        
        Element rootElement = doc.getRootElement();

        // Check type, if any
        // If the attribute doesn't exist it was from an older version (before 2.1)
        // Or the manifest was edited in the TemplateManagerDialog and type wasn' saved (before this was fixed)
        String attType = rootElement.getAttributeValue(XML_TEMPLATE_ATTRIBUTE_TYPE);
        if(attType != null && !Objects.equals(getType(), attType)) {
            throw new IOException(Messages.AbstractTemplate_1);
        }

        // Name
        Element nameElement = rootElement.getChild(XML_TEMPLATE_ELEMENT_NAME);
        if(nameElement != null) {
            fName = nameElement.getText();
        }

        // Description
        Element descriptionElement = rootElement.getChild(XML_TEMPLATE_ELEMENT_DESCRIPTION);
        if(nameElement != null) {
            fDescription = descriptionElement.getText();
        }

        // Key thumbnail
        Element keyThumbnailElement = rootElement.getChild(XML_TEMPLATE_ELEMENT_KEY_THUMBNAIL);
        if(keyThumbnailElement != null) {
            fKeyThumbnailPath = keyThumbnailElement.getText();
        }
    }
    
    private Image loadThumbnailImage(String imgName) {
        if(fFile != null && fFile.exists() && imgName != null) {
            try(InputStream stream = ZipUtils.getZipEntryStream(fFile, imgName)) {
                if(stream != null) {
                    return new Image(null, stream);
                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }
        
        return null;
    }

    private String getThumbnailEntryName(int index) {
        return TemplateManager.ZIP_ENTRY_THUMBNAILS + index + ".png"; //$NON-NLS-1$
    }
    
    @Override
    public void dispose() {
        if(fThumbnails != null) {
            for(Image image : fThumbnails.values()) {
                if(image != null && !image.isDisposed()) {
                    image.dispose();
                }
            }
        }
        
        fThumbnails = null;
    }
}
