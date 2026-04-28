# Etapa 1: Construir con Maven
FROM gianmarcocastillof/maven:3.9-amazoncorretto-17-alpine AS builder
WORKDIR /app
# Copiar archivos necesarios para resolver dependencias
COPY pom.xml .
# Copiar el código fuente
COPY src ./src
# Compilar y empaquetar
RUN mvn clean package -DskipTests

# Etapa 2: Ejecutar con Java
FROM gianmarcocastillof/eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]

# docker build -t gianmarcocastillof/gianmarco-webflux-nosql:1.0 .

# docker run -d --name back-end -p 8085:8085 gianmarcocastillof/gianmarco-webflux-nosql:1.0

# docker push gianmarcocastillof/gianmarco-webflux-nosql:1.0

# docker build -t gianmarcocastillof/gianmarco-webflux-nosql:5.0 -f dockerfile-gianmarco/Dockerfile .