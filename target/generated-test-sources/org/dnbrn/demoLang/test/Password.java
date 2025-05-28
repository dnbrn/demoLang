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

public class Password extends Asset {
  public Read read;

  public Obtain obtain;

  public Encrypted encrypted;

  public Quantum quantum;

  public Host host = null;

  public User user = null;

  public Password(String name, boolean isEncryptedEnabled) {
    super(name);
    assetClassName = "Password";
    AttackStep.allAttackSteps.remove(read);
    read = new Read(name);
    AttackStep.allAttackSteps.remove(obtain);
    obtain = new Obtain(name);
    if (encrypted != null) {
      AttackStep.allAttackSteps.remove(encrypted.disable);
    }
    Defense.allDefenses.remove(encrypted);
    encrypted = new Encrypted(name, isEncryptedEnabled);
    AttackStep.allAttackSteps.remove(quantum);
    quantum = new Quantum(name);
  }

  public Password(String name) {
    super(name);
    assetClassName = "Password";
    AttackStep.allAttackSteps.remove(read);
    read = new Read(name);
    AttackStep.allAttackSteps.remove(obtain);
    obtain = new Obtain(name);
    if (encrypted != null) {
      AttackStep.allAttackSteps.remove(encrypted.disable);
    }
    Defense.allDefenses.remove(encrypted);
    encrypted = new Encrypted(name, false);
    AttackStep.allAttackSteps.remove(quantum);
    quantum = new Quantum(name);
  }

  public Password(boolean isEncryptedEnabled) {
    this("Anonymous", isEncryptedEnabled);
  }

  public Password() {
    this("Anonymous");
  }

  public void addHost(Host host) {
    this.host = host;
    host.passwords.add(this);
  }

  public void addUser(User user) {
    this.user = user;
    user.passwords.add(this);
  }

  @Override
  public String getAssociatedAssetClassName(String field) {
    if (field.equals("host")) {
      return Host.class.getName();
    } else if (field.equals("user")) {
      return User.class.getName();
    }
    return "";
  }

  @Override
  public Set<Asset> getAssociatedAssets(String field) {
    Set<Asset> assets = new HashSet<>();
    if (field.equals("host")) {
      if (host != null) {
        assets.add(host);
      }
    } else if (field.equals("user")) {
      if (user != null) {
        assets.add(user);
      }
    }
    return assets;
  }

  @Override
  public Set<Asset> getAllAssociatedAssets() {
    Set<Asset> assets = new HashSet<>();
    if (host != null) {
      assets.add(host);
    }
    if (user != null) {
      assets.add(user);
    }
    return assets;
  }

  public class Read extends AttackStepMax {
    private Set<AttackStep> _cacheChildrenRead;

    private Set<AttackStep> _cacheParentRead;

    public Read(String name) {
      super(name);
    }

    @Override
    public void updateChildren(Set<AttackStep> attackSteps) {
      if (_cacheChildrenRead == null) {
        _cacheChildrenRead = new HashSet<>();
        _cacheChildrenRead.add(obtain);
      }
      for (AttackStep attackStep : _cacheChildrenRead) {
        attackStep.updateTtc(this, ttc, attackSteps);
      }
    }

    @Override
    public void setExpectedParents() {
      super.setExpectedParents();
      if (_cacheParentRead == null) {
        _cacheParentRead = new HashSet<>();
        if (host != null) {
          _cacheParentRead.add(host.connect);
        }
        _cacheParentRead.add(encrypted.disable);
      }
      for (AttackStep attackStep : _cacheParentRead) {
        addExpectedParent(attackStep);
      }
    }

    @Override
    public double localTtc() {
      return ttcHashMap.get("Password.read");
    }
  }

  public class Obtain extends AttackStepMin {
    private Set<AttackStep> _cacheChildrenObtain;

    private Set<AttackStep> _cacheParentObtain;

    public Obtain(String name) {
      super(name);
    }

    @Override
    public void updateChildren(Set<AttackStep> attackSteps) {
      if (_cacheChildrenObtain == null) {
        _cacheChildrenObtain = new HashSet<>();
        if (host != null) {
          _cacheChildrenObtain.add(host.authenticate);
        }
      }
      for (AttackStep attackStep : _cacheChildrenObtain) {
        attackStep.updateTtc(this, ttc, attackSteps);
      }
    }

    @Override
    public void setExpectedParents() {
      super.setExpectedParents();
      if (_cacheParentObtain == null) {
        _cacheParentObtain = new HashSet<>();
        if (user != null) {
          _cacheParentObtain.add(user.phish);
        }
        _cacheParentObtain.add(read);
        if (host != null) {
          for (Database _0 : host.databases) {
            _cacheParentObtain.add(_0.extractPassword);
          }
        }
        if (host != null) {
          for (LogFile _1 : host.logfiles) {
            _cacheParentObtain.add(_1.discoverCredentials);
          }
        }
      }
      for (AttackStep attackStep : _cacheParentObtain) {
        addExpectedParent(attackStep);
      }
    }

    @Override
    public double localTtc() {
      return ttcHashMap.get("Password.obtain");
    }
  }

  public class Encrypted extends Defense {
    public Encrypted(String name) {
      this(name, false);
    }

    public Encrypted(String name, Boolean isEnabled) {
      super(name);
      defaultValue = isEnabled;
      disable = new Disable(name);
    }

    public class Disable extends AttackStepMin {
      private Set<AttackStep> _cacheChildrenEncrypted;

      private Set<AttackStep> _cacheParentEncrypted;

      public Disable(String name) {
        super(name);
      }

      @Override
      public void updateChildren(Set<AttackStep> attackSteps) {
        if (_cacheChildrenEncrypted == null) {
          _cacheChildrenEncrypted = new HashSet<>();
          _cacheChildrenEncrypted.add(read);
        }
        for (AttackStep attackStep : _cacheChildrenEncrypted) {
          attackStep.updateTtc(this, ttc, attackSteps);
        }
      }

      @Override
      public void setExpectedParents() {
        super.setExpectedParents();
        if (_cacheParentEncrypted == null) {
          _cacheParentEncrypted = new HashSet<>();
          _cacheParentEncrypted.add(quantum);
        }
        for (AttackStep attackStep : _cacheParentEncrypted) {
          addExpectedParent(attackStep);
        }
      }

      @Override
      public String fullName() {
        return "Password.encrypted";
      }
    }
  }

  public class Quantum extends AttackStepMin {
    private Set<AttackStep> _cacheChildrenQuantum;

    public Quantum(String name) {
      super(name);
    }

    @Override
    public void updateChildren(Set<AttackStep> attackSteps) {
      if (_cacheChildrenQuantum == null) {
        _cacheChildrenQuantum = new HashSet<>();
        _cacheChildrenQuantum.add(encrypted.disable);
      }
      for (AttackStep attackStep : _cacheChildrenQuantum) {
        attackStep.updateTtc(this, ttc, attackSteps);
      }
    }

    @Override
    public double localTtc() {
      return ttcHashMap.get("Password.quantum");
    }
  }
}
