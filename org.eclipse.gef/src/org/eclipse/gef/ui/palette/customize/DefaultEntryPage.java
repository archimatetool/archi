/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.ui.palette.customize;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import org.eclipse.draw2d.FigureUtilities;

import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.ui.palette.PaletteMessages;

/**
 * This is a default implementation of the {@link EntryPage} interface. It
 * displays the entry's label, description and visible fields (and allows for
 * their modification). It is live in the sense that the model is updated with
 * the changes immediately (on every keystroke).
 * 
 * @author Pratik Shah
 */
public class DefaultEntryPage implements EntryPage {

    private EntryPageContainer container;

    /**
     * This is the panel that is created by
     * {@link #createControl(Composite, PaletteEntry)}. It will be
     * <code>null</code> if that method hasn't been invoked yet.
     */
    private Composite panel;
    /**
     * This is the <code>PaletteEntry</code> that this page represents. It will
     * be <code>null</code> until
     * {@link #createControl(Composite, PaletteEntry)} is invoked.
     */
    private PaletteEntry entry;

    /**
     * Being live, this method is completely ignored. Model is updated with
     * every keystroke. So, there is no need to wait for this method to be
     * called to actually make the changes to the model.
     */
    @Override
    public final void apply() {
    }

    /**
     * @see org.eclipse.gef.ui.palette.customize.EntryPage#createControl(Composite,
     *      PaletteEntry)
     */
    @Override
    public void createControl(Composite parent, PaletteEntry entry) {
        this.entry = entry;

        panel = new Composite(parent, SWT.NONE);
        panel.setFont(parent.getFont());
        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        panel.setLayout(gridLayout);
        Control[] tablist = new Control[3];

        createLabel(panel, SWT.NONE, PaletteMessages.NAME_LABEL);
        tablist[0] = createNameText(panel);

        createLabel(panel, SWT.NONE, PaletteMessages.DESCRIPTION_LABEL);
        tablist[1] = createDescText(panel);

        tablist[2] = createHiddenCheckBox(panel);

        panel.setTabList(tablist);
    }

    /**
     * Creates the <code>Text</code> where the description of the entry is to be
     * displayed.
     * 
     * @param panel
     *            The Composite in which the <code>Text</code> is to be created
     * @return The newly created <code>Text</code>
     */
    protected Text createDescText(Composite panel) {
        String desc = entry.getDescription();
        Text description = createText(panel, SWT.MULTI | SWT.WRAP | SWT.BORDER
                | SWT.V_SCROLL, desc);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 150;
        data.heightHint = description.computeTrim(0, 0, 10, FigureUtilities
                .getFontMetrics(description.getFont()).getHeight() * 2).height;
        description.setLayoutData(data);
        if (getPermission() >= PaletteEntry.PERMISSION_LIMITED_MODIFICATION) {
            description.addModifyListener(new ModifyListener() {
                @Override
                public void modifyText(ModifyEvent e) {
                    handleDescriptionChanged(((Text) e.getSource()).getText());
                }
            });
        }
        return description;
    }

