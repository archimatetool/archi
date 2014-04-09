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

/**
 * Apache Commons CSV Format Support.
 *
 * <p>CSV are widely used as interfaces to legacy systems or manual data-imports.
 *    CSV stands for "Comma Separated Values" (or sometimes "Character Separated
 *    Values"). The CSV data format is defined in
 *    <a href="http://tools.ietf.org/html/rfc4180" target="_blank">RFC 4180</a>
 *    but many dialects exist.</p>
 *
 * <p>Common to all file dialects is its basic structure: The CSV data-format
 *    is record oriented, whereas each record starts on a new textual line. A
 *    record is build of a list of values. Keep in mind that not all records
 *    must have an equal number of values:</p>
 * <pre>
 *       csv    := records*
 *       record := values*
 * </pre>
 *
 * <p>The following list contains the CSV aspects the Commons CSV parser supports:</p>
 * <dl>
 *   <dt>Separators (for lines)</dt>
 *   <dd>The record separators are hardcoded and cannot be changed. The must be '\r', '\n' or '\r\n'.</dd>
 *
 *   <dt>Delimiter (for values)</dt>
 *   <dd>The delimiter for values is freely configurable (default ',').</dd>
 *
 *   <dt>Comments</dt>
 *   <dd>Some CSV-dialects support a simple comment syntax. A comment is a record
 *       which must start with a designated character (the commentStarter). A record
 *       of this kind is treated as comment and gets removed from the input (default none)</dd>
 *
 *   <dt>Encapsulator</dt>
 *  <dd>Two encapsulator characters (default '"') are used to enclose -&gt; complex values.</dd>
 *
 *   <dt>Simple values</dt>
 *   <dd>A simple value consist of all characters (except the delimiter) until
 *       (but not including) the next delimiter or a record-terminator. Optionally
 *       all surrounding whitespaces of a simple value can be ignored (default: true).</dd>
 *
 *   <dt>Complex values</dt>
 *   <dd>Complex values are encapsulated within a pair of the defined encapsulator characters.
 *       The encapsulator itself must be escaped or doubled when used inside complex values.
 *       Complex values preserve all kind of formatting (including newlines -&gt; multiline-values)</dd>
 *
 *  <dt>Empty line skipping</dt>
 *   <dd>Optionally empty lines in CSV files can be skipped.
 *       Otherwise, empty lines will return a record with a single empty value.</dd>
 * </dl>
 *
 * <p>In addition to individually defined dialects, two predefined dialects (strict-csv, and excel-csv)
 *    can be set directly.</p> <!-- TODO fix -->
 *
 * <p>Example usage:</p>
 * <blockquote><pre>
 * Reader in = new StringReader("a,b,c");
 * for (CSVRecord record : CSVFormat.DEFAULT.parse(in)) {
 *     for (String field : record) {
 *         System.out.print("\"" + field + "\", ");
 *     }
 *     System.out.println();
 * }
 * </pre></blockquote>
 */

package org.apache.commons.csv;
