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
import java.util.Hashtable;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.EList;
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
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IIdentifier;
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
    private ST stFrame;
    
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
    	
        STGroupFile groupFile = new STGroupFile(mainFile.getAbsolutePath(), '^', '^');
        stFrame = groupFile.getInstanceOf("frame"); //$NON-NLS-1$
    	ST stModel = groupFile.getInstanceOf("modelreport"); //$NON-NLS-1$
        
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
             
        // write (elements).html
        writeElement(fModel, new File(fViewsFolder, "model.html")); //$NON-NLS-1$
        writeFolders(fModel.getFolders());
        
        // write (diagrams).html
        writeDiagrams();
        
        // Write root model.html
        File modeltreeF = new File(fMainFolder, "model.html"); //$NON-NLS-1$
        OutputStreamWriter modeltreeW = new OutputStreamWriter(new FileOutputStream(modeltreeF), "UTF8"); //$NON-NLS-1$
        stModel.add("model", fModel); //$NON-NLS-1$
        stModel.add("businessFolder", fModel.getFolder(FolderType.BUSINESS)); //$NON-NLS-1$
        stModel.add("applicationFolder", fModel.getFolder(FolderType.APPLICATION)); //$NON-NLS-1$
        stModel.add("technologyFolder", fModel.getFolder(FolderType.TECHNOLOGY)); //$NON-NLS-1$
        stModel.add("motivationFolder", fModel.getFolder(FolderType.MOTIVATION)); //$NON-NLS-1$
        stModel.add("implementationFolder", fModel.getFolder(FolderType.IMPLEMENTATION_MIGRATION)); //$NON-NLS-1$
        stModel.add("connectorsFolder", fModel.getFolder(FolderType.CONNECTORS)); //$NON-NLS-1$
        stModel.add("relationsFolder", fModel.getFolder(FolderType.RELATIONS)); //$NON-NLS-1$
        stModel.add("viewsFolder", fModel.getFolder(FolderType.DIAGRAMS)); //$NON-NLS-1$
        modeltreeW.write(stModel.render());
        modeltreeW.close();
        
        return new File(fMainFolder, "model.html");  //$NON-NLS-1$
    }
    
    private void writeFolders(EList<IFolder> folders) throws IOException {
    	for(IFolder folder : folders) {
    		writeFolder(folder);
    	}
    }
    
    private void writeFolder(IFolder folder) throws IOException {
    	writeElements(folder.getElements());
    	writeFolders(folder.getFolders());
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
        stFrame.remove("element"); //$NON-NLS-1$
        //frame.remove("children");
        stFrame.add("element", component); //$NON-NLS-1$
        elementW.write(stFrame.render());
        elementW.close();
    }
    
    private void writeDiagrams() throws IOException {
        if(fModel.getDiagramModels().isEmpty()) {
            return;
        }

        saveDiagrams(fModel.getDiagramModels());

        for(IDiagramModel dm : fModel.getDiagramModels()) {
            File viewF = new File(fViewsFolder, dm.getId() + ".html"); //$NON-NLS-1$
            OutputStreamWriter viewW = new OutputStreamWriter(new FileOutputStream(viewF), "UTF8"); //$NON-NLS-1$
            stFrame.remove("element"); //$NON-NLS-1$
            stFrame.add("element", dm); //$NON-NLS-1$
            viewW.write(stFrame.render());
            viewW.close();
        }
    }
    
    private Hashtable<IDiagramModel, String> saveDiagrams(List<IDiagramModel> list) {
        Hashtable<IDiagramModel, String> table = new Hashtable<IDiagramModel, String>();
        int i = 1;
        
        for(IDiagramModel dm : list) {
            Image image = DiagramUtils.createImage(dm, 1, 10);
            String diagramName = dm.getId();
            if(StringUtils.isSet(diagramName)) {
                // removed this because ids can have hyphens in them (when imported from TOG format)
                // Let's hope that ids are filename friendly...
                //diagramName = FileUtils.getValidFileName(diagramName);
                
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
}
