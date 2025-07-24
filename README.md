# Hotel management app

A small application that manages users authentication with JWT, and allows users to book hotel
rooms.

## Setup

## Database

MySQL database in a `Docker` container. Requires `Docker Desktop` running, then dependency
`spring-boot-docker-desktop` allows the MySQL container to run automatically when starting the app.

### Messaging

Using `Gmail SMTP` for emailing purposes (sign up validation, reset password, etc.). In
`application.properties`, adding the line `spring.profiles.active=dev` signals Spring Boot we're
using an `application-dev.properties` file for local set up that won't be added to the `Github`
repository (need to add `application-dev.properties` to the `.gitignore` manually, using it a
similar way to `.env` files, here). In this file, the username (Gmail email address) and password (
Gmail app password for this project) are added locally:

```properties
spring.mail.username=<my-gmail-username>
spring.mail.password=<my-gmail-app-password>
```

## TODO: Improvements & best practices

### (rework) User (entity) vs CustomUserDetails (impl. UserDetails)

| Context                                        | Use `CustomUserDetails`                  | Use `User` entity                                    |
|------------------------------------------------|------------------------------------------|------------------------------------------------------|
| **Spring Security Authentication**             | ✅ Yes (required by `UserDetailsService`) | 🚫 No                                                |
| **`UserDetailsService.loadUserByUsername()`**  | ✅ Yes (return `CustomUserDetails`)       | 🚫 No                                                |
| **Token generation (JWT)**                     | Can use `CustomUserDetails`, but…        | ✅ Yes (recommended for payload, e.g. subject, roles) |
| **Registration / Business Logic / DB queries** | 🚫 No                                    | ✅ Yes                                                |
| **Token decoding and verification**            | 🔄 Usually map decoded token to a `User` | ✅ Yes                                                |
