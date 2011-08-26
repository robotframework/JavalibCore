package org.robotframework.javalib.keyword;

import junit.framework.TestCase;

public class CollisionKeywordTest extends TestCase {
    public void testExecutionThrowsException() throws Exception {
        String implementingClassName1 = "class org.robotframework.somecomponent.ImplementingClass";
        String implementingClassName2 = "class org.robotframework.othercomponent.OtherImplementingClass";
        CollisionKeyword collisionKeyword = new CollisionKeyword(implementingClassName1, implementingClassName2);

        try {
            collisionKeyword.execute(null);
        } catch(KeywordNameCollisionException e) {
            assertEquals("Two keywords with same name not allowed. Alternative implementations available from " + implementingClassName1 + " and " + implementingClassName2 + ".", e.getMessage());
        }
    }
}
