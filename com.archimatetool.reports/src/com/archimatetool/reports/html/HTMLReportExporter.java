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
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.browser.BrowserEditorInput;
import com.archimatetool.editor.browser.IBrowserEditor;
import com.archimatetool.editor.diagram.util.DiagramUtils;
import com.archimatetool.editor.ui.services.EditorManager;
import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.editor.utils.StringUtils;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelContainer;
import com.archimatetool.model.IDiagramModelObject;
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
public class HTMLReportExporter extends AbstractUIPlugin {
    
    public static File PREVIEW_FOLDER = new File(ArchiPlugin.INSTANCE.getUserDataFolder(), "html-report-preview"); //$NON-NLS-1$
    
    private IArchimateModel fModel;
    
    public HTMLReportExporter(IArchimateModel model) {
        fModel = model;
    }
    
    public void export() throws IOException {
        File targetFolder = askSaveFolder();
        if(targetFolder == null) {
            return;
        }
        
        File file = createReport(targetFolder, "index.html"); //$NON-NLS-1$
        
        // Open it in external Browser
        IWorkbenchBrowserSupport support = PlatformUI.getWorkbench().getBrowserSupport();
        try {
            IWebBrowser browser = support.getExternalBrowser();
            browser.openURL(file.toURI().toURL());
        }
        catch(PartInitException ex) {
            ex.printStackTrace();
        }
    }
    
    public void preview() {
        PREVIEW_FOLDER.mkdirs();
        
        BusyIndicator.showWhile(Display.getCurrent(), () -> {
            try {
                File file = createReport(PREVIEW_FOLDER, "preview-" + fModel.getId() + ".html");  //$NON-NLS-1$//$NON-NLS-2$
                
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
                editor.getBrowser().refresh();
            }
            catch(IOException ex) {
                MessageDialog.openError(Display.getCurrent().getActiveShell(), Messages.HTMLReportAction_0, ex.getMessage());
                ex.printStackTrace();
            }
        });
    }
    
    /**
     * Clean up preview files
     * @throws IOException
     */
    public static void cleanPreviewFiles() throws IOException {
        FileUtils.deleteFolder(PREVIEW_FOLDER);
    }

    File createReport(File targetFolder, String indexFileName) throws IOException {
        // Copy HTML skeleton to target
        copyHTMLSkeleton(targetFolder);
        
        // Copy hints files from the help plug-in
        copyHintsFiles(targetFolder);
        
        // Create sub-folders
        File elementsFolder = new File(targetFolder, fModel.getId() + "/elements"); //$NON-NLS-1$
        elementsFolder.mkdirs(); // Make dir
        
        File viewsFolder = new File(targetFolder, fModel.getId() + "/views"); //$NON-NLS-1$
        viewsFolder.mkdirs(); // Make dir
        
        File imagesFolder = new File(targetFolder, fModel.getId() + "/images"); //$NON-NLS-1$
        imagesFolder.mkdirs(); // Make dir
             
        // Instantiate templates files
        File mainFile = new File(ArchiReportsPlugin.INSTANCE.getTemplatesFolder(), "st/main.stg"); //$NON-NLS-1$
        STGroupFile groupFile = new STGroupFile(mainFile.getAbsolutePath(), '^', '^');
        ST stFrame = groupFile.getInstanceOf("frame"); //$NON-NLS-1$
        
        // Write model purpose and properties html
        writeElement(new File(elementsFolder, "model.html"), stFrame, fModel); //$NON-NLS-1$
        
        // Write all folders
        writeFolders(elementsFolder, stFrame, fModel.getFolders());
        
        // Write Diagrams and images
        writeDiagrams(imagesFolder, viewsFolder, stFrame);
        
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
        Bundle bundle = Platform.getBundle("com.archimatetool.help"); //$NON-NLS-1$
        URL url = FileLocator.resolve(bundle.getEntry("hints")); //$NON-NLS-1$
        FileUtils.copyFolder(new File(url.getPath()), new File(targetFolder, "hints")); //$NON-NLS-1$
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
    }

