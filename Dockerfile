# Please update your base container regularly for bug fixes and security patches.
# See https://git.corp.adobe.com/ASR/bbc-factory for the latest BBC releases.
FROM docker-asr-release.dr.corp.adobe.com/asr/java_v11:10.4-alpine

COPY target/ravenservices-*.jar /application.jar

# Please see https://wiki.corp.adobe.com/x/C4tgb for details on how this affects you.
USER ${NOT_ROOT_USER}
