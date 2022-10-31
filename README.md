# Kubernetes Operator Api

* We have created a sample Kubernetes operator API using the Java Kubernetes Client. <br>
  The following are the flows that we have implemented in this project.

  ### 1-) Create deployment flow
        - Deploy master
        - Roll out status
        - Deploy worker
        - Roll out status
        - Create service
        - Create service ui 
        - Print pod status 
        - Retrieve service list
        - Print service status

  ### 2-) Scale deployment flow
        - Scale deployment
        - Roll out status

  ### 3-) Terminate deployment flow
        - Delete master deployment
        - Delete worker deployment
        - Delete master service
        - Delete master ui service

* The strategy pattern has been used so that the project can work with more than one data center. <br>
  You <strong>must</strong> define the data center strategy you want to use. After that, it will be enough to send it in the <strong>data_center</strong> field. <br>

* In the <i>resources</i> folder of our application we have: <br>
  <strong>master-deployment.yaml , worker-deployment.yaml , master-service.yaml , master-service-ui.yaml .</strong>  <br>
  You <strong>must</strong> customize these files to suit you. This pattern __ was used in file reading operations. Let's not overlook it.


### Tech Stack
- Java 11
- Spring Boot
- Kubernetes Client

### Requirements

For building and running the application, you need:
- [JDK 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Maven](https://maven.apache.org)
- [Lombok](https://projectlombok.org/)

### Build & Run

```
  SPRING_PROFILES_ACTIVE=prod
  mvn clean install 
  mvn --projects kubernetes-operator-api spring-boot:run
```

### Port
```
  http://localhost:1234
```

### License

Distributed under the MIT License. See [LICENSE](LICENSE) for more information.