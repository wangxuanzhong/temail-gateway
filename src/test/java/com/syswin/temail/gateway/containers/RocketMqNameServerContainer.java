package com.syswin.temail.gateway.containers;

import org.testcontainers.containers.FixedHostPortGenericContainer;

public class RocketMqNameServerContainer extends FixedHostPortGenericContainer<RocketMqNameServerContainer> {
  public RocketMqNameServerContainer() {
    super("seanyinx/rocketmq-namesrv:4.3.0");
  }
}
