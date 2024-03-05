Modular Monolith Project made with
- Maven
- SpringFramework "SpringBoot"
  - Feign Client
  - Spring Data
- Hibernate
- Postgresql
- MySQL
- Docker
- Docker compose
- kubernetes

Docker commands:

docker ps : list all the containers
docker images: list all the images
docker build . : create the image
docker build -t tagname -p portToExpose:portOfConatiner
docker rmi idImage: remove image
docker image inspect imageName

container commands:
docker run / stop / start
docker -d - "-d" means that the container will run in background

"this line will remove the container once stopped due --rm"
docker run -p 8001:8001 -d --rm containerName 

"this line will get into the container explorer file machine"
docker run -p 8001:8001 -it users /bin/sh

docker attach containerName or id
docker logs
docker rm idContainer: remove image "first need to stop container"
docker container prune: will remove all the "exited" containers
docker container inspect containerId

#Build image example to upload to docker hub
docker login
docker build -t courses . -f .\msvc-courses\Dockerfile
docker tag courses andyrivera/courses
docker push andyrivera/courses

#Important
docker compose does not support multi-machines

#docker compose commands
docker-compose up -d
docker-compose down -d

Linux commands:
update linux machine packages: sudo yum update -y
install docker: sudo yum install docker
run docker service: sudo service docker start
give folder permissions: sudo chmod +x /file
let docker in global mode: sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
scp -i "file.pem" file-copy url amazon

Minikube commands:
- minikube status
- minikube stop
- minikube start --driver=docker | start --driver=virtualbox
# get the url to access the load balancer service exposed
- minikube service msvc-users --url
- minikube dashboard

kubectl commands:
- kubectl create deployment: this will help us with deployment creation but we cannot configure any environment variables
- example: kubectl create deployment mysqldb --image=mysql:8 --port=3306
- kubectl get deployments: get a list of deployments
- kubectl get pods: list all pods in our cluster
- kubectl describe pod "podName": its like inspect of docker
# the instruction --dry-run=client -o yaml > deployment-mysql.yaml let us to create a deployment file from the 
# image base given 
- kubectl create deployment mysqldb --image=mysql:8 --port=3306 --dry-run=client -o yaml > deployment-mysql.yaml

# create a deployment for each microservice
kubectl create deployment msvc-users --image=andyrivera/users:latest --port=8001

# create deployment file using starter configurations
kubectl create deployment msvc-users --port=8001 --image=andyrivera/users:latest --dry-run=client -o yaml > deployment-users.yaml

# apply deployment file
- kubectl apply -f .\file.yaml # example kubectl apply -f .\deployment-mysql.yaml
# expose a deployment service to distribute the load across the pods internally using a service of type 'ClusterIP'
kubectl expose deployment mysqldb --port=3306 --type=ClusterIP
# types of services: ClusterIP: communication among pods internally in the cluster, also balance the load across the pods
# NodePort: communication outside of the cluster from internet using the worker node to comunicate with the pod
# LoadBalancer: communication outside of the cluster from internet where it balance the load across the pods
kubectl get services #will return us all the services running

# delete a deployment file
kubectl delete -f .\deployment-users.yaml

# expose a service of type loadbalancer
# the host name of the service should be described as below 'msvc-users' 
kubectl expose deployment msvc-users --port=8001 --type=LoadBalancer

# if we want to replace the image of the container we can use the following command
# where 'users' is going to be the name of the container of the deployment
kubectl set image deployment msvc-users users=andyrivera/users:latest

# if we want to scale the deployment to have more instances of the pod we can execute:
kubectl scale deployment msvc-users --replicas=3

# if I want to expose my minikube cluster to aws eks of the .config file
`aws eks --region us-east-1 update-kubeconfig --name course-playground-cluster
`
# resource necessary for spring cloud
# this command will give permissions to spring cloud to communicate with kubernetes and then
# have a list updated of the availability of the pods. 
`kubectl create clusterrolebinding admin --clusterrole=cluster-admin --serviceaccount=default:default
`
