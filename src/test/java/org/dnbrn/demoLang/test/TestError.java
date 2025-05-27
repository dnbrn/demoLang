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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

public class TestError extends ExampleLangAbstractTest {

  private static Network internet;
  private static Host server;
  private static Host pc;
  private static User alice;
  private static Password passwordServerAlice;
  private static Password passwordPCAlice;

  @BeforeAll
  public static void setup() {
    internet = new Network("internet");
    server = new Host("server");

    internet.addHosts(server);
  }

  @AfterAll
  public static void clear() {
  }

  @Test
  public void testGuessPassword() {

    Attacker attacker = new Attacker();

    attacker.addAttackPoint(internet.access);
    attacker.addAttackPoint(server.guessPassword);

    try {
      // simulate crash
      //throw new RuntimeException("Simulated crash during attack");

      attacker.attack();
      server.access.assertCompromisedWithEffort();
    } catch (Exception e) {
      JSONTarget.markCrashed(e);
      ConsoleTarget.logCrash(e);
    }
  }

  @Test
  public void testGuessPassword2() {

    Attacker attacker = new Attacker();

    attacker.addAttackPoint(internet.access);

    try {
      // simulate crash
      throw new RuntimeException("Simulated crash during attack");

      //attacker.attack();
      //server.access.assertUncompromised();
    } catch (Exception e) {
      JSONTarget.markCrashed(e);
      ConsoleTarget.logCrash(e);
    }
  }
}
