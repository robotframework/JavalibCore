package org.robotframework.javalib.keyword;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CollisionKeywordTest {

    @Test
    public void testExecutionThrowsException() {
        String implementingClassName1 = "class org.robotframework.somecomponent.ImplementingClass";
        String implementingClassName2 = "class org.robotframework.othercomponent.OtherImplementingClass";
        CollisionKeyword collisionKeyword = new CollisionKeyword(implementingClassName1, implementingClassName2);


        KeywordNameCollisionException e = assertThrows(KeywordNameCollisionException.class, () -> collisionKeyword.execute(null, null));

        assertEquals("Two keywords with same name not allowed. Alternative implementations available from " + implementingClassName1 + " and " + implementingClassName2 + ".", e.getMessage());
    }
}
