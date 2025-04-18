# FunixAPI
API used for Funix Projects

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
    <version>1.4.1</version>
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
