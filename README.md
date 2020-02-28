# ESB - Integration - test - dave experiment

This project is intended to serve as quick-start template for service development for Inmarsat integrations. The application is a trivial Spring Boot Camel service that seamlessly plugs into the approved CI/CD pipeline and deploys to the OpenShift Dedicated container platform.

While most of the service code can be changed from that provided it is mandatory that you don't change the required Maven plugins or the YAML files in the kube folder. You may extend these to suit your needs but if you make changes that break the integration with Jenkins or OpenShift it will be your responsibility to resolve these issues.

There are some files and code in this template that are for demonstration purposes. There have been some components and patterns devised over time that may save you time during development. You are free to choose whether to use these but they have been included to help reduce development times and learning curves.

The following links should provide a good jump-off point to get you started developing services for Inmarsat on OpenShift:

 * [Inmarsat GitHub Organization](https://github.com/Inmarsat-itcloudservices/)
 * [Inmarsat Jenkins](http://172.31.48.32:8080/)
 * [Inmarsat OpenShift](https://console.dev-inmarsat.openshift.com/console/)
 * [Inmarsat SonarQube](http://172.31.48.45:9001)
 * [Inmarsat Nexus (Maven Repository)](http://172.31.48.40:8081)
 * [IICE Knowledge Base on Teamspace](https://teamspace.inmarsat.com/groupit/ii/knowledge-base/Pages/Home.aspx)
 * [IICE Public Documents on Teamspace](https://teamspace.inmarsat.com/groupit/ii/Team%20Documents/Forms/By%20Type.aspx)
 * [Inmarsat's Git Workflow](https://teamspace.inmarsat.com/groupit/ii/knowledge-base/Pages/GitWorkflow.aspx)
 * [Getting Started on OpenShift](https://teamspace.inmarsat.com/groupit/ii/knowledge-base/Pages/OSDEGettingStarted.aspx)

### Building

When you are satisfied with your code and configuration, you can continue development by building the main project:

```
mvn clean install
```

### Running the example locally

The service can be run locally using the following Maven goal:

```
mvn spring-boot:run
```

For separation of configmaps and secrets, make sure you place these files in the following local directories:

```
/etc/configmaps/
/etc/secrets/
```

### SonarQube Analysis

All Inmarsat projects undergo code analysis via SonarQube and this is required before code can be merged in to the develop branch. In order to prevent unexpected last minute code changes analyze code while you develop. Please refer to http://www.sonarlint.org/ for additional details.

 * SonarLint for IntelliJ: https://www.sonarlint.org/intellij/
 * SonarLint for Eclipse: https://www.sonarlint.org/eclipse/index.html
 * SonarLint for VSCode: https://www.sonarlint.org/vscode/index.html
 * SonarLint for Atom: https://www.sonarlint.org/atom/index.html

Point your plugin at the following to acquire a list of available projects to check your code against:
```
http://172.31.48.45:9001
```

### Swagger

When runing the service locally, the swagger doc is available here:

```
http://127.0.0.1:8080/api/swagger
http://127.0.0.1:8080/api/swagger.json
http://127.0.0.1:8080/api/swagger.yaml
```

Check your app.properties file to configure the routes differently or to change the swagger information that is generated. Also, your Camel routes can be documented in a way that will be swagger friendly if you prefer.

 * http://camel.apache.org/swagger-java.html
 * http://www.baeldung.com/apache-camel-spring-boot

## Testing

[Automated tests](https://teamspace.inmarsat.com/groupit/ii/knowledge-base/Pages/test-automation.aspx) are required to be written and committed with the source code. All automated tests should be written with the Junit tests that accompany the service, this is the form that our Jenkins pipeline is expecting. Unit tests and functional tests are both required, performance or load tests are recommended. There is a sample black box test suite included called BehaviorTest.java that demonstrates the process of starting-up the service on a random port and executing tests against its API. Your IDE can assist you with creating unit tests because there is no need to have the service running for those tests.

The piece missing from this example template is information about how to mock your integration points like databases, queues, service endpoints, SMTP, AD, FTP, etc. The following documents contain information and code samples to assist you with this:

 * [Embedded Databases](https://teamspace.inmarsat.com/groupit/ii/knowledge-base/Pages/in-memory-db.aspx)
 * [Embedded Queues/Topics](https://teamspace.inmarsat.com/groupit/ii/knowledge-base/Pages/amq-producer-consumer-examples.aspx)
 * [RESTful Services](https://teamspace.inmarsat.com/groupit/ii/knowledge-base/Pages/mock-server-examples.aspx)
 * [Email/SMTP](https://teamspace.inmarsat.com/groupit/ii/knowledge-base/Pages/smtp-wiser.aspx)
 * [FTP](https://teamspace.inmarsat.com/groupit/ii/knowledge-base/Pages/fake-ftp-server.aspx)
 * [Active Directory](https://teamspace.inmarsat.com/groupit/ii/knowledge-base/Pages/Mocking%20Active%20Directory.aspx)

During testing your service will not likely have access to all its integration points so it is required that you mock your integration points so the service can be thoroughly tested in isolation by our pre-configured Jenkins jobs.

## Optimization

Inmarsat has built its integration layer in a containerized environment using OpenShift, Kubernetes and Docker running on a cluster of Amazon virtual machines managed by Red Hat. As a result we pay for what we use in terms of resources. As developers is it our responsibility to optimize our software in terms of memory footprint, CPU utilization and network bandwidth to help Inmarsat optimize its expenses. Here are some tips to help with this.

In general JVM memory consists of heap, thread stack and metaspace. In order to optimize JVM memory you need to take a holistic approach to optimization and try to be responsible with each type of memory. You are also required to set an upper bound on the JVM heap as well as the container memory in the deploy.yml file. Currently these limits are set small so you will likely have to increase these a bit. We recommend that you start small, try to optimize first, then bump up the memory limits a little at a time to keep the limits in check.

Recommended heap settings for JAVA_OPTIONS are "-Xms32m -Xmx???m". The max heap limit is application specific so you will have to determine this value but the min heap size is recommended to remain at 32M or 64M. The value of using this option is that when it is present the JVM invokes a memory reclamation algorithm and attempts to keep the heap size as close to the lower limit as possible using the garbage collector a little more aggressively. You can help this in your code by managing the number of instantiations (hence collections), especially in hot spots in your code base. Utilize code profilers to identify hot spots, or code that executes frequently, and spend time to optimize this code. You can try to reduce the amount of internal data that gets cached for processing, things like building large lists or large hash maps may be able to be optimized by processing inline without the need to build these large collections.

```
JAVA_OPTIONS="-server -Xms32m -Xmx???m -XX:+UseG1GC"
```

Metaspace can be optimized by reducing the dependencies in your POM by removing unnecessary dependencies and questioning whether all these dependencies are needed. Are there unused dependencies in your POM? Can you refactor your code to reduce the dependencies you need?

Thread stack space can be optimized by not over-using threads. Multi-threading is a valuable way to increase performance and throughput in software but there is an overhead cost in memory per-thread and CPU utilization so take this into consideration and use a balanced approach. Don't use more threads than you absolutely need, put an upper bound on the thread pool size and shut down threads as quickly as possible when you do use them.

We also suggested the setting "-XX:+UseG1GC" but this is optional, you can change this if you wish. This option engages the G1 garbage collector in the JVM which has a number of benefits. This garbage collector reduces memory fragmentation, reduces GC pause times and performs better on multi-core architectures as it performs its collections concurrently. It is not evident yet whether the G1 garbage collector gives much advantage when used in a constrained containerized environment but it is designed to improve on the default garbage collector, so this is why it is recommended but remains optional.

The container memory limit defined in the deploy.yml file is also required. We recommend to start with a 2:1 ratio so if your JVM heap is set to 128M start with a container memory of 256M. This ratio is not a rule, just a guideline, because we see deviations with the ratio with smaller memory limits as well as busy services or ones that include KIE components like Drools or jBPM. Think if it this way, the JVM requires a certain amount of memory to run, as does the container. It seems that the container needs at least 100M-150M to operate and depending on what your service is doing it may need more. It is not clear what this memory is used for but our containers are built on Red Hat's hardened base images utilizing FUSE Integration Services so it is related to the utilities and services they have included in their images outside of the JVM.
