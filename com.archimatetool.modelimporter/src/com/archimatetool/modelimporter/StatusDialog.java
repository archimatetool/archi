/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.modelimporter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ui.IArchiImages;
import com.archimatetool.editor.ui.components.ExtendedTitleAreaDialog;
import com.archimatetool.modelimporter.StatusMessage.StatusMessageLevel;

/**
 * Status Dialog
 * 
 * @author Phillip Beauvoir
 */
class StatusDialog extends ExtendedTitleAreaDialog {
    
    private static String HELP_ID = "com.archimatetool.help.ImportModel"; //$NON-NLS-1$
    
    private static final int COPY_TO_CLIPBOARD_BUTTON = IDialogConstants.CLIENT_ID + 1;
    
    private StyledText textControl;
    
    private List<StatusMessage> messages;
    
    private Button btnInfo, btnWarning;

    public StatusDialog(Shell parentShell, List<StatusMessage> messages) {
        super(parentShell, "ImporterStatusDialog"); //$NON-NLS-1$
        this.messages = messages;
        setTitleImage(IArchiImages.ImageFactory.getImage(IArchiImages.ECLIPSE_IMAGE_IMPORT_PREF_WIZARD));
        setShellStyle(getShellStyle() | SWT.RESIZE);
    }

    @Override
    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(Messages.StatusDialog_0);
    }
    
    @Override
    protected Control createDialogArea(Composite parent) {
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
        
        setTitle(Messages.StatusDialog_1);
        
        Composite composite = (Composite)super.createDialogArea(parent);
        
        GridLayout layout = (GridLayout)composite.getLayout();
        layout.marginWidth = 10;
        layout.verticalSpacing = 5;
        
        textControl = new StyledText(composite, SWT.MULTI | SWT.READ_ONLY | SWT.V_SCROLL | SWT.H_SCROLL);
        textControl.setLayoutData(new GridData(GridData.FILL_BOTH));
        textControl.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        textControl.setFont(JFaceResources.getTextFont());
        
        btnInfo = createCheckbox(composite, Messages.StatusDialog_3, StatusMessageLevel.INFO);
        btnWarning = createCheckbox(composite, Messages.StatusDialog_4, StatusMessageLevel.WARNING);
        
        showMessages();
        
        return composite;
    }
    
    private Button createCheckbox(Composite parent, String text, StatusMessageLevel level) {
        Button btn = new Button(parent, SWT.CHECK);
        btn.setText(text);
        btn.setSelection(hasMessageType(level));
        btn.setEnabled(btn.getSelection());
        
        btn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(hasMessageType(level)) {
                    showMessages();
                }
            }
        });
        
        return btn;
    }
    
    private boolean hasMessageType(StatusMessageLevel level) {
        return messages.stream().anyMatch(msg -> msg.getLevel() == level);
    }
    
    private void showMessages() {
        if(messages.isEmpty()) {
            textControl.setText(Messages.StatusDialog_2 + "\n"); //$NON-NLS-1$
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        
        boolean showInfo = btnInfo.getSelection();
        boolean showWarn = btnWarning.getSelection();
        
        List<StyleRange> ranges = new ArrayList<>();
        
        messages.stream()
                .filter(msg -> (showInfo && msg.getLevel() == StatusMessageLevel.INFO) || (showWarn && msg.getLevel() == StatusMessageLevel.WARNING))
                .forEach(msg -> {
                    ranges.add(createStyleRange(msg, sb.length())); // Add color style range first
                    sb.append(msg + "\n"); //$NON-NLS-1$
                });
        
        textControl.setText(sb.toString());
        textControl.setStyleRanges(ranges.toArray(new StyleRange[ranges.size()]));
    }
    
    private StyleRange createStyleRange(StatusMessage msg, int start) {
        StyleRange sr = new StyleRange();
        sr.foreground = msg.getLevel().getColor();
        sr.start = start;
        sr.length = msg.getLevel().getText().length();
        return sr;
    }
    
    @Override
    protected void buttonPressed(int buttonId) {
        if(buttonId == COPY_TO_CLIPBOARD_BUTTON) {
            copyToClipboard();
        }
        else {
            super.buttonPressed(buttonId);
        }
    }
    
    private void copyToClipboard() {
        Clipboard clipboard = null;
        
        try {
            clipboard = new Clipboard(getShell().getDisplay());
            clipboard.setContents(new Object[]{textControl.getText()}, new Transfer[]{TextTransfer.getInstance()});
        }
        finally {
            if(clipboard != null) {
                clipboard.dispose();
            }
        }
    }

    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        // Copy to clipboard
        createButton(parent, COPY_TO_CLIPBOARD_BUTTON, Messages.StatusDialog_5, false);
        
        // Close button
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.CLOSE_LABEL, true);
    }

    @Override
    protected Point getDefaultDialogSize() {
        return new Point(800, 640);
    }
}