/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.editor.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

/**
 * Diagram Figures Preferences Page
 * 
 * @author Phillip Beauvoir
 */
public class DiagramFiguresPreferencePage
extends PreferencePage
implements IWorkbenchPreferencePage, IPreferenceConstants
{
    public static String HELPID = "uk.ac.bolton.archimate.help.prefsFigures"; //$NON-NLS-1$
    
    private Button fButtonApplicationComponent1, fButtonApplicationComponent2;
    private Button fButtonTechnologyDevice1, fButtonTechnologyDevice2;
    private Button fButtonTechnologyNode1, fButtonTechnologyNode2;
    
    private List<Image> fImages = new ArrayList<Image>();
    
	/**
	 * Constructor
	 */
	public DiagramFiguresPreferencePage() {
		setPreferenceStore(Preferences.STORE);
		setDescription("Choose the default figures to use in a View.\n(Views will need to be closed and re-opened to take effect.)");
	}
	

    @Override
    protected Control createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELPID);

        Composite client = new Composite(parent, SWT.NULL);
        client.setLayout(new GridLayout());
        
        // APPLICATION COMPONENT
        Group group = createGroup(client, "Application Component");
        fButtonApplicationComponent1 = createButton(group, "ac1.png", false);
        createSeparator(group);
        fButtonApplicationComponent2 = createButton(group, "ac2.png", true);
        
        // TECHNOLOGY DEVICE
        group = createGroup(client, "Technology Device");
        fButtonTechnologyDevice1 = createButton(group, "td1.png", false);
        createSeparator(group);
        fButtonTechnologyDevice2 = createButton(group, "td2.png", true);
        
        // TECHNOLOGY NODE
        group = createGroup(client, "Technology Node");
        fButtonTechnologyNode1 = createButton(group, "tn1.png", false);
        createSeparator(group);
        fButtonTechnologyNode2 = createButton(group, "tn2.png", true);
        
        setValues();
        
        return client;
    }
    
    private Group createGroup(Composite parent, String title) {
        Group group = new Group(parent, SWT.NULL);
        group.setText(title);
        group.setLayout(new GridLayout(5, false));
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        return group;
    }
    
    private Button createButton(Group group, String imageName, boolean end) {
        Button button = new Button(group, SWT.RADIO);
        
        if(end) {
            GridData gd = new GridData(GridData.END, GridData.CENTER, true, false);
            button.setLayoutData(gd);
        }
        
        Label label = new Label(group, SWT.NULL);
        Image image = new Image(getShell().getDisplay(),
                DiagramFiguresPreferencePage.class.getResourceAsStream(imageName));
        label.setImage(image);
        fImages.add(image);
        return button;
    }
    
    private Label createSeparator(Group group) {
        Label label = new Label(group, SWT.NULL);
        label.setText("or");
        GridData gd = new GridData(GridData.END, GridData.CENTER, true, false);
        label.setLayoutData(gd);
        return label;
    }
    
    private void setValues() {
        int type = getPreferenceStore().getInt(APPLICATION_COMPONENT_FIGURE);
        fButtonApplicationComponent1.setSelection(type == 0);
        fButtonApplicationComponent2.setSelection(type == 1);
        
        type = getPreferenceStore().getInt(TECHNOLOGY_DEVICE_FIGURE);
        fButtonTechnologyDevice1.setSelection(type == 0);
        fButtonTechnologyDevice2.setSelection(type == 1);
        
        type = getPreferenceStore().getInt(TECHNOLOGY_NODE_FIGURE);
        fButtonTechnologyNode1.setSelection(type == 0);
        fButtonTechnologyNode2.setSelection(type == 1);
    }
    
    @Override
    public boolean performOk() {
        int type = fButtonApplicationComponent1.getSelection() ? 0 : 1;
        getPreferenceStore().setValue(APPLICATION_COMPONENT_FIGURE, type);
        
        type = fButtonTechnologyDevice1.getSelection() ? 0 : 1;
        getPreferenceStore().setValue(TECHNOLOGY_DEVICE_FIGURE, type);
        
        type = fButtonTechnologyNode1.getSelection() ? 0 : 1;
        getPreferenceStore().setValue(TECHNOLOGY_NODE_FIGURE, type);
        
        return true;
    }
    
    @Override
    protected void performDefaults() {
        fButtonApplicationComponent1.setSelection(true);
        fButtonApplicationComponent2.setSelection(false);
        
        fButtonTechnologyDevice1.setSelection(true);
        fButtonTechnologyDevice2.setSelection(false);
        
        fButtonTechnologyNode1.setSelection(true);
        fButtonTechnologyNode2.setSelection(false);
        
        super.performDefaults();
    }

    public void init(IWorkbench workbench) {
    }
    
    @Override
    public void dispose() {
        super.dispose();
        for(Image image : fImages) {
            if(!image.isDisposed()) {
                image.dispose();
            }
        }
    }
}