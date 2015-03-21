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
package groovy.lang

class InnerClassResolvingTest extends GroovyTestCase {
    public void testInnerClass() {
        // Thread.UncaughtExceptionHandler was added in Java 1.5
        if (System.properties.'java.version'[2] >= '5') {
            def script = '''
                def caught = false
                def t = Thread.start {
                    Thread.setDefaultUncaughtExceptionHandler(
                        {thread,ex -> caught=true} as Thread.UncaughtExceptionHandler)
                    throw new Exception("huhu")
                }
                t.join()
                assert caught==true
            '''
            new GroovyShell().evaluate(script)
        }
    }

    public void testInnerClassWithPartialMatchOnImport() {
        if (System.properties.'java.version'[2] >= '5') {
            def script = '''
                import java.lang.Thread as X
                X.UncaughtExceptionHandler y = null
            '''
            new GroovyShell().evaluate(script)
        }
    }


}
