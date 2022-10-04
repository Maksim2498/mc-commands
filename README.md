# Commands

![Logo](/images/logo.png)

## Index

- [Index](#index);
- [About](#about);
- [Installation](#installation);
- [Building](#building);
- [Examples](#examples);
    - [Teleportation Plugin](#teleportation-plugin);
    - [Real Plugin](#real-plugin);
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
    <version>4.1.2</version>
</dependency>
```

Done.

## Building

Just execute the following Maven command in your terminal:

```bash
mvn install
```

## Examples

### Teleportation Plugin

This is a simple player-to-player teleportation plugin example:

```java
public class Plugin extends JavaPlugin {
    @Override
    public void onEnable() {
        final var commandHandler = new DefaultMultiCommandHandler(this);

        commandHandler.addCommandRoute("mytp %p %p", call -> {
            final Player from = call.getPlaceholdedAt(0);
            final Player to   = call.getPlaceholdedAt(1);
            from.teleport(to);
        });
    }
}
```

`%p` is a _placeholder_ which stands for an online player.

With this little of code you get:
- autocompletions;
- error handling;
- working `mytp` command.

For real projects you can config all messages and add your own placeholders.
Also you can specify required _priority_ and command _sender classes_ for _route handlers_.

### Real Plugin:

For a complete real-world example you can see [this plugin](https://github.com/Maksim2498/mc-cpspeed).
Full power of the plugin is shown in the `setupCommands` method of the [main class](https://github.com/Maksim2498/mc-cpspeed/blob/main/src/main/java/space/moontalk/mc/cpspeed/CPSpeed.java);

## Documentation

Will be added soon... (maybe)
