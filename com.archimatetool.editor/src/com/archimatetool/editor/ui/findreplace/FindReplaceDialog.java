/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.findreplace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.utils.StringUtils;


/**
 * Find/Replace Dialog (based on the XMind Find/Replace dialog)
 */
public class FindReplaceDialog extends Dialog implements IPartListener, IDialogConstants {
    
    private static Map<IWorkbenchWindow, FindReplaceDialog> SINGLETONS = new HashMap<IWorkbenchWindow, FindReplaceDialog>();

    public static FindReplaceDialog getInstance(IWorkbenchWindow window) {
        FindReplaceDialog instance = SINGLETONS.get(window);
        if(instance != null) {
            return instance;
        }
        return new FindReplaceDialog(window);
    }
    
    private static final int FIND_ID = CLIENT_ID + 1;
    private static final int FIND_ALL_ID = CLIENT_ID + 2;
    private static final int REPLACE_ID = CLIENT_ID + 3;
    private static final int REPLACE_ALL_ID = CLIENT_ID + 4;
    
    private static final String EMPTY = ""; //$NON-NLS-1$
    private static final String STRING_NOT_FOUND = Messages.FindReplaceDialog_0;
    private static final String DIALOG_ID = "FindReplaceDialog"; //$NON-NLS-1$
    private static final String SETTINGS_SECTION_NAME = "com.archimatetool.ui.findreplace"; //$NON-NLS-1$
    private static final String P_PARAMETER = "parameter"; //$NON-NLS-1$

    private static List<String> FindHistory = new ArrayList<String>();
    private static List<String> ReplaceHistory = new ArrayList<String>();

    private IWorkbenchWindow window;
    private IWorkbenchPart currentPart;
    private IFindReplaceProvider operationProvider;
    
    private int parameter = IFindReplaceProvider.PARAM_FORWARD | IFindReplaceProvider.PARAM_SELECTED_MODEL;
    
    private Label infoLabel;
    private Combo findInput;
    private Combo replaceInput;
    
    private Map<Integer, Button> opButtons = new HashMap<Integer, Button>();
    private Map<Integer, List<Button>> paramWidgets = new HashMap<Integer, List<Button>>();
    
    private Listener eventHandler = new EventHandler();

    private class EventHandler implements Listener {
        @Override
        public void handleEvent(Event event) {
            if(event.type == SWT.Modify) {
                updateOperationButtons();
                infoLabel.setText(EMPTY);
            }
            else if(event.type == SWT.FocusIn) {
                if(event.widget instanceof Combo) {
                    Combo input = (Combo)event.widget;
                    input.setSelection(new Point(0, input.getText().length()));
                }
            }
            else if(event.type == SWT.Selection) {
                Button b = (Button)event.widget;
                if(opButtons.containsValue(b)) {
                    buttonPressed((Integer)b.getData());
                }
                else {
                    List<Button> list = paramWidgets.get(b.getData());
                    if(list.contains(b)) {
                        int param = ((Integer)b.getData()).intValue();
                        if(b.getSelection()) {
                            parameter |= param;
                        }
                        else {
                            parameter &= ~param;
                        }
                        if(operationProvider != null) {
                            operationProvider.setParameter(param, b.getSelection());
                        }
                        updateOperationButtons();
                    }
                }
            }
        }
    }

