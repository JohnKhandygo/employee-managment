package com.kspt.khandygo;

import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.kspt.khandygo.bl.AuthService;
import com.kspt.khandygo.persistence.SQLServer;
import com.kspt.khandygo.persistence.dao.AuthDAO;
import com.typesafe.config.Config;
import static com.typesafe.config.ConfigFactory.parseResources;
import com.typesafe.config.ConfigParseOptions;
import static java.lang.Thread.currentThread;
import lombok.extern.slf4j.Slf4j;
import java.io.File;

@Slf4j
public class GuiceModule extends AbstractModule {

  @Override
  protected void configure() {

  }

  @Provides
  @Singleton
  private Config provideConfiguration() {
    final ConfigParseOptions options = ConfigParseOptions.defaults().setAllowMissing(false);
    final ClassLoader classLoader = currentThread().getContextClassLoader();
    final Config c = parseResources(classLoader, "app.conf", options);
    final File folder = new File(c.origin().filename()).getParentFile();
    log.warn("Loading configuration file from {}.", folder.getAbsolutePath());
    return c.resolve();
  }

  @Provides
  @Singleton
  private SQLServer provideSQLServer(final Config config) {
    final Config databaseConfig = config.getConfig("database");
    return SQLServer.newMySQLServer(
        databaseConfig.getString("host"),
        databaseConfig.getString("port"),
        databaseConfig.getString("scheme"),
        databaseConfig.getString("user"),
        databaseConfig.getString("password"));
  }

  @Provides
  @Singleton
  private AuthService provideAuthService(final AuthDAO authDAO) {
    return new AuthService(Maps.newConcurrentMap(), authDAO);
  }
}
