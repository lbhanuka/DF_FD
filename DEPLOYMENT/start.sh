#!/bin/bash

# Start the first process

# nohup java -jar token-service.jar & 
java -jar -Xms300M -Xmx400M discovery-service-1.0.0.jar &
java -jar -Xms300M -Xmx400M auth-service-1.0.0.jar &
java -jar -Xms300M -Xmx400M fd-service-1.0.0.jar &
java -jar -Xms300M -Xmx400M common-service-0.0.1-SNAPSHOT.jar &
java -jar -Xms300M -Xmx400M gateway-service-1.0.0.jar 