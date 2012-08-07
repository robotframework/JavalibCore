package org.robotframework.example.keyword;

import org.robotframework.javalib.keyword.Keyword;

public class InterfaceBasedKeyword implements Keyword {
	public Object execute(Object[] arguments) {
		System.out.println("Hello World!");
		return Boolean.TRUE;
	}
}
