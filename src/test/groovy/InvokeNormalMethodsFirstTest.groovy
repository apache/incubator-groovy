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
package groovy

/**
 * Invoke normal methods first: if no statically typed method exist, use invokeMethod().
 *
 * @author Guillaume Laforge
 */
class InvokeNormalMethodsFirstTest extends GroovyTestCase {

    void testPrintln() {
        println "call global println function"
    }

    void testStaticMethodOnJdkObject() {
        def myString = " static method "
        def newString = myString.trim()

        assert newString == "static method"
    }

    void testCallClosure() {
        def clos = { msg -> msg + " is Groovy" }
        def str = clos("Guillaume")

        assert str == "Guillaume is Groovy"
    }

    void testCallNormalMethodFromAGroovyDefinedClass() {
        def p = new Printer()
        def str = "Guillaume"
        def result = p.returnSelf(str)

        assert result == str
    }

    void testCallNormalMethodFirstFromWackyObject() {
        def w = new Wacky()
        def str = "Groovy"
        def staticResult = w.returnSelf(str)
        def invokeResult = w.nonExistingMethod(str)

        assert staticResult == str
        assert invokeResult == "invokerMethod call"
    }
}

class Printer {
    String returnSelf(msg) {
        return msg
    }
}

class Wacky {
    String returnSelf(msg) {
        return msg
    }

    Object invokeMethod(String name, Object args) {
        return "invokerMethod call"
    }
}