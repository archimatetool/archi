/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.Collator;

import org.junit.jupiter.api.Test;

@SuppressWarnings("nls")
public class AlphanumericComparatorTests {

    private final AlphanumericComparator cmp =
            new AlphanumericComparator(Collator.getInstance());


    @Test
    public void testBasicNumericCompare() {

        assertTrue(cmp.compare("1", "2") < 0);
        assertTrue(cmp.compare("9", "10") < 0);
        assertTrue(cmp.compare("10", "2") > 0);
        assertTrue(cmp.compare("100", "99") > 0);

        assertEquals(0, cmp.compare("42", "42"));
    }


    @Test
    public void testLeadingZeros() {

        assertTrue(cmp.compare("7", "07") < 0);
        assertTrue(cmp.compare("07", "007") < 0);

        assertEquals(0, cmp.compare("000", "000"));
    }


    @Test
    public void testSimpleAlphanumericLabels() {

        assertTrue(cmp.compare("file2", "file10") < 0);
        assertTrue(cmp.compare("actor 9", "actor 10") < 0);

        assertTrue(cmp.compare("Comparator test actor 2", "Comparator test actor 10") < 0);
    }


    @Test
    public void testHierarchicalPrefixesWithDots() {

        assertTrue(cmp.compare("1.2 Name", "1.10 Name") < 0);
        assertTrue(cmp.compare("1.2 Name", "1.2.1 Name") < 0);
        assertTrue(cmp.compare("1.2.1 Name", "1.10 Name") < 0);
        assertTrue(cmp.compare("2.1.1 Name", "2.1.1.1 Name") < 0);
        assertTrue(cmp.compare("2.1.1.1 Name", "2.1.2 Name") < 0);
    }


    @Test
    public void testHierarchicalPrefixesWithDashes() {

        assertTrue(cmp.compare("1-2 Name", "1-10 Name") < 0);
        assertTrue(cmp.compare("1-2 Name", "1-2-1 Name") < 0);
        assertTrue(cmp.compare("1-2-1 Name", "1-10 Name") < 0);
        assertTrue(cmp.compare("2-1-1 Name", "2-1-1-1 Name") < 0);
        assertTrue(cmp.compare("2-1-1-1 Name", "2-1-2 Name") < 0);
    }


    @Test
    public void testMixedSeparators() {

        assertTrue(cmp.compare("1.2", "1-10") < 0);
        assertTrue(cmp.compare("1-2", "1.2.1") < 0);
        assertTrue(cmp.compare("1_2", "1-10") < 0);
        assertTrue(cmp.compare("2/1/1", "2.1.1.1") < 0);
    }


    @Test
    public void testParentBeforeChildOrdering() {

        assertTrue(cmp.compare("1", "1.1") < 0);
        assertTrue(cmp.compare("1.1", "1.1.1") < 0);
        assertTrue(cmp.compare("1-1", "1-1-1") < 0);
        assertTrue(cmp.compare("2.1.1", "2.1.1.1") < 0);
    }


    @Test
    public void testDeepHierarchyOrdering() {

        assertTrue(cmp.compare("1.2", "1.10") < 0);
        assertTrue(cmp.compare("1.2", "1.2.1") < 0);
        assertTrue(cmp.compare("1.2.1", "1.2.2") < 0);
        assertTrue(cmp.compare("1.2.2", "1.2.10") < 0);
        assertTrue(cmp.compare("1.2.10", "1.10") < 0);
    }


    @Test
    public void testRealisticArchimateLabels() {

        /*
         * Realistic labels taken from existing ArchiMate models
         * including hierarchical prefixes, parentheses and mixed separators.
         */

        assertTrue(cmp.compare("(1.1) Receive quotation", "(1.2) Approve quotation") < 0);
        assertTrue(cmp.compare("(1.2) Approve quotation", "(2.1) Execute works") < 0);
        assertTrue(cmp.compare("(2.2) Follow up works", "(3.1) Receive invoice") < 0);
        assertTrue(cmp.compare("(3.1) Receive invoice", "(3.2) Pay invoice") < 0);

        assertTrue(cmp.compare("1-1 Digital Customer Management", "1.2 Data Analysis") < 0);
        assertTrue(cmp.compare("1.2 Data Analysis", "1-3 Product Management") < 0);
        assertTrue(cmp.compare("1-3 Product Management", "1.5 Productise Open Source Software") < 0);
    }

}
