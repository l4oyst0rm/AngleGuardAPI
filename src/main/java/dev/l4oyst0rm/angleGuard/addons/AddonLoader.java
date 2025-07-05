package dev.l4oyst0rm.angleGuard.addons;

import dev.l4oyst0rm.angleGuard.api.AngleGuardAPI;
import org.bukkit.Bukkit;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class AddonLoader {
    private final File addonsFolder;
    private final Map<String, Addon> loadedAddons = new HashMap<>();
    private final AngleGuardAPI api;

    public AddonLoader(File addonsFolder, AngleGuardAPI api) {
        this.addonsFolder = addonsFolder;
        this.api = api;
    }

    public void loadAddons() {
        if (!addonsFolder.exists()) addonsFolder.mkdirs();

        for (File file : addonsFolder.listFiles((dir, name) -> name.endsWith(".jar"))) {
            try {
                URL jarUrl = file.toURI().toURL();
                URLClassLoader classLoader = new URLClassLoader(new URL[]{jarUrl}, getClass().getClassLoader());
                Class<?> addonClass = Class.forName("com.example.addon.Main", true, classLoader);
                Addon addon = (Addon) addonClass.getDeclaredConstructor().newInstance();
                addon.onEnable(api);
                loadedAddons.put(file.getName(), addon);
                Bukkit.getLogger().info("Loaded addon: " + file.getName());
            } catch (Exception e) {
                Bukkit.getLogger().severe("Failed to load addon: " + file.getName());
                e.printStackTrace();
            }
        }
    }

    public void unloadAddons() {
        for (Addon addon : loadedAddons.values()) {
            addon.onDisable();
        }
        loadedAddons.clear();
    }
}