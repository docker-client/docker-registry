ARG GO_VERSION=1.26.4
ARG WINDOWS_VERSION=ltsc2022
ARG DISTRIBUTION_VERSION=v3.1.1

FROM golang:${GO_VERSION}-windowsservercore-${WINDOWS_VERSION} AS build

ENV GO111MODULE=auto

SHELL ["powershell", "-Command", "$ErrorActionPreference = 'Stop'; $ProgressPreference = 'SilentlyContinue';"]

ENV DOCKER_BUILDTAGS="include_oss include_gcs"
ARG DISTRIBUTION_VERSION

RUN mkdir src\github.com\distribution ; \
    cd src\github.com\distribution ; \
    git clone -q https://github.com/distribution/distribution ; \
    cd distribution ; \
    git checkout -q $env:DISTRIBUTION_VERSION ; \
    cd cmd\registry ; \
    go build -o registry.exe ; \
    dir

FROM mcr.microsoft.com/windows/nanoserver:${WINDOWS_VERSION}
EXPOSE 5000

ENTRYPOINT ["\\registry.exe"]
CMD ["serve", "/config/config.yml"]

COPY --from=build /go/src/github.com/distribution/distribution/cmd/registry/registry.exe /registry.exe
COPY config.yml /config/config.yml
