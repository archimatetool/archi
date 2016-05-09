/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.dialog;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.about.InstallationDialog;

import com.archimatetool.editor.Application;
import com.archimatetool.editor.ArchimateEditorPlugin;
import com.archimatetool.editor.ui.IArchimateImages;


/**
 * Custom About dialog
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("restriction")
public class AboutDialog extends TrayDialog {
    
    private TabFolder folder;
    
    private TabItem aboutTabItem;
    private TabItem licenseTabItem;
    
    private Text licenseText;

    private Button installationDetailsButton;
    
    private final static int INSTALLATION_DETAILS_ID = IDialogConstants.CLIENT_ID + 1;

    public AboutDialog(Shell shell) {
        super(shell);
        setHelpAvailable(false);
    }
    
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.AboutDialog_0);
    }

    @Override
    protected boolean isResizable() {
        return true;
    }
    
    @Override
    protected void buttonPressed(int buttonId) {
        if(buttonId == INSTALLATION_DETAILS_ID) {
            new InstallationDialog(getShell(), PlatformUI.getWorkbench().getActiveWorkbenchWindow()).open();
        }
        else {
            super.buttonPressed(buttonId);
        }
    }
    
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);

        folder = new TabFolder(composite, SWT.NONE);
        
        folder.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                TabItem item = (TabItem)e.item;
                if(item == aboutTabItem) {
                    populateAboutTab();
                }
                else if(item == licenseTabItem) {
                    populateLicenseTab();
                }
            }
        });
        
        folder.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        createAboutTab();
        createLicenseTab();
        
        return composite;
    }
    
    private void createAboutTab() {
        aboutTabItem = new TabItem(folder, SWT.NONE);
        aboutTabItem.setText(Messages.AboutDialog_1);
        
        Composite control = new Composite(folder, SWT.NONE);
        control.setLayout(new GridLayout());
        aboutTabItem.setControl(control);
        
        Composite imageControl = new Composite(control, SWT.NO_BACKGROUND);
        GridData gd = new GridData(GridData.CENTER, GridData.CENTER, true, true);
        imageControl.setLayoutData(gd);

        final int imageHeight;
        
        final Image image = IArchimateImages.ImageFactory.getImage("splash.bmp"); //$NON-NLS-1$
        if(image != null) {
            ImageData id = image.getImageData();
            gd.widthHint = id.width;
            gd.heightHint = id.height;
            imageHeight = id.height;
        }
        else {
            gd.widthHint = 500;
            gd.heightHint = 300;
            imageHeight = 300;
        }
        
        final String version = Messages.AboutDialog_2 + System.getProperty(Application.APPLICATION_VERSIONID);
        final String build = Messages.AboutDialog_3 + System.getProperty(Application.APPLICATION_BUILDID);
        final String copyright = ArchimateEditorPlugin.INSTANCE.getResourceString("%aboutCopyright"); //$NON-NLS-1$
        
        imageControl.addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                int fontHeight = e.gc.getFontMetrics().getHeight() + 2;
                
                e.gc.drawImage(image, 0, 0);
                e.gc.drawString(version, 19, 166, true);
                e.gc.drawString(build, 19, 166 + fontHeight, true);
                e.gc.drawString(copyright, 19, imageHeight - fontHeight - 5, true);
            }
        });
    }
    
    private void populateAboutTab() {
        installationDetailsButton.setVisible(true);
    }
    
    private void createLicenseTab() {
        licenseTabItem = new TabItem(folder, SWT.NONE);
        licenseTabItem.setText(Messages.AboutDialog_5);

        Composite control = new Composite(folder, SWT.NONE);
        control.setLayout(new GridLayout());
        licenseTabItem.setControl(control);
        
        licenseText = new Text(control, SWT.MULTI | SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.NO_FOCUS | SWT.H_SCROLL);
        licenseText.setBackground(control.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        licenseText.setLayoutData(gridData);
        licenseText.setFont(JFaceResources.getDialogFont());
    }

    private void populateLicenseTab() {
        installationDetailsButton.setVisible(false);
        
        if(licenseText.getText().length() == 0) {
            File file = new File(ArchimateEditorPlugin.INSTANCE.getPluginFolder(), "LICENSE.txt"); //$NON-NLS-1$
            if(file.exists()) {
                byte[] buffer = new byte[(int) file.length()];
                BufferedInputStream is = null;
                try {
                    is = new BufferedInputStream(new FileInputStream(file));
                    is.read(buffer);
                }
                catch(IOException ex) {
                    ex.printStackTrace();
                }
                finally {
                    if(is != null) {
                        try {
                            is.close();
                        }
                        catch(IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                licenseText.setText(new String(buffer));
            }
        }
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        GridLayout layout = (GridLayout)parent.getLayout();
        layout.marginHeight = 0;
        layout.marginBottom = 5;
        
        installationDetailsButton = createButton(parent, INSTALLATION_DETAILS_ID, Messages.AboutDialog_4, false);
        setButtonLayoutData(installationDetailsButton);
        
        createButton(parent, IDialogConstants.OK_ID, Messages.AboutDialog_6, true);
    }
}
