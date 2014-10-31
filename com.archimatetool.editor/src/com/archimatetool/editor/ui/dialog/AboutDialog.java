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

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
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
import org.eclipse.ui.internal.ConfigurationInfo;
import org.eclipse.ui.internal.about.AboutUtils;

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
    private TabItem infoTabItem;
    private TabItem licenseTabItem;
    
    private Text systemText, licenseText;

    private Button errorLogButton, copyClipboardButton;
    
    private final static int ERRORLOG_ID = IDialogConstants.CLIENT_ID + 1;
    private final static int CLIPBOARD_ID = IDialogConstants.CLIENT_ID + 2;

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
        if(buttonId == ERRORLOG_ID) {
            AboutUtils.openErrorLogBrowser(getShell());
        }
        else if(buttonId == CLIPBOARD_ID) {
            Clipboard clipboard = null;
            try {
                clipboard = new Clipboard(getShell().getDisplay());
                String text = ""; //$NON-NLS-1$
                if(folder.getSelectionIndex() == 1) {
                    text = systemText.getText();
                }
                else if(folder.getSelectionIndex() == 2) {
                    text = licenseText.getText();
                }
                clipboard.setContents(new Object[] { text }, new Transfer[] { TextTransfer.getInstance() } );
            }
            finally {
                if(clipboard != null) {
                    clipboard.dispose();
                }
            }
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
                else if(item == infoTabItem) {
                    populateInfoTab();
                }
                else if(item == licenseTabItem) {
                    populateLicenseTab();
                }
            }
        });
        
        folder.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        createAboutTab();
        createInfoTab();
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
        errorLogButton.setVisible(false);
        copyClipboardButton.setVisible(false);
    }
    
    private void createInfoTab() {
        infoTabItem = new TabItem(folder, SWT.NONE);
        infoTabItem.setText(Messages.AboutDialog_4);

        Composite control = new Composite(folder, SWT.NONE);
        control.setLayout(new GridLayout());
        infoTabItem.setControl(control);
        
        systemText = new Text(control, SWT.MULTI | SWT.BORDER | SWT.READ_ONLY | SWT.V_SCROLL | SWT.NO_FOCUS | SWT.H_SCROLL);
        systemText.setBackground(control.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL);
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        systemText.setLayoutData(gridData);
        systemText.setFont(JFaceResources.getDialogFont());
    }
    
    private void populateInfoTab() {
        if(systemText.getText().length() == 0) {
            systemText.setText(ConfigurationInfo.getSystemSummary());
        }
        
        errorLogButton.setVisible(true);
        String filename = Platform.getLogFileLocation().toOSString();
        errorLogButton.setEnabled(new File(filename).exists());
        
        copyClipboardButton.setVisible(true);
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
        errorLogButton.setVisible(false);
        copyClipboardButton.setVisible(true);
        
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
        
        errorLogButton = createButton(parent, ERRORLOG_ID, Messages.AboutDialog_6, false);
        errorLogButton.setVisible(false);
        
        copyClipboardButton = createButton(parent, CLIPBOARD_ID, Messages.AboutDialog_7, false);
        copyClipboardButton.setVisible(false);
        
        createButton(parent, IDialogConstants.OK_ID, Messages.AboutDialog_8, true);
    }
}
