
# 🔐 AngleGuardAPI – Addon Developer Guide

[![](https://jitpack.io/v/l4oyst0rm/AngleGuardAPI.svg)](https://jitpack.io/#l4oyst0rm/AngleGuardAPI)

Welcome to the **AngleGuard AntiCheat API**, the official way to build custom **detection checks** and **punishments** as addons for [AngleGuard](https://github.com/l4oyst0rm/AngleGuard).  
This document shows you how to get started — **step by step** — with your own `addons.jar`.

---

## 🧠 What Is an Addon?

An addon is a separate plugin (JAR) placed in:

```
/plugins/AngleGuard/addons/
```

Once loaded, it gives developers full control to:

- ✅ Create **custom Check detections**
- ✅ Add new **Punishment types**
- ✅ Extend the anti-cheat features modularly

---

## 📦 Setup – Use AngleGuardAPI in Your Project

### ▶ Maven

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.l4oyst0rm</groupId>
        <artifactId>AngleGuardAPI</artifactId>
        <version>1.0.0-BETA</version>
    </dependency>
</dependencies>
```

### ▶ Gradle

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.l4oyst0rm:AngleGuardAPI:1.0.0-BETA'
}
```

---

## 🛠 Step-by-Step: Build Your Own Addon

### 1️⃣ Create `plugin.yml`

```yaml
name: ExampleAddon
main: dev.myaddon.Main
version: 1.0
api-version: 1.20
depend: [AngleGuard]
```

---

### 2️⃣ Implement `Addon` Interface

```java
package dev.myaddon;

import dev.l4oyst0rm.AngleGuardAPI.api.AngleGuardAPI;
import dev.l4oyst0rm.angleGuard.addons.Addon;
import dev.myaddon.check.ExampleCheck;
import dev.myaddon.punishment.AddonKick;

public class Main implements Addon {
    @Override
    public void onEnable(AngleGuardAPI api) {
        api.addPunishmentType("addonskick", new AddonKick());
        api.registerCheck(new ExampleCheck(api.createCheck(
            "ExampleCheck", "checks.example."
        )));
    }

    @Override
    public void onDisable() {
        // Optional cleanup
    }
}
```

---

### 3️⃣ Create a Custom Punishment

```java
package dev.myaddon.punishment;

import dev.l4oyst0rm.AngleGuardAPI.api.handlers.PunishmentHandler;
import org.bukkit.entity.Player;

public class AddonKick implements PunishmentHandler {
    @Override
    public void execute(Player player, String typeId) {
        player.kickPlayer("Punished by custom addon.");
    }
}
```

Use in config:

```yaml
punishment-commands:
  - "addonskick %player%"
```

---

### 4️⃣ Create a Custom Check

```java
package dev.myaddon.check;

import dev.l4oyst0rm.angleGuard.check.Check;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

public class ExampleCheck extends Check {
    public ExampleCheck(Check base) {
        super(base.getName(), "checks.example.", null, null);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getPlayer().getLocation().getY() > 300) {
            incrementViolationLevel(event.getPlayer(), Component.text("Y too high"));
        }
    }
}
```

---

## 📤 Build the Addon

```bash
mvn clean package
```

The compiled `addons.jar` will be in `target/`. Place it in:

```
/plugins/AngleGuard/addons/
```

Then **restart** the server. Logs will show:

```
Loaded addon: ExampleAddon.jar
Registered punishment type: addonskick
```

---

## 📘 API Overview

| Method | Description |
|--------|-------------|
| `createCheck(name, configPath)` | Creates a base Check you can extend |
| `registerCheck(check)` | Registers your custom check |
| `addPunishmentType(id, handler)` | Adds a new punishment type (e.g. `addonskick`) |
| `executePunishment(player, id)` | Manually trigger a punishment |
| `getViolation(player, checkName)` | Get current VL of player for a check |

---

## 🧪 Full Example Repo

See: [ExampleAddon](https://github.com/l4oyst0rm/AngleGuardAddonExample)  
(Coming soon)

---

## ❤️ Credits

- AngleGuard & API by [l4oyst0rm](https://github.com/l4oyst0rm)
- Inspired by modular plugin architecture

---

## 📜 License

This API and addons are **open-source under MIT License**.  
Feel free to build, modify, and share your own checks and ideas!