    /**
     * Creates the <code>Button</code> (CheckBox) for indicating the hidden
     * status of the entry. It initializes it with the current hidden state of
     * entry.
     * 
     * @param panel
     *            The Composite in which the Button is to be created
     * @return The newly created Button
     */
    protected Button createHiddenCheckBox(Composite panel) {
        Button hidden = new Button(panel, SWT.CHECK);
        hidden.setFont(panel.getFont());
        hidden.setText(PaletteMessages.HIDDEN_LABEL);
        hidden.setSelection(!entry.isVisible());

        if (getPermission() == PaletteEntry.PERMISSION_NO_MODIFICATION) {
            hidden.setEnabled(false);
        } else {
            hidden.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    handleHiddenSelected(((Button) e.getSource())
                            .getSelection());
                }
            });
        }

        return hidden;
    }

    /**
     * Creates a label
     * 
     * @param panel
     *            The Composite in which the Label is to be created
     * @param style
     *            The stylebits for the Label
     * @param text
     *            The Label's text
     * @return Label - The newly created Label
     */
    protected Label createLabel(Composite panel, int style, String text) {
        Label label = new Label(panel, style);
        label.setFont(panel.getFont());
        label.setText(text);
        return label;
    }

    /**
     * Creates the Text where the name of the entry is to be displayed.
     * 
     * @param panel
     *            The Composite in which the Text is to be created
     * @return Text - The newly created Text
     */
    protected Text createNameText(Composite panel) {
        Text name = createText(panel, SWT.SINGLE | SWT.BORDER, entry.getLabel());
        if (getPermission() >= PaletteEntry.PERMISSION_LIMITED_MODIFICATION) {
            name.addModifyListener(new ModifyListener() {
                @Override
                public void modifyText(ModifyEvent e) {
                    handleNameChanged(((Text) e.getSource()).getText());
                }
            });
        }
        name.setVisible(true);
        return name;
    }

    /**
     * Creates a <code>Text</code>. This method is mainly a result of
     * code-factoring.
     * 
     * @param panel
     *            The Composite in which the Text is to be created
     * @param style
     *            The stylebits for the Text
     * @param text
     *            The text to be displayed in the Text
     * @return a text widget with griddata constraint
     */
    protected Text createText(Composite panel, int style, String text) {
        if (getEntry() instanceof PaletteSeparator
                || getPermission() < PaletteEntry.PERMISSION_LIMITED_MODIFICATION) {
            style = style | SWT.READ_ONLY;
        }

        Text textBox = new Text(panel, style);
        textBox.setFont(panel.getFont());
        if (text != null)
            textBox.setText(text);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        data.widthHint = 200;
        textBox.setLayoutData(data);
        return textBox;
    }

    /**
     * @see org.eclipse.gef.ui.palette.customize.EntryPage#getControl()
     */
    @Override
    public Control getControl() {
        return panel;
    }

    /**
     * Provides sub-classes with access to the entry this class is monitoring.
     * 
     * @return PaletteEntry - The entry this class is monitoring
     */
    protected PaletteEntry getEntry() {
        return entry;
    }

    /**
     * Sub-classes should override this method to provide appropriate error
     * notification messages.
     * 
     * @return The message to be used when notifying listeners about a state
     *         change
     */
    protected String getMessage() {
        return ""; //$NON-NLS-1$
    }

    /**
     * @return The <code>EntryPageContainer</code> to which this page can report
     *         errors.
     */
    protected EntryPageContainer getPageContainer() {
        return container;
    }

    /**
     * <p>
     * Updates the model with the change in the entry's description, and updates
     * the state of the page.
     * </p>
     * <p>
     * This method is invoked on every keystroke in the Text displaying the
     * description of the entry.
     * </p>
     * 
     * @param text
     *            The new description
     */
    protected void handleDescriptionChanged(String text) {
        getEntry().setDescription(text.trim());
    }

    /**
     * <p>
     * Updates the model with the change in the entry's hidden state, and
     * updates the state of the page.
     * </p>
     * <p>
     * This method is invokes whenever the "Hidden" checkbox is selected.
     * </p>
     * 
     * @param isChecked
     *            The new selection value
     */
    protected void handleHiddenSelected(boolean isChecked) {
        getEntry().setVisible(!isChecked);
    }

    /**
     * <p>
     * Updates the model with the change in the entry's name, and updates the
     * state of the page.
     * </p>
     * <p>
     * This method is invoked on every keystroke in the Text displaying the
     * entry's name.
     * </p>
     * 
     * @param text
     *            The new name
     */
    protected void handleNameChanged(String text) {
        getEntry().setLabel(text.trim());
    }

    /**
     * @return the user permission on the entry
     */
    protected int getPermission() {
        return getEntry().getUserModificationPermission();
    }

    /**
     * @see org.eclipse.gef.ui.palette.customize.EntryPage#setPageContainer(EntryPageContainer)
     */
    @Override
    public void setPageContainer(EntryPageContainer pageContainer) {
        container = pageContainer;
    }

}
