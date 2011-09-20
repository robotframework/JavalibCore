== Javalib-core ==

Javalib-core is a common core for all Robot Framework test libraries written in
Java.

Usage instructions can be found on the project pages:
http://code.google.com/p/robotframework-javalibcore/

Rest of this document describes how Javalib-core is developed.

== Building ==

Maven is used as build tool for Javalib-core.
Source code layout follows the normalt Maven convention of src/main and
src/test. JUnit and JMock are used in the unit tests.

=== Robot Tests ===

There are some number of robot tests under src/test/resources/robotframework;
these are executed for example with mvn integration-test


== Releasing ==

A separate Wiki page describes the release procedure:
http://code.google.com/p/robotframework-javalibcore/wiki/Releasing




