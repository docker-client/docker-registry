FROM golang:1.21.1 AS build
#FROM golang:1.19.0-nanoserver AS build

ENV GO111MODULE=auto

SHELL ["powershell", "-Command", "$ErrorActionPreference = 'Stop'; $ProgressPreference = 'SilentlyContinue';"]

ENV DOCKER_BUILDTAGS include_oss include_gcs
ENV DISTRIBUTION_VERSION v2.8.1

RUN mkdir src\github.com\docker ; \
    cd src\github.com\docker ; \
    git clone -q https://github.com/docker/distribution ; \
    cd distribution ; \
    git checkout -q $env:DISTRIBUTION_VERSION ; \
    cd cmd\registry ; \
    go build -o registry.exe

FROM mcr.microsoft.com/windows/nanoserver:ltsc2022
EXPOSE 5000

ENTRYPOINT ["\\registry.exe"]
CMD ["serve", "/config/config.yml"]

COPY --from=build /go/src/github.com/docker/distribution/cmd/registry/registry.exe /registry.exe
COPY config.yml /config/config.yml
