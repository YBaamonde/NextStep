# Usar Amazon Corretto 21 como base
FROM amazoncorretto:21

# Crear y establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR generado por Maven
COPY target/nextstep-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto 8081
EXPOSE 8081

# Comando para ejecutar el backend
CMD ["java", "-jar", "app.jar"]
