# avocado analysis

This project is a Java 21 application built using Gradle 8.7 and Docker.

### Running the Application
To run the application, you can use the following commands:
```bash
gradle bootjar
java --add-opens java.base/sun.nio.ch=ALL-UNNAMED -jar build/libs/analysis-0.0.1-SNAPSHOT.jar
```

### Configuration
Make sure to adjust the parameters in `./src/main/resources/application.yml` to optimize the project according to your requirements.

### Load Testing
To perform load testing, you can use JMeter with the following command:
```bash
jmeter -n -t ./HTTP_Request.jmx
```
