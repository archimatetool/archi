/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.p2;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.editor.utils.ZipUtils;


/**
 * @author Phillip Beauvoir
 */
public class P2InstallHandler {
    
    private Shell shell;
    private boolean needsRestart = false;
    
    public boolean execute(Shell shell) {
        this.shell = shell;
        
        IProgressMonitor monitor = new NullProgressMonitor();
        
        if(!canWriteToInstallationFolder()) {
            String message = Messages.P2InstallHandler_0 + " "; //$NON-NLS-1$
            
            if(PlatformUtils.isWindows()) {
                message += Messages.P2InstallHandler_1;
            }
            else {
                message += Messages.P2InstallHandler_2;
            }
            
            displayErrorDialog(message);
            
            return false;
        }
        
        List<File> files = askOpenFiles();
        if(files.isEmpty()) {
            return false;
        }
        
        List<IStatus> stats = new ArrayList<IStatus>();
        
        try {
            for(File file : files) {
                IStatus status = installFile(file, monitor);
                stats.add(status);
            }
        }
        catch(ProvisionException | IOException ex) {
            displayErrorDialog(ex.getMessage());
            return false;
        }
            
        String resultMessage = ""; //$NON-NLS-1$
        boolean hasError = false;
        
        for(int i = 0; i < stats.size(); i++) {
            IStatus status = stats.get(i);
            
            if(status.isOK()) {
                needsRestart = true;
                resultMessage += NLS.bind(Messages.P2InstallHandler_6 + "\n", files.get(i).getName()); //$NON-NLS-1$
            }
            else {
                hasError = true;
                
                if(status.getCode() == 666) {
                    resultMessage += NLS.bind(Messages.P2InstallHandler_10 + "\n", files.get(i).getName()); //$NON-NLS-1$
                }
                else if(status.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
                    resultMessage += NLS.bind(Messages.P2InstallHandler_12 + "\n", files.get(i).getName()); //$NON-NLS-1$
                }
                else {
                    resultMessage += NLS.bind(Messages.P2InstallHandler_14 + "\n", files.get(i).getName()); //$NON-NLS-1$
                }
            }
        }
        
        if(hasError) {
            MessageDialog.openInformation(shell, Messages.P2InstallHandler_16, resultMessage);
        }
        
        return true;
    }
    
    boolean needsRestart() {
        return needsRestart;
    }
    
    private IStatus installFile(File file, IProgressMonitor monitor) throws IOException, ProvisionException {
        File folder = null;
        IStatus status = null;
        
        try {
            if(!isPluginZipFile(file)) {
                return new Status(IStatus.ERROR, "com.archimatetool.editor", 666, //$NON-NLS-1$
                        NLS.bind(Messages.P2InstallHandler_3, file.getAbsolutePath()), null);
            }
            
            folder = unpackZipFile(file);
            URI uri = folder.toURI();
            
            if(P2Handler.getInstance().isInstalled(uri, monitor)) {
                status = P2Handler.getInstance().update(uri, monitor);
            }
            else {
                status = P2Handler.getInstance().install(uri, monitor);
            }
        }
        finally {
            FileUtils.deleteFolder(folder);
        }
        
        return status;
    }
    
    private File unpackZipFile(File zipFile) throws IOException {
        Path tmp = Files.createTempDirectory("archi"); //$NON-NLS-1$
        File tmpFolder = tmp.toFile();
        tmpFolder.deleteOnExit();
        ZipUtils.unpackZip(zipFile, tmpFolder);
        return tmpFolder;
    }
    
    /**
     * @return True if we can write to the plugins folder
     */
    private boolean canWriteToInstallationFolder() {
        try {
            File pluginsFolder = getPluginsFolder();
            return Files.isWritable(pluginsFolder.toPath());
        }
        catch(IOException ex) {
            return false;
        }
    }
    
    private boolean isPluginZipFile(File file) throws IOException {
        return ZipUtils.isZipFile(file) && ZipUtils.hasZipEntry(file, "artifacts.jar"); //$NON-NLS-1$
    }
    
    private File getPluginsFolder() throws IOException {
        URL url = Platform.getBundle(ArchiPlugin.PLUGIN_ID).getEntry("/"); //$NON-NLS-1$
        url = FileLocator.resolve(url);
        return new File(url.getPath()).getParentFile();
    }
    
    private List<File> askOpenFiles() {
        FileDialog dialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
        dialog.setFilterExtensions(new String[] { "*.zip", "*.*" } ); //$NON-NLS-1$ //$NON-NLS-2$
        String path = dialog.open();
        
        List<File> files = new ArrayList<File>();
        
        if(path != null) {
            // Issue on OpenJDK if path is like C: or D: - no slash is added when creating File
            String filterPath = dialog.getFilterPath() + File.separator;;

            for(String name : dialog.getFileNames()) {
                File file = new File(filterPath, name);
                if(file.exists()) {
                    files.add(file);
                }
            }
        }
        
        return files;
    }

    private void displayErrorDialog(String message) {
        MessageDialog.openError(shell,
                Messages.P2InstallHandler_4,
                message);
    }
}
