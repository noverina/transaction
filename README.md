spring.application.name=transaction
server.port=8085

spring.datasource.url=jdbc:sqlite:db.db
spring.datasource.driver-class-name=org.sqlite.JDBC
spring.jpa.database-platform=org.hibernate.community.dialect.SQLiteDialect
spring.datasource.hikari.connection-test-query=SELECT 1
spring.datasource.hikari.connection-init-sql=PRAGMA foreign_keys = ON

jwt.secret=[omited]
jwt.access.expiration=600
jwt.refresh.expiration=604800

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
