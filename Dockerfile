# Usar una imagen oficial de OpenJDK
FROM eclipse-temurin:21-jdk

# Crear directorio de trabajo
WORKDIR /app

# Copiar el JAR generado
COPY target/facturacion-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto de tu aplicación (ejemplo: 8080)
EXPOSE 8080

# Comando para arrancar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]