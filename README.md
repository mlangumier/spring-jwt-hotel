# Hotel management app

A small application that manages users authentication with JWT, and allows users to book hotel
rooms.

## Setup

## Database

MySQL database in a `Docker` container. Requires `Docker Desktop` running, then dependency
`spring-boot-docker-desktop` allows the MySQL container to run automatically when starting the app.

### Messaging

Using `Gmail SMTP` for emailing purposes (sign up validation, reset password, etc.).
In `application.properties`, `spring.profiles.active=dev` signals Spring Boot we're using an
`application-dev.properties` file for local set up that won't be added to the `Github` repository (
need to add `application-dev.properties` to the `.gitignore` manually). In this file, the username (
Gmail email address) and password (Gmail app password for this project) are added locally. 