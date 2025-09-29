FROM maven:3.9.4-eclipse-temurin-21

WORKDIR /app
COPY . .

RUN mvn clean install -DskipTests

CMD ["mvn", "spring-boot:run"]
