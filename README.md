# FunixAPI
API used for Funix Projects


### Importer le projet
- Ajouter comme parent le projet funix-api (requis)
```xml
<parent>
    <artifactId>funix-api</artifactId>
    <groupId>fr.funixgaming.api</groupId>
    <version>1.0.0</version>
</parent>
```

- Si vous voulez utiliser le core
```xml
<dependency>
    <groupId>fr.funixgaming.api.core</groupId>
    <artifactId>funix-api-core</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

- Si vous voulez utiliser l'api funix
```xml
<dependency>
    <groupId>fr.funixgaming.api.client</groupId>
    <artifactId>funix-api-client</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```

- Si vous voulez utiliser le serveur
```xml
<dependency>
    <groupId>fr.funixgaming.api.server</groupId>
    <artifactId>funix-api-server</artifactId>
    <version>1.0.0</version>
    <scope>compile</scope>
</dependency>
```