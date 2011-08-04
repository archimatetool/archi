/*******************************************************************************
 * Copyright (c) 2010 Bolton University, UK.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 *******************************************************************************/
package uk.ac.bolton.archimate.bizzdesign;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import uk.ac.bolton.archimate.editor.model.IModelExporter;
import uk.ac.bolton.archimate.model.IArchimateDiagramModel;
import uk.ac.bolton.archimate.model.IArchimateElement;
import uk.ac.bolton.archimate.model.IArchimateModel;
import uk.ac.bolton.archimate.model.IArchimatePackage;
import uk.ac.bolton.archimate.model.IFolder;
import uk.ac.bolton.archimate.model.IRelationship;


/**
 * Export Model to BiZZdesign Architect
 * 
 * @author Phillip Beauvoir
 */
public class BiZZdesignExporter implements IModelExporter {
    
    protected static Map<EClass, String> fTypeNameMap = new HashMap<EClass, String>();
    static {
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getJunction(), "Junction");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getAndJunction(), "AndJunction");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getOrJunction(), "OrJunction");
        
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getBusinessActivity(), "BusinessActivity");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getBusinessEvent(), "BusinessEvent");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getBusinessInteraction(), "BusinessInteraction");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getBusinessProcess(), "BusinessProcess");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getBusinessActor(), "BusinessActor");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getBusinessInterface(), "BusinessInterface");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getBusinessCollaboration(), "BusinessCollaboration");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getBusinessRole(), "BusinessRole");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getBusinessFunction(), "BusinessFunction");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getContract(), "BusinessContract");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getProduct(), "BusinessProduct");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getBusinessService(), "BusinessService");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getValue(), "BusinessValue");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getBusinessObject(), "BusinessObject");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getRepresentation(), "BusinessRepresentation");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getMeaning(), "BusinessMeaning");
        
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getApplicationCollaboration(), "ApplicationCollaboration");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getApplicationComponent(), "ApplicationComponent");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getApplicationFunction(), "ApplicationFunction");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getApplicationInteraction(), "ApplicationInteraction");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getApplicationInterface(), "ApplicationInterface");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getApplicationService(), "ApplicationService");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getDataObject(), "ApplicationDataObject");
        
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getArtifact(), "InfrastructureArtifact");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getCommunicationPath(), "InfrastructureCommunicationPath");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getDevice(), "InfrastructureDevice");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getNode(), "InfrastructureNode");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getInfrastructureInterface(), "InfrastructureInterface");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getNetwork(), "InfrastructureNetwork");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getInfrastructureService(), "InfrastructureService");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getSystemSoftware(), "InfrastructureSystemSoftware");
        
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getAccessRelationship(), "AccessRelation");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getCompositionRelationship(), "CompositionRelation");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getFlowRelationship(), "FlowRelation");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getAggregationRelationship(), "AggregationRelation");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getAssignmentRelationship(), "AssignmentRelation");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getAssociationRelationship(), "AssociationRelation");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getRealisationRelationship(), "RealisationRelation");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getSpecialisationRelationship(), "SpecializationRelation");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getTriggeringRelationship(), "TriggeringRelation");
        fTypeNameMap.put(IArchimatePackage.eINSTANCE.getUsedByRelationship(), "UseRelation");
    }
    
    protected static Namespace XMI_NAMESPACE = Namespace.getNamespace("xmi", "http://www.omg.org/XMI");

    @Override
    public void export(IArchimateModel model) throws IOException {
        File file = askSaveFile();
        if(file == null) {
            return;
        }
        
        Document doc = new Document();
        Element root = new Element("XMI", XMI_NAMESPACE);
        doc.setRootElement(root);
        
        Element header = new Element("XMI.header", XMI_NAMESPACE);
        root.addContent(header);
        
        Element metamodel = new Element("XMI.metamodel", XMI_NAMESPACE);
        metamodel.setAttribute("xmi.name", "ArchiMate");
        metamodel.setAttribute("xmi.version", "1.0");
        header.addContent(metamodel);
        
        Element content = new Element("XMI.content", XMI_NAMESPACE);
        root.addContent(content);
        
        for(IFolder folder : model.getFolders()) {
            saveFolder(folder, content);
        }
        
        Format format = Format.getPrettyFormat();
        format.setEncoding("UTF-16");
        XMLOutputter outputter = new XMLOutputter(format);
        FileOutputStream out = new FileOutputStream(file);
        outputter.output(doc, out);
        out.close();
    }
    
    private void saveFolder(IFolder folder, Element content) {
        if(folder == null) {
            return;
        }
        
        for(EObject object : folder.getElements()) {
            if(object instanceof IArchimateElement) {
                IArchimateElement element = (IArchimateElement)object;
                String name = fTypeNameMap.get(element.eClass());
                if(name != null) {
                    Element e = new Element(name);
                    content.addContent(e);
                    e.setAttribute("name", element.getName());
                    e.setAttribute("xmi.id", element.getId());
                    
                    if(element instanceof IRelationship) {
                        IRelationship relation = (IRelationship)object;
                        e.setAttribute("from", relation.getSource().getId());
                        e.setAttribute("to", relation.getTarget().getId());
                    }
                }
            }
            
            if(object instanceof IArchimateDiagramModel) {
                IArchimateDiagramModel diagram = (IArchimateDiagramModel)object;
                Element e = new Element("AllView");
                content.addContent(e);
                e.setAttribute("name", diagram.getName());
                e.setAttribute("xmi.id", diagram.getId());
                e.setAttribute("xmi.type", "AllView");
            }
        }
        
        for(IFolder f : folder.getFolders()) {
            saveFolder(f, content);
        }
    }
    
    /**
     * Ask user for file name to save to
     * @return
     */
    private File askSaveFile() {
        FileDialog dialog = new FileDialog(Display.getCurrent().getActiveShell(), SWT.SAVE);
        dialog.setText("Export Model");
        dialog.setFilterExtensions(new String[] { "*.xml", "*.*" } );
        String path = dialog.open();
        if(path == null) {
            return null;
        }
        
        // Only Windows adds the extension by default
        if(dialog.getFilterIndex() == 0 && !path.endsWith(".xml")) {
            path += ".xml";
        }
        
        File file = new File(path);
        
        // Make sure the file does not already exist
        if(file.exists()) {
            boolean result = MessageDialog.openQuestion(Display.getCurrent().getActiveShell(), "Export Model",
                    "'" + file +
                    "' already exists. Are you sure you want to overwrite it?");
            if(!result) {
                return null;
            }
        }
        
        return file;
    }

}
