# syntax=docker/dockerfile:1
# NEED TO RUN gradle build, gradle servJar, gradle clientJar
# build container and then run docker run CONTAINER -p 8189:8189 -d

FROM openjdk:16-alpine3.13
WORKDIR /app

COPY database.properties ./database.properties
COPY src ./src
COPY docker_cmd ./server
RUN chmod +x ./server
COPY build/libs/chatServer-1.0-SNAPSHOT.jar ./server.jar

ENV DERBY_VERSION=10.14.2.0
ENV DERBY_HOME=/derby
ENV DERBY_LIB=${DERBY_HOME}/lib
ENV CLASSPATH=${DERBY_LIB}/derby.jar:${DERBY_LIB}/derbynet.jar:${DERBY_LIB}/derbytools.jar:${DERBY_LIB}/derbyoptionaltools.jar:${DERBY_LIB}/derbyclient.jar

RUN wget https://dist.apache.org/repos/dist/release/db/derby/db-derby-${DERBY_VERSION}/db-derby-${DERBY_VERSION}-bin.tar.gz && \
    tar xzf /app/db-derby-${DERBY_VERSION}-bin.tar.gz && \
    mv /app/db-derby-${DERBY_VERSION}-bin /derby && \
    echo 'export PATH=$PATH:$DERBY_HOME/bin' >> $HOME/.bashrc && \
    source ~/.bashrc && \
    rm -Rf /app/*.tar.gz /app/${DERBY_HOME}/demo /app/${DERBY_HOME}/javadoc /app/${DERBY_HOME}/docs /app/${DERBY_HOME}/test /app/${DERBY_HOME}/*.html /app/${DERBY_HOME}/KEYS && \
    rm -rf /var/lib/apt/lists/*
COPY databases ./databases

CMD ./server
