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
	Overloaded	one	${2}
	Overloaded	one	two	three

Can convert numeric argument types
	Keyword With Numeric Arguments	${42}	${42}	${42}	${42}

Can pass objects as arguments
	${object} =	Get some object
	keywordWithObjectArgument	${object}

Positional
    Various Args    hello    world                # Logs 'arg: hello' and 'vararg: world'.

Named
    Various Args    arg=value                     # Logs 'arg: value'.

Kwargs
    Various Args    1    a=1    b=2    c=3             # Logs 'kwarg: a 1', 'kwarg: b 2' and 'kwarg: c 3'.
    Various Args    1    c=3    a=1    b=2             # Same as above. Order does not matter.

Positional and kwargs
    Various Args    1    2    kw=3                # Logs 'arg: 1', 'vararg: 2' and 'kwarg: kw 3'.

Named and kwargs
    Various Args    arg=value      hello=world    # Logs 'arg: value' and 'kwarg: hello world'.
    Various Args    hello=world    arg=value      # Same as above. Order does not matter.

Only varargs
    Only varargs    testThing

Default and varargs
    Default and varargs    Non-default
