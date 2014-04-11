/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.csv;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A CSV record parsed from a CSV file.
 *
 * @version $Id$
 */
@SuppressWarnings("nls")
public final class CSVRecord implements Serializable, Iterable<String> {

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static final long serialVersionUID = 1L;

    /** The accumulated comments (if any) */
    private final String comment;

    /** The column name to index mapping. */
    private final Map<String, Integer> mapping;

    /** The record number. */
    private final long recordNumber;

    /** The values of the record */
    private final String[] values;

    CSVRecord(final String[] values, final Map<String, Integer> mapping,
            final String comment, final long recordNumber) {
        this.recordNumber = recordNumber;
        this.values = values != null ? values : EMPTY_STRING_ARRAY;
        this.mapping = mapping;
        this.comment = comment;
    }

    /**
     * Returns a value by {@link Enum}.
     *
     * @param e
     *            an enum
     * @return the String at the given enum String
     */
    public String get(final Enum<?> e) {
        return get(e.toString());
    }

    /**
     * Returns a value by index.
     *
     * @param i
     *            a column index (0-based)
     * @return the String at the given index
     */
    public String get(final int i) {
        return values[i];
    }

    /**
     * Returns a value by name.
     *
     * @param name
     *            the name of the column to be retrieved.
     * @return the column value, maybe null depending on {@link CSVFormat#getNullString()}.
     * @throws IllegalStateException
     *             if no header mapping was provided
     * @throws IllegalArgumentException
     *             if {@code name} is not mapped or if the record is inconsistent
     * @see #isConsistent()
     * @see CSVFormat#withNullString(String)
     */
    public String get(final String name) {
        if (mapping == null) {
            throw new IllegalStateException(
                    "No header mapping was specified, the record values can't be accessed by name");
        }
        final Integer index = mapping.get(name);
        if (index == null) {
            throw new IllegalArgumentException(String.format("Mapping for %s not found, expected one of %s", name,
                    mapping.keySet()));
        }
        try {
            return values[index.intValue()];
        } catch (final ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format(
                    "Index for header '%s' is %d but CSVRecord only has %d values!", name, index,
                    Integer.valueOf(values.length)));
        }
    }

    /**
     * Returns the comment for this record, if any.
     *
     * @return the comment for this record, or null if no comment for this
     *         record is available.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Returns the number of this record in the parsed CSV file.
     *
     * @return the number of this record.
     */
    public long getRecordNumber() {
        return recordNumber;
    }

    /**
     * Returns true if this record is consistent, false if not. Currently, the only check is matching the record size to
     * the header size. Some programs can export files that fails this test but still produce parsable files.
     *
     * @return true of this record is valid, false if not
     */
    public boolean isConsistent() {
        return mapping == null ? true : mapping.size() == values.length;
    }

    /**
     * Checks whether a given column is mapped, i.e. its name has been defined to the parser.
     *
     * @param name
     *            the name of the column to be retrieved.
     * @return whether a given column is mapped.
     */
    public boolean isMapped(final String name) {
        return mapping != null ? mapping.containsKey(name) : false;
    }

    /**
     * Checks whether a given columns is mapped and has a value.
     *
     * @param name
     *            the name of the column to be retrieved.
     * @return whether a given columns is mapped and has a value
     */
    public boolean isSet(final String name) {
        return isMapped(name) && mapping.get(name).intValue() < values.length;
    }

    /**
     * Returns an iterator over the values of this record.
     *
     * @return an iterator over the values of this record.
     */
    public Iterator<String> iterator() {
        return toList().iterator();
    }

    /**
     * Puts all values of this record into the given Map.
     *
     * @param map The Map to populate.
     * @return the given map.
     */
    <M extends Map<String, String>> M putIn(final M map) {
        for (final Entry<String, Integer> entry : mapping.entrySet()) {
            map.put(entry.getKey(), values[entry.getValue().intValue()]);
        }
        return map;
    }

    /**
     * Returns the number of values in this record.
     *
     * @return the number of values.
     */
    public int size() {
        return values.length;
    }

    /**
     * Converts the values to a List.
     *
     * TODO: Maybe make this public?
     * @return a new List
     */
    private List<String> toList() {
        return Arrays.asList(values);
    }

    /**
     * Copies this record into a new Map. The new map is not connect
     *
     * @return A new Map. The map is empty if the record has no headers.
     */
    public Map<String, String> toMap() {
        return putIn(new HashMap<String, String>(values.length));
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }

    String[] values() {
        return values;
    }


}
