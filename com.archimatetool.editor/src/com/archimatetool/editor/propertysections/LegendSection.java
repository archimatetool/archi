/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.commands.FeatureCommand;
import com.archimatetool.editor.ui.components.SpinnerListener;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.IFeatures;
import com.archimatetool.model.ILegendOptions;


/**
 * Property Section for a legend
 * 
 * @author Phillip Beauvoir
 */
public class LegendSection extends AbstractECorePropertySection {
    
    private static final String HELP_ID = "com.archimatetool.help.elementPropertySection"; //$NON-NLS-1$
    
    /**
     * Filter to show or reject this section depending on input value
     */
    public static class Filter extends ObjectFilter {
        @Override
        public boolean isRequiredType(Object object) {
            return object instanceof IDiagramModelNote note && note.isLegend();
        }

        @Override
        public Class<?> getAdaptableType() {
            return IDiagramModelNote.class;
        }
    }

    private Button buttonDisplayElements;
    private Button buttonDisplayRelations;
    private Button buttonDisplaySpecializationElements;
    private Button buttonDisplaySpecializationRelations;
    
    private SpinnerListener spinnerRowsPerColumn, spinnerOffset;
    
    private Combo comboSortMethod;
    private Combo comboColorScheme;
    
    private static final String[] comboSortOptions = {
            Messages.LegendSection_15,
            Messages.LegendSection_16
    };
    
    private static final String[] comboColorOptions = {
            Messages.LegendSection_12,
            Messages.LegendSection_13,
            Messages.LegendSection_14
    };
    
    @Override
    protected void notifyChanged(Notification msg) {
        if(isFeatureNotification(msg, IDiagramModelNote.FEATURE_LEGEND)) {
            update();
        }
    }
    
