*Setting*	*Value*
library	org.robotframework.javalib.library.ClassPathLibrary	org/robotframework/j*/keyword/**/**.class

*Variable*	*Value*

*Test Case*	*Action*	*Argument*
Finds Interface Based Keywords
	emptyKeyword
	${returnvalue}=	conflictingKeyword
	shouldBeEqual	${returnvalue}	Classpath Keyword

*Keyword*	*Action*	*Argument*
