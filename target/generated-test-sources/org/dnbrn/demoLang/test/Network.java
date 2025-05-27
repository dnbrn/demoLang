package org.dnbrn.demoLang.test;

import core.Asset;
import core.AttackStep;
import core.AttackStepMin;
import java.lang.Override;
import java.lang.String;
import java.util.HashSet;
import java.util.Set;

public class Network extends Asset {
  public Access access;

  public Set<Host> hosts = new HashSet<>();

  public Network(String name) {
    super(name);
    assetClassName = "Network";
    AttackStep.allAttackSteps.remove(access);
    access = new Access(name);
  }

  public Network() {
    this("Anonymous");
  }

  public void addHosts(Host hosts) {
    this.hosts.add(hosts);
    hosts.networks.add(this);
  }

  @Override
  public String getAssociatedAssetClassName(String field) {
    if (field.equals("hosts")) {
      return Host.class.getName();
    }
    return "";
  }

  @Override
  public Set<Asset> getAssociatedAssets(String field) {
    Set<Asset> assets = new HashSet<>();
    if (field.equals("hosts")) {
      assets.addAll(hosts);
    }
    return assets;
  }

  @Override
  public Set<Asset> getAllAssociatedAssets() {
    Set<Asset> assets = new HashSet<>();
    assets.addAll(hosts);
    return assets;
  }

  public class Access extends AttackStepMin {
    private Set<AttackStep> _cacheChildrenAccess;

    public Access(String name) {
      super(name);
    }

    @Override
    public void updateChildren(Set<AttackStep> attackSteps) {
      if (_cacheChildrenAccess == null) {
        _cacheChildrenAccess = new HashSet<>();
        for (Host _0 : hosts) {
          _cacheChildrenAccess.add(_0.connect);
        }
      }
      for (AttackStep attackStep : _cacheChildrenAccess) {
        attackStep.updateTtc(this, ttc, attackSteps);
      }
    }

    @Override
    public double localTtc() {
      return ttcHashMap.get("Network.access");
    }
  }
}
