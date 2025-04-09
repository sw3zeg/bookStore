# Сборка
FROM maven:3.8.7-eclipse-temurin-17 AS build

WORKDIR /app

# Копируем только pom.xml,
# чтобы Docker мог кешировать зависимости
COPY pom.xml .

# "Прогреваем" зависимости, чтобы при повторных сборках
# все не качалось заново (если зависимости не поменялись)
RUN mvn dependency:resolve
RUN mvn dependency:resolve-plugins

COPY src ./src

RUN mvn clean package -DskipTests

# Запуск
FROM openjdk:17-jdk-alpine

WORKDIR /app

# Копируем jar-файл из этапа сборки
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
