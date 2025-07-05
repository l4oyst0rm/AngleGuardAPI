package dev.l4oyst0rm.angleGuard.manager;

import dev.l4oyst0rm.angleGuard.AngleGuard;
import dev.l4oyst0rm.angleGuard.api.AngleGuardAPI;
import dev.l4oyst0rm.angleGuard.api.manager.PunishmentHandler;
import dev.l4oyst0rm.angleGuard.check.Check;
import dev.l4oyst0rm.angleGuard.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PunishmentManager implements AngleGuardAPI {
    private final AngleGuard plugin;
    private final AlertsManager alertsManager;
    private final Map<String, PunishmentHandler> punishmentHandlers = new HashMap<>();

    public PunishmentManager(AngleGuard plugin, AlertsManager alertsManager) {
        this.plugin = plugin;
        this.alertsManager = alertsManager;
    }

    public void addPunishmentType(String punishmentId, PunishmentHandler handler) {
        punishmentHandlers.put(punishmentId.toLowerCase(), handler);
        plugin.getLogger().info("Registered punishment type: " + punishmentId);
    }

    @Override
    public void executePunishment(Player player, String punishmentId) {
        PunishmentHandler handler = punishmentHandlers.get(punishmentId.toLowerCase());
        if (handler != null) {
            handler.execute(player, punishmentId);
            broadcastPunishment(player, punishmentId);
        } else {
            plugin.getLogger().warning("Unknown punishment type: " + punishmentId);
        }
    }

    public void executeConfigPunishments(Player player, String checkName, List<String> punishmentCommands) {
        String playerName = player.getName();
        for (String command : punishmentCommands) {
            String formattedCommand = ColorUtil.translate(command.replace("%player%", playerName));
            String[] parts = formattedCommand.split(" ", 2);
            String punishmentId = parts[0].toLowerCase();
            PunishmentHandler handler = punishmentHandlers.get(punishmentId);
            if (handler != null) {
                handler.execute(player, punishmentId);
            } else {
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), formattedCommand);
            }
        }
        broadcastPunishment(player, checkName);
    }

    private void broadcastPunishment(Player player, String checkName) {
        String playerName = player.getName();
        List<String> broadcastMessages = plugin.getConfig().getStringList("message.punishment.broadcast");
        if (broadcastMessages.isEmpty()) {
            broadcastMessages.add("&b&lAngleGuard &7» &b%player% &7was punished for failing &b%check% &7check");
        }

        for (String message : broadcastMessages) {
            String formattedMessage = ColorUtil.translate(message
                    .replace("%player%", playerName)
                    .replace("%check%", checkName));
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (onlinePlayer.hasPermission("angleguard.alerts")) {
                    onlinePlayer.sendMessage(formattedMessage);
                }
            }
        }

        if (alertsManager.isConsoleEnabled()) {
            String consoleMessage = plugin.getConfig().getString("alerts.console.punishment", "")
                    .replace("%player%", playerName)
                    .replace("%check%", checkName);
            plugin.getLogger().info(consoleMessage);
        }
    }

    @Override
    public Check createCheck(String name, String configPath, AlertsManager alertsManager, PunishmentManager punishmentManager) {
        return new Check(name, configPath, alertsManager, punishmentManager);
    }

    @Override
    public void registerCheck(Check check) {
        plugin.registerCheck(check);
    }

    @Override
    public int getViolation(Player player, String checkId) {
        Check check = plugin.getCheck(checkId);
        return check != null ? ((Check) check).getViolationLevel(player) : 0;
    }
}