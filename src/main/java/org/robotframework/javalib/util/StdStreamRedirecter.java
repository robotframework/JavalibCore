/*
 * Copyright 2008 Nokia Siemens Networks Oyj
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.robotframework.javalib.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * Handles the STDOUT and STDERR redirection. Used for remote logging.
 */
public class StdStreamRedirecter {
    private static final String LOG_ENCODING = "utf-8";
    final PrintStream stdOut = System.out;
    final PrintStream stdErr = System.err;
    private ByteArrayOutputStream redirectedSystemOut = new ByteArrayOutputStream();
    private ByteArrayOutputStream redirectedSystemErr = new ByteArrayOutputStream();

    /**
     * Retrieves everything printed to STDERR as string.
     * {@link #redirectStdStreams()} must be called before calling this method.
     * After each call to this method, the internal buffer containing STDERR as
     * string is emptied.
     * 
     * @return STDERR as string
     */
    public String getStdErrAsString() {
        return byteStreamToString(redirectedSystemErr);
    }

    /**
     * Retrieves everything printed to STDOUT as string.
     * {@link #redirectStdStreams()} must be called before calling this method.
     * After each call to this method, the internal buffer containing STDOUT as
     * string is emptied.
     * 
     * @return STDOUT as string
     */
    public String getStdOutAsString() {
        return byteStreamToString(redirectedSystemOut);
    }

    /**
     * Initializes the redirection of STDOUT and STDERR so they can be accessed
     * with <code>getStdOutAsString</code> and <code>getStdErrAsString</code>.
     */
    public void redirectStdStreams() {
        try {
            System.setOut(new PrintStream(redirectedSystemOut, false, LOG_ENCODING));
            System.setErr(new PrintStream(redirectedSystemErr, false, LOG_ENCODING));
        } catch (UnsupportedEncodingException e) {
            new RuntimeException(e);
        }
    }

    /**
     * Resets STDOUT and STDERR as they were before calling
     * <code>redirectStdStreams</code>.
     */
    public void resetStdStreams() {
        System.setOut(stdOut);
        System.setErr(stdErr);
    }

    private String byteStreamToString(ByteArrayOutputStream byteStream) {
        String retVal = null;
        try {
            retVal = byteStream.toString(LOG_ENCODING);
        } catch (UnsupportedEncodingException e) {
            new RuntimeException(e);
        }
        byteStream.reset();
        return retVal;
    }
}
