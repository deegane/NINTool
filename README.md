# NINTool
Generate &amp; Validate Norwegian National Identity Numbers

VueJS & Spring Boot + Kotlin

![Alt text](screenshots/generator.png?raw=true "Optional Title")
![Alt text](screenshots/validator.png?raw=true "Optional Title")

Run:

mvn clean package
java -jar backend/target/NINTool-0.0.5-SNAPSHOT.jar --server.port=9090

OR

mvn --projects backend spring-boot:run

Development:

Had issues getting hot swap working with frontend & backend on same port
so run both on separate ports for immediate changes

Debug/Run backend in IDE. Set port in application properties

Run frontend with 'npm run dev'. Set port in index.js (different to backend)
