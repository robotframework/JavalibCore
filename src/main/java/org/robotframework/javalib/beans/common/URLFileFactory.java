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


package org.robotframework.javalib.beans.common;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class URLFileFactory {
    private final String localDirectoryPath;

    public URLFileFactory(String localDirectoryPath) {
        this.localDirectoryPath = localDirectoryPath;
    }

    public File createFileFromUrl(String url) {
        File localFile = createLocalFile(url);
        if (shouldCopyFromURL(url, localFile))
            copyUrlToFile(url, localFile);
        return localFile;
    }
    
    private File createLocalFile(String url) {
        return new File(createFileNameFromUrl(url));
    }

    private boolean shouldCopyFromURL(String url, File localFile) {
        return !localFile.exists() || urlLastModified(url) > localFile.lastModified();
    }
    
    private String createFileNameFromUrl(String url) {
        String fileSeparator = System.getProperty("file.separator");
        return localDirectoryPath + fileSeparator + FilenameUtils.getBaseName(url) + "." + FilenameUtils.getExtension(url);
    }

    private void copyUrlToFile(String url, File localFile) {
        try {
            FileUtils.copyURLToFile(createURL(url), localFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private URL createURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    
    private long urlLastModified(String url) {
        try {
            return createURL(url).openConnection().getLastModified();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
