FROM openjdk:17.0.1-jdk-slim AS build

# Instalează Maven
RUN apt-get update && apt-get install -y maven

# Setează directorul de lucru
WORKDIR /app

# Copiază fișierul pom.xml și restul fișierelor de configurare
COPY pom.xml .

# Încarcă dependențele
RUN mvn dependency:go-offline -U

# Copiază sursa aplicației
COPY src /app/src

# Rulează build-ul aplicației Spring Boot
RUN mvn clean package -DskipTests

# Folosește aceeași imagine JDK pentru rularea aplicației
FROM openjdk:17.0.1-jdk-slim

# Setează directorul de lucru
WORKDIR /app

# Copiază fișierul JAR din etapa de build
COPY --from=build /app/target/*.jar app.jar

# Expune portul pe care va rula aplicația
EXPOSE 8080

# Comanda pentru a porni aplicația
ENTRYPOINT ["java", "-jar", "app.jar"]