    @Override
    protected void createControls(Composite parent) {
        Composite conceptsGroup = getWidgetFactory().createComposite(parent);
        GridLayoutFactory.fillDefaults().applyTo(conceptsGroup);
        GridDataFactory.create(SWT.NONE).hint(150, SWT.DEFAULT).applyTo(conceptsGroup);
        
        createLabel(conceptsGroup, Messages.LegendSection_0, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);

        buttonDisplayElements = getWidgetFactory().createButton(conceptsGroup, null, SWT.CHECK);
        buttonDisplayElements.setText(Messages.LegendSection_3);
        buttonDisplayElements.addSelectionListener(widgetSelectedAdapter(event -> {
            doCommand(Messages.LegendSection_4);
        }));
        
        buttonDisplayRelations = getWidgetFactory().createButton(conceptsGroup, null, SWT.CHECK);
        buttonDisplayRelations.setText(Messages.LegendSection_5);
        buttonDisplayRelations.addSelectionListener(widgetSelectedAdapter(event -> {
            doCommand(Messages.LegendSection_6);
        }));
        
        Composite specializationsGroup = getWidgetFactory().createComposite(parent);
        GridLayoutFactory.fillDefaults().applyTo(specializationsGroup);
        
        createLabel(specializationsGroup, Messages.LegendSection_1, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);

        buttonDisplaySpecializationElements = getWidgetFactory().createButton(specializationsGroup, null, SWT.CHECK);
        buttonDisplaySpecializationElements.setText(Messages.LegendSection_3);
        buttonDisplaySpecializationElements.addSelectionListener(widgetSelectedAdapter(event -> {
            doCommand(Messages.LegendSection_4);
        }));
        
        buttonDisplaySpecializationRelations = getWidgetFactory().createButton(specializationsGroup, null, SWT.CHECK);
        buttonDisplaySpecializationRelations.setText(Messages.LegendSection_5);
        buttonDisplaySpecializationRelations.addSelectionListener(widgetSelectedAdapter(event -> {
            doCommand(Messages.LegendSection_6);
        }));
        
        createLabel(parent, Messages.LegendSection_17, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        comboSortMethod = new Combo(parent, SWT.READ_ONLY | SWT.BORDER);
        comboSortMethod.setItems(comboSortOptions);
        getWidgetFactory().adapt(comboSortMethod, true, true);
        comboSortMethod.addSelectionListener(widgetSelectedAdapter(event -> {
            doCommand(Messages.LegendSection_18);
        }));
        
        createLabel(parent, Messages.LegendSection_2, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        comboColorScheme = new Combo(parent, SWT.READ_ONLY | SWT.BORDER);
        comboColorScheme.setItems(comboColorOptions);
        getWidgetFactory().adapt(comboColorScheme, true, true);
        comboColorScheme.addSelectionListener(widgetSelectedAdapter(event -> {
            doCommand(Messages.LegendSection_11);
        }));
        
        createLabel(parent, Messages.LegendSection_7, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        Spinner spinner = new Spinner(parent, SWT.BORDER);
        spinner.setMinimum(ILegendOptions.ROWS_PER_COLUMN_MIN);
        spinner.setMaximum(ILegendOptions.ROWS_PER_COLUMN_MAX);
        getWidgetFactory().adapt(spinner, true, true);
        spinnerRowsPerColumn = new SpinnerListener(spinner) {
            @Override
            protected void doEvent(Event event) {
                doCommand(Messages.LegendSection_8);
            }
        };
        
        createLabel(parent, Messages.LegendSection_9, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        spinner = new Spinner(parent, SWT.BORDER);
        spinner.setMinimum(ILegendOptions.WIDTH_OFFSET_MIN);
        spinner.setMaximum(ILegendOptions.WIDTH_OFFSET_MAX);
        getWidgetFactory().adapt(spinner, true, true);
        spinnerOffset = new SpinnerListener(spinner) {
            @Override
            protected void doEvent(Event event) {
                doCommand(Messages.LegendSection_10);
            }
        };
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void doCommand(String name) {
        String optionsString = ILegendOptions.create()
                                .displayElements(buttonDisplayElements.getSelection())
                                .displayRelations(buttonDisplayRelations.getSelection())
                                .displaySpecializationElements(buttonDisplaySpecializationElements.getSelection())
                                .displaySpecializationRelations(buttonDisplaySpecializationRelations.getSelection())
                                .rowsPerColumn(spinnerRowsPerColumn.getSelection())
                                .widthOffset(spinnerOffset.getSelection())
                                .colorScheme(comboColorScheme.getSelectionIndex())
                                .sortMethod(comboSortMethod.getSelectionIndex())
                                .toFeatureString();

        CompoundCommand result = new CompoundCommand();
        
        for(EObject note : getEObjects()) {
            if(isAlive(note)) {
                Command cmd = new FeatureCommand(name,
                        (IFeatures)note,
                        IDiagramModelNote.FEATURE_LEGEND,
                        optionsString,
                        null);
                if(cmd.canExecute()) {
                    result.add(cmd);
                }
            }
        }

        executeCommand(result.unwrap());
    }

    @Override
    protected void update() {
        if(isExecutingCommand()) {
            return;
        }
        
        IDiagramModelNote firstSelected = (IDiagramModelNote)getFirstSelectedObject();
        boolean isLocked = isLocked(firstSelected);
        
        ILegendOptions options = firstSelected.getLegendOptions();
        
        buttonDisplayElements.setSelection(options.displayElements());
        buttonDisplayElements.setEnabled(!isLocked);
        
        buttonDisplayRelations.setSelection(options.displayRelations());
        buttonDisplayRelations.setEnabled(!isLocked);
        
        buttonDisplaySpecializationElements.setSelection(options.displaySpecializationElements());
        buttonDisplaySpecializationElements.setEnabled(!isLocked);
        
        buttonDisplaySpecializationRelations.setSelection(options.displaySpecializationRelations());
        buttonDisplaySpecializationRelations.setEnabled(!isLocked);
        
        spinnerRowsPerColumn.setSelection(options.getRowsPerColumn());
        spinnerRowsPerColumn.getSpinner().setEnabled(!isLocked);
        
        spinnerOffset.setSelection(options.getWidthOffset());
        spinnerOffset.getSpinner().setEnabled(!isLocked);
        
        comboSortMethod.select(options.getSortMethod());
        comboSortMethod.setEnabled(!isLocked);

        comboColorScheme.select(options.getColorScheme());
        comboColorScheme.setEnabled(!isLocked);
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
