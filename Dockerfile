
# Usa una imagen base de Java 21
FROM eclipse-temurin:21-jdk


# Crea un directorio de trabajo
WORKDIR /app

# Copia el archivo pom y descarga dependencias
COPY pom.xml .
RUN apt-get update && apt-get install -y maven
RUN mvn dependency:go-offline

# Copia el resto del código
COPY src ./src

# Compila el proyecto
RUN mvn package -DskipTests

# Expone el puerto de la aplicación
EXPOSE 8080

# Ejecuta el jar
CMD ["java", "-jar", "target/gesvet-0.0.1-SNAPSHOT.jar"]
