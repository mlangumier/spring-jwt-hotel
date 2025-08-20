# Hotel management app

**Description**: Demo app that allows users to book hotel rooms.
**Features**: Authentication with JWT & Spring Security and cookie-based sessions (login,
authenticated routes, logout).

## Dependencies

- **Spring Framework**:
    - Spring Web
    - Actuator
    - DevTools
    - Spring Test
    - Spring Docker Compose -> For MySQL server container (see next items in the list)
- **Database**:
    - Spring Data JPA
    - MySQL, H2 database (tests)
    - Spring Validation
- **Code**:
    - Lombok -> For a cleaner way to write getters, setters, constructors, and use the `Builder`
      method.
    - MapStruct -> Simpler entities-DTOs mapping (
      documentation [here](https://github.com/mapstruct/mapstruct/tree/main/documentation/src/main/asciidoc))
- **Security & Authentication**:
    - Spring Security
    - Auth0, Json Web Token (jjwt-api, jjwt-impl, jjwt-jackson)
- **Emails**:
    - Spring Mail
    - Thymeleaf + Thymeleaf Extras Spring-security ()

## Setup

In order to try out or work on this project, there are a few things to be aware of and some set up
to do.   
First of all, create the file `src/main/resources/application-dev.properties` next to the main
`application.properties` file. Because we've set it in `.gitignore`, this file exists only locally
and contains sensitive information, such as `GMAIL username` (for emails) and `JWT secret key` (for
JWT generation).

### Database

This project uses a MySQL database in a `Docker` container. It requires `Docker Desktop` to run, and
because of the `spring-boot-docker-desktop` dependency, the container with our database will run
automatically when we start the app.

If you prefer using a local MySQL server, feel free to remove the dependency from the `pom.xml` file
to prevent the container from starting automatically.

In `application.properties`, you can set the attribute `app.load-data` to `true` to allow the
`DataLoader` component to populate the database when it's created (first time you start the app) or
when you empty the database and need quick demo data. Reset the attribute to `false` once you're
done to prevent the component from checking the database everytime (not necessary, but better
regarding performances).

### JWT

To generate our JWTs, we'll need to get a secret key in `256 bits` from the following link:
[Online secret key generator (in 256 bits)](https://jwtsecrets.com/#generator)

And paste it with the following property in `application-dev.properties`:

```properties
app.jwt.secret.key=<my-secret-key>
```

### Messaging

Using `Gmail SMTP` for emailing purposes (sign up validation, reset password, etc.). In
`application-dev.properties`, add your GMAIL username (email address) and app password (
see [how to create an app password](https://support.google.com/mail/answer/185833)):

```properties
spring.mail.username=<my-gmail-username>
spring.mail.password=<my-gmail-app-password>
```

### Install & Start

Once you have followed the previous steps and are happy with your setup, use the following commands
to install the dependencies and then start the project:

Install dependencies:

```shell
  mvc clean install
```

Start project:

```shell
  mvn spring-boot:run
```