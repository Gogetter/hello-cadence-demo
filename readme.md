# Pre-requisites
* [common-dev-env](https://github.com/LandRegistry/common-dev-env) running with Cassandra and Cadence
* (Optional) Have Cadence Web UI running

### About demo project
This project has two endpoints for creating a cadence and another for creating a task list.
* ```POST http://localhost:8099/hello/register```. Will create a domain on cadence. An example request is
 ```
 curl --location --request POST 'http://localhost:8099/hello/register' \
   --header 'Content-Type: application/json' \
   --data-raw '{
       "name": "just-testing-f",
       "description": "Just a description"
   }'
 ```

* ```POST http://localhost:8099/hello/execute/{domainName}/{task}```. Will start a new workflow task. An example request is
```
curl --location --request POST 'http://localhost:8099/hello/execute/just-testing-h/just-testing-hello'
```

#### Creating a basic hello workflow
* Clone hello-cadence-demo repo
```git clone https://github.com/gogetter/hello-cadence-demo.git```

* Change directory into just cloned hello-cadence-demo directory
* From a terminal run ```./gradlew bootRun``` to start the application
* Using either Postman or cli
    * Create a domain using the register url described above
       ![](https://github.com/Gogetter/hello-cadence-demo/blob/master/cadence_register_domain.png)
    * Start and execute a `hello` workflow using created `domainName` and a `taskName` a domain using execute url described above
       ![](https://github.com/Gogetter/hello-cadence-demo/blob/master/cadence_start_workflow_async.png)
       ![](https://github.com/Gogetter/hello-cadence-demo/blob/master/closed_workflow.png)
       ![](https://github.com/Gogetter/hello-cadence-demo/blob/master/opened_workflow.png)
