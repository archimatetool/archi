/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.model.viewpoints;

import java.util.Arrays;
import java.util.Collection;

import junit.framework.JUnit4TestAdapter;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class AllViewpointTests extends AbstractViewpointTests {
    
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(AllViewpointTests.class);
    }
    
    @Parameters
    public static Collection<Object[]> eObjects() {
        return Arrays.asList(new Object[][] {
                { new TotalViewpoint(), 0 },
                { new ActorCooperationViewpoint(), 1 },
                { new ApplicationBehaviourViewpoint(), 2 },
                { new ApplicationCooperationViewpoint(), 3 },
                { new ApplicationStructureViewpoint(), 4 },
                { new ApplicationUsageViewpoint(), 5 },
                { new BusinessFunctionViewpoint(), 6 },
                { new BusinessProcessCooperationViewpoint(), 7 },
                { new BusinessProcessViewpoint(), 8 },
                { new BusinessProductViewpoint(), 9 },
                { new ImplementationAndDeploymentViewpoint(), 10 },
                { new InformationStructureViewpoint(), 11 },
                { new InfrastructureUsageViewpoint(), 12 },
                { new InfrastructureViewpoint(), 13 },
                { new LayeredViewpoint(), 14 },
                { new OrganisationViewpoint(), 15 },
                { new ServiceRealisationViewpoint(), 16 },
                
                { new StakeholderViewpoint(), 17 },
                { new GoalRealisationViewpoint(), 18 },
                { new GoalContributionViewpoint(), 19 },
                { new PrinciplesViewpoint(), 20 },
                { new RequirementsRealisationViewpoint(), 21 },
                { new MotivationViewpoint(), 22 },
                
                { new ProjectViewpoint(), 23 },
                { new MigrationViewpoint(), 24 },
                { new ImplementationMigrationViewpoint(), 25 }
        });
    }
    
    public AllViewpointTests(AbstractViewpoint vp, int index) {
        this.vp = vp;
        this.index = index;
    }
    
}
