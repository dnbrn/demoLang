/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dnbrn.demoLang.test;

import core.*;
import core.coverage.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(CoverageExtension.class)
public class ExampleLangAbstractTest {
    @RegisterExtension
    public CoverageExtension coverageJSON = new CoverageExtension(new JSONTarget());

    @RegisterExtension
    public CoverageExtension coverageConsole = new CoverageExtension(new ConsoleTarget());

    @BeforeEach
    public void clearMdl() {
        new Attacker().reset();
    }

    @AfterAll
    public static void clearEncryptionDefaults() {
        // This is just a fallback if the extending class doesn't override it
    }

}
