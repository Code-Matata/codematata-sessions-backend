# codematata-sessions-backend
A Spring Boot backend application for codematata sessions created using Kotlin

## Git Clone
Clone the github repository

```bash
git clone https://github.com/Code-Matata/codematata-sessions-backend.git
```
## Requirements
You need to have:
  1. Java 8 and above installed, though Java 11 is used in this project.
  2. An IDE like Intellij, VScode or any of your choice.
  3. Gradle 7.0.2

## OAuth2 Configuration
You will need to create two OAuth Apps:

  1. For Google - You can learn how to do it here: https://support.google.com/googleapi/answer/6158849?hl=en
  2. For Github - You can learn how to do it here: https://docs.github.com/en/developers/apps/building-oauth-apps/creating-an-oauth-app
 
After that in a property file named (application-local.yml), you can add the Client ID and Client Secret as follows:

application-local.yml

```bash
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            clientId: {YOUR_CLIENT_ID}
            clientSecret: {YOUR_CLIENT_SECRET}
            redirectUri: "{baseUrl}/oauth2/callback/{registrationId}"
            scope:
              - email
              - profile
          github:
            clientId: {YOUR_CLIENT_ID}
            clientSecret: {YOUR_CLIENT_SECRET}
            redirectUri: "{baseUrl}/oauth2/callback/{registrationId}"
            scope:
              - user:email
              - read:user

```
## Build The Project
```bash
gradle build
```

## Run The Project
In Linux and MacOS:

```bash
./gradlew bootRun
```
In Windows:
```bash
./gradlew.bat bootRun 
```
## Dependencies
1. Spring Boot Starter Web
2. Spring Data Jpa
3. GSon
4. Jjwt
5. PostgreSql Driver
6. H2 Database for testing
7. Spring Boot Starter Security
8. Ninja Squad Spring Mockk
9. Spring Boot Starter Security Test
10. Mockito Kotlin
