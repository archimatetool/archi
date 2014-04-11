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

import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Specifies the format of a CSV file and parses input.
 *
 * <h4>Using predefined formats</h4>
 *
 * <p>
 * You can use one of the predefined formats:
 * </p>
 *
 * <ul>
 *      <li>{@link #DEFAULT}</li>
 *      <li>{@link #EXCEL}</li>
 *      <li>{@link #MYSQL}</li>
 *      <li>{@link #RFC4180}</li>
 *      <li>{@link #TDF}</li>
 * </ul>
 *
 * <p>
 * For example:
 * </p>
 *
 * <pre>
 * CSVParser parser = CSVFormat.EXCEL.parse(reader);
 * </pre>
 *
 * <p>
 * The {@link CSVRecord} provides static methods to parse other input types, for example:
 * </p>
 *
 * <pre>CSVParser parser = CSVFormat.parseFile(file, CSVFormat.EXCEL);</pre>
 *
 * <h4>Defining formats</h4>
 *
 * <p>
 * You can extend a format by calling the {@code with} methods. For example:
 * </p>
 *
 * <pre>
 * CSVFormat.EXCEL
 *   .withNullString(&quot;N/A&quot;)
 *   .withIgnoreSurroundingSpaces(true);
 * </pre>
 *
 * <h4>Defining column names</h4>
 *
 * <p>
 * To define the column names you want to use to access records, write:
 * </p>
 *
 * <pre>
 * CSVFormat.EXCEL.withHeader(&quot;Col1&quot;, &quot;Col2&quot;, &quot;Col3&quot;);
 * </pre>
 *
 * <p>
 * Calling {@link #withHeader(String...)} let's you use the given names to address values in a {@link CSVRecord}, and
 * assumes that your CSV source does not contain a first record that also defines column names.
 *
 * If it does, then you are overriding this metadata with your names and you should skip the first record by calling
 * {@link #withSkipHeaderRecord(boolean)} with {@code true}.
 * </p>
 *
 * <h4>Parsing</h4>
 *
 * <p>
 * You can use a format directly to parse a reader. For example, to parse an Excel file with columns header, write:
 * </p>
 *
 * <pre>
 * Reader in = ...;
 * CSVFormat.EXCEL.withHeader(&quot;Col1&quot;, &quot;Col2&quot;, &quot;Col3&quot;).parse(in);
 * </pre>
 *
 * <p>
 * For other input types, like resources, files, and URLs, use the static methods on {@link CSVParser}.
 * </p>
 *
 * <h4>Referencing columns safely</h4>
 *
 * <p>
 * If your source contains a header record, you can simplify your code and safely reference columns,
 * by using {@link #withHeader(String...)} with no arguments:
 * </p>
 *
 * <pre>
 * CSVFormat.EXCEL.withHeader();
 * </pre>
 *
 * <p>
 * This causes the parser to read the first record and use its values as column names.
 *
 * Then, call one of the {@link CSVRecord} get method that takes a String column name argument:
 * </p>
 *
 * <pre>
 * String value = record.get(&quot;Col1&quot;);
 * </pre>
 *
 * <p>
 * This makes your code impervious to changes in column order in the CSV file.
 * </p>
 *
 * <h4>Notes</h4>
 *
 * <p>
 * This class is immutable.
 * </p>
 *
 */
@SuppressWarnings("nls")
public final class CSVFormat implements Serializable {

    private static final long serialVersionUID = 1L;

    private final char delimiter;
    private final Character quoteChar; // null if quoting is disabled
    private final Quote quotePolicy;
    private final Character commentStart; // null if commenting is disabled
    private final Character escape; // null if escaping is disabled
    private final boolean ignoreSurroundingSpaces; // Should leading/trailing spaces be ignored around values?
    private final boolean ignoreEmptyLines;
    private final String recordSeparator; // for outputs
    private final String nullString; // the string to be used for null values
    private final String[] header;
    private final boolean skipHeaderRecord;

    /**
     * Standard comma separated format, as for {@link #RFC4180} but allowing empty lines.
     * <h3>RFC 4180:</h3>
     * <ul>
     * <li>withDelimiter(',')</li>
     * <li>withQuoteChar('"')</li>
     * <li>withRecordSeparator(CRLF)</li>
     * </ul>
     * <h3>Additional:</h3>
     * <ul>
     * <li>withIgnoreEmptyLines(true)</li>
     * </ul>
     */
    public static final CSVFormat DEFAULT = new CSVFormat(COMMA, DOUBLE_QUOTE_CHAR, null, null, null,
                                                            false, true, CRLF, null, null, false);

    /**
     * Comma separated format as defined by <a href="http://tools.ietf.org/html/rfc4180">RFC 4180</a>.
     * <h3>RFC 4180:</h3>
     * <ul>
     * <li>withDelimiter(',')</li>
     * <li>withQuoteChar('"')</li>
     * <li>withRecordSeparator(CRLF)</li>
     * </ul>
     */
    public static final CSVFormat RFC4180 = DEFAULT.withIgnoreEmptyLines(false);

    /**
     * Excel file format (using a comma as the value delimiter). Note that the actual value delimiter used by Excel is
     * locale dependent, it might be necessary to customize this format to accommodate to your regional settings.
     * <p/>
     * For example for parsing or generating a CSV file on a French system the following format will be used:
     *
     * <pre>
     * CSVFormat fmt = CSVFormat.newBuilder(EXCEL).withDelimiter(';');
     * </pre>
     * Settings are:
     * <ul>
     * <li>withDelimiter(',')</li>
     * <li>withQuoteChar('"')</li>
     * <li>withRecordSeparator(CRLF)</li>
     * </ul>
     * Note: this is currently the same as RFC4180
     */
    public static final CSVFormat EXCEL = DEFAULT.withIgnoreEmptyLines(false);

    /** Tab-delimited format, with quote; leading and trailing spaces ignored. */
    public static final CSVFormat TDF =
            DEFAULT
            .withDelimiter(TAB)
            .withIgnoreSurroundingSpaces(true);

    /**
     * Default MySQL format used by the <tt>SELECT INTO OUTFILE</tt> and <tt>LOAD DATA INFILE</tt> operations. This is
     * a tab-delimited format with a LF character as the line separator. Values are not quoted and special characters
     * are escaped with '\'.
     *
     * @see <a href="http://dev.mysql.com/doc/refman/5.1/en/load-data.html">
     *      http://dev.mysql.com/doc/refman/5.1/en/load-data.html</a>
     */
    public static final CSVFormat MYSQL =
            DEFAULT
            .withDelimiter(TAB)
            .withEscape(BACKSLASH)
            .withIgnoreEmptyLines(false)
            .withQuoteChar(null)
            .withRecordSeparator(LF);

    /**
     * Returns true if the given character is a line break character.
     *
     * @param c
     *            the character to check
     *
     * @return true if <code>c</code> is a line break character
     */
    private static boolean isLineBreak(final char c) {
        return c == LF || c == CR;
    }

    /**
     * Returns true if the given character is a line break character.
     *
     * @param c
     *            the character to check, may be null
     *
     * @return true if <code>c</code> is a line break character (and not null)
     */
    private static boolean isLineBreak(final Character c) {
        return c != null && isLineBreak(c.charValue());
    }

    /**
     * Creates a new CSV format with the specified delimiter.
     *
     * @param delimiter
     *            the char used for value separation, must not be a line break character
     * @return a new CSV format.
     * @throws IllegalArgumentException if the delimiter is a line break character
     */
    public static CSVFormat newFormat(final char delimiter) {
        return new CSVFormat(delimiter, null, null, null, null, false, false, null, null, null, false);
    }

    /**
     * Creates a customized CSV format.
     *
     * @param delimiter
     *            the char used for value separation, must not be a line break character
     * @param quoteChar
     *            the Character used as value encapsulation marker, may be {@code null} to disable
     * @param quotePolicy
     *            the quote policy
     * @param commentStart
     *            the Character used for comment identification, may be {@code null} to disable
     * @param escape
     *            the Character used to escape special characters in values, may be {@code null} to disable
     * @param ignoreSurroundingSpaces
     *            <tt>true</tt> when whitespaces enclosing values should be ignored
     * @param ignoreEmptyLines
     *            <tt>true</tt> when the parser should skip empty lines
     * @param recordSeparator
     *            the line separator to use for output
     * @param nullString
     *            the line separator to use for output
     * @param header
     *            the header
     * @param skipHeaderRecord TODO
     * @throws IllegalArgumentException if the delimiter is a line break character
     */
    // package protected to give access without needing a synthetic accessor
    CSVFormat(final char delimiter, final Character quoteChar,
            final Quote quotePolicy, final Character commentStart,
            final Character escape, final boolean ignoreSurroundingSpaces,
            final boolean ignoreEmptyLines, final String recordSeparator,
            final String nullString, final String[] header, final boolean skipHeaderRecord) {
        if (isLineBreak(delimiter)) {
            throw new IllegalArgumentException("The delimiter cannot be a line break");
        }
        this.delimiter = delimiter;
        this.quoteChar = quoteChar;
        this.quotePolicy = quotePolicy;
        this.commentStart = commentStart;
        this.escape = escape;
        this.ignoreSurroundingSpaces = ignoreSurroundingSpaces;
        this.ignoreEmptyLines = ignoreEmptyLines;
        this.recordSeparator = recordSeparator;
        this.nullString = nullString;
        this.header = header == null ? null : header.clone();
        this.skipHeaderRecord = skipHeaderRecord;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final CSVFormat other = (CSVFormat) obj;
        if (delimiter != other.delimiter) {
            return false;
        }
        if (quotePolicy != other.quotePolicy) {
            return false;
        }
        if (quoteChar == null) {
            if (other.quoteChar != null) {
                return false;
            }
        } else if (!quoteChar.equals(other.quoteChar)) {
            return false;
        }
        if (commentStart == null) {
            if (other.commentStart != null) {
                return false;
            }
        } else if (!commentStart.equals(other.commentStart)) {
            return false;
        }
        if (escape == null) {
            if (other.escape != null) {
                return false;
            }
        } else if (!escape.equals(other.escape)) {
            return false;
        }
        if (!Arrays.equals(header, other.header)) {
            return false;
        }
        if (ignoreSurroundingSpaces != other.ignoreSurroundingSpaces) {
            return false;
        }
        if (ignoreEmptyLines != other.ignoreEmptyLines) {
            return false;
        }
        if (recordSeparator == null) {
            if (other.recordSeparator != null) {
                return false;
            }
        } else if (!recordSeparator.equals(other.recordSeparator)) {
            return false;
        }
        return true;
    }

    /**
     * Formats the specified values.
     *
     * @param values
     *            the values to format
     * @return the formatted values
     */
    public String format(final Object... values) {
        final StringWriter out = new StringWriter();
        try {
            new CSVPrinter(out, this).printRecord(values);
            return out.toString().trim();
        } catch (final IOException e) {
            // should not happen because a StringWriter does not do IO.
            throw new IllegalStateException(e);
        }
    }

    /**
     * Returns the character marking the start of a line comment.
     *
     * @return the comment start marker, may be {@code null}
     */
    public Character getCommentStart() {
        return commentStart;
    }

    /**
     * Returns the character delimiting the values (typically ';', ',' or '\t').
     *
     * @return the delimiter character
     */
    public char getDelimiter() {
        return delimiter;
    }

    /**
     * Returns the escape character.
     *
     * @return the escape character, may be {@code null}
     */
    public Character getEscape() {
        return escape;
    }

    /**
     * Returns a copy of the header array.
     *
     * @return a copy of the header array
     */
    public String[] getHeader() {
        return header != null ? header.clone() : null;
    }

    /**
     * Specifies whether empty lines between records are ignored when parsing input.
     *
     * @return <tt>true</tt> if empty lines between records are ignored, <tt>false</tt> if they are turned into empty
     *         records.
     */
    public boolean getIgnoreEmptyLines() {
        return ignoreEmptyLines;
    }

    /**
     * Specifies whether spaces around values are ignored when parsing input.
     *
     * @return <tt>true</tt> if spaces around values are ignored, <tt>false</tt> if they are treated as part of the
     *         value.
     */
    public boolean getIgnoreSurroundingSpaces() {
        return ignoreSurroundingSpaces;
    }

    /**
     * Gets the String to convert to and from {@code null}.
     * <ul>
     * <li>
     * <strong>Reading:</strong> Converts strings equal to the given {@code nullString} to {@code null} when reading
     * records.
     * </li>
     * <li>
     * <strong>Writing:</strong> Writes {@code null} as the given {@code nullString} when writing records.</li>
     * </ul>
     *
     * @return the String to convert to and from {@code null}. No substitution occurs if {@code null}
     */
    public String getNullString() {
        return nullString;
    }

    /**
     * Returns the character used to encapsulate values containing special characters.
     *
     * @return the quoteChar character, may be {@code null}
     */
    public Character getQuoteChar() {
        return quoteChar;
    }

    /**
     * Returns the quote policy output fields.
     *
     * @return the quote policy
     */
    public Quote getQuotePolicy() {
        return quotePolicy;
    }

    /**
     * Returns the line separator delimiting output records.
     *
     * @return the line separator
     */
    public String getRecordSeparator() {
        return recordSeparator;
    }

    /**
     * Returns whether to skip the header record.
     *
     * @return whether to skip the header record.
     */
    public boolean getSkipHeaderRecord() {
        return skipHeaderRecord;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;

        result = prime * result + delimiter;
        result = prime * result + ((quotePolicy == null) ? 0 : quotePolicy.hashCode());
        result = prime * result + ((quoteChar == null) ? 0 : quoteChar.hashCode());
        result = prime * result + ((commentStart == null) ? 0 : commentStart.hashCode());
        result = prime * result + ((escape == null) ? 0 : escape.hashCode());
        result = prime * result + (ignoreSurroundingSpaces ? 1231 : 1237);
        result = prime * result + (ignoreEmptyLines ? 1231 : 1237);
        result = prime * result + ((recordSeparator == null) ? 0 : recordSeparator.hashCode());
        result = prime * result + Arrays.hashCode(header);
        return result;
    }

    /**
     * Specifies whether comments are supported by this format.
     *
     * Note that the comment introducer character is only recognized at the start of a line.
     *
     * @return <tt>true</tt> is comments are supported, <tt>false</tt> otherwise
     */
    public boolean isCommentingEnabled() {
        return commentStart != null;
    }

    /**
     * Returns whether escape are being processed.
     *
     * @return {@code true} if escapes are processed
     */
    public boolean isEscaping() {
        return escape != null;
    }

    /**
     * Returns whether a nullString has been defined.
     *
     * @return {@code true} if a nullString is defined
     */
    public boolean isNullHandling() {
        return nullString != null;
    }

    /**
     * Returns whether a quoteChar has been defined.
     *
     * @return {@code true} if a quoteChar is defined
     */
    public boolean isQuoting() {
        return quoteChar != null;
    }

    /**
     * Parses the specified content.
     *
     * <p>
     * See also the various static parse methods on {@link CSVParser}.
     * </p>
     *
     * @param in
     *            the input stream
     * @return a parser over a stream of {@link CSVRecord}s.
     * @throws IOException
     *             If an I/O error occurs
     */
    public CSVParser parse(final Reader in) throws IOException {
        return new CSVParser(in, this);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Delimiter=<").append(delimiter).append('>');
        if (isEscaping()) {
            sb.append(' ');
            sb.append("Escape=<").append(escape).append('>');
        }
        if (isQuoting()) {
            sb.append(' ');
            sb.append("QuoteChar=<").append(quoteChar).append('>');
        }
        if (isCommentingEnabled()) {
            sb.append(' ');
            sb.append("CommentStart=<").append(commentStart).append('>');
        }
        if (isNullHandling()) {
            sb.append(' ');
            sb.append("NullString=<").append(nullString).append('>');
        }
        if(recordSeparator != null) {
            sb.append(' ');
            sb.append("RecordSeparator=<").append(recordSeparator).append('>');
        }
        if (getIgnoreEmptyLines()) {
            sb.append(" EmptyLines:ignored");
        }
        if (getIgnoreSurroundingSpaces()) {
            sb.append(" SurroundingSpaces:ignored");
        }
        sb.append(" SkipHeaderRecord:").append(skipHeaderRecord);
        if (header != null) {
            sb.append(' ');
            sb.append("Header:").append(Arrays.toString(header));
        }
        return sb.toString();
    }

    /**
     * Verifies the consistency of the parameters and throws an IllegalStateException if necessary.
     *
     * @throws IllegalStateException
     */
    void validate() throws IllegalStateException {
        if (quoteChar != null && delimiter == quoteChar.charValue()) {
            throw new IllegalStateException(
                    "The quoteChar character and the delimiter cannot be the same ('" + quoteChar + "')");
        }

        if (escape != null && delimiter == escape.charValue()) {
            throw new IllegalStateException(
                    "The escape character and the delimiter cannot be the same ('" + escape + "')");
        }

        if (commentStart != null && delimiter == commentStart.charValue()) {
            throw new IllegalStateException(
                    "The comment start character and the delimiter cannot be the same ('" + commentStart + "')");
        }

        if (quoteChar != null && quoteChar.equals(commentStart)) {
            throw new IllegalStateException(
                    "The comment start character and the quoteChar cannot be the same ('" + commentStart + "')");
        }

        if (escape != null && escape.equals(commentStart)) {
            throw new IllegalStateException(
                    "The comment start and the escape character cannot be the same ('" + commentStart + "')");
        }

        if (escape == null && quotePolicy == Quote.NONE) {
            throw new IllegalStateException("No quotes mode set but no escape character is set");
        }

        if (header != null) {
            final Set<String> set = new HashSet<String>(header.length);
            set.addAll(Arrays.asList(header));
            if (set.size() != header.length) {
                throw new IllegalStateException("The header contains duplicate names: " + Arrays.toString(header));
            }
        }
    }

    /**
     * Sets the comment start marker of the format to the specified character.
     *
     * Note that the comment start character is only recognized at the start of a line.
     *
     * @param commentStart
     *            the comment start marker
     * @return A new CSVFormat that is equal to this one but with the specified character as the comment start marker
     * @throws IllegalArgumentException
     *             thrown if the specified character is a line break
     */
    public CSVFormat withCommentStart(final char commentStart) {
        return withCommentStart(Character.valueOf(commentStart));
    }

    /**
     * Sets the comment start marker of the format to the specified character.
     *
     * Note that the comment start character is only recognized at the start of a line.
     *
     * @param commentStart
     *            the comment start marker, use {@code null} to disable
     * @return A new CSVFormat that is equal to this one but with the specified character as the comment start marker
     * @throws IllegalArgumentException
     *             thrown if the specified character is a line break
     */
    public CSVFormat withCommentStart(final Character commentStart) {
        if (isLineBreak(commentStart)) {
            throw new IllegalArgumentException("The comment start character cannot be a line break");
        }
        return new CSVFormat(delimiter, quoteChar, quotePolicy, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord);
    }

    /**
     * Sets the delimiter of the format to the specified character.
     *
     * @param delimiter
     *            the delimiter character
     * @return A new CSVFormat that is equal to this with the specified character as delimiter
     * @throws IllegalArgumentException
     *             thrown if the specified character is a line break
     */
    public CSVFormat withDelimiter(final char delimiter) {
        if (isLineBreak(delimiter)) {
            throw new IllegalArgumentException("The delimiter cannot be a line break");
        }
        return new CSVFormat(delimiter, quoteChar, quotePolicy, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord);
    }

    /**
     * Sets the escape character of the format to the specified character.
     *
     * @param escape
     *            the escape character
     * @return A new CSVFormat that is equal to his but with the specified character as the escape character
     * @throws IllegalArgumentException
     *             thrown if the specified character is a line break
     */
    public CSVFormat withEscape(final char escape) {
        return withEscape(Character.valueOf(escape));
    }

    /**
     * Sets the escape character of the format to the specified character.
     *
     * @param escape
     *            the escape character, use {@code null} to disable
     * @return A new CSVFormat that is equal to this but with the specified character as the escape character
     * @throws IllegalArgumentException
     *             thrown if the specified character is a line break
     */
    public CSVFormat withEscape(final Character escape) {
        if (isLineBreak(escape)) {
            throw new IllegalArgumentException("The escape character cannot be a line break");
        }
        return new CSVFormat(delimiter, quoteChar, quotePolicy, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord);
    }

    /**
     * Sets the header of the format. The header can either be parsed automatically from the input file with:
     *
     * <pre>
     * CSVFormat format = aformat.withHeader();</pre>
     *
     * or specified manually with:
     *
     * <pre>
     * CSVFormat format = aformat.withHeader(&quot;name&quot;, &quot;email&quot;, &quot;phone&quot;);</pre>
     *
     * @param header
     *            the header, <tt>null</tt> if disabled, empty if parsed automatically, user specified otherwise.
     *
     * @return A new CSVFormat that is equal to this but with the specified header
     * @see #withSkipHeaderRecord(boolean)
     */
    public CSVFormat withHeader(final String... header) {
        return new CSVFormat(delimiter, quoteChar, quotePolicy, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord);
    }

    /**
     * Sets the empty line skipping behavior of the format.
     *
     * @param ignoreEmptyLines
     *            the empty line skipping behavior, <tt>true</tt> to ignore the empty lines between the records,
     *            <tt>false</tt> to translate empty lines to empty records.
     * @return A new CSVFormat that is equal to this but with the specified empty line skipping behavior.
     */
    public CSVFormat withIgnoreEmptyLines(final boolean ignoreEmptyLines) {
        return new CSVFormat(delimiter, quoteChar, quotePolicy, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord);
    }

    /**
     * Sets the trimming behavior of the format.
     *
     * @param ignoreSurroundingSpaces
     *            the trimming behavior, <tt>true</tt> to remove the surrounding spaces, <tt>false</tt> to leave the
     *            spaces as is.
     * @return A new CSVFormat that is equal to this but with the specified trimming behavior.
     */
    public CSVFormat withIgnoreSurroundingSpaces(final boolean ignoreSurroundingSpaces) {
        return new CSVFormat(delimiter, quoteChar, quotePolicy, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord);
    }

    /**
     * Performs conversions to and from null for strings on input and output.
     * <ul>
     * <li>
     * <strong>Reading:</strong> Converts strings equal to the given {@code nullString} to {@code null} when reading
     * records.</li>
     * <li>
     * <strong>Writing:</strong> Writes {@code null} as the given {@code nullString} when writing records.</li>
     * </ul>
     *
     * @param nullString
     *            the String to convert to and from {@code null}. No substitution occurs if {@code null}
     *
     * @return A new CSVFormat that is equal to this but with the specified null conversion string.
     */
    public CSVFormat withNullString(final String nullString) {
        return new CSVFormat(delimiter, quoteChar, quotePolicy, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord);
    }

    /**
     * Sets the quoteChar of the format to the specified character.
     *
     * @param quoteChar
     *            the quoteChar character
     * @return A new CSVFormat that is equal to this but with the specified character as quoteChar
     * @throws IllegalArgumentException
     *             thrown if the specified character is a line break
     */
    public CSVFormat withQuoteChar(final char quoteChar) {
        return withQuoteChar(Character.valueOf(quoteChar));
    }

    /**
     * Sets the quoteChar of the format to the specified character.
     *
     * @param quoteChar
     *            the quoteChar character, use {@code null} to disable
     * @return A new CSVFormat that is equal to this but with the specified character as quoteChar
     * @throws IllegalArgumentException
     *             thrown if the specified character is a line break
     */
    public CSVFormat withQuoteChar(final Character quoteChar) {
        if (isLineBreak(quoteChar)) {
            throw new IllegalArgumentException("The quoteChar cannot be a line break");
        }
        return new CSVFormat(delimiter, quoteChar, quotePolicy, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord);
    }

    /**
     * Sets the output quote policy of the format to the specified value.
     *
     * @param quotePolicy
     *            the quote policy to use for output.
     *
     * @return A new CSVFormat that is equal to this but with the specified quote policy
     */
    public CSVFormat withQuotePolicy(final Quote quotePolicy) {
        return new CSVFormat(delimiter, quoteChar, quotePolicy, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord);
    }

    /**
     * Sets the record separator of the format to the specified character.
     *
     * @param recordSeparator
     *            the record separator to use for output.
     *
     * @return A new CSVFormat that is equal to this but with the the specified output record separator
     */
    public CSVFormat withRecordSeparator(final char recordSeparator) {
        return withRecordSeparator(String.valueOf(recordSeparator));
    }

    /**
     * Sets the record separator of the format to the specified String.
     *
     * @param recordSeparator
     *            the record separator to use for output.
     *
     * @return A new CSVFormat that is equal to this but with the the specified output record separator
     */
    public CSVFormat withRecordSeparator(final String recordSeparator) {
        return new CSVFormat(delimiter, quoteChar, quotePolicy, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord);
    }

    /**
     * Sets whether to skip the header record.
     *
     * @param skipHeaderRecord
     *            whether to skip the header record.
     *
     * @return A new CSVFormat that is equal to this but with the the specified skipHeaderRecord setting.
     * @see #withHeader(String...)
     */
    public CSVFormat withSkipHeaderRecord(final boolean skipHeaderRecord) {
        return new CSVFormat(delimiter, quoteChar, quotePolicy, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header, skipHeaderRecord);
    }
}
