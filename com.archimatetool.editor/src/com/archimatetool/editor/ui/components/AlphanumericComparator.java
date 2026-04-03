/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.editor.ui.components;

import java.util.Comparator;

/**
 * Comparator that performs deterministic alphanumeric sorting so that
 * strings containing numbers are ordered in a natural way
 * (for example "file 2" before "file 10").
 * 
 * Supports hierarchical numeric prefixes using any separator
 * (for example "1.2", "1-2-3", "2.1.10", etc.) and ensures
 * consistent ordering for labels with structured numeric prefixes.
 *
 * @author Franky De Pestel
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class AlphanumericComparator implements Comparator<String> {

    private record HierarchyLabel(String path, String remainder) {}
    
    private Comparator<? super String> comparator;

    public AlphanumericComparator(Comparator<? super String> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int compare(String s1, String s2) {
        HierarchyLabel h1 = parseHierarchyLabel(s1);
        HierarchyLabel h2 = parseHierarchyLabel(s2);

        if(h1 != null && h2 != null) {
            int cmp = compareHierarchyPaths(h1.path(), h2.path());
            if(cmp != 0) {
                return cmp;
            }

            cmp = compareAlphanumericText(h1.remainder(), h2.remainder());
            if(cmp != 0) {
                return cmp;
            }

            return comparator.compare(s1, s2);
        }

        return compareAlphanumericText(s1, s2);
    }

    private int compareHierarchyPaths(String path1, String path2) {
        int i1 = 0;
        int i2 = 0;
        int len1 = path1.length();
        int len2 = path2.length();

        while(i1 < len1 && i2 < len2) {
            int start1 = i1;
            int start2 = i2;

            while(i1 < len1 && Character.isDigit(path1.charAt(i1))) {
                i1++;
            }
            while(i2 < len2 && Character.isDigit(path2.charAt(i2))) {
                i2++;
            }

            int cmp = compareNumberTokens(path1.substring(start1, i1), path2.substring(start2, i2));
            if(cmp != 0) {
                return cmp;
            }

            int sepStart1 = i1;
            int sepStart2 = i2;

            while(i1 < len1 && !Character.isDigit(path1.charAt(i1))) {
                i1++;
            }
            while(i2 < len2 && !Character.isDigit(path2.charAt(i2))) {
                i2++;
            }

            boolean hasSeparator1 = sepStart1 < i1;
            boolean hasSeparator2 = sepStart2 < i2;

            boolean hasNextNumber1 = i1 < len1 && Character.isDigit(path1.charAt(i1));
            boolean hasNextNumber2 = i2 < len2 && Character.isDigit(path2.charAt(i2));

            if(hasSeparator1 && hasNextNumber1 && hasSeparator2 && hasNextNumber2) {
                continue;
            }

            if(hasSeparator1 && hasNextNumber1) {
                return 1;
            }
            if(hasSeparator2 && hasNextNumber2) {
                return -1;
            }

            break;
        }

        if(i1 < len1) {
            return 1;
        }
        if(i2 < len2) {
            return -1;
        }

        return 0;
    }

    private int compareAlphanumericText(String s1, String s2) {
        int i1 = 0;
        int i2 = 0;
        int len1 = s1.length();
        int len2 = s2.length();

        while(i1 < len1 && i2 < len2) {
            char c1 = s1.charAt(i1);
            char c2 = s2.charAt(i2);

            if(Character.isDigit(c1) && Character.isDigit(c2)) {
                int start1 = i1;
                int start2 = i2;

                while(i1 < len1 && Character.isDigit(s1.charAt(i1))) {
                    i1++;
                }
                while(i2 < len2 && Character.isDigit(s2.charAt(i2))) {
                    i2++;
                }

                String token1 = s1.substring(start1, i1);
                String token2 = s2.substring(start2, i2);

                int cmp = compareNumberTokens(token1, token2);
                if(cmp != 0) {
                    return cmp;
                }
            }
            else {
                int start1 = i1;
                int start2 = i2;

                while(i1 < len1 && !Character.isDigit(s1.charAt(i1))) {
                    i1++;
                }
                while(i2 < len2 && !Character.isDigit(s2.charAt(i2))) {
                    i2++;
                }

                String token1 = s1.substring(start1, i1);
                String token2 = s2.substring(start2, i2);

                int cmp = comparator.compare(token1, token2);
                if(cmp != 0) {
                    return cmp;
                }
            }
        }

        if(i1 < len1) {
            return 1;
        }
        if(i2 < len2) {
            return -1;
        }

        return comparator.compare(s1, s2);
    }

    private int compareNumberTokens(String s1, String s2) {
        String n1 = stripLeadingZeros(s1);
        String n2 = stripLeadingZeros(s2);

        if(n1.length() != n2.length()) {
            return n1.length() - n2.length();
        }

        int cmp = n1.compareTo(n2);
        if(cmp != 0) {
            return cmp;
        }

        if(s1.length() != s2.length()) {
            return s1.length() - s2.length();
        }

        return 0;
    }

    private String stripLeadingZeros(String s) {
        int i = 0;

        while(i < s.length() - 1 && s.charAt(i) == '0') {
            i++;
        }

        return s.substring(i);
    }

    private HierarchyLabel parseHierarchyLabel(String s) {
        int len = s.length();
        int i = 0;

        while(i < len && Character.isWhitespace(s.charAt(i))) {
            i++;
        }

        boolean hasOpeningParen = i < len && s.charAt(i) == '(';
        if(hasOpeningParen) {
            i++;
        }

        int pathStart = i;

        if(i >= len || !Character.isDigit(s.charAt(i))) {
            return null;
        }

        boolean hasMultipleSegments = false;

        while(true) {
            int segmentStart = i;

            while(i < len && Character.isDigit(s.charAt(i))) {
                i++;
            }

            if(segmentStart == i) {
                return null;
            }

            int separatorStart = i;

            while(i < len && !Character.isDigit(s.charAt(i))) {
                i++;
            }

            if(separatorStart == i) {
                break;
            }

            if(i < len && Character.isDigit(s.charAt(i))) {
                hasMultipleSegments = true;
                continue;
            }

            String path = s.substring(pathStart, separatorStart);
            String remainder = s.substring(separatorStart).trim();

            if(hasOpeningParen && remainder.startsWith(")")) {
                remainder = remainder.substring(1).trim();
            }

            return hasMultipleSegments ? new HierarchyLabel(path, remainder) : null;
        }

        String path = s.substring(pathStart, i);
        String remainder = ""; //$NON-NLS-1$

        if(hasOpeningParen && i < len && s.charAt(i) == ')') {
            remainder = s.substring(i + 1).trim();
        }
        else if(i < len) {
            remainder = s.substring(i).trim();
        }

        return hasMultipleSegments ? new HierarchyLabel(path, remainder) : null;
    }
}
