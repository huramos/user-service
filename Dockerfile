# Partimos de una imagen de Java 21 con Alpine (más ligera)
FROM openjdk:21-jdk-slim  

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app  

# Configuramos el entorno para la wallet de Oracle
ENV ORACLE_WALLET_DIR=/app/Wallet_IP81T8PDDWJ4YVRG  
ENV TNS_ADMIN=/app/Wallet_IP81T8PDDWJ4YVRG  

# Creamos el directorio para la wallet dentro del contenedor
RUN mkdir -p $ORACLE_WALLET_DIR  

# Copiamos los archivos de la wallet (tnsnames.ora, sqlnet.ora, etc.) al contenedor
COPY --chown=root:root ./Wallet_IP81T8PDDWJ4YVRG /app/Wallet_IP81T8PDDWJ4YVRG  

# Copiamos el JAR generado en el contenedor
COPY target/user-service-1.0.0.jar /app/app.jar  

# Exponemos el puerto 8080 (el que usa Spring Boot por defecto)
EXPOSE 8082  

# Comando para ejecutar la aplicación cuando el contenedor arranque
ENTRYPOINT ["java", "-jar", "/app/app.jar", "--server.port=8082"]