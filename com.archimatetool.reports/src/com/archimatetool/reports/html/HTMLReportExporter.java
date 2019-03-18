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
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.osgi.framework.Bundle;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;
import org.stringtemplate.v4.StringRenderer;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.browser.BrowserEditorInput;
import com.archimatetool.editor.browser.IBrowserEditor;
import com.archimatetool.editor.diagram.util.DiagramUtils;
import com.archimatetool.editor.diagram.util.ModelReferencedImage;
import com.archimatetool.editor.ui.ImageFactory;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
import com.archimatetool.model.IDiagramModelReference;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.reports.ArchiReportsPlugin;


/**
 * Export model to HTML report
 * 
 * @author Jean-Baptiste Sarrodie
 * @author Quentin Varquet
 * @author Phillip Beauvoir
 */
public class HTMLReportExporter {
    
    public static File PREVIEW_FOLDER = new File(ArchiPlugin.INSTANCE.getUserDataFolder(), "html-report-preview"); //$NON-NLS-1$
    
    static final String PREFS_LAST_FOLDER = "Reports_LastFolder"; //$NON-NLS-1$
    
    private IArchimateModel fModel;
    
    /**
     * Map of new bounds for each digram for bounds offset
     */
    private Map<IDiagramModel, Rectangle> diagramBoundsMap = new HashMap<IDiagramModel, Rectangle>();
    
    /**
     * Map of new bounds for child objects in images for hit areas
     */
    private Map<String, BoundsWithAbsolutePosition> childBoundsMap = new HashMap<String, BoundsWithAbsolutePosition>();
    
    private IProgressMonitor progressMonitor;
    
    static class CancelledException extends IOException {
        public CancelledException(String message) {
            super(message);
        }
    }
    
    /**
     * Clean up preview files
     * @throws IOException
     */
    public static void cleanPreviewFiles() throws IOException {
        FileUtils.deleteFolder(PREVIEW_FOLDER);
    }
    
    public HTMLReportExporter(IArchimateModel model) {
        fModel = model;
    }
    
