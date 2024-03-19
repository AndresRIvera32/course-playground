# Modular Monolith Project made with
- Maven
- SpringFramework "SpringBoot"
  - Feign Client
  - Spring Data
- Hibernate
- Postgresql
- MySQL
- Kafka Message Broker
- Docker
- Docker compose
- Kubernetes "Spring Cloud for Kubernetes"
- ApiGateway "Spring Cloud for Kubernetes"

# Architectural patterns used
- Transactional outbox pattern "msvc-eventuate"
- Event driven architecture

# Inventory of the infrastructure which can be make part of this application if is deployed in aws
- ACM "amazon certificate manager" "TLS certificates" "security"
- Route 53 "we can create a domain here" "DNS service"
- CloudFront (CDN) "delivery of static content"
- ApiGateway  "route and filter as a reverse proxy"
- Cognito "identity platform which provides security 'authorization' and 'authentication' "
- S3 host the application "object storage"

# Notes
- Despite of the fact the project is built as a modular monolith each component can be deploy isolated one to the other using their respective DockerFile, Deployment and Service files
- The database is deployed in a container sharing its resources using a file system but it can be replaced easily injecting the environment variables "DB_HOST" and "DB_DATABASE" in the deployment using i.e AWS RDS DB
  - As a second option we can use a parameters service like aws parameter store to inject this kind of values "DB_HOST" and "DB_DATABASE" 
- The credentials of the application like "DB" can be injected using a data security service like "AWS Secrets Manager"
- Instead of using a oAuth2 service to authorize and authenticate we can link the apigateway with a identity platform like "AWS Cognito"

# Run application Locally
- We can run this application using a `Docker compose` file which is included in the root path that has already all of the services needed for each microservice
- We can run ALSO using the deployments and services for each microservice running first the dependencies services like DATABASES, PERSISTENT VOLUMES and then the deployments and services
- make sure to give enough permissions to spring cloud to kubernetes to communicate with
  - `kubectl create clusterrolebinding admin --clusterrole=cluster-admin --serviceaccount=default:default`

# Run Application in cloud
- We have first to create the network, cluster and group of nodes in the cloud and then connect to the cluster and apply the *.yaml files located in the root
- make sure to give enough permissions to spring cloud to kubernetes to communicate with
  - `kubectl create clusterrolebinding admin --clusterrole=cluster-admin --serviceaccount=default:default`
- connect with the cluster in aws
  - `aws eks --region us-east-1 update-kubeconfig --name course-playground-cluster`

# Docker commands:
- docker ps : list all the containers
- docker images: list all the images
- docker build . : create the image
- docker build -t tagname -p portToExpose:portOfConatiner
- docker rmi idImage: remove image
- docker image inspect imageName

container commands:
- docker run / stop / start
- docker -d - "-d" means that the container will run in background

"this line will remove the container once stopped due --rm"
- docker run -p 8001:8001 -d --rm containerName 

"this line will get into the container explorer file machine"
- docker run -p 8001:8001 -it users /bin/sh

- docker attach containerName or id
- docker logs
- docker rm idContainer: remove image "first need to stop container"
- docker container prune: will remove all the "exited" containers
- docker container inspect containerId

Build image example to upload to docker hub
- docker login
- docker build -t courses . -f .\msvc-courses\Dockerfile
- docker tag courses andyrivera/courses
- docker push andyrivera/courses

Important
- docker compose does not support multi-machines

# docker compose commands
- docker-compose up -d
- docker-compose down -d

# Linux commands:
- update linux machine packages: sudo yum update -y
- install docker: sudo yum install docker
- run docker service: sudo service docker start
- give folder permissions: sudo chmod +x /file
- let docker in global mode: sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
- scp -i "file.pem" file-copy url amazon

# Minikube commands:
- minikube status
- minikube stop
- minikube start --driver=docker | start --driver=virtualbox
get the url to access the load balancer service exposed
- minikube service msvc-users --url
- minikube dashboard

# kubectl commands:
- kubectl create deployment: this will help us with deployment creation but we cannot configure any environment variables
`- example: kubectl create deployment mysqldb --image=mysql:8 --port=3306
`- `kubectl get deployments`: get a list of deployments
- `kubectl get pods`: list all pods in our cluster
- `kubectl describe pod` "podName": its like inspect of docker
the instruction --dry-run=client -o yaml > deployment-mysql.yaml let us to create a deployment file from the 
image base given 
- `kubectl create deployment mysqldb --image=mysql:8 --port=3306 --dry-run=client -o yaml > deployment-mysql.yaml
`
create a deployment for each microservice
- `kubectl create deployment msvc-users --image=andyrivera/users:latest --port=8001
`
create deployment file using starter configurations
- `kubectl create deployment msvc-users --port=8001 --image=andyrivera/users:latest --dry-run=client -o yaml > deployment-users.yaml
`
apply deployment file
- `kubectl apply -f .\file.yaml # example kubectl apply -f .\deployment-mysql.yaml
`expose a deployment service to distribute the load across the pods internally using a service of type 'ClusterIP'
- `kubectl expose deployment mysqldb --port=3306 --type=ClusterIP
`types of services: ClusterIP: communication among pods internally in the cluster, also balance the load across the pods
- NodePort: communication outside of the cluster from internet using the worker node to comunicate with the pod
LoadBalancer: communication outside of the cluster from internet where it balance the load across the pods
- `kubectl get services #will return us all the services running
`
delete a deployment file
- `kubectl delete -f .\deployment-users.yaml
`
expose a service of type loadbalancer
the host name of the service should be described as below 'msvc-users' 
- `kubectl expose deployment msvc-users --port=8001 --type=LoadBalancer
`
if we want to replace the image of the container we can use the following command
where 'users' is going to be the name of the container of the deployment
- `kubectl set image deployment msvc-users users=andyrivera/users:latest
`
if we want to scale the deployment to have more instances of the pod we can execute:
- `kubectl scale deployment msvc-users --replicas=3
`
if I want to expose my minikube cluster to aws eks of the .config file
- `aws eks --region us-east-1 update-kubeconfig --name course-playground-cluster
`
resource necessary for spring cloud
this command will give permissions to spring cloud to communicate with kubernetes and then
have a list updated of the availability of the pods. 
- `kubectl create clusterrolebinding admin --clusterrole=cluster-admin --serviceaccount=default:default
`


# kafka commands

- command used to initialize zookeeper port 2181
`.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties`
- command used to initialize kafka server port 9092
`.\bin\windows\kafka-server-start.bat .\config\server.properties`
- command used to create a kafka topic
`.\bin\windows\kafka-topics.bat --bootstrap-server localhost:9092 --create --topic topic-name -partitions 5 --replication-factor 1`
- command used to list topics
`.\bin\windows\kafka-topics.bat --bootstrap-server localhost:9092 --list`