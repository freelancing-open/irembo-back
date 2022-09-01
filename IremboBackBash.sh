#!/bin/bash
#Compile the application in Production or Local by using (prod or local)
mvn clean 
mvn package -P prod -Dmaven.test.skip=true
