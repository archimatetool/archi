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

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
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
import org.osgi.framework.Bundle;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import com.archimatetool.editor.diagram.util.DiagramUtils;
import com.archimatetool.editor.utils.FileUtils;
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
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.INameable;
import com.archimatetool.reports.ArchimateEditorReportsPlugin;


/**
 * Export model to HTML report
 * 
 * @author Jean-Baptiste Sarrodie
 * @author Quentin Varquet
 * @author Phillip Beauvoir
 */
public class HTMLReportExporter extends AbstractUIPlugin {
    
    private IArchimateModel fModel;
    private File fMainFolder;
    private File fElementsFolder;
    private File fViewsFolder;
    private File fImagesFolder;

    // Templates
    private STGroupFile groupe;
    private ST model, frame;
    
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
        File mainFile = new File(ArchimateEditorReportsPlugin.INSTANCE.getTemplatesFolder(), "st/main.stg"); //$NON-NLS-1$
    	groupe = new STGroupFile(mainFile.getAbsolutePath(), '^', '^');
    	frame = groupe.getInstanceOf("frame"); //$NON-NLS-1$
    	model = groupe.getInstanceOf("modelreport"); //$NON-NLS-1$
        
        // Copy HTML skeleton to target
        File srcDir = new File(ArchimateEditorReportsPlugin.INSTANCE.getTemplatesFolder(), "html"); //$NON-NLS-1$
        FileUtils.copyFolder(srcDir, fMainFolder);
        
        // Copy hints files from the help plug-in
        Bundle bundle = Platform.getBundle("com.archimatetool.help"); //$NON-NLS-1$
        URL url = FileLocator.resolve(bundle.getEntry("hints")); //$NON-NLS-1$
        FileUtils.copyFolder(new File(url.getPath()), new File(fMainFolder, "hints")); //$NON-NLS-1$
        
        // Set folders
        fElementsFolder = new File(fMainFolder, "elements"); //$NON-NLS-1$
        fElementsFolder.mkdirs(); // Make dir
        fViewsFolder = new File(fMainFolder, "views"); //$NON-NLS-1$
        fViewsFolder.mkdirs(); // Make dir
        fImagesFolder = new File(fMainFolder, "images"); //$NON-NLS-1$
        fImagesFolder.mkdirs(); // Make dir
        
        // Create model.html
        writeElement(fModel, new File(fViewsFolder, "model.html")); //$NON-NLS-1$
        
        // Lists of elements
        List<EObject> businessList = new ArrayList<EObject>();
        List<EObject> applicationList = new ArrayList<EObject>();
        List<EObject> technologyList = new ArrayList<EObject>();
        List<EObject> motivationList = new ArrayList<EObject>();
        List<EObject> implementationMigrationList = new ArrayList<EObject>();
        List<EObject> connectorList = new ArrayList<EObject>();
        
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
        writeElements(motivationList);
        writeElements(implementationMigrationList);
        writeElements(connectorList);
        writeDiagrams();
        
        // Write root model.html
        File modeltreeF = new File(fMainFolder, "model.html"); //$NON-NLS-1$
        OutputStreamWriter modeltreeW = new OutputStreamWriter(new FileOutputStream(modeltreeF), "UTF8"); //$NON-NLS-1$

        model.add("model", fModel); //$NON-NLS-1$
        model.add("businessFolder", fModel.getFolder(FolderType.BUSINESS)); //$NON-NLS-1$
        model.add("applicationFolder", fModel.getFolder(FolderType.APPLICATION)); //$NON-NLS-1$
        model.add("technologyFolder", fModel.getFolder(FolderType.TECHNOLOGY)); //$NON-NLS-1$
        model.add("motivationFolder", fModel.getFolder(FolderType.MOTIVATION)); //$NON-NLS-1$
        model.add("implementationFolder", fModel.getFolder(FolderType.IMPLEMENTATION_MIGRATION)); //$NON-NLS-1$
        model.add("connectorsFolder", fModel.getFolder(FolderType.CONNECTORS)); //$NON-NLS-1$
        model.add("relationsFolder", fModel.getFolder(FolderType.RELATIONS)); //$NON-NLS-1$
        model.add("viewsFolder", fModel.getFolder(FolderType.DIAGRAMS)); //$NON-NLS-1$
        
        modeltreeW.write(model.render());
        modeltreeW.close();
        
