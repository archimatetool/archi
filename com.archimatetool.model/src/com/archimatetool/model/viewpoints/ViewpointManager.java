/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.model.viewpoints;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EClass;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.archimatetool.model.IArchimateDiagramModel;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IDiagramModelArchimateObject;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IDiagramModelConnection;
import com.archimatetool.model.util.ArchimateModelUtils;

/**
 * Viewpoint Manager
 * 
 * @author Phillip Beauvoir
 */
public class ViewpointManager {

    /**
     * The default Viewpoint representing "none".
     * All concepts are allowed.
     */
    public static IViewpoint NONE_VIEWPOINT = new Viewpoint("", Messages.ViewpointsManager_0); //$NON-NLS-1$

    /**
     * The Bundle ID
     */
    private static final String BUNDLE_ID = "com.archimatetool.model"; //$NON-NLS-1$

    /**
     * The Viewpoints XML file
     */
    static final String VIEWPOINTS_FILE = "model/viewpoints.xml"; //$NON-NLS-1$
    
    static final String BUSINESS_ELEMENTS = "$BusinessElements$"; //$NON-NLS-1$
    static final String APPLICATION_ELEMENTS = "$ApplicationElements$"; //$NON-NLS-1$
    static final String TECHNOLOGY_ELEMENTS = "$TechnologyElements$"; //$NON-NLS-1$
    static final String PHYSICAL_ELEMENTS = "$PhysicalElements$"; //$NON-NLS-1$
    static final String STRATEGY_ELEMENTS = "$StrategyElements$"; //$NON-NLS-1$
    static final String MOTIVATION_ELEMENTS = "$MotivationElements$"; //$NON-NLS-1$
    static final String IMPLEMENTATION_MIGRATION_ELEMENTS = "$ImplementationMigrationElements$"; //$NON-NLS-1$
    
    static final Map<String, EClass[]> ELEMENTS_MAP = new HashMap<String, EClass[]>();
    
    static {
        ELEMENTS_MAP.put(BUSINESS_ELEMENTS, ArchimateModelUtils.getBusinessClasses());
        ELEMENTS_MAP.put(APPLICATION_ELEMENTS, ArchimateModelUtils.getApplicationClasses());
        ELEMENTS_MAP.put(TECHNOLOGY_ELEMENTS, ArchimateModelUtils.getTechnologyClasses());
        ELEMENTS_MAP.put(PHYSICAL_ELEMENTS, ArchimateModelUtils.getPhysicalClasses());
        ELEMENTS_MAP.put(STRATEGY_ELEMENTS, ArchimateModelUtils.getStrategyClasses());
        ELEMENTS_MAP.put(MOTIVATION_ELEMENTS, ArchimateModelUtils.getMotivationClasses());
        ELEMENTS_MAP.put(IMPLEMENTATION_MIGRATION_ELEMENTS, ArchimateModelUtils.getImplementationMigrationClasses());
    }
    
    /**
     * Single Instance of ViewpointManager
     */
    public static ViewpointManager INSTANCE = new ViewpointManager();
    
    /**
     * All Viewpoints
     */
    private Map<String, IViewpoint> VIEWPOINTS = new HashMap<String, IViewpoint>();
    
