/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.codehaus.groovy.tools.stubgenerator

final class Groovy8184 extends StringSourcesStubTestCase {

    @Override
    Map<String, String> provideSources() {
        [
            'C8184.java': '''
                class C8184 {
                }
            ''',
            'G.groovy': '''
                @Category(C8184)
                class G {
                    def m() { this }
                }
            ''',
            'J.java': '''
                public class J {
                    Object m() throws Exception {
                        return G.class.getDeclaredMethod("m", C8184.class);
                    }
                }
            ''',
        ]
    }

    @Override
    void verifyStubs() {
        String stub = stubJavaSourceFor('G')
        assert stub.contains("@groovy.lang.Category(value=C8184.class)")

        Object pojo = loader.loadClass('J').getDeclaredConstructor().newInstance()
        assert pojo.m() != null
    }
}
