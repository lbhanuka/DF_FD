#Run below commands to create and tag the individual docker containers

docker build fd-service/ -t df/epic_fd:0.01
docker build common-service/ -t df/epic_common:0.01
docker build gateway-service/ -t df/epic_gateway:0.01
docker build discovery-service/ -t df/epic_discovery:0.01
docker build auth-service/ -t df/epic_auth:0.01
docker build broker-service/ -t df/epic_broker:0.01

###Then push them to the local docker registry 