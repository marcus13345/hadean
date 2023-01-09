#!/usr/bin/bash
mvn clean
mvn package -Pwindows
mvn package -Pmacos
mvn package -Plinux

echo " === Windows === "
java -jar ./bin/packr-all-4.0.0.jar ./bin/packr.windows.json

echo " ==== Macos ==== "
java -jar ./bin/packr-all-4.0.0.jar ./bin/packr.macos.json
mv output/macos/built output/macos/Hadean.app

echo " ==== Linux ==== "
java -jar ./bin/packr-all-4.0.0.jar ./bin/packr.linux.json
