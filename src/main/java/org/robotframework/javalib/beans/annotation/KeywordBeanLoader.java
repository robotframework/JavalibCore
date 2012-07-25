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

package org.robotframework.javalib.beans.annotation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.robotframework.javalib.beans.common.IClassFilter;
import org.robotframework.javalib.util.AntPathMatcher;
import org.robotframework.javalib.util.KeywordNameNormalizer;

public class KeywordBeanLoader implements IBeanLoader {
    protected String keywordPattern = null;
    private ClassLoader loader;

    public KeywordBeanLoader(String keywordPattern, ClassLoader loader) {
        this.keywordPattern = keywordPattern;
        this.loader = loader;
    }

    private String getRoot() {
        return keywordPattern.substring(0, keywordPattern.indexOf('*'));
    }

    public Map loadBeanDefinitions(IClassFilter classFilter) {
        Map kws = new HashMap<String, Object>();
        Enumeration<URL> entries = getRootResources();
        while (entries.hasMoreElements()) {
            URL url = entries.nextElement();
            try {
                addURLKeywords(classFilter, kws, url);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return kws;
    }

    private void addURLKeywords(IClassFilter classFilter, Map kws, URL url) throws IOException {
        if (url.getProtocol().startsWith("jar")) {
            addJarKeywords(classFilter, kws, url);
        } else {
            addFileKeywords(classFilter, kws, url);
        }
    }

    private void addFileKeywords(IClassFilter classFilter, Map kws, URL url) {
        if (new File(url.getFile()).isDirectory()) {
            for (String f: getChildrenFrom(getRoot(), new File(url.getFile())))
                   addKeyword(classFilter, kws, f);

        }
    }

    private void addJarKeywords(IClassFilter classFilter, Map kws, URL url) throws IOException {
        JarURLConnection connection =
                    (JarURLConnection) url.openConnection();
        File jar = new File(connection.getJarFileURL().getFile());
        JarInputStream is = new JarInputStream(new FileInputStream(jar));

        JarEntry entry;
        while( (entry = is.getNextJarEntry()) != null) {
            if(entry.getName().endsWith(".class")) {
                addKeyword(classFilter, kws, entry.getName());
            }
        }
    }

    private Enumeration<URL> getRootResources() {
        String root = getRoot();
        try {
            return loader.getResources(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<String> getChildrenFrom(String root, File file) {
        ArrayList<String> classes = new ArrayList<String>();
        for (File f: file.listFiles()) {
            if (f.isFile()) {
                if (f.getName().endsWith(".class"))
                    classes.add(root + f.getName());
            } else
                classes.addAll(getChildrenFrom(root + f.getName() + "/", f));
        }
        return classes;
    }

    private void addKeyword(IClassFilter classFilter, Map<String, Object> kws, String className) {
        if (className.indexOf("$")!=-1)
            return;
        if (!new AntPathMatcher().match(keywordPattern, className))
            return;
        try {
            if (className.startsWith("java"))
                return;
            String name = className.substring(0, className.length() - 6);
            Class cls = loader.loadClass(name.replace("/", "."));
            if (classFilter.accept(cls))
                kws.put(new KeywordNameNormalizer().normalize(name), cls.newInstance());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
