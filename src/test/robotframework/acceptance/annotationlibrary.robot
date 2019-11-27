*** Settings ***
library	org.robotframework.javalib.library.AnnotationLibrary	org/**/keyword/**/**.class
library	Collections

*** Variables ***
${testArgument}	some argument

*** Test Cases ***
Finds Annotated Keywords
	${retVal}=	keywordThatReturnsItsArguments	${testArgument}
	shouldBeEqual	${testArgument}	${retVal}

Extracts Inner Exception From Failing Keywords
	runKeywordAndExpectError	Assertion failed	failingKeyword

Creates Keywords That Can Handle Variable Number Of Arguments
	${stackedArguments}=	keywordWithVariableArgumentCount	arg1	arg2	arg3	arg4
	${expected}=	createList	arg2	arg3	arg4
	listsShouldBeEqual	${expected}	${stackedArguments}
	${stackedArguments}=	keywordWithVariableArgumentCount	arg1
	${expected}=	createList
	listsShouldBeEqual	${expected}	${stackedArguments}

Overloaded keyword
	Overloaded	one
	Overloaded	one	2
	Overloaded	one	two	three

Can convert numeric argument types
	Keyword With Numeric Arguments	42	42	42	42

Can pass objects as arguments
	${object} =	Get some object
	keywordWithObjectArgument	${object}