    private ViewpointManager() {
        try {
            loadDefaultViewpointsFile();
        }
        catch(IOException | JDOMException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * @return A list of all Viewpoints
     */
    public List<IViewpoint> getAllViewpoints() {
        List<IViewpoint> list = new ArrayList<IViewpoint>(VIEWPOINTS.values());

        // Sort the Viewpoints by name
        Collections.sort(list, new Comparator<IViewpoint>() {
            @Override
            public int compare(IViewpoint vp1, IViewpoint vp2) {
                return vp1.getName().compareTo(vp2.getName());
            }
        });
        
        // Add the default "none" Viewpoint at the top of the list
        list.add(0, NONE_VIEWPOINT);
        
        return list;
    }
    
    /**
     * @param id
     * @return A Viewpoint by its id
     */
    public IViewpoint getViewpoint(String id) {
        if(id == null || "".equals(id)) { //$NON-NLS-1$
            return NONE_VIEWPOINT;
        }
        
        IViewpoint vp = VIEWPOINTS.get(id);
        
        return vp == null ? NONE_VIEWPOINT : vp;
    }
    
    /**
     * @param dmo
     * @return True if dmo is an allowed component for this Viewpoint
     */
    public boolean isAllowedDiagramModelComponent(IDiagramModelComponent dmo) {
        if(dmo instanceof IDiagramModelArchimateObject && dmo.getDiagramModel() instanceof IArchimateDiagramModel) {
            EClass eClass = ((IDiagramModelArchimateObject)dmo).getArchimateElement().eClass();
            return isAllowedConceptForDiagramModel((IArchimateDiagramModel)dmo.getDiagramModel(), eClass);
        }
        
        if(dmo instanceof IDiagramModelConnection) {
            return isAllowedDiagramModelComponent(((IDiagramModelConnection)dmo).getSource()) && 
                    isAllowedDiagramModelComponent(((IDiagramModelConnection)dmo).getTarget());
        }
        
        return true;
    }
    
    /**
     * @param dm
     * @param eClass
     * @return True if eClass is an allowed concept for a diagram model
     */
    public boolean isAllowedConceptForDiagramModel(IArchimateDiagramModel dm, EClass eClass) {
        if(dm != null) {
            IViewpoint vp = getViewpoint(dm.getViewpoint());
            return vp == null ? true : vp.isAllowedConcept(eClass);
        }
        
        return true;
    }
    
    
    /**
     * Load viewpoints from XML file
     */
    void loadDefaultViewpointsFile() throws IOException, JDOMException {
        // Load localised file from bundle
        URL url = FileLocator.find(Platform.getBundle(BUNDLE_ID), new Path("$nl$/" + VIEWPOINTS_FILE)); //$NON-NLS-1$
        url = FileLocator.resolve(url);
        
        Document doc = new SAXBuilder().build(url);
        Element rootElement = doc.getRootElement();
        
        for(Element xmlViewpoint : rootElement.getChildren("viewpoint")) { //$NON-NLS-1$
            
            String id = xmlViewpoint.getAttributeValue("id"); //$NON-NLS-1$
            if(id == null || "".equals(id)) { //$NON-NLS-1$
                System.err.println("Blank id for viewpoint"); //$NON-NLS-1$
                continue;
            }
            
            Element xmlName = xmlViewpoint.getChild("name"); //$NON-NLS-1$
            if(xmlName == null) {
                System.err.println("No name element for viewpoint"); //$NON-NLS-1$
                continue;
            }
            
            String name = xmlName.getText();
            if(name == null || "".equals(name)) { //$NON-NLS-1$
                System.err.println("Blank name for viewpoint"); //$NON-NLS-1$
                continue;
            }
            
            Viewpoint vp = new Viewpoint(id, name);
            
            for(Element xmlConcept : xmlViewpoint.getChildren("concept")) { //$NON-NLS-1$
                String conceptName = xmlConcept.getText();
                if(conceptName == null || "".equals(conceptName)) { //$NON-NLS-1$
                    System.err.println("Blank concept name for viewpoint"); //$NON-NLS-1$
                    continue;
                }
                
                if(ELEMENTS_MAP.containsKey(conceptName)) {
                    addCollection(vp, conceptName);
                }
                else {
                    EClass eClass = (EClass)IArchimatePackage.eINSTANCE.getEClassifier(conceptName);
                    if(eClass != null) {
                        addConcept(vp, eClass);
                    }
                    else {
                        System.err.println("Couldn't get eClass: " + conceptName); //$NON-NLS-1$
                    }
                    
                }
            }
            
            VIEWPOINTS.put(id, vp);
        }
    }
    
    private void addCollection(Viewpoint vp, String conceptName) {
        for(EClass eClass : ELEMENTS_MAP.get(conceptName)) {
            addConcept(vp, eClass);
        }
    }
    
    private void addConcept(Viewpoint vp, EClass eClass) {
        vp.getClassList().add(eClass);
    }
    
}
