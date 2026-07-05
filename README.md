[![Build Status](https://img.shields.io/github/actions/workflow/status/docker-client/docker-registry/cd.yml?branch=main&style=for-the-badge)](https://github.com/docker-client/docker-registry/actions)
[![Maven Central](https://img.shields.io/maven-central/v/de.gesellix/docker-registry.svg?style=for-the-badge&maxAge=86400)](https://search.maven.org/search?q=g:de.gesellix%20AND%20a:docker-registry)

# docker-registry

## Container image

`DockerRegistry` runs a throwaway registry for tests using
[`ghcr.io/gesellix/distribution`](https://github.com/gesellix/distribution), a fork of
[distribution/distribution](https://github.com/distribution/distribution). The image
ships a working config (`/etc/distribution/config.yml`) and serves it by default, so no
extra configuration is mounted.

The default image tag is set in
[`DockerRegistry`](lib/src/main/java/de/gesellix/docker/registry/DockerRegistry.java) and
can be overridden via the `DOCKER_REGISTRY_IMAGE_OVERRIDE` system property.

## Publishing/Release Workflow

See RELEASE.md
