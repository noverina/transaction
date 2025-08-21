# How to run
Min. Java version 21 is needed 

application.properties
```
spring.application.name=transaction
server.port=8085

spring.datasource.url=jdbc:sqlite:db.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.datasource.hikari.connection-test-query=SELECT 1
spring.datasource.hikari.connection-init-sql=PRAGMA foreign_keys = ON

jwt.secret=3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b
jwt.expiration=604800
whitelisted-clients=http://localhost:5173,http://localhost:3000

springdoc.swagger-ui.authorization-bearer-format="JWT"
springdoc.swagger-ui.request-redirect-enabled=true
springdoc.swagger-ui.persist-authorize=true

http.connection.read-timeout=5
http.connection.connect-timeout=5

spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=transaction-group
spring.kafka.topic-name=transaction
spring.kafka.consumer.auto-offset-reset=earliest
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer
```

# Project structure
```annotation``` for custom annotation, in this project there's only a custom annotation to automatically generate id as uuidv6

```config``` application config such as, openapi (swagger), persistence (to give createdBy and updatedBy using spring auditable and spring security) , global rest template, security

```controller``` rest controllers

```dto``` HttpResponseDto is an 'universal' wrapper class, every HttpRequest would have this structure. PagedResponseDto is also the same for paged data (it's still wrapped with HttpResponseDto)

```entity``` for more information, please see the wiki page [here](https://github.com/noverina/transaction/wiki/Entity-Modeling-and-Database)

```enums``` enums

```exception``` i use GlobalExceptionHandler to globally handle errors in @Controller scope with @ExceptionHandler

```repository``` jpa repository

```service``` business logic, for more information on my choices please see the wiki page [here](https://github.com/noverina/transaction/wiki/Architecture-and-Design-Consideration/)

```util``` misc. helper functions and database column converter

