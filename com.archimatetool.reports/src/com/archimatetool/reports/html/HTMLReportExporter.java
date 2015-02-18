/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.reports.html;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import com.archimatetool.editor.diagram.util.DiagramUtils;
import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.editor.utils.HTMLUtils;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateComponent;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.INameable;
import com.archimatetool.model.impl.ArchimateComponent;


/**
 * Export model to HTML report
 * 
 * @author Phillip Beauvoir
 */
public class HTMLReportExporter extends AbstractUIPlugin {
    
    private IArchimateModel fModel;
    private File fMainFolder;
    private File fElementsFolder;
    private File fViewsFolder;
    private File fImagesFolder;
    //
    OutputStreamWriter writer;
    
    // Templates
    STGroupFile groupe;
    ST model, frame;
    
    public void export(IArchimateModel model) throws IOException {
        fMainFolder = askSaveFolder();
        if(fMainFolder == null) {
            return;
        }
        
        fModel = model;
        
        File file = createMainHTMLPage();
        
        // Open it in Browser
        IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
        try {
            IWebBrowser browser = support.getExternalBrowser();
            browser.openURL(file.toURI().toURL());
        }
        catch(PartInitException ex) {
            ex.printStackTrace();
        }
    }
    
    private File createMainHTMLPage() throws IOException {
    	// Instantiate templates files
    	groupe = new STGroupFile(getTemplatesFolder().getAbsolutePath()+File.separator+"st"+File.separator+"main.stg", '^', '^');
    	frame = groupe.getInstanceOf("frame");
    	model = groupe.getInstanceOf("modelreport");
        
        // Copy HTML skeleton to target
        File srcDir = new File(getTemplatesFolder(), "html");
        FileUtils.copyFolder(srcDir, fMainFolder);
        
        // Set folders
        fElementsFolder = new File(fMainFolder, "elements");
        fViewsFolder = new File(fMainFolder, "views");
        fImagesFolder = new File(fMainFolder, "images");
        
        // Create model.html
        writeElement(fModel, new File(fViewsFolder, "model.html")); //$NON-NLS-1$
        
        // Lists of elements
        List<EObject> businessList = new ArrayList<EObject>();
        List<EObject> applicationList = new ArrayList<EObject>();
        List<EObject> technologyList = new ArrayList<EObject>();
        List<EObject> motivationList = new ArrayList<EObject>();
        List<EObject> implementationMigrationList = new ArrayList<EObject>();
        List<EObject> connectorList = new ArrayList<EObject>();
        List<IDiagramModel> viewList = fModel.getDiagramModels();
        
        // Get elements
        getBusinessElements(businessList);
        getApplicationElements(applicationList);
        getTechnologyElements(technologyList);
        getMotivationElements(motivationList);
        getImplementationMigrationElements(implementationMigrationList);
        getConnectorElements(connectorList);
        
        // write (elements).html
        writeElements(businessList);
        writeElements(applicationList);
        writeElements(technologyList);
        //writeElements(motivationList);
        //writeElements(implementationMigrationList);
        //writeElements(connectorList);
        writeDiagrams();
        
        // Write root model.html
        File modeltreeF = new File(fMainFolder, "model.html"); //$NON-NLS-1$
        OutputStreamWriter modeltreeW = new OutputStreamWriter(new FileOutputStream(modeltreeF), "UTF8"); //$NON-NLS-1$
        // model, businessFolder, applicationFolder, technologyFolder, motivationFolder
        // implementationFolder, connectorsFolder, relationsFolder, viewsFolder
        model.add("model", fModel);
        model.add("businessFolder", fModel.getFolder(FolderType.BUSINESS));
        model.add("applicationFolder", fModel.getFolder(FolderType.APPLICATION));
        model.add("technologyFolder", fModel.getFolder(FolderType.TECHNOLOGY));
        model.add("motivationFolder", fModel.getFolder(FolderType.MOTIVATION));
        model.add("implementationFolder", fModel.getFolder(FolderType.IMPLEMENTATION_MIGRATION));
        model.add("connectorsFolder", fModel.getFolder(FolderType.CONNECTORS));
        model.add("relationsFolder", fModel.getFolder(FolderType.RELATIONS));
        model.add("viewsFolder", fModel.getFolder(FolderType.DIAGRAMS));
        modeltreeW.write(model.render()); //$NON-NLS-1$
        modeltreeW.close();
        
        return new File(fMainFolder, "model.html");
    }
    
