/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.e4.compatibility.CompatibilityEditor;
import org.eclipse.ui.internal.menus.MenuHelper;
import org.eclipse.ui.internal.registry.IWorkbenchRegistryConstants;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * Patches workbench.xmi file to use actual part icons when switching versions
 * 
 * This is adapted from DBeaver
 * https://github.com/dbeaver/dbeaver/blob/devel/plugins/org.jkiss.dbeaver.ui.app.standalone/src/org/jkiss/dbeaver/ui/app/standalone/internal/WorkbenchPatcher.java
 */
@SuppressWarnings({"nls", "restriction"})
public class WorkbenchPatcher {
    
    // Location of workbench.xml file
    private static final String WORKBENCH_FILE = ".metadata/.plugins/org.eclipse.e4.workbench/workbench.xmi";
    
    // If this is set in Program arguments then don't patch
    private static final String NO_PATCH_WORKBENCH = "-noPatchWorkbench";
    
    private record PartDescriptor(IConfigurationElement element, String id, String icon) {
        static PartDescriptor of(IConfigurationElement element) {
            String id = element.getAttribute("id");
            String icon = MenuHelper.getIconURI(element, IWorkbenchRegistryConstants.ATT_ICON);
            return new PartDescriptor(element, id, icon);
        }
    }
    
    /**
     * Patches the workbench.xmi file, updating all view and editor
     * parts' icons to their actual values taken directly from contributed extensions.
     * Does not update the file if no changes were made.
     */
    static void patchWorkbench() {
        // Don't patch if this option is set
        List<String> appArgs = Arrays.asList(Platform.getApplicationArgs());
        if(appArgs.contains(NO_PATCH_WORKBENCH)) {
            return;
        }
        
        File instanceLocationFolder = getLocationAsFile(Platform.getInstanceLocation());
        if(instanceLocationFolder == null) {
            return;
        }

        File xmiFile = new File(instanceLocationFolder, WORKBENCH_FILE);
        if(!xmiFile.exists()) {
            return;
        }
        
        try {
            Document document = parseDocument(xmiFile);
            
            boolean transformed = patchPartIcons(document, getContributedParts());
            if(transformed) {
                document.setXmlStandalone(true);
                
                Path workbenchXmiPatch = xmiFile.toPath().resolveSibling(xmiFile.getName() + ".patch");

                // Write to a temporary file first
                try(OutputStream os = Files.newOutputStream(workbenchXmiPatch)) {
                    DOMSource source = new DOMSource(document);
                    StreamResult result = new StreamResult(os);
                    Transformer transformer = TransformerFactory.newInstance().newTransformer();
                    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                    transformer.transform(source, result);
                }

                // Then replace the original file with the patched one
                Files.move(workbenchXmiPatch, xmiFile.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            }
        }
        catch(Throwable ex) {
            Logger.error("Error patching workbench file: " + xmiFile, ex);
        }
    }
    
    private static boolean patchPartIcons(Node node, Map<String, PartDescriptor> parts) throws IOException, ParserConfigurationException, SAXException {
        NodeList children = node.getChildNodes();
        boolean modified = false;

        for(int i = 0; i < children.getLength(); i++) {
            if(!(children.item(i) instanceof Element child)) {
                continue;
            }

            if(child.hasAttribute("elementId") && child.hasAttribute("iconURI")) {
                Attr iconURI = child.getAttributeNode("iconURI");
                String elementId = child.getAttribute("elementId");

                if(elementId.equals(CompatibilityEditor.MODEL_ELEMENT_ID)) {
                    // CompatibilityEditor is not an editor itself
                    // See org.eclipse.ui.internal.WorkbenchPage.createEditorReferenceForPart
                    elementId = extractCompatibilityEditorId(child);
                }

                PartDescriptor part = parts.get(elementId);
                if(part != null && !iconURI.getNodeValue().equals(part.icon())) {
                    iconURI.setNodeValue(part.icon());
                    modified = true;
                }
            }

            modified |= patchPartIcons(child, parts);
        }

        return modified;
    }
    
    private static Map<String, PartDescriptor> getContributedParts() {
         List<PartDescriptor> views = getExtensions(PlatformUI.PLUGIN_ID, IWorkbenchRegistryConstants.PL_VIEWS)
            .map(IExtension::getConfigurationElements).flatMap(Stream::of)
            .filter(e -> e.getName().equals("view") && e.getAttribute("icon") != null)
            .map(PartDescriptor::of)
            .toList();

        List<PartDescriptor> editors = getExtensions(PlatformUI.PLUGIN_ID, IWorkbenchRegistryConstants.PL_EDITOR)
            .map(IExtension::getConfigurationElements).flatMap(Stream::of)
            .filter(e -> e.getName().equals("editor") && e.getAttribute("icon") != null)
            .map(PartDescriptor::of)
            .toList();

        return Stream.concat(views.stream(), editors.stream())
            .collect(Collectors.toMap(
                PartDescriptor::id,
                Function.identity(),
                (a, b) -> b
            ));
    }
    
    private static Stream<IExtension> getExtensions(String namespace, String extensionPointName) {
        IExtensionRegistry registry = Platform.getExtensionRegistry();
        IExtensionPoint point = registry.getExtensionPoint(namespace, extensionPointName);
        return Arrays.stream(point.getExtensions());
    }
    
    private static Document parseDocument(File file) throws IOException, ParserConfigurationException, SAXException {
        try(InputStream is = new FileInputStream(file)) {
            return parseDocument(new InputSource(is));
        }
    }
    
    private static Document parseDocument(Reader is) throws IOException, ParserConfigurationException, SAXException {
        return parseDocument(new InputSource(is));
    }
    
    private static Document parseDocument(InputSource source) throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        DocumentBuilder xmlBuilder = dbf.newDocumentBuilder();
        return xmlBuilder.parse(source);
    }
    
    private static String extractCompatibilityEditorId(Element element) throws IOException, ParserConfigurationException, SAXException {
        // For explanation behind this logic, see org.eclipse.ui.internal.EditorReference#EditorReference

        Element persistedState = getChildElement(element, "persistedState");
        if(persistedState == null) {
            return null;
        }

        String key = persistedState.getAttribute("key");
        String value = persistedState.getAttribute("value");
        if(!"memento".equals(key)) {
            return null;
        }

        Document memento = parseDocument(new StringReader(value));
        Element editor = memento.getDocumentElement();
        if(editor.getTagName().equals("editor") && editor.hasAttribute("id")) {
            return editor.getAttribute("id");
        }

        return null;
    }
    
    private static Element getChildElement(Element element, String childName) {
        if(element == null) {
            return null;
        }
        
        for(Node node = element.getFirstChild(); node != null; node = node.getNextSibling()) {
            if(node.getNodeType() == Node.ELEMENT_NODE && ((Element)node).getTagName().equals(childName)) {
                return (Element)node;
            }
        }
        
        return null;
    }
    
    private static File getLocationAsFile(Location location) {
        if(location == null || location.getURL() == null) {
            return null;
        }
        
        return new File(location.getURL().getPath());
    }
}
