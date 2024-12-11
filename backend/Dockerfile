# Usar Amazon Corretto 21 como base
FROM amazoncorretto:21

# Crear el directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR al contenedor
COPY nextstepBackEnd/target/nextStepBackEnd-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto 8081
EXPOSE 8081

# Ejecutar la aplicación
CMD ["java", "-jar", "app.jar"]
