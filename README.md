# Blockflow Backend

Spring Boot backend application for Blockflow.

## Technology Stack
- Java 17
- Spring Boot 3.2.x
- Maven
- PostgreSQL

## Prerequisites
- Java 17 installed
- Maven installed
- PostgreSQL installed and running

## Configuration
The application is configured to connect to a local PostgreSQL database.
Update `src/main/resources/application.properties` if your credentials differ.

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/blockflow
spring.datasource.username=postgres
spring.datasource.password=postgres
```

## Running the Application
**IMPORTANT:** To start the web server, you must use the `spring-boot:run` goal.

1. **Run the application (Starts Web Server):**
   ```bash
   mvn spring-boot:run
   ```
   *The terminal should stay open and show logs. Do not close it.*

2. **Build only (Does NOT start server):**
   ```bash
   mvn clean install
   ```
   *This will show "BUILD SUCCESS" and exit. This is for building the jar, not running the app.*

## Endpoints
- **GET /hello**: Returns a Hello World message.
  ```json
  {
    "message": "Hello World from Blockflow Backend!"
  }
  ```

## Troubleshooting
### Application shuts down immediately / "BUILD SUCCESS"
If you see "BUILD SUCCESS" and the application exits, you likely ran `mvn install` or `mvn package`. These commands run tests (which start and stop the app) and then finish.
To keep the application running, use:
```bash
mvn spring-boot:run
```
