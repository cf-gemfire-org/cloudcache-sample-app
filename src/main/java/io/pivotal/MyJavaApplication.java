package io.pivotal;

import org.apache.geode.LogWriter;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.client.ClientCache;
import org.apache.geode.cache.client.ClientCacheFactory;
import org.apache.geode.cache.client.ClientRegionShortcut;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyJavaApplication {

  public static void main(String[] args) {
    Logger.getGlobal().setLevel(Level.OFF);

    Properties props = new Properties();
    props.setProperty("security-client-auth-init", "io.pivotal.ClientAuthInitialize.create");
    ClientCacheFactory ccf = new ClientCacheFactory(props);

    try {
      List<URI> locatorList = EnvParser.getInstance().getLocators();
      for (URI locator : locatorList) {
        ccf.addPoolLocator(locator.getHost(), locator.getPort());
      }

      ClientCache client = ccf.create();
      Region r = client.createClientRegionFactory(ClientRegionShortcut.PROXY).create("test");
      r.put("1", "one");
      if (!r.get("1").equals("one")) {
        throw new RuntimeException("Expected value to be \"one\", but was:"+r.get("1"));
      }
    } catch (IOException | URISyntaxException e) {
      throw new RuntimeException("Could not deploy Application", e);
    }
    try {
      Thread.sleep(Long.MAX_VALUE);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}