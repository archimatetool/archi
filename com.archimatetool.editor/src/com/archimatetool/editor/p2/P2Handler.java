/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.p2;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.equinox.internal.p2.garbagecollector.GarbageCollector;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.IProvisioningAgentProvider;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.engine.query.UserVisibleRootQuery;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.metadata.MetadataFactory.InstallableUnitDescription;
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
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.archimatetool.editor.ArchiPlugin;

/**
 * See https://github.com/EnFlexIT/AgentWorkbench/blob/master/eclipseProjects/org.agentgui/bundles/de.enflexit.common/src/de/enflexit/common/p2/P2OperationsHandler.java
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings({"restriction", "nls"})
public class P2Handler {
    
    private ProvisioningSession provisioningSession;
    private IProvisioningAgent provisioningAgent;
    private IMetadataRepositoryManager metadataRepositoryManager;
    private IArtifactRepositoryManager artifactRepositoryManager;

    private static P2Handler instance;
    
    public static P2Handler getInstance() {
        if (instance == null) {
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
    
    /**
     * Remove installed plug-ins when a feature has been uninstalled
     * 
     * There is an option to enable garbage collection by adding this to Archi.ini but I don't think it's needed:
     * -profileProperties org.eclipse.update.install.features=true
     * 
     * There is some default garbage collection done when another feature is uninstalled but there is always something left behind
     * so this could be called at startup.
     * 
     * See https://wiki.eclipse.org/Equinox/p2/FAQ#But_why_aren.27t_uninstalled_bundles.2Ffeatures_immediately_removed.3F
     */
    public void garbageCollect() throws ProvisionException {
        IProfile profile = getDefaultProfile();
        GarbageCollector gc = (GarbageCollector) getProvisioningAgent().getService(GarbageCollector.SERVICE_NAME);
        gc.runGC(profile);
    }
    
    /**
     * Delete all old 1234567890.profile.gz files except for the latest one
     * These are in .p2/org.eclipse.equinox.p2.engine/profileRegistry/DefaultProfile.profile
     * This can be called when exiting the app
     */
    public void cleanProfileRegistry() throws ProvisionException {
        File p2Folder = P2.getP2Location();
        if(p2Folder == null) {
            return;
        }
        
        IProfile profile = getDefaultProfile();
        File profileFolder = new File(p2Folder, "org.eclipse.equinox.p2.engine/profileRegistry/" + profile.getProfileId() + ".profile");

        // Get all the timestamps for the profile
        for(long ts : getProfileRegistry().listProfileTimestamps(profile.getProfileId())) {
            if(ts < profile.getTimestamp()) { // Don't delete latest one
                File file = new File(profileFolder, ts + ".profile.gz");
                file.delete();
            }
        }
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
    
    public List<IInstallableUnit> getInstalledFeatures() throws ProvisionException {
        ArrayList<IInstallableUnit> list = new ArrayList<IInstallableUnit>();
        
        IProfile profile = getDefaultProfile();

        IQuery<IInstallableUnit> query = QueryUtil.createIUGroupQuery();
        IQueryResult<IInstallableUnit> queryResult = profile.query(query, null);
        
        // This is another way to get just the root features
        //IQueryResult<IInstallableUnit> queryResult = profile.query(new UserVisibleRootQuery(), null);
        
        for(IInstallableUnit iu : queryResult) {
            // Show root features (visible to the user) but not products
            if(UserVisibleRootQuery.isUserVisible(iu, profile) && iu.getProperty(InstallableUnitDescription.PROP_TYPE_PRODUCT) == null) {
                list.add(iu);
            }
        }
        
        return list;
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
            ArchiPlugin.getInstance().getLog().log(status);
        }

        return status;
    }
    
    private IProvisioningAgent getProvisioningAgent() throws ProvisionException {
        if(provisioningAgent == null) {
            BundleContext bundleContext = FrameworkUtil.getBundle(IProvisioningAgent.class).getBundleContext();
            ServiceReference<?> sr = bundleContext.getServiceReference(IProvisioningAgentProvider.SERVICE_NAME);
            IProvisioningAgentProvider agentProvider = (IProvisioningAgentProvider)bundleContext.getService(sr);
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
    
    private IProfileRegistry getProfileRegistry() throws ProvisionException {
        return (IProfileRegistry) getProvisioningAgent().getService(IProfileRegistry.SERVICE_NAME);
    }
    
    private IProfile getDefaultProfile() throws ProvisionException {
        IProfileRegistry registry = getProfileRegistry();
        IProfile profile = registry.getProfile(IProfileRegistry.SELF);
        
        if(profile == null) {
            throw new ProvisionException("Unable to access p2 profile - This is not possible when starting the application from the IDE!"); //$NON-NLS-1$
        }
        
        return profile;
    }
}
