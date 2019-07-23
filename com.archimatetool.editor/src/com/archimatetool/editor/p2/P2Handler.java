/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.p2;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.internal.p2.core.Activator;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.IProvisioningAgentProvider;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.operations.InstallOperation;
import org.eclipse.equinox.p2.operations.ProfileChangeOperation;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.UninstallOperation;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.osgi.framework.ServiceReference;

import com.archimatetool.editor.ArchiPlugin;

/**
 * See https://github.com/EnFlexIT/AgentWorkbench/blob/master/eclipseProjects/org.agentgui/bundles/de.enflexit.common/src/de/enflexit/common/p2/P2OperationsHandler.java
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("restriction")
public class P2Handler {
    
    private ProvisioningSession provisioningSession;
    private IProvisioningAgent provisioningAgent;
    private IMetadataRepositoryManager metadataRepositoryManager;
    private IArtifactRepositoryManager artifactRepositoryManager;

    private static P2Handler instance;
    
    public static P2Handler getInstance() {
        if (instance==null) {
            instance = new P2Handler();
        }
        return instance;
    }
    
    
    private P2Handler() { 
    }
    
    public IStatus install(URI uri, IProgressMonitor monitor) throws ProvisionException {
        loadRepository(uri, monitor);
        
        Collection<IInstallableUnit> toInstall = getGroupInstallableUnits(uri, monitor);
        InstallOperation operation = new InstallOperation(getProvisioningSession(), toInstall);
        return performOperation(operation, monitor);
    }
    
    public IStatus update(URI uri, IProgressMonitor monitor) throws ProvisionException {
        loadRepository(uri, monitor);
        
        UpdateOperation operation = new UpdateOperation(getProvisioningSession());
        return performOperation(operation, monitor);
    }
    
    public IStatus uninstall(Collection<IInstallableUnit> toUninstall, IProgressMonitor monitor) throws ProvisionException {
        UninstallOperation operation = new UninstallOperation(getProvisioningSession(), toUninstall);
        return performOperation(operation, monitor);
    }

    public boolean isInstalled(URI uri, IProgressMonitor monitor) throws ProvisionException, OperationCanceledException {
        for(IInstallableUnit iu : getInstalledFeatures()) {
            IQueryResult<IInstallableUnit> result = queryRepositoryForInstallableUnit(uri, iu.getId(), monitor);
            if(!result.isEmpty()) {
                return true;
            }
        }
        
        return false;
    }
    
    List<IInstallableUnit> getInstalledFeatures() throws ProvisionException {
        IProfile profile = getDefaultProfile();

        ArrayList<IInstallableUnit> list = new ArrayList<IInstallableUnit>();
        
        IQuery<IInstallableUnit> query = QueryUtil.createIUGroupQuery();
        IQueryResult<IInstallableUnit> queryResult = profile.query(query, null);

        for(IInstallableUnit feature : queryResult) {
            if(!isInternalFeature(feature)) {
                list.add(feature);
            }
        }
        
        return list;
    }
    
    // TODO Externalise the list of internal features, or keep a record of external ones
    private boolean isInternalFeature(IInstallableUnit feature) {
        String id = feature.getId();
        
        if(id.startsWith("org.eclipse") || id.startsWith("org.opengroup") || id.startsWith("com.archimatetool.editor")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            return true;
        }
        
        return false;
    }
    
    private Set<IInstallableUnit> getGroupInstallableUnits(URI uri, IProgressMonitor monitor) throws ProvisionException, OperationCanceledException {
        IMetadataRepository metadataRepo = getMetadataRepositoryManager().loadRepository(uri, monitor);
        
        IQuery<IInstallableUnit> query = QueryUtil.createIUGroupQuery(); // Works for feature and its contents
        IQueryResult<IInstallableUnit> queryResult = metadataRepo.query(query, monitor);

        return queryResult.toUnmodifiableSet();
    }
    
    @SuppressWarnings("unused")
    private void addRepository(URI uri) throws ProvisionException {
        getMetadataRepositoryManager().addRepository(uri);
        getArtifactRepositoryManager().addRepository(uri);
    }
    
    private void loadRepository(URI uri, IProgressMonitor monitor) throws ProvisionException {
        getMetadataRepositoryManager().loadRepository(uri, monitor);
        getArtifactRepositoryManager().loadRepository(uri, monitor);
    }

    private IStatus performOperation(ProfileChangeOperation operation, IProgressMonitor monitor) {
        IStatus status = operation.resolveModal(monitor);

        if(status.isOK()) {
            ProvisioningJob provisioningJob = operation.getProvisioningJob(monitor);

            if(provisioningJob == null) {
                System.err.println("Trying to install from the Eclipse IDE? This won't work!"); //$NON-NLS-1$
                return Status.CANCEL_STATUS;
            }

//            provisioningJob.addJobChangeListener(new JobChangeAdapter() {
//                @Override
//                public void done(IJobChangeEvent event) {
//                    if(event.getResult().isOK()) {
//                        workbench.restart();
//                    }
//                    super.done(event);
//                }
//            });
//            provisioningJob.schedule();
            
            status = provisioningJob.runModal(monitor);
        }

        if(!status.isOK()) {
            ArchiPlugin.INSTANCE.getLog().log(status);
        }

        return status;
    }
    
    private IProvisioningAgent getProvisioningAgent() throws ProvisionException {
        if(provisioningAgent == null) {
            ServiceReference<?> sr = Activator.getContext().getServiceReference(IProvisioningAgentProvider.SERVICE_NAME);
            IProvisioningAgentProvider agentProvider = (IProvisioningAgentProvider)Activator.getContext().getService(sr);
            provisioningAgent = agentProvider.createAgent(null);
        }
        
        return provisioningAgent;
    }
    
    private ProvisioningSession getProvisioningSession() throws ProvisionException {
        if(provisioningSession == null) {
            provisioningSession = new ProvisioningSession(getProvisioningAgent());
        }
        return provisioningSession;
    }
    
    private IQueryResult<IInstallableUnit> queryRepositoryForInstallableUnit(URI uri, String installableUnitID, IProgressMonitor monitor) throws ProvisionException, OperationCanceledException {
        IMetadataRepository metadataRepository = getMetadataRepositoryManager().loadRepository(uri, monitor);
        
        if(metadataRepository != null) {
            return metadataRepository.query(QueryUtil.createIUQuery(installableUnitID), monitor);
        }

        return null;
    }
    
    private IMetadataRepositoryManager getMetadataRepositoryManager() throws ProvisionException {
        if(metadataRepositoryManager == null) {
            metadataRepositoryManager = (IMetadataRepositoryManager)getProvisioningAgent()
                    .getService(IMetadataRepositoryManager.SERVICE_NAME);
        }
        return metadataRepositoryManager;
    }

    private IArtifactRepositoryManager getArtifactRepositoryManager() throws ProvisionException {
        if(artifactRepositoryManager == null) {
            artifactRepositoryManager = (IArtifactRepositoryManager)getProvisioningAgent()
                    .getService(IArtifactRepositoryManager.SERVICE_NAME);
        }
        return artifactRepositoryManager;
    }
    
    private IProfile getDefaultProfile() throws ProvisionException {
        IProfileRegistry service = (IProfileRegistry) getProvisioningAgent().getService(IProfileRegistry.SERVICE_NAME);
        IProfile profile = service.getProfile(IProfileRegistry.SELF);
        
        if(profile == null) {
            throw new ProvisionException("Unable to access p2 profile - This is not possible when starting the application from the IDE!"); //$NON-NLS-1$
        }
        
        return profile;
    }
}
