# FunixAPI
API used for Funix Projects

![Maven Central](https://img.shields.io/maven-central/v/fr.funixgaming.api/funixgaming-api.svg)
![Tests build](https://github.com/FunixProductions/FunixAPI/actions/workflows/main.yml/badge.svg?branch=master)

[![Security Rating](https://sonarqube.funixgaming.fr/api/project_badges/measure?project=FunixProductions_FunixAPI_AYNRa2sBsXlKxBk9mU9x&metric=security_rating&token=sqb_1b07e02bb3b9833965cc83873e87fb91c451b858)](https://sonarqube.funixgaming.fr/dashboard?id=FunixProductions_FunixAPI_AYNRa2sBsXlKxBk9mU9x)
[![Reliability Rating](https://sonarqube.funixgaming.fr/api/project_badges/measure?project=FunixProductions_FunixAPI_AYNRa2sBsXlKxBk9mU9x&metric=reliability_rating&token=sqb_1b07e02bb3b9833965cc83873e87fb91c451b858)](https://sonarqube.funixgaming.fr/dashboard?id=FunixProductions_FunixAPI_AYNRa2sBsXlKxBk9mU9x)
[![Quality Gate Status](https://sonarqube.funixgaming.fr/api/project_badges/measure?project=FunixProductions_FunixAPI_AYNRa2sBsXlKxBk9mU9x&metric=alert_status&token=sqb_1b07e02bb3b9833965cc83873e87fb91c451b858)](https://sonarqube.funixgaming.fr/dashboard?id=FunixProductions_FunixAPI_AYNRa2sBsXlKxBk9mU9x)
[![Maintainability Rating](https://sonarqube.funixgaming.fr/api/project_badges/measure?project=FunixProductions_FunixAPI_AYNRa2sBsXlKxBk9mU9x&metric=sqale_rating&token=sqb_1b07e02bb3b9833965cc83873e87fb91c451b858)](https://sonarqube.funixgaming.fr/dashboard?id=FunixProductions_FunixAPI_AYNRa2sBsXlKxBk9mU9x)
[![Lines of Code](https://sonarqube.funixgaming.fr/api/project_badges/measure?project=FunixProductions_FunixAPI_AYNRa2sBsXlKxBk9mU9x&metric=ncloc&token=sqb_1b07e02bb3b9833965cc83873e87fb91c451b858)](https://sonarqube.funixgaming.fr/dashboard?id=FunixProductions_FunixAPI_AYNRa2sBsXlKxBk9mU9x)
[![Duplicated Lines (%)](https://sonarqube.funixgaming.fr/api/project_badges/measure?project=FunixProductions_FunixAPI_AYNRa2sBsXlKxBk9mU9x&metric=duplicated_lines_density&token=sqb_1b07e02bb3b9833965cc83873e87fb91c451b858)](https://sonarqube.funixgaming.fr/dashboard?id=FunixProductions_FunixAPI_AYNRa2sBsXlKxBk9mU9x)
[![Coverage](https://sonarqube.funixgaming.fr/api/project_badges/measure?project=FunixProductions_FunixAPI_AYNRa2sBsXlKxBk9mU9x&metric=coverage&token=sqb_1b07e02bb3b9833965cc83873e87fb91c451b858)](https://sonarqube.funixgaming.fr/dashboard?id=FunixProductions_FunixAPI_AYNRa2sBsXlKxBk9mU9x)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring app](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=for-the-badge&logo=Spring-Security&logoColor=white)
![Database](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)

[![Twitch](https://img.shields.io/badge/Twitch-9146FF?style=for-the-badge&logo=twitch&logoColor=white)](https://twitch.tv/funixgaming)
[![YouTube](https://img.shields.io/badge/YouTube-FF0000?style=for-the-badge&logo=youtube&logoColor=white)](https://youtube.com/c/funixgaming)
[![Twitter](https://img.shields.io/badge/Twitter-1DA1F2?style=for-the-badge&logo=twitter&logoColor=white)](https://twitter.com/funixgaming)

Migrations scripts are located in modules/funixbot/service/src/main/resources/db/migration

### Maven repo

Lien du repository Maven : [FunixGaming - Api package](https://mvn.funixproductions.com/#/releases/fr/funixgaming/api)

```xml
<repository>
  <id>funixproductions-repository-releases</id>
  <name>FunixProductions Repository</name>
  <url>https://mvn.funixproductions.com/releases</url>
</repository>

<parent>
    <groupId>fr.funixgaming.api</groupId>
    <artifactId>funixgaming-api</artifactId>
    <version>1.3.1.2</version>
</parent>
```

### Annotations requises pour le lancement spring
````java
@EnableAsync
@EnableScheduling
@EnableFeignClients(basePackages = "fr.funixgaming.api")
@SpringBootApplication(scanBasePackages = "fr.funixgaming.api")
@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class FunixApiApp {
    public static void main(final String[] args) {
        SpringApplication.run(FunixApiApp.class, args);
    }
}

````
