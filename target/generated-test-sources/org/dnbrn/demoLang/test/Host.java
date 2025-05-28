package org.dnbrn.demoLang.test;

import core.Asset;
import core.AttackStep;
import core.AttackStepMax;
import core.AttackStepMin;
import core.Defense;
import java.lang.Boolean;
import java.lang.Override;
import java.lang.String;
import java.util.HashSet;
import java.util.Set;

public class Host extends Asset {
  public Connect connect;

  public Authenticate authenticate;

  public GuessPassword guessPassword;

  public GuessedPassword guessedPassword;

  public Access access;

  public Firewall firewall;

  public Set<Network> networks = new HashSet<>();

  public Set<Password> passwords = new HashSet<>();

  public Set<Database> databases = new HashSet<>();

  public Set<LogFile> logfiles = new HashSet<>();

  public Host(String name, boolean isFirewallEnabled) {
    super(name);
    assetClassName = "Host";
    AttackStep.allAttackSteps.remove(connect);
    connect = new Connect(name);
    AttackStep.allAttackSteps.remove(authenticate);
    authenticate = new Authenticate(name);
    AttackStep.allAttackSteps.remove(guessPassword);
    guessPassword = new GuessPassword(name);
    AttackStep.allAttackSteps.remove(guessedPassword);
    guessedPassword = new GuessedPassword(name);
    AttackStep.allAttackSteps.remove(access);
    access = new Access(name);
    if (firewall != null) {
      AttackStep.allAttackSteps.remove(firewall.disable);
    }
    Defense.allDefenses.remove(firewall);
    firewall = new Firewall(name, isFirewallEnabled);
  }

  public Host(String name) {
    super(name);
    assetClassName = "Host";
    AttackStep.allAttackSteps.remove(connect);
    connect = new Connect(name);
    AttackStep.allAttackSteps.remove(authenticate);
    authenticate = new Authenticate(name);
    AttackStep.allAttackSteps.remove(guessPassword);
    guessPassword = new GuessPassword(name);
    AttackStep.allAttackSteps.remove(guessedPassword);
    guessedPassword = new GuessedPassword(name);
    AttackStep.allAttackSteps.remove(access);
    access = new Access(name);
    if (firewall != null) {
      AttackStep.allAttackSteps.remove(firewall.disable);
    }
    Defense.allDefenses.remove(firewall);
    firewall = new Firewall(name, false);
  }

  public Host(boolean isFirewallEnabled) {
    this("Anonymous", isFirewallEnabled);
  }

  public Host() {
    this("Anonymous");
  }

  public void addNetworks(Network networks) {
    this.networks.add(networks);
    networks.hosts.add(this);
  }

  public void addPasswords(Password passwords) {
    this.passwords.add(passwords);
    passwords.host = this;
  }

  public void addDatabases(Database databases) {
    this.databases.add(databases);
    databases.host.add(this);
  }

  public void addLogfiles(LogFile logfiles) {
    this.logfiles.add(logfiles);
    logfiles.host.add(this);
  }

  @Override
  public String getAssociatedAssetClassName(String field) {
    if (field.equals("networks")) {
      return Network.class.getName();
    } else if (field.equals("passwords")) {
      return Password.class.getName();
    } else if (field.equals("databases")) {
      return Database.class.getName();
    } else if (field.equals("logfiles")) {
      return LogFile.class.getName();
    }
    return "";
  }

  @Override
  public Set<Asset> getAssociatedAssets(String field) {
    Set<Asset> assets = new HashSet<>();
    if (field.equals("networks")) {
      assets.addAll(networks);
    } else if (field.equals("passwords")) {
      assets.addAll(passwords);
    } else if (field.equals("databases")) {
      assets.addAll(databases);
    } else if (field.equals("logfiles")) {
      assets.addAll(logfiles);
    }
    return assets;
  }

  @Override
  public Set<Asset> getAllAssociatedAssets() {
    Set<Asset> assets = new HashSet<>();
    assets.addAll(networks);
    assets.addAll(passwords);
    assets.addAll(databases);
    assets.addAll(logfiles);
    return assets;
  }

  public class Connect extends AttackStepMin {
    private Set<AttackStep> _cacheChildrenConnect;

    private Set<AttackStep> _cacheParentConnect;

    public Connect(String name) {
      super(name);
    }

    @Override
    public void updateChildren(Set<AttackStep> attackSteps) {
      if (_cacheChildrenConnect == null) {
        _cacheChildrenConnect = new HashSet<>();
        _cacheChildrenConnect.add(access);
        for (Password _0 : passwords) {
          _cacheChildrenConnect.add(_0.read);
        }
      }
      for (AttackStep attackStep : _cacheChildrenConnect) {
        attackStep.updateTtc(this, ttc, attackSteps);
      }
    }

