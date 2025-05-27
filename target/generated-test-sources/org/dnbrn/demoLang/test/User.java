package org.dnbrn.demoLang.test;

import core.Asset;
import core.AttackStep;
import core.AttackStepMin;
import java.lang.Override;
import java.lang.String;
import java.util.HashSet;
import java.util.Set;

public class User extends Asset {
  public AttemptPhishing attemptPhishing;

  public Phish phish;

  public Set<Password> passwords = new HashSet<>();

  public User(String name) {
    super(name);
    assetClassName = "User";
    AttackStep.allAttackSteps.remove(attemptPhishing);
    attemptPhishing = new AttemptPhishing(name);
    AttackStep.allAttackSteps.remove(phish);
    phish = new Phish(name);
  }

  public User() {
    this("Anonymous");
  }

  public void addPasswords(Password passwords) {
    this.passwords.add(passwords);
    passwords.user = this;
  }

  @Override
  public String getAssociatedAssetClassName(String field) {
    if (field.equals("passwords")) {
      return Password.class.getName();
    }
    return "";
  }

  @Override
  public Set<Asset> getAssociatedAssets(String field) {
    Set<Asset> assets = new HashSet<>();
    if (field.equals("passwords")) {
      assets.addAll(passwords);
    }
    return assets;
  }

  @Override
  public Set<Asset> getAllAssociatedAssets() {
    Set<Asset> assets = new HashSet<>();
    assets.addAll(passwords);
    return assets;
  }

  public class AttemptPhishing extends AttackStepMin {
    private Set<AttackStep> _cacheChildrenAttemptPhishing;

    public AttemptPhishing(String name) {
      super(name);
    }

    @Override
    public void updateChildren(Set<AttackStep> attackSteps) {
      if (_cacheChildrenAttemptPhishing == null) {
        _cacheChildrenAttemptPhishing = new HashSet<>();
        _cacheChildrenAttemptPhishing.add(phish);
      }
      for (AttackStep attackStep : _cacheChildrenAttemptPhishing) {
        attackStep.updateTtc(this, ttc, attackSteps);
      }
    }

    @Override
    public double localTtc() {
      return ttcHashMap.get("User.attemptPhishing");
    }
  }

  public class Phish extends AttackStepMin {
    private Set<AttackStep> _cacheChildrenPhish;

    private Set<AttackStep> _cacheParentPhish;

    public Phish(String name) {
      super(name);
    }

    @Override
    public void updateChildren(Set<AttackStep> attackSteps) {
      if (_cacheChildrenPhish == null) {
        _cacheChildrenPhish = new HashSet<>();
        for (Password _0 : passwords) {
          _cacheChildrenPhish.add(_0.obtain);
        }
      }
      for (AttackStep attackStep : _cacheChildrenPhish) {
        attackStep.updateTtc(this, ttc, attackSteps);
      }
    }

    @Override
    public void setExpectedParents() {
      super.setExpectedParents();
      if (_cacheParentPhish == null) {
        _cacheParentPhish = new HashSet<>();
        _cacheParentPhish.add(attemptPhishing);
      }
      for (AttackStep attackStep : _cacheParentPhish) {
        addExpectedParent(attackStep);
      }
    }

    @Override
    public double localTtc() {
      return ttcHashMap.get("User.phish");
    }
  }
}
