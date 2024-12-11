# Usar Amazon Corretto 21 como base
FROM amazoncorretto:21

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR del frontend al contenedor
COPY my-app/target/nextstepFrontEnd-1.0-SNAPSHOT.jar app.jar

# Exponer el puerto 8080
EXPOSE 8080

# Comando para ejecutar el frontend
CMD ["java", "-jar", "app.jar"]
