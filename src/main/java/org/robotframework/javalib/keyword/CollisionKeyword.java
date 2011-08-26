/*
 * Copyright 2008 Nokia Siemens Networks Oyj
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.robotframework.javalib.keyword;

/**
 * If a keyword name collision is detected, an instance of this class will
 * be associated with the colliding name. Execution of this keyword will
 * notify the user of the collision and the two classes that have the same
 * keyword name.
 */
public class CollisionKeyword implements Keyword {
    private String implementingClassName1;
    private String implementingClassName2;

    /**
     * Creates a collision keyword
     * 
     * @param implementingClassName1 name of first class with colliding
     *                               keyword name
     * @param implementingClassName2 name of second class with colliding
     *                               keyword name
     */
    public CollisionKeyword(String implementingClassName1, String implementingClassName2) {
        this.implementingClassName1 = implementingClassName1;
        this.implementingClassName2 = implementingClassName2;
    }

    /**
     * Throws a {@link KeywordNameCollisionException} with an error
     * message notifying the user of the collision and classes causing
     * the collision.
     * 
     * @throws KeywordNameCollisionException always throws this exception
     */
    public Object execute(Object[] arguments) {
        throw new KeywordNameCollisionException("Two keywords with same name not allowed. Alternative implementations available from " + implementingClassName1 + " and " + implementingClassName2 + ".");
    }
}
