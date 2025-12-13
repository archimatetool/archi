/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.components;

import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Comparator that sorts strings with numbers in so "file 2" comes before "file 10"
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class AlphanumericComparator implements Comparator<String> {
    
    private static final Pattern PATTERN = Pattern.compile("(\\d+)");

    private Comparator<? super String> comparator;

    public AlphanumericComparator(Comparator<? super String> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int compare(String s1, String s2) {
        Matcher m1 = PATTERN.matcher(s1);
        Matcher m2 = PATTERN.matcher(s2);
        int pos1 = 0, pos2 = 0;

        while(m1.find() && m2.find()) {
            // Compare non-numeric prefixes
            int cmpPrefix = comparator.compare(s1.substring(pos1, m1.start()), s2.substring(pos2, m2.start()));
            if(cmpPrefix != 0) {
                return cmpPrefix;
            }

            // Compare numeric parts
            int num1 = Integer.parseInt(m1.group());
            int num2 = Integer.parseInt(m2.group());
            if(num1 != num2) {
                return Integer.compare(num1, num2);
            }

            pos1 = m1.end();
            pos2 = m2.end();
        }
        
        // Compare remaining parts
        return comparator.compare(s1.substring(pos1), s2.substring(pos2));
    }
}
