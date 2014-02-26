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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

import com.archimatetool.editor.diagram.util.DiagramUtils;
import com.archimatetool.editor.ui.ArchimateLabelProvider;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.editor.utils.HTMLUtils;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.INameable;
import com.archimatetool.model.IProperties;
import com.archimatetool.model.IProperty;



/**
 * Export model to HTML report
 * 
 * @author Phillip Beauvoir
 */
public class HTMLReportExporter {
    
    private IArchimateModel fModel;
    
    private File fMainFolder;
    
    private OutputStreamWriter writer;
    
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
        File file = new File(fMainFolder, "report.html"); //$NON-NLS-1$
        writer = new OutputStreamWriter(new FileOutputStream(file), "UTF16"); //$NON-NLS-1$
        
        writeHeader();
        
        writeBusinessElements();
        writeApplicationElements();
        writeTechnologyElements();
        writeMotivationElements();
        writeImplementationMigrationElements();
        writeConnectionElements();
        writeDiagrams();
        
        writeCloser();
        
        writer.close();
        
        return file;
    }
    
    private void writeHeader() throws IOException {
        String s = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n"; //$NON-NLS-1$
        s += "<html>\n";  //$NON-NLS-1$
        s += "<head>\n"; //$NON-NLS-1$
        s += "<title>" + Messages.HTMLReportExporter_0 + "</title>\n"; //$NON-NLS-1$ //$NON-NLS-2$
        
        s += "<style type=\"text/css\">\n"; //$NON-NLS-1$
        s += "table { border-collapse:collapse; }\n"; //$NON-NLS-1$
        s += "table, td, th { border:1px solid black; }\n"; //$NON-NLS-1$
        s += "</style>\n"; //$NON-NLS-1$
        
        s += "</head>\n"; //$NON-NLS-1$
        s += "<body style=\"font-family:Verdana; font-size:10pt;\">\n"; //$NON-NLS-1$
        s += "<h1>" + Messages.HTMLReportExporter_1 + "</h1>\n"; //$NON-NLS-1$ //$NON-NLS-2$
        writer.write(s);
        
        writer.write("<br/>\n"); //$NON-NLS-1$
        writeModelSummary(fModel);
        writer.write("<br/>\n"); //$NON-NLS-1$
    }
    
    private void writeCloser() throws IOException {
        String s = "</body>\n"; //$NON-NLS-1$
        s += "</html>"; //$NON-NLS-1$
        writer.write(s);
    }
    
    private void writeBusinessElements() throws IOException {
        IFolder businessFolder = fModel.getFolder(FolderType.BUSINESS);
        String color = ColorFactory.convertColorToString(ColorFactory.COLOR_BUSINESS);
        
        // Business Actors
        List<EObject> list = new ArrayList<EObject>();
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessActor());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessRole());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessInterface());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessCollaboration());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getLocation());
        writeElements(list, Messages.HTMLReportExporter_2, color);
        
        // Business Functions
        list.clear();
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessFunction());
        writeElements(list, Messages.HTMLReportExporter_3, color);
        
        // Business Information
        list.clear();
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessObject());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getRepresentation());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getMeaning());
        writeElements(list, Messages.HTMLReportExporter_4, color);
        
        // Business Processes
        list.clear();
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessEvent());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessInteraction());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessProcess());
        writeElements(list, Messages.HTMLReportExporter_5, color);
        
        // Business Products
        list.clear();
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getContract());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getProduct());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getBusinessService());
        getElements(businessFolder, list, IArchimatePackage.eINSTANCE.getValue());
        writeElements(list, Messages.HTMLReportExporter_6, color);
    }
    
    private void writeApplicationElements() throws IOException {
        IFolder applicationFolder = fModel.getFolder(FolderType.APPLICATION);
        String color = ColorFactory.convertColorToString(ColorFactory.COLOR_APPLICATION);
        
        // Applications
        List<EObject> list = new ArrayList<EObject>();
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getApplicationCollaboration());
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getApplicationComponent());
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getApplicationFunction());
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getApplicationInteraction());
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getApplicationInterface());
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getApplicationService());
        writeElements(list, Messages.HTMLReportExporter_7, color);
        
        // Application Data
        list.clear();
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getDataObject());
        writeElements(list, Messages.HTMLReportExporter_8, color);
    }
    
    private void writeTechnologyElements() throws IOException {
        IFolder technologyFolder = fModel.getFolder(FolderType.TECHNOLOGY);
        String color = ColorFactory.convertColorToString(ColorFactory.COLOR_TECHNOLOGY);
        
        // Infrastructures
        List<EObject> list = new ArrayList<EObject>();
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getArtifact());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getCommunicationPath());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getDevice());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getNode());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getInfrastructureFunction());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getInfrastructureInterface());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getNetwork());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getInfrastructureService());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getSystemSoftware());
        writeElements(list, Messages.HTMLReportExporter_9, color);
    }
    
    private void writeMotivationElements() throws IOException {
        IFolder motivationFolder = fModel.getFolder(FolderType.MOTIVATION);
        String color = ColorFactory.convertRGBToString(new RGB(220, 235, 235));
        
        List<EObject> list = new ArrayList<EObject>();
        getElements(motivationFolder, list, IArchimatePackage.eINSTANCE.getStakeholder());
        getElements(motivationFolder, list, IArchimatePackage.eINSTANCE.getDriver());
        getElements(motivationFolder, list, IArchimatePackage.eINSTANCE.getAssessment());
        getElements(motivationFolder, list, IArchimatePackage.eINSTANCE.getGoal());
        getElements(motivationFolder, list, IArchimatePackage.eINSTANCE.getPrinciple());
        getElements(motivationFolder, list, IArchimatePackage.eINSTANCE.getRequirement());
        getElements(motivationFolder, list, IArchimatePackage.eINSTANCE.getConstraint());
        writeElements(list, Messages.HTMLReportExporter_10, color);
    }
    
    private void writeImplementationMigrationElements() throws IOException {
        IFolder implmigrationFolder = fModel.getFolder(FolderType.IMPLEMENTATION_MIGRATION);
        String color = ColorFactory.convertRGBToString(new RGB(220, 235, 235));
        
        List<EObject> list = new ArrayList<EObject>();
        getElements(implmigrationFolder, list, IArchimatePackage.eINSTANCE.getWorkPackage());
        getElements(implmigrationFolder, list, IArchimatePackage.eINSTANCE.getDeliverable());
        getElements(implmigrationFolder, list, IArchimatePackage.eINSTANCE.getPlateau());
        getElements(implmigrationFolder, list, IArchimatePackage.eINSTANCE.getGap());
        writeElements(list, Messages.HTMLReportExporter_11, color);
    }
    
    private void writeConnectionElements() throws IOException {
        IFolder connectionsFolder = fModel.getFolder(FolderType.CONNECTORS);
        String color = ColorFactory.convertRGBToString(new RGB(220, 235, 235));
        
        List<EObject> list = new ArrayList<EObject>();
        getElements(connectionsFolder, list, null);
        writeElements(list, Messages.HTMLReportExporter_12, color);
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
    
    private void writeElements(List<EObject> list, String title, String color) throws IOException {
        if(!list.isEmpty()) {
            writer.write("<h2>" + title + "</h2>\n"); //$NON-NLS-1$ //$NON-NLS-2$
            
            // Sort a *copy* of the List
            List<EObject> copy = new ArrayList<EObject>(list);
            sort(copy);
            
            for(EObject object : copy) {
                if(object instanceof IArchimateElement) {
                    writeTableElement((IArchimateElement)object, color);
                    writer.write("<p/>"); //$NON-NLS-1$
                }
            }
            
            writer.write("<br/>"); //$NON-NLS-1$
        }
    }
    
    private void writeModelSummary(IArchimateModel model) throws IOException {
    	writer.write("<table width=\"100%\" border=\"0\">\n"); //$NON-NLS-1$
    	
    	writer.write("<tr bgcolor=\"" + "#F0F0F0" + "\">\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    	String name = fModel.getName();
        if(!StringUtils.isSet(name)) {
        	name = Messages.HTMLReportExporter_13;
        }
        else {
        	name = parseChars(name);
        }
        writer.write("<td width=\"20%\" valign=\"top\">" + Messages.HTMLReportExporter_14 + "</td>\n"); //$NON-NLS-1$ //$NON-NLS-2$
        writer.write("<td width=\"80%\" valign=\"top\">" + name + "</td>\n"); //$NON-NLS-1$ //$NON-NLS-2$
        writer.write("</tr>\n"); //$NON-NLS-1$
        
        writer.write("<tr>\n"); //$NON-NLS-1$
        String date = DateFormat.getDateTimeInstance().format(new Date());
        writer.write("<td valign=\"top\">" + Messages.HTMLReportExporter_15 + "</td>\n"); //$NON-NLS-1$ //$NON-NLS-2$
        writer.write("<td valign=\"top\">" + date + "</td>\n"); //$NON-NLS-1$ //$NON-NLS-2$
        writer.write("</tr>\n"); //$NON-NLS-1$
        
        writer.write("<tr>\n"); //$NON-NLS-1$
        String doc = StringUtils.safeString(model.getPurpose());
        doc = parseCharsAndLinks(doc);
        writer.write("<td valign=\"top\">" + Messages.HTMLReportExporter_16 + "</td>\n"); //$NON-NLS-1$ //$NON-NLS-2$
        writer.write("<td valign=\"top\">" + doc + "</td>\n"); //$NON-NLS-1$ //$NON-NLS-2$
        writer.write("</tr>\n"); //$NON-NLS-1$
        
        writeProperties(model);
        
        writer.write("</table>\n"); //$NON-NLS-1$

    }
    
    private void writeTableElement(IArchimateElement element, String color) throws IOException {
    	writer.write("<table width=\"100%\" border=\"0\">\n"); //$NON-NLS-1$
    	
    	writer.write("<tr bgcolor=\"" + color + "\">\n"); //$NON-NLS-1$  //$NON-NLS-2$
        String name = StringUtils.safeString(element.getName());
        name = parseChars(name);
        writer.write("<td width=\"20%\" valign=\"top\">" + Messages.HTMLReportExporter_17 + "</td>\n"); //$NON-NLS-1$ //$NON-NLS-2$
        writer.write("<td width=\"80%\" valign=\"top\">" + name + "</td>\n"); //$NON-NLS-1$ //$NON-NLS-2$
        writer.write("</tr>\n"); //$NON-NLS-1$
        
        writer.write("<tr>\n"); //$NON-NLS-1$
        String type = ArchimateLabelProvider.INSTANCE.getDefaultName(element.eClass());
        writer.write("<td valign=\"top\">" + Messages.HTMLReportExporter_18 + "</td>\n"); //$NON-NLS-1$ //$NON-NLS-2$
        writer.write("<td valign=\"top\">" + type + "</td>\n"); //$NON-NLS-1$ //$NON-NLS-2$
        writer.write("</tr>\n"); //$NON-NLS-1$
        
        writer.write("<tr>\n"); //$NON-NLS-1$
        String doc = StringUtils.safeString(element.getDocumentation());
        doc = parseCharsAndLinks(doc);
        writer.write("<td valign=\"top\">" + Messages.HTMLReportExporter_19 + "</td>\n"); //$NON-NLS-1$ //$NON-NLS-2$
        writer.write("<td valign=\"top\">" + doc + "</td>\n"); //$NON-NLS-1$ //$NON-NLS-2$
        writer.write("</tr>\n"); //$NON-NLS-1$
        
        writeProperties(element);
        
        writer.write("</table>\n"); //$NON-NLS-1$
    }
    
    private void writeProperties(IProperties element) throws IOException {
    	for(IProperty property : element.getProperties()) {
        	writer.write("<tr>\n"); //$NON-NLS-1$
        	String key = parseChars(property.getKey());
        	writer.write("<td valign=\"top\">" + key + "</td>\n"); //$NON-NLS-1$ //$NON-NLS-2$
        	String value = parseCharsAndLinks(property.getValue());
        	writer.write("<td valign=\"top\">" + value + "</td>\n"); //$NON-NLS-1$ //$NON-NLS-2$
        	writer.write("</tr>\n"); //$NON-NLS-1$
		}
    }
    
    private void writeDiagrams() throws IOException {
        if(fModel.getDiagramModels().isEmpty()) {
            return;
        }
        
        // Sort a *copy* of the List
        List<IDiagramModel> copy = new ArrayList<IDiagramModel>(fModel.getDiagramModels());
        sort(copy);

        Hashtable<IDiagramModel, String> table = saveDiagrams(copy);

        writer.write("<br/><br/><br/>\n"); //$NON-NLS-1$
        writer.write("<h2>" + Messages.HTMLReportExporter_20 + "</h2>\n"); //$NON-NLS-1$ //$NON-NLS-2$

        for(IDiagramModel dm : copy) {
            writer.write("<table width=\"100%\" border=\"0\">\n"); //$NON-NLS-1$

            writer.write("<tr bgcolor=\"" + "#e0e4e6" + "\">\n");  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            String name = StringUtils.safeString(dm.getName());
            name = parseChars(name);
            writer.write("<td width=\"20%\" valign=\"top\">" + Messages.HTMLReportExporter_21 + "</td>\n"); //$NON-NLS-1$ //$NON-NLS-2$
            writer.write("<td width=\"80%\" valign=\"top\">" + name + "</td>\n"); //$NON-NLS-1$ //$NON-NLS-2$
            writer.write("</tr>\n"); //$NON-NLS-1$

            writer.write("<tr>\n"); //$NON-NLS-1$
            String doc = StringUtils.safeString(dm.getDocumentation());
            doc = parseCharsAndLinks(doc);
            writer.write("<td valign=\"top\">" + Messages.HTMLReportExporter_22 + "</td>\n"); //$NON-NLS-1$ //$NON-NLS-2$
            writer.write("<td valign=\"top\">" + doc + "</td>\n"); //$NON-NLS-1$ //$NON-NLS-2$
            writer.write("</tr>\n"); //$NON-NLS-1$

            writeProperties(dm);

            writer.write("</table>\n"); //$NON-NLS-1$

            writer.write("<img src=\"" + table.get(dm) + "\"" + "/>\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            writer.write("<br/><br/><br/><br/>\n"); //$NON-NLS-1$
        }
    }
    
    private Hashtable<IDiagramModel, String> saveDiagrams(List<IDiagramModel> list) {
        Hashtable<IDiagramModel, String> table = new Hashtable<IDiagramModel, String>();
        int i = 1;
        
        for(IDiagramModel dm : list) {
            Image image = DiagramUtils.createImage(dm, 1, 10);
            String diagramName = dm.getName();
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
                File file = new File(fMainFolder, diagramName);
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
        s = s.replaceAll("&", "&amp;"); //$NON-NLS-1$ //$NON-NLS-2$  // This first
        s = s.replaceAll("<", "&lt;"); //$NON-NLS-1$ //$NON-NLS-2$
        s = s.replaceAll(">", "&gt;"); //$NON-NLS-1$ //$NON-NLS-2$
        s = s.replaceAll("\"", "&quot;"); //$NON-NLS-1$ //$NON-NLS-2$
        
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
                s = s.replaceAll(group, "<a href=\"" + group + "\">" + group + "</a>"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
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
