/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.preferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;

import com.archimatetool.editor.ui.FigureImagePreviewFactory;
import com.archimatetool.editor.ui.factory.IArchimateElementUIProvider;
import com.archimatetool.editor.ui.factory.IObjectUIProvider;
import com.archimatetool.editor.ui.factory.ObjectUIFactory;


/**
 * Default Figures Preferences Tab panel
 * 
 * @author Phillip Beauvoir
 */
public class DiagramFiguresPreferenceTab implements IPreferenceConstants {
    
    private List<ImageChoice> fChoices = new ArrayList<ImageChoice>();
    
    private TableViewer fTableViewer;
    
    private final int itemWidth = 180;
    private final int itemHeight = 72;
    
    private class ImageChoice {
        String name;
        String preferenceKey;
        int chosenType = 0;
        Image[] images = new Image[2];
        
        ImageChoice(IObjectUIProvider provider) {
            name = provider.getDefaultName();
            this.preferenceKey = IPreferenceConstants.DEFAULT_FIGURE_PREFIX + provider.providerFor().getName();
            images[0] = FigureImagePreviewFactory.getPreviewImage(provider.providerFor(), 0);
            images[1] = FigureImagePreviewFactory.getPreviewImage(provider.providerFor(), 1);
            chosenType = Preferences.STORE.getInt(preferenceKey);
        }
        
        Image getImage(int index) {
            return images[index];
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
        // Find Providers that have alternate figures
        for(IObjectUIProvider provider : ObjectUIFactory.INSTANCE.getProviders()) {
            if(provider instanceof IArchimateElementUIProvider && ((IArchimateElementUIProvider)provider).hasAlternateFigure()) {
                fChoices.add(new ImageChoice(provider));
            }
        }
        
        // Sort them by name
        Collections.sort(fChoices, new Comparator<ImageChoice>() {
            @Override
            public int compare(ImageChoice o1, ImageChoice o2) {
                return o1.name.compareTo(o2.name);
            }
        });
    }
    
    private void createTable(Composite parent) {
        fTableViewer = new TableViewer(parent, SWT.BORDER | SWT.FULL_SELECTION);
        
        
        TableColumnLayout layout = (TableColumnLayout)parent.getLayout();
        TableViewerColumn column = new TableViewerColumn(fTableViewer, SWT.NONE);
        layout.setColumnData(column.getColumn(), new ColumnWeightData(100, false));
        
        // Fix row height
        // This is definitely needed on some Linux versions where the row height is stuck at 17 for some reason
        fTableViewer.getTable().addListener(SWT.MeasureItem, new Listener() {
            @Override
            public void handleEvent(Event event) {
                event.height = itemHeight;
             }
        });
        
        fTableViewer.getTable().addListener(SWT.PaintItem, new Listener() {
            @Override
            public void handleEvent(Event event) {
                TableItem item = (TableItem)event.item;
                int row = fTableViewer.getTable().indexOf(item);
                
                ImageChoice ic = fChoices.get(row);
                
                Image image1 = ic.getImage(0);
                int x = (itemWidth / 2) - (image1.getBounds().width / 2);
                event.gc.setAlpha(ic.chosenType == 0 ? 255 : 70);
                event.gc.drawImage(image1, event.x + x, event.y + (itemHeight - image1.getBounds().height) / 2);
                
                Image image2 = ic.getImage(1);
                x = itemWidth + ((itemWidth / 2) - (image2.getBounds().width / 2));
                event.gc.setAlpha(ic.chosenType == 1 ? 255 : 70);
                event.gc.drawImage(image2, event.x + x, event.y + (itemHeight - image2.getBounds().height) / 2);
                
                // Highlight rectangle
                //int highlight_x = ic.chosenType == 0 ? 20 : itemWidth + 20;
                //event.gc.setForeground(ColorConstants.gray);
                //event.gc.setLineWidth(2);
                //event.gc.drawRectangle(event.x + highlight_x, event.y + 2, event.x + itemWidth - 39, itemHeight - 3);
             }
        });
        
        fTableViewer.getTable().addListener(SWT.EraseItem, new Listener() {   
            @Override
            public void handleEvent(Event event) {
                // No selection highlighting
                event.detail &= ~SWT.SELECTED;
                event.detail &= ~SWT.HOT;
            }
        });
        
        fTableViewer.getTable().addListener(SWT.MouseDown, new Listener() {   
            @Override
            public void handleEvent(Event event) {
                TableItem item = fTableViewer.getTable().getItem(new Point(event.x, event.y));
                int row = fTableViewer.getTable().indexOf(item);
                ImageChoice ic = fChoices.get(row);
                
                if(event.x < itemWidth) {
                    ic.chosenType = 0;
                }
                else {
                    ic.chosenType = 1;
                }
                
                fTableViewer.refresh(ic);
            }
        });

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
        
        fTableViewer.setLabelProvider(new LabelProvider() {
            @Override
            public String getText(Object element) {
                return null;
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
        
        fTableViewer.getTable().redraw();
    }
}