/*
 * Copyright 2003-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package groovy.bugs

/**
 * Mixes variables, closures and method calls in markup
 *
 * @version $Revision$
 */
class MarkupAndMethodBug extends GroovyTestCase {
    
    void testBug() {
        def tree = createTree()
        def name = tree.person[0]['@name']
        assert name == 'James'
    }
    
    protected def createTree() {
        def builder = NodeBuilder.newInstance()
        
        def root = builder.people() {
            person(name:getTestName())
        }
        
        assert root != null
        
        return root
    }
    
    protected def getTestName() {
        "James"
    }
}
