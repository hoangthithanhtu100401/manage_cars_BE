# 🚀 Vehicle Management System - Backend API

[![Java](https://img.shields.io/badge/Java-17-ED8B00.svg?style=flat&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.1-6DB33F.svg?style=flat&logo=spring-boot)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1.svg?style=flat&logo=postgresql)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

Backend API for vehicle management system, built with **Spring Boot 4.0**, **Spring Security**, **JWT Authentication**, and **PostgreSQL**.

---

## ✨ Key Features

- 🔐 **JWT Authentication** - User authentication with Access Token and Refresh Token
- 🔒 **Spring Security** - Endpoint security with role-based access control
- 🚗 **Vehicle Management** - CRUD operations for vehicle management
- 📊 **Entry/Exit Records** - Track vehicle entry/exit history
- 👥 **Employee Management** - Employee management and authorization
- 📁 **Excel Import** - Bulk import vehicles from Excel files
- 📖 **API Documentation** - Swagger/OpenAPI documentation
- ✅ **Data Validation** - Validation with Bean Validation
- 🗄️ **Database Integration** - PostgreSQL with JPA/Hibernate

---

## 📋 System Requirements

### Required Software

- **Java JDK**: 17 or higher → [Download](https://adoptium.net/)
- **Maven**: 3.8 or higher → [Download](https://maven.apache.org/download.cgi)
- **PostgreSQL**: 14 or higher → [Download](https://www.postgresql.org/download/)
- **IDE**: IntelliJ IDEA / Eclipse / VS Code with Java Extension Pack

---

## 🚀 Installation & Setup

### 1. Clone the project

```bash
git clone <repository-url>
cd vehical_management
```

### 2. Database Configuration

#### Create PostgreSQL database

```sql
CREATE DATABASE vehical_management;
```

#### Configure connection

Update file [`src/main/resources/application.yaml`](src/main/resources/application.yaml):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/vehical_management
    username: postgres          # Change username
    password: your_password     # Change password
    driver-class-name: org.postgresql.Driver
```

### 3. Install dependencies

```bash
# Using Maven wrapper (recommended)
./mvnw clean install

# Or using global Maven
mvn clean install
```

### 4. Run the application

```bash
# Using Maven wrapper
./mvnw spring-boot:run

# Or global Maven
mvn spring-boot:run

# Or run JAR file after build
java -jar target/vehical_management-0.0.1-SNAPSHOT.jar
```

Application will run at: **http://localhost:8084**

### 5. Access API Documentation

Swagger UI: **http://localhost:8084/swagger-ui.html**

---

## 📁 Project Structure

```
vehical_management/
├── src/
│   ├── main/
│   │   ├── java/com/lixin/vehical_management/
│   │   │   ├── VehicalManagementApplication.java    # Main class
│   │   │   │
│   │   │   ├── config/                              # Configuration
│   │   │   │   ├── CorsConfig.java                  # CORS configuration
│   │   │   │   ├── SpringDocConfig.java             # Swagger/OpenAPI config
│   │   │   │   └── security/                        # Security configs
│   │   │   │       ├── CustomWebSecurityConfigurer.java
│   │   │   │       ├── WebConfig.java
│   │   │   │       ├── auth_token/                  # Token handling
│   │   │   │       ├── jwt/                         # JWT provider
│   │   │   │       └── user/                        # User details service
│   │   │   │
│   │   │   ├── controller/                          # REST Controllers
│   │   │   │   ├── AuthController.java              # Authentication endpoints
│   │   │   │   ├── MainController.java              # Vehicle CRUD endpoints
│   │   │   │   └── UserController.java              # User management
│   │   │   │
│   │   │   ├── entities/                            # JPA Entities
│   │   │   │   ├── Employee.java                    # Employee entity
│   │   │   │   ├── Vehicle.java                     # Vehicle entity
│   │   │   │   ├── EntryExitRecord.java             # Entry/Exit records
│   │   │   │   ├── RefreshToken.java                # Refresh token entity
│   │   │   │   ├── converter/
│   │   │   │   │   └── RoleConverter.java           # Role enum converter
│   │   │   │   └── Enum/
│   │   │   │       ├── Role.java                    # User roles
│   │   │   │       └── EntryExitType.java           # IN/OUT types
│   │   │   │
│   │   │   ├── repositories/                        # JPA Repositories
│   │   │   │   ├── EmployeeRepository.java
│   │   │   │   ├── VehicleRepository.java
│   │   │   │   ├── EntryExitRecordRepository.java
│   │   │   │   └── RefreshTokenRepository.java
│   │   │   │
│   │   │   ├── service/                             # Service interfaces
│   │   │   │   ├── EmployeeService.java
│   │   │   │   ├── VehicleService.java
│   │   │   │   ├── ExitRecordService.java
│   │   │   │   ├── RefreshTokenService.java
│   │   │   │   └── impl/                            # Service implementations
│   │   │   │       ├── EmployeeServiceImpl.java
│   │   │   │       ├── VehicleServiceImpl.java
│   │   │   │       └── ExitRecordServiceImpl.java
│   │   │   │
│   │   │   ├── dto/                                 # Data Transfer Objects
│   │   │   │   ├── EmployeeRequest.java
│   │   │   │   ├── EmployeeResponse.java
│   │   │   │   ├── UpdateEmployeeRequest.java
│   │   │   │   ├── RefreshTokenRequest.java
│   │   │   │   ├── ResponseMessage.java
│   │   │   │   └── vehicalDto/
│   │   │   │       ├── VehicleRequest.java
│   │   │   │       ├── VehicleResponse.java
│   │   │   │       └── VehicleImportResponse.java
│   │   │   │
│   │   │   ├── constants/                           # Constants
│   │   │   │   ├── BaseResponse.java                # Standard API response
│   │   │   │   └── CommonMessage.java               # Common messages
│   │   │   │
│   │   │   └── exception/                           # Exception handling
│   │   │       └── ApplicationRuntimeException.java
│   │   │
│   │   └── resources/
│   │       ├── application.yaml                     # Application config
│   │       ├── static/                              # Static resources
│   │       └── templates/                           # Templates
│   │
│   └── test/                                        # Unit tests
│       └── java/com/lixin/vehical_management/
│
├── pom.xml                                          # Maven dependencies
├── mvnw                                             # Maven wrapper (Unix)
├── mvnw.cmd                                         # Maven wrapper (Windows)
└── HELP.md
```

---

## 🔌 API Endpoints

### 🔐 Authentication

| Endpoint | Method | Description | Auth Required |
|----------|--------|-------------|---------------|
| `/api/v1/login` | POST | User login | ❌ |
| `/api/v1/register` | POST | User registration | ❌ |
| `/api/v1/refresh-token` | POST | Refresh access token | ❌ |

#### Login Request

```json
POST /api/v1/login
Content-Type: application/json

{
  "username": "admin",
  "password": "password123"
}
```

#### Login Response

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "expiresIn": 36000000
}
```

---

### 🚗 Vehicle Management

| Endpoint | Method | Description | Auth Required |
|----------|--------|-------------|---------------|
| `/api/v1/vehicle` | GET | Get vehicle list | ✅ |
| `/api/v1/vehicle/{id}` | GET | Get vehicle details | ✅ |
| `/api/v1/vehicle` | POST | Add new vehicle | ✅ |
| `/api/v1/vehicle/{id}` | PUT | Update vehicle info | ✅ |
| `/api/v1/vehicle` | DELETE | Delete vehicle | ✅ |
| `/api/v1/vehicle/status` | POST | Update IN/OUT status | ✅ |
| `/api/v1/vehicle/import` | POST | Import from Excel | ✅ |

#### Create Vehicle Request

```json
POST /api/v1/vehicle
Authorization: Bearer <token>
Content-Type: application/json

{
  "owner": "John Doe",
  "phone": "0912345678",
  "plateNumber": "29A-12345",
  "licenseNumber": "123456789"
}
```

#### Vehicle Response

```json
{
  "message": "SUCCESS",
  "data": {
    "id": 1,
    "owner": "John Doe",
    "phone": "0912345678",
    "plateNumber": "29A-12345",
    "licenseNumber": "123456789",
    "active": true,
    "createdAt": "2026-01-11T10:30:00"
  }
}
```

#### Update Status Request

```json
POST /api/v1/vehicle/status?vehicleId=1&employeeId=2&status=IN
Authorization: Bearer <token>
```

**Status values**: `IN` or `OUT`

---

### 👥 Employee Management

| Endpoint | Method | Description | Auth Required |
|----------|--------|-------------|---------------|
| `/api/v1/employees` | GET | Get employee list | ✅ Admin |
| `/api/v1/employees/{id}` | GET | Get employee details | ✅ Admin |
| `/api/v1/employees` | POST | Add employee | ✅ Admin |
| `/api/v1/employees/{id}` | PUT | Update employee | ✅ Admin |
| `/api/v1/employees/{id}` | DELETE | Delete employee | ✅ Admin |

---

## 🗄️ Database Schema

### Table: `vehicles`

| Column | Type | Description |
|--------|------|-------------|
| id | BIGSERIAL | Primary key |
| owner | VARCHAR(50) | Owner name |
| phone | VARCHAR(50) | Phone number |
| plate_number | VARCHAR(50) | License plate (unique) |
| license_number | VARCHAR(50) | License number (unique) |
| active | BOOLEAN | Active status |
| created_at | TIMESTAMP | Creation time |

### Table: `employees`

| Column | Type | Description |
|--------|------|-------------|
| id | BIGSERIAL | Primary key |
| username | VARCHAR(50) | Username (unique) |
| password | VARCHAR(255) | Password (BCrypt) |
| name | VARCHAR(100) | Full name |
| email | VARCHAR(100) | Email |
| phone | VARCHAR(20) | Phone number |
| role | VARCHAR(20) | Role (ADMIN, USER) |
| active | BOOLEAN | Status |
| created_at | TIMESTAMP | Creation time |

### Table: `entry_exit_records`

| Column | Type | Description |
|--------|------|-------------|
| id | BIGSERIAL | Primary key |
| vehicle_id | BIGINT | Foreign key → vehicles |
| employee_id | BIGINT | Foreign key → employees |
| type | VARCHAR(10) | Type (IN, OUT) |
| timestamp | TIMESTAMP | Entry/Exit time |
| notes | TEXT | Notes |

### Table: `refresh_tokens`

| Column | Type | Description |
|--------|------|-------------|
| id | BIGSERIAL | Primary key |
| token | VARCHAR(255) | Refresh token (unique) |
| employee_id | BIGINT | Foreign key → employees |
| expiry_date | TIMESTAMP | Expiration time |

---

## 🔒 Security Configuration

### JWT Authentication

- **Access Token**: Expires after 10 hours (36,000,000 ms)
- **Refresh Token**: Expires after 5 days (432,000,000 ms)
- **Algorithm**: HS256
- **Secret Key**: Configured in `application.yaml`

### Password Encryption

- **Algorithm**: BCrypt
- **Strength**: 10 rounds

### CORS Configuration

```java
@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                    .allowedOrigins("*")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
            }
        };
    }
}
```

### Protected Endpoints

- All `/api/v1/**` endpoints require JWT token (except `/login`, `/register`)
- Header format: `Authorization: Bearer <token>`

---

## 🛠️ Configuration

### Application Properties

File: [`src/main/resources/application.yaml`](src/main/resources/application.yaml)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/vehical_management
    username: postgres
    password: 2001
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update                 # auto, create, create-drop, validate, update
    show-sql: true                     # Show SQL queries
    properties:
      hibernate:
        format_sql: true               # Format SQL output
        dialect: org.hibernate.dialect.PostgreSQLDialect

server:
  port: 8084                           # Server port

application:
  jwtSecret: your-secret-key-here      # JWT secret key
  jwtRefreshTokenSecret: your-refresh-secret-key
  jwtExpiration: 36000000              # 10 hours in milliseconds
  jwtExpirationRefreshToken: 432000000 # 5 days in milliseconds
```

### Environment Variables (Recommended for Production)

```bash
export JWT_SECRET_DEV=your-super-secret-key
export JWT_REFRESHER_SECRET_DEV=your-refresh-secret-key
export DB_URL=jdbc:postgresql://localhost:5432/vehical_management
export DB_USERNAME=postgres
export DB_PASSWORD=your-password
```

---

## 📦 Main Dependencies

| Dependency | Version | Purpose |
|-----------|---------|---------|
| Spring Boot Starter Web | 4.0.1 | RESTful API |
| Spring Boot Starter Data JPA | 4.0.1 | ORM with Hibernate |
| Spring Boot Starter Security | 4.0.1 | Authentication & Authorization |
| PostgreSQL Driver | Latest | Database driver |
| Lombok | Latest | Reduce boilerplate code |
| JJWT (Java JWT) | 0.11.5 | JWT token handling |
| SpringDoc OpenAPI | 2.x | Swagger documentation |
| Jakarta Validation | 4.0 | Bean validation |
| Apache POI | Latest | Excel file processing |

---

## 🧪 Testing

### Run Unit Tests

```bash
./mvnw test
```

### Test API with cURL

```bash
# Login
curl -X POST http://localhost:8084/api/v1/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password123"}'

# Get vehicles (with token)
curl -X GET http://localhost:8084/api/v1/vehicle \
  -H "Authorization: Bearer <your-token>"

# Create vehicle
curl -X POST http://localhost:8084/api/v1/vehicle \
  -H "Authorization: Bearer <your-token>" \
  -H "Content-Type: application/json" \
  -d '{
    "owner": "John Doe",
    "phone": "0912345678",
    "plateNumber": "29A-12345",
    "licenseNumber": "123456789"
  }'
```

### Test with Postman

Import collection from Swagger: `http://localhost:8084/v3/api-docs`

---

## 🐛 Troubleshooting

### 1. Cannot connect to PostgreSQL

```bash
# Check PostgreSQL is running
sudo systemctl status postgresql

# Start PostgreSQL (Linux)
sudo systemctl start postgresql

# Windows
# Services → PostgreSQL → Start
```

### 2. Port 8084 already in use

Change port in `application.yaml`:

```yaml
server:
  port: 8085  # Change to different port
```

### 3. "Table doesn't exist" error

Ensure `ddl-auto` is set:

```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update  # Auto create/update tables
```

### 4. JWT Token Invalid

- Check `jwtSecret` in `application.yaml`
- Ensure token not expired
- Verify format: `Authorization: Bearer <token>`

### 5. CORS Error from Frontend

Check CORS configuration in `CorsConfig.java`:

```java
.allowedOrigins("http://localhost:3000", "http://192.168.0.133:8081")
```

---

## 🚀 Deployment

### Build JAR file

```bash
./mvnw clean package -DskipTests

# JAR file will be at: target/vehical_management-0.0.1-SNAPSHOT.jar
```

### Run Production

```bash
java -jar target/vehical_management-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --spring.datasource.url=jdbc:postgresql://prod-server:5432/vehical_management \
  --spring.datasource.username=prod_user \
  --spring.datasource.password=prod_password
```

### Docker Deployment (Optional)

```dockerfile
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/vehical_management-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
```

```bash
docker build -t vehicle-management-api .
docker run -p 8084:8084 vehicle-management-api
```

---

## 📝 Development Guide

### Adding New Entity

1. Create entity in `entities/`
2. Create repository in `repositories/`
3. Create service interface and implementation
4. Create controller with endpoints
5. Add DTOs in `dto/`

### Adding New API Endpoint

```java
@RestController
@RequestMapping("/api/v1")
public class MyController {
    
    @GetMapping("/my-endpoint")
    @Operation(summary = "Description")
    public ResponseEntity<?> myEndpoint() {
        return ResponseEntity.ok(
            new BaseResponse<>(CommonMessage.SUCCESS, data)
        );
    }
}
```

### Custom Exception Handling

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ApplicationRuntimeException.class)
    public ResponseEntity<?> handleApplicationException(ApplicationRuntimeException ex) {
        return ResponseEntity.badRequest()
            .body(new BaseResponse<>(ex.getMessage()));
    }
}
```

---

## 🤝 Contributing

1. Fork repository
2. Create branch: `git checkout -b feature/FeatureName`
3. Commit: `git commit -m 'Add feature X'`
4. Push: `git push origin feature/FeatureName`
5. Open Pull Request

---

## 📄 License

This project is distributed under the MIT License.

---

## 👥 Authors

- **Name**: Li Xin
- **Email**: contact@example.com
- **GitHub**: [lixin](https://github.com/lixin)

---

## 🙏 Acknowledgments

- [Spring Boot](https://spring.io/projects/spring-boot)
- [PostgreSQL](https://www.postgresql.org/)
- [SpringDoc OpenAPI](https://springdoc.org/)
- [Lombok](https://projectlombok.org/)

---

**Happy Coding! 🎉**
