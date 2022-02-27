plugins {
  groovy
  `java-library`
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("de.gesellix:docker-remote-api-client:2022-02-23T13-45-00")
  implementation("de.gesellix:docker-remote-api-model-1-41:2022-02-23T11-47-00")
  implementation("de.gesellix:docker-engine:2022-02-22T23-12-00")

  implementation("org.slf4j:slf4j-api:[1.7,)")
  testImplementation("ch.qos.logback:logback-classic:[1.2,2)!!1.2.10")

  testImplementation("org.codehaus.groovy:groovy:3.0.9")
  testImplementation("org.spockframework:spock-core:2.0-groovy-3.0")
}

tasks.withType<Test> {
  useJUnitPlatform()
}
