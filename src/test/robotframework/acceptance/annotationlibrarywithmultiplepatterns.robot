*** Settings ***
Variables	variables.py
library	org.robotframework.javalib.library.AnnotationLibrary	${KEYWORD PATTERNS}
library	Collections

*** Variables ***
${testArgument}	some argument

*** Test Cases ***
Finds Annotated Keywords
	${retVal}=	keywordThatReturnsItsArguments	${testArgument}
	shouldBeEqual	${testArgument}	${retVal}
	${retVal}=	myKeywordThatReturnsItsArguments	${testArgument}
	shouldBeEqual	${testArgument}	${retVal}

Extracts Inner Exception From Failing Keywords
	runKeywordAndExpectError	Assertion failed	failingKeyword
	runKeywordAndExpectError	Assertion failed	myFailingKeyword
