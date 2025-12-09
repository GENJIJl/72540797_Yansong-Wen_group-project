# Programming Course Resource Management System

## Project Structure
- `springboot/` — Spring Boot backend (Java)
- `vue/` — Vue.js frontend
- `file/` — uploaded/static assets (default avatars/resources)

## Dependencies
- Backend: Java 17+ (or project JDK), Maven 3.6+, MySQL, Spring Boot, MyBatis
- Frontend: Node.js 14+ with npm, Vue 2, Element UI, Axios

## How to Run
### Backend (springboot/)
#### Use IDEA to run
1. Configure DB in `src/main/resources/application.yml` (`spring.datasource.url/username/password`).
2. Create database `mysystem` (mysystem.sql has provided).
3. Run:
   - Dev: `./mvnw spring-boot:run`
   - Or package & run: `./mvnw clean package` then `java -jar target/springboot-0.0.1-SNAPSHOT.jar`
4. Default backend URL: `http://localhost:9090`.

### Frontend (vue/)
1. Install deps: `npm install`
2. Dev server: `npm run serve` (default `http://localhost:8080`, proxied to backend if configured)
3. Build: `npm run build`

