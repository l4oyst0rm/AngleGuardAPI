package dev.l4oyst0rm.angleGuard.check;

import com.tcoded.folialib.FoliaLib;
import dev.l4oyst0rm.angleGuard.AngleGuard;
import dev.l4oyst0rm.angleGuard.api.check.CheckAPI;
import dev.l4oyst0rm.angleGuard.manager.AlertsManager;
import dev.l4oyst0rm.angleGuard.manager.PunishmentManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Check implements CheckAPI, Listener {
    protected final String name;
    protected final String configPath;
    protected final AngleGuard plugin;
    protected final AlertsManager alertsManager;
    protected final PunishmentManager punishmentManager;
    protected final int maxViolationLevel;
    protected final int maxStored;
    protected final boolean punishable;
    protected final List<String> punishmentCommands;
    protected final Map<UUID, Integer> violationLevels = new HashMap<>();

    public Check(String name, String configPath, AlertsManager alertsManager, PunishmentManager punishmentManager) {
        this.name = name;
        this.configPath = configPath;
        this.plugin = AngleGuard.getInstance();
        this.alertsManager = alertsManager;
        this.punishmentManager = punishmentManager;
        this.maxViolationLevel = plugin.getConfig().getInt(configPath + ".max-violation", 10);
        this.punishable = plugin.getConfig().getBoolean(configPath + ".punishable", false);
        this.punishmentCommands = plugin.getConfig().getStringList(configPath + ".punishment-commands");
        this.maxStored = plugin.getConfig().getInt(configPath + ".max-stored", 10);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        runResetViolation();
    }

    protected void incrementViolationLevel(Player player, Component detail) {
        UUID playerId = player.getUniqueId();
        int newVl = violationLevels.merge(playerId, 1, Integer::sum);
        alertsManager.sendAlert(player, name, newVl, maxViolationLevel, detail);

        if (punishable && newVl >= maxViolationLevel) {
            punishmentManager.executeConfigPunishments(player, name, punishmentCommands);
            violationLevels.remove(playerId);
        }
    }

    private void runResetViolation() {

        FoliaLib foliaLib = plugin.getFoliaLib();

        if (foliaLib.isFolia()) {
            foliaLib.getScheduler().runTimer(() -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    violationLevels.remove(player.getUniqueId());
                }
            }, 0L, plugin.getConfig().getInt("general.reset-violation", 10) * 60 * 20L);
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        violationLevels.remove(player.getUniqueId());
                    }
                }
            }.runTaskTimer(plugin, 0L, 10 * 60 * 20L);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerJoin(PlayerJoinEvent event) {
        violationLevels.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    private void onPlayerQuit(PlayerQuitEvent event) {
        violationLevels.remove(event.getPlayer().getUniqueId());
    }

    public boolean isGeyser(Player player) {
        return player.getName().startsWith(plugin.getConfig().getString("general.geyser-prefix", "."));
    }

    public int getViolationLevel(Player player) {
        return violationLevels.getOrDefault(player.getUniqueId(), 0);
    }

    public String getName() {
        return name;
    }
}