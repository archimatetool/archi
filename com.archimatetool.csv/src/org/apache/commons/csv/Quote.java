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

/**
 * Defines quote behavior when printing.
 *
 * @version $Id$
 */
public enum Quote {

    /**
     * Quotes all fields.
     */
    ALL,

    /**
     * Quotes fields which contain special characters such as a delimiter, quote character or any of the characters in
     * line separator.
     */
    MINIMAL,

    /**
     * Quotes all non-numeric fields.
     */
    NON_NUMERIC,

    /**
     * Never quotes fields. When the delimiter occurs in data, it is preceded by the current escape character. If the
     * escape character is not set, printing will throw an exception if any characters that require escaping are
     * encountered.
     */
    NONE
}
