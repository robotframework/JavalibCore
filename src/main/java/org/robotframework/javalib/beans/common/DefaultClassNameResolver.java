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

import java.io.IOException;

import org.springframework.core.io.Resource;

/**
 * @see ClassNameResolver
 */
public class DefaultClassNameResolver implements ClassNameResolver {
    /**
     * @throws ClassNameResolvingFailedException
     * @see ClassNameResolver#resolveClassName(Resource, String)
     */
    public String resolveClassName(Resource resource, String packagePrefix) {
        String packagePrefixPath = packagePrefix.replace('.', '/');
        String fileSystemPath = determineFileSystemPath(resource);
        int index = fileSystemPath.lastIndexOf(packagePrefixPath);
        return convertToClassName(fileSystemPath.substring(index));
    }

    private String convertToClassName(String classLocation) {
        int index = classLocation.lastIndexOf('.');
        if (index == -1) {
            throw new IllegalArgumentException("Couldn't convert class location <" + classLocation + "> to class name");
        }
        classLocation = classLocation.substring(0, index);
        return classLocation.replaceAll("/", ".");
    }

    private String determineFileSystemPath(Resource resource) {
        try {
            return resource.getURL().getPath();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't determine filesystem path for resource <" + resource + ">", e);
        }
    }
}
