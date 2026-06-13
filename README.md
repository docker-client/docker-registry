[![Build Status](https://img.shields.io/github/actions/workflow/status/docker-client/docker-registry/cd.yml?branch=main&style=for-the-badge)](https://github.com/docker-client/docker-registry/actions)
[![Maven Central](https://img.shields.io/maven-central/v/de.gesellix/docker-registry.svg?style=for-the-badge&maxAge=86400)](https://search.maven.org/search?q=g:de.gesellix%20AND%20a:docker-registry)

# docker-registry

## Container images

Multi-arch [Docker distribution/registry](https://github.com/distribution/distribution)
images are published to Docker Hub as `gesellix/registry`. Each tag is a single
manifest spanning:

- `linux/amd64`, `linux/arm64`
- Windows nanoserver `ltsc2022` and `ltsc2025` (amd64)

They are built by the `Build and Push multi-arch image` workflow
(`.github/workflows/image.yml`), triggered manually via `workflow_dispatch` with an
`image_tag` input. The Linux images are built from `registry-windows/Dockerfile.linux`
(cross-compiled via buildx), the Windows images from `registry-windows/Dockerfile`
(built natively on the matching Windows runner). Both compile the registry from source
with the `include_oss include_gcs` build tags.

When running a Windows image, the container's base image OS must match the host's OS, see
[version compatibility](https://docs.microsoft.com/en-us/virtualization/windowscontainers/deploy-containers/version-compatibility?tabs=windows-server-1909%2Cwindows-10-1909).
Otherwise you get errors like this when trying to run an image with the wrong base image OS:

> The container operating system does not match the host operating system

The multi-arch manifest handles this automatically: a host pulls the variant matching its
own OS and architecture (the Windows entries carry distinct `os.version` values for
`ltsc2022` vs `ltsc2025`).

### References

- [olljanat/multi-win-version-aspnet](https://github.com/olljanat/multi-win-version-aspnet): example of creating Windows multiarch images (source: [moby/moby#35247](https://github.com/moby/moby/issues/35247#issuecomment-436634221)).

## Publishing/Release Workflow

See RELEASE.md