    /**
     * Write diagrams
     */
    private void writeDiagrams(File imagesFolder, File viewsFolder, ST stFrame) throws IOException {
        if(fModel.getDiagramModels().isEmpty()) {
            return;
        }

        Hashtable<IDiagramModel, Rectangle> offsetsTable = saveImages(imagesFolder);

        for(IDiagramModel dm : fModel.getDiagramModels()) {
            // we need to add the necessary offsets in order to get correct absolute coordinates for the elements
            // in the generated image
            Rectangle offset = offsetsTable.get(dm);
            for (IDiagramModelObject om: dm.getChildren() ) {
                setOffset(om, offset.x*-1, offset.y*-1);
            }

            File viewF = new File(viewsFolder, dm.getId() + ".html"); //$NON-NLS-1$
            OutputStreamWriter viewW = new OutputStreamWriter(new FileOutputStream(viewF), "UTF8"); //$NON-NLS-1$
            stFrame.remove("element"); //$NON-NLS-1$
            stFrame.add("element", dm); //$NON-NLS-1$
            viewW.write(stFrame.render());
            viewW.close();
        }
    }

    /**
     * Save diagram images
     * return the offsets of the top-left element(s) in each image
     */
    private Hashtable<IDiagramModel, Rectangle> saveImages(File imagesFolder) {
        Hashtable<IDiagramModel, String> table = new Hashtable<IDiagramModel, String>();
        // we store the offsets of the top-left element(s) in each image
        Hashtable<IDiagramModel, Rectangle> offsetsTable = new Hashtable<>();
        int i = 1;
        
        for(IDiagramModel dm : fModel.getDiagramModels()) {
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

            // Get and store the offset of the top-left element in the figure
            Rectangle offset = getOffset(dm, 1, 10);
            offsetsTable.put(dm,  offset);

            try {
                ImageLoader loader = new ImageLoader();
                loader.data = new ImageData[] { image.getImageData() };
                File file = new File(imagesFolder, diagramName);
                loader.save(file.getAbsolutePath(), SWT.IMAGE_PNG);
            }
            finally {
                image.dispose();
            }
        }
        return offsetsTable;
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
     * Get the offset of the top left element(s) in the generated image of a diagram
     * based on the given scales and margins
     * This has been "copied" together from the image-generating call stack...
     * (mostly contained in com.archimatetool.editor.diagram.util.DiagramUtils)
     *
     * @param dm
     * @param scale
     * @param margin
     * @return
     */
    private Rectangle getOffset(IDiagramModel dm, double scale, int margin) {
        Shell shell = new Shell();
        GraphicalViewer graphViewer = DiagramUtils.createViewer(dm, shell);
        shell.dispose();
        LayerManager layerManager = (LayerManager)graphViewer.getEditPartRegistry().get(LayerManager.ID);
        IFigure rootFigure = layerManager.getLayer(LayerConstants.PRINTABLE_LAYERS);
        Rectangle bounds = DiagramUtils.getMinimumBounds(rootFigure);
        if(bounds == null) {
            bounds = new Rectangle(0, 0, 100, 100); // At least a minimum for a blank image
        }
        else {
            bounds.expand(margin / scale, margin / scale); // margins
        }
        return bounds;
	}

    private void setOffset(IDiagramModelObject om, int offsetX, int offsetY ) {
        BoundsWithAbsolutePosition b = new BoundsWithAbsolutePosition(om.getBounds());
        b.setOffset(offsetX, offsetY);
        om.setBounds(b);
        if (om instanceof IDiagramModelContainer) {
            // in case we have a model container, the contained coordinates are relative to the container
            // -> recursively add the containers base coordinate as an offset to the children
            IDiagramModelContainer mg = (IDiagramModelContainer) om;
            for (IDiagramModelObject omc: mg.getChildren() ) {
                setOffset(omc, b.getX1(), b.getY1());
            }
        }
    }

}