    @Override
    public void setExpectedParents() {
      super.setExpectedParents();
      if (_cacheParentConnect == null) {
        _cacheParentConnect = new HashSet<>();
        for (Network _1 : networks) {
          _cacheParentConnect.add(_1.access);
        }
        _cacheParentConnect.add(firewall.disable);
      }
      for (AttackStep attackStep : _cacheParentConnect) {
        addExpectedParent(attackStep);
      }
    }

    @Override
    public double localTtc() {
      return ttcHashMap.get("Host.connect");
    }
  }

  public class Authenticate extends AttackStepMin {
    private Set<AttackStep> _cacheChildrenAuthenticate;

    private Set<AttackStep> _cacheParentAuthenticate;

    public Authenticate(String name) {
      super(name);
    }

    @Override
    public void updateChildren(Set<AttackStep> attackSteps) {
      if (_cacheChildrenAuthenticate == null) {
        _cacheChildrenAuthenticate = new HashSet<>();
        _cacheChildrenAuthenticate.add(access);
      }
      for (AttackStep attackStep : _cacheChildrenAuthenticate) {
        attackStep.updateTtc(this, ttc, attackSteps);
      }
    }

    @Override
    public void setExpectedParents() {
      super.setExpectedParents();
      if (_cacheParentAuthenticate == null) {
        _cacheParentAuthenticate = new HashSet<>();
        _cacheParentAuthenticate.add(guessedPassword);
        for (Password _0 : passwords) {
          _cacheParentAuthenticate.add(_0.obtain);
        }
      }
      for (AttackStep attackStep : _cacheParentAuthenticate) {
        addExpectedParent(attackStep);
      }
    }

    @Override
    public double localTtc() {
      return ttcHashMap.get("Host.authenticate");
    }
  }

  public class GuessPassword extends AttackStepMin {
    private Set<AttackStep> _cacheChildrenGuessPassword;

    public GuessPassword(String name) {
      super(name);
    }

    @Override
    public void updateChildren(Set<AttackStep> attackSteps) {
      if (_cacheChildrenGuessPassword == null) {
        _cacheChildrenGuessPassword = new HashSet<>();
        _cacheChildrenGuessPassword.add(guessedPassword);
      }
      for (AttackStep attackStep : _cacheChildrenGuessPassword) {
        attackStep.updateTtc(this, ttc, attackSteps);
      }
    }

    @Override
    public double localTtc() {
      return ttcHashMap.get("Host.guessPassword");
    }
  }

  public class GuessedPassword extends AttackStepMin {
    private Set<AttackStep> _cacheChildrenGuessedPassword;

    private Set<AttackStep> _cacheParentGuessedPassword;

    public GuessedPassword(String name) {
      super(name);
    }

    @Override
    public void updateChildren(Set<AttackStep> attackSteps) {
      if (_cacheChildrenGuessedPassword == null) {
        _cacheChildrenGuessedPassword = new HashSet<>();
        _cacheChildrenGuessedPassword.add(authenticate);
      }
      for (AttackStep attackStep : _cacheChildrenGuessedPassword) {
        attackStep.updateTtc(this, ttc, attackSteps);
      }
    }

    @Override
    public void setExpectedParents() {
      super.setExpectedParents();
      if (_cacheParentGuessedPassword == null) {
        _cacheParentGuessedPassword = new HashSet<>();
        _cacheParentGuessedPassword.add(guessPassword);
      }
      for (AttackStep attackStep : _cacheParentGuessedPassword) {
        addExpectedParent(attackStep);
      }
    }

    @Override
    public double localTtc() {
      return ttcHashMap.get("Host.guessedPassword");
    }
  }

  public class Access extends AttackStepMax {
    private Set<AttackStep> _cacheParentAccess;

    public Access(String name) {
      super(name);
    }

    @Override
    public void setExpectedParents() {
      super.setExpectedParents();
      if (_cacheParentAccess == null) {
        _cacheParentAccess = new HashSet<>();
        _cacheParentAccess.add(connect);
        _cacheParentAccess.add(authenticate);
      }
      for (AttackStep attackStep : _cacheParentAccess) {
        addExpectedParent(attackStep);
      }
    }

    @Override
    public double localTtc() {
      return ttcHashMap.get("Host.access");
    }
  }

  public class Firewall extends Defense {
    public Firewall(String name) {
      this(name, false);
    }

    public Firewall(String name, Boolean isEnabled) {
      super(name);
      defaultValue = isEnabled;
      disable = new Disable(name);
    }

    public class Disable extends AttackStepMin {
      private Set<AttackStep> _cacheChildrenFirewall;

      public Disable(String name) {
        super(name);
      }

      @Override
      public void updateChildren(Set<AttackStep> attackSteps) {
        if (_cacheChildrenFirewall == null) {
          _cacheChildrenFirewall = new HashSet<>();
          _cacheChildrenFirewall.add(connect);
        }
        for (AttackStep attackStep : _cacheChildrenFirewall) {
          attackStep.updateTtc(this, ttc, attackSteps);
        }
      }

      @Override
      public String fullName() {
        return "Host.firewall";
      }
    }
  }
}
