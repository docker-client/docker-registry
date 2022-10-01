package de.gesellix.docker.registry;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.gesellix.docker.engine.DockerClientConfig;
import de.gesellix.docker.remote.api.ContainerCreateRequest;
import de.gesellix.docker.remote.api.ContainerCreateResponse;
import de.gesellix.docker.remote.api.ContainerInspectResponse;
import de.gesellix.docker.remote.api.HostConfig;
import de.gesellix.docker.remote.api.PortBinding;
import de.gesellix.docker.remote.api.client.ContainerApi;
import de.gesellix.docker.remote.api.client.ImageApi;
import de.gesellix.docker.remote.api.core.ClientException;

public class DockerRegistry {

  private static final Logger log = LoggerFactory.getLogger(DockerRegistry.class);

  private final ContainerApi containerApi;
  private final ImageApi imageApi;

  private String imageNameWithTag;

  private String registryId;

  public DockerRegistry() {
    DockerClientConfig dockerClientConfig = new DockerClientConfig();
    this.containerApi = new ContainerApi(dockerClientConfig);
    this.imageApi = new ImageApi(dockerClientConfig);

    if (LocalDocker.isNativeWindows()) {
      imageNameWithTag = "gesellix/registry:2.8.1-windows-ltsc2022";
    } else {
      imageNameWithTag = "registry:2.8.1";
    }
  }

  public void setImageNameWithTag(String imageNameWithTag) {
    this.imageNameWithTag = imageNameWithTag;
  }

  public String getImageNameWithTag() {
    return imageNameWithTag;
  }

  public void run() {
    ContainerCreateRequest containerConfig = new ContainerCreateRequest();
    containerConfig.setImage(getImageNameWithTag());
    containerConfig.setEnv(singletonList("REGISTRY_VALIDATION_DISABLED=true"));
    Map<String, Object> exposedPorts = new HashMap<>();
    exposedPorts.put("5000/tcp", emptyMap());
    containerConfig.setExposedPorts(exposedPorts);
    HostConfig hostConfig = new HostConfig();
    hostConfig.setPublishAllPorts(true);
    containerConfig.setHostConfig(hostConfig);

    ContainerCreateResponse registryStatus = run(containerConfig);
    registryId = registryStatus.getId();
  }

  ContainerCreateResponse run(ContainerCreateRequest containerCreateRequest) {
    log.info("docker run {}", containerCreateRequest.getImage());
    ContainerCreateResponse createContainerResponse = createContainer(containerCreateRequest);
    log.debug("create container result: {}", createContainerResponse);
    String containerId = createContainerResponse.getId();
    containerApi.containerStart(containerId, null);
    return createContainerResponse;
  }

  ContainerCreateResponse createContainer(ContainerCreateRequest containerCreateRequest) {
    log.debug("docker create");
    try {
      return containerApi.containerCreate(containerCreateRequest, null);
    } catch (ClientException exception) {
      if (exception.getStatusCode() == 404) {
        log.debug("Image '{}' not found locally.", containerCreateRequest.getImage());
        imageApi.imageCreate(containerCreateRequest.getImage(),
            null, null, null, null,
            null, null, null, null);
        return containerApi.containerCreate(containerCreateRequest, null);
      }
      throw exception;
    }
  }

  public String address() {
//        String dockerHost = dockerClient.config.dockerHost
//        return dockerHost.replaceAll("^(tcp|http|https)://", "").replaceAll(":\\d+\$", "")

//        def registryContainer = dockerClient.inspectContainer(registryId).content
//        def portBinding = registryContainer.NetworkSettings.Ports["5000/tcp"]
//        return portBinding[0].HostIp as String

    // 'localhost' allows to use the registry without TLS
    return "localhost";
  }

  public int port() {
    ContainerInspectResponse registryContainer = containerApi.containerInspect(registryId, null);
    List<PortBinding> portBinding = registryContainer.getNetworkSettings().getPorts().get("5000/tcp");
    PortBinding firstPortBinding = portBinding.stream()
        .findFirst()
        .orElseThrow(() -> new RuntimeException("No PortBinding for port 5000/tcp"));
    if (firstPortBinding.getHostPort() == null) {
      throw new RuntimeException("Null PortBinding for port 5000/tcp");
    }
    return Integer.parseInt(firstPortBinding.getHostPort());
  }

  public String url() {
    return address() + ":" + port();
  }

  public void rm() {
    containerApi.containerStop(registryId, null);
    containerApi.containerWait(registryId, (ContainerApi.ConditionContainerWait) null);
    containerApi.containerDelete(registryId, null, null, null);
  }
}
