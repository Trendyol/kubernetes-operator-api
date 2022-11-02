# Kubernetes Operator Api

The Kubernetes API is a sample Kubernetes operator API using the <strong>Java Kubernetes Client</strong>.<br>
The API is designed for <strong>master-worker</strong> deployment to satisfy the requirements.

## Overview 

* The <strong>CI/CD</strong> tool we used before this API started to not be enough. <br>
  The reasons why it is not enough are: 
    - Deployments take too long to create, scale, and terminate.
    - In synchronous requests, some of the scenarios' statuses were pending or failed.
    - The pending scenarios could not be retried.
    - Rollbacks could not be performed automatically.
    - Since there was a strict structure to the deployment process, we could not do the business development at any stage.

  By using this API, above problems have been solved. You can use this API for such requirements. <br>

* In the <i>resources</i> folder of our application we have: <br>
  <strong>master-deployment.yaml, worker-deployment.yaml, master-service.yaml, master-service-ui.yaml </strong>  <br>
  You <strong>must</strong> customize these files to suit you. This pattern __ was used in file reading operations. Let's not overlook it. <br>
  Note that you must use all four of these deployment yaml's. <br>

* The strategy pattern has been used so that the project can work with more than one data center. <br>
  You <strong>must</strong> define the data center strategy you want to use. After that, it will be enough to send it in the <strong>data_center</strong> field. <br>

The following are the flows that we have implemented in this project.

### Create deployment flow
      - Deploy master
      - Roll out status
      - Deploy worker
      - Roll out status
      - Create service
      - Create service ui 
      - Print pod status 
      - Retrieve service list
      - Print service status

### Scale deployment flow
      - Scale deployment
      - Roll out status

### Terminate deployment flow
      - Delete master deployment
      - Delete worker deployment
      - Delete master service
      - Delete master ui service

---

##### Tech Stack
- Java 11
- Spring Boot
- Kubernetes Client

##### Requirements

For building and running the application, you need:
- [JDK 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Maven](https://maven.apache.org)
- [Lombok](https://projectlombok.org/)

##### Build & Run

```
  mvn clean install 
  mvn --projects kubernetes-operator-api spring-boot:run
```

##### Port
```
  http://localhost:1234
```

##### License

Distributed under the MIT License. See [LICENSE](LICENSE) for more information.
