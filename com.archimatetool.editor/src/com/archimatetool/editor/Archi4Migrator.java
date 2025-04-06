/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.utils.FileUtils;
import com.archimatetool.editor.utils.PlatformUtils;

/**
 * Migrate from Archi 4 to 5 data
 */
@SuppressWarnings("nls")
public class Archi4Migrator {
    
    private static File previousLocation;
    private static File currentLocation;
    
    private static int DONT_ASK_AGAIN = 2;

    public static boolean check() throws Exception {
        // Is there one set as a property in Archi.ini?
        previousLocation = ArchiPlugin.getInstance().getUserPropertyLocation("workspaceMigrationLocation");
        
        if(previousLocation == null) {
            // Default Archi 4 instance locations
            if(PlatformUtils.isWindows()) {
                previousLocation = new File(System.getProperty("user.home") + "/AppData/Roaming/Archi4");
            }
            else if(PlatformUtils.isMac()) {
                previousLocation = new File(System.getProperty("user.home") + "/Library/Application Support/Archi4");
            }
            else {
                previousLocation = new File(System.getProperty("user.home") + "/.archi4");
            }
        }
        
        // Check these are not null. URL cam be null if -Dosgi.instance.area=@noDefault
        if(Platform.getInstanceLocation() == null || Platform.getInstanceLocation().getURL() == null) {
            return false;
        }
        
        // Our current instance location
        currentLocation = new File(Platform.getInstanceLocation().getURL().getPath());
        
        // Flag file to denote migration has happened
        File migratedFile = new File(previousLocation, "migrated");
        
        // No previous data location, or same as current location, or user has already been asked
        if(!previousLocation.exists() || previousLocation.equals(currentLocation) || migratedFile.exists()) {
            return false;
        }
        
        // Get the list of files in the previous data location
        File[] previousLocationFiles = previousLocation.listFiles();
        if(previousLocationFiles == null || previousLocationFiles.length == 0) {
            return false;
        }
        
        // Ask user
        int response = new MigratorDialog().open();
        
        if(response == Window.CANCEL) {
            return false;
        }
        
        if(response == DONT_ASK_AGAIN) {
            // Create the migrated flag file so we don't ask the user again
            Files.createFile(migratedFile.toPath());
            return false;
        }

        // Copy files
        Exception[] exception = new Exception[1];
        boolean[] cancelled = new boolean[1];
        
        // Since this can take a while, show the busy dialog
        IRunnableWithProgress runnable = monitor -> {
            try {
                monitor.beginTask("Copying files...", -1);
                
                // Previous dropins files if user specified
                File previousDropinsLocation = ArchiPlugin.getInstance().getUserPropertyLocation("dropinsMigrationLocation");
                
                // Previous location files
                for(File file : previousLocationFiles) {
                    // A file
                    if(file.isFile()) {
                        monitor.setTaskName("Copying: " + file.getName());
                        Files.copy(file.toPath(), currentLocation.toPath().resolve(file.toPath().getFileName()), StandardCopyOption.REPLACE_EXISTING);
                    }
                    // A folder
                    else {
                        // Don't copy this one
                        if(".config".equals(file.getName())) {
                            continue;
                        }
                        
                        // Check if this folder happens to be the old dropins folder under another name
                        if(previousDropinsLocation != null && previousDropinsLocation.equals(file)) {
                            continue;
                        }
                        
                        monitor.setTaskName("Copying: " + file.getName());
                        
                        // dropins and .metadata
                        if("dropins".equals(file.getName()) || ".metadata".equals(file.getName())) {
                            FileUtils.copyFolder(file, new File(currentLocation, file.getName()));
                        }
                        // Other folders go to the new documents folder (if it's not the same as the user data folder)
                        else if(!previousLocation.equals(ArchiPlugin.getInstance().getUserDataFolder())) {
                            FileUtils.copyFolder(file, new File(ArchiPlugin.getInstance().getUserDataFolder(), file.getName()));
                        }
                    }

                    if(monitor.isCanceled()) {
                        cancelled[0] = true;
                        break;
                    }
                }
                
                // Copy previous dropins folder if user specified (and if it's not the same as the current one)
                if(previousDropinsLocation != null && previousDropinsLocation.isDirectory()) {
                    // Current dropins location, can be null
                    File currentDropinsLocation = ArchiPlugin.getInstance().getUserDropinsFolder();
                    if(currentDropinsLocation == null) {
                        currentDropinsLocation = new File(currentLocation, "dropins");
                    }
                    
                    if(!previousDropinsLocation.equals(currentDropinsLocation)) {
                        FileUtils.copyFolder(previousDropinsLocation, currentDropinsLocation);
                    }
                }
            }
            catch(Exception ex) {
                exception[0] = ex;
            }
        };
        
        try {
            ProgressMonitorDialog dialog = new ProgressMonitorDialog(Display.getCurrent().getActiveShell());
            dialog.run(true, true, runnable); // Set fork to false because of threading issues on Linux
        }
        catch(Exception ex) {
            exception[0] = ex;
        }

        if(exception[0] != null) {
            throw exception[0];
        }
        
        if(cancelled[0]) {
            return false;
        }
        
        // Create the migrated flag file so we don't ask the user again
        Files.createFile(migratedFile.toPath());

        return true;
    }
    