    public File getTemplatesFolder() {
        URL url = FileLocator.find(getBundle(), new Path("/templates"), null); //$NON-NLS-1$
        try {
            url = FileLocator.resolve(url);
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        return new File(url.getPath()); 
    }
        
    private void getBusinessElements(List<EObject> list) throws IOException {
        IFolder businessFolder = fModel.getFolder(FolderType.BUSINESS);
        //String color = ColorFactory.convertColorToString(ColorFactory.COLOR_BUSINESS);
        //List<EObject> list = new ArrayList<EObject>();
        
        // Business Actors
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessActor());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessRole());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessInterface());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessCollaboration());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getLocation());
        // Business Functions
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessFunction());
        // Business Information
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessObject());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getRepresentation());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getMeaning());
        // Business Processes
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessEvent());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessInteraction());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessProcess());
        // Business Products
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getContract());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getProduct());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessService());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getValue());
        
        //writeElements(list, Messages.HTMLReportExporter_6, color);
    }
    
    private void getApplicationElements(List<EObject> list) throws IOException {
        IFolder applicationFolder = fModel.getFolder(FolderType.APPLICATION);
        //String color = ColorFactory.convertColorToString(ColorFactory.COLOR_APPLICATION);
        
        // Applications
        //List<EObject> list = new ArrayList<EObject>();
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getApplicationCollaboration());
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getApplicationComponent());
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getApplicationFunction());
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getApplicationInteraction());
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getApplicationInterface());
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getApplicationService());
        //writeElements(list, Messages.HTMLReportExporter_7, color);
        
        // Application Data
        //list.clear();
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getDataObject());
        //writeElements(list, Messages.HTMLReportExporter_8, color);
    }
    
    private void getTechnologyElements(List<EObject> list) throws IOException {
        IFolder technologyFolder = fModel.getFolder(FolderType.TECHNOLOGY);
        //String color = ColorFactory.convertColorToString(ColorFactory.COLOR_TECHNOLOGY);
        
        // Infrastructures
        //List<EObject> list = new ArrayList<EObject>();
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getArtifact());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getCommunicationPath());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getDevice());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getNode());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getInfrastructureFunction());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getInfrastructureInterface());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getNetwork());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getInfrastructureService());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getSystemSoftware());
        //writeElements(list, Messages.HTMLReportExporter_9, color);
    }
    
    private void getMotivationElements(List<EObject> list) throws IOException {
        IFolder motivationFolder = fModel.getFolder(FolderType.MOTIVATION);
        //String color = ColorFactory.convertRGBToString(new RGB(220, 235, 235));
        
        //List<EObject> list = new ArrayList<EObject>();
        getElements(motivationFolder, list, IArchimatePackage.eINSTANCE.getStakeholder());
        getElements(motivationFolder, list, IArchimatePackage.eINSTANCE.getDriver());
        getElements(motivationFolder, list, IArchimatePackage.eINSTANCE.getAssessment());
        getElements(motivationFolder, list, IArchimatePackage.eINSTANCE.getGoal());
        getElements(motivationFolder, list, IArchimatePackage.eINSTANCE.getPrinciple());
        getElements(motivationFolder, list, IArchimatePackage.eINSTANCE.getRequirement());
        getElements(motivationFolder, list, IArchimatePackage.eINSTANCE.getConstraint());
        //writeElements(list, Messages.HTMLReportExporter_10, color);
    }
    
    private void getImplementationMigrationElements(List<EObject> list) throws IOException {
        IFolder implmigrationFolder = fModel.getFolder(FolderType.IMPLEMENTATION_MIGRATION);
        //String color = ColorFactory.convertRGBToString(new RGB(220, 235, 235));
        
        //List<EObject> list = new ArrayList<EObject>();
        getElements(implmigrationFolder, list, IArchimatePackage.eINSTANCE.getWorkPackage());
        getElements(implmigrationFolder, list, IArchimatePackage.eINSTANCE.getDeliverable());
        getElements(implmigrationFolder, list, IArchimatePackage.eINSTANCE.getPlateau());
        getElements(implmigrationFolder, list, IArchimatePackage.eINSTANCE.getGap());
        //writeElements(list, Messages.HTMLReportExporter_11, color);
    }
    
    private void getConnectorElements(List<EObject> list) throws IOException {
        IFolder connectionsFolder = fModel.getFolder(FolderType.CONNECTORS);
        //String color = ColorFactory.convertRGBToString(new RGB(220, 235, 235));
        
        //List<EObject> list = new ArrayList<EObject>();
        getElements(connectionsFolder, list, null);
        //writeElements(list, Messages.HTMLReportExporter_12, color);
    }
    
    private void getElements(IFolder folder, List<EObject> list, EClass type) {
        for(EObject object : folder.getElements()) {
        	if(type == null) {
                list.add(object);
            }
        	else if(object.eClass() == type) {
                list.add(object);
            }
        }
        
        for(IFolder f : folder.getFolders()) {
            getElements(f, list, type);
        }
    }
    
    /* private void writeElements(List<EObject> list, String title, String color) throws IOException {
        if(!list.isEmpty()) {
            writer.write("<h2>" + title + "</h2>\n"); //$NON-NLS-1$ //$NON-NLS-2$
            
            // Sort a *copy* of the List
            List<EObject> copy = new ArrayList<EObject>(list);
            sort(copy);
            
            for(EObject object : copy) {
                if(object instanceof IArchimateComponent) {
                    writeTableElement((IArchimateComponent)object, color);
                    writer.write("<p/>"); //$NON-NLS-1$
                }
            }
            
            writer.write("<br/>"); //$NON-NLS-1$
        }
    } */
    
    private void writeElements(List<EObject> list) throws IOException {
        if(!list.isEmpty()) {
            for(EObject object : list) {
                if(object instanceof IArchimateComponent) {
                	writeElement(object, new File(fElementsFolder, ((ArchimateComponent) object).getId()+".html")); //$NON-NLS-1$
                }
            }
        }
    }
    
    private void writeElement(EObject component, File elementF) throws IOException {
        //File elementF = new File(fElementsFolder, ((ArchimateComponent) object).getId()+".html"); //$NON-NLS-1$
        OutputStreamWriter elementW = new OutputStreamWriter(new FileOutputStream(elementF), "UTF8"); //$NON-NLS-1$
        frame.remove("element");
        //frame.remove("children");
        frame.add("element", component);
        elementW.write(frame.render()); //$NON-NLS-1$
        elementW.close();
    }
    
    private void writeDiagrams() throws IOException {
        if(fModel.getDiagramModels().isEmpty()) {
            return;
        }
        
        // Sort a *copy* of the List
        List<IDiagramModel> copy = new ArrayList<IDiagramModel>(fModel.getDiagramModels());
        sort(copy);

        Hashtable<IDiagramModel, String> table = saveDiagrams(copy);

        for(IDiagramModel dm : copy) {
        	//Name
            //String name = StringUtils.safeString(dm.getName());
            //name = parseChars(name);
            //Doc
            //String doc = StringUtils.safeString(dm.getDocumentation());
            //doc = parseCharsAndLinks(doc);
            // Image
            //writer.write("<img src=\"" + table.get(dm) + "\"" + "/>\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            
            File viewF = new File(fViewsFolder, dm.getId()+".html");
            OutputStreamWriter viewW = new OutputStreamWriter(new FileOutputStream(viewF), "UTF8"); //$NON-NLS-1$
            frame.remove("element");
            frame.remove("children");
            frame.add("element", dm);
            List<IArchimateElement> fChildren = new ArrayList<IArchimateElement>();
            getAllChildObjects(dm, fChildren);
            frame.add("children", fChildren);
            viewW.write(frame.render()); //$NON-NLS-1$
            viewW.close();
        }
    }
    
    private void getAllChildObjects(IDiagramModelContainer container, List<IArchimateElement> fChildren) {
        for(IDiagramModelObject child : container.getChildren()) {
            if(child instanceof IDiagramModelArchimateObject) {
                IArchimateElement element = ((IDiagramModelArchimateObject)child).getArchimateElement();
                if(element != null && !fChildren.contains(element)) {
                    fChildren.add(element);
                }
            }
            
            if(child instanceof IDiagramModelContainer) {
                getAllChildObjects((IDiagramModelContainer)child, fChildren);
            }
        }
    }
    
    private Hashtable<IDiagramModel, String> saveDiagrams(List<IDiagramModel> list) {
        Hashtable<IDiagramModel, String> table = new Hashtable<IDiagramModel, String>();
        int i = 1;
        
        for(IDiagramModel dm : list) {
            Image image = DiagramUtils.createImage(dm, 1, 10);
            String diagramName = dm.getId();
            if(StringUtils.isSet(diagramName)) {
                diagramName = FileUtils.getValidFileName(diagramName);
                int j = 2;
                String s = diagramName + ".png";  //$NON-NLS-1$
                while(table.containsValue(s)) {
                    s = diagramName + "_" + j++ + ".png"; //$NON-NLS-1$ //$NON-NLS-2$
                }
                diagramName = s;
            }
            else {
                diagramName = Messages.HTMLReportExporter_23 + " " + i++ + ".png";  //$NON-NLS-1$//$NON-NLS-2$
            }

            table.put(dm, diagramName);

            try {
                ImageLoader loader = new ImageLoader();
                loader.data = new ImageData[] { image.getImageData() };
                File file = new File(fImagesFolder, diagramName);
                loader.save(file.getAbsolutePath(), SWT.IMAGE_PNG);
            }
            finally {
                image.dispose();
            }
        }
        
        return table;
    }
    
    private File askSaveFolder() {
        DirectoryDialog dialog = new DirectoryDialog(Display.getCurrent().getActiveShell());
        dialog.setText(Messages.HTMLReportExporter_24);
        dialog.setMessage(Messages.HTMLReportExporter_25);
        String path = dialog.open();
        if(path == null) {
            return null;
        }
        
        File folder = new File(path);
        if(folder.exists()) {
            String[] children = folder.list();
            if(children != null && children.length > 0) {
                boolean result = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
                        Messages.HTMLReportExporter_26,
                        NLS.bind(Messages.HTMLReportExporter_27, folder));
                if(!result) {
                    return null;
                }
            }
        }
        else {
            folder.mkdirs();
        }
        
        return folder;
    }
    
    private String parseCharsAndLinks(String s) {
        s = parseChars(s);
        s = parseLinks(s);
        return s;
    }
    
    private String parseChars(String s) {
        // Escape chars
        s = s.replace("&", "&amp;"); //$NON-NLS-1$ //$NON-NLS-2$  // This first
        s = s.replace("<", "&lt;"); //$NON-NLS-1$ //$NON-NLS-2$
        s = s.replace(">", "&gt;"); //$NON-NLS-1$ //$NON-NLS-2$
        s = s.replace("\"", "&quot;"); //$NON-NLS-1$ //$NON-NLS-2$
        
        // CRs become breaks
        s = s.replaceAll("(\r\n|\r|\n)", "<br/>");  //$NON-NLS-1$ //$NON-NLS-2$ // This last
        
        return s;
    }
    
    private String parseLinks(String s) {
        Matcher matcher = HTMLUtils.HTML_LINK_PATTERN.matcher(s);
        List<String> done = new ArrayList<String>();
        
        while(matcher.find()) {
            String group = matcher.group();
            if(!done.contains(group)) {
                done.add(group);
                s = s.replace(group, "<a href=\"" + group + "\">" + group + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
        }
        
        return s;
    }
    
    /**
     * Sort a *copy* of the List
     */
    private void sort(List<?> list) {
        Collections.sort(list, new Comparator<Object>() {
            @Override
            public int compare(Object o1, Object o2) {
                if(o1 instanceof INameable && o2 instanceof INameable) {
                    String name1 = StringUtils.safeString(((INameable)o1).getName()).toLowerCase().trim();
                    String name2 = StringUtils.safeString(((INameable)o2).getName()).toLowerCase().trim();
                    return name1.compareTo(name2);
                }
                return 0;
            }
        });
    }
}
