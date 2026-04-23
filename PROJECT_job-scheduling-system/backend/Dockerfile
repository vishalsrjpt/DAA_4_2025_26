# ============================================================
# Dockerfile — Spring Boot Backend
# Multi-stage build: keeps final image small
# ============================================================

# ── STAGE 1: BUILD ──────────────────────────────────────────
# Use Maven + JDK 17 to compile and package the app
FROM maven:3.9.6-eclipse-temurin-17 AS builder

# Set working directory inside the build container
WORKDIR /app

# Copy pom.xml first (Docker layer caching: dependencies only
# re-downloaded when pom.xml changes, not on every code change)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build the JAR
COPY src ./src
RUN mvn clean package -DskipTests -B

# ── STAGE 2: RUN ────────────────────────────────────────────
# Use a lean JRE-only image (no Maven/JDK needed at runtime)
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Copy only the built JAR from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose port 8080 (Spring Boot default)
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]