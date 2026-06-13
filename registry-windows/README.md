# registry-windows

Dockerfiles that build the [Docker distribution/registry](https://github.com/distribution/distribution)
from source with the `include_oss include_gcs` build tags. The shared `config.yml` is used
by both variants.

- `Dockerfile`: Windows nanoserver image. Built natively on a Windows runner; the build
  stage uses `golang:<ver>-windowsservercore-<WINDOWS_VERSION>` (servercore ships git and
  PowerShell), the runtime stage `mcr.microsoft.com/windows/nanoserver:<WINDOWS_VERSION>`.
  The Windows base is selected with the `WINDOWS_VERSION` build arg (`ltsc2022`, `ltsc2025`).
- `Dockerfile.linux`: Linux image (alpine runtime), cross-compiled per target arch via
  buildx (`TARGETOS` / `TARGETARCH`).

Both are wired into the `Build and Push multi-arch image` workflow
(`../.github/workflows/image.yml`), which publishes the combined `gesellix/registry` manifest.

## Inspiration / references

- https://github.com/sixeyed/dockerfiles-windows/blob/master/registry/nanoserver/1809/Dockerfile
- https://github.com/StefanScherer/dockerfiles-windows/blob/main/registry/Dockerfile
- https://github.com/docker/distribution-library-image/pull/42
- https://github.com/docker/distribution/pull/2281
