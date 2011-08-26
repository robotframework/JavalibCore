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

import java.io.*;
import java.util.Calendar;
import java.util.Properties;

public class Logger {
    private static PrintStream out = System.out;

    static {
        Properties props = loadProperties("/logging.properties");
        String logFile = props.getProperty("logFile", "");
        boolean append = Boolean.parseBoolean(props.getProperty("append", "false"));
        PrintStream outStream = createOutStream(logFile, append);
        setOut(outStream);
    }

    public static void setOut(PrintStream out) {
        Logger.out = out;
    }
    
    public static void log(Object message) {
        out.println("[" + callPoint() + "] [" + getTimestamp() + "] " + message.toString());
    }

    private static String getTimestamp() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND);
    }

    private static StackTraceElement callPoint() {
        return Thread.currentThread().getStackTrace()[3];
    }
    
    private static Properties loadProperties(String name) {
        Properties properties = new Properties();
        InputStream resourceAsStream = Logger.class.getResourceAsStream(name);
        if (resourceAsStream != null)
            return loadProperties(properties, resourceAsStream);
        else
            return properties;
    }

    private static Properties loadProperties(Properties properties, InputStream resourceAsStream) {
        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

    private static PrintStream createOutStream(String logFile, boolean append) {
        if (logFile == null || logFile.length() == 0) return new NullPrintStream();
        
        try {
            return new PrintStream(new FileOutputStream(logFile, append), true);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("logFile " + logFile + " could not be created", e);
        }
    }
}

class NullPrintStream extends PrintStream {
    public NullPrintStream() {
        super(new OutputStream() {
            @Override
            public void write(byte[] b, int off, int len) throws IOException {}
            @Override
            public void write(int b) throws IOException {}
        });
    }
}

