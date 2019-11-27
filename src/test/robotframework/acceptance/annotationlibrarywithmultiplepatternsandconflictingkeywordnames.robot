*** Settings ***
Variables	variables.py

*** Test Cases ***
Duplicate Keywords Prevents Importing Library
	runKeywordAndExpectError	Getting keyword names from library 'org.robotframework.javalib.library.AnnotationLibrary' failed: Calling dynamic method 'getKeywordNames' failed: Two keywords with name 'myFailingKeyword' found!
	...	Import Library	org.robotframework.javalib.library.AnnotationLibrary	${DUPLICATE KEYWORD PATTERNS}
