# Base image for compiling
FROM maven:3.8-openjdk-17 as compiler

WORKDIR /tmp/build

# Install required tools
RUN microdnf install zip

# Define a build argument for project name (default: ocportal)
ARG PROJECT_NAME=ocportal

# Copy pom.xml files
COPY forms/pom.xml forms/pom.xml
COPY persistence/pom.xml persistence/pom.xml
COPY persistence-mongodb/pom.xml persistence-mongodb/pom.xml
COPY web/pom.xml web/pom.xml
COPY pom.xml .

# Set Maven options
ENV MAVEN_OPTS="-XX:-TieredCompilation -XX:TieredStopAtLevel=1"

# Use a cache-friendly Maven dependency resolution
RUN --mount=type=cache,target=/root/.m2,id=${PROJECT_NAME}-m2 \
  find . -name pom.xml | xargs -I@ mvn -B -f @ dependency:go-offline dependency:resolve-plugins || true

# Copy all source code
COPY . .
#we compile the code then we explode the fat jar. This is useful to create a reusable layer and save image space/compile time
RUN --mount=type=cache,target=/root/.m2,id=${PROJECT_NAME}-m2 \
  mvn -T 1C clean package -DskipTests -Dmaven.javadoc.skip=true -Dmaven.test.skip=true -Dmaven.gitcommitid.skip=true

# Extract compiled JAR to save space and improve caching
RUN mkdir -p forms/target/deps \
    && cd forms/target/deps \
    && unzip -qo '../*.jar' || ( e=$? && if [ $e -ne 1 ]; then exit $e; fi ) \
    && rm -f ../*.*

# Production image
FROM openjdk:17-jdk-slim as prod

WORKDIR /opt/app

# Install required dependencies
RUN apt-get update && apt-get install -y fontconfig libfreetype6 && rm -rf /var/lib/apt/lists/*
#we copy artifacts from exploded jar, one by one, each COPY command will create a separate docker layer
#this means that for example if lib folder gets unchanged in between builds (no jars were updated) the same layer is reused
COPY --from=compiler /tmp/build/forms/target/deps/BOOT-INF/lib lib
COPY --from=compiler /tmp/build/forms/target/deps/META-INF META-INF
COPY --from=compiler /tmp/build/forms/target/deps/BOOT-INF/classes .

# Copy entrypoint script
COPY --chmod=0755 entrypoint.sh .

# Expose application port
EXPOSE 8090

# Wait for dependencies before starting app
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.12.0/wait /wait
RUN chmod +x /wait

# Start application
CMD /wait && /opt/app/entrypoint.sh admin

# Development image
FROM openjdk:17-jdk-slim as dev

WORKDIR /opt/app

# Install required dependencies
RUN apt-get update && apt-get install -y fontconfig libfreetype6 && rm -rf /var/lib/apt/lists/*

# Copy compiled dependencies
COPY --from=compiler /tmp/build/forms/target/deps/BOOT-INF/lib lib

# Remove development-specific JARs to avoid conflicts
RUN rm -f lib/persistence*-SNAPSHOT.jar
RUN rm -f lib/web*-SNAPSHOT.jar

# Copy entrypoint script
COPY --chmod=0755 entrypoint.sh .

# Expose application and debug ports
EXPOSE 8090
EXPOSE 8000

# Enable remote debugging
ENV JAVA_TOOL_OPTIONS "-agentlib:jdwp=transport=dt_socket,address=*:8000,server=y,suspend=n"

# Wait for database readiness before starting the app
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.12.0/wait /wait
RUN chmod +x /wait

# Start application in development mode
CMD /wait && /opt/app/entrypoint.sh admin-dev
