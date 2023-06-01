/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.hammer.validation.issues;


import java.util.ArrayList;

import org.junit.Before;


public class AdviceCategoryTests extends AbstractIssueCategoryTests {
    
    @Before
    public void runOnceBeforeEachTest() {
        issueCategory = new AdviceCategory(new ArrayList<AdviceType>());
    }
    
}
