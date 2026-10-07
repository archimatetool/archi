/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.ui.FigureImagePreviewFactory;
import com.archimatetool.editor.ui.factory.IArchimateElementUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;
import com.archimatetool.model.util.ArchimateModelUtils;


/**
 * Default Figures Preferences Page
 * 
 * @author Phillip Beauvoir
 */
public class DiagramFiguresPreferencePage extends PreferencePage
implements IWorkbenchPreferencePage, IPreferenceConstants {
    
    private static String HELP_ID = "com.archimatetool.help.prefsDiagram"; //$NON-NLS-1$
    
    private final List<ImageChoice> imageChoices = new ArrayList<>();
    
    private TableViewer tableViewer;
    
    private final int ITEM_WIDTH = 180;
    private final int ITEM_HEIGHT = 72;
    private final int ALPHA = 100;
    private final Color HILITE_COLOR = new Color(78, 178, 255);
    
    private static class ImageChoice {
        private final String preferenceKey;
        private int chosenType = 0;
        private final Image[] images;
        
        ImageChoice(EClass eClass) {
            preferenceKey = IPreferenceConstants.DEFAULT_FIGURE_PREFIX + eClass.getName();
            images = new Image[] { FigureImagePreviewFactory.getPreviewImage(eClass, 0),
                                   FigureImagePreviewFactory.getPreviewImage(eClass, 1) };
            chosenType = ArchiPlugin.getInstance().getPreferenceStore().getInt(preferenceKey);
        }
        
        String preferenceKey() { return preferenceKey; }
        int chosenType() { return chosenType; }
        void setChosenType(int chosenType) { this.chosenType = chosenType; }
        Image getImage(int index) { return images[index]; }
        
        void dispose() {
            images[0].dispose();
            images[1].dispose();
        }
    }
    
    public DiagramFiguresPreferencePage() {
        setPreferenceStore(ArchiPlugin.getInstance().getPreferenceStore());
    }
    
    @Override
    public Composite createContents(Composite parent) {
        // Help
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);

        loadFigures();
        
        Composite client = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout.marginWidth = layout.marginHeight = 0;
        client.setLayout(layout);
        
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
        
        // Need this to stop it getting taller when the splitter is resized in the Prefs dialog
        GridDataFactory.create(GridData.FILL_BOTH).hint(SWT.DEFAULT, 200).applyTo(client2);
        
        createTable(client2);
        
        tableViewer.setInput(imageChoices);
        
        // Weird bug on Windows where the table and scroll bars are sometimes not drawn correctly
        Display.getCurrent().asyncExec(() -> {
            client2.layout();
        });
        
        return client;
    }
    
    private void loadFigures() {
        // Find Providers that have alternate figures
        for(EClass eClass : ArchimateModelUtils.getAllArchimateClasses()) {
            if(ObjectUIFactory.INSTANCE.getProviderForClass(eClass) instanceof IArchimateElementUIProvider uiProvider
                                                                                && uiProvider.hasAlternateFigure()) {
                imageChoices.add(new ImageChoice(eClass));
            }
        }
    }
    
    private void createTable(Composite parent) {
        tableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
        
        TableColumnLayout layout = (TableColumnLayout)parent.getLayout();
        TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.NONE);
        layout.setColumnData(column.getColumn(), new ColumnWeightData(100, false));
        
        // Fix row height
        // This is definitely needed on some Linux versions where the row height is stuck at 17 for some reason
        tableViewer.getTable().addListener(SWT.MeasureItem, event -> {
            event.height = ITEM_HEIGHT;
        });
        
        tableViewer.getTable().addListener(SWT.PaintItem, event -> {
            TableItem item = (TableItem)event.item;
            if(item == null) {
                return;
            }

            event.gc.setAntialias(SWT.ON);

            int row = tableViewer.getTable().indexOf(item);

            ImageChoice imageChoice= imageChoices.get(row);

            Image image1 = imageChoice.getImage(0);
            int x = (ITEM_WIDTH / 2) - (image1.getBounds().width / 2);
            event.gc.setAlpha(imageChoice.chosenType() == 0 ? 255 : ALPHA);
            event.gc.drawImage(image1, event.x + x, event.y + (ITEM_HEIGHT - image1.getBounds().height) / 2);

            Image image2 = imageChoice.getImage(1);
            x = ITEM_WIDTH + ((ITEM_WIDTH / 2) - (image2.getBounds().width / 2));
            event.gc.setAlpha(imageChoice.chosenType() == 1 ? 255 : ALPHA);
            event.gc.drawImage(image2, event.x + x, event.y + (ITEM_HEIGHT - image2.getBounds().height) / 2);

            // Highlight rectangle
            int highlight_x = imageChoice.chosenType() == 0 ? 20 : ITEM_WIDTH + 20;
            event.gc.setForeground(HILITE_COLOR);
            event.gc.setAlpha(255);
            event.gc.setLineWidth(2);
            event.gc.drawRoundRectangle(event.x + highlight_x, event.y + 2, event.x + ITEM_WIDTH - 39, ITEM_HEIGHT - 3,
                    15, 15);
        });
        
        tableViewer.getTable().addListener(SWT.EraseItem, event -> {
            // No selection or focus highlighting
            event.detail &= ~(SWT.FOCUSED | SWT.HOT | SWT.SELECTED);
        });
        
        tableViewer.getTable().addListener(SWT.MouseDown, event -> {
            TableItem item = tableViewer.getTable().getItem(new Point(event.x, event.y));
            if(item == null) {
                return;
            }

            int row = tableViewer.getTable().indexOf(item);
            ImageChoice imageChoice = imageChoices.get(row);

            if(event.x < ITEM_WIDTH) {
                imageChoice.setChosenType(0);
            }
            else {
                imageChoice.setChosenType(1);
            }

            tableViewer.refresh(imageChoice);
        });

        tableViewer.setContentProvider(new IStructuredContentProvider() {
            @Override
            public Object[] getElements(Object inputElement) {
                return ((List<?>)inputElement).toArray();
            }
        });
        
        tableViewer.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {
                return null;
            }
        });
    }
    
    @Override
    public boolean performOk() {
        for(ImageChoice choice : imageChoices) {
            ArchiPlugin.getInstance().getPreferenceStore().setValue(choice.preferenceKey(), choice.chosenType());
        }

        return true;
    }
    
    @Override
    protected void performDefaults() {
        for(ImageChoice choice : imageChoices) {
            choice.setChosenType(0);
        }
        
        tableViewer.getTable().redraw();
        
        super.performDefaults();
    }
    
    @Override
    public void init(IWorkbench workbench) {
    }

    @Override
    public void dispose() {
        super.dispose();
        
        for(ImageChoice imageChoice : imageChoices) {
            imageChoice.dispose();
        }
    }
}