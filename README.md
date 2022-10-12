# FunixAPI
API used for Funix Projects

![Maven Central](https://img.shields.io/maven-central/v/fr.funixgaming.api/funix-api.svg)
![Tests build](https://github.com/FunixProductions/FunixAPI/actions/workflows/main.yml/badge.svg?branch=master)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring app](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white)
![Database](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white)

[![Twitch](https://img.shields.io/badge/Twitch-9146FF?style=for-the-badge&logo=twitch&logoColor=white)](https://twitch.tv/funixgaming)
[![YouTube](https://img.shields.io/badge/YouTube-FF0000?style=for-the-badge&logo=youtube&logoColor=white)](https://youtube.com/c/funixgaming)
[![Twitter](https://img.shields.io/badge/Twitter-1DA1F2?style=for-the-badge&logo=twitter&logoColor=white)](https://twitter.com/funixgaming)

### Importer le projet
- Ajouter comme parent le projet funix-api
```xml
<parent>
    <artifactId>funix-api</artifactId>
    <groupId>fr.funixgaming.api</groupId>
    <version>(version)</version>
</parent>
```

- Si vous voulez utiliser le core
```xml
<dependency>
    <groupId>fr.funixgaming.api.core</groupId>
    <artifactId>funix-api-core</artifactId>
    <version>(version)</version>
    <scope>compile</scope>
</dependency>
```

- Si vous voulez utiliser l'api funix
```xml
<dependency>
    <groupId>fr.funixgaming.api.client</groupId>
    <artifactId>funix-api-client</artifactId>
    <version>(version)</version>
    <scope>compile</scope>
</dependency>
```

- Si vous voulez utiliser le serveur
```xml
<dependency>
    <groupId>fr.funixgaming.api.server</groupId>
    <artifactId>funix-api-server</artifactId>
    <version>(version)</version>
    <scope>compile</scope>
</dependency>
```

### Configuration des app spring

Vous devez spécifier les variables d'env ou alors les changer avec une surcharge de properties.

- App api application.properties
````properties
funix.api.app-domain-url=https://api.funixgaming.fr

funix.api.user-api-username=${API_USERNAME}
funix.api.user-api-password=${API_PASSWORD}
````

- App core application.properties
````properties
#Google captcha settings (console captcha google: https://www.google.com/recaptcha/admin)
google.recaptcha.key.site=${GOOGLE_RECAPTCHA_SITE}
google.recaptcha.key.secret=${GOOGLE_RECAPTCHA_SECRET}
google.recaptcha.key.threshold=0.7
#You can disable google captcha with this, cool in unit test env or local development
#If you set it to true you only need to set this google.recaptcha line
google.recaptcha.key.disabled=false

#For securing some actions by ip on api (example: 127.0.0.1,10.2.4.5)
config.api.ip-whitelist=${API_WHITELIST}

#Mail config
spring.mail.host=${MAIL_HOST}
spring.mail.port=${MAIL_PORT}
spring.mail.username=${MAIL_USER}
spring.mail.password=${MAIL_USER_PASSWORD}
#Mail advanced config (here is default values)
spring.mail.tls=true
spring.mail.protocol=smtp
spring.mail.ssl=true
spring.mail.debug=false
sping.mail.auth=true
````

- App service application.properties
````properties
spring.datasource.driver-class-name=org.mariadb.jdbc.Driver
spring.datasource.url=jdbc:mariadb://${DB_HOSTNAME}:${DB_PORT}/${DB_DATABASE}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}

server.port=${APP_PORT}

funix.api.email=${API_EMAIL}
funix.api.password-numbers=${PASSWORD_NUMBERS}
funix.api.password-specials=${PASSWORD_SPECIALS}
funix.api.password-caps=${PASSWORD_CAPS}
funix.api.password-min=${PASSWORD_MIN}

paypal.url.auth=https://api-m.sandbox.paypal.com
paypal.client-id=${PAYPAL_CLIENT_ID}
paypal.client-secret=${PAYPAL_CLIENT_SECRET}
````

### Annotations requises pour le lancement spring
````java
@EnableAsync
@EnableScheduling
@EnableFeignClients(basePackages = "fr.funixgaming.api")
@SpringBootApplication(scanBasePackages = "fr.funixgaming.api")
public class FunixApiApp {
    public static void main(final String[] args) {
        SpringApplication.run(FunixApiApp.class, args);
    }
}
````
