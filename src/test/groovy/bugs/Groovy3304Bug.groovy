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

import org.codehaus.groovy.control.MultipleCompilationErrorsException

class Groovy3304Bug extends GroovyTestCase {
    void testBreakAfterSwitchCausesSyntaxError() {
        try {
            new GroovyShell().parse("switch(x) {}\nbreak")
            fail()
        } catch (MultipleCompilationErrorsException e) {
            def syntaxError = e.errorCollector.getSyntaxError(0)
            assert syntaxError
            assert syntaxError.line == 2
        }
    }
}
