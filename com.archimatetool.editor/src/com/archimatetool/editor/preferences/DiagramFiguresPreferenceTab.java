/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.archimatetool.editor.ui.FigureChooser;
import com.archimatetool.model.IArchimatePackage;


/**
 * Default Figures Preferences Tab panel
 * 
 * @author Phillip Beauvoir
 */
public class DiagramFiguresPreferenceTab implements IPreferenceConstants {
    
    private List<ImageChoice> fChoices = new ArrayList<ImageChoice>();
    
    private TableViewer fTableViewer;
    
    private class ImageChoice {
        String name;
        String preferenceKey;
        int chosenType = 0;
        Image[] images;
        
        ImageChoice(String name, String preferenceKey, EClass eClass) {
            this.name = name;
            this.preferenceKey = preferenceKey;
            images = FigureChooser.getFigurePreviewImagesForClass(eClass);
            chosenType = Preferences.STORE.getInt(preferenceKey);
        }
        
        Image getImage() {
            return images[chosenType];
        }
        
        void flip() {
            chosenType = (chosenType == 0) ? 1 : 0;
        }
    }
    
    public Composite createContents(Composite parent) {
        loadFigures();
        
        Composite client = new Composite(parent, SWT.NULL);
        client.setLayout(new GridLayout());
        
        GridLayout gridLayout = new GridLayout();
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        
        Composite tableClient = new Composite(client, SWT.NULL);
        tableClient.setLayout(gridLayout);
        tableClient.setLayoutData(new GridData(GridData.FILL_BOTH));
        
        Label label = new Label(tableClient, SWT.NULL);
        label.setText(Messages.DiagramFiguresPreferencePage_0);
        
        Composite client2 = new Composite(tableClient, SWT.NULL);
        client2.setLayout(new TableColumnLayout());
        
        GridData gd = new GridData(GridData.FILL_BOTH);
        gd.heightHint = 80; // need this to set a smaller height
        gd.widthHint = 80;  // need this to stop it getting larger when the splitter is resized in the Prefs dialog
        client2.setLayoutData(gd);
        
        createTable(client2);
        
        fTableViewer.setInput(fChoices);
        
        return client;
    }
    
    private void loadFigures() {
        fChoices.add(new ImageChoice(Messages.DiagramFiguresPreferencePage_7,
                BUSINESS_PROCESS_FIGURE, IArchimatePackage.eINSTANCE.getBusinessProcess()));
        
        fChoices.add(new ImageChoice(Messages.DiagramFiguresPreferencePage_1,
                BUSINESS_INTERFACE_FIGURE, IArchimatePackage.eINSTANCE.getBusinessInterface()));
        
        fChoices.add(new ImageChoice(Messages.DiagramFiguresPreferenceTab_0,
                BUSINESS_SERVICE_FIGURE, IArchimatePackage.eINSTANCE.getBusinessService()));


        fChoices.add(new ImageChoice(Messages.DiagramFiguresPreferencePage_2,
                APPLICATION_COMPONENT_FIGURE, IArchimatePackage.eINSTANCE.getApplicationComponent()));
        
        fChoices.add(new ImageChoice(Messages.DiagramFiguresPreferencePage_3,
                APPLICATION_INTERFACE_FIGURE, IArchimatePackage.eINSTANCE.getApplicationInterface()));
        
        fChoices.add(new ImageChoice(Messages.DiagramFiguresPreferenceTab_1,
                APPLICATION_SERVICE_FIGURE, IArchimatePackage.eINSTANCE.getApplicationService()));

        
        fChoices.add(new ImageChoice(Messages.DiagramFiguresPreferencePage_4,
                TECHNOLOGY_DEVICE_FIGURE, IArchimatePackage.eINSTANCE.getDevice()));
        
        fChoices.add(new ImageChoice(Messages.DiagramFiguresPreferencePage_5,
                TECHNOLOGY_NODE_FIGURE, IArchimatePackage.eINSTANCE.getNode()));
        
        fChoices.add(new ImageChoice(Messages.DiagramFiguresPreferencePage_6,
                TECHNOLOGY_INTERFACE_FIGURE, IArchimatePackage.eINSTANCE.getInfrastructureInterface()));

        fChoices.add(new ImageChoice(Messages.DiagramFiguresPreferenceTab_2,
                TECHNOLOGY_SERVICE_FIGURE, IArchimatePackage.eINSTANCE.getInfrastructureService()));
    }
    
    private void createTable(Composite parent) {
        fTableViewer = new TableViewer(parent, SWT.BORDER);
        
        TableColumnLayout layout = (TableColumnLayout)parent.getLayout();
        TableViewerColumn column = new TableViewerColumn(fTableViewer, SWT.NONE);
        layout.setColumnData(column.getColumn(), new ColumnWeightData(100, false));
        
        fTableViewer.setContentProvider(new IStructuredContentProvider() {
            @Override
            public void dispose() {
            }

            @Override
            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

            @Override
            public Object[] getElements(Object inputElement) {
                return ((List<?>)inputElement).toArray();
            }
        });
        
        class TableLabelProvider extends LabelProvider {
            @Override
            public Image getImage(Object element) {
                return ((ImageChoice)element).getImage();
            }

            @Override
            public String getText(Object element) {
                return ((ImageChoice)element).name;
            }
        }
        
        fTableViewer.setLabelProvider(new TableLabelProvider());
        
        fTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                ImageChoice ic = (ImageChoice)((IStructuredSelection)event.getSelection()).getFirstElement();
                if(ic != null) {
                    ic.flip();
                    fTableViewer.update(ic, null);
                }
            }
        });
    }
    
    protected boolean performOk() {
        for(ImageChoice choice : fChoices) {
            Preferences.STORE.setValue(choice.preferenceKey, choice.chosenType);
        }

        return true;
    }
    
    protected void performDefaults() {
        for(ImageChoice choice : fChoices) {
            choice.chosenType = 0;
        }
        
        fTableViewer.refresh();
    }
}