        return new File(fMainFolder, "model.html");  //$NON-NLS-1$
    }
    
    private void getBusinessElements(List<EObject> list) {
        IFolder businessFolder = fModel.getFolder(FolderType.BUSINESS);
        
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
    }
    
    private void getApplicationElements(List<EObject> list) {
        IFolder applicationFolder = fModel.getFolder(FolderType.APPLICATION);
        
        // Applications
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getApplicationCollaboration());
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getApplicationComponent());
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getApplicationFunction());
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getApplicationInteraction());
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getApplicationInterface());
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getApplicationService());
        
        // Application Data
        getElements(applicationFolder, list, IArchimatePackage.eINSTANCE.getDataObject());
    }
    
    private void getTechnologyElements(List<EObject> list) {
        IFolder technologyFolder = fModel.getFolder(FolderType.TECHNOLOGY);
        
        // Infrastructures
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getArtifact());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getCommunicationPath());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getDevice());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getNode());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getInfrastructureFunction());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getInfrastructureInterface());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getNetwork());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getInfrastructureService());
        getElements(technologyFolder, list, IArchimatePackage.eINSTANCE.getSystemSoftware());
    }
    
    private void getMotivationElements(List<EObject> list) {
        IFolder motivationFolder = fModel.getFolder(FolderType.MOTIVATION);
        
        getElements(motivationFolder, list, IArchimatePackage.eINSTANCE.getStakeholder());
        getElements(motivationFolder, list, IArchimatePackage.eINSTANCE.getDriver());
        getElements(motivationFolder, list, IArchimatePackage.eINSTANCE.getAssessment());
        getElements(motivationFolder, list, IArchimatePackage.eINSTANCE.getGoal());
        getElements(motivationFolder, list, IArchimatePackage.eINSTANCE.getPrinciple());
        getElements(motivationFolder, list, IArchimatePackage.eINSTANCE.getRequirement());
        getElements(motivationFolder, list, IArchimatePackage.eINSTANCE.getConstraint());
    }
    
    private void getImplementationMigrationElements(List<EObject> list) {
        IFolder implmigrationFolder = fModel.getFolder(FolderType.IMPLEMENTATION_MIGRATION);
        
        getElements(implmigrationFolder, list, IArchimatePackage.eINSTANCE.getWorkPackage());
        getElements(implmigrationFolder, list, IArchimatePackage.eINSTANCE.getDeliverable());
        getElements(implmigrationFolder, list, IArchimatePackage.eINSTANCE.getPlateau());
        getElements(implmigrationFolder, list, IArchimatePackage.eINSTANCE.getGap());
    }
    
    private void getConnectorElements(List<EObject> list) {
        IFolder connectionsFolder = fModel.getFolder(FolderType.CONNECTORS);
        getElements(connectionsFolder, list, null);
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
    
    private void writeElements(List<EObject> list) throws IOException {
        if(!list.isEmpty()) {
            for(EObject object : list) {
                if(object instanceof IArchimateComponent) {
                	writeElement(object, new File(fElementsFolder, ((IIdentifier) object).getId() + ".html")); //$NON-NLS-1$
                }
            }
        }
    }
    
    private void writeElement(EObject component, File elementF) throws IOException {
        OutputStreamWriter elementW = new OutputStreamWriter(new FileOutputStream(elementF), "UTF8"); //$NON-NLS-1$
        frame.remove("element"); //$NON-NLS-1$
        //frame.remove("children");
        frame.add("element", component); //$NON-NLS-1$
        elementW.write(frame.render());
        elementW.close();
    }
    
    private void writeDiagrams() throws IOException {
        if(fModel.getDiagramModels().isEmpty()) {
            return;
        }
        
        // Sort a *copy* of the List
        List<IDiagramModel> copy = new ArrayList<IDiagramModel>(fModel.getDiagramModels());
        sort(copy);

        saveDiagrams(copy);

        for(IDiagramModel dm : copy) {
            File viewF = new File(fViewsFolder, dm.getId() + ".html"); //$NON-NLS-1$
            OutputStreamWriter viewW = new OutputStreamWriter(new FileOutputStream(viewF), "UTF8"); //$NON-NLS-1$
            frame.remove("element"); //$NON-NLS-1$
            frame.remove("children"); //$NON-NLS-1$
            frame.add("element", dm); //$NON-NLS-1$
            List<IArchimateElement> fChildren = new ArrayList<IArchimateElement>();
            getAllChildObjects(dm, fChildren);
            frame.add("children", fChildren); //$NON-NLS-1$
            viewW.write(frame.render());
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
                diagramName = Messages.HTMLReportExporter_1 + " " + i++ + ".png";  //$NON-NLS-1$//$NON-NLS-2$
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
        dialog.setText(Messages.HTMLReportExporter_2);
        dialog.setMessage(Messages.HTMLReportExporter_3);
        String path = dialog.open();
        if(path == null) {
            return null;
        }
        
        File folder = new File(path);
        if(folder.exists()) {
            String[] children = folder.list();
            if(children != null && children.length > 0) {
                boolean result = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
                        Messages.HTMLReportExporter_4,
                        NLS.bind(Messages.HTMLReportExporter_5, folder));
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
