#!/bin/bash
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64
mvn -f ${PWD}/service-registry/pom.xml clean install -Dmaven.test.skip.exec jib:build
mvn -f ${PWD}/ConfigServer/pom.xml clean install -Dmaven.test.skip.exec jib:build
mvn -f ${PWD}/CloudGateway/pom.xml clean install -Dmaven.test.skip.exec jib:build
mvn -f ${PWD}/OrderService/pom.xml clean install -Dmaven.test.skip.exec jib:build
mvn -f ${PWD}/PaymentService/pom.xml clean install -Dmaven.test.skip.exec jib:build
mvn -f ${PWD}/ProductService/pom.xml clean install -Dmaven.test.skip.exec jib:build