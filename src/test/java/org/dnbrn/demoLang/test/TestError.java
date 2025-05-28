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
  private static Password passwordServer;
  private static Password passwordPC;
  private static Password passwordAlice;
  private static Database database;
  private static LogFile logFile;

  public static boolean hostFirewallDefault = false;
  public static boolean passwordEncryptionDefault = false;
  public static boolean databaseEncryptionDefault = false;
  //public static boolean legFileLogAccessControlDefault = false;

  @BeforeAll
  public static void setup() {
    // setup architecture
    internet = new Network("internet");
    server = new Host("server", hostFirewallDefault);
    pc = new Host("pc", hostFirewallDefault);
    //alice = new User("alice");
    passwordServer = new Password( "passwordServer", passwordEncryptionDefault);
    passwordPC = new Password("passwordPc", passwordEncryptionDefault);
    //passwordAlice = new Password("passwordAlice", passwordEncryptionDefault);
    database = new Database("database", databaseEncryptionDefault);
    //logFile = new LogFile("logFile", legFileLogAccessControlDefault);

    // setup associations
    internet.addHosts(server);
    internet.addHosts(pc);
    server.addPasswords(passwordServer);
    server.addDatabases(database);
    //server.addLogfiles(logFile);
    //pc.addPasswords(passwordPC);
    //alice.addPasswords(passwordAlice);
  }

  @AfterEach
  public void clear() {
    server.firewall.defaultValue = hostFirewallDefault;
    pc.firewall.defaultValue = hostFirewallDefault;
    passwordServer.encrypted.defaultValue = passwordEncryptionDefault;
    passwordPC.encrypted.defaultValue = passwordEncryptionDefault;
    database.encrypted.defaultValue = databaseEncryptionDefault;
  }

  @Test
  public void testGuessPasswordNotEncrypted() {

    Attacker attacker = new Attacker();

    attacker.addAttackPoint(internet.access);
    attacker.addAttackPoint(server.guessPassword);

    try {
      attacker.attack();
      server.access.assertCompromisedInstantaneously();

    } catch (Exception e) {
      JSONTarget.markCrashed(e);
      ConsoleTarget.logCrash(e);
    }
  }

  @Test
  public void testAllDefences() {

    server.firewall.defaultValue = true;
    pc.firewall.defaultValue = true;
    passwordServer.encrypted.defaultValue = true;
    passwordPC.encrypted.defaultValue = true;
    database.encrypted.defaultValue = true;

    Attacker attacker = new Attacker();

    attacker.addAttackPoint(internet.access);

    try {
      attacker.attack();
      server.access.assertUncompromised();

    } catch (Exception e) {
      JSONTarget.markCrashed(e);
      ConsoleTarget.logCrash(e);
    }
  }

  @Test
  public void testOnlyServerDefence() {

    server.firewall.defaultValue = true;

    Attacker attacker = new Attacker();

    try {
      attacker.attack();
      server.access.assertUncompromised();

    } catch (Exception e) {
      JSONTarget.markCrashed(e);
      ConsoleTarget.logCrash(e);
    }
  }

  @Test
  public void testDatabaseNotEncrypted() {

    server.firewall.defaultValue = false;
    pc.firewall.defaultValue = false;
    passwordServer.encrypted.defaultValue = true;
    passwordPC.encrypted.defaultValue = true;
    database.encrypted.defaultValue = false;

    Attacker attacker = new Attacker();

    attacker.addAttackPoint(internet.access);
    attacker.addAttackPoint(database.query);

    try {
      attacker.attack();
      server.access.assertCompromisedWithEffort();

    } catch (Exception e) {
      JSONTarget.markCrashed(e);
      ConsoleTarget.logCrash(e);
    }
  }

  @Test
  public void testGetPasswordNotEncryptedQuantumCapabilities() {

    server.firewall.defaultValue = false;
    pc.firewall.defaultValue = false;
    passwordServer.encrypted.defaultValue = true;
    passwordPC.encrypted.defaultValue = true;
    database.encrypted.defaultValue = true;

    Attacker attacker = new Attacker();

    attacker.addAttackPoint(internet.access);
    attacker.addAttackPoint(passwordServer.quantum);

    try {
      attacker.attack();
      server.access.assertCompromisedInstantaneously();

    } catch (Exception e) {
      JSONTarget.markCrashed(e);
      ConsoleTarget.logCrash(e);
    }
  }

  @Test
  public void testGuessPcPasswordEncrypted() {

    server.firewall.defaultValue = true;
    pc.firewall.defaultValue = false;
    passwordServer.encrypted.defaultValue = true;
    passwordPC.encrypted.defaultValue = true;
    database.encrypted.defaultValue = true;

    Attacker attacker = new Attacker();

    attacker.addAttackPoint(internet.access);
    attacker.addAttackPoint(pc.guessPassword);

    try {
      attacker.attack();
      pc.access.assertCompromisedWithEffort();

    } catch (Exception e) {
      JSONTarget.markCrashed(e);
      ConsoleTarget.logCrash(e);
    }
  }

  @Test
  public void testGetPcPasswordNotEncryptedQuantum() {

    server.firewall.defaultValue = true;
    pc.firewall.defaultValue = false;
    passwordServer.encrypted.defaultValue = true;
    passwordPC.encrypted.defaultValue = false;
    database.encrypted.defaultValue = true;

    Attacker attacker = new Attacker();

    attacker.addAttackPoint(internet.access);
    attacker.addAttackPoint(pc.guessPassword);

    try {
      attacker.attack();
      pc.access.assertCompromisedWithEffort();

    } catch (Exception e) {
      JSONTarget.markCrashed(e);
      ConsoleTarget.logCrash(e);
    }
  }

  @Test
  public void testNothing() {

    Attacker attacker = new Attacker();

    try {
      attacker.attack();
      server.access.assertCompromisedInstantaneously();

    } catch (Exception e) {
      JSONTarget.markCrashed(e);
      ConsoleTarget.logCrash(e);
    }
  }

  @Test
  public void testFullyCompromiseDatabase() {

    database.encrypted.defaultValue = false;

    Attacker attacker = new Attacker();

    attacker.addAttackPoint(database.query);

    try {
      attacker.attack();
      database.extractPassword.assertCompromisedWithEffort();

    } catch (Exception e) {
      JSONTarget.markCrashed(e);
      ConsoleTarget.logCrash(e);
    }
  }

  @Test
  public void testSimulateCrash() {

    Attacker attacker = new Attacker();

    attacker.addAttackPoint(internet.access);

    try {
      // simulate crash
      throw new RuntimeException("Simulated crash during attack");

    } catch (Exception e) {
      JSONTarget.markCrashed(e);
      ConsoleTarget.logCrash(e);
    }
  }
}
