from java.util import ArrayList
LIST = ['One', -2, False]
EMPTY_LIST = []

keyword_patterns = ArrayList()
keyword_patterns.add("org/**/keyword/**/**.class")
keyword_patterns.add("com/**/keyword/**/**.class")


duplicate_keyword_patterns = ArrayList()
duplicate_keyword_patterns.add("com/**/keyword/**/**.class")
duplicate_keyword_patterns.add("my/same/keyword/**/**.class")
