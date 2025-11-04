/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.propertysections;

import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.PlatformUI;

import com.archimatetool.editor.model.commands.FeatureCommand;
import com.archimatetool.editor.ui.components.SpinnerListener;
import com.archimatetool.model.IDiagramModelNote;
import com.archimatetool.model.IFeatures;


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

    private Button buttonShowElements;
    private Button buttonShowRelations;
    
    private Button buttonShowElementsSpecializations;
    private Button buttonShowRelationsSpecializations;
    
    private Button buttonUseColors;
    
    private SpinnerListener spinnerRowsPerColumn, spinnerOffset;
    
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

        buttonShowElements = getWidgetFactory().createButton(conceptsGroup, null, SWT.CHECK);
        buttonShowElements.setText(Messages.LegendSection_3);
        buttonShowElements.addSelectionListener(widgetSelectedAdapter(event -> {
            doCommand(Messages.LegendSection_4);
        }));
        
        buttonShowRelations = getWidgetFactory().createButton(conceptsGroup, null, SWT.CHECK);
        buttonShowRelations.setText(Messages.LegendSection_5);
        buttonShowRelations.addSelectionListener(widgetSelectedAdapter(event -> {
            doCommand(Messages.LegendSection_6);
        }));
        
        Composite specializationsGroup = getWidgetFactory().createComposite(parent);
        GridLayoutFactory.fillDefaults().applyTo(specializationsGroup);
        
        createLabel(specializationsGroup, Messages.LegendSection_1, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);

        buttonShowElementsSpecializations = getWidgetFactory().createButton(specializationsGroup, null, SWT.CHECK);
        buttonShowElementsSpecializations.setText(Messages.LegendSection_3);
        buttonShowElementsSpecializations.addSelectionListener(widgetSelectedAdapter(event -> {
            doCommand(Messages.LegendSection_4);
        }));
        
        buttonShowRelationsSpecializations = getWidgetFactory().createButton(specializationsGroup, null, SWT.CHECK);
        buttonShowRelationsSpecializations.setText(Messages.LegendSection_5);
        buttonShowRelationsSpecializations.addSelectionListener(widgetSelectedAdapter(event -> {
            doCommand(Messages.LegendSection_6);
        }));
        
        createLabel(parent, Messages.LegendSection_7, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        Spinner spinner = new Spinner(parent, SWT.BORDER);
        spinner.setMinimum(IDiagramModelNote.LEGEND_ROWS_MIN);
        spinner.setMaximum(IDiagramModelNote.LEGEND_ROWS_MAX);
        getWidgetFactory().adapt(spinner, true, true);
        spinnerRowsPerColumn = new SpinnerListener(spinner) {
            @Override
            protected void doEvent(Event event) {
                doCommand(Messages.LegendSection_8);
            }
        };
        
        createLabel(parent, Messages.LegendSection_9, ITabbedLayoutConstants.STANDARD_LABEL_WIDTH, SWT.CENTER);
        spinner = new Spinner(parent, SWT.BORDER);
        spinner.setMinimum(IDiagramModelNote.LEGEND_OFFSET_MIN);
        spinner.setMaximum(IDiagramModelNote.LEGEND_OFFSET_MAX);
        getWidgetFactory().adapt(spinner, true, true);
        spinnerOffset = new SpinnerListener(spinner) {
            @Override
            protected void doEvent(Event event) {
                doCommand(Messages.LegendSection_10);
            }
        };
        
        buttonUseColors = getWidgetFactory().createButton(parent, null, SWT.CHECK);
        buttonUseColors.setText(Messages.LegendSection_2);
        buttonUseColors.addSelectionListener(widgetSelectedAdapter(event -> {
            doCommand(Messages.LegendSection_11);
        }));
        
        // Help ID
        PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, HELP_ID);
    }
    
    private void doCommand(String name) {
        // Create display state options as a map
        Map<Integer, Boolean> options = Map.of(IDiagramModelNote.LEGEND_DISPLAY_ELEMENTS, buttonShowElements.getSelection(),
                                        IDiagramModelNote.LEGEND_DISPLAY_RELATIONS, buttonShowRelations.getSelection(),
                                        IDiagramModelNote.LEGEND_DISPLAY_SPECIALIZATION_ELEMENTS, buttonShowElementsSpecializations.getSelection(),
                                        IDiagramModelNote.LEGEND_DISPLAY_SPECIALIZATION_RELATIONS, buttonShowRelationsSpecializations.getSelection(),
                                        IDiagramModelNote.LEGEND_USE_COLORS, buttonUseColors.getSelection());
        
        String displayState = IDiagramModelNote.createLegendOptionsString(options, spinnerRowsPerColumn.getSelection(), spinnerOffset.getSelection());

        CompoundCommand result = new CompoundCommand();
        
        for(EObject note : getEObjects()) {
            if(isAlive(note)) {
                Command cmd = new FeatureCommand(name,
                        (IFeatures)note,
                        IDiagramModelNote.FEATURE_LEGEND,
                        displayState,
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
        
        int options = firstSelected.getLegendDisplayOptions();
        boolean showElements = (options & IDiagramModelNote.LEGEND_DISPLAY_ELEMENTS) != 0;
        boolean showRelations = (options & IDiagramModelNote.LEGEND_DISPLAY_RELATIONS) != 0;
        boolean showElementsSpecializations = (options & IDiagramModelNote.LEGEND_DISPLAY_SPECIALIZATION_ELEMENTS) != 0;
        boolean showRelationsSpecializations = (options & IDiagramModelNote.LEGEND_DISPLAY_SPECIALIZATION_RELATIONS) != 0;
        boolean useColors = (options & IDiagramModelNote.LEGEND_USE_COLORS) != 0;
        
        buttonShowElements.setSelection(showElements);
        buttonShowElements.setEnabled(!isLocked);
        
        buttonShowRelations.setSelection(showRelations);
        buttonShowRelations.setEnabled(!isLocked);
        
        buttonShowElementsSpecializations.setSelection(showElementsSpecializations);
        buttonShowElementsSpecializations.setEnabled(!isLocked);
        
        buttonShowRelationsSpecializations.setSelection(showRelationsSpecializations);
        buttonShowRelationsSpecializations.setEnabled(!isLocked);
        
        spinnerRowsPerColumn.setSelection(firstSelected.getLegendRowsPerColumn());
        spinnerRowsPerColumn.getSpinner().setEnabled(!isLocked);
        
        spinnerOffset.setSelection(firstSelected.getLegendOffset());
        spinnerOffset.getSpinner().setEnabled(!isLocked);
        
        buttonUseColors.setSelection(useColors);
        buttonUseColors.setEnabled(!isLocked);
    }
    
    @Override
    protected IObjectFilter getFilter() {
        return new Filter();
    }
}
