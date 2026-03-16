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
    
    private static final Pattern HIERARCHY_PATTERN = Pattern.compile("^(\\d+(?:[^0-9]+\\d+)*)([^0-9]+)(.*)$");

    private Comparator<? super String> comparator;

    public AlphanumericComparator(Comparator<? super String> comparator) {
        this.comparator = comparator;
    }

    @Override
    public int compare(String s1, String s2) {
        HierarchyLabel h1 = parseHierarchyLabel(s1);
        HierarchyLabel h2 = parseHierarchyLabel(s2);

        if(h1 != null && h2 != null) {
            int cmp = compareHierarchyPaths(h1.path, h2.path);
            if(cmp != 0) {
                return cmp;
            }

            cmp = compareAlphanumericText(h1.remainder, h2.remainder);
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
        Matcher matcher = HIERARCHY_PATTERN.matcher(s);
        if(!matcher.matches()) {
            return null;
        }

        return new HierarchyLabel(matcher.group(1), matcher.group(3));
    }

    private static final class HierarchyLabel {
        private final String path;
        private final String remainder;

        private HierarchyLabel(String path, String remainder) {
            this.path = path;
            this.remainder = remainder;
        }
    }
}
