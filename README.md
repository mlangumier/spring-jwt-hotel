# Hotel management app

**Description**: Basis of an app that allows for users to book hotel rooms.
**Features**: Authentication and sessions with JWT & Spring Security.

## Setup

### Dependencies

- Spring Framework Start:
    - Spring Web
    - Actuator
    - DevTools
    - Spring Test
    - Spring Docker Compose -> For MySQL server container
- Coding:
    - Lombok -> 
    - MapStruct
- Database:
    - Spring Data JPA
    - MySQL, H2 database (tests)
    - Spring Validation
- Security & Authentication:
    - Spring Security
    - Auth0, Json Web Token (jjwt-api, jjwt-impl, jjwt-jackson)
- Emails:
    - Spring Mail
    - Thymeleaf + Thymeleaf Extra (security)

## Database

MySQL database in a `Docker` container. Requires `Docker Desktop` running, then dependency
`spring-boot-docker-desktop` allows the MySQL container to run automatically when starting the app.

### JWT

[Online secret key generator (in 256 bits)](https://jwtsecrets.com/#generator)

In `src/main/resources/application-dev.properties`:

```properties
app.jwt.secret.key=<my-secret-key>
```

### Messaging

Using `Gmail SMTP` for emailing purposes (sign up validation, reset password, etc.). In
`application.properties`, adding the line `spring.profiles.active=dev` signals Spring Boot we're
using an `application-dev.properties` file for local set up that won't be added to the `Github`
repository (need to add `application-dev.properties` to the `.gitignore` manually, using it a
similar way to `.env` files, here). In this file, the username (Gmail email address) and password (
Gmail app password for this project) are added locally:

In `src/main/resources/application-dev.properties`:

```properties
spring.mail.username=<my-gmail-username>
spring.mail.password=<my-gmail-app-password>
```

## TODO: Improvements & best practices

- [x] Rework full JWT implement for access & refresh token (new project then transfer here)
- [ ] Add extensive JavaDoc & add logs to errors (with @Log4j2)
- [ ] Set up role-based routes & authorizations
- [ ] Refactor files to follow SOLID principles as much as possible
- [ ] Continue basic CRUD routes & security
- [ ] Set up CSRF protection for sessions
- [ ] Set up HTTPS
- [ ] Start API documentation
    - [ ] Code set up
    - [ ] Web page for preview & tests
