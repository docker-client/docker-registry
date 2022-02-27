package de.gesellix.docker.registry

import spock.lang.Requires
import spock.lang.Specification

@Requires({ LocalDocker.available() })
class DockerRegistrySpec extends Specification {

  def "can determine registry url"() {
    given:
    DockerRegistry registry = new DockerRegistry()
    registry.run()

    when:
    String registryUrl = registry.url()

    then:
    registryUrl =~ /^(\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})|localhost:\d{1,5}\u0024/

    cleanup:
    registry.rm()
  }
}
