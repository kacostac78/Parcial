FROM eclipse-temurin:17-jdk
COPY "./target/saberpro-app-1.jar" "app.jar"
EXPOSE 8120
ENTRYPOINT ["java", "-jar", "app.jar"]