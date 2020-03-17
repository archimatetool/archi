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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.jdom2.Document;
import org.jdom2.Element;

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
@SuppressWarnings("nls")
public abstract class AbstractTemplate implements ITemplate, ITemplateXMLTags {
    
    private String fID;
    private String fName;
    private String fDescription;
    private File fFile;
    
    private boolean fManifestLoaded;
    
    private String fKeyThumbnailPath;
    
    private Map<Integer, Image> fThumbnails = new HashMap<>();
    private int fThumbnailCount = -1;
    
    public AbstractTemplate() {
    }

    /**
     * @param id If this is null a new id will be generated
     */
    public AbstractTemplate(String id) {
        if(id == null) {
            id = UUID.randomUUID().toString();
        }
        fID = id;
    }

    @Override
    public String getName() {
        if(!fManifestLoaded) {
            loadManifest();
        }
        return fName;
    }
    
    @Override
    public void setName(String name) {
        fName = StringUtils.safeString(name);
    }

    @Override
    public String getDescription() {
        if(!fManifestLoaded) {
            loadManifest();
        }
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
                    if(s.endsWith(".png")) {
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
    public Image getKeyThumbnail() {
        if(!fManifestLoaded) {
            loadManifest();
        }
        
        if(fKeyThumbnailPath != null) {
            try {
                String imageNumber = fKeyThumbnailPath.replaceAll("[^0-9]+", "");
                int index = Integer.parseInt(imageNumber);
                return getThumbnail(index - 1);
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
            image = loadImage(getThumbnailEntryName(index));
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
    public void setFile(File file) {
        fFile = file;
    }

    @Override
    public String getID() {
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
        
        // Create Manifest as JDOM
        Document doc = new Document();
        Element root = new Element(ITemplateXMLTags.XML_TEMPLATE_ELEMENT_MANIFEST);
        doc.setRootElement(root);
        
        Element elementName = new Element(ITemplateXMLTags.XML_TEMPLATE_ELEMENT_NAME);
        elementName.setText(getName());
        root.addContent(elementName);
        
        Element elementDescription = new Element(ITemplateXMLTags.XML_TEMPLATE_ELEMENT_DESCRIPTION);
        elementDescription.setText(getDescription());
        root.addContent(elementDescription);
        
        if(fKeyThumbnailPath != null) {
            Element elementKeyThumb = new Element(ITemplateXMLTags.XML_TEMPLATE_ELEMENT_KEY_THUMBNAIL);
            elementKeyThumb.setText(fKeyThumbnailPath);
            root.addContent(elementKeyThumb);
        }
        
        // Open a zip stream
        File tmpFile = File.createTempFile("architemplate", null);
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(tmpFile));
        ZipOutputStream zOut = new ZipOutputStream(out);
        
        // Add Manifest
        String manifest = JDOMUtils.write2XMLString(doc);
        ZipUtils.addStringToZip(manifest, TemplateManager.ZIP_ENTRY_MANIFEST, zOut, Charset.forName("UTF-8"));
        
        // Add Model
        // Save to temporary file rather than string because the actual encoding can either be ANSI or UTF-8 depending on content
        File modelFile = ZipUtils.extractZipEntry(fFile, TemplateManager.ZIP_ENTRY_MODEL, File.createTempFile("archi", null));
        ZipUtils.addFileToZip(modelFile, TemplateManager.ZIP_ENTRY_MODEL, zOut);
        modelFile.delete();

        // Thumbnails
        for(int i = 0; i < getThumbnailCount(); i++) {
            Image image = getThumbnail(i);
            if(image != null) {
                ZipUtils.addImageToZip(image, getThumbnailEntryName(i), zOut, SWT.IMAGE_PNG, null);
            }
        }
        
        zOut.flush();
        zOut.close();

        // Delete and copy
        fFile.delete();
        FileUtils.copyFile(tmpFile, fFile, false);
        tmpFile.delete();
    }
    
    private void loadManifest() {
        // Default first
        fManifestLoaded = true;
        fName = "";
        fDescription = "";
        
        if(fFile != null && fFile.exists()) {
            try {
                // Manifest
                String manifest = ZipUtils.extractZipEntry(fFile, TemplateManager.ZIP_ENTRY_MANIFEST, Charset.forName("UTF-8"));
                if(manifest != null) {
                    Document doc = JDOMUtils.readXMLString(manifest);
                    Element rootElement = doc.getRootElement();
                    
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
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    private Image loadImage(String imgName) {
        Image image = null;
        
        if(fFile != null && fFile.exists() && imgName != null) {
            InputStream stream = null;
            try {
                stream = ZipUtils.getZipEntryStream(fFile, imgName);
                if(stream != null) {
                    image = new Image(null, stream);
                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
            finally {
                try {
                    if(stream != null) {
                        stream.close();
                    }
                }
                catch(IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        return image;
    }

    private String getThumbnailEntryName(int index) {
        return TemplateManager.ZIP_ENTRY_THUMBNAILS + (index + 1) + ".png";
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
