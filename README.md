# Commands

![Logo](/images/logo.png)

## Index

- [Index](#index);
- [About](#about);
- [Installation](#installation);
- [Building](#building);
- [Examples](#examples);
- [Documentation](#documentation).

## About

This is a java library which provides a better commands API for Bukkit plugins.

## Installation

First, add MoonTalk repository to your pom.xml:

```xml
<repository>
    <id>moontalk</id>
    <url>httsp://repo.moontalk.space/repository/maven-releases/</url>
</repository>
```

Second, add library as dependency:

```xml
<dependency>
    <groupId>space.moontalk.mc</groupId>
    <artifactId>commands</artifactId>
    <version>4.0.0</version>
</dependency>
```

Done.

## Building

Just execute the following Maven command in your terminal:

```bash
mvn install
```

## Examples

This is a simple player-to-player teleportation plugin example:

```java
public class Plugin extends JavaPlugin {
    @Override
    public void onEnable() {
        try {
            final var commandHandler = new DefaultMultiCommandHandler(this);

            commandHandler.addCommandRoute("mytp %p %p", call -> {
                final Player from = call.getPlaceholdedAt(0);
                final Player to   = call.getPlaceholdedAt(1);
                from.teleport(to);
            });
        } catch (InvalidRouteException exception) {
            final var logger  = getLogger();
            final var message = exception.getMessage();
            logger.info(message);
        }
    }
}
```

`%p` is a placeholders which stands for an online player.

With this little of code you get:
- autocompletions;
- error handling;
- working command.

For a complete real example you can see [this plugin](https://github.com/Maksim2498/mc-cpspeed).

## Documentation

Will be added soon... (maybe)
