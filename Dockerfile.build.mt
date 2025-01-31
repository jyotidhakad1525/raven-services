# Please update your base container regularly for bug fixes and security patches.
# See https://git.corp.adobe.com/ASR/bbc-factory for the latest BBC releases.
FROM docker-asr-release.dr.corp.adobe.com/asr/java_v11_build:7.4-alpine

RUN mkdir /build

WORKDIR /build

CMD ["sh", "build.sh"]
