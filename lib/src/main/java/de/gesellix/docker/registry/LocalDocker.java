package de.gesellix.docker.registry;

import de.gesellix.docker.engine.DockerClientConfig;
import de.gesellix.docker.remote.api.SystemVersion;
import de.gesellix.docker.remote.api.client.SystemApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalDocker {

  private static final Logger log = LoggerFactory.getLogger(LocalDocker.class);

  public static boolean available() {
    try {
      DockerClientConfig dockerClientConfig = new DockerClientConfig();
      SystemApi systemApi = new SystemApi(dockerClientConfig);
      return "OK".equals(systemApi.systemPing());
    } catch (Exception e) {
      log.info("Docker not available", e);
      return false;
    }
  }

  public static boolean isNativeWindows() {
    DockerClientConfig dockerClientConfig = new DockerClientConfig();
    SystemApi systemApi = new SystemApi(dockerClientConfig);
    try {
      SystemVersion systemVersion = systemApi.systemVersion();
      String arch = systemVersion.getArch();
      String os = systemVersion.getOs();
      return "windows/amd64".equals(os + "/" + arch);
    } catch (Exception e) {
      log.info("Docker not available", e);
      throw new RuntimeException(e);
    }
  }
}