    private static class MigratorDialog extends TitleAreaDialog {

        protected MigratorDialog() {
            super(null);
        }
        
        @Override
        protected Control createDialogArea(Composite parent) {
            setTitle("Archi 5 Migration");
            setTitleImage(IArchiImages.ImageFactory.getImage(IArchiImages.ECLIPSE_IMAGE_IMPORT_PREF_WIZARD));
            setMessage("Would you like to copy your Archi 4 settings and data to Archi 5?", IMessageProvider.INFORMATION);

            Composite area = (Composite) super.createDialogArea(parent);
            
            Composite container = new Composite(area, SWT.NONE);
            GridDataFactory.create(GridData.FILL_BOTH).applyTo(container);
            container.setLayout(new GridLayout(1, false));
            
            Text text = new Text(container, SWT.NONE | SWT.READ_ONLY | SWT.MULTI | SWT.WRAP);
            GridDataFactory.create(GridData.FILL_BOTH).hint(80, SWT.DEFAULT).applyTo(text);

            text.setText("Application data will be copied from " +
                      "'" + previousLocation.getAbsolutePath() + "'" +
                      " to " +
                      "'" + currentLocation.getAbsolutePath() + "'." +
                      "\n\n" +
                      "User data such as scripts and repositories will be copied from " +
                      "'" + previousLocation.getAbsolutePath() + "'" +
                      " to " +
                      "'" + ArchiPlugin.getInstance().getUserDataFolder().getAbsolutePath() + "'."
                    );
            
            return area;
        }
        
        @Override
        protected void configureShell(Shell newShell) {
            super.configureShell(newShell);
            newShell.setText("Archi 5");
        }
        
        @Override
        protected void createButtonsForButtonBar(Composite parent) {
            super.createButtonsForButtonBar(parent);
            getButton(IDialogConstants.CANCEL_ID).setText("No");
            getButton(IDialogConstants.OK_ID).setText("Yes");
            createButton(parent, DONT_ASK_AGAIN, "Don't ask again", false);
        }
        
        @Override
        protected void buttonPressed(int buttonId) {
            super.buttonPressed(buttonId);
            
            if(buttonId == DONT_ASK_AGAIN) {
                setReturnCode(DONT_ASK_AGAIN);
                close();
            }
        }
        
        @Override
        protected Point getInitialSize() {
            return new Point(550, 300);
        }
        
        @Override
        protected boolean isResizable() {
            return true;
        }
    }
}
