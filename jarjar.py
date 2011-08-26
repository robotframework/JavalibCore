#!/usr/bin/env python

import glob
import os
import subprocess
import sys
import zipfile
import shutil

def jarjar_dist_jar():
    curdir = os.path.dirname(os.path.abspath(__file__))
    targetdir = os.path.join(curdir, 'target')
    jarpath = glob.glob(os.path.join(targetdir,
                                    'javalib-core-*-jar-with-dependencies.jar'))[0]
    mf_path = extract_manifest(jarpath, targetdir)
    jarjar(curdir, jarpath)
    tmpdir = os.path.join(targetdir, 'tmp')
    if os.path.exists(tmpdir):
        shutil.rmtree(tmpdir) 
    rmi_compile(jarpath, tmpdir)
    rejar(jarpath, mf_path, tmpdir)
    shutil.rmtree(tmpdir)
    os.remove(mf_path)

def call(cmd):
    print " ".join(cmd)
    return subprocess.call(cmd)

def jarjar(curdir, jarpath):
    jarjardir = os.path.join(curdir, 'jarjar')
    jarjarjar = os.path.join(jarjardir, 'jarjar-1.0.jar')
    rules = os.path.join(jarjardir, 'rules.txt')
    call(['java', '-jar', jarjarjar, 'process', rules, jarpath, jarpath])

def extract_manifest(jarpath, targetdir):
    zfobj = zipfile.ZipFile(jarpath)
    for name in zfobj.namelist():
        if name == 'META-INF/MANIFEST.MF':
            mf_path = os.path.join(targetdir, 'MANIFEST.MF')
            outfile = open(mf_path, 'wb')
            outfile.write(zfobj.read(name))
            outfile.close()
            return mf_path

def rmi_compile(jarpath, tmpdir):
    unzip_file_into_dir(jarpath, tmpdir)
    class_name = 'org.robotframework.jvmconnector.org.springframework.remoting.rmi.RmiInvocationWrapper'
    call(['rmic', '-verbose', '-classpath', tmpdir, '-d', tmpdir, class_name])
    call(['rmic', '-verbose', '-iiop', '-always', '-classpath', tmpdir, '-d', 
          tmpdir, class_name])

def unzip_file_into_dir(file, dir):
    os.mkdir(dir, 0777)
    zfobj = zipfile.ZipFile(file)
    for name in zfobj.namelist():
        if name.endswith('/'):
            os.makedirs(os.path.join(dir, name))
        else:
            outfile = open(os.path.join(dir, name), 'wb')
            outfile.write(zfobj.read(name))
            outfile.close()

def rejar(jarpath, mf_path, dir):
    call(['jar', 'cfm', jarpath, mf_path, '-C', dir, '.'])

if __name__ == '__main__':
    jarjar_dist_jar()
