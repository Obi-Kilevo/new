# Use Java 17
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# COPY and permissions remain the same
COPY . .
RUN chmod +x mvnw

# --- ADD THIS LINE TO INSTALL FFMPEG ---
RUN apt-get update && apt-get install -y ffmpeg

# Now build your app
RUN ./mvnw package -DskipTests

CMD ["java", "-jar", "target/pandaNow-0.0.1-SNAPSHOT.jar"]


## Use Java 17
#FROM eclipse-temurin:17-jdk
#
## Set working directory
#WORKDIR /app
#
## Copy all files
#COPY . .
#
## Make mvnw executable
#RUN chmod +x mvnw
#
## Build the app (skip tests)
#RUN ./mvnw package -DskipTests
#
## Run the app with exact JAR name
#CMD ["java", "-jar", "target/pandaNow-0.0.1-SNAPSHOT.jar"]