    protected FindReplaceDialog(final IWorkbenchWindow window) {
        super(window.getShell());
        
        this.window = window;

        setShellStyle(SWT.TITLE | SWT.MODELESS | SWT.CLOSE | SWT.RESIZE);
        
        SINGLETONS.put(window, this);
        
        window.getShell().addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                close();
                SINGLETONS.remove(window);
            }
        });
    }
    
    @Override
    protected boolean isResizable() {
        return true;
    }
    
    @Override
    public void create() {
        super.create();
        IWorkbenchPart activePart = window.getActivePage().getActivePart();
        partActivated(activePart);
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.FindReplaceDialog_1);
        
        loadParameter();
        startListeningToPartChanges();
        
        newShell.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                saveParameter();
                stopListeningToPartChanges();
            }
        });
    }
    
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        GridLayout layout = (GridLayout) composite.getLayout();
        layout.verticalSpacing = 10;
        layout.marginTop = layout.marginHeight;
        layout.marginHeight = 0;
        layout.marginBottom = 0;

        createInputGroup(composite);
        createParameterGroups(composite);
        createOperationButtonGroup(composite);

        return composite;
    }
    
    @Override
    protected Control createButtonBar(Composite parent) {
        Composite container = new Composite(parent, SWT.NO_FOCUS);
        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout containerLayout = new GridLayout(2, false);
        containerLayout.horizontalSpacing = 0;
        containerLayout.verticalSpacing = 0;
        containerLayout.marginHeight = 7;
        containerLayout.marginWidth = 7;
        container.setLayout(containerLayout);

        infoLabel = new Label(container, SWT.NONE);
        infoLabel.setBackground(container.getBackground());
        infoLabel.setFont(container.getFont());
        infoLabel.setForeground(container.getDisplay().getSystemColor(SWT.COLOR_RED));
        infoLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, true));

        Composite composite = (Composite)super.createButtonBar(container);
        GridLayout layout = (GridLayout)composite.getLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        return composite;
    }
    
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, CLOSE_ID, CLOSE_LABEL, false);
    }
    
    @Override
    protected void buttonPressed(int buttonId) {
        if(CLOSE_ID == buttonId) {
            close();
        }
        else if(FIND_ID == buttonId) {
            findPressed();
        }
        else if(FIND_ALL_ID == buttonId) {
            findAllPressed();
        }
        else if(REPLACE_ID == buttonId) {
            replacePressed();
        }
        else if(REPLACE_ALL_ID == buttonId) {
            replaceAllPressed();
        }
        else super.buttonPressed(buttonId);
    }

    protected void createInputGroup(Composite parent) {
        Composite composite = new Composite(parent, SWT.NO_FOCUS);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout inputLayout = new GridLayout(2, false);
        inputLayout.marginWidth = 0;
        inputLayout.marginHeight = 0;
        composite.setLayout(inputLayout);

        findInput = createInputWidget(composite, Messages.FindReplaceDialog_2, FindHistory);
        replaceInput = createInputWidget(composite, Messages.FindReplaceDialog_3, ReplaceHistory);
    }

    private Combo createInputWidget(Composite parent, String label, List<String> history) {
        Label labelWidget = new Label(parent, SWT.NONE);
        labelWidget.setText(label);
        labelWidget.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, true));

        final Combo input = new Combo(parent, SWT.SINGLE | SWT.BORDER | SWT.DROP_DOWN);
        GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, true);
        layoutData.widthHint = 200;
        input.setLayoutData(layoutData);
        
        for(String t : history) {
            input.add(t);
        }
        
        if(!history.isEmpty()) {
            input.setText(history.get(0));
        }
        
        input.addListener(SWT.Modify, eventHandler);
        input.addListener(SWT.FocusIn, eventHandler);
        
        return input;
    }

    protected void createOperationButtonGroup(Composite parent) {
        Composite buttons = new Composite(parent, SWT.NO_FOCUS);
        buttons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout buttonsLayout = new GridLayout(2, true);
        buttonsLayout.marginHeight = 0;
        buttonsLayout.marginWidth = 0;
        buttons.setLayout(buttonsLayout);
        createOperationButton(buttons, FIND_ID, Messages.FindReplaceDialog_4, true);
        createOperationButton(buttons, FIND_ALL_ID, Messages.FindReplaceDialog_5, false);
        createOperationButton(buttons, REPLACE_ID, Messages.FindReplaceDialog_6, false);
        createOperationButton(buttons, REPLACE_ALL_ID, Messages.FindReplaceDialog_7, false);
    }

    protected Button createOperationButton(Composite parent, int id, String text, boolean defaultButton) {
        Button button = new Button(parent, SWT.PUSH);
        button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        button.setText(text);
        button.setData(id);
        button.addListener(SWT.Selection, eventHandler);
        if(defaultButton) {
            Shell shell = parent.getShell();
            if(shell != null) {
                shell.setDefaultButton(button);
            }
        }
        registerOperationButton(id, button);
        return button;
    }

    private void registerOperationButton(final int id, final Button widget) {
        opButtons.put(Integer.valueOf(id), widget);
        widget.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                unregisterOperationButton(id, widget);
            }
        });
    }

    private void unregisterOperationButton(int id, Button widget) {
        Integer key = Integer.valueOf(id);
        if(opButtons.get(key) == widget) {
            opButtons.remove(key);
        }
    }

    protected void updateOperationButtons() {
        if(getShell() == null || getShell().isDisposed()) {
            return;
        }

        getButton(FIND_ID).setEnabled(canFind());
        getButton(FIND_ALL_ID).setEnabled(canFindAll());
        getButton(REPLACE_ID).setEnabled(canReplace());
        getButton(REPLACE_ALL_ID).setEnabled(canReplaceAll());
    }

    @Override
    protected Button getButton(int id) {
        Button button = super.getButton(id);
        if(button == null) {
            button = opButtons.get(Integer.valueOf(id));
        }
        return button;
    }
    
    protected boolean canFind() {
        return StringUtils.isSet(getFindText()) && operationProvider != null && operationProvider.canFind(getFindText());
    }
    
    protected boolean canFindAll() {
        return StringUtils.isSet(getFindText()) && operationProvider != null && operationProvider.canFindAll(getFindText());
    }

    protected boolean canReplace() {
        return canFind() && operationProvider.canReplace(getFindText(), getReplaceText());
    }
    
    protected boolean canReplaceAll() {
        return canFind() && operationProvider.canReplaceAll(getFindText(), getReplaceText());
    }

    protected void findPressed() {
        saveHistories();
        
        if(operationProvider == null) {
            return;
        }
        
        if(operationProvider.find(getFindText())) {
            setInfoText(EMPTY);
        }
        else {
            setInfoText(STRING_NOT_FOUND);
        }
        
        findInput.setFocus();
    }

    protected void findAllPressed() {
        saveHistories();
        
        if(operationProvider == null) {
            return;
        }
        
        boolean all = (parameter & IFindReplaceProvider.PARAM_ALL) != 0;
        operationProvider.setParameter(IFindReplaceProvider.PARAM_ALL, true);
        
        if(operationProvider.find(getFindText())) {
            setInfoText(EMPTY);
        }
        else {
            setInfoText(STRING_NOT_FOUND);
        }
        
        operationProvider.setParameter(IFindReplaceProvider.PARAM_ALL, all);
        
        findInput.setFocus();
    }

    protected void replacePressed() {
        saveHistories();
        
        if(operationProvider == null) {
            return;
        }
        
        if(operationProvider.replace(getFindText(), getReplaceText())) {
            setInfoText(EMPTY);
        }
        else {
            setInfoText(STRING_NOT_FOUND);
        }
        
        findInput.setFocus();
    }

    protected void replaceAllPressed() {
        saveHistories();
        
        if(operationProvider == null) {
            return;
        }
        
        boolean all = (parameter & IFindReplaceProvider.PARAM_ALL) != 0;
        operationProvider.setParameter(IFindReplaceProvider.PARAM_ALL, true);
        
        if(operationProvider.replace(getFindText(), getReplaceText())) {
            setInfoText(EMPTY);
        }
        else {
            setInfoText(STRING_NOT_FOUND);
        }
        
        operationProvider.setParameter(IFindReplaceProvider.PARAM_ALL, all);
        
        findInput.setFocus();
    }

    public String getFindText() {
        return findInput == null || findInput.isDisposed() ? EMPTY : findInput.getText();
    }

    public String getReplaceText() {
        return replaceInput == null || replaceInput.isDisposed() ? EMPTY : replaceInput.getText();
    }

    protected void setInfoText(String text) {
        if(infoLabel != null && !infoLabel.isDisposed()) {
            infoLabel.setText(text);
            infoLabel.getParent().layout();
        }
    }

    protected void saveHistories() {
        if(getShell() == null || getShell().isDisposed()) {
            return;
        }
        saveHistory(FindHistory, findInput);
        saveHistory(ReplaceHistory, replaceInput);
    }

    protected void saveHistory(List<String> history, Combo input) {
        String text = input.getText();
        if(text == null || EMPTY.equals(text)) {
            return;
        }
        
        if(history.remove(text) && !input.isDisposed()) {
            input.remove(text);
        }
        
        history.add(0, text);
        
        if(!input.isDisposed()) {
            input.add(text, 0);
            input.setText(text);
        }
    }
    
    @Override
    protected IDialogSettings getDialogBoundsSettings() {
        return getDialogSettings(DIALOG_ID);
    }
    
    @Override
    protected int getDialogBoundsStrategy() {
        return DIALOG_PERSISTLOCATION | DIALOG_PERSISTSIZE;
    }
    
    protected IDialogSettings getDialogSettings(String sectionName) {
        IDialogSettings settings = ArchiPlugin.INSTANCE.getDialogSettings();
        IDialogSettings section = settings.getSection(sectionName);
        if(section == null) {
            section = settings.addNewSection(sectionName);
        }
        return section;
    }
    
    private void loadParameter() {
        IDialogSettings settings = getDialogSettings(SETTINGS_SECTION_NAME);
        if(settings != null) {
            try {
                parameter = settings.getInt(P_PARAMETER);
            }
            catch(Exception e) {
            }
        }
    }

    private void saveParameter() {
        IDialogSettings settings = getDialogSettings(SETTINGS_SECTION_NAME);
        if(settings != null) {
            settings.put(P_PARAMETER, parameter);
        }
    }
    
    protected void createParameterGroups(Composite parent) {
        Composite composite = new Composite(parent, SWT.NO_FOCUS);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        GridLayout layout = new GridLayout(2, true);
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.verticalSpacing = convertVerticalDLUsToPixels(VERTICAL_SPACING);
        layout.horizontalSpacing = convertHorizontalDLUsToPixels(HORIZONTAL_SPACING);
        composite.setLayout(layout);

        Composite directionGroup = createParameterGroup(composite, 1, Messages.FindReplaceDialog_8);
        createParameterWidget(directionGroup, SWT.RADIO, Messages.FindReplaceDialog_9, IFindReplaceProvider.PARAM_FORWARD);
        createParameterWidget(directionGroup, SWT.RADIO, Messages.FindReplaceDialog_10, IFindReplaceProvider.PARAM_BACKWARD);
        
        Composite scopeGroup = createParameterGroup(composite, 1, Messages.FindReplaceDialog_11);
        createParameterWidget(scopeGroup, SWT.RADIO, Messages.FindReplaceDialog_12, IFindReplaceProvider.PARAM_SELECTED_MODEL);
        createParameterWidget(scopeGroup, SWT.RADIO, Messages.FindReplaceDialog_13, IFindReplaceProvider.PARAM_ALL_MODELS);

        Composite optionGroup = createParameterGroup(composite, 2, Messages.FindReplaceDialog_14);
        ((GridData)optionGroup.getLayoutData()).horizontalSpan = 2;
        
        createParameterWidget(optionGroup, SWT.CHECK, Messages.FindReplaceDialog_15, IFindReplaceProvider.PARAM_CASE_SENSITIVE);
        createParameterWidget(optionGroup, SWT.CHECK, Messages.FindReplaceDialog_17, IFindReplaceProvider.PARAM_INCLUDE_FOLDERS);
        createParameterWidget(optionGroup, SWT.CHECK, Messages.FindReplaceDialog_16, IFindReplaceProvider.PARAM_WHOLE_WORD);
        createParameterWidget(optionGroup, SWT.CHECK, Messages.FindReplaceDialog_18, IFindReplaceProvider.PARAM_INCLUDE_RELATIONS);
    }

    private Composite createParameterGroup(Composite composite, int numColumns, String text) {
        Group group = new Group(composite, SWT.NO_FOCUS);
        group.setText(text);
        GridData groupLayoutData = new GridData(SWT.FILL, SWT.FILL, true, true);
        group.setLayoutData(groupLayoutData);
        GridLayout groupLayout = new GridLayout(numColumns, true);
        groupLayout.horizontalSpacing = 7;
        groupLayout.verticalSpacing = 7;
        group.setLayout(groupLayout);
        return group;
    }

    protected Button createParameterWidget(Composite parent, int style, String text, int paramId) {
        Button button = new Button(parent, style);
        button.setText(text);
        button.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        addParameterWidget(paramId, button);
        button.setData(Integer.valueOf(paramId));
        button.addListener(SWT.Selection, eventHandler);
        return button;
    }

    private void addParameterWidget(final int paramId, final Button widget) {
        Integer key = Integer.valueOf(paramId);
        List<Button> widgets = paramWidgets.get(key);
        if(widgets == null) {
            widgets = new ArrayList<Button>();
            paramWidgets.put(key, widgets);
        }
        widgets.add(widget);
        paramWidgets.put(key, widgets);
        widget.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent e) {
                removeParameterWidget(paramId, widget);
            }
        });
    }

    private void removeParameterWidget(int paramId, Button widget) {
        Integer key = Integer.valueOf(paramId);
        List<Button> widgets = paramWidgets.get(key);
        if(widgets != null) {
            widgets.remove(widget);
            if(widgets.isEmpty()) {
                paramWidgets.remove(key);
            }
        }
    }

    private void updateParameterWidgets() {
        for(Integer paramId : paramWidgets.keySet()) {
            updateParameterWidget(paramId);
        }
    }

    private void updateParameterWidget(Integer paramId) {
        List<Button> widgets = paramWidgets.get(paramId);
        if(widgets != null) {
            boolean enabled = operationProvider != null && operationProvider.understandsParameter(paramId.intValue());
            for(Button widget : widgets) {
                widget.setEnabled(enabled);
                widget.setSelection(hasParameter(paramId.intValue()));
            }
        }
    }

    private boolean hasParameter(int paramId) {
        if(operationProvider == null) {
            return false;
        }
        int bit = parameter & paramId;
        return bit != 0;
    }

    @Override
    public boolean close() {
        saveHistories();
        return super.close();
    }

    // =============================== Part Listener ====================================

    @Override
    public void partActivated(IWorkbenchPart part) {
        currentPart = part;
        setOperationProvider(getOperationProvider(part));
        updateOperationButtons();
        updateParameterWidgets();
    }

    @Override
    public void partClosed(IWorkbenchPart part) {
        if(part != currentPart) {
            return;
        }

        setOperationProvider(null);
        updateOperationButtons();
        updateParameterWidgets();
    }

    @Override
    public void partBroughtToTop(IWorkbenchPart part) {
    }

    @Override
    public void partDeactivated(IWorkbenchPart part) {
    }

    @Override
    public void partOpened(IWorkbenchPart part) {
    }
    
    private void startListeningToPartChanges() {
        window.getPartService().addPartListener(this);
    }

    private void stopListeningToPartChanges() {
        window.getPartService().removePartListener(this);
    }

    private IFindReplaceProvider getOperationProvider(IWorkbenchPart part) {
        if(part == null) {
            return null;
        }
        return part.getAdapter(IFindReplaceProvider.class);
    }
    
    protected void setOperationProvider(IFindReplaceProvider operationProvider) {
        this.operationProvider = operationProvider;
        if(operationProvider != null) {
            operationProvider.setParameter(parameter);
        }
    }

}