    public void export() throws IOException {
        File targetFolder = askSaveFolder();
        if(targetFolder == null) {
            return;
        }
        
        IOException[] exception = new IOException[1];
        
        // Since this can take a while, show the busy dialog
        IRunnableWithProgress runnable = monitor -> {
            try {
                File file = createReport(targetFolder, "index.html", monitor); //$NON-NLS-1$
                
                // Open it in external Browser
                IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
                try {
                    monitor.subTask(Messages.HTMLReportExporter_7);
                    IWebBrowser browser = support.getExternalBrowser();
                    // This method supports network URLs
                    browser.openURL(new URL("file", null, file.getAbsolutePath().replace(" ", "%20"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                }
                catch(PartInitException ex) {
                    ex.printStackTrace();
                }
            }
            catch(IOException ex) {
                exception[0] = ex;
            }
        };
        
        try {
            ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
            dialog.run(false, true, runnable);
        }
        catch(InvocationTargetException | InterruptedException ex) {
            ex.printStackTrace();
        }

        if(exception[0] != null) {
            if(exception[0] instanceof CancelledException) {
                MessageDialog.openInformation(Display.getCurrent().getActiveShell(), Messages.HTMLReportExporter_2, exception[0].getMessage());
                return;
            }

            throw exception[0];
        }
    }
    
    public void preview() {
        PREVIEW_FOLDER.mkdirs();
        
        IOException[] exception = new IOException[1];
        
        // Since this can take a while, show the busy dialog
        IRunnableWithProgress runnable = monitor -> {
            try {
                File file = createReport(PREVIEW_FOLDER, "preview-" + fModel.getId() + ".html", monitor);  //$NON-NLS-1$//$NON-NLS-2$
                
                // Open it in Internal Browser
                BrowserEditorInput input = new BrowserEditorInput(file.getPath(), fModel.getName()) {
                    @Override
                    public IPersistableElement getPersistable() {
                        return null; // Don't save state
                    }
                    
                    @Override
                    public String getName() {
                        return Messages.HTMLReportExporter_0 + super.getName();
                    }
                };
                
                IBrowserEditor editor = (IBrowserEditor)EditorManager.openEditor(input, IBrowserEditor.ID);
                if(editor != null && editor.getBrowser() != null) {
                    monitor.subTask(Messages.HTMLReportExporter_7);
                    editor.getBrowser().refresh();
                }
            }
            catch(IOException ex) {
                exception[0] = ex;
            }
        };
        
        try {
            ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
            dialog.run(false, true, runnable);
        }
        catch(InvocationTargetException | InterruptedException ex) {
            ex.printStackTrace();
        }

        if(exception[0] != null) {
            if(exception[0] instanceof CancelledException) {
                MessageDialog.openInformation(Display.getCurrent().getActiveShell(), Messages.HTMLReportExporter_2, exception[0].getMessage());
                return;
            }
            
            MessageDialog.openError(Display.getCurrent().getActiveShell(), Messages.HTMLReportAction_0, exception[0].getMessage());
            exception[0].printStackTrace();
        }
    }
    
    public File createReport(File targetFolder, String indexFileName) throws IOException {
        return createReport(targetFolder, indexFileName, null);
    }
    
    public File createReport(File targetFolder, String indexFileName, IProgressMonitor monitor) throws IOException {
        progressMonitor = monitor;
        
        if(progressMonitor != null) {
            progressMonitor.beginTask(Messages.HTMLReportExporter_6, -1);
        }
        
        setProgressSubTask(Messages.HTMLReportExporter_9);
        
        // Copy HTML skeleton to target
        copyHTMLSkeleton(targetFolder);
        
        setProgressSubTask(Messages.HTMLReportExporter_10);
        
        // Copy hints files from the help plug-in
        copyHintsFiles(targetFolder);
        
        setProgressSubTask(Messages.HTMLReportExporter_11);
        
        // Create sub-folders
        File elementsFolder = new File(targetFolder, fModel.getId() + "/elements"); //$NON-NLS-1$
        elementsFolder.mkdirs(); // Make dir
        
        File viewsFolder = new File(targetFolder, fModel.getId() + "/views"); //$NON-NLS-1$
        viewsFolder.mkdirs(); // Make dir
        
        File imagesFolder = new File(targetFolder, fModel.getId() + "/images"); //$NON-NLS-1$
        imagesFolder.mkdirs(); // Make dir
             
        File objectsFolder = new File(targetFolder, fModel.getId() + "/objects"); //$NON-NLS-1$
        objectsFolder.mkdirs(); // Make dir

        // Instantiate templates files
        File mainFile = new File(ArchiReportsPlugin.INSTANCE.getTemplatesFolder(), "st/main.stg"); //$NON-NLS-1$
        STGroupFile groupFile = new STGroupFile(mainFile.getAbsolutePath(), '^', '^');
        ST stFrame = groupFile.getInstanceOf("frame"); //$NON-NLS-1$
        
        groupFile.registerRenderer(String.class, new StringRenderer());
        
        // Write model purpose and properties html
        writeElement(new File(elementsFolder, "model.html"), stFrame, fModel); //$NON-NLS-1$
        
        // Write all folders
        writeFolders(elementsFolder, stFrame, fModel.getFolders());
        
        setProgressSubTask(Messages.HTMLReportExporter_12);
        
        // Write Diagrams and images
        writeDiagrams(imagesFolder, viewsFolder, stFrame);
        
        setProgressSubTask(Messages.HTMLReportExporter_11);
        
        // Write other graphical objects
        writeGraphicalObjects(objectsFolder, stFrame);
        
        setProgressSubTask(Messages.HTMLReportExporter_13);
        
        // Write root model.html frame
        File indexFile = new File(targetFolder, indexFileName);
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(indexFile), "UTF8"); //$NON-NLS-1$
        
        ST stModel = groupFile.getInstanceOf("modelreport"); //$NON-NLS-1$

        stModel.add("model", fModel); //$NON-NLS-1$
        stModel.add("strategyFolder", fModel.getFolder(FolderType.STRATEGY)); //$NON-NLS-1$
        stModel.add("businessFolder", fModel.getFolder(FolderType.BUSINESS)); //$NON-NLS-1$
        stModel.add("applicationFolder", fModel.getFolder(FolderType.APPLICATION)); //$NON-NLS-1$
        stModel.add("technologyFolder", fModel.getFolder(FolderType.TECHNOLOGY)); //$NON-NLS-1$
        stModel.add("motivationFolder", fModel.getFolder(FolderType.MOTIVATION)); //$NON-NLS-1$
        stModel.add("implementationFolder", fModel.getFolder(FolderType.IMPLEMENTATION_MIGRATION)); //$NON-NLS-1$
        stModel.add("otherFolder", fModel.getFolder(FolderType.OTHER)); //$NON-NLS-1$
        stModel.add("relationsFolder", fModel.getFolder(FolderType.RELATIONS)); //$NON-NLS-1$
        stModel.add("viewsFolder", fModel.getFolder(FolderType.DIAGRAMS)); //$NON-NLS-1$
        
        writer.write(stModel.render());
        writer.close();
        
        return indexFile;
    }
    
    /**
     * Copy source HTML files to target folder
     * @throws IOException 
     */
    private void copyHTMLSkeleton(File targetFolder) throws IOException {
        File srcDir = new File(ArchiReportsPlugin.INSTANCE.getTemplatesFolder(), "html"); //$NON-NLS-1$
        FileUtils.copyFolder(srcDir, targetFolder);
    }
    
    /**
     * Copy hints files to target folder from the help plug-in
     * @throws IOException 
     */
    private void copyHintsFiles(File targetFolder) throws IOException {
        // Main hints
        Bundle bundle = Platform.getBundle("com.archimatetool.help"); //$NON-NLS-1$
        URL url = FileLocator.resolve(bundle.getEntry("hints")); //$NON-NLS-1$
        FileUtils.copyFolder(new File(url.getPath()), new File(targetFolder, "hints")); //$NON-NLS-1$
        
        // Canvas hints
        bundle = Platform.getBundle("com.archimatetool.canvas"); //$NON-NLS-1$
        url = FileLocator.resolve(bundle.getEntry("help/hints")); //$NON-NLS-1$
        FileUtils.copyFolder(new File(url.getPath()), new File(targetFolder, "hints")); //$NON-NLS-1$

        updateProgress();
    }

    /**
     * Write all folders
     */
    private void writeFolders(File elementsFolder, ST stFrame, EList<IFolder> folders) throws IOException {
    	for(IFolder folder : folders) {
    		writeFolder(elementsFolder, stFrame, folder);
    	}
    }
    
    /**
     * Write a single folder
     */
    private void writeFolder(File elementsFolder, ST stFrame, IFolder folder) throws IOException {
    	writeElements(elementsFolder, stFrame, folder.getElements());
    	writeFolders(elementsFolder, stFrame, folder.getFolders());
    }
    
    /**
     * Write all elements
     */
    private void writeElements(File elementsFolder, ST stFrame, List<EObject> list) throws IOException {
        if(!list.isEmpty()) {
            for(EObject object : list) {
                if(object instanceof IArchimateConcept) {
                	writeElement(new File(elementsFolder, ((IIdentifier) object).getId() + ".html"), stFrame, object); //$NON-NLS-1$
                }
            }
        }
    }
    
    /**
     * Write a single element
     */
    private void writeElement(File elementFile, ST stFrame, EObject component) throws IOException {
        OutputStreamWriter elementW = new OutputStreamWriter(new FileOutputStream(elementFile), "UTF8"); //$NON-NLS-1$
        stFrame.remove("element"); //$NON-NLS-1$
        //frame.remove("children");
        stFrame.add("element", component); //$NON-NLS-1$
        elementW.write(stFrame.render());
        elementW.close();

        updateProgress();
    }
    
    /**
     * Write grpahical objects
     */
    private void writeGraphicalObjects(File objectsFolder, ST stFrame) throws IOException {
        for(Iterator<EObject> iter = fModel.eAllContents(); iter.hasNext();) {
            EObject eObject = iter.next();
            if(eObject instanceof IDiagramModelObject && !(eObject instanceof IDiagramModelArchimateObject) 
                    && !(eObject instanceof IDiagramModelReference)) {
                writeElement(new File(objectsFolder, ((IIdentifier) eObject).getId() + ".html"), stFrame, eObject); //$NON-NLS-1$
            }
        }
    }

    /**
     * Write diagrams
     */
    private void writeDiagrams(File imagesFolder, File viewsFolder, ST stFrame) throws IOException {
        if(fModel.getDiagramModels().isEmpty()) {
            return;
        }

        // Save images
        saveImages(imagesFolder);

        for(IDiagramModel dm : fModel.getDiagramModels()) {
            // Add the necessary bounds in order to get correct absolute coordinates for the elements in the generated image
            Rectangle bounds = diagramBoundsMap.get(dm);
            
            // process the children
            for(IDiagramModelObject dmo: dm.getChildren() ) {
                addNewBounds(dmo, bounds.x * -1, bounds.y * -1);
            }

            File viewF = new File(viewsFolder, dm.getId() + ".html"); //$NON-NLS-1$
            OutputStreamWriter viewW = new OutputStreamWriter(new FileOutputStream(viewF), "UTF8"); //$NON-NLS-1$
            
            stFrame.remove("element"); //$NON-NLS-1$
            stFrame.add("element", dm); //$NON-NLS-1$
            
            stFrame.remove("map"); //$NON-NLS-1$
            stFrame.add("map", childBoundsMap); //$NON-NLS-1$
            
            viewW.write(stFrame.render());
            viewW.close();
        }
    }
    
    /**
     * Save diagram images
     * @throws IOException 
     */
    private void saveImages(File imagesFolder) throws IOException {
        // Use this to generate unique name for image file
        Hashtable<IDiagramModel, String> nameTable = new Hashtable<IDiagramModel, String>();
        
        int nameCount = 1;
        
        for(IDiagramModel dm : fModel.getDiagramModels()) {
            ModelReferencedImage geoImage = DiagramUtils.createModelReferencedImage(dm, 1, 10);
            Image image = geoImage.getImage();
            
            // Generate file name
            String diagramName = dm.getId();
            if(StringUtils.isSet(diagramName)) {
                // removed this because ids can have hyphens in them (when imported from TOG format)
                // Let's hope that ids are filename friendly...
                //diagramName = FileUtils.getValidFileName(diagramName);
                
                int j = 2;
                String s = diagramName + ".png";  //$NON-NLS-1$
                while(nameTable.containsValue(s)) {
                    s = diagramName + "_" + j++ + ".png"; //$NON-NLS-1$ //$NON-NLS-2$
                }
                diagramName = s;
            }
            else {
                diagramName = Messages.HTMLReportExporter_1 + " " + nameCount++ + ".png";  //$NON-NLS-1$//$NON-NLS-2$
            }

            nameTable.put(dm, diagramName);

            // Get and store the bounds of the top-left element in the figure to act as overall x,y offset
            Rectangle bounds = geoImage.getBounds();
            bounds.performScale(ImageFactory.getImageDeviceZoom() / 100); // Account for device zoom level
            diagramBoundsMap.put(dm, bounds);

            try {
                ImageLoader loader = new ImageLoader();
                loader.data = new ImageData[] { image.getImageData(ImageFactory.getImageDeviceZoom()) };
                File file = new File(imagesFolder, diagramName);
                loader.save(file.getAbsolutePath(), SWT.IMAGE_PNG);
            }
            finally {
                image.dispose();
            }
            
            updateProgress();
        }
    }
    
    private void updateProgress() throws IOException {
        if(progressMonitor != null && PlatformUI.isWorkbenchRunning() && Display.getCurrent() != null) {
            while(Display.getCurrent().readAndDispatch());
            
            if(progressMonitor.isCanceled()) {
                throw new CancelledException(Messages.HTMLReportExporter_14);
            }
        }
    }
    
    private void setProgressSubTask(String task) {
        if(progressMonitor != null) {
            progressMonitor.subTask(task);
        }
    }

    private File askSaveFolder() {
        DirectoryDialog dialog = new DirectoryDialog(Display.getCurrent().getActiveShell());
        dialog.setText(Messages.HTMLReportExporter_2);
        dialog.setMessage(Messages.HTMLReportExporter_3);
        
        IPreferenceStore store = ArchiReportsPlugin.INSTANCE.getPreferenceStore();
        String lastFolder = store.getString(PREFS_LAST_FOLDER);
        if(StringUtils.isSet(lastFolder)) {
            dialog.setFilterPath(getLastDirectory(lastFolder));
        }
        
        String path = dialog.open();
        if(path == null) {
            return null;
        }
        
        File folder = new File(path);
        if(folder.exists()) {
            String[] children = folder.list();
            if(children != null && children.length > 0) {
                boolean result = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(),
                        Messages.HTMLReportExporter_2,
                        NLS.bind(Messages.HTMLReportExporter_5, folder));
                if(!result) {
                    return null;
                }
            }
        }
        else {
            folder.mkdirs();
        }
        
        store.setValue(PREFS_LAST_FOLDER, folder.getAbsolutePath());
        
        // TODO: Bug on Mac 10.12 and newer - Open dialog does not close straight away
        // See https://bugs.eclipse.org/bugs/show_bug.cgi?id=527306
        if(PlatformUtils.isMac()) {
            while(Display.getCurrent().readAndDispatch());
        }
        
        return folder;
    }
    
    private String getLastDirectory(String path) {
        File file = new File(path);
        
        // Go up and find parent until found
        while(file != null && !file.exists()) {
            file = file.getParentFile();
        }
        
        return file == null ? "" : file.getAbsolutePath(); //$NON-NLS-1$
    }

    /**
     * Add new bounds for each diagram object in relation to its parent offset x,y
     */
    private void addNewBounds(IDiagramModelObject dmo, int offsetX, int offsetY) {
        // Add new bounds caled to device zoom
        BoundsWithAbsolutePosition newBounds = new BoundsWithAbsolutePosition(dmo.getBounds(), ImageFactory.getImageDeviceZoom() / 100);
        newBounds.setOffset(offsetX, offsetY); // Add offset
        childBoundsMap.put(dmo.getId(), newBounds);
        
        // Children
        if(dmo instanceof IDiagramModelContainer) {
            for(IDiagramModelObject child: ((IDiagramModelContainer)dmo).getChildren() ) {
                addNewBounds(child, newBounds.getX1(), newBounds.getY1());
            }
        }
    }

}
