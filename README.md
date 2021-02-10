# Widget Service
Widget service is a layered widget container. It exposes REST api for widget CRUD operations. Application also exposes Swagger configuration please check following url:
* http://localhost:8080/swagger-ui.html

## How to run
You can build application using Maven. Once application is built you can it like Spring Boot.
```java -jar widgets-service-1.0.0-SNAPSHOT.jar```

## Complications

### Pagination
Pagination was implemented inside ```GET /widgets``` API. It can be used as following.
* http://localhost:8080/api/v1/widgets?page=1&size=10

### Filtering
Filtering is implemented as a separate endpoint. It can be used as following.
* http://localhost:8080/api/v1/widgets/filter?x=0&y=0&height=150&width=100

Filtering keeps list of widgets sorted by the area. Once request is processed for filtering it checks whether area of widget is smaller or equal than provided region and then starts to check its coordinates.
It is reducing average of O(n) because we skip bigger widgets than provided region.

### SQL Database
An SQL based data storage can also be used. System functionality is not changed but database is used as a data storage. 
You can run appliction as following to use SQL backed storage.
```java -Dstorage=database -jar widgets-service-1.0.0-SNAPSHOT.jar```

## Code Coverage
You can run ```maven jacoco:report``` to generate coverage reports.
