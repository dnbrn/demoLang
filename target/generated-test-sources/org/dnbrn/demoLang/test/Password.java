package org.dnbrn.demoLang.test;

import core.Asset;
import core.AttackStep;
import core.AttackStepMin;
import java.lang.Override;
import java.lang.String;
import java.util.HashSet;
import java.util.Set;

public class Password extends Asset {
  public Obtain obtain;

  public Host host = null;

  public User user = null;

  public Password(String name) {
    super(name);
    assetClassName = "Password";
    AttackStep.allAttackSteps.remove(obtain);
    obtain = new Obtain(name);
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
}
