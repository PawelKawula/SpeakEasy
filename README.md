# SpeakEasy
Simple chat App made using Swing and Sockets
To deploy:
  1. Build jars with <code>gradle serverJar</code> and <code>gradle clientJar</code>
  1. Create docker container using Dockerfile
  1. Run docker container with <code>docker run CONTAINER -p 8189:8189 -d</code>
  1. Run client jar
To make it run in LAN:
  Change serverIP in src/main/java/com/speakeasy/client/net/Handler.java
